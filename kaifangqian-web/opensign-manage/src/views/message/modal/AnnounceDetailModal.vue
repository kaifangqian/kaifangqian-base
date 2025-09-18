<!--
  @description 开放签

  Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
  必须公开修改后的完整源码（包括衍生作品），详见协议全文。
-->

<template>
  <div>
    <BasicModal
      v-bind="$attrs"
      @register="registerModal"
      :title="getTitle"
      @ok="handleSubmit"
      :destroyOnClose="true"
      :style="{ height: '800px' }"
    >
      <Descriptions size="middle" :title="state.detailInfo.title" class="detail-header">
        <DescriptionsItem>
          <div>
            <div class="">
              <span class="detail-item"
                ><span class="item-title">发布人：</span>{{ state.detailInfo.sender }}</span
              >
              <span class="detail-item"
                ><span class="item-title">发布时间： </span> {{ state.detailInfo.createTime }}</span
              >
            </div>
          </div>
        </DescriptionsItem>
      </Descriptions>
      <span class="html-text-box" v-html="state.detailInfo.msgContent"></span>
      <div class="file-list">
        <p class="file-title">附件列表</p>
        <div v-for="(item, index) in fileList" :key="index">
          <span class="file-name">{{ item.realName }}</span>
          <a href="javascript:;" @click="handleDownload(item)">
            <!-- <a :href="baseDownLoadUrl+'/file/downloadFileStream/'+ item.id" :download="item.realName">  -->
            下载</a
          >
        </div>
      </div>
      <div class="detail-button">
        <template v-for="(item, index) in state.detailInfo.buttons" :key="index">
          <a-button @click="handleButton(item)" :type="item.buttonStyle">{{
            item.buttonName
          }}</a-button>
        </template>
      </div>
      <template #footer>
        <a-button @click="closeModal()">关闭</a-button>
      </template>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, reactive, nextTick } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  // import { downloadByData } from '/@/utils/file/download';
  import { Descriptions, DescriptionsItem } from 'ant-design-vue';
  import { announceDetail, getAnnounceFile } from '/@/api/announce';
  import { useGo } from '/@/hooks/web/usePage';
  // import FileSaver from 'file-saver';
  import { getToken } from '/@/utils/auth';

  export default defineComponent({
    name: 'MessageDetailModal',
    components: { Descriptions, BasicModal, DescriptionsItem },
    setup() {
      const state = reactive({
        detailInfo: {
          title: '',
          sender: '',
          msgContent: '',
          createTime: '',
        },
      });
      const fileList = ref([]);
      const minHeight = ref('');
      const go = useGo();
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        console.log(data);
        setModalProps({ confirmLoading: false, width: 1200 });
        let recordId = data.record.id;
        if (recordId) {
          let result = await announceDetail({ id: recordId });
          fileList.value = await getAnnounceFile({ fatherId: recordId });
          if (result) {
            state.detailInfo = { ...result };
            nextTick(() => {
              minHeight.value = '90%';
            });
          }
        }
      });
      const getTitle = ref('查看详情');
      const detailTitle = ref('测试');
      function handleSubmit() {
        closeModal();
      }
      function handleDownload(record) {
        const token: string = getToken() as string;
        console.log(token, 'token字符串');
        const req = new XMLHttpRequest();
        req.open(
          'GET',
          location.origin +
            import.meta.env.VITE_GLOB_API_URL +
            '/file/downloadFileStream/' +
            record.id,
          true,
        );
        req.responseType = 'blob';
        req.setRequestHeader('X-Access-Token', token);
        req.onload = function () {
          const data = req.response;
          const blob = new Blob([data], { type: 'application/octet-stream' });
          console.log(blob, '文件对象');
          let dom = document.createElement('a');
          let url = window.URL.createObjectURL(blob);
          dom.href = url;
          dom.download = decodeURI(record.realName);
          dom.style.display = 'none';
          document.body.appendChild(dom);
          dom.click();
          dom?.parentNode.removeChild(dom);
          window.URL.revokeObjectURL(url);
        };
        req.send();
      }
      function handleButton(item) {
        console.log(item);
        closeModal();
        go('/myannounce/' + item.id);
      }

      return {
        closeModal,
        registerModal,
        getTitle,
        detailTitle,
        handleSubmit,
        state,
        handleButton,
        minHeight,
        fileList,
        handleDownload,
      };
    },
  });
</script>
<style lang="less" scoped>
  .detail-header {
    border-bottom: 1px solid #f0f0f0;

    .detail-item {
      margin-right: 15px;
    }
  }

  .detail-button {
    position: absolute;
    bottom: 5px;
    right: 14px;

    :deep(.ant-btn) {
      margin: 0 4px;
    }
  }

  .ant-modal {
    height: 100%;
  }

  .file-list {
    .file-title {
    }

    .file-name {
      // color:@primary-color;
      margin-right: 5px;
    }
  }
</style>

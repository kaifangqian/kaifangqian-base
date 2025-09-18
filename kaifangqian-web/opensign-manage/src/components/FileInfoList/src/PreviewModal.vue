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
  <BasicModal
    width="800px"
    title="预览"
    class="upload-preview-modal"
    v-bind="$attrs"
    @register="registerModal"
    :showOkBtn="false"
  >
  <img :src="previewImgBase64" alt="">
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import {getImgBase64} from '/@/api/sys/upload';

  export default defineComponent({
    name:'Preview',
    components: { BasicModal },
    props: {

    },
    setup() {
      const previewImgBase64 = ref('');

      const [registerModal, {setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          defaultFullscreen:true,
          cancelText:'关闭',
          zIndex:1300,
          getContainer: () => document.body.querySelector('.register-form') || document.body,
        });
        if(data.imgId){
          let result = await getImgBase64({imgId:data?.imgId});
          if(result){
            previewImgBase64.value = result.image
          }
        }
      })
    
      return {
        registerModal,
        closeModal,
        previewImgBase64
      };
    },
  });
</script>
<style lang="less">
  .upload-preview-modal {
    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }
  }
</style>

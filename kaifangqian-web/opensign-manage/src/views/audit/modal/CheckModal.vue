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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
      <div class="content-area">
          <ul>
            <li>
              <span class="detail-title">营业执照</span>
              <!-- <img :src="base64License" alt="" /> -->
              <a-image :src="base64License" :width="338" :height="202" alt="加载失败" class="resrun-preview-img"/>
            </li>
            <li v-if="enterpriseInfo.realItem != 3">
              <span class="detail-title">组织机构名称</span>
              <span class="detail-value">{{ enterpriseInfo.name  }}</span>
            </li>
            <li v-if="enterpriseInfo.realItem == 1 || enterpriseInfo.realItem == 4">
              <span class="detail-title">统一社会信用代码</span>
              <span class="detail-value">{{ enterpriseInfo.organizationNo  }}</span>
            </li>
            <li v-if="enterpriseInfo.realItem != 2">
              <span class="detail-title">法人姓名</span>
              <span class="detail-value">{{ enterpriseInfo.corporation  }}</span>
            </li>
            <li v-if="enterpriseInfo.realItem != 2">
              <span class="detail-title">法人身份证正反面</span>
              <span class="detail-value value-img">
                <a-space :size="20">
                  <a-image :src="base64IdPic1"  alt="加载失败" class="resrun-preview-img"/>
                  <a-image :src="base64IdPic2"  alt="加载失败" class="resrun-preview-img"/>
                </a-space>
                
                <!-- <img :src="base64IdPic1" alt="" />
                <img :src="base64IdPic2" alt="" /> -->
              </span>
            </li>
            <li v-if="enterpriseInfo.realItem != 2">
              <span class="detail-title">法人证件号</span>
              <span class="detail-value">{{ enterpriseInfo.corporationNo  }}</span>
            </li>
            <li v-if="enterpriseInfo.realItem != 2">
              <span class="detail-title">法人证手机号</span>
              <span class="detail-value">{{ enterpriseInfo.phone  }}</span>
            </li>
            <li>
              <span class="detail-title">企业授权确认书</span>
              <span class="detail-value">
                <BasicFileList :fileList="[enterpriseInfo.authorizeBook]"  v-if="enterpriseInfo.authorizeBook"/>
              </span>
            </li>
          </ul>
        </div>
      <template #footer>
        <div v-if="isUpdate">
          <a-button type="info" @click="handleCancel">取消</a-button>
          <a-button type="info" @click="handleReject">驳回</a-button>
          <a-button type="primary" @click="handlePass">通过</a-button>
        </div>
        <div v-if="!isUpdate">
          <a-button type="info" @click="handleCancel">取消</a-button>
        </div>
      </template>
    </BasicModal>
    <a-modal v-model:visible="approveVisible" title="驳回" @ok="handleOk" :width="600" :zIndex="999">
      <div class="reject-area">
        <BasicForm @register="registerForm" ></BasicForm>
      </div>
    

    </a-modal>

  </div>
</template>
<script lang='ts'>
import { defineComponent, ref, unref, computed, onMounted, nextTick } from 'vue'
import { BasicModal, useModalInner } from '/@/components/Modal';
import { Icon } from '/@/components/Icon';
import { BasicForm, useForm } from '/@/components/Form';
import { checkEnterpriseAudit, getEnterpriseAuditInfo } from '/@/api/audit';
import { useMessage } from '/@/hooks/web/useMessage';
import { getImgBase64 } from '/@/api/sys/upload';
import { BasicFileList } from '/@/components/FileInfoList/index';
import  { rejcetFormSchema } from '../data'



export default defineComponent({
  name: 'CheckModal',
  components: {
    BasicModal,
    Icon,
    BasicFileList,
    BasicForm
  },
  setup(_, { emit }) {
    const isUpdate = ref(true);
    const approveVisible = ref(false);
    const checkMsg = ref('');
    const recordId = ref('');
    const enterpriseInfo:any = ref({});
    const base64License = ref('');
    const base64IdPic1= ref('');
    const base64IdPic2= ref('');
    const { createMessage: msg } = useMessage();
    const [registerForm, {  resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: rejcetFormSchema,
        showActionButtonGroup: false,
      });

    const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
      setModalProps({
        confirmLoading: false,
        width: 1200,
        cancelText: '关闭',
        zIndex:888
      });
      isUpdate.value = !!data?.isUpdate;
      recordId.value = data.record.id;
      resetDetail();
      if (unref(recordId)) {
        let result = await getEnterpriseAuditInfo({id:unref(recordId)})
        if(result){
          enterpriseInfo.value = {
            ...result,
            realItem: data.record.realItem
          };
          fetchBase64(base64License,result.businessLicense?.id);
          fetchBase64(base64IdPic1,result.idCardFace?.id);
          fetchBase64(base64IdPic2,result.idCardEmblem?.id);
        }
      }

    });

    function resetDetail(){
      base64License.value = '';
      base64IdPic1.value = '';
      base64IdPic2.value = '';
    }



    const getTitle = computed(() => (!unref(isUpdate) ? '企业租户实名审核' : '企业租户实名审核'));

    onMounted(()=>{
      
    })

    async function handleSubmit() {
      try {
        setModalProps({ confirmLoading: true });
        let result;
        if (result) {
          msg.success('操作成功')
          closeModal();
          emit('success');
        }
      } finally {
        setModalProps({ confirmLoading: false });
      }
    }
    function handlePass() {
      checkOptions(true)
    }
    function handleCancel() {
      closeModal()
    }
    function handleReject() {
      approveVisible.value = true;
      nextTick(()=>{
        resetFields();
      })
    }
    async function handleOk() {
      checkOptions(false)
    }

    async function checkOptions(type){
      let data ={}
      if(!type){
        data  = await validate();
      }
      console.log(data)
      let result = await checkEnterpriseAudit({
        logId:unref(recordId),
        authStatus:type,
        ...data
      });
      if(result){
        emit('success')
      }
      approveVisible.value = false;
      closeModal()
    }
    async function fetchBase64(imgField, imgId){
      if(!imgId) return;
        let result = await getImgBase64({imgId})
        if(result){
          imgField.value = result.image;
        }
    }


    return {
      registerModal,
      getTitle,
      isUpdate,
      handleSubmit,
      enterpriseInfo,
      handlePass,
      handleReject,
      approveVisible,
      handleOk,
      checkMsg,
      handleCancel,
      base64License,
      base64IdPic1,
      base64IdPic2,
      registerForm


    };
  }
})
</script>
<style lang="less" scoped>
.content-area {
  ul {
    width: 70%;
    margin: 0 auto;
  }

  li {
    margin: 10px 0;
    display: flex;
    align-items: center;

    .detail-title {
      font-size: 14px;
      width: 150px;
      display: inline-block;
      text-align: right;
      margin-right: 10px;
      color: #666;
    }

    .detail-value {
      font-size: 14px;
      color: #000;
      font-weight: 500;

    }

    img {
      width: 280px;
      height: 200px;

    }
    .value-img{
      img{
        margin:5px;
      }
    }
  }
}
</style>
<style>
  .resrun-preview-img{
    height: 202px !important;
    max-width: 338px !important;
  }
</style>

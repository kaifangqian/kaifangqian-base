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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit"
    @cancel="handleCancel">
      <div class="modal-content-bg">
      <!-- 查询用户表单 -->
      <template v-if="searchResult === 0">
        <p style="margin-bottom: 12px; font-weight: 500; font-size: 20px;text-align: center;color: #000;">查找/创建企业</p>
        <BasicForm @register="registerForm" />
        <!-- <div class="query-company-header">
          <p>查询企业信息后，若未查询到结果，请点击创建企业按钮进行创建。</p>
        </div> -->
        <div class="w-full flex justify-center">
          <a-button type="primary" class="w-[150px]" @click="queryUser">查询</a-button>
        </div>
      </template>
      <!-- 未查询到结果进行创建企业的提示-->
      <template v-if="searchResult === 1">
        <a-empty :image="emptyImage"></a-empty>
        <p style="text-align: center;color: #666;">搜索 「{{companyName}}」 的结果</p>
        <div class="w-full flex justify-center">
          <a-space :size="20">
        <a-button type="info" class="w-[100px]" @click="searchResult = 0">返回</a-button>
        <a-button type="primary" class="w-[100px]" @click="createCompany">创建企业</a-button>
          </a-space>
        </div>
        <div class="create-company-footer" >
        点击创建企业代表您已阅读并同意
        <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
        <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
        我们将为您注册全新的企业账号
        </div>
        <!-- <BasicForm @register="registerForm" />
        <div class="w-full flex justify-center">
          <a-button type="primary" class="w-[150px]" @click="queryUser">查询</a-button>
        </div> -->
      </template>

      </div>

      <template #footer>
        <div class="w-full flex justify-center">
          <a-button type="primary" class="w-[150px]" @click="queryUser">查询</a-button>
        </div>
      </template> 
    </BasicModal>
    <CompanyInfoModal @register="companyInfoRegister" @success="companyInfoHandle" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, unref, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStoreWithOut } from '/@/store/modules/user';
  import { useModal } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { Empty } from 'ant-design-vue';
  import { companyQueryApi,companyAddApi } from '/@/api/auth/userAuth';
  import CompanyInfoModal from './CompanyInfoModal.vue';
  import { getMyTenantDeparts} from '/@/api/sys/user'; 

  defineEmits(['success','register']);
  
  const userStore = useUserStoreWithOut();

  const { createMessage: msg,createConfirm } = useMessage();

  const emptyImage = Empty.PRESENTED_IMAGE_SIMPLE;

  const searchResult = ref(0);
  const companyName = ref('');
  const [companyInfoRegister, { openModal: openCompanyInfo, closeModal: closeCompanyInfo }] =
    useModal();

  const [registerForm, { validate, getFieldsValue ,resetFields}] = useForm({
    labelWidth: 0,
    schemas: [
      {
        field: 'name',
        label: '',
        component: 'Input',
        required: true,
        colProps: { span: 24 },
        componentProps: {
          placeholder: '请输入企业名称',
        },
      },
    ],
    showActionButtonGroup: false,
    actionColOptions: {
      span: 23,
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false, width: 500, footer: null ,canFullscreen:false});
  });

  const getTitle = computed(() => '查询/创建企业');

  async function handleSubmit() {
    try {
      const values = await getFieldsValue();
      console.log('values:', values);
      const asyncPage = window.location.origin + '/#/user/centerInfo';
      //   const result = await getPersonAuthApi({ callbackPage: asyncPage, name: values.name });
      //   if (result.authUrl) {
      // window.open(result.authUrl, '_self');
      //   }
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
  async function queryUser() {
    try {
      setModalProps({ confirmLoading: true });
      const values = await getFieldsValue();
      if (!values.name) {
        msg.error('请输入企业名称');
        return;
      }

      let tenantList = await getMyTenantDeparts();
      let matchDepart:any = tenantList.filter((u:any)=>u.tenantName == values.name);
      if((matchDepart&&matchDepart.length)){
          createConfirm({
          title: '温馨提示', 
          content: '您已经关联 「'+ values.name +' 」企业，是否切换到该企业空间下？',
          okText:'切换企业',
          cancelText:'取消',
          iconType: 'warning',
          onOk:async function () {
            handleMenuClick(matchDepart[0])
          }
        })
      }else{
        const result = await companyQueryApi({ companyName: values.name });
        if (result.isExist === 1) {
          companyName.value = "";
          openCompanyInfo(true, {
            record: result,
          });
        } else {
          searchResult.value = 1;
          companyName.value = values.name;
        }
      }
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
  async function createCompany() { 
    const { code ,message} = await companyAddApi({name: companyName.value});
    if (code == 200) {
      closeModal();
      msg.success('创建企业成功,即将进入该企业空间下',3);
      let tenantList = await getMyTenantDeparts();
      let matchDepart:any = tenantList.filter((u:any)=>u.tenantName == companyName.value);
      if((matchDepart&&matchDepart.length && companyName.value)){
          handleMenuClick(matchDepart[0])
      }
    }else{
      msg.error('创建企业失败:'+ message, 3);
    }

  }

  async function handleMenuClick(depart){
  let params = {
      departId:depart.departId
    }
    const result = await userStore.selectTenantAuth(params);
    if(result){
        msg.success('切换成功,即将跳转')
    }
  }
  function handleCancel(){
    searchResult.value = 0;
    companyName.value = "";
    resetFields();


  }
  function companyInfoHandle() {}
  async function handleTerm(type){
    window.open('/#/terms/service/'+ type)
  }
</script>
<style lang="less" scoped>
  .modal-content-bg {
    // background: #f7fafd;
    // background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)),
    //   linear-gradient(135deg, #eff6ff -4%, #ffffff 100%);
    border-radius: 10px;
    padding: 12px 24px 12px 24px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  }
  .certified-header {
    margin-bottom: 20px;
    .tip-title {
      font-size: 15px;
      font-weight: 600;
      color: #222;
    }
    p {
      margin-bottom: 0;
    }
  }
  :deep(.ant-upload-select) {
    width: 100%;
    height: 200px;
    border-radius: 5px;
  }
  .query-company-header {
    margin-top: -10px;
    margin-bottom: 20px;
    font-size: 13px;
    color: #888;
    background: #f0f5ff;
    border-radius: 6px;
    padding: 10px 16px;
    p {
      margin-bottom: 0;
    }
  }
  .w-full.flex.justify-center {
    margin-top: 8px;
    margin-bottom: 8px;
  }
  .create-company-footer {
    margin-top: 24px;
    font-size: 13px;
    color: #888;
    background: #f6f6f6;
    border-radius: 6px;
    padding: 10px 16px;
    text-align: center;
    a {
      color: #1890ff;
      &:hover {
        color: #40a9ff;
        text-decoration: underline;
      }
    }
  }
  :deep(.ant-empty) {
    margin: 24px 0 12px 0;
  }
  :deep(.ant-btn) {
    font-size: 15px;
    border-radius: 6px;
    min-width: 90px;
  }

  :deep(.ant-input-affix-wrapper){
    border-radius: 4px;
    border-color: #d9e3f0;
    height: 40px;
    &:hover {
      border-color: #127fd2;  
    }
  }
  :deep(.ant-input) {
    font-size: 14px;
    color: #666;
    padding: 6px 12px;
  }
  :deep(.ant-btn-primary) {
    // font-size: 16px;
    border-radius: 8px;
    min-width: 100px;
    height: 32px;
    background: linear-gradient(90deg, #1890ff 0%, #40a9ff 100%);
    border: none;
    color: #fff;
    // font-weight: 600;
    box-shadow: 0 2px 8px rgba(24,144,255,0.10);
    transition: background 0.2s, box-shadow 0.2s;
    &:hover, &:focus {
      background: linear-gradient(90deg, #127fd2 0%, #1890ff 100%);
      color: #fff;
      box-shadow: 0 4px 16px rgba(24,144,255,0.18);
    }
  }
</style>

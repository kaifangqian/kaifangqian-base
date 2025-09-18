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
        <BasicForm @register="registerForm" >
          <template #internalCcerId="{model,field}">
              <div class="cc-user-set">
                <a-select v-model:value="model[field]"  :options="ccusers"  mode="multiple" :fieldNames="{value:'internalCcerId',label:'internalCcerName'}" >
                </a-select>
              <a-button type="primary" @click="handleAddUser">选择抄送人</a-button>
            </div>
          </template>
          <template #externalCcedValue="{model,field}">
            <a-input v-model:value="model[field]" placeholder="请填写手机号" v-if="webConfig.systenAccountType == 'phone'"/>
            <a-input v-model:value="model[field]" placeholder="请填写邮箱" v-else-if="webConfig.systenAccountType == 'email'"/>
            <a-input v-model:value="model[field]" placeholder="请填写手机号或邮箱" v-else/>
          </template>
        </BasicForm>
        <TabTreeModal @register="registerUserModal" @success="handleSuccess" />
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed  } from 'vue'
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicForm, useForm } from '/@/components/Form';
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import { ccFormSchema } from '../data';
  import { getDeptNoCountTree } from '/@/api/sys/dept'; 
  import { getRoleTreeList,getUserByRoleId,  } from '/@/api/sys/role'; 
  import { getUserList } from '/@/api/demo/system'; 
  import { isMobile,isEmail } from '/@/utils/validate';
  import { useUserStore } from '/@/store/modules/user';
  import type { Rule } from 'ant-design-vue/es/form';

  export default defineComponent({
    name: 'FileCCModal',
    components:{
      BasicModal,
      BasicForm,
      TabTreeModal
    },
    setup(_, { emit }){
      
      const validateMobile =  (_rule: Rule, value: string) =>{
        if(!value){
          return Promise.reject('请输入手机号');
        }
        if(!isMobile(value)){
          return Promise.reject('手机号格式不正确');
        }else{
          return Promise.resolve(); 
        }
      }
      const validateEmail =  (_rule: Rule, value: string) =>{
        if(!value){
          return Promise.reject('请输入邮箱');
        }
        if(!isEmail(value)){
          return Promise.reject('邮箱格式不正确');
        }else{
          return Promise.resolve(); 
        }
      }
      const validateEmailOrMobile =  (_rule: Rule, value: string) =>{
        if(!value){
          return Promise.reject('请输入手机号或邮箱');
        }
        if(isMobile(value)){
          return validateMobile(_rule,value); 
        }else if(isEmail(value)){
          return validateEmail(_rule,value); 
        }else{
          return Promise.reject('手机号或邮箱格式不正确');
        }
      }
      
      const isUpdate = ref(true);
      const rowId = ref('');
      const ccusers:any = ref([])

      const userStore = useUserStore();
      const webConfig:any = userStore.getWebConfig;
      const { createMessage: msg } = useMessage();

      const [registerUserModal, { openModal,closeModal:closeUserMoal }] = useModal();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭' 
        });
        rowId.value = data.record?.sealId;
        resetFields();
        setFieldsValue({ccerType:1})
        
      });
      function buildAccountRules(){
        if(webConfig.systenAccountType == 'phone'){
          ccFormSchema[3].rules = [{required:true,validator:validateMobile}];
          
        }else if(webConfig.systenAccountType == 'email'){
          ccFormSchema[3].rules = [{required:true,validator:validateEmail}];
        }else{
          ccFormSchema[3].rules = [{required:true,validator:validateEmailOrMobile}];
        }
      }
      buildAccountRules();
      const [registerForm, {setFieldsValue,resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: ccFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
      
      const getTitle = computed(() => (!unref(isUpdate) ? '添加抄送人' : '添加抄送人'));

      function handleAddUser(){
        openModal(true,{
          isupDate:false,
          title:'添加签署人',
          tabs:[
              { 
                title:'根据组织选人',
                type:'deptUser',
                api:getDeptNoCountTree,
                asyncTree:true,
                asyncApi:getUserList,
                asyncFieldNames:{
                  title:'nickName',
                  key:'id'
                },
                fieldNames:{
                  children:'children',
                  title:'departName',
                  key:'id'
                }
              },
              { 
                title:'根据角色选人',
                type:'roleUser',
                api:getRoleTreeList,
                asyncTree:true,
                asyncApi:getUserByRoleId,
                asyncFieldNames:{
                  title:'nickName',
                  key:'userId'
                },
                fieldNames:{
                  children:'children',
                  title:'name',
                  key:'id'
                }
              },
            ]
        })
      }

      function handleSuccess(list){
        if(!list.length){
          msg.warning('请至少选择一个抄送人')
        }
        let keys:string[] = [];
        console.log(list)
        ccusers.value = [];
        list.map(item=>{
          ccusers.value.push({
            internalCcerId:item.tenantUserId,
            internalCcerName:item.nickName,
            ccerType:1,
            ccerAddType:2,
            id:'',
            externalCcedType:0
          })
          keys.push(item.tenantUserId);
        })
        closeUserMoal();
        setFieldsValue({
          internalCcerId:keys
        })

      }

      async function handleSubmit() {
        try {
            let data = await validate();
            let ccUser;
            if(data.ccerType==1){
              ccUser = ccusers.value;
            }else{
              ccUser = [{
                ccerType:2, 
                ccerAddType:2,
                id:'',
                internalCcerId:'',
                externalCcerName:data.externalCcerName,
                externalCcedValue:data.externalCcedValue,
                externalCcedType:data.externalCcedValue?(isMobile(data.externalCcedValue)?1:2):0,
              }]
            }
            if(!ccUser.length){
              msg.warning('请选择抄送人');
              return;
            }
            closeModal();
            emit('success',ccUser);
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        registerForm,
        ccusers,
        registerUserModal,
        handleSuccess,
        handleAddUser,webConfig
      };
    }
  })
</script>
<style lang="less" scoped>
.cc-user-set{
  display: flex;
  align-items: center;
  width:100%;
  :deep(.ant-select){
    // width:250px;
  }
}
  
</style>

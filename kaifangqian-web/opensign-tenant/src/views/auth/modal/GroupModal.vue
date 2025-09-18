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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
         <BasicForm @register="registerForm" >
        </BasicForm>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { Icon } from '/@/components/Icon';
  import { createAuthGroupFormSchema } from '../data';
  import { addAuth,editAuth,getAuthGroupInfo, } from '/@/api/auth/group';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'GroupModal',
    components:{
      BasicModal,
      BasicForm,
      PlusOutlined,
      Icon,
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const recordId = ref('');
      const appId = ref("");
      const { createMessage: msg } = useMessage();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: createAuthGroupFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        appId.value = data?.appId;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          let result = await getAuthGroupInfo({id:recordId.value })
          setFieldsValue({
            ...result,
          });
        }else{
           recordId.value = '';
        }
      });

  
      const getTitle = computed(() => (!unref(isUpdate) ? '新增权限组分类' : '编辑权限组分类'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let result = reactive({});
          if(!unref(isUpdate)){
             result = await addAuth({...values,tenantAppId:unref(appId)})
          }else{
            result = await editAuth({
              ...values,
              id:unref(recordId),
              tenantAppId:unref(appId)
            })
          }
          if(result){
            msg.success('操作成功');
            emit('success');
            closeModal();
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
     

      return { 
          registerModal,
          getTitle,
          registerForm,
          handleSubmit
      
      };
    }
  })
</script>
<style>
 
</style>

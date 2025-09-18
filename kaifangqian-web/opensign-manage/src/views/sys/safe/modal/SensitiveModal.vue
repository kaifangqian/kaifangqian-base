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
  import { defineComponent,ref,unref,computed } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { tableFormSchema } from '../data';
  import { updateSensitive, addSensitive} from '/@/api/sys/safe';

  export default defineComponent({
    name: 'SensitiveModal',
    components:{
      BasicModal,
      BasicForm
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const recordId = ref('');
      const recordInfo = ref();
      const { createMessage: msg } = useMessage();

      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        resetFields()
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          recordInfo.value = data.record;
          setFieldsValue({
            ...data.record,
            sensitiveType:data.record.sensitiveType==='phone'?1:0
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));


      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: tableFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
    
      async function handleSubmit(){
        try {
          const values = await validate();

          setModalProps({ confirmLoading: true });
          let result;
          if(!unref(isUpdate)){
              result = await addSensitive({...unref(recordInfo),...values,sensitiveType:values.sensitiveType===0?'password':'phone'});
          }else{
              result = await updateSensitive({...unref(recordInfo),...values,sensitiveType:values.sensitiveType===0?'password':'phone'});
          }
          if(result){
            msg.success('保存成功');
            closeModal();
            emit('success');
          }else{
            msg.warning(result.message)
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
  
    

      return {
        registerModal,
        handleSubmit,
        getTitle,
        registerForm
      }
    },
  })
</script>
<style>
 
</style>

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
        
      </BasicForm>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref, unref, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { announceTypeForm } from '../data';
  import { announceTreeAdd,announceTreeEdit } from '/@/api/announce'; 
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name:'TempalteTypeModal',
    components: { BasicForm, BasicModal},
    setup(_,{emit}) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const { createMessage: msg } = useMessage();
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false,width:1200 });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          console.log(data.record,'回显数据。。。。')
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
        }else{
          resetFields();
          rowId.value  = '';
        }
          
      });
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: announceTypeForm,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
      const getTitle = computed(() => (unref(isUpdate) ? '编辑公告分类':'新增公告分类'));
      async function handleSubmit(){
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let result;
          if(!unref(isUpdate)){
              result =  await announceTreeAdd(values);
              msg.success('新增成功');
          }else{
              result = await announceTreeEdit({id:unref(rowId),...values});
              msg.success('编辑成功');
          }
          if(result){
            closeModal();
            emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
        closeModal()

      }

      return {
        registerModal,
        registerForm,
        getTitle,
        handleSubmit
      };
    },
  });
</script>
<style lang="less" scoped>
.detail-header{
  .detail-title{
    font-size: 18px;
    font-weight: 6000;
  }
}
</style>

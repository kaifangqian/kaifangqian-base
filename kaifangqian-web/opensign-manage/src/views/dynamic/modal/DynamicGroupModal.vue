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
  import { createArticleGroupFormSchema } from '../data';
  import { addArticleGroup, editArticleGroup } from '/@/api/article';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'DynamicGroupModal',
    components:{
      BasicModal,
      BasicForm,
      PlusOutlined,
      Icon,
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const recordId = ref('');
      const { createMessage: msg } = useMessage();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: createArticleGroupFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ 
          confirmLoading: false,
          width:500,
          height:80,
          minHeight:80,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        recordId.value = data?.record?.recordId;
        // setFieldsValue({
        //     ...data.record,
        //   })
      });

  
      const getTitle = computed(() => (!unref(isUpdate) ? '新增分类' : '编辑分类'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let result = reactive({});
          if(!unref(isUpdate)){
             result = await addArticleGroup({...values})
          }else{
            result = await editArticleGroup({
              ...values,
              id:unref(recordId),
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
<style lang="less">
 
</style>

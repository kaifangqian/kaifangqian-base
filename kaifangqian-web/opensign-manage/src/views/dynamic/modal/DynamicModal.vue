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
          <template #content="{model,field}">
            <Tinymce v-model="model[field]" @change="handleTinymceChange" width="100%" :showImageUpload="false" />
        </template>
        </BasicForm>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { Icon } from '/@/components/Icon';
  import { createArticleFormSchema } from '../data';
  import { addArticleAdd, editArticle, getArticleInfo } from '/@/api/article';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Tinymce } from '/@/components/Tinymce';

  export default defineComponent({
    name: 'DynamicModal',
    components:{
      BasicModal,
      BasicForm,
      PlusOutlined,
      Icon,
      Tinymce
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const recordId = ref('');
      const recordInfo =ref();
      const tinymceValue = ref('')
      const { createMessage: msg } = useMessage();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: createArticleFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          let result = await getArticleInfo({id:recordId.value })
          recordInfo.value = result;
          setFieldsValue({
            ...result,
          });
        }else{
           recordId.value = '';
        }
      });

  
      const getTitle = computed(() => (!unref(isUpdate) ? '新增文章' : '编辑文章'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          let result = reactive({});
          if(!unref(isUpdate)){
             result = await addArticleAdd({...values,status:0})
          }else{
            result = await editArticle({
              ...unref(recordInfo),
              ...values,
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

      function handleTinymceChange(){
        
      }
     

      return { 
          registerModal,
          getTitle,
          registerForm,
          handleTinymceChange,
          handleSubmit,
          tinymceValue
      
      };
    }
  })
</script>
<style>
 
</style>

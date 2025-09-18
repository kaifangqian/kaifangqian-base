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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" @cancel="handleCancel">
      <BasicForm @register="registerForm">
        <template #content>
            <Tinymce v-model="tinymceValue" @change="handleTinymceChange" width="100%" :showImageUpload="false" />
        </template>
      </BasicForm>
    </BasicModal>
  </div>
</template>
<script lang='ts'>

import { defineComponent, ref,computed, unref} from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { BasicForm,useForm } from '/@/components/Form';
import { Tinymce } from '/@/components/Tinymce';
import { tinyFormSchema } from '../data';
import { setProtocol } from '/@/api/protocol';

export default defineComponent({
  name: 'CertificateForm',
  components:{
    BasicModal,
    BasicForm,
    Tinymce
  },
  setup(_,{emit}){

    const recordId = ref('');
    const tinymceValue = ref('')
    
    
    const { createMessage: msg } = useMessage();
   
     
    const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭',
          maskClosable:false,
        });
        recordId.value = data?.record?.id;
        tinymceValue.value = data?.record?.mediateContent
    });

    const [registerForm, { }] = useForm({
        labelWidth: 100,
        schemas: tinyFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
    const getTitle = computed(()=>('数字证书隐私协议'));
      async function handleOk(){
       
      }
      function handleTinymceChange(){
        
      }
      function handleCancel(){
        closeModal()
      }
      async function handleSubmit(){

        let params = {
          value:tinymceValue.value,
          id:unref(recordId),
          type:'certificate'
        }
        let result = await setProtocol(params)
        if(result){
          closeModal();
          emit('success')
          msg.success('操作成功')
        }
       
      }

    

      return {
        registerModal,
        handleSubmit,
        getTitle,
        handleOk,
        handleCancel,
        registerForm,
        tinymceValue,
        handleTinymceChange
      }
  }
})
</script>
<style lang="less" scoped>
</style>

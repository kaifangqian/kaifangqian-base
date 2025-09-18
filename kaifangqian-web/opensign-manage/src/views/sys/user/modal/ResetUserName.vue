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
           <Form  class="p-2 enter-x" :model="formData" :rules="getFormRules" ref="formRef" :labelCol="{span:3}">
              <FormItem name="mobile" class="enter-x" label="用户名">
                  <Input
                    size="large"
                    v-model:value="formData.username"
                    placeholder="用户名"
                    class="fix-auto-fill"
                  />
                </FormItem>
          </Form>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed,reactive } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { unBindEmail,bindEmail } from '/@/api/sys/user';
  import { Form, Input,Row, Col } from 'ant-design-vue';
  import { useFormRules } from '/@/views/sys/login/useLogin';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { updateUserRealname } from '/@/api/sys/user';


  export default defineComponent({
    name: 'AnnounceFormModal',
    components:{
      BasicModal,
      Form,
      FormItem:Form.Item,
      Input,
      Row, Col
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const { createMessage: msg } = useMessage();
      const formData = reactive({
        username:""
      })
    
      const { getFormRules } = useFormRules();
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false,width:550, });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          formData.username = data.record.username;
        }
         
      });
      const getTitle = computed(() => (unref(isUpdate) ? '修改用户名' : '修改用户名'));

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let params = {
            realname:formData.username
          }
          let result = await updateUserRealname(params);
          if(result){
            closeModal();
            msg.success('操作成功');
            emit('success' );
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
     

      return { registerModal,formData,getFormRules, getTitle, handleSubmit};
    }
  })
</script>
<style >
 
</style>

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
          <Form  class="p-2 enter-x" :model="formData" ref="formRef" :labelCol="{span:3}">
              <FormItem name="time" class="enter-x" label="免验设置">
                <Switch v-model:checked="formData.signFlag" checkedChildren="已开启" unCheckedChildren="已关闭"></Switch>
              </FormItem>
              <FormItem name="sms" class="enter-x" label="时效">
                <Select v-model:value="formData.time" :options="timeList" placeholder="请选择"></Select>
              </FormItem>
          </Form>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import type { SelectProps } from 'ant-design-vue';
  import { defineComponent,ref,unref,computed,reactive } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Form, Input,Row, Col, Select, Switch } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'SignFreeSet',
    components:{
      BasicModal,
      Form,
      FormItem:Form.Item,
      Input,
      Row, Col,
      Select,
      Switch
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const formData = reactive({
        signFlag:false,
        time:''
      });
      const timeList = ref<SelectProps['options']>([
        {value:'5',label:'5分钟'},
        {value:'15',label:'15分钟'},
        {value:'30',label:'30分钟'},
        {value:'60',label:'1h'},
        {value:'120',label:'2h'},
        {value:'240',label:'4h'},
        {value:'480',label:'8h'},
        {value:'720',label:'12h'},
        {value:'1440',label:'24h'},
      ])
      const { createMessage: msg } = useMessage();
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false,width:550, });
        isUpdate.value = !!data?.isUpdate;
        
         
      });
      const getTitle = computed(() => (unref(isUpdate) ? '免验设置' : '免验设置'));

      async function handleSubmit() {
        try {
          msg.success('设置成功');
          closeModal();
          emit('success')
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
     

      return {
        registerModal,
        formData,
        getTitle,
        handleSubmit,
        timeList
       };
    }
  })
</script>
<style >
 
</style>

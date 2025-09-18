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
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="70%"
    @ok="handleSubmit"
    >
      <Tabs>
        <TabPane key="1" tab="基本信息">
            <BasicForm @register="registerForm" />
        </TabPane>
      </Tabs>
  </BasicDrawer>
</template>
<script lang='ts'>

import { defineComponent, computed, unref, ref } from 'vue';
import { Tabs } from 'ant-design-vue';
import { BasicForm, useForm } from '/@/components/Form/index';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import {organizeFormSchema } from '../data';
import { addOrganize} from '/@/api/sys/dept'


export default defineComponent({
  name: 'Organize',
  components:{
    BasicForm,
    BasicDrawer,
    Tabs,
    TabPane: Tabs.TabPane
  },
  setup(_, { emit }){
      const isUpdate = ref(true);
      const getTitle = computed(() => ('添加组织'));
      const [registerForm, { setFieldsValue, validate,clearValidate }] = useForm({
        labelWidth: 100,
        schemas: organizeFormSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        clearValidate()
        if(unref(isUpdate)) {
          setFieldsValue({
            ...data.record
          })
        }
      })

      async function handleSubmit(){
       try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          // TODO custom api
          let result = await addOrganize(values);
          
          if(result){
            closeDrawer();
            emit('success');
          }
         
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
    }
    return {
      registerDrawer,
      registerForm,
      getTitle,
      handleSubmit

    }
  }
})
</script>
<style>
 
</style>

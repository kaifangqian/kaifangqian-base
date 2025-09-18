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
    width="50%"
    @ok="handleSubmit"
    >
      <span>{{logContent}}</span>
      <!-- <BasicForm @register="registerForm" /> -->
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  // import { BasicForm, useForm } from '/@/components/Form/index';
  // import { logInfoSchema, } from '../log';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { Tabs,Card } from 'ant-design-vue';
  import { getSysErrorlogInfo } from '/@/api/sys/log';

  export default defineComponent({
    name: 'DeptBatchDrawer',
    components: { BasicDrawer,
    //  BasicForm, 
     Tabs,  
     TabPane: Tabs.TabPane
     ,Card},
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const logContent = ref('');

      // const [registerForm, { resetFields, setFieldsValue }] = useForm({
      //   labelWidth: 100,
      //   schemas: logInfoSchema,
      //   showActionButtonGroup: false,
      //   baseColProps: { lg: 24, md: 24 },
      // });
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        // resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          if(data.record.errorLogId){
            getStackErrorContent(data.record.errorLogId)
          }
        }
      });


      const getTitle = computed(() => ('日志详情'));

      async function handleSubmit() {
        try {
          setDrawerProps({ confirmLoading: true });
          closeDrawer();
          emit('success');
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      async function getStackErrorContent( id: string){
        let result = await getSysErrorlogInfo({id});
        if(result){
          logContent.value = result.errorStackInfo;
          // setFieldsValue({
          //   logContent:result.errorStackInfo
          // });
        }
      }

      return { registerDrawer, getTitle, handleSubmit ,isUpdate, logContent};
    },
  });
</script>

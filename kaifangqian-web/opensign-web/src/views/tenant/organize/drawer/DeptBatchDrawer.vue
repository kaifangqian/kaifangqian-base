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
      <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formDeptBatchSchema, } from '../data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { Tabs,Card } from 'ant-design-vue';
  import { addUser} from '/@/api/sys/user';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'DeptBatchDrawer',
    components: { BasicDrawer, BasicForm, Tabs,  TabPane: Tabs.TabPane,Card},
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const defaultDeptKeys = ref();
      const { createMessage: msg } = useMessage();

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: formDeptBatchSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        console.log(data.record.checkedKeys,'---dddf-')
        if (unref(isUpdate)) {
          defaultDeptKeys.value = data.record.checkedKeys;
          setFieldsValue({
            ...data.record,
            departName:unref(defaultDeptKeys)
          });
        }
      });

      const getTitle = computed(() => ('批量编辑部门'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          let result = await addUser({
            ...values});
          if(result){
              msg.success('操作成功')
          }
          // TODO custom api
          console.log(values);
          closeDrawer();
          emit('success');
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }

      return { registerDrawer, registerForm, getTitle, handleSubmit ,isUpdate};
    },
  });
</script>

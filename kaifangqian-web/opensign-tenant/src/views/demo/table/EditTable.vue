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
      <BasicTable @register="registerTable" @edit-change="onEditChange">
        <template #toolbar>
          <a-button type="primary" @click="handleCreate">新增</a-button>
        </template>
        <template #action="{ record, column }">
          <TableAction :actions="createActions(record, column)" />
        </template>
      </BasicTable>
      <TableModal @register="registerModal" @success="handleSuccess"  />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import {
    BasicTable,
    useTable,
    TableAction,
    BasicColumn,
    ActionItem,
    EditRecordRow,
  } from '/@/components/Table';

  import { useModal } from '/@/components/Modal';
  import TableModal from '/@/views/demo/modal/TableModal.vue'
  // import { optionsListApi } from '/@/api/demo/select';
  import { demoListApi } from '/@/api/demo/table';
  // import { treeOptionsListApi } from '/@/api/demo/tree';
  import { cloneDeep } from 'lodash-es';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';

  const columns: BasicColumn[] = [
    {
      title: '名称',
      dataIndex: 'name',
      editRow: true,
      editComponentProps: {
        prefix: '$',
      },
    },
    {
      title: '默认输入状态',
      dataIndex: 'name7',
      editRow: true,
    },
    {
      title: '输入框校验',
      dataIndex: 'name1',
      editRow: true,
      align: 'left',
      // 默认必填校验
      editRule: true,
    },
    {
      title: '输入框函数校验',
      dataIndex: 'name2',
      editRow: true,
      align: 'right',
      editRule: async (text) => {
        if (text === '2') {
          return '不能输入该值';
        }
        return '';
      },
    },
    {
      title: '数字输入框',
      dataIndex: 'id',
      editRow: true,
      editRule: true,
      editComponent: 'InputNumber',
    },
   
    {
      title: '勾选框',
      dataIndex: 'name5',
      editRow: true,
      editComponent: 'Checkbox',
      editValueMap: (value) => {
        return value ? '是' : '否';
      },
      width: 100,
    },
    {
      title: '开关',
      dataIndex: 'name6',
      editRow: true,
      editComponent: 'Switch',
      editValueMap: (value) => {
        return value ? '开' : '关';
      },
      width: 100,
    },
  ];
  export default defineComponent({
    components: { BasicTable, TableAction ,TableModal},
    setup() {
      const { createMessage: msg } = useMessage();
      const currentEditKeyRef = ref('');
      const { hasPermission } = usePermission();
      const [registerModal, { openModal }] = useModal();
      const [registerTable] = useTable({
        title: '编辑表格',
        titleHelpMessage: [],
        api: demoListApi,
        columns: columns,
        useSearchForm: true,
        showIndexColumn: false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:false},
        formConfig: {
          labelWidth: 120,
          schemas: [
            { field: 'menuName',  label: '关键字',   component: 'Input',  colProps: { span: 8 }, },
            {  field: 'status', label: '状态', component: 'Select',  componentProps: {
                options: [
                  { label: '启用', value: '0' },
                  { label: '停用', value: '1' },
                ],
              },
              colProps: { span: 8 },
            }
          ],
        },
        actionColumn: {
          width: 160,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
      });

      function handleCreate() {
        openModal(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: EditRecordRow) {
        currentEditKeyRef.value = record.key;
        record.onEdit?.(true);
      }

      function handleCancel(record: EditRecordRow) {
        currentEditKeyRef.value = '';
        record.onEdit?.(false, false);
      }
      function handleSuccess(){

      }

      async function handleSave(record: EditRecordRow) {
        // 校验
        msg.loading({ content: '正在保存...', duration: 0, key: 'saving' });
        const valid = await record.onValid?.();
        if (valid) {
          try {
            const data = cloneDeep(record.editValueRefs);
            console.log(data);
            //TODO 此处将数据提交给服务器保存
            // ...
            // 保存之后提交编辑状态
            const pass = await record.onEdit?.(false, true);
            if (pass) {
              currentEditKeyRef.value = '';
            }
            msg.success({ content: '数据已保存', key: 'saving' });
          } catch (error) {
            msg.error({ content: '保存失败', key: 'saving' });
          }
        } else {
          msg.error({ content: '请填写正确的数据', key: 'saving' });
        }
      }

      function createActions(record: EditRecordRow, column: BasicColumn): ActionItem[] {
        if (!record.editable) {
          return [
            {
              label: '编辑',
              disabled: currentEditKeyRef.value ? currentEditKeyRef.value !== record.key : false,
              onClick: handleEdit.bind(null, record),
            },
          ];
        }
        return [
          {
            label: '保存',
            onClick: handleSave.bind(null, record, column),
          },
          {
            label: '取消',
            popConfirm: {
              title: '是否取消编辑',
              confirm: handleCancel.bind(null, record, column),
            },
          },
        ];
      }

      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }

      return {
        hasPermission,
        handleSuccess,
        registerModal,
        handleCreate,
        registerTable,
        handleEdit,
        createActions,
        onEditChange,
      };
    },
  });
</script>

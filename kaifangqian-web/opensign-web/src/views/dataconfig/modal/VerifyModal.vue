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
        <BasicForm @register="registerForm" class="modal-form">
        </BasicForm>
        <a-button block type="dashed" > 生成 </a-button>
        <Tabs>
          <TabPane key="1" tab="局部规则">
              <BasicTable @register="registerTable" @edit-change="handleEditChange">
                  <template #toolbar>
                    <a-button  type="dashed" @click="handleAdd"> <template #icon><PlusOutlined /></template>新增 </a-button>
                  </template>
                  <template #headerTop>
                    <a-alert type="info" show-icon>
                        <template #message>
                            <span>局部规则按照你输入的位数有序的校验</span>
                        </template>
                    </a-alert>
                  </template>
                  <template #action="{ record, column }">
                      <TableAction :actions="createActions(record, column)" />
                  </template>
              </BasicTable>
          </TabPane>
          <TabPane key="2" tab="全局规则">
              <template #toolbar>
                 <a-button  type="dashed" @click="handleAdd"> <template #icon><PlusOutlined /></template>新增 </a-button>
              </template>
              <template #headerTop>
                <a-alert type="info" show-icon>
                    <template #message>
                        <span>全局规则可校验用户输入的所有字符；全局规则的优先级比局部规则优先级要高</span>
                    </template>
                </a-alert>
              </template>
              <BasicTable @register="registerGlobalTable" @edit-change="handleEditChange">
                <template #action="{ record, column }">
                    <TableAction :actions="createActions(record, column)" />
                  </template>
              </BasicTable>
          </TabPane>
        </Tabs>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed } from 'vue'
  import { BasicTable, useTable, EditRecordRow, BasicColumn, ActionItem, TableAction } from '/@/components/Table';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Tabs,Alert } from 'ant-design-vue';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { BasicForm, useForm } from '/@/components/Form';
  import { verifyModalFormSchema, verifyLocalModalColumns, verifyGlobalModalColumns } from '../data'
  import { addCheckRuleList } from '/@/api/dict';
  import { useMessage } from '/@/hooks/web/useMessage';


  export default defineComponent({
    name: 'DictModal',
    components:{
      BasicTable,
      BasicModal,
      AAlert:Alert,
      BasicForm,
      PlusOutlined,
      TableAction,
      Tabs,
      TabPane: Tabs.TabPane,
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const rowId = ref('');
      const tinymceValue = ref();
      const { createMessage: msg } = useMessage();

      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: verifyModalFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerTable, { getDataSource }] = useTable({
        columns: verifyLocalModalColumns,
        showIndexColumn: false,
        dataSource: [],
        actionColumn: {
          width: 160,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        pagination: false,
      });

      const [registerGlobalTable, { getDataSource: getDataGlobalSource }] = useTable({
        columns: verifyGlobalModalColumns,
        showIndexColumn: false,
        dataSource: [],
        actionColumn: {
          width: 160,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        pagination: false,
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
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增' : '编辑'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let result;
          if(!unref(isUpdate)){
              result = await addCheckRuleList(values);
          }else{
              result = await addCheckRuleList(values);
          }
          if(result){
            msg.success('保存成功');
            closeModal();
            emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
          }else{
            msg.warning(result.message)
          }
          console.log(values);
          closeModal();
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      function handleEditChange(data: Recordable) {
        console.log(data);
      }
      function handleEdit(record: EditRecordRow) {
        record.onEdit?.(true);
      }

      function handleCancel(record: EditRecordRow) {
        record.onEdit?.(false);
        if (record.isNew) {
          const data = getDataSource();
          const index = data.findIndex((item) => item.key === record.key);
          data.splice(index, 1);
        }
      }

      function handleest(){
          const data = getDataGlobalSource();
          console.log(data)
      }

      function handleSave(record: EditRecordRow) {
        record.onEdit?.(false, true);
      }

      function createActions(record: EditRecordRow, column: BasicColumn): ActionItem[] {
        if (!record.editable) {
          return [
            {
              label: '编辑',
              onClick: handleEdit.bind(null, record),
            },
            {
              label: '删除',
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
      function handleAdd() {
        const data = getDataSource();
        const addRow: EditRecordRow = {
          type: '',
          content: '',
          length: '',
          editable: true,
          isNew: true,
          key: `${Date.now()}`,
        };
        data.push(addRow);
      }

      return { registerModal,registerTable,registerGlobalTable, handleEditChange, registerForm, createActions, handleest,getTitle, handleSubmit,tinymceValue,handleTinymceChange,handleAdd };
    }
  })
</script>
<style lang="less" scoped>
.modal-form{
  :deep(.ant-row){
    justify-content: center;
  }
}
 
</style>

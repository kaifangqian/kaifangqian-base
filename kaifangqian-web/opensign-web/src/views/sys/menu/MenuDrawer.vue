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
    width="60%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
      <Tabs v-if="currentMenuType==1">
          <TabPane key="1" tab="按钮配置">
             <a-button  type="dashed" @click="handleAdd">新增 </a-button>
              <BasicTable @register="registerTable" @edit-change="handleEditChange">
              <template #formTableId="{ record,index }">
                  <Select v-model:value="record.formTableId" style="width:100px" :options="optionsData" disabled :fieldNames="{label: 'tableTxt', value: 'id',}"/>
                  <a-button @click="handlePreTable(index)">选择</a-button>
              </template>
              <template #ruleFlag="{record}">
                  <Select v-model:value="record.ruleFlag" style="width:100px"  :options="optionsRuleFlag" :disabled="record.id?true:false" />
              </template>
              <template #action="{ record, column }">
                  <TableAction :actions="createActions(record, column)" />
              </template>
              </BasicTable>
          </TabPane>
        </Tabs>
  </BasicDrawer>
  <PerTable @register="registerModal" @success="handleSuccess" />
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, nextTick, onMounted } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema,btnCloumns } from './menu.data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { Tabs,Select } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { cloneDeep } from 'lodash-es';
  import { BasicTable, useTable, EditRecordRow, BasicColumn, ActionItem, TableAction } from '/@/components/Table';
  import { setMenu ,updateMenu,getMenuBtnList,deleteMenu } from '/@/api/demo/system';
  import { getAuthTable } from '/@/api/auth/group';
  import PerTable from './modal/PerTable.vue'

  export default defineComponent({
    name: 'MenuDrawer',
    components: { BasicDrawer, BasicForm, Tabs, TabPane: Tabs.TabPane,BasicTable, TableAction,Select,PerTable},
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const optionsRuleFlag = ref([
        { label: '是', value: 1 },
        { label: '否', value: 0 },
      ])
      const isUpdate = ref();
      const bthEdit = ref(false);
      const recordId = ref('');
      const currentMenuType = ref(0);
      const currentBtnIndex = ref(0);
      const [registerModal, { openModal,closeModal }] = useModal();
      const { createMessage: msg } = useMessage();
      const optionsData = ref();

      const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
        labelWidth: 100,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 12, md: 24 },
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        // const treeData = await getMenuList();
        if (unref(isUpdate)) {
          //更新表单属性
          updateSchema({  field: 'menuType', componentProps: { disabled:true }});
          setFieldsValue({
            ...data.record,
            routeFlag:data.record.routeFlag
            // routeFlag:data.record.routeFlag===1?true:false
          });
          recordId.value = data.record.id;
          currentMenuType.value = data.record.menuType;
          if(data.record.menuType==1){
            reloadTabletrigger(unref(recordId));
          }
        }else{
          updateSchema({  field: 'menuType', componentProps: { disabled:false }});
          recordId.value = "";
          currentMenuType.value = 0;
        }
      });
      const [registerTable, { getDataSource,setTableData,updateTableData }] = useTable({
        columns: btnCloumns,
        showIndexColumn: true,
        dataSource: [],
        actionColumn: {
          width: 160,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        pagination: false,
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));

      onMounted(() => {
        fetch();
      });
      async function fetch(){
         let result = await getAuthTable({});
          if(result){
            optionsData.value = result.records;
          }
      }
      async function reloadTabletrigger(parentId:string){
         const btnTableData = await getMenuBtnList({parentId});
          nextTick(()=>{
              setTableData(cloneDeep(btnTableData));
          })
      }
      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          // TODO custom api
          let params = {
            ...values,
            // routeFlag:values.values?1:0
          }
          let result = ref();
          if(!unref(isUpdate)){
            result = await setMenu(params);
          }else{
            result = await updateMenu({
              ...params,
              id:unref(recordId)
            });
          }
          if(result){
            closeDrawer();
            emit('success');
          }
          
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
       function handleEdit(record: EditRecordRow) {
        // bthEdit.value = true;
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
      function handleEditChange(data: Recordable) {
        console.log(data);
      }

      async function handleSave(record: EditRecordRow) {
       // 校验
        msg.loading({ content: '正在保存...', duration: 0, key: 'saving' });
        const valid = await record.onValid?.();
        if (valid) {
          try {
            const data = cloneDeep(record.editValueRefs);
            // console.log(data,'--行数据---');
            // console.log(record,'--行数据2---');
            // 保存之后提交编辑状态
            let params = {
              ...data,
              parentId:unref(recordId),
              menuType:2,
              id:record.id,
              ruleFlag:record.ruleFlag,
              formTableId:unref(record).formTableId
            }
            let result = ref();
            if(record.id){
              result = await updateMenu(params);
            }else{
              result = await setMenu(params);
            }
            if(result){
              msg.success({ content: '保存成功', key: 'saving' });
              reloadTabletrigger(unref(recordId))
              record.onEdit?.(false, true);
            }else{
              msg.error({ content: '保存失败', key: 'saving' });
            }
           
          } catch (error) {
            msg.error({ content: '保存失败', key: 'saving' });
          }
        } else {
          msg.error({ content: '请填写正确的数据', key: 'saving' });
        }
      }
      async function handleDelete(record: EditRecordRow){
        let deleteResult = await deleteMenu({id:record.id});
        if(deleteResult){
          reloadTabletrigger(unref(recordId))
          msg.success({ content: '操作成功', key: 'saving' });
        }else{
          msg.error({ content: '操作失败', key: 'saving' });
        }
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
              onClick:handleDelete.bind(null, record)
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

      function handleAdd(){
        const data = getDataSource();
        const addRow: EditRecordRow = {
          name: '',
          code: '',
          component: '',
          editable: true,
          isNew: true,
          key: `${Date.now()}`,
        };
        data.push(addRow);

      }
      function handleSuccess(record){
        updateTableData(currentBtnIndex.value,'formTableId',record[0].id);
        closeModal();
      }
      function handlePreTable(index){
        currentBtnIndex.value = index;
        openModal(true,{
          isUpdate:false
        })
      }

      return { 
        registerDrawer,
        handlePreTable, 
        registerForm, 
        handleAdd,
        createActions,
        getTitle, 
        handleSubmit,
        handleEditChange,
        registerTable,
        currentMenuType,
        handleSuccess,
        registerModal,
        optionsData,
        optionsRuleFlag,
        isUpdate 
      };
    },
  });
</script>
<style lang="less" scoped>
:deep(.ant-table-body){
  padding-bottom: 50px;
}
</style>

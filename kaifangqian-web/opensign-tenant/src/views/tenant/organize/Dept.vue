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
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess" @row-click="onRowClick" @expand="handleExpand"
    :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
      <template #toolbar>
          <a-button type="primary" @click="handleCreate" v-if="hasPermission(['dept:add'])">添加部门 </a-button>
          <a-button type="default" @click="handleBatchDelete" v-if="hasPermission(['dept:deletebatch'])">批量删除 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction :stop-buttonPropagation="true"
          :actions="[
            {
              label: '编辑部门信息',
              onClick: handleDeptMenu.bind(null, record,true),
              auth:'dept:edit'
            },
            {
              label: '设置部门主管',
              onClick: handleDeptManager.bind(null, record),
              auth:'dept:setmanager'
            },
            {
              label: '添加子部门',
              onClick: handleDeptMenu.bind(null, record,false),
              auth:'dept:addchild'
            },
          ]"
        >
        </TableAction>
      </template>
    </BasicTable>
    <DeptDrawer @register="registerDeptDrawer" @success="handleSuccess" />
    <DeptBatchDrawer @register="registerDeptBatchDrawer" @success="handleSuccess" />
    <TabTreeModal @register="registerModal" @success="handleTreeSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref,reactive,unref } from 'vue';
  import { BasicTable, useTable, TableAction, EditRecordRow} from '/@/components/Table';
  import { deptColumns } from './data';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import DeptDrawer from './drawer/DeptDrawer.vue';
  import DeptBatchDrawer from './drawer/DeptBatchDrawer.vue';
  import TabTreeModal from './modal/TabTreeModal.vue';
  import { getUserList } from '/@/api/demo/system'; 
  // import { getRoleTreeList, getUserByRoleId } from '/@/api/sys/role'; 
  import { editDept, getDeptLevel } from '/@/api/sys/dept';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { usePermission } from '/@/hooks/web/usePermission';

  export default defineComponent({
    name: 'Dept',
    components: { BasicTable, TableAction,DeptDrawer,DeptBatchDrawer,TabTreeModal},
    setup() {
      const recordId = ref('');
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerDeptDrawer, { openDrawer: openDeptDrawer }] = useDrawer();
      const { createMessage: msg } = useMessage();
      const { hasPermission } = usePermission();
      const [registerModal, { openModal,closeModal }] = useModal();
      const [registerDeptBatchDrawer, { openDrawer:openDeptBatchDrawer }] = useDrawer();
      const [registerTable, { reload }] = useTable({
        title: '',
        api: getDeptLevel,
        columns:deptColumns,
        isTreeTable: true,
        pagination: true,
        isTriggerSelect:false,
        canResize: false,
        striped: false,
        bordered: false,
        rowKey:'id',
        defaultExpandAllRows:true,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        titleHelpMessage: '树形table和序号不可同时并存',
        showTableSetting: false,
        afterFetch:handleAfterFeatchData,
        // actionColumn: {
        //   // width: 180,
        //   aligin:'center',
        //   title: '操作',
        //   flag:"ACTION",
        //   dataIndex: 'action',
        //   slots: { customRender: 'action' },
        //   fixed: undefined,
        // },
      });
       function handleAfterFeatchData(list){
        list.map(item=>{
          item.children = item.childCount?[]:null;
        })
      }
      function handleBatchDelete (){
          if(!unref(checkedKeys).length){
            msg.warning('请勾选要删除的部门数据');
            return
          }
      }
      function handleBatchEdit(){
        if(!unref(checkedKeys).length){
          msg.warning('请勾选要编辑的部门数据');
          return
        }
        openDeptBatchDrawer(true,{
          isUpdate:true,
          record:{
            checkedKeys:unref(checkedKeys)
          }
        })
      }
      function handleCreate() {
        openDeptDrawer(true, {
          isUpdate: false,
        });
      }
      function onRowClick(record: EditRecordRow) {
        // openDeptDrawer(true,{
        //   isUpdate:true,
        //   record:{
        //       ...record
        //   }
        // })
      } 
      function handleDeptMenu(record:Recordable,flag: string | boolean) {
        console.log(record,'-----点击了菜单-----')
        openDeptDrawer(true,{
          isUpdate:flag,
          record:{
              ...record,
              currentDeptId:record.id   // 统一部门id 名称
          }
        })
      }
      function handleDeptManager(record: Recordable){
        recordId.value = record.id;
        console.log( recordId.value, '---部门id--')
        openModal(true,{
          isUpdate: true,
          title:'设置主管',
           tabs:[
              { 
                title:'人员列表',
                type:'user',
                api:getUserList,
                asyncTree:true,
                params:{
                  id:record.id
                },
                resultField:'records',
                fieldNames:{
                  children:'children',
                  title:'nickName',
                  key:'id'
                }
              }
            ]
          // tabs:[
          //     { 
          //       title:'根据组织选人',
          //       type:'deptUser',
          //       api:getMyDeptTreeList,
          //       asyncTree:true,
          //       asyncApi:getUserList,
          //       asyncFieldNames:{
          //         children:'children',
          //         title:'username',
          //         key:'id'
          //       },
          //       fieldNames:{
          //         children:'children',
          //         title:'departName',
          //         key:'id'
          //       }
          //     },{
          //       title:'根据角色选人',
          //       type:'roleUser',
          //       api:getRoleTreeList,
          //       asyncTree:true,
          //       asyncApi:getUserByRoleId,
          //       asyncFieldNames:{
          //         children:'children',
          //         title:'username',
          //         key:'id'
          //       },
          //       fieldNames:{
          //         children:'children',
          //         title:'name',
          //         key:'id'
          //       }
          //     }
          //   ]
        })
      }

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys,'-----keys-----');
        checkedKeys.value = selectedRowKeys;
      }

      function handleEdit(record: Recordable) {
        openDeptDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        console.log(record);
      }

      function handleSuccess() {
        reload();
      }

      function onFetchSuccess() {
        // 默认展开所有表项
        // nextTick(expandAll);
      }
      async function handleTreeSuccess(list) {
        let userIds:string[] = [];
        list.map( item => {
          userIds.push(item.id)
        })
        let result = reactive({})
          // 设置主管
          let params = {
              id:unref(recordId),
              userIds
          }
          result = await editDept(params);
          if(result){
            msg.success('操作成功');
            closeModal()
          }
      }
      async function handleExpand(expanded,record){
        if(!expanded) return;
        let result = await getDeptLevel({id:record.id})
        if(result){
          result.map(item=>{
            item.children = item.childCount?[]:null;
          })
          record.children = result;
        }
      }

      return {
        hasPermission,
        registerTable,
        registerDeptDrawer,
        registerDeptBatchDrawer,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
        handleDeptMenu,
        onRowClick,
        handleDeptManager,
        handleBatchEdit,
        handleBatchDelete,
        registerModal,
        handleTreeSuccess,
        checkedKeys,
        onSelectChange,
        handleExpand
      };
    },
  });
</script>
<style lang="less" scoped>
.resrun-basic-table{
  margin-left:0;
}
</style>

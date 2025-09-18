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
      <div class="tab-table-btn">
          <a-button @click="handleRefresh"><template #icon><SyncOutlined /></template></a-button>
          <a-button type="primary" @click="handleAddUser" v-if="hasPermission(['authgroup:addrole'])"><template #icon><PlusOutlined  /></template>添加角色</a-button>
          <a-button type="danger" @click="handleDeleteAuthUser" v-if="hasPermission(['authgroup:deleterole'])">删除</a-button>
          <a-button @click="setSelectedRowKeyList">全选</a-button>
          <a-button @click="clearSelect">全不选</a-button>
      </div>
      <BasicTable ref="tableRef" @register="registerTable"  :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
        <template #name="{text,record}">
          <Icon  icon="ant-design:user-outlined" v-if="record.authType=='user'"/>
          <Icon  icon="ant-design:apartment-outlined" v-if="record.authType=='dept'"/>
          <Icon  icon="ant-design:usergroup-add-outlined" v-if="record.authType=='role'"/>
          <span style="margin-left:10px">{{text}}</span>
        </template>
      </BasicTable>
  </div>
</template>
<script lang='ts'>
import { defineComponent,computed,ref, unref } from 'vue';
import { roleColumns } from '../data';
import { SyncOutlined, PlusOutlined} from '@ant-design/icons-vue';
import { BasicTable,useTable, TableActionType } from '/@/components/Table';
import { Icon} from '/@/components/Icon';
import { useMessage } from '/@/hooks/web/useMessage';
import {deleteAuthRoleData} from '/@/api/auth/group';
import { usePermission } from '/@/hooks/web/usePermission';

export default defineComponent({
  name: 'TabList',
  components:{
    BasicTable,
    Icon,
    PlusOutlined,
    SyncOutlined
  },
  props:{
    api:{ type: Function as PropType<() => Promise<void>> },
    groupId:String

  },
  setup(props,{emit}){

    const dataApi = computed(()=>props.api);
    const { groupId } = props;
    const tableRef = ref<Nullable<TableActionType>>(null);
    const checkedKeys = ref<Array<string | number>>([]);
    const { createMessage: msg } = useMessage();
    const { hasPermission } = usePermission();
    const [registerTable,{reload, getDataSource}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: dataApi,
        columns:roleColumns,
        immediate:false,
        useSearchForm: false,
        rowKey:'id',
        fetchSetting:{
          listField:'records'
        },
        searchInfo:{
          groupId:props.groupId
        },
        showIndexColumn: false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        pagination:true,
        tableSetting: { fullScreen: false ,redo:false},
    })

    function fetchData(){
      reload({searchInfo:{groupId:unref(groupId)}});
      return;
    }

    function getTableAction() {
        const tableAction = unref(tableRef);
        if (!tableAction) {
          throw new Error('tableAction is null');
        }
        return tableAction;
    }

    function clearSelect() {
        getTableAction().clearSelectedRowKeys();
    }


    function onSelectChange(selectedRowKeys: (string | number)[]) {
          checkedKeys.value = selectedRowKeys;
    }
    function setSelectedRowKeyList() {
        let data = getDataSource();
        let dataKey:string[] = [];
        data.map(item=>{
          dataKey.push(item.id)
        })
        getTableAction().setSelectedRowKeys(dataKey);
    }
    function handleRefresh(){
       reload({searchInfo:{groupId:unref(groupId)}});
    }
    function handleAddUser(){
      emit('handleAddUser')
    }
    async function handleDeleteAuthUser(){
       if(!unref(checkedKeys).length){
          msg.warning('请选择操作数据');
          return;
        }
        let params = {
          ids:unref(checkedKeys).join(',')
        }
        let result = await deleteAuthRoleData(params);
        if(result){
          msg.success('操作成功');
          reload({searchInfo:{groupId:unref(groupId)}});
          // checkedKeys.value = [];
          // closeDrawer();
          // emit('success');
        }
    }
    return {
      setSelectedRowKeyList,
      handleRefresh,
      handleAddUser,
      handleDeleteAuthUser,
      registerTable,
      tableRef,
      checkedKeys,
      onSelectChange,
      clearSelect,
      fetchData,
      hasPermission
    }
  }
})
</script>
<style lang="less" scoped>
  .tab-table-btn{
    .ant-btn{
      margin:0 5px;
    }
  }
</style>

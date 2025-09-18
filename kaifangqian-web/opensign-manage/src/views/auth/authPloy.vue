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
  <div class="resrun-tree-table">
    <BasicTree
      ref="basicTree"
      title=""
      :toolbar="false"
      :checkable="false"
      :fieldNames="{ title: 'name', key: 'id' }"
      :search="false"
      :isRemoteSearch="true"
      :toggleSwitch="true"
      :treeData="treeData"
      @changePanelShow="changePanelShow"
      @select="onTreeSelect"
    >
    </BasicTree>
    <BasicTable @register="registerTable"   :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, getCheckboxProps:getCheckboxProps, onChange: onSelectChange }">
      <template #toolbar>
          <a-button type="primary" @click="handlerPloy" :disabled="!permissionId" v-if="hasPermission(['authploy:add'])"> 新建 </a-button>
          <a-button type="danger" @click="handleBatchDelete" v-if="checkedKeys.length&&hasPermission(['authploy:delete'])"> 删除 </a-button>
      </template>
      <template #dataName="{text,record}">
          <Icon icon="ant-design:safety-certificate-outlined" color="#ea7270" />
          <span style="margin:0 10px">{{text}}</span>
          <span v-if="record.systemFlag===1" style="color:#E34D59">(默认)</span>
      </template>
      <template #action="{ record }">
          <a-button type="link" :disabled="record.systemFlag===1" size="small" @click="onRowClick(record)" >编辑</a-button>
      </template>
    </BasicTable>
    <PloyDrawer @register="registerDrawer" @success="handleSuccess"/>
  </div>
</template>
<script lang='ts'>
import { defineComponent,ref,onMounted, unref } from 'vue'
import { BasicTable,useTable,EditRecordRow } from '/@/components/Table';
import { BasicTree,TreeItem} from '/@/components/Tree/index';
import { Icon} from '/@/components/Icon';
import {getAuthData, getAllMenuBtnList, deleteAuthData, getAllMenuandAuthTree } from '/@/api/auth/group';
import { useMessage } from '/@/hooks/web/useMessage';
import {eachTree } from '/@/utils/helper/treeHelper';
import { useDrawer } from '/@/components/Drawer';
import PloyDrawer from './drawer/PloyDrawer.vue'
import {authColumns } from './data';
import { usePermission } from '/@/hooks/web/usePermission';

export default defineComponent({
  name: '数据权限策略管理',
  props:{
      appId:{
        type:String
      }
    },
  components: { 
      BasicTable,
      BasicTree,
      PloyDrawer,
      Icon
  },
  setup(props,{emit}){
    const treeData = ref<TreeItem[]>([]);
    const appId = ref(props.appId);

    const { createMessage: msg } = useMessage();

    const checkedKeys = ref<Array<string | number>>([]);
    const permissionId = ref('');
    const { hasPermission } = usePermission();
    const [registerDrawer, { openDrawer}] = useDrawer();
    const [registerTable,{reload,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAuthData,
        columns:authColumns,
        immediate:false,
        fetchSetting:{
          listField:'records'
        },
        rowKey:'id',
        useSearchForm: false,
        showIndexColumn: true,
        canResize: false,
        isTriggerSelect:false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        pagination:true
    });
    async function fetch() {
      let result =  (await getAllMenuandAuthTree({tenantAppId:unref(appId)})) as unknown as TreeItem[];
      if(result){
        treeData.value = result;
        treeData.value.map(item=>{
          item.disabled = true;
          item.name = item.appName;
          item.id = item.appId;
          item.children = item.permissions;
        })
        console.log( treeData.value)
        eachTree( treeData.value,(item, _parent)=>{
            if(item.menuType===2){
              item.icon = 'ant-design:safety-certificate-outlined';
              item.color = '#E34D59';
            }else{
              // item.disabled = true;
              item.icon = '';
              item.disableCheckbox = true;
            }
        })
      }
    }
    onMounted(() => {
      fetch();
    });
    async function handleBatchDelete(){
        if(!unref(checkedKeys).length){
          msg.warning('请勾选要删除的数据');
          return;
        }
        let params = {
          id:unref(checkedKeys).join(',')
        }
        let result = await deleteAuthData(params);
        if(result){
          msg.success('删除成功');
          reload({searchInfo:{permissionId:unref(permissionId)}});
          checkedKeys.value = [];
        } 
    }
    function handlerPloy(){
      openDrawer(true,{
        isUpdate:false,
        record:{
           permissionId:unref(permissionId)
        }
      })
    }
    function onTreeSelect(key,e){
      if(!key.length) return;
      permissionId.value = key[0];
      reload({searchInfo:{permissionId:key[0]}});
      setProps({
        searchInfo:{permissionId:key[0]}
      });
    }
    function changePanelShow(val){
        emit('changePanelShow',val)
    }
    function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
    }
    function onRowClick(record: EditRecordRow){
      if(!hasPermission(['authploy:edit'])){
        msg.warning('无此操作权限')
        return 
      }
      if(record.systemFlag===1){
        return;
      }
      openDrawer(true,{
        isUpdate:true,
        record:{
          ...record,
        }
      })
    }
    function getCheckboxProps(record){
      return {
        disabled:record.systemFlag === 1
      }

    }
    function handleSuccess(){
       reload({searchInfo:{permissionId:unref(permissionId)}});
    }
    return {
        treeData,
        hasPermission,
        changePanelShow,
        registerTable,
        onSelectChange,
        onTreeSelect,
        onRowClick,
        checkedKeys,
        handlerPloy,
        handleSuccess,
        registerDrawer,
        permissionId,
        handleBatchDelete,
        getCheckboxProps
    }
  }
})
</script>
<style>
 
</style>
 
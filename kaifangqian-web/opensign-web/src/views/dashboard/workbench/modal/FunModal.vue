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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit"  :destroyOnClose="true"
      >
      <!-- <a-alert message="如一个应用包内无任何应用或门户，则该应用不可勾选" type="info" />  -->
      <div class="app-fun-area">
        <div class="tree-area">
          <p class="select-title">选择快捷操作</p>
            <a-tree
                v-model:expandedKeys="expandedKeys"
                v-model:selectedKeys="selectedKeys"
                v-model:checkedKeys="checkedKeys"
                :tree-data="treeData"
                checkable
                :checkStrictly="true"
                @check="handleCheck"
                :fieldNames="{
                  title:'name',
                  key:'id'
                }"
              >
                <template #title="{ title }">
                  <span >{{ title }}</span>
                </template>
              </a-tree>
          </div>
          <div class="select-area">
            <p class="select-title">已添加的快捷操作</p>
            <ul>
              <li v-for="(item,index) in selectData" :key="index" class="select-item" >
                <span>{{ item.title }}</span>
                <Icon icon="ant-design:delete-twotone" @click="handleDelete(item)"/>
              </li>
            </ul>
          </div>
      </div>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed, onMounted } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Icon } from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getAuthAppAndMenu } from '/@/api/auth/group';
  import { getMenuList, setAppFastUpdate, getAppFastList , getMyAppsAndMenuList} from '/@/api/demo/system';
  import { TreeItem } from '/@/components/Tree/index';
  import { cloneDeep } from "lodash-es";


  interface PermissionItem{
    appId:string;
    permissionId:string;
  }
  export default defineComponent({
    name: 'FunModal',
    components:{
      BasicModal,
      Icon
    },
    setup(_, { emit }){


      const isUpdate = ref(true);
      const selectData = ref<Options[]>([]);
      const checkedKeys = ref<Array<string | number>>([]);
      const expandedKeys = ref<Array<string | number>>([]);
      const selectedKeys = ref<Array<string | number>>([]);
      const treeData = ref<TreeItem[]>([]);


      const { createMessage: msg } = useMessage();
    

      const [registerModal, { setModalProps }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1000,
          cancelText:'关闭',
          minHeight:400 
        });
        isUpdate.value = !!data?.isUpdate;
        selectData.value = [];
        getSelectedFast()
        init()
      });

    
      const getTitle = computed(() => (!unref(isUpdate) ? '快捷操作管理' : '快捷操作管理'));

      onMounted(()=>{
        // init()
      })
      async function init() {
        let result =  await getMyAppsAndMenuList({});
        if(result){
          let list = cloneDeep(result);
          list.map(item=>{
            item.name = item.appName;
            item.title = item.appName;
            item.id = item.appId;
            item.disableCheckbox = true;
            treeNodeFormat(item.permissions,item.appId)

            console.log(item.permissions,'过滤后的应用菜单--')
            item.children = item.permissions.filter(item=>item.fastFlag==true) ;
          })
          console.log(list,'-  list--')
          treeData.value = list;
        }
      }
      //已选快捷功能
      async function getSelectedFast(){
        let result = await getAppFastList({});
        checkedKeys.value = [];
        expandedKeys.value = [];
        if(result){
          result.map(item=>{
            item.title = item.permissionName;
            item.id = item.permissionId;
            checkedKeys.value.push(item.permissionId)
            expandedKeys.value.push(item.appId);
          });
          expandedKeys.value = [...new Set(expandedKeys.value)]
          selectData.value = result;
        }
      }

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      async function onLoadData(treeNode){
        return new Promise((resolve: (value?: unknown) => void) => {
          setTimeout(async() => {
            if (!treeNode.children) {
              resolve();
              return;
            }
            let result = await getMenuList({appId:treeNode.id});
            treeNodeFormat(result,treeNode.appId);
            treeNode.dataRef.children = treeNode.dataRef.children.concat(result);
            treeData.value = [...treeData.value];
            resolve();
            return;
          }, 500);
        })
      }

      function treeNodeFormat(list,appId){
        list = list.filter(item=>item.fastFlag==true)
       
        list.map(item=>{
          // if(item.name == '任务调度'){
          //   debugger
          // }
          item.title = item.name;
          item.appId = appId;
          if(item.menuType==0 || !item.fastFlag){
            item.disableCheckbox = true;
          }
          if(item.children){
            item.children =  setChildren(item.children,appId)
           
          }
        })
      }

      function setChildren(list,appId){
       let children = list.filter(item=>item.fastFlag)
        children.map(item=>{
        item.appId = appId;
        item.title = item.name
       })
       return children
      }

      async function handleSubmit() {
        // if(checkedKeys.value.length>1){
        //   msg.warning('只能选择一条数据进行授权')
        //   return;
        // }
        let list:PermissionItem[] = [];
        selectData.value.map(item=>{
          list.push({
            appId:item.appId,
            permissionId:item.id
          })
        })
        try {
          let result = await setAppFastUpdate({
            sysTenantAppVersionVOs:list
          })
          if(result){
            msg.success('授权成功')
            emit('success')
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

    function formatSelectOptions(keys){
      selectData.value = [];
      keys.map(key=>{
        let matchOption = findItemById(cloneDeep(treeData.value),key)
        console.log(matchOption,'匹配项目')
        if(matchOption){
          selectData.value.push(matchOption) 
        }
      })
    }

      function findItemById(arr, id) {
          for (let i = 0; i < arr.length; i++) {
            const item = arr[i];

            if (item.id === id) {
              return item;
            }

            if (item.children && item.children.length > 0) {
              const foundItem = findItemById(item.children, id);
              if (foundItem) {
                return foundItem;
              }
            }
          }

          return null;
      }

      function handleCheck(keys,e){
        console.log(keys,e,'000000000000000000')
        checkedKeys.value = keys.checked;
        formatSelectOptions(unref(checkedKeys))
      } 
      function handleDelete(row){
        let index = checkedKeys.value.indexOf(row.id);
        checkedKeys.value.splice(index,1);
        formatSelectOptions(unref(checkedKeys))
      }
     

      return { 
          registerModal,
          onLoadData,
          getTitle,
          handleSubmit,
          checkedKeys,
          onSelectChange,
          expandedKeys,
          selectedKeys,
          treeData,
          handleCheck,
          selectData,
          handleDelete
      
      };
    }
  })
</script>
<style lang="less" scoped>
.app-fun-area{
  display:flex;
  justify-content: space-between;
  margin-top:15px;
  .select-title{
    font-weight: 600;
  }
  .tree-area{
    width:50%;
    border-right: 1px solid #f5f5f5;
    padding:5px 0px;
    min-height: 380px;
  };
  .select-area{
    width:50%;
    padding:5px 15px;
    box-sizing: border-box;
    .select-item{
      cursor: pointer;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
:deep(.ant-tree-checkbox-disabled){
  .ant-tree-checkbox-inner{
    display:none;
  }
}
 
</style>

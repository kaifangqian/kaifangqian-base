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
      :toolbar="true"
      :checkable="false"
      search
      :fieldNames="{children:'children', title:'name', key:'id'}"
      :toggleSwitch="true"
      :isRemoteSearch="true"
      :treeData="treeData"
      :searchApiData="filterData"
      @searchChange="searchRole"
      :beforeRightClick="getRightMenuList"
      @rowClick="handleRowClick"
      @changePanelShow="changePanelShow"
      @loadSearchData="handleLoadSearchData"
      @refresh="handleRefresh"
      @select="onTreeSelect"
    >
      <template #action>
          <a-button type="default" @click="showRoleDrawer(true)" :class="[prefixCls+'-action']" v-if="hasPermission(['rolegroup:add'])">
            <Icon icon="ant-design:usergroup-add-outlined" />
              新增角色组 </a-button>
          <a-button type="default" @click="showRoleDrawer(false)" :class="[prefixCls+'-action']" v-if="hasPermission(['role:add'])">
           <Icon icon="ant-design:user-add-outlined"/>
              新增角色</a-button>
      </template>
    </BasicTree>
        <div :class="prefixCls+'-table'">
             <div :class="prefixCls+'-custom-header'">
                <span :class="prefixCls+'-custom-title'">{{roleName}}</span>
                <div :class="prefixCls+'-custom-btn'">
                    <a href="javascript:;"  v-if="hasPermission(['rolegroup:edit']) && isRoleGroup" @click="onEditRoleGroup">
                      <Icon icon="ant-design:usergroup-add-outlined"/>
                      编辑角色组
                    </a>
                    <a href="javascript:;"  v-if="hasPermission(['role:edit']) && !isRoleGroup"  @click="onEditRole">
                      <Icon icon="ant-design:user-outlined"/>
                      编辑角色
                    </a>
                </div>   
            </div>
            <BasicTable @register="registerTable" @edit-change="onEditChange"
            :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
              <template #realname="{text,record}">
                <div class="realname-content">
                  <!-- <div class="avatar-img">
                    <img :src="record.avatarBase64" alt=""  v-if="record.avatarBase64"/>
                    <span v-else>{{text}}</span>
                  </div> -->
                  <span style="width: 50px;">{{text}}</span>
                  <a-tag color="blue" v-if="record.manageFlag">主管</a-tag>
                </div>
              </template>
              <template #toolbar>
                <a-button type="primary" @click="showUserModal" :disabled="isRoleGroup" v-if="hasPermission(['role:adduser'])">添加人员 </a-button>
                <a-button type="default" @click="handleDeleteUser" :disabled="isRoleGroup" v-if="hasPermission(['role:removeuser'])">移出人员 </a-button>
              </template>
              <template #status="{text}">
                  <div>
                    {{text==1?'激活':'禁用'}}
                  </div>
              </template>
              <template #action="{ record }">
                  <a-button type="link"  size="small" @click="onRowClick(record)">编辑</a-button>
              </template>
            </BasicTable>
        </div>
    <!-- <DeptDrawer @register="registerDeptDrawer" @success="handleSuccess" />
    <UserDrawer @register="registerUserDrawer" @success="handleSuccess" /> -->
    <UserDrawer @register="registerUserDrawer" @success="handleSuccess" />
    <RoleDrawer @register="registerRoleDrawer" @success="handleSuccess" />
    <TabTreeModal @register="registerUserModal" @success="handleAddUserSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, h ,ref,onMounted,toRaw,unref } from 'vue';
  import { Row, Col, Divider } from 'ant-design-vue';
  import { BasicTree, TreeActionItem, ContextMenuItem,TreeItem} from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { roleColumns } from './data';
  import { EllipsisOutlined } from '@ant-design/icons-vue';
  import { getRoleList, getRoleTreeList,getMyRoleTreeList, getUserByRoleId, removeUserBatch, addUserToRole } from '/@/api/sys/role'; 
  import { getRoleByKeywordList,getUserByKeywordList} from '/@/api/demo/system'; 
  import { getMyDeptTreeList, } from '/@/api/sys/dept';
  import { getUserList } from '/@/api/demo/system'; 
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { useDrawer } from '/@/components/Drawer';
  import DeptDrawer from './drawer/DeptDrawer.vue';
  import UserDrawer from './drawer/UserDrawer.vue';
  import RoleDrawer from './drawer/RoleDrawer.vue';
  import TabTreeModal from './modal/TabTreeModal.vue';

  import {
    BasicTable,
    useTable,
    TableAction,
    EditRecordRow,
  } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';

  interface ResultChild {
    icon:string;
    title:string;
  }

  interface ResultItem {
    total:number;
    page:number;
    showMore:boolean;
    list:Array<ResultChild>,
    groupName?: string;
    type?:string;
    key?: string;
  }

  export default defineComponent({
    name:'Role',
    components: { BasicTree, Row, Col,BasicTable,TableAction,Divider,Icon,DeptDrawer,UserDrawer,RoleDrawer,TabTreeModal},
    emits:['changePanelShow'],
    setup(_,{emit}) {
      const { prefixCls } = useDesign('organize');
      const { createMessage: msg } = useMessage();
      const currentEditKeyRef = ref('');
      const roleName = ref();
      const isRoleGroup = ref(false);
      const filterData = ref<ResultItem[]>([]);
      const searchText = ref('');
      // const [registerDeptDrawer, { openDrawer:openDeptDrawer }] = useDrawer();
      const [registerUserDrawer, { openDrawer:openUserDrawer }] = useDrawer();
      const [registerRoleDrawer, { openDrawer:openRoleDrawer }] = useDrawer();
      const { hasPermission } = usePermission();
      const currentSelectedId = ref();  //角色、角色组id

      const treeData = ref<TreeItem[]>([]);
      const panelShow = ref(true);
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerUserModal, { openModal,closeModal }] = useModal();
      const [registerTable,{ reload,setProps }] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getUserByRoleId,
        immediate:false,
        columns: roleColumns,
        rowKey:'id',
        useSearchForm: false,
        showIndexColumn: false,
        isTriggerSelect:false,
        bordered: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        fetchSetting:{
          listField:'records'
        },
        showTableSetting: false,
        canResize: false,
        striped:false,
      });

      
      async function fetch() {
          treeData.value =  await getRoleTreeList();
          if( treeData.value.length){
            let params = {
                id:treeData.value[0].id
            }
            reload({searchInfo:{...params}});
            setProps({
                searchInfo:{id:treeData.value[0].id}
            });
            roleName.value =  treeData.value[0].name;
            currentSelectedId.value = treeData.value[0].id;
            isRoleGroup.value = true;
          }
      }
      function onLoadData(treeNode){
        return new Promise((resolve: (value?: unknown) => void) => {
          if (!treeNode.children) {
            resolve();
            return;
          }
          (async() => {
              let parentId = treeNode.id;
              if(!parentId){
                  resolve();
                  return;
              }
              let result = await getRoleList({groupId:parentId});
              if(result){
                result.map(item=>{
                    item.children = [];
                    item.title = item.roleName;
                    item.key = item.id;
                })
                console.log(treeNode,'33333')
                treeNode.dataRef.children = treeNode.dataRef.children.concat(result);
                treeNode.children = result;
              }else{
                treeNode.dataRef.children = [];
              }
              treeData.value = [...treeData.value];
              resolve();
              return;
          })();
        })
      }

      onMounted(() => {
        fetch();
      });
      async function searchRole(val){
        console.log(val,'搜索----')
        searchText.value = val;
        let roleResult = await getRoleByKeywordList({keyWord:val,pageSize:5});
        // let userResult = await getUserByKeywordList({keyWord:val,pageSize:5});
        
        filterData.value =  [
          {
            groupName:'角色',
            list:roleResult.records,
            total:roleResult.total,
            showMore:roleResult.total>5?true:false,
            page:roleResult.current,
            type:'role'
          },
          // {
          //   groupName:'成员',
          //   list:userResult.records,
          //   total:userResult.total,
          //   showMore:userResult.total>5?true:false,
          //   page:userResult.current,
          //   type:'user'
          // },
        ]
      }
      async function handleLoadSearchData(type,page){
        if(type==='user'){
          let result = await getUserByKeywordList({keyWord:unref(searchText),pageSize:5*page});
          filterData.value[1].list = result.records;
          filterData.value[1].page = result.size/5 ;
          filterData.value[1].showMore = (result.total>result.pages)?true:false;
        }
        if(type==='role'){
          let result = await getRoleByKeywordList({keyWord:unref(searchText),pageSize:5*page});
          filterData.value[2].list = result.records;
          filterData.value[2].page = result.size/5 ;
          filterData.value[2].showMore = (result.total>result.pages)?true:false;
        }

      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function handleEdit(record: EditRecordRow) {
        currentEditKeyRef.value = record.key;
        record.onEdit?.(true);
      }
     
      function handleCreateUser(){

      }
      function onRowClick(record: EditRecordRow) {
        openUserDrawer(true,{
          isUpdate:true,
          record:{
              ...record,
              id:record.userId
          }
        })
      }
      function onTreeSelect(keys,e){
        roleName.value = e.node.title;
        currentSelectedId.value = keys[0];
        //角色组下不可添加人员
        if(!e.node.parentId){
          isRoleGroup.value = true;
          return;
        }
        isRoleGroup.value = false;
        if(!keys.length) return;
        // currentSelectedId.value = keys[0];
        setProps({
            searchInfo:{id: keys[0]}
        });
        reload({searchInfo:{id:keys[0]}});
      }
      async function handleDeleteUser(){
        if(!unref(checkedKeys).length){
          msg.warning('请选择要删除的数据');
          return;
        }
        let params = {
          ids:unref(checkedKeys).join(',')
        }
        let result = await removeUserBatch(params);
        if(result){
          msg.success('移除成功');
          reload({searchInfo:{id:unref(currentSelectedId)}});
          checkedKeys.value = [];
        } 
      }
      function changePanelShow(val){
         emit('changePanelShow',val)
      }
      function handleRowClick(row,type){
        if(type==='user'){
          if(!hasPermission(['user:edit'])) {
              msg.warning('暂无权限')
              return
          }
           openUserDrawer(true,{
              isUpdate:true,
              record:{
                  ...toRaw(row)
              }
            })
        }
        if(type==='role'){
           if(!hasPermission(['role:edit'])) {
            msg.warning('暂无权限')
            return
          }
          openRoleDrawer(true,{
              isUpdate:true,
              record:{
                  ...toRaw(row)
              }
            })
        }

      }

      

      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }

      // function showDeptDrawer(){
      //   openDeptDrawer(true, {
      //     isUpdate: false,
      //   });
      // }
      function showRoleDrawer(roleGroupFlag?:boolean){
        openRoleDrawer(true, {
          isUpdate: false,
          isRoleGroup:roleGroupFlag
        });
      }
      function showUserModal(){
         openModal(true,{
            isUpdate:true,
            title:'添加人员',
            tabs:[
              { 
                title:'根据组织选人',
                type:'deptUser',
                api:getMyDeptTreeList,
                asyncTree:true,
                asyncApi:getUserList,
                asyncFieldNames:{
                  children:'children',
                  title:'realname',
                  key:'id'
                },
                fieldNames:{
                  children:'children',
                  title:'departName',
                  key:'id'
                }
              },{
                title:'根据角色选人',
                type:'roleUser',
                api:getRoleTreeList,
                asyncTree:true,
                asyncApi:getUserByRoleId,
                asyncFieldNames:{
                  children:'children',
                  title:'realname',
                  key:'id'
                },
                fieldNames:{
                  children:'children',
                  title:'name',
                  key:'id'
                }
              }
            ]
         })
      }

      function showMenu(node: any) {
        console.log(node);
        // basicTree.value.handleRightClick(node)
        getRightMenuList(node)
      }
      function onEditRoleGroup(){
        openRoleDrawer(true, {
            isUpdate: true,
            isRoleGroup:true,
            record:{
              id:unref(currentSelectedId)
            }
        });
      }
      function onEditRole(){
        openRoleDrawer(true, {
            record:{
              id:unref(currentSelectedId)
            },
            isUpdate:true
          })
      }

      function getRightMenuList(node: any): ContextMenuItem[] {
        if(!node.parentId) return [
          {
            label:'编辑角色组',
            handler: ()=>{
              openRoleDrawer(true, {
                isUpdate: true,
                isRoleGroup:true,
                record:{
                  id:node.id
                }
              });
            },
            auth:['rolegroup:edit'],
            // icon: 'ant-design:edit-outlined',
          }
        ];
        return [
          {
            label: '编辑角色',
            handler: () => {
              openRoleDrawer(true, {
                record:{
                  ...toRaw(node)
                },
                isUpdate:true
              })
            },
            auth:['role:edit'],
            // icon: 'ant-design:edit-outlined',
          }
        ];
      }
      const actionList: TreeActionItem[] = [
        {
          render: (node) => {
            return h(EllipsisOutlined, {
              class: 'ml-2',
              onClick: () => {
                showMenu(node);
              },
            });
          },
        },
      ];
      function handleSuccess() {
          fetch();
      }
      async function handleAddUserSuccess(list){
          let userIds:string[] = [];
          list.map( item => {
            userIds.push(item.id)
          })
          let params = {
            roleId:unref(currentSelectedId),
            userIds
          }
          let result = await addUserToRole(params);
          if(result){
            msg.success('添加成功');
            closeModal();
            reload({searchInfo:{id:unref(currentSelectedId)}});
          }
      }
      function handleRefresh(){
        fetch()
      }
      function createIcon({ level }) {
        if (level === 1) {
          return 'ion:git-compare-outline';
        }
        if (level === 2) {
          return 'ion:home';
        }
        if (level === 3) {
          return 'ion:airplane';
        }
        return '';
      }
      return { 
        treeData,
        filterData,
        searchRole,
        checkedKeys,
        actionList, 
        getRightMenuList,
        onRowClick,
        registerUserModal, 
        createIcon ,
        hasPermission,
        prefixCls,
        registerUserDrawer,
        roleName,
        changePanelShow,
        handleSuccess,
        registerTable,
        handleEdit,
        panelShow,
        onEditChange,
        onSelectChange,
        handleCreateUser,
        showUserModal,
        registerRoleDrawer,
        onTreeSelect,
        handleDeleteUser,
        showRoleDrawer,
        handleAddUserSuccess,
        handleRefresh,
        isRoleGroup,
        onLoadData,
        handleRowClick,
        handleLoadSearchData,
        onEditRoleGroup,
        onEditRole
      }
    },
  });
</script>
<style lang="less" scoped>
 @prefix-cls: ~'@{namespace}-organize';
.@{prefix-cls} {
  &.panelHide{
    .ant-tabs-nav{
      margin-left:10px;
      transition: all 0.4s;
    }
  }
  &-table{
    flex:1;
  }
  &-custom-header{
    height: 71px;
    background: #fff;
    padding: 2px 15px;
    // margin-left: 10px;
    /* margin-bottom: 10px; */
    border-bottom: 1px solid #f6f6f6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .ant-divider{
      color:#1890ff
    }
  }
  &-custom-title{
    font-size: 14px;
    font-weight: 600;
  }
  &-custom-btn a{
    font-size: 14px;
  }
  .ant-tabs-nav{
    margin:0;
    background-color: #fff;
    padding:0 15px;
  }
}
.realname-content{
    display: flex;
    justify-content: left;
    margin-left: 10px;
    align-items: center;
    .avatar-img{
      width: 40px;
      height: 40px;
      background: #127fd2;
      border-radius: 5px;
      color: #fff;
      white-space: nowrap;
      display: flex;
      justify-content: center;
      align-items: center;
      box-sizing: border-box;
      padding: 0 5px;
      span{
        overflow: hidden;
      }
    }
  }
.resrun-tree-table{
  display: flex;
  height:100%;
}
// .resrun-tree{
//   flex: 0 0 250px
// }
.resrun-basic-table{
  flex: auto;
  margin-left: 10px;
}

</style>

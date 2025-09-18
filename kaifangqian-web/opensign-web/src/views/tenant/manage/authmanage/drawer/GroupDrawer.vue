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
    width="70%"
    @ok="handleSubmit"
    >
      <Tabs v-model:value="tabValue" @change="handleTabChange">
        <TabPane :key="1" tab="基本信息">
            <BasicForm @register="registerForm" />
        </TabPane>
        <TabPane :key="2" tab="成员管理" v-if="isUpdate">
            <div class="tab-table-btn">
                <a-button @click="handleRefresh"><template #icon><SyncOutlined /></template></a-button>
                <a-button type="primary" @click="handleAddUser" v-if="hasPermission(['authgroup:addmember'])"><template #icon><PlusOutlined /></template>添加成员</a-button>
                <a-button type="danger" @click="handleDeleteAuthUser" v-if="hasPermission(['authgroup:deletemember'])">删除</a-button>
                <a-button @click="setSelectedRowKeyList">全选</a-button>
                <a-button @click="clearSelect">全不选</a-button>
            </div>
            <BasicTable ref="tableRef" @register="registerTable"  :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
              <template #name="{text,record}">
                <Icon  icon="ant-design:user-outlined" v-if="record.authType=='user'"/>
                <Icon  icon="ant-design:apartment-outlined" v-if="record.authType=='dept'"/>
                <Icon  icon="ant-design:usergroup-add-outlined" v-if="record.authType=='role'"/>
                <span style="margin-left:10px">{{text + (record.userDepartName?  ' ' + '('+  record.userDepartName +')':'')}}</span>
              </template>
            </BasicTable>
        </TabPane>
        <TabPane :key="3" tab="权限" v-if="isUpdate">
          <div class="resrun-tree-table">
                <BasicTree
                    ref="basicTree"
                    title=""
                    :toolbar="false"
                    :toggleSwitch="false"
                    :checkable="true"
                    :selectedKeys="selectedKeys"
                    :checkedKeys="treeCheckedKeys"
                    :search="false"
                    :fieldNames="{ title: 'name', key: 'id' }"
                    :treeData="treeData"
                    :checkStrictly="true"
                    :checkStrictlySwitch="true"
                    @select="onTreeSelect"
                >
                </BasicTree>
                <BasicTable @register="registerAuthTable"  :rowSelection="{ type: 'checkbox',selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
                  <template #dataName="{text,record}">
                    <Icon icon="ant-design:safety-certificate-outlined" color="#ea7270" />
                    <span>{{text}}</span>
                    <span v-if="record.defaultFlag===1">(默认)</span>
                  </template>
                </BasicTable>
          </div>
        </TabPane>
        <TabPane :key="4" tab="角色授权管理" v-if="isUpdate">
           <TabList ref="roleTabRef" :groupId="recordId" :api="getAuthRoleData" @handleAddUser="handleRoleUser" />
        </TabPane>
      </Tabs>
      <TabTreeModal @register="registerModal" @success="handleSuccess" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, onMounted,nextTick, toRaw } from 'vue';
  import { Tabs,Collapse,  } from 'ant-design-vue';
  import { SyncOutlined, PlusOutlined} from '@ant-design/icons-vue';
  import { treeFormSchema,userColumns,authColumns } from '../data';
  import { BasicTable,useTable, TableActionType } from '/@/components/Table';
  import { BasicTree,TreeItem,TreeActionType} from '/@/components/Tree/index';
  import { BasicCollapseTree } from '/@/components/CollapseTree';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useModal } from '/@/components/Modal';
  import { Icon} from '/@/components/Icon';
  import {eachTree } from '/@/utils/helper/treeHelper';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addAuth,editAuth,deleAuthMember,addAuthRoleData, getAuthGroupMember,addAuthMember,getAllMenuBtnList,getAuthData,getAuthDataChecked,setAuthPermission,getAuthRoleData} from '/@/api/auth/group'; 
  import { getUserList } from '/@/api/demo/system'; 
  import { getDeptNoCountTree } from '/@/api/sys/dept'; 
  import { usePermission } from '/@/hooks/web/usePermission';
  import { getRoleTreeList,getUserByRoleId,  } from '/@/api/sys/role'; 
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import TabList from './TabList.vue';
// import { filter } from '../../../utils/helper/treeHelper';
  interface AuthItem {
      authType:string;
      authId:string;
      departId?:string;
      type?:string;
  };
  interface AuthRoleItem {
      authType:string;
      authId:string;
      departId?:string;
      type?:string;
  };
  interface PerItem {
    ruleIds:Array<string|number>
  }
  interface RuleItem {
      permissionId:string;
      ruleIds:Array<string|number>
  }
  interface ActionType {
  fetchData:Function 
 }
  export default defineComponent({
    name: 'AuthGroupDrawer',
    components: { 
      TabTreeModal,
      BasicDrawer, BasicForm, 
      Tabs,  
      TabPane: Tabs.TabPane,
      BasicTable,
      Collapse,
      CollapsePanel:Collapse.Panel,SyncOutlined,PlusOutlined,Icon,
      BasicCollapseTree,
      BasicTree,
      TabList
    },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const treeData = ref<TreeItem[]>([]);
      const recordId = ref('');
      const authList = ref([]);
      const tabValue = ref<number| string>(1);
      const checkedKeys = ref<Array<string | number>>([]);
      const selectedKeys = ref<Array<string | number>>([]);
      const treeCheckedKeys = ref<Array<string | number>>([]);
      const ruleVOS = ref<RuleItem[]>([]);
      const currentPermissionId = ref('');
      const basicTree = ref<Nullable<TreeActionType>>(null);
      const tableRef = ref<Nullable<TableActionType>>(null);
      const roleTabRef = ref<Nullable<ActionType>>(null);
      const treeType = ref('');

      const { createMessage: msg } = useMessage();
      const { hasPermission } = usePermission();

      function getTableAction() {
        const tableAction = unref(tableRef);
        if (!tableAction) {
          throw new Error('tableAction is null');
        }
        return tableAction;
      }
      function setSelectedRowKeyList() {
        let data = getDataSource();
        let dataKey:string[] = [];
        data.map(item=>{
          dataKey.push(item.id)
        })
        getTableAction().setSelectedRowKeys(dataKey);
      }
      function clearSelect() {
        getTableAction().clearSelectedRowKeys();
      }
      async function fetch() {
        treeData.value =  (await getAllMenuBtnList({})) as unknown as TreeItem[];
        eachTree( treeData.value,(item, _parent)=>{
            if(item.menuType===2){
              item.icon = 'ant-design:safety-certificate-outlined';
              item.color = '#E34D59';
            }else{
              item.icon = '';
              // item.disabled = true;
              // item.disableCheckbox = true;
            }
        })
      }
      function getTree() {
        const tree = unref(basicTree);
        if (!tree) {
          throw new Error('tree is null!');
        }
        return tree;
      }
      onMounted(() => {
        // fetch();
      })
      const [registerModal, { openModal,closeModal }] = useModal();
      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: treeFormSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });

      const [registerTable,{reload, setProps:setMemberProps,getDataSource}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAuthGroupMember,
        columns:userColumns,
        immediate:false,
        useSearchForm: false,
        rowKey:'id',
        fetchSetting:{
          listField:'records'
        },
        beforeFetch:handleFetch,
        showIndexColumn: false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        pagination:true,
        tableSetting: { fullScreen: false ,redo:false},
      })
      const [registerAuthTable,{reload:reloadAuth,setProps,setSelectedRowKeys}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAuthData,
        columns:authColumns,
        fetchSetting:{
          listField:'records'
        },
        rowKey:'id',
        immediate:false,
        useSearchForm: false,
        showIndexColumn: false,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        pagination:true
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        fetch();
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '编辑权限组' : '编辑权限组'));
      function handleRoleUser(){
        treeType.value = 'role';
        openModal(true,{
            isupDate:false,
            title:'添加角色',
            tabs:[
                { 
                  title:'角色',
                  type:'role',
                  onlyRole:true,
                  api:getRoleTreeList,
                  asyncTree:true,
                  // asyncApi:getRoleList,
                  // asyncTree:true,
                  fieldNames:{
                    children:'children',
                    title:'name',
                    key:'id'
                  }
                },
              ]
        })
      }
      function handleFetch(){
        setMemberProps({
             searchInfo:{groupId:unref(recordId)}
        })
      }
      function handleAddUser(){
        treeType.value = 'user';
        openModal(true,{
          isupDate:false,
          title:'添加成员',
          tabs:[
              { 
                title:'组织',
                type:'dept',
                api:getDeptNoCountTree,
                asyncTree:true,
                asyncFieldNames:{
                  title:'name',
                  key:'id'
                },
                fieldNames:{
                  title:'departName',
                  key:'id'
                }
              },
              { 
                title:'角色',
                type:'role',
                api:getRoleTreeList,
                asyncTree:true,
                fieldNames:{
                  children:'children',
                  title:'name',
                  key:'id'
                }
              },
              { 
                title:'根据组织选人',
                type:'deptUser',
                api:getDeptNoCountTree,
                asyncTree:true,
                asyncApi:getUserList,
                asyncFieldNames:{
                  title:'username',
                  key:'id'
                },
                fieldNames:{
                  children:'children',
                  title:'departName',
                  key:'id'
                }
              },
              { 
                title:'根据角色选人',
                type:'roleUser',
                api:getRoleTreeList,
                asyncTree:true,
                asyncApi:getUserByRoleId,
                asyncFieldNames:{
                  title:'realname',
                  key:'userId'
                },
                fieldNames:{
                  children:'children',
                  title:'name',
                  key:'id'
                }
              },
            ]
        })
      }

      function handleRefresh(){
          reload({searchInfo:{groupId:unref(recordId)}});
      }
      async function handleTabChange(val){
        tabValue.value = val;
        if(val===2){
          nextTick(()=>{
            reload({searchInfo:{groupId:unref(recordId)}});
          })
          return;
        }
        if(val===3){
          getCheckedData();
        }
        if(val===4){
          nextTick(()=>{
            const roleRef = unref(roleTabRef);
            roleRef?.fetchData();
          })
         
        }
      }
      async function authAddRole(list){
        let roleIds:Array<string> = [];
        list.map(item=>{
          roleIds.push(item.id)
        })
        let params = {
          groupId:unref(recordId),
          roleIds
        }
        let result = await addAuthRoleData(params);
        if(result){
          closeModal()
          const roleRef = unref(roleTabRef);
          roleRef?.fetchData();
        }

      } 
      async function handleSuccess(list){
        if(unref(treeType)==='role'){
          authAddRole(list);
          return 
        }
        let memberList:Array<AuthItem> = [];
        list.map(item=>{
          memberList.push({
            authType:item.authType,
            authId:item.id,
            departId:item.departId
          })
        })
        let params = {
          groupId:unref(recordId),
          memberList
        }
        let result = await addAuthMember(params);
        if(result){
          closeModal()
          msg.success(result.message);
          reload({searchInfo:{groupId:unref(recordId)}});
        }
      }
       function onSelectChange(selectedRowKeys: (string | number)[]) {
          checkedKeys.value = selectedRowKeys;
      }
      async function handleDeleteAuthUser(){
        if(!unref(checkedKeys).length){
          msg.warning('请选择操作数据');
          return;
        }
        let params = {
          ids:unref(checkedKeys).join(',')
        }
        let result = await deleAuthMember(params)
        if(result){
          msg.success('操作成功');
          reload({searchInfo:{groupId:unref(recordId)}});
          // checkedKeys.value = [];
          // closeDrawer();
          // emit('success');
        }

      }
      function onTreeSelect(key,e){
        if(!key.length) return;
        if(e.node.menuType !==2) return;
        reloadAuth({searchInfo:{permissionId:key[0]}})
        currentPermissionId.value = key[0];
        getCheckedData(key[0]);
        setProps({
          searchInfo:{permissionId:key[0]}
        });
      }
      async function getCheckedData(permissionId?:string){
        let params = {
          groupId:unref(recordId),
          permissionId
        }
        let result = await getAuthDataChecked(params);
        if(result&&permissionId){
          ruleVOS.value = result;
          //table选中默认权限
          if(result.length){
            checkedKeys.value = result[0].ruleIds;
            setSelectedRowKeys([...result[0].ruleIds]);
          }
          console.log(ruleVOS,'---默认权限-----')
        }else{
          //设置tree选中的节点
          if(!permissionId){
            treeCheckedKeys.value = [];
            result.map(item=>{
                treeCheckedKeys.value.push(item.permissionId);
            })
          }
        }
      }
       async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          let result;
          console.log(tabValue.value,2222)
          if(tabValue.value == 1){
            console.log(!unref(isUpdate),111)
            if(!unref(isUpdate)){
                result = await addAuth(values)
              }else{
                result = await editAuth({
                  ...values,
                  id:unref(recordId)
                })
            }
          }else if(tabValue.value ==3){
            const keys = toRaw(getTree().instance.getCheckedKeys());
              // 默认权限
              let defaultRuleVOS:RuleItem[] = unref(ruleVOS).filter((item)=>{
                return item.permissionId !== unref(currentPermissionId)
              });
              let filterKeys = keys.checked?keys.checked:keys; // 层级关联是
              let parentKeys = filterKeys.filter(key=>key !== unref(currentPermissionId));
              parentKeys.map(key=>{
                defaultRuleVOS.push({
                  permissionId:key,
                  ruleIds:[]
                })
              })
              defaultRuleVOS.push({
                permissionId:unref(currentPermissionId),
                ruleIds:unref(checkedKeys)
              })
              let params = {
                groupId:unref(recordId),
                ruleVOS:defaultRuleVOS
              }
              console.log(params,'------组装好的数据-----')
              result = setAuthPermission(params);
          }
        
          if(result){
            msg.success('操作成功')
            closeDrawer();
            emit('success');
          }
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }

      return { 
        hasPermission,
        handleRoleUser,
        setSelectedRowKeyList,
        clearSelect,
        handleRefresh,
        handleAddUser,
        handleSuccess,
        registerModal,
        registerDrawer, 
        registerForm,
        handleDeleteAuthUser,
        onSelectChange, 
        getTitle, 
        handleSubmit, 
        isUpdate, 
        basicTree,
        registerTable,
        registerAuthTable,
        treeData, 
        authList,
        selectedKeys,
        handleTabChange,
        treeCheckedKeys,
        onTreeSelect,
        checkedKeys,
        tabValue,
        tableRef,
        recordId,
        getAuthRoleData,
        roleTabRef 
      };
    },
  });
</script>
<style lang="less" scoped>
  .tab-table-btn{
    .ant-btn{
      margin:0 5px;
    }
  }
</style>
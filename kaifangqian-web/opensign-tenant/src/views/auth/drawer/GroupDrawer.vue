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
      <a-card title="" size="small">
        <BasicForm @register="registerForm">
          <template #parentId="{model,field}">
              <a-tree-select
                  :disabled="model['groupType'] == 1 || model['groupType']==2"
                  v-model:value="model[field]"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  placeholder="请选择"
                  allow-clear
                  :fieldNames="{ label: 'groupName',  value: 'id',}"
                  tree-default-expand-all
                  :tree-data="treeData"
                >
              </a-tree-select>
            </template>
        </BasicForm>
        <div  class="basic-btn-save">
          <a-button type="primary" @click="handleSaveBasic">保存</a-button>
        </div>
        <div class="auth-group-config-list">
            <div class="auth-group-config-item">
              <p class="config-title">权限组成员</p>
              <div>
                  <a-button    type="link" @click="handleAddUser" v-if="hasPermission(['authgroup:addmember'])">设置权限组成员</a-button>
                  <div class="config-content-area">
                      <ul>
                        <li v-for="(item,index) in memberList" :key="index">
                          <Icon icon="ant-design:partition-outlined" color="#666" v-if="item.authType === 'dept'"/>
                          <Icon icon="ant-design:user-outlined" color="#666" v-if="item.authType === 'user'"/>
                          <Icon icon="ant-design:usergroup-add-outlined" color="#666" v-if="item.authType === 'role'"/>
                          {{ item.title || item.name }}
                          <Icon icon="ant-design:close-circle-outlined" color="red" class="delete-item" @click="handleDeleteUser(item)"/>
                        </li>
                      </ul>
                  </div>
              </div>
            </div>
            <!-- <div class="auth-group-config-item">
              <p class="config-title">角色管理范围</p>
              <div>
                <a-button type="link"  @click="handleRoleUser">设置角色管理范围</a-button>
              
                <div  class="config-content-area">
                    <ul>
                        <li v-for="(item,index) in roleList" :key="index">
                          <Icon icon="ant-design:usergroup-add-outlined" color="#666" />
                          {{ item.title || item.roleName }}
                          <Icon icon="ant-design:close-circle-outlined" color="red" class="delete-item" @click="handleDeleteRoleUser(item)"/>
                        </li>
                      </ul>
                </div>
              </div>
            </div> -->
            <div class="auth-group-config-item">
              <p class="config-title">功能及数据管理权限</p>
              <div>
                <!-- <a-button type="link" @click="handleAuthConfig" :disabled="(rowInfo.groupType =='1' || rowInfo.groupType=='2')">权限配置</a-button> -->
                <a-button type="link" @click="handleAuthConfig">权限配置</a-button>
                <a-tooltip>
                    <template #title>全部权限和基础权限只能查看</template>
                    <Icon icon="ant-design:question-circle-twotone" color="#127fd2" v-if="rowInfo.groupType=='1' || rowInfo.groupType=='2'"/>
                </a-tooltip>
               <!-- <div  class="config-content-area">
                    <ul>
                        <li v-for="(item,index) in authList" :key="index">
                          <Icon icon="ant-design:safety-certificate-outlined" color="#ea7270" />
                          {{ item.title }}
                          <Icon icon="ant-design:close-circle-outlined" color="red" class="delete-item"/>
                        </li>
                      </ul>
                </div> -->
              </div>
            </div>
        </div>
      </a-card>
      <TabTreeModal @register="registerModal" @success="handleSuccess"/>
      <AuthConfigModal @register="registerAuthConfigModal" @success="handleAuthConfigSuccess" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, toRaw } from 'vue';
  import { treeFormSchema } from '../data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { TableActionType } from '/@/components/Table';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { useModal } from '/@/components/Modal';
  import { Icon} from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { addAuth,editAuth,deleAuthMember,getAuthGroup,deleteAuthRoleData,addAuthRoleData, getAuthGroupMember,addAuthMember,setAuthPermission,getAuthRoleData} from '/@/api/auth/group'; 
  import { getUserList } from '/@/api/demo/system'; 
  import { getDeptNoCountTree } from '/@/api/sys/dept'; 
  import { usePermission } from '/@/hooks/web/usePermission';
  import { getRoleTreeList,getUserByRoleId,  } from '/@/api/sys/role'; 
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import TabList from './TabList.vue';
  import AuthConfigModal from '../modal/AuthConfigModal.vue';


  interface ConfigItem {
    [key: string]:string
  }
  interface AuthItem {
      authType:string;
      authId:string;
      departId?:string;
      type?:string;
  };
  // interface AuthRoleItem {
  //     authType:string;
  //     authId:string;
  //     departId?:string;
  //     type?:string;
  // };
  // interface PerItem {
  //   ruleIds:Array<string|number>
  // }
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
      TabList,
      Icon,
      AuthConfigModal
    },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const recordId = ref('');
      const memberList = ref(<ConfigItem[]>[]);
      const roleList =  ref(<ConfigItem[]>[]);
      const authList =  ref(<ConfigItem[]>[]);
      const appId = ref('');
      const rowInfo = ref({
        groupType:''
      });
      const treeType = ref('');
      const treeData = ref([]);
      const tabValue = ref<number| string>(1);
      const checkedKeys = ref<Array<string | number>>([]);
      const selectedKeys = ref<Array<string | number>>([]);
      const treeCheckedKeys = ref<Array<string | number>>([]);
      const ruleVOS = ref<RuleItem[]>([]);
      const currentPermissionId = ref('');
      // const basicTree = ref<Nullable<TreeActionType>>(null);
      const tableRef = ref<Nullable<TableActionType>>(null);
      const roleTabRef = ref<Nullable<ActionType>>(null);

      const { createMessage: msg } = useMessage();
      const { hasPermission } = usePermission();

      function getTree() {
        // const tree = unref(basicTree);
        // if (!tree) {
        //   throw new Error('tree is null!');
        // }
        // return tree;
      }
      const [registerModal, { openModal,closeModal }] = useModal();
      const [registerAuthConfigModal, { openModal:openConfigModal,closeModal:closeConfigModal }] = useModal();
      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: treeFormSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false,showOkBtn:false,cancelText:'关闭' });
        isUpdate.value = !!data?.isUpdate;
        appId.value = data?.appId;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
          rowInfo.value = {...data.record};
          getAuthMumber();
          getAuthRole();
        
        }else{
          rowInfo.value = {groupType:''};
        }

        let result = await getAuthGroup({tenantAppId:unref(appId)});
        if(result){
          treeData.value = result;
        }
        
      });

      async function  getAuthMumber(){
          // 请求权限组成员
          let users = await getAuthGroupMember({groupId:unref(recordId),tenantAppId:unref(appId)})
          console.log(users,'-----数据结果------')
          if(users){
            memberList.value = users.records;
          }
      }
      async function getAuthRole(){
          // 请求角色管理范围成员
          let roles = await getAuthRoleData({groupId:unref(recordId),tenantAppId:unref(appId)})
          if(roles){
            roleList.value = roles.records;
          }
      }

      const getTitle = computed(() => (!unref(isUpdate) ? '编辑权限组' : '编辑权限组'));
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
                params:{
                  tenantAppId:unref(appId)
                },
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
                  title:'realname',
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
                params:{
                    tenantAppId:unref(appId)
                  },
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
      function handleRoleUser(){
        // if(unref(rowInfo).groupType=='1' || unref(rowInfo).groupType=='2'){
        //   return
        // }
        console.log("rowInfo.value:",rowInfo.value);
        treeType.value = 'role';
        openModal(true,{
            isupDate:false,
            title:'添加角色',
            // groupType:rowInfo.value.groupType,
            tabs:[
                { 
                  title:'角色',
                  type:'role',
                  onlyRole:true,
                  api:getRoleTreeList,
                  params:{
                    tenantAppId:unref(appId)
                  },
                  asyncTree:true,
                  fieldNames:{
                    children:'children',
                    title:'name',
                    key:'id'
                  }
                },
              ]
        })
      }
      function handleAuthConfig(){
        // if(unref(rowInfo).groupType=='1' || unref(rowInfo).groupType=='2'){
        //   return
        // }
        openConfigModal(true,{
            isUpdate:true,
            appId:unref(appId),
            record:{
              id:unref(recordId),
              groupType:rowInfo.value.groupType 
            }
        })
      }
     
      async function authAddRole(list){
        let roleIds:Array<string> = [];
        list.map(item=>{
          roleIds.push(item.id)
        })
        let params = {
          groupId:unref(recordId),
          roleIds,
          tenantAppId:unref(appId)
        }
        let result = await addAuthRoleData(params);
        if(result){
          closeModal();
          getAuthRole();
          // const roleRef = unref(roleTabRef);
          // roleRef?.fetchData();
        }

      } 
      async function handleSuccess(list){
        console.log(list,'选择的人员')
        if(unref(treeType)==='role'){
          roleList.value = list;
          authAddRole(list);
          return 
        }
        let users:Array<AuthItem> = [];
        list.map(item=>{
          users.push({
            authType:item.authType,
            authId:item.id,
            departId:item.departId
          })
        })
        let params = {
          groupId:unref(recordId),
          memberList:users,
          tenantAppId:unref(appId)
        }
        let result = await addAuthMember(params);
        if(result){
          closeModal();
          getAuthMumber()
          msg.success(result.message);
        }
      }
       function onSelectChange(selectedRowKeys: (string | number)[]) {
          checkedKeys.value = selectedRowKeys;
      }

      async function handleDeleteRoleUser(row){
        let params = {
          ids:row.id
        }
        let result = await deleteAuthRoleData(params);
        if(result){
          msg.success('操作成功');
          getAuthRole();
        }
      }
      async function handleDeleteUser(row){
        let params = {
          ids:row.id
        }
        let result = await deleAuthMember(params)
        if(result){
          msg.success('操作成功');
          getAuthMumber();
        }
      }
      async function handleSaveBasic(){
        const values = await validate();
        let result = await editAuth({
              ...values,
              id:unref(recordId),
              tenantAppId:unref(appId)
        });
        if(result){
          msg.success('保存成功');
        }

      }
      function handleAuthConfigSuccess(){
        closeConfigModal()
      }

       async function handleSubmit() {
        closeDrawer();
        return;
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          let result;
          console.log(tabValue.value,2222)
          if(tabValue.value == 1){
            if(!unref(isUpdate)){
                result = await addAuth({...values,tenantAppId:unref(appId)})
              }else{
                result = await editAuth({
                  ...values,
                  id:unref(recordId),
                  tenantAppId:unref(appId)
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
        rowInfo,
        registerAuthConfigModal,
        handleAuthConfig,
        handleDeleteUser,
        hasPermission,
        memberList,
        roleList,
        authList,
        treeData,
        handleRoleUser,
        handleAddUser,
        handleSuccess,
        registerModal,
        registerDrawer, 
        registerForm,
        onSelectChange, 
        getTitle, 
        handleSubmit, 
        isUpdate, 
        selectedKeys,
        treeCheckedKeys,
        checkedKeys,
        tabValue,
        tableRef,
        recordId,
        getAuthRoleData,
        roleTabRef,
        handleDeleteRoleUser,
        handleSaveBasic,
        handleAuthConfigSuccess 
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
  :deep(.auth-pane .scrollbar__wrap){
      margin-bottom: 40px;
  }
  .basic-btn-save{
    width:100%;
    text-align: right;
  }
  .auth-group-config-item{
    display:flex;
    position: relative;
    .config-title{
      width: 130px;
      text-align: right;
      margin-right:10px;
      padding: 5px 0;
    }
    .config-content-area{
      min-height: 30px;
      width: 700px;
      height: auto;
      /* background: #f7f7f7; */
      padding: 10px 15px;
      border-radius: 5px;
      /* border: 1px solid #eee; */
      margin: 10px 0;
      ul{
        display: flex;
        margin-bottom: 0;
        flex-wrap: wrap;
        li{
          margin-right: 10px;
          border: 1px solid #ddd;
          border-radius: 4px;
          padding:2px 10px;
          position: relative;
          cursor: pointer;
          margin:5px 5px;
          .delete-item{
           visibility: hidden;
            position: absolute;
            top:-5px;
            right:-7px;
          }
          &:hover{
            .delete-item{
             visibility: visible;
            }
          }
        }
      }
    }
  }
</style>

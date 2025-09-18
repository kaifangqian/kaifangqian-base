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
    :destroyOnClose="true"
    :title="getTitle"
    width="50%"
    @ok="handleSubmit"
    >
      <Tabs  @change="handleTabChange">   
        <TabPane key="roleGroup" tab="角色组信息" v-if="isRoleGroup">
          <BasicForm @register="registerRoleGroupForm" />
        </TabPane>
        <TabPane key="role" tab="基本信息" v-if="!isRoleGroup">
          <BasicForm @register="registerForm" >
            <template #parentId="{ model, field }">
              <Select v-model:value="model[field]" placeholder="请选择" > 
                  <SelectOption v-for="(item,index) in roleGroupSource" :value="item.id" :key="index">{{item.roleName}}</SelectOption>
              </Select>
            </template>
          </BasicForm>
        </TabPane>
        <TabPane key="roleAuth" tab="权限" v-if="isUpdate&&!isRoleGroup">
            <BasicTable @register="registerAuthTable" :rowSelection="{ type: 'checkbox',selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
              <template #toolbar>
                  <a-button type="primary" @click="handleAddAuth" v-if="hasPermission(['roleedit:authadd'])">添加权限</a-button>
                  <a-button type="danger" @click="handleDeleteAuth" v-if="hasPermission(['roleedit:authdelete'])">删除</a-button>
                </template>
            </BasicTable>
        </TabPane>
      </Tabs>
  </BasicDrawer>
  <AuthModal @register="registerModal" @success="handleSuccess" />
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, reactive,nextTick } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formRoleSchema,formRoleGroupSchema,roleUserColumns,authColumns } from '../data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { Tabs,Card,Select } from 'ant-design-vue';
  import { getRoleUserCount } from '/@/api/demo/system';
  import { addRole,updateRole,getRoleInfo,getRoleGroupList ,getMyRoleGroupList} from '/@/api/sys/role';
  import { getOrgAuthData,removeOrgAuthData } from '/@/api/auth/group';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useModal } from '/@/components/Modal';
  import AuthModal from '../modal/AuthModal.vue';
  import { usePermission } from '/@/hooks/web/usePermission';

   import {
    BasicTable,useTable
  } from '/@/components/Table';

  export default defineComponent({
    name: 'RoleDrawer',
    components: { BasicDrawer, BasicForm, Tabs,Select,SelectOption:Select.Option, TabPane: Tabs.TabPane,BasicTable,Card,AuthModal},
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const isRoleGroup = ref(false);
      const recordId = ref('');
      const roleGroupSource = ref([]);
      const checkedKeys = ref<Array<string | number>>([]);
      const { hasPermission } = usePermission();
      const { createMessage: msg } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: formRoleSchema,
        showActionButtonGroup: false,
      });

      const [registerRoleGroupForm,{ validate:validateGroup,setFieldsValue:setGroupFieldsValue }] = useForm({
        labelWidth: 100,
        schemas: formRoleGroupSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 12, md: 12 },
      });

      const [registerTable] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getRoleUserCount,
        columns:roleUserColumns,
        useSearchForm: false,
        showIndexColumn: false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:false},
      });
      const [registerAuthTable,{reload:reloadAuth}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getOrgAuthData,
        columns:authColumns,
        useSearchForm: false,
        showIndexColumn: false,
        immediate:false,
        rowKey:'id',
        fetchSetting:{
          listField:'records'
        },
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        isRoleGroup.value = !!data?.isRoleGroup;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          setFieldsValue({
            ...data.record,
          });
          let result = await getRoleInfo({id:data.record?.id});
          // 回显角色组和角色信息
          if(unref(isRoleGroup)){
            setGroupFieldsValue({
              ...result
            })
          }else{
            setFieldsValue({
              ...result
            })
          }
        }else{
          recordId.value = '';
        }
        let roleResult = await getRoleGroupList();
        setArrayItemProperty(roleResult);
        roleGroupSource.value = roleResult;
        console.log( roleGroupSource.value )
      });

      function setArrayItemProperty(list) {
        list.map(item=>{
          if(!item.oprateFlag){
            item.disabled = true;
          }
          if(item.children){
            setArrayItemProperty(item.children);
          }
        })
      }

      const getTitle = computed(() => (unref(isRoleGroup)?(!unref(isUpdate)?'新增角色组':'编辑角色组'):!unref(isUpdate) ? '新增角色' : '编辑角色'));
      
      function handleTabChange(val){
        if(val=='roleAuth'){
          nextTick(()=>{
            reloadAuth({searchInfo:{authId:unref(recordId)}});
          })
        }
      }
      async function handleSubmit() {
        try {
          let values = reactive({})
          if(unref(isRoleGroup)){
            values = await validateGroup();
          }else{
            values = await validate();
          }
          setDrawerProps({ confirmLoading: true });
          // TODO custom api
          let result = reactive({});
          if(!unref(isUpdate)){  
            result = await addRole(values);
          }else{
            result = await updateRole({id:unref(recordId),...values});
          }
          if(result){
            msg.success('操作成功');
            closeDrawer();
            emit('success');
          }
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }
      function handleAddAuth(){
        openModal(true,{
          isUpdate:false,
          record:{
            recordId:unref(recordId),
            authType:'user'
          }
        })
      }
      async function handleDeleteAuth(){
        if(!unref(checkedKeys).length){
          msg.error('请选择要删除的数据');
          return;
        }
         let params = {
          ids:unref(checkedKeys).join(','),
        }
        let result = await removeOrgAuthData(params);
        if(result){
          msg.success('操作成功')
          reloadAuth({searchInfo:{authId:unref(recordId)}});
        }
      }

      function handleSuccess(){
        reloadAuth({searchInfo:{authId:unref(recordId)}});
      }

      return {hasPermission,roleGroupSource,checkedKeys,onSelectChange,handleAddAuth,handleDeleteAuth,handleSuccess,registerModal, registerDrawer, registerForm,handleTabChange, registerRoleGroupForm, getTitle, handleSubmit ,isUpdate, isRoleGroup, registerTable,registerAuthTable};
    },
  });
</script>

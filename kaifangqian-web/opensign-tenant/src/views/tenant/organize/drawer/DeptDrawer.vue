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
    width="40%"
    @ok="handleSubmit"
    >
     <!-- <Tabs @change="changeTab">
        <TabPane key="1" tab="基本信息">
            <BasicForm @register="registerForm" />
        </TabPane>
        <TabPane key="2" tab="人员角色" v-if="isUpdate">
            <BasicTable @register="registerTable"> </BasicTable>
        </TabPane>
        <TabPane key="3" tab="权限" v-if="isUpdate">
            <BasicTable @register="registerAuthTable" :rowSelection="{ type: 'checkbox',selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
                <template #toolbar>
                  <a-button type="primary" @click="handleAddAuth" v-if="hasPermission(['deptedit:authadd'])">添加权限</a-button>
                  <a-button type="danger" @click="handleDeleteAuth" v-if="hasPermission(['deptedit:authdelete'])">删除</a-button>
                 </template>
            </BasicTable>
        </TabPane>
      </Tabs> -->
      <BasicForm @register="registerForm" />
    <AuthModal @register="registerModal" @success="handleSuccess" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref, reactive,nextTick } from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { formSchema,roleUserColumns,authColumns } from '../data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { Tabs } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { DeptAdd, DeptUpdate} from '/@/api/demo/system';
  import { getUserByDeptId  } from '/@/api/sys/user';
  import { getOrgAuthData,removeOrgAuthData } from '/@/api/auth/group';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { getUserRoleListByDeptId,getDeptInfoById,getMyDeptTreeList  } from '/@/api/sys/dept';

   import {
    BasicTable,useTable
  } from '/@/components/Table';

  import AuthModal from '../modal/AuthModal.vue';

  export default defineComponent({
    name: 'DeptDrawer',
    components: { BasicDrawer, BasicForm, Tabs,  TabPane: Tabs.TabPane,BasicTable,AuthModal},
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const { hasPermission } = usePermission();
      const checkedKeys = ref<Array<string | number>>([]);
      const { createMessage: msg } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const [registerForm, { resetFields, setFieldsValue, validate,updateSchema,clearValidate,removeSchemaByFiled }] = useForm({
        labelWidth: 100,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });
      const recordId = ref();

      const [registerTable,{reload:reloadUserRole}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getUserRoleListByDeptId,
        columns:roleUserColumns,
        useSearchForm: false,
        showIndexColumn: false,
        showTableSetting: false,
        canResize: false,
        immediate:false,
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
        showTableSetting: false,
        rowKey:'id',
        fetchSetting:{
          listField:'records'
        },
        immediate:false,
        canResize: false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:false},
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '添加部门' : '编辑部门'));


      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        recordId.value = data.record?.id || data.record?.currentDeptId;  //部门记录id or 通过点击左侧tree 获取的当前部门id
        const treeData = await getMyDeptTreeList({});
        updateSchema({
            field: 'parentId', componentProps: {  treeData}
          })
        if(unref(recordId)) {
            setFieldsValue({
                parentId:unref(recordId),
            })
            //获取该部门下人员列表
            const userData = await getUserByDeptId({id:unref(recordId),pageNo:1,pageSize:1000});
            userData.records.map(item=>{
              item.label = item.realname;
              item.value = item.id;
            });
            updateSchema([
              { field: 'parentId', required:true, componentProps: { disabled:true,}},
              { field: 'userIds', required:true, componentProps: {  options: userData.records}},
            ]);
        }else{
            updateSchema({
                field: 'parentId', required:true, componentProps: { disabled:false , treeData}
            })
        }
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
           // 通过 header 编辑当前部门
          if(unref(recordId)){
            let result = await getDeptInfoById({id:unref(recordId)});
            if(result){
              setFieldsValue({
                  ...result,
                  userIds:result.userIds || []
              })
              if(result.parentId){
                updateSchema([
                    { field: 'parentId', required:true, componentProps: {  disabled:true }},
                ]);
                setFieldsValue({
                  parentId:result.parentName
                })
              }else{
                updateSchema({
                    field: 'parentId', required:false, componentProps: { disabled:true ,}
                })
              }
            }
            clearValidate();
          }
        }else{
          //新增时清除设置主管字段
          removeSchemaByFiled('userIds');
        }
      });

      function changeTab(val){
        if(val==2){
          nextTick(()=>{
            reloadUserRole({searchInfo:{id:unref(recordId)}});
          })
        }
        if(val==3){
          nextTick(()=>{
            reloadAuth({searchInfo:{authId:unref(recordId)}});
          })
        }
      }
      function handleAddAuth(){
        openModal(true,{
          isUpdate:false,
          record:{
            recordId:unref(recordId),
            authType:'dept',
            departId:unref(recordId)
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



      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          // TODO custom api
          let result = reactive({});
          if(unref(isUpdate)){
            result = await DeptUpdate({
              ...values,
              id:unref(recordId)
            })
          }else{
            result = await DeptAdd(values);
          }
          if(result){
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
      function handleSuccess(){
        reloadAuth({searchInfo:{authId:unref(recordId)}});
      }
      return {hasPermission,handleAddAuth,handleSuccess,handleDeleteAuth, registerModal, registerDrawer, registerForm, getTitle, checkedKeys,onSelectChange,handleSubmit ,isUpdate, registerTable,registerAuthTable,changeTab};
    },
  });
</script>

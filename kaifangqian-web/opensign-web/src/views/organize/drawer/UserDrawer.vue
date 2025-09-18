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
      <Tabs @change="handleTabChange" :animated="false">
        <TabPane key="1" tab="基本信息">
          <Card title="基础信息" size="small">
            <Form  :model="formData" :colon="false" :rules="getFormRules" ref="formRef" :label-col="labelCol" :wrapper-col="wrapperCol">
              <Row>
                <Col :span="12" :gutter="1">
                  <FormItem name="username" label="账号" >
                      <Input  v-model:value="formData.username"   placeholder="请输入账号" class="fix-auto-fill"  />
                  </FormItem>
                  <FormItem name="realname" label="姓名" >
                      <Input    v-model:value="formData.realname"  placeholder="请输入姓名" />
                  </FormItem>
                  <FormItem name="departIds" label="部门" >
                      <TreeSelect  v-model:value="formData.departIds"
                          :multiple="true" 
                          :tree-data="depts" 
                          :field-names="{  children: 'children', label: 'departName',   value: 'id'}" 
                          @change="handleDeptChange(formData.departIds)"
                          placeholder="请选择部门"
                      > 
                      </TreeSelect>
                  </FormItem>
                    
                </Col>
                <Col :span="12">
                    <!-- <FormItem name="avatar" label="头像" >
                      <div class="user-avatar">
                          <img :src="avatarBase64" alt="" v-if="formData.avatar">
                          <span v-else>{{formData.realname}}</span>
                      </div>
                    </FormItem> -->
                    <FormItem name="manageDepartIds" label="主管部门" >
                        <Select v-model:value="formData.manageDepartIds" mode="multiple" placeholder="请选择主管部门" > 
                            <SelectOption v-for="(item,index) in deptSource" :value="item.id" :key="index">{{item.departName}}</SelectOption>
                        </Select>
                    </FormItem>
                    <FormItem name="post" label="职务">
                        <Input v-model:value="formData.post"  placeholder="请输入职务" ></Input>
                    </FormItem>
                    <FormItem name="roleIds" label="角色">
                      <TreeSelect
                        :multiple="true" 
                        :fieldNames="{children:'children', label:'name',  value: 'id' }"
                        v-model:value="formData.roleIds"
                        :tree-data="roleTreeSource"
                        placeholder="请选择角色"
                      > </TreeSelect>
                    </FormItem>
                </Col>
              </Row>
            </Form>
          </Card>
          <Card title="个人信息" size="small">
              <BasicForm @register="registerFormMore">
                <template #phone="{model,field}" key="phone">
                  <Input v-model:value="model[field]" @blur="handlePhoneBlur" ></Input>
                </template>
                <template #email="{model,field}">
                  <Input v-model:value="model[field]" key="email"></Input>
                </template>
              </BasicForm>
          </Card>
        </TabPane>
        <TabPane key="3" tab="权限" v-if="isUpdate">
            <BasicTable @register="registerAuthTable" :rowSelection="{ type: 'checkbox',selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
                <template #toolbar>
                  <a-button type="primary" @click="handleAddAuth" v-if="hasPermission(['useredit:authadd'])">添加权限</a-button>
                  <a-button type="danger" @click="handleDeleteAuth" v-if="hasPermission(['useredit:authdelete'])">删除</a-button>
                </template>
            </BasicTable>
        </TabPane>
      </Tabs>
  </BasicDrawer>
  <AuthModal @register="registerModal" @success="handleSuccess" />
</template>
<script lang="ts">
  import type { Rule } from 'ant-design-vue/es/form';
  import { defineComponent, ref, computed, unref,nextTick} from 'vue';
  import { BasicForm, useForm } from '/@/components/Form/index';
  import { BasicTable,useTable, FormSchema  } from '/@/components/Table';
  import { authColumns,roleUserColumns } from '../data';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { Tabs,Card, Select,Form, Input,Row,Col,TreeSelect } from 'ant-design-vue';
  import { getOrgAuthData,removeOrgAuthData } from '/@/api/auth/group';
  import { getUserRoleListByDeptId  } from '/@/api/sys/dept';
  import { addUser, editUser, getUserInfoById , checkUserInfoRepeat} from '/@/api/sys/user';
  import { getAllRoleTreeListForSelect } from '/@/api/sys/role';
  import { getImgBase64 } from '/@/api/sys/upload';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { usePermission } from '/@/hooks/web/usePermission';
  // import { getDeptTreeList, } from '/@/api/demo/system';
  import { getAllDeptTreeForSelect } from '/@/api/sys/dept';
  import {useFormValid} from '/@/views/sys/login/useLogin';
  import { findMatchItemInList } from '/@/components/_util/util';
  import AuthModal from '../modal/AuthModal.vue';
  import { isMobile,isEmail } from '/@/utils/validate';
  import { Md5 } from "ts-md5";
  // import { setArrayItemProperty } from '/@/utils/util';


  interface DeptSourceItem {
    departName:string;
    id:string
  }
  interface  RoleItem {
    id:string;
    name:string;
    oprateFlag:boolean;
    title:string;
    disabled?:boolean;
    parentId?:string
  }
  
  export default defineComponent({
    name: 'UserDrawer',
    components: { 
      Form, 
      FormItem:Form.Item,
      Input, 
      BasicDrawer,
      BasicForm,
      Tabs,  
      TabPane: Tabs.TabPane, 
      BasicTable, 
      Card, 
      AuthModal, 
      Select,
      SelectOption:Select.Option,
      Row,
      Col,TreeSelect
    },


    emits: ['success', 'register'],
    setup(_, { emit }) {

      const validateUserName = async (_rule: Rule, value: string)=>{
          if(!value){
            return Promise.reject('请输入账号');
          }
          const validateResult = await checkUserInfoRepeat({type:'username',key:value,userId:unref(recordId) })
          if(validateResult.checkResult){
            return Promise.reject('该账号已存在');
          }
          return Promise.resolve();
      }
      const validateUserPhone = async (_rule: Rule, value: string)=>{
          if(!value){
            return Promise.resolve();
          }
          if(value && !isMobile(value)){
              return Promise.reject('手机号格式不正确');
          }
          const validateResult = await checkUserInfoRepeat({type:'phone',key:value,userId:unref(recordId) })
          if(validateResult.checkResult){
            return Promise.reject('该手机号已存在');
          }
          return Promise.resolve();
      }
      const validateUserEmail = async (_rule: Rule, value: string)=>{
          if(!value){
            return Promise.resolve();
          }
          if(value && !isEmail(value)){
            return Promise.reject('邮箱格式不正确');
          }
          const validateResult = await checkUserInfoRepeat({type:'email',key:value,userId:unref(recordId) })
          if(validateResult.checkResult){
            return Promise.reject('该邮箱已存在');
          }
          return Promise.resolve();
      }


      const getFormRules = ref({
        username:[{ required: true, trigger:'blur', validator:validateUserName}],
        realname:[{ required: true, trigger:'change', message:'请输入姓名'}],
        departIds:[{ required: true, trigger:'change', message:'请选择部门'}],
        // roleIds:[{ required: true, trigger:'change', message:'请选择角色'}],
      })
      const formUserSchemaMore:FormSchema[] = [
        { field: 'phone',  label: '手机',   component: 'Input', colProps: { span: 12 }, slot:'phone',
          rules: [{ required: false,trigger:'blur',  validator:validateUserPhone}] },
        {  field: 'telephone',  label: '分机号', component: 'Input',  colProps: { span: 12 }},
        {  field: 'email', label: '邮箱',  component: 'Input',  colProps: { span: 12 }, 
         rules: [{ required: false,trigger:'blur',validator:validateUserEmail }], slot:'email' },
        {  field: 'workNo',  label: '工号', component: 'Input',  colProps: { span: 12 },  },
        {  field: 'birthday',  label: '生日', component: 'DatePicker', colProps: { span: 12 }, },
        {  field: 'sex', label: '性别',  component: 'Select',  colProps: { span: 12 }, componentProps:{
            options: [
              { label: '男', value: 1 },
              { label: '女', value: 2 },
            ],
          }
        },
      ]
      let  formData = ref({
        avatar:'',
        username:'',
        realname:'',
        manageDepartIds:[],
        departIds:[],
        post:'',
        roleIds:[]

      })
      const { hasPermission } = usePermission();
      const formRef = ref();
      // const { validForm } = useFormValid(formRef);
      const isUpdate = ref(true);
      const departId = ref('');
      const { createMessage: msg } = useMessage();
      const recordId = ref('');
      const avatarBase64 = ref('');
      const checkedKeys = ref<Array<string | number>>([]);
      const roleTreeSource = ref<RoleItem[]>([]);
      const depts = ref([]);
      const deptSource = ref<DeptSourceItem[]>([]);

      const [registerModal, { openModal }] = useModal();
      // const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
      //   labelWidth: 100,
      //   schemas: formUserSchema,
      //   showActionButtonGroup: false,
      //   baseColProps: { lg: 12, md: 12 },
      // });
      const [registerFormMore,{setFieldsValue:setPersonalFieldsValue,getFieldsValue,updateSchema,validateFields }] = useForm({
        labelWidth: 100,
        schemas: formUserSchemaMore,
        showActionButtonGroup: false,
        baseColProps: { lg: 12, md: 12 },
      });
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
        immediate:false,
        rowKey:'id',
        fetchSetting:{
          listField:'records'
        },
        showIndexColumn: false,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });

      const [registerDrawer, { setDrawerProps, closeDrawer}] = useDrawerInner(async (data) => {
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        let deptResult = await getAllDeptTreeForSelect();
        setArrayItemProperty(deptResult);
        depts.value = deptResult || [];
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          console.log(recordId.value,'===记录id==')
          departId.value = data.record.departId;
          if( recordId.value){
            getUserInfo(recordId.value)
          }
        }else{
          recordId.value = '';
          // const form = unref(formRef);
          formData.value = {
            avatar:'',
            username:'',
            realname:'',
            manageDepartIds:[],
            departIds:[],
            post:'',
            roleIds:[]
          }

          // console.log('表单实例',unref(form))
          // form.resetFields(['username']);
        }
        // if(!unref(roleTreeSource)){
          let result:Array<RoleItem>  = await getAllRoleTreeListForSelect();
          if(!result) return;
            // result.map(item => {
            //   item.disabled = item.parentId?false:true;
            // })
            setArrayItemProperty(result);
            roleTreeSource.value = result;
      });
      function handlePhoneBlur(){
        console.log('失焦')
        // updateSchema([
        //   { field: 'phone',  label: '手机',   component: 'Input', colProps: { span: 12 }, slot:'phone', 
        //     rules: [{ required: false,trigger:'blur',validator:validateUserPhone }] },
        //   {  field: 'email', label: '邮箱',  component: 'Input',  colProps: { span: 12 }, 
        //     rules: [{ required: false,trigger:'blur',validator:validateUserEmail }], slot:'email',},
        // ])
      }

      function setArrayItemProperty(list) {
        list.map(item=>{
          if(!item.oprateFlag){
            item.disabled = true;
          }
          if(item.children){
            setArrayItemProperty(item.children)
          }
        })
      }
      const getTitle = computed(() => (!unref(isUpdate) ? '添加成员' : '编辑成员'));
      async function getUserInfo(id){
        let result = await getUserInfoById({userId:id});
        if(result){
          // 主管部门数据整理
          if(result.departIds){
            handleDeptChange(result.departIds);
          }
          // 头像数据请求
          if(result.avatar){
            loadAvatar(result.avatar)
          }
          nextTick(()=>{
            formData.value = Object.assign({},{...result,roleIds:result.roleIds || []});
            setPersonalFieldsValue({
              ...result
            })
          })
          
        }
      }
      function handleTabChange(val){
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
      async function handleSubmit() {
        try {
          // formRef.value.clearValidate(['username']);
          //提交时不进行接口校验
          
          // const values = await validForm();
          // const valuesMore = await validateMore();
          const { validForm } = useFormValid(formRef);
          // const values = unref(formData);
          const values = await validForm();
          if(!values){
              return 
          }
          const valuesMore = await getFieldsValue();
          setDrawerProps({ confirmLoading: true });
          // TODO custom api
          let result:Object= {};
          let msgText:string = '';
          if(unref(isUpdate)){
            result = await editUser({...values,...valuesMore,id:unref(recordId)});
            msgText = '编辑成功';
          }else{
            result = await addUser({  ...values,...valuesMore,password:Md5.hashStr('123456')});
            msgText = '新增成功'; 
          }
          if(result){
              msg.success(msgText);
              console.log(values);
              closeDrawer();
              emit('success');
          }else{

          }
         
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }
      function handleAddAuth(){
        openModal(true,{
          isUpdate:false,
          record:{
            recordId:unref(recordId),
            authType:'user',
            departId:unref(departId)
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

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }
      function handleSuccess(){
        reloadAuth({searchInfo:{authId:unref(recordId)}});
      }
      function handleDeptChange(ids){
        console.log(ids)
        deptSource.value = [];
        ids.map(key=>{
          let matchItem = findMatchItemInList(unref(depts),{id:key});
          if(!matchItem) return;
          deptSource.value.push({
            departName:matchItem.departName,
            id:matchItem.id
          })

        })


      }
      async function loadAvatar(avatarId){
        if(!avatarId) return ;
        let result = await getImgBase64({imgId:avatarId});
        avatarBase64.value =   result.image;
      }

      return {
        handleDeptChange, 
        depts, 
        handlePhoneBlur,
        deptSource, 
        avatarBase64,
        handleAddAuth,
        handleDeleteAuth, 
        roleTreeSource, 
        registerModal,
        handleSuccess,
        registerDrawer, 
        // registerForm,
        registerFormMore,
        getTitle, 
        checkedKeys,
        handleSubmit,
        isUpdate,
        registerAuthTable,
        handleTabChange,
        onSelectChange,
        registerTable,
        formData,
        getFormRules,
        formRef,
        labelCol: { style: { width: '110px' } },
        wrapperCol: { span: 24 },
        hasPermission,
      };
    },
  });
</script>
<style lang="less" scoped>
.user-avatar{
    width: 87px;
    height: 87px;
    border: 1px solid #ededed;
    border-radius: 50%;
    background: #127fd2;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #ffff;
    white-space: nowrap;
    padding: 20px;
    span{
      overflow: hidden;
    }
}
</style>

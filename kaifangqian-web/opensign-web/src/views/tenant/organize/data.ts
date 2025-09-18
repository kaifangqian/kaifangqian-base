/*
 * @description 开放签
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

import { getRoleTreeList,getRoleGroupList,getMyRoleGroupList } from '/@/api/sys/role';

import { getAllUserList , checkUserInfoRepeat } from '/@/api/sys/user';

import { getDeptTreeList } from '/@/api/demo/system';

import { isMobile,isEmail } from '/@/utils/validate';
import type { Rule } from 'ant-design-vue/es/form';

const validatorPhone = async (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入手机号');
  }else  if(value && !isMobile(value)){
    return Promise.reject('手机号格式不正确');
  }else if(value){
    let result = await checkUserInfoRepeat({checkType:'phone',key:value,userId:'' })
    if(result){
      return Promise.resolve();
    }else{
      return Promise.resolve();
    }
  } else {
    return Promise.resolve();
  }
}
const validatorEmail = (_rule: Rule, value: string) =>{
  if(value && !isEmail(value)){
    return Promise.reject('邮箱格式不正确');
  }else {
    return Promise.resolve();
  }
}

export const roleUserColumns:BasicColumn[] = [
  {
    title:'名称',
    dataIndex:'roleName'
  },
  {
    title:'人数',
    dataIndex:'userCount'
  },
]
export const authColumns:BasicColumn[] = [
  {
    title:'权限组分类',
    dataIndex:'pname',
    width:100
  },
  {
    title:'权限组',
    dataIndex:'groupName',
    width:150
  },
  {
    title:'描述',
    dataIndex:'groupDesc'
  },
]
export const deptColumns:BasicColumn[] = [
  {
    title:'部门名称',
    dataIndex:'departName',
    width: 400,
  },
  {
    title:'成员数',
    dataIndex:'userCount',
    width: 100,
  },
  {
    title:'主管',
    dataIndex:'manageNames',
  },
  {
    title:'操作',
    dataIndex:'action',
    align:'left',
    slots: { customRender: 'action' },
  },
]
export const userColumns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'realname',
    slots: { customRender: 'realname' }
  },
  {
    title: '角色',
    dataIndex: 'roleNames',
    slots: { customRender: 'roleNames' },
  },
  {
    title: '工号',
    dataIndex: 'workNo',
   
  },
  {
    title: '手机号',
    dataIndex: 'phone'
  },
 
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    slots: { customRender: 'status' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
  },
];
export const roleColumns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'realname',
    slots: { customRender: 'realname' },
  },
  {
    title: '部门',
    dataIndex: 'departNames',
  },
  {
    title: '工号',
    dataIndex: 'workNo',
   
  },
  {
    title: '手机号',
    dataIndex: 'phone'
  },
 
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 100,
    slots: { customRender: 'status' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
  },
];
export const organizeFormSchema: FormSchema[] = [
  {
    field: 'departName',
    label: '组织名称',
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  // {
  //   field: 'userIds',
  //   label: '设置主管',
  //   component: 'ApiSelect',
  //   required: true,
  //   componentProps:{
  //     mode:'multiple',
  //     allowClear: true,
  //     api: getAllUserList,
  //     resultField: 'records',
  //     labelField:'realname',
  //     valueField:'id',
  //     getPopupContainer: () => document.body,
  //   }
  // },
 
  {
    field: 'mobile',
    label: '电话',
    component: 'Input',
  },
  {
    field: 'address',
    label: '区域',
    component: 'Input',
  },
  {
    field: 'description',
    label: '简介',
    component: 'InputTextArea',
  },
 
];
export const formSchema: FormSchema[] = [
  {
    field: 'departName',
    label: '部门名称',
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    field: 'parentId',
    label: '上级部门',
    required: true,
    component: 'TreeSelect',
    componentProps: {
      fieldNames: {
        label: 'departName',
        key: 'id',
        value:'id',
        title:'departName'
      },
      getPopupContainer: () => document.body,
    },
    colProps: { span: 24 },
  },
  {
    field: 'userIds',
    label: '设置主管',
    component: 'Select',
    required: false,
    componentProps:{
      mode:'multiple',
      allowClear: true,
      // api: getAllUserList,
      // resultField: 'records',
      // labelField:'realname',
      // valueField:'id',
      getPopupContainer: () => document.body,
    }
  },
 
  {
    field: 'mobile',
    label: '电话',
    component: 'Input',
  },
  {
    field: 'address',
    label: '区域',
    component: 'Input',
  },
  {
    field: 'description',
    label: '简介',
    component: 'InputTextArea',
  },
 
];

export const formUserSchema:FormSchema[] = [
  {
    field: 'username',
    label: '账号',
    component: 'Input',
    slot:'username',
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'avatar',
    label: '头像',
    component: 'Input',
    slot:'avatar',
    required: false,
    colProps: { span: 12 },
  },
  {
    field: 'realname',
    label: '姓名',
    component: 'Input',
    slot:'realname',
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'manageDepartIds',
    label: '主管部门',
    component: 'Select',
    required: false,
    slot:'manageDepartIds',
    colProps: { span: 12 },
    componentProps:{
      multiple:true,
      allowClear:true,
      options:[
        {label:'3333',value:'2222'}
      ],
      // api: getDeptTreeList,
      // fieldNames: {
      //   label: 'departName',
      //   key: 'orgCode',
      // },
      getPopupContainer: () => document.body,
    }
    
  },
  {
    field: 'departIds',
    label: '部门',
    component: 'ApiTreeSelect',
    slot:'departIds',
    required: true,
    colProps: { span: 12 },
    componentProps:({ schema, tableAction, formActionType, formModel }) => {
      return {
          multiple:true,
          api: getDeptTreeList,
          fieldNames: {
            label: 'departName',
            key: 'orgCode',
          },
          onChange: e => {
            // let matchItem = findMatchItemInList();
            console.log(formModel)
          },
          getPopupContainer: () => document.body,
      }
    }
  },
  {
    field: 'post',
    label: '职务',
    slot:'post',
    component: 'Input',
    required: false,
    colProps: { span: 12 },
  },
  {
    field: 'roleIds',
    label: '角色',
    component: 'ApiTreeSelect',
    required: false,
    colProps: { span: 12 },
    slot: 'roleSelect',
    componentProps:{
      mode:'multiple',
      api: getRoleTreeList,
      fieldNames: {
        label: 'name',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    }
  },
 
]


export const formUserSchemaMore:FormSchema[] = [
  {
    field: 'phone',
    label: '手机',
    rules: [{ required: false,trigger:'blur',validator:validatorPhone }],
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'telephone',
    label: '分机号',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'email',
    label: '邮箱',
    slot:'email',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'workNo',
    label: '工号',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'birthday',
    label: '生日',
    component: 'DatePicker',
    colProps: { span: 12 },
  },
  {
    field: 'sex',
    label: '性别',
    component: 'Select',
    colProps: { span: 12 },
    componentProps:{
      options: [
        { label: '男', value: 1 },
        { label: '女', value: 2 },
      ],
    }
  },
]
export const formRoleSchema:FormSchema[] = [
  {
    field: 'roleName',
    label: '角色名称',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'parentId',
    label: '角色组',
    component: 'ApiTreeSelect',
    slot:'parentId',
    colProps: { span: 12 },
    required: true,
    componentProps:{
      api: getRoleGroupList,
      params:{
        parentId:''
      },
      resultField:'records',
      fieldNames: {
        label: 'roleName',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    }
  },
  {
    field: 'roleCode',
    label: '角色编码',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'description',
    label: '描述',
    component: 'InputTextArea',
    colProps: { span: 12 },
  },
]
export const formRoleGroupSchema:FormSchema[] = [
  {
    field: 'roleName',
    label: '角色组名称',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'roleCode',
    label: '角色编码',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
]

export const formDeptBatchSchema:FormSchema[] = [
  {
    field: 'departName',
    label: '部门名称',
    component: 'ApiTreeSelect',
    required: false,
    colProps: { span: 24 },
    componentProps:{
      disabled:true,
      multiple:true,
      api: getDeptTreeList,
      fieldNames: {
        label: 'departName',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    }
  },
  {
    field: 'departIds',
    label: '上级部门',
    component: 'ApiTreeSelect',
    required: true,
    colProps: { span: 24 },
    componentProps:{
      api: getDeptTreeList,
      fieldNames: {
        label: 'departName',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    }
  },
]

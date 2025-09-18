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

import { BasicColumn, FormSchema } from '/@/components/Table';
import { getBusinessLineGroupList } from '/@/api/businessLine'
import type { Rule } from 'ant-design-vue/es/form';
import { isMobile } from '/@/utils/validate';

const validateMobile =  (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入手机号');
  }
  if(!isMobile(value)){
    return Promise.reject('手机号格式不正确');
  }else{
    return Promise.resolve(); 
  }
}

export const ccFormSchema:FormSchema[] =[
  {
    field: 'ccedOpportunityType',
    label: '签署设置',
    component: 'Select',
    required:false,
    // colProps: { span: 24 },
    componentProps:{
      options:[
        {label:'文件发起时',value:1},
        {label:'文件签署完成时',value:2},
      ],
      // labelCol:{
      //   span:12
      // },
      wrapperCol:{
        span:12
      }
    }
  },
  {
    field: 'ccerType',
    label: '支持抄送的用户类型',
    component: 'Input',
    slot:'ccerType',
    colProps: { span: 24 },
    componentProps:{
      labelCol:{
        span:24
      },
      wrapperCol:{
        span:24
      }
    }
  },
  {
    field: 'addCcerType',
    label: '允许发起人添加抄送人',
    component: 'Switch',
    colProps: { span: 24 },
    componentProps:{
      // labelCol:{
      //   span:24
      // },
      // wrapperCol:{
      //   span:24
      // },
    }
  },
  
]


export const lineGroupCreateFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '分组名称',
    labelWidth:100,
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    field: 'parentFolderId',
    label: '所属分组',
    component: 'ApiTreeSelect',
    slot:'parentFolderId',
    required: false,
    labelWidth:100,
    colProps: { span: 24 },
    componentProps:{
      api:getBusinessLineGroupList,
      // resultField: 'record',
      // labelField:'name',
      // valueField:'id',
      fieldNames:{
        label:'name',
        value:'id',
        key:'id'
      }

    }
  },
]

export const lineCreateFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '业务线名称',
    labelWidth:100,
    component: 'Input',
    required: true,
    colProps: { span: 24 },
  },
  {
    field: 'folderId',
    label: '业务线分组',
    component: 'ApiTreeSelect',
    slot:'folderId',
    required: true,
    labelWidth:100,
    colProps: { span: 24 },
    componentProps:{
      api:getBusinessLineGroupList,
      fieldNames:{
        label:'name',
        value:'id',
        key:'id'
      },
      getPopupContainer:(triggerNode)=>{
        return triggerNode.parentElement;
      }
      

    }
  },
]
export const lineFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '业务线名称',
    component: 'Input',
    required: false,
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    required: false,
    colProps: { span: 8 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'启用',value:1},
        {label:'停用',value:2},
      ]
    }
  },
]

export const lineColumns: BasicColumn[] = [
  {
    title: '业务线名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  },
  {
    title: '管理员',
    dataIndex: 'managerList',
    slots: { customRender: 'managerList' },
  },
  {
    title: '更新时间',
    dataIndex: 'updateDate',
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
  },
]

export const sealColumns:BasicColumn[] =[
  {
    title:'印章名称',
    dataIndex:'sealName'
  },
  {
    title:'印章id',
    dataIndex:'sealId'
  },
]

export const  ccAddFormSchema:FormSchema[] = [
  {
    field: 'ccerType',
    label: '文件抄送：',
    component: 'Select',
    colProps: { span: 20 },
    required:true,
    componentProps:{
      disabled:true,
      defaultValue:1,
      options:[
        {label:'内部用户',value:1},
        {label:'外部用户',value:2},
      ]
    }
  },
  {
    field: 'externalCcerName',
    label: '姓名：',
    component: 'Input',
    colProps: { span: 20 },
    required:false,
    componentProps:{
    },
    ifShow:({values}) =>{
      return values.ccerType == 2
    }
  },
  {
    field: 'externalCcedType',
    label: '外部抄送人类型：',
    component: 'Input',
    colProps: { span: 20 },
    componentProps:{
    },
    show:false
  },
  {
    field: 'externalCcedValue',
    label: '账号：',
    component: 'Input',
    colProps: { span: 20 },
    slot:'externalCcedValue',
    rules:[{required:true,validator:validateMobile}],
    componentProps:{
    },
    ifShow:({values}) =>{
      return values.ccerType == 2
    }
  },
  
  {
    field: 'internalCcerId',
    label: '抄送人：',
    component: 'Input',
    colProps: { span: 20 },
    slot:'internalCcerId',
    componentProps:{
    },
    ifShow:({values}) =>{
      return values.ccerType == 1
    }
  },
]

export const tplColumns:BasicColumn[] =[
  {
    title:'模板名称',
    dataIndex:'templateName'
  },
  /* {
    title:'模板编号',
    dataIndex:'templateCode'
  }, */
  {
    title:'模板类型',
    dataIndex:'templateType',
    slots: { customRender: 'templateType' },
  },
]

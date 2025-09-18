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

import { BasicColumn, FormSchema  } from '/@/components/Table';
import type { Rule } from 'ant-design-vue/es/form';

import { isMobile } from '/@/utils/validate';

import dayjs, { Dayjs } from 'dayjs';

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
const disabledDate = (current: Dayjs) => {
  // Can not select days before today and today
  return current && current <= dayjs().subtract(1, 'day').endOf('day');
}


export const lineBasicFormSchema:FormSchema[] =[
  // {
  //   field: 'name',
  //   label: '业务线：',
  //   component: 'Input',
  //   slot:'name',
  //   colProps: { span: 14 },
  //   componentProps:{
  //     disabled:true
  //   }
  // },
  {
    field: 'fileList',
    label: '文件：',
    component: 'Select',
    slot:'fileList',
    required:true,
    colProps: { span: 14 },
    componentProps:{

    },
  },
  {
    field: 'codeType',
    label: '编号类型：',
    component: 'Input',
    colProps: { span: 14 },
    show:false,
    componentProps:{
      disabled:true
    }
  },
  {
    field: 'code',
    label: '编号：',
    component: 'Input',
    // required:  ({ values }) => {
    //    return  values.codeType == 1
    //  },
    required: false,
    colProps: { span: 14 },
    componentProps:{

    },
    slot: 'code',
    // ifShow: ({ values }) => {
    //   console.log(values,'000000')
    //   return   values.codeType == 1
    // },

  },
  {
    field: 'subjectType',
    label: '主题类型：',
    component: 'Input',
    colProps: { span: 14 },
    show:false,
    componentProps:{
      disabled:true
    }
  },
  {
    field: 'subject',
    label: '主题：',
    component: 'Input',
    required:  ({ values }) => {
       return  values.subjectType == 1
     },
    colProps: { span:14 },
    componentProps:{

    },
    slot: 'subject',
    // ifShow: ({ values }) => {
    //   return   values.subjectType == 1
    // },
  },
  {
    field: 'expireDate',
    label: '签署截止日期：',
    component: 'DatePicker',
    required:false,
    colProps: { span: 14 },
    componentProps:{
      disabledDate:disabledDate
    },
  },

  // {
  //   field: 'addAnnexType',
  //   label: '附件：',
  //   component: 'Input',
  //   slot:'addAnnexType',
  //   required:false,
  //   colProps: { span:14 },
  //   componentProps:{

  //   },
  //   ifShow:({values}) =>{
  //     return values.addAnnexType == 1
  //   }
  // },
]

export const lineSignerFormSchema:FormSchema[] =[
  {
    field: 'signOrderType',
    label: '签署顺序',
    component: 'Input',
    slot:'signOrderType',
    required:true,
    colProps: { span: 24 },
    componentProps:{
      // labelCol:{
      //   span:4
      // },
      // wrapperCol:{
      //   span:20
      // }
    }
  },
  {
    field: 'signatory',
    label: '',
    component: 'Input',
    slot:'signatory',
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
  
]

export const approveFormSchema:FormSchema[] = [
    {
    field: 'addAnnexType',
    label: '附件：',
    component: 'Input',
    slot:'addAnnexType',
    required:false,
    colProps: { span:14 },
    componentProps:{

    },
    ifShow:({values}) =>{
      return values.addAnnexType == 1
    }
  },
  {
    field: 'ccedType',
    label: '文件抄送：',
    component: 'Input',
    slot:'ccedType',
    colProps: { span: 20 },
    componentProps:{
    },  
    ifShow: ({ values }) => {
      return   values.ccedType == 1
    },
  },
  // {
  //   field: 'caSignType',
  //   label: '签署方式：',
  //   slot:'caSignType',
  //   component: 'RadioGroup',
  //   colProps: { span: 12 },
  //   componentProps:{
  //     options:[
  //       {label:'使用CA数字证书（符合电子签名法）',value:1},
  //       {label:'使用平台防篡改证书（保护文件）（无法律效力）',value:2},
  //       {label:'合成签名和印章图片（不保护文件）（无法律效力）',value:3},
  //     ]
  //   },
  // },
  {
    field: 'approveList',
    label: '内部审批流程：',
    component: 'Input',
    slot:'approveList',
    colProps: { span: 20 },
    componentProps:{
      labelCol:{
        span:24
      },
      wrapperCol:{
        span:24
      }
    },
    ifShow: ({ values }) => {
      console.log(values)
      return  values.baseVo&&values.baseVo.beforeStartApproveType==1
    },
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
  // {
  //   title:'申请时间',
  //   dataIndex:'applyTime'
  // },
]
export const recordColumns:BasicColumn[] =[
  {
    title:'操作人',
    dataIndex:'operatorName'
  },
  {
    title:'操作类型',
    dataIndex:'operateType'
  },
  {
    title:'操作动作',
    dataIndex:'actionType'
  },
  {
    title:'操作时间',
    dataIndex:'operateTime'
  },
  {
    title:'操作说明',
    dataIndex:'operateNotes'
  }
]
export const tplColumns:BasicColumn[] = [
  {
    title:'模板名称',
    dataIndex:'templateName'
  },
  {
    title:'模板编号',
    dataIndex:'templateCode'
  },
  {
    title:'模板类型',
    dataIndex:'templateType',
    slots: { customRender: 'templateType' },
  },
]
export const  ccFormSchema:FormSchema[] = [
  {
    field: 'ccerType',
    label: '文件抄送：',
    component: 'Select',
    colProps: { span: 20 },
    required:true,
    componentProps:{
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
    required:true,
    colProps: { span: 20 },
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
    slot:'externalCcedValue',
    colProps: { span: 20 },
    componentProps:{
    },
    rules:[{required:true,validator:validateMobile}],
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

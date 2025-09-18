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

/*
 * @Author: ningw 
 * @Date: 2022-06-21 18:39:26 
 * @Last Modified by: ningw
 * @Last Modified time: 2024-01-15 17:54:52
 */
import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';


export const passwordSchema: FormSchema[] = [
  {
    field: 'password_composition',
    label: '至少包含：',
    component: 'CheckboxGroup',
    required:true,
    componentProps:{
      options: [
        {
          label: '数字',
          value: 'num',
        },
        {
          label: '小写字母',
          value: 'lower',
        },
        {
          label: '大写字母',
          value: 'upper',
        },
        {
          label: '特殊字符(除空格外)',
          value: 'special',
        },
      ],
    },
    suffix: '',
  },
  {
    field: 'password_minimum_len',
    label: '密码最小长度：',
    component: 'InputNumber',
    required:true,
    itemProps: {
      extra: '限制密码最小长度。默认8位字符，长度范围为6-32位',
    },
    componentProps:{
      addonAfter:'+',
      addonBefore:'-',
      defaultValue:8,
      min:6,
      max:32
    },
    suffix: '位字符',
  },
  {
    field: 'password_invalid_time',
    label: '定期失效时间：',
    required:true,
    component: 'InputNumber',
    itemProps: {
      extra: '限制密码定期失效须重置密码。默认为0即不限制，最长可设置365天',
    },
    componentProps:{
      addonAfter:'+',
      addonBefore:'-'
    },
    colProps: { span: 24 },
    suffix: '天',
  },
  {
    field: 'password_repeat_limit',
    label: '密码重复限制：',
    component: 'InputNumber',
    colProps: { span: 12 },
    required:true,
    itemProps: {
      extra: '限制新密码与历史密码的重复，默认与前1次密码不重复，最多可限制与前 24 次密码不重复，0次表示不检查历史密码重复',
    },
    componentProps:{
      addonAfter:'+',
      addonBefore:'-'
    },
    suffix: '次',
  }
]
export const accessSchema: FormSchema[] = [
  {
    field: 'divider-basic',
    component: 'Divider',
    label: '登录设置',
    componentProps:{
      style:{
        fontSize:'14px',
        fontWeight:600
      }
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'login_repeat_limit',
    label: '登录重试限制：',
    component: 'InputNumber',
    required:true,
    itemProps: {
      extra: '密码输入错误N次，还有M次机会，超出失败数量后，账号冻结1小时',
    },
    suffix: '次/小时',
  },
  {
    field: 'long_time_no_login',
    label: '长时间未登录设置：',
    component: 'InputNumber',
    required:true,
    suffix: '天',
    itemProps: {
      extra: '限制帐号长时间未登录时间。默认为0即不限制，最长可设置365天，超出时限则要求用户在登录时进行二次身份校验'
    }
  },
  {
    field: 'offsite_login',
    label: '异地登录限制：',
    required:true,
    component: 'Switch',
    colProps: { span: 24 },
    itemProps: {
      extra: '限制用户异地登录设置，默认不开启，可手动开启；开启后用户如果出现异地登录则需要二次身份校验'
    }
    // suffix: '天',
    // componentProps: {
    //   options: [
    //     { label: '是', value: true },
    //     { label: '否', value: false },
    //   ],

    // },
  },
  {
    field: 'divider-basic',
    component: 'Divider',
    label: '登录状态/失效限制',
    componentProps:{
      style:{
        fontSize:'14px',
        fontWeight:600
      }
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'no_opetate_keep_alive',
    label: '无操作登录保持时间: ',
    required:true,
    component: 'Select',
    itemProps: {
      extra: '用户登录后，无操作时会话可保持时长，超过此时长将会退出登录，默认为2小时',
    },
    // suffix: '小时',
    colProps: {
      span: 5,
    },
    componentProps: {
      options: [
        {  label: '15min',   value: '0.25',  },
        {  label: '30min',   value: '0.5',  },
        {  label: '1h',   value: '1',  },
        {  label: '2h',   value: '2',  },
        {  label: '4h',   value: '4',  },
        {  label: '8h',   value: '8',  },
        {  label: '12h',   value: '12',  },
        {  label: '24h',   value: '24',  },
      ],
    },
  },
  {
    field: 'max_keep_alive',
    label: '最大登录保持时间: ',
    required:true,
    component: 'Select',
    itemProps: {
      extra: '用户登录后会话最大可保持时长，超过此时长将会退出登录，默认为24小时',
    },
    // suffix: '小时',
    componentProps: {
      options: [
        {  label: '15min',   value: '0.25',  },
        {  label: '30min',   value: '0.5',  },
        {  label: '1h',   value: '1',  },
        {  label: '2h',   value: '2',  },
        {  label: '4h',   value: '4',  },
        {  label: '8h',   value: '8',  },
        {  label: '12h',   value: '12',  },
        {  label: '24h',   value: '24',  },
      ],
    },
  },
  // {
  //   field: 'divider-basic',
  //   component: 'Divider',
  //   label: 'IP设置',
  //   componentProps:{
  //     style:{
  //       fontSize:'14px',
  //       fontWeight:600
  //     }
  //   },
  //   colProps: {
  //     span: 24,
  //   },
  // },
  // {
  //   field: 'IPLimit',
  //   label: 'IP登录限制',
  //   required:true,
  //   component: 'Switch',
  //   colProps: { span: 24 },
  // },
  // {
  //   field: 'type',
  //   label: 'IP类型',
  //   component: 'RadioButtonGroup',
  //   defaultValue: '0',
  //   required:true,
  //   itemProps:{
  //     extra:'设置白名单限制后，允许账号在白名单限制 IP（段）内登录'
  //   },
  //   componentProps: {
  //     defaultValue:'white',
  //     options: [
  //       { label: '黑名单', value: 'black' },
  //       { label: '白名单', value: 'white' },
  //       // { label: '常用名单', value: 'common' },
  //     ],
  //   },
  // },
  // {
  //   field: 'ipConfig',
  //   label: '配置IP',
  //   component: 'Input',
  //   required:true,
  //   colProps: { span: 24 },
  //   slot: 'customSlot',
  // },
]

export const IPSchema:FormSchema[] = [
  {
    field: 'ipSwitch',
    label: 'IP限制：',
    // required:true,
    component: 'Switch',
    slot: 'ipSwitch',
    itemProps:{
      extra:'您可以为账号开启IP限制，约束账号在安全环境下登录和使用系统。'
    },
    colProps: { span: 24 },
    defaultValue:false,
    componentProps:({ schema, tableAction, formActionType, formModel }) => {
      return {
        onChange: e => {
          console.log(schema, tableAction, formActionType, formModel);
        }
      }
      
    }
  },
  {
    field: 'type',
    label: 'IP类型：',
    component: 'RadioButtonGroup',
    defaultValue: 'black',
    required:true,
    itemProps:{
      extra:'设置黑名单限制后，不允许账号在黑名单限制 IP（段）内登录。'
    },
    slot: 'radioButton',
    componentProps: ({ schema, tableAction, formActionType, formModel }) => {
      return {
        defaultValue:'white',
        options: [
          { label: '黑名单', value: 'black' },
          { label: '白名单', value: 'white' },
          // { label: '常用名单', value: 'common' },
        ],
        onChange:e => {
          console.log(schema, tableAction, formActionType, formModel,e,'3333333333');
          // const {reload}=tableAction
        }
      };
    },
    ifShow: ({ values }) => {
      return  values.ipSwitch === true
    } 
  },
  // {
  //   field: 'ipConfig',
  //   label: '配置IP',
  //   component: 'Input',
  //   // required:true,
  //   colProps: { span: 24 },
  //   slot: 'ipConfig',
  // },
]

export const IPColumns:BasicColumn[] = [
  {
    title:'IP地址区间',
    dataIndex:'ipStart',
    editRow: true,
    colSpan: 2,
    editComponent: 'Input',
    editComponentProps: {
      align:'left'
    },
    slots: { customRender: 'ipStart' },
  },
  {
    title:'IP地址区间结束',
    dataIndex:'ipEnd',
    editRow: true,
    colSpan: 0,
    editComponent: 'Input',
    // editComponentProps: {
    //   align:'left'
    // },
    // slots: { customRender: 'ipEnd' },
  },
  {
    title:'描述',
    dataIndex:'ipDesc',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      aligin:'left'
    },
  },
]
export const sensitiveColumn:BasicColumn[] = [
  {
    title:'名称',
    dataIndex:'name'
  },
  {
    title:'功能模块',
    dataIndex:'moduleName'
  },
  {
    title:'描述',
    dataIndex:'sensitiveDesc'
  },
  {
    // title:'状态',
    dataIndex:'sensitiveFlag',
    slots: { customRender: 'sensitiveFlag',title:'customTitle' },
  },
  {
    title:'校验方式',
    dataIndex:'validateMethods',
    slots: { customRender: 'validateMethods' },
  },
  {
    title:'更新时间',
    dataIndex:'createTime'
  },
]
export const searchFormSchema:FormSchema[] = [
  {
    field: 'name',
    label: '名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'moduleName',
    label: '功能模块',
    component: 'Input',
    colProps: { span: 8 },
  },
]
export const tableFormSchema:FormSchema[] = [
  {
    field: 'name',
    label: '名称',
    component: 'Input',
    colProps: { span: 24 },
    componentProps:{
      disabled:true,
    }
  },
  {
    field: 'moduleName',
    label: '功能模块',
    component: 'Input',
    colProps: { span: 24 },
    componentProps:{
      disabled:true,
      api: '',
      labelField:'title',
      valueField:'id',
    }
  },
  {
    field: 'sensitiveFlag',
    label: '状态',
    required:false,
    component: 'Switch',
    colProps: { span: 24 },
  },
  // {
  //   field: 'createTime',
  //   label: '日期',
  //   required:false,
  //   component: 'DatePicker',
  //   colProps: { span: 24 },
  //   componentProps:{
  //     disabled:true,
  //     format:'YYYY-MM-DD'
  //   }
  // },
  // {
  //   field: 'sensitiveType',
  //   label: '校验方式',
  //   component: 'RadioGroup',
  //   defaultValue: 0,
  //   componentProps: {
  //     options: [
  //       { label: '密码校验', value: 0 },
  //       { label: '短信校验', value: 1 },
  //     ],
  //   },
  // },
  {
    field: 'sensitiveTime',
    label: '时效',
    component: 'Select',
    required:true,
    colProps: { span: 24 },
    componentProps: {
      options: [
        {  label: '15min',   value: 0.25,  },
        {  label: '30min',   value: 0.5,  },
        {  label: '1h',   value: 1,  },
        {  label: '2h',   value: 2,  },
        {  label: '4h',   value: 4,  },
        {  label: '8h',   value: 8,  },
        {  label: '12h',   value: 12,  },
        {  label: '24h',   value: 24,  },
      ],
   },
  },
  {
    field: 'sensitiveDesc',
    label: '描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]


/**
 * Ip 限制
 */

 export const accessIPColumns:BasicColumn[] = [
  {
    title:'IP地址',
    dataIndex:'ip',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
    },
  }
]
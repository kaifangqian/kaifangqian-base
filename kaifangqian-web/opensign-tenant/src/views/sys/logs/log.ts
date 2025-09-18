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
import { getSysOpeartionType,getSysWarning,getSysWarningLevel } from '/@/api/sys/log';
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';


export const operationColumns:BasicColumn[] = [
  {
    title:'位置',
    dataIndex:'logContent',
    slots: { customRender: 'logContent' },
    width:250
  },
  {
    title:'操作类型',
    dataIndex:'operateType',
    slots: { customRender: 'operateType' },
  },
  {
    title:'日志类型',
    dataIndex:'logStatus',
    slots: { customRender: 'logStatus' },
  },
  {
    title:'操作人账号',
    dataIndex:'username'
  },
  {
    title:'操作人姓名',
    dataIndex:'realname'
  },
  {
    title:'IP',
    dataIndex:'ip'
  },
  {
    title:'操作时间',
    dataIndex:'createTime'
  },
]

export const operationSearchFormSchema: FormSchema[] = [
  {
    field: 'realname',
    label: '操作人姓名',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'ip',
    label: 'IP',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'operateType',
    label: '操作类型',
    component: 'ApiSelect',
    componentProps:{
      api: getSysOpeartionType,
      resultField: 'result',
      labelField:'label',
      valueField:'value'
    },
    // componentProps: {
    //   options: [
    //     { label: '操作', value: 0 },
    //     { label: '查询', value: 1 },
    //     { label: '添加', value: 2 },
    //     { label: '修改', value: 3 },
    //     { label: '删除', value: 4 },
    //     { label: '导入', value: 5 },
    //     { label: '导出', value: 6 },
    //     { label: '登录', value: 7 },
    //     { label: '登出', value: 8 },
    //   ],
    // },
    colProps: { span:  6 },
  },
  {
    field: 'logStatus',
    label: '日志类型',
    component: 'Select',
    componentProps: {
      options: [
        { label: '正常', value: 1 },
        { label: '异常', value: -1 },
        { label: '告警', value: 0 },
        
      ],
    },
    colProps: { span:  6 },
  },
  {
    field: '[beginTime, endTime]',
    label: '操作时间',
    component: 'RangePicker',
    componentProps: {
      allowClear:false,
      format: 'YYYY/MM/DD',
      locale,
      placeholder: ['开始时间', '结束时间'],
    },
    colProps: { span:  6 },
  },
];


export const abnormalColumns:BasicColumn[] = [
  {
    title:'位置',
    dataIndex:'logContent',
    slots: { customRender: 'logContent' },
  },
  {
    title:'操作人账号',
    dataIndex:'username'
  },
  {
    title:'操作人姓名',
    dataIndex:'realname'
  },
  {
    title:'IP',
    dataIndex:'ip'
  },
  {
    title:'操作时间',
    dataIndex:'createTime'
  },
]
export const abnormalSearchFormSchema:FormSchema[] = [
  {
    field: 'realname',
    label: '操作人姓名',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'ip',
    label: 'IP',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: '[beginTime, endTime]',
    label: '操作时间',
    component: 'RangePicker',
    componentProps: {
      allowClear:false,
      format: 'YYYY/MM/DD',
      locale,
      placeholder: ['开始时间', '结束时间'],
    },
    colProps: { span:  6 },
  },
]
export const logInfoSchema:FormSchema[] = [
  {
    field: 'logContent',
    label: '',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps:{
      autoSize:{
        minRows: 10,
      },
      allowClear:true
    }
  },
 
]
export const securityColumns:BasicColumn[] = [
  {
    title:'警告内容',
    dataIndex:'warningName',
    width:200
  },
  {
    title:'位置',
    dataIndex:'logContent',
    slots: { customRender: 'logContent' },
    width:200
  },
  {
    title:'警告类型',
    dataIndex:'warningType',
    slots: { customRender: 'warningType' },
    width:100
  },
  {
    title:'警告级别',
    dataIndex:'warningLevel',
    slots: { customRender: 'warningLevel' },
    width:100
  },
  {
    title:'操作人账号',
    dataIndex:'username',
    width:150
  },
  {
    title:'操作人姓名',
    dataIndex:'realname',
    width:150
  },
  {
    title:'IP',
    dataIndex:'ip'
  },
  {
    title:'操作时间',
    dataIndex:'createTime'
  },
]
export const securitySearchFormSchema:FormSchema[] = [
 
  {
    field: 'warningType',
    label: '警告类型',
    component: 'ApiSelect',
    componentProps:{
      api: getSysWarning,
      resultField: 'result',
      labelField:'label',
      valueField:'value'
    },
    // componentProps: {
    //   options: [
    //     { label: '越权', value: '0' },
    //     { label: '高频访问', value: '1' },
    //   ],
    // },
    colProps: { span: 6 },
  },
  {
    field: 'warningLevel',
    label: '警告级别',
    component: 'ApiSelect',
    componentProps:{
      api: getSysWarningLevel,
      resultField: 'result',
      labelField:'label',
      valueField:'value'
    },
    // componentProps: {
    //   options: [
    //     { label: '告警', value: '0' },
    //     { label: '预警', value: '1' },
    //   ],
    // },
    colProps: { span: 6 },
  },
  {
    field: 'realname',
    label: '操作人姓名',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'IP',
    label: 'IP',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: '[beginTime, endTime]',
    label: '操作时间',
    component: 'RangePicker',
    componentProps: {
      allowClear:false,
      format: 'YYYY/MM/DD',
      locale,
      placeholder: ['开始时间', '结束时间'],
    },
    colProps: { span:  6 },
  },
]


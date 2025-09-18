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

export const certicifateColumn:BasicColumn[] = [
  {
    title:'颁发时间',
    dataIndex:'issueTime',
  },
  {
    title:'证书序列号',
    width:400,
    dataIndex:'certSuqeNo',
  },
  {
    title:'使用者',
    dataIndex:'userName',
  },
  {
    title:'证书类型',
    dataIndex:'certType',
    slots: { customRender: 'certType' },
  },
  {
    title:'颁发机构',
    dataIndex:'issueOrg',
  },
  {
    title:'加密算法',
    dataIndex:'algorithmType',
    slots: { customRender: 'algorithmType' },
  },
  // {
  //   title:'存储介质',
  //   dataIndex:'storageMedium',
  // },
  {
    title:'状态',
    dataIndex:'certStatus',
    slots: { customRender: 'certStatus' },
  },
  {
    title:'有效期',
    dataIndex:'cerRange',
    width:300,
    slots: { customRender: 'cerRange' },
  },
  
]

export const certificateSearchFormSchema:FormSchema[] = [
  {
    field: 'userName',
    label: '使用者名称',
    component: 'Input',
    required: false,
    colProps: { span: 6 },
  },
  {
    field: 'certType',
    label: '证书类型',
    component: 'Select',
    required: false,
    colProps: { span: 6 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'平台防篡改证书',value:1},
        {label:'测试数字证书',value:2},
        {label:'CA事件数字证书',value:3},
        {label:'CA长效数字证书',value:4},
      ]
    }
  },
  {
    field: 'certStatus',
    label: '状态',
    component: 'Select',
    required: false,
    colProps: { span: 6 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'有效',value:1},
        {label:'吊销',value:2},
        {label:'失效',value:3},
      ]
    }
  },
  {
    field: 'promulgateTime',
    label: '颁发时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
  },
  {
    field: 'invalidTime',
    label: '失效时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
  },
]

export const certificateEntSearchFormSchema:FormSchema[] = [
  {
    field: 'userName',
    label: '使用者名称',
    component: 'Input',
    required: false,
    colProps: { span: 6 },
  },
  {
    field: 'certType',
    label: '证书类型',
    component: 'Select',
    required: false,
    colProps: { span: 6 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'CA数字证书',value:4},
        {label:'平台防篡改证书',value:6},
      ]
    }
  },
  {
    field: 'certStatus',
    label: '状态',
    component: 'Select',
    required: false,
    colProps: { span: 6 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'有效',value:1},
        {label:'吊销',value:2},
        {label:'失效',value:3},
      ]
    }
  },
  {
    field: 'promulgateTime',
    label: '颁发时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
  },
  {
    field: 'invalidTime',
    label: '失效时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
  },
]




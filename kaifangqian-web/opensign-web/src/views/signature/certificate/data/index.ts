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
import type { Rule } from 'ant-design-vue/es/form';


export const registerColumns:BasicColumn[] =[
  {
    title:'颁发时间',
    dataIndex:'issueTime'
  },
  {
    title:'证书序列号',
    dataIndex:'certSuqeNo'
  },
  {
    title:'使用者',
    dataIndex:'sysTenantUserName'
  },
  {
    title:'证书类型',
    dataIndex:'holderType',
    slots: { customRender: 'holderType' },
  },
  {
    title:'颁发机构',
    dataIndex:'issue'
  },
  {
    title:'加密算法',
    dataIndex:'algorithmType',
    slots: { customRender: 'algorithmType' },
  },
  {
    title:'存储介质',
    dataIndex:'storageMedium'
  },
  {
    title:'状态',
    dataIndex:'validateStatus',
    slots: { customRender: 'validateStatus' },
  },
  {
    title:'有效期',
    dataIndex:'templateTitle',
    slots: { customRender: 'validityTime' },
  },
  {
    title:'操作',
    dataIndex:'action',
    flag:'ACTION',
    slots: { customRender: 'action' },
    align:'center',
    width:100
  },
]

export const registerSearchFormSchema: FormSchema[] = [
  {
    field: 'holderName',
    label: '使用者',
    component: 'Input',
    colProps: { span: 6 },
  },
  
  {
    field: 'holderType',
    label: '证书类型',
    component: 'Select',
    componentProps: {
      options: [
        { label: '个人', value: 1 },
        { label: '企业', value: 2 },
        
      ],
    },
    colProps: { span:  6 },
  },
  {
    field: 'validateStatus',
    label: '证书状态',
    component: 'Select',
    componentProps: {
      options: [
        { label: '有效', value: 1 },
        { label: '失效', value: 2 },
      ],
    },
    colProps: { span:  6 },
  },
];

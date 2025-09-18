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

import {getMyTenantDeparts} from "/@/api/sys/user"
export const registerColumns:BasicColumn[] =[
  {
    title:'签名图片',
    dataIndex:'annexId',
    slots: { customRender: 'annexId' },
    width:350,
  },
  {
    title:'签名名称',
    dataIndex:'sealName'
  },

  {
    title:'创建时间',
    dataIndex:'createTime',
    slots: { customRender: 'createTime' },
    // width:200,
  },
  // {
  //   title:'是否为默认',
  //   dataIndex:'isDefault',
  //   slots: { customRender: 'isDefault' },
  //    width:100
  // },
  
  {
    title:'操作',
    dataIndex:'action',
    flag:'ACTION',
    slots: { customRender: 'action' },
    align:'center',
    // width:100
  },
]


export const signatureAuthColumns:BasicColumn[] =[
  {
    title:'授权企业',
    dataIndex:'tenantName'
  },
  {
    title:'签署业务',
    dataIndex:'signReName',
    slots: { customRender: 'signReName' }
  },
  {
    title:'授权签名',
    dataIndex:'annexId',
    slots: { customRender: 'annexId' },
  },
  {
    title:'授权截止日期',
    dataIndex:'authTime',
    slots: { customRender: 'authTime' },
    width:120,
  },
  {
    title:'授权状态',
    dataIndex:'authStatus',
    slots: { customRender: 'authStatus' },
    width:100
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


export const formSchema: FormSchema[] = [
  {
    field: 'tenantId',
    label: '授权企业',
    component: 'Select',
    slot:'folderId',
    required: true,
    labelWidth:100,
    colProps: { span: 24 },
    componentProps:{
    }
  },
]

export const registerSearchFormSchema: FormSchema[] = [];



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

export const authorizationColumn:BasicColumn[] = [
  {
    title:'开发者名称',
    dataIndex:'developerName',
    slots: { customRender: 'developerName' },
  },
  {
    title:'企业名称',
    width:400,
    dataIndex:'tenantName',
  },
  {
    title:'状态',
    dataIndex:'status',
    slots: { customRender: 'status' },
  },
  {
    title:'创建时间',
    dataIndex:'createTime',
  },
  {
    title:'操作',
    width:200,
    dataIndex:'action',
    slots: { customRender: 'action' },
    
  },
]

export const authorizationFormSchema:FormSchema[] = [
  {
    field: 'developerName',
    label: '开发者名称',
    component: 'Input',
    required: false,
    colProps: { span: 6 },
  },
 
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    required: false,
    colProps: { span: 6 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'启用',value:1},
        {label:'停用',value:0},
      ]
    }
  }
]

export const createFormSchema:FormSchema[] = [
  {
    field: 'id',
    label: '开发者简称',
    component: 'Input',
    show: false,
    colProps: { span: 22 },
  },
  {
    field: 'developerName',
    label: '开发者简称',
    component: 'Input',
    required: true,
    colProps: { span: 22 },
  },
  {
    field: 'callbackUrl',
    label: '回调地址',
    component: 'Input',
    required: true,
    colProps: { span: 22 },
  },
  {
    field: 'tenantId',
    label: '所属企业',
    component: 'Input',
    required: true,
    slot:'tenantId',
    colProps: { span: 22 },
  },
  /* {
    field: 'tenantId',
    label: '所属企业',
    component: 'ApiSelect',
    colProps: { span: 12 },
    required:true,
    slot:'tenantId',
    componentProps:{
      api: getAuthGroup,
      resultField: 'result',
      labelField:'groupName',
      valueField:'id'
    }
  }, */
  
  {
    field: 'publicKey',
    label: '公钥',
    component: 'InputTextArea',
    required: true,
    colProps: { span: 22 },
    componentProps: {
       disabled: false,
       autoSize:{ minRows: 5, maxRows: 24 }
    },
  },
   
]




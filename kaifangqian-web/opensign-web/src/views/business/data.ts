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

export const businessColumns:BasicColumn[] =[
  {
    title:'业务线名称',
    dataIndex:'name',
  },
  {
    title:'管理员',
    dataIndex:'managerList',
    slots: { customRender: 'managerList' },
    width:300,
  },
  {
    title:'类型',
    dataIndex:'folderName',
    width:180,
  },
  {
    title:'状态',
    dataIndex:'status',
    width:180,
    align:'center',
    slots: { 
      customRender: 'status',
    },
  },
  {
    title:'操作',
    dataIndex:'action',
    width:200,
    align:'center',
    slots: { customRender: 'action' },
  },
]


export const registerSearchFormSchema: FormSchema[] = [
  
  {
    field: 'name',
    label: '业务线名称',
    component: 'Input',
    required:false,
    colProps: { span: 8 },
  },
  {
     field: 'status',
     label: '状态',
     component: 'Select',
     colProps: { span: 8 },
     componentProps:{
       options:[
         {label:'全部',value:''},
         {label:'启用',value:'1'},
         {label:'停用',value:'2'},
       ]
     }
   },
  
]


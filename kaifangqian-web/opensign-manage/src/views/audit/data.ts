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

export const tableColumn:BasicColumn[] = [
  {
    title:'法人单位名称',
    dataIndex:'name',
  },
  // {
  //   title:'法定代表人',
  //   dataIndex:'corporation',
  // },
  // {
  //   title:'认证类型',
  //   dataIndex:'authType',
  //   slots: { customRender: 'authType' },
  // },
  {
    title:'事项',
    dataIndex:'realItem',
    slots: { customRender: 'realItem' },
  },
  // {
  //   title:'申请人',
  //   dataIndex:'applyUser',
  // },
  {
    title:'申请时间',
    dataIndex:'applyTime',
  },
  {
    title:'审核状态',
    dataIndex:'authStatus',
    slots: { customRender: 'authStatus' },
  },
  {
    title:'审核人',
    dataIndex:'checkUser',
  },
  {
    title:'操作',
    dataIndex:'action',
    
    slots: { customRender: 'action' },
  },
  
]

export const tableSearchFormSchema:FormSchema[] = [
  {
    field: 'name',
    label: '法人单位名称',
    component: 'Input',
    required: false,
    colProps: { span: 6 },
  },
  

  {
    field: 'applyTime',
    label: '时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
    componentProps: {
      // format: 'YYYY-MM-DD HH:mm:ss',
      placeholder: ['开始时间', '结束时间'],
    },
  },
]
export const rejcetFormSchema:FormSchema[] = [
  {
    field: 'checkMsg',
    label: '驳回原因',
    component: 'InputTextArea',
    required: true,
    colProps: { span: 24 },
    componentProps:{
      autoSize:{ minRows: 8, maxRows: 34 }
    }
  },
]


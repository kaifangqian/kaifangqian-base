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

export const personalColumn: BasicColumn[] = [
  {
    title: '姓名',
    dataIndex: 'name',
  },
  {
    title: '手机号',
    dataIndex: 'phone',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  // {
  //   title:'证件号',
  //   dataIndex:'organizationNo',
  // },
  {
    title: '认证状态',
    dataIndex: 'authStatus',
    slots: { customRender: 'authStatus' },
  },
  {
    title: '租户状态',
    dataIndex: 'tenantStatus',
    slots: { customRender: 'tenantStatus' },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    dataIndex: 'action',
    slots: { customRender: 'action' },
  },
];

export const personalSearchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '姓名',
    component: 'Input',
    required: false,
    colProps: { span: 6 },
  },
  {
    field: 'tenantStatus',
    label: '状态',
    component: 'Select',
    required: false,
    colProps: { span: 6 },
    componentProps: {
      options: [
        { label: '启用', value: 1 },
        { label: '停用', value: 2 },
      ],
    },
  },
  {
    field: 'createTime',
    label: '时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
  },
];

export const enterpriseColumn: BasicColumn[] = [
  {
    title: '法人单位名称',
    dataIndex: 'name',
  },
  {
    title: '法定代表人',
    dataIndex: 'corporation',
  },
  {
    title: '认证状态',
    dataIndex: 'authStatus',
    slots: { customRender: 'authStatus' },
  },
  // {
  //   title:'用户类型',
  //   dataIndex:'sysType',
  //   slots: { customRender: 'sysType' },
  // },
  {
    title: '租户状态',
    dataIndex: 'tenantStatus',
    slots: { customRender: 'tenantStatus' },
  },
  {
    title: '认证申请人',
    dataIndex: 'applyUserName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    dataIndex: 'action',
    slots: { customRender: 'action' },
  },
];

export const enterpriseSearchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '法人单位名称',
    component: 'Input',
    required: false,
    colProps: { span: 4 },
  },
  {
    field: 'tenantStatus',
    label: '状态',
    component: 'Select',
    required: false,
    colProps: { span: 4 },
    componentProps: {
      options: [
        { label: '全部', value: null },
        { label: '启用', value: 1 },
        { label: '停用', value: 2 },
      ],
    },
  },
  // {
  //   field: 'sysType',
  //   label: '用户类型',
  //   component: 'Select',
  //   required: false,
  //   colProps: { span: 4 },
  //   componentProps: {
  //     options: [
  //       { label: '全部', value: '' },
  //       { label: '核心企业', value: 'core' },
  //       { label: '普通企业', value: 'base' },
  //     ],
  //   },
  // },
  {
    field: 'createTime',
    label: '时间',
    component: 'RangePicker',
    required: false,
    colProps: { span: 6 },
  },
];

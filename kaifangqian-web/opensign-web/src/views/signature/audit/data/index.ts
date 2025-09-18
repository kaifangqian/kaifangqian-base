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

import {sealType} from "../../seal/data"

export const registerColumns:BasicColumn[] =[
  {
    title:'印章名称',
    dataIndex:'sealName'
  },
  {
    title:'印章类型',
    dataIndex:'sealType',
    slots: { customRender: 'sealType' },
  },
  {
    title:'制作完成时间',
    dataIndex:'createTime'
  },
  {
    title:'变更',
    dataIndex:'changeCount',
    slots: { customRender: 'changeCount' },
  },
  {
    title:'停用',
    dataIndex:'stopCount'
  },
  {
    title:'收缴',
    dataIndex:'collectionCount',
    slots: { customRender: 'collectionCount' },
  },
  {
    title:'激活',
    dataIndex:'activateCount'
  },
  {
    title:'当前状态',
    dataIndex:'sealStatus',
    slots: { customRender: 'sealStatus' },
  }
]

export const registerSearchFormSchema: FormSchema[] = [
  {
    field: 'sealType',
    label: '印章类型',
    component: 'Select',
    componentProps: {
      options: [
        { label: '全部', value: "" },
        ...sealType
      ],
    },
    colProps: { span:  6 },
  },
  {
    field: 'sealName',
    label: '印章名称',
    component: 'Input',
    colProps: { span: 6 },
  },
];

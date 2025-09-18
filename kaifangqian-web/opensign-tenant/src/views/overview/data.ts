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

export const memberJoinColumn:BasicColumn[] = [
  {
    title:'申请人',
    dataIndex:'nickName',
  },
  {
    title:'手机号',
    dataIndex:'phone',
  },
  {
    title:'邮箱',
    dataIndex:'email',
  },
  {
    title:'申请时间',
    dataIndex:'applyTime',
  },
  {
    title:'审核状态',
    dataIndex:'status',
    slots: { customRender: 'status' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
  },
  
]
export const recordColumn:BasicColumn[] = [
  {
    title:'法人单位名称',
    dataIndex:'name',
  },
  {
    title:'法定代表人',
    dataIndex:'corporation',
  },
  {
    title:'认证类型',
    dataIndex:'authType',
    slots: { customRender: 'authType' },
  },
  {
    title:'事项',
    dataIndex:'realItem',
    slots: { customRender: 'realItem' },
  },
  {
    title:'申请人',
    dataIndex:'applyUser',
  },
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
    title:'审核时间',
    dataIndex:'checkTime',
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
  },
  
]
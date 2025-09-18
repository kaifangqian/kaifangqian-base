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
import { getAuthGroup } from '/@/api/auth/group'; 



export const tableSealDivestedColumns:BasicColumn[] =[
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
    title:'申请人',
    dataIndex:'applierName'
  },
  {
    title:'申请时间',
    dataIndex:'applyTime'
  },
  {
    title:'状态',
    dataIndex:'applyStatus',
    slots: { customRender: 'applyStatus' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    flag:'ACTION',
    align:'center',
  },
]

export const tableSealMakeColumns:BasicColumn[] =[
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
    title:'制作方式',
    dataIndex:'createType',
    slots: { customRender: 'createType' },
  },
  {
    title:'申请人',
    dataIndex:'applierName'
  },
  {
    title:'申请时间',
    dataIndex:'applyTime'
  },
  {
    title:'状态',
    dataIndex:'applyStatus',
    slots: { customRender: 'applyStatus' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    flag:'ACTION',
    align:'center',
  },
]

export const tableSealStateColumns:BasicColumn[] =[
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
    title:'操作类型',
    dataIndex:'operateType',
    slots: { customRender: 'operateType' },
  },
  {
    title:'申请人',
    dataIndex:'applierName'
  },
  {
    title:'申请时间',
    dataIndex:'applyTime'
  },
  {
    title:'状态',
    dataIndex:'applyStatus',
    slots: { customRender: 'applyStatus' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    flag:'ACTION',
    align:'center',
  },
]

export const tableSealEditColumns:BasicColumn[] =[
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
    title:'操作人',
    dataIndex:'applierName'
  },
  {
    title:'操作时间',
    dataIndex:'applyTime'
  },
]

export const SealMakeData = [
  {
    sealName:"发票专用章",
    sealType:"1",
    applierName:"admin",
    applyTime:"2023年8月17日19:27:05",
    sealStatus:"申请中",
  }
]

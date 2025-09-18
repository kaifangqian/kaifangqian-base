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

export const tableOperateColumns:BasicColumn[] =[
  {
    title:'操作人',
    dataIndex:'sysTenantUserName'
  },
  {
    title:'操作时间',
    dataIndex:'operateTime'
  },
  {
    title:'操作事项',
    dataIndex:'operateName',
  }
]

export const tableApproveColumns:BasicColumn[] =[
  {
    title:'申请人',
    dataIndex:'sysTenantUserName'
  },
  {
    title:'申请时间',
    dataIndex:'applyTime',
  },
  {
    title:'申请类型',
    dataIndex:'operateName',
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

export const tableUseColumns:BasicColumn[] =[
  {
    title:'系统授权标识',
    dataIndex:'sealName'
  },
  {
    title:'调用时间',
    dataIndex:'sealType',
    slots: { customRender: 'sealType' },
  },
]


export const ProcessApplyState = [
  {
    value:1,
    label:'待提交'
  },
  {
    value:2,
    label:'待重新提交'
  },
  {
    value:3,
    label:'待审批'
  },
  {
    value:4,
    label:'审批未通过'
  },
  {
    value:5,
    label:'审批通过'
  },
  {
    value:6,
    label:'作废'
  },
]
export const getProcessApplyState=(value:number)=>{
  	let result =  ProcessApplyState.filter((item:any) => {
  			return item.value == value 
  	})[0]
    if(result){
      return result;
    }else{
      return {};
    }
}

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

export enum SealRecordType{
  SealMake = 1,
  SealEdit = 2,
  SealChange = 3,
  SealStart = 4,
  SealStop = 5,
  SealDivested = 6,
  SealDestruction = 7,
}


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

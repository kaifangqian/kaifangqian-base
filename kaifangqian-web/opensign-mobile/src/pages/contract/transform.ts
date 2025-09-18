/**
 * @description : 状态转换
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
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
export function loadRuStatus(status:number){
  switch(status){
    case 1:
      return '草稿';
    case 2:
      return '发起审批中';
    case 3:
      return '发起审批不通过';
    case 4:
      return '已删除';
    case 5:
      return '填写中';
    case 6:
      return '已拒填';
    case 7:
      return '签署中';
    case 8:
      return '已拒签';
    case 9:
      return '已失效';
    case 10:
      return '已撤回';
    case 11:
      return '已完成';
    case 12:
      return '已发起';
    default:
      return '';
  }
}
export function loadWriteStatus(status:number){
  switch(status){
    case 0:
      return '无需填写';
    case 1:
      return '未填写';
    case 2:
      return '待填写';
    case 3:
      return '已填写';
    case 4:
      return '已拒填';
    default:
        return '';
    
  }
}
export function loadSignColor(status:number){
  switch(status){
    case 0:
      return '#726464';
    case 1:
      return '#726464';
    case 2:
      return '#b55f0a';
    case 3:
      return '#7ab140';
    case 4:
      return '#eb620f';
    default:
        return '#108ee9';
    
  }
}

export function loadSignStatus(status:number){
  switch(status){
    case 0:
      return '无需签署';
    case 1:
      return '未签署';
    case 2:
      return '待签署';
    case 3:
      return '已签署';
    case 4:
      return '已拒签';
    default:
        return '';
    
  }
}

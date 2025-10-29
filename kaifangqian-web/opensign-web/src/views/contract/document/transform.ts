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

export function loadRuStatus(status){
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

export function getRuStatusTagProps(status: number) {
  switch (status) {
    case 1: // 草稿
      return { type: 'default', color: '#909399', plain: false };
    case 2: // 发起审批中
      return { type: 'primary', color: '#1989fa', plain: false };
    case 3: // 发起审批不通过
      return { type: 'danger', color: '#ee0a24', plain: false };
    case 4: // 已删除
      return { type: 'default', color: '#c0c4cc', plain: true };
    case 5: // 填写中
      return { type: 'warning', color: '#ff976a', plain: false };
    case 6: // 已拒填
      return { type: 'danger', color: '#ee0a24', plain: false };
    case 7: // 签署中
      return { type: 'primary', color: '#1989fa', plain: false };
    case 8: // 已拒签
      return { type: 'danger', color: '#ee0a24', plain: false };
    case 9: // 已失效
      return { type: 'default', color: '#c0c4cc', plain: true };
    case 10: // 已撤回
      return { type: 'default', color: '#c0c4cc', plain: true };
    case 11: // 已完成
      return { type: 'success', color: '#07c160', plain: false };
    case 12: // 已发起
      return { type: 'primary', color: '#1989fa', plain: false };
    default:
      return { type: 'default', color: '#909399', plain: true };
  }
}
export function loadWriteStatus(status){
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
export function loadSignColor(status){
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

export function loadSignStatus(status){
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

  export function loadApprovalColor(status){
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

export function loadApprovalStatus(status){
  switch(status){
    case 0:
      return '无需审批';
    case 1:
      return '未审批';
    case 2:
      return '待审批';
    case 3:
      return '审批通过';
    case 4:
      return '审批不通过';
    default:
        return '';
    
  }
}

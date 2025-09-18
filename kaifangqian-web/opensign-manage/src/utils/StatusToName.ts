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

export function loadCertificationStatus(status){
  switch(status){
    case 0:
      return '';
    case 1:
      return 'orange';
    case 2:
      return 'green';
    case 3:
      return 'red';
    default:
      return '';
  }
}
export function loadCertificationText(status){
  switch(status){
    case 0:
      return '未认证';
    case 1:
      return '认证审核中';
    case 2:
      return '已认证';
    case 3:
      return '认证审核失败';
    default:
      return '未认证';
  }
}
export function loadCertificationAuthType(status){
  switch(status){
    case 1:
      return '实名认证';
    case 2:
      return '认证变更';
    case 3:
      return '实名认证';
    default:
      return '实名认证';
  }
}
export function loadCertificationRealItemType(status){
  switch(status){
    case 1:
      return '实名认证';
    case 2:
      return '企业名称变更';
    case 3:
      return '企业法人变更';
    case 4:
      return '企业主体变更';
    default:
      return '实名认证';
  }
}
export function loadCertificationMixText(record){
  if(record.authStatus==0 || !record.authStatus ){
    return '未认证'
  }else if(record.authStatus == 1 && (record.authType == 1 ||  record.authType == 3) && record.realItem == 1){
    return '认证审核中'
  }else if(record.authStatus == 1 && (record.authType == 1 ||  record.authType == 3) && record.realItem != 1){
    return '变更认证审核中'
  }else if(record.authStatus == 1 && record.authType == 2){
    return '变更认证审核中'
  }else if(record.authStatus == 2 && record.authType == 1){
    return '认证通过'
  }else if(record.authStatus == 2 && record.authType == 2){
    return '变更认证通过'
  }else if(record.authStatus == 2 && record.authType == 3){
    return '认证通过'
  }else if(record.authStatus == 3 && record.authType == 1){
    return '认证审核失败'
  }else if(record.authStatus == 3 && record.authType == 2){
    return '变更审核失败'
  }else if(record.authStatus == 3 && record.authType == 3){
    return '认证审核失败'
  }
}

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
      return '#ff9900';
    case 1:
      return '#ed952d';
    case 2:
      return '#19be6b';
    case 3:
      return '#f56c6c';
    default:
      return '#ff9900';
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
      return '企业主体变更';
  }
}
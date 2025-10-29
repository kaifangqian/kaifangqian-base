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

export function loadInitiatorSignerType(type){
  switch (type) {
    case 1:
      return '经办人签字';
    case 2:
      return '法人签字';
    case 3:
      return '个人签字';
    case 4:
      return '组织签章';
    case 5:
      return '个人审批';
    default:
      return '';
  }
}

export function loadControlIcon(type){
  switch (type) {
    case 'seal':
      return 'ant-design:node-index-outlined';
    case 'sign-date':
      return 'ant-design:calendar-outlined';
  }
}
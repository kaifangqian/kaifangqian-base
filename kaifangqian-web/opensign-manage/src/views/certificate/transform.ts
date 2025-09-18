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

export function loadCerStatus(status){
  switch(status){
    case 1:
      return  '有效';
    default:
      return '失效'
  }
}
export function loadCerAlgorithmType(status){
  switch(status){
    case 'RSA':
      return  'RSA';
    case 'SM2':
      return  'SM2';
    default:
      return ''
  }
}
export function loadCerType(status){
  switch(status){
    case 1:
      return  '平台防篡改证书';
    case 2:
      return  '测试数字证书';
    case 3:
      return  'CA事件数字证书';
    case 4:
      return  'CA长效数字证书';
    default:
      return ''
  }
}

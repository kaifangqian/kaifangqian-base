/**
 * @description 云盾认证API
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
import {isOrganizationCode ,isIdNumber, isMobile} from '@/utils/validate';

export const validateOrganizationCode =  (value: string) =>{
  if(!value){
    return "请输入组织机构代码";
  }
  if(!isOrganizationCode(value)){
    return '统一社会信用代码格式不正确';
  }
  return null;
}

export const validateIdNumber =  (value: string) =>{
  if(!value){
    return '请输入证件号';
  }
  if(!isIdNumber(value)){
    return '证件号格式不正确';
  }
  return Promise.resolve(); 
}
export const validatePhone=  (value: string) =>{
  if(!value){
    return '请输入手机号';
  }
  if(!isMobile(value)){
    return '手机号格式不正确';
  }
  return Promise.resolve(); 
}
export const validateSms=  (value: string) =>{
  if(!value){
    return '请输入验证码';
  }
  if(!(/^(?:\d{4}|\d{6})$/.test(value))){
    return '验证码格式不正确';
  }
}
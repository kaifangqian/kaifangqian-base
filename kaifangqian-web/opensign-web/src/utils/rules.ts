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

import type { Rule } from 'ant-design-vue/es/form';

import { isMobile, isEmail } from '/@/utils/validate';

export const validateMobile =  (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入手机号');
  }
  if(!isMobile(value)){
    return Promise.reject('手机号格式不正确');
  }else{
    return Promise.resolve(); 
  }
}
export const validateEmail =  (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入邮箱');
  }
  if(!isEmail(value)){
    return Promise.reject('邮箱格式不正确');
  }else{
    return Promise.resolve(); 
  }
}
export const validatePassword =  (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入密码');
  }
  return Promise.resolve(); 
}

export const validateSmscode = (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入验证码');
  }
  // if(value && !/^\d{6}$/.test(value)){
  //   return Promise.reject('请输入6位数字'); 
  // }
  return Promise.resolve(); 
}
export const validateSignPssword = (_rule: Rule, value: string) =>{
  if(!value){
    return Promise.reject('请输入签署密码');
  }
  if(value && !/^\d{6}$/.test(value)){
    return Promise.reject('密码须为6位数字'); 
  }
  return Promise.resolve(); 
}

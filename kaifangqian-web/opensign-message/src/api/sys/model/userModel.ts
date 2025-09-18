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

/**
 * @description: Login interface parameters
 */

import type {  TenantDepartList } from '/#/store';

export interface LoginParams {
  username?: string;
  password?: string;
  checkKey?: number;
  captcha?: string;
  phone?: string | number | null;
  captchaKey?: string;
  type?: string;

}

export interface RoleInfo {
  roleName: string;
  value: string;
}

export interface DeptInfo {
  id:string;
  departName:string;
  orgCode:string;
  [propName: string]: any;
}

/**
 * @description: Login interface return value
 */
export interface LoginResultModel {
  userId: string | number;
  token: string;
  role: RoleInfo;
  departs:DeptInfo;
  username: string;
  multi_depart:number;
  user_tenant_depart:TenantDepartList[]

}

/**
 * @description: Get user information return value
 */
export interface GetUserInfoModel {
  parentId?: any;
  // 密码是否需要修改
  passwordEditFlag?: boolean;
  roles?: RoleInfo[];
  // 用户id
  userId?: string | number;
  // 用户名
  username?: string;
  // 真实名字
  realName?: string;
  // 头像
  avatar?: string;
  // 介绍
  desc?: string;
  // 部门
  departs?:DeptInfo[];
  // 部门数量1 单个 2 多个
  multi_depart?:number,
  user_tenant_depart:TenantDepartList[]

}

/**
 * @description: getAccountList
 */

export interface TenantParams{
  departId:string;
  departName?:string;
}
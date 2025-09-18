/**
 * @description : 全局状态
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
// Lock screen information
export interface LockInfo {
  // Password required
  pwd?: string | undefined;
  // Is it locked?
  isLock?: boolean;
}
 
// export interface TenantInfo {
//   departId:string;
//   departName:string;
//   selectFlag:boolean;
//   tenantId:string | null;
// }
export interface TenantInfo {
  tenantId?: string ;
  tenantType?: string;
  id?:string;
  tenantName?: string;
  organizationNo?: string;
  corporation?: string;
  corporationNo?: string;
  phone?: string;
  email?: string;
  tenantProvince?: string;
  tenantCity?: string;
  tenantDistrict?:string;
  organizationAddress?:string;
  lifespanType?:string;
  contactsName?:string;
  contactsEmail?:string;
  contactsPhone?:string;
  authStatus?:string;
  idPic1:any,
  idPic2:any,
  organizationPic:any,
  [key: string]: string;
}

export interface TenantDepartList {
  tenantId:string;
  tenantName:string;
  selectFlag?:boolean;
  departs:TenantInfo[],
  useFlag?:boolean
  
}

export interface UserInfo {
  loginDepartId?: any;
  loginTenantId?: string | null;
  loginTenantType?: string | null;
  jobAppId?: string;
  id?:string;
  userId?: string | number;
  username?: string;
  realname?: string;
  avatar?: string;
  avatarImg?: string;
  desc?: string;
  homePath?: string;
  roles?: any;
  phone?:string;
  email?:string;
  departNames?:[];
  roleNames?:[];
  createTime?:[];
  passwordEditFlag?:boolean;
  initUserInfo?:boolean;
  passwordLevel?:string;
  tenantUserId?:string;
  loginTenantName?:string;
  loginDepartName?:string;
  personalTenantFlag?:boolean;
  nickName?:string;
  // [key: string]: string;
}

 

export interface SafeInfo {
  phone?: string | number;
  username: string | number;
}

export interface PerInfo {
  authList:[],
  menuTree:[]
}

export interface SensitiveHeaderState {
  sensitivePassword?:string;
  sensitiveTelepon?:string;
  sensitiveCaptch?:string;
  sensitiveCaptchKey?:string;
}

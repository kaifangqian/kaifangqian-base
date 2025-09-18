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

import { defHttp } from '/@/utils/http/axios';

enum Api {
  ApplicationList = '/sys/getMyTenantAPPs',
  OpenPersonalTenant = '/system/sysTenantInfo/openPersonalTenant',
  getTenantApps = '/system/sysTenantInfo/getTenantAllApps',
  AddPersonalTenantAppVersion = '/system/sysTenantInfo/addPersonalTenantAppVersion',
  JionTenant = '/system/sysTenantInfo/jionTenant',
  RegisterTenant = '/system/tenantInfoExtend/tenantRegister',
  RegisterPersonalTenant = '/sys/personalTenantRegister',
  RegisterTenantAuth = '/copyright/tenantInfoExtend/submitAuth',
  RegisterTenantInfo = '/copyright/tenantInfoExtend/queryById',
  QuickAppTree = '/copyright/tenantInfoExtend/queryById',
  
}

/**
 * @description: 应用列表
 */
export function getApplication(params) {
  return defHttp.get({ url: Api.ApplicationList,params });
}
/**
 * @description: 快捷操作应用列表
 */
export function getQuickAppTree(params) {
  return defHttp.get({ url: Api.QuickAppTree,params });
}
/**
 * @description: 开通个人租户空间
 */
export function openPersonalTenant(params) {
  return defHttp.put({ url: Api.OpenPersonalTenant,params });
}
/**
 * @description: 个人应用市场
 */
export function getTenantApps(params) {
  return defHttp.get({ url: Api.getTenantApps,params });
}
/**
 * @description: 个人租户新增应用
 */
export function addPersonalTenantAppVersion(params) {
  return defHttp.put({ url: Api.AddPersonalTenantAppVersion,params });
}
/**
 * @description: 加入已有企业
 */
export function jionTenant(params) {
  return defHttp.put({ url: Api.JionTenant,params},{ errorMessageMode: 'none', isReturnNativeResponse:true });
}
/**
 * @description: 租户注册
 */
export function registerTenant(params) {
  return defHttp.put({ url: Api.RegisterTenant,params });
}
/**
 * @description: 租户实名认证
 */
export function registerTenantAuth(params) {
  return defHttp.post({ url: Api.RegisterTenantAuth,params });
}
/**
 * @description: 租户实名认证详情
 */
export function registerTenantInfo(params) {
  return defHttp.get({ url: Api.RegisterTenantInfo,params });
}
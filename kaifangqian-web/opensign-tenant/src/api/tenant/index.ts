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
  
  ApplicationUpdateStatus = '/system/sysTenantApp/updateStatus',
  ApplicationUpdateUseful = '/system/sysTenantApp/updateUseful',
  ApplicationInfo = '/system/sysTenantApp/queryById',
  ApplicationList = '/sys/getMyTenantAPPs',
  OpenPersonalTenant = '/system/sysTenantInfo/openPersonalTenant',
  getTenantApp = '/system/sysTenantInfo/getTenantAllApps',
  AddPersonalTenantAppVersion = '/system/sysTenantInfo/addPersonalTenantAppVersion',
  TenantApps = '/system/sysTenantApp/list',
  JionTenant = '/system/sysTenantInfo/jionTenant',
}

/**
 * @description: 应用详情
 */
export function getApplicationInfo(params) {
  return defHttp.get({ url: Api.ApplicationInfo,params });
}
/**
 * @description: 停用、启用应用
 */
export function updateAppStatus(params) {
  return defHttp.put({ url: Api.ApplicationUpdateStatus,params },{isTransformResponse:false});
}
/**
 * @description: 修改范围
 */
export function updateAppUseful(params) {
  return defHttp.put({ url: Api.ApplicationUpdateUseful,params });
}


/**
 * @description: 应用列表
 */
export function getTenantApp(params) {
  return defHttp.get({ url: Api.TenantApps,params });
}
/**
 * @description: 开通个人租户空间
 */
export function openPersonalTenant(params) {
  return defHttp.put({ url: Api.OpenPersonalTenant,params });
}
// /**
//  * @description: 个人应用市场
//  */
// export function getTenantApps(params) {
//   return defHttp.get({ url: Api.getTenantApp,params });
// }
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
  return defHttp.put({ url: Api.JionTenant,params });
}
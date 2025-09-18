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
import { getLoginToken, getToken } from '/@/utils/auth';

enum Api {
  AppList = '/app/list',
  AppAdd = '/app/add',
  AppUpdate = '/app/update',
  DeleteApp = '/app/delete',
  AppVersionList = 'app/version/list',
  AppInfo = '/system/sysAppInfo/queryById',
}

/**
 * @description: 应用列表
 */
export function getAppList(params) {
  return defHttp.get({ url: Api.AppList,params });
}
/**
 * @description: 新增应用
 */
export function addApp(params) {
  return defHttp.post({ url: Api.AppAdd,params });
}
/**
 * @description: 删除应用
 */
export function deleteApp(params) {
  return defHttp.delete({ url: Api.DeleteApp,params });
}
/**
 * @description: 编辑应用
 */
export function updateApp(params) {
  return defHttp.post({ url: Api.AppUpdate,params });
}
/**
 * @description: 查询应用信息
 */
export function getAppInfoById(params) {
  const loginToken:string = getLoginToken() as string;
  return defHttp.get({ url: Api.AppInfo, params },{withToken:false, loginToken:loginToken });
}
/**
 * @description: 查询应用版本
 */
export function getAppVersionList(params) {
  return defHttp.get({ url: Api.AppVersionList, params });
}

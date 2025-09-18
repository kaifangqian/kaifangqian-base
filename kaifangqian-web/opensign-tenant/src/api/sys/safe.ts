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

/*
 * @Author: ningw 
 * @Date: 2022-07-12 16:39:01 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-08-08 19:25:49
 */

import { defHttp } from '/@/utils/http/axios';


enum Api {
  SafeSensitive = '/api/monitorConfig/list',
  SafeSensitiveAdd = '/api/monitorConfig/add',
  SafeSensitiveEdit = '/api/monitorConfig/edit',
  SafeSensitiveDelete = '/api/monitorConfig/deleteBatch',
  SafeIpList = '/api/monitorBlacklist/info',
  SafeIpAdd = '/api/monitorBlacklist/add',
  SafeIpEdit = '/api/monitorBlacklist/edit',

  SafeConfig = '/api/sysConfig',
  SysConfigNoAuth = '/api/sysConfig/passwordComposition',
  SysConfigPasswordLength = '/api/sysConfig/passwordMinimumLen',

 


}


/**
 * @description: 敏感操作列表
 */
export function getSensitiveList(params) {
  return defHttp.get({ url: Api.SafeSensitive, params }, { errorMessageMode: 'none' });
}

/**
 * @description: 配置敏感操作
 */
export function addSensitive(params) {
  return defHttp.post({ url: Api.SafeSensitiveAdd, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 更新敏感操作
 */
export function updateSensitive(params) {
  return defHttp.put({ url: Api.SafeSensitiveEdit, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 删除敏感操作
 */
export function deleteSensitive(params) {
  return defHttp.delete({ url: Api.SafeSensitiveDelete, params }, { errorMessageMode: 'none' });
}


/**
 * @description: 限制ip 列表
 */
export function getIpList(params) {
  return defHttp.get({ url: Api.SafeIpList, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 新增限制ip 列表
 */
export function addIpLimit(params) {
  return defHttp.post({ url: Api.SafeIpAdd, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 编辑限制ip 列表
 */
export function updateIpList(params) {
  return defHttp.put({ url: Api.SafeIpEdit, params }, { errorMessageMode: 'none' });
}

export function getSafeConfig (params) {
  return defHttp.get({url:Api.SafeConfig,params})
}

export function setSafeConfig (params) {
  return defHttp.post({url:Api.SafeConfig,params})
}

export function getSystemPasswordConfig (params) {
  return defHttp.get({url:Api.SysConfigNoAuth,params})
}
export function getSystemPasswordLength () {
  return defHttp.get({url:Api.SysConfigPasswordLength})
}


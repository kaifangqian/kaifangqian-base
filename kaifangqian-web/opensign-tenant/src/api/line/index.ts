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
  LineList = '/sys/dict/treeList',
  LineCategoryTree = '/sys/dict/treeList',
  LineConfigInfo = '/sys/dict/treeList',
  LineConfigSave = '/sys/dict/treeList',
}
/**
 * @description: 业务线分组列表
 */
export function getLineTree(params) {
  return defHttp.get({ url: Api.LineCategoryTree,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 业务线列表
 */
export function getBusinessLine(params) {
  return defHttp.get({ url: Api.LineList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 业务线配置信息
 */
export function getLineConfigInfo(params) {
  return defHttp.get({ url: Api.LineConfigInfo,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 保存业务线配置信息
 */
export function saveLineConfig(params) {
  return defHttp.post({ url: Api.LineConfigSave,params }, { errorMessageMode: 'none' });
}
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

/**
 *
 *
 * @param params
 * @returns
 */
export function silentQueryApi(params) {
  return defHttp.get({ url: '/yundun/sign/silent/service/info/query', params });
}

export function silentQueryRecordApi(params) {
  return defHttp.get({ url: '/yundun/sign/silent/service/record/query', params });
}
export function silentOpenApi(params) {
  return defHttp.post({ url: '/yundun/sign/silent/service/open', params });
}

export function silentCloseApi(params) {
  return defHttp.post({ url: '/yundun/sign/silent/service/close', params });
}

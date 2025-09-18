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


export function getMyConfirmType(params){
 return defHttp.get({ url: "/user/seal/auth/getMyConfirmType" ,params});
}


export function getListSignRe(params){
 return defHttp.get({ url: "/user/seal/auth/listSignRe" ,params});
}

export function getSealAuthList(params){
 return defHttp.get({ url: "/user/seal/auth/list" ,params});
}


export function saveSealAuth(params){
 return defHttp.post({ url: "/user/seal/auth/add" ,params});
}

export function cancleSealAuth(params){
 return defHttp.post({ url: "/user/seal/auth/cancle" ,params});
}

export function deleteSealAuth(params){
 return defHttp.delete({ url: "/user/seal/auth/delete" ,params});
}



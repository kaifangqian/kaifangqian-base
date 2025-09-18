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
 * @Date: 2022-06-28 14:32:55 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-09-09 15:29:26
 */
import { defHttp } from '/@/utils/http/axios';
import { RoleList } from './model/roleModel';
// import { ErrorMessageMode } from '/#/axios';

enum Api {
  RoleGroupList = '/sys/role/listGroup',
  RoleMyGroupList = '/sys/role/myRoleGroup',
  RoleListByGroupId = '/sys/role/listRole',
  RoleTreeList = '/sys/role/allRoleTree',
  RoleMyTreeList = '/sys/role/myRoleTree',
  RoleUserlist = '/sys/role/getUsersByRoleId',
  RoleAdd = '/sys/role/add',
  RoleEdit = '/sys/role/edit',
  RoleDelete = '/sys/role/delete',
  RoleBatchDelete = '/sys/role/deleteBatch',
  RoleUserList = '/sys/role/getUsersByRoleId',
  RoleInfo = '/sys/role/queryById',
  RoleRemoveBatch = '/sys/role/removeBatch',
  RoleAddUser = '/sys/role/addRoleUser',
  RoleAllTreeForSelect = '/sys/role/allRoleTreeForSelect'

}


/**
 * @description: 获取角色组
 */

export function getRoleGroupList() {
  return defHttp.get({ url: Api.RoleGroupList });
}
/**
 * @description: 获取我的角色组
 */

export function getMyRoleGroupList() {
  return defHttp.get({ url: Api.RoleMyGroupList });
}
/**
 * @description: 获取角色组下的角色
 */

export function getRoleList(params) {
  return defHttp.get({ url: Api.RoleListByGroupId, params });
}
/**
 * @description:  用于反显的角色树
 */

export function getAllRoleTreeListForSelect() {
  return defHttp.get({ url: Api.RoleAllTreeForSelect });
}

export function getRoleTreeList() {
  return defHttp.get({ url: Api.RoleTreeList });
}
export function getMyRoleTreeList() {
  return defHttp.get({ url: Api.RoleMyTreeList });
}

export function getUserByRoleId(params) {
  return defHttp.get<RoleList>({ url: Api.RoleUserList,params });
}

export function addRole(params) {
  return defHttp.post({ url: Api.RoleAdd, params });
}

export function updateRole(params) {
  return defHttp.post<RoleList>({ url: Api.RoleEdit,params });
}

export function deleteRole(params) {
  return defHttp.post<RoleList>({ url: Api.RoleDelete,params });
}

export function getRoleInfo(params) {
  return defHttp.get({ url: Api.RoleInfo,params });
}
export function removeUserBatch(params) {
  return defHttp.delete({ url: Api.RoleRemoveBatch,params });
}

export function addUserToRole(params) {
  return defHttp.post({ url: Api.RoleAddUser,params });
}

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
 * @Last Modified time: 2023-09-19 10:10:36
 */
import { defHttp } from '/@/utils/http/axios';

enum Api {
 AuthGroup = '/system/sysAuthGroup/list',
 AuthAdd = '/system/sysAuthGroup/add',
 AuthEdit = '/system/sysAuthGroup/edit',
 AuthInfo = '/system/sysAuthGroup/queryById',
 AuthDelete = '/system/sysAuthGroup/delete',
 AuthDeleteBatch = '/system/sysAuthGroup/deleteBatch',

 AuthMember = '/system/sysAuthGroupMember/list',
 AuthAddMember = '/system/sysAuthGroupMember/add',
 AuthDeleteBatchMember = '/system/sysAuthGroupMember/deleteBatch',
 AuthMenuList = '/sys/permission/list',
 AuthDataChecked = '/system/sysAuthGroupPermission/list',
 AuthDataPerEdit = '/system/sysAuthGroupPermission/edit',


 AuthData = '/system/sysPermissionData/list',
 AuthDataAdd = '/system/sysPermissionData/add',
 AuthDataEdit = '/system/sysPermissionData/edit',
 AuthDataDelete = '/system/sysPermissionData/delete',
 AuthDataRules = '/system/sysPermissionData/queryById',

 AuthRoleData = '/system/sysAuthGroupRole/listRole',
 AuthRoleAdd = '/system/sysAuthGroupRole/add',
 AuthRoleDelete = '/system/sysAuthGroupRole/deleteBatch',
 
 // 组织模块相关权限
 AuthOrgList = '/system/sysAuthGroupMember/listByAuthId',
 AuthOrgAdd = '/system/sysAuthGroupMember/addGroup',
 AuthOrgDelete = '/system/sysAuthGroupMember/deleteBatch',
 
 //菜单权限表
 AuthTabelList = '/form/onlFormTable/list',
 AuthTabelFieldList = '/form/onlTableField/list',

 AuthAppAndMenuList = '/sys/permission/listTenantAppMenu',
 
}


/**
 * @description: getAuthGroup
 */
export function getAuthGroup(params) {
  return defHttp.get({ url: Api.AuthGroup,params }, { errorMessageMode: 'none' });
}


/**
 * @description: 所有应用+菜单
 */
export function getAuthAppAndMenu(params) {
  return defHttp.get({ url: Api.AuthAppAndMenuList,params }, { errorMessageMode: 'none' });
}

/**
 * @description: addAuthGroup
 */
export function addAuth(params) {
  return defHttp.post({ url: Api.AuthAdd,params }, { errorMessageMode: 'none' });
}
/**
 * @description: addAuthGroup
 */
export function getAuthGroupInfo(params) {
  return defHttp.get({ url: Api.AuthInfo,params }, { errorMessageMode: 'none' });
}

/**
 * @description: editAuthGroup
 */
export function editAuth(params) {
  return defHttp.put({ url: Api.AuthEdit, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 删除权限组
 */
export function deleteAuth(params) {
  return defHttp.delete({ url: Api.AuthDelete, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 批量删除权限组
 */
export function deleteBatchAuth(params) {
  return defHttp.delete({ url: Api.AuthDeleteBatch, params }, { errorMessageMode: 'none' });
}

/**
 * @description: getAuthMember
 */
export function getAuthGroupMember(params) {
  return defHttp.get({ url: Api.AuthMember, params });
}

/**
 * @description: AuthAddMember
 */
export function addAuthMember(params) {
  return defHttp.post({ url: Api.AuthAddMember, params });
}
/**
 * @description: AuthAddMember
 */
export function deleAuthMember(params) {
  return defHttp.delete({ url: Api.AuthDeleteBatchMember, params });
}
/**
 * @description: 获取用户目录+菜单+按钮
 */
export function getAllMenuBtnList(params) {
  return defHttp.get({ url: Api.AuthMenuList, params });
}
/**
 * @description: 查询用户拥有的按钮/表单访问权限
 */
export function getAuthData(params) {
  return defHttp.get({ url: Api.AuthData, params });
}

/**
 * @description: 查询当前按钮已经分配设置的权限
 */
export function getAuthDataChecked(params) {
  return defHttp.get({ url: Api.AuthDataChecked, params });
}
/**
 * @description: 查询权限组查询绑定的角色/组
 */
export function getAuthRoleData(params) {
  return defHttp.get({ url: Api.AuthRoleData, params });
}
/**
 * @description: 权限组新增角色/组
 */
export function addAuthRoleData(params) {
  return defHttp.post({ url: Api.AuthRoleAdd, params });
}
/**
 * @description: 权限组删除角色/组
 */
export function deleteAuthRoleData(params) {
  return defHttp.delete({ url: Api.AuthRoleDelete, params });
}

/**
 * @description: 设置按钮权限
 */
export function setAuthPermission(params) {
  return defHttp.post({ url: Api.AuthDataPerEdit, params });
}

/**
 * @description: 新增权限策略
 */
export function addAuthData(params) {
  return defHttp.post({ url: Api.AuthDataAdd, params });
}
/**
 * @description: 编辑权限策略
 */
export function editAuthData(params) {
  return defHttp.put({ url: Api.AuthDataEdit, params });
}
/**
 * @description: 编辑权限策略规则查询
 */
export function getAuthDataRules(params) {
  return defHttp.get({ url: Api.AuthDataRules, params });
}
/**
 * @description: 编辑权限策略规则查询
 */
export function deleteAuthData(params) {
  return defHttp.delete({ url: Api.AuthDataDelete, params });
}

/**
 * @description: 人员、组织、角色权限查询
 */
export function getOrgAuthData(params) {
  return defHttp.get({ url: Api.AuthOrgList, params });
}

/**
 * @description: 人员、组织、角色权限新增
 */
export function setOrgAuthData(params) {
  return defHttp.post({ url: Api.AuthOrgAdd, params });
}
/**
 * @description: 人员、组织、角色权限删除
 */
export function removeOrgAuthData(params) {
  return defHttp.delete({ url: Api.AuthOrgDelete, params });
}

/**
 * @description: 获取权限表
 */
export function getAuthTable(params) {
  return defHttp.get({ url: Api.AuthTabelList, params });
}

/**
 * @description: 获取权限表字段
 */
export function getAuthTableField(params) {
  return defHttp.get({ url: Api.AuthTabelFieldList, params });
}




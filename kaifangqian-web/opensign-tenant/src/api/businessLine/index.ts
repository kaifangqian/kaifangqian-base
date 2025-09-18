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
  BusinessLineGroup = '/sign/re/folder/tree',
  BusinessLineGroupAdd = '/sign/re/folder/save',
  BusinessLineGroupDelete = '/sign/re/folder/delete',
  BusinessLineBasicInfo = '/app/list',
  BusinessLineCopy = '/sign/re/operate/copy',
  BusinessLineMove = '/sign/re/operate/move',
  BusinessLineStatus = '/sign/re/operate/status',


  BusinessLineCreate = '/sign/re/create',
  BusinessLineList = '/sign/re/list',
  BusinessLineInfoAll = '/sign/re/info/all',
  BusinessLineApprove = '/sign/re/list/approve',

  BusinessLineInfo = '/sign/re/info/base',
  BusinessLineSetBasic = '/sign/re/save/base',

  BusinessLineAuth = '/sign/re/list/auth',
  BusinessLineSetAuth = '/sign/re/save/auth',

  BusinessLineSetControl = '/sign/re/save/control',
  BusinessLineGetControl = '/sign/re/list/control',

 
  BusinessLineFiles = '/sign/re/list/file',
  BusinessLineSignerList = '/sign/re/list/signer',
  BusinessLineControl = '/sign/re/list/control',
  BusinessLineImages = '/sign/file/list/image/annex',
}

/**
 * @description: 业务线分组列表
 */
export function getBusinessLineGroupList(params) {
  return defHttp.get({ url: Api.BusinessLineGroup,params });
}
/**
 * @description: 业务线分组创建
 */
export function createBusinessGroup(params) {
  return defHttp.post({ url: Api.BusinessLineGroupAdd,params });
}
/**
 * @description: 业务线分组删除
 */
export function deleteBusinessGroup(params) {
  return defHttp.delete({ url: Api.BusinessLineGroupDelete,params });
}

/**
 * @description: 业务线创建
 */
export function createBusinessLine(params) {
  return defHttp.post({ url: Api.BusinessLineCreate,params });
}
/**
 * @description: 业务线复制
 */
export function copyBusinessLine(params) {
  return defHttp.post({ url: Api.BusinessLineCopy,params });
}
/**
 * @description: 业务线移动
 */
export function moveBusinessLine(params) {
  return defHttp.post({ url: Api.BusinessLineMove,params });
}
/**
 * @description: 业务线停用、启用
 */
export function statusBusinessLine(params) {
  return defHttp.post({ url: Api.BusinessLineStatus,params });
}

/**
 * @description: 业务线列表
 */
export function getBusinessLineList(params) {
  return defHttp.get({ url: Api.BusinessLineList,params });
}
/**
 * @description: 业务线文件
 */
export function getBusinessLineFile(params) {
  return defHttp.get({ url: Api.BusinessLineFile,params });
}
/**
 * @description: 业务线基本配置详情
 */
export function getBusinessLineInfo(params) {
  return defHttp.get({ url: Api.BusinessLineInfo,params });
}
/**
 * @description: 业务线全部详情
 */
export function getBusinessLineInfoAll(params) {
  return defHttp.get({ url: Api.BusinessLineInfoAll,params });
}
/**
 * @description: 业务线审批详情
 */
export function getBusinessLineApprove(params) {
  return defHttp.post({ url: Api.BusinessLineApprove,params });
}
/**
 * @description: 业务线权限配置详情
 */
export function getBusinessLineAuth(params) {
  return defHttp.get({ url: Api.BusinessLineAuth,params });
}
/**
 * @description: 业务线权限配置
 */
export function setBusinessLineAuth(params) {
  return defHttp.post({ url: Api.BusinessLineSetAuth,params });
}

/**
 * @description: 业务线基本数据详情
 */
export function getBusinessLineBasicInfo(params) {
  return defHttp.get({ url: Api.BusinessLineBasicInfo,params });
}
/**
 * @description: 业务线基本数据保存
 */
export function setBusinessLineBasic(params) {
  return defHttp.post({ url: Api.BusinessLineSetBasic,params });
}
/**
 * @description: 业务线文件获取
 */
export function getBusinessLineFiles(params) {
  return defHttp.get({ url: Api.BusinessLineFiles,params });
}
/**
 * @description: 业务线签署人获取
 */
export function getBusinessLineSignerList(params) {
  return defHttp.get({ url: Api.BusinessLineSignerList,params });
}
/**
 * @description: 业务线控件保存
 */
export function setBusinessLineControl(params) {
  return defHttp.post({ url: Api.BusinessLineSetControl,params });
}
/**
 * @description: 业务线控件获取
 */
export function getBusinessLineControl(params) {
  return defHttp.get({ url: Api.BusinessLineGetControl,params });
}
/**
 * @description: 业务线图片获取
 */
export function getBusinessLineImages(params) {
  return defHttp.get({ url: Api.BusinessLineImages,params });
}


export function getReNotice(params){
  return defHttp.get({ url: "/sign/re/notice",params});
}


export function saveReNotice(params){
  return defHttp.post({ url: "/sign/re/notice",params});
}


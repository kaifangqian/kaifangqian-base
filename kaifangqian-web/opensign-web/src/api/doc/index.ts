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

import { useUserStore } from '/@/store/modules/user';
const userStore = useUserStore();
const tenantInfo = userStore.getTenantInfo;
let listType ='';
if(tenantInfo.tenantType=='1'){
  listType = '/company'
}else{
  listType = '/personal'
}

enum  Api{

  DraftList =  '/task/listDraft',
  SendList = '/task/listSend',
  PendingMyList = '/task/listMyJob',
  BoxList = '/task/listInbox',
  CopyMeList = '/task/listCopyMe',
  RecycleMeList = '/task/listRecycle',
  AllList = '/task/listAll',
  OtherJobList = '/task/listOtherJob',
  RunningList = '/task/listRunning',
  FinishList = '/task/listFinish',
  InvalidList = '/task/listInvalid',
  checkOperate = '/company/task/checkOperate',
}

/**
 * @description: 获取已发送
 */
export function getCompanySendList(params) {
  return defHttp.get({ url: listType + Api.SendList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取草稿
 */
export function getCompanyDraftList(params) {
  return defHttp.get({ url:  listType + Api.DraftList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 收件箱
 */
export function getCompanyBoxList(params) {
  return defHttp.get({ url:  listType + Api.BoxList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 抄送我的
 */
export function getCompanyCopyMeList(params) {
  return defHttp.get({ url:  listType + Api.CopyMeList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 回收站
 */
export function getCompanyRecycleMeList(params) {
  return defHttp.get({ url:  listType + Api.RecycleMeList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 全部文档
 */
export function getCompanyAllList(params) {
  return defHttp.get({ url:  listType + Api.AllList,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 待我处理
 */
export function getPendingList(params) {
  return defHttp.get({ url:  listType + Api.PendingMyList,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 待他人处理
 */
export function getOtherJobList(params) {
  return defHttp.get({ url:  listType + Api.OtherJobList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 未完成
 */
export function getRunningList(params) {
  return defHttp.get({ url:  listType + Api.RunningList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 已完成
 */
export function getFinishList(params) {
  return defHttp.get({ url:  listType + Api.FinishList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 已失效
 */
export function getInvalidList(params) {
  return defHttp.get({ url:  listType + Api.InvalidList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 获取列表操作
 */
export function getCheckOperates(params) {
  return defHttp.get({ url: Api.checkOperate,params }, { errorMessageMode: 'none' });
}

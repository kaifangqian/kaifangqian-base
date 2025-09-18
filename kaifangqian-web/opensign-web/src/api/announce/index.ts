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
  AnnounceTypeTree = '/system/sysAnnouncementType/tree',
  AnnounceTypeAdd = '/system/sysAnnouncementType/add',
  AnnounceTypeEdit = '/system/sysAnnouncementType/edit',
  AnnounceTypeDelete = '/system/sysAnnouncementType/delete',
  AnnounceTypeDeleteBatch = '/system/sysAnnouncementType/deleteBatch',

  AnnounceList = '/sys/annountCement/list',
  AnnounceAdd = '/sys/annountCement/add',
  AnnounceEdit = '/sys/annountCement/edit',
  AnnounceDeleteBatch = '/sys/annountCement/deleteBatch',
  AnnounceQueryById = '/sys/annountCement/queryById',
  AnnounceRelease = '/sys/annountCement/doReleaseData',
  AnnounceRevoke = '/sys/annountCement/doReovkeData',

  MyAnnounceSendList = '/sys/sysAnnouncementSend/getMyAnnouncementSend',
  MyAnnounceIsRead = '/sys/sysAnnouncementSend/isRead',
  MyAnnounceReadAll = '/sys/sysAnnouncementSend/readAll',

  Files = '/file'

}

/**
 * @description: 公告类型树
 */
 export function getAnnounceFile(params) {
  return defHttp.get({ url: Api.Files,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告类型树
 */
 export function getAnnounceTree(params) {
  return defHttp.get({ url: Api.AnnounceTypeTree,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告类型树添加
 */
 export function announceTreeAdd(params) {
  return defHttp.post({ url: Api.AnnounceTypeAdd,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告类型树编辑
 */
 export function announceTreeEdit(params) {
  return defHttp.put({ url: Api.AnnounceTypeEdit,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告类型树删除
 */
 export function announceTreeDelete(params) {
  return defHttp.delete({ url: Api.AnnounceTypeDeleteBatch,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 公告发布列表
 */
 export function announceList(params) {
  return defHttp.get({ url: Api.AnnounceList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告发布新增
 */
 export function announceAdd(params) {
  return defHttp.post({ url: Api.AnnounceAdd,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告发布编辑
 */
 export function announceEdit(params) {
  return defHttp.put({ url: Api.AnnounceEdit,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告发布删除
 */
 export function announceDeleteBatch(params) {
  return defHttp.delete({ url: Api.AnnounceDeleteBatch,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告发布
 */
 export function announceRelease(params) {
  return defHttp.get({ url: Api.AnnounceRelease,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告撤销
 */
 export function announceRevoke(params) {
  return defHttp.get({ url: Api.AnnounceRevoke,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 公告详情
 */
 export function announceDetail(params) {
  return defHttp.get({ url: Api.AnnounceQueryById,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 我的公告
 */
 export function getMyAnnounceList(params) {
  return defHttp.get({ url: Api.MyAnnounceSendList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 标记已读
 */
 export function myAnnounceSendRead(params) {
  return defHttp.get({ url: Api.MyAnnounceIsRead,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 全部标记已读
 */
 export function myAnnounceSendReadAll(params) {
  return defHttp.get({ url: Api.MyAnnounceReadAll,params }, { errorMessageMode: 'none' });
}
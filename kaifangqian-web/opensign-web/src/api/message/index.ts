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
 * @Last Modified time: 2022-09-29 11:08:32
 */
import { defHttp } from '/@/utils/http/axios';

enum Api {
  MessageTree = '/message/template/list',
  MessageList = '/message/template/list',
  MessageAdd = '/message/template/add',
  MessageEdit = '/message/template/edit',
  MessageTemplateInfo = '/message/template/queryById',

  MessageTemplateList = '/message/templateType/tree',
  MessageTemplateAdd = '/message/templateType/add',
  MessageTemplateEdit = '/message/templateType/edit',
  MessageTemplateDelete = '/message/templateType/delete',

  MessageMy = '/mes/messageSendRecord/listMy',
  MessageIsRead = '/mes/messageSendRecord/isRead',
  MessageQueryById = '/mes/messageSendRecord/queryById',
  MessageIsReadAll = '/mes/messageSendRecord/readAll',
  MessageMyDeleteBatch = '/mes/messageSendRecord/deleteBatch',

  MessageThird  = '/mes/message_3rdRecord/list',
  MessageThirdRetry = '/mes/message_3rdRecord/retry',
  MessageThirdQueryById = '/mes/message_3rdRecord/queryById',

}


/**
 * @description: getAuthGroup
 */
export function getMessageTree(params) {
  return defHttp.get({ url: Api.MessageTree,params }, { errorMessageMode: 'none' });
}

/**
 * @description: getAuthGroup
 */
export function getMessageList(params) {
  return defHttp.get({ url: Api.MessageList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 新增消息模板
 */
export function addMessageList(params) {
  return defHttp.post({ url: Api.MessageAdd,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 编辑消息模板
 */
export function editMessage(params) {
  return defHttp.put({ url: Api.MessageEdit,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 模板类型tree
 */
export function getTemplateTypeList(params) {
  return defHttp.get({ url: Api.MessageTemplateList,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 新增模板类型tree
 */
export function addTemplateType(params) {
  return defHttp.post({ url: Api.MessageTemplateAdd,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 编辑模板类型tree
 */
export function editTemplateType(params) {
  return defHttp.put({ url: Api.MessageTemplateEdit,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 编辑模板类型tree
 */
export function getTemplateInfo(params) {
  return defHttp.get({ url: Api.MessageTemplateInfo,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 删除模板类型tree
 */
export function deleteTemplateType(params) {
  return defHttp.delete({ url: Api.MessageTemplateDelete,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 我的消息
 */
export function getMyMessage(params) {
  return defHttp.get({ url: Api.MessageMy,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 标记已读
 */
export function readMyMessage(params) {
  return defHttp.get({ url: Api.MessageIsRead,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 全部标记已读
 */
export function readAllMyMessage(params) {
  return defHttp.get({ url: Api.MessageIsReadAll,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 获取我的消息详情
 */
export function getMyMessageInfo(params) {
  return defHttp.get({ url: Api.MessageQueryById,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 消息批量删除
 */
export function deleteMessageBatch(params) {
  return defHttp.delete({ url: Api.MessageMyDeleteBatch,params }, { errorMessageMode: 'none' });
}

/**
 * @description: 消息发送记录列表
 */
export function getMessageHistory(params) {
  return defHttp.get({ url: Api.MessageThird,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 消息发送记录列表详情
 */
export function getMessageHistoryInfo(params) {
  return defHttp.get({ url: Api.MessageThirdQueryById,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 消息发送记录 消息重试
 */
export function getMessageRetry(params) {
  return defHttp.get({ url: Api.MessageThirdRetry,params }, { errorMessageMode: 'none' });
}







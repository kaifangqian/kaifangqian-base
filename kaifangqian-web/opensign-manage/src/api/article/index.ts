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
  ArticleAddGroup = '/sys/article/type/add',
  ArticleEditGroup = '/sys/article/type/edit',
  ArticleDeleteGroup = '/sys/article/type/delete',
  ArticleGrouplist = '/sys/article/type/list',
  ArticleAdd = '/sys/article/add',
  ArticleInfo = '/sys/article/info',
  ArticleEdit = '/sys/article/edit',
  ArticleDelete = '/sys/article/delete',
  ArticleList = '/sys/article/list',
  ArticleNoAuthList = '/sys/article/listNoAuth',

}

/**
 * @description: 文章新增分类
 */
export function addArticleGroup(params) {
  return defHttp.post({ url: Api.ArticleAddGroup,params });
}

/**
 * @description: 文章编辑分类
 */
export function editArticleGroup(params) {
  return defHttp.put({ url: Api.ArticleEditGroup,params });
}
/**
 * @description: 文章删除分类
 */
export function deleteArticleGroup(params) {
  return defHttp.delete({ url: Api.ArticleDeleteGroup,params });
}
/**
 * @description: 文章分类列表
 */
export function getArticleGroupList(params) {
  return defHttp.get({ url: Api.ArticleGrouplist,params });
}
/**
 * @description: 文章新增
 */
export function addArticleAdd(params) {
  return defHttp.post({ url: Api.ArticleAdd,params });
}
/**
 * @description: 文章详情
 */
export function getArticleInfo(params) {
  return defHttp.get({ url: Api.ArticleInfo,params });
}
/**
 * @description: 文章编辑
 */
export function editArticle(params) {
  return defHttp.put({ url: Api.ArticleEdit,params });
}
/**
 * @description: 文章删除
 */
export function deleteArticle(params) {
  return defHttp.delete({ url: Api.ArticleDelete,params });
}
/**
 * @description: 文章列表
 */
export function getArticleList(params) {
  return defHttp.get({ url: Api.ArticleList,params });
}
/**
 * @description: 文章无权限列表
 */
export function getNoAuthArticleList(params) {
  return defHttp.delete({ url: Api.ArticleNoAuthList,params });
}
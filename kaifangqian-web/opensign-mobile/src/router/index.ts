/**
 * @description : 路由配置文件
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
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router';

export const layoutRoutes: Array<RouteRecordRaw> = [
  {
    path: 'index',
    name: '开放签',
    meta: {
      title: '开放签',
      leftArrow: false,
      keepAlive: true,
    },
    component: () => import('@/pages/index.vue'),
  },
  {
    path: '/write/:signRuId?/:taskId?',
    name: '填写',
    meta: {
      title: '开放签',
      leftArrow: true,
      keepAlive: false,
    },
    component: () => import('@/pages/signwrite/write.vue'),
  },
  {
    path: '/detail/:signRuId?',
    name: '详情',
    meta: {
      title: '开放签',
      leftArrow: true,
      keepAlive: false,
    },
    component: () => import('@/pages/detail/docDetail.vue'),
  },
  {
    path: '/doc/:signRuId?/:docId?',
    name: '签约文档',
    meta: {
      title: '开放签',
      leftArrow: true,
      keepAlive: false,
    },
    component: () => import('@/pages/components/Document.vue'),
  },

  {
    path: 'image-rotate',
    name: 'image-rotate',
    meta: {
      title: 'image-rotate',
    },
    component: () => import('@/pages/image-rotate/index.vue'),
  },
  {
    path: '/signContract/:signRuId?',
    name: '签署',
    meta: {
      title: '签署',
      leftArrow: true,
      keepAlive: true,
    },
    component: () => import('@/pages/contract/SignContract.vue'),
  },
  {
    path: '/wishCheck',
    name: '签署结果',
    meta: {
      title: '签署结果',
      leftArrow: true,
      keepAlive: true,
    },
    component: () => import('@/pages/contract/WishCheck.vue'),
  },
  {
    path: '/noauth',
    name: '无权访问',
    meta: {
      title: '无权访问',
      leftArrow: false,
      keepAlive: false,
    },
    component: () => import('@/pages/base/noAuth.vue'),
  },
  {
    path: '/enterprise',
    name: '企业实名',
    meta: {
      title: '企业实名',
      leftArrow: true,
      keepAlive: false,
    },
    component: () => import('@/pages/auth/enterprise.vue'),
  },
  {
    path: '/enterprise/details',
    name: '企业实名详情',
    meta: {
      title: '实名详情',
      leftArrow: true,
      keepAlive: false,
    },
    component: () => import('@/pages/auth/enterpriseDetails.vue'),
  },

  {
    path: '/personal/:authStatus?',
    name: '个人实名',
    meta: {
      title: '个人实名',
      leftArrow: true,
    },
    component: () => import('@/pages/auth/personal.vue'),
  },
  {
    path: '/personal/details',
    name: '个人实名详情',
    meta: {
      title: '个人实名详情',
      leftArrow: true,
    },
    component: () => import('@/pages/auth/personalDetails.vue'),
  },
];

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/index',
    children: layoutRoutes,
  },
  // 不需要layout的页面
  {
    path: '/login',
    name: 'login',
    meta: {
      title: 'login',
    },
    component: () => import('@/pages/login/index.vue'),
  },
  {
    path: '/base/:code?',
    name: 'base',
    meta: {
      title: 'base',
    },
    component: () => import('@/pages/base/index.vue'),
  },
  {
    path: '/check/face',
    name: 'checkFace',
    meta: {
      title: 'check',
    },
    component: () => import('@/pages/base/FaceLoading.vue'),
  },
  // 替代vue2中的'*'通配符路径
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
  history: createWebHashHistory(), // history 模式则使用 createWebHistory()
  routes,
});
export default router;

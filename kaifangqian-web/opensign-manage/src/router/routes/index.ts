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

import type { AppRouteRecordRaw, AppRouteModule } from '/@/router/types';

import { PAGE_NOT_FOUND_ROUTE, REDIRECT_ROUTE } from '/@/router/routes/basic';

import { PageEnum } from '/@/enums/pageEnum';

import { LAYOUT } from '/@/router/constant';


// const modules = import.meta.globEager('./modules/**/*.ts');
const modules = import.meta.globEager('./modules/*.ts');


const routeModuleList: AppRouteModule[] = [];

Object.keys(modules).forEach((key) => {
  const mod = modules[key].default || {};
  const modList = Array.isArray(mod) ? [...mod] : [mod];
  routeModuleList.push(...modList);
});

export const asyncRoutes = [PAGE_NOT_FOUND_ROUTE, ...routeModuleList];

export const RootRoute: AppRouteRecordRaw = {
  path: '/',
  name: 'Root',
  redirect: PageEnum.BASE_HOME,
  meta: {
    title: 'Root',
  },
};

export const LoginRoute: AppRouteRecordRaw = {
  path: '/login',
  name: 'Login',
  component: () => import('/@/views/sys/login/Login.vue'),
  meta: {
    title: '登录',
  },
};
export const JoinRoute: AppRouteRecordRaw = {
  path: '/join',
  name: 'Join',
  component: () => import('/@/views/sys/login/Join.vue'),
  meta: {
    title: '加入',
  },
}
export const ExperienceLoginRoute: AppRouteRecordRaw = {
  path: '/login/experience',
  name: 'Login',
  component: () => import('/@/views/sys/login/Login.vue'),
  meta: {
    title: '体验登录',
  },
};

export const ApplicationInsideRoute: AppRouteRecordRaw = {
  path: '/tenant/appmanage',
  name: '应用管理',
  parentId:'',
  menuType:0,
  icon: 'ant-design:appstore-outlined',
  component: LAYOUT,
  redirect: '/tenant/appmanage/basic',
  meta: {
    hideMenu:false,
    title: '应用管理',
  },
  children:[]
}
export const MicroForm : AppRouteModule = {
  path:'/micro/form',
  name:'MicroForm',
  component: () => import('/@/views/microform/index.vue'),
  meta: {
    title: '表单',
  },
}
// Basic routing without permission
// export const basicRoutes = [ExperienceLoginRoute, LoginRoute, RootRoute, REDIRECT_ROUTE, PAGE_NOT_FOUND_ROUTE, JoinRoute];
export const basicRoutes = [ExperienceLoginRoute, RootRoute, REDIRECT_ROUTE, PAGE_NOT_FOUND_ROUTE, JoinRoute, MicroForm];

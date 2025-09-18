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
export const HomeRoute: AppRouteRecordRaw = {
  path: '/dashboard/workbench',
  name: 'Home',
  component: LAYOUT,
  meta: {
    title: '首页',
  },
  children: [
    {
      path: '/dashboard/workbench',
      name: 'Home',
      component: () => import('/@/views/dashboard/workbench/index.vue'),
      meta: {
        hideMenu: true,
        title: '首页',
      },
    },
  ],
};

export const LoginRoute: AppRouteRecordRaw = {
  path: '/login',
  name: 'Login',
  component: () => import('/@/views/sys/login/Login.vue'),
  meta: {
    title: '登录',
  },
};
export const TransitionRoute: AppRouteRecordRaw = {
  path: '/transition',
  name: 'Transition',
  component: () => import('/@/views/sys/login/Transition.vue'),
  meta: {
    title: '注册成功',
  },
};
export const AuthLoginRoute: AppRouteRecordRaw = {
  path: '/auth/login',
  name: 'AuthLogin',
  component: () => import('/@/views/sys/auth-login/index.vue'),
  meta: {
    title: '授权登录',
  },
  // children: [
  //   {
  //     path: 'auth/login',
  //     name: '授权登录',
  //     menuType:1,
  //     component: () => import('/@/views/sys/auth-login/index.vue'),
  //     meta: {
  //       hideMenu:false,
  //       title: '授权登录',
  //     },
  //   },
  // ]
};
export const RegisterRoute: AppRouteRecordRaw = {
  path: '/register',
  name: 'Register',
  component: () => import('/@/views/sys/login/Register.vue'),
  meta: {
    ignoreAuth: true,
    title: '注册',
  },
};
export const JoinRoute: AppRouteRecordRaw = {
  path: '/join',
  name: 'Join',
  component: () => import('/@/views/sys/login/Join.vue'),
  meta: {
    ignoreAuth: true,
    title: '加入',
  },
};
export const ContractNoauth: AppRouteRecordRaw = {
  path: '/contract/noauth/:type?',
  name: 'ContractNoauth',
  component: () => import('/@/views/contract/noauth/index.vue'),
  meta: {
    ignoreAuth: true,
    title: '文档详情',
  },
};

export const ContractBase: AppRouteRecordRaw = {
  path: '/contract/detail/base/:code?/:signRuId?/:taskId?/:taskType?',
  name: 'ContractBase',
  component: () => import('/@/views/contract/detail/base.vue'),
  meta: {
    ignoreAuth: true,
    title: '文档详情',
  },
};

export const ExperienceLoginRoute: AppRouteRecordRaw = {
  path: '/login/experience',
  name: 'Login',
  component: () => import('/@/views/sys/login/Login.vue'),
  meta: {
    ignoreAuth: true,
    title: '体验登录',
  },
};

export const TermService: AppRouteRecordRaw = {
  path: '/terms/service/:type?',
  name: 'TermService',
  component: () => import('/@/views/sys/login/TermServices.vue'),
  meta: {
    ignoreAuth: true,
    title: '服务条款',
  },
};
export const PrivacyPolicy: AppRouteRecordRaw = {
  path: '/privacy/policy',
  name: 'PricacyPolicy',
  component: () => import('/@/views/sys/login/PrivacyPolicy.vue'),
  meta: {
    ignoreAuth: true,
    title: '隐私协议',
  },
};

export const YdCallBackPage: AppRouteRecordRaw = {
  path: '/callbackpage',
  name: 'Callbackpage',
  component: () => import('/@/views/callback/index.vue'),
  meta: {
    ignoreAuth: true,
    title: '云盾同步回调统一地址',
  },
};

export const ApplicationInsideRoute: AppRouteRecordRaw = {
  path: '/tenant/appmanage',
  name: '应用管理',
  parentId: '',
  menuType: 0,
  icon: 'ant-design:appstore-outlined',
  component: LAYOUT,
  redirect: '/tenant/appmanage/basic',
  meta: {
    hideMenu: false,
    title: '应用管理',
  },
  children: [
    {
      path: 'basic?type=application',
      name: '基本信息',
      parentId: 'app-1',
      menuType: 1,
      component: () => import('/@/views/tenant/manage/Basic.vue'),
      meta: {
        hideMenu: false,
        title: '基本信息',
      },
    },
    {
      path: 'auth?type=application',
      name: '权限管理',
      parentId: 'app-1',
      menuType: 1,
      component: () => import('/@/views/tenant/manage/authmanage/authGroup.vue'),
      meta: {
        hideMenu: false,
        title: '权限管理',
      },
    },
    {
      path: 'role?type=application',
      name: '角色管理',
      parentId: 'app-1',
      menuType: 1,
      component: () => import('/@/views/tenant/manage/rolemanage/index.vue'),
      meta: {
        hideMenu: false,
        title: '角色管理',
      },
    },
  ],
};

export const wishCheckRouter: AppRouteRecordRaw = {
  path: '/wishCheck',
  name: 'wishCheck',
  component: () => import('/@/views/contract/sign/WishCheck.vue'),
  meta: {
    ignoreAuth: true,
    title: '意愿校验结果',
  },
};

// Basic routing without permission
export const basicRoutes = [
  ExperienceLoginRoute,
  AuthLoginRoute,
  LoginRoute,
  ContractBase,
  TransitionRoute,
  RootRoute,
  REDIRECT_ROUTE,
  PAGE_NOT_FOUND_ROUTE,
  JoinRoute,
  RegisterRoute,
  TermService,
  PrivacyPolicy,
  ContractNoauth,
  wishCheckRouter,
  YdCallBackPage,
];

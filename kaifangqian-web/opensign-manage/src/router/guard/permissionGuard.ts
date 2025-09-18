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

import type { Router, RouteRecordRaw } from 'vue-router';

import { usePermissionStoreWithOut } from '/@/store/modules/permission';

import { PageEnum } from '/@/enums/pageEnum';

import { APP_ID } from '/@/enums/cacheEnum';

import { createLocalStorage } from '/@/utils/cache';
import { useUserStoreWithOut } from '/@/store/modules/user';

import { PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic';

import { getHashQueryString } from '/@/utils';

import { RootRoute } from '/@/router/routes';

const LOGIN_PATH = PageEnum.BASE_LOGIN;

const JOIN_PATH = PageEnum.JOIN_PATH;

const ROOT_PATH = RootRoute.path;

const whitePathList: PageEnum[] = [LOGIN_PATH, JOIN_PATH];


const userStore = useUserStoreWithOut();
const ls = createLocalStorage();

const shareToken =  getHashQueryString('token');
const appId = getHashQueryString('appId');
const appToken = getHashQueryString('token');
const loginToken = getHashQueryString('loginToken');

if(appToken){
  userStore.setToken(appToken);
}
if(loginToken){
  userStore.setToken(loginToken);
}

if(shareToken){
  userStore.setToken(shareToken)
}
if(appId){
  ls.set(APP_ID,appId)
}

function findRouteWithMenuType(routes) {
  for (const route of routes) {
    if (route.menuType === 1) {
      return route; // 找到第一个满足条件的路由
    }
    if (route.children && route.children.length > 0) {
      const childResult = findRouteWithMenuType(route.children);
      if (childResult) {
        return childResult; // 在子路由中找到满足条件的路由
      }
    }
  }
  return null; // 没有找到满足条件的路由
}

function findRouteWithPath(routes,path) {
  for (const route of routes) {
    if (route.path == path || route.path.includes(path) || route.fullPath == path) {
      return route; // 找到第一个满足条件的路由
    }
    if (route.children && route.children.length > 0) {
      const childResult = findRouteWithPath(route.children, path);
      if (childResult) {
        return childResult; // 在子路由中找到满足条件的路由
      }
    }
  }
  return null; // 没有找到满足条件的路由
}

export function createPermissionGuard(router: Router) {
  const permissionStore = usePermissionStoreWithOut();
  router.beforeEach(async (to, from, next) => {
    if (
      from.path === ROOT_PATH &&
      to.path === PageEnum.BASE_HOME &&
      userStore.getUserInfo.homePath &&
      userStore.getUserInfo.homePath !== PageEnum.BASE_HOME
    ) {
      next(userStore.getUserInfo.homePath);
      return;
    }

    const token = userStore.getToken;

    // Whitelist can be directly entered
    if (whitePathList.includes(to.path as PageEnum)) {
      if (to.path === LOGIN_PATH && token) {
        const isSessionTimeout = userStore.getSessionTimeout;
        try {
          await userStore.afterLoginAction();
          if (!isSessionTimeout) {
            next((to.query?.redirect as string) || '/');
            return;
          }
        } catch {}
      }
      next();
      return;
    }

    // token does not exist
    if (!token) {
      // You can access without permission. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
        next();
        return;
      }

      // redirect login page
      const redirectData: { path: string; replace: boolean; query?: Recordable<string> } = {
        path: LOGIN_PATH,
        replace: true,
      };
      if (to.path) {
        redirectData.query = {
          ...redirectData.query,
          redirect: to.path,
        };
      }
      next(redirectData);
      return;
    }

    // Jump to the 404 page after processing the login
    if (
      from.path === LOGIN_PATH &&
      to.name === PAGE_NOT_FOUND_ROUTE.name &&
      to.fullPath !== (userStore.getUserInfo.homePath || PageEnum.BASE_HOME)
    ) {
      next(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
      return;
    }

    // get userinfo while last fetch time is empty
    if (userStore.getLastUpdateTime === 0) {
      try {
        await userStore.getUserInfoAction();
        await userStore.getTnantInfo();
      } catch (err) {
        next();
        return;
      }
    }

    if (permissionStore.getIsDynamicAddedRoute) {
      next();
      return;
    }

    const routes = await permissionStore.buildRoutesAction();
    const firstMenu = findRouteWithMenuType(routes);

    routes.forEach((route) => {
      //修正路由path
      if(!route.path.startsWith('/')){
        route.path = `/${route.path}`
      }
      router.addRoute(route as unknown as RouteRecordRaw);
    });

    router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);

    permissionStore.setDynamicAddedRoute(true);

    if (to.name === PAGE_NOT_FOUND_ROUTE.name) {
      // 动态添加路由后，此处应当重定向到fullPath，否则会加载404页面内容
      let matchRoute = findRouteWithPath(routes,to.path)
      if(matchRoute){
        next({ path: to.fullPath, replace: true, query: to.query });
      }else{
        next({ path: firstMenu.fullPath, replace: true, query: to.query });
      }
    } else {
      const redirectPath = (from.query.redirect || to.path) as string;
      const redirect = decodeURIComponent(redirectPath);
      const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect };
      next(nextData);
    }
  });
}

/**
 * @description : 路由守卫
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

import type { Router, RouteRecordRaw } from 'vue-router';
import { Notify } from 'vant';
import { APP_TOKEN, CACHE_VIEWS } from '@/utils/cache/constant';
import session from '@/utils/cache/session';
import { useUserStore } from '@/store/modules/user';
import { useMenuStore } from '@/store/modules/menu';

const whitePathList: any = ['/base', '/check/face'];
export function setupRouterGuard(router: Router) {
  router.beforeEach(async (to, from, next) => {
    const userStore = useUserStore();
    await userStore.buildWebConfig();
    const token = userStore.getAppToken;
    if (to.path == '/login') {
      console.log('login token:', token);
      // 登录或者注册才可以往下进行
      if (token) {
        next('/index');
      } else {
        next();
      }
      // next();
    } else {
      if (userStore.getLastUpdateTime === 0) {
        await userStore.reloadTenantInfo();
        await userStore.afterLoginAction();
      }
      const menuInfo = useMenuStore();
      if (
        (to.path == '/personal' && from.path == '/signContract') ||
        (from.path == '/personal' && to.path == '/signContract')
      ) {
        menuInfo.setCacheViews(CACHE_VIEWS);
      } else {
        const cacheViews = menuInfo.getCacheViews;
        const nowCaches = cacheViews.filter((v) => v !== 'signContract');
        menuInfo.setCacheViews(nowCaches);
      }
      // 获取 token
      // const token = session.getItem(APP_TOKEN);
      // token 不存在
      if (whitePathList.includes(to.path)) {
        next();
        return;
      }
      if (token === null || token === '') {
        // Notify({ type: 'warning', message: '暂无登录信息', duration: 1000 });
        next('/login');
      } else {
        next();
      }
    }
  });
}

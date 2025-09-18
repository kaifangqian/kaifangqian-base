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

import type { AppRouteModule } from '/@/router/types';
import { LAYOUT } from '/@/router/constant';

const auth: AppRouteModule = {
  path: '/auth',
  name: '权限',
  component: LAYOUT,
  redirect: '/group',
  meta: {
    hideMenu: true,
    hideChildrenInMenu:true,
    icon: 'ant-design:setting-outlined',
    title: '权限',
    orderNo: 20,
  },
  children: [
    {
      path: 'group',
      name: '权限管理',
      component: () => import( /* @vite-ignore */'/@/views/auth/authGroup.vue'),
      meta: {
        title: '权限管理',
        hideMenu: false,
      },
    },
    {
      path: 'ploy',
      name: '数据权限策略管理',
      component: () => import( /* @vite-ignore */'/@/views/auth/authPloy.vue'),
      meta: {
        title: '数据权限策略管理',
        hideMenu: false,
      },
    }
    
  ],
};


export default auth;

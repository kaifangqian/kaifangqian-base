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

const table: AppRouteModule = {
  path: '/table',
  name: 'Table',
  component: LAYOUT,
  redirect: '/table/normal',
  meta: {
    hideChildrenInMenu: false,
    icon: 'simple-icons:about-dot-me',
    title: '列表',
    orderNo: 12,
  },
  children: [
    {
      path: 'normal',
      name: 'RegularTable',
      component: () => import('/@/views/demo/table/RegularTable.vue'),
      meta: {
        title: '普通列表',
        icon: 'simple-icons:about-dot-me',
        orderNo: 121,
      },
    },
    {
      path: 'expand',
      name: 'ExpandTable',
      component: () => import('/@/views/demo/table/ExpandTable.vue'),
      meta: {
        title: '展开列表',
        icon: 'simple-icons:table-outlined',
        orderNo: 122,
      },
    },
    {
      path: 'edit',
      name: 'EditTable',
      component: () => import('/@/views/demo/table/EditTable.vue'),
      meta: {
        title: '编辑列表',
        icon: 'simple-icons:table-outlined',
        orderNo: 123,
      },
      
    }
  ],
};

export default table;

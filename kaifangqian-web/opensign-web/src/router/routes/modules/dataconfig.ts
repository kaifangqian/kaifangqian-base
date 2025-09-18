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

const dataConfig: AppRouteModule = {
  path: '/data',
  name: 'Data',
  component: LAYOUT,
  redirect: '/data',
  meta: {
    hideChildrenInMenu: false,
    icon: 'ant-design:database-outlined',
    title: '数据配置',
    orderNo: 26,
  },
  children: [
    {
      path: 'dict',
      name: 'Dict',
      component: () => import( /* @vite-ignore */'/@/views/dataconfig/Dict.vue'),
      meta: {
        title: '数据字典',
        hideMenu: false,
      },
    },
    {
      path: 'serial',
      name: 'SerialNumber',
      component: () => import( /* @vite-ignore */'../../../views/dataconfig/SerialNumber.vue'),
      meta: {
        title: '编号规则',
        hideMenu: false,
      },
    },
    {
      path: 'verify',
      name: 'Verify',
      component: () => import( /* @vite-ignore */'/@/views/dataconfig/Verify.vue'),
      meta: {
        title: '系统校验规则',
        hideMenu: false,
      },
    },
  ],
};

export default dataConfig;

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

import { Router } from 'vue-router'
import { useUserStoreWithOut } from '/@/store/modules/user';


declare global {
  interface Window {
    eventCenterForTenantVite: any
    __MICRO_APP_NAME__: string
    __MICRO_APP_ENVIRONMENT__: string
    __MICRO_APP_BASE_APPLICATION__: string
  }
}

// 与基座进行数据交互
export function initMicroData (router: Router) {
  const userStore = useUserStoreWithOut();
  // eventCenterForTenantVite 是基座添加到window的数据通信对象
  if (window.eventCenterForTenantVite) {
    // 主动获取基座下发的数据
    console.log('child-vite getData:', window.eventCenterForTenantVite.getData())
    let token = window.eventCenterForTenantVite.getData()?.token;
    let defaultPath = window.eventCenterForTenantVite.getData()?.path;
    if(token){
      userStore.setToken(token);
    }
    console.log(defaultPath,'默认路径')
    if(defaultPath){
      router.push(defaultPath)
    }
    // 监听基座下发的数据变化
    window.eventCenterForTenantVite.addDataListener((data: Record<string, unknown>) => {
      console.log('child-vite addDataListener:', data)

      if (data.path && typeof data.path === 'string' && router) {
        data.path = data.path.replace(/^#/, '')
        // 当基座下发path时进行跳转
        if (data.path && data.path !== router.currentRoute.value.path) {
          router.push(data.path as string)
        }
      }
    })

    // 向基座发送数据
    setTimeout(() => {
      window.eventCenterForTenantVite.dispatch({ myname: 'child-vite' })
    }, 3000)
  }
}
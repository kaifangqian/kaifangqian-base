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

import 'virtual:windi-base.css';
import 'virtual:windi-components.css';
import '/@/design/index.less';
import 'virtual:windi-utilities.css';
import '/@/assets/style/common.css';
// Register icon sprite
import 'virtual:svg-icons-register';
import App from './App.vue';
import { createApp } from 'vue';
import { initAppConfigStore } from '/@/logics/initAppConfig';
import { setupErrorHandle } from '/@/logics/error-handle';
import { router, setupRouter } from '/@/router';
import { setupRouterGuard } from '/@/router/guard';
import { setupI18n } from '/@/locales/setupI18n';
import { setupStore } from '/@/store';
import { setupGlobDirectives } from '/@/directives';
import { registerGlobComp } from '/@/components/registerGlobComp';
import eventHub from '/@/utils/eventHub';


import axios from 'axios';


declare global {
  interface Window {
    eventCenterForAppNameVite: any
    __MICRO_APP_NAME__: string
    __MICRO_APP_ENVIRONMENT__: string
    __MICRO_APP_BASE_APPLICATION__: string
  }
}
let app = createApp(App);


function transformAppInfoData(rawData) {
    const transformedData: any = {};

    rawData.forEach(item => {
        // 根据appId将数据分类
        switch (item.appId) {
            case "490489ab-d8b4-414c-ad77-d856962c286f":
                transformedData.sign_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "13131313":
                transformedData.tenant_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "70588803-52e4-433d-a61f-0a68e1febd72":
                transformedData.manage_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "313123131":
                transformedData.backstage_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "70588803-52e4-ss3d-a61f-0a68e1febd72":
                transformedData.auth_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "70588803-52e4-ss3d-a61f-0a68e1febder":
                transformedData.message_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "70588803-52e4-ss3d-a61f-0a68e1febxbn":
                transformedData.scan_code_service = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
                break;
            case "70588803-52e4-ss3d-a61f-0a68e1fe12d":
                transformedData.mobile_app_info = {
                    url: item.url,
                    appCode: item.appCode,
                    appId: item.appId
                };
            default:
                break;
        }
    });

    return transformedData;
}

async function bootstrap() {
  

  app.use(router);
  
  // 待单独写成文件调用
  app.config.globalProperties.eventHub = eventHub;


  setupRouterGuard(router);

  // Configure store
  setupStore(app);

  // Initialize internal system configuration
  initAppConfigStore();

  // // sensitice opetration
  // setupSensitiveHandle();

  // Register global components
  registerGlobComp(app);

  // Multilingual configuration
  // Asynchronous case: language files may be obtained from the server side

  // Configure routing
  setupRouter(app);

  await setupI18n(app);


  // router-guard

  //micro lister

  // Register global directive
  setupGlobDirectives(app);

  // Configure global error handling
  setupErrorHandle(app);

  // https://next.router.vuejs.org/api/#isready
  // await router.isReady();
  // app.use(Antd)
  app.mount('#app');

  axios.get('/resrun-paas/sys/listApp?t=' + new Date().getTime()).then(res => {
    if (res.data.result) {
        window.appInfo = transformAppInfoData(res.data.result);
    }
  })

}
bootstrap();

/**
 * @description : menu
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

import { defineStore } from 'pinia';
import session from '@/utils/cache/session';
import {
  USER_INFO_KEY,
  APP_LOGIN_TOKEN,
  APP_AUTHORIZED_TOKEN,
  APP_TOKEN,
  APP_CODE,
  TENANT_DEPARTS,
  LOGIN_TENANT_INFO,
  APP_WEB_CONFIG,
} from '@/utils/cache/constant';

import Api, { LoginParams, LoginResult } from '@/api/user';
// import type { UserInfo } from '@/store';

export const useUserStore = defineStore('user', {
  // id: 'user', // id必填，且需要唯一。两种写法
  state: () => {
    return {
      userInfo: null,
      appToken: null,
      authToken: null,
      tenantDeparts: null,
      loginDepart: null,
      tenantInfo: null,
      webConfig: undefined,
      lastUpdateTime: 0,
    };
  },
  getters: {
    getWebConfig(): any {
      return this.webConfig || session.getItem(APP_WEB_CONFIG);
    },
    getUserInfo(): any {
      return this.userInfo || session.getItem(USER_INFO_KEY) || {};
    },
    getAppToken(): string {
      return this.appToken || session.getItem(APP_TOKEN) || null;
    },
    getAuthToken(): string {
      return this.authToken || session.getItem(APP_AUTHORIZED_TOKEN) || null;
    },
    getTenantDeparts(): any {
      const result = this.tenantDeparts || session.getItem(TENANT_DEPARTS) || [];
      console.log('getTenantDeparts', result);
      return this.tenantDeparts || session.getItem(TENANT_DEPARTS) || [];
    },
    getTenantInfo(): any {
      return this.tenantInfo || session.getItem(LOGIN_TENANT_INFO) || {};
    },
    getLastUpdateTime(): number {
      return this.lastUpdateTime;
    },
  },
  actions: {
    setWebConfig(info: any | null) {
      this.webConfig = info;
      session.setItem(APP_WEB_CONFIG, info);
    },
    setUserInfo(info: any | null) {
      this.userInfo = info ? info : {}; // for null or undefined value
      session.setItem(USER_INFO_KEY, info);
      this.lastUpdateTime = new Date().getTime();
    },
    setAppToken(info: string | null | undefined) {
      this.appToken = info ? info : ''; // for null or undefined value
      session.setItem(APP_TOKEN, info);
    },
    setAuthToken(info: string | null | undefined) {
      this.authToken = info ? info : ''; // for null or undefined value
      session.setItem(APP_AUTHORIZED_TOKEN, info);
    },
    setTenantDeparts(info: any) {
      this.tenantDeparts = info ? info : []; // for null or undefined value
      session.setItem(TENANT_DEPARTS, info);
    },
    setTenantInfo(info: any) {
      this.tenantInfo = info ? info : {}; // for null or undefined value
      session.setItem(LOGIN_TENANT_INFO, info);
      this.lastUpdateTime = new Date().getTime();
    },
    removeSessionAll() {
      session.clear();
      this.setUserInfo(null);
      this.setAppToken(null);
      this.setAuthToken(null);
      this.setTenantDeparts(null);
      this.setTenantInfo(null);
      this.setWebConfig(null);
    },
    logout() {
      //TODO  调用登出接口
      this.removeSessionAll();
      return true;
    },
    async phoneLogin(params: LoginParams) {
      const loginResult = { status: false, depts: [] };
      params.appCode = APP_CODE;
      params.type = 'phone';
      const data = await Api.phoneLogin(params);
      if (data.code == 200) {
        const { appToken, authorizedToken, user_tenant_depart } = data.result as LoginResult;
        this.setAppToken(appToken);
        this.setAuthToken(authorizedToken);
        // this.reloadTenantDeparts();
        loginResult.status = true;
        loginResult.depts = user_tenant_depart;
      }
      return loginResult;
    },
    async login(params: LoginParams) {
      const loginResult = { status: false, depts: [] };
      params.appCode = APP_CODE;
      const data = await Api.login(params);
      if (data.code == 200) {
        const { appToken, authorizedToken, user_tenant_depart } = data.result as LoginResult;
        this.setAppToken(appToken);
        this.setAuthToken(authorizedToken);
        loginResult.status = true;
        loginResult.depts = user_tenant_depart;
      }
      return loginResult;
    },
    async registerOrLogin(params: LoginParams) {
      const loginResult = { status: false, depts: [] };
      params.appCode = APP_CODE;
      params.type = 'phone';
      const data = await Api.linRegisterOrLogin(params);
      if (data.code == 200) {
        const { appToken, authorizedToken, user_tenant_depart } = data.result as LoginResult;
        this.setAppToken(appToken);
        this.setAuthToken(authorizedToken);
        // this.reloadTenantDeparts();
        loginResult.status = true;
        loginResult.depts = user_tenant_depart;
      }
      return loginResult;
    },
    async reloadTenantDeparts(): any {
      const tenantDepts = await Api.getMyAppTenantDeparts();
      this.setTenantDeparts(tenantDepts.result);
      return tenantDepts.result;
    },
    async selectTenant(params: any): any {
      const result = { status: false };
      const data = await Api.loginBySelectAccount(params);
      this.setAppToken(data.result);
      // await this.getTnantInfo();
      await this.reloadTenantInfo();
      await this.afterLoginAction();
      await this.reloadTenantDeparts();
      result.status = true;
      return result;
    },
    async singleSign(data: any) {
      this.removeSessionAll();
      this.setAppToken(data.appToken);
      this.setAuthToken(data.authorizedToken);
      await this.afterLoginAction();
      // await this.getTnantInfo();
      await this.reloadTenantInfo();
    },
    async reloadTenantInfo() {
      if (!this.getAppToken) return null;
      const data = await Api.getUserExtendInfo();
      this.setTenantInfo(data.result);
    },
    async afterLoginAction() {
      if (!this.getAppToken) return null;
      const data = await Api.getUserInfo();
      this.setUserInfo(data.result);
    },
    async buildWebConfig() {
      if (!this.getWebConfig) {
        const web: any = await Api.getWebsiteConfig();
        const webConfig: any = {
          websiteTitle: web.result.website_title || '电子签章',
          websiteWhiteLogo: web.result.website_white_logo,
          websiteOtherLogo: web.result.website_other_logo,
          systenAccountType: web.result.system_account_type || 'phone_email',
        };
        this.webConfig = webConfig;
        session.setItem(APP_WEB_CONFIG, webConfig);
      }
    },
  },
});

/**
 * @description 用户相关API
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

import http, { Response } from '@/utils/http';

import { appHeader, authHeader, realNameHeader } from '../';
export interface LoginParams {
  appCode?: string;
  captcha?: string;
  captchaKey?: string | null;
  phone?: string;
  type?: string;
  password?: string;
  checkKey?: string;
  username?: string;
  email?: string;
  account?: string;
}

interface UserInfo {
  id: number;
  username: string;
  mobile: number;
  email: string;
}

export interface LoginResult {
  user_tenant_depart: [];
  appToken: string;
  authorizedToken: string;
}

export default {
  async phoneLogin(params: LoginParams) {
    return await http.post<Response<LoginResult>>('/sys/phoneLogin', params);
  },
  async login(params: LoginParams) {
    return await http.post<Response<LoginResult>>('/sys/login', params);
  },
  async sendCode(params: any) {
    return await http.get<Response>('/sys/sendMessage', params);
  },
  async sendEmailCode(params: any) {
    return await http.get<Response>('/sys/sendEmail', params);
  },
  async loginBySelectAccount(params: any) {
    return await http.put<Response<string>>('/sys/updateTenantDepart', params, appHeader());
  },
  async getUserExtendInfo() {
    return await http.get<Response>('/system/tenantInfoExtend/getTenantInfoExt', {}, appHeader());
  },
  async getUserInfo() {
    return await http.get<Response>('/sys/user/getUserInfo', {}, appHeader());
  },
  async getMyAppTenantDeparts() {
    return await http.get<Response>('/sys/getMyAppTenantDeparts', {}, appHeader());
  },
  async openPersonalTenant() {
    return await http.put<Response>('/system/sysTenantInfo/openPersonalTenant', {}, appHeader());
  },
  async jionTenant(params: any) {
    return await http.put<Response>('/system/sysTenantInfo/jionTenant', params, appHeader());
  },
  async personAuth(params: any) {
    return await http.post<Response>(
      '/system/tenantInfoExtend/person/realAuth',
      params,
      realNameHeader()
    );
  },
  async checkFace(params: any) {
    return await http.get<Response>(
      'system/tenantInfoExtend/person/auth/check',
      params,
      appHeader()
    );
  },
  async personAuthType() {
    return await http.get<Response>('system/tenantInfoExtend/person/auth/type', {}, appHeader());
  },
  async enterpriseAuth(params: any) {
    return await http.post<Response>(
      '/system/tenantInfoExtend/enterprise/realAuth',
      params,
      appHeader()
    );
  },
  async linRegisterOrLogin(params: any) {
    return await http.post<Response>('/sys/codeRegisterLogin', params);
  },

  // WhiteLogo = 'file/downloadFileBase64Type/website_white_logo',
  // OtherLogo = 'file/downloadFileBase64Type/website_other_logo',
  // WebsiteTitle = '/sys/websiteTitle',
  async getWhiteLogo(params: any) {
    return await http.get<Response>('/file/downloadFileBase64Type/website_white_logo', params);
  },
  async getOtherLogo(params: any) {
    return await http.get<Response>('/file/downloadFileBase64Type/website_other_logo', params);
  },
  async getWebTitle(params: any) {
    return await http.get<Response>('/sys/websiteTitle', params);
  },
  async getWebsiteConfig() {
    return await http.get<Response>('/sys/websiteConfig');
  },

  async companyAuthApi(params: any) {
    return await http.post<Response>('/yundun/auth/enterprise/add', params, appHeader());
  },
  async personAuthApi(params: any) {
    return await http.post<Response>('/yundun/auth/personal/add', params, appHeader());
  },
};

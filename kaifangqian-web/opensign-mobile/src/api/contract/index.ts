/**
 * @description 合同签署相关API
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

import { appHeader, authHeader } from '../';

export interface LoginParams {
  appCode?: string;
  captcha: string;
  captchaKey: string | null;
  phone?: string;
  email?: string;
  account?: string;
  type?: string;
}

export default {
  async getListMy(params: any) {
    return await http.get<Response>('/company/task/listMyJob', params, appHeader());
  },
  async getListAll(params: any) {
    return await http.get<Response>('/company/task/listAll', params, appHeader());
  },
  async getListSend(params: any) {
    return await http.get<Response>('/company/task/listSend', params, appHeader());
  },
  async getListCopyMe(params: any) {
    return await http.get<Response>('/company/task/listCopyMe', params, appHeader());
  },
  async getListFinish(params: any) {
    return await http.get<Response>('/company/task/listFinish', params, appHeader());
  },
  async getListInvalid(params: any) {
    return await http.get<Response>('/company/task/listInvalid', params, appHeader());
  },
  async checkOperate(params: any) {
    return await http.get<Response>('/company/task/checkOperate', params, appHeader());
  },
  async getDocFiles(params: any) {
    return await http.get<Response>('/sign/ru/list/file', params, appHeader());
  },
  async getDocImgsById(params: any) {
    return await http.get<Response>('/sign/file/list/image/annex', params, appHeader());
  },
  async getDocImgsInSign(params: any) {
    return await http.get<Response>('/sign/ru/list/image', params, appHeader());
  },
  async getDocInfoByRuId(params: any) {
    return await http.get<Response>('/sign/ru/info/base', params, appHeader());
  },
  async getOperators(params: any) {
    return await http.get<Response>('/sign/ru/list/operator/status', params, appHeader());
  },
  async getDocControlsByDocId(params: any) {
    return await http.get<Response>('/sign/ru/list/control', params, appHeader());
  },
  async getNewTokenByAppLoginToken(params: any) {
    return await http.get<Response>('/sys/user/getNewToken', params, authHeader());
  },
  async rejectWrite(params: any) {
    return await http.post<Response>('/sign/ru/run/reject/write', params, appHeader());
  },
  async getSignerIdAndSealId(params: any) {
    return await http.get<Response>('/sign/ru/run/signer/identify', params, appHeader());
  },
  async submitWrite(params: any) {
    return await http.post<Response>('/sign/ru/run/submit/write', params, appHeader());
  },
  async saveWrite(params: any) {
    return await http.post<Response>('/sign/ru/start/save/write', params, appHeader());
  },
  async getLinkParams(params: any) {
    return await http.get<Response>('/mes/message_3rdRecord/getParaByCode', params, appHeader());
  },
  async getLinkRuInfo(params: any) {
    return await http.get<Response>('/sign/ru/info/link', params, appHeader());
  },
  async selectTenant(params: any) {
    return await http.get<Response>('/sign/ru/info/link', params, appHeader());
  },
  async sendCode(params: any) {
    return await http.get<Response>('/sys/sendMessage', params, appHeader());
  },
  async sendEmailCode(params: any) {
    return await http.get<Response>('/sys/sendEmail', params);
  },
  async openPersonalTenant(params: any) {
    return await http.put<Response>(
      '/system/sysTenantInfo/openPersonalTenant',
      params,
      appHeader()
    );
  },
  async createCompany(params: any) {
    return await http.post<Response>(
      '/system/tenantInfoExtend/enterprise/add',
      params,
      appHeader()
    );
  },
  async checkNOAuthType(params: any) {
    return await http.get<Response>('/sign/ru/info/operator/check', params, appHeader());
  },
  async getViewCheck(params: any) {
    return await http.get<Response>('/sign/ru/info/view/check', params, appHeader());
  },
  async linRegisterOrLogin(params: any) {
    return await http.post<Response>('/sys/phoneRegisterLogin', params, appHeader());
  },
  async verifyCertificate(params: any) {
    return await http.get<Response>('/sign/ru/run/certificate/verify', params, appHeader());
  },
  async submitSign(params: any) {
    return await http.post<Response>('/sign/ru/run/phone/submit/sign', params, appHeader());
  },
  async rejectSign(params: any) {
    return await http.post<Response>('/sign/ru/run/reject/sign', params, appHeader());
  },
  async updateCertificate(params: any) {
    return await http.post<Response>('/sign/ru/run/certificate/update', params, appHeader());
  },
  async getSignatureList(params: any) {
    return await http.get<Response>('/sign/person/seal/list', params, appHeader());
  },
  async getBase64ById(params: any) {
    return await http.get<Response>('/file/downloadFileBase/' + params.id, params, appHeader());
  },
  async getSignKey(params: any) {
    return await http.get<Response>('/sign/ru/sign/getKey', params, appHeader());
  },
  async verifySignSeal(params: any) {
    return await http.get<Response>('/sign/ru/run/seal/verify', params, appHeader());
  },
  async getSignSealIds(params: any) {
    return await http.get<Response>(
      '/sign/ru/entSeal/enabled/list/authorized',
      params,
      appHeader()
    );
  },
  async checkOperatorStatus(params: any) {
    return await http.get<Response>('/sign/ru/info/operator/status', params, appHeader());
  },
  async getSignAuthStatus(params: any) {
    return await http.get<Response>(
      '/system/sysTenantInfo/getTenantAuthStatus',
      params,
      appHeader()
    );
  },
  async chekTokenEffective(params: any) {
    return await http.get<Response>('/sys/check2', params);
  },
  async getOrderNo(params: any) {
    return await http.get<Response>('/user/confirm/getOrderNo', params, appHeader());
  },
  async getConfirmType(params: any) {
    return await http.get<Response>('/sign/ru/run/sign/confirm', params, appHeader());
  },
  async getCallbackPath(params: any) {
    return await http.get<Response>('/sign/ru/get/callbackPageUrl', params, appHeader());
  },
  //设置签约密码
  async setSignPassword(params: any) {
    return await http.post<Response>('/sys/sysUserConfig/updatePassword', params, appHeader());
  },
  async getCodeBySms(params: any) {
    return await http.get<Response>('/user/confirm/sendMessage', params, appHeader());
  },
  async getCodeByEmail(params: any) {
    return await http.get<Response>('/user/confirm/sendEmail', params, appHeader());
  },
  async submitConfirmParams(params: any) {
    return await http.post<Response>('/user/confirm/confirmPara', params, appHeader());
  },
  async submitConfirmByPassword(params: any) {
    return await http.post<Response>('/user/confirm/confirmByPassword', params, appHeader());
  },
  async submitConfirmByDoubleAnd(params: any) {
    return await http.post<Response>('/user/confirm/confirmByPhoneEmail', params, appHeader());
  },
  async getSignConfig(params: any) {
    return await http.get<Response>('/sys/sysUserConfig', params, appHeader());
  },
  async sendEmaildownlaod(params: any, urlSuffix: string) {
    return await http.get<Response>('/sign/ru/doc/file/download' + urlSuffix, params, appHeader());
  },
  async getVerifyCertificate(params: any) {
    return await http.get<Response>('/sign/ru/run/certificate/verify', params, appHeader());
  },
  async confirmSignRu(params: any) {
    return await http.post<Response>('/sign/ru/run/completed/sign', params, appHeader());
  },
  async getBaseRuInfo(params: any) {
    return await http.get<Response>('/sign/ru/info/link', params, appHeader());
  },
};

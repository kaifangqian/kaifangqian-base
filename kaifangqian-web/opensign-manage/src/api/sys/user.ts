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

/*
 * @Author: ningw 
 * @Date: 2022-06-28 14:32:55 
 * @Last Modified by: ningw
 * @Last Modified time: 2024-04-22 15:42:56
 */
import { defHttp } from '/@/utils/http/axios';
import { LoginParams, LoginResultModel, GetUserInfoModel } from './model/userModel';
import { ErrorMessageMode,UploadFileParams } from '/#/axios';
import { getLoginToken } from '/@/utils/auth';
import { isDevMode } from '/@/utils/env';

enum Api {
  WhiteLogo = '/file/downloadFileBase64Type/website_white_logo',
  OtherLogo = '/file/downloadFileBase64Type/website_other_logo',
  WebsiteTitle = '/sys/websiteTitle',
  Login = '/sys/login',
  TenantDeparts = '/sys/getMyAppTenantDeparts',
  UserJoin = '/sys/userJion',
  OpenPersonalTenant = '/sys/openPersonalTenant',
  LoginMobile = '/sys/phoneLogin',
  VisitorLogin = '/sys/visitorLogin',
  UserUpdatePassword = '/sys/updatePassword',
  UserUpdatePasswordByToken = '/sys/updatePasswordToken',
  SendEmail = '/sys/sendEmail',
  UnbindPhone = '/sys/unbindPhone',
  BindPhone = '/sys/bindPhone',
  UnbindEmail = '/sys/unbindEmail',
  BindEmail = '/sys/bindEmail',

  UsernameSendCode = '/sys/sendMessageByUsername',
  UsernameSendEmail= '/sys/sendEmailByUsername',

  Logout = '/sys/logout',
  LoginSmsCode = '/sys/sendMessage',
  GetUserInfo = '/sys/user/getUserInfo',
  GetTenantInfo = '/system/tenantInfoExtend/getTenantInfoExt',
  GetPermCode = '/sys/permission/getPermCode',
  GetSensitive = '/api/monitorConfig/getSensitiveOP',
  GetImgCode = '/sys/randomImage',
  TestRetry = '/testRetry',
  AccountList = '/getAccounts',
  SelectAccount = '/sys/updateTenantDepart',
  FindUserName = '/sys/findUsername',
  UserValidateInfo = '/sys/validate',

  CheckRepeat = '/sys/user/checkExist',
  UserList = '/sys/user/list',
  AddUser = '/sys/user/add',
  EditUser = '/sys/user/edit',
  InfoUser = '/sys/user/userInfo',
  InfoUserOrg = '/sys/user/tenantUserInfo',

  DeleteUser = '/sys/user/delete',
  DeleteBatchUser = '/sys/user/deleteBatch',
  BrozenBatch = '/sys/user/frozenBatch',
  DeptUserList = '/sys/sysDepart/getUsersByDepartId',
  updateDepartBatch = '/sys/user/updateDepartBatch',
  UserUpdateRealname = '/sys/user/updateRealname',
  UserResetPassword = '/sys/user/resetPassword',
  ImgBase64 = '/file/downloadFileBase',
  ImgStream = '/file/downloadFileStream',

  GetUserDepartsList = '/sys/sysDepart/queryMySelectList',

  AllUserList = '/sys/user/getAllUsers',

  TokenSmsCode = '/sys/sendMessageByToken',
  TokenEmailCode = '/sys/sendEmailByToken',

  
  CheckToken ='/sys//user/checkToken',

  UserJoinList = '/sys/user/join',

  TenantUser = '/sysTenantUserRecord/list',
  TenantUserCheck = '/sysTenantUserRecord/check',
  GetAppTokenByAuthToken = '/sys/user/getNewToken',

  



  
}


/**
 * @description: 获取系统logo白色
 */
export function getWhiteLogo(params) {
  return defHttp.get({ url: Api.WhiteLogo, params });
}
/**
* @description: 获取系统title
*/
export function getWebTitle(params) {
  return defHttp.get({ url: Api.WebsiteTitle, params });
}
/**
* @description: 获取系统logo
*/
export function getOtherLogo(params) {
  return defHttp.get({ url: Api.OtherLogo, params });
}


/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<LoginResultModel>(
    {
      url: Api.Login,
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

/**
 * 校验token是否一致
 */

export function checkToken(params){
  const localToken = isDevMode()?(localStorage.getItem('VUE_RESRUN_MANAGE__DEVELOPMENT__2.8.0__COMMON__LOCAL__KEY__')? 
        JSON.parse(localStorage.getItem('VUE_RESRUN_MANAGE__DEVELOPMENT__2.8.0__COMMON__LOCAL__KEY__') as string ).value.TOKEN__.value:''):
        localStorage.getItem('VUE_RESRUN_MANAGE__PRODUCTION__2.8.0__COMMON__LOCAL__KEY__')? 
        JSON.parse(localStorage.getItem('VUE_RESRUN_MANAGE__PRODUCTION__2.8.0__COMMON__LOCAL__KEY__') as string ).value.TOKEN__.value:'';
  return defHttp.get({ url: Api.CheckToken ,params},{isReturnNativeResponse:true,  withToken:false, loginToken:localToken });
}

export function doLogout() {
  const localToken = isDevMode()?(localStorage.getItem('VUE_RESRUN_MANAGE__DEVELOPMENT__2.8.0__COMMON__LOCAL__KEY__')? 
        JSON.parse(localStorage.getItem('VUE_RESRUN_MANAGE__DEVELOPMENT__2.8.0__COMMON__LOCAL__KEY__') as string ).value.TOKEN__.value:''):
        localStorage.getItem('VUE_RESRUN_MANAGE__PRODUCTION__2.8.0__COMMON__LOCAL__KEY__')? 
        JSON.parse(localStorage.getItem('VUE_RESRUN_MANAGE__PRODUCTION__2.8.0__COMMON__LOCAL__KEY__') as string ).value.TOKEN__.value:'';
    return defHttp.get({ url: Api.Logout },{isReturnNativeResponse:true,  withToken:false, loginToken:localToken });
}
export function doLogoutAll() {
  const loginToken:string = getLoginToken() as string;
  return defHttp.get({ url: Api.Logout },{ withToken:false, loginToken:loginToken });
}



// 根据授权token获取应用token
export function getAppTokenByAuthToken(params) {
  const loginToken:string = getLoginToken() as string;
  return defHttp.get({ url: Api.GetAppTokenByAuthToken, params },{ withToken:false, loginToken:loginToken });
}

/**
 * @description: 成员加入申请
 */
export function getJoinMemberData() {
  return defHttp.get({ url: Api.TenantUser }, { errorMessageMode: 'none' });
}
/**
 * @description: 成员加入申请审核
 */
export function checkTenantUser(params) {
  return defHttp.put({ url: Api.TenantUserCheck,params }, { errorMessageMode: 'none' });
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}
/**
 * @description: getTenantInfo
 */
export function getUserExtendInfo() {
  return defHttp.get({ url: Api.GetTenantInfo }, { errorMessageMode: 'none' });
}
// /**
//  * @description: 成员加入申请
//  */
// export function getJoinMemberData() {
//   return defHttp.get({ url: Api.UserJoinList }, { errorMessageMode: 'none' });
// }
export function getTokenSms(params) {
  return defHttp.get({ url: Api.TokenSmsCode ,params}, { errorMessageMode: 'none' });
}
export function getTokenEmail(params) {
  return defHttp.get({ url: Api.TokenEmailCode,params }, { errorMessageMode: 'none' });
}
/**
 * @description: 校验用户信息是否重复
 */
export function checkUserInfoRepeat(params) {
  return defHttp.get({ url: Api.CheckRepeat ,params}, { errorMessageMode: 'none' });
}

export function getPermCode() {
  return defHttp.get<string[]>({ url: Api.GetPermCode });
}
export function getSensitiveCode() {
  return defHttp.get({ url: Api.GetSensitive });
}
/**
 * @desc 获取租户部门列表
 * @returns 
 */
export function getMyTenantDeparts() {
  return defHttp.get({ url: Api.TenantDeparts });
}
/**
 * @desc 开通个人空间
 * @returns 
 */
export function openPersonalTenant() {
  return defHttp.put({ url: Api.OpenPersonalTenant });
}
/**
 * @desc 加入已有企业或租户
 * @returns 
 */
export function userJion(params) {
  return defHttp.put({ url: Api.UserJoin, params });
}

export function getImgCode(params) {
  return defHttp.get({ url: Api.GetImgCode +'/'+ params.key });
}

export function getAccountList(params) {
  return defHttp.get({ url: Api.AccountList,params });
}

export function getAllUserList() {
  return defHttp.get({ url: Api.UserList });
}

export function addUser(params) {
  return defHttp.post({ url: Api.AddUser,params });
}

export function editUser(params) {
  return defHttp.post({ url: Api.EditUser,params });
}

/**
 * @desc 角色人员详情
 * @param params 
 * @returns 
 */
export function getUserInfoById(params) {
  return defHttp.get({ url: Api.InfoUser,params });
}
/**
 * @desc 组织管理人员详情
 * @param params 
 * @returns 
 */
export function getUserInfoOrgById(params) {
  return defHttp.get({ url: Api.InfoUserOrg,params });
}

export function deleteUser(params) {
  return defHttp.delete({ url: Api.DeleteUser,params });
}
export function deleteUserBatch(params) {
  return defHttp.delete({ url: Api.DeleteBatchUser,params });
}

export function setBrozenBatch(params) {
  return defHttp.put({ url: Api.BrozenBatch,params });
}

export function loginBySelectAccount(params) {
  return defHttp.put({ url: Api.SelectAccount,params });
}

export function changeDept(params) {
  return defHttp.put({ url: Api.updateDepartBatch,params });
}

export function getSmsCode(params) {
  return defHttp.get({ url: Api.LoginSmsCode,params });
}
export function getSmsCodeByUsername(params) {
  return defHttp.get({ url: Api.UsernameSendCode,params });
}
export function getEmailCodeByUsername(params) {
  return defHttp.get({ url: Api.UsernameSendEmail,params });
}
export function getEmailCode(params) {
  return defHttp.get({ url: Api.SendEmail,params });
}
/**
 * @descrption 找回账号
 * @param params 
 * @returns 
 */
export function getUserName(params) {
  return defHttp.get({ url: Api.FindUserName,params });
}

export function LoginByMobile(params) {
  return defHttp.post({ url: Api.LoginMobile,params });
}
export function LoginVisitor(params) {
  return defHttp.post({ url: Api.VisitorLogin,params });
}

export function updatePassword(params) {
  return defHttp.post({ url: Api.UserUpdatePassword,params });
}

export function updatePasswordByToken(params) {
  return defHttp.post({ url: Api.UserUpdatePasswordByToken,params });
}
export function unBindTelephone(params) {
  return defHttp.post({ url: Api.UnbindPhone,params });
}
export function bindTelephone(params) {
  return defHttp.post({ url: Api.BindPhone,params });
}
export function unBindEmail(params) {
  return defHttp.post({ url: Api.UnbindEmail,params });
}
export function bindEmail(params) {
  return defHttp.post({ url: Api.BindEmail,params });
}
export function updateUserRealname(params) {
  return defHttp.post({ url: Api.UserUpdateRealname,params });
}
export function resetPassword(params) {
  return defHttp.put({ url: Api.UserResetPassword,params });
}
export function getUserDepartsList() {
  return defHttp.get({ url: Api.GetUserDepartsList });
}
export function getUserByDeptId(params) {
  return defHttp.get({ url: Api.DeptUserList,params });
}

export function validateUserInfo(params) {
  return defHttp.get({ url: Api.UserValidateInfo,params });
}

export function getAllUser() {
  return defHttp.get({ url: Api.AllUserList });
}



export function testRetry() {
  return defHttp.get(
    { url: Api.TestRetry },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}

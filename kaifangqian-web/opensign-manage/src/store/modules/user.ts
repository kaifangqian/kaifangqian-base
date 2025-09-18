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

import type { UserInfo, TenantDepartList,SafeInfo, PerInfo, TenantInfo } from '/#/store';
import type { ErrorMessageMode } from '/#/axios';
import { defineStore } from 'pinia';
import { store } from '/@/store';
import { RoleEnum } from '/@/enums/roleEnum';
import { PageEnum } from '/@/enums/pageEnum';
import { ROLES_KEY, TOKEN_KEY, USER_INFO_KEY, LOGIN_TOKEN_KEY, TENANT_INFO_KEY} from '/@/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '/@/utils/auth';
import { GetUserInfoModel, LoginParams ,TenantParams, LoginResultModel} from '/@/api/sys/model/userModel';
import { doLogout, doLogoutAll, getUserInfo, loginApi, getImgCode, loginBySelectAccount, LoginByMobile, LoginVisitor, getUserExtendInfo} from '/@/api/sys/user';
import { useMessage } from '/@/hooks/web/useMessage';
import { router } from '/@/router';
import { Modal, Button } from 'ant-design-vue';
import { usePermissionStore } from '/@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '/@/router/routes/basic';
import { isArray } from '/@/utils/is';
import { h } from 'vue';

interface UserState <T = any> {
  userInfo: Nullable<UserInfo>;
  tenantInfo:Nullable<TenantInfo>;
  safeInfo:T;
  token?: string;
  pageToken?: string;
  tokenInvalidType: number;
  loginToken?: string;
  roleList: RoleEnum[];
  sessionTimeout?: boolean;
  keepLoginAlive?: boolean;
  lastUpdateTime: number;
  changePasswordTimer:Nullable<T>,
  permissionInfo:T,
  tenantList:TenantDepartList[]
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    // token失效类型
    tokenInvalidType:200,
    // user info
    tenantInfo: null,
    //safe status info
    safeInfo:{}, 
    // token
    token: undefined,
    // pagetoken  页面缓存token
    pageToken: undefined,
    // login token
    loginToken: undefined,
    // roleList
    roleList: [],
    // Whether the login expired
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
    //change password tip
    changePasswordTimer:null,
    // show mac login timeout modal
    keepLoginAlive: true,

    // auth and menu info
    permissionInfo:{},

    // tenant info
    tenantList:[]

  }),
  getters: {
    getKeepLoginAlive() :boolean {
      return !!this.keepLoginAlive;
    },
    getUserInfo(): UserInfo {
      return this.userInfo || getAuthCache<UserInfo>(USER_INFO_KEY) || {};
    },
    getTenantInfo(): TenantInfo {
      return this.tenantInfo || getAuthCache<TenantInfo>(TENANT_INFO_KEY) || {};
    },
    getSafeInfo(): SafeInfo {
      return this.safeInfo  
    },
    getChangePasswordTimer(): any {
        return this.changePasswordTimer;
    },
    getToken(): string {
      return this.token || getAuthCache<string>(TOKEN_KEY);
    },
    getPageToken(): string {
      return this.pageToken || '';
    },
    getTokenInvalidType():number {
      return this.tokenInvalidType
    },
    getLoginToken(): string {
      return this.loginToken || getAuthCache<string>(LOGIN_TOKEN_KEY);
    },
    getRoleList(): RoleEnum[] {
      return this.roleList.length > 0 ? this.roleList : getAuthCache<RoleEnum[]>(ROLES_KEY);
    },
    getSessionTimeout(): boolean {
      return !!this.sessionTimeout;
    },
    getLastUpdateTime(): number {
      return this.lastUpdateTime;
    },
    getPermissionInfo() : PerInfo{
      return this.permissionInfo;
    }
  },
  actions: {
    setTokenInvalidType(type:number){
      this.tokenInvalidType = type;
    },
    setKeepLoginAlive(flag: boolean){
      this.keepLoginAlive = flag;
    },
    setToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(TOKEN_KEY, info);
    },
    setPageToken(info: string | undefined) {
      this.pageToken = info ? info : ''; // for null or undefined value
    },
    setTkenInvalidType(info:number) {
      this.tokenInvalidType = info;
    },
    setLoginToken(info: string | undefined) {
      this.loginToken = info ? info : ''; // for null or undefined value
      setAuthCache(LOGIN_TOKEN_KEY, info);
    },
    clearChangePasswordTimer(){
      clearInterval(this.changePasswordTimer);
    },
    setRoleList(roleList: RoleEnum[]) {
      this.roleList = roleList;
      setAuthCache(ROLES_KEY, roleList);
    },
    setUserInfo(info: UserInfo | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    setTenantInfo(info: TenantInfo | null) {
      this.tenantInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(TENANT_INFO_KEY, info);
    },
    setSafeInfo(info:SafeInfo) {
      this.safeInfo = {
        ... this.safeInfo,
        ...info
      };
    },
    setPermissionInfo(info:PerInfo){
      this.permissionInfo = info;
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
    },
    resetState() {
      this.userInfo = null;
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = false;
    },
    /**
     * @description: code
     */
    async getLoginCode(){
      const data = await getImgCode({});
      return data;
    },
    /**
     * @description: login
     */
    async login(
      params: LoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      },
    ): Promise<LoginResultModel | GetUserInfoModel | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        let  data 
        if(params.type && params.type==='phone'){
          data = await LoginByMobile(loginParams);
        }if(params.type && params.type === 'visitor'){
          data = await LoginVisitor(loginParams);
        }else if(!params.type){
          data = await loginApi(loginParams, mode);
        }
        //清除本地缓存 防止携带上次登录token
        localStorage.clear();
        const { appToken,authorizedToken} = data;
        // save token
        this.setToken(appToken);
        this.setLoginToken(authorizedToken);
        return this.afterLoginAction(goHome);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async selectTenant(params:TenantParams):Promise<GetUserInfoModel | null>{
      const accountInfo = await loginBySelectAccount(params)
      console.log(accountInfo,'账户信息')
      this.setToken(accountInfo);
      if(accountInfo){
          return this.afterLoginAction(true);
      }else{
        return accountInfo
      }
    },

    async afterLoginAction(goHome?: boolean): Promise<GetUserInfoModel | null> {
      if (!this.getToken) return null;
      // get user info
      const userInfo = await this.getUserInfoAction();
      // this.getTnantInfo();
           
      const sessionTimeout = this.sessionTimeout;
      if (sessionTimeout) {
        this.setSessionTimeout(false);
      } else {
        const permissionStore = usePermissionStore();
        if (!permissionStore.isDynamicAddedRoute) {
          const routes = await permissionStore.buildRoutesAction();
          routes.forEach((route) => {
            router.addRoute(route as unknown as RouteRecordRaw);
          });
          router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
          permissionStore.setDynamicAddedRoute(true);
        }
        goHome && (await router.replace(userInfo?.homePath || PageEnum.BASE_HOME));
      }
      return  userInfo;
    },

     /**
     * 租户信息
     */
     async  getTnantInfo(){
      if (!this.getToken) return null;
      const tenantInfo = await getUserExtendInfo();
      this.setTenantInfo(tenantInfo);
    },

    async getUserInfoAction(): Promise<UserInfo | null> {
      if (!this.getToken) return null;
      const userInfo = await getUserInfo();
      const { roles = [] } = userInfo;
      if (isArray(roles)) {
        const roleList = roles.map((item) => item.value) as RoleEnum[];
        this.setRoleList(roleList);
      } else {
        userInfo.roles = [];
        this.setRoleList([]);
      }
      if(userInfo.passwordEditFlag){
        this.changePassWordWarning()
      }
      //将缓存token跟更新至页面缓存 token
      this.setPageToken(this.getToken);
      this.setUserInfo(userInfo);
      return userInfo;
    },
    /**
     * @description: login timeout confirm
     */

    confirmRetryLogin(){
      const {createWarningModal } = useMessage();
      createWarningModal({
        title: '', 
        content: '登录超时,请重新登录。!',
        iconType: 'warning',
        onOk: async () => {
          await this.logout(true);
        },
      })
    },
    /**
     * @description: change password warning
     */
    changePassWordWarning(){
      let clickFlag = false;
      this.changePasswordTimer = setInterval(()=>{
        if(clickFlag) return;
        clickFlag = true;
        Modal.confirm({
            title: '提示', 
            content: '您的密码已失效，请修改密码后重新登录',
            iconType: 'warning',
            okText: '修改密码',
            cancelText: '取消',
            onOk() {
              router.push('/user/center/:change');
              clickFlag = false;
              // clearInterval(timer);
            },
            onCancel() {
              clickFlag = false;
            }
        })
      },10*60*1000);
      // },6*1000);
    },
    /**
     * @description: logout
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          await doLogout();
        } catch {
          console.log('注销Token失败');
        }
      }
      this.setToken(undefined);
      this.setSessionTimeout(false);

      const appAuthInfo =  window?.appInfo.sign_app_info;
      const appBusinessInfo =  window?.appInfo.manage_app_info;
      var hash = location.hash || '/#/overview';
      hash = hash.startsWith("/")?hash:"/"+hash;
      const hashPath = hash.split("?");
      window.open(appAuthInfo?.url+'/#/auth/login?appId='+ appBusinessInfo.appId + '&appCode='+ appBusinessInfo.appCode +'&redirect='+ encodeURIComponent(appBusinessInfo?.url+hashPath[0]),'_self')
    },

    /**
     * 退出所有应用
     */
    async logoutAll (goLogin = true) {
      // if (this.getToken) {
      //     try {
      //       await doLogoutAll();
      //     } catch {
      //       console.log('注销Token失败');
      //     }
      // }
      const appInfo = window.appInfo;
      window.open(appInfo.sign_app_info.url+'/#/login','_self');
      this.setToken(undefined);
      this.setLoginToken(undefined);
      this.setSessionTimeout(false);
      this.setUserInfo(null);
    },
    /**
     * @description: Confirm before logging out
     */
    confirmLoginOut() {
      const { createConfirm } = useMessage();
      createConfirm({
        iconType: 'warning',
        title: () => h('span', '温馨提醒'),
        content: () => h('span','是否确认退出系统'),
        okText:'确定',
        cancelText:'取消',
        onOk: async () => {
          await this.logout(true);
        },
      });
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}

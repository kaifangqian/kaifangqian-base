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

import type {
  ProjectConfig,
  HeaderSetting,
  MenuSetting,
  TransitionSetting,
  MultiTabsSetting,
} from '/#/config';
import type { BeforeMiniState,SensitiveHeaderState,AppInfo } from '/#/store';

import { defineStore } from 'pinia';
import { store } from '/@/store';

import { ThemeEnum } from '/@/enums/appEnum';
import { APP_DARK_MODE_KEY_, PROJ_CFG_KEY, SENSITIVE_TIME_KEY,APP_LIST_KEY } from '/@/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '/@/utils/auth';
import { Persistent } from '/@/utils/cache/persistent';
import { darkMode } from '/@/settings/designSetting';
import { resetRouter } from '/@/router';
import { deepMerge } from '/@/utils';

interface AppState {
  darkMode?: ThemeEnum;
  // sensitive request header
  sensitiveHeader:{
    sensitivePassword?:string,
    sensitiveEmail?:string,
    sensitiveTelepon?:string,
    sensitiveCaptch?:string,
    sensitiveCaptchKey?:string,
    sensitiveType?:string,
  },
  sensitiveConfig:{
    sensitiveType:string;
    visible:boolean;
  };
  axiosRetryConfig:{
    instance:{
      request:Function
    };
    config:{};
    callback:Function
  },
  msgCount?:string | number,
  // Page loading status
  pageLoading: boolean;

  isTokenExpired: boolean;
  // project config
  projectConfig: ProjectConfig | null;
  // When the window shrinks, remember some states, and restore these states when the window is restored
  beforeMiniInfo: BeforeMiniState;
  apps: AppInfo[]
}
let timeId: TimeoutHandle;
export const useAppStore = defineStore({
  id: 'app',
  state: (): AppState => ({
    darkMode: undefined,
    pageLoading: false,
    msgCount:0,
    sensitiveConfig:{
      sensitiveType:'phone',
      visible:false
    },
    isTokenExpired:false,
    axiosRetryConfig:{
      instance:{
        request:Function
      },
      config:{},
      callback:Function
    },
    apps:Persistent.getLocal(APP_LIST_KEY) || [],
    projectConfig: Persistent.getLocal(PROJ_CFG_KEY),
    beforeMiniInfo: {},
    sensitiveHeader:{} as SensitiveHeaderState
  }),
  getters: {
    getMsgCount(): string | number | undefined{
        return this.msgCount;
    },
    getSensitiveConfig(): { visible:boolean, sensitiveType:string} {
      return this.sensitiveConfig;
    },
    getAxiosRetryConfig(): { config:{},  instance:{}, callback:Function } {
      return this.axiosRetryConfig;
    },
    getPageLoading(): boolean {
      return this.pageLoading;
    },
    getIsTokenExpired(): boolean {
      return this.isTokenExpired;
    },
    getDarkMode(): 'light' | 'dark' | string {
      return this.darkMode || localStorage.getItem(APP_DARK_MODE_KEY_) || darkMode;
    },
    getSensitiveHeader():SensitiveHeaderState{
      return this.sensitiveHeader
    },
    getSensitiveTime():void{
      return  getAuthCache(SENSITIVE_TIME_KEY);
    },

    getBeforeMiniInfo(): BeforeMiniState {
      return this.beforeMiniInfo;
    },

    getProjectConfig(): ProjectConfig {
      return this.projectConfig || ({} as ProjectConfig);
    },

    getHeaderSetting(): HeaderSetting {
      return this.getProjectConfig.headerSetting;
    },
    getMenuSetting(): MenuSetting {
      return this.getProjectConfig.menuSetting;
    },
    getTransitionSetting(): TransitionSetting {
      return this.getProjectConfig.transitionSetting;
    },
    getMultiTabsSetting(): MultiTabsSetting {
      return this.getProjectConfig.multiTabsSetting;
    },
    getApps():AppInfo[]{
      return this.apps;
    }
  },
  actions: {
    setMsgCount (state) :void {
      this.msgCount = state;
    },
    setSensitiveConfig (state: any):void {
      this.sensitiveConfig = state;
    },
    setAxiosRetryConfig (state: any):void {
      this.axiosRetryConfig = {
        ...this.axiosRetryConfig,
        ...state
      };
    },
    clearAxiosRetryConfig ():void {
      this.axiosRetryConfig  = {
        instance:{
          request:Function
        },
        config:{},
        callback:Function
      }
    },
    setApps(apps:AppInfo[]){
      this.apps = apps;
      Persistent.setLocal(APP_LIST_KEY, this.apps);
    },
    setPageLoading(loading: boolean): void {
      this.pageLoading = loading;
    },
    setIsTokenExpired(expired: boolean): void {
      this.isTokenExpired = expired;
    },

    setDarkMode(mode: ThemeEnum): void {
      this.darkMode = mode;
      localStorage.setItem(APP_DARK_MODE_KEY_, mode);
    },

    clearSensitiveHeader():void {
      this.sensitiveHeader = {};
    },
    setSensitiveHeader(state):void{
      setAuthCache(SENSITIVE_TIME_KEY,state);
      this.sensitiveHeader = state;
    },

    setSensitiveTime(){
      setAuthCache(SENSITIVE_TIME_KEY,new Date().getTime())
    },

    setBeforeMiniInfo(state: BeforeMiniState): void {
      this.beforeMiniInfo = state;
    },

    setProjectConfig(config: DeepPartial<ProjectConfig>): void {
      this.projectConfig = deepMerge(this.projectConfig || {}, config);
      Persistent.setLocal(PROJ_CFG_KEY, this.projectConfig);
    },

    async resetAllState() {
      resetRouter();
      Persistent.clearAll();
    },
    async setPageLoadingAction(loading: boolean): Promise<void> {
      if (loading) {
        clearTimeout(timeId);
        // Prevent flicker
        timeId = setTimeout(() => {
          this.setPageLoading(loading);
        }, 50);
      } else {
        this.setPageLoading(loading);
        clearTimeout(timeId);
      }
    },
  },
});

// Need to be used outside the setup
export function useAppStoreWithOut() {
  return useAppStore(store);
}

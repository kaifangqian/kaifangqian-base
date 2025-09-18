import { ErrorTypeEnum } from '/@/enums/exceptionEnum';
import { MenuModeEnum, MenuTypeEnum } from '/@/enums/menuEnum';
import { RoleInfo } from '/@/api/sys/model/userModel';
import type { AppRouteRecordRaw, Menu, SensitiveItem } from '/@/router/types';


// Lock screen information
export interface LockInfo {
  // Password required
  pwd?: string | undefined;
  // Is it locked?
  isLock?: boolean;
}

// Error-log information
export interface ErrorLogInfo {
  // Type of error
  type: ErrorTypeEnum;
  // Error file
  file: string;
  // Error name
  name?: string;
  // Error message
  message: string;
  // Error stack
  stack?: string;
  // Error detail
  detail: string;
  // Error url
  url: string;
  // Error time
  time?: string;
}
export interface TenantInfo {
  departId:string;
  departName:string;
  selectFlag:boolean;
  tenantId:string | null;
  authStatus?:string | number;
}
export interface TenantDepartList {
  tenantId:string;
  tenantName:string;
  selectFlag?:boolean;
  departs:TenantInfo[];
  useFlag:boolean;
}

export interface UserInfo {
  loginDepartId?: any;
  jobAppId?: string;
  id?:string;
  userId?: string | number;
  username?: string;
  realname?: string;
  avatar?: string;
  avatarImg?: string;
  desc?: string;
  homePath?: string;
  roles?: RoleInfo[];
  phone?:string;
  email?:string;
  departNames?:[];
  roleNames?:[];
  createTime?:[];
  passwordEditFlag?:boolean;
  passwordLevel?:string;
  [key: string]: string;
}

export interface BeforeMiniState {
  menuCollapsed?: boolean;
  menuSplit?: boolean;
  menuMode?: MenuModeEnum;
  menuType?: MenuTypeEnum;
}

export interface SafeInfo {
  phone?: string | number;
  username: string | number;
}

export interface PerInfo {
  authList:[],
  menuTree:[]
}

export interface SensitiveHeaderState {
  sensitivePassword?:string;
  sensitiveTelepon?:string;
  sensitiveCaptch?:string;
  sensitiveCaptchKey?:string;
}

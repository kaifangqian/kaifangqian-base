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
// export interface TenantInfo {
//   departId:string;
//   departName:string;
//   selectFlag:boolean;
//   tenantId:string | null;
// }
export interface TenantInfo {
  tenantId?: string ;
  tenantType?: string;
  id?:string;
  tenantName?: string;
  organizationNo?: string;
  corporation?: string;
  corporationNo?: string;
  phone?: string;
  email?: string;
  tenantProvince?: string;
  tenantCity?: string;
  tenantDistrict?:string;
  organizationAddress?:string;
  lifespanType?:string;
  contactsName?:string;
  contactsEmail?:string;
  contactsPhone?:string;
  authStatus?:string|number;
  idPic1:{
    id:string;
    realNmae:string;
  },
  idPic2:{
    id:string;
    realNmae:string;
  },
  organizationPic:{
    id:string;
    realNmae:string;
  },
  [key: string]: string;
}

export interface TenantDepartList {
  tenantId:string;
  tenantName:string;
  selectFlag?:boolean;
  departs:TenantInfo[],
  useFlag?:boolean
  
}

export interface UserInfo {
  loginDepartId?: any;
  loginTenantId?: string | null;
  loginTenantType?: string | number | null;
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
  initUserInfo?:boolean;
  passwordLevel?:string;
  tenantUserId?:string;
  loginTenantName?:string;
  loginDepartName?:string;
  personalTenantFlag?:boolean;
  nickName?:string;
  sysType?:string;
  // [key: string]: string;
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
  email?:string | number;
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

export interface AppInfo {
  appAddress:string;
  appCode:string;
  appName:string;
  appFront:string;
  id:string;
}

export interface Webconfig{
  //网站标题
  websiteTitle:string;
  //网站白色logo
  websiteWhiteLogo:string ;
  //非白色logo
  websiteOtherLogo:string ;
  //Copyright
  websiteCopyright:string ;
  //账号类型 手机号、邮箱、手机号或邮箱
  systenAccountType:string ;
}

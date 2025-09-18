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
 * @Date: 2022-07-12 16:39:01 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-08-19 14:49:07
 */

import { defHttp } from '/@/utils/http/axios';


enum Api {
  SysLogs = '/api/operateLog/query/page',
  SysLogsInfo = '/api/operateLog/query/info',
  SysErrorLog = '/api/errorLog/query/page',
  SysErrorLogInfo = '/api/errorLog/query/info',
  SysWarningLog = '/api/warningLog/query/page',
  SysWarningLogInfo = '/api/warningLog/query/info',

  LogTypeOperation = '/api/logType/getOperateLogTypeList',
  LogTypeWarning = '/api/logType/getWarningLogTypeLit',
  LogTypeWarningLevel = '/api/logType/getWarningLogLevelList'

 


}


/**
 * @description: 系统日志
 */
export function getSyslog(params) {
  return defHttp.get({ url: Api.SysLogs, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 系统日志详情
 */
export function getSyslogInfo(params) {
  return defHttp.get({ url: Api.SysLogsInfo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 异常日志
 */
export function getSysErrorlog(params) {
  return defHttp.get({ url: Api.SysErrorLog, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 异常日志详情
 */
export function getSysErrorlogInfo(params) {
  return defHttp.get({ url: Api.SysErrorLogInfo, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 预警日志
 */
export function getSysWarninglog(params) {
  return defHttp.get({ url: Api.SysWarningLog, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 预警日志
 */
export function getSysWarninglogInfo(params) {
  return defHttp.get({ url: Api.SysWarningLogInfo, params }, { errorMessageMode: 'none' });
}


/**
 * @description: 日志操作类型
 */
export function getSysOpeartionType(params) {
  return defHttp.get({ url: Api.LogTypeOperation, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 日志预警类型
 */
export function getSysWarning(params) {
  return defHttp.get({ url: Api.LogTypeWarning, params }, { errorMessageMode: 'none' });
}
/**
 * @description: 日志级别类型
 */
export function getSysWarningLevel(params) {
  return defHttp.get({ url: Api.LogTypeWarningLevel, params }, { errorMessageMode: 'none' });
}


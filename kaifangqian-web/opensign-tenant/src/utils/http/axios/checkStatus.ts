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

import type { ErrorMessageMode } from '/#/axios';
import { useMessage } from '/@/hooks/web/useMessage';
// import { getToken } from '/@/utils/auth';
// import { Button } from 'ant-design-vue'
// import { h } from 'vue';


// import router from '/@/router';
// import { PageEnum } from '/@/enums/pageEnum';
import { useUserStoreWithOut } from '/@/store/modules/user';
import projectSetting from '/@/settings/projectSetting';
import { SessionTimeoutProcessingEnum } from '/@/enums/appEnum';
// import { useLoginState, LoginStateEnum } from '/@/views/sys/login/useLogin';

const { createMessage, createErrorModal, notification,createWarningModal } = useMessage();

const error = createMessage.error!;
const stp = projectSetting.sessionTimeoutProcessing;
// let noticeTimerId: ReturnType<typeof setInterval> | null;
// let invalidToken = false;

export function checkStatus(
  status: number,
  code: number,
  msg: string,
  errorMessageMode: ErrorMessageMode = 'message',
): void {
  const userStore = useUserStoreWithOut();
  let errMessage = '';

  switch (status) {
    case 400:
      errMessage = `${msg}`;
      break;
    case 401:
      if (code === 402) {
        // 登录token失效
        setTimeout(() => {
          userStore.logoutAll();
        }, 2000);
      } else {
        userStore.setToken(undefined);
        // errMessage = msg || '用户没有权限（令牌、用户名、密码错误）!';
        if (stp === SessionTimeoutProcessingEnum.PAGE_COVERAGE) {
          //直接跳转登出
          userStore.setSessionTimeout(true);
        } else {
          userStore.setKeepLoginAlive(false);
          userStore.setTokenInvalidType(401);
        }
      }
      break;
      // Cannot destructure property 'token' of 'data' as it is null.
    case 402:
      //授权token失效
      userStore.setLoginToken(undefined);
      userStore.setTokenInvalidType(402);   
        break;
    case 403:
      errMessage = '用户得到授权，但是访问是被禁止的!';
      break;
    // 404请求不存在
    case 404:
      errMessage = '网络请求错误,未找到该资源!';
      break;
    case 405:
      errMessage = '网络请求错误,请求方法未允许!';
      break;
    case 408:
      errMessage = '网络请求超时!';
      break;
    case 500:
      errMessage = '服务器错误';
      break;
    case 501:
      errMessage = '网络未实现!';
      break;
    case 502:
      errMessage = '网络错误!';
      break;
    case 503:
      errMessage = '服务不可用，服务器暂时过载或维护!';
      break;
    case 504:
      errMessage = '网络超时!';
      break;
    case 505:
      errMessage = 'http版本不支持该请求!';
      break;
    default:
  }

  if (errMessage) {
    if (errorMessageMode === 'modal') {
      createErrorModal({ title: '错误提示', content: errMessage });
    } else if (errorMessageMode === 'message') {
      error({ content: errMessage, key: `global_error_message_status_${status}` });
    }
  }
}

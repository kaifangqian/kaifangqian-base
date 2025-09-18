/**
 * @description 云盾认证API
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
import Api from '@/api/user';
// import { useUserStore } from '/@/store/modules/user';

export async function handleAuth(tenantType: number, asyncUrl?: string) {
  // const userStore = useUserStore();
  const asyncPage = asyncUrl || window.location.origin + window.location.pathname + '#/index';
  if (tenantType === 1) {
    const { result } = await Api.companyAuthApi({ callbackPage: asyncPage });
    if (result.authStatus === 0) {
      window.open(result.authUrl, '_self');
    } else if (result.authStatus === 1) {
      // await userStore.getTnantInfo();
      window.open(asyncPage, '_self');
    }
  } else {
    const { result } = await Api.personAuthApi({ callbackPage: asyncPage });
    if (result.authStatus === 0) {
      // userStore.clearUserInfo();
      window.open(result.authUrl, '_self');
    } else if (result.authStatus === 1) {
      // await userStore.getTnantInfo();
      window.open(asyncPage, '_self');
    }
  }
}

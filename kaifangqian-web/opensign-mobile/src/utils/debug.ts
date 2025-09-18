/**
 * @description debug 配置文件
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
/* eslint-disable @typescript-eslint/no-explicit-any */
import { storage } from './storage';

// MODE，即env[MODE]文件的环境名称(应用运行的模式)
const { MODE, VITE_BUILD_VCONSOLE } = import.meta.env;

// 传入debug参数，将debug存入/移除localStorage
const config = (debug: any) => {
  if (debug === '1') {
    storage.setItem('debug', debug);
  } else {
    storage.removeItem('debug');
  }
  init();
};

// 初始化 vconsole，控制隐藏/显示
const init = () => {
  const vc = <HTMLElement>document.querySelector('#__vconsole');
  const debug = storage.getItem('debug');
  if (VITE_BUILD_VCONSOLE === 'true' && MODE === 'test' && vc) {
    vc.style.display = debug === '1' ? '' : 'none';
  }
};

export default { init, config };

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

import { ElementObj } from './index.d';

const isServer = typeof window === 'undefined';

export const resizeHandler = (mutationsList: MutationRecord[], observer: any, element: ElementObj) => {
  const listeners = element.resizeListners || [];
  if (listeners.length) {
    listeners.forEach((fn: Function) => fn());
  }
};

export const addResizeListener = (element: ElementObj, fn: any) => {
  if (isServer) return;
  if (!element.resizeListners) {
    element.resizeListners = [];
    window.addEventListener('resize', fn);
    const mutationObserverSupported = typeof MutationObserver !== 'undefined';
    if (mutationObserverSupported) {
      element.observer = new MutationObserver((mutationsList, observer) => resizeHandler(mutationsList, observer, element));
      const config = {
        attributes: true,
        childList: true,
        subtree: true,
        characterData: true,
      };

      element.observer.observe(element as Node, config);
    }
  }
  element.resizeListners.push(fn);
};

export const removeResizeListener = (element: ElementObj, fn: Function) => {
  if (!element || !element.resizeListners) return;
  element.resizeListners.splice(element.resizeListners.indexOf(fn), 1);
  if (!element.resizeListners.length) {
    element.observer.disconnect();
  }
};

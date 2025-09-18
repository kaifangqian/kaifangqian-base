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

import { computed, Ref, ref, unref } from 'vue';

export interface UseFullScreenContext {
  wrapClassName: Ref<string | undefined>;
  modalWrapperRef: Ref<ComponentRef>;
  extHeightRef: Ref<number>;
}

export function useFullScreen(context: UseFullScreenContext) {
  // const formerHeightRef = ref(0);
  const fullScreenRef = ref(false);

  const getWrapClassName = computed(() => {
    const clsName = unref(context.wrapClassName) || '';
    return unref(fullScreenRef) ? `fullscreen-modal ${clsName} ` : unref(clsName);
  });

  function handleFullScreen(e: Event) {
    e && e.stopPropagation();
    fullScreenRef.value = !unref(fullScreenRef);

    // const modalWrapper = unref(context.modalWrapperRef);

    // if (!modalWrapper) return;

    // const wrapperEl = modalWrapper.$el as HTMLElement;
    // if (!wrapperEl) return;
    // const modalWrapSpinEl = wrapperEl.querySelector('.ant-spin-nested-loading') as HTMLElement;

    // if (!modalWrapSpinEl) return;

    // if (!unref(formerHeightRef) && unref(fullScreenRef)) {
    //   formerHeightRef.value = modalWrapSpinEl.offsetHeight;
    // }

    // if (unref(fullScreenRef)) {
    //   modalWrapSpinEl.style.height = `${window.innerHeight - unref(context.extHeightRef)}px`;
    // } else {
    //   modalWrapSpinEl.style.height = `${unref(formerHeightRef)}px`;
    // }
  }
  return { getWrapClassName, handleFullScreen, fullScreenRef };
}

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

import { ComputedRef, isRef, nextTick, Ref, ref, unref, watch } from 'vue';
import { onMountedOrActivated } from '/@/hooks/core/onMountedOrActivated';
import { useWindowSizeFn } from '/@/hooks/event/useWindowSizeFn';
import { useLayoutHeight } from '/@/layouts/default/content/useContentViewHeight';
import { getViewportOffset } from '/@/utils/domUtils';
import { isNumber, isString } from '/@/utils/is';

export interface CompensationHeight {
  // 使用 layout Footer 高度作为判断补偿高度的条件
  useLayoutFooter: boolean;
  // refs HTMLElement
  elements?: Ref[];
}

type Upward = number | string | null | undefined;

/**
 * 动态计算内容高度，根据锚点dom最下坐标到屏幕最下坐标，根据传入dom的高度、padding、margin等值进行动态计算
 * 最终获取合适的内容高度
 *
 * @param flag 用于开启计算的响应式标识
 * @param anchorRef 锚点组件 Ref<ElRef | ComponentRef>
 * @param subtractHeightRefs 待减去高度的组件列表 Ref<ElRef | ComponentRef>
 * @param substractSpaceRefs 待减去空闲空间(margins/paddings)的组件列表 Ref<ElRef | ComponentRef>
 * @param offsetHeightRef 计算偏移的响应式高度，计算高度时将直接减去此值
 * @param upwardSpace 向上递归减去空闲空间的 层级 或 直到指定class为止 数值为2代表向上递归两次|数值为ant-layout表示向上递归直到碰见.ant-layout为止
 * @returns 响应式高度
 */
export function useContentHeight(
  flag: ComputedRef<Boolean>,
  anchorRef: Ref,
  subtractHeightRefs: Ref[],
  substractSpaceRefs: Ref[],
  upwardSpace: Ref<Upward> | ComputedRef<Upward> | Upward = 0,
  offsetHeightRef: Ref<number> = ref(0),
) {
  const contentHeight: Ref<Nullable<number>> = ref(null);
  const { footerHeightRef: layoutFooterHeightRef } = useLayoutHeight();
  let compensationHeight: CompensationHeight = {
    useLayoutFooter: true,
  };

  const setCompensation = (params: CompensationHeight) => {
    compensationHeight = params;
  };

  function redoHeight() {
    nextTick(() => {
      calcContentHeight();
    });
  }

  function calcSubtractSpace(
    element: Element | null | undefined,
    direction: 'all' | 'top' | 'bottom' = 'all',
  ): number {
    function numberPx(px: string) {
      return Number(px.replace(/[^\d]/g, ''));
    }
    let subtractHeight = 0;
    const ZERO_PX = '0px';
    if (element) {
      const cssStyle = getComputedStyle(element);
      const marginTop = numberPx(cssStyle?.marginTop ?? ZERO_PX);
      const marginBottom = numberPx(cssStyle?.marginBottom ?? ZERO_PX);
      const paddingTop = numberPx(cssStyle?.paddingTop ?? ZERO_PX);
      const paddingBottom = numberPx(cssStyle?.paddingBottom ?? ZERO_PX);
      if (direction === 'all') {
        subtractHeight += marginTop;
        subtractHeight += marginBottom;
        subtractHeight += paddingTop;
        subtractHeight += paddingBottom;
      } else if (direction === 'top') {
        subtractHeight += marginTop;
        subtractHeight += paddingTop;
      } else {
        subtractHeight += marginBottom;
        subtractHeight += paddingBottom;
      }
    }
    return subtractHeight;
  }

  function getEl(element: any): Nullable<HTMLDivElement> {
    if (element == null) {
      return null;
    }
    return (element instanceof HTMLDivElement ? element : element.$el) as HTMLDivElement;
  }

  async function calcContentHeight() {
    if (!flag.value) {
      return;
    }
    // Add a delay to get the correct height
    await nextTick();

    const anchorEl = getEl(unref(anchorRef));
    if (!anchorEl) {
      return;
    }
    const { bottomIncludeBody } = getViewportOffset(anchorEl);

    // substract elements height
    let substractHeight = 0;
    subtractHeightRefs.forEach((item) => {
      substractHeight += getEl(unref(item))?.offsetHeight ?? 0;
    });

    // subtract margins / paddings
    let substractSpaceHeight = calcSubtractSpace(anchorEl) ?? 0;
    substractSpaceRefs.forEach((item) => {
      substractSpaceHeight += calcSubtractSpace(getEl(unref(item)));
    });

    // upwardSpace
    let upwardSpaceHeight = 0;
    function upward(element: Element | null, upwardLvlOrClass: number | string | null | undefined) {
      if (element && upwardLvlOrClass) {
        const parent = element.parentElement;
        if (parent) {
          if (isString(upwardLvlOrClass)) {
            if (!parent.classList.contains(upwardLvlOrClass)) {
              upwardSpaceHeight += calcSubtractSpace(parent, 'bottom');
              upward(parent, upwardLvlOrClass);
            } else {
              upwardSpaceHeight += calcSubtractSpace(parent, 'bottom');
            }
          } else if (isNumber(upwardLvlOrClass)) {
            if (upwardLvlOrClass > 0) {
              upwardSpaceHeight += calcSubtractSpace(parent, 'bottom');
              upward(parent, --upwardLvlOrClass);
            }
          }
        }
      }
    }
    if (isRef(upwardSpace)) {
      upward(anchorEl, unref(upwardSpace));
    } else {
      upward(anchorEl, upwardSpace);
    }

    let height =
      bottomIncludeBody -
      unref(layoutFooterHeightRef) -
      unref(offsetHeightRef) -
      substractHeight -
      substractSpaceHeight -
      upwardSpaceHeight;

    // compensation height
    const calcCompensationHeight = () => {
      compensationHeight.elements?.forEach((item) => {
        height += getEl(unref(item))?.offsetHeight ?? 0;
      });
    };
    if (compensationHeight.useLayoutFooter && unref(layoutFooterHeightRef) > 0) {
      calcCompensationHeight();
    } else {
      calcCompensationHeight();
    }

    contentHeight.value = height;
  }

  onMountedOrActivated(() => {
    nextTick(() => {
      calcContentHeight();
    });
  });
  useWindowSizeFn(
    () => {
      calcContentHeight();
    },
    50,
    { immediate: true },
  );
  watch(
    () => [layoutFooterHeightRef.value],
    () => {
      calcContentHeight();
    },
    {
      flush: 'post',
      immediate: true,
    },
  );

  return { redoHeight, setCompensation, contentHeight };
}

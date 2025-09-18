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

import type { ComputedRef, Ref } from 'vue';
import { nextTick, unref } from 'vue';
import { warn } from '/@/utils/log';

export function useTableScrollTo(
  tableElRef: Ref<ComponentRef>,
  getDataSourceRef: ComputedRef<Recordable[]>,
) {
  let bodyEl: HTMLElement | null;

  async function findTargetRowToScroll(targetRowData: Recordable) {
    const { id } = targetRowData;
    const targetRowEl: HTMLElement | null | undefined = bodyEl?.querySelector(
      `[data-row-key="${id}"]`,
    );
    //Add a delay to get new dataSource
    await nextTick();
    bodyEl?.scrollTo({
      top: targetRowEl?.offsetTop ?? 0,
      behavior: 'smooth',
    });
  }

  function scrollTo(pos: string): void {
    const table = unref(tableElRef);
    if (!table) return;

    const tableEl: Element = table.$el;
    if (!tableEl) return;

    if (!bodyEl) {
      bodyEl = tableEl.querySelector('.ant-table-body');
      if (!bodyEl) return;
    }

    const dataSource = unref(getDataSourceRef);
    if (!dataSource) return;

    // judge pos type
    if (pos === 'top') {
      findTargetRowToScroll(dataSource[0]);
    } else if (pos === 'bottom') {
      findTargetRowToScroll(dataSource[dataSource.length - 1]);
    } else {
      const targetRowData = dataSource.find((data) => data.id === pos);
      if (targetRowData) {
        findTargetRowToScroll(targetRowData);
      } else {
        warn(`id: ${pos} doesn't exist`);
      }
    }
  }

  return { scrollTo };
}

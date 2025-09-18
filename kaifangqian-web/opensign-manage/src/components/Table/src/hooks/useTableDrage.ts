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

/**
 * useTableDraggable.ts 表格列拖拽
 * @param dataSource table数据集合
 * @returns customRow 行属性方法
 */

 import type { BasicTableProps } from '../types/table';
 import { ComputedRef,unref } from 'vue';

 export function useDrag<T>(
  propsRef: ComputedRef<BasicTableProps>,
  dataSource: Array<T>,
 ){
  let dragItem;
  let targItem;
  const customDrag = (record: T) => {
    const {showDragColumn } = unref(propsRef);
    return {
      draggable: true,
      ondrag(e: DragEvent) {
        dragItem = record
      },
      ondrop(e: DragEvent) {
        targItem = record
      },
      ondragend(e: DragEvent) {
        if(!showDragColumn) return
        if (dragItem.id !== targItem.id) {
          const dragItemIndex = dataSource.indexOf(dragItem);
          const targItemIndex = dataSource.indexOf(targItem);
          // 解构交换
          [dataSource[dragItemIndex], dataSource[targItemIndex]] = [dataSource[targItemIndex], dataSource[dragItemIndex]]
        }
      },
      ondragover(e: DragEvent) {
        return false
      }
    }
  }
 
  return {
    customDrag
  }
}
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

import { ComputedRef, Ref } from 'vue';
import type { BasicTableProps } from '../types/table';
import { unref } from 'vue';
import { ROW_KEY } from '../const';
import { isString, isFunction } from '/@/utils/is';
import { cloneDeep,findIndex } from 'lodash-es';

interface Options {
  setSelectedRowKeys: (keys: string[]) => void;
  getSelectRowKeys: () => string[];
  clearSelectedRowKeys: () => void;
  emit: EmitType;
  getAutoCreateKey: ComputedRef<boolean | undefined>;
}

function getKey(
  record: Recordable,
  rowKey: string | ((record: Record<string, any>) => string) | undefined,
  autoCreateKey?: boolean,
) {
  if (!rowKey || autoCreateKey) {
    return record[ROW_KEY];
  }
  if (isString(rowKey)) {
    return record[rowKey];
  }
  if (isFunction(rowKey)) {
    return record[rowKey(record)];
  }
  return null;
}

export function useCustomRow(
  propsRef: ComputedRef<BasicTableProps>,
  { setSelectedRowKeys, getSelectRowKeys, getAutoCreateKey, clearSelectedRowKeys, emit}: Options,
  tableData: Ref<Recordable[]>,
  
) {
  let dragItem;
  let targItem;
  const customRow = (record: Recordable, index: number) => {
    const { rowSelection, rowKey, clickToRowSelect, isTriggerSelect = true,showDragColumn,  } = unref(propsRef);
    
    return {
      draggable: false,
      onClick: (e: Event) => {
        e?.stopPropagation();
        function handleClick() {
          if (!rowSelection || !clickToRowSelect) return;
          const keys = getSelectRowKeys();
          const key = getKey(record, rowKey, unref(getAutoCreateKey));
          if (!key) return;

          const isCheckbox = rowSelection.type === 'checkbox';
          if (isCheckbox) {
            // 找到tr
            const tr: HTMLElement = (e as MouseEvent)
              .composedPath?.()
              .find((dom: HTMLElement) => dom.tagName === 'TR') as HTMLElement;
            if (!tr) return;
            // 找到Checkbox，检查是否为disabled
            const checkBox = tr.querySelector('input[type=checkbox]');
            if (!checkBox || checkBox.hasAttribute('disabled')) return;
            if (!keys.includes(key)) {
              setSelectedRowKeys([...keys, key]);
              return;
            }
            const keyIndex = keys.findIndex((item) => item === key);
            keys.splice(keyIndex, 1);
            setSelectedRowKeys(keys);
            return;
          }

          const isRadio = rowSelection.type === 'radio';
          if (isRadio) {
            if (!keys.includes(key)) {
              if (keys.length) {
                clearSelectedRowKeys();
              }
              setSelectedRowKeys([key]);
              return;
            }
            clearSelectedRowKeys();
          }
        }
        isTriggerSelect&&handleClick();
        emit('row-click', record, index, e);
      },
      onDblclick: (event: Event) => {
        emit('row-dbClick', record, index, event);
      },
      onContextmenu: (event: Event) => {
        emit('row-contextmenu', record, index, event);
      },
      onMouseenter: (event: Event) => {
        emit('row-mouseenter', record, index, event);
      },
      onMouseleave: (event: Event) => {
        emit('row-mouseleave', record, index, event);
      },
      ondragstart(event:Event) {
        // console.log(record,'----拖动对象----')
        dragItem = cloneDeep(record)
      },
      ondragover(event){
        event.preventDefault()
      },
      ondrop(event) {
        // console.log(record,'---目标对象record----')
        event.stopPropagation();
        targItem = cloneDeep(record);
      },
      ondragend() {
          if(!showDragColumn) return;
          let list = unref(tableData)
          const dragItemIndex = findIndex(list,(v=>v.id === dragItem.id ));
          const targItemIndex = findIndex(list,(v=>v.id === targItem.id ));
          // console.log(dragItemIndex,targItemIndex);
          if (dragItem && targItem && (dragItemIndex !== targItemIndex)) {
            // 解构交换
            [list[dragItemIndex], list[targItemIndex]] = [list[targItemIndex], list[dragItemIndex]]; 
          }       
      },
      // ondragover() {
      //   return false
      // }
    };
  };

  return {
    customRow,
  };
}

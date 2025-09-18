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

import type { InsertNodeParams, KeyType, FieldNames, TreeItem } from './tree';
import type { Ref, ComputedRef } from 'vue';
import type { TreeDataItem } from 'ant-design-vue/es/tree/Tree';

import { cloneDeep } from 'lodash-es';
import { unref } from 'vue';
import { forEach } from '/@/utils/helper/treeHelper';

export function useTree(treeDataRef: Ref<TreeDataItem[]>, getFieldNames: ComputedRef<FieldNames>) {
  function getAllKeys(list?: TreeDataItem[]) {
    const keys: string[] = [];
    const treeData = list || unref(treeDataRef);
    const { key: keyField, children: childrenField } = unref(getFieldNames);
    if (!childrenField || !keyField) return keys;

    for (let index = 0; index < treeData.length; index++) {
      const node = treeData[index];
      keys.push(node[keyField]!);
      const children = node[childrenField];
      if (children && children.length) {
        keys.push(...(getAllKeys(children) as string[]));
      }
    }
    return keys as KeyType[];
  }

  // get keys that can be checked and selected
  function getEnabledKeys(list?: TreeDataItem[]) {
    const keys: string[] = [];
    const treeData = list || unref(treeDataRef);
    const { key: keyField, children: childrenField } = unref(getFieldNames);
    if (!childrenField || !keyField) return keys;

    for (let index = 0; index < treeData.length; index++) {
      const node = treeData[index];
      node.disabled !== true && node.selectable !== false && keys.push(node[keyField]!);
      const children = node[childrenField];
      if (children && children.length) {
        keys.push(...(getEnabledKeys(children) as string[]));
      }
    }
    return keys as KeyType[];
  }

  function getChildrenKeys(nodeKey: string | number, list?: TreeDataItem[]) {
    const keys: KeyType[] = [];
    const treeData = list || unref(treeDataRef);
    const { key: keyField, children: childrenField } = unref(getFieldNames);
    if (!childrenField || !keyField) return keys;
    for (let index = 0; index < treeData.length; index++) {
      const node = treeData[index];
      const children = node[childrenField];
      if (nodeKey === node[keyField]) {
        keys.push(node[keyField]!);
        if (children && children.length) {
          keys.push(...(getAllKeys(children) as string[]));
        }
      } else {
        if (children && children.length) {
          keys.push(...getChildrenKeys(nodeKey, children));
        }
      }
    }
    return keys as KeyType[];
  }

  // Update node
  function updateNodeByKey(key: string, node: TreeDataItem, list?: TreeDataItem[]) {
    if (!key) return;
    const treeData = list || unref(treeDataRef);
    const { key: keyField, children: childrenField } = unref(getFieldNames);

    if (!childrenField || !keyField) return;

    for (let index = 0; index < treeData.length; index++) {
      const element: any = treeData[index];
      const children = element[childrenField];

      if (element[keyField] === key) {
        treeData[index] = { ...treeData[index], ...node };
        break;
      } else if (children && children.length) {
        updateNodeByKey(key, node, element[childrenField]);
      }
    }
  }

  // Expand the specified level
  function filterByLevel(level = 1, list?: TreeDataItem[], currentLevel = 1) {
    if (!level) {
      return [];
    }
    const res: (string | number)[] = [];
    const data = list || unref(treeDataRef) || [];
    for (let index = 0; index < data.length; index++) {
      const item = data[index];

      const { key: keyField, children: childrenField } = unref(getFieldNames);
      const key = keyField ? item[keyField] : '';
      const children = childrenField ? item[childrenField] : [];
      res.push(key);
      if (children && children.length && currentLevel < level) {
        currentLevel += 1;
        res.push(...filterByLevel(level, children, currentLevel));
      }
    }
    return res as string[] | number[];
  }

  /**
   * 添加节点
   */
  function insertNodeByKey({ parentKey = null, node, push = 'push' }: InsertNodeParams) {
    const treeData: any = cloneDeep(unref(treeDataRef));
    if (!parentKey) {
      treeData[push](node);
      treeDataRef.value = treeData;
      return;
    }
    const { key: keyField, children: childrenField } = unref(getFieldNames);
    if (!childrenField || !keyField) return;

    forEach(treeData, (treeItem) => {
      if (treeItem[keyField] === parentKey) {
        treeItem[childrenField] = treeItem[childrenField] || [];
        treeItem[childrenField][push](node);
        return true;
      }
    });
    treeDataRef.value = treeData;
  }
  /**
   * 批量添加节点
   */
  function insertNodesByKey({ parentKey = null, list, push = 'push' }: InsertNodeParams) {
    const treeData: any = cloneDeep(unref(treeDataRef));
    if (!list || list.length < 1) {
      return;
    }
    if (!parentKey) {
      for (let i = 0; i < list.length; i++) {
        treeData[push](list[i]);
      }
    } else {
      const { key: keyField, children: childrenField } = unref(getFieldNames);
      if (!childrenField || !keyField) return;

      forEach(treeData, (treeItem) => {
        if (treeItem[keyField] === parentKey) {
          treeItem[childrenField] = treeItem[childrenField] || [];
          for (let i = 0; i < list.length; i++) {
            treeItem[childrenField][push](list[i]);
          }
          treeDataRef.value = treeData;
          return true;
        }
      });
    }
  }
  // Delete node
  function deleteNodeByKey(key: string, list?: TreeDataItem[]) {
    if (!key) return;
    const treeData = list || unref(treeDataRef);
    const { key: keyField, children: childrenField } = unref(getFieldNames);
    if (!childrenField || !keyField) return;

    for (let index = 0; index < treeData.length; index++) {
      const element: any = treeData[index];
      const children = element[childrenField];

      if (element[keyField] === key) {
        treeData.splice(index, 1);
        break;
      } else if (children && children.length) {
        deleteNodeByKey(key, element[childrenField]);
      }
    }
  }

  // Get selected node
  function getSelectedNode(key: KeyType, list?: TreeItem[], selectedNode?: TreeItem | null) {
    if (!key && key !== 0) return null;
    const treeData = list || unref(treeDataRef);
    treeData.forEach((item) => {
      if (selectedNode?.key || selectedNode?.key === 0) return selectedNode;
      if (item.key === key) {
        selectedNode = item;
        return;
      }
      if (item.children && item.children.length) {
        selectedNode = getSelectedNode(key, item.children, selectedNode);
      }
    });
    return selectedNode || null;
  }
  return {
    deleteNodeByKey,
    insertNodeByKey,
    insertNodesByKey,
    filterByLevel,
    updateNodeByKey,
    getAllKeys,
    getChildrenKeys,
    getEnabledKeys,
    getSelectedNode,
  };
}

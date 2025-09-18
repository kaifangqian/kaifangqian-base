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

import { computed, ComponentInternalInstance, unref } from 'vue';
import type { CSSProperties } from 'vue';

export function useMenuItem(instance: ComponentInternalInstance | null) {
  const getParentMenu = computed(() => {
    return findParentMenu(['Menu', 'SubMenu']);
  });

  const getParentRootMenu = computed(() => {
    return findParentMenu(['Menu']);
  });

  const getParentSubMenu = computed(() => {
    return findParentMenu(['SubMenu']);
  });

  const getItemStyle = computed((): CSSProperties => {
    let parent = instance?.parent;
    if (!parent) return {};
    const indentSize = (unref(getParentRootMenu)?.props.indentSize as number) ?? 20;
    let padding = indentSize;

    if (unref(getParentRootMenu)?.props.collapse) {
      padding = indentSize;
    } else {
      while (parent && parent.type.name !== 'Menu') {
        if (parent.type.name === 'SubMenu') {
          padding += indentSize;
        }
        parent = parent.parent;
      }
    }
    return { paddingLeft: padding + 'px' };
  });

  function findParentMenu(name: string[]) {
    let parent = instance?.parent;
    if (!parent) return null;
    while (parent && name.indexOf(parent.type.name!) === -1) {
      parent = parent.parent;
    }
    return parent;
  }

  function getParentList() {
    let parent = instance;
    if (!parent)
      return {
        uidList: [],
        list: [],
      };
    const ret: any[] = [];
    while (parent && parent.type.name !== 'Menu') {
      if (parent.type.name === 'SubMenu') {
        ret.push(parent);
      }
      parent = parent.parent;
    }
    return {
      uidList: ret.map((item) => item.uid),
      list: ret,
    };
  }

  function getParentInstance(instance: ComponentInternalInstance, name = 'SubMenu') {
    let parent = instance.parent;
    while (parent) {
      if (parent.type.name !== name) {
        return parent;
      }
      parent = parent.parent;
    }
    return parent;
  }

  return {
    getParentMenu,
    getParentInstance,
    getParentRootMenu,
    getParentList,
    getParentSubMenu,
    getItemStyle,
  };
}

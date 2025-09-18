<!--
  @description 开放签

  Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
  必须公开修改后的完整源码（包括衍生作品），详见协议全文。
-->

<template>
  <li :class="getClass">
    <template v-if="!getCollapse">
      <div :class="`${prefixCls}-submenu-title`" @click.stop="handleClick" :style="getItemStyle">
        <slot name="title"></slot>
        <Icon
          icon="eva:arrow-ios-downward-outline"
          :size="14"
          :class="`${prefixCls}-submenu-title-icon`"
        />
      </div>
      <transition>
        <ul :class="prefixCls" v-show="opened">
          <slot></slot>
        </ul>
      </transition>
    </template>

    <Popover
      placement="right"
      :overlayClassName="`${prefixCls}-menu-popover`"
      v-else
      :visible="getIsOpend"
      @visible-change="handleVisibleChange"
      :overlayStyle="getOverlayStyle"
      :align="{ offset: [0, 0] }"
    >
      <div :class="getSubClass" v-bind="getEvents(false)">
        <div
          :class="[
            {
              [`${prefixCls}-submenu-popup`]: !getParentSubMenu,
              [`${prefixCls}-submenu-collapsed-show-tit`]: collapsedShowTitle,
            },
          ]"
        >
          <slot name="title"></slot>
        </div>
        <Icon
          v-if="getParentSubMenu"
          icon="eva:arrow-ios-downward-outline"
          :size="14"
          :class="`${prefixCls}-submenu-title-icon`"
        />
      </div>
      <!-- eslint-disable-next-line -->
      <template #content v-show="opened">
        <div v-bind="getEvents(true)">
          <ul :class="[prefixCls, `${prefixCls}-${getTheme}`, `${prefixCls}-popup`]">
            <slot></slot>
          </ul>
        </div>
      </template>
    </Popover>
  </li>
</template>

<script lang="ts">
  import type { CSSProperties, PropType } from 'vue';
  import type { SubMenuProvider } from './types';
  import {
    defineComponent,
    computed,
    unref,
    getCurrentInstance,
    toRefs,
    reactive,
    provide,
    onBeforeMount,
    inject,
  } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { propTypes } from '/@/utils/propTypes';
  import { useMenuItem } from './useMenu';
  import { useSimpleRootMenuContext } from './useSimpleMenuContext';
  import Icon from '/@/components/Icon';
  import { Popover } from 'ant-design-vue';
  import { isBoolean, isObject } from '/@/utils/is';
  import mitt from '/@/utils/mitt';

  const DELAY = 200;
  export default defineComponent({
    name: 'SubMenu',
    components: {
      Icon,
      Popover,
    },
    props: {
      name: {
        type: [String, Number] as PropType<string | number>,
        required: true,
      },
      disabled: propTypes.bool,
      collapsedShowTitle: propTypes.bool,
    },
    setup(props) {
      const instance = getCurrentInstance();

      const state = reactive({
        active: false,
        opened: false,
      });

      const data = reactive({
        timeout: null as TimeoutHandle | null,
        mouseInChild: false,
        isChild: false,
      });

      const { getParentSubMenu, getItemStyle, getParentMenu, getParentList } =
        useMenuItem(instance);

      const { prefixCls } = useDesign('menu');

      const subMenuEmitter = mitt();

      const { rootMenuEmitter } = useSimpleRootMenuContext();

      const {
        addSubMenu: parentAddSubmenu,
        removeSubMenu: parentRemoveSubmenu,
        removeAll: parentRemoveAll,
        getOpenNames: parentGetOpenNames,
        isRemoveAllPopup,
        sliceIndex,
        level,
        props: rootProps,
        handleMouseleave: parentHandleMouseleave,
      } = inject<SubMenuProvider>(`subMenu:${getParentMenu.value?.uid}`)!;

      const getClass = computed(() => {
        return [
          `${prefixCls}-submenu`,
          {
            [`${prefixCls}-item-active`]: state.active,
            [`${prefixCls}-opened`]: state.opened,
            [`${prefixCls}-submenu-disabled`]: props.disabled,
            [`${prefixCls}-submenu-has-parent-submenu`]: unref(getParentSubMenu),
            [`${prefixCls}-child-item-active`]: state.active,
          },
        ];
      });

      const getAccordion = computed(() => rootProps.accordion);
      const getCollapse = computed(() => rootProps.collapse);
      const getTheme = computed(() => rootProps.theme);

      const getOverlayStyle = computed((): CSSProperties => {
        return {
          minWidth: '200px',
        };
      });

      const getIsOpend = computed(() => {
        const name = props.name;
        if (unref(getCollapse)) {
          return parentGetOpenNames().includes(name);
        }
        return state.opened;
      });

      const getSubClass = computed(() => {
        const isActive = rootProps.activeSubMenuNames.includes(props.name);
        return [
          `${prefixCls}-submenu-title`,
          {
            [`${prefixCls}-submenu-active`]: isActive,
            [`${prefixCls}-submenu-active-border`]: isActive && level === 0,
            [`${prefixCls}-submenu-collapse`]: unref(getCollapse) && level === 0,
          },
        ];
      });

      function getEvents(deep: boolean) {
        if (!unref(getCollapse)) {
          return {};
        }
        return {
          onMouseenter: handleMouseenter,
          onMouseleave: () => handleMouseleave(deep),
        };
      }

      function handleClick() {
        const { disabled } = props;
        if (disabled || unref(getCollapse)) return;
        const opened = state.opened;

        if (unref(getAccordion)) {
          const { uidList } = getParentList();
          rootMenuEmitter.emit('on-update-opened', {
            opend: false,
            parent: instance?.parent,
            uidList: uidList,
          });
        } else {
          rootMenuEmitter.emit('open-name-change', {
            name: props.name,
            opened: !opened,
          });
        }
        state.opened = !opened;
      }

      function handleMouseenter() {
        const disabled = props.disabled;
        if (disabled) return;

        subMenuEmitter.emit('submenu:mouse-enter-child');

        const index = parentGetOpenNames().findIndex((item) => item === props.name);

        sliceIndex(index);

        const isRoot = level === 0 && parentGetOpenNames().length === 2;
        if (isRoot) {
          parentRemoveAll();
        }
        data.isChild = parentGetOpenNames().includes(props.name);
        clearTimeout(data.timeout!);
        data.timeout = setTimeout(() => {
          parentAddSubmenu(props.name);
        }, DELAY);
      }

      function handleMouseleave(deepDispatch = false) {
        const parentName = getParentMenu.value?.props.name;
        if (!parentName) {
          isRemoveAllPopup.value = true;
        }

        if (parentGetOpenNames().slice(-1)[0] === props.name) {
          data.isChild = false;
        }

        subMenuEmitter.emit('submenu:mouse-leave-child');
        if (data.timeout) {
          clearTimeout(data.timeout!);
          data.timeout = setTimeout(() => {
            if (isRemoveAllPopup.value) {
              parentRemoveAll();
            } else if (!data.mouseInChild) {
              parentRemoveSubmenu(props.name);
            }
          }, DELAY);
        }
        if (deepDispatch) {
          if (getParentSubMenu.value) {
            parentHandleMouseleave?.(true);
          }
        }
      }

      onBeforeMount(() => {
        subMenuEmitter.on('submenu:mouse-enter-child', () => {
          data.mouseInChild = true;
          isRemoveAllPopup.value = false;
          clearTimeout(data.timeout!);
        });
        subMenuEmitter.on('submenu:mouse-leave-child', () => {
          if (data.isChild) return;
          data.mouseInChild = false;
          clearTimeout(data.timeout!);
        });

        rootMenuEmitter.on(
          'on-update-opened',
          (data: boolean | (string | number)[] | Recordable) => {
            if (unref(getCollapse)) return;
            if (isBoolean(data)) {
              state.opened = data;
              return;
            }
            if (isObject(data) && rootProps.accordion) {
              const { opend, parent, uidList } = data as Recordable;
              if (parent === instance?.parent) {
                state.opened = opend;
              } else if (!uidList.includes(instance?.uid)) {
                state.opened = false;
              }
              return;
            }

            if (props.name && Array.isArray(data)) {
              state.opened = (data as (string | number)[]).includes(props.name);
            }
          },
        );

        rootMenuEmitter.on('on-update-active-name:submenu', (data: number[]) => {
          if (instance?.uid) {
            state.active = data.includes(instance?.uid);
          }
        });
      });

      function handleVisibleChange(visible: boolean) {
        state.opened = visible;
      }

      // provide
      provide<SubMenuProvider>(`subMenu:${instance?.uid}`, {
        addSubMenu: parentAddSubmenu,
        removeSubMenu: parentRemoveSubmenu,
        getOpenNames: parentGetOpenNames,
        removeAll: parentRemoveAll,
        isRemoveAllPopup,
        sliceIndex,
        level: level + 1,
        handleMouseleave,
        props: rootProps,
      });

      return {
        getClass,
        prefixCls,
        getCollapse,
        getItemStyle,
        handleClick,
        handleVisibleChange,
        getParentSubMenu,
        getOverlayStyle,
        getTheme,
        getIsOpend,
        getEvents,
        getSubClass,
        ...toRefs(state),
        ...toRefs(data),
      };
    },
  });
</script>

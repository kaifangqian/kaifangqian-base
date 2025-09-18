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
    <div :class="bem()" class="flex px-2 py-1.5 items-center">
      <slot name="headerTitle" v-if="slots.headerTitle"></slot>
      <span>{{title}}</span>
      <div class="flex items-center flex-1 cursor-pointer justify-self-stretch"  v-if="search || toolbar" >
          <div :class="getInputSearchCls" v-if="search">
            <InputSearch
              placeholder="搜索"
              allowClear
              v-model:value="searchValue"
            />
          </div>

          <Dropdown @click.prevent v-if="toolbar">
            <!-- <Icon icon="ion:ellipsis-vertical" /> -->
            <template #overlay>
              <Menu @click="handleMenuClick">
                <template v-for="item in toolbarList" :key="item.value">
                  <MenuItem v-bind="{ key: item.value }">
                    {{ item.label }}
                  </MenuItem>
                  <MenuDivider v-if="item.divider" />
                </template>
              </Menu>
            </template>
          </Dropdown>
        </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, ref, watch, useSlots } from 'vue';
  import { Dropdown, Menu, MenuItem, MenuDivider, InputSearch } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import { useDebounceFn } from '@vueuse/core';
  import { createBEM } from '/@/utils/bem';
  import { ToolbarEnum } from './tree';

  const searchValue = ref('');

  const [bem] = createBEM('tree-header');

  const props = defineProps({
    helpMessage: {
      type: [String, Array] as PropType<string | string[]>,
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
    toolbar: {
      type: Boolean,
      default: false,
    },
    size: {
      type: String,
      default: 'small',
    },
    checkable: {
      type: Boolean,
      default: false,
    },
    checkStrictlySwitch: {
      type: Boolean,
      default: false,
    },
    search: {
      type: Boolean,
      default: false,
    },
    searchText: {
      type: String,
      default: '',
    },
    refresh: {
      type: Function,
      default: undefined,
    },
    checkAll: {
      type: Function,
      default: undefined,
    },
    expandAll: {
      type: Function,
      default: undefined,
    },
  } as const);
  const emit = defineEmits(['strictly-change', 'search']);

  const slots = useSlots();

  const getInputSearchCls = computed(() => {
    const titleExists = slots.headerTitle || props.title;
    return [
      'mr-1',
      'w-full',
      {
        ['ml-5']: titleExists,
      },
    ];
  });

  const toolbarList = computed(() => {
    const { checkable,checkStrictlySwitch } = props;
    const defaultToolbarList = [
      { label:'刷新', value: ToolbarEnum.REFRESH },
      // { label: '展开全部', value: ToolbarEnum.EXPAND_ALL },
      // {
      //   label: '折叠全部',
      //   value: ToolbarEnum.UN_EXPAND_ALL,
      //   divider: checkable,
      // },
    ];

    return checkable
      ? (checkStrictlySwitch? [
          { label: '选择全部', value: ToolbarEnum.SELECT_ALL },
          {
            label: '取消选择',
            value: ToolbarEnum.UN_SELECT_ALL,
            divider: checkable,
          },
          ...defaultToolbarList,
          { label: '层级关联', value: ToolbarEnum.CHECK_STRICTLY },
          { label: '层级独立', value: ToolbarEnum.CHECK_UN_STRICTLY },
        ] : [
          // { label: '层级关联', value: ToolbarEnum.CHECK_STRICTLY },
          // { label: '层级独立', value: ToolbarEnum.CHECK_UN_STRICTLY },
          { label: '选择全部', value: ToolbarEnum.SELECT_ALL },
          {
            label: '取消选择',
            value: ToolbarEnum.UN_SELECT_ALL,
            divider: checkable,
          },
          ...defaultToolbarList,
        ])
      : defaultToolbarList;
  });

  function handleMenuClick(e: { key: ToolbarEnum }) {
    const { key } = e;
    switch (key) {
      case ToolbarEnum.REFRESH:
        props.refresh();
      case ToolbarEnum.SELECT_ALL:
        props.checkAll?.(true);
        break;
      case ToolbarEnum.UN_SELECT_ALL:
        props.checkAll?.(false);
        break;
      case ToolbarEnum.EXPAND_ALL:
        props.expandAll?.(true);
        break;
      case ToolbarEnum.UN_EXPAND_ALL:
        props.expandAll?.(false);
        break;
      case ToolbarEnum.CHECK_STRICTLY:
        emit('strictly-change', false);
        break;
      case ToolbarEnum.CHECK_UN_STRICTLY:
        emit('strictly-change', true);
        break;
    }
  }

  function emitChange(value?: string): void {
    emit('search', value);
  }

  const debounceEmitChange = useDebounceFn(emitChange, 200);

  watch(
    () => searchValue.value,
    (v) => {
      debounceEmitChange(v);
    },
  );

  watch(
    () => props.searchText,
    (v) => {
      if (v !== searchValue.value) {
        searchValue.value = v;
      }
    },
  );
</script>

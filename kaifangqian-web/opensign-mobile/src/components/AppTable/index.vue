<!--
  @description apptable

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
  <div class="table-wrap">
    <table>
      <thead>
        <tr>
          <th v-if="props.expandable || props.checkable"></th>
          <th v-for="col in props.columns" :key="col.key" :class="{ break: col.break }">{{
            col.title
          }}</th>
        </tr>
      </thead>
      <tbody>
        <template v-for="(row, rowIndex) in state.tableData" :key="rowIndex">
          <tr v-if="!row._hide" :class="{ active: row._active }" @click="onRowActive(rowIndex)">
            <td v-if="props.expandable || props.checkable" class="action">
              <van-icon
                v-if="'_expand' in row"
                :name="row._expand ? 'arrow-down' : 'arrow'"
                @click="onExpand(row)"
              />
              <van-icon
                v-if="'_checked' in row"
                :name="row._checked ? 'checked' : 'circle'"
                @click="onChecked(row)"
              />
            </td>
            <td v-for="col in props.columns" :key="col.key" :class="{ break: col.break }">
              <template v-if="'render' in col">
                <Render :row="row" :column="col" :index="rowIndex" :render="col.render"></Render>
              </template>
              <template v-else-if="'slot' in col">
                <slot :row="row" :column="col" :index="rowIndex" :name="col.slot"></slot>
              </template>
              <template v-else>{{ row[col.key] }}</template>
            </td>
          </tr>
        </template>
      </tbody>
    </table>
    <div v-if="!props.data.length" class="no-data">
      <van-empty image-size="2rem" description="No Data" />
    </div>
  </div>
  <van-pagination
    v-if="props.total / props.page.pageSize > 1"
    v-model="state.pageNum"
    :total-items="props.total"
    :items-per-page="props.page.pageSize"
    @change="onChangePage"
  />
</template>

<script setup lang="ts">
  import { PropType } from 'vue';
  import Render from './render';
  import { TableColumnsItem, TableRowItem, Page } from './types';
  import { cloneDeep } from 'lodash-es';

  const props = defineProps({
    // 同时兼容render和slot两种写法，render优先
    columns: {
      type: Array as PropType<TableColumnsItem[]>,
      default: () => [],
      required: true,
    },
    data: {
      type: Array as PropType<TableRowItem[]>,
      default: () => [],
      required: true,
    },
    page: {
      type: Object as PropType<Page>,
      default: () => ({}),
    },
    total: {
      type: Number,
      default: 0,
    },
    expandable: {
      type: Boolean,
      default: false,
    },
    checkable: {
      type: Boolean,
      default: false,
    },
    multiCheck: {
      type: Boolean,
      default: false,
    },
  });

  const state = reactive({
    pageNum: 1,
    tableData: <TableRowItem[]>[],
  });

  const emit = defineEmits(['on-expand', 'on-checked', 'change-page']);

  watchEffect(() => {
    state.pageNum = props.page.pageNum;
  });

  watch(
    () => props.data,
    (data) => {
      const result: TableRowItem[] = [];
      data.forEach((row, index) => {
        // _expand控制行展开，_checked控制行勾选，_hide控制行隐藏
        // 这里默认父行为展开操作，子行为勾选操作，隐藏子行，可扩展传默认值
        row._index = index;
        !('_expand' in row) && (row._expand = false);
        if (row.children) {
          row.children.forEach((child: TableRowItem, childIndex: number) => {
            if ('_hide' in child && row._expand) {
              child._hide = false;
            } else {
              child._hide = true;
            }
            !('_checked' in child) && (child._checked = false);
            child._index = childIndex;
            child._parentIndex = index;
          });
          result.push(...[row, ...row.children]);
        } else {
          result.push(row);
        }
      });
      state.tableData = result;
    },
    { deep: true, immediate: true }
  );

  const onExpand = (row: TableRowItem) => {
    row._expand = !row._expand;
    emit('on-expand', cloneDeep(row));
  };
  const onChecked = (row: TableRowItem) => {
    if (!props.multiCheck) {
      if (row._checked) {
        row._checked = false;
      } else {
        for (const item of state.tableData) {
          '_checked' in item && (item._checked = false);
        }
        row._checked = true;
      }
    } else {
      // 可扩展多选
    }
    emit('on-checked', cloneDeep(row));
  };
  const onRowActive = (index: number) => {
    state.tableData.forEach((row, i) => (row._active = index === i));
  };
  const onChangePage = (pageNum: number) => {
    emit('change-page', pageNum);
  };
</script>

<style lang="less" scoped>
  .table-wrap {
    max-height: calc(100% - 80px);
    border: @border;
    white-space: nowrap;
    overflow: auto;
    table {
      width: 100%;
      border-collapse: collapse;
      border-spacing: 0;
      empty-cells: show;
      thead tr {
        height: 80px;
        border-bottom: @border;
        z-index: 2;
        th {
          background-color: #fafafa;
          font-weight: 600;
          white-space: nowrap;
          // sticky固定
          position: sticky;
          top: 0;
          z-index: 2;
          &:not(:last-child) {
            border-right: @border;
          }
        }
      }
      tbody tr {
        height: 100px;
        // &:not(:last-child) {
        //   border-bottom: @border;
        // }
        &.active {
          background-color: #eee;
        }
        td {
          &:not(:last-child) {
            border-right: @border;
          }
        }
      }

      th,
      td {
        padding: 8px 16px;
        text-align: left;
        &.action {
          font-size: 32px;
        }
        :deep(button) {
          height: 58px;
          padding: 0 32px;
          border-radius: 6px;
          vertical-align: middle;
        }
        &.break {
          min-width: 450px;
          white-space: break-spaces;
        }
      }
    }
    .no-data {
      text-align: center;
      .van-empty {
        padding: 0;
      }
    }
  }
</style>

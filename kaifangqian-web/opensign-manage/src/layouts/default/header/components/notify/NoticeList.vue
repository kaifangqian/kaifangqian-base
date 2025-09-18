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
<div>
   <a-list :class="prefixCls"  :pagination="getPagination">
    <template v-for="(item,index) in getData" :key="index">
      <a-list-item class="list-item">
        <a-list-item-meta @click="handleChangeRouter(item)">
          <template #title>
            <div class="title">
              <a-typography-paragraph
                @click="handleTitleClick(item)"
                style="width: 100%; margin-bottom: 0 !important"
                :style="{ cursor: isTitleClickable ? 'pointer' : '' }"
                :delete="false"
                :ellipsis="
                  $props.titleRows && $props.titleRows > 0
                    ? { rows: $props.titleRows, tooltip: !!item.title }
                    : false
                "
                :content="item.title"
              />
              <div class="extra" v-if="item.extra">
                <a-tag class="tag" :color="item.color">
                  {{ item.extra }}
                </a-tag>
              </div>
              <span v-if="!item.isRead" class="sign-status" @click.stop="handleRead(item)">标为已读</span>
            </div>
          </template>

          <template #avatar>
            <a-badge status="processing" v-if="item.isRead==0"/>
            <a-badge status="default" v-if="item.isRead==1"/>
          </template>

          <template #description>
            <div>
              <!-- <div class="description" v-if="item.description">
                <a-typography-paragraph
                  style="width: 100%; margin-bottom: 0 !important"
                  :ellipsis="
                    $props.descRows && $props.descRows > 0
                      ? { rows: $props.descRows, tooltip: !!item.description }
                      : false
                  "
                  :content="item.description"
                />
              </div> -->
              <div class="custom-description">
                <div class="notify-tag">
                   <a-tag color="pink">{{item.typeName}}</a-tag>
                </div>
                <div class="datetime">
                  {{ item.createTime }}
                </div>
              </div>
              
            </div>
          </template>
        </a-list-item-meta>
      </a-list-item>
    </template>
  </a-list>
</div>
 
</template>
<script lang="ts">
  import { computed, defineComponent, PropType, ref, watch, unref } from 'vue';
  import { ListItem } from './data';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { List, Avatar, Tag, Typography } from 'ant-design-vue';
  import { isNumber } from '/@/utils/is';
  export default defineComponent({
    components: {
      [Avatar.name]: Avatar,
      [List.name]: List,
      [List.Item.name]: List.Item,
      AListItemMeta: List.Item.Meta,
      ATypographyParagraph: Typography.Paragraph,
      [Tag.name]: Tag,
    },
    props: {
      list: {
        type: Array as PropType<ListItem[]>,
        default: () => [],
      },
      pageSize: {
        type: [Boolean, Number] as PropType<Boolean | Number>,
        default: 5,
      },
      currentPage: {
        type: Number,
        default: 1,
      },
      titleRows: {
        type: Number,
        default: 1,
      },
      descRows: {
        type: Number,
        default: 2,
      },
      onTitleClick: {
        type: Function as PropType<(Recordable) => void>,
      },
    },
    emits: ['update:currentPage','read','router'],
    setup(props, { emit }) {
      const { prefixCls } = useDesign('header-notify-list');
      const current = ref(props.currentPage || 1);
      const getData = computed(() => {
        const { pageSize, list } = props;
        if (pageSize === false) return [];
        let size = isNumber(pageSize) ? pageSize : 10;
        return list.slice(size * (unref(current) - 1), size * unref(current));
        // return list;
      });
      watch(
        () => props.currentPage,
        (v) => {
          current.value = v;
        },
      );
      const isTitleClickable = computed(() => !!props.onTitleClick);
      const getPagination = computed(() => {
        const { list, pageSize } = props;
        if (pageSize > 0 && list && list.length > pageSize) {
          return {
            total: list.length,
            pageSize,
            //size: 'small',
            current: unref(current),
            onChange(page) {
              current.value = page;
              emit('update:currentPage', page);
            },
          };
        } else {
          return false;
        }
      });

      function handleTitleClick(item: ListItem) {
        props.onTitleClick && props.onTitleClick(item);
      }
      function handleRead(record){
        emit('read', record);
      }
      function handleChangeRouter(record){
        emit('router', record);
      }

      return { handleChangeRouter, prefixCls, getPagination, getData, handleTitleClick, isTitleClickable, handleRead };
    },
  });
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-header-notify-list';

  .@{prefix-cls} {
    &::-webkit-scrollbar {
      display: none;
    }
   
      .list-item{
        cursor: pointer;
        &:hover  .sign-status{
            display: block;
        }
      }
      // .list-item:hover{
      //   background:#e7eaef;
      // }
     .custom-description{
        display: flex;
        justify-content: space-between;
      }

    ::v-deep(.ant-pagination-disabled) {
      display: inline-block !important;
    }
   
    .title{
      display: flex;
      .sign-status{
        color:#0960bd;
        cursor: pointer;
        flex-basis: 67px;
        display: none;
      }
    }


    &-item {
      padding: 6px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s;
       

      .title {
        margin-bottom: 8px;
        font-weight: normal;
      
        .extra {
          float: right;
          margin-top: -1.5px;
          margin-right: 0;
          font-weight: normal;

          .tag {
            margin-right: 0;
          }
        }
        

        .avatar {
          margin-top: 4px;
        }

        .description {
          font-size: 12px;
          line-height: 18px;
        }

        .datetime {
          margin-top: 4px;
          font-size: 12px;
          line-height: 18px;
        }
      }
    }
  }
</style>

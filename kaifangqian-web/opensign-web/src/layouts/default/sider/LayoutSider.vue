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
    <div
    class="slide-custom"
      v-if="getMenuFixed && !getIsMobile"
      :style="getHiddenDomStyle"
      v-show="showClassSideBarRef"
    ></div>
    <Sider
      v-show="showClassSideBarRef"
      ref="sideRef"
      breakpoint="lg"
      collapsible
      :class="getSiderClass"
      :width="getMenuWidth"
      :collapsed="getCollapsed"
      :collapsedWidth="getCollapsedWidth"
      :theme="getMenuTheme"
      @breakpoint="onBreakpointChange"
      :trigger="getTrigger"
      v-bind="getTriggerAttr"
    >
      <template #trigger v-if="getShowTrigger">
        <LayoutTrigger />
      </template>
      <LayoutMenu :theme="getMenuTheme" :menuMode="getMode" :splitType="getSplitType" />
      <DragBar ref="dragBarRef" />
    </Sider>
</div>
</template>
<script lang="ts">
  import { computed, defineComponent, ref, unref, CSSProperties, h } from 'vue';

  import { Layout } from 'ant-design-vue';
  import LayoutMenu from '../menu/index.vue';
  import LayoutTrigger from '/@/layouts/default/trigger/index.vue';

  import { MenuModeEnum, MenuSplitTyeEnum } from '/@/enums/menuEnum';

  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useTrigger, useDragLine, useSiderEvent } from './useLayoutSider';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { useDesign } from '/@/hooks/web/useDesign';

  import DragBar from './DragBar.vue';
  export default defineComponent({
    name: 'LayoutSideBar',
    components: { Sider: Layout.Sider, LayoutMenu, DragBar, LayoutTrigger },
    setup() {
      const dragBarRef = ref<ElRef>(null);
      const sideRef = ref<ElRef>(null);

      const {
        getCollapsed,
        getMenuWidth,
        getSplit,
        getMenuTheme,
        getRealWidth,
        getMenuHidden,
        getMenuFixed,
        getIsMixMode,
        toggleCollapsed,
      } = useMenuSetting();

      const { prefixCls } = useDesign('layout-sideBar');

      const { getIsMobile } = useAppInject();

      const { getTriggerAttr, getShowTrigger } = useTrigger(getIsMobile);

      useDragLine(sideRef, dragBarRef);

      const { getCollapsedWidth, onBreakpointChange } = useSiderEvent();

      const getMode = computed(() => {
        return unref(getSplit) ? MenuModeEnum.INLINE : null;
      });

      const getSplitType = computed(() => {
        return unref(getSplit) ? MenuSplitTyeEnum.LEFT : MenuSplitTyeEnum.NONE;
      });

      const showClassSideBarRef = computed(() => {
        return unref(getSplit) ? !unref(getMenuHidden) : true;
      });

      const getSiderClass = computed(() => {
        return [
          prefixCls,
          {
            [`${prefixCls}--fixed`]: unref(getMenuFixed),
            [`${prefixCls}--mix`]: unref(getIsMixMode) && !unref(getIsMobile),
          },
        ];
      });

      const getHiddenDomStyle = computed((): CSSProperties => {
        const width = `${unref(getRealWidth)}px`;
        return {
          width: width,
          overflow: 'hidden',
          flex: `0 0 ${width}`,
          maxWidth: width,
          minWidth: width,
          transition: 'all 0.2s',
        };
      });

      // 在此处使用计算量可能会导致sider异常
      // andv 更新后，如果trigger插槽可用，则此处代码可废弃
      const getTrigger = h(LayoutTrigger);

      return {
        prefixCls,
        sideRef,
        dragBarRef,
        getIsMobile,
        getHiddenDomStyle,
        getSiderClass,
        getTrigger,
        getTriggerAttr,
        getCollapsedWidth,
        getMenuFixed,
        showClassSideBarRef,
        getMenuWidth,
        getCollapsed,
        getMenuTheme,
        onBreakpointChange,
        getMode,
        getSplitType,
        getShowTrigger,
        toggleCollapsed,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-layout-sideBar';

  .@{prefix-cls} {
    z-index: @layout-sider-fixed-z-index;
    border-right:1px solid #ddd;

    &--fixed {
      position: fixed;
      top: 0;
      left: 0;
      height: 100%;
    }

    &--mix {
      top: @header-height;
      height: calc(100% - @header-height);
    }

    &.ant-layout-sider-dark {
      background-color: @sider-dark-bg-color;

      .ant-layout-sider-trigger {
        color: darken(@white, 25%);
        background-color: @trigger-dark-bg-color;

        &:hover {
          color: @white;
          background-color: @trigger-dark-hover-bg-color;
        }
      }
    }
    &.ant-layout-sider-light{
      background:#f6f6f6;
    }
    .scroll-container .scrollbar__wrap{
      padding:20px 0;
    }
    .scroll-container .scrollbar__wrap::-webkit-scrollbar { 
        width: 0 !important
     }

    &:not(.ant-layout-sider-dark) {
      // box-shadow: 2px 0 8px 0 rgba(29, 35, 41, 0.05);

      .ant-layout-sider-trigger {
        color: @text-color-base;
        border-top: 1px solid @border-color-light;
      }
    }

    .ant-layout-sider-zero-width-trigger {
      top: 40%;
      z-index: 10;
    }

    & .ant-layout-sider-trigger {
      height: 36px;
      line-height: 36px;
    }
  }
</style>

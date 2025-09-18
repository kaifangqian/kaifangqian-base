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
  <div :class="`c-scrollbar-bar c-scrollbar-${direction}`"
       @mousedown="handleBarMouseDown"
       :style="barStyle"
       ref="barRef">
    <div class="c-scrollbar-thumb"
         ref="thumbRef"
         @mousedown="handleThumbMouseDown"
         :style="thumbStyleObj"></div>
  </div>
</template>

<script lang="ts">
import { computed, ComputedRef, ref } from 'vue';
import { BAR_MAP } from './utils';
import { BarMap, BarMapItem } from './index.d';

export default {
  name: 'c-scrollbar-bar',
  props: {
    parentRef: {
      required: true,
    },
    direction: {
      type: String,
      default: 'vertical', // 支出2个值 水平 horizontal 垂直 vertical
    },
    size: {
      // thumb的宽度或高度
      type: String,
    },
    move: {
      type: String,
    },
    trigger: {
      type: String,
      default: 'hover', // hover 鼠标移动上去显示 always 一直显示 none 不显示
    },
    barStyle: {
      // 设置轨道
      type: Object,
      default: () => ({
        'background-color': '',
      }),
    },
    thumbStyle: {
      // 设置滑块
      type: Object,
      default: () => ({
        'background-color': 'rgba(0, 0, 0, 0.2)',
      }),
    },
  },
  setup(props: any) {
    const barRef = ref();
    const bar: ComputedRef<BarMapItem> = computed(() => (BAR_MAP as BarMap)[props.direction]);
    const thumbStyleObj = computed(() => ({
      ...props.thumbStyle,
      [bar.value.size]: props.size,
      transform: `translate${bar.value.axis}(${props.move})`,
    }));

    /**
     * 点击轨道 滚动区域滚动到对应的位置 定位到滑块的中心点
     */
    const thumbRef = ref();
    function handleBarMouseDown(e: any) {
      const client = e[bar.value.client]; // 点击位置距客户端顶部或最左边的位置
      const wrap = barRef.value.getBoundingClientRect()[bar.value.direction]; // 滚动轨道距顶部或最左边的位置
      const offset = Math.abs(wrap - client); // 距离元素上或左边距的距离
      const thumbHalf = thumbRef.value[bar.value.offset] / 2; // thumb一般的高度
      const $parentWrap = props.parentRef;
      const thumbPosPercent = ((offset - thumbHalf) * 100) / barRef.value[bar.value.offset];
      $parentWrap[bar.value.scroll] = (thumbPosPercent * $parentWrap[bar.value.scrollSize]) / 100;
    }

    const thumbDrag = ref(false);

    /**
     * 拖动过程中
     */
    function handleThumbMove(e: any) {
      if (!thumbDrag.value) {
        return;
      }
      const client = e[bar.value.client]; // 点击位置距客户端顶部或最左边的位置
      const wrap = barRef.value.getBoundingClientRect()[bar.value.direction]; // 滚动轨道距顶部或最左边的位置
      const offset = Math.abs(wrap - client); // 距离元素上或左边距的距离
      const thumbHalf = thumbRef.value[bar.value.offset] / 2; // thumb一般的高度
      const $parentWrap = props.parentRef;
      const thumbPosPercent = ((offset - thumbHalf) * 100) / barRef.value[bar.value.offset];
      $parentWrap[bar.value.scroll] = (thumbPosPercent * $parentWrap[bar.value.scrollSize]) / 100;
    }

    /**
     * 放开鼠标，拖动结束
     */
    function hanldeThumbUp(event: any) {
      thumbDrag.value = false;
      if (event.target) {
        document.removeEventListener('mousemove', handleThumbMove); // 注册在document上而不是event.target上是为了更流畅，为了解决鼠标滑动过快，滚动滑块跟不上，和未释放鼠标时，在滑块旁滚动的问题
        document.removeEventListener('mouseup', hanldeThumbUp);
      }
      document.onselectstart = () => null;
    }

    /**
     * 拖拽开始
     */
    function startDrag(event: Event) {
      thumbDrag.value = true;
      if (event.target) {
        document.addEventListener('mousemove', handleThumbMove);
        document.addEventListener('mouseup', hanldeThumbUp);
      }
      document.onselectstart = () => false;
    }

    /**
     * 鼠标在滑块thumb上按下
     */
    function handleThumbMouseDown(event: Event) {
      startDrag(event);
    }

    return {
      barRef,
      thumbRef,
      bar,
      thumbStyleObj,
      handleBarMouseDown,
      handleThumbMouseDown,
    };
  },
};
</script>

<style lang="less">
.c-scrollbar-bar {
  position: absolute;
  z-index: 1;
  border-radius: 4px;
  transition: opacity 120ms ease-out;

  .c-scrollbar-thumb {
    width: 0;
    height: 0;
    cursor: pointer;
    border-radius: inherit;
    background-color: rgba(0, 0, 0, 0.7);
    transition: 0.3 background-color;
  }

  &.c-scrollbar-vertical {
    width: 6px;
    top: 0;
    bottom: 0;
    right: 0;
    .c-scrollbar-thumb {
      width: 100%;
    }
  }

  &.c-scrollbar-horizontal {
    height: 6px;
    left: 0;
    right: 0;
    bottom: 0;
    .c-scrollbar-thumb {
      height: 100%;
    }
  }
}
</style>

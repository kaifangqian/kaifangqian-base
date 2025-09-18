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
  <div :class="`c-scrollbar c-scrollbar-${trigger}`"
       :style="{width,height,'max-height':maxHeight,'max-width':maxWidth}">
    <div :class="{'c-scrollbar-wrap':true,[`c-scrollbar-wrap-${direction}`]:true}"
         ref="wrapRef"
         :style="{'max-height':maxHeight,'max-width':maxWidth}"
         @scroll="handleScroll">

      <div class="c-scrollbar-content">
        <slot></slot>
      </div>

      <Bar direction="vertical"
           :parentRef="wrapRef"
           :size="vThumbHeight"
           :move="moveY"
           :barStyle="vBarStyle"
           :thumbStyle="vThumbStyle"
           v-show="hasVBar"
           class="c-scrollbar-bar-v-bar"
           v-if="direction !== 'x'"></Bar>

      <Bar direction="horizontal"
           :parentRef="wrapRef"
           :size="hThumbWidth"
           :move="moveX"
           :barStyle="hBarStyle"
           :thumbStyle="hThumbStyle"
           v-show="hasHBar"
           class="c-scrollbar-bar-h-bar"
           v-if="direction !== 'y'"></Bar>

    </div>
  </div>
</template>

<script lang="ts">
import { onMounted, onUnmounted, reactive, ref, toRefs } from 'vue';
import Bar from './Bar.vue';
import { addResizeListener, removeResizeListener } from './resize-event';

export default {
  name: 'c-scrollbar',
  components: {
    Bar,
  },
  props: {
    width: {
      type: String,
      default: '',
    },
    height: {
      type: String,
      default: '',
    },
    maxHeight: {
      type: String,
      default: '',
    },
    maxWidth: {
      type: String,
      default: '',
    },
    trigger: {
      type: String,
      default: 'always', // hover 鼠标移动上去显示 always 一直显示 none 不显示
    },
    noresize: Boolean, // 如果 container 尺寸不会发生变化，最好设置它可以优化性能
    direction: {
      type: String,
      default: 'all', // all 横向 纵向 都出现滚动条 x 水平 y 垂直
    },
    vBarStyle: Object,
    hBarStyle: Object,
    vThumbStyle: Object,
    hThumbStyle: Object,
  },
  setup(props: any, { emit }: any) {
    const wrapRef = ref();
    const state = reactive({
      vThumbHeight: '0',
      hThumbWidth: '0',
      moveX: '0%',
      moveY: '0%',
      hover: false,
    });

    /**
     * 真实滚动条滚动时，修改滚动条bar移动距离
     */
    function handleScroll(event: Event) {
      const { scrollTop, clientHeight, scrollLeft, clientWidth }: any = event.target;
      state.moveY = `${(scrollTop * 100) / clientHeight}%`;
      state.moveX = `${(scrollLeft * 100) / clientWidth}%`;
      emit('scroll', event);
    }

    /**
     * 更新thumb的宽高 外层容器高度 / 整个内容的高度（整个可滚动内容区域） =  滚动条thumb的高度 / 滚动轨道的高度（一般滚动轨道的高度与外层容器相等）
     */
    const hasVBar = ref(false);
    const hasHBar = ref(false);
    function update() {
      if (wrapRef.value) {
        const heightPercentage = (wrapRef.value.clientHeight * 100) / wrapRef.value.scrollHeight;
        const widthPercentage = (wrapRef.value.clientWidth * 100) / wrapRef.value.scrollWidth;

        if (heightPercentage < 100) {
          hasVBar.value = true;
        } else {
          hasVBar.value = false;
        }

        if (widthPercentage < 100) {
          hasHBar.value = true;
        } else {
          hasHBar.value = false;
        }
        state.vThumbHeight = heightPercentage < 100 ? `${heightPercentage}%` : '';
        state.hThumbWidth = widthPercentage < 100 ? `${widthPercentage}%` : '';
      }
    }

    function setScrollTop(value: number) {
      wrapRef.value.scrollTop = value;
    }

    function setScrollLeft(value: number) {
      wrapRef.value.scrollLeft = value;
    }

    onMounted(() => {
		// setTimeout(function(){
		// 	update(); // 初始化调用一次，计算滚动条默认高度
		// 	if (!props.noresize) {
		// 	  addResizeListener(wrapRef.value, update); // 监听元素变化，如果容器DOM变化触发更新
		// 	}
		// },10);
		update(); // 初始化调用一次，计算滚动条默认高度
		if (!props.noresize) {
		  addResizeListener(wrapRef.value, update); // 监听元素变化，如果容器DOM变化触发更新
		}
      
    });

    onUnmounted(() => {
      if (!props.noresize) {
        removeResizeListener(wrapRef.value, update);
      }
    });

    return {
      ...toRefs(state),
      wrapRef,
      handleScroll,
      hasVBar,
      hasHBar,
      setScrollTop,
      setScrollLeft,
	  update
    };
  },
};
</script>

<style lang="less">
.c-scrollbar {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;
  background: transparent;

  &.c-scrollbar-hover {
    .c-scrollbar-bar {
      opacity: 0;
    }

    &:hover,
    &:focus,
    &:active {
      .c-scrollbar-bar {
        opacity: 1;
      }
    }
  }

  &.c-scrollbar-always {
    .c-scrollbar-bar {
      opacity: 1;
    }
  }

  &.c-scrollbar-none {
    .c-scrollbar-bar {
      opacity: 0;
    }
  }
}

.c-scrollbar-wrap-all {
  overflow: scroll;
}
.c-scrollbar-wrap-x {
  overflow-x: scroll;
  overflow-y: hidden;
}
.c-scrollbar-wrap-y {
  overflow-y: scroll;
  overflow-x: hidden;
}
.c-scrollbar-wrap {
  height: 100%;
  -ms-overflow-style: none;
  scrollbar-width: none;

  &::-webkit-scrollbar {
    width: 0;
    height: 0;
    display: none;
  }
}
</style>

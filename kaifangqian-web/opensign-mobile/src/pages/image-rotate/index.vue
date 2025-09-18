<!--
  @description 预览图片旋转

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
  <div class="image-preview-rotate-bind">
    <p>v-bind方式</p>
    <van-image width="100" height="100" :src="img" @click="onPreviewBind" />
    <van-image-preview v-model:show="state.showPreviewBind" :images="[img]">
      <template #cover><van-icon name="replay" @click="setRotateBind" /></template>
    </van-image-preview>
  </div>
  <div>
    <p>css全局变量方式</p>
    <van-image width="100" height="100" :src="img" @click="onPreview" />
    <van-image-preview
      v-model:show="state.showPreview"
      :images="[img]"
      teleport="body"
      class-name="image-preview-rotate"
    >
      <template #cover><van-icon name="replay" @click="setRotate" /></template>
    </van-image-preview>
  </div>
</template>

<script setup lang="ts">
  import { ImagePreview } from 'vant';
  import { setRotate } from '@/utils';

  // ImagePreview 是一个函数，ImagePreview.Component才是组件
  const VanImagePreview = ImagePreview.Component;

  const img = 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg';

  const state = reactive({
    showPreviewBind: false,
    showPreview: false,
    rotate: '0deg',
  });

  const onPreviewBind = () => {
    state.showPreviewBind = true;
    state.rotate = '0deg';
  };
  const setRotateBind = () => {
    state.rotate = parseInt(state.rotate) + 90 + 'deg';
  };

  const onPreview = () => {
    state.showPreview = true;
    setRotate('0deg');
  };
</script>

<style lang="less">
  .van-image-preview__cover {
    font-size: 40px;
    color: #fff;
    left: 50%;
    top: auto;
    bottom: var(--van-padding-md);
    transform: translate(-50%);
    &:active {
      opacity: 0.4;
    }
  }

  .image-preview-rotate-bind .van-image-preview__image .van-image__img {
    transform: rotate(v-bind('state.rotate'));
  }

  .image-preview-rotate .van-image-preview__image .van-image__img {
    transform: rotate(var(--image-rotate));
  }
</style>

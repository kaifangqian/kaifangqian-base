<!--
  @description ：App.vue

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
  <router-view />
</template>

<script setup lang="ts">
  import debug from '@/utils/debug';
  import copyPaste from '@/utils/lib/copy-paste';
  import { useTitle } from '@vueuse/core';
  // import Api from '@/api/user';
  import { useUserStore } from '@/store/modules/user';

  onMounted(() => {
    // 因为debug是存入localStorage中的，刷新页面会从localStorage取出，根据debug控制是否隐藏
    debug.init();

    copyPaste.disable();
    const user = useUserStore();
    console.log('user.getWebConfig', user.getWebConfig);
    initTitle(user.getWebConfig?.websiteTitle);
  });

  async function initTitle(websiteTitle: any) {
    let defaultTitle = import.meta.env.VITE_APP_TITLE;
    let title = useTitle(defaultTitle);
    // let { result, code } = await Api.getWebTitle({});
    // if (code == 200) {
    title.value = websiteTitle || defaultTitle;
    // }
  }

  onBeforeUnmount(() => {
    copyPaste.enable();
  });
</script>

<style>
  #app {
    font-family: Inter, Avenir, Helvetica, Arial, sans-serif;
    font-synthesis: none;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    -webkit-text-size-adjust: 100%;
  }
</style>

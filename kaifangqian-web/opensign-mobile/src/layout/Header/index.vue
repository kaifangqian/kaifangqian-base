<!--
  @description Header

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
  <div class="header">
    <van-nav-bar
      :title="(route.path==='/index'? title :route.name as string)"
      :left-arrow="(route.meta.leftArrow && route.meta.leftArrow as Boolean) == true"
      @click-left="navBack"
      :left-text="(route.meta.leftArrow && route.meta.leftArrow as Boolean) == true ? '返回' : ''"
    >
      <!-- <template #right>
                <van-icon name="friends-o" />
            </template> -->
    </van-nav-bar>
    <!-- <van-action-sheet v-model="actionSheet..show" title="标题">
		  <p>内容</p>
		</van-action-sheet> -->
    <!-- <van-action-sheet v-model:show="actionSheet.show" :actions="actions" @select="onSelect" /> -->
  </div>
</template>

<script setup lang="ts">
  import { getHashQueryString, decodeURIs } from '@/utils/util';
  import Api from '@/api/contract/index';
  import session from '@/utils/cache/session';
  import { APP_WEB_CONFIG } from '@/utils/cache/constant';
  import DownloadActionSheet from '@/pages/components/DownloadActionSheet.vue';

  const title = ref('');
  setTimeout(() => {
    const userConfig = session.getItem(APP_WEB_CONFIG);
    title.value = userConfig.websiteTitle;
  });

  const callbackPage = getHashQueryString('callbackPage');
  const router = useRouter();
  const route = useRoute();

  async function navBack() {
    if (callbackPage && typeof callbackPage == 'string') {
      window.open(decodeURIs(callbackPage), '_self');
    } else {
      router.push({
        path: '/',
      });
      //   router.go(-1);
    }
  }
</script>

<style lang="less" scoped>
  .header {
    .van-nav-bar {
      background: @gray;

      :deep(.van-nav-bar__title) {
        color: #fff;
      }
    }

    :deep(.van-nav-bar .van-icon) {
      color: #fff;
    }

    :deep(.van-nav-bar__text) {
      color: #fff;
    }
  }
</style>

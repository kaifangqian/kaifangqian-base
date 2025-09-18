<!--
  @description layout

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
    <div class="layout">
        <Header />
        <div class="content" style="padding: 10px;">
            <router-view v-slot="{ Component }">
                <keep-alive :include="cachedViews">
                    <component :is="Component" />
                </keep-alive>
                <!-- <component :is="Component" v-if="!$route.meta.keepAlive" /> -->
            </router-view>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import Header from './Header/index.vue';
import { useMenuStore } from '@/store/modules/menu';

const menuInfo = useMenuStore();

const cachedViews = computed(() => menuInfo.getCacheViews)


</script>

<style lang="less" scoped>
.layout {
    position: absolute;
    width: 100%;
    height: 100%;

    .header {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: @header-height;
        // height: 150px;
        z-index: 1;
        box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.2);
    }

    .content {
        margin-top: @header-height;
        height: calc(100% - @header-height);
        overflow: auto;
        background-color: #f7fafd;
    }
}
</style>

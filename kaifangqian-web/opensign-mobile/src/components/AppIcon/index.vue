<!--
  @description icon

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
  <i class="icon" :style="{ 'background-image': `url(${iconUrl})` }" />
</template>

<script setup lang="ts">
  const props = defineProps({
    name: {
      type: String,
      required: true,
    },
  });

  const iconUrl = computed(() => {
    return new URL(`/src/assets/icons/${props.name}.png`, import.meta.url).href;
  });
</script>

<style lang="less" scoped>
  .icon {
    display: inline-block;
    width: 24px;
    height: 24px;
    background: center / contain;
    vertical-align: middle;
  }
</style>

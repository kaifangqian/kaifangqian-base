<!--
  @description loading

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
    <van-overlay :show="loading" class='base-mask'>
        <van-loading class='base-loading' v-if="loading" size="24px" color="#1989fa">{{ text }}</van-loading>
    </van-overlay>
</template>

<script lang="ts">
import { ref, defineComponent } from "vue"
export default defineComponent({
    name: "Loading",
    props: {
        loading: {
            type: Boolean,
            default: false
        },
        text: {
            type: String,
            default: '加载中...'
        }
    },
    setup() {
        return {
        }
    }
})
</script>

<style lang="less" scoped>
.base-mask {
    background-color: rgba(41, 39, 39, 0.7);
    z-index: 2210;

    .base-loading {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
    }
}
</style>

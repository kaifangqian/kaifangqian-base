<!--
  @description svg 图标组件

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
    <svg aria-hidden="true" :style="getStyle">
        <use :href="symbolId" :fill="color" />
    </svg>
</template>
  
<script>
// import type { CSSProperties } from 'vue';
import { defineComponent, computed } from 'vue'

export default defineComponent({
    name: 'SvgIcon',
    props: {
        prefix: {
            type: String,
            default: 'icon',
        },
        name: {
            type: String,
            required: true,
        },
        color: {
            type: String,
            default: '#333',
        },
        size: {
            type: [Number, String],
            default: 16,
        },
    },
    setup(props) {
        const symbolId = computed(() => `#${props.prefix}-${props.name}`)
        const getStyle = computed(() => {
            const { size } = props;
            let s = `${size}`;
            s = `${s.replace('px', '')}px`;
            return {
                width: s,
                height: s,
            };
        });
        return { symbolId, getStyle }
    },
})
</script>
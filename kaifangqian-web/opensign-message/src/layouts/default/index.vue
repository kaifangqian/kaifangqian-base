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
  <Layout :class="prefixCls">
    <LayoutHeader headerName="消息中心"></LayoutHeader>
    <LayoutContent />    
  </Layout>
</template>

<script lang="ts">
  import { defineComponent} from 'vue';
  import { Layout } from 'ant-design-vue';
  import LayoutContent from './content/index.vue';
  import LayoutHeader  from '../header/index.vue'

  import { useDesign } from '/@/hooks/web/useDesign';

  export default defineComponent({
    name: 'DefaultLayout',
    components: {
      LayoutContent,
      Layout,
      LayoutHeader
    },
    setup() {
      const { prefixCls } = useDesign('default-layout');      
      return {
        prefixCls,
     
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-default-layout';

  .@{prefix-cls} {
    display: flex;
    width: 100%;
    min-height: 100%;
    background-color: @content-bg;
    flex-direction: column;
    height:100%!important;

    > .ant-layout {
      min-height: 100%;
    }

    &-main {
      width: 100%;
      // margin-left: 1px;
      background-color: @content-bg;
    }
  }
</style>

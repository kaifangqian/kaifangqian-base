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
  <span :class="[getfooterClass,getCollapsed?'unfold':'fold']" @click="toggleCollapsed">
    <MenuUnfoldOutlined v-if="getCollapsed" /> <MenuFoldOutlined v-else />
  </span>
</template>
<script lang="ts">
  import { defineComponent ,computed} from 'vue';
  import { MenuUnfoldOutlined, MenuFoldOutlined } from '@ant-design/icons-vue';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { propTypes } from '/@/utils/propTypes';

  export default defineComponent({
    name: 'HeaderTrigger',
    components: { MenuUnfoldOutlined, MenuFoldOutlined },
    props: {
      theme: propTypes.oneOf(['light', 'dark']),
    },
    setup(props) {
      const { getCollapsed, toggleCollapsed } = useMenuSetting();
      const { prefixCls } = useDesign('layout-menu-footer-trigger');
      // const collapsedClass =  getCollapsed?'unfold':'fold' 
      let getfooterClass = computed(() => [
          prefixCls,
          props.theme,
        ]);
      return { getCollapsed, toggleCollapsed, prefixCls,getfooterClass };
    },
  });
</script>
<style lang="less">
@prefix-cls: ~'@{namespace}-layout-menu-footer-trigger';

 .@{prefix-cls}{
    position: absolute;
    bottom: 0px;
    // width: 100%;
    height: 32px;
    cursor: pointer;
    padding-left: 16px;
    display: flex;
    align-items: center;
    & svg{
      font-size:20px;
    }
    &.light{
      color: #262626;
      // background: #ffffff;
      // border-top: 1px solid #e4e4e4;
        & .anticon {
          background: rgba(243, 243, 243, 1);
          padding:6px;
          border-radius: 2px;
        }
    }
    &.dark{
      color: #fff;
       & .anticon {
          background: rgba(75, 75, 75, 1);
          padding:6px;
          border-radius: 2px;
        }
    }
    &.unfold{
      padding-left: 8px;
      // justify-content: center;
    }
    &.fold{
      justify-content: normal;
      background: #f4f4f4;
      width: 100%;
      padding: 0px 16px;
      border-top: 1px solid #eee;
      height: 45px;
      z-index: 99;
    }
}

</style>

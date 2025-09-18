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
  <div class="business-header-container" :class="`${prefixCls}`">
    <div :class="`${prefixCls}-back`">
      <Icon icon="ant-design:left-outlined"/>
      <span>返回</span>
    </div>
    <div :class="`${prefixCls}-title`">{{  getBusinessTitle}}</div>
    <div :class="`${prefixCls}-actions`">
      <a-button type="primary">保存</a-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
import { useDesign } from '/@/hooks/web/useDesign';
// import { useAppStore } from '/@/store/modules/app';
import { Icon } from '/@/components/Icon';
export default defineComponent({
  name:"BusinessHeader",
  components:{
    Icon
  },
  setup() {
    const { prefixCls } = useDesign('layout-business-header');
    const { getBusinessTitle } = useHeaderSetting();
    console.log(getBusinessTitle)


    return {
      prefixCls,
      getBusinessTitle
    }
  }
})
</script>

<style lang="less" scoped>
 @prefix-cls: ~'@{namespace}-layout-business-header';

 .@{prefix-cls}{
  display: flex;
  align-items: center;
  justify-content: space-between;
  height:62px;
  border-bottom:1px solid #f0f0f0;
  padding:0 20px;
  position: fixed;
  width: 100%;
  background: #fff;
  z-index: 999;
  &-back{
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  &-title{
    font-size: 18px;
    font-weight: 600;
  }
 }



</style>

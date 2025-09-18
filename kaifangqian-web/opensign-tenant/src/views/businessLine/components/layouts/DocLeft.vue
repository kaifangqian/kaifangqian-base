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
  <div class="doc-layout-side-container">
    <p class="layout-side-title">
     <span> {{ title }} </span>
     <a-tooltip>
      <template #title>可指定各个签署方在文件上签署的位置,拖拽签署方下面对应的签署控件到文档正文区域</template>
      <Icon icon="ant-design:question-circle-outlined" style="margin-left:10px;"/>
    </a-tooltip>
    
    </p>
    <Scrollbar width="100%" height="100%" >
      <div class="side-left-content">
      <slot></slot>
      </div>
    </Scrollbar>
  </div> 
</template>

<script lang="ts">
import {ref,defineComponent} from "vue";
import { Scrollbar } from '/@/components/Scrollbar';
import { Icon } from '/@/components/Icon';
export default defineComponent({
  name:"DocLeft",
  components:{
    Scrollbar,
    Icon
  },
  props:{
    title:{
      type:String,
    }
  },
  setup() {
    const data = ref('') 
    return {
         data  
    }
  }
})
</script>

<style lang="less" scoped>
.doc-layout-side-container{
  width:350px;
  min-height:calc(100vh - 63px);
  border-right: 1px solid #eee;
  padding:20px 25px 40px 25px;
  position: relative;
}
</style>

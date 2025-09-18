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
  <div class="container">
    <div class="header-area">
      <div class="header-left">
        <img src="../../../assets/images/logo.png" alt="">
      </div>
      <div class="header-right">
        <a-button type="primary">免费注册</a-button>
      </div>
    </div>
    <div class="body-area">
      隐私协议
    </div>
   
  </div>
</template>

<script lang="ts">
import {ref,defineComponent} from "vue"
export default defineComponent({
  name:"App",
  setup() {
    const data = ref('') 
    return {
         data  
    }
  }
})
</script>

<style lang="less" scoped>
.header-area{
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 5px 8px 0 rgb(64 106 103 / 13%);
  background: hsla(0,0%,100%,.95);
  padding:0 20px;
  .header-left{
    img{
      width:300px;
    }
  }
}
.body-area{
  margin-top:60px;
}
</style>

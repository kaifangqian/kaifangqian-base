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
  <div class="no-auth">
    <SvgIcon name="no-auth1" :size="100"/>
    <p>您暂无该应用的权限</p>
    <p>请联系企业管理员为您分配权限</p>
    <a-button type="primary" @click="backParent()" style="width: 100px;">返回</a-button>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, onMounted, unref } from 'vue';
  import { SvgIcon } from '/@/components/Icon';
    
  export default defineComponent({
    name: 'noAuth',
    components: {
      SvgIcon
    },
    setup() {
      const backParent=()=>{
      	if(window.opener){
          window.close();
          window.opener.focus();
      	}else{
          window.open(window.appInfo.sign_app_info.url);
          window.close();
      	}
      }
      return{
        backParent
      }
    }
  })
</script>

<style lang="less">
  .no-auth{
    width: 300px;
    text-align: center;
    margin: 0 auto;
    padding-top: 300px;
    font-size: 16px;
    font-weight: 600;
  }
</style>

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
  <div class="bpms-container">
    <div id="bpms-paas"></div>
    <micro-app name='app-bpms' url='http://localhost:8904/' baseroute='/app-bpms' keep-alive :data='microAppData'  @datachange='handleDataChange' @mounted='mounted' @created="created"></micro-app>
  </div>
</template>

<script lang="ts">
  import { defineComponent,ref,unref,computed,reactive } from 'vue'
  import microApp from '@micro-zoe/micro-app';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getToken } from '/@/utils/auth';
  import { useUserStore } from '/@/store/modules/user';



export default defineComponent({
  name:"MicroBpms",
  setup(_, ){
     
      const { createMessage: msg } = useMessage();
      const userStore = useUserStore();
      const microAppData = reactive({data:{
          token:getToken()
        }});

      function handleDataChange (e) {
      console.log('来自子应用的数据：', e.detail.data)
      }
      function mounted(){
        // microApp.setData('app-boot', { token: '/bpmns/application/ini' })
        const permissionInfo = userStore.getPermissionInfo;
        console.log(permissionInfo,'路由信息micro')
        microApp.setData('app-bpms', {permission: permissionInfo})
    
      }
      function created(){
        console.log('开始发送token')
        const token = getToken();
        microApp.setData('app-bpms', {token });
        msg.success('发送成功');
      }
   
      return { 
          handleDataChange,
          mounted,
          microAppData,
          created
      
      };
    }
})
</script>

<style lang="less" scoped>
</style>

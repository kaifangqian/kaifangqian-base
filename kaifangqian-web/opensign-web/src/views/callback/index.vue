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
  <div v-if="loadingState" class="loading-container">
    <div class="loading-spinner"></div>
    <div class="loading-text">加载中</div>
  </div>
</template>


<script lang='ts'>

import { getCallBackPage} from '/@/views/callback/api';
import { message } from 'ant-design-vue';
import { onMounted,ref } from 'vue';
import { getHashQueryString } from '/@/utils';

export default ({
  name: 'YdCallBackPage',
  components:{
    
  },
setup(){

  // 使用响应式数据控制加载状态
  const loadingState = ref(true); 
  // loading时显示加载中

  onMounted(()=>{
    init();
     
  })

  async function init(){
    try {
      // 根据云盾回调地址中的token获取同步回调地址的上下文信息

      // 从云盾回调地址中获取token
      const token = getHashQueryString('state');
      if(token){
        const result  = await getCallBackPage({ token });
        console.log('result-----',result);
        if(result && result.token){
          // 获取成功
          // 跳转到同步回调地址
          window.location.href = result.token;
        }else{
          // 获取失败
          message.error('回调处理失败');
        }
      }else{
        // 获取token失败
        message.error('同步回调参数异常');
      }
   
    }catch (error) {
          console.error('回调处理出错:', error);
          message.error('回调处理异常');
        } finally {
          loadingState.value = false;
        }
  }





  
  return {
    loadingState
  }


  
},
})
</script>
<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  min-height: 100vh; /* 确保占满整个视口高度 */
  margin-top: 0; /* 移除原来的 margin-top */
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.loading-text {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>

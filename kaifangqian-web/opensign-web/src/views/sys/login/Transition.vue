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
  <div class="transition-area">
      <div class="main-transition">
        <Icon icon="ant-design:check-circle-outlined" size="80" color="#127fd2"/>
        <p class="transition-title">用户注册成功</p>
        <p class="transition-tip">
          <span class="transition-number">{{ countNumber  }}</span>
          <span>秒后自动跳转登录页</span>
        </p>
      </div>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent,onMounted,nextTick} from "vue";
import { Icon } from '/@/components/Icon';
import { useGo } from '/@/hooks/web/usePage';

export default defineComponent({
  name:"Transition",
  components:{
    Icon
  },
  setup() {
    const countNumber = ref(3);
    let timer: ReturnType<typeof setInterval> | null;
    const go = useGo();

    onMounted(()=>{
          timer = setInterval(() => {
          console.log(countNumber.value,'00000----')
          if(countNumber.value == 0){
            clearInterval(timer);
            go('/login')
            return;
          }
          nextTick(()=>{
            countNumber.value = countNumber.value - 1;
          })
        },1000)
      
    })
    return {
      countNumber  
    }
  }
})
</script>

<style lang="less" scoped>
.transition-area{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  .transition-title{
    font-size: 34px;
    margin-top:20px;
  }
  .main-transition{
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
  }
  .transition-tip{
    font-size: 18px;
  }
  .transition-number{
    font-size: 28px;
    color:#127fd2;
    margin-right: 10px;
  }
}
</style>

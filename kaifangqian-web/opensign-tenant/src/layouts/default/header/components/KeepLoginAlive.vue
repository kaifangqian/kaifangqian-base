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
  <div>
    <a-modal class="keep-alive-modal" v-model:visible="visibleKeepAliveLogin" width="280px" title="" :footer="null" :closable="false" :destroyOnClose="true" >
      <div class="countdown-process">
        <a-progress type="circle" :percent="countdown" :width="120" status="active">
          <template #format="countdown">
            <p class="countdown-number">{{ progresstext }}</p>
            <p class="countdown-text">秒</p>
          </template>
        </a-progress>
        <!-- <SvgIcon name="no-auth" size="70" /> -->
      </div>
      <div class="alive-tip">
        <p class="alive-tip-title">登录超时</p>
        <p class="alive-tip-content">为了您的账号安全，请重新登录</p>
        <p class="alive-tip-content">给您带来的不便敬请谅解</p>
      </div>
      <a-button type="primary" class="alive-btn" @click="handleLoginOut">重新登录</a-button>
    </a-modal>
  </div>
</template>
<script lang='ts'>
import { defineComponent, computed,ref, watch} from 'vue';
import { useUserStore } from '/@/store/modules/user';
import { SvgIcon } from '/@/components/Icon';

export default defineComponent({
  name: 'KeepLoginAlive',
  components:{
    SvgIcon,
  },
  setup(){

    const userStore = useUserStore();
    const countdown = ref(100);
    const progresstext = ref(5);
    const timer = ref<any>();

    const visibleKeepAliveLogin = computed(() => userStore.getKeepLoginAlive === false);

    watch(
      visibleKeepAliveLogin,
      (val) => {
        if(val){
          timer.value = setInterval(()=>{
            progresstext.value = progresstext.value - 1;
            countdown.value =  countdown.value - 20;
            if(progresstext.value==0){
              clearInterval(timer.value);
              userStore.logout(true);
              userStore.setKeepLoginAlive(true);
              handleLoginOut();
            }
          },1000)
        }
      },
      {
        immediate: true,
      },
    );

    function handleLoginOut(){
      userStore.logout(true);
    }



    return {
      visibleKeepAliveLogin,
      countdown,
      handleLoginOut,
      progresstext
    }
  }
})
</script>
<style lang="less">
.keep-alive-modal{
  p{
    margin-bottom: 0;
  }
  text-align: center;
  .ant-modal-body{
    position: relative;
    padding-bottom: 30px;
  }
  .countdown-process{
    margin:20px 0 30px;
   .ant-progress-inner{
      background-color: #fff;
    }
  }
  .ant-progress-text{
    .countdown-number{
      font-size: 26px;
    }
    .countdown-text{
      font-size: 18px;
      margin-top:10px;
      color: #666;
    }
  }
  .alive-tip-title{
    font-size: 18px;
    margin-bottom:10px;
    font-weight: 600;
  }
  .alive-tip-content{
    font-size: 14px;
    color: #666;
  }
  .alive-btn{
    margin-top:20px;
    width:80%;
  }
}
.countdown-text{

}
 
</style>

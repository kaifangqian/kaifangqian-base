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

/*
 * @Author: ningw 
 * @Date: 2022-06-28 18:22:10 
 * @Last Modified by: ningw
 * @Last Modified time: 2024-01-05 10:21:22
 */
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="租户切换" @ok="handleSubmit">
       <p>检测到当前应用已在其他页签切换了租户，是否同步租户信息到当前页签</p>
       <template #footer> 
          <a-button type="default" @click="handleTokenSwitch(false)">否</a-button>
          <a-button type="primary" @click="handleTokenSwitch(true)">是</a-button>
       </template>
    </BasicModal>
  </div>
</template>
<script lang='ts'>
import { defineComponent, onMounted, onUnmounted } from 'vue'
import { BasicModal,useModal } from '/@/components/Modal';
import { Icon } from '/@/components/Icon';
import { useUserStore } from '/@/store/modules/user';
// import { useMessage } from '/@/hooks/web/useMessage';
import { getToken } from '/@/utils/auth'; 
import { checkToken } from '/@/api/sys/user';
import { isDevMode } from '/@/utils/env';

export default defineComponent({
  name: 'LoginSelectAccount',
  components:{
    BasicModal,
    // BasicForm,
    Icon
  },
  setup(_,{emit}){

      const userStore = useUserStore();

      const [registerModal, { openModal,closeModal }] = useModal();
      onMounted(()=>{
        console.log('tab切换组件加载了')
        // document.addEventListener("visibilitychange", function() {
        //   if (document.visibilityState === 'visible') {
        //     const localToken = isDevMode()?(localStorage.getItem('VUE_RESRUN_ADMIN__DEVELOPMENT__2.8.0__COMMON__LOCAL__KEY__')? 
        //     JSON.parse(localStorage.getItem('VUE_RESRUN_ADMIN__DEVELOPMENT__2.8.0__COMMON__LOCAL__KEY__') as string ).value.TOKEN__.value:''):
        //     localStorage.getItem('VUE_RESRUN_ADMIN__PRODUCTION__2.8.0__COMMON__LOCAL__KEY__')? 
        //     JSON.parse(localStorage.getItem('VUE_RESRUN_ADMIN__PRODUCTION__2.8.0__COMMON__LOCAL__KEY__') as string ).value.TOKEN__.value:'';
        //     const pageToken = userStore.getPageToken;
        //     console.log(localToken,'两个token',pageToken)
        //     if(localToken !== pageToken){
        //       checkTenantInfoByToken(pageToken, localToken);
        //     }else{
        //       checkTenantInfoByToken(pageToken, localToken);
        //     }
        //     // 页面处于活动状态
        //   } else {
        //     // 页面处于非活动状态
        //   }
        // });

      })
      onUnmounted(()=>{
        console.log('组件卸载了')
        document.removeEventListener('visibilitychange',()=>{

        })
      })
      async function checkTenantInfoByToken(pageToken, localToken){
        let response = await checkToken({token:pageToken});
        if(response.data.code==500){
          userStore.logout(true);
          return
        }
        if(response.data.result){
          userStore.setPageToken(localToken);
        }else{
          openModal(true,{
            isUpdate:false
          })
        }
      }
      function handleSubmit(){
          emit('success');
          closeModal()
      }
      function handleLogOut(type){
        switch(type){
          case 0:
            closeModal;
            break;
          case 1:
            userStore.logout(true);
            break;
          case 2:
          userStore.logoutAll(true);
            break;

        }

      }
      function handleTokenSwitch(type){
        const localToken = getToken() as string;
        const pageToken:string = userStore.getPageToken;
        if(type){
          //本地缓存更新页面缓存token
          userStore.setPageToken(localToken);
        }else{
          //页面缓存更新本地缓存
          userStore.setToken(pageToken)
        }
        closeModal();
        window.location.reload();
      }
      function handleBack(){
        closeModal();
      }
    return {
      handleTokenSwitch,
      handleLogOut,
      registerModal,
      handleSubmit,
      handleBack
    }
  },
})
</script>
<style lang="less" scoped>
 
</style>

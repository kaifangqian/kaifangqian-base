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
  <div class="sign-enter-container">
      <a-button type="primary"  shape="round" class="sign-enter"
      @click="handleSelectLine">
        <template #icon>
          <Icon icon="ant-design:plus-outlined"/>
        </template>
        发起签署
      </a-button>
      <!-- <a-button type="primary"  shape="round" class="sign-enter"
      @click="handleSelectLine" v-if="userType == 'core'">
        <template #icon>
          <Icon icon="ant-design:plus-outlined"/>
        </template>
        发起签署
      </a-button> -->
      <!-- <a-tooltip placement="bottom" v-else>
        <template #title>
          <span>您所在的企业暂无发起权限，请联系系统管理员进行企业账号升级</span>
        </template>
        <a-button type="primary" :disabled="true"  shape="round" class="sign-enter" @click="handleSelectLine">
          <template #icon>
            <Icon icon="ant-design:plus-outlined"/>
          </template>
          发起签署
        </a-button>
      </a-tooltip> -->
          
    
    <BusinessLineModal @register="registerModal" @success="handleStart"/>
  </div>
</template>

<script lang="ts">
import {defineComponent,ref} from "vue";
import BusinessLineModal from '/@/layouts/default/header/components/sign/modal/BusinessLineModal.vue';

import { getBusinessLine } from '/@/api/contract';

import {createBusinessLineAuth} from "/@/api/license"

import { Icon } from '/@/components/Icon';

import { useModal } from '/@/components/Modal';

import { useMessage } from '/@/hooks/web/useMessage';
import { useRouter } from 'vue-router';
import { useUserStore } from '/@/store/modules/user';


export default defineComponent({
  name:"SignEnter",
  components:{
    Icon,
    BusinessLineModal
  },
  setup() {

    const [registerModal,{openModal,closeModal}] = useModal();
    const { createMessage: msg } = useMessage();
    const router = useRouter();
    const userStore = useUserStore();
    const userType = userStore.userInfo?.sysType;
    // const userType = "core";
    async  function handleSelectLine(){
      
      const apiLimit = await createBusinessLineAuth();
      if(apiLimit.flag == 3){
        msg.warning('软件授权已过期，无法发起签署');
        return;
      }
      router.push("/business")
      // let result = await getBusinessLine({});
      // if(result.length>1){
      //   openModal(true,{
      //     isUpdate:false,
      //     record:{
      //       list: result
      //     }
      //   })
      // }else if(result.length==1){  
      //   // window.open('/#/contract/start?__full__&signReId=' + result[0].id)
      //   router.push({
      //     path:"/contract/start?__full__",
      //     query:{
      //       signReId:result[0].id
      //     }
      //   })
        
      // }else if(result.length==0){
      //   msg.warning('您暂无发起权限，请联系企业管理员')
      // }

     
    }

    function handleStart(val){
      closeModal()
      // window.open('/#/contract/start?__full__&signReId=' + val)
      router.push({
        path:"/contract/start?__full__",
        query:{
          signReId:val
        }
      })
    }

    return {
      registerModal,
      handleSelectLine,
      handleStart,userType
    }
  }
})
</script>

<style lang="less" scoped>
</style>

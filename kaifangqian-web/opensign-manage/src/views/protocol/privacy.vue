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
  <div class="container adjust-area">
        <a-button type="primary" @click="handleEdit">编辑</a-button>
        <p v-html="serveInfo.mediateContent"></p>
        <PrivacyModal @register="registerModal" @success="handleSuccess"></PrivacyModal>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent, onMounted } from "vue";
import PrivacyModal from './modal/PrivacyModal.vue';
import { BasicModal, useModal } from '/@/components/Modal';
import { getProtocolInfoByToken } from '/@/api/protocol';

export default defineComponent({
  name:"Privacy",
  components:{
    BasicModal,
    PrivacyModal
  },
  setup() {
    const data = ref('');
    const serveInfo = ref({
      mediateContent:'',
      id:''
    }) 

    const [registerModal,{openModal}] = useModal();
    function handleEdit(){
      openModal(true,{
        isUpdate:true,
        record:{
          ...serveInfo.value
        }
      })
    }
    
    onMounted(()=>{
      fetch()
    })

    async function fetch(){
      let result = await getProtocolInfoByToken({type:'privacy'});
      if(result){
        serveInfo.value.mediateContent = result.value;
        serveInfo.value.id = result.id
      }
    }
    function handleSuccess(){
      fetch()
    }

    return {
         data,
         serveInfo,
         registerModal,
         handleEdit,
         handleSuccess 
    }
  }
})
</script>

<style lang="less" scoped>
.adjust-area{
  padding:20px 25px;
}
</style>

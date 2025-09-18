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
  <div class="">
    <div class="header-area">
     <div>  {{title}} </div>
    </div>
    <div class="body-area">
      <p v-html="content"></p>
    </div>
   
  </div>
</template>

<script lang="ts">

import { ref,defineComponent, onMounted }  from "vue";
import { getProtocolInfo } from '/@/api/protocol';
import { useRoute } from "vue-router";
import { useTitle as usePageTitle } from '@vueuse/core';

export default defineComponent({
  name:"Term",
  setup() {

    const content = ref('');
    const title = ref('');
    const pageTitle = usePageTitle();

    onMounted(async ()=>{
      const router = useRoute();
      console.log(router,'----fgf----')
      const type = router.params.type
      if(type=='serve'){
        title.value = '服务协议';
        pageTitle.value = '服务协议';
      }
      if(type=='privacy'){
        title.value = '隐私政策';
        pageTitle.value = '隐私政策';
      }
      if(type=='certificate'){
        title.value = '数字证书使用协议';
        pageTitle.value = '数字证书使用协议';
      }
      let result = await getProtocolInfo({type:type})
      if(result){
        content.value = result;
      }
    })
    return {
      content,
      title
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
  justify-content: center;
  align-items: center;
  box-shadow: 0 5px 8px 0 rgb(64 106 103 / 13%);
  background: hsla(0,0%,100%,.95);
  padding:0 20px;
  font-weight: 600;
  font-size:20px;

}
.body-area{
  margin-top:100px;
  padding:25px;
}
.term-body{
  width:1200px;
  margin:0 auto;

}
</style>

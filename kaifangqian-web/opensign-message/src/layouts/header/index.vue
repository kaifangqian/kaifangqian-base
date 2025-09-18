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
  <div class="header-container">
    <div class="header-line">
        <div class="title-name">   
            <!-- <Icon icon="ant-design:alert-outlined" size="40" color="#127fd2" /> -->
            <span>{{ headerName }}</span>
        </div>
      <div class="header-close" @click="handleClose"> 
        <Icon icon="ant-design:close-outlined"></Icon>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent, h} from "vue";
import Icon from '/@/components/Icon';
import { notification } from 'ant-design-vue';

export default defineComponent({
  name:"LayoutHeader",
  components:{
    Icon
  },
  props:{
    headerName:{
      type:String,
      default:'消息中心'
    }
  },
  setup(props) { 
    const headerName = ref(props.headerName)

    function handleClose(){
        window.close()
    }
    // const content1 =  h('span', {domProps: {innerHTML:'<p>fdsfdsfdsfds</p>'}})
    //       notification.open({
    //           duration: null,
    //           message: '温馨提示',
    //           description: () => {
            
    //               return  content1
    //         },
    //         });
    return {
         headerName,
         handleClose 
    }
  }
})
</script>

<style lang="less" scoped>
.header-container{
  height:60px;
  width:100%;
  display:flex;
  padding:0 20px;
  position:fixed;
  background:#fff;
  z-index:999;
  border-bottom:1px solid #d9d9d9;
  .header-line{
    width:100%;
    display:flex;
    font-size:18px;
    justify-content: space-between;
    align-items: center;
    font-weight:600;
    .header-close{
      cursor: pointer;
      font-size:14px;
      font-weight:500;
    }
    .title-name{
        display: flex;
        align-items: center;
    }
  }
}
</style>

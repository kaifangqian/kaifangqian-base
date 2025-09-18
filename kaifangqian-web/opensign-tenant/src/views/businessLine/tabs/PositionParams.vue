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
  <div class="businessline-position-container">
    <div class="auto-set-list">
      <!-- <img src="~@/assets/images/line-auto-set.png" alt=""> -->
      <div class="auto-set-item">
        <h2>一、预设签署流程</h2>
        <div>
          <p>  <a-badge status="default" />请先在【基本信息设置】-【签署方】中选择【经办人发起时，按照预设流程发起并签署】并设置签署方；</p>
        </div>
      </div>
      <div class="auto-set-item">
        <h2>二、添加签约文件</h2>
        <div>
          <p>  <a-badge status="default" />请先在【基本信息设置】-【签约文件】中上传文件或选择公共模板；</p>
        </div>
      </div>
      <div class="auto-set-item">
        <h2>三、位置及参数设置</h2>
        <div>
          <p>  <a-badge status="default" />点击【位置及参数设置】，进行签署方签署位置设置以及指定模板中各参数的填写方；</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {ref, defineComponent, computed} from "vue"
export default defineComponent({
  name:"PositionParams",
  props:{
    lineFormData:{
      type:Object
    }
  },
  setup(props,) {
    const positionForm = computed(()=>props.lineFormData);

    
    function getFormData(){
      return positionForm.value
    }
    return {
      getFormData  
    }
  }
})
</script>

<style lang="less" scoped>
.businessline-position-container{
  height:100%;
  position: relative;
}
.auto-set-list{
  position: absolute;
  left:50%;
  top:50%;
  transform: translate(-50%,-50%);
  text-align: center;
  img{
    display: inline-block;
    margin-bottom:20px;

  }
  h2{
    font-weight: 600;
  }
  div{
    margin-left:20px;
    text-align: left;
  }
}
</style>

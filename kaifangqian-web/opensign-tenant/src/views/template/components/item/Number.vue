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
  
  <div ref="input" :class="['control-'+element.type,'control-item','arow-content',
  	element.controlClick?'click':'default',
  	]"
    :style="[
      'transform:translate('+element.position.left+'px,'+element.position.top+'px)',
      'width:' + element.size.width + 'px',
      'height:' + element.size.height + 'px']"
    >
  	<InputNumber ref="inputRef" v-model:value="element.value"  @blur="inputBlur"
  		:style="['width:100%;height:100%',
  		'font-size:'+element.style.fontSize + 'px',
  		'text-align:' + element.style.textAlign,
  		'pointer-events: none;padding:0']"  :controls="false" :stringMode="true" class=""
  	 :placeholder="element.placeholder" :bordered="false"/>
  </div>
  
</template>

<script lang="ts" setup>
  import {ref} from 'vue';
  import {InputNumber} from 'ant-design-vue';
  const props = defineProps({
    element:{
    	type: Object,
      default:{}
    },
  });
  const emit = defineEmits(["change","blur"]);
  
  const inputRef = ref();
  
  // function valueChange(element:any){
  // 	if(element.required){
  // 		element.error = false;
  // 	}
  // }
  
  function inputBlur(){
  	// props.element.controlClick = false;
    emit('blur')
  }
  
  function itemFocus(){
    inputRef.value.focus();
  }
  
  
  defineExpose({
    itemFocus
  })
</script>

<style lang="less">
  
  .control-number .ant-input-number .ant-input-number-input-wrap,.ant-input-number-input{
    height:100%;
    text-align: inherit;
  }
  
  .control-number .ant-input-number-input{
    padding:0 !important;
  }
</style>

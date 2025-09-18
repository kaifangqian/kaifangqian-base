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
  
  <div ref="input" :class="['control-'+ element.type,'control-item','arow-content',
  	element.controlClick?'click':'default',]"
    :style="[
      'transform:translate('+element.position.left+'px,'+ element.position.top + 'px)',
      'width:' + element.size.width + 'px',
      'height:' + element.size.height + 'px']"
    >
  	<DatePicker ref="inputRef" v-model:value="element.value"
  	:placeholder="element.format" :format="element.format" :value-format="element.format"
  	class="control-date-picker"
  	 :style="['width:100%;height:100%; resize: none;',  
     'font-size:'+  element.style.fontSize + 'px', 
      'text-align:' + element.style.textAlign, 
      'fontFamily:' + element.style.fontFamily,
  	 dbclick?'':'pointer-events: none;'
  	 ]" :open="datePickerOpen"
  	 @blur="inputBlur" :bordered="false" @change="valueChange">
  	<template #suffixIcon>
  	</template>
  	</DatePicker>
  </div> 
</template>

<script lang="ts" setup>
  import {ref} from 'vue';
  import {DatePicker} from 'ant-design-vue';
  const props = defineProps({
    element:{
    	type: Object,
      default:{}
    },
  });
  const datePickerOpen = ref(false);
  const emit = defineEmits(["change","blur"]);
  
  const dbclick = ref(false);
  const inputRef = ref();
  
  // function valueChange(element:any){
  // 	if(element.required){
  // 		element.error = false;
  // 	}
  // }
  
  function inputBlur(){
  	// props.element.controlClick = false;
    dbclick.value = false;
    datePickerOpen.value = false;
    emit('blur')
  }
  
  function itemFocus(){
    dbclick.value = true;
    datePickerOpen.value = true;
    inputRef.value.focus();
  }
  
  function valueChange(){
     datePickerOpen.value = false;
  }
  
  defineExpose({
    itemFocus
  })
</script>

<style lang="less">
  .control-date .ant-picker .ant-picker-input,.ant-picker-input>input{
    text-align: inherit;
    font-size: inherit !important;
  }
</style>

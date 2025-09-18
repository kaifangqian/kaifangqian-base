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
  
  <div :class="['control-'+element.controlType,'control-item','arow-content','this-click',
  	element.controlClick?'click':'default',
  	]"
    :style="[
      'width:' + element.size.width + 'px',
      'height:' + element.size.height + 'px']"
      >
     <Select ref="inputRef" v-model:value="element.value" :bordered="false" :disabled="element.disabled"
     :placeholder="element.placeholder"
     :style="[
       'width:100%;height: 100%;',
       'font-size:'+ element.style.fontSize + 'px',
       'justify-content:' + element.style.textAlign,
       'fontFamily:' + element.style.fontFamily,
     ]" @blur="inputBlur"
     @change="inputBlur">
       <SelectOption  :value="item.uid" v-for="(item,indexOption) in element.widgetList" :key="indexOption">{{item.n}}</SelectOption>
     </Select>
  </div>
  
</template>

<script lang="ts" setup>
  import {ref,onMounted} from 'vue';
  import {Select,SelectOption } from 'ant-design-vue';
  import {uuid} from "/@/utils"
  const props = defineProps({
    element:{
    	type: Object,
      default:{}
    },
  });
  const emit = defineEmits(["change","blur"]);
  
  const inputRef = ref();
  const open = ref(false);
  // function valueChange(element:any){
  // 	if(element.required){
  // 		element.error = false;
  // 	}
  // }
  
  function inputBlur(){
  	// props.element.controlClick = false;
    open.value = false;
    emit('blur')
  }
  
  function itemFocus(){
    open.value = true;
    if(inputRef.value)
    inputRef.value.focus();
  }
  
  function init(){
    
    const widgetList = [
      {
        n:"选项1",
        v:false,
        uid:uuid()
      },
      {
        n:"选项2",
        v:false,
        uid:uuid()
      }
    ]
    props.element.widgetList = widgetList;
  }
  
  onMounted(()=>{
    // init();
  })
  
  
  defineExpose({
    itemFocus
  })
</script>

<style lang="less" scoped>
  .control-dropdown-list .ant-select .ant-select-selector,.ant-select-selector .ant-select-selection-item  {
    height: 100%;
    justify-content:inherit;
  }
  :deep(.ant-select-selection-search){
    input{
      position:absolute;
      z-index:-1;
    }
  }
  
  
  .control-dropdown-list .ant-select .ant-select-selector .ant-select-selection-item{
    display: flex;
    // justify-content: center;
    align-items: center;
    font-size: inherit !important;
  }
  :deep(.ant-select-selector){
    padding:0;
  }
</style>

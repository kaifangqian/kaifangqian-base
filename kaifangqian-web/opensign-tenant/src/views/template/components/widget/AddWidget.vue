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
  <div class="add-widget">
    <Button type="primary" size="small" style="width: 100%;" @click="addWidget">新增选项</Button>
  </div>
</template>

<script lang="ts" setup>
  import {Button} from 'ant-design-vue';
  import {ControlType} from '../dist/ControlData';
  import {uuid} from "/@/utils"
  import { CanvasZoom } from '../dist/ControlData';
  
  
  const props = defineProps({
    element:{
      type:Object,
      default:{}
    },
    doc:{
      type:Object,
      default:{}
    },
  });
  
  function addWidget(){
    if(props.element.type == ControlType.Radio ||props.element.type == ControlType.CheckBox){
      addRadio();
    }
    if(props.element.type == ControlType.DropdownList){
      addDropdownItem();
    }
  }
  function addDropdownItem(){
    var lastIndex = 0;
    props.element.widgetList.forEach((item,index)=>{
      lastIndex = index + 1;
    })
    const widget = {
      uid:uuid(),
      n:"选项"+(lastIndex+1),
      v:false
    }
    props.element.widgetList.push(widget);
  }
  //单选和多选的属性相同  新增方法可以服用
  function addRadio(){
    var lastIndex = 0;
    props.element.widgetList.forEach((item,index)=>{
      lastIndex = index + 1;
    })
    const widget = {
      x:props.element.position.left,
      y:props.element.position.top + props.element.size.height,
      w:props.element.widgetList[0].w,
      h:props.element.widgetList[0].h,
      uid:uuid(),
      n:"选项"+(lastIndex+1),
      v:false
    }
    
    // console.log(JSON.stringify(widget));
    // props.element.widgetList.push(widget);
    // props.element.size.height = props.element.size.height + props.element.widgetList[0].w;
    //计算可X轴可移动的范围
    const target = props.doc.targets.find(item=>item.page == props.element.position.page);
    const startY = target.transformHeight + CanvasZoom.space * (props.element.position.page+1);
    const endY = startY + target.height - props.element.widgetList[0].h;
    if(widget.y >= endY){
      widget.y = endY;
    }else{
      props.element.size.height = props.element.size.height + props.element.widgetList[0].h;
    }
    props.element.widgetList.push(widget);
  }
  
</script>

<style lang="less" scoped>
  .add-widget{
    display: flex;
    justify-content: center;
    padding: 8px 35px;
  }
</style>

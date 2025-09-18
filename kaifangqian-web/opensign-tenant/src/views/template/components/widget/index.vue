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
  <draggable :list="element.widgetList" itemKey="uid" :sort="true" handle=".mover" :animation="300" :touchStartThreshold="5">
    <template #item="{ element }">
      <div class="resrun-widget">
        <div class="item-order mover">
          <!-- control-drag.svg -->
          <!-- <Icon size="18" icon="ant-design:ordered-list-outlined"/> -->
          <SvgIcon name="control-drag" size="18"></SvgIcon>
        </div>
        <div class="item-name">
          <Input  v-model:value="element.n" />
        </div>
        <div class="item-del">
           <a-button type="text" size="small" @click="deleteWidget(element)">
              <template #icon>
                <Icon size="18" icon="ant-design:delete-outlined"/>
              </template>
            </a-button>
        </div>
      </div>
    </template>
  </draggable>
</template>

<script lang="ts" setup>
  import {ref} from 'vue';
  import {Input,Checkbox,Radio,RadioGroup,CheckboxGroup} from 'ant-design-vue';
  import {getMoveScoped} from "../dist/ControlerMoveRange"
  import {Icon,SvgIcon} from '/@/components/Icon'; 
  import {ControlType} from '../dist/ControlData';
  import draggable from 'vuedraggable'
  
  const props = defineProps({
    widget:{
    	type: Object,
      default:{}
    },
    element:{
      type:Object,
      default:{}
    },
  });
  
  const emit = defineEmits(["delControl"]);
  
  function deleteWidget(widget){
    if(props.element.widgetList.length == 1){
      emit('delControl')
      return;
    }
    if(props.element.type == ControlType.Radio || props.element.type == ControlType.CheckBox){
      deleteRadio(widget);
    }
    if(props.element.type == ControlType.DropdownList){
      delDropdownItem(widget);
    }
  }
  
  function delDropdownItem(widget){
    props.element.widgetList = props.element.widgetList.filter(item=>item.uid != widget.uid );
  }
  //const controlSize = 32;
  function deleteRadio(widget){
    const scoped1 = getMoveScoped(props.element);
    props.element.widgetList = props.element.widgetList.filter(item=>item.uid != widget.uid );
    const scoped2 = getMoveScoped(props.element);
    
    //重新计算外部空间的大小
    
    if(scoped1.minY == scoped2.minY){
      props.element.size.height = scoped2.maxY - scoped2.minY + widget.w;
    }else{
      props.element.size.height = props.element.size.height - (scoped2.minY - scoped1.minY);
      props.element.position.top = props.element.position.top + (scoped2.minY - scoped1.minY);
    }
    if(scoped1.minX == scoped2.minX){
      props.element.size.width = scoped2.maxX - scoped2.minX + widget.w;
    }else{
      props.element.size.width = props.element.size.width - (scoped2.minX - scoped1.minX);
      props.element.position.left = props.element.position.left + (scoped2.minX - scoped1.minX);
    }
    
    //如果只剩了一个   外部大小于子控件大小位置一致
    if(props.element.widgetList.length == 1){
      props.element.size.width = widget.w;
      props.element.size.height = widget.w;
      props.element.widgetList[0].x = props.element.position.left;
      props.element.widgetList[0].y = props.element.position.top;
    }
  }
  
</script>

<style lang="less" scoped>
  .resrun-widget{
    display: flex;
    height: 40px;
    .item-order,.item-del{
      width: 30px;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items:center;
    }
    .item-order{
      cursor: move;
    }
    .item-name{
      flex: 1;
      padding: 5px;
      box-sizing: content-box;
    }
  }
  .resrun-widget:hover,.resrun-widget:active{
      background-color: rgba(0, 0, 0,0.06) !important;
  }
</style>

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
    <div>
      <RadioGroup v-model:value="element.values" style="display:inline">
       <template v-for="(item,index) in element.widgetList">
         <div class="radio-item-box" :style="[
           'transform:translate('+item.x+'px,'+item.y+'px)',
           cursorStyle]"
           @mousedown.salf="itemMovemousedown($event,item)" >
           <Radio :style="radioStyle" :value="item.uid" ></Radio>
           <span class="delete" :class="element.controlClick?'active':''">
             <!-- <CloseCircleOutlined style="font-size: 18px;"/> -->
             <Icon @click="deleteWidget(item)" color="red" size="18" icon="ant-design:close-circle-outlined"/>
           </span>
         </div>
       </template>
      </RadioGroup>
      <div class="control-operation" :style="[
        'transform:translate('+element.position.left+'px,'+(element.position.top + element.size.height)+'px)',
        'width:' + element.size.width + 'px',
      ]">
        <Icon @click="addWidget" size="25" icon="ant-design:plus-square-filled"/>
      </div>
    </div>
</template>

<script lang="ts" setup>
  import {ref,reactive,onMounted} from 'vue';
  import {RadioGroup,Radio} from 'ant-design-vue';
  import {Icon,SvgIcon} from '/@/components/Icon';  
  
  import {moveRange,currentMoveRange,currentPosition,currentPositionReverse,getMoveScoped} from "../dist/ControlerMoveRange"
  import {bubbleSort,uuid} from "/@/utils"
  import { CanvasZoom } from '../dist/ControlData';
  import {retainDecimals} from "/@/utils"
  
  const controlSize = 32;
  
  const props = defineProps({
    element:{
    	type: Object,
      default:{}
    },
    docPageSize:{
      type:Number
    },
    doc:{
      type:Object,
      default:{}
    },
  });
  
  
  const emit = defineEmits(["change","blur","delControl"]);
  
  const cursorStyle = ref("cursor:move;");
  
  const radioStyle = reactive({
    "margin":"0",
    "z-inde":"-1"
  });
  
  const nowPoint = ref({
  	x: 0,
  	y:0
  })
  const eventMove = ref({
  	x:0,
  	y:0
  });
  const moveInterval = ref({
      startX:0,
      startY:0,
      endX:0,
      endY:0
    });
  const moveScoped = ref();
  const nowElementSub = ref<any>();
  const inputRef = ref();
  
  function inputBlur(){
    emit('blur')
  }
  
  function itemFocus(){
    // inputRef.value.focus();
  }
  
  function deleteWidget(widget){
    console.log("deleteWidget");
    if(props.element.widgetList.length == 1){
      emit('delControl')
      return;
    }
    const scoped1 = getMoveScoped(props.element);
    props.element.widgetList = props.element.widgetList.filter(item=>item.uid != widget.uid );
    const scoped2 = getMoveScoped(props.element);
    
    //重新计算外部空间的大小
    
    if(scoped1.minY == scoped2.minY){
      props.element.size.height = scoped2.maxY - scoped2.minY + controlSize;
    }else{
      props.element.size.height = props.element.size.height - (scoped2.minY - scoped1.minY);
      props.element.position.top = props.element.position.top + (scoped2.minY - scoped1.minY);
    }
    if(scoped1.minX == scoped2.minX){
      props.element.size.width = scoped2.maxX - scoped2.minX + controlSize;
    }else{
      props.element.size.width = props.element.size.width - (scoped2.minX - scoped1.minX);
      props.element.position.left = props.element.position.left + (scoped2.minX - scoped1.minX);
    }
    
    //如果只剩了一个   外部大小于子控件大小位置一致
    if(props.element.widgetList.length == 1){
      props.element.size.width = controlSize;
      props.element.size.height = controlSize;
      props.element.widgetList[0].x = props.element.position.left;
      props.element.widgetList[0].y = props.element.position.top;
    }
  }
  
  function resetWidget(){
    
  }
  
  function addWidget(){
    var lastIndex = 0;
    props.element.widgetList.forEach((item,index)=>{
      lastIndex = index + 1;
    })
    var widget = {
      x:props.element.position.left,
      y:props.element.position.top + props.element.size.height,
      w:controlSize,
      h:controlSize,
      uid:uuid(),
      n:"选项"+(lastIndex + 1),
      v:false
    }
    
    
    //计算可X轴可移动的范围
    const target = props.doc.targets.find(item=>item.page == props.element.position.page);
    const startY = target.transformHeight + CanvasZoom.space * (props.element.position.page+1);
    const endY = startY + target.height - controlSize;
    if(widget.y >= endY){
      widget.y = endY;
    }else{
      props.element.size.height = props.element.size.height + controlSize;
    }
    props.element.widgetList.push(widget);
    
  }
  
  function itemMovemousedown(e:any,elementSub:any){
    
    props.element.controlClick = true;
    nowElementSub.value = elementSub;
    elementSub.click = true;
    props.element.move = false;
  	nowPoint.value = {
  		x:e.clientX,
  		y:e.clientY
  	}
    
    //计算可X轴可移动的范围
    const target = props.doc.targets.find(item=>item.page == props.element.position.page);
    const startY = target.transformHeight + CanvasZoom.space * (props.element.position.page+1);
    const endY = startY + target.height - controlSize;
    
    const startX = (props.doc.maxWidth - target.width) / 2;
    const endX = props.doc.maxWidth - ((props.doc.maxWidth - target.width )/ 2) - controlSize;
    moveInterval.value.startX = retainDecimals(startX,2);
    moveInterval.value.endX = retainDecimals(endX);
    moveInterval.value.startY = retainDecimals(startY);
    moveInterval.value.endY = retainDecimals(endY);
        
    moveScoped.value = getMoveScoped(props.element);
    console.log("itemMovemousedown",moveScoped.value,moveInterval.value);
    
  	//offsetX:e.offsetX,
  	//offsetY:e.offsetY,
  	document.addEventListener('mousemove', moveControl)
  	//松开鼠标清除移动函数事件
  	document.addEventListener('mouseup',
  	function () {
  		document.removeEventListener('mousemove', moveControl)
  		removeMousedown();
  	},
  	{
  	// 只绑定一次事件
  	once: true,
  	})
  }
  
  
  function moveControl(e:any){
  	//当前鼠标位置
  	var nowPos = {
  	  x: e.clientX,
  	  y: e.clientY,
  	}
  	//计算鼠标移动过的距离
  	var dis = {
  	  x: nowPos.x - nowPoint.value.x,
  	  y: nowPos.y - nowPoint.value.y,
  	}
  	nowPoint.value = nowPos;
  	
    //子控件移动完成赋值
    nowElementSub.value.x += dis.x ;
    nowElementSub.value.y += dis.y;
    
    //只有一个单选的时候  不需要计算，直接跟随子元素移动
    if(props.element.widgetList.length <= 1){
      props.element.position.top +=  dis.y ;
      props.element.position.left +=  dis.x ;
      return;
    }
    
    //判断父控件范围
    if(dis.y>0){
      // console.log("回去",dis.y);
      if(nowElementSub.value.y > moveScoped.value.maxY){
        props.element.size.height += nowElementSub.value.y - moveScoped.value.maxY ;
      }
      
      if(nowElementSub.value.y > moveScoped.value.minY && nowElementSub.value.y < moveScoped.value.minY2){
        props.element.size.height -= nowElementSub.value.y - moveScoped.value.minY ;
        props.element.position.top += nowElementSub.value.y - moveScoped.value.minY ;
      }
      
      if(nowElementSub.value.y > moveScoped.value.minY && nowElementSub.value.y >= moveScoped.value.minY2){
        if(nowElementSub.value.y - dis.y < moveScoped.value.minY2){
          props.element.size.height -= moveScoped.value.minY2 - moveScoped.value.minY ;
           props.element.position.top += moveScoped.value.minY2 - moveScoped.value.minY ;
        }
      }
    }else if(dis.y<0){
      // console.log("开始",dis.y);
      if(nowElementSub.value.y < moveScoped.value.maxY && nowElementSub.value.y >= moveScoped.value.maxY2){
        props.element.size.height += nowElementSub.value.y - moveScoped.value.maxY ;
      }
      if(nowElementSub.value.y < moveScoped.value.maxY && nowElementSub.value.y < moveScoped.value.maxY2){
        if(nowElementSub.value.y - dis.y > moveScoped.value.maxY2){
          props.element.size.height += moveScoped.value.maxY2 - moveScoped.value.maxY ;
        }
      }
      
      if(nowElementSub.value.y < moveScoped.value.minY && nowElementSub.value.y <= moveScoped.value.minY2){
        props.element.size.height -= nowElementSub.value.y - moveScoped.value.minY ;
        props.element.position.top += nowElementSub.value.y - moveScoped.value.minY ;
      }
    }
    
    if(dis.x>0){
      // console.log("回去",dis.y);
      if(nowElementSub.value.x > moveScoped.value.maxX){
        props.element.size.width += nowElementSub.value.x - moveScoped.value.maxX ;
      }
      
      if(nowElementSub.value.x > moveScoped.value.minX && nowElementSub.value.x < moveScoped.value.minX2){
        props.element.size.width -= nowElementSub.value.x - moveScoped.value.minX ;
        props.element.position.left += nowElementSub.value.x - moveScoped.value.minX ;
      }
      
      if(nowElementSub.value.x > moveScoped.value.minX && nowElementSub.value.x >= moveScoped.value.minX2){
        if(nowElementSub.value.x - dis.x < moveScoped.value.minX2){
          props.element.size.width -= moveScoped.value.minX2 - moveScoped.value.minX ;
           props.element.position.left += moveScoped.value.minX2 - moveScoped.value.minX ;
        }
      }
    }else if(dis.x<0){
      if(nowElementSub.value.x < moveScoped.value.maxX && nowElementSub.value.x >= moveScoped.value.maxX2){
        props.element.size.width += nowElementSub.value.x - moveScoped.value.maxX ;
      }
      if(nowElementSub.value.x < moveScoped.value.maxX && nowElementSub.value.x < moveScoped.value.maxX2){
        if(nowElementSub.value.x - dis.x > moveScoped.value.maxX2){
          props.element.size.width += moveScoped.value.maxX2 - moveScoped.value.maxX ;
        }
      }
      
      if(nowElementSub.value.x < moveScoped.value.minX && nowElementSub.value.x <= moveScoped.value.minX2){
        props.element.size.width -= nowElementSub.value.x - moveScoped.value.minX ;
        props.element.position.left += nowElementSub.value.x - moveScoped.value.minX ;
      }
    }
    moveScoped.value = getMoveScoped(props.element);
  }
  
  
  function removeMousedown(){
    console.log("props.element.position",props.element.position);
  	let x = nowElementSub.value.x;
  	let y = nowElementSub.value.y;
  	// let x = props.element.position!.left! + eventMove.value.x;
  	// let y = props.element.position!.top! + eventMove.value.y;
  	const optItem:any = {
  		x:x,
  		y:y,
  		pageSize:props.docPageSize,
  		size:{
        width: nowElementSub.value.w,
        height: nowElementSub.value.h,
      },
  		currentPage:props.element.position.page,
      maxWidth:props.doc.maxWidth
  	};
  	currentMoveRange(optItem,props.doc.targets);
  	nowElementSub.value.x =optItem.x;
  	nowElementSub.value.y = optItem.y;
  	nowElementSub.value.p = optItem.currentPage;
    
    
    const scoped = getMoveScoped(props.element);
    // const opt:any = {
    // 	x:props.element.position!.left,
    // 	y:props.element.position!.top,
    // 	pageSize:props.docPageSize,
    // 	size:{
    //     width: props.element.size.width,
    //     height: props.element.size.height,
    //   },
    // 	currentPage:0,
    // };
    // moveRange(opt);
    // props.element.size.width =opt.x;
    // props.element.size.height = opt.y;
    // props.element.size.width = opt.currentPage;
    
    // props.element.position.left =opt.x;
    // props.element.position.top = opt.y;
    // props.element.position.page = opt.currentPage;
    // debugger;
    console.log("removeMousedown:",scoped.minX , moveInterval.value.startX);
    if(scoped.maxX - scoped.minX + controlSize != props.element.size.width){
      if(scoped.minX <= moveInterval.value.startX){
        props.element.position.left = moveInterval.value.startX;
      }
      props.element.size.width = scoped.maxX - scoped.minX + controlSize;
    }
    if(scoped.maxY - scoped.minY + controlSize != props.element.size.height){
      // const startHeight = CanvasZoom.height * optItem.currentPage + CanvasZoom.space * (optItem.currentPage+1);
      if(scoped.minY == moveInterval.value.startY){
        props.element.position.top = moveInterval.value.startY;
      }
      props.element.size.height = scoped.maxY - scoped.minY + controlSize;
    }
    props.element.move = true;
  }
  
  function init(){
    
    const widgetList = [
      {
        x:0,
        y:0,
        w:controlSize,
        h:controlSize,
        n:"选项1",
        v:false,
        uid:uuid()
      },
      {
        x:0,
        y:0,
        w:controlSize,
        h:controlSize,
        n:"选项2",
        v:false,
        uid:uuid()
      }
    ]
    props.element.widgetList = widgetList;
    const control =props.element;
    props.element.widgetList.forEach((sub,index)=>{
      sub.x = control.position.left;
      sub.y = control.position.top + index * sub.h ;
    })
    props.element.size.height = props.element.widgetList.length * props.element.widgetList[0].h;
  }
  
  onMounted(()=>{
    // init();
  })
  // init();
  defineExpose({
    itemFocus,
    init,
    controlSize
  })
</script>

<style lang="less" scoped>
  .radio-item-box{
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px;
    border-radius: 2px;
    border-style: solid;
    border-color: var(--translucent);
    position: absolute;
    z-index:2;
    .delete{
      display: none;
      position: absolute;    
      top: -12px;
      right: -13px;
      cursor: pointer;
      z-index:999;
    }
  }
  .radio-item-box:hover{
    .active{
      display: flex;
    }
  }
  .control-operation{
    position: absolute;
    color:#127fd2;
    text-align: center;
  }
</style>

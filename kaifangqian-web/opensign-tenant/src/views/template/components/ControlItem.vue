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
  <span :class="['this-click']" :style="[
    '--primary:'+ReceiveColorItem.primary,
    '--translucent:'+ [element.error?'red':ReceiveColorItem.translucent],
    '--background:'+ReceiveColorItem.background,
    '--background-rgb:'+ReceiveColorItem.backgroundRgb
  ]">
    <!--控件外层骨架-->
      <!-- 'left:'+element.position.left+'px;top:'+element.position.top+'px;', -->
		<div :class="['resrun-control','resrun-control-move','this-click',element.controlClick?'click':'default']"
		:style="[
      'transform:translate('+element.position.left+'px,'+element.position.top+'px)',
		
		'width:' + element.size.width + 'px',
		'height:' + element.size.height + 'px',
		cursorStyle]"
		@mousedown="controlMovemousedown($event,element)"
		@mouseenter.stop="controlMouseover"
		@mouseleave="mouseoverMouseout"
		@dblclick="formItemFocus" 
		> 
    <div class="control-handles">
    	<div class="handle handle-top-left receive-color this-click"
    	v-on:mousedown.salf="controlZoomMousedown($event,element,1)"
    	v-if="element.controlClick && element.zoom"
    	></div>
    	<template v-if="!isSign">
    		<div class="closeControl handle-top-right receive-color this-click"
    			v-on:click.stop="controlDelete" v-if="element.controlClick && !element.disabled">
    			<Icon color="red" size="18" icon="ant-design:close-circle-outlined"/>
    		</div>
    	</template>
    	<template v-else>
    		<div class="closeControl handle-top-right receive-color this-click"
    			v-on:click.stop="clearSeal" v-if="element.setting && !element.close">
    			<Icon color="red" size="18" icon="ant-design:close-circle-outlined"/>
    		</div>
    	</template>
    
    	
    	<div class="handle handle-bottom-left receive-color this-click"
    	v-on:mousedown.salf="controlZoomMousedown($event,element,2)"
    	v-if="element.controlClick && element.zoom"
    	></div>
    	<div class="handle handle-bottom-right receive-color this-click"
    	v-on:mousedown.salf="controlZoomMousedown($event,element,3)"
    	v-if="element.controlClick && element.zoom"
    	></div>
    </div>
    <div class="control-required" v-if="element.isRequired">
    	*
    </div>
  </div>
  <ControlPage v-if="ControlPage" :docPageSize="doc.pageSize" :element="element" :doc="doc"
      @blur="inputBlur" @delControl="controlDelete" ref="controlRef"/>  
  </span>
  
</template>

<script lang="ts" setup>
  // import type { PropType } from 'vue';
	import {defineAsyncComponent,ref} from 'vue';
	// import {Input,InputNumber,DatePicker,Popconfirm,InputSearch} from 'ant-design-vue';
	import {getColor} from "./dist/ReceiveColor" 
	import {Icon} from '/@/components/Icon';
	// import dayjs from 'dayjs';
	import {ControlType,CanvasZoom} from "./dist/ControlData"
  
	import {moveRange,currentPositionReverse,currentPage} from "./dist/ControlerMoveRange"
	// import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';
	
  
  const props = defineProps({
    element:{
    	type: Object,
      default:{}
    },
    doc:{
    	type:Object,
      default:{}
    },
    isSign:{
    	type:Boolean,
    	default:false
    }
  });
  const emit = defineEmits(["controlMousedown","controlDelete","clearSeal"],);
			const cursorStyle = ref("cursor:move;");
			const zoomType = ref(0);
			const dbclick = ref(false);
			
      const controlRef = ref();
			const nowPoint = ref({
				x: 0,
				y:0
			})
			function controlDelete(e:any){
				emit('controlDelete',props.element)
			}
			function clearSeal(){
				emit('clearSeal',props.element)
			}
			function controlMovemousedown(e:any,element:any,falg:boolean){
        // return;
				props.element.controlClick = true;
        console.log(element);
				emit('controlMousedown',element)
        if(element.disabled){
        	return;
        }
				if(!element.move){
					return;
				}
        
				nowPoint.value = {
					x:e.clientX,
					y:e.clientY
				}
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
				// eventMove.value.x = dis.x;
				// eventMove.value.y = dis.y;
				props.element.position!.left += dis.x ;
				props.element.position!.top += dis.y;
        
        
        if(props.element.widgetList && props.element.widgetList.length>0){
          props.element.widgetList.forEach((sub)=>{
            sub.x += dis.x;
            sub.y += dis.y;
          })
        }
			}
			function removeMousedown(){
				let x = props.element.position!.left!;
				let y = props.element.position!.top!;
				const opt:any = {
					x:x,
					y:y,
					pageSize:props.doc!.pageSize,
					size:props.element.size,
					currentPage:props.element.position.page,
          //新增下列三个属性
          //页面总高度
          allHeight:props.doc.imageAllHeight,
          //所有页面最大的宽度
          maxWidth: props.doc.maxWidth,
          
				};
        console.log("removeMousedown old:",JSON.stringify(opt));
        moveRange(opt,props.doc.targets);
        console.log("removeMousedown new:",JSON.stringify(opt));
				props.element.position.left =opt.x;
				props.element.position.top = opt.y;
				props.element.position.page = opt.currentPage;
        
        
        if(props.element.widgetList && props.element.widgetList.length>0){
          props.element.widgetList.forEach((sub)=>{
            sub.x -= x  - opt.x
            sub.y -= y - opt.y;
          })
        }
       
        
			}
			
			function controlZoomMousedown(e:any,element:any,type:number){
				//移动方位不一致，位置计算也不相同  通过type区分
				zoomType.value = type;
				//点击当前坐标点
				nowPoint.value = {
					x:e.clientX,
					y:e.clientY
				}
				//绑定移动事件
				document.addEventListener('mousemove', zoomControl)
				//松开鼠标清除移动函数事件  
				document.addEventListener('mouseup',
				function () {
					//还原让控件可拖动
					document.removeEventListener('mousemove', zoomControl)
				},
				{
				// 只绑定一次事件
				once: true,
				})
			}
			function zoomControl(e:any){
				e.stopPropagation();
				//移动到新的坐标点
				var nowPos = {
				  x: e.clientX,
				  y: e.clientY,
				}
				//计算鼠标移动过的距离
				var dis = {
				  x: nowPos.x - nowPoint.value.x,
				  y: nowPos.y - nowPoint.value.y,
				}
				//将旧的坐标更换成新的坐标
				nowPoint.value = nowPos;
				const nowPon:any = {
					width:0,
					height:0,
					left:0,
					top:0
				}
				if(zoomType.value == 3){
					//横纵坐标 直接+
					nowPon.width = props.element.size!.width + dis.x;
					nowPon.height = props.element.size!.height + dis.y;
					nowPon.top = props.element.position.top;
					nowPon.left = props.element.position.left;
				}
				if(zoomType.value == 2){
					nowPon.width = props.element.size!.width + dis.x * -1;
					nowPon.height = props.element.size!.height + dis.y;
					nowPon.left = props.element.position!.left! + dis.x;
					nowPon.top = props.element.position.top;
				}
				if(zoomType.value == 1){
					nowPon.width = props.element.size!.width + dis.x * -1;
					nowPon.left = props.element.position.left! + dis.x;
					nowPon.height = props.element.size!.height + dis.y * -1;
					nowPon.top = props.element.position.top! + dis.y;
				}
				if(nowPon.width < props.element.size!.minWidth){
					nowPon.width = props.element.size!.minWidth
					nowPon.left = props.element.position.left;
				}
				
				if(nowPon.height < props.element.size!.minHeight){
					nowPon.height = props.element.size!.minHeight
					nowPon.top = props.element.position.top;
				}
				if(nowPon.left<0){
					nowPon.left = 0;
				}
				if(nowPon.top<0){
					nowPon.top = 0;
				}
				const target = props.doc.targets[props.element.position.page];
				if(nowPon.width>target.width){
					nowPon.width =target.width;
				}
				if(nowPon.height>target.height){
					nowPon.height =target.height;
				}
				props.element.size!.width = nowPon.width
				props.element.size!.height = nowPon.height;
				props.element.position.left = nowPon.left;
				props.element.position.top = nowPon.top;
				
			}
			function formItemFocus (){
				// if(props.element.disabled){
				// 	return;
				// }
				if(props.element.controlOperationType !== 'sign'){
					dbclick.value = true;
          cursorStyle.value = "cursor:text;";
          controlRef.value.itemFocus();
				}
        console.log("formItemFocus");
			}
			function inputBlur(){
				cursorStyle.value = "cursor:move;";
				// props.element.controlClick = false;
				// if(datePicker.value){
				// 	datePickerOpen.value = false;
				// }
				// dbclick.value = false;
			}
			
			const ReceiveColorItem:any = ref(null);
		 
			if(!ReceiveColorItem.value){
				ReceiveColorItem.value = getColor(-2,null)
			}
			
		 
			const moveIn = ref(false);
			const moveTimeout:any = ref(null);
			function controlMouseover(e:any){
				//防止鼠标快速划过控件显示
				moveTimeout.value = setTimeout(function(){
					moveIn.value = true;
				},100)
			}
			function mouseoverMouseout(e:any){
				if(!moveIn.value){
					clearTimeout(moveTimeout.value)
				}
				moveIn.value = false;
			}
			function settingSeal(e:any){
				props.element.showModal = true;
			}
			function initData(){
				if(props.element.disabled){
					cursorStyle.value = "cursor: not-allowed;";
				}else{
					if(props.element.controlOperationType == 'sign'){
						cursorStyle.value = "cursor: pointer;";
					}else{
						cursorStyle.value = "cursor: move;";
					}
				}
				//日期格式回显需要特殊处理
				// if(props.element.type == ControlType.Date){
				// 	if(props.element.value && props.element.value != ''){
				// 		//props.element.value = dayjs(props.element.value);
				// 	}
				// }
				// props.element.position.top = currentPositionReverse(props.element.position.top!,props.element.position.page!);
				//import dayjs from 'dayjs';
				//cursor: pointer;
			}
			initData();
      var ControlPage:any  = null;
      //动态加载控件
      if(props.element.type == ControlType.Radio){
        console.log("Radio:",props.element);
        ControlPage  = defineAsyncComponent(() =>import('./item/Radio.vue'))
      }else if(props.element.type == ControlType.LineText){
        ControlPage  = defineAsyncComponent(() =>import('./item/LineText.vue'))
      }else if(props.element.type == ControlType.MultilineText){
        ControlPage  = defineAsyncComponent(() =>import('./item/MultilineText.vue'))
      }else if(props.element.type == ControlType.Number){
        ControlPage  = defineAsyncComponent(() =>import('./item/Number.vue'))
      }else if(props.element.type == ControlType.Date){
        ControlPage  = defineAsyncComponent(() =>import('./item/Date.vue'))
      }else if(props.element.type == ControlType.CheckBox){
        ControlPage  = defineAsyncComponent(() =>import('./item/CheckBox.vue'))
      }else if(props.element.type == ControlType.DropdownList){
        ControlPage  = defineAsyncComponent(() =>import('./item/DropdownList.vue'))
      }else if(props.element.type == ControlType.Image){
        ControlPage  = defineAsyncComponent(() =>import('./item/Image.vue'))
      }
</script>

<style lang="less" scoped>
	
	.resrun-control{
		user-select: none;
		cursor: move;
		position: absolute;
    z-index:2;
	}
	.resrun-control :hover{
		
	}
	.signer-tips{
		width: 100%;
		border: 1px solid #e3e3e3;
		background-color: #f8f8f8;
		position: absolute;
		top: calc(100% + 10px);
		text-align: center;
	}
	.control-required{
		position: absolute;
		height: 100%;
		left:-10px;
		top:0;
		line-height: 100%;
		display: flex;
		align-items: center;
		color:red
	}
	.control-item{
		position: absolute;
    z-index:1;
	}
	.setting-sign{
		position: absolute;
		bottom: 0;
		width: 100%;
		text-align: center;
		line-height: 30px;
		background-color: #eee;
	}
	.control-item.default{
		/* width:100%;
		height: 100%; */
		/* border: 1px;
		border-style: dashed;
		border-radius: 1px;
		border-color:var(--translucent);
		background-color: rgb(var(--background-rgb),0.5) */
	}
  .resrun-control{
    border: 1px;
    border-style: dashed;
    border-radius: 1px;
    border-color:var(--translucent);
    background-color: rgb(var(--background-rgb),0.5)
  }
  .resrun-control.click{
    border: 1px;
    border-radius: 2px;
    border-style: solid;
    border-color:var(--translucent);
    background-color: rgba(255,255,255,0.3);
    
    border-style: solid !important;
  }
	.control-item.click{
		/* width:100%;
		height: 100%; */
		/* border: 1px;
		border-radius: 2px;
		border-style: solid;
		border-color:var(--translucent);
		background-color: #fff; */
	}
	.control-item label{
		color: #a9a9a9;
		padding-left: 5px;
	}
	.control-handles{
		.handle{
			width: 5px;
			height: 5px;
			background-color: var(--primary);
			position: absolute;
		}
		.closeControl{
			width: 18px;
			height: 18px;
			position: absolute;
			font-size: 20px;
			cursor: default;
			background-color: #fff;
			display: flex;
			border-radius: 50%;
		}
		.handle-top-left{
			top:-3px;
			left: -3px;
			cursor: nw-resize;
		}
		.handle-top-right{
			top:-12px;
			right: -13px;
		}
		.handle-bottom-left{
			bottom:-3px;
			left: -3px;
			cursor: sw-resize;
		}
		.handle-bottom-right{
			bottom :-3px;
			right: -3px;
			cursor: se-resize;
		}
	}
	.control-signature{
		display: flex;
		flex-direction: column;
		text-align: center;
		align-items: center;
		span{
			pointer-events: none;
			display: block;
		}
		span:nth-child(1){
			//line-height: 30px;
			height: 40px;
			padding-top: 5px;
		}
	}
	.control-seal{
		display: flex;
		flex-direction: column;
		text-align: center;
		justify-content: center;
		span{
			display: block;
			pointer-events: none;
		}
		span:nth-child(1){
			height: 40px;
			padding-top: 5px;
		}
		span:nth-child(2){
			line-height: 30px;
		}
	}
	.seal-modal{
		width: 200px;
		.seal-modal-title{
			font-weight: 600;
			font-size: 14px;
			line-height: 30px;
		}
		.seal-item{
			border-style: dashed;
			border-color: #e0e0e0;
			border-width: 1px;
			padding: 20px 20px 0 20px;
			margin-bottom: 20px;
			cursor: pointer;
			.seal-img{
				width: 160px;
				height: 160px;
			}
			.seal-name{
				line-height: 30px;
				text-align: center;
			}
		}
		.seal-item:hover{
			border-color:#5589E2
		}
	}
	.control-sign-date,.control-date{
		display: flex;
		flex-direction: column;
		text-align: center;
		justify-content: center;
		span{
			line-height: 30px;
			pointer-events: none;
		}
	}
</style>
<style>
	.seal-popover .ant-popover-message-title{
		padding-left: 0px !important;
	}
</style>

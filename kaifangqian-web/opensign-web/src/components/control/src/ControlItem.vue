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
	<span :class="['resrun-control','resrun-control-move','this-click']"
	:style="['left:'+element.position.left+'px;top:'+element.position.top+'px;',
	'--primary:'+ReceiveColorItem.primary,
	'--translucent:'+ReceiveColorItem.translucent,
	'--background:'+ReceiveColorItem.background,
	'--background-rgb:'+ReceiveColorItem.backgroundRgb,
	'width:' + element.size.width + 'px',
	'height:' + element.size.height + 'px',
	cursorStyle]"
	@mousedown="controlMovemousedown($event,element)"
	@mouseenter.stop="controlMouseover"
	@mouseleave="mouseoverMouseout"
	@dblclick="formItemFocus" 
	>
		<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
			element.controlClick?'click':'default',
			]"
			v-if="element.type === ControlType.LineText">
			<Input ref="lineInput" v-model:value="element.value"  @blur="inputBlur" 
				:style="['width:100%;height:100%',
				'font-size:'+element.style.fontSize + 'px',
				'text-align:' + element.style.textAlign,
				'fontFamily:' + element.style.fontFamily,
				'pointer-events: none;']" 
			 :placeholder="element.placeholder" :bordered="false"></Input>
		</div>
		<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
			element.controlClick?'click':'default']"
			v-if="element.type === ControlType.MultilineText">
			<Textarea ref="linesInput"  v-model:value="element.value" :placeholder="element.placeholder"
			 :style="['width:100%;height:100%; resize: none;',
			 'font-size:'+element.style.fontSize + 'px',
			 'text-align:' + element.style.textAlign,
			 'fontFamily:' + element.style.fontFamily,
			 'pointer-events: none;']"
			 @blur="inputBlur" :bordered="false" />
		</div>
		<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
			element.controlClick?'click':'default']"
			v-if="element.type === ControlType.Signature">
			<span >
				<SvgIcon :name="element.icon" :size="40"></SvgIcon>
			</span>
			<span>{{element.title}}</span>
		</div>
		<Popconfirm placement="leftTop" :showCancel="false" v-model:visible="element.showModal" :disabled="true" 
			v-if="element.type === ControlType.Seal" overlayClassName="seal-popover">
			<template #title>
				<div class="seal-modal">
					<div class="seal-modal-title">
						请选择电子签章
					</div>
					<div style="padding: 10px 0;">
						<InputSearch v-model:value="element.sealName" placeholder="seal search text" style="width: 200px"/>
					</div>
					<div class="seal-item" @click="addSeal(seal)">
						<div class="seal-img">
							<img :src="seal" class="wh100"/>
						</div>
						<div class="seal-name">
							电子公章
						</div>
					</div>
					<div class="seal-item" @click="addSeal(seal2)">
						<div class="seal-img">
							<img :src="seal2" class="wh100"/>
						</div>
						<div class="seal-name">
							电子合同专用章
						</div>
					</div>
				</div>
			</template>
			<template #icon>
				
			</template>
			<template #okButton>
			</template>
			
			<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
				element.controlClick?'click':'default']">
				<template v-if="!element.setting">
					<span>
						<SvgIcon :name="element.icon" :size="40"></SvgIcon>
					</span>
					<span>{{element.title}}</span>
				</template>
				<template v-else>
					<img :src="element.data"/>
				</template>
				<template v-if="moveIn && element.controlOperationType == 'sign'">
					<div class="setting-sign"
						@mouseup ="settingSeal">
						{{element.setting?'更换签章':'设置签章'}}
					</div>
				</template>
			</div>
		</Popconfirm>
		
		
		<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
			element.controlClick?'click':'default']"
			v-if="element.type === ControlType.Number">
			<InputNumber ref="lineInput" v-model:value="element.value"  @blur="inputBlur"
				:style="['width:100%;height:100%',
				'font-size:'+element.style.fontSize + 'px',
				'text-align:' + element.style.textAlign,
				'pointer-events: none;']"  :controls="false" :stringMode="true"
			 :placeholder="element.placeholder" :bordered="false"/>
		</div>
		<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
			element.controlClick?'click':'default']"
			v-if="element.type === ControlType.SignDate">
			<template v-if="element.controlOperationType == 'sign'">
				<div class="setting-sign-date">
					2023-02-18
				</div>
			</template>
			<span v-else>{{element.title}}</span>
		</div>
		<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
			element.controlClick?'click':'default']"
			v-if="element.type === ControlType.Date">
			<!-- <span>{{element.name}}</span> -->
			<DatePicker ref="datePicker" v-model:value="element.value" 
			:placeholder="element.format" :format="element.format"
			class="control-date-picker"
			 :style="['width:100%;height:100%; resize: none;',
			 'font-size:'+element.style.fontSize + 'px',
			 'text-align:' + element.style.textAlign,
			 'fontFamily:' + element.style.fontFamily,
			 dbclick?'':'pointer-events: none;'
			 ]" :open="datePickerOpen" 
			 @blur="inputBlur" @change="dateChange" :bordered="false">
			<template #suffixIcon>
			</template>
			</DatePicker>
		</div>
		<!-- :open="datePickerOpen" -->
		<div class="control-handles">
			<div class="handle handle-top-left receive-color this-click"
			v-on:mousedown="controlZoomMousedown($event,element,1)"
			v-if="element.controlClick && element.zoom"
			></div>
			<template v-if="element.controlOperationType == 'sign'">
				<div class="closeControl handle-top-right receive-color this-click 312"
					v-on:click.stop="controlDelete" v-if="element.controlClick && element.setting && !element.disabled">
				
					<Icon color="red" size="18" icon="ant-design:close-circle-outlined"/>
				</div>
			</template>
			<template v-else>
				<div class="closeControl handle-top-right receive-color this-click 123"
					v-on:click="controlDelete" v-if="element.controlClick">
					<Icon color="red" size="18" icon="ant-design:close-circle-outlined"/>
				</div>
			</template>
			
			<div class="handle handle-bottom-left receive-color this-click"
			v-on:mousedown="controlZoomMousedown($event,element,2)"
			v-if="element.controlClick && element.zoom"
			></div>
			<div class="handle handle-bottom-right receive-color this-click"
			v-on:mousedown="controlZoomMousedown($event,element,3)"
			v-if="element.controlClick && element.zoom"
			></div>
		</div>
	</span>
	<div class="signer-tips" v-if="element.controlClick && element.user.index != -1"
	:style="['left:'+element.position.left+'px;top:'+(element.position.top +10+ element.size.height)+'px;',
	'width:' + element.size.width + 'px',
	]"
	>
		{{element.user.userName}}
	</div>
	</div>
</template>

<script lang="ts">
	import {defineComponent,ref,reactive,getCurrentInstance,watch} from 'vue';
	import {Input,Textarea,InputNumber,DatePicker,Popconfirm,InputSearch} from 'ant-design-vue';
	import {getColor} from "/@/views/contract/document/data/ReceiveColor"
	import {SvgIcon,Icon} from "@/components/Icon";
	import seal from "@/assets/images/seal.png"
	
	import seal2 from "@/assets/images/seal.svg"
	
	import {ControlType} from "./data/ControlData"
	import {moveRange} from "./data/ControlerMoveRange"
	import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';
	export default defineComponent({
		name: 'control-item',
		components: {
			Input ,Textarea,SvgIcon,Icon,InputSearch,
			InputNumber,DatePicker,Popconfirm
		},
		props:{
			element:{
				type: Object
			},
			doc:{
				type:Object
			}
			
		},
		emits:["controlMousedown","controlDelete"],
		setup(props:any,{emit}) {
			
			 
			const cursorStyle = ref("cursor:move;");
			const zoomType = ref(0);
			const input = ref(null);
			const lineInput = ref<any>(null);
			const linesInput = ref<any>(null);
			const datePicker = ref<any>(null);
			const dbclick = ref(false);
			const datePickerOpen = ref(false);
			const sealVisible = ref(true);
			
			const nowPoint = ref({
				x: 0,
				y:0
			})
			const eventMove = ref({
				x:0,
				y:0
			});
			function controlDelete(e:any){
				console.log("controlDelete");
				emit('controlDelete',props.element,e)
			}
			function controlMovemousedown(e:any,element:any,falg:boolean){
				if(element.disabled){
					return;
				}
				props.element.controlClick = true;
				emit('controlMousedown',element,input.value,e)
				if(!element.move){
					return;
				}
				nowPoint.value = {
					x:e.clientX,
					y:e.clientY
				}
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
				// eventMove.value.x = dis.x;
				// eventMove.value.y = dis.y;
				props.element.position.left += dis.x ;
				props.element.position.top += dis.y;
				// removeMousedown();
			}
			function removeMousedown(){
				//document.removeEventListener('mousemove', setOffset)
				let x = props.element.position.left + eventMove.value.x;
				let y = props.element.position.top + eventMove.value.y;
				
				const opt:any = {
					x:x,
					y:y,
					pageSize:props.doc.pageSize,
					size:props.element.size,
					currentPage:0,
				};
				moveRange(opt);
				props.element.position.left =opt.x;
				props.element.position.top = opt.y;
				props.element.position.page = opt.currentPage;
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
				const nowPon = {
					width:0,
					height:0,
					left:0,
					top:0
				}
				if(zoomType.value == 3){
					//横纵坐标 直接+
					nowPon.width = props.element.size.width + dis.x;
					nowPon.height = props.element.size.height + dis.y;
					nowPon.top = props.element.position.top;
					nowPon.left = props.element.position.left;
				}
				if(zoomType.value == 2){
					nowPon.width = props.element.size.width + dis.x * -1;
					nowPon.height = props.element.size.height + dis.y;
					nowPon.left = props.element.position.left + dis.x;
					nowPon.top = props.element.position.top;
				}
				if(zoomType.value == 1){
					nowPon.width = props.element.size.width + dis.x * -1;
					nowPon.left = props.element.position.left + dis.x;
					nowPon.height = props.element.size.height + dis.y * -1;
					nowPon.top = props.element.position.top + dis.y;
				}
				if(nowPon.width < props.element.size.minWidth){
					nowPon.width = props.element.size.minWidth
					nowPon.left = props.element.position.left;
				}
				
				if(nowPon.height < props.element.size.minHeight){
					nowPon.height = props.element.size.minHeight
					nowPon.top = props.element.position.top;
				}
				if(nowPon.left<0){
					nowPon.left = 0;
				}
				if(nowPon.top<0){
					nowPon.top = 0;
				}
				if(nowPon.width>800){
					nowPon.width =800;
				}
				if(nowPon.height>1131){
					nowPon.height =1131;
				}
				props.element.size.width = nowPon.width
				props.element.size.height = nowPon.height;
				props.element.position.left = nowPon.left;
				props.element.position.top = nowPon.top;
				
			}
			function controlZoomMouseover(opt:any){
				//emit('moveDisabled',opt);
			}
			function inputMousedown(e:any){
			}
			function formItemFocus (){
				if(props.element.disabled){
					return;
				}
				if(props.element.controlOperationType == 'sign'){
					
				}else{
					cursorStyle.value = "cursor:text;";
					if(lineInput.value){
						lineInput.value.focus();
					}
					if(linesInput.value){
						linesInput.value.focus();
					}
					if(datePicker.value){
						datePicker.value.focus();
						datePickerOpen.value = true;
					}
					dbclick.value = true;
				}
			}
			function inputBlur(){
				cursorStyle.value = "cursor:move;";
				props.element.controlClick = false;
				if(datePicker.value){
					datePickerOpen.value = false;
				}
				dbclick.value = false;
			}
			function dateChange(){
				datePickerOpen.value = false;
			}
			const ReceiveColorItem:any = ref(null);
			//debugger;
			if(props.element.user){
				ReceiveColorItem.value = getColor(props.element.user.index,null)
			}
			if(props.element.controlOperationType == 'sign'){
				ReceiveColorItem.value = getColor(-2,null)
			}
			if(!ReceiveColorItem.value){
				ReceiveColorItem.value = getColor(-1,null)
			}
			
			// console.log(ReceiveColorItem.value,props.element.user);
			watch(() => props.element.user.index,
			(newValue,oldValue) => {
				ReceiveColorItem.value = getColor(newValue,null)
			})
			const moveIn = ref(false);
			const moveTimeout:any = ref(null);
			function controlMouseover(e:any){
				//防止鼠标快速划过控件显示
				moveTimeout.value = setTimeout(function(){
					console.log("controlMouseover");
					moveIn.value = true;
				},100)
			}
			function mouseoverMouseout(e:any){
				console.log("mouseoverMouseout");
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
				//cursor: pointer;
			}
			function addSeal(seal:any){
				//props.element.dataId = dataId;
				props.element.data = seal;
				props.element.setting = true;
				
			}
			initData();
			return{
				controlMovemousedown,
				inputBlur,
				cursorStyle,inputMousedown,
				getColor,
				ReceiveColorItem,controlZoomMousedown,
				controlZoomMouseover,
				input,formItemFocus,
				linesInput,lineInput,
				controlDelete,ControlType,
				dbclick,datePicker,datePickerOpen,
				dateChange,locale,
				controlMouseover,mouseoverMouseout,moveIn,
				settingSeal,seal,seal2,sealVisible,addSeal
			}
		}
	})
</script>

<style lang="scss" scoped>
	
	.resrun-control{
		user-select: none;
		cursor: move;
		position: absolute;
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
	.control-item{
		position: relative;
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
		width:100%;
		height: 100%;
		border: 1px;
		border-style: dashed;
		border-radius: 1px;
		border-color:var(--translucent);
		background-color: rgb(var(--background-rgb),0.5)
	}
	.control-item.click{
		width:100%;
		height: 100%;
		border: 1px;
		border-radius: 2px;
		border-style: solid;
		border-color:var(--translucent);
		background-color: #fff;
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
			z-index: 999;
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
			top:-2px;
			left: -2px;
			cursor: nw-resize;
		}
		.handle-top-right{
			top:-7px;
			right: -7px;
		}
		.handle-bottom-left{
			bottom:-2px;
			left: -2px;
			cursor: sw-resize;
		}
		.handle-bottom-right{
			bottom :-2px;
			right: -2px;
			cursor: se-resize;
		}
	}
	.control-signature{
		display: flex;
		flex-direction: column;
		text-align: center;
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

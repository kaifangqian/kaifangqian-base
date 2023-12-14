<template>
	<div>
		<span :class="['resrun-control','resrun-control-move','this-click']"
		:style="['left:'+element.position.left+'px;top:'+element.position.top+'px;',
		'width:' + element.size.width + 'px',
		'height:' + element.size.height + 'px',
		'--primary:#1F56B8',
		'--translucent:#5589E2',
		'--background:#ACC5F1',
		'--background-rgb:172 197 241',
		cursorStyle]"
		@mousedown.salf="controlMovemousedown($event,element)"
		@mouseenter.stop="controlMouseover"
		@mouseleave="mouseoverMouseout">
		 
			<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
				element.controlClick?'click':'default']"
				v-if="element.type === ControlType.Signature">
					<img :src="'data:image/png;base64,'+element.value" :height="element.size.height"
					:width="element.size.width" style="pointer-events: none;"/>
			</div>
			<div ref="input" :class="['control-'+element.type,'control-item','arow-content',
				element.controlClick?'click':'default']" v-if="element.type === ControlType.Seal">
					<img :src="'data:image/png;base64,'+element.value" style="pointer-events: none;"/>
			</div>
			<div class="control-handles">
				<div class="handle handle-top-left receive-color this-click"
				v-on:mousedown.salf="controlZoomMousedown($event,element,1)"
				v-if="element.controlClick && element.zoom"
				></div>
				<div class="closeControl handle-top-right receive-color this-click"
					v-on:click.stop="controlDelete">
					<CloseCircleOutlined style="font-size: 18px;color: red;"/>
				</div>
				 
				<div class="handle handle-bottom-left receive-color this-click"
				v-on:mousedown.salf="controlZoomMousedown($event,element,2)"
				v-if="element.controlClick && element.zoom"
				></div>
				<div class="handle handle-bottom-right receive-color this-click"
				v-on:mousedown.salf="controlZoomMousedown($event,element,3)"
				v-if="element.controlClick && element.zoom"
				></div>
			</div>
		</span>
		<div class="signer-tips" v-if="element.controlClick && element.user.index != -1"
		:style="['left:'+element.position.left+'px;top:'+(element.position.top +10+ element.size.height)+'px;',
		'width:' + element.size.width + 'px']">
			{{element.user.signerName}}
		</div>
	</div>
</template>

<script lang="ts">
	import {defineComponent,ref,reactive,getCurrentInstance,watch} from 'vue';
	import {ControlType,CanvasZoom} from "./data/ControlData"
	import {moveRange,currentPosition,currentPositionReverse} from "./data/ControlerMoveRange"
	
	
	
	import { CloseCircleOutlined} from '@ant-design/icons-vue';
	export default defineComponent({
		name: 'control-item',
		components: {
			CloseCircleOutlined
		},
		props:{
			element:{
				type: Object
			},
			doc:{
				type:Object
			},
			isSign:{
				type:Boolean,
				default:false
			}
		},
		emits:["controlMousedown","controlDelete","clearSeal"],
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
				emit('controlDelete',props.element)
			}
			function clearSeal(){
				emit('clearSeal',props.element)
			}
			function controlMovemousedown(e:any,element:any,falg:boolean){
				
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
				props.element.position.left += dis.x ;
				props.element.position.top += dis.y;
			}
			function removeMousedown(){
				let x = props.element.position.left + eventMove.value.x;
				let y = props.element.position.top + eventMove.value.y;
				const opt:any = {
					x:x,
					y:y,
					pageSize:props.element.pageSize,
					size:props.element.size,
					currentPage:0,
				};
				moveRange(opt);
				const pageLeft = currentPosition(opt.y,opt.currentPage);
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
				
				if(nowPon.width>CanvasZoom.width){
					nowPon.width =CanvasZoom.width;
				}
				if(nowPon.height>CanvasZoom.height){
					nowPon.height =CanvasZoom.height;
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
				 
			}
			function inputBlur(){
				 
			}
			function dateChange(element:any){
				 
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
				 
				props.element.position.top = currentPositionReverse(props.element.position.top,props.element.position.page);
				//import dayjs from 'dayjs';
				//cursor: pointer;
			}
			function addSeal(seal:any){
				//props.element.dataId = dataId;
				props.element.data = seal;
				props.element.setting = true;
				
			}
			//如果必填   有值改变将error清除掉
			function valueChange(element:any){
				if(element.required){
					element.error = false;
				}
			}
			initData();
			return{
				controlMovemousedown,
				inputBlur,
				cursorStyle,inputMousedown,
				controlZoomMousedown,
				controlZoomMouseover,
				input,formItemFocus,
				linesInput,lineInput,
				controlDelete,ControlType,
				dbclick,
				controlMouseover,mouseoverMouseout,moveIn,
				settingSeal,sealVisible,addSeal,
				valueChange,clearSeal
			}
		}
	})
</script>

<style lang="less" scoped>
	
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
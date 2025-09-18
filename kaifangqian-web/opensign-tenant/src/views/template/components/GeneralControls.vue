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
	<div class="controls wh100">
		<ul class="clear_flaot">
			<draggable :list="newControlList" ghost-class="ghost" handle=".control-move" filter=".forbid" item-key="icon"
				 :force-fallback="true" chosen-class="chosenClass" animation="300" @end="controlsDragOver" @start="controlsDragStart"
				 :group="group" :fallback-class="false" :fallback-on-body="true" :touch-start-threshold="50"
				 :fallback-tolerance="50" :clone="clone" :sort="false">
				<template #item="{ element }">
					<li :class="['control-li',
					'control-move']" v-if="isHighControls(element)">
						<span>
							<SvgIcon :name="element.icon" :size="20"></SvgIcon>
						</span>
						<span class="name">{{element.name}}</span>
					</li>
          <a-tooltip placement="top" v-else>
            <template #title>
              <span>当前软件授权不支持该控件，如需使用，请联系系统管理员升级软件授权</span>
            </template>
            <li :class="['control-li',
            'control-disabled']">
            	<span>
            		<SvgIcon :name="element.icon" :size="20"></SvgIcon>
            	</span>
            	<span class="name">{{element.name}}</span>
            </li>
          </a-tooltip>
				</template>
			</draggable>
		</ul>
	</div>
</template>

<script lang="ts">
	import {defineComponent,ref,reactive,getCurrentInstance,onMounted,defineExpose} from 'vue';
	
	import draggable from 'vuedraggable'
	import {getControlList} from "./dist/ControlData"
	import {Icon,SvgIcon} from "/@/components/Icon";
	import {deepClone,uuid} from "/@/utils"
	import {moveRange,currentPage} from "./dist/ControlerMoveRange"
	import {ControlType,isFillControl,CanvasZoom} from "./dist/ControlData"
	export default defineComponent({
		name: 'Controls',
		components: {
			Icon,SvgIcon,draggable,
		},
		props:{
			user:{
				type: Object,
			},
			activeControl:{
				type: Array
			},
			nowDocument:{
				type: Object
			},
      controlTypeList:{
        type:Array
      },
      highControls:{
        type:Boolean,
        default:false
      }
		},
		emits:["dragOver"],
		setup(props:any,{emit}) {
			function clone(origin:any){
				const data = JSON.parse(JSON.stringify(origin))
        data.pageSize = props.nowDocument.pageSize;
        data.controlClick = false;
				data.uid = uuid();
        data.interfaceParamName = data.interfaceParamName + data.uid;
				return data
			}
			//开始之前将所有的控件设置为非点击状态
			function controlsDragStart(){
				props.nowDocument.activeControl.forEach((item:any)=>{
					item.controlClick = false;
				})
			}
			function controlsDragOver(e:any) {
				const moveTarget  = props.nowDocument.activeControl[e.newIndex];

				if(moveTarget){
          
          // console.log("over:",opt,moveTarget.position);
          
          console.log("moveTarget:",moveTarget,e);
					const opt:any = {
						x:e.originalEvent.offsetX - (moveTarget.size.width/2),
						y:e.originalEvent.offsetY - (moveTarget.size.height/2),
						pageSize:props.nowDocument.pageSize,
						size:moveTarget.size,
						currentPage:0,
            //新增下列三个属性
            //页面总高度
            allHeight:props.nowDocument.imageAllHeight,
            //所有页面最大的宽度
            maxWidth: props.nowDocument.maxWidth,
					};
          
          // currentPage(opt,props.doc.targets);
          // opt.currentPage = current.currentPage;
          
					moveRange(opt,props.nowDocument.targets);
					moveTarget.position.left = opt.x;
					moveTarget.position.top = opt.y;
					moveTarget.position.page = opt.currentPage;
					moveTarget.controlClick = true;
          
          if(moveTarget.type == ControlType.Radio || moveTarget.type == ControlType.CheckBox){
            // console.log("多选");
            initRadio(moveTarget);
          }
          if(moveTarget.type == ControlType.DropdownList){
            initDropdown(moveTarget);
          }
          
					emit("dragOver",e,moveTarget)
				}
			}
      function initDropdown(moveTarget){
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
       moveTarget.widgetList = widgetList;
      }
      function initRadio(moveTarget){
        const controlSize = 32;
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
        moveTarget.widgetList = widgetList;
        moveTarget.widgetList.forEach((sub,index)=>{
          sub.x = moveTarget.position.left;
          sub.y = moveTarget.position.top + index * sub.h ;
        })
        moveTarget.size.height = moveTarget.widgetList.length * moveTarget.widgetList[0].h;
      }
      function loadDisabledClass(ele:any){
          // if(!props.user.signType){
          //     return ''
          // }
          // if(ele.icon === 'sign' || ele.icon === 'seal'){
          //     return props.user.signType&&props.user.signType === 2?'control-disabled':''
          // }else{
          //     return props.user.writtenType&&props.user.writtenType === 2?'control-disabled':''
          // }
          // return 'control-disabled'
      }
			const group = ref({
				name: "itxst",
				put: true, //允许拖入
				pull:'clone', //允许拖出
				/* pull:(e:any)=>{
					return false;
				} */
			})
			const controlClass = (element:any)=>{
        if(element.type == ControlType.Radio || element.type == ControlType.CheckBox || element.type == ControlType.DropdownList || element.type == ControlType.Image){
          if(props.highControls){
            return "control-move";
          }
          return "control-disabled";
        }else{
          return "control-move";
        }
				//return false;
			}
      
      function isHighControls(element:any){
        if(element.type == ControlType.Radio || element.type == ControlType.CheckBox || element.type == ControlType.DropdownList || element.type == ControlType.Image){
          if(!props.highControls){
            return false;
          }
        }
        return true;
      }
      const newControlList = ref<any>([]);
      function loadData(){
        const controlList = getControlList(props.highControls);
        // if(props.controlTypeList && props.controlTypeList.length>0){
        //   controlList.forEach((item:any)=>{
        //     props.controlTypeList.forEach(type=>{
        //       if(item.type == type){
                // newControlList.value.push(deepClone(item))
        //       }
        //     })
        //   })
        // }else{
          newControlList.value = controlList;
        // }
      }
			return{
				newControlList,
				clone,group,isFillControl,
				controlsDragOver,
				controlsDragStart,isHighControls,
        loadDisabledClass,controlClass,loadData
			}
		}
	})
	
</script>

<style lang="less" scoped>
	.controls{
		display: flex;
		justify-content:center;
		align-items: center;
    height: 100%;
	}
  .controls ul{
    margin: 0;
    padding: 0;
  }
	.control-li{
		width: 80px;
		height: 46px;
		// border: 1px solid #eee;
		float: left;
		// margin-left: 10px;
		// margin-top: 10px;
		box-sizing: content-box;
		cursor:grab;
		user-select: none;
	}
	.control-li:hover{
		background-color: #e3e3e3;
	}
	.control-li span{
		display: block;
		line-height: 27px;
		text-align: center;
	}
	.control-li span:nth-child(1){
		height: 20px;
		text-align: center;
		padding-top: 6px;
	}
    .control-disabled{
        color: #bdbdbd;
		fill:#bdbdbd;
        cursor: not-allowed;
        .name,svg{
            pointer-events: none;
			fill:inherit;
        }
		
    }
</style>

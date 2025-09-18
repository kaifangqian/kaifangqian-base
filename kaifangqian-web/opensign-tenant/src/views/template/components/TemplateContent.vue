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

<!-- 控件列表 -->
<template>
  <div class="document-template-layout">
    <div class="document-content">
    	<Scrollbar width="100%" height="100%" :native="true" :noresize="true" ref="scrollbarRef"
      :viewStyle="[
        'width:'+calcScrollWidth()+'px',
        'height:'+(nowDocument.imageAllHeight * zoom / 100)+'px',
      ]">
        <div class="document-list" :style="[
    			'height: '+ (nowDocument.imageAllHeight) +'px;',
          'width:'+nowDocument.maxWidth+'px;',
          'transform:scale('+ zoom / 100 +')',
          'transform-origin: '+50+'% 0px'
    		]" v-if="nowDocument && nowDocument.images">
        
    			<template v-for="item in nowDocument.images">
    				<div class="document-page group"  v-if="item"
    					:style="[
                'transform: translate('+((nowDocument.maxWidth - item.target.width)/2)+'px,'+(item.target.transformHeight + (item.page+1) * 16)+'px);',
                'width:' +item.target.width+'px;',
                'height:' + item.target.height+'px;'
              ]">
    					<img style="width: 100%;" v-lazy="baseUrl + '/file/downloadFileStream/' + item.annexId"/>
              <!-- <img style="width: 100%;" :src="item.img"/> -->
    				</div>
    			</template>
         <!-- <div class="control-box">
            <template v-for="element in nowDocument.activeControl">
              <control-item :doc="nowDocument" :element="element " :isSign="false"
               @controlDelete="controlDelete" @controlMousedown="controlMousedown" @clearSeal="clearSeal"/>
            </template>
          </div> -->
    			<draggable :list="nowDocument.activeControl" ghost-class="ghost" draggable=".test"
    			item-key="id"
    			:group="group" :force-fallback="true" chosen-class="chosenClass" animation="300"
    			:fallback-class="true" :fallback-on-body="true" :touch-start-threshold="50"
    			:fallback-tolerance="50" 
    			style="width: 100%;height: 100%;" v-if="showControl" class="control-box"> 
    				<template #item="{ element }"  >
    					<control-item :doc="nowDocument" :element="element" :isSign="false"
    					 @controlDelete="controlDelete" @controlMousedown="controlMousedown" @clearSeal="clearSeal"/>
    				</template>
    			</draggable>
    		</div>
    	</Scrollbar>
    </div>
   <div class="document-toolbar">
      <div class="zoom-bar">
          <Icon icon="ant-design:minus-circle-outlined" size="20" @click="handleZoom(1)" :class="zoom <= 40?'zoom-disabled':''"/>
          <!-- <a-input v-model:value="zoom" style="width:60px" suffix="%" ></a-input> -->
          <span style="font-size:12px;line-height: 20px;vertical-align:top;user-select: none;">{{zoom+'%'}}</span>
          <Icon icon="ant-design:plus-circle-outlined" size="20" :class="zoom >= 200?'zoom-disabled':''" @click="handleZoom(2)"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
		import {defineComponent,ref,watch,getCurrentInstance} from 'vue';
		import {Badge,Modal } from 'ant-design-vue';
		/* import CScrollbar from '@/components/scrollbar/src/Index.vue'; */
     import { Scrollbar } from '/@/components/Scrollbar';
		import draggable from 'vuedraggable'
		
		import {Icon} from "/@/components/Icon";
		import ControlItem from './ControlItem.vue';
		import {CanvasZoom} from './dist/ControlData'
		import {getWindowAvailable} from "@/utils";
		import { getAppEnvConfig } from '/@/utils/env';
		export default defineComponent({
			name: 'document-info',
			components: {
				Badge,Scrollbar,Icon,
				draggable,ControlItem
			},
			props:{
				nowDocument:{
					type: Object
				},
				showControl:{
					type:Boolean
				},
			},
			emits:["controlMousedown","controlDelete"],
			setup(props,{emit}) {
				const {VITE_GLOB_API_URL} = getAppEnvConfig();
        
        const zoom = ref(100);
        const scrollWidth = ref(800);
        const scrollbarRef = ref();
        watch(
          ()=>props.nowDocument.maxWidth,
          (val)=>{
            if(val){
              zoom.value =  parseInt( getWindowAvailable(711,800) / val  * 100)
              calcScrollHeight();
              // scrollWidth.value = getWindowAvailable(704);
            }
          },
          // {deep:true}
        )
        function calcScrollWidth(){
          const  visual = getWindowAvailable(711);
          const newWidth = props.nowDocument.maxWidth * (zoom.value/100);
          return visual>newWidth?visual:newWidth;
        }
        function calcScrollHeight(){
          console.log("hhh",props.nowDocument.imageAllHeight * zoom.value / 100 , props.nowDocument.imageAllHeight);
          
          // const  visual = getWindowAvailable(711);
          // const newWidth = props.nowDocument.maxWidth * (zoom.value/100);
          // return visual>newWidth?visual:newWidth;
        }
				const baseUrl = ref(VITE_GLOB_API_URL);
				const group = ref({
					name: "itxst",
					put: true, //允许拖入
					pull:true, //允许拖出
				})
				//控件删除
				function controlDelete(item:any){
					emit("controlDelete",item)
				}
				function clearSeal(item:any){
					emit("clearSeal",item)
				}
        //控件点击事件
				function controlMousedown(item:any){
					emit("controlMousedown",item)
				}
        const handleZoom = (type:number) => {
          // if(zoom.value < 500 || zoom.value > 40){
          //   return;
          // }
          if(type === 1){
            if(zoom.value > 40){
              const temZoom = Number(zoom.value) - 10;
              zoom.value = temZoom<40?40:temZoom;
            }
            
          }else{
            if(zoom.value < 200){
              var temZoom = Number(zoom.value) + 10;
              zoom.value = temZoom>200?200:temZoom;
            }
            // zoom.value = Number(zoom.value) + 10;
          }
          calcScrollHeight();
          // CanvasZoom.zoom = zoom.value;
          scrollbarRef.value.update();
          // console.log(scrollbarRef.value);
          // emit("handleZoom",zoom.value);
        }
				return{
					controlDelete,controlMousedown,zoom,scrollbarRef,calcScrollWidth,
					CanvasZoom,baseUrl,group,clearSeal,handleZoom,getWindowAvailable
				}
			}
		})
		
</script>

<style lang="less" scoped>
  .document-template-layout{
    height: 100%;
    width: 100%;
  }
	.document-content{
		flex:1;
		height: 100%;
		overflow: auto;
		.document-list{
			position: relative;
			margin: 0 auto;
			padding-bottom: 40px;
      box-sizing: content-box;
		}
		.document-page{
			
			position: absolute;
		}
		.document-bottom{
			height: 40px;
			border-bottom: 1px solid rgba(0, 0, 0, 0.08);
			border-top: 1px solid rgba(0, 0, 0, 0.08);
		}
		/* .document-body{
			height: calc(100% - 40px);
		} */
	}
  .document-toolbar{
    width: 110px;
    height: 30px;
    border-radius: 20px;
    background-color: #fff;
    position: fixed;
    bottom:5px;
    left: calc(50% - 50px);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
    padding: 5px 0px;
  }
  .zoom-bar{
    text-align: center;
  }
  :deep(.scrollbar__wrap){
    overflow:visible;
  }
  .zoom-disabled{
    color: #bdbdbd;
    cursor: not-allowed;
  }
</style>
<style>
  .control-box{
    position: relative;
  }
</style>

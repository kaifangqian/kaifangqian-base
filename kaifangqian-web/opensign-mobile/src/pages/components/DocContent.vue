<!--
  @description DocContent

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
    <div class="doc-layout-container">
        <slot></slot>
        <div class="doc-content">
            <div class="zoom-container" 
                :style="[
                  'width:' + (nowDocument&&nowDocument.maxWidth * (zoom / 100)) + 'px',
                  'height: '+ (nowDocument&&nowDocument.imageAllHeight) * (zoom / 100) +'px;',
                  'margin:0 auto',
                ]">
                <div class="document-list" :style="[
                    'height: '+ (nowDocument.imageAllHeight) +'px;',
                    'width:'+ nowDocument.maxWidth + 'px;',
                    'transform:scale('+ zoom / 100 +')',
                    'transform-origin: 0% 0%'
                ]" v-if="nowDocument && nowDocument.images && nowDocument.imageLoading">
                    <template v-for="(item,index) in nowDocument.images">
                        <div class="document-page group" v-if="item"
                            :style="[
                                'transform: translate('+ '-50%,'+(item.target.transformHeight + (item.page+1) * 16)+'px);',
                                'width:' + item.target.width +'px;',
                                'height:' + item.target.height +'px;',
                            ]"  :key="index">
                            <img style="width: 100%;" v-lazy="baseUrl + '/file/downloadFileStream/' + item.annexId" />
                            <!-- 页面编号角标 -->
                            <div class="page-corner">
                            </div>
                            <div class="page-num">
                            {{ item.page + 1 }}
                            </div>
                        </div>
                    </template>
                    <template v-for="(element,elementIndex) in nowDocument.activeControl" :key="elementIndex"  style="width: 100%;height: 100%;position: relative;">
                        <control-item :element="element" @controlDelete="controlDelete" @controlMousedown="controlMousedown"  
                            @clearSeal="clearSeal" @controlClick="controlClick" @controlMenu="controlMenu" :doc="nowDocument">
                        </control-item>
                    </template>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script lang="ts">
import { ref, defineComponent, Ref } from "vue";
import draggable from 'vuedraggable';
// import { CanvasZoom } from './data/ControlData';
import { getCanvasZoom, getColtrolByType } from "@/utils/ControlData"

import ControlItem from './items/ControlItem.vue';

interface DragListDataModel {
    id: number,
    name: string
}

interface DragPropsDataModel {
    modelValue: Array<{
        id: number
        name: string
    }>
}

export default defineComponent({
    name: "DocContent",
    components: {
        draggable,
        ControlItem,
    },
    props: {
        nowDocument: {
            type: Object
        },
        showControl: {
            type: Boolean
        },
        isSign: {
            type: Boolean,
            default: false
        },
        spinning: {
            type: Boolean,
            default: false
        }
    },
    setup(props, { emit }) {
        const clientWidth = ref(document.body.clientWidth);
        console.log(props.nowDocument,'组件内的文档')
        const CanvasZoom = ref(getCanvasZoom(clientWidth.value - 20, 100));


        const group = ref({
            name: "itxst",
            put: true, //允许拖入
            pull: true, //允许拖出
        })
        const baseImgWidth = window.innerWidth - 20;
        const aspecRatio = 800 / 1131;

        const dragList = ref([
            { id: 1, name: '看万山红遍，层林尽染；漫江碧透，百舸争流。' },
            { id: 3, name: '怅寥廓，问苍茫大地，谁主沉浮？' },
            { id: 0, name: '独立寒秋，湘江北去，橘子洲头。' },
            { id: 2, name: '鹰击长空，鱼翔浅底，万类霜天竞自由。' },
            { id: 4, name: '携来百侣曾游，忆往昔峥嵘岁月稠。' },
            { id: 5, name: '恰同学少年，风华正茂；书生意气，挥斥方遒。' },
            { id: 6, name: '指点江山，激扬文字，粪土当年万户侯。' },
            { id: 7, name: '曾记否，到中流击水，浪遏飞舟？' }
        ])

        watch(
            ()=> props.nowDocument&&props.nowDocument.maxWidth,
            (val)=>{
                if(val){
                    zoom.value = Math.round( (window.innerWidth - 20) / val  * 100)
                }
            },
            {deep:true}
        )
        const zoom = ref(100);

        const handleZoom = (type:number) => {
            if(type === 1){
            zoom.value = Number(zoom.value) - 10;
            }else{
            zoom.value = Number(zoom.value) + 10;
            }
            emit("handleZoom",zoom.value);
        }

        // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
        let baseUrl = import.meta.env.VITE_APP_API_BASE_URL;

        //签署人点击事件
        function controlDelete(item: any) {
            emit("controlDelete", item)
        }
        function controlMousedown(item: any) {
            emit("controlMousedown", item)
        }
        function clearSeal() {

        }

       //控件点击  
        function controlClick(e, element) {
            emit("controlClick", e, element)
        }
        //菜单点击
        function controlMenu(e, element, isBatch:boolean=false) {
             emit("controlMenu", e, element)
        }
        return {
            CanvasZoom,
            baseUrl,
            controlDelete,
            controlMousedown,
            group,
            clearSeal,
            controlClick,
            controlMenu,
            zoom,
            dragList,
            baseImgWidth,
            aspecRatio,

        }
    }
})
</script>
  
<style lang="less" scoped>
.doc-layout-container {
    flex: 1 1 0;
    // padding: 25px 0;
    background: #f9f9f9;
    overflow-y: auto;
    margin-top: 92px;
    padding-bottom:80px;

    .doc-content {
        height: 100%;
        padding-bottom: 80px;
        // overflow-y: scroll;
        overflow: hidden;
    }

    .doc-content::-webkit-scrollbar-thumb {
        background-color: rgb(190, 188, 188);
        /* 设置滚动条滑块的颜色 */
        border-radius: 6px;
        /* 设置滚动条滑块的圆角 */
        cursor: pointer;
    }

    .doc-content::-webkit-scrollbar {
        width: 8px;
        /* 设置滚动条的宽度 */
    }

    .doc-content::-webkit-scrollbar-track {
        background-color: #f0f0f0;
        /* 滚动条轨道的背景色 */
    }

    :deep(.scrollbar__wrap) {
        overflow: visible;
    }


    .document-list {
        position: relative;
        // margin: 0 auto;
        width: 100%;
        display:flex;
        margin-top:-32px;
        flex-direction: column;
        align-items: center;
    }

    .document-page {
        width: 100%;
        position: absolute;
        top: 0;
        left: 50%;
        border: 1px solid #ccc;
        box-sizing: border-box;
        box-shadow: 0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1);

        img {
            width: 100%;
            height: 100%;
        }
    }

    .document-bottom {
        height: 40px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);
        border-top: 1px solid rgba(0, 0, 0, 0.08);
    }

    .document-body {
        height: calc(100% - 40px);
    }
}

.page-corner {
    position: absolute;
    right: 0px;
    bottom: 0px;
    width: 0;
    height: 0;
    border-color: #ccc transparent;
    border-width: 0 0 1.5rem 1.5rem;
    border-style: solid;
    }

  .page-num {
    position: absolute;
    bottom: 5px;
    right: 0.2rem;
    font-size:36px;
    color: #fff;
    // padding-right: 20px;
  }
</style>
  
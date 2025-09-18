<!--
  @description 控件

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
        <span :class="['resrun-control', 'resrun-control-move', 'pos-' + element.position.top, 'this-click']" :style="['left:' + element.position.left + 'px;top:' + element.position.top + 'px;',
        '--primary:' + ReceiveColorItem.primary,
        '--translucent:' + [element.error ? 'red' : ReceiveColorItem.translucent],
        '--background:' + ReceiveColorItem.background,
        '--background-rgb:' + ReceiveColorItem.backgroundRgb,
        'width:' + element.size.width + 'px',
        'height:' + element.size.height + 'px',
        'display:' + 'block',
        'position:' + 'absolute',
        // 'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
            cursorStyle]" @touchstart="controlMovemousedown($event, element)" @touchmove="controlMouseover"
            @touchend="(e)=>mouseoverMouseout(e,element)" @dblclick="formItemFocus">
            <van-tag type="primary" class="batch-icon" v-if="element.isBatch" >批量</van-tag>
            <slot></slot>
            <div></div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default',
            ]" v-if="element.controlType === ControlType.LineText" @click.stop="controlClick($event, element)">
                <van-field ref="lineInput" v-model:value="element.value" @blur="inputBlur" :style="['width:100%;height:100%',
                    'font-size:' + element.style.fontSize+ 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    'pointer-events: none;']" @change="valueChange(element)" :placeholder="element.placeholder"
                    :bordered="false"></van-field>
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.MultilineText"
                @click="controlClick($event, element)">
                <van-field ref="linesInput" v-model:value="element.value" :placeholder="element.placeholder" :style="['width:100%;height:100%; resize: none;',
                    'font-size:' + element.style.fontSize+ 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    'pointer-events: none;']" @blur="inputBlur" @change="valueChange(element)" :bordered="false" />
            </div>
            
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Seal"
                    @touchend.prevent="controlClick($event, element)" >
                    <van-popover placement="top" theme="dark" style="max-width:200px" :offset="[30,28]" teleport=".doc-content"  :actions="sealActions"  v-model:show="element.showPopover">
                        <template #action="{ action }">
                            <div class="custom-action" v-if="action.text !=='删除'" @touchend="(e)=>controlMenuClick(e,element,action.text=='批量应用'?true:false)">
                                <SvgIcon :name="action.icon" size="20" color="#999"></SvgIcon>
                                <span style="margin-left:4px;">{{ action.text }}</span>
                            </div>
                            <div class="custom-action" v-if="action.text =='删除'" @touchend="controlDelete">
                                <SvgIcon :name="action.icon" size="20" color="#999"></SvgIcon>
                                <span style="margin-left:4px;">{{ action.text }}</span>
                            </div>
                        </template>
                    </van-popover>
                    <template v-if="element.sealId" class="seal-img">
                        <img :src="baseUrl + '/file/downloadFileStream/' + element.annexId" />
                    </template>
                    <template v-else-if="!element.setting">
                        <span>
                            <SvgIcon :name="element.icon" :size="30"></SvgIcon>
                        </span>
                        <span>{{ element.title }}</span>
                    </template>
                    <template v-else>
                        <img :src="element.seal.signature" />
                    </template>
                    <template v-if="moveIn && element.controlOperationType == 'sign'">
                        <div class="setting-sign" @mouseup="settingSeal">
                            {{ element.setting ? '更换签章' : '设置签章' }}
                        </div>
                    </template>
                </div>
        
                <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.ChopStamp"
                    @touchend.prevent="controlClick($event, element)">
                    <van-popover placement="top" theme="dark"  style="max-width:200px"   :offset="[30,28]"  :actions="sealActions" teleport=".doc-content" v-model:show="element.showPopover">
                        <template #action="{ action }">
                            <div class="custom-action" v-if="action.text !=='删除'" @touchend="(e)=>controlMenuClick(e,element,action.text=='批量应用'?true:false)">
                                <SvgIcon :name="action.icon" size="20" color="#999"></SvgIcon>
                                <span style="margin-left:4px;">{{ action.text }}</span>
                            </div>
                            <div class="custom-action" v-if="action.text =='删除'" @touchend="controlDelete">
                                <SvgIcon :name="action.icon" size="20" color="#999"></SvgIcon>
                                <span style="margin-left:4px;">{{ action.text }}</span>
                            </div>
                        </template>
                    </van-popover>
                    <template v-if="element.sealId" class="riding-seal-img">
                        <img :src="baseUrl + '/file/downloadFileStream/' + element.annexId" />
                    </template>
                    <template v-else-if="!element.setting">
                        <span>
                            <SvgIcon :name="element.icon" :size="30"></SvgIcon>
                        </span>
                        <span>{{ element.title }}</span>
                    </template>
                    <template v-else>
                        <img :src="element.ridingseal.signature" />
                    </template>
                    <template v-if="moveIn && element.controlOperationType == 'sign'">
                        <div class="setting-sign" @mouseup="settingSeal">
                            {{ element.setting ? '更换签章' : '设置签章' }}
                        </div>
                    </template>
                </div>
            
        
                <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Signature"
                    @touchend.prevent="controlClick($event, element)" >
                    <van-popover placement="top" theme="dark"  style="max-width:200px"  :offset="[30,28]" :actions="signatureActions" teleport=".doc-content" v-model:show="element.showPopover">
                        <template #action="{ action }">
                            <div class="custom-action" v-if="action.text !=='删除'" @touchend="(e)=>controlMenuClick(e,element,action.text=='批量应用'?true:false)">
                                <SvgIcon :name="action.icon" size="20" color="#999"></SvgIcon>
                                <span style="margin-left:4px;margin-top:4px">{{ action.text }}</span>
                            </div>
                            <div class="custom-action" v-if="action.text =='删除'" @touchend="controlDelete">
                                <SvgIcon :name="action.icon" size="20" color="#999"></SvgIcon>
                                    <span style="margin-left:4px;margin-top:4px">{{ action.text }}</span>
                            </div>
                        </template>
                    </van-popover>
                    <template v-if="element.signatureId">
                        <img :src="baseUrl + '/file/downloadFileStream/' + element.signatureId" class="signature-img" />
                    </template>
                    <template v-else-if="element.signature">
                        <img :src="element.signature" class="signature-img" :key="element.id" />
                    </template>
                    <template v-else-if="!element.setting">
                        <span>
                            <SvgIcon :name="element.icon" :size="30"></SvgIcon>
                        </span>
                        <span>{{ element.title }}</span>
                    </template>
                    <template v-else>
                        <span>{{ element.title }}</span>
                        <img :src="element.seal.signature" :height="element.size.height" :width="element.size.width"
                            v-if="element.seal.sealStyle == 5" />
                        <img :src="element.seal.signature" :height="element.size.height" :width="element.size.height"
                            v-if="element.seal.sealStyle == 4" />
                    </template>
                </div>
                    <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                    element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.SignDate"
                        @touchend.prevent="controlClick($event, element)" >
                        <van-popover placement="top" theme="dark"   style="max-width:200px"  :offset="[30,18]"  :actions="signDateActions" teleport=".doc-content" v-model:show="element.showPopover">
                            <template #action="{ action }">
                                <div class="custom-action" v-if="action.text !=='删除'" @touchend="(e)=>controlMenuClick(e,element,action.text=='批量应用'?true:false)">
                                    <SvgIcon :name="action.icon" size="16" color="#999"></SvgIcon>
                                    <span style="margin-left:4px;">{{ action.text }}</span>
                                </div>
                                <div class="custom-action" v-if="action.text =='删除'" @touchend="controlDelete">
                                    <SvgIcon :name="action.icon" size="16" color="#999"></SvgIcon>
                                    <span style="margin-left:4px;">{{ action.text }}</span>
                                </div>
                            </template>
                        </van-popover>
                        <template v-if="element.setting">
                            <div class="setting-sign-date">
                                <img :src="'data:image/png;base64,' + element.value" />
                            </div>
                        </template>
                        <template v-else-if="element.format">
                            <div class="setting-sign-date">
                                <span>{{ element.format }}</span>
                            </div>
                        </template>
                        <span v-else>{{ element.title }}</span>
                </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Number"
                @click="controlClick($event, element)">
                <van-field ref="lineInput" v-model:value="element.value" @blur="inputBlur" type="number" :style="['width:100%;height:100%',
                    'font-size:' + element.style.fontSize / 75 + 'rem',
                    'text-align:' + element.style.textAlign,
                    'pointer-events: none;']" :controls="false" :stringMode="true" :placeholder="element.placeholder"
                    :bordered="false" @change="valueChange(element)" />
            </div>
           
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Date"
                @touchend.prevent="controlClick($event, element)">
                <!-- <span>{{element.name}}</span> -->
                <van-calendar ref="datePicker" v-model:value="element.value" :placeholder="element.format"
                    :format="element.format" :value-format="element.format" class="control-date-picker" :style="['width:100%;height:100%; resize: none;',
                        'font-size:' + element.style.fontSize / 75 + 'rem',
                        'text-align:' + element.style.textAlign,
                        'fontFamily:' + element.style.fontFamily,
                        dbclick ? '' : 'pointer-events: none;'
                    ]" :open="datePickerOpen" @blur="inputBlur" @change="dateChange(element)" :bordered="false">
                    <template #suffixIcon>
                    </template>
                </van-calendar>
            </div>
            <div class="control-handles">
                <div class="handle handle-top-left receive-color this-click"
                    v-on:mousedown.salf="controlZoomMousedown($event, element, 1)"
                    v-if="element.controlClick && element.zoom"></div>
                <!-- <template v-if="element.delete">
                    <div class="closeControl handle-top-right receive-color this-click" @touchend="controlDelete"
                        v-if="element.controlClick && !element.disabled">
                        <SvgIcon name="delete" :size="50"></SvgIcon>
                    </div>
                </template>
                <template v-else>
                    <div class="closeControl handle-top-right receive-color this-click" v-on:click.stop="clearSeal"
                        v-if="element.setting && !element.close">
                    </div>
                </template> -->
                <div class="handle handle-bottom-left receive-color this-click"
                    v-on:mousedown.salf="controlZoomMousedown($event, element, 2)"
                    v-if="element.controlClick && element.zoom"></div>
                <div class="handle handle-bottom-right receive-color this-click"
                    v-on:mousedown.salf="controlZoomMousedown($event, element, 3)"
                    v-if="element.controlClick && element.zoom"></div>
            </div>
            <div class="control-required" v-if="element.required">
                *
            </div>
        </span>
        
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch,onMounted, onBeforeUnmount } from 'vue';
import { getColor } from "../data/ReceiveColor"
import { ControlType, isFillControl, CanvasZoom } from "../data/ControlData"
import { moveRange, currentPosition, currentPositionReverse } from "../data/ControlerMoveRange"
import { Notify } from 'vant';

interface Action {
    text:string;
    icon?:string;
    action?:Function
}

export default defineComponent({
    name: 'control-item',
    components: {

    },
    props: {
        element: {
            type: Object,
            default: function () {
                return {}
            }
        },
        doc: {
            type: Object
        },
        isSign: {
            type: Boolean,
            default: true
        },
        zoom: {
            type: Number,
            default: 1
        },
    },
    emits: ["controlMousedown", "controlDelete", "clearSeal", 'controlMenu', 'controlClick', 'refreshControlPosition'],
    setup(props: any, { emit }) {

        const cursorStyle = ref("cursor:move;");
        const zoomType = ref(0);
        const input = ref(null);
        const lineInput = ref<any>(null);
        const linesInput = ref<any>(null);
        const datePicker = ref<any>(null);
        const dbclick = ref(false);
        const datePickerOpen = ref(false);
        const showPopover = ref(false);
        const sealVisible = ref(true);
        const isDragging = ref(false);
        // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
        let baseUrl = import.meta.env.VITE_APP_API_BASE_URL;
        const baseImgWidth = window.innerWidth - 20;
        const aspecRatio = baseImgWidth / 800;
        const sealActions = ref(<Action[]>[
            { text: '批量应用',icon:'' },
            { text: '设置签章',icon:'' },
            { text: '删除', icon:''}
        ])
        const signatureActions = ref(<Action[]>[
            { text: '批量应用',icon:'' },
            { text: '设置签名',icon:'' },
        ])
        const signDateActions = ref(<Action[]>[
            { text: '批量应用' },
            { text: '设置格式' },
        ])

        const nowPoint = ref({
            x: 0,
            y: 0
        })
        const eventMove = ref({
            x: 0,
            y: 0
        });

        const actions = [
            { text: 'yyyy年MM月dd日' },
            { text: 'YYYY-MM-DD' },
            { text: 'YYYY/MM/DD' },
        ];
        watch(() => props.element.colorIndex,
            (newValue, oldValue) => {
                // ReceiveColorItem.value = getColor(newValue,null)
                console.log(newValue, '0000000000')
            })
        watch(() => props.element.showPopover,
            (newValue, oldValue) => {
                if(newValue){
                    setTimeout(()=>{
                        let pops = document.querySelectorAll('.van-popup');
                        pops&&pops.forEach((el:any)=>{
                            let elWidth = el.getBoundingClientRect().width;
                            if(el.offsetLeft < 0){
                                el.style.left = '0px';
                                let arrowEl = el.querySelectorAll('.van-popover__arrow')
                                if(arrowEl){
                                    arrowEl[0].style.left = '20%'
                                    arrowEl[0].style.transition = 'left 0.3s ease';
                                }
                            }
                            if(el.offsetLeft + elWidth > (window.innerWidth - 20)){
                                let arrowEl = el.querySelectorAll('.van-popover__arrow')
                                if(arrowEl){
                                    arrowEl[0].style.left = '70%'
                                    arrowEl[0].style.transition = 'left 0.3s ease';
                                }
                                el.style.left = (window.innerWidth - 20 - elWidth) + 'px';
                                el.style.right = '0px';
                                // el.style.left = 'auto';
                            }
                            if(el.offsetLeft > 0  && el.offsetLeft + elWidth < (window.innerWidth - 20)){
                                el.style.left = el.offsetLeft + 'px';
                                let arrowEl = el.querySelectorAll('.van-popover__arrow')
                                if(arrowEl){
                                     arrowEl[0].style.left = '50%'
                                     arrowEl[0].style.transition = 'left 0.3s ease';
                                }
                            }
                        })
                    },100)
                    
                }
            })
        watch(() => props.element.controlClick,
            (newValue, oldValue) => {
                loadAction()
            })
            loadAction()
            function loadAction () {
                if((props.isSign &&  props.element.originType == 3) || ( !props.isSign && props.element.originType !=1)){
                    if(props.element.controlClick && !props.element.disabled && props.element.delete){
                        sealActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '设置签章',icon:'seal-light' },
                            { text: '删除', icon:'delete-file'}
                        ]
                        signatureActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '设置签名',icon:'signature-light' },
                            { text: '删除',icon:'delete-file'}
                        ]
                        signDateActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '设置格式',icon:'format' },
                            { text: '删除',icon:'delete-file'}
                        ]
                    }else{
                        sealActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '设置签章',icon:'seal' },
                        ]
                        signatureActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '设置签名',icon:'signature-light' },
                        ]
                        signDateActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '设置格式',icon:'format' },
                        ]
                    }
                   
                        
                }else{
                    if(props.element.controlClick && !props.element.disabled && props.element.zoom){
                        sealActions.value = [
                            { text: '设置签章',icon:'seal-light' },
                            { text: '删除',icon:'delete-file'}
                        ]
                        signatureActions.value = [
                            { text: '设置签名',icon:'signature-light' },
                            { text: '删除',icon:'delete-file'}
                        ]
                        signDateActions.value = [
                            { text: '批量应用',icon:'batch' },
                            { text: '删除',icon:'delete-file'}
                        ]
                    }else{
                        sealActions.value = [
                            { text: '设置签章', icon:'seal-light' },
                        ]
                        signatureActions.value = [
                            { text: '设置签名',icon:'signature-light' },
                        ]
                        signDateActions.value = [
                            { text: '设置格式',icon:'format' },
                        ]
                    }
                  
                }
            }
        

        onMounted(()=>{
            document.addEventListener('click', function(event) {
            // 获取点击的目标元素
                var target = event.target;
                // 检查点击的标签类名是否为 "control-item"


                if (!target?.classList.contains('control-item')) {
                    props.element.showPopover = false;
                }
            });
        })
        onBeforeUnmount(()=>{
            document.removeEventListener('click', function(event) {
                var target = event.target;
                // 检查点击的标签类名是否为 "control-item"
                if (!target?.classList.contains('control-item')) {
                    props.element.showPopover = false;
                }
            })
        })

        function onSelectFormDateType(val){

        }
        function controlDelete(e: any) {
            //console.log("controlDelete click",e);
            emit('controlDelete', props.element)
        }
        function clearSeal() {
            emit('clearSeal', props.element)
        }
        function controlMovemousedown(e: any, element: any, falg?: boolean) {
            if (element.disabled) {
                return;
            }
            props.element.controlClick = true;
            props.element.showPopover = true;
            // emit('controlMousedown', element, input.value, e)
            emit('controlMousedown', props.element)
            if (!element.move) {
                return;
            }

            nowPoint.value = {
                x: e.touches[0].clientX,
                y: e.touches[0].clientY
            }
            document.addEventListener('touchmove', moveControl)
            //松开鼠标清除移动函数事件
            document.addEventListener('touchend', moveEnd)

        }
        function moveControl(e: any) {
            e.stopPropagation();
            // if(!element) return; 
            //当前鼠标位置
            var nowPos = {
                x: e.touches[0].clientX,
                y: e.touches[0].clientY,
            }
            //计算鼠标移动过的距离
            var dis = {
                x: nowPos.x - nowPoint.value.x,
                y: nowPos.y - nowPoint.value.y,
            }
            let zoomDis ={
                x:dis.x / props.zoom,
                y:dis.y / props.zoom,
            }

            nowPoint.value = nowPos;
            // eventMove.value.x = dis.x;
            // eventMove.value.y = dis.y;
            props.element.position.left += zoomDis.x;
            props.element.position.top += zoomDis.y;
            // props.element.position.left += dis.x
            // props.element.position.top += dis.y
            // props.element.position.left = newOffset.x;
            // props.element.position.top = newOffset.y;
            isDragging.value = true;
            Notify.clear()
            const target = props.doc.targets[props.element.position.page];
            let showXRange = false;
            let showYRange = false;
            let showChopYRange = false; 
            if (props.element.controlType === 'chop-stamp') {
                let batchMaxY = target.transformHeight + (target.page + 1) * CanvasZoom.space + props.element.minTarget.minHeight - props.element.size.height;
                if(props.element.position.top > batchMaxY){
                    showChopYRange = true;
                    props.element.position.top = batchMaxY;
                }else{
                    showChopYRange = false;
                }
                props.doc.images.map(v=>{
                    if(v.page == props.element.position.page && showChopYRange ){
                    v.showChopStampRange = true;
                    v.rangHeight = props.element.minTarget.minHeight;
                    Notify({ type: 'warning', message: '关联的文档中存在小于当前位置的页面，请在限制区域内指定位置' })
                    }else{
                    v.showChopStampRange = false
                    }
                })
            }
            if(props.element.isBatch && props.element.controlType !== 'chop-stamp'){
                if( props.element.position.left > ((props.element.minTarget.minWidth -  props.element.size.width) + ( (props.doc.maxWidth - target.width) / 2))){
                    showXRange = true;
                    props.element.position.left = (props.element.minTarget.minWidth -  props.element.size.width) + ( (props.doc.maxWidth - target.width) / 2)
                }else{
                    showXRange = false;
                }
                let batchMaxY = target.transformHeight + (target.page + 1) * CanvasZoom.space + props.element.minTarget.minHeight - props.element.size.height;
                if(props.element.position.top > batchMaxY){
                    showYRange = true;
                    props.element.position.top = batchMaxY;
                }else{
                    showYRange = false;
                }
            }
            props.doc.images.map(v=>{
                if(v.page == props.element.position.page && (showXRange || showYRange) ){
                    v.showRange = true;
                    Notify({ type: 'warning', message: '关联的文档中存在小于当前位置的页面，请在限制区域内指定位置' })
                }else{
                    v.showRange = false
                }
            })
        }
        function moveEnd(e: any) {
            e.stopPropagation();
            // if (props.element.controlType == 'chop-stamp') {
            //     props.element.position.left
            // }
            // emit('refreshControlPosition',props.element)
            document.removeEventListener('touchmove', moveControl);
            document.removeEventListener('touchend', moveEnd);
            props.doc.images.map(v=>{
                v.showRange = false;
                v.showChopStampRange = false
            })
           
        }
        function calculateControlRang(element:any, refresh = true) {
            let x = props.element.position.left + eventMove.value.x;
            let y = props.element.position.top + eventMove.value.y;

            const opt: any = {
                x: x,
                y: y,
                pageSize: props.doc.pageSize,
                size: props.element.size,
                currentPage: props.element.position.page,
                allHeight:props.doc.imageAllHeight,
                //所有页面最大的宽度
                maxWidth: props.doc.maxWidth,
                isBatch:element.isBatch,
                minTarget:element?.minTarget
            };
            moveRange(opt, props.doc.targets, element.isBatch);
            if (element.controlType == 'chop-stamp') {
                const elementTarget = props.doc.targets[opt.currentPage];
                const elementOffsetWidth = ( props.doc.maxWidth - elementTarget.width) / 2;
                opt.x = (elementOffsetWidth + elementTarget.width) -  element.size.width
            }
            props.element.position.left = opt.x;
            props.element.position.top = opt.y;
            props.element.position.page = opt.currentPage;
            if(!element.isBatch){
                props.element.propertyVoList = [
                    {
                        controlId: element.id,
                        id:'relation_doc_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        propertyType:"relation_doc",
                        propertyValue:props.doc.signRuDocId,
                    },
                    {
                        controlId: element.id,
                        id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        propertyType:"page_config",
                        propertyValue:'custom',
                    },
                    {
                        controlId: element.id,
                        id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        propertyType:"custom",
                        propertyValue:opt.currentPage+1+'',
                    }
                ]
            }
            if(refresh){
                emit('refreshControlPosition', props.element)
            }
        }
        function controlMouseover(e: any) {
             e.preventDefault();
            //防止鼠标快速划过控件显示
            moveTimeout.value = setTimeout(function () {
                console.log("controlMouseover");
                moveIn.value = true;
            }, 100)
        }
        function mouseoverMouseout(e: any, element) {
            // e.stopPropagation();
            isDragging.value = false;
            calculateControlRang(element)
            if (!moveIn.value) {
                clearTimeout(moveTimeout.value)
            }
            moveIn.value = false;
        }
        function controlZoomMousedown(e: any, element: any, type: number) {
            //移动方位不一致，位置计算也不相同  通过type区分
            zoomType.value = type;
            //点击当前坐标点
            nowPoint.value = {
                x: e.clientX,
                y: e.clientY
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
        function zoomControl(e: any) {
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
                width: 0,
                height: 0,
                left: 0,
                top: 0
            }
            if (zoomType.value == 3) {
                //横纵坐标 直接+
                nowPon.width = props.element.size.width + dis.x;
                nowPon.height = props.element.size.height + dis.y;
                nowPon.top = props.element.position.top;
                nowPon.left = props.element.position.left;
            }
            if (zoomType.value == 2) {
                nowPon.width = props.element.size.width + dis.x * -1;
                nowPon.height = props.element.size.height + dis.y;
                nowPon.left = props.element.position.left + dis.x;
                nowPon.top = props.element.position.top;
            }
            if (zoomType.value == 1) {
                nowPon.width = props.element.size.width + dis.x * -1;
                nowPon.left = props.element.position.left + dis.x;
                nowPon.height = props.element.size.height + dis.y * -1;
                nowPon.top = props.element.position.top + dis.y;
            }
            if (nowPon.width < props.element.size.minWidth) {
                nowPon.width = props.element.size.minWidth
                nowPon.left = props.element.position.left;
            }

            if (nowPon.height < props.element.size.minHeight) {
                nowPon.height = props.element.size.minHeight
                nowPon.top = props.element.position.top;
            }
            if (nowPon.left < 0) {
                nowPon.left = 0;
            }
            if (nowPon.top < 0) {
                nowPon.top = 0;
            }

            if (nowPon.width > CanvasZoom.width) {
                nowPon.width = CanvasZoom.width;
            }
            if (nowPon.height > CanvasZoom.height) {
                nowPon.height = CanvasZoom.height;
            }
            props.element.size.width = nowPon.width
            props.element.size.height = nowPon.height;
            props.element.position.left = nowPon.left;
            props.element.position.top = nowPon.top;

        }
        function controlZoomMouseover(opt: any) {
            //emit('moveDisabled',opt);
        }
        function inputMousedown(e: any) {
        }
        //控件点击
        function controlClick($event: any, element) {
            $event.preventDefault()
            if (isDragging.value) {
                return
            }
            if (props.isSign) {
                if (element.controlType == 'sign-date') {
                    emit('controlClick', $event, element)
                }
                if (element.controlType == 'seal' || element.controlType == 'chop-stamp') {
                    emit('controlClick', $event, element)
                }
                if (element.controlType == 'signature') {
                    emit('controlClick', $event, element)
                }

            } else {
                if ((element.originType == 2 || element.originType == 3) && element.controlType == 'sign-date') {
                    emit('controlClick', $event, element)
                }
                if ((element.originType == 2 || element.originType == 3) && (element.controlType == 'seal' || element.controlType == 'chop-stamp') ){
                    emit('controlClick', $event, element)
                }
                if ((element.originType == 2 || element.originType == 3) && element.controlType == 'signature') {
                    emit('controlClick', $event, element)
                }
            }
        }
       
        function controlMenuClick($event, element, isBatch:boolean=false) {
            $event.preventDefault()
            if (isDragging.value) {
                return
            }
            if (props.isSign) {
                if (element.controlType == 'sign-date') {
                    emit('controlMenu', $event, element,isBatch )
                }
                if (element.controlType == 'seal' || element.controlType == 'chop-stamp') {
                    emit('controlMenu', $event, element,isBatch )
                }
                if (element.controlType == 'signature') {
                    emit('controlMenu', $event, element,isBatch )
                }

            } else {
                if ((element.originType == 2 || element.originType == 3) && element.controlType == 'sign-date') {
                    emit('controlMenu', $event, element,isBatch )
                }
                if ((element.originType == 2 || element.originType == 3) && (element.controlType == 'seal' || element.controlType == 'chop-stamp') ){
                    emit('controlMenu', $event, element,isBatch )
                }
                if ((element.originType == 2 || element.originType == 3) && element.controlType == 'signature') {
                    emit('controlMenu', $event, element,isBatch )
                }
            }

        }
        function formItemFocus() {
            if (props.element.disabled) {
                return;
            }
            if (props.element.controlOperationType == 'sign') {

            } else {
                cursorStyle.value = "cursor:text;";
                if (lineInput.value) {
                    lineInput.value.focus();
                }
                if (linesInput.value) {
                    linesInput.value.focus();
                }
                if (datePicker.value) {
                    datePicker.value.focus();
                    datePickerOpen.value = true;
                }
                dbclick.value = true;
            }
        }
        function inputBlur() {
            cursorStyle.value = "cursor:move;";
            props.element.controlClick = false;
            if (datePicker.value) {
                datePickerOpen.value = false;
            }
            dbclick.value = false;
        }
        function dateChange(element: any) {
            datePickerOpen.value = false;
            valueChange(element);
        }
        const ReceiveColorItem: any = ref(null);
        //debugger;
        // if(props.element.user){
        // 	var index = props.element.user.index | props.element.user.signerId;
        // 	ReceiveColorItem.value = getColor(props.element.user.index,null)

        // }
        // if(props.element.controlOperationType == 'sign'){
        // 	ReceiveColorItem.value = getColor(-2,null)
        // }
        if (!ReceiveColorItem.value) {
            ReceiveColorItem.value = getColor(-2, null)
        }

        // console.log(ReceiveColorItem.value,props.element.user);
        const moveIn = ref(false);
        const moveTimeout: any = ref(null);

        function settingSeal(e: any) {
            props.element.showModal = true;
        }
        function initData() {
            if (props.element.disabled) {
                cursorStyle.value = "cursor: not-allowed;";
            } else {
                if (props.element.controlOperationType == 'sign') {
                    cursorStyle.value = "cursor: pointer;";
                } else {
                    cursorStyle.value = "cursor: move;";
                }
            }
            //日期格式回显需要特殊处理
            if (props.element.controlType == ControlType.Date) {
                if (props.element.value && props.element.value != '') {
                    // props.element.value = dayjs(props.element.value);
                }
            }
            console.log("initData:contr计算前:", props.element);
            // props.element.position.top = currentPositionReverse(props.element.position.top, props.element.position.page);
            console.log("initData:contr计算后:", props.element);
            //import dayjs from 'dayjs';
            //cursor: pointer;
        }
        function addSeal(seal: any) {
            //props.element.dataId = dataId;
            props.element.data = seal;
            props.element.setting = true;

        }
        //如果必填   有值改变将error清除掉
        function valueChange(element: any) {
            if (element.required) {
                element.error = false;
            }
        }
        // initData();
        return {
            controlMovemousedown,
            inputBlur,
            cursorStyle, inputMousedown,
            getColor,
            ReceiveColorItem, controlZoomMousedown,
            controlZoomMouseover,
            input, formItemFocus,
            linesInput, lineInput,
            controlDelete, ControlType,
            dbclick, datePicker, datePickerOpen,
            dateChange,
            controlMouseover, mouseoverMouseout, moveIn,
            settingSeal, sealVisible, addSeal,
            valueChange, isFillControl, clearSeal,
            controlMenuClick,
            baseUrl,
            showPopover,
            sealActions,
            signatureActions,
            signDateActions,
            actions,
            onSelectFormDateType,
            controlClick,
            calculateControlRang,
        }
    }
})
</script>

<style lang="less" scoped>
.resrun-control {
    user-select: none;
    cursor: move;
    position: absolute;
}

.resrun-control :hover {}

.signer-tips {
    width: 100%;
    border: 1px solid #e3e3e3;
    background-color: #f8f8f8;
    position: absolute;
    top: calc(100% + 10px);
    text-align: center;
}

.control-required {
    position: absolute;
    height: 100%;
    left: -10px;
    top: 0;
    line-height: 100%;
    display: flex;
    align-items: center;
    color: red
}

.control-item {
    position: relative;
    font-size: 20px;

    :deep(.van-cell) {
        padding: 4px 8px;
        background-color: rgba(0, 160, 232, 0.5);
    }

    :deep(.van-field__body) {
        height: 100%;
        font-size: 12px;
        text-align: center;

        .van-field__control {
            text-align: center;
        }
    }
}

.setting-sign {
    position: absolute;
    bottom: 0;
    width: 100%;
    text-align: center;
    line-height: 30px;
    background-color: #eee;
}

.control-item.default {
    width: 100%;
    height: 100%;
    border: 3px;
    border-style: dashed;
    border-radius: 1px;
    border-color: var(--translucent);
    background-color: rgb(var(--background-rgb), 0.5)
}
.control-item {
    :deep(.van-popover__wrapper){
        height:0;
        width:0;
    }
}
.doc-content{
    
    .custom-action{
        display:flex;
        font-size:24px;
        flex-direction: column;
        align-items: center
    }
}


.control-item.click {
    width: 100%;
    height: 100%;
    border: 3px;
    border-radius: 1px;
    border-style: solid;
    border-color: var(--translucent);
    background-color: rgb(var(--background-rgb), 0.1)
    // background-color: #fff;
}

.control-item label {
    color: #a9a9a9;
    padding-left: 5px;
}

.control-handles {
    .handle {
        width: 5px;
        height: 5px;
        background-color: var(--primary);
        position: absolute;
        z-index: 999;
    }

    .closeControl {
        width: 30px;
        height: 30px;
        position: absolute;
        font-size: 20px;
        cursor: default;
        background-color: #fff;
        display: flex;
        border-radius: 50%;
        z-index: 999;
    }

    .handle-top-left {
        top: -2px;
        left: -2px;
        cursor: nw-resize;
    }

    .handle-top-right {
        top: -7px;
        right: -7px;

        svg {
            width: 100% !important;
            height: 100% !important;
        }
    }

    .handle-bottom-left {
        bottom: -2px;
        left: -2px;
        cursor: sw-resize;
    }

    .handle-bottom-right {
        bottom: -2px;
        right: -2px;
        cursor: se-resize;
    }
}

.control-signature {
    display: flex;
    // flex-direction: column;
    text-align: center;
    align-items: center;
    font-size: 30px;
    white-space: nowrap;
    justify-content: center;

    span {
        pointer-events: none;
        display: block;
    }

    .signature-img {
        width: 100%;
        height: 100%;
    }

    span:nth-child(1) {
        //line-height: 30px;
        // height: 40px;
        padding-top: 10px;
    }
}

.control-seal,  .control-chop-stamp  {
    display: flex;
    flex-direction: column;
    text-align: center;
    justify-content: center;
    font-size:30px;

    span {
        display: block;
        pointer-events: none;
    }

    span:nth-child(1) {
        // height: 40px;
        padding-top: 5px;
    }

    // span:nth-child(2) {
    //     line-height: 30px;
    // }

    img {
        width: 100%;
        height: 100%;
    }
}

.seal-modal {
    width: 200px;

    .seal-modal-title {
        font-weight: 600;
        font-size: 14px;
        line-height: 30px;
    }

    .seal-item {
        border-style: dashed;
        border-color: #e0e0e0;
        border-width: 1px;
        padding: 20px 20px 0 20px;
        margin-bottom: 20px;
        cursor: pointer;

        .seal-img {
            width: 160px;
            height: 160px;
        }

        .seal-name {
            line-height: 30px;
            text-align: center;
        }
    }

    .seal-item:hover {
        border-color: #5589E2
    }
}

.setting-sign-date {
    white-space: nowrap;
    font-size: 28px;
}

.control-sign-date,
.control-date {
    font-size:30px;
    display: flex;
    flex-direction: column;
    text-align: center;
    justify-content: center;

    span {
        line-height: 30px;
        pointer-events: none;
    }
}
:deep(.van-popover__wrapper){
    width:100%;
    height:100%;
    display: inline;
}
.control-footer-action{
    position: absolute;
    white-space:nowrap;
    display: flex;
    background-color: rgba(37, 37, 44, 0.89);
    z-index:2020;
    .triangle{
        position: absolute;
        width: 20px;
        height: 20px;
        bottom: -8px;
        background-color: rgba(37, 37, 44, 0.89);
        left: 50%;
        transform: translate(-50%, 0px) rotate(45deg);
        // transform:  rotate(45deg);
    }
    :deep(.van-button){
        display: block;
        width:100%;
        border:none;
        height:200px;
        background-color: rgba(37, 37, 44, 0.89);
        color:#fff;
        font-size:32px;
        .van-button__text{
            display: flex;
            flex-direction: column;
            align-items: center;
        }
    }
}
:deep(.van-popover__action){
    white-space: nowrap;
}
.batch-icon{
    position:absolute;
    z-index: 9;
    font-size: 16px;
    background-color: rgba(0, 160, 232, 0.6);
}

</style>
<style>
.seal-popover .ant-popover-message-title {
    padding-left: 0px !important;
}
.doc-content .van-popover__action{
    height:110px;
    width:130px;
    padding:0;
    justify-content: center
}
.doc-content .van-popover__content{
    display: flex;
    white-space: nowrap;
    justify-content: center;
    padding:0 10px;
}
</style>

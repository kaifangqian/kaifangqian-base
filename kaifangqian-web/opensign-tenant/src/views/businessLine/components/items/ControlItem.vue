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
        <span :class="['resrun-control', 'resrun-control-move', 'this-click', 'pos-' + element.position.top]" :style="['left:' + element.position.left  + 'px;top:' + element.position.top + 'px;',
        '--primary:' + ReceiveColorItem.primary,
        '--translucent:' + [element.error ? 'red' : ReceiveColorItem.translucent],
        '--background:' + ReceiveColorItem.background,
        '--background-rgb:' + (element.originType == '1' ? ReceiveColorItem.backgroundRgb : ''),
        'width:' + element.size.width + 'px',
        // 'transform:scale('+ zoom / 100 +')',
        'transform-origin: 0 0',
        'height:' + element.size.height + 'px',
        'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
            cursorStyle]" @mousedown="controlMovemousedown($event, element)" @mouseenter.stop="controlMouseover"
            @mouseleave="mouseoverMouseout" @dblclick="formItemFocus">
            <a-tag :color="getColor(element.colorIndex, 'background')" v-if="element.isBatch"
                style="position:absolute;left:0;z-index:999">批量</a-tag>
            <slot></slot>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default',
            ]" v-if="element.controlType === ControlType.LineText" @click.stop="controlClick($event, element)">
                <Input ref="lineInput" v-model:value="element.value" @blur="inputBlur" :style="['width:100%;height:100%',
                    'font-size:' + element.style.fontSize + 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    'padding:0',
                    'pointer-events: none;']" @change="valueChange(element)" :placeholder="element.placeholder"
                    :bordered="false"></Input>
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.MultilineText"
                @click="controlClick($event, element)">
                <a-textarea ref="linesInput" v-model:value="element.value" :placeholder="element.placeholder" :style="['width:100%;height:100%;padding:0;resize: none;',
                    'font-size:' + element.style.fontSize + 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    'pointer-events: none;']" @blur="inputBlur" @change="valueChange(element)" :bordered="false" />
            </div>
            <!-- 签名 -->
            <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown"> 
                <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Signature"
                    @click="controlClick($event, element)">
                    <template v-if="element.signatureId">
                        <img :src="baseUrl + '/file/downloadFileStream/' + element.signatureId" class="signature-img" />
                    </template>
                    <template v-else-if="element.signature">
                        <img :src="element.signature" class="signature-img" />
                    </template>
                    <template v-else-if="!element.setting">
                        <span>
                            <SvgIcon :name="element.icon" :size="30"></SvgIcon>
                        </span>
                        <span>{{ element.title }}</span>
                    </template>
                    <template v-else>
                        <img :src="element.seal.signature" :height="element.size.height" :width="element.size.width"
                            v-if="element.seal.sealStyle == 5" />
                        <img :src="element.seal.signature" :height="element.size.height" :width="element.size.height"
                            v-if="element.seal.sealStyle == 4" />
                    </template>
                </div>
                <template #overlay v-if="!isMoving">
                    <a-menu :style="['width:' + element.size.width + 'px']">
                        <a-menu-item @click="handleBatchUse(element)">
                            <a target="_blank" rel="noopener noreferrer" class="batch-text">
                                批量应用
                            </a>
                        </a-menu-item>
                    </a-menu>
                </template>
            </a-dropdown>
            <!-- 企业签章 -->
            <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown">
                <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Seal"
                    @click.stop="controlClick($event, element)">
                    <template v-if="element.sealId" class="seal-img">
                        <img :src="baseUrl + '/file/downloadFileStream/' + element.sealId" />
                    </template>
                    <template v-else-if="!element.setting">
                        <span>
                            <SvgIcon :name="element.icon" :size="40"></SvgIcon>
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
                <template #overlay v-if="!isMoving">
                    <a-menu>
                        <a-menu-item @click="handleBatchUse(element)">
                            <a target="_blank" rel="noopener noreferrer" class="batch-text">
                                批量应用
                            </a>
                        </a-menu-item>
                    </a-menu>
                </template>
            </a-dropdown>
            <!-- 骑缝章 -->
            <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown">
                <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.ChopStamp"
                    @touchend.prevent="controlClick($event, element)">
                    <template v-if="element.ridinSealId" class="chop-stamp-img">
                        <img :src="baseUrl + '/file/downloadFileStream/' + element.annexId" />
                    </template>
                    <template v-else-if="!element.setting">
                        <span>
                            <SvgIcon :name="element.icon" :size="40"></SvgIcon>
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
                <template #overlay v-if="!isMoving">
                    <a-menu>
                        <a-menu-item @click="handleBatchUse(element)">
                            <a target="_blank" rel="noopener noreferrer" class="batch-text">
                                批量应用
                            </a>
                        </a-menu-item>
                    </a-menu>
                </template>
            </a-dropdown>
            <!-- 签署日期 -->
            <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown">
                <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.SignDate"
                    @click="controlClick($event, element)">
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
                <template #overlay v-if="!isMoving">
                    <a-menu>
                        <a-menu-item @click="handleBatchUse(element)">
                            <a target="_blank" rel="noopener noreferrer" class="batch-text">
                                批量应用
                            </a>
                        </a-menu-item>
                        <a-divider />
                        <a-dropdown>
                            <a-menu-item >
                                <a target="_blank" rel="noopener noreferrer" class="batch-text">
                                    设置格式
                                </a>
                            </a-menu-item>
                            <template #overlay>
                                <a-menu @click="(e)=>handleSignDateFormat(e,element)">
                                    <a-menu-item key="yyyy年MM月dd日">
                                    <a href="javascript:;">yyyy年MM月dd日</a>
                                    </a-menu-item>
                                    <a-menu-item key="YYYY-MM-DD">
                                    <a href="javascript:;">YYYY-MM-DD</a>
                                    </a-menu-item>
                                    <a-menu-item key="YYYY/MM/DD">
                                        <a href="javascript:;">YYYY/MM/DD</a>
                                    </a-menu-item>
                                </a-menu>
                            </template>
                        </a-dropdown>
                    </a-menu>
                </template>
            </a-dropdown>

            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Number"
                @click="controlClick($event, element)"  :placeholder="element.placeholder">
                <InputNumber ref="lineInput" v-model:value="element.value" @blur="inputBlur" :style="['width:100%;height:100%;padding:0;',
                    'font-size:' + element.style.fontSize + 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    'pointer-events: none;']" :class="['control-text-'+ element.style.textAlign]" :controls="false" :stringMode="true" :placeholder="element.placeholder"
                    :bordered="false" @change="valueChange(element)" />
            </div>

            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
            element.controlClick ? 'click' : 'default']" v-if="element.controlType === ControlType.Date"
                @click="controlClick($event, element)">
                <!-- <span>{{element.name}}</span> -->
                <DatePicker ref="datePicker" v-model:value="element.value" :placeholder="element.placeholder || '请选择日期'"
                    :format="element.format" :value-format="element.format" class="control-date-picker" :style="['width:100%;height:100%; resize: none;',
                        'font-size:' + element.style.fontSize + 'px',
                        'text-align:' + element.style.textAlign,
                        'fontFamily:' + element.style.fontFamily,
                        dbclick ? '' : 'pointer-events: none;'
                    ]" :open="datePickerOpen" @blur="inputBlur" @change="dateChange(element)" :bordered="false">
                    <template #suffixIcon>
                    </template>
                </DatePicker>
            </div>
            <!-- <component  :is="ControlPage" v-if="ControlPage" :docPageSize="doc.pageSize" :element="element"
            @blur="inputBlur" @delControl="controlDelete" ref="controlRef"/> -->
            <Image v-if="element.controlType === ControlType.Image" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
            <CheckBox v-if="element.controlType === ControlType.CheckBox" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
            <Radio v-if="element.controlType === ControlType.Radio" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
            <DropdownList v-if="element.controlType === ControlType.DropdownList" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
            <div class="control-handles">
                <div class="handle handle-top-left receive-color this-click"
                    v-on:mousedown.self="controlZoomMousedown($event, element, 1)"
                    v-if="element.controlClick && element.zoom"></div>
                <template v-if="element.delete">
                    <div class="closeControl handle-top-right receive-color this-click" v-on:click.stop="controlDelete"
                        v-if="element.controlClick && !element.disabled">
                        <Icon color="red" size="18" icon="ant-design:close-circle-outlined" />
                    </div>
                </template>
                <template v-else>
                    <div class="closeControl handle-top-right receive-color this-click" v-on:click.stop="clearSeal"
                        v-if="element.setting && !element.close">
                        <Icon color="red" size="18" icon="ant-design:close-circle-outlined" />
                    </div>
                </template>


                <div class="handle handle-bottom-left receive-color this-click"
                    v-on:mousedown="controlZoomMousedown($event, element, 2)"
                    v-if="element.controlClick && element.zoom"></div>
                <div class="handle handle-bottom-right receive-color this-click"
                    v-on:mousedown="controlZoomMousedown($event, element, 3)"
                    v-if="element.controlClick && element.zoom"></div>
            </div>
            <div class="control-required" v-if="element.required">
                *
            </div>
            <div class="signer-info" :style="['background-color:rgb(' + getColor(element.colorIndex, 'backgroundRgb') + ')']" v-if="['signature', 'sign-date', 'seal', 'chop-stamp'].includes(element.controlType)">
              <span>{{element.signerInfo.signerType==1?'发起方':'接收方'+ element.signerInfo?.receiverOrder }}</span>
              <span>{{element.signerInfo.signerType==1? 
               ('-'+ element.signerInfo?.senderName + '-' + (element.signerInfo?.senderSignType==1?'自动盖章':element.signerInfo?.senderUserName))
              :(element.signerInfo?.senderName ? ('-'+element.signerInfo?.senderName):'-个人')}}</span>
            </div>
        </span>
      
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch, defineAsyncComponent } from 'vue';
import { Input, InputNumber, DatePicker, Popconfirm, InputSearch } from 'ant-design-vue';
import { getColor } from "../data/ReceiveColor"
import { Icon, SvgIcon } from '/@/components/Icon';
// import dayjs from 'dayjs';
import { ControlType, isFillControl, CanvasZoom } from "../data/ControlData"
import { moveRange, currentPosition, currentPositionReverse, recaculateBatchControlPosInPage} from "../data/ControlerMoveRange"
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';
import { getAppEnvConfig } from '/@/utils/env';
import { cloneDeep } from 'lodash-es';
import { getPopupContainer } from '/@/utils';
import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
import { useMessage } from '/@/hooks/web/useMessage';

let ControlPage : any = null; 
export default defineComponent({
    name: 'control-item',
    components: {
        Input, SvgIcon, Icon, InputSearch,
        InputNumber, DatePicker, Popconfirm,
        Image:createAsyncComponent(() => import('../controls/Image.vue')),
        CheckBox:createAsyncComponent(() => import('../controls/CheckBox.vue')),
        Radio:createAsyncComponent(() => import('../controls/Radio.vue')),
        DropdownList:createAsyncComponent(() => import('../controls/DropdownList.vue'))

    },
    props: {
        element: {
            type: Object,
            default: function () {
                return {}
            }
        },
        doc: {
            type: Object,
            default: function () {
                return {}
            }
        },
        isSign: {
            type: Boolean,
            default: false
        },
        isSubmitControl: {
            type: Boolean,
            default: false
        },
        isReload: {
            type: Boolean,
            default: false
        },
        zoom: {
            type: Number,
            default: 1
        },
    },
    emits: ["controlMousedown", "controlDelete", "clearSeal", 'controlClick', 'refreshControlPosition', 'controlBatchUse','signDateFormat','controlFocus'],
    setup(props: any, { emit }) {
        const cursorStyle = ref("cursor:move;");
        const zoomType = ref(0);
        const input = ref(null);
        const lineInput = ref<any>(null);
        const linesInput = ref<any>(null);
        const datePicker = ref<any>(null);
        const dbclick = ref(false);
        const datePickerOpen = ref(false);
        const sealVisible = ref(true);
        const isMoving = ref(false);
        const { VITE_GLOB_API_URL } = getAppEnvConfig();
        const baseUrl = ref(VITE_GLOB_API_URL);
        const moveElement = ref(null)
        const { createMessage: msg } = useMessage();
        const moveDis = ref({
            x: 0,
            y: 0
        })
        const mouseDownPos = ref({
            x: 0,
            y: 0
        })
        const nowPoint = ref({
            x: 0,
            y: 0
        })
        const eventMove = ref({
            x: 0,
            y: 0
        });
                
        watch(() => props.isReload,
            (newValue,) => {
              if(newValue){

              };
        })
        function controlDelete(e: any) {
            //console.log("controlDelete click",e);
            emit('controlDelete', props.element)
        }
        function clearSeal() {
            emit('clearSeal', props.element)
        }
        function controlMovemousedown(e: any, element: any, falg?: boolean) {
            e.preventDefault();
            if (['date','line-text','multiline-text','number','image','dropdown-list','radio','check-box'].includes(element.controlType)) {
              emit('controlFocus', element)
            }
            //console.log("control click",element.controlOperationType);
            if (element.disabled) {
                return;
            }
            moveElement.value = cloneDeep(element);
            props.element.controlClick = true;
            //emit('controlMousedown',element,input.value,e)
            emit('controlMousedown', element)
            if (!element.move) {
                return;
            }
            nowPoint.value = {
                x: e.clientX,
                y: e.clientY
            }
            mouseDownPos.value = {
                x: e.clientX,
                y: e.clientY
            }
            //offsetX:e.offsetX,
            //offsetY:e.offsetY,
            document.addEventListener('mousemove',moveControl)
            //松开鼠标清除移动函数事件
            document.addEventListener('mouseup',
                function () {
                    document.removeEventListener('mousemove', moveControl)
                    removeMousedown(element);
                    props.doc.images.map(v=>{
                      v.showRange = false
                      v.showChopStampRange = false;
                    })
                },
                {
                    // 只绑定一次事件
                    once: true,
                })
        }

        function dragWarning(){
            msg.warning('关联的文档中存在小于当前位置的页面，请在限制区域内指定位置')
        }
        function moveControl(e: any, element?:any) {
            isMoving.value = true;
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
            moveDis.value = {
                x: e.clientX - mouseDownPos.value.x,
                y: e.clientY - mouseDownPos.value.y,
            }

            nowPoint.value = nowPos;
            // eventMove.value.x = dis.x;
            // eventMove.value.y = dis.y;
            props.element.position.left += (dis.x / props.zoom);
            props.element.position.top += (dis.y / props.zoom );
            // removeMousedown();
            // 拖拽范围限制
            msg.destroy()
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
                  dragWarning()
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
                v.showRange = true
                dragWarning()
              }else{
                v.showRange = false
              }
            })

        }
        function removeMousedown(element) {
            isMoving.value = false;
            //document.removeEventListener('mousemove', setOffset)
            let x = props.element.position.left + eventMove.value.x;
            let y = props.element.position.top + eventMove.value.y;

            const opt: any = {
                x: x,
                y: y,
                pageSize: props.doc.pageSize,
                size: props.element.size,
                currentPage: 0,
                allHeight:props.doc.imageAllHeight,
                //所有页面最大的宽度
                maxWidth: props.doc.maxWidth,
                //pageLeft:0,
                isBatch:element.isBatch,
                minTarget:element?.minTarget
            };
            moveRange(opt, props.doc.targets, element.isBatch);
            if (element.controlType == 'chop-stamp') {
              const elementTarget = props.doc.targets[opt.currentPage];
              const elementOffsetWidth = ( props.doc.maxWidth - elementTarget.width) / 2;
              opt.x = (elementOffsetWidth + elementTarget.width) -   element.size.width
            }
            props.element.position.left = opt.x;
            props.element.position.top = opt.y;
            // props.element.position.top = pageLeft;
            props.element.position.page = opt.currentPage;
            props.element.showXRange = opt.showXRange;
            props.element.showYRange = opt.showYRange;
            // if(!props.doc.signReDocId){
            //     msg.warning('关联文档id缺失')
            // }
            
            if(!element.isBatch){
              props.element.propertyVoList = [
                  {
                      controlId: element.id,
                      id:'relation_doc_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                      propertyType:"relation_doc",
                      propertyValue:props.doc.signRuDocId || element.controlDocId,
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
            emit('refreshControlPosition', props.element, moveDis.value)
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
        function controlClick($event, element) {
            if ((element.originType == 2 || element.originType == 3) && element.controlType == 'sign-date') {
                emit('controlClick', $event, element)
            }
            if ((element.originType == 2 || element.originType == 3) && element.controlType == 'seal') {
                emit('controlClick', $event, element)
            }
            if ((element.originType == 2 || element.originType == 3) && element.controlType == 'signature') {
                emit('controlClick', $event, element)
            }
        }
        //批量设置
        function handleBatchUse(element) {
            emit('controlBatchUse', element)
        }
        //日期格式设置
        function handleSignDateFormat(e,element) {
            console.log(e,element,'点击---')
            element.format = e.key;
            if(element.isBatch){
                emit('signDateFormat', element)
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

        
        const moveIn = ref(false);
        const moveTimeout: any = ref(null);
        function controlMouseover(e: any) {
            //防止鼠标快速划过控件显示
            moveTimeout.value = setTimeout(function () {
                console.log("controlMouseover");
                moveIn.value = true;
            }, 100)
        }
        function mouseoverMouseout(e: any) {
            console.log("mouseoverMouseout");
            if (!moveIn.value) {
                clearTimeout(moveTimeout.value)
            }
            moveIn.value = false;
        }
        function settingSeal(e: any) {
            props.element.showModal = true;
        }
        function initData() {
            if (props.isSubmitControl) {
                return
            }
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
                    //props.element.value = dayjs(props.element.value);
                }
            }
            // console.log("initData:contr计算前:",props.element);
            // if(!['signature', 'sign-date', 'seal', 'chop-stamp'].includes(props.element.controlType)){
            //     props.element.position.top = currentPositionReverse(props.element.position.top, props.element.position.page);
            // }
            // console.log("initData:contr计算后:",props.element);
            //import dayjs from 'dayjs';
            //cursor: pointer;
            // if(props.element.controlType == ControlType.Radio){
            //   ControlPage  = defineAsyncComponent(() =>import('../controls/Radio.vue'))
            // }else if(props.element.controlType == ControlType.LineText){
            // //  ControlPage  = defineAsyncComponent(() =>import('../controls/LineText.vue'))
            //  ControlPage  = '';
            // }else if(props.element.controlType == ControlType.MultilineText){
            // //  ControlPage  = defineAsyncComponent(() =>import('../controls/MultilineText.vue'))
            //   ControlPage  = '';
            // }else if(props.element.controlType == ControlType.Number){
            // //  ControlPage  = defineAsyncComponent(() =>import('../controls/Number.vue'))
            //   ControlPage  = '';
            // }else if(props.element.controlType == ControlType.Date){
            // //  ControlPage  = defineAsyncComponent(() =>import('../controls/Date.vue'))
            //   ControlPage  = '';
            // }else if(props.element.controlType == ControlType.CheckBox){
            //   ControlPage  = defineAsyncComponent(() =>import('../controls/CheckBox.vue'))
            // }else if(props.element.controlType == ControlType.DropdownList){
            //   ControlPage  = defineAsyncComponent(() =>import('../controls/DropdownList.vue'))
            // }else if(props.element.controlType == ControlType.Image){
            //   ControlPage  = defineAsyncComponent(() =>import('../controls/Image.vue'))
            // }
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
        initData()
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
            dateChange, locale,
            controlMouseover, mouseoverMouseout, moveIn,
            settingSeal, sealVisible, addSeal,
            valueChange, isFillControl, clearSeal,
            controlClick,
            baseUrl,
            ControlPage,
            getPopupContainer,
            handleBatchUse,
            isMoving,
            handleSignDateFormat
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
    border: 1px;
    border-style: dashed;
    border-radius: 1px;
    // border-bottom:none;
    border-color: var(--translucent);
    // background-color: rgb(var(--background-rgb), 0.2)
}

.control-item.click {
    width: 100%;
    height: 100%;
    border: 1px;
    border-radius: 2px;
    border-style: solid;
    // border-bottom:none;
    border-color: var(--translucent);
    background-color: #fff;
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
        width: 18px;
        height: 18px;
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
    flex-direction: column;
    text-align: center;
    align-items: center;

    span {
        pointer-events: none;
        display: block;
        line-height:1;
    }

    .signature-img {
        width: 100%;
        height: 100%;
    }

    span:nth-child(1) {
        //line-height: 30px;
        // height: 40px;
        padding-top: 5px;
    }
}

.control-seal,
.control-chop-stamp {
    display: flex;
    flex-direction: column;
    text-align: center;
    justify-content: center;

    span {
        display: block;
        pointer-events: none;
    }

    span:nth-child(1) {
        height: 40px;
        padding-top: 5px;
    }

    span:nth-child(2) {
        line-height: 30px;
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

.control-sign-date,
.control-date {
    display: flex;
    flex-direction: column;
    text-align: center;
    justify-content: center;

    span {
        line-height: 30px;
        pointer-events: none;
    }
}
.control-dropdown{
    .ant-divider-horizontal{
        margin:0;
    }
}
.signer-info{
  border:1px solid #5589E2;
  border-top-style: dashed;
  text-align: center;
  padding:2px 10px;
  color:#fff;
  font-size:10px;
}
// 控件对齐方式
.control-text-right{
  :deep(.ant-input-number-input){
    text-align: right;
    padding:0;
  }
  :deep(.ant-picker-input input){
    font-size:inherit;
  }
}
.control-text-left{
  :deep(.ant-input-number-input){
    text-align: left;
    padding:0;
  }
  :deep(.ant-picker-input input){
    font-size:inherit;
  }
}
.control-text-center{
  :deep(.ant-input-number-input){
    text-align: center;
    padding:0;
    font-size:inherit;
  }
  :deep(.ant-picker-input input){
    font-size:inherit;
  }
}
</style>
<style lang="less">
.seal-popover .ant-popover-message-title {
    padding-left: 0px !important;
}

</style>

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
    <span
      :class="['resrun-control', 'resrun-control-move', 'pos-' + element.position.top ,'this-click',(element.sealId || element.signatureId)?'hasSignImage':'',isSign?'sign-control-dropdown':'']"
      :style="[
        'left:' + element.position.left + 'px;top:' + element.position.top + 'px;',
        '--primary:' + ReceiveColorItem.primary,
        '--translucent:' + [element.error ? 'red' : ReceiveColorItem.translucent],
        '--background:' + ReceiveColorItem.background,
        '--background-rgb:' + ReceiveColorItem.backgroundRgb,
        // 'transform: translate('+ element.offsetX +'px,'+(element.position.top)+'px);',
        'width:' + element.size.width + 'px',
        'transform-origin: 0 0',
        'height:' + element.size.height + 'px',
        'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
        cursorStyle,
      ]"
      @mousedown="controlMovemousedown($event, element)"
      @mouseenter.stop="controlMouseover"
      @mouseleave="mouseoverMouseout"
      @dblclick="formItemFocus"
    >
    <a-tag :color="getColor(element.colorIndex, 'background')" v-if="element.isBatch" style="position:absolute;left:0;z-index:999">批量</a-tag>
      <slot></slot>
      <div
        ref="input"
        :class="[
          'control-' + element.controlType,
          'control-item',
          'arow-content',
          element.controlClick ? 'click' : 'default',
        ]"
        v-if="element.controlType === ControlType.LineText"
        @click.stop="controlClick($event, element)"
      >
        <Input
          ref="lineInput"
          v-model:value="element.value"
          @blur="inputBlur"
          :style="[
            'width:100%;height:100%;padding:0',
            'font-size:' + element.style.fontSize + 'px',
            'text-align:' + element.style.textAlign,
            'fontFamily:' + element.style.fontFamily,
            'pointer-events: none;',
          ]"
          @change="valueChange(element)"
          :placeholder="element.placeholder"
          :bordered="false"
        ></Input>
      </div>
      <div
        ref="input"
        :class="[
          'control-' + element.controlType,
          'control-item',
          'arow-content',
          element.controlClick ? 'click' : 'default',
        ]"
        v-if="element.controlType === ControlType.MultilineText"
        @click="controlClick($event, element)"
      >
        <a-textarea
          ref="linesInput"
          v-model:value="element.value"
          :placeholder="element.placeholder"
          :style="[
            'width:100%;height:100%; resize: none;padding:0',
            'font-size:' + element.style.fontSize + 'px',
            'text-align:' + element.style.textAlign,
            'fontFamily:' + element.style.fontFamily,
            'pointer-events: none;',
          ]"
          @blur="inputBlur"
          @change="valueChange(element)"
          :bordered="false"
        />
      </div>
     
      <!-- 企业签章 -->
      <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown" :getPopupContainer="(triggerNode) => triggerNode.parentNode">
        <div
          ref="input"
          :class="[
            'control-' + element.controlType,
            'control-item',
            'arow-content',
            element.controlClick ? 'click' : 'default',
          ]"
          v-if="element.controlType === ControlType.Seal"
          
        >
          <template v-if="element.sealId">
            <img :src="baseUrl + '/file/downloadFileStream/' + element.annexId"  class="seal-img"/>
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

        <template #overlay v-if="!isMoving && (isSign || (isSign &&  element.originType==3) || ( !isSign && element.originType !=1))">
          <a-menu
            :style="[
              'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
            ]"
          >
            <a-menu-item @click="handleBatchUse(element)"  v-if="(isSign &&  element.originType==3) || ( !isSign && element.originType !=1)">
              <a target="_blank" rel="noopener noreferrer"  class="batch-text"> 批量应用 </a>
            </a-menu-item>
            <a-divider />
            <a-menu-item @click="controlClick($event, element)" v-if="isSign">
              <a target="_blank" rel="noopener noreferrer"   class="batch-text"> 设置签章 </a>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>

      <!-- 骑缝章 -->
      <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown" :getPopupContainer="(triggerNode) => triggerNode.parentNode">
        <div
          ref="input"
          :class="[
            'control-' + element.controlType,
            'control-item',
            'arow-content',
            element.controlClick ? 'click' : 'default',
          ]"
          v-if="element.controlType === ControlType.ChopStamp"
          
        >
          <template v-if="element.sealId">
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

        <template #overlay v-if="!isMoving && (isSign || (isSign &&  element.originType==3) || ( !isSign && element.originType !=1))">
          <a-menu
            :style="[
              'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
            ]"
          >
            <a-menu-item @click="handleBatchUse(element)"  v-if="(isSign &&  element.originType == 3) || ( !isSign && element.originType !=1)">
              <a target="_blank" rel="noopener noreferrer"  class="batch-text"> 批量应用 </a>
            </a-menu-item>
            <a-divider />
            <a-menu-item @click="controlClick($event, element)" v-if="isSign">
              <a target="_blank" rel="noopener noreferrer" class="batch-text"> 设置签章 </a>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
       <!-- 签名 -->
       <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown" :getPopupContainer="(triggerNode) => triggerNode.parentNode">
        <div
          ref="input"
          :class="[
            'control-' + element.controlType,
            'control-item',
            'arow-content',
            element.controlClick ? 'click' : 'default',
          ]"
          v-if="element.controlType === ControlType.Signature"
          
        >
          <template v-if="element.signatureId">
            <img
              :src="baseUrl + '/file/downloadFileStream/' + element.signatureId"
              class="signature-img"
            />
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
            <img
              :src="element.seal.signature"
              :height="element.size.height"
              :width="element.size.width"
              v-if="element.seal.sealStyle == 5"
            />
            <img
              :src="element.seal.signature"
              :height="element.size.height"
              :width="element.size.height"
              v-if="element.seal.sealStyle == 4"
            />
          </template>
        </div>

        <template #overlay v-if="!isMoving && (isSign || (isSign &&  element.originType==3) || ( !isSign && element.originType !=1))">
          <a-menu
            :style="[
              'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
            ]"
          >
            <a-menu-item @click="handleBatchUse(element)"  v-if="(isSign &&  element.originType==3) || ( !isSign && element.originType !=1)">
              <a target="_blank" rel="noopener noreferrer"  class="batch-text"> 批量应用 </a>
            </a-menu-item>
            <a-divider />
            <a-menu-item @click="controlClick($event, element)" v-if="isSign">
              <a target="_blank" rel="noopener noreferrer" class="batch-text"> 设置签名 </a>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
      <!-- 签署日期 -->
      <a-dropdown placement="bottomLeft" overlayClassName="control-dropdown" :getPopupContainer="(triggerNode) => triggerNode.parentNode">
        <div
          ref="input"
          :class="[
            'control-' + element.controlType,
            'control-item',
            'arow-content',
            element.controlClick ? 'click' : 'default',
          ]"
          v-if="element.controlType === ControlType.SignDate"
         
        >
          <template v-if="element.setting">
            <div class="setting-sign-date">
              <img :src="'data:image/png;base64,' + element.value" />
            </div>
          </template>

          <template v-else-if="element.format">
            <div class="setting-sign-date">
              <span>{{ isSign ? element.today : element.format }}</span>
            </div>
          </template>
          <span v-else>{{ element.title }}</span>
        </div>

        <template #overlay v-if="!isMoving && ((isSign &&  element.originType == 3) || ( !isSign && element.originType !=1))">
          <a-menu
            :style="[
              'background-color:rgba(' + getColor(element.colorIndex, 'backgroundRgba') + ')',
            ]"
          >
            <a-menu-item @click="handleBatchUse(element)"  v-if="(isSign &&  element.originType == 3) || ( !isSign && element.originType !=1)">
              <a target="_blank" rel="noopener noreferrer"  class="batch-text"> 批量应用 </a>
            </a-menu-item>
            <a-divider />
            <a-dropdown>
                <a-menu-item v-if="(isSign && element.originType == 3) || ( !isSign && element.originType !=1)">
                    <a target="_blank" rel="noopener noreferrer"  class="batch-text">
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

      <div
        ref="input"
        :class="[
          'control-' + element.controlType,
          'control-item',
          'arow-content',
          element.controlClick ? 'click' : 'default',
        ]"
        v-if="element.controlType === ControlType.Number"
        @click="controlClick($event, element)"
      >
        <InputNumber
          ref="lineInput"
          v-model:value="element.value"
          @blur="inputBlur"
          :style="[
            'width:100%;height:100%;padding:0',
            'font-size:' + element.style.fontSize + 'px',
            'fontFamily:' + element.style.fontFamily,
            'text-align:' + element.style.textAlign,
            'pointer-events: none;',
            'display: flex;',
            'align-items: center;',
          ]"
          :class="['control-text-'+ element.style.textAlign]"
          :controls="false"
          :stringMode="true"
          :placeholder="element.placeholder"
          :bordered="false"
          @change="valueChange(element)"
        />
      </div>

      <div
        ref="input"
        :class="[
          'control-' + element.controlType,
          'control-item',
          'arow-content',
          element.controlClick ? 'click' : 'default',
        ]"
        v-if="element.controlType === ControlType.Date"
        @click="controlClick($event, element)"
      >
        <DatePicker
          ref="datePicker"
          v-model:value="element.value"
          :placeholder="element.format"
          :format="element.format?.toUpperCase()"
          :value-format="element.format?.toUpperCase()"
          :class="['control-date-picker','control-text-'+ element.style.textAlign]"
          :style="[
            'width:100%;height:100%; resize: none;padding:0',
            'font-size:' + element.style.fontSize + 'px',
            'text-align:' + element.style.textAlign,
            'fontFamily:' + element.style.fontFamily,
            'fontFamily:' + element.style.fontFamily,
            dbclick ? '' : 'pointer-events: none;',
          ]"
          :open="datePickerOpen"
          @blur="inputBlur"
          @change="dateChange(element)"
          :bordered="false"
        >
          <template #suffixIcon> </template>
        </DatePicker>
      </div>
<!-- 
      <component v-if="ControlPage" :docPageSize="doc.pageSize" :element="element"
      @blur="inputBlur" @delControl="controlDelete" ref="controlRef"/>   -->

      <Image v-if="element.controlType === ControlType.Image" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
      <CheckBox v-if="element.controlType === ControlType.CheckBox" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
      <Radio v-if="element.controlType === ControlType.Radio" :docPageSize="doc.pageSize" :element="element"  @blur="inputBlur" @delControl="controlDelete" />
      <DropdownList v-if="element.controlType === ControlType.DropdownList" :docPageSize="doc.pageSize" :element="element"   @delControl="controlDelete" />


      <component  :is="ControlPage" v-if="ControlPage" :docPageSize="doc.pageSize" :element="element"
      @blur="inputBlur" @delControl="controlDelete" ref="controlRef"/>
      
      <!-- :open="datePickerOpen" -->
      <div class="control-handles">
        <div
          class="handle handle-top-left receive-color this-click"
          v-on:mousedown="controlZoomMousedown($event, element, 1)"
          v-if="element.controlClick && element.zoom"
        >
        </div>
        <!-- <div class="closeControl handle-top-right receive-color this-click"
					v-on:click.stop="controlDelete" v-if="element.controlClick && !element.disabled">
					<Icon color="red" size="18" icon="ant-design:close-circle-outlined"/>
				</div> -->

        <template v-if="element.delete">
          <div
            class="closeControl handle-top-right receive-color this-click"
            v-on:click.stop="controlDelete"
            v-if="element.controlClick && !element.disabled"
          >
            <Icon color="red" size="18" icon="ant-design:close-circle-outlined" />
          </div>
        </template>

        <template v-else>
          <div
            class="closeControl handle-top-right receive-color this-click"
            v-on:click.stop="clearSeal"
            v-if="element.setting && !element.close"
          >
            <Icon color="red" size="18" icon="ant-design:close-circle-outlined" />
          </div>
        </template>

        <div
          class="handle handle-bottom-left receive-color this-click"
          v-on:mousedown="controlZoomMousedown($event, element, 2)"
          v-if="element.controlClick && element.zoom"
        >
        </div>
        <div
          class="handle handle-bottom-right receive-color this-click"
          v-on:mousedown="controlZoomMousedown($event, element, 3)"
          v-if="element.controlClick && element.zoom"
        >
        </div>
      </div>
      <div class="control-required" v-if="element.required"> * </div>
      <div class="signer-info" :style="['background-color:rgb(' + getColor(element.colorIndex, 'backgroundRgb') + ')']" v-if="loadSignerInfo(element) && !isSign">
          <span v-if="element.signerInfo.signerType == 1">
            {{element.signerInfo.startName + '-'+ element.signerInfo.nodeName + '-' + (element.signerInfo?.senderSignType==1?'自动盖章':element.signerInfo?.senderUserName)}}</span>
          <span v-if="element.signerInfo.signerType == 2">
            {{element.signerInfo?.receiveName + '-个人签字'}}</span>
          <span v-if="element.signerInfo.signerType == 3">
            {{element.signerInfo?.receiveName + '-' + element.signerInfo.nodeName}}</span>
       </div>
    </span>

    
  </div>
</template>

<script lang="ts">
import { defineComponent, defineAsyncComponent, ref, watch } from 'vue';
import { Input, InputNumber, DatePicker, Popconfirm, InputSearch } from 'ant-design-vue';
import { getColor } from '../data/ReceiveColor';
import { Icon, SvgIcon } from '/@/components/Icon';
// import dayjs from 'dayjs';
import { ControlType, isFillControl, CanvasZoom } from '../data/ControlData';
import { moveRange, currentPosition, currentPositionReverse } from '../data/ControlerMoveRange';
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN';
import { getAppEnvConfig } from '/@/utils/env';
import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
import { getTodayDateByFormat } from '/@/utils'
import { useMessage } from '/@/hooks/web/useMessage';
import { json } from 'stream/consumers';

var ControlPage: any = null;

export default defineComponent({
  name: 'control-item',
  components: {
    Input,
    SvgIcon,
    Icon,
    InputSearch,
    InputNumber,
    DatePicker,
    Popconfirm,
    Image:createAsyncComponent(() => import('../controls/Image.vue')),
    CheckBox:createAsyncComponent(() => import('../controls/CheckBox.vue')),
    Radio:createAsyncComponent(() => import('../controls/Radio.vue')),
    DropdownList:createAsyncComponent(() => import('../controls/DropdownList.vue'))
  },
  props: {
    element: {
      type: Object,
      default: function () {
        return {};
      },
    },
    doc: {
      type: Object,
    },
    isSign: {
      type: Boolean,
      default: false,
    },
    isSubmitControl: {
      type: Boolean,
      default: false,
    },
    zoom: {
        type: Number,
        default: 1
    },
  },
  emits: [
    'controlMousedown',
    'controlDelete',
    'clearSeal',
    'controlClick',
    'refreshControlPosition',
    'controlBatchUse',
    'controlFocus'
  ],
  setup(props: any, { emit }) {
    const cursorStyle = ref('cursor:move;');
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
    const today = ref();
    const moveDis = ref({
      x: 0,
      y: 0,
    });
    const mouseDownPos = ref({
      x: 0,
      y: 0,
    });

    const nowPoint = ref({
      x: 0,
      y: 0,
    });
    const eventMove = ref({
      x: 0,
      y: 0,
    });

    const { createMessage: msg } = useMessage();

    function loadSignerInfo(element){
      return ['signature','sign-date','seal','chop-stamp'].includes(element.controlType);
    }
    function controlDelete(e: any) {
      //console.log("controlDelete click",e);
      emit('controlDelete', props.element);
    }
    function clearSeal() {
      emit('clearSeal', props.element);
    }
    function controlMovemousedown(e: any, element: any, falg?: boolean) {
      if (['date','line-text','multiline-text','number','image','dropdown-list','radio','check-box'].includes(element.controlType)) {
        emit('controlFocus', element)
      }
      if (props.element.disabled) {
        return;
      }
      if (props.element.controlOperationType == 'sign') {
      } else {
        // cursorStyle.value = 'cursor:text;';
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

      
      e.preventDefault();
      //console.log("control click",element.controlOperationType);
      if (element.disabled) {
        return;
      }
      props.element.controlClick = true;
      //emit('controlMousedown',element,input.value,e)
      emit('controlMousedown', element);
      if (!element.move) {
        return;
      }
      nowPoint.value = {
        x: e.clientX,
        y: e.clientY,
      };
      //offsetX:e.offsetX,
      //offsetY:e.offsetY,
      document.addEventListener('mousemove', moveControl);
      //松开鼠标清除移动函数事件
      document.addEventListener(
        'mouseup',
        function () {
          document.removeEventListener('mousemove', moveControl);
          removeMousedown(element);
          props.doc.images.map(v=>{
            v.showRange = false
            v.showChopStampRange = false;
          })
        },
        {
          // 只绑定一次事件
          once: true,
        },
      );
    }

    function dragWarning(){
      msg.warning('关联的文档中存在小于当前位置的页面，请在限制区域内指定位置')
    }
    function moveControl(e: any) {
      // e.stopPropagation()
        isMoving.value = true;
      //当前鼠标位置
      var nowPos = {
        x: e.clientX,
        y: e.clientY,
      };
      //计算鼠标移动过的距离
      var dis = {
        x: nowPos.x - nowPoint.value.x,
        y: nowPos.y - nowPoint.value.y,
      };
      moveDis.value = {
        x: e.clientX - mouseDownPos.value.x,
        y: e.clientY - mouseDownPos.value.y,
      };
      nowPoint.value = nowPos;
      // eventMove.value.x = dis.x;
      // eventMove.value.y = dis.y;
      // props.element.position.left += dis.x;
      // props.element.position.top += dis.y;

      props.element.position.left += (dis.x / props.zoom);
      props.element.position.top += (dis.y / props.zoom );

      // removeMousedown();
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
          v.showRange = true;
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
        controlType:props.element.controlType,
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
        opt.x = (elementOffsetWidth + elementTarget.width) -   element.size.width
      }
      //   const pageLeft = currentPosition(opt.y, opt.currentPage);
      props.element.position.left = opt.x;
      props.element.position.top = opt.y;
      // props.element.position.top = pageLeft;
      props.element.position.page = opt.currentPage;
      //props.element.position.pageLeft = pageLeft;
      //修复控件跨页拖拽时propertyVoList中页码不变的bug
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
      emit('refreshControlPosition', props.element, moveDis.value)
    }

    function controlZoomMousedown(e: any, element: any, type: number) {
      //移动方位不一致，位置计算也不相同  通过type区分
      zoomType.value = type;
      //点击当前坐标点
      nowPoint.value = {
        x: e.clientX,
        y: e.clientY,
      };
      //绑定移动事件
      document.addEventListener('mousemove', zoomControl);
      //松开鼠标清除移动函数事件
      document.addEventListener(
        'mouseup',
        function () {
          //还原让控件可拖动
          document.removeEventListener('mousemove', zoomControl);
        },
        {
          // 只绑定一次事件
          once: true,
        },
      );
    }
    function zoomControl(e: any) {
      e.stopPropagation();
      //移动到新的坐标点
      var nowPos = {
        x: e.clientX,
        y: e.clientY,
      };
      //计算鼠标移动过的距离
      var dis = {
        x: nowPos.x - nowPoint.value.x,
        y: nowPos.y - nowPoint.value.y,
      };
      //将旧的坐标更换成新的坐标
      nowPoint.value = nowPos;
      const nowPon = {
        width: 0,
        height: 0,
        left: 0,
        top: 0,
      };
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
        nowPon.width = props.element.size.minWidth;
        nowPon.left = props.element.position.left;
      }

      if (nowPon.height < props.element.size.minHeight) {
        nowPon.height = props.element.size.minHeight;
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
      props.element.size.width = nowPon.width;
      props.element.size.height = nowPon.height;
      props.element.position.left = nowPon.left;
      props.element.position.top = nowPon.top;
    }
    function controlZoomMouseover(opt: any) {
      //emit('moveDisabled',opt);
    }
    function inputMousedown(e: any) {}
    function controlClick($event, element) {
      if (props.isSign) {
        if (element.controlType == 'sign-date') {
          emit('controlClick', $event, element);
        }
        if (element.controlType == 'seal' || element.controlType == 'chop-stamp') {
          emit('controlClick', $event, element);
        }
        if (element.controlType == 'signature') {
          emit('controlClick', $event, element);
        }
      } else {
        if (
          (element.originType == 2 || element.originType == 3) &&
          element.controlType == 'sign-date'
        ) {
          emit('controlClick', $event, element);
        }
        if ((element.originType == 2 || element.originType == 3) && element.controlType == 'seal') {
          emit('controlClick', $event, element);
        }
        if (
          (element.originType == 2 || element.originType == 3) &&
          element.controlType == 'signature'
        ) {
          emit('controlClick', $event, element);
        }
      }
    }
    //批量设置
    function handleBatchUse(element) {
      emit('controlBatchUse', element);
    }
     //日期格式设置
     function handleSignDateFormat(e,element) {
        console.log(e,element,'点击---')
        element.format = e.key;
        // 获取今天日期
        element.today = getTodayDateByFormat(element.format);
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
        cursorStyle.value = 'cursor:text;';
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
      cursorStyle.value = 'cursor:move;';
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
      ReceiveColorItem.value = getColor(-2, null);
    }

    // console.log(ReceiveColorItem.value,props.element.user);
    watch(
      () => props.element.colorIndex,
      (newValue, oldValue) => {
        // ReceiveColorItem.value = getColor(newValue,null)
        // console.log(newValue, '0000000000');
      },
    );
    const moveIn = ref(false);
    const moveTimeout: any = ref(null);
    function controlMouseover(e: any) {
      //防止鼠标快速划过控件显示
      moveTimeout.value = setTimeout(function () {
        console.log('controlMouseover');
        moveIn.value = true;
      }, 100);
    }
    function mouseoverMouseout(e: any) {
      console.log('mouseoverMouseout');
      if (!moveIn.value) {
        clearTimeout(moveTimeout.value);
      }
      moveIn.value = false;
    }
    function settingSeal(e: any) {
      props.element.showModal = true;
    }
    function initData() {
      if (props.isSubmitControl) {
        return;
      }
      if (props.element.disabled) {
        cursorStyle.value = 'cursor: not-allowed;';
      } else {
        if (props.element.controlOperationType == 'sign') {
          cursorStyle.value = 'cursor: pointer;';
        } else {
          cursorStyle.value = 'cursor: move;';
        }
      }
      //日期格式回显需要特殊处理
      if (props.element.controlType == ControlType.Date) {
        if (props.element.value && props.element.value != '') {
          // props.element.value = dayjs(props.element.value);
        }
      }

      // console.log('签署日期控件格式处理-----',props.element.controlType,props.isSign);
      // 签署日期控件格式处理
      if (props.element.controlType == ControlType.SignDate && props.isSign) {
        props.element.today = getTodayDateByFormat(props.element.format);
      }
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
    initData();
    return {
      controlMovemousedown,
      inputBlur,
      cursorStyle,
      inputMousedown,
      getColor,
      ReceiveColorItem,
      controlZoomMousedown,
      handleBatchUse,
      controlZoomMouseover,
      input,
      formItemFocus,
      linesInput,
      lineInput,
      controlDelete,
      ControlType,
      dbclick,
      datePicker,
      datePickerOpen,
      dateChange,
      locale,
      controlMouseover,
      mouseoverMouseout,
      moveIn,
      settingSeal,
      sealVisible,
      addSeal,
      valueChange,
      isFillControl,
      clearSeal,
      controlClick,
      loadSignerInfo,
      baseUrl,
      isMoving,
      ControlPage,
      handleSignDateFormat,
      today,
    };
  },
});
</script>

<style lang="less" scoped>
.resrun-control {
  user-select: none;
  cursor: move;
  position: absolute;
}

.hasSignImage{
  background-color: rgb(255 255 255 / 0) !important;
  .control-item{
    background-color: rgb(255 255 255 / 0) !important;
  }

}

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
  color: red;
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
  border-color: var(--translucent);
  // background-color: rgb(var(--background-rgb), 0.5);
}

.control-item.click {
  width: 100%;
  height: 100%;
  border: 1px;
  border-radius: 2px;
  border-style: solid;
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
  }

  .signature-img {
    width: 100%;
    height: 100%;
  }

  span:nth-child(1) {
    //line-height: 30px;
    height: 30px;
    padding-top: 5px;
  }
}

.control-seal {
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
    border-color: #5589e2;
  }
}

.control-sign-date,
.control-date {
  // display: flex;
  // flex-direction: column;
  text-align: center;
  justify-content: center;

  span {
    line-height: 30px;
    pointer-events: none;
  }
}
.control-dropdown{
    text-align: center;
    .ant-divider-horizontal{
        margin:0;
    }
    
    :deep(.ant-dropdown-menu-item):hover,:deep(.ant-dropdown-menu-submenu-title):hover{
      background:#EBF7FF!important;
      color:#127fd2;
      .batch-text{
        color:#127fd2;
      }
    }
  
    

}
.signer-info{
  border: 1px solid #5589E2;
  border-top-style: dashed;
  text-align: center;
  padding: 2px 10px;
  color: #fff;
  font-size: 10px;
}


// 控件对齐方式
.control-text-right{
  :deep(.ant-input-number-input){
    text-align: right;
    padding:0;
  }
  :deep(.ant-picker-input input){
    text-align: right;
    padding:0;
    font-size:inherit;
  }
}
.control-text-left{
  :deep(.ant-input-number-input){
    text-align: left;
    padding:0;
  }
  :deep(.ant-picker-input input){
    text-align: left;
    padding:0;
    font-size:inherit;
  }
}
.control-text-center{
  :deep(.ant-input-number-input){
    text-align: center;
    padding:0;
  }
  :deep(.ant-picker-input input){
    text-align: center;
    padding:0;
    font-size:inherit;
  }
}
</style>

<style>
.seal-popover .ant-popover-message-title {
  padding-left: 0px !important;
}
.signer-info + div{
  position:sticky !important;
}
.control-handles + div{
  position:sticky !important;
}
.resrun-control .ant-dropdown{
      position:sticky!important;
      margin-top:-36px;
}
.sign-control-dropdown .ant-dropdown{
  margin-top:0;
}
</style>

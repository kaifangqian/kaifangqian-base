<!--
  @description 控件组件

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
        <span :class="['resrun-control', 'resrun-control-move', 'pos-' + element.position.top , 'this-click']" :style="['left:' + (element.position.left) + 'px;top:' + (element.position.top) + 'px;',
        '--primary:' + ReceiveColorItem.primary,
        '--aspecRatio:' + aspecRatio,
        '--background:' + ReceiveColorItem.background,
        '--background-rgb:' + ReceiveColorItem.backgroundRgb,
        'width:' + (element.size.width) + 'px',
        'height:' + (element.size.height) + 'px', cursorStyle]">
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default',
            ]" v-if="element.controlType === ControlType.LineText">
                <van-field ref="lineInput" v-model="element.value" :style="['width:100%;height:100%;padding:0;',
                    'font-size:' + element.style.fontSize + 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    '--fontSize:' + element.style.fontSize,
                    '--textAlign:' + element.style.textAlign,
                    'pointer-events: none;']" :placeholder="element.placeholder" :bordered="true"></van-field>
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default']" v-if="element.controlType === ControlType.MultilineText">
                <van-field ref="linesInput" v-model="element.value" :placeholder="element.placeholder" :style="['width:100%;height:100%;padding:0; resize: none;',
                    'font-size:' + element.style.fontSize + 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    '--fontSize:' + element.style.fontSize,
                    '--textAlign:' + element.style.textAlign,
                    'pointer-events: none;']" :bordered="false" type="textarea" />
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default']" v-if="element.controlType === ControlType.Signature">
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default']" v-if="element.controlType === ControlType.Seal"></div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default']" v-if="element.controlType === ControlType.Number">
                <van-field ref="lineInput" v-model="element.value" type="number" :style="['width:100%;height:100%;padding:0',
                    'font-size:' + element.style.fontSize + 'px',
                    'text-align:' + element.style.textAlign,
                    'fontFamily:' + element.style.fontFamily,
                    '--fontSize:' + element.style.fontSize,
                    '--textAlign:' + element.style.textAlign,
                    'pointer-events: none;']" :controls="false" :stringMode="true" :placeholder="element.placeholder"
                    :bordered="false" />
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default']" v-if="element.controlType === ControlType.SignDate">
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
                <span v-else>{{ element.value || element.title }}</span>
            </div>
            <div ref="input" :class="['control-' + element.controlType, 'control-item', 'arow-content',
                'default']" v-if="element.controlType === ControlType.Date">
                <span>{{element.formatValue}}</span>
                <van-calendar ref="datePicker" v-model="element.value" :placeholder="element.format || '请选择日期'"
                     :value-format="element.format" class="control-date-picker" :style="['width:100%;height:100%; resize: none;',
                        'font-size:' + element.style.fontSize + 'px',
                        'text-align:' + element.style.textAlign,
                        'fontFamily:' + element.style.fontFamily,
                        dbclick ? '' : 'pointer-events: none;'
                    ]" :bordered="false">
                </van-calendar>
            </div>
            <Image v-if="element.controlType === ControlType.Image" :docPageSize="doc.pageSize" :element="element" />
            <CheckBox v-if="element.controlType === ControlType.CheckBox" :docPageSize="doc.pageSize" :element="element"  />
            <Radio v-if="element.controlType === ControlType.Radio" :docPageSize="doc.pageSize" :element="element" />
            <DropdownList v-if="element.controlType === ControlType.DropdownList" :docPageSize="doc.pageSize" :element="element" />
            <div class="control-required" v-if="element.required">
                *
            </div>
        </span>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue';
import { getColor } from "../data/ReceiveColor"
import { ControlType, isFillControl, CanvasZoom } from "../data/ControlData"
import { moveRange, currentPosition, currentPositionReverse } from "../data/ControlerMoveRange"
import Image from '../controls/Image.vue';
import CheckBox from '../controls/CheckBox.vue';
import Radio from '../controls/Radio.vue';
import DropdownList from '../controls/DropdownList.vue';

export default defineComponent({
    name: 'control-item',
    components: {
      Image,
      CheckBox,
      Radio,
      DropdownList

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
            default: false
        }
    },
    emits: ["controlMousedown", "controlDelete", "clearSeal", 'controlClick'],
    setup(props: any, { emit }) {

        const baseImgWidth = window.innerWidth - 20;
        const aspecRatio = baseImgWidth / 800;

        const cursorStyle = ref("cursor:move;");
        const zoomType = ref(0);
        const input = ref(null);
        const lineInput = ref<any>(null);
        const linesInput = ref<any>(null);
        const datePicker = ref<any>(null);
        const dbclick = ref(false);
        const datePickerOpen = ref(false);
        const sealVisible = ref(true);
        // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
        let baseUrl = import.meta.env.VITE_APP_API_BASE_URL;
        const ReceiveColorItem: any = ref(null);
        if (!ReceiveColorItem.value) {
            ReceiveColorItem.value = getColor(-2, null)
        }

        watch(
            ()=>props.element,
            (el)=>{
                if(el.controlType == 'date'&& el.value){
                        const dateStr = formatDate(el.value);
                        const date = new Date(dateStr);
                        const year = date.getFullYear();
                        const month = String(date.getMonth() + 1).padStart(2, '0');
                        const dateOfMonth = String(date.getDate()).padStart(2, '0');
                        console.log(el.value,'日期信息', date, year, month, dateOfMonth)
                        if(el.format == 'YYYY年MM月DD日'){
                            el.formatValue = `${year}年${month}月${dateOfMonth}日`;
                        }
                        if(el.format == 'YYYY-MM-DD'){
                            el.formatValue = `${year}-${month}-${dateOfMonth}`;
                        }
                        if(el.format == 'YYYY/MM/DD'){
                            el.formatValue = `${year}/${month}/${dateOfMonth}`;
                        }
                }
            },
            {deep:true}
        )
        function padZero(num:string) {
            return num.toString().padStart(2, '0');
            }

        function formatDate(dateStr:string) {
            const [year, month, day] = dateStr.split('-');
            const formattedMonth = padZero(month);
            const formattedDay = padZero(day);
            return `${year}-${formattedMonth}-${formattedDay}`;
        }


        return {
            cursorStyle,
            ReceiveColorItem,
            aspecRatio,
            getColor,
            input,
            linesInput, lineInput,
            ControlType,
            dbclick, datePicker, datePickerOpen,
            sealVisible,
            isFillControl,
            baseUrl
        }
    }
})
</script>

<style lang="less" scoped>
.resrun-control {
    user-select: none;
    cursor: move;
    position: absolute;

    :deep(.control-multiline-text .van-field) {
        line-height: 1;

        .van-field__control--min-height {
            min-height: 90%;
        }
    }
}

:deep(.van-field__control--min-height) {
    min-height: 10px;
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

    :deep(.van-cell) {
        padding: 4px 8px;
        // background-color: rgba(0, 160, 232, 0.5);
    }

    :deep(.van-field__body) {
        height: 100%;
        .van-field__control{
            font-size: calc(var(--fontSize)px);
            text-align: var(--textAlign);
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
    border-radius: 1px;
    border-color: var(--translucent);
    // background-color: rgba(0, 160, 232, 0.5)
    border:2px dashed #6aa0ff;
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
        height: 40px;
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
</style>
<style>
.seal-popover .ant-popover-message-title {
    padding-left: 0px !important;
}
</style>

/**
 * @description 判断填写控件是否需要进行填写
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
export enum ControlType {
    Signature = "signature",
    Seal = "seal",
    LineText = "line-text",
    MultilineText = "multiline-text",
    Date = "date",
    SignDate = "sign-date",
    Number = "number",
}
//判断控是否需要进行填写
export const isFillControl = (type: string) => {
    let isFill = false;
    switch (type) {
        case ControlType.LineText:
            isFill = true;
            break;
        case ControlType.MultilineText:
            isFill = true;
            break;
        case ControlType.Date:
            isFill = true;
            break;
        case ControlType.Number:
            isFill = true;
            break;
        case ControlType.SignDate:
            isFill = true;
            break;
        default:
            isFill = false;
    }
    return isFill;
}

export enum ControlOperationType {
    template = "template",
    create = "create",
    sign = "sign",
}

export interface ControlSize {
    [key: string]: any;
    //控件宽度
    width: number;
    //控件高度
    height: number;
    //控件最小宽度
    minWidth: number;
    //控件最小高度
    minHeight: number;
}
export interface ControlPosition {
    [key: string]: any;
    //控件x坐标
    left: number;
    //控件y坐标
    right: number;
}


export interface ControlOptins {
    [key: string]: any;
    //控件类型
    type: String;
    //控件名称
    name: String;
    //控件iocn
    icon: String;
    //控件大小
    size?: ControlSize;
    //控件位置
    position?: ControlPosition;
    //控件提示内容
    placeholder?: String;
    //控件绑定的value值
    value: String
    //是否点一点击 active
    controlClick: Boolean,
    //是否可缩放
    zoom: Boolean,
    //是否可移动
    move: Boolean,

}
const CanvasZoom = {
    space: 16,
    width: 800,
    height: 1131,
    zoom: 100,
    originWidth: 2481,
    originHeight: 3508,
}


// export const getCanvasZoom = (width: number, zoom: number) => {
//     var ratio = width / CanvasZoom.originWidth;
//     return {
//         width: ratio * CanvasZoom.originWidth * (zoom / 100),
//         height: ratio * CanvasZoom.originHeight * (zoom / 100),
//         zoom: zoom,
//         space: 16 * ratio
//     }
// }
export const getCanvasZoom = (width: number, zoom: number) => {
    var ratio = width / CanvasZoom.width;
    return {
        width: ratio * CanvasZoom.width,
        height: ratio * CanvasZoom.height,
        zoom: zoom,
        space: 16 * ratio
    }
}

export function getColtrolByType(type: string): any {
    return controlListSource.filter((item: any) => {
        return item.type == type
    })[0]
}
export const controlListSource = [
    {
        icon: "sign",
        name: "签名位置",
        title: "签名位置",
        type: ControlType.Signature,
        placeholder: "",
        value: "",
        controlClick: false,
        zoom: false,
        move: true,
        required: false,
        attr: ['name'],
        style: {},
        size: {
            width: 150,
            height: 70,
            minWidth: 150,
            minHeight: 70,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: -1,
            userName: "",
        }
    },
    {
        icon: "seal",
        name: "企业签章",
        title: "企业签章",
        type: ControlType.Seal,
        placeholder: "",
        value: "",
        controlClick: false,
        zoom: false,
        move: true,
        required: false,
        attr: ['name'],
        style: {},
        size: {
            width: 160,
            height: 160,
            minWidth: 160,
            minHeight: 160,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: -1,
            userName: "",
        }
    },
    {
        icon: "date",
        name: "签署日期",
        title: "签署日期",
        type: ControlType.SignDate,
        placeholder: "",
        value: "",
        controlClick: false,
        zoom: false,
        move: true,
        required: false,
        format: "",
        attr: ['name', 'format'],
        style: {
            fontSize: 14,
            fontFamily: "宋体",
            textAlign: "center"
        },
        size: {
            width: 180,
            height: 40,
            minWidth: 180,
            minHeight: 40,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: -1,
            userName: "",
        }
    },
    {
        icon: "line1",
        name: "单行文本",
        title: "单行文本",
        type: ControlType.LineText,
        placeholder: "请输入内容",
        value: "",
        controlClick: false,
        zoom: true,
        move: true,
        required: false,
        attr: ['name', 'fontSize', 'placeholder', 'value', 'fontFamily', 'textAlign', 'required', 'interfaceParamName'],
        style: {
            fontSize: 14,
            fontFamily: "宋体",
            textAlign: "left"
        },
        size: {
            width: 200,
            height: 30,
            minWidth: 200,
            minHeight: 30,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: -1,
            userName: "",
        }
    },
    {
        icon: "line2",
        name: "多行文本",
        title: "多行文本",
        type: ControlType.MultilineText,
        placeholder: "请输入内容",
        value: "",
        controlClick: false,
        zoom: true,
        move: true,
        required: false,
        attr: ['name', 'fontSize', 'placeholder', 'value', 'fontFamily', 'textAlign', 'required', 'interfaceParamName'],
        style: {
            fontSize: 14,
            fontFamily: "宋体",
            textAlign: "left"
        },
        size: {
            width: 200,
            height: 60,
            minWidth: 200,
            minHeight: 30,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: -1,
            userName: "",
        }
    },
    {
        icon: "number",
        name: "数字",
        title: "数字",
        type: ControlType.Number,
        placeholder: "请输入数字",
        value: "",
        controlClick: false,
        zoom: true,
        move: true,
        required: false,
        attr: ['name', 'fontSize', 'placeholder', 'value', 'fontFamily', 'textAlign', 'required', 'interfaceParamName'],
        style: {
            fontSize: 14,
            fontFamily: "宋体",
            textAlign: "left"
        },
        size: {
            width: 200,
            height: 30,
            minWidth: 200,
            minHeight: 30,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: 0,
            userName: "",
        }
    },
    {
        icon: "date",
        title: "填写日期",
        name: "填写日期",
        type: ControlType.Date,
        placeholder: "",
        value: "",
        controlClick: false,
        zoom: true,
        move: true,
        required: false,
        format: "YYYY年MM月DD日",
        attr: ['name', 'fontSize', 'format', 'value', 'fontFamily', 'textAlign', 'required', 'interfaceParamName'],
        style: {
            fontSize: 14,
            fontFamily: "宋体",
            textAlign: "left"
        },
        size: {
            width: 160,
            height: 30,
            minWidth: 160,
            minHeight: 30,
        },
        position: {
            left: 0,
            top: 0,
            page: -1,
        },
        user: {
            index: 0,
            userName: "",
        }
    },
]

export const ControlFontSizeList = [
    {
        value: 10,
        label: '六号',
    },
    {
        value: 12,
        label: '小五',
    },
    {
        value: 14,
        label: '五号',
    },
    {
        value: 16,
        label: '小四',
    },
    {
        value: 18,
        label: '四号',
    },
    {
        value: 20,
        label: '小三',
    },
    {
        value: 21,
        label: '三号',
    },
    {
        value: 24,
        label: '小二',
    },
    {
        value: 29,
        label: '二号',
    },
    {
        value: 32,
        label: '小一',
    },
    {
        value: 34,
        label: '一号',
    },
    {
        value: 48,
        label: '小初',
    },
    {
        value: 56,
        label: '初号',
    }
]

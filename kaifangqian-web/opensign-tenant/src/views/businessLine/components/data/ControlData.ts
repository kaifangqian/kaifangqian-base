/*
 * @description 开放签
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
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

import type { SelectProps } from 'ant-design-vue';

import { BasicColumn } from '/@/components/Table';

import {retainDecimals} from "/@/utils"
import { currentPosition, currentWidgetPosition, currentPositionReverse } from './ControlerMoveRange'

export enum ControlType {
    Signature = "signature",
    Seal = "seal",
    ChopStamp = "chop-stamp",
    LineText = "line-text",
    MultilineText = "multiline-text",
    Date = "date",
    SignDate = "sign-date",
    Number = "number",
    //单选按钮
    Radio = "radio",
    //多选按钮
    CheckBox = "check-box",
    //下拉列表
    DropdownList = "dropdown-list",
    //图片
    Image = "image"
}
//文档列表组件
export const docColumns: BasicColumn[] = [
    {
        title: '文档名称',
        dataIndex: 'name'
    },

    {
        title: '文档页数',
        dataIndex: 'docPage',
    },
]
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
export const CanvasZoom = {
    space: 16,
    width: 800,
    height: 1131,
    zoom: 100
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
          width: 112,
          height: 52,
          minWidth: 112,
          minHeight: 52,
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
          width: 120,
          height: 120,
          minWidth: 120,
          minHeight: 120,
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
          width: 134,
          height: 30,
          minWidth: 134,
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
      icon: "chop-stamp",
      name: "骑缝签章",
      title: "骑缝签章",
      type: ControlType.ChopStamp,
      placeholder: "",
      value: "",
      controlClick: false,
      zoom: false,
      move: true,
      required: false,
      attr: ['name'],
      style: {},
      size: {
          width: 120,
          height: 120,
          minWidth: 120,
          minHeight: 120,
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
          width: 149,
          height: 22,
          minWidth: 149,
          minHeight: 22,
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
          width: 149,
          height: 15,
          minWidth: 200,
          minHeight: 22,
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
          width: 149,
          height: 22,
          minWidth: 149,
          minHeight: 22,
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
          width: 120,
          height: 22,
          minWidth: 120,
          minHeight: 22,
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
    icon:"radio",
    title:"单选框",
    name:"单选框",
    type:ControlType.Radio,
    placeholder:"",
    value:"",
    interfaceParamName:"radio_",
    controlClick:false,
    zoom:false,
    move:true,
    isRequired:false,
    widgetList:[],
    // format:"YYYY年MM月DD日",
    attr:['name','radio','required','interfaceParamName'],
    style:{},
    size:{
      width:32,
      height:64,
      minWidth:CanvasZoom.width,
      minHeight:CanvasZoom.height,
    },
    position:{
      left: 0,
      top: 0,
      page:-1,
    },
    user:{
      index:0,
      userName:"",
    }
  },
  {
    icon:"check-box",
    title:"多选框",
    name:"多选框",
    type:ControlType.CheckBox,
    placeholder:"",
    value:"",
    interfaceParamName:"check_box_",
    controlClick:false,
    zoom:false,
    move:true,
    isRequired:false,
    attr:['name','checkBox','required','interfaceParamName'],
    size:{
      width:32,
      height:64,
      minWidth:100,
      minHeight:25,
    },
    style:{},
    position:{
      left: 0,
      top: 0,
      page:-1,
    },
    user:{
      index:0,
      userName:"",
    }
  },
  {
    icon:"select",
    title:"下拉选择",
    name:"下拉选择",
    type:ControlType.DropdownList,
    placeholder:"请选择",
    value:undefined,
    interfaceParamName:"select_",
    controlClick:false,
    zoom:true,
    move:true,
    isRequired:false,
    // format:"YYYY年MM月DD日",
    attr:['name','fontSize','select','fontFamily','textAlign','required','interfaceParamName'],
    style:{
      fontSize:14,
      fontFamily:"宋体",
      textAlign:"left"
    },
    size:{
      width:120,
      height:22,
      minWidth:100,
      minHeight:25,
    },
    position:{
      left: 0,
      top: 0,
      page:-1,
    },
    user:{
      index:0,
      userName:"",
    }
  },
  {
    icon:"img",
    title:"图片",
    name:"图片",
    type:ControlType.Image,
    placeholder:"",
    value:"",
    interfaceParamName:"img_",
    controlClick:false,
    zoom:true,
    move:true,
    isRequired:false,
    // format:"YYYY年MM月DD日",
    attr:['name','size','required','interfaceParamName'],
    style:{
      // fontSize:14,
      // fontFamily:"宋体",
      // textAlign:"left"
    },
    size:{
      width:120,
      height:120,
      minWidth:30,
      minHeight:30,
    },
    position:{
      left: 0,
      top: 0,
      page:-1,
    },
    user:{
      index:0,
      userName:"",
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



/**
 * 计算页面实际要显示图片的大小
 * @param {any[]} images 
 * @return 
 */ 
export function pageScaling(images:any[]){
  var targets:any[] = [];
  var transformHeight = 0;
  var maxWidth = 0;
  images.forEach((item:any)=>{
    const original = {
      height: item.imageHeight,
      width:item.imageWidth,
      page: item.page
    }
    var target:any = calculateAspectRatio(original);
    maxWidth = target.width>maxWidth?target.width:maxWidth;
    target.transformHeight = transformHeight;
    item.target = target;
    targets.push(target);
    transformHeight += item.target.height;
  })
  return {targets:targets,maxWidth:maxWidth};
}
function calculateAspectRatio(original:any){
  // var ratio = 0.744;
  var ratio = 1;
  if(original){
    var newWidth = original.width / ratio;
    var newHeight =  original.height / ratio;
 
    
    return{
      width:Number(newWidth),
      height:Number(newHeight),
      ratio: ratio,
      originalWidth: original.width,
      originalHeight:original.height,
      page: original.page
    }
  }
  return{
    width: 800,
    height:1136,
    originalWidth: 800,
    originalHeight:1136,
    ratio:1,
    page: 0
  }
}



//结合图片实际宽高整理控件
export function formatControls(controls,targets,pageMaxWidth){
  const temp:any = [];
  if(controls && controls.length>0){
  	controls.forEach((item:any,index:number)=>{
  		const basaeColtrol = getColtrolByType(item.controlType);
      const target = targets[item.page];
      const offsetWidth = (pageMaxWidth - target.width) / 2
      console.log( item.page, 'offsetWidth---'+ offsetWidth, 'arget.width---'+ target.width, 'v.offsetX---'+ item.offsetX, 'v.width====='+ item.width, '控件xxxxxxxxxx信息')
  		temp.push({
        ...item,
  			size:{
          width: parseInt(item.width) || basaeColtrol.width,
          height: parseInt(item.height) || basaeColtrol.height,
  				minWidth:basaeColtrol.size.minWidth,
  				minHeight:basaeColtrol.size.minHeight,
  			},
  			// position:{
  			// 	left: item.controlType == 'chop-stamp' ? (offsetWidth + target.width) - item.width : ((offsetWidth + item.offsetX) >= ((offsetWidth + target.width) - item.width)?(offsetWidth + target.width) - item.width:offsetWidth + item.offsetX),
  			// 	top: currentPositionReverse(item.offsetY,target),
  			// 	page:parseInt(item.page),
  			// },
        user:{},
        widgetList: paramBuildWidgets(item.properties,target,offsetWidth)
  		})
  	})
  }
  console.log("build:",temp);
  return temp;
}
//子控件位置计算逻辑
export function paramBuildWidgets(widgetStr:string,target,offsetWidth){
  if(widgetStr && typeof widgetStr == 'string'){
    var widgets =  JSON.parse(widgetStr);
    console.log("widgets:",widgets);
    widgets.forEach(item=>{
      item.y = currentPositionReverse(item.y,target)
      if(!item.x){
        item.x = 0;
      }else{
        item.x =item.x + offsetWidth;
      }
    })
    return widgets;
  }
  return [];
}

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

export enum ControlType {
  //企业签章
  Seal = "seal",
	//个人签名
  Signature = "signature",
  //签署时间
  SignDate="sign-date",
  //单行文本
	LineText = "line-text",
  //多行文本
	MultilineText = "multiline-text",
	//日期
  Date="date",
  //数字
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
//判断控是否需要进行填写
export const isFillControl=(type:string)=>{
	let isFill = false;
	switch(type){
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
		// case ControlType.SignDate:
		// 	isFill = true;
		// 	break;
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

export interface ControlSize{
	[key: string]: any;
	//控件宽度
	width:number;
	//控件高度
	height:number;
	//控件最小宽度
	minWidth:number;
	//控件最小高度
	minHeight:number;
}
export interface ControlPosition{
	[key: string]: any;
	//控件x坐标
	left: number;
	//控件y坐标
	right: number;
}
export interface ValueStyle{
	[key: string]: any;
	//字体大小
	fontSize?: number;
	//字体类型
	fontFamily?: String | 'SimSun' | 'SimHei' | 'SimKai' | 'SimFang';
  //字体样式
  textAlign?: String | 'left'|'right'|'center';
}

export interface ControlOptins{
	[key: string]: any;
  //控件iocn
  icon: String;
  //控件名称 可改变
  name:String;
  //空间标题固定 不可修改
  title: String;
	//控件类型
	type:ControlType;
	//控件提示内容
	placeholder?: String;
	//控件绑定的value值
	value?:Object;
  //接口参数名
  interfaceParamName: String;
  //是否点击 active
  controlClick:Boolean,
  //是否可缩放
  zoom:Boolean;
  //是否可移动
  move:Boolean;
  //是否必填
  isRequired?: Boolean;
  //控件的属性配置
  attr?: Array<string>;
	//控件位置
	position:ControlPosition;
  //字体样式
  style?: ValueStyle;
  //控件大小
  size?:ControlSize;
  //控件扩展属性
  properties?:Object;
  //控件所属用户
  user?:Object;
}

export const CanvasZoom = {
	space:16,
	width:800,
	height: 1131,
	zoom:100
}

export function getColtrolByType(type:string):any {
	return controlList.filter((item:any) => {
			return item.type == type 
		})[0]
}
export const controlList = [
	// {
	// 	icon:"sign",
	// 	name:"个人签名",
	// 	title:"个人签名",
	// 	type:ControlType.Signature,
	// 	placeholder:"",
	// 	value:"",
	// 	controlClick:false,
	// 	zoom:false,
	// 	move:true,
	// 	required:false,
	// 	attr:['name'],
	// 	style:{},
	// 	size:{
	// 		width:150,
	// 		height:70,
	// 		minWidth:150,
	// 		minHeight:70,
	// 	},
	// 	position:{
	// 		left: 0,
	// 		top: 0,
	// 		page:-1,
	// 	},
	// 	user:{
	// 		index:-1,
	// 		userName:"",
	// 	}
	// },
	// {
	// 	icon:"seal",
	// 	name:"企业签章",
	// 	title:"企业签章",
	// 	type:ControlType.Seal,
	// 	placeholder:"",
	// 	value:"",
	// 	controlClick:false,
	// 	zoom:false,
	// 	move:true,
	// 	required:false,
	// 	attr:['name'],
	// 	style:{},
	// 	size:{
	// 		width:160,
	// 		height:160,
	// 		minWidth:160,
	// 		minHeight:160,
	// 	},
	// 	position:{
	// 		left: 0,
	// 		top: 0,
	// 		page:-1,
	// 	},
	// 	user:{
	// 		index:-1,
	// 		userName:"",
	// 	}
	// },
	// {
	// 	icon:"date",
	// 	name:"签署日期",
	// 	title:"签署日期",
	// 	type:ControlType.SignDate,
	// 	placeholder:"",
	// 	value:"",
	// 	controlClick:false,
	// 	zoom:false,
	// 	move:true,
	// 	required:false,
	// 	format:"yyyy年MM月dd日",
	// 	attr:['name','format'],
	// 	style:{
	// 		fontSize:14,
	// 		fontFamily:"宋体",
	// 		textAlign:"center"
	// 	},
	// 	size:{
	// 		width:180,
	// 		height:40,
	// 		minWidth:180,
	// 		minHeight:40,
	// 	},
	// 	position:{
	// 		left: 0,
	// 		top: 0,
	// 		page:-1,
	// 	},
	// 	user:{
	// 		index:-1,
	// 		userName:"",
	// 	}
	// },
	{
		icon:"line1",
		name:"单行文本",
		title:"单行文本",
		type:ControlType.LineText,
		placeholder:"请输入内容",
		value:"",
    interfaceParamName:"text_",
		controlClick:false,
		zoom:true,
		move:true,
		isRequired:false,
		attr:['name','fontSize','placeholder','value','fontFamily','textAlign','required','interfaceParamName'],
		style:{
			fontSize:14,
			fontFamily:"宋体",
			textAlign:"left"
		},
		size:{
			width:200,
			height:30,
			minWidth:50,
			minHeight:24,
		},
		position:{
			left: 0,
			top: 0,
			page:-1,
		},
		user:{
			index:-1,
			userName:"",
		}
	},
	{
		icon:"line2",
		name:"多行文本",
		title:"多行文本",
		type:ControlType.MultilineText,
		placeholder:"请输入内容",
		value:"",
    interfaceParamName:"text_",
		controlClick:false,
		zoom:true,
		move:true,
		isRequired:false,
		attr:['name','fontSize','placeholder','value','fontFamily','textAlign','required','interfaceParamName'],
		style:{
			fontSize:14,
			fontFamily:"宋体",
			textAlign:"left"
		},
		size:{
			width:200,
			height:60,
			minWidth:50,
			minHeight:25,
		},
		position:{
			left: 0,
			top: 0,
			page:-1,
		},
		user:{
			index:-1,
			userName:"",
		}
	},
	{
		icon:"number",
		name:"数字",
		title:"数字",
		type:ControlType.Number,
		placeholder:"请输入数字",
		value:"",
    interfaceParamName:"number_",
		controlClick:false,
		zoom:true,
		move:true,
		isRequired:false,
		attr:['name','fontSize','placeholder','value','fontFamily','textAlign','required','interfaceParamName'],
		style:{
			fontSize:14,
			fontFamily:"宋体",
			textAlign:"left"
		},
		size:{
			width:200,
			height:30,
			minWidth:25,
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
		icon:"date",
		title:"填写日期",
		name:"填写日期",
		type:ControlType.Date,
		placeholder:"",
		value:"",
    interfaceParamName:"date_",
		controlClick:false,
		zoom:true,
		move:true,
		isRequired:false,
		format:"YYYY年MM月DD日",
		attr:['name','fontSize','format','value','fontFamily','textAlign','required','interfaceParamName'],
		style:{
			fontSize:14,
			fontFamily:"宋体",
			textAlign:"left"
		},
		size:{
			width:160,
			height:30,
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
    		width:160,
    		height:30,
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
    		width:160,
    		height:160,
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

export const ControlFontSizeList= [
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

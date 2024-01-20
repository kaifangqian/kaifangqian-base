export enum ControlType {
	Signature = "signature",
	Seal = "seal",
	// LineText = "line-text",
	// MultilineText = "multiline-text",
	// Date="date",
	// SignDate="sign-date",
	// Number = "number",
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


export interface ControlOptins{
	[key: string]: any;
	//控件类型
	type:String;
	//控件名称
	name:String;
	//控件iocn
	icon: String;
	//控件大小
	size?:ControlSize;
	//控件位置
	position?:ControlPosition;
	//控件提示内容
	placeholder?: String;
	//控件绑定的value值
	value:String
	//是否点一点击 active
	controlClick:Boolean,
	//是否可缩放
	zoom:Boolean,
	//是否可移动
	move:Boolean,
	
}
export const CanvasZoom = {
	space:16,
	width:800,
	height: 1131,
	zoom:100
}

export const controlList = [
	{
		icon:"seal",
		name:"电子印章",
		title:"企业签章",
		type:ControlType.Seal,
		placeholder:"",
		value:"",
		zoom:false,
		move:true,
		size:{
			width:160,
			height:160,
			minWidth:160,
			minHeight:160,
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
		icon:"signature",
		name:"手写签名",
		title:"个人签名",
		type:ControlType.Signature,
		placeholder:"",
		value:"",
		zoom:false,
		move:true,
		size:{
			width:150,
			height:70,
			minWidth:150,
			minHeight:70,
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
	}
]
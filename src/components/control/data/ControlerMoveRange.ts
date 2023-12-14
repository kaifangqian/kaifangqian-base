
import {CanvasZoom} from "./ControlData";


export function moveRange(opt:any){
	//CanvasZoom
	const allHeight = (CanvasZoom.height + CanvasZoom.space) * opt.pageSize; 
	if(opt.x < 0){
		opt.x = 0;
	}
	else if(opt.x>CanvasZoom.width - opt.size.width){
		opt.x = CanvasZoom.width - opt.size.width;
	}
	if(opt.y<CanvasZoom.space){
		opt.y = CanvasZoom.space;
	}else if(opt.y>allHeight){
		opt.y = allHeight;
	}
	currentPage(opt);
	isControlPage(opt,allHeight);
}
//计算控件所在每页的位置
export function currentPosition(top:number,pageSize:number){
	return top - (pageSize + 1) * CanvasZoom.space - pageSize * CanvasZoom.height;
}
//将每页的位置转换成为一大页的位置
export function currentPositionReverse (top:number,pageSize:number){
	return top + (pageSize + 1) * CanvasZoom.space + pageSize * CanvasZoom.height;
}

//计算控件所在的页面
function currentPage(opt:any){
	
	for(var i =0;i<opt.pageSize;i++){
		//计算控件是否再空白区域
		var start = (i+1) * CanvasZoom.space + i * CanvasZoom.height;
		var end =(i+1) * CanvasZoom.space + (i+1) * CanvasZoom.height;
		// console.log(opt.y,start,end);
		if(opt.y>start && opt.y< end){
			opt.currentPage = i;
		}
	}
	// console.log(opt);
}

//判断控件是否再边界上
function isControlPage(opt:any,allHeight:number){
	//var temHeight = 0;
	var start = (opt.currentPage+1) * CanvasZoom.space + opt.currentPage * CanvasZoom.height;
	var end =(opt.currentPage+1) * CanvasZoom.space + (opt.currentPage+1) * CanvasZoom.height;
	var top = opt.y + opt.size.height;
	// console.log("scope:",start,end,opt.y + opt.size.height);
	
	if(top>start && top< end){
		return;
	}
	if(top>allHeight){
		opt.y = allHeight - opt.size.height ;
		return;
	}
	
	var outSize = top - end;
	
	if(outSize>opt.size.height/2){
		opt.currentPage += 1;
		opt.y = end + CanvasZoom.space;
	}else{
		opt.y = end - opt.size.height;
	}
	console.log("超出：",outSize);
}
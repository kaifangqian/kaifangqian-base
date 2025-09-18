/**
 * @description 控件移动的坐标计算
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

import { CanvasZoom } from "./ControlData";
import { retainDecimals, bubbleSort } from '@/utils'

const baseImgWidth = window.innerWidth - 20;
const aspecRatio = baseImgWidth / 800;
import { cloneDeep } from 'lodash-es';


let newCanvas = cloneDeep(CanvasZoom);

newCanvas.width = CanvasZoom.width * aspecRatio;
newCanvas.height = CanvasZoom.height * aspecRatio;
newCanvas.space = CanvasZoom.space * aspecRatio;


export function moveRange(opt: any, targets:any, isBatch?:boolean) {
  const allHeight = opt.allHeight;
  const  minWidth = targets.reduce((min, item) => {
    return parseFloat(item.originalWidth) < parseFloat(min.originalWidth) ? item : min;
  }, targets[0]).originalWidth;
  //计算控件所在的页码
  currentPage(opt, targets);
  //计算控件可移动的范围
  calculateOffset(opt,targets, allHeight, Number(minWidth), isBatch);
  
  //计算控件是否骑在两页中间
  isControlPage(opt,targets);
}


//计算控件所在的页面
export function currentPage(opt:any,targets:any){
  var searchFlag = false;
  //处理在可见区域内的
  for(var i =0;i<opt.pageSize;i++){
    var start = i * CanvasZoom.space + targets[i].transformHeight;
    var end = start + targets[i].height + CanvasZoom.space;
    if(opt.y>=start && opt.y<= end){
      opt.currentPage = opt.isBatch?opt.currentPage:i;
      opt.height = targets[i].height;
      opt.width = targets[i].width;
      opt.transformHeight = targets[i].transformHeight;
      searchFlag = true;
    }
  }  
  //处理在非可见区域外的位置
  if(!searchFlag){
    if(opt.y < CanvasZoom.space){
      opt.currentPage = 0;
      opt.height = targets[0].height;
      opt.width = targets[0].width;
      opt.transformHeight = targets[0].transformHeight
    }else{
      opt.currentPage = targets.length-1;
      opt.height = targets[targets.length - 1].height;
      opt.width = targets[targets.length - 1].width;
      opt.transformHeight = targets[targets.length - 1].transformHeight
    }
  }
}


export function calculateOffset(opt:any,targets, allHeight:number, minWidth:number, isBatch?:boolean){

  const minX = retainDecimals((opt.maxWidth - opt.width) / 2);
  const maxX = retainDecimals(opt.maxWidth - ((opt.maxWidth - opt.width )/ 2));
  const target = targets[opt.currentPage]
  const batchMinY = target.transformHeight + (target.page + 1) * CanvasZoom.space;
  let batchMaxY = target.transformHeight + (target.page + 1) * CanvasZoom.space + target.height - opt.size.height;
  if(isBatch && opt?.minTarget){
    batchMaxY = target.transformHeight + (target.page + 1) * CanvasZoom.space + opt.minTarget.minHeight - opt.size.height;
  }

  //判断坐标是否移出文档的范围
  if(opt.x <= 0){
    opt.x = minX
  }else  if(opt.x < minX && opt.x > 0){
    opt.x = minX;
  }else if(opt.x>maxX - opt.size.width){
    opt.x = maxX - opt.size.width;
  }
  if(isBatch){
    if(opt.x > (minWidth - opt.size.width) + ( (opt.maxWidth - opt.width) / 2)){
      opt.x =  (minWidth - opt.size.width) + ( (opt.maxWidth - opt.width) / 2); 
    }
    if(opt.y < batchMinY){
      opt.y = batchMinY;
    }
    if(opt.y > batchMaxY){
      opt.y = batchMaxY;
    }
  }
  if(opt.y<CanvasZoom.space){
    opt.y = CanvasZoom.space;
  }else if(opt.y>allHeight){
    opt.y = allHeight;
  }
}
//计算控件所在每页的位置
export function currentPosition(top: number, target: any) {
  return top - target.transformHeight - (target.page + 1) * CanvasZoom.space
  }
//将每页的位置转换成为一大页的位置
export function currentPositionReverse (top:number,target:any){
  return parseInt(top) + (target.page + 1) * CanvasZoom.space + target.transformHeight;
}

//判断控件是否再边界上
function isControlPage(opt:any,allHeight:number){
	//var temHeight = 0;
  var recalculate = false;
  // debugger;
	var start = opt.currentPage * CanvasZoom.space + opt.transformHeight;
	var end = start + opt.height + CanvasZoom.space;
  
  const startHeight = opt.transformHeight + CanvasZoom.space * (opt.currentPage+1);
  const endHeight = startHeight + opt.height;
  
  const minX = retainDecimals((opt.maxWidth - opt.width) / 2);
  const maxX = retainDecimals(opt.maxWidth - ((opt.maxWidth - opt.width )/ 2));
  
	var top = opt.y;
	console.log("scope:",start,end,minX,maxX,opt);
	
	if(top>start && top< end){
    if(opt.y < startHeight){
    	opt.y = startHeight;
    }else if(opt.y > endHeight - opt.size.height){
    	opt.y = endHeight - opt.size.height;
    }
    
    if(opt.width < opt.size.width){
    	opt.size.width = opt.width ;
    }
    if(opt.height < opt.size.height){
    	opt.size.height = opt.height;
      opt.y = startHeight;
    }
    
		return;
	}
	if(top>allHeight){
		opt.y = allHeight - opt.size.height ;
		return;
	}
	
	var outSize = top - end;
	console.log("超出：",outSize);
  return recalculate;
}

export function getMoveScoped(element:any){
    var xs:Array<any> = [];
    var ys:Array<any> = [];
    element.widgetList.forEach((sub)=>{
      xs.push(sub.x);
      ys.push(sub.y);
    })
    
    bubbleSort(xs);
    bubbleSort(ys);
    const minX2 = xs.length == 1?xs[0]:xs[1];
    const maxX2 = xs.length == 1 || xs.length == 2?xs[0]:xs[xs.length-2];
    
    const minY2 = ys.length == 1?ys[0]: ys[1];
    const maxY2 = ys.length == 1 || ys.length == 2?ys[0]:ys[ys.length-2];
    
    return {
      minX:xs[0],
      minX2:minX2,
      maxX: xs[xs.length-1],
      maxX2: maxX2,
      minY:ys[0],
      maxY:ys[ys.length-1],
      minY2:minY2,
      maxY2:maxY2
    }
  }

  
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
      var target:any = calculateAspectRatio(original, images); 
      maxWidth = target.width>maxWidth?target.width:maxWidth;
      target.transformHeight = transformHeight;
      item.target = target;
      targets.push(target);
      transformHeight += item.target.height;
    })
    return {targets:targets,maxWidth:maxWidth};
  }

  
function calculateAspectRatio(original:any,imgs){
    const maxImageWidthItem = imgs.reduce((max, item) => {
      return parseFloat(item.imageWidth) > parseFloat(max.imageWidth) ? item : max;
    }, imgs[0]);

    // const ratio = Number((maxImageWidthItem.imageWidth / baseImgWidth).toFixed(3))
    const ratio = 1

    if(original){
      var newWidth = original.width / ratio;
      var newHeight =  original.height / ratio;
   
      
      return{
        width: Number(newWidth) ,
        height:Number(newHeight),
        ratio: ratio,
        originalWidth: original.width,
        originalHeight:original.height,
        page: original.page
      }
    }
    return{
      width: 800 ,
      height:1136 ,
      originalWidth: 800 ,
      originalHeight:1136,
      ratio:1,
      page: 0
    }
  }
  

  /**
   * 子控件位置整理
   */

  export function buildWidgets(widgetList:[], target, element, maxWidth){
    const offsetRatio = element.offsetX / element.pageWidth;
    const controlRatio = target.width / element.pageWidth;
    const offsetWidth = (maxWidth - target.width) / 2;
    var widgets:any = [];
    if(widgetList){
      widgetList.forEach((item:any)=>{
        var widget = {
          uid:item.uid,
          x:target.width + offsetWidth,
          y:target.transformHeight + (element.page+1) * 16 + target.height * (item.y / element.pageHeight),
          w:item.w * controlRatio,
          h:item.h * controlRatio,
          n:item.n,
          v:item.v,
        }
        widgets.push(widget);
      })
      return widgets;
    }
    return null
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



/**
 * 查找批量控件所在页码最小宽度和高度
 * @param docs 
 * @param docKeys 
 * @param pages 
 * @returns 
 */
export function  findMinImageSize(docs, docKeys, pages) {
  let minImageWidth = Infinity;
  let minImageHeight = Infinity;
  let minTarget = {
    minWidth: Infinity,
    minHeight:Infinity,
    offsetWidth: Infinity,
  }
  docKeys.forEach(docKey => {
      const doc = docs.find(d => d.signRuDocId === docKey);
      if (doc) {
        pages.forEach(pageNum => {
              const image = doc.images.find(img => img.page === pageNum);
              if (image && image.imageWidth < minImageWidth) {
                  minImageWidth = image.imageWidth;
                  minTarget.minWidth =  Number(minImageWidth);
                  minTarget.offsetWidth =  (doc.maxWidth - minImageWidth) / 2;
              }
              if (image && image.imageHeight < minImageHeight) {
                  minImageHeight = image.imageHeight;
                  minTarget.minHeight = Number(minImageHeight);
              }                

          });
      }
  })
    return minTarget;
}




//控件位置计算包含控件当前页范围
/**
 * @param element {当前控件信息}
 * @param targets {文档页码信息}
 * @param offsetX { 控件控件偏移量X }
 * @param offsetY { 控件控件偏移量Y }
 * @param maxWidth { 文档最大宽度 }
 * @returns 
 */
export function recaculateBatchControlPosInPage(element: any, targets, offsetX, offsetY, maxWidth, minTarget?:any) {
  if(offsetX<=0){
    offsetX=0
  }
  if(offsetY<=0){
    offsetY=0
  }

  //当前页信息
  const pageTarget = targets[element.position.page];
  //当前页图片偏移量
  const pageOffsetWidth =  (maxWidth - pageTarget.width) / 2;
  //当前页开始位置
  const pageStart = pageTarget.transformHeight +  (pageTarget.page + 1) * CanvasZoom.space;
  //当前页结束位置
  const pageEnd = pageTarget.transformHeight + (pageTarget.page + 1) * CanvasZoom.space +  pageTarget.height;

  //当前控件top值
  if(offsetY + element.size.height > minTarget.minHeight){
    offsetY = minTarget.minHeight - element.size.height;
  }
  
  const top  = currentPositionReverse(offsetY, pageTarget);

  //控件位置信息
  let position = {
    x:0,
    y:0
  }
  
  const chopStampLeft = (pageOffsetWidth + pageTarget.width) - element.size.width;  // 骑缝章偏移量
  let outLeft = (pageOffsetWidth + pageTarget.width) - element.size.width;  //当前图片最大left值
  let inLeft = pageOffsetWidth + offsetX;  //当前图片left值
  if(minTarget){
    const minLeft = minTarget.minWidth - element.size.width //所选文档最小的图片的最大left值
    position.x = element.controlType == 'chop-stamp' ? 
        chopStampLeft :
        // (inLeft >= minLeft ?
        // (outLeft > minLeft ? minLeft  : outLeft) : 
        // (inLeft > minLeft ?  minLeft : inLeft ))
        (offsetX >= minLeft ? (minLeft + pageOffsetWidth) : (inLeft > outLeft ? outLeft : inLeft))
        
 }else{
      position.x = element.controlType == 'chop-stamp' ? 
        chopStampLeft :
        ((pageOffsetWidth + offsetX) >= ((pageOffsetWidth + pageTarget.width) - element.size.width)?
        outLeft : 
        inLeft)
 }
 if(position.x <=0){
  position.x = 0;
 }
 
  
  if(top >= pageStart && top <= pageEnd){
      position.y = top
      return position
  }
  if(top > pageEnd){
    position.y = pageEnd - element.size.height
    return position
  }
  if(top < pageStart){
    position.y =  pageStart
    return position
  }
  return position
}


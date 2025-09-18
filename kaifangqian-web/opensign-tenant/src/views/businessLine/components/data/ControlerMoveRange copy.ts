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


import { CanvasZoom } from "./ControlData";
import {bubbleSort,uuid} from "/@/utils/index";

export function moveRange(opt: any) {
    //CanvasZoom
    const allHeight = (CanvasZoom.height + CanvasZoom.space) * opt.pageSize;
    console.log(allHeight, '总高度')
    if (opt.x < 0) {
        opt.x = 0;
    }
    else if (opt.x > CanvasZoom.width - opt.size.width) {
        opt.x = CanvasZoom.width - opt.size.width;
    }
    if (opt.y < CanvasZoom.space) {
        opt.y = CanvasZoom.space;
    } else if (opt.y > allHeight) {
        opt.y = allHeight;
    }
    console.log(opt, '测试1111')
    currentPage(opt);
    isControlPage(opt, allHeight);
}


//计算控件所在每页的位置
export function currentPosition(top: number, pageSize: number) {
    return Number(top) - (pageSize + 1) * CanvasZoom.space - pageSize * CanvasZoom.height;
}
//将每页的位置转换成为一大页的位置
export function currentPositionReverse(top: number, pageSize: number) {
    //拖动控件切换文档时top超过一页高度计算成所在页面top再计算总位置
    if (top > CanvasZoom.height) {
        let newTop = currentPosition(top, pageSize);
        return newTop + (pageSize + 1) * CanvasZoom.space + pageSize * CanvasZoom.height;
    }
    return Number(top) + (pageSize + 1) * CanvasZoom.space + pageSize * CanvasZoom.height;

}

//计算控件所在的页面
function currentPage(opt: any) {

    for (var i = 0; i < opt.pageSize; i++) {
        //计算控件是否再空白区域
        var start = (i + 1) * CanvasZoom.space + i * CanvasZoom.height;
        var end = (i + 1) * CanvasZoom.space + (i + 1) * CanvasZoom.height;
        // console.log(opt.y,start,end);
        if (opt.y > start && opt.y < end) {
            opt.currentPage = i;
        }
    }
    // console.log(opt);
}

//判断控件是否再边界上
function isControlPage(opt: any, allHeight: number) {
    //var temHeight = 0;
    var start = (opt.currentPage + 1) * CanvasZoom.space + opt.currentPage * CanvasZoom.height;
    var end = (opt.currentPage + 1) * CanvasZoom.space + (opt.currentPage + 1) * CanvasZoom.height;
    var top = opt.y + opt.size.height;
    // console.log("scope:",start,end,opt.y + opt.size.height);

    if (top > start && top < end) {
        return;
    }
    if (top > allHeight) {
        opt.y = allHeight - opt.size.height;
        return;
    }

    var outSize = top - end;

    if (outSize > opt.size.height / 2) {
        opt.currentPage += 1;
        opt.y = end + CanvasZoom.space;
    } else {
        opt.y = end - opt.size.height;
    }
    // console.log("超出：",outSize);
}

//判断控件是否再边界上
export function recaculateControlPosInPage(opt: any,) {

    console.log(opt, '计算位置')
    var start = (opt.position.page) * CanvasZoom.space + opt.position.page * CanvasZoom.height;
    var end = (opt.position.page + 1) * CanvasZoom.space + (opt.position.page + 1) * CanvasZoom.height;
    var top = opt.position.top;

    if (top > start && top < end) {
        return;
    }
    if (top > end) {
        opt.position.top = end - opt.size.height
        return;
    }
    if (top < end) {
        opt.position.top = start + 16;
        return;
    }
}
//批量控件位置
export function recaculateBatchControlPosInPage(opt: any, moveElement: any) {
    var start = (moveElement.page + 1) * CanvasZoom.space + moveElement.page * CanvasZoom.height;
    var end = (moveElement.page + 1) * CanvasZoom.space + (moveElement.page + 1) * CanvasZoom.height;
    var top = opt.position.top
    console.log(opt, start, end, top, '计算位置2')

    if (top > start && top < end) {
        return;
    }
    if (top > end) {
        opt.position.top = end - opt.size.height
        return;
    }
    if (top < start) {
        opt.position.top = start
        return;
    }
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

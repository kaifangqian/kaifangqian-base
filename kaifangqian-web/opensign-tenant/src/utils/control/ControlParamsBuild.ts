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

import {currentPosition} from "./ControlerMoveRange"
import {CanvasZoom,getColtrolByType} from "./ControlData"

export function controlBuildParams(controls){
  const temp:any = [];
  if(controls && controls.length>0){
    console.log("controlBuildParams",controls);
    controls.forEach((item:any)=>{
    	temp.push({
    		"fontFamily": item.style.fontFamily,
    		"fontSize": item.style.fontSize,
    		"height": item.size.height,
    		"name": item.name,
    		"offsetX": item.position.left,
    		"offsetY": currentPosition(item.position.top,item.position.page),//计算成为每页的位置
    		"pageWidth":CanvasZoom.width,
    		"pageHeight":CanvasZoom.height,
    		"page": item.position.page,
    		"placeholder": item.placeholder,
    		"relatedDocId": 0,
    		"relatedDocType": 0,
    		"isRequired": item.isRequired?1:2,
    		"interfaceParamName": item.interfaceParamName,
    		"textAlign": item.style.textAlign,
    		"type": item.type,
    		"value": item.value,
    		"width": item.size.width,
    		"written": 1,
    		"format":item.format,
    		"signerId": item.user.signerId,
    		"tenantId": item.user.tenantId,
    	})
    })
  }
  return temp;
}

export function paramBuildControls(controls,disabled?){
  const temp:any = [];
  disabled = disabled || false;
  if(controls && controls.length>0){
  	controls.forEach((item:any,index:number)=>{
  		const basaeColtrol = getColtrolByType(item.type);
  		temp.push({
        disabled:disabled,
  			id :parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
  			uid : item.id,
  			icon:basaeColtrol.icon,
  			name:item.name,
  			title:basaeColtrol.title,
  			type:item.type,
  			placeholder:item.placeholder,
  			value:item.value,
  			controlClick:false,
  			zoom:basaeColtrol.zoom,
  			move:basaeColtrol.move,
  			isRequired:item.isRequired == 1,
  			attr:basaeColtrol.attr,
  			format:item.format,
        interfaceParamName: item.interfaceParamName,
  			style:{
  				fontSize:parseInt(item.fontSize),
  				fontFamily:item.fontFamily,
  				textAlign:item.textAlign,
  			},
  			size:{
  				width:parseInt(item.width),
  				height:parseInt(item.height),
  				minWidth:basaeColtrol.size.minWidth,
  				minHeight:basaeColtrol.size.minHeight,
  			},
  			position:{
  				left: parseInt(item.offsetX),
  				top: parseInt(item.offsetY),
  				page:parseInt(item.page),
  			},
        user:{}
  		})
  	})
  }
  return temp;
}

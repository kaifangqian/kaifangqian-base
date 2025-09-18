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

/**
 * 点击其他关闭当前显示的内容
 * 
 * addOtherClickEvent
 * 
 * removeOtherClickEvent
 */

export const addOtherClickEvent=(click:any)=>{
	document.addEventListener('click', click);
}
export const removeOtherClickEvent=(click:any)=>{
	document.removeEventListener('click', click);
}

export const htmlNodeFilter=(e:any,className:string):boolean=>{
	var parentNode = e.parentNode;
	if(parentNode && parentNode.localName != 'body'){
		if(parentNode.className  && classNameIncludes(parentNode,className)){
			return true;
		}else{
			return htmlNodeFilter(parentNode,className);
		}
	}else{
		return false;
	}
}
export const test=(e:any):boolean=>{
	 
	var parentNode = e.parentNode;
	console.log(parentNode);
	if(parentNode){
		if(parentNode.className && parentNode.className.includes('this-no-close',0)){
			//closeFlag = false;
			return true;
		}else{
			return test(parentNode);
		}
	}
	return false;
	
}

function classNameIncludes(parentNode,className){
  try{
    return parentNode.className.includes(className,0)
  }catch(e){
    console.log(e);
    return false;
  }
}

export function parentElement(e:any){
	return e.parentNode;
}

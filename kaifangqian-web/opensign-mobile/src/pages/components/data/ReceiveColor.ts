/**
 * @description 签署人签署控件颜色定义
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
import type{ReceiveColor} from "./ReceiveColorItem";

const color:Array<ReceiveColor> = [
	{
		primary:"#1F56B8",
		translucent:"#5589E2",
		background:"#00A0E8",
		backgroundRgb:"0, 160, 232",
		backgroundRgba:"0, 160, 232, 0.7",
	},
	{
		primary:"#F7BA02",
		translucent:"#FEE290",
		background:"#01BAD2",
		backgroundRgb:"1, 186, 210",
		backgroundRgba:"1, 186, 210, 0.7"
	},
	{
		primary:"#6D858D",
		translucent:"#8FA2A8",
		background:"#4d9821",
		backgroundRgb:"77, 152, 33",
		backgroundRgba:"77, 152, 33, 0.7"
	},
	{
		primary:"#5C9E29",
		translucent:"#C2E8A6",
		background:"#E01515",
		backgroundRgb:"224, 21, 21",
		backgroundRgba:"224, 21, 21, 0.7",
	},
	{
		primary:"#C2D402",
		translucent:"#F6FE9F",
		background:"#FF6100",
		backgroundRgb:"255, 97, 0",
		backgroundRgba:"255, 97, 0, 0.7",
	},
	{
		primary:"#DB0FC4",
		translucent:"#F570E6",
		background:"#FAC450",
		backgroundRgb:"250, 196, 80",
		backgroundRgba:"250, 196, 80, 0.7"
	},
	{
		primary:"#F74602",
		translucent:"#FE9D77",
		background:"#005074",
		backgroundRgb:"0, 80, 116",
		backgroundRgba:"0, 80, 116, 0.7",
	},
	{
		primary:"#9D896C",
		translucent:"#CEC4B6",
		background:"#274c11",
		backgroundRgb:"39, 76, 17",
		backgroundRgba:"39, 76, 17, 0.7",
	},
	{
		primary:"#D0586C",
		translucent:"#EBB7C0",
		background:"#700b0b",
		backgroundRgb:"211, 11, 11",
		backgroundRgba:"211, 11, 11, 0.7",
	},
	{
		primary:"#1b29e2",
		translucent:"#EBB7C0",
		background:"#5E30B5",
		backgroundRgb:"94, 48, 181",
		backgroundRgba:"94, 48, 181, 0.7",
	},
	{
		primary:"#5ed772",
		translucent:"#EBB7C0",
		background:"#7d6228",
		backgroundRgb:"125, 98, 40",
		backgroundRgba:"125, 98, 40, 0.7",
    
	}
]
const warningColor = {
	primary:"#FF0000",
	translucent:"#FF4040",
	background:"#FF7373",
	backgroundRgb:"255, 115, 115",
	backgroundRgba:"255, 115, 115, 0.9",
}

export function getColor(index:number,key:string|null){
  if(index==undefined){
    return {}
  }
	if(index == -1){
		return warningColor;
	}else if(index == -2){
		return color[0];
	}
	if(key){
		return color[index][key];
	}
	return color[index];
}
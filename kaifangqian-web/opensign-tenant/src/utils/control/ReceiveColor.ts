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

import type{ReceiveColor} from "./ReceiveColorItem";

const color:Array<ReceiveColor> = [
	{
		primary:"#1F56B8",
		translucent:"#5589E2",
		background:"#ACC5F1",
		backgroundRgb:"172, 197, 241"
	},
	{
		primary:"#F7BA02",
		translucent:"#FEE290",
		background:"#FEE9A9",
		backgroundRgb:"254, 233, 169"
	},
	{
		primary:"#6D858D",
		translucent:"#8FA2A8",
		background:"#B1BEC3",
		backgroundRgb:"177, 190, 195"
	},
	{
		primary:"#5C9E29",
		translucent:"#C2E8A6",
		background:"#A7DE7D",
		backgroundRgb:"167, 222, 125"
	},
	{
		primary:"#C2D402",
		translucent:"#F6FE9F",
		background:"#F1FE6D",
		backgroundRgb:"241, 254, 109"
	},
	{
		primary:"#DB0FC4",
		translucent:"#F570E6",
		background:"#F137DB",
		backgroundRgb:"241, 55, 219"
	},
	{
		primary:"#F74602",
		translucent:"#FE9D77",
		background:"#FD713A",
		backgroundRgb:"253, 113, 58"
	},
	{
		primary:"#15B251",
		translucent:"#56EB8F",
		background:"#1FE56B",
		backgroundRgb:"31, 229, 107"
	},
	{
		primary:"#9D896C",
		translucent:"#CEC4B6",
		background:"#B6A791",
		backgroundRgb:"182, 167, 145"
	},
	{
		primary:"#D0586C",
		translucent:"#EBB7C0",
		background:"#DD8896",
		backgroundRgb:"221, 136, 150"
	},
]
const warningColor = {
	primary:"#FF0000",
	translucent:"#FF4040",
	background:"#FF7373",
	backgroundRgb:"255, 115, 115"
}

export function getColor(index:number,key:string|null){
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
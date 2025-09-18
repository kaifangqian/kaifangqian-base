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

/*
 * sessionStorage 退出或者关闭浏览器  存储的信息会丢失
 */
const storage = window.sessionStorage;
export default {
    getItem(key:string) {
        try {
			let tem = storage.getItem(key);
			if(tem){
				return JSON.parse(tem);
			}else{
				return null;
			}
			 
        } catch (err) {
			if(key){
				return storage.getItem(key);
			}else{
				return null;
			}
            
        }
    },
    setItem(key:string, val:string) {
        storage.setItem(key, JSON.stringify(val));
    },
    clear() {
        storage.clear();
    },
    keys(index:number) {
        return storage.key(index);
    },
    removeItem(key:string) {
        storage.removeItem(key);
    }
}

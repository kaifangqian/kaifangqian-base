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
 * components util
 */

/**
 * 清理空值，对象
 * @param children
 * @returns {*[]}
 */
export function filterEmpty (children = []) {
  return children.filter(c => c.tag || (c.text && c.text.trim() !== ''))
}
/**
 * 根据id查找对象
 * @param key
 * @returns {*{}}
 */
export function findMatchItemInList(list, conditions) {
    function isMatch(object) {
        return Object.keys(conditions).length > 0 ? Object.keys(conditions).every(key => {
        if (Object.prototype.toString.call(object[key]) === '[object Array]') {
            return object[key].includes(conditions[key])
        } else {
            return object[key] === conditions[key]
        }
        }) : false
    }
    for (const object of list) {
        if (isMatch(object)) {
            return object
        }
        if (object.children) {
            const res = findMatchItemInList(object.children, conditions)
            if (res) return res
        }
    }
    return null
}
/**
 * @description ：storage.ts
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
export const storage = {
  getItem(key: string) {
    const item = localStorage.getItem(key);
    if (item) {
      const { value, expires } = JSON.parse(item);
      if (!expires || expires >= Date.now()) {
        return value;
      } else {
        this.removeItem(key);
        return null;
      }
    }
    return null;
  },

  /**
   *
   * @param key
   * @param value
   * @param maxAge 相对缓存时间，单位s，默认1day，转换为expires(绝对时间)存储，传null不过期
   */
  setItem(key: string, value: string, maxAge: number | null = 60 * 60 * 24 * 1) {
    this.removeItem(key);
    const expires = maxAge && Date.now() + maxAge * 1000;
    localStorage.setItem(key, JSON.stringify({ value, expires }));
  },

  removeItem(key: string) {
    localStorage.removeItem(key);
  },

  clear() {
    localStorage.clear();
  },
};

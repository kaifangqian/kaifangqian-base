/**
 * @description types
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
/* eslint-disable @typescript-eslint/no-explicit-any */
import { VNodeTypes } from 'vue';

export interface Props {
  row: any;
  column: TableColumnsItem;
  index: number;
}

export type H = typeof h;

export type Render = (h: H, props: Props) => VNodeTypes;

export interface TableColumnsItem {
  key: string;
  title: string;
  render?: Render;
  slot?: string;
  break?: boolean; // 超长内容是否换行
}

export type TableRowItem<T = any> = T & {
  _index?: number;
  _parentIndex?: number;
  _expand?: boolean; // 是否展开子行，字段无表示当前行无展开
  _hide?: boolean; // 当前行是否隐藏
  _checked?: boolean; // 当前行是否勾选，字段无表示当前行无勾选
  _active?: boolean; // 是否点击当前行，添加背景色
  children?: TableRowItem[];
};

export interface Page {
  pageNum: number;
  pageSize: number;
}

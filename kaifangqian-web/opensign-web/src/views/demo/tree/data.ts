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

import { TreeItem } from '/@/components/Tree/index';

export const treeData: TreeItem[] = [
  {
    title: '运维 ',
    name: '运维 ',
    key: '0-0',
    icon:'ion:home',
    type:'user',
    children: [
      { title: 'leaf', key: '0-0-0' },
      {
        title: 'leaf',
        key: '0-0-1',
        children: [
          { title: 'leaf', key: '0-0-0-0', children: [{ title: 'leaf', key: '0-0-0-0-1' }] },
          { title: 'leaf', key: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    title: '运维2 ',
    name: '运维2 ',
    key: '0-9',
    icon:'ion:home',
    type:'user',
    children: [
      { title: 'leaf', key: '0-9-0' },
      {
        title: 'leaf',
        key: '0-9-1',
        children: [
          { title: 'leaf', key: '0-9-0-0', children: [{ title: 'leaf', key: '0-9-0-0-1' }] },
          { title: 'leaf', key: '0-9-0-1' },
        ],
      },
    ],
  },
  {
    title: '运维3 ',
    name: '运维3 ',
    key: '0-8',
    icon:'ant-design:api-filled',
    type:'dept',
    children: [
      { title: 'leaf', key: '0-8-0' },
      {
        title: 'leaf',
        key: '0-8-1',
        children: [
          { title: 'leaf', key: '0-8-0-0', children: [{ title: 'leaf', key: '0-8-0-0-1' }] },
          { title: 'leaf', key: '0-8-0-1' },
        ],
      },
    ],
  },
  {
    title: '后端',
    key: '1-1',
    icon:'ant-design:folder-open-filled',
    children: [
      { title: 'leaf', key: '1-1-0' },
      { title: 'leaf', key: '1-1-1' },
    ],
  },
  {
    title: '前端',
    key: '2-2',
    children: [
      { title: 'leaf', key: '2-2-0' },
      { title: 'leaf', key: '2-2-1' },
    ],
  },
];

export const treeData2: any[] = [
  {
    name: 'parent ',
    id: '0-0',

    children: [
      { name: 'leaf', id: '0-0-0' },
      {
        name: 'leaf',
        id: '0-0-1',

        children: [
          {
            name: 'leaf',

            id: '0-0-0-0',
            children: [{ name: 'leaf', id: '0-0-0-0-1' }],
          },
          { name: 'leaf', id: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    name: 'parent 2',
    id: '1-1',

    children: [
      { name: 'leaf', id: '1-1-0' },
      { name: 'leaf', id: '1-1-1' },
    ],
  },
  {
    name: 'parent 3',
    id: '2-2',

    children: [
      { name: 'leaf', id: '2-2-0' },
      { name: 'leaf', id: '2-2-1' },
    ],
  },
];

export const treeData3: any[] = [
  {
    name: 'parent ',
    key: '0-0',
    children: [
      { name: 'leaf', key: '0-0-0' },
      {
        name: 'leaf',
        key: '0-0-1',
        children: [
          {
            name: 'leaf',
            key: '0-0-0-0',
            children: [{ name: 'leaf', key: '0-0-0-0-1' }],
          },
          { name: 'leaf', key: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    name: 'parent 2',
    key: '1-1',

    children: [
      { name: 'leaf', key: '1-1-0' },
      { name: 'leaf', key: '1-1-1' },
    ],
  },
  {
    name: 'parent 3',
    key: '2-2',

    children: [
      { name: 'leaf', key: '2-2-0' },
      { name: 'leaf', key: '2-2-1' },
    ],
  },
];

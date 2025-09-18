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

import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const applicationColumn:BasicColumn[] = [
  {
    title:'应用',
    dataIndex:'application',
    slots: { customRender: 'application' },
  },
  {
    title:'可用范围',
    dataIndex:'auth',
    slots: { customRender: 'auth' },
  },
  {
    title:'操作',
    dataIndex:'action',
    width:180,
    slots: { customRender: 'action' },
  },
]

export const  appSearchFormSchema:FormSchema[] = [
  {
    field: 'application',
    label: '应用名称',
    component: 'Input',
    colProps: { span: 8 },
  }
]

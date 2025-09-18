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

import { BasicColumn, FormSchema } from '/@/components/Table';
import type { Rule } from 'ant-design-vue/es/form';

// import {businessType} from "../../doc/data"

export const registerColumns:BasicColumn[] =[
  {
    title:'部门',
    dataIndex:'useSealDept'
  },
  {
    title:'业务类型',
    dataIndex:'businessType'
  },
  {
    title:'用印方式',
    dataIndex:'useSealWay'
  },
  {
    title:'加盖印章类型',
    dataIndex:'sceneType',
    slots: { customRender: 'sceneType' },
  },
  {
    title:'印章类型',
    dataIndex:'sealType'
  },
  {
    title:'用印次数',
    dataIndex:'useSealCount',
    slots: { customRender: 'useSealCount' },
  },
]

export const registerSearchFormSchema: FormSchema[] = [
  {
    field: 'sealType',
    label: '用印部门',
    component: 'Select',
    componentProps: {
      options: [
        { label: '个人', value: 1 },
        { label: '企业', value: 2 },
        
      ],
    },
    colProps: { span:  6 },
  },
  {
    field: 'sealType',
    label: '业务类型',
    component: 'Select',
    componentProps: {
      options: [
        { label: '全部', value: "" },
        // ...businessType
      ],
    },
    colProps: { span:  6 },
  },
  {
    field: 'recordTime',
    label: '时间范围',
    component: 'RangePicker',
    required:false,
    colProps: { span: 6 },
  }
];

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
import  { getTemplateTypeList } from '/@/api/message';
import dayjs from 'dayjs';

export const messageHistorySearchFormSchema: FormSchema[] = [
    {
      field: 'mesTitle',
      label: '标题',
      component: 'Input',
      colProps: { span: 4 },
      componentProps: {
       
      }
    },
    // {
    //     field: 'templateType',
    //     label: '类型',
    //     component: 'ApiTreeSelect',
    //     colProps: { span: 4 },
    //     componentProps: {
    //         api:getTemplateTypeList,
    //         fieldNames: {
    //             label: 'name',
    //             key: 'id',
    //             value:'id'
    //           },
    //     },
    // },
    {
        field: 'readFlag',
        label: '状态',
        component: 'Select',
        colProps: { span: 4 },
        componentProps:{
          options: [
            {  label: '已读',   value: 1,  },
            {  label: '未读',   value: 0,  },
          ],
        }
    },
    {
      field: '[beginTime, endTime]',
      label: '接收时间',
      component: 'RangePicker',
      componentProps: {
        allowClear:true,
        format: 'YYYY-MM-DD',
        placeholder: ['开始时间', '结束时间'],
      },
      colProps: { span:  6 },
    },
    
   
    
];

  
export const messageHistoryColumns: BasicColumn[] = [
  {
    title: '消息类型',
    dataIndex: 'typeName',
    width: 100,
    slots: { customRender: 'mesTitle' },
  },
  {
    title: '标题',
    dataIndex: 'mesTitle',
    width: 1000,
    slots: { customRender: 'mesTitle' },
  },
  {
    title: '接收时间',
    dataIndex: 'createTime',
    width: 100,
    customRender: ({ record }) => {
      const date = record.createTime;
      return date ? dayjs(date).format('YYYY年M月D日') : '';
    },
  },
];



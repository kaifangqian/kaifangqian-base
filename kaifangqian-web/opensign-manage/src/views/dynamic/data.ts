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
import { getArticleGroupList} from '/@/api/article'; 

export const tableColumns:BasicColumn[] =[
  {
    title:'标题',
    dataIndex:'title',
    slots: { customRender: 'title' },
  },
  {
    title:'分类',
    dataIndex:'typeName',
  },
  {
    title:'发布状态',
    dataIndex:'status',
    slots: { customRender: 'status' },
  },
  {
    title:'发布时间',
    dataIndex:'createTime',
  },
  {
    title:'操作人',
    dataIndex:'author',
  },
  {
    title:'更新时间',
    dataIndex:'updateTime',
  },
  {
    title:'操作',
    dataIndex:'action',
    width:220,
    slots: { customRender: 'action' },
  },
]
export const searchFormSchema:FormSchema[] =[
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '发布状态',
    component: 'Select',
    colProps: { span: 8 },
    componentProps:{
      options:[
        {label:'全部',value:''},
        {label:'草稿',value:0},
        {label:'已发布',value:1},
        {label:'已下架',value:2},
      ]
    }
  },
]

export const createArticleGroupFormSchema:FormSchema[] = [
  {
    field: 'typeName',
    label: '分组名 ',
    component: 'Input',
    colProps: { span: 24 },
    required:true,
    componentProps: {
      disabled: false,
    }
  },
  // {
  //   field: 'groupDesc',
  //   label: '描述',
  //   component: 'InputTextArea',
  //   colProps: { span: 24 },
  //   componentProps: {
  //     disabled: false,
  //     autoSize:{ minRows: 5, maxRows: 24 }
  //  },
  // },
]
export const createArticleFormSchema:FormSchema[] = [
  {
    field: 'title',
    label: '标题 ',
    component: 'Input',
    colProps: { span: 13 },
    required:true,
    componentProps: {
      disabled: false,
    }
  },
  {
    field: 'typeId',
    label: '分类 ',
    component: 'ApiSelect',
    colProps: { span: 13 },
    required:true,
    componentProps: {
      api:getArticleGroupList,
      resultField: 'records',
      labelField:'typeName',
      valueField:'id'
    }
  },
  {
    
    field: 'author',
    label: '作者 ',
    component: 'Input',
    colProps: { span: 13 },
    required:true,
    componentProps: {
      disabled: false,
    }
  },
  {
    field: 'summary',
    label: '摘要',
    component: 'InputTextArea',
    required:true,
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
  {
    field: 'content',
    label: '正文',
    component: 'Input',
    slot:'content',
    required:true,
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]
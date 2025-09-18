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
 * @Author: ningw 
 * @Date: 2022-06-23 14:52:57 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-09-28 15:34:06
 */
import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getDictTree } from '/@/api/dict';

/**
 * 列表搜索表单
 */
export const searchFormSchema:FormSchema[] =[
  {
    field: 'name',
    label: '关键字',
    component: 'Input',
    colProps: { span: 8 },
  },
]
/**
 * 列表字段配置
 */
 export const tableColumns:BasicColumn[] =[
  {
    title:'项目名',
    dataIndex:'itemText'
  },
  {
    title:'项目值',
    dataIndex:'itemValue'
  },
  // {
  //   title:'简拼',
  //   dataIndex:'simpleName'
  // },
  {
    title:'排序',
    dataIndex:'sortOrder'
  },
  {
    title:'有效',
    dataIndex:'status',
    slots: { customRender: 'status' },
  },
  {
    title:'备注',
    dataIndex:'description'
  },
  // {
  //   title:'操作',
  //   dataIndex:'action',
  //   slots: { customRender: 'action' },
  //   align:'center',
  // },
]

/**
 * 左侧tree 表单
 */
export const treeFormSchema:FormSchema[] =[
  {
    field: 'parentId',
    label: '所属上级',
    component: 'ApiTreeSelect',
    colProps: { span: 24 },
    componentProps:{
      multiple:false,
      api: getDictTree,
      params:{
        type:'group'
      },
      fieldNames: {
        label: 'name',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
  },
  {
    field: 'dictName',
    label: '分类名称',
    required:true,
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'dictCode',
    label: '分类编码',
    required:true,
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'sortOrder',
    label: '排序',
    component: 'InputNumber',
    colProps: { span: 24 },
  },
  {
    field: 'status',
    label: '是否有效',
    component: 'Switch',
    colProps: { span: 24 },
  },
  {
    field: 'description',
    label: '备注',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]
/**
 * 列表表单
 */

 export const tableFormSchema:FormSchema[] =[
  {
    field: 'parentId',
    label: '上级',
    component: 'ApiTreeSelect',
    slot: 'parentId',
    colProps: { span: 24 },
    // componentProps:{
    //   multiple:false,
    //   api: getDictTree,
    //   params:{
    //     type:'dict'
    //   },
    //   fieldNames: {
    //     label: 'name',
    //     value: 'id',
    //   },
    //   getPopupContainer: () => document.body,
    // }
  },
  {
    field: 'itemText',
    label: '项目名',
    required:true,
    component: 'Input',
    colProps: { span: 24 },
  }, 
  {
    field: 'itemValue',
    label: '项目值',
    required:true,
    component: 'Input',
    colProps: { span: 24 },
  },
  
  {
    field: 'sortOrder',
    label: '排序',
    component: 'InputNumber',
    colProps: { span: 24 },
  },
  {
    field: 'status',
    label: '是否有效',
    component: 'Switch',
    colProps: { span: 24 },
  },
  {
    field: 'description',
    label: '备注',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]

/**
 * 单号规则列表
 */
export const serialColumns:BasicColumn[] = [
  {
    title:'单号标识',
    dataIndex:'idKey'
  },
  {
    title:'单号名称 ',
    dataIndex:'idName'
  },
  {
    title:'内容',
    dataIndex:'content'
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    align:'center',
    width:180
  },
]

/**
 * 单号搜索
 */

export const serialSearchFormSchema:FormSchema[] =[
  {
    field: 'idName',
    label: '单号名称',
    component: 'Input',
    colProps: { span: 8 },
  },
]

/**
 * 单号规则
 */
 export const serialModalFormSchema:FormSchema[] =[
  {
    field: 'idKey',
    label: '单号标识',
    required:true,
    component: 'Input',
    colProps: { span: 18 },
  },
  {
    field: 'idName',
    label: '单号名称',
    required:true,
    component: 'Input',
    colProps: { span: 18 },
  },
  // {
  //   field: 'content',
  //   label: '内容',
  //   required:false,
  //   component: 'InputTextArea',
  //   colProps: { span: 18 },
  //   componentProps: {
  //     disabled: true,
  //     autoSize:{ minRows: 5, maxRows: 24 }
  //  },
  // },
]
/**
 * 单号规则配置列表
 */

 export const serialModalColumns:BasicColumn[] = [
  {
    title:'类型',
    dataIndex:'ruleType',
    editRow: true,
    editComponent: 'Select',
    editComponentProps: {
      options: [
        { label: '文本',  value: 'text', },
        { label: '业务编号',  value: 'params', },
        { label: '日期（yyyyMMdd）',  value: 'date', },
        { label: '时间（yyyyMMdd HHmmss）',  value: 'time', },
        { label: '时间戳',  value: 'timestrap', },
        { label: '序列号', value: 'serial', },
        { label: '自定义', value: 'custom', }
      ],
    },
  },
  {
    title:'规则内容 ',
    dataIndex:'ruleContent',
    slots: { customRender: 'ruleContent' },
    editRow: true,
    editComponent: 'Input',
  },
  {
    title:'长度 ',
    dataIndex:'ruleLength',
    editRow: true,
    editComponent: 'InputNumber',
  },
]

/**
 * 系统校验规则搜索
 */
 export const verifySearchFormSchema:FormSchema[] = [
  {
    field: 'name',
    label: '规则名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'code',
    label: '规则code',
    component: 'Input',
    colProps: { span: 8 },
  },
]

/**
 * 校验规则列表
 */

 export const verifyColumns:BasicColumn[] = [
  {
    dataIndex: 'name',
    title: '规则名称',
  },
  {
    title:'规则code ',
    dataIndex:'code',
  },
  {
    title:'规则描述 ',
    dataIndex:'length',
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    align:'center',
  },
]

/**
 * 系统校验规则表单
 */
 export const verifyModalFormSchema:FormSchema[] = [
  {
    field: 'ruleName',
    label: '规则名称',
    required:true,
    component: 'Input',
    colProps: { span: 18 },
  },
  {
    field: 'ruleCode',
    label: '规则code',
    required:true,
    component: 'Input',
    colProps: { span: 18 },
  },
  {
    field: 'ruleClass',
    label: '规则描述',
    required:false,
    component: 'InputTextArea',
    colProps: { span: 18 },
    componentProps: {
      disabled: true,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]


/**
 * 系统局部规则配置列表
 */

 export const verifyLocalModalColumns:BasicColumn[] = [
  {
    title:'位数',
    dataIndex:'type',
    editRow: true,
    editComponent: 'Input',
    width:200
  },
  {
    title:'规则(正则表达式)',
    dataIndex:'content',
    editRow: true,
    editComponent: 'Input',
  },
  {
    title:'提示文本 ',
    dataIndex:'length',
    editRow: true,
    editComponent: 'Input',
  },
]

/**
 * 系统全局规则配置
 */

 export const verifyGlobalModalColumns:BasicColumn[] = [
  {
    title:'优先级',
    dataIndex:'type',
    editRow: true,
    editComponent: 'Input',
    width:200
  },
  {
    title:'规则(正则表达式)',
    dataIndex:'content',
    editRow: true,
    editComponent: 'Input',
  },
  {
    title:'提示文本 ',
    dataIndex:'length',
    editRow: true,
    editComponent: 'Input',
  },
]

/**
 * 功能测试
 */
export const dictGroupFormSchema:FormSchema[] = [
  {
    field: 'dictName',
    label: '字典名称',
    required:true,
    component: 'Input',
    colProps: { span: 18 },
  },
]
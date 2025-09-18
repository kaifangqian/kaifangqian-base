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

export const tableColumns:BasicColumn[] =[
  {
    title:'任务名称',
    dataIndex:'jobName'
  },
  {
    title:'任务描述',
    dataIndex:'jobDescription'
  },
  {
    title:'任务参数',
    dataIndex:'jobParams'
  },
  {
    title:'参数',
    dataIndex:'params'
  },
  {
    title:'cron表达式',
    dataIndex:'cronExpression'
  },
  {
    title:'状态',
    dataIndex:'isPause'
  },
  {
    title:'备注',
    dataIndex:'description'
  },
  {
    title:'创建时间',
    dataIndex:'createTime'
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
  },
]

export const searchFormSchema: FormSchema[] = [
  {
    field: 'time',
    label: '创建日期',
    component: 'DatePicker',
    colProps: { span: 8 },
  },
 
  {
    field: 'name',
    label: '任务名称',
    component: 'Select',
    colProps: { span: 8 },
  }
];

export const taskFormSchema: FormSchema[] = [
  {
    field: 'jobName',
    label: '任务名称',
    component: 'Input',
    required:true,
    colProps: { span: 24 },
  },
  {
    field: 'jobDescription',
    label: '任务描述',
    required:false,
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'jobParams',
    label: '任务参数',
    required:false,
    component: 'Input',
    colProps: { span: 24}, 
  },
  {
    field: 'executeType',
    label: '执行配置',
    required:true,
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      placeholder:'执行类型',
      options: [
        { label: '单机执行', value: 1 },
        { label: '广播执行', value: 2 },
        { label: 'Map执行', value: 3 },
        { label: 'MapReduce执行', value: 4 },
      ],
    } 
  },
  {
    field: 'processorType',
    label: '',
    required:true,
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      placeholder:'处理器类型',
      options: [
        { label: '内建', value: 1 },
        { label: '外置（动态加载）', value: 4 },
      ],
    }  
  },
  {
    field: 'processorInfo',
    label: '',
    required:true,
    component: 'Input',
    colProps: { span: 8 }, 
  },
  
];

export const taskRuleFormSchema:FormSchema[] = [
  {
    field: 'rule',
    label: '触发条件',
    component: 'CheckboxGroup',
    // defaultValue: 0,
    colProps: { span: 24 },
    componentProps: {
      options: [
        { label: '成功', value: 0 },
        { label: '失败', value: 1 },
      ],
    }
  },
  {
    field: 'rule',
    label: '通知模板',
    component: 'Select',
    defaultValue: ['0'],
    colProps: { span: 24 },
    componentProps: {
      options: [
        { label: '模板1', value: '0' },
        { label: '模板2', value: '1' },
      ],
    }
  },
  {
    field: 'receive',
    label: '接收人',
    component: 'Input',
    // defaultValue: 0,
    colProps: { span: 24 },
  }
]



export const instanceColumns: BasicColumn[] = [
  {
    title:'任务ID',
    dataIndex:'jobId'
  },
  {
    title:'任务名称',
    dataIndex:'jobName'
  },
  {
    title:'任务实例ID',
    dataIndex:'instanceId'
  },
  {
    title:'状态',
    dataIndex:'status',
    slots: { customRender: 'status' },
  },
  {
    title:'触发时间',
    dataIndex:'actualTriggerTime'
  },
  {
    title:'结束时间',
    dataIndex:'finishedTime'
  }
]

export const instanceFormSchema:FormSchema[] = [
  {
    field: 'jobId',
    label: '任务Id',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      allowClear:true
    }
  },
  {
    field: 'instanceId',
    label: '任务实例Id',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
      allowClear:true
    }
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    colProps: { span: 6 },
    componentProps: {
      allowClear:true,
      options: [
        { label: '等待派发', value: '1' },
        { label: '等待Worker接收', value: '2' },
        { label: '运行中', value: '3' },
        { label: '失败', value: '4' },
        { label: '成功', value: '5' },
        { label: '取消', value: '9' },
        { label: '手动停止', value: '10' },
      ],
    }
  },

]

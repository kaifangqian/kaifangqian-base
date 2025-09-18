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
import { getTemplateTypeList } from '/@/api/message';
import { getAnnounceTree } from '/@/api/announce'; 
import { getAppList } from '/@/api/backstage'; 

export const templateColumns:BasicColumn[] =[
  {
    title:'模板code',
    dataIndex:'templateCode',
  },
  {
    title:'模板标题',
    dataIndex:'templateTitle',
  },
  // {
  //   title:'模板内容',
  //   dataIndex:'templateContent'
  // },
  {
    title:'模板类型',
    // slots:{ customRender:'templateType'},
    dataIndex:'typeName',
  },
  {
    title:'操作',
    dataIndex:'action',
    width:180,
    slots: { customRender: 'action' },
  },
]

export const tplBtnCloumns:BasicColumn[] = [
  {
    title:'按钮code',
    dataIndex:'buttonCode',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  {
    title:'按钮名称',
    dataIndex:'buttonName',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  {
    title:'路由地址',
    dataIndex:'buttonRoute',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  {
    title:'参数',
    dataIndex:'buttonParas',
    editRow: false,
    editComponent: 'Input',
    slots: { customRender: 'buttonParas' },
    editComponentProps: {
      size:'default',
      required:false,
      disabled:true
    },
  },
  {
    title:'按钮描述',
    dataIndex:'buttonDesc',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  {
    title:'按钮样式',
    dataIndex:'buttonStyle',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  {
    title:'路由跳转方式',
    dataIndex:'routeMethod',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  {
    title:'排序',
    dataIndex:'buttonOrder',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:false,
    },
  },
  {
    title:'操作',
    dataIndex:'action',
    editRow: false,
    editComponent: 'Input',
    slots: { customRender: 'action' },
    fixed: 'right',
    editComponentProps: {
      size:'default',
      required:false,
    },
  },
 
]

export const tplSearchFormSchema: FormSchema[] = [
  {
    field: 'templateCode',
    label: '模板code',
    component: 'Input',
    colProps: { span: 8 },
  },
 
  {
    field: 'templateTitle',
    label: '模板标题',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const templateTypeForm:FormSchema[] = [
  {
    field: 'parentId',
    label: '上级',
    component: 'ApiTreeSelect',
    required: false,
    colProps: { span: 12 },
    componentProps: {
      api: getTemplateTypeList,
       disabled: false,
       fieldNames: {
          label: 'name',
          key: 'id',
          value:'id'
        },
    },
  },
  {
    field: 'typeName',
    label: '类型名称',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
  },
  {
    field: 'description',
    label: '类型描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },

]

export const tplFormSchema: FormSchema[] = [
  {
    field: 'templateCode',
    label: '模板code',
    component: 'Input',
    required: true,
    colProps: { span: 12 },
    componentProps: { disabled: false },
  },
  {
    field: 'templateType',
    label: '模板类型',
    component: 'ApiTreeSelect',
    componentProps: {
      api: getTemplateTypeList,
      disabled: false,
      fieldNames: {
         label: 'name',
         key: 'id',
         value:'id'
       },
    },
    colProps: { span: 12 },
  },
  // {
  //   field: 'msgSendType',
  //   label: '推送方式',
  //   component: 'Select',
  //   componentProps: {
  //     options: [
  //       {  label: '系统',   value: '1',  },
  //       {  label: '邮件',   value: '2',  },
  //     ],
  //   },
  //   colProps: { span: 12 },
  // },
  // {
  //   field: 'msgType',
  //   label: '消息模板类目',
  //   component: 'Select',
  //   componentProps: {
  //     options: [
  //       {  label: '线索管理',   value: '1',  },
  //       {  label: '邮件管理',   value: '2',  },
  //     ],
  //   },
  //   colProps: { span: 24 },
  // },
  {
    field: 'templateTitle',
    label: '模板标题',
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'appId',
    label: '所属应用',
    required: true,
    component: 'ApiSelect',
    slot:'appId',
    componentProps: {
      // api: getAppList,
      // disabled: false,
      // resultField: 'records',
      // labelField:'appName',
      // valueField:'id'
    },
    colProps: { span: 12 },
  },
  {
    field: 'templateContent',
    component: 'Input',
    label: '模板内容',
    rules: [{ required: false }],
    slot:'templateContent' ,
    colProps: { span: 24 },
  }
];
export const tplSendFormSchema: FormSchema[] = [
  {
    field: 'msgTitle',
    label: '模板标题',
    component: 'Input',
    colProps: { span: 24 },
    componentProps: { disabled: true },
  },
  {
    field: 'msgContent',
    label: '模板内容',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
       disabled: true,
       autoSize:{ minRows: 5, maxRows: 12 }
    },
  },
  {
    field: 'testData',
    label: '测试数据',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 12 }
   },
  },
  {
    field: 'msgType',
    label: '消息通道',
    component: 'Select',
    componentProps: {
      options: [
        {  label: '系统',   value: '1',  },
        {  label: '邮件',   value: '2',  },
      ],
    },
    colProps: { span: 24 },
  },
  {
    field: 'receive',
    component: 'Input',
    label: '消息接收方',
    colProps: { span: 24 },
  }
];
export const myMessageSearchFormSchema: FormSchema[] = [
  {
    field: 'mesTitle',
    label: '消息标题',
    component: 'Input',
    colProps: { span: 6 },
  },
  {
    field: 'templateType',
    label: '消息类型',
    component: 'ApiTreeSelect',
    colProps: { span: 6 },
    componentProps: {
      api: getTemplateTypeList,
       disabled: false,
       fieldNames: {
          label: 'name',
          key: 'id',
          value:'id'
        },
    },
  },
  {
    field: 'readFlag',
    label: '状态',
    component: 'Select',
    colProps: { span: 6 },
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
      allowClear:false,
      format: 'YYYY-MM-DD',
      placeholder: ['开始时间', '结束时间'],
    },
    colProps: { span:  6 },
  },
];

export const myMessageColumns:BasicColumn[] = [
  {
    title:'消息标题',
    dataIndex:'mesTitle'
  },
  {
    title:'消息类型',
    dataIndex:'typeName'
  },
  {
    title:'接收时间',
    dataIndex:'createTime'
  },
  {
    title:'阅读时间',
    dataIndex:'readTime'
  },
  {
    title:'状态',
    dataIndex:'isRead',
    slots: { customRender: 'isRead' },
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    fixed: 'right',
  },
]

export const messageHistorySearchFormSchema: FormSchema[] = [
  {
    field: 'channelType',
    label: '发送通道',
    component: 'Select',
    colProps: { span: 6 },
    componentProps: {
      options:[
        {  label: '短信',   value: '2',  },
        {  label: '邮件',   value: '1',  },
      ]
    }
  },
  {
    field: '[beginTime, endTime]',
    label: '发送时间',
    component: 'RangePicker',
    componentProps: {
      allowClear:true,
      format: 'YYYY-MM-DD',
      placeholder: ['开始时间', '结束时间'],
    },
    colProps: { span:  6 },
  },
  {
    field: 'account',
    label: '接收人账号',
    component: 'Input',
    colProps: { span: 6 },
    componentProps: {
     
    },
  },
  {
    field: 'sendStatus',
    label: '发送状态',
    component: 'Select',
    colProps: { span: 6 },
    componentProps:{
      options: [
        {  label: '待发送',   value: '0',  },
        {  label: '发送成功',   value: '1',  },
        {  label: '发送失败',   value: '2',  },
      ],
    }
  },
  
];
export const messageHistoryColumns:BasicColumn[] = [
  {
    title:'消息标题',
    dataIndex:'mesTitle'
  },
  {
    title:'消息类型',
    dataIndex:'typeName'
  },
  {
    title:'发送通道',
    dataIndex:'channelType',
    slots:{ customRender: 'channelType'}
  },
  {
    title:'接收人',
    dataIndex:'mesReceiver3rds'
  },
  {
    title:'发送时间',
    dataIndex:'mesSendTime'
  },
  {
    title:'发送状态',
    dataIndex:'mesSendStatus',
    slots: { customRender: 'mesSendStatus' },
  },
]


export const announceSearchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const announcementColumns:BasicColumn[] = [
  {
    title:'标题',
    dataIndex:'title',
  },
  {
    title:'分类',
    dataIndex:'typeName',
  },
  {
    title:'发布人',
    dataIndex:'sender'
  },
  {
    title:'优先级',
    dataIndex:'priority',
    width:80,
    slots: { customRender: 'priority' },
  },
  {
    title:'通告对象',
    dataIndex:'msgType',
    slots: { customRender: 'msgType' },
  },
  {
    title:'发布状态',
    dataIndex:'sendStatus',
    width:80,
    slots: { customRender: 'sendStatus' },
  },
  {
    title:'发布时间',
    dataIndex:'sendTime'
  },
  {
    title:'撤销时间',
    dataIndex:'cancelTime'
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    align:'center',
    width:180,
    fixed: 'right',
  },
]

export const announceFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    required:true,
    colProps: { span: 12 },
  },
 
  {
    field: 'announcementType',
    label: '分类',
    component: 'ApiTreeSelect',
    colProps: { span: 12 },
    required:true,
    componentProps:{
      disabled:false,
      api: getAnnounceTree,
      fieldNames: {
        label: 'name',
        key: 'id',
        value:'id'
      },
    }
  },
  {
    field: 'startTime',
    label: '开始时间',
    component: 'DatePicker',
    required:true,
    colProps: { span: 12 },
    componentProps:{
      format:'YYYY-MM-DD'
    }
  },
  {
    field: 'endTime',
    label: '结束时间',
    component: 'DatePicker',
    required:true,
    colProps: { span: 12 },
    componentProps:{
      format:'YYYY-MM-DD'
    }
  },
  {
    field: 'priority',
    label: '优先级',
    component: 'Select',
    required:false,
    colProps: { span: 12 },
    componentProps:{
      options:[
        {label:'低',value:'L'},
        {label:'中',value:'M'},
        {label:'高',value:'H'},
        

      ]
    }
  },
  {
    field: 'msgType',
    label: '公告类型',
    component: 'Select',
    required:true,
    colProps: { span: 12 },
    componentProps:{
      options:[
        {label:'指定用户',value:'USER'}, 
        {label:'全体用户',value:'ALL'}
      ]
    }
  },
  
  {
    field: 'msgAbstract',
    label: '摘要',
    component: 'InputTextArea',
    required:true,
    colProps: { span: 12 },
  },
  {
    field: 'userIds',
    label: '指定用户',
    component: 'Input',
    required:true,
    colProps: { span: 12 },
    slot:'userIds',
    ifShow: ({ values }) => {
      return values.msgType&&values.msgType === 'USER'
    },
  },
  
  {
    field: 'msgContent',
    component: 'Input',
    label: '内容',
    rules: [{ required: false }],
    slot:'msgContent' ,
    colProps: { span: 24 },
  },
  {
    field: 'files',
    component: 'Input',
    label: '附件',
    rules: [{ required: false }],
    slot:'files' ,
    colProps: { span: 24 },
  }
];

export const announceTypeForm: FormSchema[] = [
  {
    field: 'parentId',
    label: '上级',
    component: 'ApiTreeSelect',
    colProps: { span: 8 },
    componentProps:{
      api: getAnnounceTree,
      fieldNames: {
        label: 'name',
        key: 'id',
        value:'id'
      },
    }
  },
  {
    field: 'typeName',
    label: '名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'description',
    label: '类型描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
  },
 
];
export const myAnnounceSearchFormSchema: FormSchema[] = [
  {
    field: 'title',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'sender',
    label: '发布人',
    component: 'Input',
    colProps: { span: 8 },
  },
];
export const myAnnouncementColumns:BasicColumn[] = [
  {
    title:'标题',
    dataIndex:'title'
  },
  {
    title:'消息类型',
    dataIndex:'typeName'
  },
  {
    title:'发布人',
    dataIndex:'sender'
  },
  {
    title:'优先级',
    dataIndex:'priority',
    slots: { customRender: 'priority' },
  },
  {
    title:'阅读状态',
    dataIndex:'isRead',
    slots: { customRender: 'isRead' },
  },
  {
    title:'发布时间',
    dataIndex:'sendTime'
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    align:'center',
    fixed: 'right',
  },
]
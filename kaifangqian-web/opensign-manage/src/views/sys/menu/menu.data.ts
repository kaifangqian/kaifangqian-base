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
 * @Date: 2022-06-28 18:44:40 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-09-07 17:17:34
 */
import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getMenuList ,getCatalogueMenuList} from '/@/api/demo/system';
import { h } from 'vue';
import { Icon } from '/@/components/Icon';

export const columns: BasicColumn[] = [
  {
    title: '菜单名称',
    dataIndex: 'name',
    width: 200,
    align: 'center',
  },
  {
    title: '类型',
    dataIndex: 'menuType',
    width: 200,
    slots: { customRender: 'menuType' },
    align: 'center',
  },
  // {
  //   title: '图标',
  //   dataIndex: 'icon',
  //   width: 50,
  //   customRender: ({ record }) => {
  //     return h(Icon, { icon: record.icon });
  //   },
  // },
  {
    title: '路由',
    dataIndex: 'path',
  },
  {
    title: '组件',
    dataIndex: 'component',
  },
  {
    title: '排序',
    dataIndex: 'sortNo',
    width: 50,
  }
];

const isDir = (type: string | number) => type === 0;
const isMenu = (type: string) => type === 'true';
const isButton = (type: string) => type === '2';

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '菜单名称',
    component: 'Input',
    colProps: { span: 8 },
  }
];
export const searchTableFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '表名',
    component: 'Input',
    colProps: { span: 8 },
  }
];

export const formSchema: FormSchema[] = [
  {
    field: 'name',
    label: '菜单名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'menuType',
    label: '类型',
    component: 'RadioGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '目录', value: 0 },
        { label: '菜单', value: 1 },
        // { label: '按钮', value: 2 },
      ],
    },
    // colProps: { lg: 24, md: 24 },
  },

  {
    field: 'parentId',
    label: '所属目录',
    component: 'ApiTreeSelect',
    componentProps: {
      api:getCatalogueMenuList,
      fieldNames: {
        label: 'name',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
  },

  {
    field: 'sortNo',
    label: '排序',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'path',
    label: '路由地址',
    component: 'Input',
    required: true,
  },
  {
    field: 'component',
    label: '组件路径',
    component: 'Input',
  },
  {
    field: 'perms',
    label: '权限标识',
    component: 'Input',
  },
  {
    field: 'icon',
    label: '图标',
    component: 'IconPicker',
    required: false,
    // ifShow: ({ values }) => isDir(values.menuType),
  },
  {
    field: 'hiddenFlag',
    label: '隐藏路由',
    component: 'Switch',
    defaultValue: false,
    componentProps: {
      options: [
        { label: '是', value: true },
        { label: '否', value: false },
      ],
      checkedChildren:'是',
      unCheckedChildren:'否'
    },
  },
  {
    field: 'keepAliveFlag',
    label: '缓存路由',
    component: 'Switch',
    defaultValue: false,
    componentProps: {
      options: [
        { label: '否', value: false },
        { label: '是', value: true },
      ],
      checkedChildren:'是',
      unCheckedChildren:'否'
    },
  },
  {
    field: 'routeFlag',
    label: '是否外链',
    component: 'Switch',
    defaultValue: false,
    componentProps: {
      options: [
        { label: '是', value: true },
        { label: '否', value: false },
      ],
      checkedChildren:'是',
      unCheckedChildren:'否'
    },
  },
  
   {
    field: 'internalOrExternal',
    label: '打开方式',
    component: 'Switch',
    defaultValue: false,
    componentProps: {
      options: [
        { label: '内部', value: false },
        { label: '外部', value: true },
      ],
      checkedChildren:'外部',
      unCheckedChildren:'内部'
    },
  },
  //  {
  //   field: 'ruleFlag',
  //   label: '默认权限',
  //   component: 'RadioGroup',
  //   defaultValue: 1,
  //   componentProps: {
  //     options: [
  //       { label: '是', value: 1 },
  //       { label: '否', value: 0 },
  //     ],
  //     checkedChildren:'是',
  //     unCheckedChildren:'否'
  //   },
  // },
];
/**
 * 权限表
 */

export const tableColumns:BasicColumn[] = [
  {
    title:'表名',
    dataIndex:'tableName'
  },
  {
    title:'描述',
    dataIndex:'tableTxt'
  },
  {
    title:'版本',
    dataIndex:'tableVersion'
  },
]

/**
 * 按钮列表
 */

export const btnCloumns:BasicColumn[] = [
  {
    title:'按钮名称',
    dataIndex:'name',
    // edit:true,
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      size:'default',
      required:true,
    },
  },
  // {
  //   title:'路径 ',
  //   dataIndex:'path',
  //   editRow: true,
  //   editComponent: 'Input',
  //   editComponentProps: {
  //     required:true,
  //   },
  // },
  {
    title:'授权标识',
    dataIndex:'perms',
    editRow: true,
    editComponent: 'Input',
    editComponentProps: {
      required:true,
    },
  },
  {
    title:'权限表',
    dataIndex:'formTableId',
    editRow: false,
    editComponent: 'Select',
    width:250,
    slots: { customRender: 'formTableId' },
    editComponentProps: {
      required:false,
    },
  },
  {
    title:'默认权限',
    dataIndex:'ruleFlag',
    editRow: false,
    editComponent: 'Select',
    slots: { customRender: 'ruleFlag' },
    editComponentProps: {
      required:true,
      options: [
        { label: '是', value: 1 },
        { label: '否', value: 0 },
      ],
    },
  },
  // {
  //   title:'按钮状态',
  //   dataIndex:'status',
  //   editRow: true,
  //   editComponent: 'Select',
  //   editComponentProps: {
  //     required:true,
  //     defaultValue:1,
  //     options: [
  //       { label: '有效', value: 1 },
  //       { label: '无效', value: 0 },
  //     ],
  //   },
  // },
]


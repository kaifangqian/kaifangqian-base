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
 * @Date: 2022-06-23 19:29:57 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-09-05 15:02:34
 */

import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getAuthGroup } from '/@/api/auth/group'; 

/**
 * 列表搜索表单
 */
export const searchFormSchema:FormSchema[] =[
  {
    field: 'name',
    label: '名称',
    component: 'Input',
    colProps: { span: 8 },
  },
]
/**
 * 新建权限组
 */
export const createGroupColumns:BasicColumn[] = [
  {
    title:'名称',
    dataIndex:'name',
    editRow: true,
    editComponent: 'Input',
    width:200
  },
  {
    title:'分类',
    dataIndex:'type',
    editRow: true,
    width:200,
    editComponent: 'ApiSelect',
    editComponentProps: {
      api: getAuthGroup,
      resultField: 'record',
      labelField:'groupName',
      valueField:'id'
    },
  },
  
]
/**
 * 新建权限组表单
 */
export const createFormSchema:FormSchema[] = [
  {
    field: 'groupName',
    label: '名称 ',
    component: 'Input',
    colProps: { span: 12 },
    required:true,
    componentProps: {
      disabled: false,
   }
  },
  {
    field: 'parentId',
    label: '分类',
    component: 'ApiSelect',
    colProps: { span: 12 },
    required:true,
    componentProps:{
      api: getAuthGroup,
      resultField: 'result',
      labelField:'groupName',
      valueField:'id'
    }
  },
  {
    field: 'groupDesc',
    label: '描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]

/**
 * 新建权限组分组
 */

 export const createAuthGroupFormSchema:FormSchema[] = [
  {
    field: 'groupName',
    label: '分组名 ',
    component: 'Input',
    colProps: { span: 24 },
    required:true,
    componentProps: {
      disabled: false,
    }
  },
  {
    field: 'groupDesc',
    label: '描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]



/**
 * 列表字段配置
 */
 export const tableColumns:BasicColumn[] =[
  {
    title:'持有人',
    dataIndex:'authorizedTenantUserName'
  },
  {
    title:'授权有效期',
    dataIndex:'timeLimit'
  },
  {
    title:'授权状态',
    dataIndex:'authType'
  },
  {
    title:'授权时间',
    dataIndex:'authTime'
  },
  {
    title:'操作',
    dataIndex:'action',
    slots: { customRender: 'action' },
    flag:'ACTION',
    align:'center',
  },
]

/**
 * 权限组基本信息
 */
export const treeFormSchema:FormSchema[] = [
  {
    field: 'groupName',
    label: '名称',
    required:true,
    component: 'Input',
    colProps: { span: 12 },
  },
  {
    field: 'parentId',
    label: '分类',
    component: 'ApiSelect',
    colProps: { span: 12 },
    required:true,
    componentProps:{
      api: getAuthGroup,
      resultField: 'result',
      labelField:'groupName',
      valueField:'id'
    }
  },
  {
    field: 'groupDesc',
    label: '描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]

/**
 * 权限组基本信息
 */
export const authBasicForm:FormSchema[] = [
 
]

/**
 * 成员管理
 */

export const userColumns:BasicColumn[] = [
  {
    title:'名称',
    dataIndex:'name',
    slots: { customRender: 'name' },
    align: 'left',
  },
]
/**
 * 成员管理
 */

export const roleColumns:BasicColumn[] = [
  {
    title:'名称',
    dataIndex:'roleName',
    slots: { customRender: 'roleName' },
    align: 'left',
  },
]
/**
 * 权限管理
 */

export const authColumns:BasicColumn[] = [
  {
    title:'名称',
    dataIndex:'dataName',
    align: 'left',
    width:250,
    slots: { customRender: 'dataName' },
  },
  {
    title:'说明',
    dataIndex:'dataDesc',
    align: 'left',
  },
]
/**
 * 策略新建
 */

export const ployFormSchema:FormSchema[] = [
  {
    field: 'dataName',
    label: '策略名称',
    required:true,
    component: 'Input',
    colProps: { span: 24 },
  },
  {
    field: 'dataDesc',
    label: '描述',
    component: 'InputTextArea',
    colProps: { span: 24 },
    componentProps: {
      disabled: false,
      autoSize:{ minRows: 5, maxRows: 24 }
   },
  },
]


export const authLogTableColumns:BasicColumn[] =[
  {
    title:'授权操作类型',
    dataIndex:'authOperateType'
  },
  {
    title:'操作时间',
    dataIndex:'createTime'
  },
  {
    title:'授权时间',
    dataIndex:'authTimeType'
  },
  {
    title:'被授权对象',
    dataIndex:'authorizedTenantUserName'
  },
  {
    title:'授权人对象',
    dataIndex:'authorizedTenantUserName'
  },
]


export function getSealType(value:number):any {
	let result =  sealType.filter((item:any) => {
			return item.value == value 
	})[0]
  if(result){
    return result;
  }else{
    return {};
  }
}
export const sealType = [
  //1、公章；2、财务专用章；3、合同专用章；4、人事专用章；5、其他
  {
    value:1,
    label:'公章'
  },
  {
    value:2,
    label:'财务专用章'
  },
  {
    value:3,
    label:'合同专用章'
  },
  {
    value:4,
    label:'人事专用章'
  },
  {
    value:5,
    label:'其他'
  },
]

export const sealState = [
  //制作中、制作失败、已停用、已启用、已收缴、已销毁；
  {
    value:1,
    label:'制作中',
    operateLabel:"制作中"
  },
  {
    value:2,
    label:'制作失败',
    operateLabel:"制作失败"
  },
  {
    value:3,
    label:'已停用',
    operateLabel:"停用"
  },
  {
    value:4,
    label:'已启用',
    operateLabel:"启用"
  },
  {
    value:5,
    label:'已收缴',
    operateLabel:"收缴"
  },
  {
    value:6,
    label:'已销毁',
    operateLabel:"销毁"
  },
]

export function getSealState(value:number):any {
	let result =  sealState.filter((item:any) => {
			return item.value == value 
	})[0]
  if(result){
    return result;
  }else{
    return {};
  }
}



export const sealApplyState = [
  //制作中、制作失败、已停用、已启用、已收缴、已销毁；
  {
    value:1,
    label:'制作',
    operateLabel:"制作"
  },
  {
    value:2,
    label:'编辑',
    operateLabel:"编辑"
  },
  {
    value:3,
    label:'章面变更',
    operateLabel:"章面变更"
  },
  {
    value:4,
    label:'启用',
    operateLabel:"启用"
  },
  {
    value:5,
    label:'停用',
    operateLabel:"停用"
  },
  {
    value:6,
    label:'收缴',
    operateLabel:"收缴"
  },
  {
    value:7,
    label:'销毁',
    operateLabel:"销毁"
  }
]
export function getSealApplyState(value:number):any {
	let result =  sealApplyState.filter((item:any) => {
			return item.value == value 
	})[0]
  if(result){
    return result;
  }else{
    return {};
  }
}

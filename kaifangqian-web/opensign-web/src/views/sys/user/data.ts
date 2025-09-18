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
 * @Date: 2022-06-21 18:39:26 
 * @Last Modified by: ningw
 * @Last Modified time: 2023-10-17 10:45:11
 */
import { FormSchema } from '/@/components/Table';
import { BasicColumn } from '/@/components/Table';
import { getDictItemsByFixedId } from '/@/api/dict';



export const passwordSchema: FormSchema[] = [
  {
    field: 'phone',
    label: '绑定手机:',
    component: 'Input',
    required:true,
    colProps: { span:24 },
    componentProps: {}
  },
  {
    field: 'sms',
    label: '手机验证码:',
    component: 'Input',
    required:true,
    slot:'smsInput',
    colProps: { span:24 },
    componentProps: {},
  }
]
export const signPasswordSchema: FormSchema[] = [
  {
    field: 'phone',
    label: '手机号或邮箱:',
    component: 'Select',
    required:true,
    colProps: { span:24 },
    componentProps: {}
  },
  {
    field: 'password',
    label: '新密码:',
    component: 'Input',
    required:true,
    slot:'password',
    colProps: { span:24 },
    componentProps: {},
  },
  {
    field: 'sms',
    label: '确认密码:',
    component: 'Input',
    required:true,
    slot:'smsInput',
    colProps: { span:24 },
    componentProps: {},
  },
]

export const authColumns: BasicColumn[] =  [
  {
    title:'权限组名',
    dataIndex:'groupName'
  },
  {
    title:'类型',
    dataIndex:'type',
    slots: { customRender: 'type' }
  },
  {
    title:'名称',
    dataIndex:'name',
    slots: { customRender: 'name' }
  },
]

export const personalSchema: FormSchema[] = [
  {
    field: 'idPic1',
    label: '身份证正面:',
    component: 'Input',
    slot:'idPic1',
    required:true,
    colProps: { span:12 },
    componentProps: {}
  },
  {
    field: 'idPic2',
    label: '身份证反面:',
    component: 'Input',
    slot:'idPic2',
    required:true,
    colProps: { span:12 },
    componentProps: {}
  },
  {
    field: 'tenantName',
    label: '姓名:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  },
  {
    field: 'phone',
    label: '联系方式:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  },
  {
    field: 'organizationNo',
    label: '证件号:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  },
  {
    field: 'ProvinceCityDistrict',
    label: '省市区',
    component: 'ApiCascader',
    required:true,
    show:true,
    slot:'ProvinceCityDistrict',
    colProps: { span: 12 },
    componentProps:{
      initFetchParams: {
        parentId:'100000',
        pageSize:100
      },
      maxTagCount:2,
      asyncFetchParamKey:'parentId',
      labelField:'itemText',
      isLoadData:true,
      showSearch:true,
      valueField:'id',
      api: getDictItemsByFixedId,
      resultField: 'records',
      isLeaf: (record) => {
        return record.childCount == 0;
      },
    },
  },
  {
    field: 'organizationAddress',
    label: '详细地址:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  },
  {
    field: 'lifespanType',
    label: '证件有效期:',
    component: 'Input',
    slot:'lifespanType',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  }
]

export const enterpriseSchema: FormSchema[] = [
  {
    field: 'organizationPic',
    label: '上传证件:',
    component: 'Input',
    slot:'organizationPic',
    required:true,
    colProps: { span:24 },
    componentProps: {}
  },
  {
    field: 'tenantName',
    label: '机构名称:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {}
  },
  {
    field: 'corporation',
    label: '法定代表人:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {}
  },
  {
    field: 'organizationNo',
    label: '机构代码:',
    component: 'Input',
    required:false,
    colProps: { span:12 },
    componentProps: {}
  },
  {
    field: 'ProvinceCityDistrict',
    label: '省市区',
    component: 'ApiCascader',
    required:true,
    show:true,
    colProps: { span: 12 },
    componentProps:{
      initFetchParams: {
        parentId:'100000',
        pageSize:100
      },
      maxTagCount:2,
      asyncFetchParamKey:'parentId',
      labelField:'itemText',
      isLoadData:true,
      showSearch:true,
      valueField:'id',
      api: getDictItemsByFixedId,
      resultField: 'records',
      isLeaf: (record) => {
        return record.childCount == 0;
      },
    },
  },
  {
    field: 'organizationAddress',
    label: '详细地址:',
    component: 'Input',
    required:true,
    colProps: { span:24 },
    componentProps: {},
  },
  {
    field: 'lifespanType',
    label: '证件有效期:',
    component: 'Input',
    slot:'lifespanType',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  },
  {
    field: 'phone',
    label: '联系方式:',
    component: 'Input',
    required:true,
    colProps: { span:12 },
    componentProps: {},
  },
]
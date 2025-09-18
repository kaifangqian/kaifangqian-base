<!--
  @description 开放签

  Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.

  注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
  必须公开修改后的完整源码（包括衍生作品），详见协议全文。
-->

<template>
  <div>
    <BasicTable @register="registerTable" >
      
     
    </BasicTable>
  </div>
</template>
<script lang='ts'>
import { defineComponent } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { columns, searchFormSchema } from './data';
import { getTestAuthDataList } from '/@/api/test/auth'

export default defineComponent({
  name: 'Index',
  components: { BasicTable, TableAction },
  setup(){
    const [registerTable, { reload }] = useTable({
        title: '',
        api: getTestAuthDataList,
        columns,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        fetchSetting:{
          listField:'records'
        },
        showIndexColumn: true,
        rowKey:'id',
        useSearchForm: true,
        dataSource:[],
        showDragColumn:false,
        bordered: false,
        isTriggerSelect:false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
    return {
      registerTable
    }

  }
 
})
</script>
<style>
 
</style>

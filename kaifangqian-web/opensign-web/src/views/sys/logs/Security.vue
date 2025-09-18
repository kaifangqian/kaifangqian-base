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
    <BasicTable @register="registerTable">
       <template #logContent="{ record }">
          <span>{{ record.moduleName + '-' + record.methodName}}</span>
      </template>
       <template #warningType="{ text }">
          <span>{{ text == '1'?'越权':(text===2?'高频访问':'拦截')}}</span>
      </template>
       <template #warningLevel="{ text }">
          <span>{{ text == '1'?'预警':'告警'}}</span>
      </template>
    </BasicTable>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { securityColumns,securitySearchFormSchema } from './log';
  import { getSysWarninglog,getSysWarninglogInfo } from '/@/api/sys/log';

  export default defineComponent({
    name:'Security',
    components: { BasicTable, TableAction },
    setup() {
      const [registerTable] = useTable({
        api: getSysWarninglog,
        title: '',
        columns: securityColumns,
        rowKey: 'id',
        fetchSetting:{
          listField:'records'
        },
        canResize: false,
        useSearchForm: true,
        showIndexColumn: false,
        formConfig: {
          labelWidth: 100,
          schemas: securitySearchFormSchema,
        },
        striped:false,
        expandRowByClick: false,
      });
      async function handleRowChange(isExpand,record){
          console.log(isExpand,record, '--ssss-')
          if(!isExpand && !record.info) return;
          let result = await getSysWarninglogInfo({id:record.id});
          if(result){
             record.info = result;
          }
      }

     
      return {
        registerTable,
        handleRowChange
      }
    }
  });
</script>
<style lang="less" scoped>
:deep(.ant-select-dropdown){
  top:-140px!important;
}
</style>

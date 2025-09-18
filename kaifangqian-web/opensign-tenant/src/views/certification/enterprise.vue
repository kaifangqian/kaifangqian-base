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
  <div class="ent-container" style="padding:24px;">
    <BasicTable @register="registerTable">
      <template #certType="{record}">
          <div> 
              <span>{{ loadCerType(record.certType) }}</span>
          </div>
        </template>
        <template #algorithmType="{record}">
          <div> 
              <span>{{ loadCerAlgorithmType(record.algorithmType) }}</span>
          </div>
        </template>
        <template #certStatus="{record}">
          <div> 
            <a-tag :color="record.certStatus==1?'#87d068':'#f50'">{{ loadCerStatus(record.certStatus) }}</a-tag>
          </div>
        </template>
        <template #cerRange="{record}">
          <div> 
              <span>{{ record.termOfValidityStartTime + '-' + record.termOfValidityEndTime }}</span>
          </div>
        </template>
    </BasicTable>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent} from "vue"
import { BasicTable,useTable } from '/@/components/Table';
import Icon from "/@/components/Icon";
import { certicifateColumn, certificateEntSearchFormSchema } from './data';
import { getEnterpriseCerList } from '/@/api/certification';
import { loadCerStatus, loadCerAlgorithmType, loadCerType} from './transform'


export default defineComponent({
  name:"PersonalCertificate",
  components:{
    Icon,
    BasicTable
  },
  setup() {
    const [registerTable,{}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getEnterpriseCerList,
        columns:certicifateColumn,
        immediate:true,
        fetchSetting:{
          listField:'records'
        },
        // formConfig: {
        //   labelWidth: 80,
        //   schemas: certificateEntSearchFormSchema,
        // },
        rowKey:'id',
        useSearchForm: false,
        showIndexColumn: false,
        canResize: false,
        isTriggerSelect:false,
        striped:false,
        bordered:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        pagination:true,
    }); 
   
    return {
      registerTable,
      loadCerStatus, loadCerAlgorithmType, loadCerType

    }
  }
})
</script>

<style lang="less" scoped>
</style>

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
  <div class="container">
    <BasicTable @register="registerTable">
        <template #authStatus="{record}">
            <div>
                <a-tag :color="loadCertificationStatus(record.authStatus)">{{ loadCertificationText(record.authStatus) }}</a-tag>
            </div>
          </template>
        <template #tenantStatus="{record}">
          <div>
             <span>{{ record.tenantStatus==1?'启用':'停用' }}</span>
          </div>
        </template>
        <template #action="{record}">
          <a-button type="link" size="small" @click="handleUpdate(record)"> {{ record.tenantStatus==1?'停用':'启用' }}</a-button>
        </template>
    </BasicTable>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent} from "vue"
import { BasicTable,useTable } from '/@/components/Table';
import Icon from "/@/components/Icon";
import { personalColumn, personalSearchFormSchema } from './data';
import { useMessage } from '/@/hooks/web/useMessage';
import { getTenantList , updateTenantStatus} from '/@/api/tenant';
import { loadCertificationStatus,  loadCertificationText} from '/@/utils/StatusToName';
import dayjs from 'dayjs';


export default defineComponent({
  name:"Personal",
  components:{
    Icon,
    BasicTable
  },
  setup() {

    const { createMessage:msg, createConfirm } = useMessage();

    const [registerTable,{reload,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getTenantList,
        columns:personalColumn,
        immediate:true,
        fetchSetting:{
          listField:'records',
        },
        formConfig: {
          labelWidth: 80,
          schemas: personalSearchFormSchema,
        },
        searchInfo:{
          tenantType:2
        },
        rowKey:'id',
        useSearchForm: true,
        showIndexColumn: false,
        canResize: false,
        isTriggerSelect:false,
        striped:false,
        bordered:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        pagination:true,
        beforeFetch:handleBeforeFetch
    }); 
    function handleBeforeFetch(params){
      if(params.createTime){
        params.beginTime = dayjs(params.createTime[0]).startOf('date').format('YYYY-MM-DD HH:mm:ss');
        params.endTime = params.createTime[1];
        params.createTime = undefined;
      }
    }

    function handleUpdate(row){ 
      createConfirm({
        title: `是否${row.tenantStatus==1?'停用':'启用'}该租户`, 
        content: "点击确定按钮时，该对话框将在1秒后关闭",
        okText:'确定',
        iconType: 'warning',
        onOk() {
          setTenantStatus(row)
        },
      })
    }
    async function setTenantStatus(row){
      let result = await updateTenantStatus({id:row.id,tenantStatus:row.tenantStatus==1?2:1});
      if(result){
        reload();
        msg.success('操作成功')
      }
    }
    return {
      registerTable,
      handleUpdate,
      loadCertificationStatus,
      loadCertificationText

    }
  }
})
</script>

<style lang="less" scoped>
</style>

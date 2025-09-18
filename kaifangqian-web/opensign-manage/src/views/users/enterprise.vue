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
        <!-- <template #sysType="{record}">
          <div>
             
             <a-tag color="green" v-if="record.sysType=='core'">核心企业</a-tag>
             <a-tag v-else>普通企业</a-tag>
          </div>
        </template> -->
        <template #action="{record}">
          <a-button type="link" size="small" @click="handleUpdate(record)"> {{ record.tenantStatus==1?'停用':'启用' }}</a-button>
          <!-- <a-button type="link" size="small" @click="handleTenantTypeUpdate(record,1)" v-if="record.sysType !== 'core'">升级用户类型</a-button>
          <a-button type="link" size="small" @click="handleTenantTypeUpdate(record,2)" v-else>降级用户类型</a-button> -->
        </template>
      </BasicTable>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue"
import { BasicTable,useTable } from '/@/components/Table';
import Icon from "/@/components/Icon";
import { enterpriseColumn, enterpriseSearchFormSchema } from './data';
import { useMessage } from '/@/hooks/web/useMessage';
import { getTenantList , updateTenantStatus,setTenantBType} from '/@/api/tenant'
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

    const [registerTable,{reload}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getTenantList,
        columns:enterpriseColumn,
        immediate:true,
        fetchSetting:{
          listField:'records'
        },
        searchInfo:{
          tenantType:1
        },
        formConfig: {
          labelWidth: 80,
          schemas: enterpriseSearchFormSchema,
        },
        dataSource:[
          {name:'张三',phone:'1365130989',email:'2343545@qq.com',applyTime:'2023-09-13',desc:'都发到付三' },
          {name:'张三2',phone:'1365130989',email:'2343545@qq.com',applyTime:'2023-09-13',desc:'都发到付三' },
          {name:'张三3',phone:'1365130989',email:'2343545@qq.com',applyTime:'2023-09-13',desc:'都发到付三' },
        ],
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
        title: `${row.tenantStatus ==1?'停用':'启用'}提示`, 
        content: `是否${row.tenantStatus==1?'停用':'启用'}该租户`,
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
    
    function handleTenantTypeUpdate(row,type){
      createConfirm({
        title: `确认提示`, 
        content: `是否将该用户${type == 1?'升级为核心企业':'降级为普通企业'}`,
        okText:'确定',
        iconType: 'warning',
        onOk() {
          // setTenantStatus(row)
          tenantTypeUpdate(row,type)
        },
      })
    }
    async function tenantTypeUpdate(row,type){
      const result = await setTenantBType({
        sysType: type==1?'core':'base',
        tenantId:row.id
      })
      if(result.code == 200){
        reload();
        msg.success('操作成功')
      }
      
      // console.log("tenantTypeUpdate:",result);
    }
    return {
      registerTable,
      handleUpdate,
      loadCertificationStatus,
      loadCertificationText,handleTenantTypeUpdate

    }
  }
})
</script>

<style lang="less" scoped>
</style>

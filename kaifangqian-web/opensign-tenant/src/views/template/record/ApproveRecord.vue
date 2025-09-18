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
     
     <template #applyStatus="{ record }">
       {{getProcessApplyState(record.applyStatus).label}}
     </template>
     <template #action="{ record }">
       <!-- 查看 ｜撤回｜作废｜下载文档｜下载审批单｜删除 -->
       <a-button type="link" size="small" @click="viewRecord(record)">查看</a-button>
       
       <a-popconfirm :disabled="!(record.applyStatus == 1 || record.applyStatus == 2 || 
       record.applyStatus == 4 || record.applyStatus == 6)" title="确认是删除用？" ok-text="确定" cancel-text="取消" @confirm="deleteRecord(record)">
          <a-button type="link" size="small" :disabled="!(record.applyStatus == 1 || record.applyStatus == 2 || 
       record.applyStatus == 4 || record.applyStatus == 6)">删除</a-button>
       </a-popconfirm>
     </template>
   </BasicTable>
</template>

<script lang="ts">
  import { defineComponent,ref,onMounted ,unref,h,watch,createVNode} from 'vue';
  import {Modal,message} from 'ant-design-vue';
  import {ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import {BasicTable,useTable} from '/@/components/Table';
  import { tableApproveColumns,getProcessApplyState} from './data';
  import { getToken } from '/@/utils/auth';
  import {applyList,deleteApproveRecord} from "../api"
  
  export default defineComponent({
    name:'ApproveRecord',
    components: {
      BasicTable
    },
    props:{
      templateId:{
      	type: String
      }
    },
    setup(props:any){
      const [registerTable,{reload ,setProps,getDataSource}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: applyList,
        // dataSource:SealMakeData,
        columns: tableApproveColumns,
        immediate:true,
        useSearchForm: false,
        showDragColumn:false,
        showIndexColumn: false,
        isTriggerSelect:false,
        bordered: false,
        fetchSetting:{
          listField:'records'
        },
        beforeFetch:(data)=>{
          data.templateId = props.templateId
        },
        rowKey:"templateApplyId",
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
      function fetch(){
        if(props.templateId){
          reload();
        }
      }
      watch(() => props.templateId,
      (newValue,oldValue) => {
      	if(newValue){
      		fetch();
      	}
      })
      
      function deleteRecord(row){
        deleteApproveRecord({templateApplyId:row.templateApplyId}).then(res=>{
          message.success("删除成功")
          fetch();
        })
      }
      function viewRecord(row:any){
        const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
        const token = getToken() as string;
        // //bpmns/form/detail?taskId=ac6ea435-2b16-4172-aa24-7c98ded60357&disabled=false token=${token}
        const url = `${base_npmns}/#/bpmns/form/detail?businessId=${row.templateApplyId}&disabled=true&type=track&token=${token}`; 
        window.open(url);
      }
      return{
        registerTable,deleteRecord,viewRecord,getProcessApplyState
      }
    }
  })
  
</script>

<style>
</style>

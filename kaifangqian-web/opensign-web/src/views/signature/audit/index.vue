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
  <!--  <template #toolbar>
      <div style="text-align: right;width: 100%;">
        <a-button type="primary" @click="sealApply">用印申请</a-button>
        <a-button type="info">导出列表</a-button>
      </div>
    </template> -->
    <template #sealType="{ record }">
      <!-- getSealType,getSealState -->
      {{getSealType(record.sealType).label}}
    </template>
    <template #sealStatus="{ record }">
      <!-- getSealType,getSealState -->
      {{getSealState(record.sealStatus).label}}
    </template>
    <template #action="{ record }">
      <!-- 查看 ｜撤回｜作废｜下载文档｜下载审批单｜删除 -->
      <a-button type="link" size="small">查看</a-button>
      
    </template>
  </BasicTable>
  <!-- <AppAuthFormModal  @register="registerModal" @success="handleSuccess"></AppAuthFormModal> -->
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { BasicTable, useTable} from '/@/components/Table';
  import {registerColumns,registerSearchFormSchema} from "./data"
  import { message } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  import {getSealType,getSealState} from "../seal/data"
  
  import {getSealAuditList} from "./api";
  import {useRouter} from "vue-router";
  
  
  export default defineComponent({
    name:'App',
    components: { 
       BasicTable
    },
    setup() {
      const router = useRouter();
      const [registerTable,{reload}] = useTable({
        title: '',
        api: getSealAuditList,
        columns:registerColumns,
        fetchSetting:{
          listField:'records'
        },
        formConfig: {
          labelWidth: 120,
          schemas:registerSearchFormSchema,
        },
        immediate:true,
        useSearchForm: true,
        isTriggerSelect:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: true,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
      })
      const [registerModal, { openModal,closeModal }] = useModal();
      function handleShowInfo(data){
          openModal(true,{
            isUpdate:true,
            record:{
              ...data,
              view:true
            }
          })
      }
      function handleRecord(data){
        openModal(true,{
          isUpdate:true,
          record:{
            ...data,
          }
        })
      }
      function enableDisableApp(flag,id){
        
      }
      function handleSuccess(){
        reload();
      }
      function handleAdd(){
        openModal(true,{
          isUpdate:false,
        })
      }
      function sealApply(){
        router.push("/seals/apply/process");
      }
      return{
        registerTable,handleShowInfo,handleRecord,handleAdd,
        enableDisableApp,registerModal,handleSuccess,
        sealApply,getSealType,getSealState
      }
    }
  })
  
  
</script>

<style>
</style>

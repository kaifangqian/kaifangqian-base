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
   <a-card title="印章统计" style="width: 100%">
     <a-row>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">全部印章</div>
             <div class="number">{{sealStatistic.allSealsCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">已启用</div>
             <div class="number">{{sealStatistic.useCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">已停用</div>
             <div class="number">{{sealStatistic.stopCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">已收缴</div>
             <div class="number">{{sealStatistic.collectCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">已销毁</div>
             <div class="number">{{sealStatistic.destructionCount}}</div>
           </div>
         </div>
       </a-col>
     </a-row>
   </a-card>
   
   <a-card title="用印统计" style="width: 100%">
     <a-row>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">总用印次数</div>
             <div class="number">{{useSealStatistic.totalUseSealsCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">电子章用印次数</div>
             <div class="number">{{useSealStatistic.electronicUseSealCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">物理章用印次数</div>
             <div class="number">{{useSealStatistic.physicsUseSealCount}}</div>
           </div>
         </div>
       </a-col>
       <a-col :span="3">
         <div class="block-box">
           <div class="block-item">
             <div class="title">接口用印次数</div>
             <div class="number">{{useSealStatistic.interfaceUseSealCount}}</div>
           </div>
         </div>
       </a-col>
     </a-row>
   </a-card>
   
   
  <BasicTable @register="registerTable">
  <!--  <template #toolbar>
      <div style="text-align: right;width: 100%;">
        <a-button type="primary" @click="sealApply">用印申请</a-button>
        <a-button type="info">导出列表</a-button>
      </div>
    </template> -->
    <!-- <template #status="{ record }">
      <a-tag :color="getDocStat(record.status).color">{{getDocStat(record.status).label}}</a-tag>
    </template> -->
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
  // import {getDocList} from "/@/api/signature/doc"
  
  // import {getDocStat} from "./common/DocStatus";
  import {useRouter} from "vue-router";
  import {getStatisticSeal,getStatisticUseSeal} from "./api";
  
  export default defineComponent({
    name:'App',
    components: { 
       BasicTable
    },
    setup() {
      const router = useRouter();
      const [registerTable,{reload}] = useTable({
        title: '',
        //api: getDocList,
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
        beforeFetch:beforeFetch
      })
      const sealStatistic = ref({
        allSealsCount:0,
        collectCount:0,
        destructionCount:0,
        stopCount:0,
        useCount: 0,
      })
      const useSealStatistic = ref({
        totalUseSealsCount: 0,
        electronicUseSealCount: 0,
        physicsUseSealCount: 0,
        interfaceUseSealCount: 0
      })
      function beforeFetch(params){
        if(params.recordTime){
          params.startTime = params.recordTime[0];
          params.endTime = params.recordTime[1];
          params.recordTime = undefined;
        }
      }
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
      function initData(){
        getStatisticSeal({}).then(res=>{
          sealStatistic.value = res;
        });
        getStatisticUseSeal({}).then(res=>{
          useSealStatistic.value = res;
        });
      }
      //getStatisticSeal
      onMounted(()=>{
        initData();
      })
      return{
        registerTable,handleShowInfo,handleRecord,handleAdd,
        enableDisableApp,registerModal,handleSuccess,
        sealApply,useSealStatistic,sealStatistic
      }
    }
  })
  
  
</script>

<style lang="less" scoped>
  
  .block-box{
    padding:10px 30px;
  }
  .block-item{
    height: 80px;
    width: 100%;
    background-color: rgba(0, 121, 254, 1);
    border-radius: 5px;
    color:#fff;
    .title{
      padding-top: 10px;
      font-size: 16px;
      font-weight: 600;
      line-height: 30px;
      text-align: center;
    }
    .number{
      font-size: 20px;
      font-weight: 700;
      line-height: 30px;
      text-align: center;
      padding-bottom: 10px;
    }
  }
  
</style>

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
    <BasicTable @register="registerTable" 
    >
    <template #status="{text}">
      <span>{{loadInstanceStatus(text)}}</span>
    </template>
    <template #action="{ record }">
        <TableAction
          :actions="[
            {
              label:'详情',
              onClick: handleDetail.bind(null, record),
              auth:'instance:info'
            },
            {
              label:'日志',
              color:'success',
              onClick: handleLog.bind(null, record),
              auth:'instance:log'
            },
            {
              label:'重试',
              color:'warning',
              onClick: handleRetry.bind(null, record),
              auth:'instance:retry'
            },
            {
              label:'停止',
              color: 'error',
              auth:'instance:stop',
              popConfirm: {
                title: '是否确认停止',
                confirm: handleStop.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <DetailModal  @register="registerModal" @success="handleSuccess" />
    <LoglModal  @register="registerLogModal" @success="handleSuccess" />
  </div>
</template>
<script lang='ts'>
import { defineComponent,onMounted } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useModal } from '/@/components/Modal';
import {instanceColumns,instanceFormSchema} from './data';
import DetailModal from './modal/DetailModal.vue';
import LoglModal from './modal/LogModal.vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { useRoute } from "vue-router";
import { useUserStore } from '/@/store/modules/user';
import { usePermission } from '/@/hooks/web/usePermission';
import { getaskInstance,setInstanceRetry,setInstanceStop } from '/@/api/task';
import JSONbig from 'json-bigint';



export default defineComponent({
  name: 'TaskInstance',
  components:{
    BasicTable,
    TableAction,
    DetailModal,
    LoglModal
  },
  setup(){
    const { createMessage } = useMessage();
    const userStore = useUserStore();
    const appId =  userStore.getUserInfo.jobAppId;
    const route = useRoute();
    const { hasPermission } = usePermission();
    const [registerModal, { openModal }] = useModal();
    const [registerLogModal, { openModal:openLogModal }] = useModal();
    const [registerTable, { reload}] = useTable({
        title: '',
        api: getaskInstance,
        columns:instanceColumns,
        isTriggerSelect:false,
        immediate:true,
        formConfig: {
          labelWidth: 120,
          schemas: instanceFormSchema,
        },
        searchInfo:{
          appId:appId,
          jobId:route.params.jobId&&route.params.jobId !== ':jobId'?route.params.jobId:undefined
        },
        fetchSetting:{
          listField:'records'
        },
        useSearchForm: true,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: true,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
          actionColumn: {
            width: 250,
            title: '操作',
            dataIndex: 'action',
            slots: { customRender: 'action' },
            fixed: 'right',
          },
      })
      onMounted(()=>{
        // fetch()
      })
      async function fetch(){
        let result  = await getaskInstance({appId:appId, pageNo:1, pageSize: 10});
        if(result){
          result = JSONbig.parse(result);
          console.log(result,'-big 结果 --')
        }
      }

      function handleDetail(record:Recordable){
          openModal(true,{
            isUpdate:false,
            record:{
              instanceId:record.instanceId,
            }
          })
      }
      function handleLog(record:Recordable){
         openLogModal(true,{
            isUpdate:false,
            record:{
              instanceId:record.instanceId,
            }
          })
      }
     async function handleRetry(record:Recordable){
        let result = await  setInstanceRetry({instanceId:record.instanceId})
        if(result){
          createMessage.success('操作成功');
          reload();
        }
      }
      async function handleStop(record:Recordable){
        let result = await  setInstanceStop({instanceId:record.instanceId})
        if(result){
          createMessage.success('操作成功');
          reload();
        }
      }
      function loadInstanceStatus(status){
        switch(status){
          case 1:
            return '等待派发';
          case 2:
            return '等待Worker接收';
          case 3:
            return '运行中';
          case 4:
            return '失败';
          case 5:
            return '成功';
          case 9:
            return '取消';
          case 10:
            return '手动停止';
          default:
            return ''
        }
      }

      function handleSuccess(){
        reload()
      }

      return {
        registerTable,
        handleDetail,
        handleLog,
        handleRetry,
        handleStop,
        registerModal,
        registerLogModal,
        handleSuccess,
        loadInstanceStatus,
        hasPermission
      }
  }
})
</script>
<style>
 
</style>

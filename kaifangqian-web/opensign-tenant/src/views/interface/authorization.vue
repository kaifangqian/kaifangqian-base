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
      <template #toolbar>
          <a-button type="primary" @click="handleAdd">新增凭证</a-button>
      </template>
      <template #certType="{record}">
          <div> 
              <!-- <span>{{ loadCerType(record.certType) }}</span> -->
          </div>
        </template>
        <template #algorithmType="{record}">
          <div> 
              <!-- <span>{{ loadCerAlgorithmType(record.algorithmType) }}</span> -->
          </div>
        </template>
        <template #certStatus="{record}">
          <div> 
            <!-- <a-tag :color="record.certStatus==1?'#87d068':'#f50'">{{ loadCerStatus(record.certStatus) }}</a-tag> -->
          </div>
        </template>
        <template #action="{record}">
          <a-button type="link">停用</a-button>
          <a-button type="link" @click="handleEdit(record)">编辑</a-button>
        </template>
        
    </BasicTable>
  </div>
  <AddAuthorizationModal @register="registerModal" @success="handleSuccess"></AddAuthorizationModal>
</template>

<script lang="ts">
  import {ref,defineComponent} from "vue"
  import { BasicTable,useTable } from '/@/components/Table';
  import Icon from "/@/components/Icon";
  import { authorizationColumn,authorizationFormSchema } from './data';
  import { developerManageList } from '/@/api/interface';
  import AddAuthorizationModal from "./modal/AddAuthorizationModal.vue"
  import { useModal } from '/@/components/Modal';
    
  export default defineComponent({
    name:"authorization",
    components:{
      Icon,
      BasicTable,AddAuthorizationModal
    },
    setup() {
      
      const [registerModal, { openModal }] = useModal();
      
      const [registerTable,{}] = useTable({
          title: '',
          titleHelpMessage: [],
          api: developerManageList,
          columns:authorizationColumn,
          immediate:true,
          fetchSetting:{
            listField:'records'
          },
          formConfig: {
            labelWidth: 80,
            schemas: authorizationFormSchema,
          },
          rowKey:'id',
          useSearchForm: true,
          showIndexColumn: false,
          canResize: false,
          isTriggerSelect:false,
          striped:false,
          bordered:false,
          showTableSetting: false,
          tableSetting: { fullScreen: false ,redo:true,setting:false,size:false,align:'right'},
          pagination:true,
      }); 
      function handleAdd(){
        openModal(true)
      }
      function handleEdit(item){
        openModal(true,{
          isUpdate:true,
          record:item
        })
      }
      function handleSuccess(){
        
      }
      return {
        registerTable,registerModal,handleSuccess,handleAdd,handleEdit
        // loadCerStatus, loadCerAlgorithmType, loadCerType

      }
    }
  })
</script>

<style lang="less" scoped>
</style>

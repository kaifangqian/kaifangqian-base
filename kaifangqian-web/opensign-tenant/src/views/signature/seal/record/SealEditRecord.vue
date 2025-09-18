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
   <BasicTable @register="registerTable" >
     
     
     <template #sealType="{ record }">
       {{getSealType(record.sealType).label}}
     </template>
     <template #applyStatus="{ record }">
       {{getProcessApplyState(record.applyStatus).label}}
     </template>
     <template #action="{ record }">
       <!-- 查看 ｜撤回｜作废｜下载文档｜下载审批单｜删除 -->
       <a-button type="link" size="small" @click="viewRecord(record.id)">查看</a-button>
       <a-button type="link" size="small" @click="deleteRecord(record.id)">删除</a-button>
     </template>
   </BasicTable>
</template>

<script lang='ts'>
  import { defineComponent,ref,onMounted ,unref,h,watch,createVNode} from 'vue';
  import {Modal,message} from 'ant-design-vue';
  import { DownOutlined, EllipsisOutlined ,TeamOutlined,ExclamationCircleOutlined } from '@ant-design/icons-vue';
  
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import {BasicTable,useTable,TableAction,EditRecordRow,} from '/@/components/Table';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useUserStore } from '/@/store/modules/user';
  import { transferRoleType } from '/@/utils/system';
  import {getSealType,sealType} from "../data"
  import { tableSealEditColumns} from './data';
  import { sealRecordList } from '../api';
  
  import {SealRecordType,getProcessApplyState} from "./index"
  
  export default defineComponent({
    name:'SealEditRecord',
    components: {
      BasicTable,TableAction
    },
    props:{
      sealId:{
      	type: String
      }
    },
    setup(props:any){
      const [registerTable,{reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: sealRecordList,
        // dataSource:SealMakeData,
        columns: tableSealEditColumns,
        immediate:false,
        useSearchForm: false,
        showDragColumn:false,
        showIndexColumn: false,
        isTriggerSelect:false,
        bordered: false,
        // formConfig: {
        //   labelWidth: 120,
        //   schemas:searchFormSchema
        // },
        rowKey:"sealAuthId",
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
      function fetch(){
        reload({
          searchInfo:{
           sealId:props.sealId,
           operateType:SealRecordType.SealEdit
          }
        });
      }
      onMounted(() => {
        fetch();
      });
      watch(() => props.sealId,
      (newValue,oldValue) => {
      	if(newValue){
      		fetch();
      	}
      })
      
      function deleteRecord(row){
        Modal.confirm({
          title: '提示',
          icon: createVNode(ExclamationCircleOutlined),
          content: createVNode('div', { style: 'color:red;' }, `确认删除这条记录吗？`),
          onOk() {
              message.success(`删除成功`);
          },
          onCancel() {
            //console.log('Cancel');
          },
          class: 'custom-confirm',
        });
      }
      function viewRecord(rowId:string){
        
      }
      return{
        registerTable,deleteRecord,viewRecord,getSealType,getProcessApplyState
      }
    }
  })
  
</script>

<style>
</style>

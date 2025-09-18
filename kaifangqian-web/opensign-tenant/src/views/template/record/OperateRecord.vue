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
     <template #sealType="{ record }">
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
  import { tableOperateColumns} from './data';
  import { getToken } from '/@/utils/auth';
  import {operateList} from "../api"
  
  export default defineComponent({
    name:'OperateRecord',
    components: {
      BasicTable,TableAction
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
        api: operateList,
        // dataSource:SealMakeData,
        columns: tableOperateColumns,
        immediate:false,
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
        rowKey:"id",
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
       
      
      function fetch(){
        console.log("fetch templateId：",props.templateId);
        if(props.templateId){
          reload();
        }
      }
      // fetch();
      watch(() => props.templateId,
      (newValue,oldValue) => {
        console.log("watch templateId：",props.templateId);
      	if(newValue){
      		fetch();
      	}
      })
      onMounted(() => {
        console.log("OperateRecord onMounted：",props.templateId);
        // fetch();
      });
      function deleteRecord(businessId){
        Modal.confirm({
          title: '提示',
          icon: createVNode(ExclamationCircleOutlined),
          content: createVNode('div', { style: 'color:red;' }, `确认删除这条记录吗？`),
          onOk() {
              // deleteSealLog({sealApplyId:businessId}).then(res=>{
              //   message.success(`删除成功`);
              //   fetch();
              // })
          },
          onCancel() {
            //console.log('Cancel');
          },
          class: 'custom-confirm',
        });
      }
      function viewRecord(businessId:string){
        const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
        const token = getToken() as string;
        //bpmns/form/detail?taskId=ac6ea435-2b16-4172-aa24-7c98ded60357&disabled=false token=${token}
        const url = `${base_npmns}/#/bpmns/form/detail?businessId=${businessId}&disabled=true&type=track&token=${token}`; 
        window.open(url);
      }
      return{
        registerTable,deleteRecord,viewRecord
      }
    }
  })
  
</script>

<style>
</style>

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
       <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
        <BasicTable @register="registerTable">

        </BasicTable>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed  } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {  BasicTable,  useTable,  } from '/@/components/Table';
  import { getRecordList } from '/@/api/contract';
  import { recordColumns } from '../data'



  export default defineComponent({
    name: 'RecordModal',
    components:{
      BasicModal,
      BasicTable,
    },
    setup(_, { emit }){

      const isUpdate = ref(true);
      const signRuId = ref('');

      const checkedKeys = ref<Array<string | number>>([]);
     
      const { createMessage: msg } = useMessage();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          // cancelText:'关闭',
          showCancelBtn:false,
          showOkBtn:false, 
          canFullscreen: false, 
        });
        signRuId.value = data.record?.signRuId;
        checkedKeys.value = [];
        
      });
      const [registerTable,{getSelectRows}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getRecordList,
        columns: recordColumns,
        isTreeTable: true,
        useSearchForm: false,
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        fetchSetting:{
          listField:'records'
        },
        isTriggerSelect:false,
        rowKey:'sealId',
        immediate:true,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        beforeFetch:beforeFetch
       
      });
      function beforeFetch(params){
       params.signRuId = signRuId.value;
      }
      const getTitle = computed(() => (!unref(isUpdate) ? '操作记录' : '操作记录'));

      async function handleSubmit() {
        try {
          
            closeModal();
            emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        registerTable,
        checkedKeys
      };
    }
  })
</script>
<style lang="less" scoped>
  
</style>

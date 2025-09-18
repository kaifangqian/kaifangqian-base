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
        <BasicTable @register="registerTable"  :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">

        </BasicTable>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed  } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {  BasicTable,  useTable,  } from '/@/components/Table';
  import { getSealList } from '/@/api/contract';
  import { sealColumns } from '../data'



  export default defineComponent({
    name: 'SealModal',
    components:{
      BasicModal,
      BasicTable,
    },
    setup(_, { emit }){

      const isUpdate = ref(true);
      const rowId = ref('');

      const checkedKeys = ref<Array<string | number>>([]);
     
      const { createMessage: msg } = useMessage();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        rowId.value = data.record?.sealId;
        checkedKeys.value = [];
        
      });
      const [registerTable,{getSelectRows}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getSealList,
        columns: sealColumns,
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
        // actionColumn: {
        //   width: 150,
        //   title: '操作',
        //   dataIndex: 'action',
        //   slots: { customRender: 'action' },
        //   fixed: 'right',
        // },
      });

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      
      const getTitle = computed(() => (!unref(isUpdate) ? '印章选择' : '印章选择'));

      async function handleSubmit() {
        try {
            let list = getSelectRows();
            if(list.length>1){
              msg.warning('只能选择一个签章');
              return 
            }
            msg.success('保存成功');
            closeModal();
            emit('success', list);
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        registerTable,
        onSelectChange,
        checkedKeys
      };
    }
  })
</script>
<style lang="less" scoped>
  
</style>

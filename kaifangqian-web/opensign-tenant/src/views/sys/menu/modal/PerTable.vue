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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
         <div class="resrun-tree-table">
              <BasicTable @register="registerTable" 
                :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
              </BasicTable>
         </div>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref } from 'vue';
  import { BasicTable, useTable} from '/@/components/Table';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getAuthTable } from '/@/api/auth/group';
  import { tableColumns,searchTableFormSchema } from '../menu.data';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'PerTable',
    components:{
      BasicModal,
      BasicTable
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const getTitle = ref('权限表');
      const currentGroupId = ref('');
      const checkedKeys = ref<Array<string | number>>([]);
      const { createMessage: msg } = useMessage();
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        clearSelectedRowKeys()
      });

      const [registerTable,{ reload ,setProps,getSelectRows,clearSelectedRowKeys}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAuthTable,
        columns: tableColumns,
        immediate:true,
        useSearchForm: true,
        rowKey:'id',
        showDragColumn:false,
        showIndexColumn: false,
        isTriggerSelect:false,
        fetchSetting:{
          listField:'records'
        },
        bordered: false,
        formConfig: {
          labelWidth: 50,
          schemas:searchTableFormSchema
        },
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
      async function handleSubmit(){
        if(checkedKeys.value.length !== 1){
          msg.warning('只能选择一条数据')
        }
        let record = await getSelectRows();
        emit('success',record)

      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys,e){
        if(keys.length){
          currentGroupId.value = keys[0];
          setProps({
            searchInfo:{parentId:unref(currentGroupId)}
          })
          reload();
        }
      }

      return {
        registerModal,
        handleSubmit,
        getTitle,
        onTreeSelect,
        registerTable,
        checkedKeys,
        onSelectChange
      }
    },
  })
</script>
<style lang="less" scoped>
 
</style>

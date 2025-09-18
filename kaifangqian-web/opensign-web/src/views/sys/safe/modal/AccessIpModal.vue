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
        <a-alert type="info" show-icon>
         <template #message>
              <span>设置后用户可在一下IP环境中登录</span>
          </template>
        </a-alert>
        <BasicTable @register="registerTable" >
            <template #action="{ record, column, index }">
                <TableAction :actions="createActions(record, column, index)" />
            </template>
          </BasicTable>
         <a-button  type="dashed" @click="handleAdd">新增 </a-button>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed } from 'vue';
  import { BasicTable, useTable, EditRecordRow, BasicColumn, ActionItem, TableAction } from '/@/components/Table';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { accessIPColumns } from '../data';
  import { getIpList, addIpLimit, updateIpList} from '/@/api/sys/safe';

  export default defineComponent({
    name: 'AccessIpModal',
    components:{
      BasicTable,
      BasicModal,
      TableAction
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const recordId = ref('');
      const recordInfo = ref();
      const ipType = ref('');

      const { createMessage: msg } = useMessage();
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        // setTableData([])
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭' 
        });
        ipType.value = data.ipType
        setProps({
            searchInfo:{type:ipType.value}
        });
        reload();
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          recordInfo.value = data.record;
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? 'IP限制' : 'IP限制'));


      const [registerTable, { reload,setProps,getDataSource,setTableData }] = useTable({
        columns: accessIPColumns,
        api:getIpList,
        showIndexColumn: true,
        dataSource: [],
        striped:false,
        immediate:false,
        size:'small',
        fetchSetting:{
          listField:'records'
        },
        actionColumn: {
          width: 160,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
        },
        pagination: false,
      });

      function handleDelete(record: EditRecordRow, index) {
        const data = getDataSource();
        data.splice(index,1)
      }
      function handleAdd() {
        const data = getDataSource();
        const addRow: EditRecordRow = {
          type: '',
          content: '',
          length: '',
          editable: true,
          isNew: true,
          key: `${Date.now()}`,
        };
        data.push(addRow);
      }


      function createActions(record: EditRecordRow, column: BasicColumn, index: number): ActionItem[] {
          return [
            {
              label: '删除',
              onClick: handleDelete.bind(null, record, index),
              // icon:'ant-design:minus-square-outlined'
            },
          ];
      }

    
      async function handleSubmit(){
        try {
          const tableData = await getDataSource();
          setModalProps({ confirmLoading: true });
          let result;
          if(!unref(isUpdate)){
              result = await addIpLimit({...unref(recordInfo),...values});
          }else{
              result = await updateIpList({...unref(recordInfo),...values});
          }
          if(result){
            msg.success('保存成功');
            closeModal();
            emit('success');
          }else{
            msg.warning(result.message)
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return {
        registerModal,
        handleSubmit,
        getTitle,
        registerTable,
        createActions,
        handleAdd
      }
    },
  })
</script>
<style lang="less" scoped>
:deep(.ant-table-wrapper){
  border: 1px solid #e4e4e4;
  padding: 0;
  height: 300px;
  .ant-table-body{
    height:auto;
  }
}
.resrun-basic-table{
  margin-left:0;
  margin:5px 0;
}
 
</style>

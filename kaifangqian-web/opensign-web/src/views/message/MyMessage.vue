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
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess"
    :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
      <template #toolbar>
        <a-button type="default" @click="handleDelete">批量删除 </a-button>
        <a-button type="primary" @click="handelRead">标记已读 </a-button>
        <a-button type="primary" @click="handelAllRead">全部标记已读 </a-button>
      </template>
      <template #headerTop  v-if="checkedKeys&&checkedKeys.length > 0">
            <a-alert type="info" show-icon>
              <template #message>
                <template v-if="checkedKeys.length > 0">
                  <span>已选中{{ checkedKeys.length }}条记录</span>
                  <a-button type="link" @click="checkedKeys = []" size="small">清空</a-button>
                </template>
                <template v-else>
                  <span>未选中任何项目</span>
                </template>
              </template>
            </a-alert>
      </template>
      <template #isRead="{ record }">
        <Tag color="blue">{{record.isRead?'已读':'未读'}}</Tag>
      </template>
      <template #action="{ record }">
        <a-button type="link" @click="handleDetail(record)" size="small">查看</a-button>
      </template>
    </BasicTable>
    <MessageDetailModal @register="registerModal" @success="handleSuccess" />
</template>
<script lang="ts">
  import { defineComponent, ref, onMounted, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { Tag } from 'ant-design-vue';
  import { getMyMessage,readMyMessage,readAllMyMessage,deleteMessageBatch } from '/@/api/message';
  import { useModal } from '/@/components/Modal';
  import { myMessageColumns, myMessageSearchFormSchema } from './data';
  import MessageDetailModal from './modal/MessageDetailModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRoute } from 'vue-router';
  import { useAppStore } from '/@/store/modules/app';
  import { formatToDate } from '/@/utils/dateUtil';

  export default defineComponent({
    name: 'MyMessage',
    components: { BasicTable, TableAction,Tag, MessageDetailModal },
    setup() {
      const checkedKeys = ref<Array<string>>([]);
      const [registerModal, { openModal }] = useModal();
      const { createMessage: msg } = useMessage();
      const route = useRoute();
      const appStore = useAppStore();
      const [registerTable, { reload,clearSelectedRowKeys}] = useTable({
        title: '',
        api: getMyMessage,
        columns:myMessageColumns,
        formConfig: {
          labelWidth: 120,
          schemas: myMessageSearchFormSchema,
        },
        fetchSetting:{
          listField:'records'
        },
        immediate:true,
        useSearchForm: true,
        rowSelection:{
          type:'checkbox',
          onChange:onSelectChange
        },
        beforeFetch:handleFetch,
        isTriggerSelect:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: true,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
        // actionColumn: {
        //   width: 80,
        //   title: '操作',
        //   dataIndex: 'action',
        //   slots: { customRender: 'action' },
        // },
      })
      onMounted(()=>{
        if(unref(route).params.infoId && unref(route).params.infoId !== ':infoId'){
          openModal(true,{
            isUpdate:false,
            record:{
              id:unref(route).params.infoId
            }
          })
        }
      })

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function handleFetch(params){
        if(params.beginTime){
          params.beginTime = formatToDate(params.beginTime,'YYYY-MM-DD')+ ' ' + '00:00:00';
        }
        if(params.endTime){
          params.endTime = formatToDate(params.endTime,'YYYY-MM-DD')+ ' ' + '23:59:59';
        }
      }
      function handleDelete(record: Recordable) {
        console.log(record);
      }

      function handleSuccess() {
        reload();
      }
      async function handleDetail(record){
        openModal(true,{
          isUpdate:false,
          record:{
            ...unref(record)
          }
        })
        let result = await readMyMessage({ids:record.id});
        if(result){
          reload();
        }
      }
      async function getUnReadMessage(){
        let result = await getMyMessage({pageNo:1,pageSize:20,readFlag:0});
        if(result){
          appStore.setMsgCount(result.total);
        }
      }
      async function handelRead(){
        if(!unref(checkedKeys)){
          msg.warning('请选择数据');
          return 
        }
        let result = await readMyMessage({ids:unref(checkedKeys).join(',')});
        if(result){
          msg.success('标记成功');
          reload();
          checkedKeys.value = [];
          clearSelectedRowKeys()
          //查询未读消息数量
          getUnReadMessage();
        }
      }
      async function handelAllRead(){
        let result = await readAllMyMessage({});
        if(result){
          msg.success('标记成功');
          reload();
        }
      }
      async function handleDelete(){
        if(!unref(checkedKeys)){
          msg.warning('请选择数据');
          return 
        }
        let result = await deleteMessageBatch({ids:unref(checkedKeys).join(',')});
        if(result){
          checkedKeys.value = [];
          msg.success('删除成功');
          reload();
        }
      }

      function onFetchSuccess() {
        // 演示默认展开所有表项
      }

      return {
        handelAllRead,
        registerModal,
        checkedKeys,
        registerTable,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
        onSelectChange,
        handleDetail,
        handelRead,
        handleDelete
      };
    },
  });
</script>

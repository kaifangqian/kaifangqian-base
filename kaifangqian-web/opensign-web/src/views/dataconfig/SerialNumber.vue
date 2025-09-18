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
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess"
    :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
      <template #toolbar>
        <a-button type="primary" @click="handleNumberModal" v-if="hasPermission(['globalId:add'])"> 新增 </a-button>
      </template>
      <template #headerTop  v-if="checkedKeys.length > 0">
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
      <template #action="{ record }">
          <a-button type="link"  size="small" @click="handleNumberModal(record)" v-if="hasPermission(['globalId:edit'])">编辑</a-button>
            <Divider type="vertical" />
            <Dropdown   :trigger="['hover']">
              <template #overlay>
                <Menu>
                    <MenuItem>
                        <a href="javascript:;" @click="handleTest(record)"  v-if="hasPermission(['globalId:test'])">测试</a>
                    </MenuItem>
                    <MenuItem>
                        <a href="javascript:;" @click="handleDelete(record)"  v-if="hasPermission(['globalId:delete'])">删除</a>
                    </MenuItem>
                </Menu>
              </template>
            <a-button type="link"  size="small">更多<DownOutlined /></a-button>
          </Dropdown>
      </template>
    </BasicTable>
    <SerialNumModal @register="register" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { Tag,Dropdown,Menu,MenuItem, Divider } from 'ant-design-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { getOrderNumber,generateOrderNumber,deleteOrderNumber} from '/@/api/dict';
  import { serialColumns, serialSearchFormSchema } from './data';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { usePermission } from '/@/hooks/web/usePermission';

  import SerialNumModal from './modal/SerialNumModal.vue';

  export default defineComponent({
    name: '单号规则',
    components: { BasicTable, TableAction,Tag, DownOutlined ,Dropdown,Menu,MenuItem,Divider,SerialNumModal},
    setup() {
      const checkedKeys = ref<Array<string | number>>([]);
      const [register, { openModal }] = useModal();
      const { createMessage: msg } = useMessage();
      const { hasPermission } = usePermission();

      const [registerTable, { reload }] = useTable({
        title: '',
        api: getOrderNumber,
        columns:serialColumns,
        isTriggerSelect:false,
        formConfig: {
          labelWidth: 120,
          schemas: serialSearchFormSchema,
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
      })

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }

      async function handleDelete(record: Recordable) {
        let result = await deleteOrderNumber({id:record.id});
        if(result){
          reload();
        }
      }

      function handleSuccess() {
        reload();
      }
      function handleView(){

      }

      function onFetchSuccess() {
        // 演示默认展开所有表项
      }
      function handleNumberModal(record: Recordable){
        if(!record.id){
          openModal(true,{
            isUpdate:false
          })
        }else{
            openModal(true,{
              isUpdate:true,
              record:{
                ...record
              }
          })
        }
        console.log(record);
      }
      async function handleTest(record: Recordable){
         let result = await generateOrderNumber({id:record.id});
         if(result){
            msg.success('测试单号:'+result);
         }
      }

      return {
        hasPermission,
        register,
        checkedKeys,
        registerTable,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
        onSelectChange,
        handleView,
        handleTest,
        handleNumberModal
      };
    },
  });
</script>

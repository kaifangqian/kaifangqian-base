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
             <BasicTree
                ref="basicTree"
                title=""
                :toolbar="true"
                :checkable="false"
                search
                :toggleSwitch="false"
                :treeData="treeData"
                @select="onTreeSelect"
              />
              <BasicTable @register="registerTable" 
                :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
              </BasicTable>
         </div>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref } from 'vue';
  import { BasicTree,TreeItem } from '/@/components/Tree/index';
  import { BasicTable, useTable} from '/@/components/Table';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getAuthGroup,setOrgAuthData } from '/@/api/auth/group';
  import { useMessage } from '/@/hooks/web/useMessage'; 

  import { searchFormSchema,tableColumns, } from '/@/views/auth/data';

  export default defineComponent({
    name: 'AuthModal',
    components:{
      BasicModal,
      BasicTree,
      BasicTable
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const getTitle = ref('权限组');
      const treeData = ref<TreeItem[]>([]);
      const currentGroupId = ref('');
      const checkedKeys = ref<Array<string | number>>([]);
      const recordId = ref('');
      const authType = ref('');
      const departId = ref('');
      const { createMessage: msg } = useMessage();
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        recordId.value = data.record.recordId;
        departId.value = data.record.departId;
        authType.value = data.record.authType;
        checkedKeys.value = [];
        fetch()
      });

      const [registerTable,{ reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAuthGroup,
        columns: tableColumns,
        immediate:false,
        useSearchForm: false,
        rowKey:'id',
        showDragColumn:false,
        showIndexColumn: false,
        isTriggerSelect:false,
        bordered: false,
        formConfig: {
          labelWidth: 120,
          schemas:searchFormSchema
        },
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });

      async function fetch() {
        treeData.value = (await getAuthGroup({})) as unknown as TreeItem[];
        treeData.value.map(item=>{
          item.title = item.groupName;
          item.key = item.id;
          item.icon = 'ant-design:folder-twotone';
        })
        if(treeData.value.length){
            setProps({
                searchInfo:{parentId:treeData.value[0].id}
            });
            reload();
            currentGroupId.value = treeData.value[0].id;
          }
      }
      async function handleSubmit(){
        let params = {
            groupIds:unref(checkedKeys),
            authType:unref(authType),
            authId:unref(recordId),
            departId:unref(departId)
        }
        let result = await setOrgAuthData(params);
        if(result){
          closeModal();
          msg.success('操作成功');
          emit('success');
        }

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
        treeData,
        onTreeSelect,
        registerTable,
        checkedKeys,
        onSelectChange
      }
    },
  })
</script>
<style>
 
</style>

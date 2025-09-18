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
  <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" wrapClassName="business-line-modal">
    <BasicForm @register="registerForm">
      <template #parentFolderId="{model,field}">
        <a-tree-select
            v-model:value="model[field]"
            show-search
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            placeholder="请选择"
            allow-clear
            tree-default-expand-all
            :tree-data="treeData"
            tree-node-filter-prop="label"
            :fieldNames="{label:'name',value:'id'}"
          >
          </a-tree-select>
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import {lineGroupCreateFormSchema} from '../data';
  import { getBusinessLineGroupList, createBusinessGroup } from '/@/api/businessLine';

  export default defineComponent({
    name: 'BusinessGroupModal',
    components: { 
      BasicModal, 
      BasicForm
    },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const treeData:any = ref([]);
    

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:500,
          // minHeight:120
        });
        isUpdate.value = !!data?.isUpdate;
        rowId.value = data?.record.id;
        if(unref(isUpdate)){
          
          setFieldsValue({
            ...data.record
          })
        }else{  
          resetFields()
        }
        let result = await getBusinessLineGroupList({})
        if(result){
          treeData.value = result;
        }
      

      });
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 50,
        schemas: lineGroupCreateFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '分组创建' : '分组编辑'));

      async function handleSubmit() {
        try {
          let data = await validate();
          let result = await createBusinessGroup({...data,id:rowId.value})
          if(result){
            emit('success');
            closeModal();
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return {
        registerModal,
        getTitle,
        handleSubmit,
        registerForm,
        treeData 
      };
    },
  });
</script>

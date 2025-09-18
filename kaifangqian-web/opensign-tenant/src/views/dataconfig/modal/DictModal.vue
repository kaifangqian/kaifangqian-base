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
        <BasicForm @register="registerForm" >
            <template #parentId="{ model, field }">
              <!-- <div style="flex:1"> -->
                <a-tree-select 
                      v-model:value="model[field]"
                      placeholder="请选择"
                      allow-clear
                      :fieldNames="{children:'children', label:'name', value: 'id' }"
                      tree-default-expand-all
                      :tree-data="state.treeData"
                      :disabled="parentIdDisabled"
                    >
                    </a-tree-select>
              <!-- </div> -->
            </template>
        </BasicForm>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed,reactive } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicForm, useForm } from '/@/components/Form';
  import { tableFormSchema } from '../data'
  import { addDictItem, editDictItem,getDictTreeItems } from '/@/api/dict'
  import { useMessage } from '/@/hooks/web/useMessage';


  export default defineComponent({
    name: 'DictModal',
    components:{
      BasicModal,
      BasicForm,
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const parentIdDisabled = ref(false);
      const rowId = ref('');
      const tinymceValue = ref();
      const state = reactive({
        dictId:null,
        treeData:[]
      })


      const { createMessage: msg } = useMessage();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        schemas: tableFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        resetFields();
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭' 
        });
        state.dictId = data?.dictId;
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          parentIdDisabled.value = data.record.parentId?false:true;
          rowId.value = data.record.id;
          setFieldsValue({
            ...data.record,
            status:data.record.status==1?true:false
          });
        }else{
            rowId.value = '';
            parentIdDisabled.value = false;
        }
        getDictItemsTreeData(state.dictId)
      });
      async function getDictItemsTreeData(dictId){
            let result = await getDictTreeItems({dictId});
            if(result){
                  state.treeData = result;
            }
        }

      const getTitle = computed(() => (!unref(isUpdate) ? '新增字典项' : '编辑字典项'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          let result;
          let params = {
              ...values,
              status:values.status?1:0,
              dictId:state.dictId,
              id:rowId.value
          }
          if(!unref(isUpdate)){
              result = await addDictItem(params);
          }else{
              result = await editDictItem(params);
          }
          if(result){
            msg.success('保存成功');
            closeModal();
            emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }

      return { parentIdDisabled,registerModal, registerForm, getTitle, handleSubmit,tinymceValue,handleTinymceChange,state };
    }
  })
</script>
<style>
 
</style>

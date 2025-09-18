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
        <a-form  :model="dataForm" name="basic" :labelCol="{ style: 'width: 100px' }"
          aut="off" ref="dataFormRef" >
          <a-form-item label="父级目录" name="parentId">
            <a-tree-select
                v-model:value="dataForm.parentId"
                style="width: 100%"
                :tree-data="treeData"
                :field-names = "fieldNames"
                allow-clear
                :show-checked-strategy="SHOW_PARENT"
                placeholder="请选择文件夹父级目录"
                tree-node-filter-prop="label"
              />
          </a-form-item>
          <a-form-item label="文件夹名称" name="name"
              :rules="[{ required: true, message: '请输入文件夹名称' }]">
            <a-input v-model:value="dataForm.name"  size="middle" placeholder="请输入文件夹名称" class="input-width" />
          </a-form-item>
        </a-form>
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
  import { TreeSelect,message } from 'ant-design-vue';
  import {saveFolder} from "../api"
  export default defineComponent({
    name: 'AddTemplateModal',
    components:{
      BasicModal,
      BasicTree,
      BasicTable
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const record = {};
      const { createMessage: msg } = useMessage();
      const getTitle = ref('');
      const dataFormRef = ref();
      const treeData = ref([]);
      const SHOW_PARENT = TreeSelect.SHOW_PARENT;
      const dataForm = ref({
          parentId:"",
          id: "",
          name: "",
          parentFolderId: "",
      });
      const fieldNames ={children:'children', label:'name', value: 'key' }
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:600,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        treeData.value = data.treeData;
        console.log(treeData.value);
        if(dataFormRef.value){
          dataFormRef.value.resetFields();
        }
        if(isUpdate.value){
          dataForm.value.parentFolderId = data.record.parentFolderId;
          dataForm.value.parentId = data.record.parentFolderId;
          dataForm.value.name = data.record.name;
          dataForm.value.id = data.record.key;
        }
        getTitle.value = isUpdate.value?'编辑文件夹':'新增文件夹'
      });
      
      
      async function handleSubmit(){
        if(dataForm.value.parentId == '0'){
          dataForm.value.parentFolderId  = "";
        }else{
          dataForm.value.parentFolderId  = dataForm.value.parentId;
        }
        const result = await saveFolder(dataForm.value);
        if(result.code == 200){
          message.success("保存成功");
          emit("success")
          closeModal();
        }
      }
      return {
        registerModal,
        handleSubmit,treeData,SHOW_PARENT,
        getTitle,dataForm,dataFormRef,fieldNames
      }
    },
  })
</script>
<style>
 
</style>

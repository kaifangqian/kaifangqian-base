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
          <a-form-item label="目标文件夹" name="targetFolderId">
            <a-tree-select
                v-model:value="dataForm.targetFolderId"
                style="width: 100%"
                :tree-data="treeData"
                :field-names = "fieldNames"
                allow-clear
                :show-checked-strategy="SHOW_PARENT"
                placeholder="请选择要移动的文件夹"
                tree-node-filter-prop="label"
              />
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
  import {templateFolderMove,templateFolderJoin} from "../api"
  export default defineComponent({
    name: 'AddTemplateModal',
    components:{
      BasicModal,
      BasicTree,
      BasicTable
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const getTitle = ref('移动模板');
      const dataFormRef = ref();
      const treeData = ref([]);
      const record = ref({})
      const SHOW_PARENT = TreeSelect.SHOW_PARENT;
      const dataForm = ref({
          targetFolderId:null,
          ids:[] as any,
      });
      const fieldNames ={children:'children', label:'name', value: 'key' }
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:600,
          height:200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        treeData.value = data.treeData;
        record.value = data.record;
        dataForm.value.ids.push(data.record.id);
        if(dataFormRef.value){
          dataFormRef.value.resetFields();
        }
      });
      
      
      async function handleSubmit(){
        try {
          //spinning.value = true;
          const values = await dataFormRef.value.validateFields();
          const response = await templateFolderMove(dataForm.value);
         if(response.code == 200){
            message.success("模板移动成功！");
            //router.push("/template/manage")
            emit("success")
            closeModal();
          }
        } catch (errorInfo) {
          console.log('Failed:', errorInfo);
          message.warning("有必填参数未填");
        }
        // const result = await saveFolder(dataForm.value);
        // if(result.code == 200){
        //   message.success("保存成功");
        //   emit("success")
        //   closeModal();
        // }
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

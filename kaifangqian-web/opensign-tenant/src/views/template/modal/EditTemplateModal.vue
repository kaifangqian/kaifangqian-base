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
          
          <a-form-item label="模板名称" name="templateName"
              :rules="[{ required: true, message: '请输入模板名称' }]">
            <a-input v-model:value="dataForm.templateName"  size="middle" placeholder="请输入模板名称" class="input-width" />
          </a-form-item>
          <!-- <a-form-item label="业务类型" name="businessType"
              :rules="[{ required: true, message: '请选择业务类型' }]">
            <a-select v-model:value="dataForm.businessType" :options="businessType" placeholder="请选择业务类型">
            </a-select>
          </a-form-item> -->
          <a-form-item label="模板说明" name="note"
              :rules="[{ required: true, message: '请输入模板说明' }]">
            <a-textarea  v-model:value="dataForm.note" size="middle" :autosize="{ minRows: 3, maxRows: 3 }" :rows="3"  class="input-width" />
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
  import {templateEdit,getTemplateInfo} from "../api"
   // import {businessType} from "../../doc/data"
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
      const getTitle = ref('编辑模板');
      const dataFormRef = ref();
      const treeData = ref([]);
      const SHOW_PARENT = TreeSelect.SHOW_PARENT;
      const dataForm = ref({
          templateName:"",
          templateId: "",
          businessType: "",
          note: "",
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
        dataForm.value.templateId = data.record.id;
        await buildTemplateInfo(data.record.id);
      });
      async function buildTemplateInfo(templateId){
         const result = await getTemplateInfo({templateId:templateId});
         dataForm.value.templateName = result.templateVo.templateName;
         dataForm.value.businessType = result.templateVo.businessType;
         dataForm.value.note = result.templateVo.note;
      }
      
      async function handleSubmit(){
        
        const result = await templateEdit(dataForm.value);
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

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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" 
      @ok="handleSubmit" :destroyOnClose="true" :maskClosable="false">
        <BasicForm @register="registerForm">
          <template #tenantId="{model,field}">
             <a-select
                v-model:value="model[field]"
                show-search
                placeholder="请输入企业名称"
                style="width: 100%"
                :default-active-first-option="false"
                :show-arrow="false"
                :filter-option="false"
                :not-found-content="null"
                :options="state.data"
                @search="fetchUser"
              >
                <template v-if="state.fetching" #notFoundContent>
                  <a-spin size="small" />
                </template>
              </a-select>
          </template>
        </BasicForm>
    </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed,reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Icon,SvgIcon } from '/@/components/Icon';
  import { BasicForm, useForm } from '/@/components/Form';
  import {createFormSchema} from "../data";
  import { cloneDeep } from 'lodash-es';
  import { getTenantList , updateTenantStatus} from '/@/api/tenant'

  import { developerManageAdd,developerManageEdit } from '/@/api/interface';
  import { message  } from 'ant-design-vue';
 
  export default defineComponent({
    name: 'SignerModal',
    components:{
      SvgIcon,Icon,
      BasicModal,BasicForm
    },
    setup(_, { emit }){
      const recordId= ref('')
      const isUpdate = ref(false);
      const state = reactive({
        data: [],
        value: [],
        fetching: false,
        // value:"",
      });
          
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭',
          defaultFullscreen:false 
        });
        isUpdate.value = data.isUpdate;
        if(isUpdate.value){
          echoUser(data.record);
        }
        resetFields();
        setFieldsValue(data.record);
      });
     
     const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
       labelWidth: 100,
       schemas: createFormSchema,
       showActionButtonGroup: false,
       actionColOptions: {
         // span: 23,
       },
     });
     function echoUser(record){
       state.data =  [{
         label:record.tenantName,
         value:record.tenantId
       }];
       
     }
     
      const getTitle = computed(() => (!unref(isUpdate) ? '新增授权凭证' : '编辑授权凭证'));
      
      async function handleSubmit(){
        try {
          const values = await validate();
          if(isUpdate.value){
            const response = await developerManageEdit(values);
            if(response.success){
              message.success("更新成功");
              emit('success');
              closeModal();
            }
          }else{
            const response = await developerManageAdd(values);
            if(response.success){
              message.success("新增成功");
              emit('success');
              closeModal();
            }
          }
        }catch(e){
        }
      }
      async function fetchUser(val){
        state.fetching = true;
        if(val){
          const result = await getTenantList({name:val,pageNo:1,pageSize:100,tenantType:1});
          const data:any = [];
          result.records.forEach(item=>{
            data.push({
              label:item.tenantName,
              value:item.id
            })
          })
          state.data =  data;
        } 
        state.fetching = false;
      }
      return {
        registerModal,
        getTitle,state,fetchUser,
        handleSubmit, registerForm
       };
    }
  })
</script>
<style lang="less" scoped>
 
 
</style>

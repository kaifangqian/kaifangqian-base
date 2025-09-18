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
    <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="60%"
    @ok="handleSubmit"
    >
      <Tabs>
        <TabPane key="1" tab="基本信息">
            <BasicForm @register="registerForm" >
              <template #logo="{model,field}" key="logo">
                
              </template>
            </BasicForm>
        </TabPane>
        <TabPane key="3" tab="版本管理">
            <BasicTable @register="registerTable" :rowSelection="{ type: 'checkbox',selectedRowKeys: checkedKeys, onChange: onSelectChange }"> 
                <template #toolbar>
                  <a-button type="primary" @click="handleAddVersion">添加版本</a-button>
                </template>
                <template #action="{ record }">
                  <a-button type="link" size="small" @click="handleEdit(record)">编辑</a-button>
                  <a-button type="link" size="small"  @click="handleDelete(record)">删除</a-button>
                </template>
            </BasicTable>
        </TabPane>
      </Tabs>
  </BasicDrawer>
</template>

<script lang="ts">
import {reactive, ref,computed,unref} from "vue";
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import { BasicForm, useForm } from '/@/components/Form/index';
import { BasicTable,useTable   } from '/@/components/Table';
import { useDrawer } from '/@/components/Drawer';
import { Tabs } from 'ant-design-vue';
import { formSchema, appVersionColumns } from '../data';
import { addApp, updateApp, getAppInfoById, getAppVersionList } from '/@/api/backstage';
import VersionDrawer from './VersionDrawer.vue';

export default{
  name:"App",
  components:{
    Tabs,  
    TabPane: Tabs.TabPane,
    BasicDrawer,
    BasicForm,
    BasicTable,
    VersionDrawer
  },
  setup(_,{emit}) {

      const checkedKeys = ref<Array<string | number>>([]);
      const recordId = ref();
      const isUpdate = ref(false);
      const getTitle = computed(() => (!unref(isUpdate) ? '添加应用' : '编辑应用'));


      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 100,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 24, md: 24 },
      });
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        recordId.value = data.record?.id;
        isUpdate.value = data.isUpdate;
        if(unref(recordId)){
          let result = await getAppInfoById({id:unref(recordId)});
          if(result){
            setFieldsValue({
              ...result
            })
          }
        }
      })

      const [registerTable,{reload}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAppVersionList,
        columns:appVersionColumns,
        useSearchForm: false,
        showIndexColumn: false,
        showTableSetting: false,
        canResize: false,
        immediate:false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:false},
      });

      const [registerVersionDrawer, { openDrawer }] = useDrawer();

      function handleAddVersion(){
        openDrawer(true,{
          idUpdate:false,
          record:{
            appId:unref(recordId)
          }
        })
      }

      function handleEdit(record){

      }
      function handleDelete(record){

      }
      function onSelectChange(){

      }
      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          let result = reactive({});
          if(unref(isUpdate)){
            result = await addApp({
              ...values,
              id:unref(recordId)
            })
          }else{
            result = await updateApp(values);
          }
          if(result){
            closeDrawer();
            emit('success');
          }
         
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }


    return {
      registerForm,
      handleSubmit,
      registerTable,
      handleAddVersion,
      handleEdit,
      handleDelete,
      checkedKeys,
      registerDrawer,
      getTitle,
      onSelectChange,
      registerVersionDrawer


    }
  }
}
</script>

<style lang="less" scoped>
</style>

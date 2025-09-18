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
  <BasicTable @register="registerTable" 
   >
      <template #toolbar>
          <a-button type="primary" @click="handleShowDrawer({})">租户开通 </a-button>
      </template>
      <template #tenantType="{ record }">
         <a-tag :color="loadTypeColor(record.tenantType)">{{ loaType(record.tenantType) }}</a-tag>
      </template>
      <template #app="{ record }">
         <a-tag :color="loadAppColor(item)" v-for="item in record.app" :key="item" style="margin:0 2px" v-if="record.app">{{ item }}</a-tag>
         <a-tag v-else color="red">尚未分配应用</a-tag>
      </template>
      <template #status="{ record }">
          <span>{{ record.status==1?'已启用':'待启用' }}</span>
      </template>
      <template #action="{ record }">
        <a-button type="link" @click="handleShowDrawer(record)" size="small">{{  record.status!=1?'启用':'停用' }}</a-button>
        <a-button type="link" @click="handleDelete(record)" size="small">应用授权</a-button>
      </template>
    </BasicTable>
    <AppDrawer @register="registerDrawer" @success="handleSuccess" />
</template>

<script lang="ts">
  import { defineComponent, onMounted } from 'vue';
  import { BasicTable, useTable, } from '/@/components/Table';
  import { useDrawer } from '/@/components/Drawer';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { tenantColumn, tenantSearchFormSchema } from './data';

  import AppDrawer from './drawer/AppDrawer.vue';
  // import { getAppList} from '/@/api/backstage';

  export default defineComponent({
    name:'App',
    components: { 
       BasicTable,
       AppDrawer
    },
    setup() {
      const { prefixCls } = useDesign('app');
      const { hasPermission } = usePermission();
      const [registerDrawer, { openDrawer,closeDrawer }] = useDrawer();

      const [registerTable] = useTable({
        title: '',
        // api: getAppList,
        columns:tenantColumn,
        fetchSetting:{
          listField:'records'
        },
        dataSource:[
          {  tenantName:'公告',desc:'重要信息发送分段锁',status:0, applicationId:'23333',tenantType:0, app:''},
          {  tenantName:'疫情动态',desc:'真实传递疫情信息，实时追踪最新动态',status:1,  applicationId:'777777777777',tenantType:1 ,app:['电子签约','运营后台']},
          {  tenantName:'公告',desc:'地方大概个方式飞洒发士大夫',status:2,  applicationId:'8888888888',tenantType:1,app:['电子签约','租户管理后台'] },
        ],
        formConfig: {
          labelWidth: 120,
          schemas:tenantSearchFormSchema,
        },
        immediate:true,
        useSearchForm: true,
        isTriggerSelect:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: true,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
      })

      function handleDelete(record){
        console.log(record)
      }

      function handleShowDrawer(row){
        openDrawer(true,{
            isUpdate:false,
            record:{
              ...row
            }
        })
      }
      
      function loadApp(limit){
        switch(limit){
          case 0:
            return '组织/企业/团队';
          case 1:
            return '个人';
          default:
            return '';
        }
      }
      
      function loaType(version){
        switch(version){
          case 0:
            return '组织/企业/团队';
          case 1:
            return '个人';
          default:
            return '个人';
        }
      }
      function loadTypeColor(status){
        switch(status){
          case 0:
            return 'orange';
          default:
            return 'blue';
        }

      }
      function loadAppColor(status){
        switch(status){
          case 0:
            return 'orange';
          default:
            return 'blue';
        }

      }
      function handleSuccess(){
        closeDrawer()
      }
    

      onMounted(()=>{
      })
      return { 
        loadTypeColor,
        loadAppColor,
        loaType,
        handleShowDrawer,
        hasPermission,
        handleSuccess,
        prefixCls,
        handleDelete,
        loadApp,
        registerTable,
        registerDrawer,
      }
    },
  });
</script>
<style lang="less" scoped>
 @prefix-cls: ~'@{namespace}-app';
 .app-info{
   display: flex;
   align-items: center;
   .app-avatar{
    width:40px;
    height: 40px;
    display: inline-block;
    border-radius: 4px;
    background-color: #999;
    margin-right:15px;
   }
   .app-name{
    font-size: 14px;
    font-weight: 500;
    color: #000;
    margin-bottom: 5px;
   }
   .app-desc{
    font-size: 12px;
    font-weight: 400;
    color: #666;
    margin-bottom: 0px;
   }

 }
</style>

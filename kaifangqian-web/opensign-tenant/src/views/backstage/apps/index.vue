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
          <a-button type="primary" @click="handleShowDrawer({})">新增应用 </a-button>
      </template>
      <template #name="{ record }">
          <div class="app-info">
              <img class="app-avatar" src="../../../assets/svg/illustration.svg" alt="">
              <div>
                <p class="app-name">{{ record.name }}</p>
                <p class="app-desc">{{ record.desc }}</p>
              </div>
          </div>
      </template>
      <template #version="{ record }">
         <a-tag :color="loadVersionColor(item)" v-for="item in record.version" :key="item" style="margin:0 2px">{{ loadVersion(item) }}</a-tag>
      </template>
      <template #limit="{ record }">
         <a-tag :color="loadLimitColor(item)" v-for="item in record.limit" :key="item" style="margin:0 2px">{{ loadLimit(item) }}</a-tag>
      </template>
      <template #status="{ record }">
          <span>{{ record.status==1?'已启用':'待启用' }}</span>
      </template>
      <template #action="{ record }">
        <a-button type="link" @click="handleShowDrawer(record)" size="small">编辑</a-button>
        <a-button type="link" @click="handleDelete(record)" size="small">删除</a-button>
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
  import { applicationColumn, appSearchFormSchema } from './data';

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
        columns:applicationColumn,
        fetchSetting:{
          listField:'records'
        },
        dataSource:[
          {  name:'公告',desc:'重要信息发送分段锁',status:0, applicationId:'23333',version:[0], limit:[1]},
          {  name:'疫情动态',desc:'真实传递疫情信息，实时追踪最新动态',status:1,  applicationId:'777777777777',version:[1,2,3] ,limit:[0,1]},
          {  name:'公告',desc:'地方大概个方式飞洒发士大夫',status:2,  applicationId:'8888888888',version:[3,2],limit:[0] },
        ],
        formConfig: {
          labelWidth: 120,
          schemas:appSearchFormSchema,
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
      
      function loadLimit(limit){
        switch(limit){
          case 0:
            return '组织/企业/团队';
          case 1:
            return '个人';
          default:
            return '';
        }
      }
      
      function loadVersion(version){
        switch(version){
          case 0:
            return '尚未设置版本';
          case 1:
            return '个人版本';
          case 2:
            return '企业（基础版本)';
          case 3:
            return 'VIP版本';
          default:
            return '个人版本';
        }
      }
      function loadVersionColor(status){
        switch(status){
          case 0:
            return 'orange';
          default:
            return 'blue';
        }

      }
      function loadLimitColor(status){
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
        loadVersionColor,
        loadLimitColor,
        loadVersion,
        handleShowDrawer,
        hasPermission,
        handleSuccess,
        prefixCls,
        handleDelete,
        loadLimit,
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

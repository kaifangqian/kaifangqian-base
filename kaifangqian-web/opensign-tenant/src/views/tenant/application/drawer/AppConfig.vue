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
  <div class="app-config-drawer">
      <BasicDrawer v-bind="$attrs" @register="registerDrawer" :title="getTitle" @ok="handleSubmit" width="100%" :destroyOnClose="true" @close="handleClose">
        <template #title>
          <Icon icon="ant-design:left-outlined" />
          <span class="app-back" @click="handleClose">返回</span>
          <a-tag class="app-name" color="blue">{{ getTitle }}</a-tag>
        </template>
        <div class="content-body">
          <div class="static-menu">
              <a-menu
                style="width: 220px"
                mode="inline"
                :open-keys="openKeys"
                v-model:selectedKeys="selectedKeys"
                @click="onMenuClick"
              >
                <a-sub-menu key="sub-app">
                  <template #icon>
                    <Icon icon="ant-design:hdd-filled" />
                  </template>
                  <template #title>应用管理</template>
                  <a-menu-item key="app">基本信息</a-menu-item>
                  <a-menu-item key="auth">权限组管理</a-menu-item>
                  <a-menu-item key="ploy">权限策略管理</a-menu-item>
                  <a-menu-item key="role">角色管理</a-menu-item>
                </a-sub-menu>
              </a-menu>
            </div>
            <a-spin :spinning="spinning">
              <div class="app-container-wrap" >
                <Basic v-if="tabKey=='app'" :appInfo="appInfo" ref="basic"/>
                <AuthGroup v-if="tabKey=='auth'" :appId="recordId"/>
                <AuthPloy v-if="tabKey=='ploy'" :appId="recordId"/>
                <Role v-if="tabKey=='role'"  :appId="recordId"/>

              </div>
            </a-spin>
        </div>
      </BasicDrawer>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref, nextTick  } from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import Icon from '/@/components/Icon';
  import Basic from './Basic.vue';
  import AuthGroup from '/@/views/auth/authGroup.vue';
  import AuthPloy from '/@/views/auth/authPloy.vue';
  import Role from '/@/views/organize/Role.vue';
  import { getApplicationInfo } from '/@/api/tenant';
  export default defineComponent({
    name: 'AuthDrawer',
    components:{
      BasicDrawer,
      Icon,
      Basic,
      AuthGroup,
      AuthPloy,
      Role
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const spinning = ref(false);
      const getTitle = ref('');
      const recordId = ref('');
      const selectedKeys = ref(['app']);
      const openKeys = ref(['sub-app']);
      const tabKey = ref('app');
      const appInfo = ref({});
      const [registerDrawer, { setDrawerProps,closeDrawer }] = useDrawerInner(async (data) => {
        setDrawerProps({ 
          confirmLoading: false,
          cancelText:'关闭',
          closable:false,
          push:{
            distance:0
          },
          getContainer:document.body.querySelector(`.app-config-drawer`) || document.body
        });
        isUpdate.value = !!data?.isUpdate;
        getTitle.value = data?.record.appName;
        recordId.value = data?.record.id;
        if(unref(recordId)){
          getAppInfo()
        }
      });

      async function getAppInfo(){
        appInfo.value = {};
        let result = await getApplicationInfo({id:unref(recordId)});
          if(result){
            nextTick(()=>{
              appInfo.value = result;
              selectedKeys.value = ['app'];
            })
          }
      }

     
      function handleSubmit(){
        emit('success');
        closeDrawer()

      }
     
      function handleClose(){
          closeDrawer();
          tabKey.value = 'app';
          selectedKeys.value = ['app'];

        }
      function onMenuClick({ key}){
        spinning.value = true;
        tabKey.value = key;
        if(key==='app'){

          getAppInfo()
        }
        setTimeout(()=>{
          spinning.value = false;
        },500)
      }
     

      return {
        appInfo,
        handleClose,
        registerDrawer,
        handleSubmit,
        closeDrawer,
        getTitle,
        onMenuClick,
        selectedKeys,
        openKeys,
        tabKey,
        spinning,
        recordId
      }
    },
  })
</script>
<style lang="less" scoped>
.app-name{
  margin-left:15px;
}
.app-back{
  font-size: 14px;
  font-weight: 600;
}
.app-config-drawer{
  :deep(.ant-drawer){
    max-width: 100vw;
    .scrollbar{
      padding:0;
    }
    .ant-drawer-header{
      cursor: pointer!important;
    }
    .scrollbar__wrap{
      padding:0!important;
    }
    .static-menu{
      .ant-menu-submenu-title{
        color: #000;
      }
      .ant-menu{
        width: 220px;
        height: 100vh;
        border-right: 1px solid #ddd;
        background: #f6f6f6;
      }
      .ant-menu-item-only-child{
        font-size: 12px;
      }
    }
    .content-body{
      display: flex;
      .ant-spin-nested-loading{
        flex:1;
      }
    }
  }
  .app-container-wrap{
    // padding:20px 30px;
    padding:0;
    width:100%;
  }
  :deep(.ant-spin-nested-loading){
    flex:1;
  }
}
 
</style>

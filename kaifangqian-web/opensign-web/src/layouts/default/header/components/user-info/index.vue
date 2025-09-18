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
    width="100%"
    @ok="handleSubmit"
    >
    <template #title>
      <div class="drawer-header">
        <div @click="handleBack" class="back-area">
          <Icon icon="ant-design:left-outlined" :size="20"/>
          <span class="drawer-btn">返回</span>
        </div>
        <span class="account-title">账号信息</span>
      </div>
    </template>
    <UserCenter />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent,computed} from 'vue';
  import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
  import UserCenter from '/@/views/sys/user/UserInfo.vue';
  import Icon from '/@/components/Icon';
  
 
  export default defineComponent({
    name: 'UserInfo',
    components: {
      BasicDrawer,
      UserCenter,
      Icon
    },
    setup(_, { emit }) {
      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        setDrawerProps({ 
          confirmLoading: false,
          closable:false
        });
      
      });
      const getTitle = computed(() => ('账号信息'));
      function handleSubmit(){
        closeDrawer();
      }
      function handleBack(){
        closeDrawer();
      }
    
      return {
        registerDrawer,
        getTitle,
        handleSubmit,
        handleBack

      }
    }
  });
</script>
<style lang="less" scoped>
.drawer-header{
  display: flex;
  align-items: center;
  cursor: pointer;
  .drawer-btn{
    font-size: 14px;
    margin-right:10px;
    font-weight: 600;
  }
  .account-title{
    font-size: 14px;
  }
  .back-area{
    display: flex;
    align-items: center;
  }
}
</style>
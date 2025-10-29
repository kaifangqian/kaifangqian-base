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
  <div class="user-list-container">
    <div class="user-content">
      <div class="user-menu">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="inline"
          :open-keys="openKeys"
          @click="handleMenuClick"
        >
          <a-menu-item key="1"
            ><template #icon> <SvgIcon name="account" size="22" /></template>我的账号</a-menu-item
          >
          <a-menu-item key="2"
            ><template #icon> <SvgIcon name="my-seal" size="22" /></template
            >{{ tenantInfo.tenantType == '1' ? '我的印章' : '我的签名' }}</a-menu-item
          >
          <a-menu-item key="4" v-if="tenantInfo.tenantType == '2'"
            ><template #icon> <SvgIcon name="free-sign-auth" size="22" /></template
            >快捷签授权</a-menu-item
          >
          <a-menu-item key="5" v-if="tenantInfo.tenantType == '2'"
            ><template #icon> <SvgIcon name="silent-sign-auth" size="22" /></template
            >静默签授权</a-menu-item
          >
          <a-menu-item key="3" v-if="tenantInfo.tenantType == '2'"
            ><template #icon> <SvgIcon name="signature-auth" size="22" /></template
            >签名授权</a-menu-item
          >
        </a-menu>
      </div>
      <div class="user-list">
        <UserInfo v-if="activeMenuKey == '1'"></UserInfo>
        <Card
          size="small"
          :title="tenantInfo.tenantType == '1' && activeMenuKey == '2' ? '我的印章' : '我的签名'"
          v-if="activeMenuKey == '2'"
          class="seal-signature"
        >
          <MySeal v-if="tenantInfo.tenantType == '1' && activeMenuKey == '2'"></MySeal>
          <Signature v-if="tenantInfo.tenantType == '2' && activeMenuKey == '2'"></Signature>
        </Card>
        <FreeSignAuth v-if="tenantInfo.tenantType == '2' && activeMenuKey == '4'"></FreeSignAuth>
        <SilentSignAuth
          v-if="tenantInfo.tenantType == '2' && activeMenuKey == '5'"
        ></SilentSignAuth>
        <MySignatureAuth
          v-if="tenantInfo.tenantType == '2' && activeMenuKey == '3'"
        ></MySignatureAuth>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { ref, unref, toRefs, defineComponent, reactive, onMounted } from 'vue';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { useUserStore } from '/@/store/modules/user';
  import UserInfo from './UserInfo.vue';
  import MySeal from '/@/views/signature/seal/mySeal.vue';
  import Signature from '/@/views/signature/sign/index.vue';
  import MySignatureAuth from '/@/views/signature/sign/MySignatureAuth.vue';
  import FreeSignAuth from '/@/views/signature/authorisation/modal/FreeSignAuth.vue';
  import SilentSignAuth from '/@/views/signature/authorisation/modal/SilentSignAuth.vue';
  import { companyAuthApi, personAuthApi, companyAuthUpdateApi } from '/@/api/auth/userAuth';
  import { useRouter } from 'vue-router';

  import { Card } from 'ant-design-vue';

  export default defineComponent({
    name: 'UserCenter',
    components: {
      Icon,
      UserInfo,
      SvgIcon,
      MySeal,
      Signature,
      Card,
      MySignatureAuth,
      FreeSignAuth,
      SilentSignAuth,
    },
    setup() {
      const popVisible = ref(false);
      const allRef = ref<HTMLDivElement | null>(null);
      const router = useRouter();
      const state = reactive({
        rootSubmenuKeys: ['sub1', 'sub2', 'sub4'],
        openKeys: ['sub2'],
        selectedKeys: ['1'],
        activeMenuKey: '1',
      });

      const userStore = useUserStore();
      const tenantInfo = userStore.getTenantInfo;

      onMounted(() => {
        const index = router.currentRoute.value.query.index;
        if (index) {
          state.selectedKeys = [index as string];
          handleMenuClick({ key: index as string });
        }
      });
      //菜单点击
      function handleMenuClick(item: { key: string }) {
        state.activeMenuKey = item.key;
      }

      //筛选条件面板
      function handleFilter() {}

      function handleBeforeVisibleChange(v) {
        console.log(v, '----');
      }

      return {
        ...toRefs(state),
        handleMenuClick,
        tenantInfo,
        allRef,
        popVisible,
        handleFilter,
        handleBeforeVisibleChange,
      };
    },
  });
</script>

<style lang="less" scoped>
    .user-list-container{
      // @media (min-width: 0px) and (max-width: 1280px){
      //   width: 1000px;
      // }
      // @media (min-width: 1280px) and (max-width: 1536px){
      //   width: 1200px;
      // }
      // @media (min-width: 1536px){
      //   width: 1506px;
      // }
    }
  .user-content {
    // width: 1506px;
    @media (min-width: 0px) and (max-width: 1280px){
      width: 1000px;
    }
    @media (min-width: 1280px) and (max-width: 1536px){
      width: 1200px;
    }
    @media (min-width: 1536px){
      width: 1506px;
    }

    margin: 0 auto;
    display: flex;
    .user-menu {
      background: #f4f4f4;
      text-align: center;
      padding-top: 20px;
      box-sizing: border-box;
      border-radius: 5px;
      height: 90vh;
      width: 220px;
    }
    :deep(.ant-menu) {
      background: #f4f4f4;
      margin-top: 20px;

      li {
        font-size: 13px;
        height: 30px;
        margin: 0;
        line-height: 30px;
        margin-bottom: 2px;
        .ant-menu-title-content {
          display: flex;
          align-items: center;
          line-height: 28px;
          .sub-menu-name {
            margin-left: 10px;
            display: inline-block;
          }
        }
      }
      .ant-menu-submenu,
      .ant-menu-submenu-inline {
        margin-bottom: 15px;
        color: #1e1e1e;
        font-weight: 700;
      }
      &:not(.ant-menu-horizontal) .ant-menu-item-selected {
        border-right: none;
        background: #e9e9e9;
        color: #1e1e1e;
        font-weight: 700;
      }
      &.ant-menu-light .ant-menu-item:hover {
        background: #e9e9e9;
        color: #1e1e1e;
      }
      &.ant-menu-light .ant-menu-submenu-title:hover {
        background: #f4f4f4;
        color: #1e1e1e;
        cursor: auto;
      }
    }
    .user-list {
      // width: 1280px;
      flex: 1;
      // margin-left:10px;
      .seal-signature {
        // margin:0 10px;
        box-shadow: 5px 5px 30px #0000000e;
      }
      :deep(.ant-card-head) {
        min-height: 40px;
        background: #fff;
        color: #333;
        font-weight: 550;
        font-size: 16px;
      }
    }
  }
  .sign-filter-area {
    text-align: right;
    margin-bottom: 15px;
    :deep(.ant-input-affix-wrapper) {
      height: 32px;
    }
  }
  .advanced-filter {
    cursor: pointer;
  }
  :deep(.ant-pagination) {
    width: 100%;
  }
</style>

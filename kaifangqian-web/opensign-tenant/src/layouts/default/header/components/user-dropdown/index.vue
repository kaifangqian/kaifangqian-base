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
  <Dropdown
    placement="bottomLeft"
    :overlayClassName="`${prefixCls}-dropdown-overlay`"
    style="top: 63px"
    :trigger="['click']"
  >
    <span :class="[prefixCls, `${prefixCls}--${theme}`]" class="flex">
      <img
        :class="`${prefixCls}__header`"
        src="../../../../../assets/images/profile-picture-icon.png"
      />
      <!-- <img :class="`${prefixCls}__header`" :src="avatarImg" v-if="avatarImg"/> -->
      <!-- <span :class="`${prefixCls}__header__no__avatar`" v-if="!avatarImg"></span> -->
      <span :class="`${prefixCls}__info hidden md:block`">
        <span :class="`${prefixCls}__name  `" class="truncate">
          {{ getUserInfo.realname }}
        </span>
      </span>
    </span>

    <template #overlay>
      <Menu @click="handleMenuClick">
        <!-- <MenuItem
          key="account"
          text="账号信息"
          icon="ant-design:user-outlined"
        /> -->
        <!-- <MenuDivider  />
        <MenuItem
          key="dept"
          text="切换身份"
          icon="ant-design:cluster-outlined"
        /> -->
        <!-- <MenuDivider /> -->
        <!-- <MenuItem
          key="theme"
          text="主题偏好设置"
          icon="ant-design:sliders-outlined"
        >
          <a-radio-group  v-model:value="defaultTheme" button-style="solid" size="small" style="margin-left:10px" @change="handleChangeTheme">
            <a-radio-button value="dark">深色</a-radio-button>
            <a-radio-button value="light">浅色</a-radio-button>
          </a-radio-group>
        </MenuItem> -->
        <!-- <MenuDivider /> -->
        <MenuItem key="logout" text="退出系统" icon="ion:power-outline" />
      </Menu>
    </template>
  </Dropdown>
  <LockAction @register="register" />
  <UserInfo @register="registerDrawer" />
  <LoginOut @register="registerLogOut" />
</template>
<script lang="ts">
  // components
  import { Dropdown, Menu } from 'ant-design-vue';

  import { defineComponent, ref, computed } from 'vue';

  import { useUserStore } from '/@/store/modules/user';
  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { useDrawer } from '/@/components/Drawer';
  import { useGo } from '/@/hooks/web/usePage';
  import type { MenuInfo } from 'ant-design-vue/lib/menu/src/interface';

  import headerImg from '/@/assets/images/logo.png';

  import { propTypes } from '/@/utils/propTypes';

  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  import { getImgBase64 } from '/@/api/sys/upload';

  import { baseHandler } from '/@/layouts/default/setting/handler';

  import UserInfo from '../user-info/index.vue';

  import LoginOut from '../LoginOut.vue';

  type MenuEvent = 'logout' | 'account' | 'dept' | 'theme';

  export default defineComponent({
    name: 'UserDropdown',
    components: {
      Dropdown,
      Menu,
      MenuItem: createAsyncComponent(() => import('./DropMenuItem.vue')),
      MenuDivider: Menu.Divider,
      LockAction: createAsyncComponent(() => import('../lock/LockModal.vue')),
      UserInfo,
      LoginOut,
    },
    props: {
      theme: propTypes.oneOf(['dark', 'light']),
    },
    setup() {
      const { prefixCls } = useDesign('header-user-dropdown');
      const { getShowDoc, getUseLockPage } = useHeaderSetting();
      const { getMenuTheme } = useMenuSetting();
      const defaultTheme = ref(getMenuTheme);
      const userStore = useUserStore();
      const go = useGo();
      const avatarImg = ref('');
      console.log(getMenuTheme, '33');
      const getUserInfo = computed(() => {
        const { realname, avatar, desc } = userStore.getUserInfo || {};
        getAvatarImg(avatar);
        return { realname, avatar: avatar || headerImg, desc };
      });
      async function getAvatarImg(id) {
        if (!id) return;
        let result = await getImgBase64({ imgId: id });
        avatarImg.value = result.image;
      }
      const [register, { openModal }] = useModal();
      const [registerLogOut, { openModal: openLogOutModal }] = useModal();
      const [registerDrawer, { openDrawer, closeDrawer }] = useDrawer();
      function handleLoginOut() {
        userStore.confirmLoginOut();
      }
      function pageToAccount() {
        // go('/user/center');
        openDrawer(true, {
          isUpdate: false,
        });
      }
      function changeDept() {
        openModal(true, {
          isUpdate: true,
        });
      }
      function changeTheme() {
        // console.log(defaultTheme.value)
        // baseHandler('CHANGE_THEME',)
      }
      function handleChangeTheme(e) {
        let theme = e.target.value;
        let color = theme === 'dark' ? '#002b45' : '#fff';
        console.log(color);
        baseHandler(11, color);
      }
      function handleMenuClick(e: MenuInfo) {
        switch (e.key as MenuEvent) {
          case 'logout':
            handleLoginOut();
            break;
          case 'account':
            pageToAccount();
            break;
          case 'dept':
            changeDept();
          case 'theme':
            changeTheme();
            break;
        }
      }
      function handleSuccess() {}

      return {
        prefixCls,
        defaultTheme,
        getUserInfo,
        handleMenuClick,
        getShowDoc,
        register,
        getUseLockPage,
        avatarImg,
        handleSuccess,
        getMenuTheme,
        handleChangeTheme,
        registerDrawer,
        registerLogOut,
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-header-user-dropdown';

  .@{prefix-cls} {
    height: @header-height;
    padding: 0 0 0 10px;
    // padding-right: 10px;
    overflow: hidden;
    font-size: 12px;
    cursor: pointer;
    align-items: center;

    img {
      width: 24px;
      height: 24px;
      margin-right: 12px;
    }

    &__header {
      // border-radius: 50%;
      width: 32px;
    }
    &__header__no__avatar {
      border-radius: 50%;
      width: 24px;
      height: 24px;
      margin-right: 12px;
      display: inline-block;
      background-color: @primary-color;
    }

    &__name {
      font-size: 14px;
    }

    &--dark {
      &:hover {
        background-color: @header-dark-bg-hover-color;
      }
    }

    &--light {
      &:hover {
        background-color: @header-light-bg-hover-color;
      }

      .@{prefix-cls}__name {
        color: @text-color-base;
      }

      .@{prefix-cls}__desc {
        color: @header-light-desc-color;
      }
    }

    &-dropdown-overlay {
      .ant-dropdown-menu-item {
        min-width: 120px;
      }
    }
  }
</style>

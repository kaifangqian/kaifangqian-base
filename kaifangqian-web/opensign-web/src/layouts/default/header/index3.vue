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
  <Header :class="getHeaderClass">
    <!-- left start -->
    <div :class="`${prefixCls}-left`">
      <!-- AppLogo -->
      <AppLogo
        v-if="getShowHeaderLogo"
        :class="`${prefixCls}-logo`"
        :theme="getHeaderTheme" 
        :style="getLogoWidth"
        :showIcon="false"
      />
      
    </div>
    <!-- left end -->

    <!-- action  -->
    <div :class="`${prefixCls}-action`">
       
      <ChangeAccount v-if="isLogin" />

      <ErrorAction v-if="getUseErrorHandle" :class="`${prefixCls}-action__item error-action`" />

     
      <FullScreen v-if="getShowFullScreen" :class="`${prefixCls}-action__item fullscreen-item`" />
       
      <UserDropDown v-if="isLogin"/>
      <!-- <SensitiveModal /> -->

      <!------最大登录保持时间到期，重新登录-------->
      <!-- <KeepLoginAlive /> -->
      <!-- 页签切换 -->
      <!-- <TabSwitch /> -->

      <SettingDrawer v-if="getShowSetting" :class="`${prefixCls}-action__item`" />
    </div>
  </Header>
</template>
<script lang="ts">
  import { defineComponent, unref, computed, ref } from 'vue';

  import { propTypes } from '/@/utils/propTypes';

  import { Layout } from 'ant-design-vue';
  import { AppLogo } from '/@/components/Application';
  import LayoutMenu from '../menu/index.vue';
  import LayoutTrigger from '../trigger/index.vue';
  // import SensitiveModal from '/@/layouts/sensitive/SensitiveModal.vue';
  import ChangeAccount from './components/change-account/index.vue';
  import TabSwitch from './components/TabSwitch.vue';
  import SignEnter from './components/sign/index.vue';


  import { AppSearch } from '/@/components/Application';

  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';
  import { useUserStore } from '/@/store/modules/user';

  import { MenuModeEnum, MenuSplitTyeEnum } from '/@/enums/menuEnum';
  import { SettingButtonPositionEnum } from '/@/enums/appEnum';

  import { UserDropDown, LayoutBreadcrumb, FullScreen, Notify, ErrorAction, KeepLoginAlive } from './components';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Icon,SvgIcon } from '/@/components/Icon';

  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  export default defineComponent({
    name: 'Header3',
    components: {
      Header: Layout.Header,
      AppLogo,
      LayoutTrigger,
      LayoutBreadcrumb,
      LayoutMenu,
      UserDropDown,
      FullScreen,
      SvgIcon,
      Notify,
      AppSearch,
      ErrorAction,
      // SensitiveModal,
      // KeepLoginAlive,
      ChangeAccount,
      // TabSwitch,
      Icon,
      SignEnter,
      SettingDrawer: createAsyncComponent(() => import('/@/layouts/default/setting/index.vue'), {
        loading: false,
      }),
    },
    props: {
      fixed: propTypes.bool,
    },
    setup(props) {
      const { prefixCls } = useDesign('layout-header');
      const {
        getShowTopMenu,
        getShowHeaderTrigger,
        getSplit,
        getIsMixMode,
        getMenuWidth,
        getIsMixSidebar,
      } = useMenuSetting();
      const { getUseErrorHandle, getShowSettingButton, getSettingButtonPosition } =  useRootSetting();

      const {
        getHeaderTheme,
        getShowFullScreen,
        getShowNotice,
        getShowContent,
        getShowBread,
        getShowHeaderLogo,
        getShowHeader,
        getShowSearch,
      } = useHeaderSetting();

      const { getIsMobile } = useAppInject();

      const isLogin = ref(false);

      const userStore = useUserStore();
      // console.log('userStore----',userStore);
      // console.log('userStore----',userStore.getToken);
      // console.log('userStore----',userStore.getKeepLoginAlive);
      
      // console.log('userStore----',userStore.getUserInfo);
      // console.log('userStore----',userStore.getUserInfo.loginDepartId);
      if(userStore.getToken) {
        isLogin.value = true;
      }
      const tenantInfo = userStore.getTenantInfo;

      const getHeaderClass = computed(() => {
        const theme = unref(getHeaderTheme);  
        console.log(theme)

        return [
          prefixCls,
          {
            [`${prefixCls}--fixed`]: props.fixed,
            [`${prefixCls}--mobile`]: unref(getIsMobile),
            [`${prefixCls}--${theme}`]: theme,
          },
        ];
      });

      const getShowSetting = computed(() => {
        if (!unref(getShowSettingButton)) {
          return false;
        }
        const settingButtonPosition = unref(getSettingButtonPosition);

        if (settingButtonPosition === SettingButtonPositionEnum.AUTO) {
          return unref(getShowHeader);
        }
        return settingButtonPosition === SettingButtonPositionEnum.HEADER;
      });

      const getLogoWidth = computed(() => {
        if (!unref(getIsMixMode) || unref(getIsMobile)) {
          return {};
        }
        const width = unref(getMenuWidth) < 180 ? 180 : unref(getMenuWidth);
        return { width: `${width}px` };
      });
      console.log(getLogoWidth,'logo-width')

      const getSplitType = computed(() => {
        return unref(getSplit) ? MenuSplitTyeEnum.TOP : MenuSplitTyeEnum.NONE;
      });

      const getMenuMode = computed(() => {
        return unref(getSplit) ? MenuModeEnum.HORIZONTAL : null;
      });
      function handleDoc(){
        window.open('https://docs.kaifangqian.com/')
      }

      return {
        handleDoc,
        prefixCls,
        getHeaderClass,
        getShowHeaderLogo,
        getHeaderTheme,
        getShowHeaderTrigger,
        getIsMobile,
        getShowBread,
        tenantInfo,
        getShowContent,
        getSplitType,
        getSplit,
        getMenuMode,
        getShowTopMenu,
        getShowFullScreen,
        getShowNotice,
        getUseErrorHandle,
        getLogoWidth,
        getIsMixSidebar,
        getShowSettingButton,
        getShowSetting,
        getShowSearch,
        isLogin,

      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
</style>

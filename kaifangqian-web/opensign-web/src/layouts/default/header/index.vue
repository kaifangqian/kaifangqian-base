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
  <Header :class="getHeaderClass" style="padding: 0 !important">
    <!-- left start -->
    <div :class="`${prefixCls}-left`">
      <!-- logo -->
      <AppLogo
        v-if="getShowHeaderLogo"
        :class="`${prefixCls}-logo`"
        :theme="getHeaderTheme"
        :style="getLogoWidth"
      />
      <!-- 暂时去掉左侧logo显示判断解决在工作台不显示问题 -->
      <!-- <AppLogo
        :class="`${prefixCls}-logo`"
        :theme="getHeaderTheme" 
        :style="getLogoWidth"
      /> -->
      <!-- <LayoutTrigger
        v-if="
          (getShowContent && getShowHeaderTrigger && !getSplit && !getIsMixSidebar) || getIsMobile
        "
        :theme="getHeaderTheme"
        :sider="false"
      /> -->
      <!-- <LayoutBreadcrumb v-if="getShowContent && getShowBread" :theme="getHeaderTheme" /> -->
    </div>
    <!-- left end -->

    <!-- menu start -->
    <div :class="`${prefixCls}-menu`" v-if="getShowTopMenu && !getIsMobile">
      <LayoutMenu
        :isHorizontal="true"
        :theme="getHeaderTheme"
        :splitType="getSplitType"
        :menuMode="getMenuMode"
      />
    </div>
    <!-- menu-end -->




    <!-- action  -->
    <div :class="`${prefixCls}-action`">
      <ChangeAccount />
      <!-- <SignEnter v-if="tenantInfo.tenantType == 1 && isShowSignEnter" /> -->
      
      <!-- <AppSearch :class="`${prefixCls}-action__item `" v-if="getShowSearch" /> -->
      <div class="tenant-btn">
        <a-button
          v-if="tenantApp"
          type="info"
          style="width: 100px; color: #1890ff; background-color: #0098e718; border: 0"
          @click="jumpApp(tenantApp)"
          >企业管理
        </a-button >
      </div>

      <div class="manage-btn">
        <a-button
          v-if="manageApp"
          type="info"
          style="width: 100px; color: #1890ff; background-color: #0098e718; border: 0"
          @click="jumpApp(manageApp)"
          >运营管理
        </a-button >
      </div>


      <div class="septal-line" v-if="tenantApp">
        <p>|</p>
      </div>

      <ErrorAction v-if="getUseErrorHandle" :class="`${prefixCls}-action__item error-action`" />

      

      <FullScreen v-if="getShowFullScreen" :class="`${prefixCls}-action__item fullscreen-item`" />

     
      <p class="help" @click="handleDoc" >帮助中心</p>
      <div class="septal-line">
        <p>|</p>
      </div>
     
      <!-- <a-tooltip placement="bottom">
        <template #title>
          <span>帮助中心</span>
        </template>
        <SvgIcon name="doc-help" style="margin-right: 20px; cursor: pointer" @click="handleDoc" />
      </a-tooltip> -->
      <Notify v-if="getShowNotice" :class="`${prefixCls}-action__item notify-item`" />
      <div class="septal-line">
        <p>|</p>
      </div>
      <UserDropDown />
      <!-- <SensitiveModal /> -->

      <!------最大登录保持时间到期，重新登录-------->
      <KeepLoginAlive />
      <!-- 页签切换 -->
      <TabSwitch />

      <!-- <SettingDrawer :class="`${prefixCls}-action__item`" /> -->
    </div>
  </Header>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed } from 'vue';

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
  import { useAppStore } from '/@/store/modules/app';

  import { MenuModeEnum, MenuSplitTyeEnum } from '/@/enums/menuEnum';
  import { SettingButtonPositionEnum } from '/@/enums/appEnum';

  import {
    UserDropDown,
    LayoutBreadcrumb,
    FullScreen,
    Notify,
    ErrorAction,
    KeepLoginAlive,
  } from './components';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { getAppTokenByAuthToken } from '/@/api/sys/user';
  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
  import { useRouter } from 'vue-router';
  export default defineComponent({
    name: 'LayoutHeader',
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
      KeepLoginAlive,
      ChangeAccount,
      TabSwitch,
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
      const isShowSignEnter = computed(() => {
        const router = useRouter();
        // console.log("header:",router.currentRoute.value);
        return router.currentRoute.value.path !== '/business';
      });

      const {
        getShowTopMenu,
        getShowHeaderTrigger,
        getSplit,
        getIsMixMode,
        getMenuWidth,
        getIsMixSidebar,
      } = useMenuSetting();

      const { getUseErrorHandle, getShowSettingButton, getSettingButtonPosition } =
        useRootSetting();
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

      console.log('getSplit', getSplit, 'getShowHeaderLogo', getShowHeaderLogo);
      const { getIsMobile } = useAppInject();

      const userStore = useUserStore();
      const appStore = useAppStore();

      const tenantInfo = userStore.getTenantInfo;

      const getHeaderClass = computed(() => {
        const theme = unref(getHeaderTheme);
        console.log(theme);

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
        console.log(getShowSettingButton, '系统配置');
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
      console.log(getLogoWidth, 'logo-width');

      const getSplitType = computed(() => {
        return unref(getSplit) ? MenuSplitTyeEnum.TOP : MenuSplitTyeEnum.NONE;
      });

      const getMenuMode = computed(() => {
        return unref(getSplit) ? MenuModeEnum.HORIZONTAL : null;
      });
      const tenantApp = computed(() => {
        const apps = appStore.getApps;
        if (apps.length > 0) {
          return apps.find((item) => item.appCode == 'app_00005');
        } else {
          return null;
        }
      });

      const manageApp = computed(() => {
        const apps = appStore.getApps;
        if (apps.length > 0) {
          return apps.find((item) => item.appCode == 'paas_manage_001');
        } else {
          return null;
        }
      });

      function handleDoc() {
        window.open('https://docs.kaifangqian.com/');
      }
      async function jumpApp(row) {
        const userInfo: any = userStore.userInfo;
        let params = {
          tenantId: userInfo.loginTenantId,
          departId: userInfo.loginDepartId,
        };
        let newToken = await getAppTokenByAuthToken(params);
        if (newToken) {
          let appInfo = {
            token: newToken,
            appCode: row.appCode,
            appId: row.id,
            //  departId:userInfo.loginDepartId,
            redirect: row.appAddress + '/#' + row.appFront.split('#')[1],
          };
          let paramsString = new URLSearchParams(appInfo).toString();
          window.open(row.appAddress + row.appFront + '?' + paramsString);
        }
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
        isShowSignEnter,
        tenantApp,
        manageApp,
        jumpApp,
      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
  .tenant-btn {
    padding: 0 10px;
  }
  .help {
    margin-top: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    cursor: pointer;
    color: #595959;
    font-weight: 400;
  }
  .septal-line {
    margin:10px 10px;
    margin-top: 20px;
    color: #e9e9e9;
  }
</style>

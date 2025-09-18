<template>
  <Header :class="getHeaderClass">
    <!-- left start -->
    <div :class="`${prefixCls}-left`">
      <!-- logo -->
      <!-- <AppLogo
        v-if="getShowHeaderLogo"
        :class="`${prefixCls}-logo`"
        :theme="getHeaderTheme" 
        :style="getLogoWidth"
      /> -->
      <!-- 暂时去掉左侧logo显示判断解决在工作台不显示问题 -->
      <AppLogo
        :class="`${prefixCls}-logo`"
        :theme="getHeaderTheme" 
        :style="getLogoWidth"
      />
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
      <!-- <AppSearch :class="`${prefixCls}-action__item `" v-if="getShowSearch" /> -->
      <!-- <ChangeAccount /> -->

      <a-button type="primary" class="back-home-btn" @click="handleBackHome">回到首页</a-button>


      <ErrorAction v-if="getUseErrorHandle" :class="`${prefixCls}-action__item error-action`" />

      <Notify v-if="getShowNotice" :class="`${prefixCls}-action__item notify-item`" />

      <!-- <FullScreen v-if="getShowFullScreen" :class="`${prefixCls}-action__item fullscreen-item`" /> -->

      <UserDropDown  />
      <SensitiveModal />

      <!------最大登录保持时间到期，重新登录-------->
      <KeepLoginAlive />
      <TabSwitch />

      <SettingDrawer v-if="getShowSetting" :class="`${prefixCls}-action__item`" />
    </div>
  </Header>
</template>
<script lang="ts">
  import { defineComponent, unref, computed } from 'vue';

  import { propTypes } from '/@/utils/propTypes';

  import { Layout } from 'ant-design-vue';
  import { AppLogo } from '/@/components/Application';
  import LayoutMenu from '../menu/index.vue';
  import LayoutTrigger from '../trigger/index.vue';
  import SensitiveModal from '/@/layouts/sensitive/SensitiveModal.vue';
  import ChangeAccount from './components/change-account/index.vue';
  import TabSwitch from './components/TabSwitch.vue';


  import { AppSearch } from '/@/components/Application';

  import { useHeaderSetting } from '/@/hooks/setting/useHeaderSetting';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useRootSetting } from '/@/hooks/setting/useRootSetting';

  import { MenuModeEnum, MenuSplitTyeEnum } from '/@/enums/menuEnum';
  import { SettingButtonPositionEnum } from '/@/enums/appEnum';

  import { UserDropDown, LayoutBreadcrumb, FullScreen, Notify, ErrorAction, KeepLoginAlive } from './components';
  import { useAppInject } from '/@/hooks/web/useAppInject';
  import { useDesign } from '/@/hooks/web/useDesign';

  import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';

  export default defineComponent({
    name: 'LayoutHeader',
    components: {
      Header: Layout.Header,
      AppLogo,
      TabSwitch,
      LayoutTrigger,
      LayoutBreadcrumb,
      LayoutMenu,
      UserDropDown,
      FullScreen,
      Notify,
      AppSearch,
      ErrorAction,
      SensitiveModal,
      KeepLoginAlive,
      ChangeAccount,
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
        console.log(getShowSettingButton,'系统配置')
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

      const getSplitType = computed(() => {
        return unref(getSplit) ? MenuSplitTyeEnum.TOP : MenuSplitTyeEnum.NONE;
      });

      const getMenuMode = computed(() => {
        return unref(getSplit) ? MenuModeEnum.HORIZONTAL : null;
      });
      function handleBackHome(){
        let appInfo = window.appInfo;
        window.open(appInfo.sign_app_info.url + '/#/dashboard/workbench','_self')
        // openOrFocusTab(appInfo.sign_app_info.url + '/#/dashboard/workbench','开放签')
      }

      function openOrFocusTab(url, tabName) {
        // 尝试在已有标签页中打开新的页面
        const existingTab = window.open(url, tabName);

        // 如果标签页已经存在，则焦点聚焦到已有标签页
        if (existingTab) {
          existingTab.focus();
        }
      }

      return {
        prefixCls,
        handleBackHome,
        getHeaderClass,
        getShowHeaderLogo,
        getHeaderTheme,
        getShowHeaderTrigger,
        getIsMobile,
        getShowBread,
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
      };
    },
  });
</script>
<style lang="less">
  @import './index.less';
  .back-home-btn{
    background: linear-gradient(90deg, #1681b1 50%, #147e93 94%);
    border-color: linear-gradient(90deg, #1681b1 50%, #147e93 94%);
  }
</style>

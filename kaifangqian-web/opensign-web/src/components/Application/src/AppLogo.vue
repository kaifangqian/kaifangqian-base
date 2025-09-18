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

<!--
 * @Author: Resrun
 * @Description: logo component
-->
<template>
  <!-- <div class="anticon" :class="getAppLogoClass" @click="goHome"> -->
  <div class="anticon" :class="getAppLogoClass" ref="menuopenRef" id="menu-open">
    <!-- <img v-if="theme==='dark'" src="../../../assets/images/logo-light.png" style="height:auto;width:70px"/>
    <img v-if="theme==='light'" src="../../../assets/images/logo.png" style="height:auto"/>
    <div class="ml-2 truncate md:opacity-100" :class="getTitleClass" v-show="showTitle">
      {{ title }}
    </div> -->
    <!-- <Icon  icon="ant-design:appstore-outlined" class="menu-open" size="28"></Icon> -->
    <!-- <SvgIcon name="more-app" size="36"  @click="handleAppSide" /> -->
    <!-- @mouseenter="controlMouseover" -->
    <SvgIcon
      name="more-app"
      size="36"
      @click="controlMouseover"
      @mouseleave="mouseoverMouseout"
      v-show="showIcon"
    />

    <img
      :src="logoBase64"
      style="height: auto; margin: 0 30px"
      @click="showAppVersion"
      v-if="logoBase64"
    />
  </div>
</template>
<script lang="ts" setup>
  import { computed, unref, ref, h } from 'vue';
  import { useGlobSetting } from '/@/hooks/setting';
  import { useGo } from '/@/hooks/web/usePage';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { PageEnum } from '/@/enums/pageEnum';
  import { useUserStore } from '/@/store/modules/user';
  import { useAppStore } from '/@/store/modules/app';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { getWhiteLogo, getOtherLogo } from '/@/api/sys/user';
  import defaultLogo from '/@/assets/images/logo-sign.png';
  import { Modal } from 'ant-design-vue';
  import { getAppEnvConfig } from '/@/utils/env';

  const props = defineProps({
    /**
     * The theme of the current parent component
     */
    theme: { type: String, validator: (v: string) => ['light', 'dark'].includes(v) },
    /**
     * Whether to show title
     */
    showTitle: { type: Boolean, default: true },
    /**
     * The title is also displayed when the menu is collapsed
     */
    alwaysShowTitle: { type: Boolean },

    showIcon: { type: Boolean, default: true },
  });

  // const logoBase64 = ref(defaultLogo);
  const logoBase64 = ref('');

  async function getLogo() {
    getOtherLogo({}).then((res) => {
      logoBase64.value = res.image;
    });
  }
  getLogo();

  const { prefixCls } = useDesign('app-logo');
  const { getCollapsedShowTitle, getCollapsed } = useMenuSetting();
  const userStore = useUserStore();
  const appStore = useAppStore();
  const { title } = useGlobSetting();
  const go = useGo();

  const versionCount = ref(0);

  const getAppLogoClass = computed(() => [
    prefixCls,
    props.theme,
    { 'collapsed-show-title': unref(getCollapsedShowTitle) },
  ]);

  const getTitleClass = computed(() => [
    `${prefixCls}__title`,
    {
      'xs:opacity-0': !props.alwaysShowTitle,
    },
  ]);
  const controlMouseover = () => {
    console.log('mouseoverMouseout 移入');
    handleAppSide();
  };
  const mouseoverMouseout = () => {
    console.log('mouseoverMouseout 移出');
  };

  function handleAppSide() {
    const showAppSide = appStore.getMenuSetting.collapsed;
    console.log(!showAppSide, '设置值');
    appStore.setProjectConfig({
      menuSetting: {
        collapsed: !showAppSide,
        clickMenuSwitch: true,
      },
    });
  }

  function goHome() {
    go(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
  }

  function showAppVersion() {
    versionCount.value++;
    if (versionCount.value == 3) {
      let { VITE_GLOB_APP_VERSION } = getAppEnvConfig();
      Modal.info({
        icon: h('Icon', { icon: 'ant-design:rocket-outlined' }),
        content: h('div', {}, [
          h('p', { style: { color: '#389e0d' } }, `版本号：${VITE_GLOB_APP_VERSION}`),
        ]),
      });
      versionCount.value = 0;
    }
  }
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-app-logo';

  .@{prefix-cls} {
    display: flex;
    align-items: center;
    // padding-left: 50px;
    cursor: pointer;
    justify-content: center;
    transition: all 0.2s ease;

    &.light {
      border-bottom: 1px solid #eee;
    }
    &.dark {
      // border-bottom: 1px solid #383838;
    }

    &.collapsed-show-title {
      padding-left: 20px;
    }

    &.light &__title {
      color: @primary-color;
    }

    &.dark &__title {
      color: @white;
    }

    &__title {
      font-size: 16px;
      font-weight: 700;
      transition: all 0.5s;
      line-height: normal;
    }
  }
</style>

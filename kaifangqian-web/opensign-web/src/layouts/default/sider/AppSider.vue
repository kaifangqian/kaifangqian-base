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
  <div
    class="app-side"
    ref="appSideRef"
    :style="getAppSideDomStyle"
    @mouseleave="mouseoverMouseout"
  >
    <div class="app-side-container">
      <div class="app-side-header">
        <SvgIcon name="more-app" size="36" />
        <div class="app-side-title">
          {{ userInfo.loginTenantName }}
        </div>
      </div>
      <div class="application-list">
        <div
          :class="[item.selectFlag ? '' : 'app-disabled']"
          @click="handleEnterApp(item)"
          class="application-item"
          v-for="(item, index) in apps"
          :key="index"
          v-if="apps && apps.length"
        >
          <div class="app-icon">
            <img :src="(item.appIcon as string)" alt="" v-if="item.appIcon" />
            <img src="../../../assets/images/app-icon.png" alt="" v-else />
          </div>
          <span class="app-name">{{ item.appName }}</span>
        </div>
        <div v-else>
          <p class="no-data">
            <img src="~@/assets/images/no-data.png" alt="" />
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import type { CSSProperties } from 'vue';
  import { ref, defineComponent, computed, onMounted, onBeforeUnmount } from 'vue';
  import { useMenuSetting } from '/@/hooks/setting/useMenuSetting';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { useAppStore } from '/@/store/modules/app';
  import { useGlobSetting } from '/@/hooks/setting';
  import { getApplication } from '/@/api/tenant';
  import { getAppTokenByAuthToken } from '/@/api/sys/user';
  import { useUserStore } from '/@/store/modules/user';
  import type { AppInfo } from '/#/store';
  import { getLoginToken } from '/@/utils/auth';

  interface AppItem {
    id: string;
    appName: string;
    [key: string]: string | number;
  }

  export default defineComponent({
    name: 'AppSide',
    components: {
      Icon,
      SvgIcon,
    },
    setup() {
      const apps = ref(<AppItem[]>[]);
      const isCloseIcon = ref(true);
      const { title } = useGlobSetting();
      const appSideRef = ref<HTMLDivElement | null>(null);
      const appStore = useAppStore();

      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo;
      const { getMenuWidth } = useMenuSetting();

      const getAppSideDomStyle = computed((): CSSProperties => {
        const showAppSide = appStore.getMenuSetting.collapsed;
        const width = showAppSide ? '280px' : 0;
        const boxShadow = showAppSide ? '5px 5px 15px 5px #b6b6b9' : '0px 0px 0px 0px #b6b6b9';
        return {
          width: width,
          overflow: 'hidden',
          flex: `0 0 ${width}`,
          maxWidth: width,
          minWidth: width,
          transition: 'all 0.4s',
          borderRight: '1px solid rgba(5, 5, 5, 0.06)',
          position: 'fixed',
          left: '0',
          height: '100%',
          bottom: '0',
          background: '#fff',
          zIndex: 999,
          boxShadow,
        };
      });

      onMounted(() => {
        // console.log('app sider load', appStore.getMenuSetting.collapsed);
        fetch();
        // document.addEventListener('click', listenDomClick);
      });
      onBeforeUnmount(() => {
        // document.removeEventListener('click',listenDomClick);
      });
      function listenDomClick(event) {
        const clickMenuSwitch = appStore.getMenuSetting.clickMenuSwitch;
        //点击展开按钮展开菜单
        if (clickMenuSwitch) {
          appStore.setProjectConfig({
            menuSetting: {
              collapsed: true,
              clickMenuSwitch: false,
            },
          });
        } else {
          // 点击其他区域或关闭按钮时关闭
          if ((appSideRef.value && !appSideRef.value.contains(event.target)) || isCloseIcon.value) {
            isCloseIcon.value = false;
            appStore.setProjectConfig({
              menuSetting: {
                collapsed: false,
                clickMenuSwitch: false,
              },
            });
          } else {
            //点击菜单区域不隐藏
            appStore.setProjectConfig({
              menuSetting: {
                collapsed: true,
                clickMenuSwitch: false,
              },
            });
          }
        }
      }
      async function fetch() {
        let result = await getApplication({});
        if (result) {
          apps.value = result.filter((item) => item.appCode != 'opensign_0001');
          const temApps: AppInfo[] = [];
          apps.value.forEach((item: any) => {
            temApps.push({
              appAddress: item.appAddress,
              appCode: item.appCode,
              appName: item.appName,
              appFront: item.appFront,
              id: item.id,
            });
          });
          appStore.setApps(temApps);
        }
      }

      function handleAppSide() {
        isCloseIcon.value = true;
        appStore.setProjectConfig({
          menuSetting: {
            collapsed: true,
          },
        });
      }
      async function handleEnterApp(row) {
        if (!row.selectFlag) {
          return;
        }
        let params = {
          tenantId: userInfo.loginTenantId,
          departId: userInfo.loginDepartId,
        };
        const loginToken = getLoginToken() as string;
        let newToken = await getAppTokenByAuthToken(params);
        if (newToken) {
          let appInfo = {
            token: newToken,
            loginToken,
            appCode: row.appCode,
            appId: row.id,
            // departId:userInfo.loginDepartId,
            redirect: row.appAddress + '/#' + row.appFront.split('#')[1],
          };
          let paramsString = new URLSearchParams(appInfo).toString();
          window.open(row.appAddress + row.appFront + '?' + paramsString);
        }
      }
      const mouseoverMouseout = () => {
        appStore.setProjectConfig({
          menuSetting: {
            collapsed: false,
            clickMenuSwitch: true,
          },
        });
      };

      return {
        getMenuWidth,
        getAppSideDomStyle,
        handleAppSide,
        title,
        apps,
        handleEnterApp,
        appSideRef,
        userInfo,
        appStore,
        mouseoverMouseout,
      };
    },
  });
</script>

<style lang="less" scoped>
  .app-side-container {
    box-sizing: border-box;
    .app-side-header {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 13px 20px;
      border-bottom: 1px solid #eee;
      white-space: nowrap;
      .app-side-title {
        font-size: 16px;
        font-weight: 700;
        transition: all 0.5s;
        margin-left: 10px;
        padding-right: 5px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;

        // margin:0 auto;
      }
    }
    .application-list {
      .application-item {
        display: flex;
        align-items: center;
        padding: 10px 15px;
        white-space: nowrap;
        cursor: pointer;
      }
    }
  }
</style>

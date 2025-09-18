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
  <div :class="prefixCls" class="relative w-full h-full flex items-center justify-center min-h-screen p-8" style="background: linear-gradient(135deg, #F5F7FA 0%, #FFFFFF 100%);">
    <div class="absolute top-8 left-8 z-50"> 
      <div class="flex items-center space-x-3">
        <img :src="logoBase64" alt="" class="w-auto h-16" />
        <!-- <div class="flex flex-col">
          <p class="text-2xl font-bold text-gray-800">{{ websiteTitle }}</p>
          <p class="text-sm text-gray-500">让电子签章更简单</p>
        </div> -->
      </div>
    </div>
  
    <div :class="prefixCls + '-form'" class="w-[460px] p-8 bg-white rounded-md shadow-md rounded-2xl" style="background: rgba(255, 255, 255, 0.95);backdrop-filter: blur(10px);box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);">
      <!-- <h2 class="text-center text-2xl font-bold mb-4">登录{{ websiteTitle }}</h2> -->
      <h2 class="text-center text-2xl font-bold mb-4">{{ pageTitle }}</h2>
      <p class="text-center text-gray-500 mb-6" v-if="TabKey === 'LOGIN_MOBILE' || TabKey === 'LOGIN' ">请登录您的账户继续使用</p>

      <!-- 自定义按钮组 -->
      <div class="flex bg-gray-100 p-1 mb-6" v-if="TabKey === 'LOGIN_MOBILE' || TabKey === 'LOGIN' " >
        <button
          @click="changeTab(LoginStateEnum.LOGIN_MOBILE)"
          :class="[
            'w-1/2 py-2 text-center ',
            TabKey === 'LOGIN_MOBILE' ? 'text-white' : 'bg-gray-100 text-gray-600'
          ]"
          :style="TabKey === 'LOGIN_MOBILE' ? 'background: #1890FF' : ''"
        
        >
          验证码登录
        </button>
        <button
          @click="changeTab(LoginStateEnum.LOGIN)"
          :class="[
            'w-1/2 py-2 text-center',
            TabKey === 'LOGIN' ? 'text-white' : 'bg-gray-100 text-gray-600'
          ]"
          :style="TabKey === 'LOGIN' ? 'background: #1890FF' : ''"
        >
          账号登录
        </button>
        <!-- <a-radio-group v-model:value="TabKey" button-style="solid" @change="changeTab">
          <a-radio-button value="LOGIN_MOBILE">验证码登录</a-radio-button>
          <a-radio-button value="LOGIN">账号登录</a-radio-button>
        </a-radio-group> -->
      </div>

      <!-- 登录表单内容 -->
      <component :is="currentComponent"></component>
      <!-- <div style="font-size:14px;margin-bottom:30px" >
          <p style="margin: 0">
            <a-checkbox v-model:checked="registerCheck">我已阅读并同意</a-checkbox>
            <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
            <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
          </p>
      </div> -->
      <div style="text-align: right" v-if="TabKey === 'LOGIN' || TabKey === 'LOGIN_MOBILE'">
        <a-button type="link" size="small" @click="showPhoneEmail('FIND_ACCOUNT')">
          忘记账号？
        </a-button>
        <a-button type="link" size="small" @click="showPhoneEmail('RESET_PASSWORD')">
          忘记密码？
        </a-button>
      </div>
    </div>
  
    <div class="copyright-footer fixed bottom-4 left-0 w-full flex justify-center items-center">
      <p v-html="webCopyRight"></p>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { computed, unref, ref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import LoginForm from './LoginForm.vue';
  import MobileForm from './MobileForm.vue';
  import ForgetPasswordForm from './ForgetPasswordForm.vue';
  import ForgetAccount from './ForgetAccount.vue';
  import { Tabs } from 'ant-design-vue';
  // import { useRoute } from 'vue-router';
  import { useUserStore } from '/@/store/modules/user';
  import { useLoginState, LoginStateEnum } from './useLogin';
  import defaultLogo from '/@/assets/images/logo-sign.png';
  defineProps({
    sessionTimeout: {
      type: Boolean,
    },
  });

  const { prefixCls } = useDesign('login');
  const { getLoginState, setLoginState } = useLoginState();
  const TabPane = Tabs.TabPane;
  setLoginState(LoginStateEnum.LOGIN);
  const userStore = useUserStore();
  const logoBase64 = ref(defaultLogo);
  const webCopyRight = ref('');
  const websiteTitle = ref('');

  userStore.setLoginToken(undefined);
  const TabKey = computed(() => LoginStateEnum[unref(getLoginState)]);
  const registerCheck = ref(true);
  const pageTitle = computed(() => {
  switch (TabKey.value) {
    case 'LOGIN_MOBILE':
    case 'LOGIN':
      return `登录${websiteTitle.value}`;
    case 'FIND_ACCOUNT':
      return '找回账号';
    case 'RESET_PASSWORD':
      return '重置登录密码';
    default:
      return `登录${websiteTitle.value}`;
  }
});
  async function initData() {
    const web: any = userStore.getWebConfig;
    webCopyRight.value = web.websiteCopyright || webCopyRight.value;
    logoBase64.value = web.websiteOtherLogo || logoBase64.value;
    websiteTitle.value = web.websiteTitle || "开放签";
  }
  initData();
  // const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);
  // function changeTab(val) {
  //   if (val === 'LOGIN_VERIFY') {
  //     setLoginState(LoginStateEnum.LOGIN_VERIFY);
  //   }
  //   // if(val==='LOGIN' && route.fullPath.indexOf('experience') === -1){
  //   if (val === 'LOGIN') {
  //     setLoginState(LoginStateEnum.LOGIN);
  //   }
  //   // if(val==='LOGIN_MOBILE' && route.fullPath.indexOf('experience') === -1){
  //   if (val === 'LOGIN_MOBILE') {
  //     setLoginState(LoginStateEnum.LOGIN_MOBILE);
  //   }
  //   if (val === 'EXPERIENCE') {
  //     setLoginState(LoginStateEnum.EXPERIENCE);
  //   }
  // }

   // 当前显示的组件
  const currentComponent = computed(() => {
    switch (TabKey.value) {
      case 'LOGIN':
        return LoginForm;
      case 'LOGIN_MOBILE':
        return MobileForm;
      case 'RESET_PASSWORD':
        return ForgetPasswordForm;
      case 'FIND_ACCOUNT':
        return ForgetAccount;
      default:
        return LoginForm;
    }
  });

  function changeTab(val:LoginStateEnum) {

    // console.log(val.target.value);
    setLoginState(val);
  }


  function showPhoneEmail(moduleName) {
    if (moduleName === 'RESET_PASSWORD') {
      setLoginState(LoginStateEnum.RESET_PASSWORD);
    } else if (moduleName === 'FIND_ACCOUNT') {
      setLoginState(LoginStateEnum.FIND_ACCOUNT);
    }
  }
  function handleTerm(type) {
    window.open('/#/terms/service/' + type);
  }




</script>


<style lang="less">
  .login {
    &-form {
      .ant-input {
        height: 40px;
        border: 1px solid #ddd;
        border-radius: 4px;
        padding: 0 12px;
        font-size: 14px;

        &:focus {
          border-color: #1890ff;
          box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.2);
        }
      }

      .ant-btn {
        width: 100%;
        height: 40px;
        background-color: #1890ff;
        border-color: #1890ff;
        color: #fff;
        border-radius: 4px;
        font-size: 14px;

        &:hover,
        &:focus {
          background-color: #0056b3;
          border-color: #0056b3;
        }
      }

      .ant-checkbox-wrapper {
        margin-right: 10px;
        font-size: 14px;
        color: #666;
      }

      .ant-form-item-label > label {
        font-size: 14px;
        color: #666;
      }

      .ant-form-item-control {
        line-height: 40pxchangeTab
      }

      .ant-form-explain {
        margin-top: 5px;
        font-size: 12px;
        color: #f5222d;
      }

      .ant-form-text {
        font-size: 14px;
        color: #666;
      }

      .ant-form-item-extra {
        font-size: 14px;
        color: #1890ff;
      }
    }
  }
  .copyright-footer{
    // font-size: 10px;
    color: rgb(156 163 175 / var(--tw-text-opacity, 1));
    margin: 0;
    text-align: center;
    font-size: 0.75rem;
    line-height: 1rem;
  }
</style>

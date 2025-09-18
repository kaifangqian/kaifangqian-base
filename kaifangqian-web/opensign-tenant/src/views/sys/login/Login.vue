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
  <div :class="prefixCls" class="relative w-full h-full ">
    <div :class="prefixCls+ '-header'">
      <div class="header-left">
        <img src="../../../assets/images/logo.png" alt="" style="width:70px"> 
        <span class="header-title">犀牛PaaS管理系统开发平台</span>
      </div>
      <div class="header-right">
        <a href="https://www.yuque.com/huxin-ch41t/resrun/akk7i0" target="_blank">帮助文档</a>
        <a href="https://resrun.cn/" target="_blank">官网</a>
      </div>
    </div>
    <div class="container relative  py-2 mx-auto sm:px-10">
      <div class="flex w-full h-full py-5  xl:py-5 xl:my-0 xl:w-12/12">
        <div
          :class="`${prefixCls}-form`"
          class="relative w-full px-5 py-8 mx-auto my-auto rounded-md shadow-md xl:bg-transparent sm:px-8 xl:p-4 xl:shadow-none sm:w-3/4 lg:w-2/4 xl:w-auto enter-x"
        >
        <Tabs size="large" @change="changeTab">
          <TabPane  tab="账号登录" key="LOGIN" v-if=" TabKey==='LOGIN' || TabKey === 'LOGIN_MOBILE' || TabKey === 'EXPERIENCE'" >
              <LoginForm />
          </TabPane>
          <TabPane tab="手机号登录" key="LOGIN_MOBILE" v-if="TabKey==='LOGIN' || TabKey=== 'LOGIN_MOBILE' || TabKey === 'EXPERIENCE'">
              <MobileForm />
          </TabPane>
          <TabPane tab="体验账号登录" key="EXPERIENCE" v-if="TabKey==='LOGIN'  || TabKey === 'EXPERIENCE' || TabKey=== 'LOGIN_MOBILE'">
              <MobileForm />
          </TabPane>
          <TabPane tab="登录校验" key="LOGIN_VERIFY" v-if="TabKey==='LOGIN_VERIFY'">
              <MobileForm />
          </TabPane>
          <!-- <ForgetPasswordForm /> -->
          <TabPane tab="重置密码" key="RESET_PASSWORD"  v-if="TabKey==='RESET_PASSWORD'">
              <ForgetPasswordForm />
          </TabPane>
          <TabPane tab="找回账号" key="FIND_ACCOUNT"  v-if="TabKey==='FIND_ACCOUNT'">
              <ForgetAccount />
          </TabPane>
        </Tabs>
        </div>
      </div>
    </div>
    <div class="login-footer">
      <p>Copyright 2019-2022 资源律动 resrun.cn 版权所有  京ICP备19060018号-1</p>
    </div>
  </div>
</template>
<script lang="ts" setup>
  import { computed, unref } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import LoginForm from './LoginForm.vue';
  import MobileForm from './MobileForm.vue';
  import ForgetPasswordForm from './ForgetPasswordForm.vue';
  import ForgetAccount from './ForgetAccount.vue';
  import { Tabs } from 'ant-design-vue';
  import { useRoute } from 'vue-router';
  import { useLoginState, LoginStateEnum } from './useLogin';
  defineProps({
    sessionTimeout: {
      type: Boolean,
    },
  });

  const { prefixCls } = useDesign('login');
  const {  getLoginState, setLoginState } = useLoginState();
  const TabPane = Tabs.TabPane;
  const route = useRoute();
  // if(route.fullPath.indexOf('experience')>-1){
  //   setLoginState(LoginStateEnum.EXPERIENCE)
  // }
  const TabKey = computed(() => LoginStateEnum[unref(getLoginState)]);

  // const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);
  function changeTab(val){
    if(val==='LOGIN_VERIFY'){
      setLoginState(LoginStateEnum.LOGIN_VERIFY)
    }
    // if(val==='LOGIN' && route.fullPath.indexOf('experience') === -1){
    if(val==='LOGIN'){
      setLoginState(LoginStateEnum.LOGIN)
    }
    // if(val==='LOGIN_MOBILE' && route.fullPath.indexOf('experience') === -1){
    if(val==='LOGIN_MOBILE'){
      setLoginState(LoginStateEnum.LOGIN_MOBILE)
    }
    if(val === 'EXPERIENCE'){
      setLoginState(LoginStateEnum.EXPERIENCE)
    }
  }

</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-login';
  @logo-prefix-cls: ~'@{namespace}-app-logo';
  @countdown-prefix-cls: ~'@{namespace}-countdown-input';
  @dark-bg: #293146;

  html[data-theme='dark'] {
    .@{prefix-cls} {
      background-color: @dark-bg;

      &::before {
        background-image: url(/@/assets/svg/login-bg-dark.svg);
      }

      .ant-input,
      .ant-input-password {
        background-color: #232a3b;
      }

      .ant-btn:not(.ant-btn-link):not(.ant-btn-primary) {
        border: 1px solid #4a5569;
      }

      &-form {
        background: transparent !important;
      }

      .app-iconify {
        color: #fff;
      }
    }

    input.fix-auto-fill,
    .fix-auto-fill input {
      -webkit-text-fill-color: #c9d1d9 !important;
      box-shadow: inherit !important;
    }
  }

  .@{prefix-cls} {
    min-height: 100%;
    overflow: hidden;
    background: #f5f5f5;
    @media (max-width: @screen-xl) {
      background-color: #293146;

      .@{prefix-cls}-form {
        background-color: #fff;
      }
    }
    &-header{
      width: 100%;
      height: 80px;
      padding: 0 30px;
      border-bottom: 1px solid #e4e4e4;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left{
        display: flex;
        align-items: center;
        height: 100%;
      }
      .header-right{
        a{
          font-size: 14px;
          font-weight: bold;
          margin:0 5px;
          color: #333;
        }
      }
      .header-title{
        color: #127fd2;
        font-size: 20px;
        font-weight: 600;
        margin-left: 5px;
      }
    }
     .login-footer{
        position: absolute;
        text-align: center;
        bottom: 0px;
        left: 50%;
        transform: translate(-50%,-50%);
      }
    &-form{
        background-color: #fff;
        min-width: 422px;
    }

    &::before {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      margin-left: -48%;
      // background-image: url(/@/assets/svg/login-bg.svg);
      background-position: 100%;
      background-repeat: no-repeat;
      background-size: auto 100%;
      content: '';
      @media (max-width: @screen-xl) {
        display: none;
      }
    }

    .@{logo-prefix-cls} {
      position: absolute;
      top: 12px;
      height: 30px;

      &__title {
        font-size: 16px;
        color: #fff;
      }

      img {
        width: 32px;
      }
    }

    .container {
      .@{logo-prefix-cls} {
        display: flex;
        width: 60%;
        height: 80px;

        &__title {
          font-size: 24px;
          color: #fff;
        }

        img {
          width: 48px;
        }
      }
    }

    &-sign-in-way {
      .anticon {
        font-size: 22px;
        color: #888;
        cursor: pointer;

        &:hover {
          color: @primary-color;
        }
      }
    }

    input:not([type='checkbox']) {
      min-width: 280px;

      @media (max-width: @screen-xl) {
        min-width: 320px;
      }

      @media (max-width: @screen-lg) {
        min-width: 260px;
      }

      @media (max-width: @screen-md) {
        min-width: 240px;
      }

      @media (max-width: @screen-sm) {
        min-width: 160px;
      }
    }

    .@{countdown-prefix-cls} input {
      min-width: unset;
    }

    .ant-divider-inner-text {
      font-size: 12px;
      color: @text-color-secondary;
    }
  }
  .icon-eye{
    cursor:pointer;
  }
</style>

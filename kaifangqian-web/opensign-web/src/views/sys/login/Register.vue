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
        <img src="../../../assets/images/logo-sign.png" alt="" style="width:150px"> 
        <!-- <span class="header-title">犀牛PaaS</span> -->
      </div>
      <!-- <div class="header-right">
        <a href="https://www.yuque.com/huxin-ch41t/resrun/akk7i0" target="_blank">帮助文档</a>
        <a href="https://resrun.cn/" target="_blank">官网</a>
      </div> -->
    </div>
    <div class="container relative ">

      <div class="flex ">
        <div class="login-bg-container">
          <p class="copyright-footer">
            <span>QQ群：482074553</span>
            <span>联系电话：15010993257</span>
            <span>邮箱：service@resrun.cn</span>
            <span>Copyright 2019-2023 北京资源律动科技有限公司</span>
            <span> 版权所有</span>
          </p>
        </div>
        <div
          :class="`${prefixCls}-form`"
          class="relative w-full px-5 py-8 mx-auto my-auto rounded-md shadow-md xl:bg-transparent sm:px-8 xl:p-4 xl:shadow-none sm:w-3/4 lg:w-2/4 xl:w-auto enter-x"
        >
        <Tabs size="large"  v-model:activeKey="activeKey" class="login-tab-header">
          <TabPane  tab="账号注册" key="REGISTER" >
              <RegisterForm />
          </TabPane>
        </Tabs>
        
        </div>
      </div>
    </div>
    <!-- <div class="login-footer">
      <p>Copyright 2019-2022 资源律动 resrun.cn 版权所有  京ICP备19060018号-1</p>
    </div> -->
  </div>
</template>
<script lang="ts">
  import { computed, unref, ref, defineComponent } from 'vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Tabs } from 'ant-design-vue';
  import { useRoute } from 'vue-router';
  import { useLoginState } from './useLogin';
  import RegisterForm from './RegisterForm.vue';



  export default defineComponent({
    name:'Register',
    components:{
      RegisterForm,
      Tabs,
      TabPane:Tabs.TabPane,
    },
    setup() {
      const { prefixCls } = useDesign('register');
      const activeKey  = ref('REGISTER');
      const {  getLoginState, setLoginState } = useLoginState();
      const route = useRoute();





      return {
        prefixCls,
        activeKey
      }
    }
  })

 



</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-register';
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
    background: #fff;
    &-form{
      box-shadow: 0 2px 9px 5px rgb(64 106 103 / 13%);
      border: none !important;
      width:430px;
    }
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
      background-color: rgba(255,255,255,0);
      display: flex;
      justify-content: space-between;
      align-items: center;
      z-index: 99;
      position: absolute;
      top:0;
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
        border: 1px solid #ddd;
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
      max-width: 100%;
      .@{logo-prefix-cls} {
        display: flex;
        width: 55%;
        height: 80px;

        &__title {
          font-size: 24px;
          color: #fff;
        }

        img {
          width: 48px;
        }
      }
      .login-bg-container{
        width:60%;
        height:100%;
        background:url('../../../assets/images/login-bg.png');
        background-size:100% 100%;
        height:100vh;
        .copyright-footer{
            position: absolute;
            bottom:15px;
            left:50%;
            color:#999;
            transform: translate(-40%);
            width:100%;
            span{
              margin:0 5px;
          }
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

    input:not([type='checkbox']):not([type='radio']) {
      min-width: 100px;

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
      font-size: 14px;
      color: @text-color-secondary;
    }
  }
  .register-type{
      :deep(.ant-form-item) .ant-form-item-label > label{
        font-size: 14px;
      }
    }
  .icon-eye{
    cursor:pointer;
  }
  .login-tab-header{
    .ant-tabs-nav{
      // width: 190px;
      margin: 0 auto 40px;
    }
    
  }
  .login-tab-header{
    .ant-tabs-content{
      width:85% !important;
      margin:0 auto;
    }
  }

</style>

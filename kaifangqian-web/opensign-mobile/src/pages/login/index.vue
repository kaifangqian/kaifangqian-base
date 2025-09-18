<!--
  @description 登录页面

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
  <div class="login">
    <!-- logo及标题区 -->
     <img :src="base64Logo" alt="logo" class="logo-img" />
    <div class="login-header">
      <!-- <div class="logo-bg"> -->
      <!-- <img :src="base64Logo" alt="logo" class="logo-img" /> -->
      <!-- </div> -->
      <div class="system-title">{{websiteTitle}}</div>
      <div class="system-desc">安全 · 合规 · 便捷 · 可信</div>
    </div>
    <!-- 登录卡片 -->
    <div class="login-card">
      <div class="content">
        <van-form @submit="onSubmit" @failed="onFailed" ref="loginFrom" v-if="loginType == 0">
          <span class="login-type">验证码登录</span>
          <van-field
            v-model.trim="loginInfo.account"
            name="account"
            :placeholder="buildAccountPlaceholder()"
            label="账号"
            label-width="1.5rem"
            :rules="[{ validator: validatorAccount }]"
          />
          <van-field
            v-model="loginInfo.captcha"
            name="password"
            placeholder="验证码"
            label="验证码"
            label-width="1.5rem"
            :rules="[{ validator: validatorCaptcha }]"
          >
            <template #button>
              <div @click="sendPhoneCode">
                <span class="custom-link" :class="duration != initDuration ? 'disabled' : ''">{{
                  countdownText
                }}</span>
              </div>
            </template>
          </van-field>
          <!-- <div style="height: 30px"></div> -->
          <van-button round block type="primary" native-type="submit" class="submit-btn"
            >登录&nbsp;/&nbsp;注册</van-button
          >
          <p style="text-align: center; display: flex; align-content: flex-start; flex-wrap: wrap">
            <van-checkbox v-model="registerCheck" shape="square" icon-size="0.3rem"></van-checkbox>
            <span class="ling-font-size" style="padding-left: 0.2rem"
              >&nbsp;&nbsp;我已阅读并同意</span
            >
            <!-- <span
              href="javascript:;"
              class="custom-link ling-font-size"
              @click="handleTerm('certificate')"
              >《数字证书使用协议》</span
            > -->
            <span href="javascript:;" class="custom-link ling-font-size" @click="handleTerm('serve')"
              >《服务协议》</span
            >
            <span
              href="javascript:;"
              class="custom-link ling-font-size"
              @click="handleTerm('privacy')"
              >《隐私政策》</span
            >
          </p>
        </van-form>

        <van-form @submit="onSubmit" @failed="onFailed" ref="loginFrom" v-if="loginType == 1">
          <span class="login-type">密码登录</span>
          <van-field
            v-model.trim="loginInfo.username"
            name="username"
            placeholder="请输入登录账号"
            label="账号"
            label-width="1.5rem"
            :rules="[{ required: true, message: '账号不能为空' }]"
          />
          <van-field
            v-model="loginInfo.password"
            type="password"
            placeholder="请输入登录密码"
            label="密码"
            label-width="1.5rem"
            :rules="[{ required: true, message: '密码不能为空' }]"
          >
          </van-field>

          <van-field
            v-model="loginInfo.captcha"
            name="password"
            placeholder="验证码"
            label="验证码"
            label-width="1.5rem"
            :rules="[{ validator: validatorImgCaptcha }]"
          >
            <template #button>
              <img :src="base64Img" v-if="base64Img" @click="reloadImgCode" />
            </template>
          </van-field>

          <van-button round block type="primary" native-type="submit" class="submit-btn"
            >登录</van-button
          >
          <p style="text-align: center; display: flex; align-content: flex-start; flex-wrap: wrap">
            <van-checkbox v-model="registerCheck" shape="square" icon-size="0.3rem"></van-checkbox>
            <span class="ling-font-size" style="padding-left: 0.2rem"
              >&nbsp;&nbsp;我已阅读并同意</span
            >
            <!-- <span
              href="javascript:;"
              class="custom-link ling-font-size"
              @click="handleTerm('certificate')"
              >《数字证书使用协议》</span
            > -->
            <span
              href="javascript:;"
              class="custom-link ling-font-size"
              @click="handleTerm('serve')"
              >《服务协议》</span
            >
            <span
              href="javascript:;"
              class="custom-link ling-font-size"
              @click="handleTerm('privacy')"
              >《隐私政策》</span
            >
          </p>
        </van-form>
      </div>
      <!-- <slot /> -->
    </div>
    <!-- 其他登录方式 -->
    <div class="other-login-area">
      <div class="other-login-divider">
        <span class="other-login-title">其他登录方式</span>
      </div>
      <span @click="loginTypeSwitch(1)" class="custom-link" v-if="loginType == 0">密码登录</span>
      <span @click="loginTypeSwitch(0)" class="custom-link" v-if="loginType == 1">验证码登录</span>
      <!-- <div class="other-login-icons">
        <div class="icon-circle"><img src="@/assets/icons/wechat.svg" alt="微信" /></div>
        <div class="icon-circle"><img src="@/assets/icons/mobile.svg" alt="手机" /></div>
      </div> -->
    </div>
    <!-- 底部版权信息 -->
    <!-- <div class="copyright-footer">
      <p v-html="webCopyRight"></p>
    </div> -->
  </div>
  <VanDialog
    v-model:show="selectDepart.show"
    title="请选择身份进行登录"
    confirm-button-text="退出登录"
    @confirm="quitSelectDepart"
    confirm-button-color="#666"
  >
    <div class="account-list">
      <div v-for="(item, index) in selectDepart.depts" @click="handleSubmit(item)" :key="index">
        <div :class="['account-item', item.useFlag ? '' : 'no-use']">
          <p :key="index" class="company-header">
            <span>{{ item.tenantName }}</span>
            <!-- <span class="current-login-tag" v-if="item.selectFlag">
              <van-tag color="#096dd9" plain v-if="item.selectFlag" size="medium">上次登录</van-tag>
            </span> -->
            <span class="current-login-tag" v-if="!item.useFlag">
              <van-tag type="danger" plain v-if="!item.useFlag" size="medium">已停用</van-tag>
            </span>
          </p>
        </div>
      </div>
    </div>
  </VanDialog>
  <VanDialog
    v-model:show="registerCheckModal"
    title="阅读并同意协议"
    :show-cancel-button="true"
    cancel-button-text="取消"
    confirm-button-text="同意并继续"
    @confirm="agreeContinue"
    confirm-button-color="#1989fa"
  >
    <div style="padding: 0.2rem 1.5rem; text-align: center">
      <p style="text-align: center">
        <span class="ling-font-size" style="font-size: 0.4rem">我已阅读并同意<br></span>
        <!-- <span
          href="javascript:;"
          class="custom-link ling-font-size"
          @click="handleTerm('certificate')"
          >《数字证书使用协议》</span
        > -->
        <span
          href="javascript:;"
          class="custom-link ling-font-size"
          style="font-size: 0.4rem"
          @click="handleTerm('serve')"
          >《服务协议》</span
        >
        <span
          href="javascript:;"
          class="custom-link ling-font-size"
          style="font-size: 0.4rem"
          @click="handleTerm('privacy')"
          >《隐私政策》</span
        >
      </p>
    </div>
  </VanDialog>
  <Verify
    @success="handleSildeSuccess"
    :imgSize="{ width: '300px', height: '155px' }"
    captchaType="blockPuzzle"
    ref="verifyRef"
  />
</template>

<script setup lang="ts">
  import { FieldType, Notify, Dialog, Toast } from 'vant';
  import Api, { LoginParams } from '@/api/user';
  import { useLoading } from '@/hooks';
  import debug from '@/utils/debug';
  import watermark from '@/utils/lib/watermark';
  import copyPaste from '@/utils/lib/copy-paste';
  import { useUserStore } from '@/store/modules/user';
  import { getImgCode } from '@/api';
  import { Md5 } from 'ts-md5';
  import defaultBase64 from '@/assets/images/logo-sign.png';
  import { isMobile, isEmail } from '@/utils/validate';
  import { Verify } from '@/components/verifition';
  interface Depart {
    id: string;
    name: string;
    useFlag: boolean;
    selectFlag: boolean;
    tenantName: string;
    tenantType: number; // 1=企业 2=个人
  }

  const VanDialog = Dialog.Component;
  const userStore = useUserStore();
  const loginFrom = ref({});
  const router = useRouter();
  const route = useRoute();
  const { startLoading, stopLoading } = useLoading();
  const initDuration = 59;
  //0 = 默认手机号登录注册   1 = 账号登录
  const loginType = ref(0);
  const duration = ref(59);
  const registerCheck = ref(false);
  const countdownText = ref('获取验证码');
  const base64Img = ref<any>();
  const verifyRef = ref({});
  const loginInfo = reactive<LoginParams>({
    captcha: '',
    captchaKey: '',
    phone: '',
    email: '',
    password: '',
    checkKey: '',
    username: '',
    account: '',
  });
  const webCopyRight = ref('');
  const websiteTitle = ref('');
  const selectDepart = ref({
    show: false,
    depts: <Depart[]>[],
  });
  const base64Logo = ref(defaultBase64);
  const registerCheckModal = ref(false);
  const accountType = userStore.getWebConfig.systenAccountType || 'phone_email';
  onMounted(() => {
    // 进入登录页时获取debug参数
    debug.config(route.query.debug);
    watermark.remove();
    copyPaste.enable();
    reloadImgCode();
    getLogo();
  });

  function handleTerm(type: string) {
    let signAppInfo = window.appInfo.sign_app_info;
    window.open(signAppInfo.url + '/#/terms/service/' + type);
  }

  onBeforeUnmount(() => {
    // const { username = '', mobile = '' } = auth.getUser();
    // watermark.add({
    //     // content: username + ' ' + mobile,
    // });
    copyPaste.disable();
  });
  async function getLogo() {
    // const {result,code} = await Api.getOtherLogo({});
    // if (code==200) {
    //     base64Logo.value = result.image;
    // }
    console.log('getWebConfig', userStore.getWebConfig);
    base64Logo.value = userStore.getWebConfig.websiteOtherLogo;
    webCopyRight.value = '© 北京资源律动科技有限公司 版权所有';
    websiteTitle.value = userStore.getWebConfig.websiteTitle || '开放签';
  }
  async function loginTypeSwitch(type: number) {
    if (type == 0) {
      await reloadImgCode();
      loginType.value = 0;
    } else {
      await reloadImgCode();
      loginType.value = 1;
    }
  }
  async function reloadImgCode() {
    const nowTime = new Date().getTime() + '';
    const result = await getImgCode(nowTime);
    loginInfo.checkKey = nowTime;
    base64Img.value = result.result.captchaImg;
  }
  function agreeContinue() {
    registerCheckModal.value = false;
    registerCheck.value = true;
    onSubmit();
  }
  const onSubmit = async () => {
    if (!loginInfo.captchaKey && loginType.value == 0) {
      Notify({ type: 'warning', message: '请先获取验证码', duration: 1000 });
      return;
    }
    try {
      await loginFrom.value.validate();
      if (!registerCheck.value) {
        registerCheckModal.value = true;
        return;
      }
      startLoading();
      var result: any = {};
      // Md5.hashStr(data.password),
      if (loginType.value == 0) {
        result = await userStore.registerOrLogin(loginInfo);
      } else if (loginType.value == 1) {
        result = await userStore.login({
          captcha: loginInfo.captcha,
          password: Md5.hashStr(loginInfo.password),
          checkKey: loginInfo.checkKey,
          username: loginInfo.username,
        });
      }
      const { status, depts } = result;
      stopLoading();
      if (status) {
        if (depts.length == 1) {
          handleSubmit(depts[0]);
        } else {
          selectDepart.value.show = true;
          selectDepart.value.depts = depts;
        }
      } else if (loginType.value == 1 && !status) {
        reloadImgCode();
      }
    } catch (e) {
      console.error('form field error:', e);
    }
  };
  function quitSelectDepart(isLogout: boolean) {
    selectDepart.value.show = false;
    selectDepart.value.depts = [];
    if (isLogout) {
      userStore.logout();
    }
  }
  async function handleSubmit(item: any) {
    const formData = {
      departId: item.departs[0].departId,
    };
    const result = await userStore.selectTenant(formData);
    if (result.status) {
      router.push('/index');
    }
  }
  const onFailed = (errorInfo = {}) => {
    console.log('failed', errorInfo);
  };

  const validatorPhone = (val: any) => {
    if (val) {
      if (isMobile(val)) {
        loginInfo.email = '';
        loginInfo.phone = val;
        return null;
      } else {
        return '手机号码格式不正确';
      }
    } else {
      return '手机号码不能为空';
    }
  };
  const validatorEmail = (val: any) => {
    if (val) {
      if (isEmail(val)) {
        loginInfo.phone = '';
        loginInfo.email = val;
        return null;
      } else {
        return '邮箱格式不正确';
      }
    } else {
      return '邮箱不能为空';
    }
  };
  const validatorPhoneOrEmail = (val: any) => {
    if (val) {
      if (isMobile(val)) {
        loginInfo.email = '';
        loginInfo.phone = val;
        return null;
      } else if (isEmail(val)) {
        loginInfo.phone = '';
        loginInfo.email = val;
        return null;
      } else {
        return '手机号或邮箱格式不正确';
      }
    } else {
      return '手机号或邮箱不能为空';
    }
  };

  function registerRemind() {
    registerCheckModal.value = true;
  }
  const validatorCaptcha = (val: any) => {
    if (val) {
      return !/(^\d{4}$)|(^\d{6}$)/.test(val) ? '验证码格式不正确' : null;
    } else {
      return '验证码不能为空';
    }
  };

  const validatorImgCaptcha = (val: any) => {
    if (val) {
      return !/^[a-zA-Z\d]{4}$/.test(val) ? '验证码格式不正确' : null;
    } else {
      return '验证码不能为空';
    }
  };

  async function sendPhoneCode() {
    console.log('sendPhoneCode', loginFrom.value);
    try {
      await loginFrom.value.validate('account');
      if (duration.value === initDuration) {
        verifyRef.value.show();
        // document.addEventListener('touchstart', inhibitMove, { passive: false });
        // document.addEventListener('touchmove', inhibitMove, { passive: false });
      }
    } catch (e) {
      console.error('form field error:', e);
    }
  }
  function inhibitMove(e: any) {
    console.log('拖动open');
    // e.preventDefault();
  }
  async function handleSildeSuccess(data: any) {
    if (duration.value === initDuration) {
      // document.removeEventListener('touchstart', inhibitMove);
      // document.removeEventListener('touchmove', inhibitMove);
      duration.value = initDuration;
      const params = {
        phone: loginInfo.phone,
        email: loginInfo.email,
        type: 'login1',
        captchaVerification: data.captchaVerification,
      };
      let response: any;
      if (isEmail(loginInfo.account)) {
        response = await Api.sendEmailCode(params);
      } else if (isMobile(loginInfo.account)) {
        response = await Api.sendCode(params);
      } else {
        Notify({ type: 'warning', message: '账号不合法', duration: 1000 });
        return;
      }
      if (response.code == 200) {
        loginInfo.captchaKey = response.result;
        console.log('send res:', response);
        Notify({ type: 'success', message: '短信发送成功', duration: 1000 });
        delayCountDown();
      } else {
        Notify({ type: 'warning', message: '获取短信验证码失败', duration: 1000 });
      }
    } else {
      return;
    }
  }

  function slideReady(proxy: any) {
    console.log('slideReady:', proxy);
  }
  const interval = ref<any>(null);
  function delayCountDown() {
    if (duration.value == 0) {
      clearTimeout(interval.value);
      duration.value = initDuration;
      countdownText.value = '获取验证码';
    } else if (duration.value > 0) {
      clearTimeout(interval.value);
      countdownText.value = duration.value + '秒后重新获取';
      duration.value--;
      interval.value = setTimeout(function () {
        delayCountDown();
      }, 1000);
    }
  }
  function buildAccountPlaceholder() {
    if (accountType == 'phone') {
      return '请输入手机号码';
    } else if (accountType == 'email') {
      return '请输入邮箱';
    } else {
      return '请输入手机号或邮箱';
    }
  }

  function validatorAccount(val: any) {
    if (accountType == 'phone') {
      return validatorPhone(val);
    } else if (accountType == 'email') {
      return validatorEmail(val);
    } else {
      return validatorPhoneOrEmail(val);
    }
  }
</script>

<style lang="less" scoped>
  .login {
    min-height: 100vh;
    background: #f7fafd;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    padding-top: 32px;

    .login-header {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-bottom: 0.5rem;
      margin-top: 0.5rem;
    }

    .logo-bg {
      width: 80px;
      height: 80px;
      // background: #e6f0fa;
      // border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16px;
    }

    .content {
      padding-top: 30px;
      margin: 0 auto;
      width: 8rem;

      .van-form {
        .van-cell {
          margin-top: 10px;
        }

        .submit-btn {
          margin-top: 40px;
          font-weight: 700;
        }
      }
    }

    .disabled {
      color: #aaa !important;
    }

    .logo {
      text-align: center;
      padding-top: 200px;
      width: 10rem;
      margin: 0 auto;
    }

    .logo-img {
      width: 2rem;
      object-fit: contain;
      // margin-bottom: 0.5rem;
      align-self: flex-start;
      margin-left: 0.5rem;
    }

    }

    .account-list {
      overflow: auto;
      max-height: 6.6rem;

      .account-item {
        /* font-size: 14px; */
        border: 1px solid #f9f9f9;
        border-radius: 8px;
        cursor: pointer;
        align-items: center;
        justify-content: space-between;
        margin-right: 10px;
        margin-bottom: 10px;

        .company-header {
          background: #f9f9f9;
          padding: 0.3rem 0.3rem;
          display: flex;
          justify-content: space-between;
          margin: 0 !important;

          .login-arrow {
            visibility: hidden;
          }

          &:hover {
            .login-arrow {
              visibility: visible;
              color: #094aee;
            }
          }
          .current-login-tag {
            width: 2rem;
            justify-content: center;
            align-items: center;
            display: flex;
          }
        }

        .account-depart {
          display: flex;
          margin-bottom: 10px;
          justify-content: space-between;
          padding: 0 20px;
        }
      }

      .no-use {
        pointer-events: none;
      }
    }

    .other-login {
      margin-top: 1rem;
      width: 100%;
      text-align: center;
      span {
        font-size: 0.4rem;
      }
    }

    .ling-font-size {
      font-size: 0.3rem;
      padding: 0 !important;
      line-height: 0.8rem;
    }

  .system-title {
    font-size: 0.7rem;
    font-weight: 700;
    color: #1f2937;
    margin-bottom: 4px;
    /* 添加轻微的文本阴影 */
    text-shadow: 0.8px 0 0 currentColor, 0 0.8px 0 currentColor;
  }

  .system-desc {
    font-size: 0.3rem;
    color: #6b7280;
    margin-bottom: 8px;
  }

  .login-card {
    // width: 340px;
    background: linear-gradient(0deg, rgba(0, 0, 0, 0.001), rgba(0, 0, 0, 0.001)), #FFFFFF;
    border-radius: 24px;
    box-shadow: 0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1);
    padding: 16px 16px 16px 16px;
    // margin-bottom: 32px;
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }

  .other-login-area {
    // width: 340px;
    margin: 1rem auto;
    text-align: center;
    width: 80%;
  }
  .other-login-divider {
    display: flex;
    align-items: center;
    width: 100%;
    margin-bottom: 0.5rem;
  }

  .other-login-title {
    color: #8c98a4;
    font-size: 0.3rem;
    margin: 0 16px;
    white-space: nowrap;
    font-weight: 400;
    flex-shrink: 0;
  }

  .other-login-divider::before,
  .other-login-divider::after {
    content: "";
    flex: 1;
    height: 1px;
    background: #cfd6df;
    opacity: 0.8;
  }

  .other-login-icons {
    display: flex;
    justify-content: center;
    gap: 24px;
  }

  .icon-circle {
    width: 44px;
    height: 44px;
    background: #f5f7fa;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 2px 8px rgba(59,130,246,0.08);
  }

  .icon-circle img {
    width: 24px;
    height: 24px;
  }
  .login-type {
    color: #1f2937;
    font-size: 0.5rem;
    margin-bottom: 16px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-weight: 600;
    /* 添加轻微的文本阴影 */
    text-shadow: 0.3px 0 0 currentColor, 0 0.3px 0 currentColor;
  }

  .copyright-footer {
    position: fixed;
    bottom: 0.2rem;
    left: 0;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 0.3rem;
    color: #ccc;
  }
</style>

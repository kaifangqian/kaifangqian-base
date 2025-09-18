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
  <div>
    <Form class="p-3 " :model="formData" :rules="getFormRules" ref="formRef">
      <p style="color: #999; font-size: 12px">未注册的用户将为您创建系统账号</p>
      <FormItem name="account" class="">
        <Input
          size="large"
          :disabled="TabKey === 'LOGIN_VERIFY' || TabKey == 'LOGIN_REGISER'"
          v-model:value="formData.account"
          :placeholder="buildAccountPlaceholder()"
          class="fix-auto-fill"
        >
          <template #prefix>
            <Icon icon="ant-design:tablet-outlined" color="#bfbfbf"></Icon>
          </template>
        </Input>
      </FormItem>
      <FormItem name="captcha" class="">
        <CountdownInput
          size="default"
          class="fix-auto-fill"
          v-model:value="formData.captcha"
          placeholder="验证码"
          ref="smsCount"
          :sendCodeApi="sendCodeApi"
        >
          <template #prefix>
            <Icon icon="ant-design:check-circle-outlined" color="#bfbfbf"></Icon>
          </template>
        </CountdownInput>
      </FormItem>
      <Row class="policy-area">
        <Col :span="24">
          <FormItem :style="{ fontSize: '14px' }">
            <p style="margin: 0">
              <a-checkbox v-model:checked="registerCheck">我已阅读并同意</a-checkbox>
              <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
              <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
              <!-- <a href="javascript:;" @click="handleTerm('certificate')">《数字证书使用协议》</a> -->
            </p>
          </FormItem>
        </Col>
      </Row>
      <FormItem class="">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading" style="background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%); height:48px;border-color: #00000000;">
          登录
        </Button>
      </FormItem>
    </Form>

    <Verify
      @success="handleSildeSuccess"
      :imgSize="{ width: '330px', height: '155px' }"
      captchaType="blockPuzzle"
      ref="verifyRef"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref, unref, computed, h } from 'vue';
  import { Form, Input, Button, Row, Col } from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import {
    useLoginState,
    LoginStateEnum,
    useFormRules,
    useFormValid,
  } from '/@/views/sys/login/useLogin';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import { getSmsCode, getEmailCode, getSmsCodeByUsername } from '/@/api/sys/user';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Md5 } from 'ts-md5';
  import { Icon } from '/@/components/Icon';
  import { useGo } from '/@/hooks/web/usePage';
  import { getAppEnvConfig } from '/@/utils/env';
  import { isMobile, isEmail } from '/@/utils/validate';
  import { Verify } from '/@/components/verifition';
  export default defineComponent({
    name: 'MobileForm',
    components: {
      Form,
      FormItem: Form.Item,
      Input,
      Button,
      CountdownInput,
      Row,
      Col,
      Icon,
      Verify,
    },
    props: {
      phone: {
        type: String,
        default: '',
      },
      email: {
        type: String,
        default: '',
      },
      partyName: {
        type: String,
        default: '',
      },
    },
    emits: ['success'],
    setup(props, { emit }) {
      const { prefixCls } = useDesign('login');
      const { createMessage, notification, createErrorModal } = useMessage();
      const { handleBackLogin } = useLoginState();
      const { setLoginState, getLoginState } = useLoginState();

      const go = useGo();
      const userStore = useUserStore();
      const sliderText = ref('向右滑动完成拼图');
      console.log(LoginStateEnum[unref(getLoginState)], 'sdfdsdfdsfd');
      const TabKey = computed(() => LoginStateEnum[unref(getLoginState)]);
      const formRef = ref();
      const smsCount = ref(null);
      const loading = ref(false);
      const slideCodeShow = ref(false);
      const captchaKey = ref('');
      const verifyUsername = ref('');
      const verifyPhone = userStore.getSafeInfo.phone;
      const accountType = userStore.getWebConfig.systenAccountType || 'phone_email';
      const verifyRef = ref({});
      const formData = reactive({
        phone: userStore.getSafeInfo.phone,
        captcha: '',
        account: '',
        email: '',
      });
      console.log(props.partyName, '企业名称');
      if (props.phone) {
        formData.account = props.phone;
        setLoginState(LoginStateEnum.LOGIN_REGISER);
      }
      if (props.email) {
        formData.account = props.email;
        setLoginState(LoginStateEnum.LOGIN_REGISER);
      }
      const { getFormRules } = useFormRules(formData, accountType);
      const disableMobile = computed(() => !!userStore.getSafeInfo.phone);
      console.log(disableMobile.value, userStore.getSafeInfo);
      const { validForm } = useFormValid(formRef);
      const registerCheck = ref(false);
      const {createConfirm } = useMessage();
      async function handleLogin() {
        let { VITE_GLOB_APP_CODE } = getAppEnvConfig();

        const data = await validForm();
        if (!data) return;
        const isAgreed = await checkAgreement();
          if (!isAgreed) {
            return;
          }
        loading.value = true;
        try {
          const userInfo = await userStore.login({
            phone: unref(TabKey) === 'LOGIN_VERIFY' ? undefined : formData.phone,
            email: unref(TabKey) === 'LOGIN_VERIFY' ? undefined : formData.email,
            captcha: formData.captcha,
            captchaKey: unref(captchaKey),
            type: unref(TabKey) == 'EXPERIENCE' ? 'visitor' : 'phone',
            password: unref(TabKey) == 'EXPERIENCE' ? Md5.hashStr('123456') : undefined,
            username: unref(TabKey) === 'LOGIN_VERIFY' ? unref(verifyUsername) : undefined,
            appCode: VITE_GLOB_APP_CODE,
          });
          if (userInfo) {
            emit('success', userInfo);
          } else {
            console.log(userInfo, '--返回的信息--');
          }
        } catch (error) {
          // createMessage.error((error as unknown as Error).message || '网络异常');
          createErrorModal({
            title: '错误提示',
            content: (error as unknown as Error).message || '网络异常',
            getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
          });
          loading.value = false;
        } finally {
          loading.value = false;
        }
      }
      function handleFail() {
        sliderText.value = '验证失败';
        setTimeout(() => {
          sliderText.value = '向右滑动完成拼图';
        }, 1000);
      }
      async function handleSildeSuccess(params) {
        sliderText.value = '验证成功';
        setTimeout(async () => {
          if (smsCount.value) {
            slideCodeShow.value = false;
            let result;
            // 异地登录带过手机号并且用username
            if (unref(TabKey) === 'LOGIN_VERIFY') {
              verifyUsername.value = userStore.getSafeInfo.username;
              result = await getSmsCodeByUsername({
                type: 'mobile_login_verify',
                username: userStore.getSafeInfo.username,
              });
            } else {
              if (isEmail(formData.account)) {
                result = await getEmailCode({
                  email: formData.account,
                  type: 'login1',
                  captchaVerification: params.captchaVerification,
                });
              } else if (isMobile(formData.account)) {
                result = await getSmsCode({
                  phone: formData.phone,
                  type: 'login1',
                  captchaVerification: params.captchaVerification,
                });
              }
            }

            if (result) {
              captchaKey.value = result;
              console.log(result, '结果');
            }
            smsCount.value.triggerClick();
          }
        }, 500);
      }
      function sendCodeApi() {
        return new Promise(async (resolve) => {
          try {
            const data = await validForm('account');
          } catch (e) {
            return false;
          }
          if (!slideCodeShow.value) {
            verifyRef.value.show();
            return false;
          }
          slideCodeShow.value = true;
          resolve(!unref(slideCodeShow));
          return !unref(slideCodeShow);
        });
      }
      function showPhoneEmail(moduleName) {}

      function handleTerm(type) {
        window.open('/#/terms/service/' + type);
      }
      function handlePhoneEmailSuccess(moduleName, methodName) {
        console.log(methodName);
        if (moduleName === 'RESET_PASSWORD') {
          setLoginState(LoginStateEnum.RESET_PASSWORD);
        } else if (moduleName === 'FIND_ACCOUNT') {
          setLoginState(LoginStateEnum.FIND_ACCOUNT);
        }
      }
      function handleRegister() {
        go('/register');
      }
      function handleSuccess() {
        formData.captcha = '';
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
      async function checkAgreement(): Promise<boolean> {
        if (registerCheck.value) {
          return true;
        }
        return new Promise((resolve) => {
          createConfirm({
            iconType: 'warning',
            title: '阅读并同意协议',
            content: h(
              'span',
              {},
              [
              '我已阅读并同意',
              h(
                'a',
                {
                href: 'javascript:;',
                onClick: () => handleTerm('serve'),
                style: 'color: #1890ff; margin: 0 4px;',
                },
                '《服务协议》'
              ),
              h(
                'a',
                {
                href: 'javascript:;',
                onClick: () => handleTerm('privacy'),
                style: 'color: #1890ff; margin: 0 4px;',
                },
                '《隐私政策》'
              ),
              ]
            ),
            cancelText: '取消',
            okText: '同意并继续',
            onOk: () => {
              registerCheck.value = true;
              resolve(true);
            },
            onCancel: () => resolve(false),
          });
        });
      }
      return {
        handleSildeSuccess,
        showPhoneEmail,
        handlePhoneEmailSuccess,
        formData,
        handleLogin,
        handleBackLogin,
        getFormRules,
        loading,
        sliderText,
        sendCodeApi,
        slideCodeShow,
        smsCount,
        handleSuccess,
        disableMobile,
        handleFail,
        TabKey,
        formRef,
        buildAccountPlaceholder,
        verifyPhone,
        handleRegister,
        verifyRef,
        registerCheck,
        handleTerm,
      };
    },
  });
</script>
<style lang="less">
  #form_item_sms {
    min-width: 280px;
  }
  .resrun-countdown-input {
    input {
      width: 280px;
    }
  }
</style>

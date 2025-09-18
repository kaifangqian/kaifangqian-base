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
          :disabled="TabKey === 'LOGIN_VERIFY'"
          v-model:value="formData.account"
          placeholder="请输入手机号码"
          class="fix-auto-fill"
          v-if="accountType == 'phone'"
        >
          <template #prefix>
            <Icon icon="ant-design:tablet-outlined" color="#bfbfbf"></Icon>
          </template>
        </Input>
        <Input
          size="large"
          :disabled="TabKey === 'LOGIN_VERIFY'"
          v-model:value="formData.account"
          placeholder="请输入邮箱"
          class="fix-auto-fill"
          v-else-if="accountType == 'email'"
        >
          <template #prefix>
            <Icon icon="ant-design:tablet-outlined" color="#bfbfbf"></Icon>
          </template>
        </Input>
        <Input
          size="large"
          :disabled="TabKey === 'LOGIN_VERIFY'"
          v-model:value="formData.account"
          class="fix-auto-fill"
          placeholder="请输入手机号或者邮箱"
          v-else
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
              <Checkbox v-model:checked="registerCheck">我已阅读并同意</Checkbox>
              <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
              <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
              <!-- <a href="javascript:;" @click="handleTerm('certificate')">《数字证书使用协议》</a> -->
            </p>
          </FormItem>
        </Col>
      </Row>
      <FormItem class="">
        <Button
          type="primary"
          size="large"
          block
          @click="handleLogin"
          :loading="loading"
          v-if="TabKey === 'LOGIN_VERIFY'"
          >登录
        </Button>
        <Button
          type="primary"
          size="large"
          style="background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%); height:48px;border-color: #00000000;"
          block
          @click="handleLogin"
          :loading="loading"
          class="rounded-lg"
          v-else
        >
          注册&nbsp;&nbsp;/&nbsp;&nbsp;登录
        </Button>
        <Button
          size="large"
          block
          class="mt-4"
          @click="handleBackLogin"
          v-if="TabKey === 'LOGIN_VERIFY'"
        >
          返回
        </Button>
      </FormItem>

    </Form>

    <MobileOrEmail @register="registerPhoneEmailModal" @success="handlePhoneEmailSuccess" />
    <LoginSelectAccount @register="registerModal" @success="handleSuccess" />
    <Verify
      @success="handleSildeSuccess"
      @error="captchaError"
      :imgSize="{ width: '330px', height: '155px' }"
      captchaType="blockPuzzle"
      ref="verifyRef"
    />
  </div>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref, unref, computed, onMounted, h } from 'vue';
  import { Form, Input, Button, Row, Col ,Checkbox} from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import { useModal } from '/@/components/Modal';
  import { useLoginState, LoginStateEnum, useFormRules, useFormValid } from './useLogin';
  import PuzzleVerification from '/@/components/verification/index.vue';
  import LoginSelectAccount from './LoginSelectAccount.vue';
  import MobileOrEmail from './MobileOrEmail.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';
  import { getSmsCode, getEmailCode, getSmsCodeByUsername } from '/@/api/sys/user';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Md5 } from 'ts-md5';
  import { Icon } from '/@/components/Icon';
  import { useGo } from '/@/hooks/web/usePage';
  import { getAppEnvConfig } from '/@/utils/env';
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
      PuzzleVerification,
      MobileOrEmail,
      LoginSelectAccount,
      Icon,
      Verify,
      Checkbox
    },

    setup(_, props) {
      const { prefixCls } = useDesign('login');
      const { createMessage, notification, createErrorModal,createConfirm } = useMessage();
      const { handleBackLogin } = useLoginState();
      const { setLoginState, getLoginState } = useLoginState();
      const [registerModal, { openModal, closeModal }] = useModal();
      const [
        registerPhoneEmailModal,
        { openModal: openPhoneEmailModal, closeModal: closePhoneEmailModal },
      ] = useModal();
      const userStore = useUserStore();
      const web: any = userStore.getWebConfig;
      const accountType = web?.systenAccountType;
      const formData = reactive({
        phone: '',
        captcha: '',
        account: userStore.getSafeInfo.phone || userStore.getSafeInfo.email,
        email: '',
      });
      const { getFormRules } = useFormRules(formData, accountType);
      const go = useGo();

      const sliderText = ref('向右滑动完成拼图');
      const TabKey = computed(() => LoginStateEnum[unref(getLoginState)]);
      const formRef = ref();
      const smsCount = ref(null);
      const loading = ref(false);
      const slideCodeShow = ref(false);
      const captchaKey = ref('');
      const verifyUsername = ref('');
      const verifyPhone = userStore.getSafeInfo.phone;
      const registerCheck = ref(false);
      const captchaResult = ref({
        falg: false,
        data: {},
      });
      console.log('TabKey', TabKey);
      const disableMobile = computed(() => !!userStore.getSafeInfo.phone);
      console.log(disableMobile.value, userStore.getSafeInfo);
      const { validForm } = useFormValid(formRef);
      async function handleLogin() {
        var validParams = ['captcha'];
        if (unref(TabKey) !== 'LOGIN_VERIFY') {
          validParams.push('account');
        }
        const data = await validForm(validParams);
        if (!data) return;
        if (!captchaKey.value) {
          createMessage.warning('请先获取验证码');
          return;
        }
        const isAgreed = await checkAgreement();
          if (!isAgreed) {
            return;
          }

        let { VITE_GLOB_APP_CODE } = getAppEnvConfig();
        VITE_GLOB_APP_CODE = VITE_GLOB_APP_CODE || 'opensign_0001';

        loading.value = true;
        try {
          const userInfo = await userStore.login({
            phone: formData.phone,
            email: formData.email,
            captcha: formData.captcha,
            captchaKey: unref(captchaKey),
            type: unref(TabKey) == 'EXPERIENCE' ? 'visitor' : 'phone',
            password: unref(TabKey) == 'EXPERIENCE' ? Md5.hashStr('123456') : undefined,
            username: unref(TabKey) === 'LOGIN_VERIFY' ? unref(verifyUsername) : undefined,
            appCode: VITE_GLOB_APP_CODE,
          });
          if (userInfo && userInfo?.user_tenant_depart) {
            if (userInfo?.user_tenant_depart.length > 1) {
              openModal(true, {
                isUpdate: false,
                record: userInfo,
              });
            } else {
              if (userInfo?.user_tenant_depart[0].departs.length > 1) {
                openModal(true, {
                  isUpdate: false,
                  record: userInfo,
                });
              } else {
                const formData = {
                  departId: userInfo?.user_tenant_depart[0].departs[0].departId,
                };
                const result = await userStore.selectTenant(formData);
                if (result) {
                  // notification.success({
                  //   message: '登录成功',
                  //   description: `${'登录成功'}: ${result.username}`,
                  //   duration: 3,
                  // });
                }
              }
            }
          } else {
            go('/dashboard/workbench');
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
        console.log('captchaSuccess', params, formData);
        sliderText.value = '验证成功';
        if (smsCount.value) {
          slideCodeShow.value = false;
          let result;
          // 异地登录带过手机号并且用username
          if (formData.phone) {
            result = await getSmsCode({
              phone: formData.phone,
              type: 'login1',
              captchaVerification: params.captchaVerification,
            });
          } else if (formData.email) {
            result = await getEmailCode({
              email: formData.email,
              type: 'login1',
              captchaVerification: params.captchaVerification,
            });
          }
          if (result) {
            captchaKey.value = result;
            console.log(result, '结果');
            smsCount.value.triggerClick();
          }
        }
      }
      function sendCodeApi() {
        const { validForm } = useFormValid(formRef);
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
      function showPhoneEmail(moduleName) {
        // openPhoneEmailModal(true,{
        //   isUpdate:false,
        //   moduleName
        // })
        if (moduleName === 'RESET_PASSWORD') {
          setLoginState(LoginStateEnum.RESET_PASSWORD);
        } else if (moduleName === 'FIND_ACCOUNT') {
          setLoginState(LoginStateEnum.FIND_ACCOUNT);
        }
      }
      function handlePhoneEmailSuccess(moduleName, methodName) {
        console.log(methodName);
        closePhoneEmailModal();
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
        closeModal();
        formData.captcha = '';
      }
      function handleTerm(type) {
        window.open('/#/terms/service/' + type);
      }
      const verifyRef: any = ref(null);
      const captchaSuccess = (params) => {
        // console.log('captchaSuccess', params, sendBtn.value);
        // captchaResult.value.falg = true;
        // captchaResult.value.data = params;
        // sendBtn.value.handleStart();
        captchaResult.value.falg = true;
        captchaResult.value.data = params;
      };
      const captchaError = (params) => {
        // console.log('captchaError', params);
      };

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
        registerPhoneEmailModal,
        registerModal,
        handleSuccess,
        disableMobile,
        handleFail,
        TabKey,
        formRef,
        verifyPhone,
        accountType,
        handleRegister,
        registerCheck,
        handleTerm,
        captchaSuccess,
        captchaError,
        verifyRef,
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

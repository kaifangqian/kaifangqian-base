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
  <template v-if="getShow">
    <Form class="p-4 " :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="account" class="">
        <Input size="large" v-model:value="formData.account" placeholder="请输入手机号或邮箱">
          <template #prefix>
            <Icon icon="ant-design:mail-outlined" color="#bfbfbf"></Icon>
          </template>
        </Input>
      </FormItem>
      <!-- <FormItem name="phone" class="" v-if="getMobile">
        <Input size="large" v-model:value="formData.phone" placeholder="手机号" >
          <template #prefix>
              <Icon icon="ant-design:tablet-outlined"  color="#bfbfbf"></Icon>
          </template>
        </Input>
      </FormItem> -->
      <FormItem name="newpassword" class="">
        <PasswordTip :password="formData.newpassword" ref="passwordRef">
          <template #passwod>
            <Input
              size="large"
              :type="formData.inputPassword ? 'password' : 'text'"
              visibilityToggle
              v-model:value="formData.newpassword"
              placeholder="密码"
            >
              <template #prefix>
                <Icon icon="ant-design:lock-outlined" color="#bfbfbf"></Icon>
              </template>
              <template #suffix>
                <div class="icon-eye" @click="handleChangePassword('inputPassword')">
                  <Icon
                    icon="ant-design:eye-invisible-outlined"
                    v-if="formData.inputPassword"
                  ></Icon>
                  <Icon icon="ant-design:eye-outlined" v-else></Icon>
                </div>
              </template>
            </Input>
          </template>
        </PasswordTip>
      </FormItem>
      <FormItem name="confirmpassword" class="">
        <PasswordTip :password="formData.confirmpassword">
          <template #passwod>
            <Input
              size="large"
              :type="formData.inputConfirmPassword ? 'password' : 'text'"
              visibilityToggle
              v-model:value="formData.confirmpassword"
              placeholder="确认密码"
            >
              <template #prefix>
                <Icon icon="ant-design:lock-outlined" color="#bfbfbf"></Icon>
              </template>
              <template #suffix>
                <div class="icon-eye" @click="handleChangePassword('inputConfirmPassword')">
                  <Icon
                    icon="ant-design:eye-invisible-outlined"
                    v-if="formData.inputConfirmPassword"
                  ></Icon>
                  <Icon icon="ant-design:eye-outlined" v-else></Icon>
                </div>
              </template>
            </Input>
          </template>
        </PasswordTip>
      </FormItem>
      <FormItem name="captcha" class="">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          ref="smsCount"
          v-model:value="formData.captcha"
          placeholder="验证码"
          :sendCodeApi="(sendCodeApi as any)"
        >
          <template #prefix>
            <Icon icon="ant-design:check-circle-outlined" color="#bfbfbf"></Icon>
          </template>
        </CountdownInput>
      </FormItem>
      <FormItem class="">
        <a-button type="primary" size="large" block @click="handleReset" :loading="loading" style="background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%); border-color: #00000000;">
          重置
        </a-button>
        <a-button size="large" block class="mt-4" @click="handleBackLogin"> 返回 </a-button>
      </FormItem>
    </Form>
    <Verify
      @success="handleSildeSuccess"
      :imgSize="{ width: '330px', height: '155px' }"
      captchaType="blockPuzzle"
      ref="verifyRef"
    />
    <!-- <PuzzleVerification :w="356" @success="handleSildeSuccess" @fail="handleFail" :sliderText="sliderText" v-if="slideCodeShow"/> -->
  </template>
</template>
<script lang="ts" setup>
  import { reactive, ref, computed, unref } from 'vue';
  import { Input, Form } from 'ant-design-vue';
  // import LoginFormTitle from './LoginFormTitle.vue';
  import {
    useLoginState,
    useFormRules,
    LoginStateEnum,
    ResetPassEnum,
    useFormValid,
  } from './useLogin';
  import { CountdownInput } from '/@/components/CountDown';
  import Icon from '/@/components/Icon';
  import PuzzleVerification from '/@/components/verification/index.vue';
  import { updatePassword, getSmsCode, getEmailCode } from '/@/api/sys/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import PasswordTip from '/@/components/PasswordTip/Index.vue';
  import { Md5 } from 'ts-md5';
  import { isMobile, isEmail } from '/@/utils/validate';
  import { Verify } from '/@/components/verifition';
  const { handleBackLogin, getLoginState, getResetPasswordType } = useLoginState();
  const FormItem = Form.Item;
  const verifyRef: any = ref(null);
  const formRef = ref();
  const loading = ref(false);

  const formData = reactive({
    phone: '',
    email: '',
    captcha: '',
    newpassword: '',
    confirmpassword: '',
    inputPassword: true,
    inputConfirmPassword: true,
    account: '',
  });
  const { getFormRules } = useFormRules(formData, 'phone_email');

  const captchaKey = ref('');
  const slideCodeShow = ref(false);
  const sliderText = ref('向右滑动完成拼图');
  const { createMessage } = useMessage();
  const smsCount = ref(null);
  const passwordRef = ref<HTMLDivElement>(null);
  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);
  const getMobile = computed(() => unref(getResetPasswordType) === ResetPassEnum.MOBILE);
  const { validForm } = useFormValid(formRef);

  async function handleReset() {
    const data = await validForm();
    if (!data) return;
    let passLevel = passwordRef.value.getPasswordLevel();
    let params = {
      // phone:formData.phone,
      // email:formData.email,
      newpassword: Md5.hashStr(formData.newpassword),
      confirmpassword: Md5.hashStr(formData.confirmpassword),
      captchaKey: unref(captchaKey),
      captcha: formData.captcha,
      type: isMobile(formData.account) ? 'phone' : 'email',
      passwordLevel: passLevel.level,
    };
    if (isMobile(formData.account)) {
      params.phone = formData.account;
    } else {
      params.email = formData.account;
    }
    let result = await updatePassword(params);
    if (result) {
      createMessage.success(result.message);
      handleBackLogin();
    }
  }
  function handleFail() {
    sliderText.value = '验证失败';
    setTimeout(() => {
      sliderText.value = '向右滑动完成拼图';
    }, 1000);
  }

  function handleChangePassword(passwordKey) {
    formData[passwordKey] = !formData[passwordKey];
  }
  function handleSildeSuccess(params) {
    sliderText.value = '验证成功';
    setTimeout(async () => {
      if (smsCount.value) {
        slideCodeShow.value = false;
        let result;
        if (isMobile(formData.account)) {
          result = await getSmsCode({
            phone: formData.account,
            type: 'editPassword',
            captchaVerification: params.captchaVerification,
          });
        } else if (isEmail(formData.account)) {
          result = await getEmailCode({
            email: formData.account,
            type: 'email',
            captchaVerification: params.captchaVerification,
          });
        }
        if (result) {
          captchaKey.value = result;
          console.log(result, '结果');
        }
        smsCount.value.triggerClick();
      }
    }, 2000);
  }
  function sendCodeApi() {
    return new Promise(async (resolve) => {
      console.log(getMobile.value, '--类型--');
      try {
        const data = await validForm(['account', 'newpassword', 'confirmpassword']);
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
</script>

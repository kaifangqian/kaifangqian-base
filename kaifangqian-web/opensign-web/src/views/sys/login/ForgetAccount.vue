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
            <Icon icon="ant-design:tablet-outlined" color="#bfbfbf"></Icon> </template
        ></Input>
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
          确定
        </a-button>
        <a-button size="large" block class="mt-4" @click="handleBackLogin"> 返回 </a-button>
      </FormItem>
    </Form>
    <!-- <PuzzleVerification
      :w="356"
      @success="handleSildeSuccess"
      @fail="handleFail"
      :sliderText="sliderText"
      v-if="slideCodeShow"
    /> -->
    <Verify
      @success="handleSildeSuccess"
      :imgSize="{ width: '330px', height: '155px' }"
      captchaType="blockPuzzle"
      ref="verifyRef"
    />
  </template>
</template>
<script lang="ts" setup>
  import { reactive, ref, computed, unref, h } from 'vue';
  import { Input, Form } from 'ant-design-vue';
  import {
    useLoginState,
    useFormRules,
    LoginStateEnum,
    ResetPassEnum,
    useFormValid,
  } from './useLogin';
  import { CountdownInput } from '/@/components/CountDown';
  import { getUserName, getSmsCode, getEmailCode } from '/@/api/sys/user';
  import { useMessage } from '/@/hooks/web/useMessage';
  import Icon from '/@/components/Icon';
  import { isMobile, isEmail } from '/@/utils/validate';
  import { Verify } from '/@/components/verifition';

  const { handleBackLogin, getLoginState, getResetPasswordType } = useLoginState();
  const FormItem = Form.Item;

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
  console.log('getFormRules', getFormRules);
  const captchaKey = ref('');
  const slideCodeShow = ref(false);
  const sliderText = ref('向右滑动完成拼图');
  const { createMessage, createConfirm } = useMessage();
  const smsCount = ref(null);
  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.FIND_ACCOUNT);
  const getMobile = computed(() => unref(getResetPasswordType) === ResetPassEnum.MOBILE);

  const { validForm } = useFormValid(formRef);

  async function handleReset() {
    const data = await validForm();
    if (!data) return;
    let params = {
      phone: formData.phone,
      email: formData.email,
      captchaKey: unref(captchaKey),
      captcha: formData.captcha,
      type: isMobile(formData.account) ? 'phone' : 'email',
    };
    if (isMobile(formData.account)) {
      params.phone = formData.account;
    } else {
      params.email = formData.account;
    }

    let result = await getUserName(params);
    if (result) {
      // createMessage.success('操作成功');
      createConfirm({
        iconType: 'success',
        title: () => h('span', '账号找回成功'),
        content: () => h('span', `您的账号为: ${result}, 点击确定跳转至登录页`),
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          handleBackLogin();
        },
      });
    }
  }
  function handleFail() {
    sliderText.value = '验证失败';
    setTimeout(() => {
      sliderText.value = '向右滑动完成拼图';
    }, 1000);
  }
  function handleSildeSuccess(params) {
    sliderText.value = '验证成功';
    setTimeout(async () => {
      if (smsCount.value) {
        slideCodeShow.value = false;
        let result;
        // if(getMobile.value){
        //   result = await getSmsCode({email:formData.email,phone:formData.phone,type:'common'});
        // }else{
        //   result = await getEmailCode({email:formData.email,phone:formData.phone,type:'email'});
        // }
        if (isMobile(formData.account)) {
          result = await getSmsCode({
            phone: formData.account,
            type: 'common',
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
    }, 1000);
  }
  const verifyRef: any = ref(null);
  function sendCodeApi() {
    return new Promise(async (resolve, reject) => {
      try {
        if (isMobile(formData.account)) {
          formData.phone = formData.account;
          console.log('isMobile');
        } else if (isEmail(formData.account)) {
          formData.email = formData.account;
          console.log('isEmail');
        } else {
          await validForm('phone');
          console.log('null------');
        }
        const data = await validForm('account');
        console.log('data:data,', data);
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

<!--
  @description 意愿身份校验

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
  <div class="confirm-info">
    <v-dialog v-model:show="confirmShow" :title="title" :showConfirmButton="false">
      <van-form ref="confirmDataRef">
        <div
          class="confirm-info-item"
          v-if="confirmType == 'phone_email' || confirmType == 'double' || isSetPassword"
          ref="selectRef"
        >
          <div class="van-cell__title van-field__label">
            <label class="confirm-info-item-label">手机号/邮箱</label>
          </div>
          <van-dropdown-menu :overlay="false">
            <van-dropdown-item
              v-model="formData.codeType"
              :options="options"
              style="top: 120px"
              :disabled="isDisabledCodeType"
            />
          </van-dropdown-menu>
        </div>
        <van-field
          v-model="formData.captcha"
          name="password"
          placeholder="短信验证码"
          label="验证码"
          v-if="confirmType == 'phone_email' || confirmType == 'double' || isSetPassword"
        >
          <template #button>
            <div @click="sendPhoneCode">
              <span class="custom-link">{{ countdownText }}</span>
            </div>
          </template>
        </van-field>
        <van-field
          v-if="confirmType == 'double' || confirmType == 'password' || isSetPassword"
          v-model="formData.password"
          type="password"
          label="密码"
          name="password"
          placeholder="请输入签署密码，6位数字"
        ></van-field>
      </van-form>
      <div class="form-footer">
        <div class="sign-password">
          <van-button
            class="text-btn"
            @click="handleChangeType('phone_email')"
            v-if="confirmType == 'password' && !isSetPassword"
            >验证码校验</van-button
          >
          <van-button
            class="text-btn"
            @click="handleSetPassword"
            v-if="(confirmType == 'double' || confirmType == 'password') && !isSetPassword"
            >忘记签署密码？</van-button
          >
          <van-button
            class="text-btn"
            @click="handleChangeType('password')"
            v-if="confirmType == 'phone_email' && !isSetPassword"
            >签署密码校验</van-button
          >
          <!-- <van-button class="text-btn" @click="handleSetPassword" v-if="confirmType=='double'">设置签署密码</van-button> -->
        </div>
        <div class="sign-methods-set" v-if="isSetPassword">
          <span>设置完成后，是否优先使用签署密码</span>
          <van-switch
            class="action-switch"
            size="20px"
            v-model="isPasswordValidate"
            @click="(e) => handleChangeOrder(e, 'password')"
          ></van-switch>
        </div>
        <div class="sign-btn">
          <van-button
            type="primary"
            v-if="confirmType == 'phone_email' && !isSetPassword"
            @click="handleSubmit"
            >确定</van-button
          >
          <van-button
            type="primary"
            v-if="(confirmType == 'double' || confirmType == 'password') && !isSetPassword"
            @click="handleSubmit"
            >提交</van-button
          >
          <van-button type="primary" v-if="isSetPassword" @click="handleSubmitAndBack"
            >保存并返回</van-button
          >
          <van-button type="default" @click="handleCancel">取消</van-button>
        </div>
      </div>
    </v-dialog>
    <Verify
      @success="onCodeSuccess"
      :imgSize="{ width: '300px', height: '155px' }"
      captchaType="blockPuzzle"
      ref="verifyRef"
    />
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, reactive, watch, onActivated } from 'vue';
  import { useUserStore } from '@/store/modules/user';
  import { Md5 } from 'ts-md5';
  import Api from '@/api/contract/index';
  import UserApi from '@/api/user';
  import { Notify, Dialog } from 'vant';
  import { Verify } from '@/components/verifition';

  export default defineComponent({
    name: 'ConfirmModal',
    components: {
      'v-dialog': Dialog.Component,
      Verify,
    },
    props: {
      //   title:{
      //     type:String,
      //     default:'身份校验'
      //   }
    },
    setup(props, { emit }) {
      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo;
      const title = ref('身份校验');
      const countdownText = ref('获取验证码');
      const isSetPassword = ref(false);
      const isPasswordValidate = ref(false);
      const orderNo = ref('');
      const confirmDataRef = ref();
      const isShowCode = ref(false);
      const duration = ref(59);
      const initDuration = 59;
      const isDisabledCodeType = ref(false);
      const selectRef = ref();
      const verifyRef = ref({});
      const signConfig = ref({
        password: '',
      });

      const validatorCaptcha = (val: any) => {
        if (val) {
          return !/^\d{6}$/.test(val) ? '验证码格式不正确' : null;
        } else {
          return '验证码不能为空';
        }
      };

      const confirmType = ref('phone_email');
      const confirmShow = ref(false);
      const options: any = ref([]);
      const interval = ref<any>(null);
      const formData = reactive({
        codeType: '',
        password: '',
        captcha: '',
        captchaKey: '',
      });
      watch(
        () => isSetPassword.value,
        (val) => {
          if (val) {
            title.value = '设置签署密码校验';
          } else {
            title.value = '身份校验';
          }
        }
      );

      onActivated(() => {
        console.log('组件打开额');
      });

      async function showDialog(data: any) {
        const { result, code } = await UserApi.getUserInfo();
        if (code == 200) {
          userStore.setUserInfo(result);
        }
        let resultPassword = await Api.getSignConfig({ type: 'user_sign_password' });
        signConfig.value.password = resultPassword.result.value;
        confirmShow.value = true;
        confirmType.value = data.confirmType;
        orderNo.value = data.orderNo;
        if (userInfo.phone && userInfo.email) {
          options.value = [
            { text: userInfo.phone, value: 'phone' },
            { text: userInfo.email, value: 'email' },
          ];
          formData.codeType = 'phone';
          isDisabledCodeType.value = false;
        } else if (userInfo.phone) {
          options.value = [{ text: userInfo.phone, value: 'phone' }];
          formData.codeType = 'phone';
          isDisabledCodeType.value = true;
        } else if (userInfo.email) {
          options.value = [{ text: userInfo.email, value: 'email' }];
          formData.codeType = 'email';
          isDisabledCodeType.value = true;
        }
      }
      function handleResetFrom() {
        formData.password = '';
        formData.captcha = '';
        formData.captchaKey = '';
        clearTimeout(interval.value);
        duration.value = initDuration;
        countdownText.value = '获取验证码';
      }
      //签署密码校验，有密码进入
      function handleChangeType(type: string) {
        if (type == 'password' && !signConfig.value.password) {
          isSetPassword.value = true;
        } else {
          confirmType.value = type;
        }
        handleResetFrom();
      }
      function handleSetPassword() {
        isSetPassword.value = true;
      }
      function handleChangeOrder(val, type) {
        console.log(val, type);
      }

      async function handleSubmitAndBack() {
        if (!formData.captchaKey) {
          Notify({ type: 'warning', message: '请获取验证码' });
          return false;
        }
        let params = {
          type: formData.codeType,
          password: Md5.hashStr(formData.password),
          captchaKey: formData.captchaKey,
          captcha: formData.captcha,
          confirmType: isPasswordValidate.value ? 'password' : confirmType.value,
        };
        let result = await Api.setSignPassword(params);
        if (result) {
          isSetPassword.value = false;
          if (isPasswordValidate.value) {
            confirmType.value = 'password';
          }
        }
      }

      async function handleSubmit() {
        // const data = await validForm(['codeType','password','captcha']);
        // if(!data) return;
        let response;
        if (confirmType.value == 'password') {
          response = await Api.submitConfirmByPassword({
            confirmType: confirmType.value,
            orderNo: orderNo.value,
            password: Md5.hashStr(formData.password),
          });
        } else {
          if (!formData.captchaKey) {
            Notify({ type: 'warning', message: '请获取验证码' });
            return false;
          }
          response = await Api.submitConfirmByDoubleAnd({
            confirmType: confirmType.value,
            orderNo: orderNo.value,
            captcha: formData.captcha,
            type: formData.codeType,
            password: Md5.hashStr(formData.password),
            captchaKey: formData.captchaKey,
          });
        }
        let { code } = response;
        if (code == 200) {
          confirmShow.value = false;
          setTimeout(() => {
            emit('success', { confirmType: confirmType.value });
          }, 100);
        }
      }
      async function onCodeSuccess(data) {
        confirmShow.value = true;
        if (formData.codeType == 'phone') {
          let { result } = await Api.getCodeBySms({
            orderNo: orderNo.value,
            captchaVerification: data.captchaVerification,
          });
          if (result) {
            formData.captchaKey = result as unknown as string;
            isShowCode.value = false;
          }
        } else {
          let { result } = await Api.getCodeByEmail({
            orderNo: orderNo.value,
            captchaVerification: data.captchaVerification,
          });
          if (result) {
            formData.captchaKey = result as unknown as string;
            isShowCode.value = false;
          }
        }
        delayCountDown();
      }
      function onCodeClose() {}

      async function sendPhoneCode() {
        try {
          if (!formData.codeType) {
            Notify({ type: 'warning', message: '请先选择手机或邮箱' });
            return;
          }
          console.log(duration.value, initDuration);
          //   onCodeSuccess();
          //   isShowCode.value = true;
          if (duration.value === initDuration) {
            confirmShow.value = false;
            verifyRef.value.show();
          }
        } catch (e) {
          console.error('form field error:', e);
        }
      }
      //   function handleSildeSuccess(data){
      //     onCodeSuccess();
      //   }
      function delayCountDown() {
        if (duration.value <= 0) {
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

      function handleCancel() {
        confirmShow.value = false;
        handleResetFrom();
      }

      return {
        confirmShow,
        sendPhoneCode,
        handleSetPassword,
        handleChangeType,
        isPasswordValidate,
        handleChangeOrder,
        showDialog,
        options,
        formData,
        handleSubmit,
        isDisabledCodeType,
        handleSubmitAndBack,
        selectRef,
        confirmType,
        validatorCaptcha,
        countdownText,
        orderNo,
        onCodeSuccess,
        handleCancel,
        onCodeClose,
        isShowCode,
        confirmDataRef,
        isSetPassword,
        duration,
        title,
        verifyRef,
      };
    },
  });
</script>

<style lang="less" scoped>
  .confirm-info {
    :deep(.van-dialog__content) {
      margin: 60px 0;
    }
    .confirm-info-item {
      display: flex;
      align-items: center;
      :deep(.van-dropdown-menu__bar) {
        box-shadow: none;
      }
      :deep(.van-dropdown-menu) {
        width: 100%;
      }
      :deep(.van-dropdown-menu__item) {
        justify-content: space-between;
      }
      :deep(.van-dropdown-menu__title--active) {
        color: #999;
      }
    }
    .confirm-info-item-label {
      font-size: 28px;
      color: #666;
      padding-left: 26px;
    }
  }
  .sign-btn {
    text-align: center;
    margin-top: 80px;
    display: flex;
    flex-direction: column;
    align-items: center;
    :deep(.van-button) {
      width: 240px;
      margin: 20px;
      height: 60px;
    }
  }
  .sign-password {
    padding-left: 20px;
  }

  .sign-methods-set {
    display: flex;
    justify-content: space-between;
    font-size: 24px;
    padding: 10px 30px;
    color: #999;
  }
</style>

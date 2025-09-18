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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
      <Form
        class="enter-x"
        :model="formData"
        :rules="getFormRules"
        ref="formRef"
        :labelCol="{ span: 3 }"
        autocomplete="off"
      >
        <FormItem name="phone" class="enter-x" label="手机号" v-if="methodsType === 'mobile'">
          <Input
            size="large"
            v-model:value="formData.phone"
            placeholder="手机号"
            :disabled="storeUserInfo.phone ? true : false"
            class="fix-auto-fill"
          />
        </FormItem>
        <FormItem name="email" class="enter-x" label="邮箱" v-if="methodsType === 'email'">
          <Input
            size="large"
            placeholder="邮箱"
            v-model:value="formData.email"
            type="text"
            :disabled="storeUserInfo.email ? true : false"
            class="fix-auto-fill"
          />
        </FormItem>
        <FormItem
          name="oldpassword"
          class="enter-x"
          label="原密码"
          v-if="methodsType === 'password'"
        >
          <!-- <PasswordTip :password="formData.oldpassword">
                    <template  #passwod> -->
          <InputPassword
            size="large"
            v-model:value="formData.oldpassword"
            placeholder="原密码"
            class="fix-auto-fill"
          />
          <!-- </template>
                  </PasswordTip> -->
        </FormItem>
        <FormItem name="newpassword" class="enter-x" label="新密码">
          <PasswordTip :password="formData.newpassword">
            <template #passwod>
              <InputPassword
                size="large"
                visibilityToggle
                v-model:value="formData.newpassword"
                placeholder="密码"
              />
            </template>
          </PasswordTip>
        </FormItem>
        <FormItem name="confirmpassword" class="enter-x" label="确认密码">
          <PasswordTip :password="formData.confirmpassword">
            <template #passwod>
              <InputPassword
                size="large"
                visibilityToggle
                v-model:value="formData.confirmpassword"
                placeholder="确认密码"
              />
            </template>
          </PasswordTip>
        </FormItem>
        <FormItem name="captcha" class="enter-x" label="验证码" v-if="methodsType !== 'password'">
          <CountdownInput
            size="large"
            class="fix-auto-fill"
            v-model:value="formData.captcha"
            placeholder="验证码"
            ref="smsCount"
            :sendCodeApi="sendCodeApi"
          />
        </FormItem>
      </Form>
      <!-- <PuzzleVerification
        :w="356"
        :style="positionStyle"
        @success="handleSildeSuccess"
        :sliderText="sliderText"
        @fail="handleFail"
        v-if="slideCodeShow"
      /> -->
      <Verify
        @success="handleSildeSuccess"
        :imgSize="{ width: '330px', height: '155px' }"
        captchaType="blockPuzzle"
        ref="verifyRef"
      />
      <template #insertFooter>
        <a-button type="info" @click="handleReturnBack">返回</a-button>
      </template>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { CountdownInput } from '/@/components/CountDown';
  import { useUserStore } from '/@/store/modules/user';
  import Icon from '/@/components/Icon';
  import PuzzleVerification from '/@/components/verification/index.vue';
  import { Form, Input, InputPassword, Row, Col } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {
    getSmsCode,
    getEmailCode,
    updatePasswordByToken,
    getTokenSms,
    getTokenEmail,
  } from '/@/api/sys/user';
  import PasswordTip from '/@/components/PasswordTip/Index.vue';
  import { CalculateScore } from '/@/components/PasswordTip/util';
  import { Md5 } from 'ts-md5';
  import { validateMobile, validateEmail, validateSmscode } from '/@/utils/rules';
  import { useFormValid } from '/@/views/sys/login/useLogin';
  import { Verify } from '/@/components/verifition';
  import type { RuleObject } from 'ant-design-vue/lib/form/interface';
  import type { Rule } from 'ant-design-vue/es/form';
  export default defineComponent({
    name: 'AnnounceFormModal',
    components: {
      BasicModal,
      CountdownInput,
      PuzzleVerification,
      Form,
      FormItem: Form.Item,
      Input,
      InputPassword,
      Row,
      Col,
      PasswordTip,
      Icon,
      Verify,
    },
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const sliderText = ref('向右滑动完成拼图');
      const smsCount = ref(null);
      const slideCodeShow = ref(false);
      const methodsType = ref('mobile');
      const userStore = useUserStore();
      const formRef = ref();
      const { validForm } = useFormValid(formRef);
      const verifyRef = ref(null);
      const formData = reactive({
        phone: '',
        email: '',
        oldpassword: '',
        newpassword: '',
        confirmpassword: '',
        captcha: '',
        captchaKey: '',
        passwordLevel: 0,
        inputOldPassword: true,
        inputPassword: true,
        inputConfirmPassword: true,
      });

      const positionStyle = {
        left: '50%',
        transform: `translate(${-50 + '%'}, ${-50 + '%'})`,
        top: '50%',
      };
      const storeUserInfo = computed(() => userStore.getUserInfo);
      const validateConfirmPassword = (_rule: Rule, value: string) => {
        if (!value) {
          return Promise.reject('请输入密码');
        }
        if (value !== formData.newpassword) {
          return Promise.reject('两次密码输入不一致');
        }
        return Promise.resolve();
      };
      const validatePassword = (_rule: Rule, value: string) => {
        if (!value) {
          return Promise.reject('请输入密码');
        }
        return Promise.resolve();
      };
      const getFormRules = ref({
        phone: [{ required: true, trigger: 'blur', validator: validateMobile }],
        email: [{ required: true, trigger: 'blur', validator: validateEmail }],
        oldpassword: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        newpassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }],
        confirmpassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }],
        captcha: [{ required: true, trigger: 'blur', validator: validateSmscode }],
      });

      const { createMessage, createWarningModal } = useMessage();
      const [registerModal, { setModalProps, closeModal, changeLoading }] = useModalInner(
        async (data) => {
          setModalProps({ confirmLoading: false, width: 600 });
          isUpdate.value = !!data?.isUpdate;
          methodsType.value = data.record.type;
          resetFormData();
          formData.phone = storeUserInfo.value.phone;
          formData.email = storeUserInfo.value.email;
          console.log(storeUserInfo.value.email, '---用户信息---');
          if (formData.phone) {
            getFormRules.value.phone = [];
          }
          if (formData.email) {
            getFormRules.value.email = [];
          }
          if (unref(isUpdate)) {
            rowId.value = data.record.id;
          }
        },
      );

      const getTitle = computed(() => (!unref(isUpdate) ? '修改密码' : '修改密码'));

      async function handleSubmit() {
        try {
          const data = await validForm();
          if (!data) return;
          setModalProps({ confirmLoading: true });
          let passwordStrength = CalculateScore(formData.newpassword).level;
          let params = {
            phone: formData.phone,
            email: formData.email,
            oldpassword: Md5.hashStr(formData.oldpassword),
            newpassword: Md5.hashStr(formData.newpassword),
            confirmpassword: Md5.hashStr(formData.confirmpassword),
            captchaKey: formData.captchaKey,
            captcha: formData.captcha,
            type:
              methodsType.value == 'mobile'
                ? 'phone'
                : methodsType.value === 'email'
                ? 'email'
                : 'password',
            passwordLevel: passwordStrength,
          };
          let result = await updatePasswordByToken(params);
          if (result) {
            createMessage.success('修改成功');
            closeModal();
            emit('success');
            createWarningModal({
              title: '',
              content: '密码修改成功,需要您重新登录!',
              iconType: 'warning',
              onOk() {
                console.log('去重新登录了');
                userStore.logout(true);
              },
            });
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handleChangePassword(passwordKey) {
        formData[passwordKey] = !formData[passwordKey];
      }
      function handleReturnBack() {
        closeModal();
        emit('back');
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      function resetFormData() {
        formData.phone = '';
        formData.email = '';
        formData.newpassword = '';
        formData.confirmpassword = '';
      }
      function sendCodeApi() {
        return new Promise(async (resolve) => {
          // let data = await validate()
          //type:methodsType.value == 'mobile'?'phone':(methodsType.value === 'email'?'email':'password'),
          try {
            console.log('sendCodeApi', methodsType.value, formData);
            var validateParms = ['newpassword', 'confirmpassword'];
            if (methodsType.value == 'mobile') {
              // createMessage.warning('请输入手机号');
              const data = await formRef.value.validateFields([
                'phone',
                'newpassword',
                'confirmpassword',
              ]);
            }
            if (methodsType.value == 'email') {
              validateParms.push('email');
              const data = await formRef.value.validateFields([
                'newpassword',
                'confirmpassword',
                'email',
              ]);
            }
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
      function handleFail() {
        sliderText.value = '验证失败';
        setTimeout(() => {
          sliderText.value = '向右滑动完成拼图';
        }, 1000);
      }
      async function handleSildeSuccess(params) {
        changeLoading(true);
        sliderText.value = '验证成功';
        // setTimeout(async () => {
        if (smsCount.value) {
          slideCodeShow.value = false;
          let result;
          if (methodsType.value === 'mobile') {
            result = await getTokenSms({
              email: storeUserInfo.value.email ? undefined : formData.email,
              phone: storeUserInfo.value.phone ? undefined : formData.phone,
              type: 'editPassword',
              captchaVerification: params.captchaVerification,
            });
          } else {
            result = await getTokenEmail({
              email: storeUserInfo.value.email ? undefined : formData.email,
              phone: storeUserInfo.value.phone ? undefined : formData.phone,
              type: 'email',
              captchaVerification: params.captchaVerification,
            });
          }
          if (result) {
            formData.captchaKey = result;
            console.log(result, '结果');
            smsCount.value.triggerClick();
          }
          changeLoading(false);
        }
        // }, 2000);
      }

      return {
        handleReturnBack,
        smsCount,
        registerModal,
        formData,
        getFormRules,
        getTitle,
        handleSubmit,
        handleTinymceChange,
        handleSildeSuccess,
        sliderText,
        slideCodeShow,
        sendCodeApi,
        handleFail,
        positionStyle,
        methodsType,
        handleChangePassword,
        formRef,
        storeUserInfo,
        verifyRef,
      };
    },
  });
</script>
<style lang="less" scoped>
  :deep(.ant-input-affix-wrapper .ant-input) {
    // height: 40px !important;
    font-size: 16px !important;
  }
</style>

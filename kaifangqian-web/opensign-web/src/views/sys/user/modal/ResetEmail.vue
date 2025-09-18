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
    <BasicModal
      v-bind="$attrs"
      @register="registerModal"
      :title="getTitle"
      @ok="handleSubmit"
      :destroyOnClose="true"
    >
      <Form
        class="p-2 enter-x"
        :model="formData"
        :rules="getFormRules"
        ref="formRef"
        :labelCol="{ span: 3 }"
      >
        <div v-if="unBindType == 'mobile'">
          <FormItem name="mobile" class="enter-x" label="手机号">
            <Input
              size="large"
              :disabled="formData.disabled"
              v-model:value="formData.phone"
              placeholder="手机号"
              class="fix-auto-fill"
            />
          </FormItem>
          <FormItem name="sms" class="enter-x" label="验证码">
            <CountdownInput
              size="large"
              class="fix-auto-fill"
              v-model:value="formData.sms"
              placeholder="短信验证码"
              ref="smsCount"
              :sendCodeApi="sendCodeApi"
            />
          </FormItem>
        </div>
        <div v-if="unBindType == 'email'">
          <FormItem name="email" class="enter-x" label="邮箱">
            <Input
              size="large"
              :disabled="formData.disabled"
              v-model:value="formData.email"
              placeholder="邮箱"
              class="fix-auto-fill"
            />
          </FormItem>
          <FormItem name="sms" class="enter-x" label="验证码">
            <CountdownInput
              size="large"
              class="fix-auto-fill"
              v-model:value="formData.sms"
              placeholder="验证码"
              ref="smsCount"
              :sendCodeApi="sendCodeApi"
            />
          </FormItem>
        </div>
        <div v-if="unBindType == 'password'">
          <FormItem name="password" class="enter-x" label="密码">
            <Input
              size="large"
              :type="formData.inputPassword ? 'password' : 'text'"
              visibilityToggle
              v-model:value="formData.password"
              placeholder="密码"
            >
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
          </FormItem>
          <FormItem name="confirmPassword" class="enter-x" label="确认密码">
            <Input
              size="large"
              :type="formData.inputConfirmPassword ? 'password' : 'text'"
              visibilityToggle
              v-model:value="formData.confirmPassword"
              placeholder="确认密码"
            >
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
          </FormItem>
        </div>
      </Form>

      <Verify
        @success="handleSildeSuccess"
        :imgSize="{ width: '330px', height: '155px' }"
        captchaType="blockPuzzle"
        ref="verifyRef"
      />
      <template #insertFooter>
        <a-button type="info" @click="handleReturnBack" v-if="isUpdate">返回</a-button>
      </template>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { CountdownInput } from '/@/components/CountDown';
  import {
    getEmailCode,
    unBindEmail,
    bindEmail,
    getTokenEmail,
    getTokenSms,
  } from '/@/api/sys/user';
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { validateSmscode } from '/@/utils/rules';
  import { useFormValid } from '/@/views/sys/login/useLogin';
  import { Md5 } from 'ts-md5';
  import { Icon } from '/@/components/Icon';
  import { isEmail } from '/@/utils/validate';
  import { Verify } from '/@/components/verifition';
  const validateEmail = (_rule: Rule, value: string) => {
    if (!value) {
      return Promise.reject('请输入邮箱');
    }
    if (value && !isEmail(value)) {
      return Promise.reject('邮箱格式不正确');
    }
    return Promise.resolve();
  };

  export default defineComponent({
    name: 'AnnounceFormModal',
    components: {
      BasicModal,
      CountdownInput,
      Form,
      FormItem: Form.Item,
      Input,
      Row,
      Col,
      Icon,
      Verify,
    },
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const sliderText = ref('向右滑动完成拼图');
      const smsCount = ref(null);
      const slideCodeShow = ref(false);
      const { createMessage: msg } = useMessage();
      const unBindType = ref('');
      const verifyRef = ref();
      const formData = reactive({
        email: '',
        sms: '',
        password: '',
        phone: '',
        disabled: false,
        captchaKey: '',
        confirmPassword: '',
        inputPassword: false,
        inputConfirmPassword: false,
      });
      const positionStyle = {
        left: '50%',
        transform: `translate(${-50 + '%'}, ${-50 + '%'})`,
        top: '50%',
      };
      const getFormRules = ref({
        sms: [{ required: true, trigger: 'blur', validator: validateSmscode }],
        email: [{ required: true, trigger: 'blur', validator: validateEmail }],
      });
      const [registerModal, { setModalProps, closeModal, changeLoading }] = useModalInner(
        async (data) => {
          setModalProps({ confirmLoading: false, width: 600 });
          isUpdate.value = !!data?.isUpdate;
          if (unref(isUpdate)) {
            formData.phone = data.record?.phone;
            formData.email = data.record?.email;
            formData.disabled = data.record.email ? true : false;
            formData.sms = '';
            unBindType.value = data.record?.type;
            getFormRules.value.email = [];
          } else {
            formData.phone = '';
            formData.email = '';
            formData.sms = '';
            formData.disabled = false;
            unBindType.value = 'email';
            getFormRules.value.email = [
              { required: true, trigger: 'blur', validator: validateEmail },
            ];
          }
        },
      );
      const formRef = ref();
      const { validForm } = useFormValid(formRef);
      const getTitle = computed(() => (unref(isUpdate) ? '解绑邮箱' : '绑定邮箱'));

      async function handleSubmit() {
        try {
          const data = await validForm();
          if (!data) return;
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let params = {
            email: formData.email,
            captcha: formData.sms,
            phone: formData.phone,
            password: Md5.hashStr(formData.password),
            captchaKey: formData.captchaKey,
            type: unref(unBindType),
          };
          let result: any = {};
          if (unref(isUpdate)) {
            result = await unBindEmail(params);
          } else {
            result = await bindEmail(params);
          }
          if (result) {
            closeModal();
            msg.success('操作成功');
            emit('success');
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function close() {
        slideCodeShow.value = false;
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      function sendCodeApi() {
        return new Promise(async (resolve) => {
          if (!formData.disabled) {
            let data = await validForm('email');
            if (!data.email) {
              msg.warning('邮箱格式不正确');
              return false;
            }
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
        sliderText.value = '验证成功';
        // setTimeout(async () => {
        changeLoading(true);
        if (smsCount.value) {
          let result;
          slideCodeShow.value = false;
          if (unBindType.value == 'email' && unref(isUpdate)) {
            result = await getTokenEmail({
              email: formData.disabled ? undefined : formData.email,
              type: 'mobile_login',
              captchaVerification: params.captchaVerification,
            });
          } else if (unBindType.value == 'mobile' && unref(isUpdate)) {
            result = await getTokenSms({
              phone: formData.disabled ? undefined : formData.phone,
              type: 'common',
              captchaVerification: params.captchaVerification,
            });
          } else {
            //绑定邮箱
            result = await getEmailCode({
              email: formData.email,
              type: 'mobile_login',
              captchaVerification: params.captchaVerification,
            });
          }
          if (result) {
            formData.captchaKey = result;
            smsCount.value.triggerClick();
          }
          changeLoading(false);
        }
        // }, 500);
      }
      function handleReturnBack() {
        closeModal();
        emit('back');
      }

      function handleChangePassword(passwordKey) {
        formData[passwordKey] = !formData[passwordKey];
      }

      return {
        unBindType,
        handleReturnBack,
        smsCount,
        formRef,
        registerModal,
        positionStyle,
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
        isUpdate,
        handleChangePassword,
        close,
        verifyRef,
      };
    },
  });
</script>
<style></style>

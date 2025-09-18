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
  import PuzzleVerification from '/@/components/verification/index.vue';
  import PasswordTip from '/@/components/PasswordTip/Index.vue';
  import Icon from '/@/components/Icon';
  import {
    getSmsCode,
    unBindTelephone,
    bindTelephone,
    getTokenSms,
    getTokenEmail,
  } from '/@/api/sys/user';
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { validateSmscode, validateEmail } from '/@/utils/rules';
  import { useFormValid } from '/@/views/sys/login/useLogin';
  import { Md5 } from 'ts-md5';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Verify } from '/@/components/verifition';
  export default defineComponent({
    name: 'AnnounceFormModal',
    components: {
      BasicModal,
      PasswordTip,
      CountdownInput,
      PuzzleVerification,
      Form,
      FormItem: Form.Item,
      Input,
      Icon,
      Row,
      Col,
      Verify,
    },
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const sliderText = ref('向右滑动完成拼图');
      const smsCount = ref(null);
      const slideCodeShow = ref(false);
      const unBindType = ref('');
      const verifyRef = ref();
      const formData = reactive({
        phone: '',
        sms: '',
        disabled: false,
        captchaKey: '',
        email: '',
        password: '',
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
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        confirmPassword: [{ required: true, trigger: 'blur', message: '请确认密码' }],
      });
      const formRef = ref();
      const { createMessage: msg, createConfirm } = useMessage();
      const { validForm } = useFormValid(formRef);
      const [registerModal, { setModalProps, closeModal, changeLoading }] = useModalInner(
        async (data) => {
          setModalProps({ confirmLoading: false, width: 555 });
          isUpdate.value = !!data?.isUpdate;
          console.log(data.record, '---------');
          if (unref(isUpdate)) {
            formData.phone = data.record?.phone;
            formData.email = data.record?.email;
            formData.sms = '';
            formData.disabled = true;
            unBindType.value = data.record?.type;
          } else {
            formData.phone = '';
            formData.disabled = false;
            formData.sms = '';
            unBindType.value = 'mobile';
          }
        },
      );

      const getTitle = computed(() => (unref(isUpdate) ? '解绑手机号' : '绑定手机号'));

      async function handleSubmit() {
        try {
          const data = await validForm();
          if (!data) return;
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let params = {
            phone: formData.phone,
            email: formData.email,
            password: Md5.hashStr(formData.password),
            captcha: formData.sms,
            captchaKey: formData.captchaKey,
            type: unref(unBindType),
          };
          let result: any = {};
          if (unref(isUpdate)) {
            result = await unBindTelephone(params);
          } else {
            result = await bindTelephone(params);
          }
          if (result) {
            handleResetForm();
            closeModal();
            emit('success');
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      function handleChangePassword(passwordKey) {
        formData[passwordKey] = !formData[passwordKey];
      }
      function sendCodeApi() {
        return new Promise(async (resolve) => {
          // let data = await validate()
          if (!formData.phone) {
            msg.warning('手机号不能为空');
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
        // setTimeout(async ()=>{
        if (smsCount.value) {
          let result;
          slideCodeShow.value = false;
          if (unBindType.value == 'email' && unref(isUpdate)) {
            result = await getTokenEmail({
              email: formData.disabled ? '' : formData.email,
              type: 'mobile_login',
              captchaVerification: params.captchaVerification,
            });
          } else if (unBindType.value == 'mobile' && unref(isUpdate)) {
            result = await getTokenSms({
              phone: formData.disabled ? '' : formData.phone,
              type: 'common',
              captchaVerification: params.captchaVerification,
            });
          } else {
            result = await getSmsCode({
              phone: formData.phone,
              type: 'common',
              captchaVerification: params.captchaVerification,
            });
          }
          if (result) {
            formData.captchaKey = result;
            smsCount.value.triggerClick();
          }
          changeLoading(false);
        }
        // },2000)
      }

      function handleReturnBack() {
        closeModal();
        emit('back');
      }

      function handleResetForm() {
        formData.phone = '';
        formData.disabled = false;
        formData.sms = '';
        formData.password = '';
        formData.email = '';
        formData.captchaKey = '';
        unBindType.value = 'mobile';
      }
      function close() {
        slideCodeShow.value = false;
      }

      return {
        handleReturnBack,
        smsCount,
        handleChangePassword,
        unBindType,
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
        close,
        verifyRef,
      };
    },
  });
</script>
<style></style>

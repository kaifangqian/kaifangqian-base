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
      :destroyOnClose="true"
      :maskClosable="false"
      wrapClassName="wish-modal"
      :afterClose="modalAfterClose"
    >
      <Form
        class="p-3 enter-x"
        :model="formData"
        :rules="getFormRules"
        ref="formRef"
        :labelCol="{ span: 4 }"
      >
        <FormItem
          name="type"
          class="enter-x"
          label=""
          v-if="confirmType == 'phone_email' || confirmType == 'double' || isSetPassword"
        >
          <Select
            v-model:value="formData.type"
            placeholder="请选择手机号或邮箱"
            class="fix-auto-fill"
            :options="accountList"
            :disabled="isDisabledCodeType"
          ></Select>
        </FormItem>
        <FormItem
          name="captcha"
          class="enter-x"
          label=""
          v-if="confirmType == 'phone_email' || confirmType == 'double' || isSetPassword"
        >
          <CountdownInput
            size="small"
            class="fix-auto-fill"
            v-model:value="formData.captcha"
            placeholder="验证码"
            ref="smsCount"
            :sendCodeApi="(sendCodeApi as any)"
          />
        </FormItem>
        <FormItem
          name="password"
          class="enter-x"
          label=""
          v-if="confirmType == 'double' || confirmType == 'password' || isSetPassword"
        >
          <Input
            v-model:value="formData.password"
            type="password"
            placeholder="请输入签署密码，6位数字"
            size="small"
            :maxlength="6"
            autocomplete="new-password"
          />
        </FormItem>
      </Form>
      <div class="form-footer">
        <div class="sign-password">
          <a-button
            type="link"
            @click="handleChangeType('phone_email')"
            v-if="confirmType == 'password' && !isSetPassword"
            >验证码校验</a-button
          >
          <a-button
            type="link"
            v-if="(confirmType == 'double' || confirmType == 'password') && !isSetPassword"
            @click="handleSetPassword"
            >忘记签署密码？</a-button
          >
          <a-button
            type="link"
            @click="handleChangeType('password')"
            v-if="confirmType == 'phone_email' && !isSetPassword"
            >签署密码校验</a-button
          >
          <!-- <a-button type="link" @click="handleSetPassword" v-if="confirmType=='double' && !isSetPassword">设置签署密码</a-button> -->
        </div>
        <div class="sign-methods-set" v-if="isSetPassword">
          <span>设置完成后，是否优先使用签署密码</span>
          <a-switch
            class="action-switch"
            v-model:checked="isPasswordValidate"
            @click="(e) => handleChangeOrder(e, 'password')"
          ></a-switch>
        </div>
        <div class="sign-btn">
          <a-button
            type="primary"
            v-if="confirmType == 'phone_email' && !isSetPassword"
            @click="handleSubmit"
            >确定</a-button
          >
          <a-button
            type="primary"
            v-if="(confirmType == 'double' || confirmType == 'password') && !isSetPassword"
            @click="handleSubmit"
            >提交</a-button
          >
          <a-button type="primary" v-if="isSetPassword" @click="handleSubmitAndBack"
            >保存并返回</a-button
          >
          <a-button type="default" v-if="isSetPassword" @click="isSetPassword = false"
            >取消</a-button
          >
        </div>
      </div>
      <Verify
        @success="handleSildeSuccess"
        :imgSize="{ width: '330px', height: '155px' }"
        captchaType="blockPuzzle"
        ref="verifyRef"
      />
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed, reactive, watch } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { getTokenSms, getTokenEmail } from '/@/api/sys/user';
  import { Icon } from '/@/components/Icon';
  import { CountdownInput } from '/@/components/CountDown';
  import { Form, Input, Row, Col, Select } from 'ant-design-vue';
  import { useUserStore } from '/@/store/modules/user';
  import { setSignPassword, getSignConfig } from '/@/api/sys/user';
  import { Md5 } from 'ts-md5';
  import { validateSmscode } from '/@/utils/rules';
  import { useFormValid } from '/@/views/sys/login/useLogin';
  import { saveSealAuth } from '../api/signatureAuth';
  import { Verify } from '/@/components/verifition';
  export default defineComponent({
    name: 'WishModal',
    components: {
      BasicModal,
      Verify,
      CountdownInput,
      Form,
      Icon,
      FormItem: Form.Item,
      Input,
      Row,
      Col,
      Select,
    },
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const sealList: any = ref([]);
      const confirmType = ref('phone_email');
      const isPasswordValidate = ref(false);
      const isSetPassword = ref(false);
      const orderNo = ref('');
      const isDisabledCodeType = ref(false);
      const signConfig = ref({
        password: '',
      });
      const verifyRef = ref();

      const sliderText = ref('向右滑动完成拼图');
      const smsCount = ref(null);
      const slideCodeShow = ref(false);
      const userStore = useUserStore();
      const storeUserInfo: any = computed(() => userStore.getUserInfo);
      const formRef = ref();
      const submitData = ref({});
      const { validForm } = useFormValid(formRef);

      const formData = reactive({
        type: '',
        password: '',
        captcha: '',
        captchaKey: '',
      });
      const accountList: any = ref([]);

      const positionStyle = {
        left: '50%',
        transform: `translate(${-50 + '%'}, ${-50 + '%'})`,
        top: '50%',
      };

      const getFormRules = ref({
        type: [{ required: true, trigger: 'blur', message: '请选择手机或邮箱' }],
        password: [{ required: true, trigger: 'blur', message: '请输入新密码' }],
        captcha: [{ required: true, trigger: 'blur', validator: validateSmscode }],
      });

      const { createMessage: msg } = useMessage();

      const [registerModal, { setModalProps, closeModal, changeLoading }] = useModalInner(
        async (data) => {
          setModalProps({
            confirmLoading: false,
            width: 400,
            height: 550,
            cancelText: '关闭',
            footer: null,
            centered: true,
          });
          handleResetFrom();
          confirmType.value = data.record.confirmType;
          submitData.value = data.record.fromData;
          let signPassword = await getSignConfig({ type: 'user_sign_password' });
          signConfig.value.password = signPassword.value;
          if (storeUserInfo.value.email && storeUserInfo.value.phone) {
            accountList.value = [
              { value: 'phone', label: storeUserInfo.value.phone },
              { value: 'email', label: storeUserInfo.value.email },
            ];
            formData.type = 'phone';
            isDisabledCodeType.value = false;
            return;
          }
          if (storeUserInfo.value.phone) {
            accountList.value = [{ value: 'phone', label: storeUserInfo.value.phone }];
            formData.type = 'phone';
            isDisabledCodeType.value = true;
            return;
          }

          if (storeUserInfo.value.email) {
            accountList.value = [{ value: 'email', label: storeUserInfo.value.email }];
            formData.type = 'email';
            isDisabledCodeType.value = true;
            return;
          }
        },
      );
      const getTitle = ref('身份校验');

      watch(
        () => isSetPassword.value,
        (val) => {
          if (val) {
            getTitle.value = '设置签署密码校验';
          } else {
            getTitle.value = '身份校验';
          }
        },
      );

      function handleResetFrom() {
        formData.password = '';
        formData.captcha = '';
        formData.captchaKey = '';
      }
      async function handleSubmit() {
        if (!formData.captchaKey && confirmType.value != 'password') {
          msg.warning('请获取验证码');
          return false;
        }
        const data = await validForm(['type', 'password', 'captcha']);
        if (!data) return;
        try {
          const result = await saveSealAuth({
            ...submitData.value,
            ...data,
            captchaKey: formData.captchaKey,
            confirmType: confirmType.value,
            password: Md5.hashStr(formData.password),
          });
          if (result.code == 200) {
            msg.success('授权添加成功');
            closeModal();
            emit('success', true);
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      function handleUse(row) {
        msg.success('操作成功');
        emit('success', row);
      }

      function sendCodeApi() {
        return new Promise(async (resolve) => {
          // let data = await validate()
          if (!formData.type) {
            msg.warning('请选择手机号或邮箱');
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

      async function handleSildeSuccess(params) {
        changeLoading(true);
        sliderText.value = '验证成功';
        if (smsCount.value) {
          slideCodeShow.value = false;
          let result;
          if (formData.type == 'phone') {
            result = await getTokenSms({
              type: 'msg',
              captchaVerification: params.captchaVerification,
            });
          } else {
            result = await getTokenEmail({
              type: 'msg',
              captchaVerification: params.captchaVerification,
            });
          }
          if (result) {
            formData.captchaKey = result;
            console.log(result, '结果');
            smsCount.value.triggerClick();
          }
        }
        changeLoading(false);
      }

      function handleChangePassword(passwordKey) {
        formData[passwordKey] = !formData[passwordKey];
      }
      function handleSetPassword() {
        isSetPassword.value = true;
        handleResetFrom();
      }
      function handleChangeOrder(val, type) {}

      function handleFail() {
        sliderText.value = '验证失败';
        setTimeout(() => {
          sliderText.value = '向右滑动完成拼图';
        }, 1000);
      }
      //签署密码校验，有密码进入, 无密码进入设置密码弹框
      function handleChangeType(type) {
        if (type == 'password' && !signConfig.value.password) {
          isSetPassword.value = true;
        } else {
          confirmType.value = type;
        }
        handleResetFrom();
      }

      async function handleSubmitAndBack() {
        const data = await validForm(['codeType', 'password', 'captcha']);
        if (!data) return;
        if (!formData.captchaKey) {
          msg.warning('请获取验证码');
          return false;
        }
        setModalProps({ confirmLoading: true });
        let params = {
          type: formData.type,
          password: Md5.hashStr(formData.password),
          captchaKey: formData.captchaKey,
          captcha: formData.captcha,
          confirmType: isPasswordValidate.value ? 'password' : '', //开关不开情况下传空 不更改原有校验顺序
        };
        let result = await setSignPassword(params);
        if (result) {
          msg.success('操作成功');
          isSetPassword.value = false;
          if (isPasswordValidate.value) {
            confirmType.value = 'password';
          }
        }
      }
      function modalAfterClose() {
        emit('success', false);
      }
      return {
        registerModal,
        handleChangePassword,
        getTitle,
        handleFail,
        handleSubmit,
        sealList,
        isSetPassword,
        handleSildeSuccess,
        handleUse,
        storeUserInfo,
        formData,
        sendCodeApi,
        handleChangeType,
        slideCodeShow,
        sliderText,
        getFormRules,
        formRef,
        positionStyle,
        accountList,
        isDisabledCodeType,
        smsCount,
        confirmType,
        handleSetPassword,
        isPasswordValidate,
        handleChangeOrder,
        modalAfterClose,
        handleSubmitAndBack,
        verifyRef,
      };
    },
  });
</script>
<style lang="less" scoped>
  .sign-methods-set {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
  }
  .sign-btn {
    text-align: center;
    margin-top: 40px;
    display: flex;
    flex-direction: column;
    align-items: center;
    :deep(.ant-btn) {
      width: 140px;
      margin: 10px;
    }
  }
  .sign-password {
    // padding-left:40px;
    :deep(.ant-btn-link):first-child {
      padding-left: 0;
    }
  }
</style>

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
      wrapClassName="wish-modal"
      :destroyOnClose="true"
    >
      <Form
        class="p-3 enter-x"
        :model="formData"
        :rules="getFormRules"
        ref="formRef"
        :labelCol="{ span: 4 }"
      >
        <FormItem name="codeType" class="enter-x" label="">
          <Select
            size="large"
            v-model:value="formData.codeType"
            placeholder="手机号或邮箱"
            class="fix-auto-fill"
            :options="changeTypeList"
            :disabled="isDisabledCodeType"
          ></Select>
        </FormItem>
        <FormItem name="captcha" class="enter-x" label="" :autoLink="false">
          <CountdownInput
            size="large"
            class="fix-auto-fill"
            v-model:value="formData.captcha"
            placeholder="验证码"
            ref="smsCount"
            :sendCodeApi="(sendCodeApi as any)"
          />
        </FormItem>
        <FormItem name="password" class="enter-x" label="">
          <Input
            size="large"
            v-model:value="formData.password"
            type="password"
            placeholder="请输入签署密码，6位数字"
            :maxlength="6"
            autocomplete="new-password"
          />
        </FormItem>
      </Form>
      <!-- <PuzzleVerification :w="356" :style="positionStyle"  @success="handleSildeSuccess" :sliderText="sliderText" @fail="handleFail" @close="slideCodeShow=false" v-if="slideCodeShow"/> -->
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
  import type { SelectProps } from 'ant-design-vue';
  import { defineComponent, ref, unref, computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { CountdownInput } from '/@/components/CountDown';
  import { useUserStore } from '/@/store/modules/user';
  import Icon from '/@/components/Icon';
  import PuzzleVerification from '/@/components/verification/index.vue';
  import { Form, Input, Row, Col, Select } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getTokenSms, getTokenEmail, setSignPassword } from '/@/api/sys/user';
  import PasswordTip from '/@/components/PasswordTip/Index.vue';
  import { Md5 } from 'ts-md5';
  import { validateSmscode, validateSignPssword } from '/@/utils/rules';
  import { useFormValid } from '/@/views/sys/login/useLogin';
  import { Verify } from '/@/components/verifition';
  export default defineComponent({
    name: 'SignPassword',
    components: {
      BasicModal,
      CountdownInput,
      PuzzleVerification,
      Form,
      FormItem: Form.Item,
      Input,
      Row,
      Col,
      PasswordTip,
      Icon,
      Select,
      Verify,
    },
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const sliderText = ref('向右滑动完成拼图');
      const smsCount = ref(null);
      const slideCodeShow = ref(false);
      const changeTypeList = ref<SelectProps['options']>([]);
      const userStore = useUserStore();
      const formRef = ref();
      const needChangeConfirmType = ref(false);
      const { validForm } = useFormValid(formRef);
      const isDisabledCodeType = ref(false);
      const verifyRef = ref();
      const formData = reactive({
        codeType: '',
        password: '',
        captcha: '',
        captchaKey: '',
        inputPassword: true,
      });

      const positionStyle = {
        left: '50%',
        transform: `translate(${-50 + '%'}, ${-50 + '%'})`,
        top: '50%',
      };
      const storeUserInfo = computed(() => userStore.getUserInfo);
      const getFormRules = ref({
        codeType: [{ required: true, trigger: 'blur', message: '请选择手机号或邮箱' }],
        password: [{ required: true, trigger: 'blur', validator: validateSignPssword }],
        captcha: [{ required: true, trigger: 'blur', validator: validateSmscode }],
      });
      const { createMessage } = useMessage();
      const [registerModal, { setModalProps, closeModal, changeLoading }] = useModalInner(
        async (data) => {
          setModalProps({ confirmLoading: false, width: 400 });
          isUpdate.value = !!data?.isUpdate;
          needChangeConfirmType.value = data.record.needChangeConfirmType;
          if (unref(isUpdate)) {
            rowId.value = data.record.id;
          }
          //先手动清除
          setTimeout(() => {
            formData.captcha = '';
            formData.password = '';
          }, 1000);

          if (storeUserInfo.value.email && storeUserInfo.value.phone) {
            changeTypeList.value = [
              { value: 'phone', label: storeUserInfo.value.phone },
              { value: 'email', label: storeUserInfo.value.email },
            ];
            formData.codeType = 'phone';
            isDisabledCodeType.value = false;
            return;
          }
          if (storeUserInfo.value.phone) {
            changeTypeList.value = [{ value: 'phone', label: storeUserInfo.value.phone }];
            formData.codeType = 'phone';
            isDisabledCodeType.value = true;
            return;
          }
          if (storeUserInfo.value.email) {
            changeTypeList.value = [{ value: 'email', label: storeUserInfo.value.email }];
            formData.codeType = 'email';
            isDisabledCodeType.value = true;
            return;
          }
        },
      );

      const getTitle = computed(() => (!unref(isUpdate) ? '设置签署密码' : '修改签署密码'));

      async function handleSubmit() {
        try {
          const data = await validForm(['codeType', 'password', 'captcha']);
          if (!data) return;
          setModalProps({ confirmLoading: true });
          let params = {
            type: formData.codeType,
            password: Md5.hashStr(formData.password),
            captchaKey: formData.captchaKey,
            captcha: formData.captcha,
            confirmType: 'phone_email',
          };
          let result = await setSignPassword(params);
          if (result) {
            createMessage.success('操作成功');
            closeModal();
            emit('success', needChangeConfirmType.value);
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
      function sendCodeApi() {
        return new Promise(async (resolve) => {
          if (!formData.codeType) {
            createMessage.warning('请选择手机号或邮箱');
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
          if (formData.codeType === 'phone') {
            result = await getTokenSms({
              email: storeUserInfo.value.email ? undefined : formData.codeType,
              phone: storeUserInfo.value.phone ? undefined : formData.codeType,
              type: 'editPassword',
              captchaVerification: params.captchaVerification,
            });
          } else {
            result = await getTokenEmail({
              email: storeUserInfo.value.email ? undefined : formData.codeType,
              phone: storeUserInfo.value.phone ? undefined : formData.codeType,
              type: 'email',
              captchaVerification: params.captchaVerification,
            });
          }
          if (result) {
            formData.captchaKey = result;
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
        isDisabledCodeType,
        slideCodeShow,
        sendCodeApi,
        handleFail,
        positionStyle,
        changeTypeList,
        handleChangePassword,
        formRef,
        storeUserInfo,
        verifyRef,
      };
    },
  });
</script>
<style lang="less" scoped>
  #form_item_password,
  #form_item_captcha {
    padding: 6px 11px;
  }
</style>

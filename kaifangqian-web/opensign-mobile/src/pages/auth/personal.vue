<!--
  @description 个人认证

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
  <div class="personal">
    <van-form @submit="onSubmit" ref="authForm">
      <van-cell-group inset>
        <van-field
          v-model="formData.name"
          name="name"
          label="姓名"
          placeholder="请输入姓名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="formData.organizationNo"
          type="text"
          name="idCard"
          label="身份证号"
          placeholder="请输入身份证号"
          :rules="[{ validator: validateIdNumber }]"
        />
        <!-- <van-field name="uploader" label="身份证人像面" :rules="[{ validator: validatorIdCardFace }]">
                    <template #input>
                        <van-uploader :max-count="1" :after-read="afterReadFace" v-model="upload.idCardFaces">
                            <div class="upload-image-style">
                                <img src="../../assets/images/card-front.png" />
                            </div>
                        </van-uploader>
                    </template>
                </van-field>
                <van-field name="uploader" label="身份证国徽面" :rules="[{ validator: validatorIdCardEmblems }]">
                    <template #input>
                        <van-uploader :max-count="1" :after-read="afterReadEmblem" v-model="upload.idCardEmblems">
                            <div class="upload-image-style">
                                <img src="../../assets/images/card-back.png" />
                            </div>
                        </van-uploader>
                    </template>
                </van-field> -->
        <van-field
          v-model="formData.phone"
          type="text"
          name="phone"
          label="手机号"
          placeholder="请输入手机号"
          :rules="[{ validator: validatePhone }]"
          v-if="authType == 'default'"
        />
        <van-field
          v-model="formData.captcha"
          name="password"
          placeholder="短信验证码"
          clearable
          label="验证码"
          :rules="[{ validator: validateSms }]"
          v-if="authType == 'default'"
        >
          <template #button>
            <div @click="sendPhoneCode">
              <span class="custom-link" :class="duration != initDuration ? 'disabled' : ''">{{
                countdownText
              }}</span>
            </div>
          </template>
        </van-field>
      </van-cell-group>
      <div style="margin: 16px">
        <van-button round block type="primary" native-type="submit"> 提交 </van-button>
      </div>
      <div class="face-auth" v-if="authTypeVisible">
        <span v-if="authType == 'default'" @click="authType = 'face'">人脸识别认证</span>
        <span v-if="authType == 'face'" @click="authType = 'default'">手机号三要素</span>
      </div>
    </van-form>
    <Verify
      @success="onCodeSuccess"
      :imgSize="{ width: '300px', height: '155px' }"
      captchaType="blockPuzzle"
      ref="verifyRef"
    />
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, onMounted, onBeforeUnmount } from 'vue';
  import { FieldType, Notify, Dialog } from 'vant';
  import { useUserStore } from '@/store/modules/user';
  import Api from '@/api/user';
  import { uploadFile } from '@/api';
  import { useRoute, useRouter } from 'vue-router';
  import http from '@/utils/http';
  import watermark from '@/utils/lib/watermark';
  import copyPaste from '@/utils/lib/copy-paste';
  import { useLoading } from '@/hooks';
  import { Verify } from '@/components/verifition';
  import {
    validateOrganizationCode,
    validateIdNumber,
    validatePhone,
    validateSms,
  } from './formValidate';
  const authTypeVisible = ref(false);
  const router = useRouter();
  const route = useRoute();
  const userStore = useUserStore();
  const { startLoading, stopLoading } = useLoading();
  const tenantInfo = ref(userStore.getTenantInfo);
  const callQuery = ref<any>({
    signRuId: null,
    taskId: null,
  });
  const authType = ref('default');
  const verifyRef = ref({});

  onMounted(() => {
    const authStatus = route.query.authStatus;
    callQuery.value.signRuId = route.query.signRuId;
    callQuery.value.taskId = route.query.taskId;

    if (authStatus && authStatus != 2) {
      return;
    }
    if (userStore.getTenantInfo.authStatus == 2) {
      // router.push("/personal/details");
    }

    Api.personAuthType().then((response) => {
      if (response && response.result == 1) {
        authTypeVisible.value = true;
      }
    });
  });

  const authForm = ref();
  const upload = ref({
    idCardFaces: [],
    idCardEmblems: [],
  });
  const formData = ref({
    name: '',
    idCardFace: '',
    idCardEmblem: '',
    organizationNo: '',
    phone: '',
    captcha: '',
    captchaKey: '',
    authType: 0,
    redirectUrl: '',
  });
  const afterReadEmblem = async (file: any) => {
    const params = {
      dataCategory: 'idpic2',
    };
    const response = await uploadFile(file.file, params);
    formData.value.idCardEmblem = response.result;
  };

  const afterReadFace = async (file: any) => {
    const params = {
      dataCategory: 'idpic1',
    };
    const response = await uploadFile(file.file, params);
    formData.value.idCardFace = response.result;
  };

  async function onSubmit() {
    if (authType.value == 'face') {
      gotoFace();
      return;
    }
    if (!formData.value.captchaKey) {
      Notify({ type: 'warning', message: '请先获取验证码', duration: 1000 });
      return;
    }
    try {
      await authForm.value.validate();
      startLoading();
      const result = await Api.personAuth(formData.value);
      if (result.code == 200) {
        userStore.afterLoginAction();
        userStore.getTnantInfo();
        Notify({ type: 'success', message: '实名认证成功，3s后关闭', duration: 3000 });
        setTimeout(function () {
          stopLoading();
          callBackSource();
        }, 3000);
      } else {
        stopLoading();
      }
    } catch (e) {
      console.error('form field error:', e);
      stopLoading();
    }
  }
  async function gotoFace() {
    startLoading();
    formData.value.authType = 1;
    //signRuId=8ae1f0ff-2168-4ff5-8fbb-7cbaa642a38b&taskId=f3e1e6d6-0c33-4655-b16d-3a2d9f4fad1d
    formData.value.redirectUrl = `${window.location.origin}${window.location.pathname}#/check/face?signRuId=${callQuery.value.signRuId}&taskId=${callQuery.value.taskId}`;
    const response = await Api.personAuth({
      ...formData.value,
      ...callQuery.value,
    });
    if (response.result) {
      window.open(response.result.url, '_self');
      stopLoading();
      // window.open(faceResult.result, '_self');
    } else {
      Notify({ type: 'success', message: '实名认证成功，3s后关闭', duration: 3000 });
      setTimeout(function () {
        stopLoading();
        callBackSource();
      }, 3000);
    }
    console.log('result', response.result.url);
  }
  function callBackSource() {
    if (callQuery.value.signRuId) {
      router.push({
        path: 'signContract',
        query: callQuery.value,
      });
    } else {
      router.push('/home');
    }
  }
  const validatorIdCardFace = (val: any) => {
    if (upload.value.idCardFaces.length == 0) {
      return '身份证人像面必须上传';
    }
  };
  const validatorIdCardEmblems = (val: any) => {
    if (upload.value.idCardFaces.length == 0) {
      return '身份证国徽面必须上传';
    }
  };
  async function onCodeSuccess(data: any) {
    if (duration.value === initDuration) {
      duration.value = initDuration;
      const params = {
        phone: formData.value.phone,
        type: 'login1',
        captchaVerification: data.captchaVerification,
      };
      const response = await Api.sendCode(params);
      if (response.code == 200) {
        formData.value.captchaKey = response.result;
        console.log('send res:', response);
        Notify({ type: 'success', message: '短信发送成功', duration: 1000 });
        delayCountDown();
      } else {
        Notify({ type: 'warning', message: '获取短信验证码失败', duration: 1000 });
      }
    } else {
      return;
    }
  }
  async function sendPhoneCode() {
    try {
      await authForm.value.validate('phone');
      if (duration.value === initDuration) {
        verifyRef.value.show();
      }
    } catch (e) {
      console.error('form field error:', e);
    }
  }
  const initDuration = 59;
  const duration = ref(59);
  const countdownText = ref('获取验证码');
  const interval = ref<any>(null);
  function delayCountDown() {
    if (duration.value == 0) {
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
</script>

<style lang="less" scoped>
  .personal {
    .van-cell-group--inset {
      margin: 0;
    }
  }

  .upload-image-style {
    width: 4rem;

    img {
      width: 100%;
    }
  }

  // :deep(.van-cell-group--inset){
  // 	margin 0 0;
  // }
  .face-auth {
    padding: 20px 0;
    span {
      color: var(--van-blue);
    }
  }
</style>

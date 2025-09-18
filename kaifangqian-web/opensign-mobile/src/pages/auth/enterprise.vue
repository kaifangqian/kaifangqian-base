<!--
  @description 企业认证

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
  <van-overlay :show="loading" class="base-mask">
    <van-loading class="base-loading" v-if="loading" size="24px" color="#1989fa"
      >上传中...</van-loading
    >
  </van-overlay>
  <div class="auth-step form-auth-container">
    <div class="step-1" v-if="authStep == 1">
      <!-- <SvgIcon name="auth-allot" size="70" class="auth-icon"/> -->
      <div class="auth-tip-container">
        <p class="tip-title" style="padding-top: 0.5rem">您正在进行企业实名认证</p>
        <p class="tip-title" style="padding-bottom: 0.5rem">认证过程中将收集您以下信息：</p>
        <div class="tip-info">
          <p>* 法人姓名、证件号、社会信用统一代码</p>
          <p>* 企业工商营业执照</p>
          <p>* 法人身份证正反面照片</p>
          <p>* 手机号</p>
          <p
            >* 企业授权确认书(需加盖企业公章)<a
              href="javascript:;"
              @click="download({ realName: '企业授权确认书.docx' }, false)"
              >下载模板</a
            >
          </p>
        </div>
        <div class="protocol-info">
          <van-space>
            <van-checkbox v-model="protocolChecked" hape="square"></van-checkbox>
            <p
              >我已阅读并同意《<a href="javascript:;" @click="handleTerm('privacy')">隐私协议</a
              >》《<a href="javascript:;" @click="handleTerm('certificate')">数字证书使用协议</a
              >》</p
            >
          </van-space>
        </div>
        <van-button
          type="primary"
          class="submit-btn"
          :disabled="!protocolChecked"
          @click="handleNextStep"
          >开始认证</van-button
        >
      </div>
    </div>
    <div class="step-2" v-if="authStep == 2">
      <div>
        <van-form @submit="onSubmit" ref="authForm">
          <van-cell-group inset>
            <van-field name="uploader" label="营业执照" :rules="[{ validator: validatorYyzz }]">
              <template #input>
                <van-uploader :max-count="1" v-model="upload.yyzz" :after-read="afterReadyyzz">
                  <div class="upload-image-style">
                    <img src="../../assets/images/license.png" />
                  </div>
                </van-uploader>
              </template>
            </van-field>
            <van-field
              v-model="formData.name"
              name="name"
              label="组织机构名称"
              placeholder="请输入组织机构名称"
              :rules="[{ required: true, message: '请填写组织机构名称' }]"
            />
            <van-field
              v-model="formData.organizationNo"
              type="text"
              name="idCard"
              label="组织机构代码"
              placeholder="请输入组织机构代码"
              :rules="[{ validator: validateOrganizationCode }]"
            />
            <van-field
              v-model="formData.corporation"
              name="corporation"
              label="法人姓名"
              placeholder="请输入法人姓名"
              :rules="[{ required: true, message: '请填写法人姓名' }]"
            />
            <van-field
              v-model="formData.corporationNo"
              name="corporationNo"
              label="法人证件号"
              placeholder="请输入法人证件号"
              :rules="[{ validator: validateIdNumber }]"
            />
            <van-field
              name="uploader"
              label="法人身份证人像面"
              :rules="[{ validator: validatorIdCardFace }]"
            >
              <template #input>
                <van-uploader
                  :max-count="1"
                  v-model="upload.idCardFaces"
                  :after-read="afterReadFace"
                >
                  <div class="upload-image-style">
                    <img src="../../assets/images/card-front.png" />
                  </div>
                </van-uploader>
              </template>
            </van-field>
            <van-field
              name="uploader"
              label="法人身份证国徽面"
              :rules="[{ validator: validatorIdCardEmblems }]"
            >
              <template #input>
                <van-uploader
                  :max-count="1"
                  v-model="upload.idCardEmblems"
                  :after-read="afterReadEmblem"
                >
                  <div class="upload-image-style">
                    <img src="../../assets/images/card-back.png" />
                  </div>
                </van-uploader>
              </template>
            </van-field>
            <van-field
              v-model="formData.phone"
              type="text"
              name="phone"
              label="法人手机号"
              placeholder="请输入法人手机号"
              :rules="[{ validator: validatePhone }]"
            />
            <van-field
              v-model="formData.captcha"
              name="password"
              placeholder="短信验证码"
              clearable
              label="验证码"
              :rules="[{ validator: validateSms }]"
            >
              <template #button>
                <div @click="sendPhoneCode">
                  <span class="custom-link" :class="duration != initDuration ? 'disabled' : ''">{{
                    countdownText
                  }}</span>
                </div>
              </template>
            </van-field>
            <van-field
              name="uploader"
              label="企业授权确认书"
              label-width="2.9rem"
              :rules="[{ validator: validatorAuthBook }]"
            >
              <template #input>
                <van-uploader
                  :max-count="1"
                  :preview-image="false"
                  accept="application/pdf,image/png,image/jpg,image/jpeg"
                  v-model="upload.authBook"
                  :after-read="afterReadAuthBook"
                >
                  <van-button icon="plus" type="primary" size="small">上传文件</van-button>
                </van-uploader>
                <!-- <van-space v-if="upload.authBook.length>0">
							<van-tag color="success">已上传</van-tag>
							<van-button icon="plus" type="primary" size="small">重新上传</van-button>
						</van-space> -->
                <div class="auth-book-preview" v-if="upload.authBook.length > 0">
                  <div class="auth-book-name">{{ upload.authBook[0].file.name }}</div>
                  <div class="auth-book-opt"
                    ><span class="custom-link" @click="upload.authBook = []">删除</span></div
                  >
                </div>
              </template>
            </van-field>
            <van-field
              name="switch"
              label="生成默认印章"
              :rules="[{ validator: validatorIdCardEmblems }]"
            >
              <template #input>
                <div class="auto-seal-preview">
                  <div style="box-sizing: content-box">
                    <van-switch
                      v-model="formData.autoSeal"
                      size="20px"
                      style="box-sizing: content-box"
                    />
                  </div>
                  <div v-if="formData.autoSeal">
                    <img :src="u81" width="100" height="100" />
                    <p style="text-align: center; line-height: 30px; margin: 0">企业印章样例</p>
                  </div>
                </div>
              </template>
            </van-field>
          </van-cell-group>
          <div style="margin: 16px">
            <van-button round block type="primary" native-type="submit"> 提交 </van-button>
          </div>
        </van-form>
      </div>
    </div>
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
  import { FieldType, Notify, Switch } from 'vant';
  import { download } from '@/api';
  import Api from '@/api/user';
  import { useLoading } from '@/hooks';
  import {
    validateOrganizationCode,
    validateIdNumber,
    validatePhone,
    validateSms,
  } from './formValidate';
  import { useUserStore } from '@/store/modules/user';
  import { uploadFile } from '@/api';
  import { useRoute, useRouter } from 'vue-router';
  import u81 from '@/assets/images/u81.png';
  import { Verify } from '@/components/verifition';
  const userStore = useUserStore();
  // const tenantInfo = ref(userStore.getTenantInfo)

  const router = useRouter();
  const route = useRoute();
  const authStep = ref(1);
  const protocolChecked = ref(false);
  const authForm = ref();
  const { startLoading, stopLoading } = useLoading();
  const loading = ref(false);
  const verifyRef = ref({});

  function handleTerm(type: string) {
    let signAppInfo = window.appInfo.sign_app_info;
    window.open(signAppInfo.url + '/#/terms/service/' + type);
  }
  function handleNextStep() {
    authStep.value = 2;
  }

  const callQuery = ref<any>({
    signRuId: null,
    taskId: null,
    taskType: null,
    path: null,
    from: null,
  });

  onMounted(() => {
    callQuery.value.signRuId = route.query.signRuId;
    callQuery.value.taskId = route.query.taskId;
    callQuery.value.taskType = route.query.taskType;
    callQuery.value.path = route.query.path;
    callQuery.value.from = route.query.from;

    const authStatus = route.query.authStatus;
    if (authStatus && authStatus != 2) {
      return;
    }
    if (userStore.getTenantInfo.authStatus == 2) {
      // router.push("/enterprise/details");
    }
  });
  const validatorIdCardFace = (val: any) => {
    if (upload.value.idCardFaces.length == 0) {
      return '法人身份证人像面必须上传';
    }
  };
  const validatorIdCardEmblems = (val: any) => {
    if (upload.value.idCardFaces.length == 0) {
      return '法人身份证国徽面必须上传';
    }
  };

  const validatorYyzz = (val: any) => {
    if (upload.value.yyzz.length == 0) {
      return '营业执照必须上传';
    }
  };

  const validatorAuthBook = (val: any) => {
    if (upload.value.authBook.length == 0) {
      return '企业授权确认书必须上传';
    }
  };

  const afterReadEmblem = async (file: any) => {
    loading.value = true;
    const params = {
      dataCategory: 'idpic2',
    };
    const response = await uploadFile(file.file, params);
    formData.value.idCardEmblem = response.result;
    setTimeout(function () {
      loading.value = false;
    }, 300);
  };

  const afterReadFace = async (file: any) => {
    loading.value = true;
    const params = {
      dataCategory: 'idpic1',
    };
    const response = await uploadFile(file.file, params);
    formData.value.idCardFace = response.result;
    setTimeout(function () {
      loading.value = false;
    }, 300);
  };
  const afterReadyyzz = async (file: any) => {
    loading.value = true;
    const params = {
      dataCategory: 'organizationpic',
    };
    const response = await uploadFile(file.file, params);
    formData.value.businessLicense = response.result;
    setTimeout(function () {
      loading.value = false;
    }, 300);
  };
  const afterReadAuthBook = async (file: any) => {
    loading.value = true;
    const params = {
      dataCategory: 'authorizebook',
    };
    const response = await uploadFile(file.file, params);
    formData.value.authorizeBook = response.result;
    setTimeout(function () {
      loading.value = false;
    }, 300);
  };

  async function onSubmit() {
    //callBackSource();

    console.log('formData', formData.value);
    if (!formData.value.captchaKey) {
      Notify({ type: 'warning', message: '请先获取验证码', duration: 1000 });
      return;
    }
    try {
      await authForm.value.validate();
      startLoading();
      const result = await Api.enterpriseAuth(formData.value);
      if (result.code == 200) {
        await userStore.reloadTenantDeparts();
        await userStore.afterLoginAction();
        await userStore.reloadTenantInfo();
        Notify({ type: 'success', message: '实名认证成功，3s后关闭', duration: 3000 });
        stopLoading();
        setTimeout(function () {
          stopLoading();
          // router.push("/home");
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

  function callBackSource() {
    if (callQuery.value.signRuId) {
      router.push({
        path: '/noauth',
        query: callQuery.value,
      });
    } else {
      router.push('/home');
    }
  }

  const formData = ref({
    name: '',
    idCardFace: '',
    idCardEmblem: '',
    organizationNo: '',
    corporation: '',
    corporationNo: '',
    businessLicense: '',
    phone: '',
    captcha: '',
    captchaKey: '',
    authType: 1,
    realItem: 1,
    authorizeBook: '',
    autoSeal: false,
  });
  const upload = ref({
    yyzz: [],
    idCardFaces: [],
    idCardEmblems: [],
    authBook: [],
  });

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
  .step-1 {
    width: 8rem;
    margin: 0 auto;

    .auth-tip-container {
      display: flex;
      flex-direction: column;
    }

    .tip-title {
      font-weight: 600;
      text-align: left;
      align-items: left;
      padding: 0;
      margin: 0;
    }

    .tip-info {
      background: #f4f9ff;
      padding: 10px 15px;
      border-radius: 2px;
    }

    .protocol-info {
      margin: 15px 0;
    }
  }

  .step-2 {
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

  .auth-book-preview {
    display: flex;
    width: 5rem;

    .auth-book-name {
      // width: 3rem;
      flex: 1;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }

    .auth-book-opt {
      width: 1.5rem;
    }
  }
  .base-mask {
    .base-loading {
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
    }
  }
</style>

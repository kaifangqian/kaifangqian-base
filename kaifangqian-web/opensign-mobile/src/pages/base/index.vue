<!--
  @description 任务办理链接基础信息页面

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
  <div class="base-link-container">
    <van-overlay :show="loading" class="base-mask">
      <van-loading class="base-loading" v-if="loading" size="24px" color="#1989fa"
        >加载中...</van-loading
      >
    </van-overlay>
    <!-- <div class="logo" v-if="!loading">
      <img :src="base64Logo" alt="logo" />
    </div> -->
    <!-- 主要内容区域 -->
    <main class="base-main-container">
      <div class="base-content-card">
        <!-- 顶部状态区域 -->
        <div class="base-header van-gradient--primary">
          <h1 class="base-title">{{ detailInfo.subject }} {{ contractTitle }}</h1>
          <p class="base-doc-no" v-if="detailInfo.docNo">文档编号：{{ detailInfo.docNo }}</p>
        </div>
        <!-- 信息展示区域 -->
        <div class="base-content-padding">
          <div class="base-info-grid">
            <!-- 文档信息卡片 -->
            <div class="van-cell-group van-cell-group--card">
              <h3 class="base-section-title">
                <van-icon
                  name="description"
                  style="font-size: 1.2em; margin-right: 8px; color: #3b82f6"
                />
                文档信息
              </h3>
              <div class="base-info-list">
                <div class="base-info-item">
                  <span class="base-info-label">文档主题：</span>
                  <span class="base-info-value">{{ detailInfo.subject }}</span>
                </div>
                <div class="base-info-item">
                  <span class="base-info-label">文档份数：</span>
                  <span class="base-info-value">共 {{ detailInfo.fileSum }} 份签署文档</span>
                </div>
                <div class="base-info-item">
                  <span class="base-info-label">文档状态：</span>
                  <van-tag
                    class="tag-style"
                    :type="getRuStatusTagProps(detailInfo.signStatus).type"
                    :color="getRuStatusTagProps(detailInfo.signStatus).color"
                    :plain="getRuStatusTagProps(detailInfo.signStatus).plain"
                  >
                    {{ loadRuStatus(detailInfo.signStatus) }}
                </van-tag>
                </div>
                <div class="base-info-item base-info-item--column">
                  <span class="base-info-label">签署文档：</span>
                  <div
                    class="base-file-list"
                    v-for="(item, index) in detailInfo.signFileNames"
                    :key="index"
                  >
                    <div class="base-file-item">
                      <van-icon name="description" class="base-file-icon" />
                      <!-- <van-icon name="description" style="font-size: 1.2em; margin-right: 8px; color: #3b82f6;" /> -->
                      <span class="base-file-name">{{ item }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 分割线 -->
            <div class="base-section-divider"></div>
            <!-- 签署信息卡片 -->
            <div class="van-cell-group van-cell-group--card">
              <h3 class="base-section-title">
                <van-icon
                  name="friends-o"
                  style="font-size: 1.2em; margin-right: 8px; color: #3b82f6"
                />
                参与方信息
              </h3>
              <div class="base-info-list">
                <div class="base-info-item">
                  <span class="base-info-label">发起方：</span>
                  <div class="base-info-value">
                    <span>{{ detailInfo.senderName }}</span>
                  </div>
                </div>
                <div class="base-info-item base-info-item--column">
                  <span class="base-info-label">签署方：</span>
                  <div 
                    class="base-signer-list"
                    v-for="(item, index) in detailInfo.signers"
                    :key="index"
                  >
                    <div class="base-signer-item">
                      <van-icon name="friends-o" class="base-file-icon" />
                      <span class="base-signer-name">{{item.signerName}}</span>
                    </div>
                  </div>
                </div>
                <div class="base-info-item">
                  <span class="base-info-label">截止时间：</span>
                  <span class="base-info-value">{{detailInfo.expireDate || '未设置'}}</span>
                </div>
                <div class="base-info-item" v-if="taskType == 'write' || taskType == 'sign'">
                  <span class="base-info-label">{{ contractTitle }}状态：</span>
                  <div class="base-info-value">
                    <span>{{ taskStatusText }}</span>
                  </div>
                  <!-- <van-tag class="tag-style">{{ taskStatusText }}</van-tag> -->
                </div>
              </div>
            </div>
          </div>
          <!-- 操作按钮 -->
          <div class="base-action-buttons">
            <van-button type="primary" size="large" class="base-action-button" @click="handleLink">
              {{ taskButtonText }}
            </van-button>
            <van-button
              v-if="callbackPage"
              size="large"
              class="base-back-button"
              @click="handleBack"
              plain
            >
              返回
            </van-button>
          </div>
        </div>
      </div>
    </main>
  </div>
  <v-dialog
    v-model:show="selectDepart.show"
    title="请选择身份进行登录"
    confirm-button-text="退出登录"
    @confirm="quitSelectDepart(true)"
    confirm-button-color="#666"
  >
    <div class="account-list">
      <div v-for="(item, index) in selectDepart.depts" @click="handleSelectTenantSubmit(item)">
        <div :class="['account-item', item.useFlag ? '' : 'no-use']">
          <p :key="index" class="company-header">
            <span>{{ item.tenantName }}</span>
            <!-- <span class="current-login-tag" v-if="item.selectFlag">
              <van-tag color="#096dd9" plain v-if="item.selectFlag" size="large">上次登录</van-tag>
            </span> -->
            <span class="current-login-tag" v-if="!item.useFlag">
              <van-tag type="danger" plain v-if="!item.useFlag" size="large">已停用</van-tag>
            </span>
          </p>
        </div>
      </div>
    </div>
  </v-dialog>

  <v-dialog
    v-model:show="loginVisible"
    title="登录"
    @confirm="quitSelectDepart(false)"
    confirm-button-text="关闭"
    teleport=".base-link-container"
  >
    <van-form @failed="onFailed" ref="loginFrom">
      <van-field
        v-model="loginInfo.account"
        disabled
        name="account"
        placeholder="手机号码"
        left-icon="phone-o"
        label=" "
        label-width="0.2rem"
      />
      <van-field
        v-model="loginInfo.captcha"
        name="password"
        placeholder="验证码"
        left-icon="chat-o"
        label=" "
        label-width="0.2rem"
        :rules="[{ validator: validatorCaptcha }]"
      >
        <template #button>
          <div @click="sendPhoneCode" class="login-sms-code">
            <span class="custom-link" :class="duration != initDuration ? 'disabled' : ''">{{
              countdownText
            }}</span>
          </div>
        </template>
      </van-field>
      <div style="height: 30px"></div>

      <p
        style="text-align: center; display: flex; align-content: flex-start; flex-wrap: wrap"
        class="trem-row"
      >
        <van-checkbox v-model="registerCheck" shape="square" icon-size="0.3rem"></van-checkbox>
        <span class="ling-font-size" style="padding-left: 5px">&nbsp;&nbsp;我已阅读并同意</span>
        <!-- <span
          href="javascript:;"
          class="custom-link ling-font-size"
          @click="handleTerm('certificate')"
          >《数字证书使用协议》</span
        > -->
        <span href="javascript:;" class="custom-link ling-font-size" @click="handleTerm('serve')"
          >《服务协议》</span
        >
        <span href="javascript:;" class="custom-link ling-font-size" @click="handleTerm('privacy')"
          >《隐私政策》</span
        >
      </p>
      <div class="dialog-action">
        <van-button type="primary" native-type="submit" class="submit-btn" @click="onSubmit"
          >注册/登录</van-button
        >
      </div>
    </van-form>
  </v-dialog>
  <Verify
    @success="handleSildeSuccess"
    :imgSize="{ width: '300px', height: '155px' }"
    captchaType="blockPuzzle"
    ref="verifyRef"
  />
</template>

<script lang="ts">
  import { ref, defineComponent, onMounted, unref } from 'vue';
  import Api, { LoginParams } from '@/api/contract/index';
  import UserApi from '@/api/user/index';
  import { useRoute, useRouter } from 'vue-router';
  import { Toast, Dialog, Notify } from 'vant';
  import session from '@/utils/cache/session';
  import { APP_TOKEN } from '@/utils/cache/constant';
  import { useUserStore } from '@/store/modules/user';
  import { isMobileDevice, getHashQueryString, decodeURIs } from '@/utils/util';
  import defaultBase64 from '@/assets/images/logo-sign.png';
  import { isMobile, isEmail } from '@/utils/validate';
  import { Verify } from '@/components/verifition';
  import { loadRuStatus, getRuStatusTagProps } from '../transform';
  // import { FileTextOutlined, TeamOutlined } from '@ant-design/icons-vue';
  export default defineComponent({
    name: 'Base',
    components: {
      'v-dialog': Dialog.Component,
      Verify,
      // FileTextOutlined,
      // TeamOutlined,
    },
    setup() {
      const detailInfo: any = ref({});
      const userStore = useUserStore();

      const selectDepart: any = ref({
        show: false,
        depts: ref(userStore.getTenantDeparts),
      });
      const initDuration = 59;
      const duration = ref(59);
      const loading = ref(true);
      const registerCheck = ref(true);
      const loginFrom = ref();
      const redirectPath = ref();
      const loginInfo = reactive<LoginParams>({
        captcha: '',
        captchaKey: null,
        phone: '',
        appCode: 'opensign_0001',
      });
      const contractTitle = ref('邀请');
      const interval = ref<any>(null);
      const callbackPage = getHashQueryString('callbackPage');
      const countdownText = ref('获取验证码');
      const route = useRoute();
      const router = useRouter();
      // const callbackPage = route.query.callbackPage;
      const linkCode = route.query.code;
      const signRuId = ref('');
      const partyName = ref('');
      const taskId = ref('');
      const tokenEffective = ref(true);
      const phone = ref('');
      const loginVisible = ref(false);
      const taskType = ref('');
      const verifyRef = ref({});
      const taskStatusText = computed(() => {
        if (taskType.value == 'write') {
          if (detailInfo.value.taskStatus === 2 && detailInfo.value.checkMenuType == 'approve') {
            return '已填写';
          } else if (
            detailInfo.value.taskStatus === 2 &&
            detailInfo.value.checkMenuType == 'reject'
          ) {
            return '已拒填';
          } else if (detailInfo.value.taskStatus === 1) {
            return '未填写';
          }
        } else if (taskType.value == 'sign') {
          if (detailInfo.value.taskStatus === 2 && detailInfo.value.checkMenuType == 'approve') {
            return '已签署';
          } else if (
            detailInfo.value.taskStatus === 2 &&
            detailInfo.value.checkMenuType == 'reject'
          ) {
            return '已拒签';
          } else if (detailInfo.value.taskStatus === 1) {
            return '未签署';
          }
        }
        return '未知状态';
      });

      const taskButtonText = computed(() => {
        if (taskType.value == 'write') {
          return detailInfo.value.signStatus === 5 && detailInfo.value.taskStatus === 1
            ? '立即填写'
            : '查看详情';
        } else if (taskType.value == 'sign') {
          return detailInfo.value.signStatus === 7 && detailInfo.value.taskStatus === 1
            ? '立即签署'
            : '查看详情';
        }
        return '查看详情';
      });
      const validatorPhone = (val: any) => {
        if (val) {
          return !/^[1][3456789][0-9]\d{8}$/.test(val) ? '手机号码格式不正确' : null;
        } else {
          return '手机号码不能为空';
        }
      };

      const validatorCaptcha = (val: any) => {
        if (val) {
          return !/(^\d{4}$)|(^\d{6}$)/.test(val) ? '验证码格式不正确' : null;
        } else {
          return '验证码不能为空';
        }
      };

      const base64Logo = ref(defaultBase64);

      onMounted(() => {
        getLogo();
        if (!isMobileDevice()) {
          // 构建新的 URL，携带参数
          var newURL: any;
          if (linkCode) {
            newURL =
              window.appInfo.sign_app_info.url.url + '/#/contract/detail/base?code=' + linkCode;
          } else {
            //signRuId.value = route.query.signRuId;
            // taskId.value = route.query.taskId;
            // taskType.value = route.query.taskType;
            // loginInfo.phone = route.query.phone;
            const query = route.query;
            newURL = `${window.appInfo.sign_app_info.url}/#/contract/detail/base?taskType=${query.taskType}&phone=${query.phone}&partyName=${query?.partyName}&email=${query.email}&signRuId=${query.signRuId}&taskId=${query.taskId}&callbackPage=${callbackPage}`;
          }

          setTimeout(() => {
            window.location.href = newURL;
            loading.value = false;
          });
          return;
        }
        if (linkCode) {
          setTimeout(() => {
            loading.value = false;
          }, 500);
          getParams();
        } else {
          noLinkCode();
        }
      });
      async function chekToken() {
        const token = session.getItem(APP_TOKEN);
        if (!token) {
          tokenEffective.value = false;
          return;
        }
        let { result, code } = await Api.chekTokenEffective({ token });
        if (code == 200) {
          if (!result) {
            tokenEffective.value = false;
          }
        }
      }
      async function getLogo() {
        const { result, code } = await UserApi.getOtherLogo({});
        if (code == 200) {
          base64Logo.value = result.image;
        }
      }
      function noLinkCode() {
        setTimeout(() => {
          loading.value = false;
        }, 500);
        signRuId.value = route.query.signRuId as string;
        taskId.value = route.query.taskId as string;
        partyName.value = route.query.partyName as string;
        taskType.value = route.query.taskType as string;
        loginInfo.account = route.query.phone || (route.query.email as string);
        console.log(route.query);
        if (signRuId.value) {
          getContrctInfo();
        }
        initTastType(route.query.taskType as string);
      }

      async function getParams() {
        let { result } = await Api.getLinkParams({ code: linkCode });
        // let params =result;
        signRuId.value = result.signRuId;
        taskId.value = result.taskId;
        taskType.value = result.taskType;
        loginInfo.phone = result.phone;
        loginInfo.email = result.email;
        loginInfo.account = result.phone || result.email;
        partyName.value = result.tenantName;
        if (signRuId.value) {
          await getContrctInfo();
        }
        initTastType(result.taskType);
        if (result.noLogin) {
          await userStore.singleSign(result);
          handleLink();
        }
      }
      async function getContrctInfo() {
        let { result } = await Api.getLinkRuInfo({ signRuId: signRuId.value, signTaskId: taskId.value });
        if (result) {
          detailInfo.value = result;
        }
      }
      function initTastType(taskType: string) {
        if (taskType == 'write') {
          contractTitle.value = '填写';
        }
        if (taskType == 'sign') {
          contractTitle.value = '签署';
        }
        if (taskType == 'copy') {
          contractTitle.value = '抄送';
        }
      }
      function handleLink() {
        chekToken();
        if (taskType.value == 'write') {
          redirectPath.value = '/write';
        }
        if (taskType.value == 'sign') {
          redirectPath.value = '/signContract';
        }
        if (taskType.value == 'copy') {
          redirectPath.value = '/detail';
        }
        const appToken = session.getItem(APP_TOKEN);
        if (!tokenEffective.value || !appToken) {
          loginVisible.value = true;
          return;
        }
        if (selectDepart.value.depts.length > 1) {
          selectDepart.value.show = true;
        } else {
          // router.push({
          //     path: redirectPath.value,
          //     query: {
          //         taskId: unref(taskId),
          //         signRuId: unref(signRuId),
          //         callbackPage:callbackPage
          //     }
          // })
          router.push(
            `${redirectPath.value}?taskId=${unref(taskId)}&signRuId=${unref(
              signRuId
            )}&callbackPage=${callbackPage}`
          );
        }
      }
      function quitSelectDepart(isLogout: boolean) {
        selectDepart.value.show = false;
        selectDepart.value.depts = [];
        // userStore.setLoginToken(undefined);
        // userStore.setTenantDeparts(undefined);
        if (isLogout) {
          userStore.logout();
        }
      }
      async function handleSelectTenantSubmit(item: any) {
        const formData = {
          departId: item.departs[0].departId,
        };
        const result = await userStore.selectTenant(formData);
        if (result.status) {
          // router.push({
          //     path: redirectPath.value,
          //     query: {
          //         taskId: unref(taskId),
          //         signRuId: unref(signRuId),
          //         callbackPage: callbackPage
          //     }
          // })
          router.push(
            `${redirectPath.value}?taskId=${unref(taskId)}&signRuId=${unref(
              signRuId
            )}&callbackPage=${callbackPage}`
          );
        }
      }
      async function onSubmit() {
        if (!loginInfo.captchaKey) {
          Notify({ type: 'warning', message: '请先获取验证码', duration: 2000 });
          return;
        }
        if (!registerCheck.value) {
          Notify({ type: 'warning', message: '请先阅读并同意平台协议', duration: 3000 });
          return;
        }

        try {
          await loginFrom.value.validate();
          const { status, depts } = await userStore.registerOrLogin(loginInfo);
          if (status) {
            selectDepart.value.depts = depts;
            if (depts.length > 1) {
              let matchDepart = depts.find((item: any) => item.tenantName === partyName.value);
              if (matchDepart) {
                handleSelectTenantSubmit(matchDepart);
              } else {
                selectDepart.value.show = true;
              }
            } else {
              handleSelectTenantSubmit(depts[0]);
              // router.push({
              //     path: redirectPath.value,
              //     query: {
              //         taskId: unref(taskId),
              //         signRuId: unref(signRuId)
              //     }
              // })
            }
          }
        } catch (e) {
          console.error('form field error:', e);
        }
      }
      async function handleRegisterOrLogin() {
        router.push({
          path: redirectPath.value,
          query: {
            taskId: unref(taskId),
            signRuId: unref(signRuId),
          },
        });
      }
      async function handleSildeSuccess(data: any) {
        loginVisible.value = true;
        if (duration.value === initDuration) {
          duration.value = initDuration;
          let response: any;
          if (isEmail(loginInfo.account)) {
            loginInfo.email = loginInfo.account;
            response = await Api.sendEmailCode({
              email: loginInfo.account,
              type: 'login1',
              captchaVerification: data.captchaVerification,
            });
          } else if (isMobile(loginInfo.account)) {
            loginInfo.phone = loginInfo.account;
            response = await Api.sendCode({
              phone: loginInfo.account,
              type: 'login1',
              captchaVerification: data.captchaVerification,
            });
          } else {
            Notify({ type: 'warning', message: '账号不合法', duration: 1000 });
            return;
          }
          if (response.code == 200) {
            loginInfo.captchaKey = response.result;
            console.log('send res:', response);
            Notify({ type: 'success', message: '验证码发送成功', duration: 1000 });
            delayCountDown();
          } else {
            Notify({ type: 'warning', message: '获取验证码失败', duration: 1000 });
          }
        } else {
          return;
        }
      }
      async function sendPhoneCode() {
        try {
          await loginFrom.value.validate('account');

          if (duration.value === initDuration) {
            loginVisible.value = false;
            verifyRef.value.show();
          }
        } catch (e) {
          console.error('form field error:', e);
        }
      }
      function delayCountDown() {
        if (duration.value == 0) {
          clearTimeout(interval.value);
          duration.value = 59;
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
      function handleTerm(type: string) {
        let signAppInfo = window.appInfo.sign_app_info;
        window.open(signAppInfo.url + '/#/terms/service/' + type);
      }

      function onFailed() {}

      async function handleBack() {
        if (callbackPage && typeof callbackPage == 'string') {
          window.open(decodeURIs(callbackPage), '_self');
          return;
        }
      }

      return {
        detailInfo,
        handleTerm,
        loading,
        contractTitle,
        handleLink,
        loginVisible,
        base64Logo,
        selectDepart,
        quitSelectDepart,
        handleSelectTenantSubmit,
        countdownText,
        loginFrom,
        duration,
        loginInfo,
        validatorPhone,
        validatorCaptcha,
        onSubmit,
        sendPhoneCode,
        onFailed,
        phone,
        initDuration,
        registerCheck,
        handleRegisterOrLogin,
        handleSildeSuccess,
        verifyRef,
        loadRuStatus,
        handleBack,
        callbackPage,
        taskType,
        taskStatusText,
        taskButtonText,
        getRuStatusTagProps,
      };
    },
  });
</script>

<style lang="less" scoped>
  html, body {
    height: 100%;
    margin: 0;
    padding: 0;
  }
  .base-mask {
    background-color: rgb(232 232 232 / 99%);

    .base-loading {
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
    }
  }

  .logo {
    text-align: left;
    padding-top: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 24px 0 12px 0; /* 上下留白 */
    width: 100%;
  }

  .logo img {
    // width: 150px;
    width: 120px; /* 适当缩小，适配移动端 */
    // max-width: 60vw;
    height: auto;
    // border-radius: 16px;    /* 圆角 */
    box-shadow: 0 4px 16px rgba(59,130,246,0.08); /* 柔和阴影 */
    background: #fff;
    object-fit: contain;
    }

  .base-link-container {
    height: 100vh; /* 新增，确保容器高度为视口高度 */
    min-height: 100vh;
    background: #f7fafd;
    display: flex;
    // display: block; // 改为block布局
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    padding-bottom: 0px;
    // 添加以下样式以支持滚动
    overflow-y: auto;
    -webkit-overflow-scrolling: touch; // iOS设备上的平滑滚动
    :deep(.van-dialog__confirm) {
      color: #999;
    }
  }

  .dialog-action {
    text-align: center;
    margin-bottom: 40px;

    :deep(.van-button) {
      height: 80px;
      border-radius: 8px;
    }
  }

  .base-card-info {
    width: 660px;
    margin: 40px auto;
    padding: 20px 50px;
    // box-shadow: 0 4px 9px 2px #eee;
    box-shadow: 0 4px 24px 0 rgba(40, 140, 242, 0.08);
    background: #fff;
    text-align: center;
    border-radius:18px;

    :deep(.van-list) {
      text-align: left;

      .van-cell__title {
        width: 200px;
        flex: 0 0 1;
      }

      .van-cell__value {
        text-align: left;
      }
    }

    .card-title {
      font-weight: 800;
      text-align: center;
      font-size: 38px;
      color: #000;
      margin-top:30px;
      margin-bottom: 10px;
      /* 渐变字体样式 */
      background: linear-gradient(90deg, #033a71 0%, #41a5f6 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      text-fill-color: transparent;
    }

    .card-title-divider {
      width: 250px;
      height: 4px;
      background: linear-gradient(90deg, #288cf2 0%, #6ebeff 100%);
      border-radius: 2px;
      margin: 0 auto 32px auto;
      opacity: 0.85;
    }

    .link-action {
      width: 320px;
      margin: 40px auto;
      // padding: 10px 16px;
      height: 80px;
      background: linear-gradient(90deg, #0084ff, #6ebeff);
      border-color: #ffffff;
      border-radius:8px;
    }
  }
  .contract-info {
    margin-top: 30px;
    li {
      margin: 32px 0;
      display: flex;
      flex-direction: column;
    }

    .info-title {
      font-size: 24px;
      font-weight: 550;
      width: 200px;
      display: inline-block;
      text-align: left;
      // margin-left: 10%;
      margin-bottom: 10px;
    }
    .info-value {
      color: #888;
      flex: 1;
      text-align: left;
      display: block;
    }
  }
  .account-list {
    overflow: auto;
    max-height: 6.6rem;

    .account-item {
      /* font-size: 14px; */
      border: 1px solid #f9f9f9;
      border-radius: 8px;
      cursor: pointer;
      align-items: center;
      justify-content: space-between;
      margin-right: 10px;
      margin-bottom: 10px;

      .company-header {
        background: #f9f9f9;
        padding: 0.3rem 0.3rem;
        display: flex;
        justify-content: space-between;
        margin: 0 !important;

        .login-arrow {
          visibility: hidden;
        }

        &:hover {
          .login-arrow {
            visibility: visible;
            color: #094aee;
          }
        }

        .current-login-tag {
          width: 3rem;
          justify-content: center;
          align-items: center;
          display: flex;
        }
      }

      .account-depart {
        display: flex;
        margin-bottom: 10px;
        justify-content: space-between;
        padding: 0 20px;
      }
    }

    .no-use {
      pointer-events: none;
    }
  }

  .trem-row {
    font-size: 28px;
    padding: 0 28px;

    .custom-link {
      color: #288cf2;
      padding: 0 2px;
      cursor: pointer;
      font-size: 28px;
    &:hover {
      text-decoration: underline;
    }
  }

    span {
      margin-bottom: 10px;
    }

    .custom-link {
      padding: 0;
    }
  }

  .login-sms-code .disabled {
    color: #aaa !important;
  }

  :deep(.ant-tag) {
    border-radius: 4px;
    padding: 2px 8px;
  }
  :deep(.ant-btn-primary) {
    background: linear-gradient(to right, #1677ff, #4096ff);
    border: none;
    transition: all 0.3s ease;
  }
  :deep(.ant-btn-primary:hover) {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(64, 150, 255, 0.4);
  }
  :deep(.ant-modal-content) {
    border-radius: 12px;
    overflow: hidden;
  }
  .aspect-w-4 {
    position: relative;
    padding-bottom: 75%;
  }
  .aspect-h-3 {
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
  }
  .base-main-container {
    max-width: 750px;
    margin: 16px auto 0;
    padding: 0 16px;
    // 确保容器可以扩展以包含所有内容
    width: 100%;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    flex: 1; /* 关键：让其自动填满父容器剩余空间 */
    
    @media (min-width: 768px) {
      margin-top: 48px;
    }
}

.base-content-card {
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 40px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  // 添加以下样式确保卡片可以扩展
  width: 100%;
  // 添加最小高度以确保内容可以扩展
  min-height: calc(100vh - 10px);
}

.base-header {
  padding: 0.25rem; /* 保持原有的 p-4 */
  color: #fff;
  background: linear-gradient(to right, #3b82f6, #2563eb); /* 蓝色渐变，替代 bg-gradient-to-r from-blue-500 to-blue-600 */
  /* 或者使用更精确的颜色值：
  background: linear-gradient(to right, #3b82f6 0%, #2563eb 100%);
  */
}

.base-title {
  font-size: 0.5rem;
  font-weight: 900;
  margin-top: 0px;
  margin-bottom: 0px;
  color: #fff;
  /* 添加轻微的文本阴影 */
  text-shadow: 0.3px 0 0 currentColor, 0 0.3px 0 currentColor;
}

.base-doc-no {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.4rem;
  margin-top: 8px;
  margin-bottom: 0px;
}

.base-content-padding {
  padding: 0.6rem;
}

.base-info-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  
  @media (min-width: 768px) {
    gap: 32px;
  }
}

.base-section-title {
  font-size: 0.45rem;
  font-weight: 800;
  color: #000;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  margin-top: 0px;
  /* 添加轻微的文本阴影 */
  text-shadow: 0.3px 0 0 currentColor, 0 0.3px 0 currentColor;
}


.base-info-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.base-info-item {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 28px;
  // font-size: 0.65rem;
  
  @media (min-width: 768px) {
    align-items: center;
  }
  
  &--column {
    flex-direction: column;
    align-items: flex-start;
    
    @media (min-width: 768px) {
      flex-direction: row;
      align-items: center;
    }
  }
}

.base-info-label {
  flex-shrink: 0;
  // width: 96px;
  margin-right: 16px;
  color: #666;
  font-size: 0.4rem;
  
  &--column {
    width: 96px;
    margin-bottom: 8px;
    
    @media (min-width: 768px) {
      margin-bottom: 0;
    }
  }
}

.base-info-value {
  flex: 1;
  color: #000;
  font-weight: 400;
  // font-size: 24px;
  font-size: 0.4rem;
}

.base-file-list, .base-signer-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 16px;
  margin-top: 8px;
  
}

.base-file-item, .base-signer-item {
  display: flex;
  align-items: center;
  background-color: #f7fafd;
  border-radius: 8px;
  padding: 6px 16px 6px 10px;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.08);
  transition: box-shadow 0.2s;
  font-size: 0.4rem;
  color: #333;
  min-width: 120px;
  max-width: 100%;
  cursor: pointer;
  border: 1px solid #e6f0fa;
  
  @media (min-width: 768px) {
    padding: 4px 16px;
  }
}

// .base-file-item:hover {
//   box-shadow: 0 4px 16px rgba(59, 130, 246, 0.16);
//   background: #e6f0fa;
// }

.base-file-icon {
  font-size: 1em;
  // color: #3b82f6;
  margin-right: 8px;
}
// .base-file-name {
//   word-break: break-all;
//   flex: 1;
// }

.base-file-name, .base-signer-name {
  color: #333;
  // font-size: 24px;
  font-size: 0.4rem;
  word-break: break-all;
  flex: 1;
}

.base-action-buttons {
  margin-top: 32px;
  // display: flex;
  justify-content: center;
  gap: 16px;
}

.base-back-button {
  padding: 0 48px;
  height: 1.2rem;
  // font-size: 24px;
  font-size: 0.4rem;
  border-radius: 16px;
  margin-bottom: 40px;
}

.base-action-button {
  padding: 0 32px;
  height: 1.2rem;
  // font-size: 24px;
  font-size: 0.4rem;
  margin-top: 0.5rem;
  margin-bottom: 0.3rem;
  font-weight: 600;
  border-radius: 16px;
  
  @media (min-width: 768px) {
    padding: 0 48px;
    height: 44px;
    font-size: 28px;
  }
}

.tag-style {
  font-size: 0.4rem;
  height: 0.6rem;

}
.base-section-divider {
  width: 100%;
  height: 1px;
  background: linear-gradient(to right, #e3e8ee 0%, #c9d6e6 100%);
  margin: 32px 0 24px 0;
  border: none;
  opacity: 0.8;
}

.van-cell-group--card {
  margin-bottom: 0;
  background: #fff;
  // border-radius: 12px;
  // box-shadow: 0 2px 8px rgba(59, 130, 246, 0.06);
  // padding: 16px 0;
}
  
</style>

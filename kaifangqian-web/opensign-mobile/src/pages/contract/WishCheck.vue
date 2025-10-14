<!--
  @description 签署意愿验证

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
<script lang="ts" setup>
  import { onMounted, ref, onUnmounted } from 'vue';
  import { useRouter } from 'vue-router';
  // import { Loading } from '/@/components/Loading';
  import { useUserStore } from '@/store/modules/user';
  import defaultLogo from '@/assets/images/logo-sign.png';
  import Api from '@/api/contract/index';
  import { Dialog, Notify } from 'vant';
  import { decodeURIs, getHashQueryString } from '@/utils/util';

  // const { createMessage } = useMessage();
  const userStore = useUserStore();
  const logoBase64 = ref(userStore.getWebConfig?.websiteOtherLogo || defaultLogo);
  const router = useRouter();
  const loading = ref(true);
  const callbackPage = getHashQueryString('callbackPage');
  const signResult = ref<any>(false);
  const contractInfo = ref<any>({});
  const failMsg = ref('');
  const signStatus = ref<boolean | null>(null);
  const countdown = ref(3); // 倒计时秒数
  let countdownTimer: any = null;
  onMounted(async () => {
    await confirmSign();
  });

  async function confirmSign() {
    try {
      const query = router.currentRoute.value.query;
      const data = await Api.confirmSignRu({ signConfirmOrderNo: query.orderNo });
      if (data.code === 200) {
        //签署成功
        signStatus.value = true;
        startCountdown();
      } else {
        if (data.result && data.result.orderStatus === 0) {
          //订单未完成意愿认证
          Dialog.confirm({
            title: '温馨提示',
            message: '您未完成签署意愿认证，本次签署未完成，请知悉！',
            confirmButtonText: '知道了',
          }).then(() => {
            handleBack();
          });
        } else {
          //签署失败
          signStatus.value = false;
          failMsg.value = data.message;
        }
      }
    } catch (error) {
      signStatus.value = false;
      failMsg.value = '系统异常，请联系系统管理员';
    }
    loading.value = false;
  }

  async function handleBack() {
    if (callbackPage && typeof callbackPage == 'string') {
      window.open(decodeURIs(callbackPage), '_self');
      return;
    }
    router.push({
      path: '/index',
    });
  }

  function startCountdown() {
    // countdown.value = 3;
    countdownTimer && clearInterval(countdownTimer);
    countdownTimer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(countdownTimer);
        handleBack();
      }
    }, 1000);
  }

  // 页面卸载时清理定时器
  onUnmounted(() => {
    countdownTimer && clearInterval(countdownTimer);
  });
</script>

<template>
  <div class="sign-info-container">
    <Loading :loading="loading" :absolute="false" tip="签署验证中。。。" />
    <!-- <Loading :loading="signLoading" text="数据提交中..." /> -->
    <!-- <Alert
      v-if="hasNextTask == true"
        message=""
        type="info"
      >
      <template #description>
        <p class="alert-tip">监测到该签约文档中您还有【{{ taskType == 'sign' ? '签署 ' : '填写' }}】任务，是否前去处理？</p>
        <div>
              <a-button type="link" @click="handleGoNextTask">立即处理</a-button>
        </div>
      </template>
    </Alert> -->
    <!-- <div class="sign-logo" v-if="signStatus != null">
      <img :src="logoBase64" style="height: 70px; width: 160px; margin: 0 30px" />
    </div> -->
    <div class="card-sign-result" v-if="signStatus === false">
      <div style="display: flex; justify-content: center; align-items: center; margin-top: 30px">
        <van-icon name="warning" style="font-size: 3em; margin-right: 8px; color: red" />
      </div>
      <div :gutter="[24, 24]">
        <p class="check-title text-xs font-semibold" style="color: red">签署失败 </p>
        <p>{{ failMsg }}</p>
      </div>
      <div class="check-info-action">
        <van-button type="primary" @click="handleBack" size="small" style="width: 150px"
          >返回</van-button
        >
      </div>
    </div>
    <div class="card-sign-result" v-if="signStatus">
      <div style="display: flex; justify-content: center; align-items: center; margin-top: 30px">
        <van-icon name="checked" style="font-size: 3em; margin-right: 8px; color: #3b82f6" />
      </div>
      <div :gutter="[24, 24]">
        <p class="check-title text-xs font-semibold" style="color: #1989fa">签署成功</p>
      </div>
      <div class="countdown-tip">
        <!-- <a-icon type="clock-circle" style="color:#1989fa;margin-right:4px;" /> -->
        签署成功，{{ countdown }} 秒后自动跳转...
      </div>
      <div class="check-info-action">
        <van-button type="primary" @click="handleBack" size="small" style="width: 150px"
          >返回</van-button
        >
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
  .contract-info {
    margin-top: 30px;

    li {
      margin: 16px 0;
      display: flex;
      flex-direction: column;
    }

    .info-title {
      font-size: 24px;
      font-weight: 550;
      width: 150px;
      display: block;
      text-align: left;
      // margin-left: 10%;
      margin-bottom: 5px;
    }

    .info-value {
      color: #888;
      flex: 1;
      text-align: left;
      display: block;
    }
  }

  .sign-info-container {
    height: calc(100vh - 150px);

    :deep(.ant-card-body) {
      padding: 24px;
    }

    :deep(.ant-alert-description) {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .sms-select {
      margin-top: 5%;
      width: 260px;
      max-width: 550px;
    }

    :deep(.ant-form) {
      .ant-row {
        display: block;
      }

      .fix-auto-fill {
        width: 260px;
        margin: 0 auto;
        margin-top: 10px;
      }
    }

    .auth-form {
      margin: 0 auto;
      margin-top: 10px;
      width: 260px;

      :deep(.ant-form-item-control) {
        margin: 8px 0;
      }
    }
  }

  .card-sign-result {
    width: 90vw;
    max-width: 550px;
    margin: 0 auto;
    margin-top: 1%;
    text-align: center;
    // height: 95%;

    .check-title {
      font-size: 32px;
      font-weight: 600;
      line-height: 50px;
    }

    .check-subtitle {
      font-size: 24px;
      font-weight: 400;
      margin: 0 auto;
      // margin-top: 10%;
      margin-top: 0;
      min-width: 70%;
      max-width: 80%;
    }

    .check-info-action {
      display: flex;
      flex-direction: column;
      justify-content: center;
      margin-top: 50px;
      align-items: center;

      :deep(.ant-btn) {
        width: 130px;
        margin: 8px auto;
      }
    }
  }

  .check-type-action {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin-top: 20px;

    :deep(.ant-btn) {
      width: 130px;
      margin: 8px auto;
    }
  }

  .alert-tip {
    margin-bottom: 0;
    margin-right: 20px;
  }

  .sign-logo {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: 5%;
  }

  .countdown-tip {
    text-align: center;
    color: #1989fa;
    // font-size: 16px;
    margin-top: 32px;
    margin-bottom: 12px;
    letter-spacing: 1px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>

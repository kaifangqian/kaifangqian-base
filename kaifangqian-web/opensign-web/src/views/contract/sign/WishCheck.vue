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

<script lang="ts" setup>
  import { onMounted, ref, onUnmounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { Loading } from '/@/components/Loading';
  import { useUserStore } from '/@/store/modules/user';
  import defaultLogo from '/@/assets/images/logo-sign.png';
  import { Card, message } from 'ant-design-vue';
  import { confirmSignRu } from '/@/api/contract/index';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {decodeURIs,getHashQueryString} from "/@/utils"
  import cheader from "/@/layouts/default/header/index3.vue";
  import { Icon } from '/@/components/Icon';

  const { createMessage,createInfoModal } = useMessage();
  const userStore = useUserStore();
  const logoBase64 = ref(userStore.getWebConfig?.websiteOtherLogo || defaultLogo);
  const router = useRouter();
  const loading = ref(true);
  const callbackPage = getHashQueryString('callbackPage'); 
  const signResult = ref<any>(false);
  const contractInfo = ref<any>({});
  const failMsg = ref('');
  const webCopyRight = ref('');

  const countdown = ref(3); // 倒计时秒数
  let countdownTimer: any = null;
  
  onMounted(async () => {
    initData();
    await confirmSign();
  });

 const signStatus = ref<boolean | null>(null); 

  async function confirmSign() {
    try {
      const query = router.currentRoute.value.query;
      const result = await confirmSignRu({ signConfirmOrderNo: query.orderNo });
      if(result.data.success){
        //签署成功
        signStatus.value = true;
        startCountdown();
      }else{
        if(result.data.result && result.data.result.orderStatus === 0){
          //订单未完成意愿认证
          // message.info('未完成签署意愿认证，签署未完成',5);
           createInfoModal({
            iconType: 'warning',
            title: '温馨提示',
            content: '您未完成签署意愿认证，本次签署未完成，请知悉！',
            okText: '知道了',
            onOk() {
              handleBack();
            },
          });
        }else{
          //签署失败
          signStatus.value = false;
          failMsg.value = result.data.message;
        }
      }
    } catch (error) {
      signStatus.value = false;
      failMsg.value = "系统异常，请联系系统管理员";
    }
    loading.value = false;
    
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

  async function initData() {
    const web: any = userStore.getWebConfig;
    // console.log('web', web);
    webCopyRight.value = web.websiteCopyright || webCopyRight.value;
  }

  async function handleBack() {
    console.log(decodeURIs(callbackPage));
    if (callbackPage && typeof callbackPage == 'string') {
      window.open(decodeURIs(callbackPage), '_self');
      return;
    }
    router.push({
      path: '/contract/doc',
    });
  }
</script>

<template>
  <cheader/>
  <div class="contract-base-container">
    <Loading :loading="loading" :absolute="false" tip="正在签署中，请耐心等待..." />
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

    <div class="flex-container" v-if="signStatus === false">  
      <Card class="detail-card" style="width: 460px; padding: 0 0 20px 20px">
        <div style="display: flex; justify-content: center; align-items: center; margin-top: 30px;">
           <Icon icon="ant-design:warning-filled" :size="40" color="red"/>
        </div>
        <div class="card-title-fail">签署失败</div>
        <p class="fail-message" >{{ failMsg }}</p>
        <a-button class="goBack" type="primary" @click="handleBack">返回</a-button>
      </Card>
    </div>
    <div class="flex-container" v-if="signStatus">  
      <Card class="detail-card" style="width: 460px; padding: 0 0 20px 20px">
        <div style="display: flex; justify-content: center; align-items: center; margin-top: 30px;">
          <Icon icon="ant-design:check-circle-filled" :size="40" color="#1989fa"/>
        </div>
        <div class="card-title">签署成功</div>
        <div class="countdown-tip">
          <!-- <a-icon type="clock-circle" style="color:#1989fa;margin-right:4px;" /> -->
          签署成功，{{ countdown }} 秒后自动跳转...
        </div>
        <a-button class="goBack" type="primary" @click="handleBack">立即返回</a-button>
      </Card>
    </div>
    <div class="copyright-footer fixed bottom-4 left-0 w-full flex justify-center items-center">
      <p v-html="webCopyRight"></p>
    </div>
  </div>
</template>

<style lang="less" scoped>
.contract-base-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh - 10; // 确保容器占满整个视口高度
    .flex-container {
      flex: 1;
      display: flex;
      // align-items: center;
      // justify-content: center;
    }
    .full-loading {
      background-color: rgba(240, 242, 245, 1);
          
    }
    .base-logo {
      display: block;
      margin: 0 auto;
      margin-top: 80px;
      height: 80px;
    }
    .detail-card {
      /* width:700px; */
      /* height:340px; */
      // margin: 80px auto;
      padding: 25px 30px;

      // box-shadow: 0 4px 24px 0 rgba(40, 140, 242, 0.08);
      // background: #fff;
      // border-radius:8px;
      border-color: #ffffff;

      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      // box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
      border-radius: 1rem;
      margin: 0 auto; // 仅保留水平居中

      .card-title {
        font-weight: 800;
        text-align: center;
        font-size: 16px;
        color: #000;
        margin-top:10px;
        margin-bottom: 10px;
        /* 渐变字体样式 */
        background: linear-gradient(90deg, #033a71 0%, #41a5f6 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        text-fill-color: transparent;
      }

      .card-title-fail {
        font-weight: 800;
        text-align: center;
        font-size: 16px;
        color: #000;
        margin-top:10px;
        margin-bottom: 10px;
        /* 渐变字体样式 */
        background: linear-gradient(90deg, #ff0000 0%, #f05b5b 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        text-fill-color: transparent;
      }

      .fail-message {
        width: 100%;
        text-align: center;
        word-wrap: break-word;
        word-break: break-word;
        margin: 0 auto;
        padding: 0 20px; // 添加一些内边距防止贴边
      }

      .card-title-divider {
        width: 120px;
        height: 2px;
        background: linear-gradient(90deg, #033a71 0%, #41a5f6 100%);
        border-radius: 2px;
        margin: 0 auto 32px auto;
        opacity: 0.85;
      }


      .contract-info {
        margin-top: 30px;
        li {
          margin: 16px 0;
          display: flex;
          flex-direction: column;
        }
        .info-title {
          font-size: 14px;
          font-weight: 550;
          width: 100px;
          display: inline-block;
          margin-bottom: 5px;
        }
        .info-value {
          color: #888;
          flex: 1;
          display: block;
        }
      }
      .goBack {
        margin: 0 auto;
        margin-top: 40px;
        display: block;
        width: 320px;
        height: 50px;
        background: linear-gradient(90deg, #0084ff, #6ebeff);
        border-color: #ffffff;
        border-radius:8px;
      }
    }
  }
  .copyright-footer{
    // font-size: 10px;
    color: rgb(156 163 175 / var(--tw-text-opacity, 1));
    margin: 0;
    text-align: center;
    font-size: 0.75rem;
    line-height: 1rem;
  }

  .countdown-tip {
    text-align: center;
    color: #1989fa;
    font-size: 16px;
    margin-top: 32px;
    margin-bottom: 12px;
    letter-spacing: 1px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
</style>



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
  <cheader/>
  <div class="contract-base-container">
    <Loading :loading="loading" :absolute="false" />
    <!-- <div class="base-logo-box">
      <img :src="logoBase64" class="base-logo" v-if="logoBase64"/>
    </div> -->
    <!-- 主要内容区域 -->
    <main class="max-w-4xl mx-auto mt-6 md:mt-12 px-2 min-w-[600px]">
      <div class="bg-white rounded-lg shadow-xl overflow-hidden">
      <!-- 顶部状态区域 -->
        <div class="bg-gradient-to-r from-blue-500 to-blue-600 p-4 text-white">
          <h1 class="text-2xl font-medium mb-2 text-white">{{ contractInfo.subject }} {{ contractTitle }}</h1>
          <p class="text-blue-100" v-if="contractInfo.docNo" >文档编号：{{ contractInfo.docNo }}</p>
        </div>
        <!-- 信息展示区域 -->
        <div class="p-4">
          <div class="grid grid-cols-1 gap-4 md:gap-8">
            <!-- 文档信息卡片 -->
            <div class="bg-gray-50 rounded-lg p-4 md:p-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4 flex items-center">
              <!-- <FileTextOutlined class="mr-2 text-blue-500" /> -->
              <FileTextOutlined style="color: #3b82f6;" class="mr-2" />
              文档信息
              </h3>
              <div class="space-y-4">
                <div class="flex items-center text-gray-600">
                  <span class="w-24 flex-shrink-0">文档主题：</span>
                  <span class="text-gray-900">{{ contractInfo.subject }}</span>
                </div>
                <div class="flex items-center text-gray-600">
                  <span class="w-24 flex-shrink-0">文档份数：</span>
                  <span class="text-gray-900">共 {{ contractInfo.fileSum }} 份签署文档</span>
                </div>
                <div class="flex items-center text-gray-600">
                  <span class="w-24 flex-shrink-0">文档状态：</span>
                  <a-tag
                    :type="getRuStatusTagProps(contractInfo.signStatus).type"
                    :color="getRuStatusTagProps(contractInfo.signStatus).color"
                    :plain="getRuStatusTagProps(contractInfo.signStatus).plain"
                  >{{ loadRuStatus(contractInfo.signStatus) }}</a-tag>
                </div>
                <div class="flex flex-col md:flex-row text-gray-600">
                  <span class="w-24 flex-shrink-0 mb-2 md:mb-0">签署文档：</span>
                  <div 
                    class="flex flex-wrap gap-2"
                    v-for="(item, index) in contractInfo.signFileNames"
                    :key="index"
                  >
                    <div class="flex items-center bg-white rounded px-3 md:px-4 py-1 shadow-sm" style="margin-right: 15px;">
                      <FileTextOutlined class="text-blue-500 mr-2" />
                      <span class="text-gray-900 text-sm ">{{item}}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 签署信息卡片 -->
            <div class="bg-gray-50 rounded-lg p-4">
              <h3 class="text-lg font-medium text-gray-900 mb-4 flex items-center">
              <!-- <TeamOutlined class="mr-2 text-blue-500" /> -->
              <TeamOutlined style="color: #3b82f6;" class="mr-2" />
              参与方信息

              </h3>
              <div class="space-y-4">
                <div class="flex items-center text-gray-600">
                  <span class="w-24 flex-shrink-0">发起方：</span>
                  <div class="flex items-center">
                    <a-avatar size="small" style="background-color: green; color: white; margin-right: 5px;" class="mr-2">{{ contractInfo.senderName?.charAt(0) || '发' }}</a-avatar>
                    <span class="text-gray-900">{{ contractInfo.senderName }}</span>
                  </div>
                </div>
                <div class="flex flex-col md:flex-row text-gray-600">
                  <span class="w-24 flex-shrink-0 mb-2 md:mb-0">签署方：</span>
                  <div 
                    class="flex flex-wrap gap-2"
                    v-for="(item, index) in contractInfo.signers"
                    :key="index"
                  >
                    <div class="flex items-center bg-white rounded px-3 md:px-4 py-1 shadow-sm" style="margin-right: 15px;">
                      <TeamOutlined class="text-blue-500 mr-2" />
                      <span class="text-gray-900 text-sm ">{{item.signerName}}</span>
                    </div>
                  </div>
                </div>
                <div class="flex items-center text-gray-600">
                  <span class="w-24 flex-shrink-0">截止时间：</span>
                  <span class="text-gray-900">{{contractInfo.expireDate || '未设置'}}</span>
                </div>
                <div class="flex items-center text-gray-600" v-if="taskType == 'write' || taskType == 'sign' || taskType == 'approval'">
                  <span class="w-24 flex-shrink-0">{{ contractTitle }}状态：</span>
                  <a-tag>{{ taskStatusText }}</a-tag>
                </div>
              </div>
            </div>
          </div>
          <!-- 操作按钮 -->
          <div class="mt-8 flex justify-center space-x-4">
            <a-button size="large" class="!rounded-button px-12 h-11 text-base min-w-[160px]" @click="handleBack" v-if="callbackPage">
            返回
            </a-button>
            <a-button type="primary" size="large" class="!rounded-button px-8 md:px-12 h-10 md:h-11 text-sm md:text-base min-w-[160px]  md:w-auto" @click="handleSee">
            {{ taskButtonText }}
            </a-button>
          </div>
        </div>
      </div>
    </main>
    <div class="copyright-footer fixed bottom-4 left-0 w-full flex justify-center items-center">
      <p v-html="webCopyRight"></p>
    </div>
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, unref, onMounted, toRefs, reactive, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import { Loading } from '/@/components/Loading';
  import { getLinkParams, getBaseRuInfo } from '/@/api/contract';
  import { Card } from 'ant-design-vue';
  import { isMobileAgent } from '/@/utils/index';
  import { decodeURIs, getHashQueryString } from '/@/utils';
  import { getOtherLogo } from '/@/api/sys/user';
  import defaultLogo from '/@/assets/images/logo-sign.png';
  import { getToken } from '/@/utils/auth';
  import { useUserStore } from '/@/store/modules/user';
  import cheader from "/@/layouts/default/header/index3.vue";
  import { FileTextOutlined, TeamOutlined } from '@ant-design/icons-vue';
  import {
    loadRuStatus,
    loadSignColor,
    loadSignStatus,
    loadWriteStatus,
    getRuStatusTagProps, 
  } from '/@/views/contract/document/transform';
  export default defineComponent({
    name: 'ContractBase',
    components: {
      Loading,
      Card,
      cheader,
      FileTextOutlined,
      TeamOutlined
    },
    setup() {
      const contractTitle = ref('邀请');
      const compState = reactive({
        absolute: false,
        loading: false,
        tip: '',
      });
      const contractInfo = ref({
        signatory: [],
      });

      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const linkCode = route.query.code;
      const signRuId = ref(route.query.signRuId);
      const taskId = ref(route.query.taskId);
      const taskType = ref(route.query.taskType);
      const phone = ref(route.query.phone);
      const email = ref(route.query.email);
      const partyName = ref(route.query.partyName);
      const callbackPage = getHashQueryString('callbackPage');
      console.log(signRuId, route, '0-000-000');
      const userStore = useUserStore();
      const webCopyRight = ref('');
      const taskStatusText = computed(() => {
        if (taskType.value == 'write') {
          if (contractInfo.value.taskStatus === 2 && contractInfo.value.checkMenuType == 'approve') {
            return '已填写';
          } else if (
            contractInfo.value.taskStatus === 2 &&
            contractInfo.value.checkMenuType == 'reject'
          ) {
            return '已拒填';
          } else if (contractInfo.value.taskStatus === 1) {
            return '未填写';
          }
        } else if (taskType.value == 'sign') {
          if (contractInfo.value.taskStatus === 2 && contractInfo.value.checkMenuType == 'approve') {
            return '已签署';
          } else if (
            contractInfo.value.taskStatus === 2 &&
            contractInfo.value.checkMenuType == 'reject'
          ) {
            return '已拒签';
          } else if (contractInfo.value.taskStatus === 1) {
            return '未签署';
          }
        } else if (taskType.value == 'approval') {
          if (contractInfo.value.taskStatus === 2 && contractInfo.value.checkMenuType == 'approve') {
            return '审批通过';
          } else if (
            contractInfo.value.taskStatus === 2 &&
            contractInfo.value.checkMenuType == 'reject'
          ) {
            return '审批不通过';
          } else if (contractInfo.value.taskStatus === 1) {
            return '未审批';
          }
        }

        return '未知状态';
      });

      const taskButtonText = computed(() => {
        if (taskType.value == 'write') {
          return contractInfo.value.signStatus === 5 && contractInfo.value.taskStatus === 1 ? '立即填写' : '查看详情';
        } else if (taskType.value == 'sign') {
          return contractInfo.value.signStatus === 7 && contractInfo.value.taskStatus === 1 ? '立即签署' : '查看详情';
        }else if (taskType.value == 'approval') {
          return contractInfo.value.signStatus === 7 && contractInfo.value.taskStatus === 1 ? '立即审批' : '查看详情';
        }
        return '查看详情';
      });

      onMounted(() => {
        initData();
        if (isMobileAgent()) {
          compState.loading = true;
          var newURL;
          if (linkCode) {
            newURL =
              window.appInfo.mobile_app_info.url +
              '/#/base?code=' +
              linkCode +
              (callbackPage ? '&callbackPage=' + callbackPage : '');
          } else {
            let accountType = 'phone';
            let accountValue = '';
            if (phone.value) {
              accountType = 'phone';
              accountValue = phone.value as string;
            } else if (email.value) {
              accountType = 'email';
              accountValue = email.value as string;
            }
            if (callbackPage && typeof callbackPage == 'string') {
              newURL = `${window.appInfo.mobile_app_info.url}/#/base?taskType=${taskType.value}&${accountType}=${accountValue}&signRuId=${signRuId.value}&taskId=${taskId.value}&callbackPage=${callbackPage}`;
            } else {
              newURL = `${window.appInfo.mobile_app_info.url}/#/base?taskType=${taskType.value}&${accountType}=${accountValue}&signRuId=${signRuId.value}&taskId=${taskId.value}`;
            }
          }
          // var newURL = 'https://h5.kaifangqian.com/#/base?code=' + linkCode;
          // 重定向到新的 URL
          setTimeout(() => {
            window.location.href = newURL;
          });
          return;
        }
        if (linkCode) {
          compState.loading = true;
          init();
        } else {
          noLinkCode();
        }
      });

      async function initData() {
        const web: any = userStore.getWebConfig;
        console.log('web', web);
        webCopyRight.value = web.websiteCopyright || webCopyRight.value;
      }

      function initTastType(taskType) {
        console.log('taskType:', taskType);
        if (taskType == 'write') {
          contractTitle.value = '填写';
        }
        if (taskType == 'sign') {
          contractTitle.value = '签署';
        }
        if (taskType == 'copy') {
          contractTitle.value = '抄送';
        }
        if (taskType == 'approval') {
          contractTitle.value = '审批';
        }
      }

      async function init() {
        let params = await getLinkParams({ code: linkCode });
        if (params) {
          // let params = JSON.parse(result);
          console.log(params, '---测试机11 -');
          signRuId.value = params.signRuId;
          taskId.value = params.taskId;
          taskType.value = params.taskType;
          phone.value = params.phone;
          email.value = params.email;
          partyName.value = params.tenantName;
          if (signRuId.value) {
            await getContrctInfo();
          }
          initTastType(params.taskType);
          // router.replace({
          //     query:{
          //       signRuId:result.signRuId,
          //       taskId:result.taskId,
          //       taskType:result.taskType,
          //     }
          //   })
          compState.loading = false;
          if (params.noLogin) {
            await userStore.singleSign(params);
            handleSee();
          }
        }
      }

      async function noLinkCode() {
        // const signRuId = ref(route.query.signRuId);
        //const taskId = ref(route.query.taskId);
        //const taskType = ref(route.query.taskType);
        // taskType=sign&phone=13793014727&signRuId=223ccb7e-d7a5-4f69-801b-5e51ab7fde3d&taskId=ca7b5c22-7d00-4233-8287-3a17d865cd16

        if (route.query.signRuId && route.query.taskType) {
          compState.loading = true;
          initTastType(taskType.value);
          getContrctInfo();
          compState.loading = false;
        }
      }
      async function getContrctInfo() {
        let result = await getBaseRuInfo({ signRuId: signRuId.value, signTaskId: taskId.value});
        if (result) {
          contractInfo.value = result;
        }
      }
      function handleSee() {
        const token = getToken() as string;
        let appAuthInfo = window.appInfo.sign_app_info;
        // let appTenantInfo = window.appInfo.tenant_app_info;
        let paramsStr = '';
        let redirectPath = '';
        let accountType = 'phone';
        let accountValue = '';
        if (phone.value) {
          accountType = 'phone';
          accountValue = phone.value as string;
        } else if (email.value) {
          accountType = 'email';
          accountValue = email.value as string;
        }

        if (taskType.value == 'write') {
          redirectPath = '/#/contract/params';
          paramsStr =
            '__full__&signRuId=' +
            signRuId.value +
            '&partyName=' +
            partyName.value +
            '&taskId=' +
            taskId.value +
            '&callbackPage=' +
            encodeURIComponent(callbackPage) +
            '&' +
            accountType +
            '=' +
            accountValue +
            '&from=list&type=receive';
        }
        if (taskType.value == 'sign') {
          redirectPath = '/#/contract/sign';
          paramsStr =
            '__full__&signRuId=' +
            signRuId.value +
            '&partyName=' +
            partyName.value +
            '&taskId=' +
            taskId.value +
            '&callbackPage=' +
            encodeURIComponent(callbackPage) +
            '&' +
            accountType +
            '=' +
            accountValue +
            '&from=list';
        }
        if (taskType.value == 'approval') {
          redirectPath = '/#/contract/approval';
          paramsStr =
            '__full__&signRuId=' +
            signRuId.value +
            '&partyName=' +
            partyName.value +
            '&taskId=' +
            taskId.value +
            '&callbackPage=' +
            encodeURIComponent(callbackPage) +
            '&' +
            accountType +
            '=' +
            accountValue +
            '&from=list';
        }
        if (taskType.value == 'copy') {
          redirectPath = '/#/contract/detail/sign';
          paramsStr =
            '__full__&signRuId=' +
            signRuId.value +
            '&partyName=' +
            partyName.value +
            '&taskId=' +
            taskId.value +
            '&callbackPage=' +
            encodeURIComponent(callbackPage) +
            '&' +
            accountType +
            '=' +
            accountValue +
            '&from=list';
        }
        // 前提:短链接参数变量中存在账号身份
        // 已登录
        // 使用当前登录身份，进入目标页面
        // 逻辑
        // 未登录
        // 查询当前登录账号的身份列表，验证短链接中的账号身份是否包含在列表中:
        // (1)包含:自动切换到该身份下，进入目标页面;
        // (2)不包含:用户选择身份，进入目标页面;
        if (token) {
          window.location.href = appAuthInfo.url + redirectPath + '?' + paramsStr;
        } else {
          window.location.href =
            appAuthInfo.url +
            '/#/auth/login?' +
            paramsStr +
            '&redirect=' +
            encodeURIComponent(appAuthInfo.url + redirectPath);
        }
      }
      const logoBase64 = ref('');
      async function getLogo() {
        getOtherLogo({}).then((res) => {
          if (res) {
            logoBase64.value = res.image;
          }
        });
      }
      getLogo();

      async function handleBack() {
        if (callbackPage && typeof callbackPage == 'string') {
          window.open(decodeURIs(callbackPage), '_self');
          return;
        }
      }
      return {
        ...toRefs(compState),
        contractTitle,
        contractInfo,
        handleSee,
        logoBase64,
        webCopyRight,
        loadRuStatus,
        loadSignColor,
        loadSignStatus,
        loadWriteStatus,
        taskType,
        callbackPage,
        handleBack,
        taskStatusText,
        taskButtonText,
        getRuStatusTagProps,
      };
    },
  });
</script>

<style lang="less" scoped>
  .contract-base-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh - 10; // 确保容器占满整个视口高度
    .flex-container {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
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
      box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
      border-radius: 1rem;
      margin: 0 auto; // 仅保留水平居中

      .card-title {
        font-weight: 800;
        text-align: center;
        font-size: 24px;
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
        width: 160px;
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
      .contract-see {
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


</style>

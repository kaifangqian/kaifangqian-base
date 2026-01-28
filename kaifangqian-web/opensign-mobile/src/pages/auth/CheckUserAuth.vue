<script setup lang="ts">
  // import { Steps, Step, Button, Tag } from 'ant-design-vue';
  // import DocHeader from '/@/views/contract/components/layouts/DocHeader.vue';
  import ExpandHeader from '@/layout/Header/ExpandHeader.vue';
  import { useUserStore } from '@/store/modules/user';
  import { onMounted, h, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { handleAuth as handleUserAuth } from '@/pages/auth/index';
  // import { handleAuth } from '/@/views/sys/user';
  // import { Icon } from '/@/components/Icon';
  const userStore = useUserStore();
  const userInfo = userStore.getUserInfo;
  const tenantInfo = userStore.getTenantInfo;
  const router = useRouter();
  const stepActive = ref(0);
  function handleCancel() {
    router.go(-1);
  }
  const pageShow = ref(false);
  onMounted(() => {
    pageShow.value = !(userInfo.personalTenant.authStatus === 2 && tenantInfo.authStatus === 2);
    if (!pageShow.value) {
      toRedirect();
    }
  });

  function toRedirect() {
    const redirect = router.currentRoute.value.query.redirect as string;
    const { signRuId, taskId } = router.currentRoute.value.query;
    if (redirect) {
      router.replace({
        path: decodeURIComponent(redirect),
        query: {
          signRuId,
          taskId,
        },
      });
    }
  }
  async function toAuth(loginTenantType: number) {
    if (loginTenantType === 2) {
      //个人
      handleUserAuth(2, location.href);
      console.log('geren');
    }
    if (loginTenantType === 1) {
      handleUserAuth(1, location.href);
      console.log('eeeee');
    }
  }
</script>

<template>
  <div class="layout" style="background-color: #f5f5f5">
    <!-- <Loading :loading="loading" text="加载中..." /> -->
    <ExpandHeader
      rightText="去认证"
      @click="toAuth(userInfo.personalTenant.authStatus == 2 ? 1 : 2)"
    />
    <div class="content" style="padding: 10px">
      <div class="check-auth-container" v-if="pageShow">
        <!-- <DocHeader :actions="[]" @cancel="handleCancel" :showDoc="false">
      <span v-if="tenantInfo.tenantType === 1">企业实名认证</span>
      <span v-if="tenantInfo.tenantType === 2">个人实名认证</span>
    </DocHeader> -->
        <div class="check-auth-body">
          <div class="auth-header">
            <!-- <Steps>
              <Step
                title="经办人"
                :status="userInfo.personalTenant.authStatus == 2 ? 'finish' : 'wait'"
              >
              </Step>
              <Step title="企业" :status="tenantInfo.authStatus == 2 ? 'finish' : 'wait'"> </Step>
            </Steps> -->
            <van-steps :active="stepActive">
              <van-step>经办人</van-step>
              <van-step>企业</van-step>
            </van-steps>
          </div>
          <!-- <div class="text-sm font-semibold text-center">
        根据《电子签名法》规定，签发合同需完成实名认证，以保障合同法律效率
      </div> -->
          <div class="auth-body">
            <div class="auth-item">
              <div class="auth-item-header flex">
                <div class="tag-step">步骤一</div>
                <div class="header-text">经办人认证 </div>
                <div class="header-status flex-center">
                  <van-tag
                    size="medium"
                    type="success"
                    v-if="userInfo.personalTenant.authStatus == 2"
                    >已认证</van-tag
                  >
                  <van-tag size="medium" type="warning" v-else>未认证</van-tag>
                </div>
              </div>
              <div class="auth-item-content">
                <p>为确保企业认证的真实性，首先需要验证经办人身份信息</p>
                <p style="color: #a1a1aa">我们将通过权威数据源验证您的身份信息</p>
              </div>
              <!-- <div class="py-[10px] px-[20px]">
            <div class="w-full bg-[#EFF6FF] text-sm">
              <div class="py-[5px] flex gap-4">
                <ExclamationCircleOutlined style="font-size: 24px;" />
                <p style="line-height: 24px;margin: 0;">经办人未实名</p>
              </div>
               v-if="userInfo.personalTenant.authStatus !== 2"
              
            </div>
          </div> -->
              <div class="bottom-btn">
                <van-button
                  v-if="userInfo.personalTenant.authStatus !== 2"
                  type="primary"
                  size="small"
                  @click="toAuth(2)"
                  style="background-color: #2563eb !important; border: 0; width: 100%"
                  >去认证</van-button
                >
              </div>
            </div>

            <div class="w-[700px] auth-item mt-[20px] pb-[20px]">
              <div class="auth-item-header">
                <div class="tag-step">步骤二</div>
                <div class="header-text text-[16px] font-bold p-0">企业认证 </div>
                <div class="header-status flex-center">
                  <van-tag size="medium" type="success" v-if="tenantInfo.authStatus == 2"
                    >已认证</van-tag
                  >
                  <van-tag size="medium" type="warning" v-else>未认证</van-tag>
                </div>
              </div>
              <div class="auth-item-content">
                <p>为确保企业认证的真实性，首先需要验证经办人身份信息</p>
                <p style="color: #a1a1aa">我们将通过权威数据源验证您的企业信息</p>
              </div>

              <div class="entp-info">
                <div style="text-align: center">
                  <SvgIcon name="entp-2" :size="40" />
                  <p style="font-weight: 600">企业信息验证</p>
                  <p style="color: #a1a1aa"
                    >系统将自动验证您的企业工商信息，请确保填写的信息准确无误</p
                  >
                  <div
                    style="border: 1px solid #dbeafe; background-color: #eff6ff; padding: 20px 0"
                  >
                    <p style="padding: 0 10px; color: #1d4ed8"
                      >您将作为认证申请人，在「云盾系统」完成对「{{
                        tenantInfo.name
                      }}」的企业实名认证，完成认证后，您将作为该企业的管理员</p
                    >
                  </div>
                </div>
              </div>
              <div class="bottom-btn">
                <van-button
                  type="primary"
                  size="small"
                  style="
                    background-color: #2563eb !important;
                    border: 0;
                    width: 100%;
                    margin-top: 10px;
                  "
                  v-if="userInfo.personalTenant.authStatus == 2 && tenantInfo.authStatus !== 2"
                  @click="toAuth(1)"
                  >去认证</van-button
                >
              </div>
            </div>

            <!-- <div class="w-[700px] auth-item mt-[20px] flex flex-row">
          <div class="flex-1">
            <p class="text-[16px] font-bold p-0">企业认证</p>

            <p class="text-zinc-400">为确保企业认证的真实性，首先需要验证经办人身份信息。</p>
            <p>我们将通过权威数据源验证您的身份信息</p>
            
          </div>
          <div class="w-[120px] flex items-center justify-center">
            <Button type="primary" @click="toAuth(1)" ghost v-if="tenantInfo.authStatus !== 2"
              >去认证</Button
            >
            <Tag color="success" v-else>已认证</Tag>
          </div>
        </div> -->
            <div class="w-full flex justify-center py-5">
              <van-button
                type="primary"
                style="width: 120px"
                v-if="userInfo.personalTenant.authStatus === 2 && tenantInfo.authStatus === 2"
                >去签署</van-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
  .check-auth-container {
    // display: flex;
    // gap: 20px;
    // padding: 20px;
    // background-color: #e5e5e5;
    width: 100%;
    // height: calc(100% - 100px);
    overflow-y: auto;
    padding: 10px 0;
    // margin: 0 auto;
    .check-auth-body {
      // width: 1200px;
      margin: 0 auto;
      // background-color: #fff;
      // height: calc(100vh - 100px);
      // box-shadow: 1px 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
      // box-shadow: 5px 5px 30px #0000000e;
      // border-radius: 10px;
      // padding: 20px;
      // border: 1px solid #f0f0f0;
      .auth-item {
        border-radius: 5px;
        box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
        border: 1px solid #f0f0f0;
        margin-top: 20px;
        background-color: #fff;
        padding-bottom: 20px;
        // color: #f0a128;
        // padding: 10px;
      }
      .auth-header {
        border: 1px solid #f0f0f0;
        border-radius: 10px;
        box-shadow: 5px 5px 30px #0000000e;
        background-color: #fff;
        padding: 0 80px;
      }
      .auth-body {
        margin-top: 10px;
      }
    }
  }
  .auth-item-header {
    display: flex;
    background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)),
      linear-gradient(90deg, #dbeafe 0%, #ffffff 100%);
    flex-direction: row;
    color: #000;
    // height: 40px;
    padding: 10px 20px;
    gap: 10px;
    border-bottom: 1px solid #f0f0f0;
    .tag-step {
      background-color: #2563eb;
      padding: 2px 15px;
      color: #fff;
      border-radius: 13px;
    }
    .header-text {
      font-size: 0.35rem;
      font-weight: 600;
      padding: 2px 15px;
    }
    .header-status {
      // font-size: 0.35rem;
      // padding: 2px 15px;
    }
  }
  .bottom-btn {
    padding: 0 20px 0;
  }
  .auth-item-content {
    font-size: 0.35rem;
    padding: 40px 20px 20px;
    p {
      color: #3f3f46;
      line-height: 0.6rem;
    }
  }
  .entp-info {
    padding: 30px 20px 20px;
    font-size: 0.35rem;
    text-align: center;
    background-color: #f9fafb;
    width: calc(100% - 40px);
    margin: 0 auto;
    p {
      line-height: 0.6rem;
      padding: 0 50px;
    }
  }
  :deep(.van-steps) {
    border-radius: inherit !important;
  }
  :deep(.van-tag) {
    line-height: inherit !important;
  }
</style>

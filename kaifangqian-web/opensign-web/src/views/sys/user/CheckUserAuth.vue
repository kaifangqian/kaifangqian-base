<script setup lang="ts">
  import { Steps, Step, Button, Tag } from 'ant-design-vue';
  import DocHeader from '/@/views/contract/components/layouts/DocHeader.vue';

  import { useUserStore } from '/@/store/modules/user';
  import { onMounted, h, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { handleAuth } from '/@/views/sys/user';
  import { Icon } from '/@/components/Icon';
  const userStore = useUserStore();
  const userInfo = userStore.getUserInfo;
  const tenantInfo = userStore.getTenantInfo;
  const router = useRouter();
  function handleCancel() {
    router.go(-1);
  }
  const actions = [];

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
          __full__: '',
          signRuId,
          taskId,
        },
      });
    }
  }
  async function toAuth(loginTenantType) {
    if (loginTenantType === 2) {
      //个人
      handleAuth(2, location.href);
    }
    if (loginTenantType === 1) {
      handleAuth(1, location.href);
    }
  }
</script>

<template>
  <div class="check-auth-container" v-if="pageShow">
    <DocHeader :actions="actions" @cancel="handleCancel" :showDoc="false">
      <span v-if="tenantInfo.tenantType === 1">企业实名认证</span>
      <span v-if="tenantInfo.tenantType === 2">个人实名认证</span>
    </DocHeader>
    <div class="check-auth-body">
      <div class="auth-header">
        <Steps>
          <Step
            title="经办人"
            :status="userInfo.personalTenant.authStatus == 2 ? 'finish' : 'wait'"
          >
          </Step>
          <Step title="企业" :status="tenantInfo.authStatus == 2 ? 'finish' : 'wait'"> </Step>
          <!-- wait finish -->
        </Steps>
      </div>
      <!-- <div class="text-sm font-semibold text-center">
        根据《电子签名法》规定，签发合同需完成实名认证，以保障合同法律效率
      </div> -->
      <div class="w-[700px] my-0 mx-auto pt-5">
        <div class="w-[700px] auth-item mt-[20px] pb-[20px]">
          <div class="auth-item-header flex">
            <div class="tag-step">步骤一</div>
            <div class="header-text text-[16px] font-bold p-0">经办人认证 </div>
            <div class="pt-[3px]">
              <Tag color="success" v-if="userInfo.personalTenant.authStatus == 2">已认证</Tag>
              <Tag color="warning" v-else>未认证</Tag>
            </div>
          </div>
          <div class="auth-item-content">
            <p class="text-zinc-700 m-0">为确保企业认证的真实性，首先需要验证经办人身份信息</p>
            <p class="text-zinc-400 m-0">我们将通过权威数据源验证您的身份信息</p>
          </div>
          <!-- <div class="py-[10px] px-[20px]">
            <div class="w-full bg-[#EFF6FF] text-sm">
              <div class="py-[5px] flex gap-4">
                <ExclamationCircleOutlined style="font-size: 24px;" />
                <p style="line-height: 24px;margin: 0;">经办人未实名</p>
              </div>
              
              
            </div>
          </div> -->
          <div class="px-[20px] pt-[10px] text-center">
            <Button
              type="primary"
              @click="toAuth(2)"
              v-if="userInfo.personalTenant.authStatus !== 2"
              style="background-color: #2563eb !important; border: 0; width: 100%"
              >去认证</Button
            >
          </div>
        </div>

        <div class="w-[700px] auth-item mt-[20px] pb-[20px]">
          <div class="auth-item-header flex">
            <div class="tag-step">步骤二</div>
            <div class="header-text text-[16px] font-bold p-0">企业认证 </div>
            <div class="pt-[3px]">
              <Tag color="success" v-if="tenantInfo.authStatus == 2">已认证</Tag>
              <Tag color="warning" v-else>未认证</Tag>
            </div>
          </div>
          <div class="auth-item-content">
            <p class="text-zinc-700 m-0">为确保企业认证的真实性，首先需要验证经办人身份信息</p>
            <p class="text-zinc-400 m-0">我们将通过权威数据源验证您的企业信息</p>
          </div>

          <div class="px-[20px]">
            <div class="bg-[#F9FAFB] pt-[50px] pb-[10px] text-center">
              <Icon icon="entp-2|svg" :size="40"></Icon>
              <p class="text-[16px] font-bold">企业信息验证</p>
              <p class="text-zinc-400">系统将自动验证您的企业工商信息，请确保填写的信息准确无误</p>
              <div class="py-5 px-3 w-[80%] mx-auto bg-[#EFF6FF]" style="border: 1px solid #dbeafe">
                <p class="m-0 text-[#1D4ED8]"
                  >您将作为认证申请人，在「云盾系统」完成对「{{
                    tenantInfo.name
                  }}」的企业实名认证，完成认证后，您将作为该企业的管理员</p
                >
              </div>
            </div>
          </div>
          <div class="px-[20px] pt-[10px] text-center">
            <Button
              type="primary"
              style="background-color: #2563eb !important; border: 0; width: 100%"
              v-if="userInfo.personalTenant.authStatus == 2 && tenantInfo.authStatus !== 2"
              @click="toAuth(1)"
              >去认证</Button
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
          <Button
            type="primary"
            style="width: 120px"
            v-if="userInfo.personalTenant.authStatus === 2 && tenantInfo.authStatus === 2"
            >去签署</Button
          >
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
    // margin: 0 auto;
    .check-auth-body {
      width: 1200px;
      margin: 0 auto;
      // background-color: #fff;
      height: calc(100vh - 100px);
      // box-shadow: 1px 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
      box-shadow: 5px 5px 30px #0000000e;
      border-radius: 10px;
      // padding: 20px;
      border: 1px solid #f0f0f0;
      .auth-item {
        border-radius: 5px;
        box-shadow: 5px 5px 30px #0000000e;
        border: 1px solid #f0f0f0;
        // color: #f0a128;
        // padding: 10px;
      }
      .auth-header {
        border-bottom: 1px solid #f0f0f0;
        padding: 10px 300px;
      }
    }
  }
  .auth-item-header {
    background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)),
      linear-gradient(90deg, #eff6ff 0%, #ffffff 100%);
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
      line-height: 26px;
    }
  }
  .auth-item-content {
    padding: 10px 20px;
    p {
      line-height: 30px;
    }
  }
  :deep(.ant-steps-item-title) {
    font-size: 14px;
  }
</style>

<template>
  <div class="activated-container">
    <div class="header">
      <h2 class="title">静默签署服务</h2>
      <ul class="description">
        <li> 为提高签署便捷性，云盾提供了静默签署服务。 </li>
        <li>
          开通服务并完成认证后，在授权有效期内，允许在本系统中使用本公司的数字证书完成自动静默签署，无需进行签署意愿校验，静默签署时即代表本公司的真实签署意愿。
        </li>
      </ul>
    </div>
    <img src="/src/assets/images/activated-icon.png" alt="Activated Icon" class="status-icon" />
    <p class="expiry-date">服务截止日期：{{ expiryDate }}</p>
    <!-- <a-button class="close-service" @click="deactivateService">关闭服务</a-button> -->
    <a-button
      class="close-service"
      type="primary"
      @click="deactivateService"
      v-if="userInfo.tenantUserId == tenantInfo.applyTenantUser"
      >关闭服务</a-button
    >
    <a-tooltip>
      <template #title>您不是企业管理员，无法关闭静默签署服务</template>
      <a-button
        class="close-service"
        type="primary"
        @click="deactivateService"
        disabled
        v-if="userInfo.tenantUserId != tenantInfo.applyTenantUser"
        >关闭服务</a-button
      >
    </a-tooltip>
    <a-button class="open-service-log" @click="viewActivationRecords">开通记录</a-button>

    <SilentSignRecordsModal @register="registerModal" />
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { silentCloseApi } from './api';
  import SilentSignRecordsModal from './SilentSignRecordsModal.vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useUserStore } from '/@/store/modules/user';

  const emit = defineEmits(['reload']);

  const [registerModal, { openModal, closeModal }] = useModal();
  const userStore = useUserStore();
  const userInfo = userStore.getUserInfo;
  const tenantInfo: any = ref({
    ...userStore.getTenantInfo,
  });

  const props = defineProps({
    expiryDate: {
      type: String,
      default: '',
    },
  });
  const { createSuccessModal, createWarningModal, createConfirm } = useMessage();
  const deactivateService = async () => {
    createConfirm({
      iconType: 'info',
      title: '温馨提示',
      content: '关闭服务后，将无法使用静默签署服务，请确认是否继续关闭服务？',
      async onOk() {
        const result = await silentCloseApi({});
        createSuccessModal({
          title: '温馨提示',
          content: '静默签署服务已关闭',
          onOk: () => {
            emit('reload');
          },
        });
      },
    });
  };

  const viewActivationRecords = async () => {
    openModal(true, {});
  };
</script>

<style scoped>
  .activated-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
  }

  .header {
    padding: 20px;
    width: 100%;
    text-align: center;
    border-radius: 4px;
    background-color: #e6f8ff;
    border: 1px solid #8dd4f7;
    margin-bottom: 40px;
  }

  .title {
    font-size: 14px;
    color: #000;
    /* margin-bottom: 10px; */
    text-align: left;
  }

  .description {
    font-size: 14px;
    color: #141e31;
    text-align: left;
  }

  .status-icon {
    margin-top: 40px;
    width: 100px;
    /* height: 100px; */
    margin-bottom: 20px;
  }

  .expiry-date {
    font-size: 14px;
    color: #666;
    margin-bottom: 30px;
  }

  .close-service {
    margin-top: 20px;
    width: 200px;
    height: 40px;
    /* background: #141e31;*/
    background-color: rgba(255, 1, 1, 0.2);
    border-color: transparent;
    /* color: #ff0101; */
    text-align: center;
    justify-content: center;
  }

  .activated-container .close-service {
    color: #ff0101; /* 更高优先级 */
  }

  .open-service-log {
    margin-top: 10px;
    width: 100px;
    height: 40px;
    color: #333;
    border: none;
    text-align: center;
    justify-content: center;
  }
</style>

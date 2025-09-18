<template>
  <div class="activated-container">
    <div class="header">
      <h2 class="title">免意愿快捷签署服务</h2>
      <ul class="description">
        <li> 为提高签署便捷性，云盾提供了免意愿快捷签署服务。 </li>
        <li>
          开通服务并完成认证后，您的授权有效期内，​当发起方选择此方式时，您可直接签署合同，无需额外验证意愿。
        </li>
      </ul>
      <!-- <p class="description">
        为提高签署便捷性，云盾提供了免意愿快捷签署服务。开通服务并完成认证后，您的授权有效期内，​当发起方选择此方式时，您可直接签署合同，无需额外验证意愿
      </p> -->
    </div>
    <img src="@/assets/images/activated-icon.png" alt="Activated Icon" class="status-icon" />
    <p class="expiry-date">服务截止日期：{{ expiryDate }}</p>
    <a-button class="close-service" @click="deactivateService">关闭服务</a-button>
    <a-button class="open-service-log" @click="viewActivationRecords">开通记录</a-button>

    <FreeSignRecordsModal @register="registerModal" />
  </div> 
</template>

<script setup>
  import { ref } from 'vue';

  import FreeSignRecordsModal from './FreeSignRecordsModal.vue'
  import { quickCloseApi } from '/@/api/yundun/quick';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';

  const emit = defineEmits(["reload"]);

  const [registerModal, { openModal, closeModal }] = useModal();
  const {createConfirm,createSuccessModal} = useMessage();
  const props = defineProps({
    expiryDate:{
      type: String,
      default:""
    },
  });

  const deactivateService = async () => {
    
    createConfirm({
      iconType: 'info',
      title: '温馨提示',
      content: '关闭服务后，将无法使用免意愿快捷签署服务，请确认是否继续关闭服务？',
      async onOk() {
        const result = await quickCloseApi();
        createSuccessModal({
        title: '温馨提示',
        content: '免意愿快捷签署服务已关闭',
        onOk: () => {
          // closeModal();
          emit("reload");
        },
      });
      }
    })
  };

  const viewActivationRecords = () => {
    openModal(true,{});
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
    color: #ff0101;
    text-align: center;
    justify-content: center;
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

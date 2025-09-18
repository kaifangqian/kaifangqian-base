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
  <div class="not-activated-container">
    <img src="@/assets/images/activate-service-icon.png" alt="activated Icon" class="status-icon" />
    <h2 class="title">免意愿快捷签署服务</h2>
    <p class="description-title">
      <span>让签约更便捷</span>
      <p class="description">
        开通服务并完成认证后，您的授权有效期内，​当发起方选择此方式时，您可直接签署合同，无需额外验证意愿
      </p>
    </p>

    <a-button class="open-service" type="primary" @click="activateService">开通服务</a-button>
    <a-button class="open-service-log" @click="viewActivationRecords">开通记录</a-button>

    <FreeSignRecordsModal @register="registerModal" />
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import { quickOpenApi, quickRecordQueryApi } from '/@/api/yundun/quick';
  import FreeSignRecordsModal from './FreeSignRecordsModal.vue';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  
  const { createConfirm, createSuccessModal } = useMessage();

  const [registerModal, { openModal, closeModal }] = useModal();
  const compState = ref({
    loading: false,
  });
  const activateService = async () => {
    // TODO: Implement activate service logic
    // quickOpenApi();
    createConfirm({
      iconType: 'info',
      title: '温馨提示',
      content: '确定开通快捷签署授权？',
      async onOk() {
        compState.value.loading = true;
        const asyncPage = window.location.origin + '/#/user/centerInfo?index=4';
        const result = await quickOpenApi({ callbackPage: asyncPage });
        if (result.status === 0) {
          compState.value.loading = false;
          window.open(result.openUrl, '_self');
        } else if (result.status === 1) {
          // await userStore.getTnantInfo();
          compState.value.loading = false;
          window.open(asyncPage, '_self');
        }
      },
    });
  };

  const viewActivationRecords = async () => {
    // TODO: Implement view activation records logic
    openModal(true, {});
  };
</script>

<style scoped>
  .not-activated-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    margin-top: 120px;
  }

  .status-icon {
    width: 100px;
    height: 100px;
    margin-bottom: 20px;
  }

  .title {
    font-size: 24px;
    color: #000;
    /* margin-bottom: 20px; */
  }

  .description-title {
    font-size: 20px;
    color: #000;
    text-align: center;
    margin-bottom: 20px;
    width: 60%;
    /* font-weight: 600; */
  }

  .description {
    margin-top: 10px;
    font-size: 14px;
    color: #929292;
    text-align: center;
  }

  .open-service {
    margin-top: 20px;
    width: 200px;
    height: 40px;
    background: linear-gradient(to right, #1890ff, #40a9ff);
    border-color: transparent;
    /* font-size: 16px; */
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

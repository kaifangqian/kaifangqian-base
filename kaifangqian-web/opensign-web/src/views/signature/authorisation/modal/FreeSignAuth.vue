<template>
  <div class="free-sign-auth">
    <!-- <div class="header">
      <h2>免意愿快捷签署服务</h2>
      <p>
        为提高签署便捷性，云盾提供了免意愿快捷签署服务，同意开通该服务，并完成认证操作后，在授权有效期内，在本系统中签署合同时，当发起方允许您免意愿签署时，无需进行额外的意愿认证
      </p>
    </div> -->

    <Unauthenticated :index="4" v-if="status === 'unauthenticated'" @reload="handleAuthenticated" />

    <NotActivated v-else-if="status === 'notActivated'" @activated="handleActivated" />

    <Activated
      v-else-if="status === 'activated'"
      :expiryDate="expiryDate"
      @reload="initData"
    />
  </div>
</template>

<script setup>
  import { onMounted, ref } from 'vue';
  import Unauthenticated from './Unauthenticated.vue';
  import NotActivated from './FreeSignNotActivated.vue';
  import Activated from './FreeSignActivated.vue';
  import { useUserStore } from '/@/store/modules/user';
  import {quickQueryApi} from '/@/api/yundun/quick';

  const status = ref(''); // 'unauthenticated', 'notActivated', 'activated'
  const expiryDate = ref('');

  const userStore = useUserStore();
  const handleAuthenticated = () => {
    status.value = 'notActivated';
  };

  const handleActivated = () => {
    status.value = 'activated';
  };

  const handleDeactivated = () => {
    status.value = 'notActivated';
  };

  async function initData() {
    // status.value = 'notActivated';
    if (userStore.tenantInfo.authStatus !== 2) {
      // 未实名
      status.value = 'unauthenticated';
      return;
    }
    const result = await quickQueryApi();
    if (result.status === 0) {
      // 未开通
      status.value = 'notActivated';
    } else if (result.status === 1) {
      if(result.deadline && result.deadline == "FOREVER_VALID"){
        expiryDate.value = "长期有效";
      }else{
        expiryDate.value = result.deadline;
      }
      status.value = 'activated';
    }
  }

  onMounted(() => {
    initData();
  });
</script>

<style scoped>
  .free-sign-auth {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
  }

  .header {
    text-align: center;
    margin-bottom: 40px;
  }
</style>

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
  <div class="silent-sign-auth">
    <Unauthenticated
      :index="5"
      v-if="status === 'unauthenticated'"
      @authenticated="handleAuthenticated"
    />

    <NotActivated v-else-if="status === 'notActivated'" @reload="initData" />
    <!-- <NotActivated /> -->

    <Activated v-else-if="status === 'activated'" :expiryDate="expiryDate" @reload="initData" />
    <!-- <Activated /> -->
  </div>
</template>

<script setup lang="ts">
  import { onMounted, ref } from 'vue';
  import Unauthenticated from './Unauthenticated.vue';
  import NotActivated from './SilentSignNotActivated.vue';
  import Activated from './SilentSignActivated.vue';
  import { useUserStore } from '/@/store/modules/user';
  import { silentQueryApi, silentQueryRecordApi } from './api';
  import { any } from 'vue-types';

  const status = ref(''); // 'unauthenticated', 'notActivated', 'activated'
  const expiryDate = ref('');
  const userStore = useUserStore();
  const handleAuthenticated = () => {
    status.value = 'notActivated';
  };

  async function initData() {
    // alert(userStore.tenantInfo.authStatus);
    if (userStore.tenantInfo.authStatus !== 2) {
      // 未实名
      status.value = 'unauthenticated';
      return;
    }
    const result = await silentQueryApi();
    console.log('result', result);
    if (result.status === 0) {
      // 未开通
      status.value = 'notActivated';
    } else if (result.status === 1) {
      if (result.deadline && result.deadline == 'FOREVER_VALID') {
        expiryDate.value = '长期有效';
      } else {
        expiryDate.value = result.deadline;
      }
      status.value = 'activated';
    }
  }
  onMounted(() => {
    initData();
  });
</script>

<style lang="less" scoped>
  .silent-sign-auth {
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

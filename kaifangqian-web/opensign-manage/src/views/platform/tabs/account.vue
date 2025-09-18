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
  <Loading :loading="loading" :absolute="false" />
  <div class="tab-item">
    <!-- <div class="item-operation">
      <a-button type="primary" @click="saveConfig">保存</a-button>
    </div> -->
    <div class="item-from">
      <div>
        <span style="font-family: 600; font-size: 14px">账号类型</span>
        <span style="font-size: 12px; color: rgba(0, 0, 0, 0.4)"
          >(支持配置注册账号类型，配置后，用户可使用对应的账号类型完成注册)</span
        >
      </div>
      <div style="padding: 20px 0">
        <a-radio-group v-model:value="formState.system_account_type" @change="saveConfig">
          <a-radio value="phone">手机号</a-radio>
          <a-radio value="email">邮箱</a-radio>
          <a-radio value="phone_email">手机号或邮箱</a-radio>
        </a-radio-group>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { getAppEnvConfig } from '/@/utils/env';
  import { getSafeConfig, setSafeConfig } from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import { message } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  import { getSystemLimit } from '/@/api/license';
  const formState = reactive({
    system_account_type: 'phone',
  });
  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const fileList1: any = ref([]);
  const fileList2: any = ref([]);
  const handlePreview = (file) => {};
  // const environment = ref('test');

  onMounted(() => {
    initData();
  });

  const configKeys = ['system_account_type'];

  async function saveConfig() {
    try {
      loading.value = true;
      // const values = await formRef.value.validateFields();
      configData.value.forEach((item: any) => {
        item.value = formState[item.type];
      });
      const result = await setSafeConfig({ sysConfigs: configData.value });
      if (result.success) {
        message.success('保存成功');
      } else {
        message.success(result.message);
      }
      // console.log("formState：",formState);
      loading.value = false;
    } catch (errorInfo) {
      loading.value = false;
    }
  }
  async function initData() {
    var params = configKeys.join(',');
    const result = await getSafeConfig({ types: params });
    if (result && result.length > 0) {
      configData.value = result;
      for (var i = 0; i < result.length; i++) {
        var item = result[i];
        formState[item.type] = item.value;
      }
    }
    // const limit = await getSystemLimit();
    // environment.value = limit.environment;
    console.log(formState);
  }
</script>

<style lang="less" scoped>
  .tab-item {
    width: 1200px;
    margin-left: 20px;

    .item-operation {
      text-align: right;
      line-height: 60px;
    }

    .item-from {
    }

    .logo-tips {
      font-size: 12px;
      color: rgba(0, 0, 0, 0.6);
      line-height: 40px;
    }
  }

  .preview-logo {
    width: 140px;
    height: 60px;
    padding: 5px;
    box-sizing: border-box;
    border: 1px solid #eee;
    background-color: #002b45;

    img {
      width: 100%;
      height: 100%;
    }
  }
</style>

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
  <a-alert type="info">
    <template #message>
      <h1>温馨提示</h1>
      <!-- <ul style="padding-left: 20px; font-size: 12px">
        <li style="list-style-type: disc"
          >如果您已购买开通腾讯云人脸核身服务，并确定在个人实名认证以及合同签署环节使用人脸识别服务，则需要进行以下配置，否则，请忽略。</li
        >
        <li style="list-style-type: disc"
          >app_id和secret的获取路径：登录腾讯云->进入人脸核身服务->自主接入->SDK/移动H5服务，如果未申请WBappid，请先申请WBappid，申请成功后，可在列表中查看id和secret。</li
        >
        <li style="list-style-type: disc"
          ><span style="color: red">在投产时，请使用WBappid对应的正式环境配置。</span></li
        >
      </ul> -->
      <ul style="padding-left: 20px; font-size: 12px">
        <li style="list-style-type: disc">合同签署环节是否允许使用人脸识别服务的开关</li>
        <li style="list-style-type: disc"
          >开关打开，则业务线配置和发起签署时，允许用户选择“人脸识别”的签署意愿验证方式，否则，只能使用普通校验方式</li
        >
      </ul>
    </template>
  </a-alert>
  <Loading :loading="loading" :absolute="false" />
  <Loading :loading="uploadLoading" :absolute="false" />
  <div class="tab-item">
    <!-- <div class="item-operation">
      <a-button type="primary" @click="saveConfig">保存</a-button>
    </div> -->
    <div class="item-from">
      <a-form
        ref="formRef"
        :model="formState"
        name="basic"
        :label-col="{ span: 3 }"
        :wrapper-col="{ span: 20 }"
        :label-align="'left'"
        autocomplete="off"
      >
        <a-form-item label="签署人脸识别验证" name="sign_confirm_type">
          <a-switch
            v-model:checked="formState.sign_confirm_type"
            checked-value="true"
            un-checked-value="false"
            @click="saveConfig"
          />
          <!-- <a-tooltip placement="bottom" v-else>
            <template #title>
              <span>当前软件授权不支持使用人脸识别，如需使用，请升级软件授权</span>
            </template>
            <a-switch
              v-model:checked="formState.sign_confirm_type"
              disabled
              checked-value="true"
              un-checked-value="false"
            />
          </a-tooltip> -->
        </a-form-item>
        <!-- <a-form-item label="个人实名认证人脸识别验证" name="person_face">
          <a-switch
            v-model:checked="formState.person_face"
            v-if="configFlag"
            checked-value="true"
            un-checked-value="false"
          />
          <a-tooltip placement="bottom" v-else>
            <template #title>
              <span>当前软件授权不支持使用人脸识别，如需使用，请升级软件授权</span>
            </template>
            <a-switch
              v-model:checked="formState.sign_confirm_type"
              disabled
              checked-value="true"
              un-checked-value="false"
            />
          </a-tooltip>
        </a-form-item> -->
        <!-- <a-divider><span>腾讯云人脸识别服务</span></a-divider>
        <a-form-item label="app_id" name="qcloud_face_app_id">
          <a-input v-model:value="formState.qcloud_face_app_id" />
        </a-form-item>
        <a-form-item label="secret" name="qcloud_face_secret">
          <a-input v-model:value="formState.qcloud_face_secret" />
        </a-form-item> -->
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { getToken } from '/@/utils/auth';
  import { fileToBase64 } from '/@/utils/file/base64Conver';
  import { getSafeConfig, setSafeConfig } from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import { message } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  // import { getSystemLimit } from '/@/api/license';
  const formState = reactive({
    sign_confirm_type: '',
    qcloud_face_app_id: '',
    qcloud_face_secret: '',
    person_face: '',
  });
  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  // const configFlag = ref(false);
  onMounted(() => {
    initData();
  });

  const configKeys = [
    'sign_confirm_type',
    'qcloud_face_secret',
    'qcloud_face_app_id',
    'person_face',
  ];

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
    // configFlag.value = limit.faceRecognitionUseFlag;
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
      margin-top: 30px;
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

<template>
  <Loading :loading="loading" :absolute="false" />
  <Loading :loading="uploadLoading" :absolute="false" />
  <div class="tab-item">
    <div class="config-section">
      <h2>账号配置</h2>
      <div class="item-from">
        <div>
          <span style="font-family: 600; font-size: 12px">账号类型</span>
          <span style="font-size: 12px; color: rgba(0, 0, 0, 0.4)"
            >(支持配置注册账号类型，配置后，用户可使用对应的账号类型完成注册)</span
          >
        </div>
        <div style="padding: 20px 0">
          <a-radio-group v-model:value="formState.system_account_type" @change="saveAccountConfig">
            <a-radio value="phone">手机号</a-radio>
            <a-radio value="email">邮箱</a-radio>
            <a-radio value="phone_email">手机号或邮箱</a-radio>
          </a-radio-group>
        </div>
      </div>
    </div>
    <div class="config-section" style="margin-top: 30px">
      <h2>签署配置</h2>
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
              @click="saveSignConfig"
            />
          </a-form-item>
          <div class="tips-container">
            <div class="tips-header" @click="faceTipsExpanded = !faceTipsExpanded">
              <span class="tips-title"> ⚠️ 签署人脸识别验证配置说明</span>
              <span class="expand-icon">
                {{ faceTipsExpanded ? '收起' : '展开' }}
                <Icon
                  :icon="faceTipsExpanded ? 'ant-design:up-outlined' : 'ant-design:down-outlined'"
                />
              </span>
            </div>
            <div v-show="faceTipsExpanded" class="tips-content">
              <ul style="padding-left: 20px; font-size: 12px">
                <li class="tips-content-li">合同签署环节是否允许使用人脸识别服务的开关</li>
                <li class="tips-content-li">
                  开关打开，则业务线配置和发起签署时，允许用户选择"人脸识别"的签署意愿验证方式，否则，只能使用普通校验方式
                </li>
              </ul>
            </div>
          </div>

          <!-- 新增个人签署实名认证配置项 -->
          <a-form-item label="个人签署实名认证" name="personal_sign_auth">
            <a-radio-group
              v-model:value="formState.personal_sign_auth"
              @change="handleAuthTypeChange"
            >
              <a-radio value="required">须实名认证</a-radio>
              <a-radio value="allowed">允许不实名认证</a-radio>
              <a-radio value="not_required">无需实名认证</a-radio>
            </a-radio-group>
          </a-form-item>
          <div class="tips-container">
            <div class="tips-header" @click="signAuthTipsExpanded = !signAuthTipsExpanded">
              <span class="tips-title"> ⚠️ 个人签署实名认证配置说明</span>
              <span class="expand-icon">
                {{ signAuthTipsExpanded ? '收起' : '展开' }}
                <Icon
                  :icon="
                    signAuthTipsExpanded ? 'ant-design:up-outlined' : 'ant-design:down-outlined'
                  "
                />
              </span>
            </div>
            <div v-show="signAuthTipsExpanded" class="tips-content">
              <ul style="padding-left: 20px; font-size: 12px">
                <li class="tips-content-li">
                  须实名认证：【强烈建议】使用个人电子签章前，必须完成实名认证，符合电子签名的合法性与安全性要求
                </li>
                <li class="tips-content-li">
                  允许不实名认证：使用个人电子签章前，允许不进行实名认证。根据具体业务自行配置，决定是否进行实名认证
                </li>
                <li class="tips-content-li">
                  无需实名认证：使用个人电子签章前，无需进行实名认证！
                  <span style="color: red; font-weight: bold"
                    >⚠️
                    注意：当选择该方式时，平台中所有个人用户签署时，均强制不进行实名认证（业务线配置的认证要求不再生效），请谨慎选择！！！</span
                  >
                </li>
              </ul>
            </div>
          </div>
        </a-form>
      </div>
    </div>
  </div>
  <!-- 弹窗确认组件 -->
  <a-modal
    v-model:visible="confirmModalVisible"
    title="非实名认证签署风险告知书"
    @ok="confirmAuthTypeChange"
    @cancel="cancelAuthTypeChange"
    okText="同意"
    cancelText="拒绝"
    width="600px"
  >
    <div class="confirm-modal-content">
      <p class="modal-title">尊敬的用户：</p>
      <p class="modal-text">
        为确保电子签名的合法性与安全性，在本平台使用个人电子签章前，必须完成实名认证。
      </p>
      <p class="modal-text">
        如您关闭实名认证要求，用户在签署时将不再进行实名认证。系统将自动使用平台防篡改证书（非CA权威数字证书）完成签名。该证书仅能保障文件在签名后不被篡改，不具备《电子签名法》所规定的法律效力。
      </p>
      <p class="modal-title">
        特别声明：建议您在确保掌握签署人真实身份的前提下，再关闭个人签署的实名认证要求。如因关闭该功能引发任何纠纷或损失，相关责任均由您所在的公司承担，与开放签（北京资源律动科技有限公司）无关。
      </p>
      <p class="modal-text modal-warning">
        若点击同意按钮，则代表您已阅读并知晓非实名认证签署的风险
      </p>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { getSafeConfig, setSafeConfig } from '/@/api/sys/safe';
  import { message } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  import Icon from '/@/components/Icon/src/Icon.vue';

  // 添加弹窗相关的响应式变量
  const confirmModalVisible = ref(false);
  const pendingAuthType = ref(''); // 临时存储待确认的认证类型
  const pendingAuthTypeLabel = ref(''); // 临时存储待确认的认证类型标签

  const formState = reactive({
    system_account_type: 'phone',
    sign_confirm_type: '',
    personal_sign_auth: 'required', // 默认为"须实名认证"
  });

  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const faceTipsExpanded = ref(true); // 控制温馨提示的展开状态
  const signAuthTipsExpanded = ref(true); // 控制温馨提示的展开状态

  onMounted(() => {
    initData();
  });

  const accountConfigKeys = ['system_account_type'];
  const signConfigKeys = ['sign_confirm_type', 'personal_sign_auth'];
  const allConfigKeys = [...accountConfigKeys, ...signConfigKeys];

  // 处理认证类型变更
  function handleAuthTypeChange(e) {
    // 从事件对象中提取值
    const value = e.target.value;
    // 如果选择的是"允许不实名认证"或"无需实名认证"，需要弹窗确认
    if (value === 'allowed' || value === 'not_required') {
      pendingAuthType.value = value;
      pendingAuthTypeLabel.value = value === 'allowed' ? '允许不实名认证' : '无需实名认证';
      confirmModalVisible.value = true;
    } else {
      // 直接保存"须实名认证"选项
      formState.personal_sign_auth = value;
      savePersonalSignAuthConfig();
    }
  }

  // 确认变更认证类型
  function confirmAuthTypeChange() {
    formState.personal_sign_auth = pendingAuthType.value;
    confirmModalVisible.value = false;
    savePersonalSignAuthConfig();
  }

  // 取消变更认证类型
  function cancelAuthTypeChange() {
    // 恢复原来的值
    const originalValue =
      (configData.value as any[]).find((item) => item.type === 'personal_sign_auth')?.value ||
      'required';
    formState.personal_sign_auth = originalValue;
    confirmModalVisible.value = false;
  }

  // 保存个人签署实名认证配置
  async function savePersonalSignAuthConfig() {
    try {
      loading.value = true;
      // 获取个人签署实名认证相关的配置数据
      const personalSignAuthConfigs = (configData.value as any[]).filter(
        (item) => item.type === 'personal_sign_auth',
      );
      personalSignAuthConfigs.forEach((item: any) => {
        item.value = formState.personal_sign_auth;
      });

      console.log('保存个人签署实名认证配置数据：', personalSignAuthConfigs);

      const result = await setSafeConfig({ sysConfigs: personalSignAuthConfigs });
      if (result.success) {
        message.success('个人签署实名认证配置保存成功');
      } else {
        message.error(result.message);
      }
      loading.value = false;
    } catch (errorInfo) {
      loading.value = false;
      message.error('保存失败');
    }
  }

  async function saveAccountConfig() {
    try {
      loading.value = true;
      // 获取账号相关的配置数据
      const accountConfigs = (configData.value as any[]).filter((item) =>
        accountConfigKeys.includes(item.type),
      );
      accountConfigs.forEach((item: any) => {
        item.value = formState[item.type];
      });

      const result = await setSafeConfig({ sysConfigs: accountConfigs });
      if (result.success) {
        message.success('账号配置保存成功');
      } else {
        message.error(result.message);
      }
      loading.value = false;
    } catch (errorInfo) {
      loading.value = false;
      message.error('保存失败');
    }
  }

  async function saveSignConfig() {
    try {
      loading.value = true;
      // 获取签署相关的配置数据
      const signConfigs = (configData.value as any[]).filter((item) =>
        signConfigKeys.includes(item.type),
      );
      signConfigs.forEach((item: any) => {
        item.value = formState[item.type];
      });

      const result = await setSafeConfig({ sysConfigs: signConfigs });
      if (result.success) {
        message.success('签署配置保存成功');
      } else {
        message.error(result.message);
      }
      loading.value = false;
    } catch (errorInfo) {
      loading.value = false;
      message.error('保存失败');
    }
  }

  async function initData() {
    try {
      const params = allConfigKeys.join(',');
      const result = await getSafeConfig({ types: params });
      if (result && result.length > 0) {
        configData.value = result;
        for (var i = 0; i < result.length; i++) {
          var item = result[i];
          formState[item.type] = item.value;
        }
      }
      console.log(formState);
    } catch (error) {
      console.error('获取配置失败:', error);
    }
  }
</script>

<style lang="less" scoped>
  .tab-item {
    // width: 1200px;
    margin-left: 20px;

    .config-section {
      border: 1px solid #f0f0f0;
      border-radius: 4px;
      padding: 20px;
    }

    .item-from {
      margin-top: 20px;
    }

    .logo-tips {
      font-size: 12px;
      color: rgba(0, 0, 0, 0.6);
      line-height: 40px;
    }

    .tips-container {
      margin-top: 5px;
      margin-bottom: 10px;
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      background-color: #f6f6f6;
    }

    .tips-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 16px;
      cursor: pointer;
      user-select: none;
    }

    .tips-title {
      font-weight: bold;
      font-size: 14px;
    }

    .expand-icon {
      font-size: 12px;
      color: #666;
    }

    .tips-content {
      padding: 0 16px 16px 16px;
    }

    .tips-content-li {
      margin-bottom: 8px;
      font-size: 12px;
      line-height: 1.5;
      color: #666;
      list-style-type: disc;
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
  .confirm-modal-content {
    padding: 20px;
    border: 1px solid #e0e0e0;
    border-radius: 6px;

    .modal-title {
      font-weight: bold;
      font-size: 14px;
      margin-bottom: 15px;
    }

    .modal-text {
      font-size: 14px;
      line-height: 1.6;
      margin-bottom: 15px;
      text-align: justify;
    }

    .modal-warning {
      color: #ff4d4f;
      font-weight: bold;
    }
  }
  .h2 {
    font-size: 18px;
  }
</style>

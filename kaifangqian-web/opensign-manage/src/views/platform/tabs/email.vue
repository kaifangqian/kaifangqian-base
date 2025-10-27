<template>
  <Loading :loading="loading" :absolute="false" />
  <div class="email-config-container">
    <div class="config-header">
      <h2>邮件配置</h2>
      <a-button type="primary" @click="saveConfig">保存配置</a-button>
    </div>
    <div class="config-form">
      <a-form
        ref="formRef"
        :model="formState"
        name="basic"
        :label-col="{ span: 24 }"
        :wrapper-col="{ span: 24 }"
        :label-align="'left'"
        autocomplete="off"
      >
        <a-form-item
          label="邮件发送方名称"
          name="email_nick_name"
          :rules="[{ required: true, message: '不能为空' }]"
        >
          <a-input v-model:value="formState.email_nick_name" placeholder="请输入邮件发送方名称" />
        </a-form-item>

        <a-form-item
          label="邮箱登录名"
          name="email_username"
          :rules="[{ required: true, message: '邮箱登录名不能为空' }]"
        >
          <a-input v-model:value="formState.email_username" placeholder="请输入邮箱登录名" />
        </a-form-item>

        <a-form-item
          label="SMTP服务授权码"
          name="email_auth_code"
          :rules="[{ required: true, message: 'SMTP服务授权码不能为空' }]"
        >
          <a-input-password
            v-model:value="formState.email_auth_code"
            placeholder="请输入SMTP服务授权码"
          />
        </a-form-item>

        <a-form-item
          label="SMTP服务器地址"
          name="email_host"
          :rules="[{ required: true, message: 'SMTP服务器地址不能为空' }]"
        >
          <a-input v-model:value="formState.email_host" placeholder="请输入SMTP服务器地址" />
        </a-form-item>

        <a-form-item
          label="端口号"
          name="email_port"
          :rules="[{ required: true, message: '端口号不能为空' }]"
        >
          <a-input v-model:value="formState.email_port" placeholder="请输入端口号" />
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { getSafeConfig, setSafeConfig } from '/@/api/sys/safe';
  import { message } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';

  const formState = reactive({
    email_username: '',
    email_host: '',
    email_auth_code: '',
    email_port: '',
    email_nick_name: '',
  });

  const loading = ref(false);
  const configData = ref([]);
  const formRef = ref();

  onMounted(() => {
    initData();
  });

  const configKeys = [
    'email_username',
    'email_host',
    'email_auth_code',
    'email_port',
    'email_nick_name',
  ];

  async function saveConfig() {
    try {
      loading.value = true;

      const values = await formRef.value.validateFields();
      configData.value.forEach((item: any) => {
        item.value = values[item.type];
      });

      const result = await setSafeConfig({ sysConfigs: configData.value });
      if (result.success) {
        message.success('保存成功');
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
      const params = configKeys.join(',');
      const result = await getSafeConfig({ types: params });
      if (result && result.length > 0) {
        configData.value = result;
        for (let i = 0; i < result.length; i++) {
          const item = result[i];
          formState[item.type as keyof typeof formState] = item.value;
        }
      }
      console.log(formState);
    } catch (error) {
      console.error('获取配置失败:', error);
      message.error('获取配置失败');
    }
  }
</script>

<style lang="less" scoped>
  .email-config-container {
    margin: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
    overflow: hidden;

    .config-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px 24px;
      border-bottom: 1px solid #f0f0f0;

      h2 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: #333;
      }
    }

    .config-form {
      padding: 24px;
      max-width: 600px;

      :deep(.ant-form-item) {
        margin-bottom: 20px;

        .ant-form-item-label {
          padding-bottom: 6px;

          label {
            font-size: 14px;
            color: #666;
            font-weight: 500;
          }
        }

        .ant-input {
          border-radius: 4px;
          padding: 8px 12px;
          font-size: 14px;
          border-color: #d9d9d9;

          &:focus {
            border-color: #40a9ff;
            box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
          }
        }

        .ant-input-password {
          border-radius: 4px;

          .ant-input {
            border-radius: 4px;
          }
        }
      }
    }
  }
</style>

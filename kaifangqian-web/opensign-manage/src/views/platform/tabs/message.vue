<template>
  <Loading :loading="loading" :absolute="false" />
  <Loading :loading="uploadLoading" :absolute="false" />
  <div class="message-config-container">
    <div class="config-header">
      <h2>短信配置</h2>
      <a-button type="primary" @click="saveConfig">保存配置</a-button>
    </div>

    <!-- 温馨提示折叠面板 -->
    <div class="tips-container">
      <div class="tips-header" @click="tipsExpanded = !tipsExpanded">
        <span class="tips-title"> ⚠️ 温馨提示</span>
        <span class="expand-icon">
          {{ tipsExpanded ? '收起' : '展开' }}
          <Icon :icon="tipsExpanded ? 'ant-design:up-outlined' : 'ant-design:down-outlined'" />
        </span>
      </div>
      <div v-show="tipsExpanded" class="tips-content">
        <div class="tip-section">
          <h3>短信开关：</h3>
          <ul>
            <li
              >开关打开：用户登录、注册、账号找回、密码修改等环节发送短信验证码，签署环节发送短信通知。</li
            >
            <li
              >开关关闭：短信验证码统一为【123456】，签署环节不发送短信通知。（ ⚠️ 注意：实名认证以及签署意愿验证相关的短信不受影响）</li
            >
          </ul>
        </div>
        <div class="tip-section">
          <h3>短信通道的选择：</h3>
          <ul>
            <li>默认通道：本系统已完成短信服务的对接，无需客户对接，可直接使用；</li>
            <li
              >客户自采阿里云短信：支持客户使用自行采购的阿里云短信服务，需要客户在阿里云购买短信服务，并申请短信签名与短信模板；</li
            >
          </ul>
        </div>
      </div>
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
        <a-form-item label="短信开关" name="send_message">
          <a-switch
            v-model:checked="formState.send_message"
            checked-value="true"
            un-checked-value="false"
          />
        </a-form-item>

        <a-form-item label="短信通道" name="sms_channel">
          <a-radio-group v-model:value="formState.sms_channel" button-style="solid">
            <a-radio-button value="resrun">默认通道</a-radio-button>
            <a-radio-button value="aliyun">客户自采阿里云短信</a-radio-button>
          </a-radio-group>
        </a-form-item>

        <template v-if="formState.sms_channel == 'aliyun'">
          <div class="aliyun-config-section">
            <div class="section-header">
              <h3>阿里云配置</h3>
            </div>

            <a-form-item label="Region ID" name="ali_sms_region_id">
              <a-input v-model:value="formState.ali_sms_region_id" placeholder="例: cn-beijing" />
            </a-form-item>

            <a-form-item label="Access Key ID" name="ali_sms_access_key_id">
              <a-input
                v-model:value="formState.ali_sms_access_key_id"
                placeholder="请输入Access Key ID"
              />
            </a-form-item>

            <a-form-item label="Access Key Secret" name="ali_sms_access_key_secret">
              <a-input-password
                v-model:value="formState.ali_sms_access_key_secret"
                placeholder="请输入Access Key Secret"
              />
            </a-form-item>

            <a-form-item label="短信签名" name="sms_sign">
              <a-input v-model:value="formState.sms_sign" placeholder="请输入短信签名" />
            </a-form-item>
          </div>

          <div class="aliyun-template-section">
            <div class="section-header">
              <h3>阿里云短信模板</h3>
            </div>

            <!-- 阿里云短信模板温馨提示 -->
            <div class="template-tips-container">
              <div class="tips-header" @click="templateTipsExpanded = !templateTipsExpanded">
                <span class="tips-title"> ⚠️ 温馨提示</span>
                <span class="expand-icon">
                  {{ templateTipsExpanded ? '收起' : '展开' }}
                  <Icon
                    :icon="templateTipsExpanded ? 'ant-design:up-outlined' : 'ant-design:down-outlined'"
                  />
                </span>
              </div>
              <div v-show="templateTipsExpanded" class="tips-content">
                <ul>
                  <li>将以下模板中的 home.kaifangqian.com 换成您自己的电子签章应用的域名</li>
                  <li>在申请阿里云短信模板时，以下短信模板中涉及的参数不能增删</li>
                </ul>
              </div>
            </div>

            <div class="template-list">
              <a-row :gutter="[0, 16]">
                <a-col :span="24" v-for="item in msgTemplate" :key="item.type">
                  <div class="template-item">
                    <div class="template-label">
                      <div class="template-title">{{ item.title }}</div>
                      <div class="template-content">
                        <span class="content-text">{{ item.content }}</span>
                        <span class="copy-icon" @click="copyContent(item.content)">
                          <Icon icon="ant-design:copy-outlined" />
                        </span>
                      </div>
                    </div>
                    <div class="template-value">
                      <a-input v-model:value="formState[item.type]" placeholder="请输入模板ID" />
                    </div>
                  </div>
                </a-col>
              </a-row>
            </div>
          </div>
        </template>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { getSafeConfig, setSafeConfig } from '/@/api/sys/safe';
  import { message } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  import { getSystemLimit } from '/@/api/license';
  import Icon from '/@/components/Icon/src/Icon.vue';

  const formState = reactive({
    send_message: '',
    ali_sms_region_id: '',
    ali_sms_access_key_id: '',
    ali_sms_access_key_secret: '',
    sms_sign: '',
    sms_channel: '',
  });

  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const tipsExpanded = ref(true); // 控制全局温馨提示的展开状态
  const templateTipsExpanded = ref(true); // 控制阿里云短信模板温馨提示的展开状态
  const environment = ref('test');

  const msgTemplate = [
    {
      content: '${code}验证码，若非本人操作，请勿泄露',
      type: 'mes_template_captcha',
      title: '验证码模板',
    },
    {
      content:
        '${companyName}添加您为企业成员，可点击 home.kaifangqian.com/#/join?key=${key}&type=2 查看',
      type: 'mes_template_inviteUser',
      title: '企业添加内部成员通知',
    },
    {
      content:
        '${sender}给您发送了一份文件《${contract}》，有部分内容需要您填写，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 填写',
      type: 'mes_template_writeTask',
      title: '文件填写通知',
    },
    {
      content:
        '${sender}给您发送了一份文件《${contract}》，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 完成签署',
      type: 'mes_template_signTaskIn',
      title: '文件签署通知',
    },
    // {
    //   content:
    //     '${sender}给您发送了一份文件《${contract}》，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 完成签署',
    //   type: 'mes_template_signTaskIn',
    //   title: '文件签署通知（发起方）',
    // },
    // {
    //   content:
    //     '${sender}给您发送了一份文件《${contract}》，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 完成签署',
    //   type: 'mes_template_signTaskOut',
    //   title: '文件签署通知（接收方）',
    // },
    {
      content:
        '${sender}发起了一份文件《${contract}》，抄送给你，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 查看',
      type: 'mes_template_copyBegin',
      title: '文件抄送通知',
    },
    // {
    //   content:
    //     '${sender}发起了一份文件《${contract}》，抄送给你，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 查看',
    //   type: 'mes_template_copyBegin',
    //   title: '文件抄送通知（发起时通知）',
    // },
    // {
    //   content:
    //     '${sender}发起了一份文件《${contract}》，抄送给你，请访问 home.kaifangqian.com/#/contract/detail/base?code=${code} 查看',
    //   type: 'mes_template_copySign',
    //   title: '文件抄送通知（签署完成时通知）',
    // },
  ];

  onMounted(() => {
    initData();
  });

  const configKeys = [
    'send_message',
    'sms_channel',
    'ali_sms_region_id',
    'ali_sms_access_key_id',
    'ali_sms_access_key_secret',
    'sms_sign',
    'mes_template_entAuthFaild',
    'mes_template_inviteUser',
    'mes_template_copyBegin',
    'mes_template_writeTask',
    'mes_template_captcha',
    'mes_template_entAuthSuccess',
    'mes_template_copySign',
    'mes_template_signTaskOut',
    'mes_template_signTaskIn',
  ];

  async function saveConfig() {
    try {
      loading.value = true;
      configData.value.forEach((item: any) => {
        item.value = formState[item.type];
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
      const limit = await getSystemLimit();
      environment.value = limit.environment;
    } catch (error) {
      console.error('获取配置失败:', error);
      message.error('获取配置失败');
    }
  }

  // 复制短信模板内容到剪贴板
  function copyContent(content: string) {
    try {
      // 首先尝试使用现代Clipboard API
      navigator.clipboard
        .writeText(content)
        .then(() => {
          message.success('复制成功');
        })
        .catch((error) => {
          console.error('Clipboard API 复制失败:', error);
          // 如果Clipboard API失败，尝试使用降级方案
          fallbackCopyTextToClipboard(content);
        });
    } catch (error) {
      console.error('复制功能出错:', error);
      // 如果Clipboard API完全不可用，尝试使用降级方案
      fallbackCopyTextToClipboard(content);
    }
  }

  // 降级方案：使用textarea和document.execCommand
  function fallbackCopyTextToClipboard(text: string) {
    try {
      const textArea = document.createElement('textarea');
      textArea.value = text;

      // 避免滚动到底部
      textArea.style.top = '0';
      textArea.style.left = '0';
      textArea.style.position = 'fixed';
      textArea.style.opacity = '0';

      document.body.appendChild(textArea);
      textArea.focus();
      textArea.select();
      try {
        const successful = document.execCommand('copy');
        if (successful) {
          message.success('复制成功');
        } else {
          message.error('复制失败，请手动复制');
        }
      } catch (error) {
        console.error('降级复制方案失败:', error);
        message.error('复制失败，请手动复制');
      }

      document.body.removeChild(textArea);
    } catch (error) {
      console.error('降级复制方案出错:', error);
      message.error('复制失败，请手动复制');
    }
  }
</script>

<style lang="less" scoped>
  .message-config-container {
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

    // 温馨提示折叠面板样式
    .tips-container {
      margin: 20px 24px 0 24px;
      border: 1px solid #e0e0e0;
      border-radius: 4px;
      background-color: #f9f9f9;

      .tips-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 16px;
        cursor: pointer;
        user-select: none;

        .tips-title {
          font-weight: bold;
          font-size: 14px;
        }

        .expand-icon {
          font-size: 12px;
          color: #666;
        }
      }

      .tips-content {
        padding: 0 16px 16px 16px;

        .tip-section {
          margin-top: 16px;

          h3 {
            margin: 0 0 8px 0;
            font-size: 14px;
            font-weight: 600;
            color: #555;
          }

          ul {
            margin: 0;
            padding-left: 20px;

            li {
              margin-bottom: 8px;
              font-size: 13px;
              line-height: 1.5;
              color: #666;
              list-style-type: disc;
            }
          }
        }
      }
    }

    .config-form {
      padding: 24px;
      // max-width: 800px;

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

        .ant-switch {
          min-width: 44px;
        }

        .ant-radio-group {
          .ant-radio-button {
            &:first-child {
              border-radius: 4px 0 0 4px;
            }

            &:last-child {
              border-radius: 0 4px 4px 0;
            }
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

    .aliyun-config-section {
      margin-top: 24px;
      padding: 20px;
      background: #fafafa;
      border-radius: 6px;

      .section-header {
        margin-bottom: 20px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }
      }
    }

    .aliyun-template-section {
      margin-top: 24px;
      padding: 20px;
      background: #fafafa;
      border-radius: 6px;

      .section-header {
        margin-bottom: 20px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: #333;
        }
      }

      // 阿里云短信模板温馨提示样式
      .template-tips-container {
        margin-bottom: 20px;
        border: 1px solid #e0e0e0;
        border-radius: 4px;
        background-color: #ffffff;

        .tips-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 12px 16px;
          cursor: pointer;
          user-select: none;

          .tips-title {
            font-weight: bold;
            font-size: 14px;
          }

          .expand-icon {
            font-size: 12px;
            color: #666;
          }
        }

        .tips-content {
          padding: 0 16px 16px 16px;

          ul {
            margin: 0;
            padding-left: 20px;

            li {
              margin-bottom: 8px;
              font-size: 13px;
              line-height: 1.5;
              color: #666;
              list-style-type: disc;
            }
          }
        }
      }

      .template-list {
        .template-item {
          // display: flex;
          flex-direction: row;
          gap: 20px;
          padding: 16px;
          background: #fff;
          border: 1px solid #e8e8e8;
          border-radius: 4px;
          align-items: center;

          .template-label {
            flex: 1;
            min-width: 0;

            .template-title {
              font-weight: 500;
              color: #333;
              margin-bottom: 4px;
            }

            .template-content {
              font-size: 13px;
              color: #666;
              background: #f5f5f5;
              padding: 8px 12px;
              border-radius: 4px;
              font-family: monospace;
              word-break: break-all;
              display: flex;
              align-items: center;

              .content-text {
                flex: 1;
                min-width: 0;
              }

              .copy-icon {
                flex: 0 0 auto;
                margin-left: 8px;
                cursor: pointer;

                i {
                  font-size: 14px;
                  color: #666;
                }

                &:hover i {
                  color: #1890ff;
                }
              }
            }
          }

          .template-value {
            flex: 0 0 300px;
            margin-top: 10px;

            .ant-input {
              width: 100%;
            }
          }
        }
      }
    }
  }
</style>

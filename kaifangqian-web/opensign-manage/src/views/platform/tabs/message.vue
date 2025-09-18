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
      <div style="padding-left: 20px; font-size: 12px">
        <h2>短信开关：</h2>
        <ul style="padding-left: 20px; font-size: 12px">
          <li style="list-style-type: disc"
            >开关打开：用户登录、注册、账号找回、密码修改等环节发送短信验证码，签署环节发送短信通知。</li
          >
          <li style="list-style-type: disc"
            >开关关闭：短信验证码统一为【123456】，签署环节不发送短信通知。（
            ⚠️注意：实名认证以及签署意愿验证相关的短信不受影响 ）</li
          >
        </ul>
        <h2>短信通道的选择：</h2>
        <ul style="padding-left: 20px; font-size: 12px">
          <li style="list-style-type: disc"
            >默认通道：本系统已完成短信服务的对接，无需客户对接，可直接使用；</li
          >
          <li style="list-style-type: disc"
            >客户自采阿里云短信：支持客户使用自行采购的阿里云短信服务，需要客户在阿里云购买短信服务，并申请短信签名与短信模板；</li
          >
        </ul>
      </div>
    </template>
  </a-alert>
  <Loading :loading="loading" :absolute="false" />
  <Loading :loading="uploadLoading" :absolute="false" />
  <div class="tab-item">
    <div class="item-operation">
      <a-button type="primary" @click="saveConfig">保存</a-button>
    </div>
    <div class="item-from">
      <a-form
        ref="formRef"
        :model="formState"
        name="basic"
        :label-col="{ span: 3 }"
        :wrapper-col="{ span: 21 }"
        :label-align="'left'"
        autocomplete="off"
      >
        <a-form-item label="短信开关" name="send_message">
          <!-- <a-tooltip
            placement="bottom"
            v-if="formState.sms_channel == 'resrun' && environment == 'test'"
          >
            <template #title>
              <span>当前为测试授权，无法进行该项配置</span>
            </template>
            <a-switch
              v-model:checked="formState.send_message"
              checked-value="true"
              un-checked-value="false"
              disabled
            />
          </a-tooltip>
          <a-switch
            v-else
            v-model:checked="formState.send_message"
            checked-value="true"
            un-checked-value="false"
          /> -->
          <a-switch
            v-model:checked="formState.send_message"
            checked-value="true"
            un-checked-value="false"
          />
        </a-form-item>
        <!-- <a-form-item label="短信签名" name="sms_sign" v-if="formState.sms_channel == 'aliyun'">
          <a-input v-model:value="formState.sms_sign" />
        </a-form-item> -->
        <a-form-item label="短信通道" name="sms_sign">
          <a-radio-group v-model:value="formState.sms_channel" button-style="solid">
            <a-radio-button value="resrun">默认通道</a-radio-button>
            <a-radio-button value="aliyun">客户自采阿里云短信</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <!-- <a-form-item label="短信签名" name="sms_sign" v-if="formState.sms_channel == 'aliyun'">
          <a-input v-model:value="formState.sms_sign" />
        </a-form-item> -->
        <template v-if="formState.sms_channel == 'aliyun'">
          <a-divider>
            <span style="font-weight: 600">阿里云配置及短信模板</span>
          </a-divider>
          <a-form-item label="regionId" name="ali_sms_region_id">
            <a-input v-model:value="formState.ali_sms_region_id" placeholder="例:cn-beijing" />
          </a-form-item>
          <a-form-item label="accessKeyId" name="ali_sms_access_key_id">
            <a-input v-model:value="formState.ali_sms_access_key_id" />
          </a-form-item>
          <a-form-item label="accessKeySecret" name="ali_sms_access_key_secret">
            <a-input v-model:value="formState.ali_sms_access_key_secret" />
          </a-form-item>
          <a-form-item label="短信签名" name="sms_sign">
            <a-input v-model:value="formState.sms_sign" />
          </a-form-item>

          <a-divider>
            <span style="font-weight: 600">阿里云短信模板</span>
          </a-divider>
          <a-row style="margin-bottom: 20px;">
            <!-- <a-col :span="4" /> -->
            <a-col :span="24">
              <a-list item-layout="horizontal" :data-source="msgTemplate" >
                <template #renderItem="{ item }">
                  <a-list-item>
                    <template #actions>
                      <a-input v-model:value="formState[item.type]" />
                    </template>
                    <a-list-item-meta :description="item.content">
                      <template #title>
                        <span>{{ item.title }}</span>
                      </template>
                    </a-list-item-meta>
                  </a-list-item>
                </template>
              </a-list>
            </a-col>
          </a-row>
        </template>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue';
  import { getSafeConfig, setSafeConfig } from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import { message } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  import { getSystemLimit } from '/@/api/license';
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
  const fileList1: any = ref([]);
  const fileList2: any = ref([]);
  const environment = ref('test');
  const msgTemplate = [
    {
      content: '${code}验证码，若非本人操作，请勿泄露',
      type: 'mes_template_captcha',
      title: '验证码模板',
    },
    {
      content: '${companyName}添加您为企业成员，可点击${domain}/#/join?key=${key}&type=2 查看',
      type: 'mes_template_inviteUser',
      title: '企业添加内部成员通知',
    },
    // {
    //   content:
    //     '您好，您提交的${companyName}认证审核未通过，原因${reason}，可点击${domain} 登录查看',
    //   type: 'mes_template_entAuthFaild',
    //   title: '企业实名认证审核不通过通知',
    // },
    // {
    //   content: '您提交的${companyName}认证信息已审核通过，可点击${domain} 登录查看',
    //   type: 'mes_template_entAuthSuccess',
    //   title: '企业实名认证审核通过通知',
    // },
    {
      content:
        '${sender}给您发送了一份文件《${contract}》，有部分内容需要您填写，请访问 ${domain}/#/contract/detail/base?code=${code} 填写',
      type: 'mes_template_writeTask',
      title: '文件填写通知',
    },
    {
      content:
        '${sender}给您发送了一份文件《${contract}》，请访问 ${domain}/#/contract/detail/base?code=${code} 完成签署',
      type: 'mes_template_signTaskIn',
      title: '文件签署通知（发起方）',
    },
    {
      content:
        '${sender}给您发送了一份文件《${contract}》，请访问 ${domain}/#/contract/detail/base?code=${code} 完成签署',
      type: 'mes_template_signTaskOut',
      title: '文件签署通知（接收方）',
    },
    {
      content:
        '${sender}发起了一份文件《${contract}》，抄送给你，请访问${domain}/#/contract/detail/base?code=${code} 查看',
      type: 'mes_template_copyBegin',
      title: '文件抄送通知（发起时通知）',
    },
    {
      content:
        '${sender}发起了一份文件《${contract}》，抄送给你，请访问${domain}/#/contract/detail/base?code=${code} 查看',
      type: 'mes_template_copySign',
      title: '文件抄送通知（签署完成时通知）',
    },
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
    const limit = await getSystemLimit();
    environment.value = limit.environment;
  }
</script>

<style lang="less" scoped>
  .tab-item {
    margin-left: 20px;
    width: 1200px;

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

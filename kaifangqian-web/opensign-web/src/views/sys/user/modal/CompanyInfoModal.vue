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
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
      <!-- <BasicForm @register="registerForm" />
      <div class="w-full flex justify-center">
        <a-button type="primary" class="w-[150px]" @click="queryUser">查询</a-button>
      </div> -->
      <!-- <div>{{ companyInfo }}</div> -->
      <div class="modal-content-bg">
        <Descriptions
          :column="1"
          :colon="false"
          layout="vertical"
          :label-style="{
            fontWeight: 'bold',
            color: '#737373',
          }"
        >
          <template #title>
            <div class="text-center">{{ companyInfo.name }}</div>
          </template>
          <DescriptionsItem label="工商执照注册号/统一社会信用代码">{{
            companyInfo.organizationNo? companyInfo.organizationNo : '--'
          }}</DescriptionsItem>
          <DescriptionsItem label="注册时间">{{ companyInfo.createTime }}</DescriptionsItem>
          <DescriptionsItem label="实名通过时间">{{ companyInfo.createTime }}</DescriptionsItem>
          <DescriptionsItem label="企业管理员">
            <template #label>
              <p>123</p>
            </template>
            <List size="small" :data-source="companyInfo.sysUserVO">
              <template #renderItem="{ item }">
                <ListItem class="flex gap-2 py-1" style="font-size: 14px; ">
                  <!-- <a-space> -->
                    <span>
                      {{ item.realname }} ({{ desensitize(item.username) }})
                    </span>
                    <!-- <span class="block w-[200px]">姓名：{{ item.nickName }}</span>
                    <span>账号：{{ item.email ? item.email : '--' }}</span> -->
                  <!-- </a-space> -->
                </ListItem>
              </template>
            </List>
          </DescriptionsItem>
        </Descriptions>
      </div>
      <template #footer>
        <div class="w-full flex justify-center">
          <a-button type="primary"  @click="joinCompany">加入企业</a-button>
        </div>
      </template>
    </BasicModal>
    <CompanyJoinModal @register="companyJoinRegister" @success="companyJoinHandle" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {Descriptions,DescriptionsItem,List,ListItem} from 'ant-design-vue';
  import { BasicForm, useForm } from '/@/components/Form';

  import CompanyJoinModal from './CompanyJoinModal.vue';
  import { useModal } from '/@/components/Modal';

  const { createMessage: msg } = useMessage();

  defineEmits(['success', 'register']);

  const companyInfo = ref<any>({
    name: '',
  });

  const [companyJoinRegister, { openModal: openCompanyJoin, closeModal: closeCompanyJoin }] =
    useModal();

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false, width: 800, mask: false,canFullscreen:false });
    companyInfo.value = data.record;
  });

  const getTitle = computed(() => '企业详情');
  const username = '';

  function desensitize(val: string) {
  if (!val) return '--';
  // 判断是否为邮箱
  if (val.includes('@')) {
    const [name, domain] = val.split('@');
    if (name.length <= 2) {
      return name[0] + '***@' + domain;
    }
    return name[0] + '***' + name[name.length - 1] + '@' + domain;
  }
  // 判断是否为手机号（11位数字）
  if (/^\d{11}$/.test(val)) {
    return val.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
  }
  // 其他类型直接返回
  return val;
  }

  async function handleSubmit() {
    try {
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
  async function joinCompany() {
    try {
      closeModal();
      openCompanyJoin(true, {
        companyName: companyInfo.value.name,
      });
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
  async function companyJoinHandle() {
    try {
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>


<style lang="less" scoped>
  .modal-content-bg {
    background: linear-gradient(135deg, #f7fafd 60%, #e3eefe 100%);
    border-radius: 14px;
    padding: 32px 32px 18px 32px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.08);
    min-width: 420px;
    @media (max-width: 600px) {
      padding: 16px 6px 10px 6px;
      min-width: 0;
    }
  }

  :deep(.ant-descriptions) {
    background: #fff;
    border-radius: 10px;
    padding: 24px 32px 12px 32px;
    margin-bottom: 24px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.06);
    border: 1px solid #e6f0fa;
  }

  :deep(.ant-descriptions-title) {
    font-size: 22px;
    font-weight: 700;
    color: #000;
    margin-bottom: 18px;
    text-align: center;
    border-bottom: 1.0px solid #e6f0fa;
    padding-bottom: 12px;
    letter-spacing: 1px;
  }



  :deep(.ant-descriptions-item-label) {
    font-size: 14px;
    // padding-bottom: 200px;
  }

  :deep(.ant-descriptions-item-content) {
    color: #222;
    font-size: 15px;
    font-weight: 500;
    padding-bottom: 2px;
  }

  :deep(.ant-list) {
    background: #f8fafc;
    border-radius: 4px;
    // padding: 10px 16px;
    // margin-top: 6px;
    border: 1px solid #e6f0fa;
    // font-size:12px;
  }

  // :deep(.ant-list-item) {
  //   padding: 6px 0 6px 0;
  //   border: none;
  //   background: transparent;
  //   transition: background 0.2s;
  //   &:hover {
  //     background: #e6f7ff;
  //   }
  // }

  .admin-title {
    font-size: 16px;
    font-weight: 600;
    color: #127fd2;
    margin-bottom: 8px;
    margin-top: 6px;
  }

  .w-full.flex.justify-center {
    margin-top: 10px;
    margin-bottom: 10px;
  }

  :deep(.ant-btn) {
    // font-size: 16px;
    border-radius: 8px;
    min-width: 100px;
    height: 32px;
    background: linear-gradient(90deg, #1890ff 0%, #40a9ff 100%);
    border: none;
    color: #fff;
    // font-weight: 600;
    box-shadow: 0 2px 8px rgba(24,144,255,0.10);
    transition: background 0.2s, box-shadow 0.2s;
    &:hover, &:focus {
      background: linear-gradient(90deg, #127fd2 0%, #1890ff 100%);
      color: #fff;
      box-shadow: 0 4px 16px rgba(24,144,255,0.18);
    }
  }

  /* 兼容性与响应式 */
  @media (max-width: 600px) {
    :deep(.ant-descriptions) {
      padding: 12px 6px 6px 6px;
    }
    :deep(.ant-list) {
      padding: 6px 4px;
    }
    :deep(.ant-btn) {
      min-width: 90px;
      height: 38px;
      font-size: 14px;
    }
  }
</style>
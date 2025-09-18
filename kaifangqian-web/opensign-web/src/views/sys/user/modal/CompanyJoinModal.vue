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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle">
      <div class="modal-content-bg">
        <div class="py-2">
          <a-space :size="20">
            <label class="block w-[60px] text-[14px]" style="color: #666;">我要加入</label>
            <span class="block font-semibold text-[14px]">{{ companyName }}</span>
            <span><a-tag color="success">已认证</a-tag></span>
          </a-space>
        </div>
        <div class="py-2">
          <a-space :size="20">
            <label class="block w-[60px] text-[14px]" style="color: #666;">邀请码</label>
            <span>
              <a-input style="width: 200px;" v-model:value="inviteCode" placeholder="请输入邀请码" />
            </span>
          </a-space>
        </div>
        <div >
           <p style="color: #999; margin-left: 80px;">企业邀请码（即团队码）是一个 8 位字母编码，可联系企业管理员获取</p>
        </div>
      </div>
      <template #footer>
        <div class="w-full flex justify-center">
          <a-button type="primary" class="w-[150px]" @click="applyJoinCompany">申请加入</a-button>
        </div>
      </template>
    </BasicModal>
  </div>
</template>
<script lang="ts" setup>
  import { ref, unref, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { jionTenant } from '/@/api/tenant';
  import { getMyTenantDeparts} from '/@/api/sys/user'; 
  import { useUserStoreWithOut } from '/@/store/modules/user';

  const userStore = useUserStoreWithOut();
 
  const emit = defineEmits(['success','register']);

  const { createMessage: msg } = useMessage();

  const companyName = ref('');

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false, width: 700, mask: false,canFullscreen:false });
    companyName.value = data.companyName;
  });

  const getTitle = computed(() => '申请加入企业');
  const inviteCode = ref('');
  async function applyJoinCompany() {
    let response;
    try {
      if (!unref(inviteCode)) {
        msg.error('请输入邀请码');
        return;
      }
      response = await jionTenant({ invitationCode: unref(inviteCode) });
      if (response.data.code == 200) {
        if (response.data.result == 1) {
          msg.success('加入企业成功',3);
          let tenantList = await getMyTenantDeparts();
          let matchDepart:any = tenantList.filter((u:any)=>u.tenantName == companyName.value);
          if((matchDepart&&matchDepart.length && companyName.value)){
              handleMenuClick(matchDepart[0])
          }
        } else {
          msg.success('已申请加入，管理员开启了申请加入审核流程，请耐心等待或联系管理员及时审核',10);
        }
        closeModal();
        emit('success');
      }else{
        msg.error(response.data.message);
      } 
    } finally {
       setModalProps({ confirmLoading: false });
    }
  }

    async function handleMenuClick(depart){
    let params = {
        departId:depart.departId
      }
      const result = await userStore.selectTenantAuth(params);
      if(result){
          msg.success('切换成功,即将跳转')
      }
    }
</script>

<style lang="less" scoped>
  .modal-content-bg {
    background: linear-gradient(135deg, #f7fafd 60%, #e3eefe 100%);
    border-radius: 10px;
    padding: 24px 24px 12px 24px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  }
  .py-2 {
    padding: 14px 0;
    // background: #fff;
    // border-radius: 6px;
    // margin-bottom: 12px;
    // box-shadow: 0 1px 6px rgba(0,0,0,0.03);
  }
  .block {
    display: inline-block;
  }
  .font-semibold {
    font-weight: 600;
    color: #222;
  }
  :deep(.ant-tag-success) {
    background: #e6fffb;
    color: #13c2c2;
    border-radius: 4px;
    font-size: 13px;
    padding: 2px 10px;
  }
  :deep(.ant-input) {
    min-width: 430px;
    border-radius: 6px;
    font-size: 15px;
    padding: 6px 12px;
  }
  .w-full.flex.justify-center {
    margin-top: 8px;
    margin-bottom: 8px;
  }
  // :deep(.ant-btn) {
  //   font-size: 15px;
  //   border-radius: 6px;
  //   min-width: 90px;
  // }
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
</style>

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
  <div class="main-invite">
    <Card size="small" title="成员加入" class="invite-card">
      <div class="invite-top">
        <div class="invite-content">
          <div class="invite-left block">
            <p class="block-title">
              企业邀请码
            </p>
            <p class="block-desc">用户可输入企业邀请码加入企业</p>
            <div class="block-code">
              {{ inviteCode }}
            </div>
            <a-button type="default" size="small" class="copy-btn" @click="handleCopy(inviteCode)">
              复制邀请码
            </a-button>
          </div>
          <div class="invite-right block">
            <p class="block-title">
              企业链接
            </p>
            <p class="block-desc">用户访问链接即可注册帐号及申请加入企业</p>
            <div class="block-qr">
              <img :src="qrCodeDataUrl" alt="企业邀请二维码" />
            </div>
            <div class="qr-actions">
              <a-button type="default" size="small" class="copy-btn" @click="handleCopy(inviteLink)">
                复制邀请链接
              </a-button>
              <a-button type="default" size="small" class="download-btn" @click="downloadQRCode">
                下载邀请二维码
              </a-button>
            </div>
          </div>
        </div>
        <div class="join-set">
          <span>加入企业须管理员审核</span>
          <a-switch v-model:checked="checked" @change="handleChangeJoinSet"></a-switch>
        </div>
      </div>
    </Card>
    <Card size="small" title="成员加入申请" class="apply-card">
      <BasicTable @register="registerTable">
        <template #status="{record}">
          <span>{{ loadStatus(record.status) }}</span>
        </template>
        <template #username="{record}">
          <span>{{ record.phone || record.email }}</span>
        </template>
        <template #action="{record}">
          <a-button type="link" :disabled="record.status != 0" @click="handlePass(record)">通过</a-button>
          <a-button type="link" :disabled="record.status != 0" @click="handleReject(record)" danger>驳回</a-button>
        </template>
      </BasicTable>
    </Card>
  </div>
</template>

<style lang="less" scoped>
.main-invite {
  max-width: 80%;
  margin: 0 auto;
  padding: 32px 16px 40px 16px;
  // background: #f5f7fa;
  min-height: 100vh;
}

.invite-card,
.apply-card {
  margin-bottom: 28px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 24px 0 rgba(0, 0, 0, 0.06);
  background: #fff;
}

.invite-top {
  width: 100%;
  padding: 28px 18px 18px 18px;
  background: #fff;
  border-radius: 8px;
  box-sizing: border-box;
}

.invite-content {
  display: flex;
  gap: 32px;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 18px;

  .block {
    flex: 1 1 360px;
    min-width: 320px;
    background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(135deg, #EFF6FF -4%, #FFFFFF 100%);
    border-radius: 8px;
    padding: 28px 24px 22px 24px;
    box-sizing: border-box;
    box-shadow: 0 2px 8px rgba(0,0,0,0.03);
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    position: relative;

    .block-title {
      font-size: 17px;
      font-weight: 600;
      color: #222;
      margin-bottom: 10px;
      letter-spacing: 0.5px;
    }

    .block-desc {
      font-size: 13px;
      color: #888;
      margin-bottom: 18px;
    }

    .block-code {
      font-size: 26px;
      color: #1a1a1a;
      margin: 12px 0 18px 0;
      word-break: break-all;
      letter-spacing: 2px;
      font-family: 'Fira Mono', 'Consolas', monospace;
      background: #fff;
      border-radius: 6px;
      padding: 8px 14px;
      border: 1px dashed #e5e7eb;
      min-height: 36px;
      display: flex;
      align-items: center;
    }

    .copy-btn {
      margin-top: 8px;
      font-size: 13px;
      border-radius: 4px;
      transition: background 0.2s;
      background: #f0f3fa;
      border: 1px solid #dbeafe;
      color: #2563eb;
      &:hover {
        background: #e0e7ff;
        color: #1d4ed8;
      }
    }
  }
}

.block-qr {
  margin: 0 auto 16px auto;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 120px;
  height: 120px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);

  img {
    width: 100px;
    height: 100px;
    border-radius: 8px;
    object-fit: contain;
    background: #fff;
    box-shadow: 0 1px 4px rgba(0,0,0,0.07);
  }
}

.qr-actions {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}

.download-btn {
  margin-top: 8px;
  font-size: 13px;
  border-radius: 4px;
  background: #f0f3fa;
  border: 1px solid #dbeafe;
  color: #2563eb;
  &:hover {
    background: #e0e7ff;
    color: #1d4ed8;
  }
}

.join-set {
  margin-top: 18px;
  font-size: 15px;
  color: #444;
  display: flex;
  align-items: center;
  gap: 12px;
  .ant-switch {
    margin-left: 0;
  }
}

@media (max-width: 900px) {
  .invite-content {
    flex-direction: column;
    gap: 18px;
    .block {
      min-width: 0;
      width: 100%;
    }
  }
  .main-invite {
    padding: 18px 4px 24px 4px;
  }
}

@media (max-width: 600px) {
  .main-invite {
    padding: 6px 0 12px 0;
  }
  .invite-card,
  .apply-card {
    margin-bottom: 18px;
    border-radius: 6px;
  }
  .invite-top {
    padding: 12px 4px 8px 4px;
    border-radius: 6px;
  }
  .invite-content .block {
    padding: 14px 8px 12px 8px;
    border-radius: 6px;
  }
  .block-qr {
    width: 90px;
    height: 90px;
    img {
      width: 70px;
      height: 70px;
      border-radius: 6px;
    }
  }
}
</style>

<script lang="ts">
import {ref, unref, defineComponent, onMounted} from "vue";
import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';
import { useMessage } from '/@/hooks/web/useMessage'; 
import { useUserStore } from '/@/store/modules/user';
import { ExclamationCircleOutlined} from '@ant-design/icons-vue';
import { Card } from 'ant-design-vue';
import { getInviteCode } from '/@/api/sys/dept';
import { BasicTable,useTable } from '/@/components/Table';
import {getJoinMemberData, checkTenantUser, setJoinFlag, getJoinFlag} from '/@/api/sys/user';
import { memberJoinColumn, joinSearchFormSchema } from './data';
import Icon from '/@/components/Icon';
import QRCode from 'qrcode';

export default defineComponent({
  name:"UserJoin",
  components:{
    ExclamationCircleOutlined,
    BasicTable,
    Icon,
    Card
  },
  setup() {
    const inviteCode = ref('');
    const inviteLink = ref('');
    const checked = ref(false);
    const joinSetId = ref('')
    const userStore = useUserStore();
    const userInfo =  userStore.getUserInfo;


    const { createMessage:msg, createConfirm } = useMessage();

    const [registerTable,{reload}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getJoinMemberData,
        columns:memberJoinColumn,
        immediate:true,
        fetchSetting:{
          listField:'records'
        },
        formConfig: {
          labelWidth: 80,
          schemas: joinSearchFormSchema,
        },
        dataSource:[],
        rowKey:'id',
        useSearchForm: true,
        showIndexColumn: false,
        canResize: false,
        isTriggerSelect:false,
        striped:false,
        bordered:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        pagination:true
    });

    onMounted(() => {
      fetch();
      getJoinFlagInfo()
    })

    const qrCodeDataUrl = ref('') // 新增：用于存储二维码 base64 数据
    // 生成二维码
    async function fetch() {
      let result = await getInviteCode({ departId: unref(userInfo.loginDepartId) })
      if (result) {
        inviteCode.value = result.invitationCode
        inviteLink.value = result.url
        try {
          qrCodeDataUrl.value = await QRCode.toDataURL(result.url) // 生成二维码
        } catch (error) {
          console.error('生成二维码失败:', error)
        }
      }
    }

    function downloadQRCode() {
      if (!qrCodeDataUrl.value) {
        msg.error('请先生成二维码')
        return
      }

      const link = document.createElement('a')
      link.href = qrCodeDataUrl.value
      link.download = `企业邀请二维码.png`
      link.click()
    }
    async function getJoinFlagInfo(){
      let result = await getJoinFlag({type:'tenant_user_check'});
        if(result){
          checked.value = JSON.parse(result.value);
          joinSetId.value = result.id;
        }
    }
    function loadStatus(status){
      switch(status){
            case 0:
              return '待审核';
            case 1:
              return '已通过';
            case 2:
              return '未通过';
            default:
              return '待审核';
          }
    }
    function handlePass(record){
      createConfirm({
        title: '确认', 
        content: "请确认是否通过该成员加入申请？",
        okText:'确定',
        iconType: 'info',
        onOk() {
          tenantUserCheck(record,1)
        },
      })

    }
    function handleReject(record){
      createConfirm({
        title: '确认', 
        content: "请确认是否驳回该成员加入申请？",
        okText:'确定',
        iconType: 'warning',
        onOk() {
          tenantUserCheck(record,2)
        },
      })
    }

    async function tenantUserCheck(record,type){
      let result = await checkTenantUser({id:record.id,status:type})
      if(result){
        msg.success('审核成功')
        reload();
      }
    }

    function handleCopy(value) {
        const { isSuccessRef } = useCopyToClipboard(
          value
        );
        unref(isSuccessRef) &&
        msg.success('复制成功');
    }
    async  function handleChangeJoinSet(val){
      let result = await setJoinFlag({value:val,id:joinSetId.value});
      if(result){
        msg.success('操作成功');
      }
    }
    return {
      inviteCode,
      inviteLink,
      handleCopy,
      userInfo,
      registerTable,
      handlePass,
      handleReject,
      checked,
      loadStatus,
      handleChangeJoinSet,
      qrCodeDataUrl,
      downloadQRCode, // 新增导出方法
    }
  }
})
</script>

<!-- <style lang="less" scoped>
.main-invite{
  // width: 90%;
  margin: 0 auto;
  padding: 25px;
}
:deep(.ant-card){
    margin-bottom: 25px;
    box-shadow: 5px 5px 30px #0000000e;
}
:deep(.ant-card-head-title){
    font-size:16px;
    font-weight:600;
  }
.invite-top {
  width: 100%;
  max-width: 960px;
  margin: 0 auto 30px; // 与下方卡片拉开距离
  padding: 20px;
  box-sizing: border-box;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}


.invite-content {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;
  margin-top: 20px;

  .block {
    flex: 1 1 calc(50% - 10px);
    min-width: 280px;
    border-radius: 6px;
    background-color: #f9f9f9;
    padding: 20px;
    box-sizing: border-box;
    transition: all 0.3s ease-in-out;

    // &:hover {
    //   transform: translateY(-2px);
    //   box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    // }

    .block-title {
      font-size: 15px;
      font-weight: 600;
      color: #333;
      margin-bottom: 8px;
    }

    .block-desc {
      font-size: 13px;
      color: #777;
      margin-bottom: 16px;
    }

    .block-code {
      font-size: 24px;
      color: #000;
      margin-top: 16px;
      word-break: break-all;
    }

    .copy-btn {
      margin-top: 12px;
      font-size: 12px;
      // padding: 4px 10px;
      // border-radius: 4px;
      // transition: background-color 0.3s;

      // &:hover {
      //   background-color: #e6e6e6;
      // }
    }
  }
}


.join-set {
  margin-top: 24px;
  font-size: 14px;
  color: #555;
  display: flex;
  align-items: center;
  justify-content: flex-start;

  .ant-switch {
    margin-left: 12px;
  }
}

  .block-qr {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100px;

  img {
    border-radius: 8px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  }
}

.download-btn {
  font-size: 12px;
  // padding: 4px 10px;
}
</style> -->

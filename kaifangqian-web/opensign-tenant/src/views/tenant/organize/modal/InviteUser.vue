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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
        <template #title>
          <span class="modal-title">{{ getTitle }}</span>
            <span class="organize-name">{{ organizeName }}</span>
        </template>
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
      </BasicModal>
</template>
<script lang='ts'>
  import { defineComponent,ref,reactive,unref } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { ExclamationCircleOutlined} from '@ant-design/icons-vue'
  import { useMessage } from '/@/hooks/web/useMessage'; 
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { useFormRules } from '/@/views/sys/login/useLogin';
  import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';
  import { getInviteCode } from '/@/api/sys/dept';
  import QRCode from 'qrcode';
  

  export default defineComponent({
    name: 'InviteUser',
    components:{
      BasicModal,
      Form,
      Input,
      Row, Col,
      ExclamationCircleOutlined
    },
    setup(_,{}){
      const getTitle = ref('邀请成员加入');
      const departId = ref('');
      const organizeName = ref('');
      const inviteCode = ref('');
      const inviteLink = ref('');
      const slideCodeShow = ref(false);
      const { getFormRules } = useFormRules();
      const formData = reactive({
        name:'',
        phone:'',
        captcha:''
      })
      const qrCodeDataUrl = ref('') // 新增：用于存储二维码 base64 数据
      const { createMessage: msg ,createSuccessModal} = useMessage();
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:800,
          canFullscreen:false,
          cancelText:'关闭' 
        });

        organizeName.value = data.record.organizeName;
        departId.value = data.record.currentDeptId;
        let result = await getInviteCode({departId:unref(departId)});
        if (result) {
          inviteCode.value = result.invitationCode
          inviteLink.value = result.url
          console.log('生成二维码链接:', result.url)
          try {
            qrCodeDataUrl.value = await QRCode.toDataURL(result.url) // 生成二维码
          } catch (error) {
            console.error('生成二维码失败:', error)
          }
        }
       
      });
      async function handleSubmit(){
       
       

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

      function handleCopy(value) {
        const { isSuccessRef } = useCopyToClipboard(
          value
        );
        unref(isSuccessRef) &&
        msg.success('复制成功');
      }
      function sendCodeApi(){
      return new Promise((resolve)=>{
          if(!formData.phone){
            msg.warning('请输入手机号');
            return false;
          }
          slideCodeShow.value = true;
          resolve(!unref(slideCodeShow));
          return !unref(slideCodeShow);
        })
      }
     

      return {
        handleCopy,
        sendCodeApi,
        registerModal,
        handleSubmit,
        getTitle,
        formData,
        getFormRules,
        inviteCode,
        inviteLink,
        organizeName,
        qrCodeDataUrl,
        downloadQRCode, // 新增导出方法
      }
    },
  })
</script>
<style lang="less" scoped>
.modal-title{
  margin-right: 20px;
}
.organize-name {
  color: #1976d2;
  padding: 4px 16px;
  box-sizing: border-box;
  border-radius: 16px;
  min-width: 60px;
  text-align: center;
  font-size: 13px;
  display: inline-block;
  background: linear-gradient(90deg, #e3f2fd 0%, #bbdefb 100%);
  font-weight: 500;
  margin-left: 10px;
  letter-spacing: 1px;
  border: 1px solid #90caf9;
  box-shadow: 0 2px 8px rgba(25, 118, 210, 0.08);
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

</style>

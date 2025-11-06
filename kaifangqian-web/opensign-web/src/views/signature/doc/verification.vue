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
  <a-spin :spinning="spinning" :delay="300">
    <div class="doc-verification-container">
      <span v-show="!verifyResult">
        <div class="verification-header">
          <h2 class="title">文件验签</h2>
          <p class="description"> 检测电子文件是否被篡改，文件是否真实有效 </p>
        </div>

        <div class="upload-wrapper">
          <a-upload
            :max-count="1"
            v-model:file-list="fileList"
            name="file"
            accept=".pdf"
            action="/resrun-paas/sign/verify/checkSign"
            :showUploadList="false"
            :headers="uploadHeaders"
            @change="handleChange"
            
          >
            <div class="upload-area" ref="uploadRef">
              <FilePdfOutlined class="file-icon" />
              <p class="upload-hint"> 请将电子文件拖动至此，或者点击上传 </p>
              <p class="file-size-tip"> 提示：支持 .pdf 格式，文件大小不超过30MB </p>
            </div>
          </a-upload>
        </div>
      </span>
      <template v-if="verifyResult">
        <div class="verify-result">
          <div class="bottom-border relative">
            <div class="text-base font-bold verify-title relative px-4"> 文档验证结果 </div>
            <div class="flex items-center gap-2 pt-2 px-4">
              <CheckCircleOutlined style="color: #52c41a" v-if="verifyResult.pdfSingResult == 4" />
              <CloseCircleOutlined style="color: #faad14" v-if="verifyResult.pdfSingResult == 3" />
              <span v-if="verifyResult.pdfSingResult == 4">文件未被修改</span>
              <span v-if="verifyResult.pdfSingResult == 3">文件已被修改</span>
            </div>
            <div class="py-2 text-sm font-semibold px-4">
              <label class="text-neutral-400 font-semibold">文档名称:</label>
              <label class="px-2">{{ verifyResult.pdfName }}</label>
            </div>
            <div class="py-2 text-sm font-semibold px-4">
              <label class="text-neutral-400">文档大小:</label>
              <label class="px-2">{{ verifyResult.pdfSize }}KB</label>
            </div>
            <div
              :class="[
                verifyResult.pdfSingResult == 1 || verifyResult.pdfSingResult == 2
                  ? ''
                  : verifyResult.pdfSingResult == 4
                  ? 'validate-success'
                  : 'validate-error',
              ]"
              class="absolute right-0 top-[-25px] w-[150px] h-[150px]"
            />
          </div>
          <a-divider>文档中包含{{ verifyResult.signatureDetails.length }}个电子签名</a-divider>
          <div class="result-content">
            
            <Collapse v-model:activeKey="activeKey" :ghost="true">
              <template #expandIcon="{ isActive }">
                <caret-right-outlined :rotate="isActive ? 90 : 0" />
              </template>
              <CollapsePanel
                :key="index"
                :header="`电子签名${index + 1}`"
                v-for="(item, index) in verifyResult.signatureDetails"
              >
                <div class="body body-border">
                  <div class="left">
                    <div class="item-label" style="color: rgba(0, 0, 0, 0.5)">签名信息</div>
                    <a-form-item label="签名方">
                      <span class="form-content-text">{{ item.certName }}</span>
                    </a-form-item>
                    <a-form-item label="签署时间">
                      <span class="form-content-text">{{ item.signTime }}</span>
                    </a-form-item>
                    <a-form-item label="签署时间来源">
                      <span class="form-content-text">{{ item.location }}</span>
                    </a-form-item>
                    <div style="height: 20px"></div>
                    <div class="item-label" style="color: rgba(0, 0, 0, 0.5)">证书信息</div>
                    <a-form-item label="序列号">
                      <span class="form-content-text">{{ item.serialNumber }}</span>
                    </a-form-item>
                    <a-form-item label="颁发机构">
                      <span class="form-content-text">{{ item.userDnName }}</span>
                    </a-form-item>
                    <a-form-item label="签名算法" v-if="item.sigAlgName">
                      <span class="form-content-text">{{ item.sigAlgName }}</span>
                    </a-form-item>
                    <a-form-item label="有效期">
                      <span class="form-content-text"
                        >{{ item.validStartTime }} - {{ item.validEndTime }}</span
                      >
                    </a-form-item>
                  </div>
                  <div class="right" v-if="verifyResult.pdfSingResult == 4">
                    <a-form-item label="">
                      <span class="form-content-success">签名有效</span>
                    </a-form-item>
                    <a-form-item label="">
                      <span class="form-content-success">自签名以来，内容未被修改</span>
                    </a-form-item>
                    <a-form-item label="" v-if="item.sealBase64">
                      <img class="seal-image" :src="'data:image/png;base64,' + item.sealBase64" />
                    </a-form-item>
                  </div>
                  <div class="right" v-if="verifyResult.pdfSingResult == 3">
                    <a-form-item label="">
                      <span class="form-content-error">签名无效</span>
                    </a-form-item>
                    <a-form-item label="">
                      <span class="form-content-error">自签名以来，内容已被篡改</span>
                    </a-form-item>
                    <a-form-item label="" v-if="item.sealBase64">
                      <img class="seal-image" :src="'data:image/png;base64,' + item.sealBase64" />
                    </a-form-item>
                  </div>
                </div>
              </CollapsePanel>
            </Collapse>
          </div>
          
        </div>
        <div class="flex justify-center py-4 gap-2">
          <a-button type="info" shape="round" @click="handleExpand" v-if="verifyResult.signatureDetails.length !== 1">{{
              verifyResult.signatureDetails.length ==activeKey.length?'收起所有签名':'展开所有签名' }}</a-button>
          <a-button type="primary" shape="round" @click="reloadUpload">重新上传</a-button>
        </div>
      </template>

      <!-- <p>{{ verifyResult }}</p> -->
    </div>
    <!-- <VerificationResultModal @register="registerModal" @success="handleSuccess" /> -->
  </a-spin>
</template>

<script lang="ts">
  import { defineComponent, ref, onMounted } from 'vue';
  import { FilePdfOutlined, CheckCircleOutlined, CloseCircleOutlined,CaretRightOutlined  } from '@ant-design/icons-vue';
  // import { useModal } from '/@/components/Modal';
  import { getAppEnvConfig } from '/@/utils/env';
  import { getToken } from '/@/utils/auth';
  import { Collapse, CollapsePanel } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  // import VerificationResultModal from "./modal/VerificationResultModal.vue"
  export default defineComponent({
    name: '',
    components: {
      FilePdfOutlined,
      CheckCircleOutlined,
      CloseCircleOutlined,
      Collapse,
      CollapsePanel,
      CaretRightOutlined 
    },
    setup() {
      const { createMessage } = useMessage();
      // const [registerModal, { setModalProps,openModal,closeModal }] = useModal();
      const fileList = ref([]);
      const { VITE_GLOB_APP_CODE } = getAppEnvConfig();
      const uploadHeaders = ref({
        'X-Access-Token': getToken(),
        'resrun-app-code': VITE_GLOB_APP_CODE,
      });
      const uploadRef = ref<any>();
      const activeKey = ref(['0']);
      const verifyResult = ref<any>(null);
      function handleSuccess() {
        spinning.value = false;
      }
      const spinning = ref(false);
      function handleChange(info: any) {
        if (info.file.status == 'uploading') {
          spinning.value = true;
          verifyResult.value = null;
        }
        if (info.file.status == 'done') {
          const result = info.file.response.result;
          if(result.pdfSingResult === 1 || result.pdfSingResult === 2){
            setTimeout(() => {
              createMessage.warning(info.file.response.message);
              spinning.value = false;
            }, 500);
          }else if(result.pdfSingResult === 3 || result.pdfSingResult === 4){
            activeKey.value = ['0'];
            setTimeout(() => {
              verifyResult.value = info.file.response.result;
              spinning.value = false;
            }, 500);
          }
        }
      }
      function reloadUpload (){
        // verifyResult.value = null;
        setTimeout(() => {
          console.log("reloadUpload",uploadRef.value);
          uploadRef.value.click()
          // console.log("ref",);
        }, 100);
      }
      
      function handleExpand(){
        // activeKey.value = [];
        if(verifyResult.value.signatureDetails.length !== activeKey.value.length){
          activeKey.value = [];
          for(var i = 0; i < verifyResult.value.signatureDetails.length; i++){
            activeKey.value.push(i+'');
          }
        }else{
          activeKey.value = [];
        }
        console.log("activeKey",activeKey.value,verifyResult.value.signatureDetails.length !== activeKey.value.length);
      }
      function showModal() {}
      onMounted(() => {
        console.log("activeKey",uploadRef.value);
      });
      return {
        showModal,
        handleSuccess,
        uploadHeaders,
        fileList,
        handleChange,
        spinning,
        verifyResult,
        activeKey,
        CheckCircleOutlined,
        CloseCircleOutlined,
        uploadRef,
        reloadUpload,
        handleExpand
      };
    },
  });
</script>

<style lang="less" scoped>
  .doc-verification-container {
    width: 100%;
    max-width: 960px;
    margin: 0 auto;
    padding: 10px 20px;
    box-sizing: border-box;
  }

  .verification-header {
    text-align: center;
    margin-bottom: 40px;
  }

  .title {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
  }

  .description {
    font-size: 16px;
    color: rgba(0, 0, 0, 0.5);
  }

  .upload-wrapper {
    display: flex;
    justify-content: center;
  }

  .upload-area {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    max-width: 800px;
    height: 300px;
    margin: 0 auto;
    border: 2px dashed rgba(0, 0, 0, 0.1);
    border-radius: 12px;
    // background-color: #f9fbfc;
    background: linear-gradient(135deg, #f8fafc 80%, #eaf7ff 100%);
    transition: all 0.3s ease-in-out;
    cursor: pointer;
    padding: 160px;
  }

  .upload-area:hover {
    border-color: #1890ff;
    background: linear-gradient(135deg, #eaf7ff 80%, #f8fafc 100%);
  }

  .file-icon {
    font-size: 60px;
    color: #88888850;
    margin-bottom: 30px;
  }

  .upload-hint {
    font-size: 18px;
    color: #333;
    margin-bottom: 10px;
  }

  .file-size-tip {
    font-size: 14px;
    color: rgba(0, 0, 0, 0.6);
  }

  .verify-result {
    // background-color: rgba(0, 0, 0, 0.2);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    padding: 20px;
  }

  .validate-success {
    background-image: url(/@/assets/icons/validate-success.svg);
    background-repeat: no-repeat;
    background-size: 150px 150px;
  }
  .validate-error {
    background-image: url(/@/assets/icons/validate-error.svg);
    background-repeat: no-repeat;
    background-size: 150px 150px;
  }
  .verify-title::after {
    // content: '';
    // position: absolute;
    // width: 4px;
    // height: 12px;
    // top: 6px;
    // left: 0;
    // background-color: #127fd2;
  }
  .result-content {
    max-height: 500px;
    overflow: auto;
    .title {
      padding: 0 15px;
      position: relative;
      line-height: 30px;
    }
    .title::after {
      content: '';
      position: absolute;
      width: 4px;
      height: 12px;
      top: 9px;
      left: 0;
      background-color: red;
    }
    .body {
      padding: 20px 0;
      display: flex;
      .left {
        width: 70%;
      }
      .right {
        width: 30%;
        height: 100%;
      }
    }
    .body-border {
      border: 1px solid rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      padding: 10px 20px;
    }

    .text-label,
    .text-value {
      line-height: 30px;
    }
    .text-label {
      text-align: left;
    }
  }
  .seal-image {
    max-width: 140px;
  }
  .form-content-success {
    color: #52c41a;
  }
  .form-content-error {
    color: #faad14;
  }
  :deep(.ant-form-item) {
    margin-bottom: 0;
  }
</style>

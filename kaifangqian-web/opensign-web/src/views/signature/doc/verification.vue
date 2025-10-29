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
      <div class="verification-header">
        <h2 class="title">文件验签</h2>
        <p class="description">
          检测电子文件是否被篡改，文件是否真实有效
        </p>
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
          <div class="upload-area">
            <FilePdfOutlined class="file-icon" />
            <p class="upload-hint">
              请将电子文件拖动至此，或者点击上传
            </p>
            <p class="file-size-tip">
              提示：支持 .pdf 格式，文件大小不超过30MB
            </p>
          </div>
        </a-upload>
      </div>
    </div>
    <VerificationResultModal @register="registerModal" @success="handleSuccess" />
  </a-spin>
</template>

<script lang="ts">
  
  import { defineComponent,ref,onMounted } from 'vue'
  import { FilePdfOutlined } from '@ant-design/icons-vue';
  import { useModal } from '/@/components/Modal';
  import { getAppEnvConfig } from '/@/utils/env';
  import { getToken } from '/@/utils/auth';
  import VerificationResultModal from "./modal/VerificationResultModal.vue"
  export default defineComponent({
    name:"",
    components:{
      FilePdfOutlined,VerificationResultModal
    },
    setup(){
      const [registerModal, { setModalProps,openModal,closeModal }] = useModal();
      const fileList = ref([]);
      const {VITE_GLOB_APP_CODE} = getAppEnvConfig();
      const uploadHeaders = ref({
        'X-Access-Token':getToken(),
        'resrun-app-code':VITE_GLOB_APP_CODE
      })
      
      function handleSuccess(){
        spinning.value = false;
      }
      const spinning = ref(false);
      function handleChange(info:any){
        if(info.file.status == "uploading"){
          spinning.value = true;
        }
        if(info.file.status == 'done'){
          //info.file.annexId = info.file.response.result;
          spinning.value = false;
          openModal(true,{
            record:info.file.response.result,
            isUpdate:false,
          })
        }
      }
      function showModal(){
        
      }
      return{
        showModal,registerModal,handleSuccess,uploadHeaders,fileList,handleChange,
        spinning
      }
    }
  });
</script>

<style lang="less" scoped>
.doc-verification-container {
  width: 100%;
  max-width: 960px;
  margin: 0 auto;
  padding: 40px 20px;
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
  // width: 100%;
  // max-width: 1000px;
  width: 1000px;
  height: 300px;
  margin: 0 auto;
  border: 1px solid rgba(0, 0, 0, 0.1);
  // border-radius: 12px;
  // background-color: #f9fbfc;
  background: linear-gradient(135deg, #f8fafc 80%, #eaf7ff 100%);
  transition: all 0.3s ease-in-out;
  cursor: pointer;
  padding: 160px;
}


.upload-area:hover {
  // border-color: #1890ff;
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
</style>

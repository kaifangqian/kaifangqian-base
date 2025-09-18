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
        <ul style="padding-left: 20px;font-size: 12px;">
          <li style="list-style-type:disc">在更新Logo设计过程中，请严格遵守国家相关法律法规，确保新设计及所含信息不构成对他人版权、商标权或任何其他合法权益的侵犯。一旦发生因Logo更换导致的任何侵权行为，责任将由平台使用者自行承担。</li>
          <li  style="list-style-type:disc;color:red" v-if="!configFlag">当前软件授权不支持自定义网站信息，如需，请升级软件授权</li>
        </ul>
      </template>
    </a-alert>
  <Loading :loading="loading" :absolute="false"/>
  <Loading :loading="uploadLoading" :absolute="false"  />
  <div class="tab-item">
    <div class="item-operation" >
      <a-button type="primary" @click="saveConfig" :disabled="!configFlag">保存</a-button>
    </div>
    <div class="item-from">
       <a-form ref="formRef"
          :model="formState"
          name="basic"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 20 }"
          autocomplete="off">
          <a-form-item
            label="系统标题"
            name="website_title">
            <a-input v-model:value="formState.website_title" :disabled="!configFlag" />
          </a-form-item>
          <a-form-item
            label="copyright"
            name="website_copyright">
            <!-- <a-input v-model:value="formState.website_copyright" /> -->
            <a-textarea v-model:value="formState.website_copyright" :rows="4" :disabled="!configFlag" />
          </a-form-item>
      
          <a-form-item label="白色logo" name="website_white_logo">
             <a-upload
               v-model:file-list="fileList1"
               name="file"
               :disabled="!configFlag"
               action="/resrun-paas/file/base64"
                :max-count="1"
               :headers="uploadHeaders"
               :before-upload="beforeUpload"
              @change="(file)=>handleChange(file,1)">
               <div style="padding-bottom: 10px;">
                 <a-button :disabled="!configFlag">
                   <Icon icon="ant-design:upload-outlined"></Icon>
                   {{fileList2.length == 1?'重新上传Logo':'上传Logo'}}
                 </a-button>
               </div>
               <template #itemRender="{ file, actions }">
                <div class="preview-logo">
                  <img :src="file.base64"/>
                </div>
               </template>
             </a-upload>
             <p class="logo-tips">支持图片格式png、jpg、gif，小于1M，尺寸：140*60，建议白色透明样式</p>
          </a-form-item>
          <a-form-item label="非白色logo" name="website_other_logo"
            >
            <a-upload
              v-model:file-list="fileList2"
              name="file"
              action="/resrun-paas/file/base64"
               :max-count="1"
              :headers="uploadHeaders"
              :before-upload="beforeUpload"
              :disabled="!configFlag"
              @change="(file)=>handleChange(file,2)">
              <div style="padding-bottom: 10px;">
                <a-button :disabled="!configFlag">
                  <Icon icon="ant-design:upload-outlined"></Icon>
                  {{fileList2.length == 1?'重新上传Logo':'上传Logo'}}
                </a-button>
              </div>
              <template #itemRender="{ file, actions }">
               <div class="preview-logo">
                 <img :src="file.base64"/>
               </div>
              </template>
            </a-upload>
            <p class="logo-tips">支持图片格式png、jpg、gif，小于1M，尺寸：140*60，建议非白色透明样式</p>
          </a-form-item>
        </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive,onMounted } from 'vue';
  import { getToken } from '/@/utils/auth';
  import { getAppEnvConfig } from '/@/utils/env';
  import { getBase64ByConfigType } from '/@/api/sys/upload';
  import {fileToBase64} from "/@/utils/file/base64Conver"
  import {getSafeConfig,setSafeConfig} from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import { message, Upload } from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  
  import { getSystemLimit} from '/@/api/license';
  
  const {VITE_GLOB_APP_CODE} = getAppEnvConfig();
  
  const formState = reactive({
    website_title: '',
    website_copyright: '',
    website_white_logo: '',
    website_other_logo: '',
  });
  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const fileList1:any = ref([]);
  const fileList2:any = ref([]);
  const handlePreview = (file)=>{
    
  }
  const configFlag = ref(false);
  //
  onMounted(()=>{
    console.log("web onMounted")
    initData();
  })
  const uploadHeaders = reactive({
    'X-Access-Token':getToken(),
    'resrun-app-code':VITE_GLOB_APP_CODE
  })
  
  const configKeys = ['website_title','website_copyright','website_white_logo','website_other_logo'];
  
  async function handleChange (info: any,type:number){
    if(info.file.status == 'done' && type == 1){
      uploadLoading.value = false;
      if(info.file.response.success){
        formState.website_white_logo = info.file.response.result
        // console.log( await fileToBase64(info.file.originFileObj));
        info.file.base64 = await fileToBase64(info.file.originFileObj);
      }else{
        message.warning(info.file.response.message);
      }
    }else if(info.file.status == 'done' && type == 2){
      uploadLoading.value = false;
      if(info.file.response.success){
        formState.website_other_logo = info.file.response.result
        info.file.base64 = await fileToBase64(info.file.originFileObj);
      }else{
        message.warning(info.file.response.message);
      }
    }
  }
  
  async function saveConfig(){
    try {
      loading.value = true;
      const values = await formRef.value.validateFields();
      configData.value.forEach((item:any)=>{
        item.value = values[item.type]
      })
      const result = await setSafeConfig({sysConfigs:configData.value})
      if(result.success){
        message.success("保存成功");
      }else{
        message.success(result.message);
      }
      loading.value = false;
    } catch (errorInfo) {
      loading.value = false;
    }
  }
  async function initData(){
    var params = configKeys.join(",")
    const result = await getSafeConfig({types:params})
    if(result && result.length>0){
      configData.value = result;
      for(var i=0;i<result.length;i++){
        var item = result[i];
        formState[item.type] = item.value;
        if(item.type =='website_white_logo'){
          const logo = await getBase64ByConfigType(item.type);
          fileList1.value.push({
            base64:logo.image
          })
        }
        if(item.type =='website_other_logo'){
          const logo = await getBase64ByConfigType(item.type);
          fileList2.value.push({
            base64:logo.image
          })
        }
      }
       
    }
    
    const limit = await getSystemLimit();
    configFlag.value = limit.websiteCustomizateUseFlag;
  }
  function beforeUpload(file:any){
    const isJpgOrPng = file.type === 'image/jpg' || file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif';
    if(!isJpgOrPng) {
      message.error('文件类型不正确');
      uploadLoading.value = false;
      return Upload.LIST_IGNORE;;
    }
    const fileSize = file.size / 1024 / 1024;
    if(fileSize > 1){
      message.error('文件大小不不能超过1M');
      uploadLoading.value = false;
      return Upload.LIST_IGNORE;;
    }
    uploadLoading.value = true;
    return true;
  }
</script>

<style lang="less" scoped>
  .tab-item{
    width:1200px;
    .item-operation{
      text-align: right;
      line-height: 60px;
    }
    .item-from{
      
    }
    .logo-tips{
      font-size: 12px;
      color:rgba(0,0,0,0.6);
      line-height: 40px;
    }
  }
  .preview-logo{
    width: 140px;
    height: 60px;
    padding: 5px;
    box-sizing: border-box;
    border:1px solid #eee;
    background-color: #002b45;
    img{
      width: 100%;
      height: 100%;
    }
  }
</style>

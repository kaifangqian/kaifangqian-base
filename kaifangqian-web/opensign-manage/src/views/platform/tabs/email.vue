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
  <Loading :loading="loading" :absolute="false" />
  <div class="tab-item">
    <div class="item-operation">
      <a-button type="primary" @click="saveConfig">保存</a-button>
    </div>
    <div class="item-from">
       <a-form ref="formRef"
          :model="formState"
          name="basic"
          :label-col="{ span: 3 }"
          :wrapper-col="{ span: 21 }"
          :label-align="'left'"
          autocomplete="off">
           
          
          <a-form-item
            label="邮件发送方名称"
            name="email_nick_name"
            :rules="[{ required: true, message: '不能为空' }]">
            <a-input v-model:value="formState.email_nick_name" />
          </a-form-item>
          
          <a-form-item
            label="邮箱登录名"
            name="email_username"
            :rules="[{ required: true, message: '邮箱登录名不能为空' }]">
            <a-input v-model:value="formState.email_username" />
          </a-form-item>
          
          <a-form-item
            label="SMTP服务授权码"
            name="email_auth_code"
            :rules="[{ required: true, message: 'SMTP服务授权码不能为空' }]">
            <a-input v-model:value="formState.email_auth_code" />
          </a-form-item>
          
          <a-form-item
            label="SMTP服务器地址"
            name="email_host"
            :rules="[{ required: true, message: 'SMTP服务器地址不能为空' }]">
            <a-input v-model:value="formState.email_host" />
          </a-form-item>
          
          <a-form-item
            label="端口号"
            name="email_port"
            :rules="[{ required: true, message: '端口号不能为空' }]">
            <a-input v-model:value="formState.email_port" />
          </a-form-item>
           
        </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive,onMounted } from 'vue';
  import {fileToBase64} from "/@/utils/file/base64Conver"
  import {getSafeConfig,setSafeConfig} from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import {message} from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  const formState = reactive({
    email_username: '',
    email_host: '',
    email_auth_code: '',
    email_port: '',
    email_nick_name:''
  });
  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const fileList1:any = ref([]);
  const fileList2:any = ref([]);
  const handlePreview = (file)=>{
    
  }
  
  onMounted(()=>{
    initData();
  })
  
  
  
  
  
  
  const configKeys = ['email_username','email_host','email_auth_code','email_port','email_nick_name'];
  
  
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
        
      }
    }
    console.log(formState);
  }
</script>

<style lang="less" scoped>
  .tab-item{
    margin-left: 20px;
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

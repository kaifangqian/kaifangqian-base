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
  <Loading :loading="uploadLoading" :absolute="false" />
  <div class="tab-item">
    <div class="item-operation">
      <a-button type="primary" @click="saveConfig">保存</a-button>
    </div>
    <div class="item-from">
       <a-form ref="formRef"
          :model="formState"
          name="basic"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 20 }"
          autocomplete="off">
          <a-form-item
            label="实名认证服务配置"
            name="tenant_auth_switch">
            <a-switch v-model:checked="formState.tenant_auth_switch" checked-value="true" un-checked-value="false"
            v-if="environment !== 'test'"/>
            <a-tooltip placement="bottom" v-else>
              <template #title>
                <span>当前为测试授权，无法进行该项配置</span>
              </template>
              <a-switch v-model:checked="formState.tenant_auth_switch" checked-value="true" un-checked-value="false" disabled/>
            </a-tooltip>
            
          </a-form-item>
          <a-divider></a-divider>
          <div style="padding: 10px 30px;font-size: 12px;">
            <p>开关打开：<span style="font-weight: 600;">已对接个人和企业实名认证环节中所需的第三方服务，</span>实名认证需要提交真实信息，提交的实名信息经第三方接口真实校验。</p>
            <p>开关关闭：<span style="font-weight: 600;">尚未对接个人和企业实名认证环节中所需的第三方服务，</span>模拟实名认证流程，实名认证时，无需个人/企业用户提交真实身份信息。</p>
          </div>
        </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive,onMounted } from 'vue';
  import { getToken } from '/@/utils/auth';
  import { getAppEnvConfig } from '/@/utils/env';
  import {fileToBase64} from "/@/utils/file/base64Conver"
  import {getSafeConfig,setSafeConfig} from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import {message} from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  import { getSystemLimit} from '/@/api/license';
  const formState = reactive({
    tenant_auth_switch: ''
  });
  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const environment = ref("test");
  onMounted(()=>{
    initData();
  })
 
  const configKeys = ['tenant_auth_switch'];
  
  
  async function saveConfig(){
    try {
      loading.value = true;
      // const values = await formRef.value.validateFields();
      configData.value.forEach((item:any)=>{
        item.value = formState[item.type]
      })
      const result = await setSafeConfig({sysConfigs:configData.value})
      if(result.success){
        message.success("保存成功");
      }else{
        message.success(result.message);
      }
      // console.log("formState：",formState);
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
    const limit = await getSystemLimit();
    environment.value = limit.environment;
    console.log(formState);
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

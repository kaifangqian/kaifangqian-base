<template>
   
  <Loading :loading="loading" :absolute="false" />
  <Loading :loading="uploadLoading" :absolute="false"/>
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
            label="CA数字证书服务配置"
            name="ca_type">
            <a-switch v-model:checked="formState.ca_type" checked-value="true" un-checked-value="false"
            v-if="environment !=='test'"/>
            
            <a-tooltip placement="bottom" v-else>
              <template #title>
                <span>当前为测试授权，无法进行该项配置</span>
              </template>
              <a-switch v-model:checked="formState.ca_type" checked-value="true" un-checked-value="false" disabled/>
            </a-tooltip>
            
          </a-form-item>
          <a-divider></a-divider>
          <div style="padding: 10px 30px;font-size: 12px;">
            <p>开关打开：<span style="font-weight: 600;">已自行对接证书服务或已获取开放签CA数字证书服务授权，并完成服务配置，用户在签署时，</span>使用CA机构下发的数字证书进行签署，签署的文件具备法律效力。</p>
            <p>开关关闭：<span style="font-weight: 600;">尚未对接证书服务且尚未获取开放签CA数字证书服务授权，</span>使用平台生成的测试证书进行签署，签署的文件不具备法律效力。如需要签署具备法律效力的电子文件，请尽快获取真正的CA数字证书授权。</p>
          </div>
          <a-form-item
            label="CA证书签发规则"
            name="ca_use_type" v-if="formState.ca_type == 'true'">
             <a-radio-group v-model:value="formState.ca_use_type" disabled>
                <a-radio  style="display: flex" value="1">规则1：企业和个人均使用事件证书</a-radio>
                <a-radio  style="display: flex"  value="2">规则2：企业使用长效证书，个人使用事件证书</a-radio>
                <a-radio  style="display: flex"  value="3">规则3：企业和个人均使用长效证书</a-radio>
            </a-radio-group>
            <p style="font-size: 12px;padding-top: 20px;color: #999;">（事件证书：针对于某一事项签发的证书，有效期1天；长效证书：有效期1年）</p>
          </a-form-item>
        </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive,onMounted } from 'vue';
  import { getAppEnvConfig } from '/@/utils/env';
  import {getSafeConfig,setSafeConfig} from '/@/api/sys/safe';
  import { Icon } from '/@/components/Icon';
  import {message} from 'ant-design-vue';
  import { Loading } from '/@/components/Loading';
  import { getSystemLimit} from '/@/api/license';
  const formState = reactive({
    ca_type: '',
    ca_use_type:''
  });
  const loading = ref(false);
  const uploadLoading = ref(false);
  const configData = ref([]);
  const formRef = ref();
  const fileList1:any = ref([]);
  const fileList2:any = ref([]);
  const handlePreview = (file)=>{
    
  }
  const environment = ref("test");
  
  onMounted(()=>{
    initData();
  })
  
  const configKeys = ['ca_type','ca_use_type'];
  
  
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

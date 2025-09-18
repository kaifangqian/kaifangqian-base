<template>
  <div class="callback-container">
    <a-alert
      message="温馨提示："
      type="info"
    >
        <template #description>
            <p><a-badge status="default" text="Default"  color="#333"/>合同签署流程中发生发送合同、填写提交、签署、撤回、完成签署等事件时，开放签会向应用的“回调地址”推送相应的事件消息，进行数据的同步。</p>
            <p><a-badge status="default" text="Default"   color="#333"/>如果您需要开通该业务线的回调服务，请确保您已经获得API接口服务的对接授权凭证，并将凭证与该业务线进行绑定。</p>
        </template>
    </a-alert>
    <a-card  title="绑定对接授权凭证">
        <div class="certificate-bind">
            <span>对接授权凭证</span>
            <a-switch v-model:checked="lineFormData.ccedType" @change="handelChange"></a-switch>
        </div>
        
    </a-card>
    <a-modal v-model:visible="certificateVisible" title="绑定对接授权凭证" @ok="handleOk">
        <a-form
                :model="formState"
                name="basic"
                ref="formRef"
                :label-col="{ span: 8 }"
                :wrapper-col="{ span: 16 }"
                autocomplete="off"
            >
                <a-form-item
                label="授权凭证"
                name="certificate"
                :rules="[{ required: true, message: '请输入授权凭证' }]"
                >
                <a-input v-model:value="formState.certificate" />
                </a-form-item>
            </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent} from "vue"
export default defineComponent({
  name:"CallBack",
  props:{
    lineFormData:{
      type:Object,
      default:function(){
        return {}
      } 
    }
  },
  setup() {

    const certificateVisible = ref(false) 
    const formRef = ref()
    const formState = ref({
        certificate:''
    })

    function handleOk(){
        formRef.value.validate().then(res=>{
            
        })

    }
    function handelChange(val){
        console.log(val,'测试数据')
        if(!val){
            certificateVisible.value = true
        }
    }
    return {
        certificateVisible,
        handleOk,  
        handelChange,
        formState,
        formRef,
    }
  }
})
</script>

<style lang="less" scoped>
.callback-container{
    .ant-card{
        margin-top:20px;
    }
    :deep(.ant-card-body){
        padding:20px 25px;
    }
    .certificate-bind{
        display: flex;
        justify-content: space-between;
    }
}
</style>

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
  <span >
    <Modal class="sensitive-modal" v-model:visible="visibleModal" width="450px" title="当前操作为敏感操作请完成验证以继续" @ok="handleOk"  @cancel="handleCancel" :closable="false" :destroyOnClose="true">
        <Form  class="p-3 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
            <FormItem name="sensitivePhone" class="enter-x" v-if="verifyType==='phone'">
              <Input
                size="large"
                v-model:value="formData.sensitivePhone"
                :disabled="userInfo.phone?true:false"
                placeholder="手机号"
                class=""
              />
            </FormItem>
            <FormItem name="sensitivePassword" class="enter-x" v-if="verifyType==='password'">
              <Input
                size="large"
                v-model:value="formData.sensitivePassword"
                placeholder="密码"
                class=""
              />
            </FormItem>
            <FormItem name="sensitiveCaptch" class="enter-x" v-if="verifyType==='phone'">
              <CountdownInput
                size="large"
                class="fix-auto-fill"
                v-model:value="formData.sensitiveCaptch"
                placeholder="短信验证码"
                ref="smsCount"
                :sendCodeApi="sendCodeApi"
              />
            </FormItem>
        </Form>
        <PuzzleVerification  :w="385" @success="handleSildeSuccess" :sliderText="sliderText" @fail="handleFail"  v-if="slideCodeShow"/>
    </Modal>
  </span>
</template>
<script lang='ts'>
import { defineComponent,ref, unref ,reactive, computed, getCurrentInstance, onBeforeUnmount} from 'vue';
import { Form, Input, Modal} from 'ant-design-vue';
import PuzzleVerification from '/@/components/verification/index.vue';
import { CountdownInput } from '/@/components/CountDown';
import { getSmsCode } from '/@/api/sys/user'; 
import {useFormValid } from '/@/views/sys/login/useLogin';
import { useUserStore } from '/@/store/modules/user';
import { useAppStoreWithOut } from '/@/store/modules/app';
import { useMessage } from '/@/hooks/web/useMessage';
import { Md5 } from "ts-md5";


export default defineComponent({
  name: 'SensitiveModal',
  components:{
    Form,
    Input,
    FormItem:Form.Item,
    PuzzleVerification,
    CountdownInput,
    Modal

  },
  props:{
    visible:{ type: Boolean },
    sensitiveVerifyType:{type:String}
  },
  setup(_,{emit}){
    const appStore  = useAppStoreWithOut();
    const slideCodeShow = ref(false);
    const sliderText = ref('向右滑动完成拼图');
    const { createMessage: msg } = useMessage();
    const userStore = useUserStore();
    const userInfo = userStore.getUserInfo;
    const smsCount = ref(null);
    const visibleModal = computed(()=> appStore.getSensitiveConfig.visible);
    console.log(visibleModal);
    const verifyType = computed(()=>  appStore.getSensitiveConfig.sensitiveType)
    const formRef = ref();
    const callBackFun = reactive({
      callbacks:[] as any
    });
    const eventHandler = async (funsArray) => {
      //回调函数可能有多个
     callBackFun.callbacks = funsArray;
    };
    const instance = getCurrentInstance();
    const { eventHub } = instance?.proxy;

    console.log(eventHub,'事件总线');
   
    eventHub.$on('btnCallback', eventHandler);

    onBeforeUnmount(() => {
      eventHub.$off('btnCallback', eventHandler);
    });

  
    const getFormRules = ref({
      sensitivePhone:[{ required: true, trigger:'blur', message:'请输入账号'}]
    })
    const formData = reactive({
      sensitivePhone:userInfo.phone,
      sensitiveCaptch:'',
      sensitiveCaptchKey:'',
      sensitivePassword:''

    });
    const { validForm } = useFormValid(formRef);



    async function sendCodeApi(){
      const data = await validForm();
      
      if(!data) return;
      return new Promise((resolve)=>{
        slideCodeShow.value = true;
        resolve(!unref(slideCodeShow));
        return !unref(slideCodeShow);
      })
    }
    function handleFail(){
      sliderText.value = '验证失败';
      setTimeout(()=>{
        sliderText.value = '向右滑动完成拼图';
      },1000)
    }

     function handleSildeSuccess(){
      sliderText.value = '验证成功';
      setTimeout(async ()=>{
        if(smsCount.value){
          slideCodeShow.value = false;
          let result = await getSmsCode({phone:formData.sensitivePhone,type:'mobile_login'});
          if(result){
            formData.sensitiveCaptchKey = result;
          }
          smsCount.value.triggerClick();
        }
      },2000)
    }


    async function handleOk(){
      const data = await validForm();
      if(!data) return;
      appStore.setSensitiveHeader({...formData,sensitivePassword: Md5.hashStr(formData.sensitivePassword)});
      let retryInfo =  appStore.getAxiosRetryConfig;
      console.log(retryInfo,'重试信息')
      if(retryInfo){
        retryInfo.instance.request(retryInfo.config).then(res=>{
          if(res.data.code===200){
            appStore.setSensitiveConfig({visible:false,sensitiveType:verifyType});
            appStore.clearSensitiveHeader();
            console.log(callBackFun.callbacks)
            callBackFun.callbacks&&callBackFun.callbacks.map(fun=>{
              fun();
            });
            //置空回调方法
            eventHub.$emit('btnCallback',[])
            msg.success('操作成功');
            console.log(eventHub)
          }else{
            msg.warning(res.data.message)
          }
          console.log(res,'再次请求成功');
        })
      }
      emit('confirm')
    }
    function handleCancel(){
     appStore.setSensitiveConfig({visible:false,sensitiveType:verifyType});
    }
    return {  
      handleCancel,
      handleOk,
      formData,
      sendCodeApi,
      slideCodeShow,
      handleSildeSuccess,
      sliderText,
      getFormRules,
      smsCount,
      handleFail,
      formRef,
      visibleModal,
      verifyType,
      userInfo
    }
  }
})
</script>

<!-- <style lang="less" scoped>
.sensitive-modal{
  .ant-modal{
    .ant-modal-body{
      padding:24px;
    }
  }
}

</style> -->

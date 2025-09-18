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
    <Modal class="sensitive-modal" v-model:visible="visibleModal" width="550px" title="当前操作为敏感操作请完成验证以继续"
       @cancel="handleCancel" :closable="false" :destroyOnClose="true">
       <div class="sensitive-method">
        <a-radio-group v-model:value="verifyMethod" name="radioGroup">
            <a-radio value="phone">手机号验证</a-radio>
            <a-radio value="email">邮箱验证</a-radio>
            <a-radio value="password">密码</a-radio>
          </a-radio-group>
       </div>
      <Form  class="p-3 enter-x" :model="formData" :rules="getFormRules" ref="formRef" :labelCol="{style: { width: '80px' }}">
          <div  v-if="verifyMethod==='phone'">
            <div v-if="formData.sensitivePhone">
              <FormItem label="手机号" name="sensitivePhone" class="enter-x" >
                <Input
                  size="large"
                  v-model:value="formData.sensitivePhone"
                  :disabled="true"
                  placeholder="手机号"
                  class=""
                />
              </FormItem>
              <FormItem label="验证码" name="sensitiveCaptch" class="enter-x">
                <CountdownInput
                  size="large"
                  class="fix-auto-fill"
                  v-model:value="formData.sensitiveCaptch"
                  placeholder="验证码"
                  ref="smsCount"
                  :sendCodeApi="sendCodeApi"
                />
              </FormItem>
            </div>
              <div v-else class="no-sensitive-bind">
                <p>您的账号暂未绑定手机号，无法进行手机号验证</p>
                <a-button type="link" @click="handleUserCenter">绑定手机号</a-button>
              </div>
          </div>
          <div  v-if="verifyMethod==='email'">
            <div v-if="formData.sensitiveEmail">
              <FormItem   label="邮箱"  name="sensitivePhone" class="enter-x" >
                <Input
                  size="large"
                  v-model:value="formData.sensitiveEmail"
                  :disabled="true"
                  placeholder="邮箱"
                  class=""
                />
              </FormItem>
              <FormItem label="验证码" name="sensitiveCaptch" class="enter-x">
                <CountdownInput
                  size="large"
                  class="fix-auto-fill"
                  v-model:value="formData.sensitiveCaptch"
                  placeholder="验证码"
                  ref="smsCount"
                  :sendCodeApi="(sendCodeApi as any)"
                />
              </FormItem>
            </div>
              <div v-else class="no-sensitive-bind">
                <p>您的账号暂未绑定邮箱，无法进行邮箱验证</p>
                <a-button type="link" @click="handleUserCenter">绑定邮箱</a-button>
              </div>
          </div>
          <div  v-if="verifyMethod==='password'"> 
            <FormItem  label="密码" name="sensitivePassword" class="enter-x" >
              <Input
                size="large"
                v-model:value="formData.sensitivePassword"
                placeholder="密码"
                class=""
              />
            </FormItem>
          </div>
        </Form>
        <PuzzleVerification  :w="385" @success="handleSildeSuccess" :sliderText="sliderText" @fail="handleFail"  v-if="slideCodeShow"/>
        <template #footer>
          
          <a-button @click="handleCancel">取消</a-button>
          <a-button @click="handleOk" type="primary" :loading="submitLoading">确定</a-button>
        </template>
    </Modal>
  </span>
</template>
<script lang='ts'>
import { defineComponent,ref, unref ,reactive, computed, getCurrentInstance, onBeforeUnmount, watch} from 'vue';
import { Form, Input, Modal} from 'ant-design-vue';
import PuzzleVerification from '/@/components/verification/index.vue';
import { CountdownInput } from '/@/components/CountDown';
import { getTokenSms, getTokenEmail} from '/@/api/sys/user'; 
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
    const verifyMethod = ref('phone')
    const { createMessage: msg } = useMessage();
    const userStore = useUserStore();
    const userInfo = userStore.getUserInfo;
    const smsCount = ref(null);
    const visibleModal = ref(false)
    const submitLoading = ref(false);
    console.log(visibleModal,'敏感操作设置------');
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


    watch(
      () => appStore.getSensitiveConfig.visible,
      (value) => {
        if (value){
          visibleModal.value = true
          formData.sensitiveCaptch = '';
          formData.sensitiveCaptchKey = '';
          formData.sensitivePassword = '';
          verifyMethod.value = 'phone';
        }else{
          visibleModal.value = false
        }
      }
    );

  
    const getFormRules = ref({
      /* sensitivePhone:[{ required: false, trigger:'blur', message:'请输入账号'}] */
    })
    const formData = reactive({
      sensitivePhone:userInfo.phone,
      sensitiveEmail:userInfo.email,
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
          let result 
          if(verifyMethod.value == 'phone'){
            result = await getTokenSms({phone:formData.sensitivePhone,type:'common'});
          }else{
            result = await getTokenEmail({email:formData.sensitiveEmail,type:'email'});
          }
          if(result){
            formData.sensitiveCaptchKey = result;
          }
          smsCount.value.triggerClick();
        }
      },2000)
    }


    async function handleOk(){
      
      submitLoading.value = true;
      const data = await validForm({});
      if(!data){
        submitLoading.value = false;
        return
      };
      appStore.setSensitiveHeader({...formData,sensitivePassword: Md5.hashStr(formData.sensitivePassword),sensitiveType:verifyMethod.value});
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
            // msg.success('操作成功');
            console.log(eventHub)
          }else{
            msg.warning(res.data.message)
          }
          submitLoading.value = false;
          console.log(res,'再次请求成功');
        })
      }
      emit('confirm');
    }
    function handleCancel(){
     appStore.setSensitiveConfig({visible:false,sensitiveType:verifyType});
    }
    function handleUserCenter(){
      let appInfo = window.appInfo.sign_app_info;
      window.open(appInfo.url+'/#/user/centerInfo')
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
      userInfo,submitLoading,
      verifyMethod,
      handleUserCenter
    }
  }
})
</script>

<style lang="less" scoped>
.sensitive-method{
  margin-bottom:25px;
}
.sensitive-modal{
  #form_item_sensitiveCaptch{
    height: 33px;
  }
  // .ant-btn-lg{
  //   height: 33px!important;
  // }
}
.no-sensitive-bind{
  display: flex;
  justify-content: center;
  align-items: center;
  p{
    margin-bottom:0;
    font-weight: 550;
  }
}
</style>

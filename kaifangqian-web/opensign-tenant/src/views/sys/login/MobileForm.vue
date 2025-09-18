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
  <div>
    <Form  class="p-3 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
        <FormItem name="phone" class="enter-x">
            <Input
              size="large"
              :disabled="TabKey==='LOGIN_VERIFY'"
              v-model:value="formData.phone"
              placeholder="手机号"
              class="fix-auto-fill"
            />
          </FormItem>
          <FormItem name="captcha" class="enter-x">
            <CountdownInput
              size="large"
              class="fix-auto-fill"
              v-model:value="formData.captcha"
              placeholder="短信验证码"
              ref="smsCount"
              :sendCodeApi="sendCodeApi"
            />
          </FormItem>
        <FormItem class="enter-x">
          <Button type="primary" size="default" block @click="handleLogin" :loading="loading">
            登录
          </Button>
          <Button size="large" block class="mt-4" @click="handleBackLogin" v-if="TabKey==='LOGIN_VERIFY'">
            返回
          </Button>
        </FormItem>
        <Row class="enter-x">
            <Col :span="24">
              <FormItem :style="{ 'text-align': 'right' }">
                <a-button type="link" size="small" @click="showPhoneEmail('FIND_ACCOUNT')">
                  忘记账号？
                </a-button>
                <a-button type="link" size="small" @click="showPhoneEmail('RESET_PASSWORD')">
                  忘记密码？
                </a-button>
              </FormItem>
            </Col>
        </Row>
      </Form>
      <PuzzleVerification :w="356" @success="handleSildeSuccess" :sliderText="sliderText" @fail="handleFail"  v-if="slideCodeShow"/>
      <MobileOrEmail  @register="registerPhoneEmailModal" @success="handlePhoneEmailSuccess"/>
      <LoginSelectAccount @register="registerModal" @success="handleSuccess"/>
  </div>
</template>
<script lang='ts'>
import { defineComponent, reactive, ref, unref, computed } from 'vue';
import { Form, Input, Button,Row, Col } from 'ant-design-vue';
import { CountdownInput } from '/@/components/CountDown';
import { useModal } from '/@/components/Modal';
import { useLoginState,LoginStateEnum, useFormRules, useFormValid } from './useLogin';
import PuzzleVerification from '/@/components/verification/index.vue';
import LoginSelectAccount from './LoginSelectAccount.vue';
import MobileOrEmail from './MobileOrEmail.vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { useUserStore } from '/@/store/modules/user';
import { getSmsCode ,getSmsCodeByUsername,getEmailCodeByUsername} from '/@/api/sys/user'; 
import { useDesign } from '/@/hooks/web/useDesign';
import { Md5 } from "ts-md5";

export default defineComponent({
  name: 'MobileForm',
  components:{
    Form,
    FormItem:Form.Item,
    Input,
    Button,
    CountdownInput,
    Row, Col,
    PuzzleVerification,
    MobileOrEmail,
    LoginSelectAccount

  },
  setup(_,{}){
    
    const { prefixCls } = useDesign('login');
    const { createMessage,notification ,createErrorModal} = useMessage();
    const { handleBackLogin } = useLoginState();
    const { setLoginState, getLoginState } = useLoginState();
    const [registerModal, { openModal,closeModal }] = useModal();
    const [registerPhoneEmailModal, { openModal:openPhoneEmailModal,closeModal:closePhoneEmailModal }] = useModal();
    const { getFormRules } = useFormRules();
    const userStore = useUserStore();
    const sliderText = ref('向右滑动完成拼图');
    const TabKey = computed(() => LoginStateEnum[unref(getLoginState)]);
    const formRef = ref();
    const smsCount = ref(null);
    const loading = ref(false);
    const slideCodeShow = ref(false);
    const captchaKey = ref('');
    const verifyUsername = ref('');
    const verifyPhone = userStore.getSafeInfo.phone;;
    const formData = reactive({
      phone:userStore.getSafeInfo.phone,
      captcha:''
    })
    const disableMobile = computed(()=> !!userStore.getSafeInfo.phone);
    console.log(disableMobile.value,userStore.getSafeInfo);
    const { validForm } = useFormValid(formRef);
    async function handleLogin() {
          const data = await validForm();
          if(!data) return;
          loading.value = true;
          try {
            const userInfo = await userStore.login({
              phone:unref(TabKey)==='LOGIN_VERIFY'?undefined:formData.phone,
              captcha: formData.captcha,
              captchaKey: unref(captchaKey),
              type:unref(TabKey) == 'EXPERIENCE'? 'visitor':'phone',
              password:unref(TabKey) == 'EXPERIENCE'? Md5.hashStr('123456') :undefined,
              username:unref(TabKey)==='LOGIN_VERIFY'?unref(verifyUsername):undefined
            });
            if(userInfo?.multi_depart ===2){
                openModal(true,{
                  isUpdate:false,
                  record:userInfo
                })
              return  
            }
            if (userInfo) {
              notification.success({
                message: '登录成功',
                description: `${'登录成功'}: ${userInfo.username}`,
                duration: 3,
              });
            }
          } catch (error) {
            // createMessage.error((error as unknown as Error).message || '网络异常');
            createErrorModal({
              title: '错误提示',
              content: (error as unknown as Error).message || '网络异常',
              getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
            });
            loading.value = false;
          } finally {
            loading.value = false;
          }
    }
    function handleFail(){
      sliderText.value = '验证失败';
      setTimeout(()=>{
        sliderText.value = '向右滑动完成拼图';
      },1000)
    }
    async function handleSildeSuccess(){
      sliderText.value = '验证成功';
      setTimeout(async ()=>{
        if(smsCount.value){
          slideCodeShow.value = false;
          let result;
          // 异地登录带过手机号并且用username 
          if(unref(TabKey)==='LOGIN_VERIFY'){
            verifyUsername.value = userStore.getSafeInfo.username;
            result = await getSmsCodeByUsername({type:'mobile_login_verify',username:userStore.getSafeInfo.username});
          }else{
            result = await getSmsCode({phone:formData.phone,type:'mobile_login'});
          }
  
          if(result){
            captchaKey.value = result;
            console.log(result,'结果');
          }
          smsCount.value.triggerClick();
        }
      },500)
    }
    function sendCodeApi(){
      return new Promise((resolve)=>{
        if(!formData.phone){
          createMessage.warning('请输入手机号');
          return false;
        }
        slideCodeShow.value = true;
        resolve(!unref(slideCodeShow));
        return !unref(slideCodeShow);
      })
    }
    function showPhoneEmail(moduleName){
      openPhoneEmailModal(true,{
        isUpdate:false,
        moduleName
      })
    }
    function handlePhoneEmailSuccess(moduleName,methodName){
      console.log(methodName)
      closePhoneEmailModal();
      if(moduleName === 'RESET_PASSWORD'){
        setLoginState(LoginStateEnum.RESET_PASSWORD);
      }else if(moduleName === 'FIND_ACCOUNT'){
        setLoginState(LoginStateEnum.FIND_ACCOUNT);
      }
    }
    function handleSuccess(){
      closeModal();
      formData.captcha = '';
    }

    return {
      handleSildeSuccess,
      showPhoneEmail,
      handlePhoneEmailSuccess,
      formData,
      handleLogin,
      handleBackLogin,
      getFormRules,
      loading,
      sliderText,
      sendCodeApi,
      slideCodeShow,
      smsCount,
      registerPhoneEmailModal,
      registerModal,
      handleSuccess,
      disableMobile,
      handleFail,
      TabKey,
      formRef,
      verifyPhone
    };
  }
})
</script>
<style lang="less">
#form_item_sms{
  min-width: 280px;
}
.resrun-countdown-input{
  input{
    width: 280px;
  }
}
 
</style>

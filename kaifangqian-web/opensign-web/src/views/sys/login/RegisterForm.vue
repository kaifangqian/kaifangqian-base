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
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      
      <!-- <FormItem name="tenantType" class="enter-x register-type" label="用户类型">
        <a-radio-group v-model:value="formData.tenantType" size="large">
          <a-radio value="2">企业用户</a-radio>
          <a-radio value="1">个人用户</a-radio>
        </a-radio-group>
      </FormItem> -->
      <FormItem name="phone" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.phone"
          placeholder="手机号"
          class="fix-auto-fill"
        >
          <template #prefix>
              <Icon icon="ant-design:tablet-outlined"  color="#bfbfbf"></Icon>
          </template>
        </Input>
      </FormItem>
      <FormItem name="password" class="enter-x">
          <PasswordTip :password="formData.password" ref="passwordRef">
            <template  #passwod>  
              <Input
                size="large"
                :type="formData.inputPassword?'password':'text'"
                visibilityToggle
                v-model:value="formData.password"
                placeholder="密码"
              >
              <template #prefix>
                <Icon icon="ant-design:lock-outlined" color="#bfbfbf"></Icon>
              </template>
                 <template #suffix>
                    <div class="icon-eye" @click.stop="handleChangePassword('inputPassword')">
                      <Icon icon="ant-design:eye-invisible-outlined" v-if="formData.inputPassword"></Icon>
                      <Icon icon="ant-design:eye-outlined" v-else></Icon>
                    </div>
                  </template>
              </Input>
            </template>
          </PasswordTip>
      </FormItem>
      <FormItem name="confirmpassword" class="enter-x">
         <PasswordTip :password="formData.confirmpassword">
              <template  #passwod>  
                <Input
                  size="large"
                  :type="formData.inputConfirmPassword?'password':'text'"
                  visibilityToggle
                  v-model:value="formData.confirmpassword"
                  placeholder="确认密码"
                >
                  <template #prefix>
                    <Icon icon="ant-design:lock-outlined" color="#bfbfbf"></Icon>
                  </template>
                  <template #suffix>
                    <div class="icon-eye" @click.stop="handleChangePassword('inputConfirmPassword')">
                      <Icon icon="ant-design:eye-invisible-outlined" v-if="formData.inputConfirmPassword"></Icon>
                      <Icon icon="ant-design:eye-outlined" v-else></Icon>
                    </div>
                  </template>
                </Input>
              </template>
         </PasswordTip>
      </FormItem>
      <!-- <FormItem name="password" class="enter-x">
        <Input
          type="password"
           size="large"
          v-model:value="formData.password"
          placeholder="密码"
          class="fix-auto-fill"
        />
      </FormItem> -->
      <FormItem name="captcha" class="enter-x">
        <CountdownInput
              size="default"
              class="fix-auto-fill"
              v-model:value="formData.captcha"
              placeholder="验证码"
              ref="smsCount"
              :sendCodeApi="sendCodeApi"
            >
            <template #prefix>
              <Icon icon="ant-design:check-circle-outlined"  color="#bfbfbf"></Icon>
            </template>
          </CountdownInput>
      </FormItem>
      <Row class="policy-area">
        <Col :span="24">
          <FormItem :style="{ 'text-align': 'center', fontSize: '14px' }">
            <p>
              <a-checkbox v-model:checked="registerCheck">我已阅读并同意</a-checkbox>
              <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
              <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
              <!-- <a href="javascript:;" @click="handleTerm('certificate')">《数字证书使用协议》</a> -->
            </p>
          </FormItem>
        </Col>
      </Row>
      <FormItem name="confirmPassword" class="enter-x" >
          <Button
            type="primary"
            class="enter-x"
            size="large"
            block
            @click="handleRegister"
            :loading="loading"
            :disabled="!registerCheck"
          >
            注册
      </Button>
      </FormItem>
     
      <Row>
        <Col :span="24">
          <FormItem :style="{ 'text-align': 'right' }">
            <a-button type="link" size="small" @click="handleLogin">
              已有账号，立即登录
            </a-button>
          </FormItem>
        </Col>
      </Row>
    </Form>
    <PuzzleVerification :w="356" @success="handleSildeSuccess" :sliderText="sliderText" @fail="handleFail" @close="close" v-if="slideCodeShow"/>
  </div>
</template>
<script lang="ts">
  import { ref, unref, defineComponent } from 'vue';
  import { Form, Input, Button, Checkbox ,Row, Col} from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useGo } from '/@/hooks/web/usePage';
  import { LoginStateEnum, useFormRules, useFormValid, useLoginState } from './useLogin';
  import {  getSmsCode } from '/@/api/sys/user';
  import { registerTenant } from '/@/api/tenant';
  import PuzzleVerification from '/@/components/verification/index.vue';
  import { Md5 } from "ts-md5";
  import PasswordTip from '/@/components/PasswordTip/Index.vue';
  import { Icon } from '/@/components/Icon';
  import { isMobile } from '/@/utils/validate';
  import {useRouter} from "vue-router";
  
  export default defineComponent({
    name:'RegisterForm',
    components:{
      PuzzleVerification,
      Form,
      FormItem: Form.Item,
      Input,
      Button,
      Checkbox,
      CountdownInput,
      Row,
      Col,
      Icon,
      PasswordTip

    },
    setup(){
      const go = useGo();

      const { createMessage:msg} = useMessage();
      const sliderText = ref('向右滑动完成拼图');
      const passwordRef = ref<HTMLDivElement | null>(null)
      const { setLoginState } = useLoginState();
      setLoginState(LoginStateEnum.REGISTER);

      const formRef = ref();
      const loading = ref(false);
      const registerCheck = ref(false);
      const slideCodeShow = ref(false);  
      const smsCount = ref(null);
      let router = useRouter();
      const type = ref(router.currentRoute.value.query?.type);
      const formData = ref({
        realName: '',
        password: '',
        phone: '',
        captcha: '',
        captchaKey: '',
        policy: false,
        tenantType:'2',
        confirmpassword:'',
        inputPassword:true,
        inputConfirmPassword:true
      });

      const { getFormRules } = useFormRules(formData.value);
      const { validForm } = useFormValid(formRef);

      async function handleRegister() {
        let passwordResult = passwordRef.value.validateAllRules();
        if(!passwordResult){
          msg.success('密码不符合规则')
          return
        }
        const data = await validForm();
        if (!data) return;
        if(!unref(formData).captchaKey){
          msg.success('请先获取验证码')
          return
        }
        let result = await registerTenant({...data,captchaKey:unref(formData).captchaKey,password:Md5.hashStr(data.password||'123456')});
        if(result){
          msg.success('注册成功,即将跳转登录页')
          setTimeout(()=>{
            if(type.value){
              window.open("", window.opener.name);
              window.close();
            }else{
              go('/transition');
            }
          },2000)
        }
      }
      function handleLogin(){
        go('/login')
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
          let result = await getSmsCode({phone:formData.value.phone,type:'register'});
  
          if(result){
            formData.value.captchaKey = result;
            console.log(result,'结果');
          }
          smsCount.value?.triggerClick();
        }
      },500)
    }

      function sendCodeApi(){
        console.log(formData,'--------表单数据---')
      return new Promise(async (resolve)=>{
        const data = await validForm(['phone','password']);
        if(!data) return;
        let passwordResult = passwordRef.value.validateAllRules();
        if(!passwordResult){
          msg.success('密码不符合规则')
          return
        }
        slideCodeShow.value = true;
        resolve(!unref(slideCodeShow));
        return !unref(slideCodeShow);
      })
    }
    function close(){
      slideCodeShow.value = false;
    }

      function handleTerm(type){
        window.open('/#/terms/service/'+ type)
        
      }
      function handlePolicy(){
        window.open('/#/privacy/policy')
      }
      function handleChangePassword(passwordKey){
        formData.value[passwordKey] = !formData.value[passwordKey];
      }

      return {
        formRef,
        handleChangePassword,
        getFormRules,
        handleFail,
        handleSildeSuccess,
        handleLogin,
        sliderText,
        formData,
        handleRegister,
        slideCodeShow,
        loading,
        smsCount,
        sendCodeApi:sendCodeApi as any,
        handleTerm,
        handlePolicy,
        registerCheck,
        close,
        passwordRef
      }
    }

  })


</script>

<style lang="less">
.register-type{
      :deep(.ant-form-item) .ant-form-item-label > label{
        font-size: 14px!important;
      }
    }
    
    .ant-radio-group {
      .ant-radio-wrapper{
        margin-right: 15px;
        font-size: 14px;
        // display: flex;
        // align-items: center;
      }
    }
    .ant-form-item .ant-form-item-label > label{
      font-size: 14px!important;
    }
    .register-type{
      :deep(.ant-form-item) .ant-form-item-label > label{
        font-size: 14px;
      }
      .ant-radio{
        margin-right:7px;
        padding-top:2px;
        // float: left;
      }
    }
    .policy-area{
      height:30px;
    } 
 
</style>

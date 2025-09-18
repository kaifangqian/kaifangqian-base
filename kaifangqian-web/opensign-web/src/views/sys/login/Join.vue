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
 
 <div :class="prefixCls" class="relative w-full h-full ">
   
    <div :class="prefixCls+ '-header'">
        <img :src="logoBase64" alt="" style="width:140px" v-if="logoBase64"> 
    </div>
    <div class="container relative  py-2 mx-auto sm:px-10">
      <div class="flex w-full h-full py-5  xl:py-5 xl:my-0 xl:w-12/12">
        <div
          :class="[`${prefixCls}-form`,isMobileAgent()?'mobile-form':'']"
          class="relative w-full px-5 py-8 mx-auto my-auto rounded-md xl:bg-transparent sm:px-8 xl:p-4 xl:shadow-none sm:w-3/4 lg:w-2/4 xl:w-auto enter-x"
        >
          <div class="invite-header">
            <!-- <img  class="app-icon" src="../../../assets/images/favicon.png" alt=""> -->
            <p class="inviter"> 「{{ organizeName}}」</p>
            <p class="inviter-desc">邀请您使用 {{ webTitle }}</p>
            <!-- <p class="organize">{{ organizeName }}</p> -->
          </div>
          <div class="resrun-invite-form">
            
            <Form  class="p-3 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
                  <FormItem name="realName">
                    <Input
                      size="large"
                      v-model:value="formData.realName"
                      placeholder="请填写您的真实姓名"
                      class="fix-auto-fill"
                    >
                    <template #prefix>
                      <Icon icon="ant-design:user-outlined"  color="#bfbfbf"></Icon>
                    </template>
                    </Input>
                  </FormItem>
                  <FormItem name="account">
                    <Input :disabled="joinType == '2'"
                      size="large"
                      v-model:value="formData.account"
                      placeholder="请填写您的手机号"
                      class="fix-auto-fill"
                      v-if="webConfig.systenAccountType == 'phone'"
                    >
                    <template #prefix>
                      <Icon icon="ant-design:tablet-outlined"  color="#bfbfbf"></Icon>
                    </template>
                  </Input>
                  <Input :disabled="joinType == '2'"
                      size="large"
                      v-model:value="formData.account"
                      placeholder="请填写您的邮箱"
                      class="fix-auto-fill"
                      v-else-if="webConfig.systenAccountType == 'email'"
                    >
                    <template #prefix>
                        <Icon icon="ant-design:mail-outlined"  color="#bfbfbf"></Icon>
                      </template>
                  </Input>
                  <Input :disabled="joinType == '2'"
                      size="large"
                      v-model:value="formData.account"
                      placeholder="请填写您的手机号或者邮箱"
                      class="fix-auto-fill"
                      v-else
                    >
                    <template #prefix>
                        <Icon icon="ant-design:tablet-outlined"  color="#bfbfbf"></Icon>
                      </template>
                  </Input>
                  </FormItem>
                  <FormItem name="captcha" class="enter-x">
                    <CountdownInput
                      size="default"
                      class="fix-auto-fill"
                      v-model:value="formData.captcha"
                      placeholder="请输入验证码"
                      ref="smsCount"
                      :sendCodeApi="(sendCodeApi as any)">
                    <template #prefix>
                      <Icon icon="ant-design:check-circle-outlined"  color="#bfbfbf"></Icon>
                    </template>
                    </CountdownInput>
                  </FormItem>
                  <div class="jon-footer" >
                    点击立即加入代表您已阅读并同意
                    <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
                    和<a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
                  </div>
                <FormItem >
                  <a-button type="primary"  block @click="handleSubmit" style="height: 40px;">
                    立即加入
                  </a-button>
                </FormItem>
              </Form>
             
          </div>

          
        </div>
      </div>
    </div>
      <Verify
        @success="handleSildeSuccess"
        @error="captchaError"
        :imgSize="{ width: '300px', height: '155px' }"
        captchaType="blockPuzzle"
        ref="verifyRef"
      ></Verify>
  
  </div>
</template>
<script lang='ts'>
import { defineComponent,ref,unref,onMounted } from 'vue';
import { CountdownInput } from '/@/components/CountDown';
import { useMessage } from '/@/hooks/web/useMessage'; 
import { Form, Input, Row, Col } from 'ant-design-vue';
import { PageEnum } from '/@/enums/pageEnum';
import { useFormRules,useFormValid } from '/@/views/sys/login/useLogin';
import { useDesign } from '/@/hooks/web/useDesign';
import { isMobileAgent, getHashQueryString } from '/@/utils';
import { getSmsCode, getEmailCode,userJion ,getTenantNameByKey} from '/@/api/sys/user';
import { Verify } from '/@/components/verifition';
import { Md5 } from "ts-md5";
import { useGo } from '/@/hooks/web/usePage';
import { getOtherLogo, getWebTitle } from '/@/api/sys/user';
import defaultLogo from '/@/assets/images/logo-sign.png';
import { useUserStore } from '/@/store/modules/user';
import { Icon } from '/@/components/Icon';


export default defineComponent({
name: 'InviteUser',
components:{
  Form,
  FormItem:Form.Item,
  Input,Icon,
  Row, Col,
  CountdownInput,
  Verify
  
},
setup(){
  const verifyRef: any = ref({});
  const captchaResult = ref({
    falg: false,
    data: {},
  });
  const getTitle = ref('邀请成员加入');
  const slideCodeShow = ref(false);
  const formRef = ref();
  const sliderText = ref('向右滑动完成拼图');
  const smsCount = ref(null);
  const defaultWidth = ref(360);
  const go = useGo();
  if(isMobileAgent()){
    defaultWidth.value = window.innerWidth-90;
  }
  const userStore = useUserStore();
  const webConfig:any = userStore.getWebConfig;
  const formData = ref({
    realName:'',
    phone:'',
    captcha:'',
    captchaKey:'',
    account:'',
    email:""
  })
  const joinType = getHashQueryString('type');
  const { getFormRules } = useFormRules(formData.value,webConfig.systenAccountType);
  const flag = ref(false);
  const { prefixCls } = useDesign('join');
  const { createMessage: msg } = useMessage();
  const { validForm } = useFormValid(formRef);
  // const organizeName = getHashQueryString('name')?decodeURI(getHashQueryString('name')):'';
  const organizeName = ref('');
  console.log(organizeName)
  const joinKey = getHashQueryString('key');
  console.log(organizeName,joinKey,'url结果');


  onMounted(()=>{
    //禁止移动端向右滑动验证码滑块返回上一页
      document.body.addEventListener('touchmove', pageTouchmove, {passive: false})
        getTenantName()
  })
  function handleTerm(type){
    window.open('/#/terms/service/'+ type)
  }
  function pageTouchmove(e){
        e.preventDefault();
        e.stopPropagation();
  }
  window.addEventListener('resize',function(){
    console.log("user join window resize");
  	// window.location.reload();
  })


 async function getTenantName(){
  let result = await getTenantNameByKey({redisKey:decodeURI(getHashQueryString('key') || '')})
  if(result){
    organizeName.value = result.tenantName;
    formData.value.account = result.phone||result.email
  }
 }

  async function handleSubmit(){
    const data = await validForm();
        if (!data) return;

    let result  = await userJion({...data,captchaKey:formData.value.captchaKey,
      redisKey:joinKey,
      password:Md5.hashStr('123456'),
      phone:formData.value.phone,
      email:formData.value.email
    });
    if(result){
      msg.success('申请加入企业成功');
      if(isMobileAgent()){
        window.location.href=window.appInfo.mobile_app_info.url;
      }else{
        go(PageEnum.BASE_LOGIN);
      }
    }
    // if(result.result == 1){
    //   msg.success('加入企业成功');
    //   if(isMobileAgent()){
    //     window.location.href=window.appInfo.mobile_app_info.url;
    //   }else{
    //     go(PageEnum.BASE_LOGIN);
    //   }
    // }else{
    //   msg.success('已申请加入，管理员开启了申请加入审核流程，请耐心等待或联系管理员及时审核',10);
    // }
  }
   async function handleSildeSuccess(params){
     document.body.addEventListener('touchmove', pageTouchmove, {passive: false})
      sliderText.value = '验证成功';
      setTimeout(async ()=>{
        if(smsCount.value){
          slideCodeShow.value = false;
          let result;
          if(formData.value.phone){
            result = await getSmsCode({phone:formData.value.phone,type:'login1',captchaVerification: params.captchaVerification,});
          }else if(formData.value.email){
             result = await getEmailCode({email:formData.value.email,type:'login1',captchaVerification: params.captchaVerification,});
          }
  
          if(result){
            formData.value.captchaKey = result;
            console.log(result,'结果');
          }
          smsCount.value?.triggerClick();
        }
      },500)
    }
    function handleFail(){
      sliderText.value = '验证失败';
      setTimeout(()=>{
        sliderText.value = '向右滑动完成拼图';
      },1000)
    }

      function sendCodeApi(){
        console.log(formData,'--------表单数据---')
        
      return new Promise(async (resolve)=>{
        try{
          const data = await validForm("account");
        }catch(e){
          return false;
        }
        
        if (!slideCodeShow.value) {
          document.body.removeEventListener('touchmove', pageTouchmove)
          verifyRef.value.show();
          return false;
        }
        // if(!formData.value.phone){
        //   msg.warning('请输入手机号');
        //   return false;
        // }
        slideCodeShow.value = true;
        resolve(!unref(slideCodeShow));
        return !unref(slideCodeShow);
      })
    }
    const logoBase64 = ref('');
    async function getLogo(){
      getOtherLogo({}).then(res=>{
          if(res){
              logoBase64.value = res.image;
          }
      })
    }

    const webTitle = ref('电子签章系统');
    async function getWebTitle(){
      getWebTitle({}).then(res=>{
          if(res){
              webTitle.value = res.title;
          }
      })
    }
    getLogo();
    getWebTitle();

    const captchaError = (params) => {
      // console.log('captchaError', params);
    };
  return {
    prefixCls,
    slideCodeShow,
    handleSildeSuccess,
    sendCodeApi,
    smsCount,
    handleSubmit,
    getTitle,
    sliderText,
    formRef,
    formData,
    isMobileAgent,
    getFormRules,
    handleFail,
    flag,webConfig,
    organizeName,joinType,
    defaultWidth,logoBase64,
    verifyRef,
    captchaError,
    handleTerm,
    webTitle,
  }
},
})
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-join';
  @logo-prefix-cls: ~'@{namespace}-app-logo';
  @countdown-prefix-cls: ~'@{namespace}-countdown-input';
  @dark-bg: #293146;
  @primary: #127fd2;
  .@{prefix-cls} {
    min-height: 100vh;
    overflow: hidden;
    background: #fff;
    background-size: 100% 100%;
    @media (max-width: @screen-xl) {
      background-color: #fff;
      background-image: none;

      .@{prefix-cls}-form {
        background-color: #fff;
      }
    }
    &-header{
        width: 100%;
        height: 120px;
        padding-top: 40px;
        display: flex;
        justify-content: center;
        align-items: center;
        .header-left{
          display: flex;
          align-items: center;
          height: 100%;
        }
        .header-right{
          a{
            font-size: 14px;
            font-weight: bold;
            margin:0 5px;
            color: #333;
          }
        }
        .header-title{
          color: #127fd2;
          font-size: 16px;
          font-weight: 600;
          margin-left: 5px;
        }
    }
    &-form{
      background:#fff;
    }
    .container{
      margin-top:20px;
    }
  }
  .mobile-form{
    width:90%;
  }
  .@{logo-prefix-cls} {
      position: absolute;
      top: 12px;
      height: 30px;

      &__title {
        font-size: 16px;
        color: #fff;
      }

      img {
        width: 32px;
      }
    }
.resrun-invite-form{
  font-size: 14px;
  margin-top:20px;

  .ant-btn-primary {
      // background: @primary;
      background: linear-gradient(90deg, #1890ff, #40a9ff);
      border-radius: 8px;
      font-size: 16px;
      font-weight: 400;
      height: 44px;
      border: none;
      box-shadow: 0 2px 8px fade(@primary, 10%);
      transition: background 0.2s;
      // &:hover, &:focus {
      //   background: darken(@primary, 8%);
      // }
    }

}
  .invite-header {
    text-align: center;
    margin-top: -30px;
    margin-bottom: 16px;
    .inviter {
      font-size: 24px;
      color: #222;
      font-weight: 700;
      margin: 0;
      letter-spacing: 0.5px;
    }
    .inviter-desc {
      font-size: 20px;
      color: #666;
      font-weight: 400;
      margin: 0;
      letter-spacing: 0.5px;
      margin-top: 5px;
    }
    img {
      width: 70px;
      height: 70px;
      border-radius: 8px;
      margin-bottom: 10px;
      display: inline-block;
      background-color: #fff;
      border: 1.5px solid #e3e3e3;
      box-shadow: 0 2px 8px rgba(18,127,210,0.06);
    }
  }
.join-container{
  width:400px;
}
  .jon-footer {
    margin-top: 10px;
    margin-bottom: 10px;
    // text-align: center;
    font-size: 13px;
    color: #888;
    a {
      color: #127fd2;
      // text-decoration: underline;
      margin: 0 2px;
      transition: color 0.2s;
      &:hover {
        color: darken(#127fd2, 10%);
      }
    }
  }
.code-input input{
  height:40px;
}
</style>

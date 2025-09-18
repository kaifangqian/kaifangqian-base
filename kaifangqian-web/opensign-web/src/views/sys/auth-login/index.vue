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
  <div :class="prefixCls" class="relative w-full h-full flex items-center justify-center min-h-screen p-8" style="background: linear-gradient(135deg, #F5F7FA 0%, #FFFFFF 100%);">
    <div class="absolute top-8 left-8 z-50"> 
      <div class="flex items-center space-x-3">
        <img :src="logoBase64" alt="" class="w-auto h-16" />
      </div>
    </div>
    <a-spin :spinning="loading" tip="授权中" :wrapperClassName="urlParams.directly? 'auth-loading-mask':''">
      <div :class="prefixCls + '-form'" class="w-[460px] p-8 bg-white rounded-md shadow-md rounded-2xl" style="background: rgba(255, 255, 255, 0.95);backdrop-filter: blur(10px);box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);">
        <h2 class="text-center text-2xl font-bold mb-4" v-if="authStep===1">登录{{ websiteTitle }}</h2>
        <p class="text-center text-gray-500 mb-6" v-if="authStep===1">请登录您的账户继续使用</p>

        <!-- <Tabs size="large" @change="changeTab"  v-if="authStep===1" v-model:activeKey="tabKey">
          <TabPane tab="验证码登录" key="mobile-login" class="w-1/2 py-2 text-center rounded-lg">
              <AuthMobileForm  @success="handleLoginSuccess" :partyName="urlParams.partyName"  :phone="urlParams.phone" :email="urlParams.email"/>
          </TabPane>
          <TabPane tab="账号登录" key="account-login" class="w-1/2 py-2 text-center rounded-lg">
              <AuthLoginForm  @success="handleLoginSuccess" :partyName="urlParams.partyName"/>
          </TabPane>
        </Tabs> -->
        <div class="flex bg-gray-100 rounded-full p-1 mb-6" v-if="authStep===1">
          <button
            @click="changeTab('mobile-login')"
            :class="[
              'w-1/2 py-2 text-center rounded-lg',
              tabKey === 'mobile-login' ? 'text-white' : 'bg-gray-100 text-gray-600'
            ]"
            :style="tabKey === 'mobile-login' ? 'background: #1890FF' : ''"
          >
            验证码登录
          </button>
          <button
            @click="changeTab('account-login')"
            :class="[
              'w-1/2 py-2 text-center rounded-lg',
              tabKey === 'account-login' ? 'text-white' : 'bg-gray-100 text-gray-600'
            ]"
            :style="tabKey === 'account-login' ? 'background: #1890FF' : ''"
          >
            账号登录
          </button>
        </div>

        <div v-if="authStep===1">
          <AuthMobileForm 
            v-if="tabKey === 'mobile-login'" 
            @success="handleLoginSuccess" 
            :partyName="urlParams.partyName"  
            :phone="urlParams.phone" 
            :email="urlParams.email"
          />
          <AuthLoginForm 
            v-else 
            @success="handleLoginSuccess" 
            :partyName="urlParams.partyName"
          />
        </div>
        <div class="auth-token" v-if="authStep===2">
          <div class="flex justify-center mb-6">
            <Icon 
              icon="ant-design:safety-outlined" 
              class="text-5xl text-blue-500"
              style="color: #1890FF; font-size: 3rem;"
            />
          </div>
          <!-- <div class="auth-app-info" style="text-align: center;">
            <SvgIcon name="auth" size="70" v-if="!appIcon"/>
            <img :src="appIcon" alt="" v-if="appIcon">
          </div> -->
          <p class="text-center text-gray-500 mb-6">账号验证通过，​立即点击「授权登录」进入系统</p>
          <a-button type="primary" size="large" block @click="handleAuth" :loading="loading" style="background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%); height:48px;border-color: #00000000;margin-top:20px;">授权登录</a-button>
          <a-button type="text" size="large" block @click="handleBack" :loading="loading" id="login-out" style="color: #666; margin-top: 10px;">退出登录</a-button>
        </div>
      </div>
      <div class="copyright-footer fixed bottom-4 left-0 w-full flex justify-center items-center">
        <p v-html="webCopyRight"></p>
      </div>
      <AuthLoginModal @register="registerModal" @success="handleSuccess" />
      <AuthSelectTenant @register="registerTenantModal" @success="handleTenantSuccess" />
    </a-spin>
  </div>
</template>

<script lang="ts">
import {ref, defineComponent,onMounted, watch, computed} from "vue";
import { Tabs } from 'ant-design-vue';
import { useModal } from '/@/components/Modal';
import AuthLoginModal from './modal/AuthLoginModal.vue';
import AuthSelectTenant from './modal/AuthSelectTenant.vue';
import { useDesign } from '/@/hooks/web/useDesign';
import { useMessage } from '/@/hooks/web/useMessage';
import { useUserStore } from '/@/store/modules/user';
import { SvgIcon } from '/@/components/Icon';
import AuthLoginForm from './components/AuthLoginForm.vue';
import AuthMobileForm from './components/AuthMobileForm.vue';
import { getLoginToken } from '/@/utils/auth';
import { Loading } from '/@/components/Loading';
import Icon from '/@/components/Icon';


import { getAppTokenByAuthToken, getMyTenantDepartsByLoginToken } from '/@/api/sys/user';
import { getAppInfoById } from '/@/api/backstage';
import { getHashQueryString, removeQueryParam } from '/@/utils';
import { router } from '/@/router';
import defaultLogo from '/@/assets/images/logo-sign.png';


export default defineComponent({
  name:"AuthLogin",
  components:{
    useModal,
    SvgIcon,
    AuthLoginModal,
    AuthLoginForm,
    AuthMobileForm,
    Tabs,
    TabPane:Tabs.TabPane,
    AuthSelectTenant,
    Loading,
    Icon

    
  },
  setup() {
    window.name="authLogin";
    const { prefixCls } = useDesign('login');
    const tabKey = ref('account-login');
    const authStep = ref(1);
    const loading = ref(false);

    const { createMessage: msg } = useMessage();
    const urlParams = ref({
      appId:getHashQueryString('appId')|| '',
      appCode:getHashQueryString('appCode')|| '',
      departId:getHashQueryString('departId')|| '',
      redirect:getHashQueryString('redirect') || '',
      directly:getHashQueryString('directly') || false,
      phone:getHashQueryString('phone') || '',
      email:getHashQueryString('email') || '',
      partyName:getHashQueryString('partyName')? decodeURIComponent(getHashQueryString('partyName')) :'',
      signRuId:getHashQueryString('signRuId') || '',
      taskId:getHashQueryString('taskId') || '',
      taskType:getHashQueryString('taskType') || '',
      callbackPage:getHashQueryString('callbackPage') || '',
    })
    console.log(urlParams.value.partyName,'路由参数')
    //判断直接跳转还是授权之后跳转
    if(urlParams.value?.directly){
      loading.value = true;
    }
    if(urlParams.value?.phone || urlParams.value?.email ){
      tabKey.value = 'mobile-login';
    }
    const appName = ref('工作台');
    const appIcon = ref('');
    const logoBase64 = ref(defaultLogo);
    const webCopyRight = ref('');
    const websiteTitle = ref('');

    const userStore = useUserStore();



    const appoken = userStore.getToken;
    const tokenInvalid = computed(()=>userStore.getTokenInvalidType);
    console.log(tokenInvalid,'失效类型')

    
    const [registerModal, { openModal,closeModal }] = useModal();
    const [registerTenantModal, { openModal:openTenantModal,closeModal:closeTenantModal }] = useModal();


    onMounted(()=>{
      init();
    })
    watch(
      ()=> userStore.getTokenInvalidType,
      (val)=>{
        if(val==401){
          init();
        }
      }
    )
    function init(){
      const web: any = userStore.getWebConfig;
      console.log("web",web);
      logoBase64.value = web.websiteOtherLogo || logoBase64.value;
      webCopyRight.value = web.websiteCopyright || webCopyRight.value;
      websiteTitle.value = web.websiteTitle || "开放签";
      const loginToken = getLoginToken();

      // webCopyRight.value = '<p class="text-gray-500 text-sm">© 北京资源律动科技有限公司 版权所有</p><p class="text-gray-500 text-sm mt-2 md:mt-0"><a href="https://beian.miit.gov.cn" target="_blank" rel="noopener noreferrer">京ICP备19060018号-4</a> · <a href="https://beian.mps.gov.cn/#/query/webSearch" target="_blank" rel="noopener noreferrer">京公网安备11011402053714</a></p>';
    


       // 1. 登录token无效 => 登录 => 授权（登录token获取授权token）=> 可以获取 直接跳转子应用；无法获取弹框选择租户获取授权token 跳转子应用
      // 2.  登录token有效 => 授权（登录token获取授权token）=> 可以获取 直接跳转子应用；无法获取弹框选择租户获取授权token 跳转子应用
      console.log(loginToken,'登录token')
      if(!loginToken){
        authStep.value  = 1; 
        loading.value = false;
      }else{
        authStep.value  = 2;
        getAppInfo() 
      }
    }
    async function getAppInfo(){
      // if(!urlParams.value.appId){
      //   msg.warning('访问链接无应用id,无法获取应用信息')
      // }else{
        if(urlParams.value.appId){
          let result = await getAppInfoById({id:urlParams.value.appId})
            if(result){
              appIcon.value = result.appIcon;
              appName.value = result.appName;
              judgeUrlParams();
            }else{
              msg.warning(result)
            }
        }else{
          judgeUrlParams();
        }
      // }
    }

    //判断url中有无租户信息(部门id)
    function judgeUrlParams(){
      //有租户信息自动获取授权token
      if(urlParams.value.departId){
        getAppToken(urlParams.value.departId)
      }else{
        authStep.value  = 2;
      }
    }
    //获取授权token
    async function getAppToken(departId){
      loading.value = true;
      let result = await getAppTokenByAuthToken({departId:departId});
      if(result){
        //授权token:短链是主应用需要更新token,授权其他应用不需要更新主应用tokn
        if(!urlParams.value.appCode){
           userStore.setToken(result);
        }else{
          userStore.getUserInfoAction();
          userStore.getTnantInfo();
        }
        // msg.success('授权成功，即将跳转应用'); 
        setTimeout(()=>{
          let paramsString = window.location.hash.includes('?')?removeQueryParam(window.location.hash.split('?')[1],'redirect'):'';
          window.open( decodeURIComponent(decodeURIComponent(urlParams.value.redirect as string))+'?' + paramsString + '&token=' + result,'_self');
        },1000)
      }else{
        loading.value = false;
        msg.warning(result)
      }
    }
    function changeTab(val){
      tabKey.value =  val;
    }

    function handleLoginSuccess(info){
      authStep.value  = 2;
      // msg.success('登录成功，开始授权');
      setTimeout(()=>{
        getAppInfo();
      },1000)
    }
    //选择租户
    function handleTenantSuccess(departId){
      urlParams.value.departId = departId;
      getAppToken(departId);

    }
    //无租户信息点击授权
    // async function handleAuth(){
    //   if(urlParams.value.partyName){
    //     let result = await getMyTenantDepartsByLoginToken({appCode:urlParams.value.appCode});
    //     let accountList:any = result;
    //     // result.map((v:any)=>{
    //     //   accountList = [...accountList,...v.departs]
    //     // })
    //     let matchDepart:any = accountList.filter((u:any)=>u.tenantName == urlParams.value.partyName)
    //     if(matchDepart&&matchDepart.length && typeof urlParams.value.redirect == 'string'){
    //       handleTenantSuccess(matchDepart[0].departId);
    //       }else{
    //         openTenantModal(true,{
    //           isUpdate:false
    //         })
    //       }
        
    //   }else{
    //     openTenantModal(true,{
    //         isUpdate:false
    //       })
    //   }
    // }

    async function handleAuth(){
      if(urlParams.value.partyName){
        let result = await getMyTenantDepartsByLoginToken({appCode:urlParams.value.appCode});
        let accountList:any = result;
        // result.map((v:any)=>{
        //   accountList = [...accountList,...v.departs]
        // })
        let matchDepart:any = accountList.filter((u:any)=>u.tenantName == urlParams.value.partyName)
        if(matchDepart&&matchDepart.length && typeof urlParams.value.redirect == 'string'){
          handleTenantSuccess(matchDepart[0].departId);
          }else{
            openTenantModal(true,{
              isUpdate:false
            })
          }
      }else{
        let result = await getMyTenantDepartsByLoginToken({appCode:urlParams.value.appCode});
        let accountList:any = result;
        console.log(accountList,'accountList')
        if(accountList.length === 1){
          handleTenantSuccess(accountList[0].departId);
        }else{
          openTenantModal(true,{
            isUpdate:false
          })
        }
      }
    }


    async function handleSuccess(){
        closeModal()
        window.open('http://localhost:8902/#/tenant/application','_self');
        return 
        // let result = await getLoginToken({token:token})
        // if(result){
        //    window.open('','_self')

        // }
    }

    function handleBack(){
        userStore.clearToken();
        closeModal();
      }

    return {
      appName,
      appIcon,
      handleLoginSuccess,
      handleTenantSuccess,
      loading,
      tabKey,
      registerModal,
      registerTenantModal,
      prefixCls,
      handleSuccess,
      changeTab,
      authStep,
      handleAuth,
      urlParams,
      logoBase64,
      webCopyRight,
      websiteTitle,
      handleBack,
           
    }
  }
})
</script>

<style lang="less" scoped>
// @import '/@/views/sys/login/login.less';
// </style>
// <style lang="less" scoped>

// .resrun-login-form{
//   padding-bottom: 30px;
// }
// .login-tab-header{
//     .ant-tabs-nav{
//       margin: 0 auto 25px;
//     }
    
//   }
// .auth-token{
//   text-align: center;
//   .resrun-svg-icon{
//     margin-top:30px;
//   }
//   .app-name{
//     margin:20px 0;
//     font-size: 16px;
//   }
// }
// .auth-app-info{
//   img{
//     border-radius: 10px;
//   }
// }
// .auth-loading-mask{
//   :deep(.ant-spin-blur){
//     opacity: 0;
//   }
//   :deep(.ant-spin-show-text){
//     max-height: 100%;
//   }
// }

.login {
    &-form {
      .ant-input {
        height: 40px;
        border: 1px solid #ddd;
        border-radius: 4px;
        padding: 0 12px;
        font-size: 14px;

        &:focus {
          border-color: #1890ff;
          box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.2);
        }
      }

      .ant-btn {
        width: 100%;
        height: 40px;
        background-color: #1890ff;
        border-color: #1890ff;
        color: #fff;
        border-radius: 4px;
        font-size: 14px;

        &:hover,
        &:focus {
          background-color: #0056b3;
          border-color: #0056b3;
        }
      }

      .ant-checkbox-wrapper {
        margin-right: 10px;
        font-size: 14px;
        color: #666;
      }

      .ant-form-item-label > label {
        font-size: 14px;
        color: #666;
      }

      .ant-form-item-control {
        line-height: 40pxchangeTab
      }

      .ant-form-explain {
        margin-top: 5px;
        font-size: 12px;
        color: #f5222d;
      }

      .ant-form-text {
        font-size: 14px;
        color: #666;
      }

      .ant-form-item-extra {
        font-size: 14px;
        color: #1890ff;
      }
    }
  }
  .copyright-footer{
    // font-size: 10px;
    color: rgb(156 163 175 / var(--tw-text-opacity, 1));
    margin: 0;
    text-align: center;
    font-size: 0.75rem;
    line-height: 1rem;
  }

</style>

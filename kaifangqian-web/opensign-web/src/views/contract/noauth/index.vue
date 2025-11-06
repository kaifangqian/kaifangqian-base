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
  <cheader v-if="!pageLoading"/>
  <div class="sign-noauth-container">
    <Loading :loading="pageLoading" text="加载中..." />
    <div class="sign-noauth-body" v-if="(tipInfo.checkStatus != 1 || taskType == 'detail' || taskType == 'copy') && !pageLoading">
      <div class="no-auth-header">
        <SvgIcon name="sign-no-auth" size="80"/>
        <p style="font-size:18px;">无权访问</p>
      </div>
      <div class="no-auth-tip">
        <p v-if="tipInfo.checkStatus==2 || tipInfo.checkStatus==3">该任务属于账号【{{  tipInfo.targetUserPhone || tipInfo.targetUserEmail }}】,您当前登录的账号是【{{  tipInfo.currentUsername }}】您无法访问该任务，请切换账号或联系文件发起方</p>
        <p v-if="tipInfo.checkStatus==4">请切换至当前账号的{{partyName?'【'+ partyName+'】':'个人' }}身份下查看该任务</p>
        
        <p v-if="tipInfo.checkStatus==5">该任务属于个人账号任务，您当前还未开通个人账号，请先开通</p>
        
        <p v-if="tipInfo.checkStatus==6">该任务属于【{{tipInfo.targetTenantName}}】，您尚未加入到该企业中，请联系企业管理员邀请您加入，加入企业后再完成该任务</p> 
        
        <p v-if="tipInfo.checkStatus==7">请切换至当前账号的 【{{tipInfo.targetTenantName}}】身份查看该任务</p>
        <p v-if="tipInfo.checkStatus==8">该任务属于【{{tipInfo.targetTenantName}}】，该企业尚未开通账号，请先创建企业</p>
        
        <p v-if="taskType == 'detail' || taskType == 'copy'">您当前登录的账号/身份无权查阅该文件，请切换账号/身份或联系文件发起方</p>
        
      </div>
      <div class="no-auth-action">
        <a-button type="primary" v-if="(tipInfo.checkStatus ==2 || tipInfo.checkStatus ==3 ) " @click="handleBackHome">返回首页</a-button>
        <!-- <a-button type="primary" v-if="(tipInfo.checkStatus == 4  && partyName) || tipInfo.checkStatus == 7" @click="handleChangeIdentity">切换身份</a-button> -->
        <a-button type="primary" v-if="tipInfo.checkStatus == 4 || tipInfo.checkStatus == 7" @click="handleChangeIdentity">切换身份</a-button>
        <a-button type="primary" v-if="tipInfo.checkStatus==5" @click="openPersonalAccount">开通个人账号</a-button>
        <!-- <a-button type="primary" v-if="tipInfo.checkStatus==8 " @click="handleEntAuth">企业认证</a-button> -->
        <a-button type="primary" v-if="tipInfo.checkStatus==8 " @click="createCompany">创建企业</a-button>
        <a-button type="primary" v-if="tipInfo.checkStatus == 6" @click="handleJoinEnterprise">申请加入</a-button>
        <a-button type="default" @click="handleLoginOut">退出登录</a-button>
      </div>
    </div>
    <CompanyJoinModal @register="companyJoinRegister"/>
  </div>
</template>

<script lang="ts">
import {ref, defineComponent, unref, onMounted } from "vue";
import { SvgIcon } from '/@/components/Icon';
import { useUserStore } from '/@/store/modules/user';
import { openPersonalTenant } from '/@/api/tenant';
import { getContractNoAuthType,getViewCheck } from '/@/api/contract';
import { useMessage } from '/@/hooks/web/useMessage';
import { useRouter } from 'vue-router';
import { useGo } from '/@/hooks/web/usePage';
import { createLocalStorage } from '/@/utils/cache';
import cheader from "/@/layouts/default/header/index2.vue";
import { getHashQueryString } from '/@/utils';
import { getMyTenantDeparts, updateTenantDepart } from '/@/api/sys/user'; 
import { companyAddApi } from '/@/api/auth/userAuth';
import CompanyJoinModal from '/@/views/sys/user/modal/CompanyJoinModal.vue';
import { useModal } from '/@/components/Modal';

export default defineComponent({
  name:"noAuth",
  components:{
    SvgIcon,cheader,CompanyJoinModal
  },
  setup() {
    const userStore = useUserStore();
    const userInfo =  userStore.getUserInfo;
    const callbackPage = getHashQueryString('callbackPage');
    const partyName = getHashQueryString('partyName')?decodeURIComponent(getHashQueryString('partyName')  as string):'';
    const tipInfo = ref({
      originAccount:'',
      currentAccount:'',
      tenantName:'',
      checkStatus:1,
      targetTenantName:"",
      currentUsername:"",
      targetUserPhone:"",
      targetUserEmail:"",
    })
    const go = useGo();
    const ls = createLocalStorage();

    const router = useRouter();
    const { currentRoute } = router;
    const route = unref(currentRoute);
    const signRuId = route.query.signRuId;
    const taskId = route.query.taskId;
    const taskType = ref(route.query.taskType);
    ls.set('Sign-Task-Id',taskId);
    const pageLoading = ref(true);
    const [companyJoinRegister, { openModal: openCompanyJoin, closeModal: closeCompanyJoin }] =
    useModal();
  
    const { createMessage: msg, createConfirm, notification } = useMessage();

    onMounted(()=>{
      //getViewCheck();
      
     if(taskType.value=='copy' || taskType.value =='detail' ){
       viewCheck();
     }else{
       getCheckInfo()
     }
      //returnSignPage();
       
    })
    async function viewCheck(){
      let result = await getViewCheck({signRuId:signRuId});
       console.log("result.value",result)
      if(result){ 
        returnSignPage();
        return;
      }
      pageLoading.value = false;
    }
    
    async function getCheckInfo(){
      let result = await getContractNoAuthType({signRuId})
      if(result){
        tipInfo.value = result;
        if(result.checkStatus === 1){
          console.log('身份正确，直接进入');
          returnSignPage();
        }
        // 账号正确、身份不正确
        if (result.checkStatus === 4 || result.checkStatus === 7) {
            console.log('身份不正确，自动切换身份');
            handleChangeIdentity();
            return;
        }
      }
      pageLoading.value = false;
      
    }

    //企业实名认证
    function handleEntAuth(){
      const token = userStore.getToken;
      let authInfo = window.appInfo.auth_app_info;
      let appInfo = {
          token:token,
          appCode:authInfo.appCode,
          appId:authInfo.appId,
          departId:userInfo.loginDepartId,
        }
        let paramsString = new URLSearchParams(appInfo).toString();
        // 山东小青新科技有限公司  tipInfo.targetTenantName disabled=true
        var url = `${authInfo.url}/#/enterprise?${paramsString}&authStep=1&tenantName=${tipInfo.value.targetTenantName}&disabled=true`;
        // window.open(authInfo.appAddress +'/#/enterprise' + '?' + paramsString+"&authStep=1",'_self')
        window.open(url,'_self')
    }

     //开通个人账号
     async function  openPersonalAccount(){
      createConfirm({
          title: '个人账号开通提醒', 
          content: '请确定是否开通个人账号？',
          iconType: 'warning',
          onOk:async function () {
            let { code ,message} = await openPersonalTenant({});
              if (code == 200) {
                msg.success('开通成功,即将跳转',3);
                let tenantList = await getMyTenantDeparts();
                let matchDepart:any = tenantList.filter((u:any)=>u.tenantType === 2);
                if((matchDepart&&matchDepart.length)){
                    handleMenuClick(matchDepart[0])
                }
              }else {
                msg.error('创建企业失败:'+ message,10);
              }
          }
        })
      }
      function returnSignPage(){
        let redirectPath = '';
        if(taskType.value=='write'){
          redirectPath = '/#/contract/params?__full__&signRuId=' +  signRuId + '&taskId=' + taskId + '&type=receive&from=list' + '&callbackPage=' + callbackPage;
        }
        if(taskType.value=='sign'){
          redirectPath = '/#/contract/sign?__full__&signRuId=' +  signRuId + '&taskId=' + taskId + '&from=list' + '&callbackPage=' + callbackPage;
        }
        if(taskType.value=='approval'){
          redirectPath = '/#/contract/approval?__full__&signRuId=' +  signRuId + '&taskId=' + taskId + '&from=list' + '&callbackPage=' + callbackPage;
        }
        if(taskType.value=='copy' || taskType.value =='detail' ){
          redirectPath = '/#/contract/detail/sign?__full__&signRuId=' +  signRuId + '&taskId=' + taskId + '&from=list' + '&callbackPage=' + callbackPage;
        }
        window.open(redirectPath,'_self')
      }
      function handleLoginOut() {
        createConfirm({
          title: '提醒', 
          content: '确定退出当前应用？',
          okText:'确定',
          cancelText:'取消',
          iconType: 'warning',
          onOk:async function () {
            userStore.clearToken(true);
          }
        })
      }

            //创建企业
      async function createCompany() {
        createConfirm({
          title: '企业创建提醒', 
          content: '您将创建【'+ tipInfo.value.targetTenantName + '】企业账号，创建成功后，您将成为企业的管理员，请确定是否创建？',
          okText:'确定创建',
          cancelText:'取消',
          iconType: 'warning',
          onOk:async function () {
            let { code ,message} = await companyAddApi({name: tipInfo.value.targetTenantName});
              if (code == 200) {
                msg.success('创建企业成功,即将进入该企业空间下',3);
                let tenantList = await getMyTenantDeparts();
                let matchDepart:any = tenantList.filter((u:any)=>u.tenantName == tipInfo.value.targetTenantName);
                if((matchDepart&&matchDepart.length && tipInfo.value.targetTenantName)){
                    handleMenuClick(matchDepart[0])
                }
              }else {
                msg.error('创建企业失败:'+ message,10);
              }
          }
        })
      }
      function handleBackHome(){
        go('/dashboard/workbench')
      }

      async function handleChangeIdentity(){
        let tenantList = await getMyTenantDeparts();
        let matchDepart = null;
        if(tenantList && tenantList.length > 0){
          const targetTenantName = partyName || tipInfo.value.targetTenantName;
          if(targetTenantName === "个人租户"){
            matchDepart = tenantList.find((u: any) => u.tenantType === 2);
          }else{
            matchDepart = tenantList.find((u: any) => u.tenantName === targetTenantName);
          }
          if(matchDepart){
            await handleMenuClick(matchDepart);
          } else {
            msg.warning('未找到匹配的身份信息');
            pageLoading.value = false;
          }
        } else {
          msg.warning('暂无可用的身份信息');
          pageLoading.value = false;
        }
      }
      async function handleMenuClick(depart){
        let params = {
            departId:depart.departId
          }
          const result = await userStore.selectTenantAuth(params);
          if(result){
              // msg.success('切换成功,即将跳转')
          }
      }

      async function handleJoinEnterprise(){
          openCompanyJoin(true, {
          companyName: tipInfo.value.targetTenantName,
        });
      }

    return {
      tipInfo,
      userInfo,
      handleEntAuth,
      openPersonalAccount,
      handleLoginOut,
      handleBackHome,taskType,
      partyName,
      handleChangeIdentity,
      createCompany,
      handleJoinEnterprise,companyJoinRegister,
      pageLoading,
    }
  }
})
</script>

<style lang="less" scoped>
.sign-noauth-container{
  text-align:center;
  .sign-noauth-body{
    margin-top:180px;
  }
  .no-auth-action{
    margin-top:20px;
    :deep(.ant-btn){
      width:120px;
      display:block;
      margin:10px auto;
    }
  }
}

  :deep(.ant-btn-primary) {
    // font-size: 16px;
    // border-radius: 8px;
    min-width: 100px;
    height: 32px;
    background: linear-gradient(90deg, #1890ff 0%, #40a9ff 100%);
    border: none;
    color: #fff;
    // font-weight: 600;
    box-shadow: 0 2px 8px rgba(24,144,255,0.10);
    transition: background 0.2s, box-shadow 0.2s;
    &:hover, &:focus {
      background: linear-gradient(90deg, #127fd2 0%, #1890ff 100%);
      color: #fff;
      box-shadow: 0 4px 16px rgba(24,144,255,0.18);
    }
  }
</style>

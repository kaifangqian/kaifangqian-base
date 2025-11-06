<!--
  @description 任务办理无权限页面

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
    <div class="sign-noauth-container">
        <Loading :loading="pageLoading" text="加载中..." />
        <div class="no-auth-change-account" v-show="!pageLoading">
            <SwitchIdentity ref="switchIdentityRef" @handleJoinSubmitCall="handleJoinSubmitCall">
                <div>

                </div>
            </SwitchIdentity>
        </div>
        <div class="sign-noauth-body" v-if="(tipInfo.checkStatus != 1 || taskType == 'detail' || taskType == 'copy') && !pageLoading">
            <div class="no-auth-header">
                <SvgIcon name="sign-no-auth" size="60" />
                <p style="font-size:14px; color: #555;">无权访问</p>
            </div>
            <div class="no-auth-tip" style="margin-top: 20px;">
                <p v-if="tipInfo.checkStatus == 2 || tipInfo.checkStatus == 3">该任务属于账号【{{ tipInfo.targetUserPhone || tipInfo.targetUserEmail}}】,您当前登录的账号是【{{ tipInfo.currentUsername }}】您无法访问该任务，请切换账号或联系文件发起方</p>
                <p v-if="tipInfo.checkStatus == 4">请切换至当前账号的{{partyName?'【'+ partyName+'】':'个人' }}身份下查看该任务</p>

                <p v-if="tipInfo.checkStatus == 5">该任务属于个人账号任务，您当前还未开通个人账号，请先开通</p>

                <p v-if="tipInfo.checkStatus == 6">该任务属于【{{ tipInfo.targetTenantName}}】，您尚未加入到该企业中，请联系企业管理员邀请您加入，加入企业后再完成该任务</p>

                <p v-if="tipInfo.checkStatus == 7">请切换至当前账号的【{{ tipInfo.targetTenantName }}】身份查看该任务</p>
                <!-- <p v-if="tipInfo.checkStatus == 8">该任务属于【{{ tipInfo.targetTenantName }}】，该企业尚未开通账号，请先完成企业实名认证</p> -->
                <p v-if="tipInfo.checkStatus == 8">该任务属于【{{ tipInfo.targetTenantName }}】，该企业尚未开通账号，请先创建企业</p>
                <p v-if="taskType == 'detail' || taskType == 'copy'">您当前登录的账号/身份无权查阅该文件，请切换账号/身份或联系文件发起方</p>

            </div>
            <div class="no-auth-action">
                <!-- <van-button type="primary" v-if="(tipInfo.checkStatus !=5 && (tipInfo.checkStatus !=4  && partyName) && tipInfo.checkStatus!=7) || (tipInfo.checkStatus ==4  && !partyName)" @click="handleBackHome">返回首页</van-button> -->
                <van-button type="primary" v-if="(tipInfo.checkStatus ==2 || tipInfo.checkStatus ==3 )" @click="handleBackHome">返回首页</van-button>
                <van-button type="primary" v-if="tipInfo.checkStatus == 4 || tipInfo.checkStatus == 7" @click="handleChangeIdentity">切换身份</van-button>
                <van-button type="primary" v-if="tipInfo.checkStatus == 5" @click="openPersonalAccount">开通个人账号</van-button>
                <van-button type="primary" v-if="tipInfo.checkStatus == 8" @click="createCompany">创建企业</van-button>
                <van-button type="primary" v-if="tipInfo.checkStatus == 6" @click="handleJoinEnterprise">申请加入</van-button>
                <van-button type="danger" @click="handleLoginOut">退出登录</van-button>
            </div>
        </div>
    </div>
</template>
  
<script lang="ts">
import { ref, defineComponent, unref, onMounted } from "vue";
import { useUserStore } from '@/store/modules/user';
import Api, { LoginParams } from '@/api/contract/index';
import { useRouter } from 'vue-router';
import { Toast, Dialog, Notify } from 'vant';
import session from "@/utils/cache/session";
import SwitchIdentity from '@/pages/components/SwitchIdentity.vue';
import { APP_TOKEN } from '@/utils/cache/constant';
import {  getHashQueryString } from '@/utils/util';


export default defineComponent({
    name: "noAuth",
    components: {
        SwitchIdentity
    },
    setup() {
        const userStore = useUserStore();
        const userInfo = userStore.getUserInfo;
        const callbackPage = getHashQueryString('callbackPage'); 
        const partyName = getHashQueryString('partyName')?decodeURIComponent(getHashQueryString('partyName') as string):'';
        const tipInfo = ref({
            originAccount: '',
            currentAccount: '',
            tenantName: '',
            checkStatus: 1,
            targetTenantName: "",
            currentUsername: "",
            targetUserPhone: "",
            targetUserEmail:'',
        })
        const switchIdentityRef = ref();
        const router = useRouter();
        const { currentRoute } = router;
        const route = unref(currentRoute);
        const signRuId = route.query.signRuId;
        const taskId = route.query.taskId;
        const taskType = ref(route.query.taskType);
        session.setItem('Sign-Task-Id', taskId);
        const pageLoading = ref(true);


        onMounted(() => {
            if (taskType.value == 'copy' || taskType.value == 'detail') {
                viewCheck();
            } else {
                getCheckInfo()
            }
            // getCheckInfo()

        })
        async function viewCheck() {
            let { result } = await Api.getViewCheck({ signRuId: signRuId });
            console.log("result.value", result)
            if (result) {
                returnSignPage();
            }
            pageLoading.value = false;
        }

        async function getCheckInfo() {
            let { result } = await Api.checkNOAuthType({ signRuId })
            if (result) {
                tipInfo.value = result;
                if (result.checkStatus === 1) {
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
        function handleEntAuth() {
            const token = session.getItem(APP_TOKEN)
            // let authInfo = userInfo.authInfo;
            // let appInfo = {
            //     token: token,
            //     appCode: authInfo.appCode,
            //     appId: authInfo.appId,
            //     departId: userInfo.loginDepartId,
            // }
            router.push({
                path: '/enterprise',
                query: {
                    taskType: taskType.value,
                    signRuId: signRuId,
                    taskId: taskId,
                    path: '/noauth',
                    from: 'auth',
                    callbackPage:callbackPage
                }
            })

        }

        //开通个人账号
        async function openPersonalAccount() {
            Dialog.confirm({
                title: '个人账号开通提醒',
                message: '请确定是否开通个人账号？',
            }).then(async () => {
                let { result, code } = await Api.openPersonalTenant({});
                if (code == 200) {
                    Notify({ type: 'success', message: '开通成功,即将跳转', duration: 3000 });

                    await userStore.reloadTenantDeparts();
                    let tenantList = userStore.getTenantDeparts;
                    let matchDepart:any = tenantList.filter((u:any)=>u.tenantType === 2);
                    if((matchDepart&&matchDepart.length)){
                        switchIdentityRef.value.handleSubmit(matchDepart[0])
                    }
                }
            }).catch(() => {
                // on cancel
            });
        }

        function returnSignPage() {
            let redirectPath = '';
            if (taskType.value == 'write') {
                // redirectPath = '/#/write?&signRuId='        + signRuId + '&taskId=' + taskId + '&callbackPage=' + callbackPage;
				redirectPath = "/write";
            }
            if (taskType.value == 'sign') {
                // redirectPath = '/#/signContract?&signRuId=' + signRuId + '&taskId=' + taskId + '&callbackPage=' + callbackPage;;
				redirectPath = "/sign";
            }
            if (taskType.value == 'approval') {
                // redirectPath = '/#/signContract?&signRuId=' + signRuId + '&taskId=' + taskId + '&callbackPage=' + callbackPage;;
				redirectPath = "/approval";
            }
            if (taskType.value == 'copy' || taskType.value == 'detail') {
                // redirectPath = '/#/detail?signRuId=' + signRuId + '&taskId=' + taskId + '&callbackPage=' + callbackPage;;
				redirectPath = "/detail";
            }
            if (taskType.value == 'sign') {
                // redirectPath = '/#/signContract?&signRuId=' + signRuId + '&taskId=' + taskId + '&callbackPage=' + callbackPage;;
				redirectPath = "/signContract";
            }
            if (taskType.value == 'copy' || taskType.value == 'detail') {
                // redirectPath = '/#/detail?signRuId=' + signRuId + '&taskId=' + taskId + '&callbackPage=' + callbackPage;
				redirectPath = "/detail";
            }
			
			const resolveUrl = router.resolve({
				path:redirectPath,
				query:{
					signRuId:signRuId,
					taskId:taskId,
					callbackPage:callbackPage
				}
			})
            window.open(resolveUrl.href, '_self')
        }
        function handleLoginOut() {
            Dialog.confirm({
                title: '提醒',
                message: '确定退出？',
            }).then(() => {
                userStore.logout();
                router.push("/login")

            }).catch(() => {
                // on cancel
            });
        }

        //创建企业
        async function createCompany() {
            Dialog.confirm({
                title: '企业创建提醒',
                message: '您将创建【'+ tipInfo.value.targetTenantName + '】企业账号，创建成功后，您将成为企业的管理员，请确定是否创建？',
                confirmButtonText: '确定创建',
                cancelButtonText: '取消'
            }).then(async () => {
                let { result, code ,message} = await Api.createCompany({name: tipInfo.value.targetTenantName});
                if (code == 200) {
                    Notify({ type: 'success', message: '创建企业成功,即将进入该企业空间下', duration: 3000 });
                    await userStore.reloadTenantDeparts();
                    let tenantList = userStore.getTenantDeparts;
                    let matchDepart:any = tenantList.filter((u:any)=>u.tenantName == tipInfo.value.targetTenantName);
                    if((matchDepart&&matchDepart.length && tipInfo.value.targetTenantName)){
                        switchIdentityRef.value.handleSubmit(matchDepart[0])
                    }
                }else {
                    Notify({ type: 'danger', message: '创建企业失败:'+ message, duration: 3000 });
                }
            }).catch(() => {
                // on cancel
            });
        }
        function handleBackHome() {
            router.push('/index')
        }


      async function handleChangeIdentity() {
        let tenantList = userStore.getTenantDeparts;
        let matchDepart = null;
        if (tenantList && tenantList.length > 0) {
          const targetTenantName = partyName || tipInfo.value.targetTenantName;
          if (targetTenantName === '个人租户') {
            matchDepart = tenantList.find((u: any) => u.tenantType === 2);
          } else {
            matchDepart = tenantList.find((u: any) => u.tenantName === targetTenantName);
          }
          if (matchDepart) {
            switchIdentityRef.value.handleSubmit(matchDepart);
          } else {
            Notify({ type: 'warning', message: '未找到匹配的身份信息', duration: 3000 });
            pageLoading.value = false;
          }
        } else {
          Notify({ type: 'warning', message: '暂无可用的身份信息', duration: 3000 });
          pageLoading.value = false;
        }
      }

      async function handleJoinEnterprise() {
        switchIdentityRef.value.handleJoinEnterprise();
      }

        async function handleJoinSubmitCall(data:any){ 
            // 这里处理 handleJoinSubmit 的回调逻辑
            // 加入成功，刷新租户列表，切换到目标身份下
            if (data.result === 1) {
                await userStore.reloadTenantDeparts();
                let tenantList = userStore.getTenantDeparts;
                let matchDepart:any = tenantList.filter((u:any)=>u.tenantName == tipInfo.value.targetTenantName);
                if((matchDepart&&matchDepart.length && tipInfo.value.targetTenantName)){
                switchIdentityRef.value.handleSubmit(matchDepart[0])
                
            } else {
                console.log("已申请加入，管理员开启了申请加入审核流程，请耐心等待或联系管理员及时审核");
            }
            } 
        }

        return {
            tipInfo,
            handleChangeIdentity,
            userInfo,
            partyName,
            handleEntAuth,
            openPersonalAccount,
            handleLoginOut,
            handleBackHome, taskType, switchIdentityRef,createCompany,handleJoinEnterprise,handleJoinSubmitCall,
            pageLoading,
        }
    }
})
</script>
  
<style lang="less" scoped>
.sign-noauth-container {
    text-align: center;

    .sign-noauth-body {
        margin-top: 180px;
        // box-shadow: 0 4px 9px 2px #eee;
        // background: #fff;
        padding: 40px 30px;
    }

    .no-auth-action {
        margin-top: 40px;
        // display: flex;
        justify-content: center;

        :deep(.van-button) {
            min-width: 200px;
            display: block;
            margin: 10px auto;
            height: 60px;
            margin-bottom: 20px;
        }
    }
}
</style>
  
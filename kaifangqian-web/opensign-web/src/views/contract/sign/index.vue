<template>
  <div class="position-container"  :style="'height: '+ (height-63) +'px'">
    <Loading :loading="loading" :absolute="false" :tip="tip"/>
    <!-- <div class="ca-auth-info">
      <a-alert  v-if="signControlInfo.type==1 && signerCaAuthInfo.authStatus !=2 && signerCaAuthInfo.certType !==1 && signerCaAuthInfo.certType != null"  type="warning" show-icon message="您尚未完成实名认证，请先完成实名认证后再签署"/>
      <a-alert  v-if="signControlInfo.type==2 && signerCaAuthInfo.authStatus !=2 && signerCaAuthInfo.certType !==1 && signerCaAuthInfo.certType != null"  type="warning" show-icon message="您当前的企业尚未完成实名认证，请联系企业管理员完成实名认证后再签署"/>
      <a-button type="primary" @click="handleAuth"  v-if="signControlInfo.type==1 && signerCaAuthInfo.authStatus !=2 && signerCaAuthInfo.certType !==1 && signerCaAuthInfo.certType != null">实名认证</a-button>
    </div> -->
    <DocHeader :actions="actions" :docs="docs"   :docId="docId" @change="handleDocChange" @cancel="handleCancel">
    </DocHeader>
   
    <div class="position-params-body">
      <DocLeft title="签署方">
        <SignStatus :signerList="signerList">
        </SignStatus>
        <SignControls
            v-if="!isDetail"
            :documentList="documentList" 
            :signerList="signerList" 
            :nowDocument="nowDocument"
            :controlChangeFlag="controlChangeFlag"
            :defaultHasControl="defaultHasControl"
            :user="signers"
            :chopStampUseFlag="chopStampUseFlag"
            @dragOver="controlsDragOver"
            @scroll="handleScroll"
            @signControlType="setSignControlType">
        </SignControls>
      </DocLeft>
      <DocContent ref="doContent" :nowDocument="nowDocument"  :docs="docs" :showControl="true" :isSign="true"  :minTargetInfo="minTargetInfo" @controlDelete="controlDelete"
				@controlMousedown="controlMousedown" @controlClick="signControlClick" @handleSetControl="handleSetControl" @signDateFormat="signDateFormat" @refreshControlPosition="refreshControlPosition">
      </DocContent>
      <DocRight title="签约详情">
        <SignDetail />
      </DocRight> 

    </div>
    <SignSealModal @register="registerSeal" @success="handleSetSeal"></SignSealModal>
    <SignatureModal @register="registerSignature" @signature="handleSetSignature" @base64Save="handleWriteSignature"></SignatureModal>
    <WishModal @register="registerWish" @success="handleConfirmSuccess"></WishModal>

    <a-modal v-model:visible="signDatevisible" title="签署日期格式" @ok="handleSignDateFormat">
      <a-select ref="select"  v-model:value="signDateType" style="width:450px">
        <a-select-option value="YYYY年MM月DD日">YYYY年MM月DD日</a-select-option>
        <a-select-option value="YYYY-MM-DD">YYYY-MM-DD</a-select-option>
        <a-select-option value="YYYY/MM/DD">YYYY/MM/DD</a-select-option>
      </a-select>
    </a-modal>

    
    <a-modal v-model:visible="needOpenPersonlAccountVisible" title="温馨提示" :footer="null">
        <p>您暂未开通个人账号，请开通个人账号</p>
        <a-button type="default" @click="handleUnSign" style="margin:0 10px">暂不签署</a-button>
        <a-button type="primary" @click="faceOpenPersonalAccount" style="margin:0 10px">开通个人账号</a-button>
    </a-modal>
    
    <a-modal v-model:visible="signCertificateVisible" title="温馨提示" :footer="null">
        <!-- 以下为持证主体的信息 -->
      <!-- 无证书 且未开通个人账号 -->
        <div class="certificate-info" v-if="!certificateInfo.tenantId && certificateInfo.returnCode=='2'">
            <p>您暂未开通个人账号，请开通个人账号</p>
            <a-button type="default" @click="handleUnSign" style="margin:0 10px">暂不签署</a-button>
            <a-button type="primary" @click="openPersonalAccount" style="margin:0 10px">开通个人账号</a-button>
        </div>
      <!-- 不校验证书 -->
        <div class="certificate-info" v-if="certificateInfo.returnCode=='1'">
            <p>签署文件时，不使用任何数字证书，仅在文件上加盖合成签名和印章图片，<span style="color:#f20404fc">无法保护文件也不具备法律效力，</span>请知悉！</p>
            <a-button type="default" @click="handleUnSign">暂不签署</a-button>
            <a-button type="primary" @click="goSign" style="margin:0 10px">继续签署</a-button>
        </div>
         <!-- 测试证书 -->
         <div class="certificate-info" v-if="certificateInfo.certType=='2'&& certificateInfo.returnCode=='2'">
            <p v-if="certificateInfo.holderType=='2'">您尚未拥有可用于电子签约的测试证书，请获取证书</p>
            <p v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus != 2 ">您尚未拥有可用于电子签约的测试证书，请先实名认证</p>
            <p v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus == 2 ">您尚未拥有可用于电子签约的测试证书，请获取证书</p>
            <p><Icon icon="ant-design:sync-outlined" /> <a-button type="link" @click="updateCertificate">刷新</a-button></p>
            <div>
              <a-button type="default" @click="handleUnSign">暂不签署</a-button>
              <a-button type="primary" @click="updateCertificate" style="margin:0 10px" v-if="certificateInfo.holderType=='2'">获取证书</a-button>
              <a-button type="primary" @click="handlePersonAuth" style="margin:0 10px" v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus != 2">实名认证</a-button>
              <a-button type="primary" @click="updateCertificate" style="margin:0 10px" v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus == 2">获取证书</a-button>
            </div>
        </div>
         <div class="certificate-info" v-if="certificateInfo.certType=='2' && certificateInfo.returnCode=='3'">
            <p v-if="certificateInfo.holderType=='2'">您签署所使用的企业证书已失效，如果您需要继续签署，请申请新的证书</p>
            <p v-if="certificateInfo.holderType=='1'">您签署所使用的证书已过期，如果您需要继续签署，请申请新的证书</p>
            <div>
              <a-button type="default" @click="handleUnSign">暂不签署</a-button>
              <a-button type="primary" @click="updateCertificate" style="margin:0 10px">更新证书</a-button>
            </div>
        </div>

        <!-- CA数字证书 -->
        <div class="certificate-info" v-if="((certificateInfo.certType=='3' || certificateInfo.certType== '4') && certificateInfo.returnCode=='2')">
            <p v-if="certificateInfo.holderType=='2'">您尚未拥有可用于电子签约的数字证书，请联系企业管理员认证企业信息，企业认证通过后，为企业自动下发数字证书</p>
            <p v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus != 2 ">您尚未拥有可用于电子签约的数字证书，请先实名认证</p>
            <p v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus == 2 ">您尚未拥有可用于电子签约的数字证书，请获取证书</p>
            <p><Icon icon="ant-design:sync-outlined" /> <a-button type="link" @click="updateCertificate">刷新</a-button></p>
            <a-button type="default" @click="handleUnSign" style="margin:0 10px">暂不签署</a-button>
            <a-button type="primary" @click="handlePersonAuth" style="margin:0 10px" v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus != 2">实名认证</a-button>
            <a-button type="primary" @click="updateCertificate" style="margin:0 10px" v-if="certificateInfo.holderType=='1' && tenantAuthInfo.authStatus == 2">获取证书</a-button>
        </div>
        <div class="certificate-info" v-if="(certificateInfo.certType=='3' || certificateInfo.certType== '4') && certificateInfo.returnCode=='3'">
            <p v-if="certificateInfo.holderType=='2'">您签署所使用的企业证书已失效，如果您需要继续签署，请申请新的证书</p>
            <p v-if="certificateInfo.holderType=='1'">您签署所使用的证书已过期，如果您需要继续签署，请申请新的证书</p>
            <div>
              <a-button type="default" @click="handleUnSign">暂不签署</a-button>
              <a-button type="primary" @click="updateCertificate" style="margin:0 10px">更新证书</a-button>
            </div>
        </div>
        <!-- 平台防篡改证书 -->
        <div class="certificate-info" v-if="certificateInfo.certType=='1' && certificateInfo.returnCode=='2' ">
            <p>您暂未开通个人账号，开通个人账号后，会为您自动颁发平台防篡改证书，用于文件签署时，保护文件不被篡改</p>
            <div>
              <a-button type="default" @click="handleUnSign">暂不签署</a-button>
              <a-button type="primary" @click="openPersonalAccount" style="margin:0 10px" v-if="certificateInfo.holderType=='1'">开通个人账号</a-button>
            </div>
        </div>
        <div class="certificate-info" v-if="certificateInfo.certType=='1' && certificateInfo.returnCode=='3'">
            <p>您签署所使用的防篡改证书已失效，如果您需要继续签署，请申请新的证书</p>
            <div>
              <a-button type="default" @click="handleUnSign">暂不签署</a-button>
              <a-button type="primary" @click="updateCertificate" style="margin:0 10px">更新证书</a-button>
            </div>
        </div>
        <div class="certificate-info" v-if="certificateInfo.certType=='1'&& certificateInfo.returnCode=='4'">
            <p>您当前用于文件签署的证书是平台下发的防篡改证书，<span style="color:#f20404fc">该证书非CA机构颁发，</span>仅用于文件保护，避免文件被篡改，<span style="color:#f20404fc">签署后的文件不具备法律效力，</span>请知悉！</p>
            <div>
              <a-button type="default" @click="handleUnSign">暂不签署</a-button>
              <a-button type="primary" @click="goSign" style="margin:0 10px">继续签署</a-button>
            </div>
        </div>
    </a-modal>
    <a-modal v-model:visible="authCertificateVisible" title="温馨提示" :footer=null>
      <p>您已经申请新的证书，可用于电子签约</p>
      <p>证书有效期：<span style="color:#f20404fc;;margin-left: 20px;">{{authCerInfo}}</span></p>
      <a-button type="primary" @click="goSign" style="position: relative;  left: 50%;  transform: translate(-50%);">继续签署</a-button>
    </a-modal>
    <a-modal v-model:visible="taskVisible" title="任务提醒" :closable="false" :maskClosable="false">
          <p>监测到该签约文档中您还有【{{ taskInfo.taskType == 'sign' ? '签署 ' : '填写' }}】任务，是否前去处理？</p>
          <template #footer>
                <a-button type="default" @click="handleNoTask">暂不处理</a-button>
                <a-button type="primary" @click="handleGoNextTask">立即处理</a-button>
          </template>
      </a-modal>
  </div>
</template>

<script lang="ts">
import {ref, unref, toRefs, defineComponent, onMounted, reactive, getCurrentInstance, watch, h, createVNode} from "vue";
import { Modal, Input, Button } from 'ant-design-vue';  
import { Icon } from '/@/components/Icon';
import { useRouter } from 'vue-router'; 
import { Loading } from '/@/components/Loading';
import DocHeader from '../components/layouts/DocHeader.vue';
import DocLeft from '../components/layouts/DocLeft.vue';
import DocContent from '../components/layouts/DocContent.vue';
import DocRight from '../components/layouts/DocRight.vue';
import Controls from '../components/Controls.vue';
import SignDetail from '../components/SignDetail.vue';
import SignStatus from '../components/SignStatus.vue';
import SignControls from '../components/SignControls.vue';
import SignSealModal from '/@/views/contract/modal/sign/signSealModal.vue';
import SignatureModal from '/@/views/contract/modal/sign/signatureModal.vue';
import WishModal from '/@/views/contract/modal/wish/WishModal.vue';
import { getColtrolByType, controlListSource  } from '../components/data/ControlData';
import { recaculateBatchControlPosInPage  } from '../components/data/ControlerMoveRange';
import { useMessage } from '/@/hooks/web/useMessage';
import { useUserStore } from '/@/store/modules/user';
import { useModal } from '/@/components/Modal';
import {  getAppTokenByAuthToken, getTenantAuthStatus } from '/@/api/sys/user';
import {TenantTypeEnum} from '/@/enums/tenant';
import { handleAuth as handleUserAuth } from '/@/views/sys/user';

import { createLocalStorage } from '/@/utils/cache';

import { 
    getControlList, 
    checkOperatorStatus, 
    getDocFiles, 
    getDocImgs, getSigners,
    rejectSign, 
    getVerifyCertificate, 
    submitSign, 
    getConfirmType,
    getUpdateCertificate, 
    verifySignSeal,
    getConfirmNo,
    getBusinessRuInfo,
    getSignNodeConfig
} from '/@/api/contract/index';
import { cloneDeep } from 'lodash-es';
import {currentPosition, recaculateControlPosInPage, currentPositionReverse, pageScaling, paramBuildWidgets, findMinImageSize} from "/@/views/contract/components/data/ControlerMoveRange"
// import { currentPosition } from "/@/views/signature/doc/control/src/data/ControlerMoveRange"
// import { CanvasZoom } from '/@/views/signature/doc/control/src/data/ControlData'
import { getAppEnvConfig } from '/@/utils/env';
import { openPersonalTenant } from '/@/api/tenant';
import { parseInputPages, getBase64Size, retainDecimals } from '/@/utils';
import { buildUUID } from '/@/utils/uuid';
import { getHashQueryString,decodeURIs } from '/@/utils';
import { getSystemLimit} from '/@/api/license';
import { ControlType,CanvasZoom } from '/@/views/contract/components/data/ControlData';
import { getPlatePersonalSignAuth } from '/@/api/sys/common';


interface BtnFun {
 	 ():void   
}

interface ButtonItem {
  type:string;
  text:string;
  disabled?:boolean;
  show?:boolean;
  callBack:BtnFun,
}

export default defineComponent({
  name:"Sign",
  components:{
    Icon,
    DocHeader,
    DocLeft,
    DocContent,
    DocRight,
    Controls,
    SignDetail,
    SignStatus,
    SignControls,
    SignSealModal,
    SignatureModal,
    WishModal,
    Loading,
    Modal, Input, Button,

  },
  setup(props,{emit}) {
      const ls = createLocalStorage();
      const docId = ref('');
      const docs:any = ref([]); 
      const signDatevisible = ref(false); 
      const personalAuth = ref(false); 
      const signCertificateVisible = ref(false); 
      const authCertificateVisible = ref(false); 
      const isSignText = ref(true);
      const chopStampUseFlag = ref(false);
      const orderNo = ref('')
      const confirmTypeInfo:any = ref({
        confirmType:'face',
        personalAccountStatus:'',
      })
      const doContent = ref()
      const minTargetInfo = ref();
      const needOpenPersonlAccountVisible = ref(false)

      const certificateInfo = ref<any>({
        certType:'',
        returnCode:'',
        returnMsg:'',
        tenantId:'',
        departId:'',
        holderType:''
      }); 
      const authCerInfo = ref({});
      const signDateType = ref(''); 
      const controlChangeFlag = ref('necessary_and_add'); //签署位置方式设置
      const defaultHasControl = ref(false)   //默认是否指定了控件
      const signDateElement= ref({format:''}); 
      const compState = reactive({
        absolute: false,
        loading: false,
        tip:'加载中...'
      })

      
      const {VITE_GLOB_API_URL} = getAppEnvConfig();
      const baseUrl = ref(VITE_GLOB_API_URL);
      const newSealSize = ref({
        width:120,
        height:120,
      })
      const sealAnnexId = ref('');//签章图片id
      const willResult = ref(false);
      const confirmAction = ref('sign');
      const controlList = ref(<any[]>[])
      const signers:any = ref({});
      const entSealId = ref('');// 企业签章id
      const privateSeal = ref('');// 个人签章base64
      const documentList:any = ref([])
      const nowDocument:any = ref({
        activeControl:[]
      });
      const signerList:any = ref([]);
      const controlsList:any = ref([]);
      const images:any = ref([])
      const router = useRouter();
      const sealCount = ref(0);
      const signatureCount = ref(0);
      const signDateCount = ref(0);
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const taskId = route.query?.taskId;
      const signRuId = route.query?.signRuId;
      const signType = route.query?.type;
      const callbackPage = getHashQueryString('callbackPage');
      const partyName = getHashQueryString('partyName');
      const isDetail = ref(true);
      const deleteIdList = ref(<string[]>[]);
      const taskVisible = ref(false)
      const taskInfo:any = ref({
        
      })
      const personalSignAuth = ref('');
      const signControlInfo:any = ref({
        type:0,
        certType:null

      });
      const currentControl:any = ref({
        signature:'',
        sealId:'',
        signatureId:''
      });
      const instance = getCurrentInstance();
      const { eventHub } = instance?.proxy;
      const signerCaAuthInfo = ref({
        authStatus:2
      })
      const userAuth = ref<{
        status: boolean,
        tenantType: number,
      }>({
        status: false,
        tenantType: 0,
      });
      const signControlSize = ref({
        width:120,
        height:120,
      })
      const actions = ref(<ButtonItem[]>[
        // {
        //   type:'default',
        //   text:'主动校验',
        //   callBack:handleValidate,
        //   show:true
        // },
        // {
        //   type:'default',
        //   text:'拒签',
        //   callBack:handleRejectSign,
        //   show: userAuth.value.status,
        // },
        // {
        //   type:'primary',
        //   text:isSignText.value?'签署':'提交签署',
        //   callBack:handleSign,
        //   show: userAuth.value.status,
        // },
        // {
        //   type:'primary',
        //   text:'实名认证',
        //   callBack:userHandleAuth,
        //   show: !userAuth.value.status
        // },
      ])

      // 0 有文档未设置签名控件 1已设置
      const submitCheck = ref<any>({
        status: 0,
        errorDocs: [],
      })
    
      ls.set('Sign-Task-Id',taskId)

      const userStore = useUserStore();
      const tenantAuthInfo =ref({
        authStatus:0,
      });
      const userInfo = userStore.getUserInfo;

      const { createMessage: msg, createSuccessModal , createConfirm, createWarningModal,createInfoModal} = useMessage();

      const [registerSeal, { openModal,closeModal }] = useModal();
      const [registerSignature, { openModal:openSignatureModal,closeModal:closeSignatureModal }] = useModal();
      const [registerWish, { openModal:openWishModal,closeModal:closeWishModal }] = useModal();






      async function handleFace(){
        let authInfo = window.appInfo.auth_app_info;
        // let params = {
        //       departId:certificateInfo.value.departId
        //     }
        // //authToken 用于实名认证
        // let authToken = await getAppTokenByAuthToken(params);
        //当前用户token
        if(confirmTypeInfo.value.personalAccountStatus  == '-1'){
            needOpenPersonlAccountVisible.value = true;
            return 
        }
        const token = userStore.getToken;
        if(token){
            let appInfo = {
                // authToken:authToken,
                token: token,
                appCode:authInfo.appCode,
                appId:authInfo.appId,
                taskId:taskId,
                departId:certificateInfo.value.departId,
                confirmType:confirmTypeInfo.value.confirmType,
                orderNo:orderNo.value,
                signRuId,
                authStatus:confirmTypeInfo.value.personalAccountStatus,
                certType:certificateInfo.value.certType,
                callbackPage:callbackPage
            }
            let paramsString = new URLSearchParams(appInfo).toString();
            setTimeout(()=>{
                window.open(authInfo.url +'/#/beforeCheck' + '?' + paramsString,'_self')
            },500)
        }
      }
       

        // async function initConfirmType(type){
        //     let result = await getConfirmType({operateType:type})
        //     if(result){
        //         confirmTypeInfo.value = result;
        //     }
        // }
        function getConfirmTypeForAction(params){
          return new Promise(async (resolve,reject)=>{
            let result = await getConfirmType(params)
            if(result){
                resolve(result)
            }else{
                reject(result)
            }
          })
        }

        // 获取签署节点的签署要求
        async function getSignNodeInfo(){
          const userCheckResult = await getVerifyCertificate({signRuId});
          console.log('userCheckResult.holderType', userCheckResult.holderType);
          // 签署主体是个人
          if(userCheckResult.holderType === TenantTypeEnum.PERSONAL){
            let platePersonSignAuth = '';
            const personalSignAuthRes = await getPlatePersonalSignAuth();
            
            // 获取平台总的签署要求
            if (personalSignAuthRes.code === 200) {
              platePersonSignAuth = personalSignAuthRes.result.personalSignAuthType;
            }
            
            // 如果平台配置要求个人签署必须认证或无需认证，则直接使用该值
            if(platePersonSignAuth === 'required' || platePersonSignAuth === 'not_required'){
              personalSignAuth.value = platePersonSignAuth;
            } else {
              // 查询该签署流程的总的签署要求
              const result = await getBusinessRuInfo({signRuId});
              
              if(result && result.baseVo.personalSignAuth === 'required' || result.baseVo.personalSignAuth === 'not_required'){
                personalSignAuth.value = result.baseVo.personalSignAuth;
              } else {
                // 查询该签署节点的签署要求
                const signNodeResult = await getSignNodeConfig({taskId});
                
                if(signNodeResult && signNodeResult.personalSignAuth){
                  personalSignAuth.value = signNodeResult.personalSignAuth;
                } else {
                  personalSignAuth.value = 'required';
                }
              }
            }
          } else {
            personalSignAuth.value = 'required';
          }
          console.log('个人签署实名认证要求：', personalSignAuth.value);
        }
    

      onMounted(async ()=>{
        try{
          compState.tip = '数据加载中...';
          compState.loading = true;
          await checkStatus()
          await getSignNodeInfo()
          await initData()
          await generateOrderNo()
          await initSysLimit();
          compState.loading = false
          compState.tip = '数据提交中...';
        } catch(e){
          console.log(e)
          compState.loading = false
          compState.tip = '数据提交中...';
        }
        
      })

      async function initSysLimit(){
        const limit = await getSystemLimit();
        chopStampUseFlag.value = limit.pagingSealUseFlag;
      }

      watch(
        [() => userAuth.value, () => isSignText.value, () => personalSignAuth.value],
        ([userAuthValue, isSignTextValue, personalSignAuthValue]) => {
          // console.log('用户实名认证状态--：', userAuthValue.status);
          // console.log('个人签署实名认证要求--：', personalSignAuthValue);
          actions.value = [
            {
              type:'default',
              text:'拒签',
              callBack:handleRejectSign,
              show: true
            },
            {
              type:'primary',
              text:isSignTextValue?'签署':'提交签署',
              callBack:handleSign,
              show: userAuthValue.status || personalSignAuthValue == 'not_required',
            },
            {
              type:'primary',
              text:'实名认证',
              callBack:userHandleAuth,
              show: !userAuthValue.status && personalSignAuthValue == 'required'
            },
          ];
        }, {
          deep: true
        }
      );

    watch(
      ()=>documentList.value,
      (list)=>{
          let allSignControls = list.flatMap(item=>item.activeControl).filter(v=>(v.controlType=='seal' || v.controlType=='signature' || v.controlType=='chop-stamp'));
          let signBase64Count =  allSignControls.filter(v=>{
              return (v.sealId || v.signature || v.signatureId)
          })
          if(signBase64Count.length){
              isSignText.value = false;
          }else{
              isSignText.value = true;
          }
      },{
          deep:true
      }
    )

      async function generateOrderNo(){
          let result = await getConfirmNo({mainId:taskId})
          if(result){
            orderNo.value = result;
          }
      }

      // 判断实名状态 
      async function checkCaAndAuthStatus(){
        const tenantInfo = userStore.getTenantInfo;
        const userCheckResult  = await getVerifyCertificate({signRuId});
        certificateInfo.value = userCheckResult;
        if(userCheckResult.holderType === TenantTypeEnum.ENTERPRISE){
          // 签署主体是否实名
          if (tenantInfo.authStatus === 2) {
            // 已实名判断签署人是否实名
            if(userCheckResult.signerAuthStatus === 2){
              userAuth.value.status = true;
              console.log("企业经办人都已实名");
            }else{
              // 去个人实名
              userAuth.value.tenantType = TenantTypeEnum.PERSONAL;
              console.log("企业已实名，企业经办人未实名");
            }
          }else{
            // 未实名  判断是否当前企业管理员 0:不是管理员；1:是管理员
            if(userCheckResult.signerIsCompanyManager === 0){
              // 提示信息：您当前的企业尚未完成实名认证，请联系企业管理员完成实名认证后再签署
              // 按钮：知道了
              userAuth.value.tenantType = 3;
              console.log("不是企业管理员，提示知道了");
            }else if(userCheckResult.signerIsCompanyManager === 1){
              if (userCheckResult.signerAuthStatus !== 2) {
                // 提示信息：企业认证需要当前认证的申请人先完成个人实名认证校验
                // 按钮：取消，立即认证
                // 立即认证-动作：使用当前签署人的个人身份发起个人实名认证
                userAuth.value.tenantType = TenantTypeEnum.PERSONAL;
                console.log("企业未实名、个人未实名，去个人实名");
              } else {// 管理员已实名，可进行企业认证
                // 提示信息：您将作为认证申请人，对 「${企业名称}」 进行实名认证
                // 按钮：取消，立即认证
                // 立即认证-动作：使用当前企业身份发起企业实名认证
                userAuth.value.tenantType = TenantTypeEnum.ENTERPRISE;
                console.log("企业未实名、去企业实名");
              }

            }
          }
          
        }else if(userCheckResult.holderType === TenantTypeEnum.PERSONAL){
          if(userCheckResult.signerAuthStatus === 2){
            userAuth.value.status = true;
            console.log("个人已实名");
          }else{
            // 去个人实名
            userAuth.value.tenantType = TenantTypeEnum.PERSONAL;
            // console.log("个人未实名，去实名");
          }
        }
      }

      function userHandleAuth(){
        // const asyncUrl = `${location.href}`;
        console.log("userAuth",userAuth.value);
        if(userAuth.value.tenantType === TenantTypeEnum.PERSONAL){
          
          createConfirm({
            iconType: 'info',
            width: '600px',
            title: '开启安全可靠的电子签章',
            // content: '为保证签署真实有效，请先完成个人实名认证校验',
            content: h('div', [
              // 第一部分：标题
              // h('div', {
              //   style: {
              //     fontSize: '16px',
              //     fontWeight: '600',
              //     marginBottom: '12px',
              //     color: '#333'
              //   }
              // }, '开启安全可靠的电子签章'),
    
              // 第二部分：操作指引
              h('div', {
                style: {
                  marginBottom: '16px'
                }
              }, [
                h('div', {
                  style: {
                    marginBottom: '8px',
                    fontWeight: '500'
                  }
                }, '为保证签署真实有效，请完成以下步骤：'),
                h('div', {
                  style: {
                    paddingLeft: '8px'
                  }
                }, [
                  h('div', '1. 点击【立即认证】'),
                  h('div', '2. 在「云盾系统」完成个人实名认证')
                ])
              ]),
              
              // 第三部分：服务说明（优化样式）
              h('div', {
                style: {
                  background: '#f0f8ff',
                  border: '1px solid #d0e9ff',
                  borderRadius: '4px',
                  padding: '12px',
                  fontSize: '13px',
                  color: '#31708f',
                  marginRight: '38px'
                }
              }, [
                h('div', {
                  style: {
                    display: 'flex',
                    alignItems: 'flex-start',
                    marginBottom: '6px'
                  }
                }, [
                  h('span', {
                    innerHTML: '&#128274;', // 锁图标
                    style: {
                      marginRight: '8px',
                      fontSize: '14px'
                    }
                  }),
                  h('span', {
                    style: {
                      fontWeight: '600',
                      fontSize: '14px'
                    }
                  }, '安全保障')
                ]),
                h('div', {
                  style: {
                    lineHeight: '1.6',
                    paddingLeft: '24px'
                  }
                }, '云盾系统为电子签章提供核心保障服务，包括实名认证、权威CA证书签发及签署意愿验证，确保签署过程安全合规。')
              ])
            ]),
            okText: '立即认证',
            async onOk() {
              await checkPersonalTenant();
              handleUserAuth(2,location.href);
            },
          });
        }
        else if(userAuth.value.tenantType === TenantTypeEnum.ENTERPRISE){
          const tenantInfo = userStore.getTenantInfo;
          createConfirm({
            iconType: 'info',
            width: '600px',
            title: '开启安全可靠的电子签章',
            // content: `您将作为认证申请人，对 「${tenantInfo.name}」 进行实名认证`,
            content: h('div', [
              // 第一部分：标题
              // h('div', {
              //   style: {
              //     fontSize: '16px',
              //     fontWeight: '600',
              //     marginBottom: '12px',
              //     color: '#333'
              //   }
              // }, '开启安全可靠的电子签章'),
    
              // 第二部分：操作指引
              h('div', {
                style: {
                  marginBottom: '16px'
                }
              }, [
                h('div', {
                  style: {
                    marginBottom: '8px',
                    fontWeight: '500'
                  }
                }, '为保证签署真实有效，请完成以下步骤：'),
                h('div', {
                  style: {
                    paddingLeft: '8px'
                  }
                }, [
                  h('div', '1. 点击【立即认证】'),
                  h('div', `2. 您将作为认证申请人，在「云盾系统」完成对「${tenantInfo.name}」的企业实名认证`)
                ])
              ]),
              
              // 第三部分：服务说明（优化样式）
              h('div', {
                style: {
                  background: '#f0f8ff',
                  border: '1px solid #d0e9ff',
                  borderRadius: '4px',
                  padding: '12px',
                  fontSize: '13px',
                  color: '#31708f',
                  marginRight: '38px'
                }
              }, [
                h('div', {
                  style: {
                    display: 'flex',
                    alignItems: 'flex-start',
                    marginBottom: '6px'
                  }
                }, [
                  h('span', {
                    innerHTML: '&#128274;', // 锁图标
                    style: {
                      marginRight: '8px',
                      fontSize: '14px'
                    }
                  }),
                  h('span', {
                    style: {
                      fontWeight: '600',
                      fontSize: '14px'
                    }
                  }, '安全保障')
                ]),
                h('div', {
                  style: {
                    lineHeight: '1.6',
                    paddingLeft: '24px'
                  }
                }, '云盾系统为电子签章提供核心保障服务，包括实名认证、权威CA证书签发及签署意愿验证，确保签署过程安全合规。')
              ])
            ]),
            okText: '立即认证',
            onOk() {
              handleUserAuth(1,location.href);
            },
          });
        }
        else if(userAuth.value.tenantType === 3){
          createInfoModal({
            iconType: 'warning',
            title: '温馨提示',
            content: '您当前的企业尚未完成实名认证，请联系企业管理员完成实名认证后再签署',
            okText: '知道了',
          });
        }
      }

      // 根据状态和当前操作人是否进行过签署进行路由重定向到签署详情或者文档详情页
     async  function checkStatus(){
        let result = await checkOperatorStatus({signRuId:signRuId});
        if(result){
          if(result.checkStatus != 1 ){
             router.replace({
                 name:'ContractNoauth',
                 query:{
                   signRuId,
                   taskId,
                   taskType:'sign',
                   callbackPage:callbackPage,
                   partyName:partyName
                 }
               })
             return 
             
          }
          if(result.ruStatus == 6 || result.ruStatus == 8 ||  result.ruStatus == 9 || result.ruStatus == 11){
              router.replace({
                name:'签约详情',
                query:{
                  ...route.query,
                  callbackPage:callbackPage
                }
              })
              return
          }
          if(result.operatorStatus==2){
            router.replace({
              name:'签约详情',
              query:{
                ...route.query,
                isDetail:'true',
                callbackPage:callbackPage
              }
            })
          }
          setTimeout(()=>{
            isDetail.value =  route.query?.isDetail=='true'? true : false;
            if(unref(isDetail)){
              actions.value = [];
              // 详情页左侧没有控件从这里请求文件
              getDocs()
            }
          })
        }   
      }
      function delayColseLoading(time?:number){
        setTimeout(function(){
          compState.loading = false;
        },time?time:300)
      }
      function initWindow() {
        var h = document.documentElement.clientHeight || document.body.clientHeight;
      	return h;
      }
      const height = ref(initWindow());
      async function initData(){
        getSignerList();
        //签署时拿到signerId再去请求
        // setTimeout(()=>{
        //   getDocs();
        // },500)
      }
      //获取文档
      async function getDocs(){
        let result = await getDocFiles({signRuId:signRuId})
        if(result){
            controlList.value = [];
            docs.value  = result;
            docId.value = result[0].id;
            docs.value.map(async(v,i)=>{
                  v.pageSize = v.docPage;
                  let result = await getDocImgs({ signFileId:  v.id });
                  v.images = result.sort((a, b) => a.page - b.page);
                  if(i==0){
                    images.value = result;
                  }
              })
            setTimeout(()=>{
                getAllDocControls();
            },100)    
        }
      }
      //获取图片列表
    async function getDocImages(){
        let result = await getDocImgs({signFileId:docId.value});
        if(result){
            images.value = result.sort((a, b) => a.page - b.page);
            documentList.value.map(v=>{
                if(v.id==docId.value){
                    v.images = result;
                    nowDocument.value.images = result;  
                }
            })
        }
    }
     
        //获取所有文档控件并整理结构
        async function getAllDocControls() {
              let result = await getControlList({ signRuId: signRuId, signerId:signControlInfo.value.signerId });
              let allControls = result.controlList;
              controlChangeFlag.value = result.controlChangeFlag;
              if(allControls.length){
                defaultHasControl.value = true;
              }else{
                defaultHasControl.value = false;
              }
              let flatControls:any = []
              //过滤掉填写控件 
              allControls = allControls.filter(v=>['signature', 'sign-date', 'seal', 'chop-stamp'].includes(v.controlType))
              // console.log(allControls,'allControls签署控件-------------')
              //将控件按文档拆分
              allControls.map((item:any)=>{
                  if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(item.controlType) && item.propertyVoList){
                        //每个控件propertyVoList 内只可能存在一个类型为page_config 的配置项
                      let pageConfig =  item.propertyVoList.filter(u=>u.propertyType == 'page_config')[0];
                      let pageCustomConfig =  item.propertyVoList.filter(u=>u.propertyType == 'custom')[0];
                      const frontUid = 'api_uid_'+ parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                      item.propertyVoList.map((v)=>{
                          if(v.propertyType == 'relation_doc' ){
                              //将每个relation_doc 控件都推入flatControls数组
                              flatControls.push({
                                  ...item,
                                  propertyVo:v,
                                  unequalId:v.id, //用于渲染  
                                  uid:frontUid,
                                  signRuDocId:v.propertyValue,
                                  controlDocId:v.propertyValue,
                                  propertyVoList:item.propertyVoList,
                                  controlPageInfo:{
                                      ...pageConfig,
                                      controlDocId:v.propertyValue,
                                  },
                                  pageCustomConfig:{
                                      ...pageCustomConfig,
                                      
                                  },
                              })
                          }
                      })
                  }else{
                      //如果是填写控件，则controlDocId 为 signRuDocId
                      item.controlDocId = item.signRuDocId;
                      item.unequalId = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(10);
                      item.propertyVoList = item.propertyVoList || [];
                      item.controlPageInfo = {};
                      flatControls.push(item);
                  }
                
              })
              // console.log(flatControls,'flatControls-------------')
              //将控件按文档分类
              let groupControls:any = [];
  
              // 遍历flatControls数组
              flatControls.forEach((m:any)=>{
                let matchSameUid = flatControls.filter(u=>u?.uid == m.uid && ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType))
                if(matchSameUid.length>1){
                    matchSameUid.map((u:any)=>{
                        u.isBatch = true;
                    })
                }
                 // 在groupControls数组中查找与m.controlDocId匹配的控件
                 let matchControl =  groupControls.find(v=>v.controlDocId == m.controlDocId);
                  // 如果没有找到匹配的控件
                  if(!matchControl){
                      // 将m添加到groupControls数组中
                      let controlItem = {
                          controlDocId:m.controlDocId,
                          id:m.controlDocId, //分组id，取文档id
                          propertyVoList:m.propertyVoList,
                          controlPageInfo:m.controlPageInfo,
                          pageCustomConfig:m.pageCustomConfig,
                          controls:buildResponseControl([{...m,propertyVo:undefined, signRuDocId:m.controlDocId,propertyVoList:m.propertyVoList}])
                      }
                      groupControls.push(controlItem)
                  }else{
                      // 将m添加到matchControl的controls数组中
                      matchControl.controls.push({
                          ...m,
                          controlDocId:m.controlDocId,
                          propertyVoList:m.propertyVoList,
                      })
                      // 对matchControl的controls数组进行处理
                      matchControl.controls = buildResponseControl(matchControl.controls)
                  }
              })
  
              // console.log(groupControls,'分组控件--')
  
              //将控件按页码配置按文档进行设置
              groupControls.map(item=>{
                  let currentDoc = docs.value.find(v=>v.id == item.controlDocId);
                  const { targets, maxWidth } = pageScaling(currentDoc.images);
                  item.controls.forEach(v=>{
                    v.offsetX = Number(v.offsetX)
                    v.offsetY = Number(v.offsetY)
                      if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(v.controlType)){
                          console.log(v,'测试控件分类-1111-')
                          item.controls = item.controls&&item.controls.filter(m => m.uid != v.uid);
                          if(v.controlPageInfo?.propertyType == 'page_config' && v.controlPageInfo?.propertyValue != 'custom'){
                              for (let i = 0; i < currentDoc.docPage; i++) {
                                  let unequalId = buildUUID() + '_' + i; 
                                  let shouldAddControl = false;
                                  const target = targets[i];
                                  const offsetWidth = (maxWidth - target.width) / 2;
  
                                  if (v.controlPageInfo.propertyValue === 'all') {
                                      shouldAddControl = true;
                                  } else if (v.controlPageInfo.propertyValue === 'odd_number' && (i + 1) % 2 !== 0) {
                                      shouldAddControl = true;
                                  } else if (v.controlPageInfo.propertyValue === 'even_number' && (i + 1) % 2 === 0) {
                                      shouldAddControl = true;
                                  }
                                  console.log(v.position.top,'控件位置')
  
                                  if (shouldAddControl) {
                                      item.controls.push({
                                          ...v,
                                        //   isBatch: true,  
                                          unequalId,
                                          propertyVoList:v.propertyVoList,
                                          uid: v.uid,
                                          signRuDocId: v.signRuDocId,
                                          signerId: v.signerId,
                                          controlClick: false,
                                          // position: {
                                          //     left: v.position.left,
                                          //     top: currentPositionReverse(v.offsetY, i),
                                          //     page: i,
                                          // },
                                          position: {
                                            left: v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width:offsetWidth + v.offsetX),
                                            top:  currentPositionReverse(v.offsetY, target),
                                            page: i,
                                          }
                                      });
                                  }
                              }
                          
                          }  else if(v.controlPageInfo?.propertyValue == 'custom'){
                              let pages = parseInputPages(v.pageCustomConfig.propertyValue.replace(/\s/g, ''));
                              pages.map(page => {
                                if(page <= currentDoc?.docPage){
                                  let unequalId = buildUUID(); 
                                  const target = targets[page-1];
                                  const offsetWidth = (maxWidth - target.width) / 2;
                                  item.controls.push({
                                      ...v,
                                    //   isBatch: true,
                                      uid: v.uid,
                                      signRuDocId: v.signRuDocId,
                                      signerId: v.signerId,
                                      propertyVoList:v.propertyVoList,
                                      controlClick: false,
                                      unequalId,
                                      // position: {
                                      //     left: v.position.left,
                                      //     top: currentPositionReverse(v.offsetY, page-1),
                                      //     page: page-1,
                                      // },
                                      position: {
                                          left: v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width:offsetWidth + v.offsetX),
                                          top:  currentPositionReverse(v.offsetY, target),
                                          page: page-1,
                                      },
                                  });
                                }
                              })
                          }
                      }
                  })
              })
              // console.log(groupControls,'分组后的控件--')
              //按文档进行控件设置
              for (let i = 0; i < docs.value.length; i++) {
                  let matchControl = groupControls.find(v=>v.controlDocId == docs.value[i].id)
                  setDocumentList(docs.value[i], matchControl?.controls || []);
              }
  
  
              // controlList.value = controlList.value.concat(allControls);
              if (unref(isDetail)) {
                  actions.value = []
              }
          }
      //获取参与方
      async function getSignerList(){
        let result = await getSigners({signRuId:signRuId});
        if(result){
          signerList.value = result;
        }
        
      }
        //文档切换
      async function handleDocChange(val){

        let matchDoc = documentList.value.find(item=>item.signRuDocId ==  val);
              nowDocument.value = matchDoc;
              docId.value = val;
              images.value = matchDoc.images;
              document.getElementsByClassName('doc-content')[0].scrollTop = 0;

      }

    
      //整理文档列表
      function setDocumentList(doc,controls){
        let machDoc = documentList.value.find(v => v.signRuDocId == doc.id)
        if (machDoc) {
            machDoc.activeControl = controls
        } else {
            const {targets,maxWidth} = pageScaling(doc.images);
            documentList.value.push({
                active:false,
                documentName: doc.name,
                annexFileId:"",
                targets:targets,
                signRuDocId:doc.id,
                maxWidth:maxWidth,
                activeControl: controls,
                pageSize: doc.docPage,
                images: cloneDeep(doc.images),
                imageAllHeight:targets[targets.length-1].transformHeight + targets[targets.length-1].height + (images.value.length * CanvasZoom.space),
                imageLoading: true
            })
        }
        nowDocument.value = documentList.value.find(v=>v.signRuDocId == docId.value);
        // controlMove.activeControl = buildResponseControl(controls);
        //receiveActive(signers.value);
      }


      // 构建文档控件
      function buildResponseControl(controls:any){
      	const controlTem:any = [];
      	if(controls && controls.length>0){
      		controls.forEach((item:any)=>{
            // let matchDoc = docs.value.filter(v =>v.id == item.signRuDocId)[0]
            // let { targets, maxWidth } = pageScaling(matchDoc.images);
            // let target = targets[item.page];
            // let offsetWidth = (maxWidth - target.width) / 2;

      			const basaeColtrol = getColtrolByType(item.controlType);
      			controlTem.push({
                    id :item.id,
                    // id :parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    uid : item.id,
                    propertyVoList:item.propertyVoList,
                    // widgetList:paramBuildWidgets(item.widgetList,target,offsetWidth),
                    controlPageInfo:item.controlPageInfo,
                    pageCustomConfig:item.pageCustomConfig,
                    icon:basaeColtrol.icon,
                    name:item.name,
                    title:basaeColtrol.title,
                    controlType:item.controlType,
                    originType:item.originType,
                    placeholder:item.placeholder,
                    value:item.value,
                    controlClick:controlChangeFlag.value == 'not_necessary'?true:false,
                    offsetX:item.offsetX,
                    fontSize:item.fontSize,
                    offsetY:item.offsetY,
                    page:item.page,
      				      zoom:basaeColtrol.zoom,
                    delete:controlChangeFlag.value == 'not_necessary'?true:false,
                    width:item.width,
                    height:item.height,
      				      move:controlChangeFlag.value == 'not_necessary'?true:false,
                    signature:'',
                    disabled:unref(isDetail)?true:false,
                    required:item.isRequired == 1,
                    attr:basaeColtrol.attr,
                    signRuDocId:item.signRuDocId,
                    colorIndex:7,
                    signerId:item.signerId,
                    signerType:item.signerType,
                    format:item.format,
                    style:{
                      fontSize:parseInt(item.fontSize),
                      fontFamily:item.fontFamily,
                      textAlign:item.textAlign,
                    },
                    size:{
                      width:parseInt(item.width) || basaeColtrol.width,
                      height:parseInt(item.height) || basaeColtrol.height,
                      minWidth:basaeColtrol.size.minWidth,
                      minHeight:basaeColtrol.size.minHeight,
                    },
                    position:{
                      left: parseInt(item.offsetX),
                      top: parseInt(item.offsetY),
                      page:parseInt(item.page),
                    },
                    user:{}
      			})
      		})
      	}
      	return controlTem;
      }

      const controlMove:any = reactive({
      	activeControl:[],
      	controlList:[],
      	disabled:false,
      	activeDom:{},
      	elementMove: {
      		id:"",
      		controlClick:false,
      		position:{
      			left:0,
      			top:0
      		},
      		size:{
      			width:0,
      			height:0,
      			minWidth:0,
      			minHeight:0,
      		},
      	},
      	nowPoint:{
      		x:0,
      		y:0
      	},
      });

      
        //控件设置应用于选定文档的指定页码
        function handleSetControl(docKeys, element) {
              // if (element.isBatch) return;
              //将当前点击的控件设置为批量控件
              element.isBatch = true;
              console.log(docKeys,element, documentList,'签约文档---') 
              //根据页面配置固化propertyVoList已知项
              if(element.oddEventType == 'custom'){
                  element.propertyVoList = [
                      {
                          controlId: element.id,
                          id:'page_config_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                          propertyType:"page_config",
                          propertyValue:'custom',
                      },
                      {
                          controlId: element.id,
                          id:'page_config_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                          propertyType:"custom",
                          propertyValue:element.custom,
                      }
                  ];
              }else{
                  // 重置propertyVoList
                  element.propertyVoList = [
                      {
                          controlId: element.id,
                          id:'page_config_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                          propertyType:"page_config",
                          propertyValue:element.oddEventType,
                      },
                  ];
              }
                //更新各个文档的propertyVoList
                documentList.value.map(v => {
                    if (docKeys.includes(v.signRuDocId)) {
                        // element.propertyVoList = element.propertyVoList.filter(v => v.propertyType !== 'relation_doc');
                        element.propertyVoList.push(
                            {
                                controlId: element.id,
                                id:'relation_doc_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                                propertyType:"relation_doc",
                                propertyValue:v.signRuDocId,
                            }
                        )
                    }
                })

                let pages:number[] = []
                documentList.value.map(v => {
                    if (element.oddEventType == 'custom') {
                      pages = parseInputPages(element.custom.replace(/\s/g, ''));
                    }else{
                      for (let i = 0; i < v.pageSize; i++) {
                        if (element.oddEventType == 'all') {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 != 0)) {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 == 0)) {
                          pages.push(i)
                        }
                      }
                    }
                })
                const minTarget  = findMinImageSize(documentList.value, docKeys,Array.from(new Set(pages)))
                minTargetInfo.value = minTarget;
                const elementTarget = nowDocument.value.targets[element.position.page];
                const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;
                const elementOffsetX = element.position.left - elementOffsetWidth;
                const elementOffsetY = currentPosition(element.position.top,elementTarget);

                documentList.value.map(v => {
                 
                  if (docKeys.includes(v.signRuDocId)) {
                      v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
                      const { targets, maxWidth } = v;
                      // console.log(element.uid,  v.signRuDocId,v.images,v.activeControl.length,'当前文档id,页数,控件')
                      if (element.oddEventType == 'custom') {
                          let pages = parseInputPages(element.custom.replace(/\s/g, ''));
                          const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                          pages.map(page => {
                               if(page <= v.pageSize){
                                  v.activeControl.push({
                                  ...element,
                                  isBatch:(pages.length>1 || docKeys.length>1)?true:false,
                                  // offsetX: element.position.left,
                                  // offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                  signRuDocId:v.signRuDocId,
                                  signerId:element.signerId,
                                  minTarget,
                                  controlClick: false,
                                  uid: element.uid || frontUid,
                                  controlPageInfo:{
                                      controlDocId:v.signRuDocId,
                                      propertyType:"page_config",
                                      propertyValue:'custom',
                                      id:element.uid + '_page_config',
                                      controlId:element.uid 
                                  },
                                  position: {
                                      left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:page - 1}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                      top: recaculateBatchControlPosInPage({...element,position:{...element.position,page:page - 1}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                                      page: page -1 ,
                                  },
                              })
                            }else{
                                //超出后删除并去重
                                // deleteIdList.value.push(element.id)
                                // deleteIdList.value = [...new Set(deleteIdList.value)]
                            }
                          })
                      }else{
                          const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                          for (let i = 0; i < v.pageSize; i++) {
                              if (element.oddEventType == 'all') {
                                const target = targets[i];
                                const offsetWidth = (maxWidth - target.width) / 2;
                                  v.activeControl.push({
                                      ...element,
                                      minTarget,
                                      isBatch: (v.images.length>1 || docKeys.length>1)?true:false,
                                      signRuDocId:v.signRuDocId,
                                      signerId:element.signerId,
                                      // offsetX: element.position.left,
                                      // offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                      controlClick: false,
                                      uid: element.uid || frontUid,
                                      controlPageInfo:{
                                          controlDocId:v.signRuDocId,
                                          propertyType:"page_config",
                                          propertyValue:element.oddEventType,
                                          id:element.uid + '_page_config',
                                          controlId:element.uid 
                                      },
                                      position: {
                                        left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                        top: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                                        page: i,
                                      },
                                  })
                              }
                              if (element.oddEventType == 'odd_number') {
                                  if ((i + 1) % 2 != 0) {
                                    const target = targets[i];
                                    const offsetWidth = (maxWidth - target.width) / 2;
                                      v.activeControl.push({
                                          ...element,
                                          minTarget,
                                          isBatch: (v.images.length>2 || docKeys.length>1)?true:false,
                                          uid: element.uid || frontUid,
                                          signRuDocId:v.signRuDocId,
                                          signerId:element.signerId,
                                          controlClick: false,
                                          offsetX: element.position.left,
                                          offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                          controlPageInfo:{
                                              controlDocId:v.signRuDocId,
                                              propertyType:"page_config",
                                              propertyValue:element.oddEventType,
                                              id:element.uid + '_page_config',
                                              controlId:element.uid 
                                          },
                                          position: {
                                            left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                            top:  recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                            page: i,
                                          },
                                      })
                                  }
                              }
                              if (element.oddEventType == 'even_number') {
                                  if ((i + 1) % 2 == 0) {
                                      v.activeControl.push({
                                          ...element,
                                          minTarget,
                                          isBatch: (v.images.length>3 || docKeys.length>1)?true:false,
                                          offsetX: element.position.left,
                                          offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                          controlClick: false,
                                          signerId:element.signerId,
                                          signRuDocId:v.signRuDocId,
                                          uid: element.uid || frontUid,
                                          controlPageInfo:{
                                              controlDocId:v.signRuDocId,
                                              propertyType:"page_config",
                                              propertyValue:element.oddEventType,
                                              id:element.uid + '_page_config',
                                              controlId:element.uid 
                                          },
                                          position: {
                                              left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                              top:  recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                              page: i,
                                          },
                                      })
                                  }
                              }
                          
                          }
                      }
                     
                  }else{
                      //其他文档的当前控件删除掉
                      v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
                      //自动切换到另一有该控件的文档
                      // if(v.signRuDocId != docId.value){
                      //     handleDocChange(v.signRuDocId)
                      // }
                  }
              })
  
              //更新当前文档
              nowDocument.value = documentList.value.find(v => v.signRuDocId == docId.value);
              console.log(documentList.value, '所有文档')
          }

        //控件日期格式
        function signDateFormat(element){
            documentList.value.map(v => {
                v.activeControl.map(item => {
                    if(item.controlType == 'sign-date'){
                        item.format = element.format;
                    }
                })
            })
        }
        // 重新计算控件位置
        function refreshControlPosition(element, moveOpt) {
            if (!moveOpt) return;
            console.log("moveOpt:",moveOpt);
            let pages:number[] = []
            let docKeys:string[] = []
                documentList.value.map(v => {
                    if (element.oddEventType == 'custom') {
                      pages = parseInputPages(element.custom.replace(/\s/g, ''));
                    }else{
                      for (let i = 0; i < v.pageSize; i++) {
                        if (element.oddEventType == 'all') {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 != 0)) {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 == 0)) {
                          pages.push(i)
                        }
                      }
                    }
                })
            docKeys =   element.propertyVoList.filter(item => item.propertyType === "relation_doc").map(item => item.propertyValue);
            const minTarget  = findMinImageSize(documentList.value, docKeys, Array.from(new Set(pages)))
            const elementTarget = nowDocument.value.targets[element.position.page];
            const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;
            const elementOffsetX = element.position.left - elementOffsetWidth;
            const elementOffsetY = currentPosition(element.position.top,elementTarget); 
            documentList.value.map(v => {
                v.activeControl.map(item => {
                    //过滤掉当前移动的控件计算其他复制的控件的位置
                    if (item.uid == element.uid && !item.controlClick) {
                       const target = v.targets[item.position.page];
                        const pageElementOffsetWidth =  ( v.maxWidth - target.width) / 2;
                        item.position = {
                            left: element.controlType == 'chop-stamp' ? (pageElementOffsetWidth + target.width) - element.size.width : ((pageElementOffsetWidth + elementOffsetX) >= ((pageElementOffsetWidth + target.width) - element.size.width)?(pageElementOffsetWidth + target.width) - element.size.width : pageElementOffsetWidth + elementOffsetX),
                            top: recaculateBatchControlPosInPage(item, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                            page: item.position.page,
                        }
                    }
                })
            })
            nowDocument.value = documentList.value.find(v => v.signRuDocId == docId.value)
        }
       //刷新控件状态
       function refreshControls(element:any,isAdd:boolean){ 
      	if(signers.value){
      		controlMove.activeControl = nowDocument.value.activeControl.filter((item:any) => {
      			return item.user.index == signers.value.index;
      		})
      	}else{
      		controlMove.activeControl = nowDocument.value.activeControl;
      	}
        controlMove.activeControl = nowDocument.value.activeControl;
        if(element.controlType == 'chop-stamp' && isAdd){
                const minImageHeightItem = nowDocument.value.images.reduce((minItem, currentItem) => {
                  return parseFloat(currentItem.imageHeight) < parseFloat(minItem.imageHeight) ? currentItem : minItem;
                });
                const elementTarget = nowDocument.value.targets[element.position.page];
                let elementOffsetY = currentPosition(element.position.top,elementTarget);
                if(elementOffsetY > Number(minImageHeightItem.imageHeight) - element.size.height){
                  elementOffsetY = Number(minImageHeightItem.imageHeight) - element.size.height;
                  element.position.top = currentPositionReverse(elementOffsetY,elementTarget);
                }
                for(let i=0;i<nowDocument.value.pageSize;i++){
                  const target = nowDocument.value.targets[i];
                  const offsetWidth = ( nowDocument.value.maxWidth - target.width) / 2;
                    let unequalId = buildUUID() + '_' + i; 
                    if(i != element.position.page){
                        nowDocument.value.activeControl.push({
                            ...element,
                            unequalId,
                            position:{
                                left:  (offsetWidth + target.width) - element.size.width, 
                                top:  currentPositionReverse(elementOffsetY, target),
                                page:i,
                            }
                        })
                    }
                }
            }
      }
      // 设置同类型控件签名图片和大小
      function setSignatureControlImage() {
        if(currentControl.value.signature){
          let base64Size = getBase64Size(currentControl.value.signature);
          base64Size = reCaculatImgSize(base64Size)
          updateAllSignatureImg(nowDocument.value.activeControl, currentControl.value.signature, base64Size)
        }
       
      }
      // 设置同类型控件签章图片和大小
      function setSealControlImage() {
        // console.log(signControlInfo.value,'signControlInfo.value.sealId---');
        // console.log(currentControl.value,'currentControl.value.sealId');
        // console.log(entSealId.value,'entSealId.value---');
        // console.log(sealAnnexId.value,'sealAnnexId.value---');
        if((unref(signControlInfo).sealId || entSealId.value ) && currentControl.value.sealId){
           updateAllSealImg(nowDocument.value.activeControl, unref(signControlInfo).sealId || entSealId.value, sealAnnexId.value, newSealSize.value)
        }
      }
      
        //控件拖拽完成事件
        function controlsDragOver(e:any,element:any) {
          controlMove.elementMove = element;
          refreshControls(element,true);
          if(element.controlType == 'signature'){
            setSignatureControlImage()
          }
          if(element.controlType == 'seal' || element.controlType == 'chop-stamp'){
            setSealControlImage()
          }
        }
        //删除控件
        function controlDelete(element:any){
            element.controlClick = false;
            // entSealId.value = '';
            // privateSeal.value = '';
            console.log("delete control:",element,nowDocument.value.activeControl)
            deleteIdList.value.push(element.id);
            documentList.value.map(v => {
                v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
            })
            nowDocument.value = documentList.value.find(v => v.signRuDocId == docId.value)
            refreshControls(element,false);
      }
      //控件点击操作
      function controlMousedown(element:any){
        // console.log(element,'点击控件-------')
      	// controlMove.elementMove = element;
      	nowDocument.value.activeControl.forEach((item:any)=>{
      		item.controlClick = false
      	})
      	element.controlClick = true;
      }
     
      //文档控件滚动定为
      function handleScroll(doc,el){
        console.log(doc,el)
        // document.getElementsByClassName('scrollbar__wrap')[1].scrollTo(0,el.position.page * 1147);
        if(doc.signRuDocId != docId.value){
          handleDocChange(doc.signRuDocId)
          setTimeout(()=>{
            // document.getElementsByClassName('doc-content')[0].scrollTo(0,el.position.page * 1147);
            document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
          },100)  
        }else{
          // document.getElementsByClassName('doc-content')[0].scrollTo(0,el.position.page * 1147);
          document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
        }
        documentList.value.map(v => {
              v.activeControl.forEach(item => {
                  item.controlClick = false
                  item.focus = false;
                  item.zoom = false;
                  if(item.uid == el.uid){
                    item.controlClick = true
                    item.focus = true;
                  }
              });
            })
        }
     // 添加签章
     function handleAddSeal() {
        let scrollTop = document.getElementsByClassName('doc-content')[0].scrollTop;
        let uid = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(10)
        const zoom = doContent.value.zoom;
        let scrollPage = 0;
        let targets = nowDocument.value.targets;
        targets.forEach((v)=>{
          let zoomImgHeight = (v.transformHeight + 16 * v.page)* zoom / 100;
          let zoomScroll = scrollTop
          if( zoomScroll > zoomImgHeight && zoomScroll < zoomImgHeight + v.height * zoom/100){
            scrollPage = v.page
          }
        })
        let sealControl: any = controlListSource.filter((v: any) => v.type == 'seal')[0];

        const baseTop = targets[scrollPage].transformHeight;
        const elementTarget = nowDocument.value.targets[scrollPage];
        const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;

        sealControl = {
            ...sealControl,
            position: {
                left: elementOffsetWidth + 130,
                top: 180 + baseTop + 16 * scrollPage,
                page: scrollPage
            },
            title: '签章位置',
            colorIndex: 7,
            delete: true,
            showPopover:false,
            signerId: signControlInfo.value.signerId,
            countIndex: sealCount.value + signatureCount.value + signDateCount.value,
            setting: false,
            signRuDocId: docId.value,
            // annexId: sealAnnexId.value,
            sealId: currentControl.value.sealId,
            propertyVoList : [
                {
                    controlId: uid,
                    id:'relation_doc_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"relation_doc",
                    propertyValue:docId.value,
                },
                {
                    controlId:uid,
                    id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"page_config",
                    propertyValue:'custom',
                },
                {
                    controlId:uid,
                    id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"custom",
                    propertyValue:scrollPage+1,
                }
            ],
            originType: 3,
            size: {
                width: sealControl.size.width,
                height: sealControl.size.height,
            },
            controlType: 'seal',


        }
        signControlSize.value = {
          width:sealControl.size.width,
          height:sealControl.size.height,
        }
        sealCount.value += 1;
        nowDocument.value.activeControl.push(sealControl);
        controlList.value.push(sealControl)

    }
        // 添加签名
        function handleAddSignature() {
            let scrollTop = document.getElementsByClassName('doc-content')[0].scrollTop;
            const zoom = doContent.value.zoom;
            let scrollPage = 0;
            let targets = nowDocument.value.targets;
            targets.forEach((v)=>{

              let zoomImgHeight = (v.transformHeight + 16 * v.page)* zoom / 100;
              let zoomScroll = scrollTop
              if( zoomScroll > zoomImgHeight && zoomScroll < zoomImgHeight + v.height * zoom/100){
                scrollPage = v.page
              }
            })
           
            let signControl: any = controlListSource.filter((v: any) => v.type == 'signature')[0];
            
            let uid = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(10)
            let unequalId = buildUUID()
           
            const baseTop = targets[scrollPage].transformHeight;
            const elementTarget = nowDocument.value.targets[scrollPage];
            const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;

            signControl = {
                ...signControl,
                position: {
                    left: elementOffsetWidth + 130,
                    top: 180 + baseTop + 16 * scrollPage,
                    page: scrollPage
                },
                title: '签名位置',
                controlClick: false,
                colorIndex: 7,
                countIndex: sealCount.value + signatureCount.value + signDateCount.value,
                setting: false,
                unequalId,
                delete: true,
                showPopover:false,
                originType: 3,
                signerId: signControlInfo.value.signerId,
                propertyVoList : [
                    {
                        controlId: uid,
                        id:'relation_doc_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        propertyType:"relation_doc",
                        propertyValue:docId.value,
                    },
                    {
                        controlId:uid,
                        id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        propertyType:"page_config",
                        propertyValue:'custom',
                    },
                    {
                        controlId:uid,
                        id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        propertyType:"custom",
                        propertyValue:scrollPage+1,
                    }
                ],
                // signature: singatureBae64.value,
                signRuDocId: docId.value,
                size: {
                    width: signControl.size.width,
                    height: signControl.size.height,
                },
                controlType: 'signature',


            }
            signControlSize.value = {
              width:signControl.size.width,
              height:signControl.size.height,
            }
            signatureCount.value += 1;
            nowDocument.value.activeControl.push(signControl);
            controlList.value.push(signControl)

        }
      //选择签署日期格式
      function handleSignDateFormat(){
        signDateElement.value.format =  signDateType.value;
        signDatevisible.value = false;
        currentControl.value.format = signDateType.value;
        signDateType.value = '';
      }
      //判断是否指定签署位置
      function checkHasSignPos(type){
        let flag = false;
        let controls =  documentList.value.flatMap(item => item.activeControl);
        //指定位置originType==2 或者拖拽空间originType==3 有一个即可签署
        let signControl = controls.filter(v=>(v.originType == 1 || v.originType == 3 || v.originType == 2 ) && (v.controlType != 'sign-date'))
        if(signControl.length){
          flag = true;
        }else{
          flag = false;
        }
        if(!flag){
            if(type==1){
                handleAddSignature()
            }else{
                handleAddSeal()
            }
        }
        return flag;
      }

      //控件点击
      function signControlClick(e,element){
        if(unref(isDetail)) return;
        currentControl.value = {
          ...currentControl.value,
          ...element,
          signature:currentControl.value.signature
        };
        // signControlSize.value = {
        //       width:element.size.width,
        //       height:element.size.height,
        // }
        if(element.controlType=='seal' || element.controlType=='chop-stamp'){
          //类型为企业签章 且签章id存在直接获取签章图片，签章id不存在弹出签章图片列表
          if(signControlInfo.value.type==2 && !signControlInfo.value.sealId){
            openModal(true,{
                isUpdate:false,
                record:{

                }
            })
          }else{
            checkSignSeal()
          }
          return
        }else if(element.controlType=='sign-date'){
          signDateElement.value = element;
          signDatevisible.value = true;
          signDateType.value = element.format;
          return
        }else{
          // getSignSignature()
          openSignatureModal(true,{
            isUpdate:false,
            recoed:{

            }
          })
          return

        }
      }
      // 选取接收方企业签章
      async function handleSetSeal(info){
        // console.log(info,"handleSetSeal---")
        entSealId.value = info.sealId;
        currentControl.value.sealId = info.sealId; 
        isSignText.value = false;
        sealAnnexId.value = info.annexId;
        getImageIdSize(info.annexId).then(res=>{
          updateAllSealImg(nowDocument.value.activeControl,info.sealId,info.annexId, res)
        })
        closeModal();
        signPosition('company');
      }
      //获取图片流宽高
      function getImageIdSize(imgId){
        let sealSize = {
          width:120,
          height:120
        }
        let newSealSize = {
          width:120,
          height:120
        }
        // 图片地址
          let img_url = baseUrl.value + '/file/downloadFileStream/' + imgId
          // 创建对象
          var img = new Image()	
          // 改变图片的src
          img.src = img_url;	
          return new Promise((resolve, reject) => {
            // 加载完成执行
            img.onload = function(){
              if((sealSize.width / sealSize.height) == (img.width / img.height)){
                newSealSize =  sealSize
                
              }else if((sealSize.width / sealSize.height) < (img.width / img.height)){
                newSealSize =  {
                  width: sealSize.width,
                  height: img.height / (img.width / sealSize.width)
                }
                
              }else if((sealSize.width / sealSize.height) > (img.width / img.height)){
                newSealSize =  {
                  width: img.width / (img.height / sealSize.height),
                  height: sealSize.height
                }
              }
              resolve(newSealSize)
            }
          })
      }

      //校验发起方签章
      async function checkSignSeal(){
        // verifySignSeal
        let result = await verifySignSeal({sealId:unref(signControlInfo).sealId});
        if(result.data.code==200){ 
            entSealId.value = signControlInfo.value.sealId;
            currentControl.value.sealId = signControlInfo.value.sealId; 
            sealAnnexId.value =  result.data.result;
            getImageIdSize(result.data.result).then(res=>{
              updateAllSealImg(nowDocument.value.activeControl, unref(signControlInfo).sealId, result.data.result, res)
            })
            isSignText.value = false;
            signPosition('company');
        }else{
          createWarningModal({
            title: '使用印章', 
            content: result.data.message,
            okText:'取消',
            iconType: 'warning',
            onCancel(){

            }
            
          })
        }
      }
      //签名图片大小计算
      function reCaculatImgSize(orisignSize){
        signControlSize.value = {
          width:112,
          height:52
        }
        if((signControlSize.value.width / signControlSize.value.height) == (orisignSize.width / orisignSize.height)){
          return orisignSize
        }
        if((signControlSize.value.width / signControlSize.value.height) < (orisignSize.width / orisignSize.height)){
          return {
            width: signControlSize.value.width,
            height: parseInt(orisignSize.height / (orisignSize.width / signControlSize.value.width))
          }
        }
        if((signControlSize.value.width / signControlSize.value.height) > (orisignSize.width / orisignSize.height)){
          return {
            width: parseInt(orisignSize.width / (orisignSize.height / signControlSize.value.height)),
            height: signControlSize.value.height
          }
        }

      }
      //选择个人签名
      function handleSetSignature(row){
        privateSeal.value = row.base64.split('base64,')[1];
        let base64Size = getBase64Size(row.base64);
        base64Size = reCaculatImgSize(base64Size)
        currentControl.value.signatureId = row.annexId;
        currentControl.value.signature = row.base64;
        isSignText.value = false;
        updateAllSignatureImg(nowDocument.value.activeControl, row.annexId, base64Size);
        closeSignatureModal();
        signPosition('person');
        
      }
      //设置手写个人签名
      function handleWriteSignature(base64){
        let base64Size = getBase64Size(base64);
        // console.log(base64Size,'图片大小')
        base64Size = reCaculatImgSize(base64Size)
        privateSeal.value = base64.split('base64,')[1];
        currentControl.value.signature = base64;
        currentControl.value.signatureId = '';
        isSignText.value = false;
        updateAllSignatureImg(nowDocument.value.activeControl, base64,  base64Size);
        signPosition('person');
      }
      //更新所有签名图片
      function updateAllSignatureImg(controls, imgStr, base64Size){
        let allControls = documentList.value.flatMap(item => item.activeControl);
        allControls.map(v=>{
          if(v.controlType=='signature'){
            //根据签名文件id或手写签名图片赋值
            if(imgStr.indexOf('base64') > 0){
              v.signature = imgStr;
              v.signatureId = '';
            }else{
              v.signature = '';
              v.signatureId = imgStr;
            }
            v.size = base64Size;
          }
        })
      }

      function signPosition(signerType: string) {
        // 定义控件优先级顺序
        const controlPriority = signerType === 'person' 
          ? ['signature'] 
          : ['seal', 'chop-stamp'];

        // 在当前文档中查找控件
        const currentDocControls = nowDocument.value.activeControl || [];
        for (const controlType of controlPriority) {
          const targetControl = currentDocControls.find((item: any) => item.controlType === controlType);
          if (targetControl) {
            scrollToControl(targetControl);
            return;
          }
        }

        // 如果当前文档没有找到，在所有文档中查找
        const allControls = documentList.value.flatMap((item: any) => item.activeControl || []);
        for (const controlType of controlPriority) {
          const targetControl = allControls.find((item: any) => item.controlType === controlType);
          if (targetControl) {
            handleDocChange(targetControl.signRuDocId).then(() => {
              setTimeout(() => {
                scrollToControl(targetControl);
              }, 100);
            });
            return;
          }
        }
      }

      // 滚动到指定控件位置的辅助函数
      function scrollToControl(control: any) {
        const element = document.getElementsByClassName('pos-' + control.position.top)[0];
        if (element) {
          element.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
      }

      //更新所有签章图片
      function updateAllSealImg(controls,sealId,annexId,sealSize?:any){
        let allControls = documentList.value.flatMap(item => item.activeControl);
        allControls.map(v=>{
          if(v.controlType=='seal' || v.controlType=='chop-stamp'){
            v.sealId = sealId;
            v.annexId = annexId;
            if(sealSize&&sealSize.width){
              newSealSize.value = {
                width: sealSize.width,
                height: sealSize.height,
              }
              v.size = {
                
              }
              v.size.width = sealSize.width;
              v.size.height = sealSize.height;
            }
          }
        })
      }
      //更新证书
      async function updateCertificate(){
        compState.loading = true;
        let result = await getUpdateCertificate({ ...certificateInfo.value });
        console.log(result,'更新证书')
        if(result.data.code==200){
          compState.loading = false;
          signCertificateVisible.value = false;
          authCertificateVisible.value = true;
          authCerInfo.value = result.data.result;
        }else{
          compState.loading = false;
          signCertificateVisible.value = true;
          authCertificateVisible.value = false;
          personalAuth.value = true;
        }
      }

      //继续任务处理
      function handleGoNextTask() {
        if (taskInfo.value.taskType == 'sign') {
            router.push({
                path: '/contract/sign',
                query: {
                    signRuId: taskInfo.value.ruId,
                    taskId: taskInfo.value.taskId,
                    __full__:'',
                    callbackPage:callbackPage
                }
            })
            setTimeout(() => {
                location.reload()
            }, 500)
            
        }
        if (taskInfo.value.taskType == 'write') {
            router.replace({
                path: '/contract/params',
                query: {
                    signRuId: taskInfo.value.ruId,
                    taskId: taskInfo.value.taskId,
                    __full__:''

                }
            })
            setTimeout(() => {
                location.reload()
            }, 500)
        }
      }
        //暂不处理业务
        function handleNoTask() {
            taskVisible.value = false;
            router.push('/dashboard/workbench')
        }
      //个人实名认证
      async function handleAuth(){
          // let params = {
          //   departId:certificateInfo.value.departId || signerCaAuthInfo.value.departId  //个人未实名时获取signerCaAuthInfo.value.departId 
          // }
          // let authInfo = window.appInfo.auth_app_info;
          // let newToken = await getAppTokenByAuthToken(params);
          // let appInfo = {
          //     token:newToken,
          //     appCode:authInfo.appCode,
          //     appId:authInfo.appId,
          //     departId:certificateInfo.value.departId,
          //     callbackPage:callbackPage,
          //   }
          //   let paramsString = new URLSearchParams(appInfo).toString();
          //   window.open(authInfo.url + '/#/personal' + '?' + paramsString + '&type=close','_self')
      }
      // 取消签署
      function handleUnSign(){
        signCertificateVisible.value = false;
      }
      // 个人无证书 
      async function handlePersonAuth(){
        //已经开通个人空间
        if(certificateInfo.value.departId){
          handleAuth()
        }else{
          //无个人空间去开通
          let result =  await openPersonalTenant({});
          if(result){
            certificateInfo.value.departId = result;
            handleAuth()
          }
        }
      }

      async function checkPersonalTenant(){
        const tenantInfo = userStore.getTenantInfo;
        // 在企业身份下，判断个人租户是否开通开通
        if(tenantInfo.tenantType === '1' &&  !certificateInfo.value.signerTenantId){
          await openPersonalTenant({});
        }
      }

      //人脸识别操作人未开通个人账号
      async function faceOpenPersonalAccount(){
          let result =  await openPersonalTenant({});
          if(result){
              msg.success('开通成功')
              needOpenPersonlAccountVisible.value = false;
              let result = await getConfirmType({operateType:'submit_sign'})
              if(result){
                  confirmTypeInfo.value = result;
                  setTimeout(()=>{
                      handleFace()
                  },500)
              }
              
          }
              
      }
      //使用平台防篡改证书开通个人账号
      async function  openPersonalAccount(){
        let result =  await openPersonalTenant({});
        if(result){
          msg.success('开通成功')
          signCertificateVisible.value = false;
          handleSubmitSign();
        }
      }
      const ignoreControl = ref(false);
      //点击签署
      function handleSign(){
        // console.log("handleSign",isSignText.value);
        // 判断是否选择了设置了印章或个人签名
        if (!isSignText.value) {
          // handleSubmitSign()
          // goSign();
          checkDocumentSignControl();
          if (!ignoreControl.value && submitCheck.value.status === 0) {
            let errorText = '';
            submitCheck.value.errorDocs.forEach((item: any) => {
              errorText += `《${item.documentName}》`;
            });
            Modal.confirm({
              iconType: 'warning',
              title: '签署文件未全部指定签署位置',
              content: `文件${errorText}未指定签署位置，请检查确认！`,
              okText: '继续签署',
              cancelText: '取消',
              onOk: () => {
                // submitCheck.value.status = -1;
                ignoreControl.value = true;
                handleSign();
              },
            });
            return;
          }
          // 判断是否是无实名认证签署
          if(personalSignAuth.value == 'not_required'){
            // 弹窗提示
            createConfirm({
              iconType: 'warning',
              width: '600px',
              title: '非实名认证签署风险告知书',
              content: '<div style="font-size: 14px; line-height: 1.6; margin-top: 20px; margin-right: 38px;">' +
                '<p style="color: #333">本次签署无需进行实名认证。如您同意，系统将使用平台防篡改证书（非CA权威数字证书）为您完成电子签名。</p>' +
                '<p style="color: red"><strong>重要声明：</strong>平台防篡改证书仅能确保文件在签名后不被篡改，不具备《电子签名法》规定的法律效力。</p>' +
                '<p style="color: #333;">点击“同意，继续签署”即代表您已知悉非实名认证签署的相关风险，并同意以该方式完成本次签署。</p>' +
                '<p>如对实名认证要求有疑议，请联系签署发起方。</p>' +
                '</div>',
              okText: '同意，继续签署',
              cancelText: '暂不签署',
              onOk() {
                submitSignData();
              }
            })
          } else {
            submitSignData();
          }
          
          
        }else{
          //类型为企业签章
          if(signControlInfo.value.type==2){
            checkHasSignPos(2);
            if(signControlInfo.value.sealId){
                checkSignSeal()
            }else{
                openModal(true,{
                  isUpdate:false,
                  record:{}
                })
            }
          }else{
            checkHasSignPos(1);
            openSignatureModal(true,{
              isUpdate:false,
              recoed:{}
            })
          } 
        }
      }
      // const ignoreControl = ref<boolean>(false);
      function checkDocumentSignControl(){ 
        submitCheck.value.errorDocs = [];
        documentList.value.forEach((doc: any) => {
          if (!doc.activeControl || doc.activeControl.length === 0) {
            submitCheck.value.errorDocs.push(doc);
          }
        });
        submitCheck.value.status = submitCheck.value.errorDocs.length === 0 ? 1 : 0;
        // console.log("documentList.value",documentList.value,submitCheck.value);
      }
      //控件合并
      function mergedDataFn(inputData){
          const mergedObjects = {};
  
          inputData.forEach(obj => {
            const id = obj.id;
            console.log(id,'控件id')
            //只传签署控件
            if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(obj.controlType)){
                if (!mergedObjects[id]) {
                // 如果 mergedObjects 中不存在该 ID，则创建一个新的对象
                    mergedObjects[id] = { ...obj };
                } else {
                    // 如果已经存在该 ID，则将属性合并到现有对象中
                    Object.assign(mergedObjects[id], obj);
                }
            }
    
           
          });
          // 将合并后的对象转换为数组
          const resultArray = Object.values(mergedObjects);
  
          return resultArray;
        }

      //继续签署
     function goSign(){
      getConfirmTypeForAction({operateType:'submit_sign'}).then(async(res)=>{
          confirmAction.value = 'sign';
          confirmTypeInfo.value = res;
          if((confirmTypeInfo.value.confirmType == 'password' || confirmTypeInfo.value.confirmType == 'phone_email' || confirmTypeInfo.value.confirmType == 'double') && !willResult.value){
            openWishModal(true,{
              isUpdate:false,
              record:{
                confirmType:confirmTypeInfo.value.confirmType,
                orderNo:orderNo.value
              }
            })
            return
          }
          submitSignData()
        })
         
      }
      //签署数据提交
     async function submitSignData(){
          let paramsControl:any = [];
          let controls =  documentList.value.flatMap(item => item.activeControl);
          // console.log(controls,'所有控件-----')
          controls.map(item=>{
            const matchDoc = documentList.value.filter(v =>v.signRuDocId ==item.signRuDocId)[0];
            const targets = matchDoc.targets;
            const target = targets[item.position.page];
            const offsetWidth = (matchDoc.maxWidth - target.width) /2;
            item.signerType = signerList.value.find(m=>m.id == item.signerId)?.signerType;
            paramsControl.push({
                  "propertyVoList": item.propertyVoList,
                  "controlDocId":item.controlDocId,
                  "fontFamily": item.style.fontFamily,
                  "fontSize": item.fontSize,
                  "name": item.name,
                  "offsetX": item.position?.left - retainDecimals(offsetWidth),
                  "offsetY": currentPosition(item.position.top,target),//计算成为每页的位置
                  "pageWidth": target.width,
                  "pageHeight": target.height,
                  "page": item.position.page,
                  "placeholder": item.placeholder,
                  "signReDocId": item.signReDocId,
                  "relatedDocId": 0,
                  "signerId": item.signerId,
                  "signerType": item.signerType,
                  "relatedDocType": 0,
                  "required": item.required?1:2,
                  "interfaceParamName": item.interfaceParamName,
                  "textAlign": item.style.textAlign,
                  "signRuDocId": item.signRuDocId,
                  "controlType": item.controlType,
                  "value": item.value,
                  "width": item.size.width,
                  "height": item.size.height,
                  "written": 1,
                  "format":item.format ||  (item.controlType=='sign-date'?'yyyy年MM月dd日':''),
                  "id":item.id,
                  "uid":item.uid,
                  "tenantId": item.user.tenantId,
              })
            })
            signCertificateVisible.value = false;
            authCertificateVisible.value = false;
            // console.log(mergedDataFn(paramsControl),'提交的控件')
            // console.log(unref(deleteIdList),'删除的控件')
            
            // const callbackPage = `${location.origin}/#/wishCheck`;
            const callbackPageYd = `${location.origin}/#/wishCheck?orderNo=${orderNo.value}&signRuId=${signRuId}&callbackPage=${callbackPage}`;
            try{
              compState.loading = true;
              let result  = await submitSign({
                  signRuId:signRuId,
                  controlList:mergedDataFn(paramsControl),
                  deleteIdList:unref(deleteIdList),
                  entSealId:unref(entSealId),
                  privateSeal:unref(privateSeal),
                  signConfirmOrderNo:orderNo.value,
                  callbackPage:callbackPageYd,
              });
              // 敏感校验
              // eventHub.$emit('btnCallback',[closeSignPage])
              if(result){
                if(result.signConfirmUrl){
                  window.open(result.signConfirmUrl, '_self');
                }else{
                  msg.warning("未成功获取到意愿校验订单");
                }
                console.log(result,'签署完返回结果-----')
                // window.location.href = result.signConfirmUrl;
                setTimeout(()=>{
                    // handleFace()
                    compState.loading = false;
                },500)
              }else{
                compState.loading = false;
              }
            }catch (error) {
              compState.loading = false;
            }
      }
      //关闭签署页
      function closeSignPage(){
        compState.loading = false;
        createSuccessModal({
          title: '签署成功，点击关闭', 
          // content: '签署成功，点击关闭',
          okText:'确定',
          iconType: 'warning',
          onOk() {
            // window.history.go(-1);
            window.close();
          }
        })
      }
      //提交签署
      async function handleSubmitSign(){
            //验证证书
            compState.loading = true;
            personalAuth.value = false;
            let certificaResult  = await getVerifyCertificate({signRuId});
            console.log(certificaResult,'返回结果')

            if(certificaResult.data.code==200){
              compState.loading = false;
              certificateInfo.value = certificaResult?.data?.result;
              if(certificateInfo.value.tenantId){
                let authResult = await getTenantAuthStatus({tenantId: certificateInfo.value.tenantId})
                if(authResult.result==0){
                    tenantAuthInfo.value.authStatus = authResult.result;
                } else{
                    tenantAuthInfo.value.authStatus = authResult;
                }
                tenantAuthInfo.value.authStatus = authResult;
            }
            

                if(certificateInfo.value.certType == '2' || certificateInfo.value.certType == '3' ){

                  let info = '';
                        //holderType=1 个人 2 企业
                  if (certificateInfo.value.holderType == '1') {
                      if (certificateInfo.value.certType == '2') {
                          info = '您当前用于文件签署的证书是平台下发的测试证书，该证书非CA机构颁发，仅用于测试，签署后的文件不具备法律效力，请知悉！\n系统即将使用您的个人信息申请数字证书并用于本次文件签署，是否同意？'

                      }
                      if (certificateInfo.value.certType == '3') {
                          info = '系统即将使用您的个人信息申请数字证书并用于本次文件签署，是否同意？'

                      }

                  }
                  if (certificateInfo.value.holderType == '2') {

                      if (certificateInfo.value.certType == '2') {
                          info = '您当前用于文件签署的证书是平台下发的测试证书，该证书非CA机构颁发，仅用于测试，签署后的文件不具备法律效力，请知悉！\n系统即将使用当前企业信息申请数字证书并用于本次文件签署，是否同意？'

                      }
                      if (certificateInfo.value.certType == '3') {
                          info = '系统即将使用当前企业信息申请数字证书并用于本次文件签署，是否同意？'

                      }
                  }
                    createConfirm({
                      title: '温馨提示', 
                      content: info,
                      okText:'同意',
                      cancelText:'不同意',
                      iconType: 'warning',
                      wrapClassName:'sign-config-modal',
                      onOk() {

                        if (certificateInfo.value.certType == '2' || certificateInfo.value.certType == '3') {
                          goSign()
                        } else{
                          if( certificaResult?.data?.result.returnCode==4){
                            //证书有效
                            goSign()
                          }else{
                            signCertificateVisible.value = true;
                          }
                        }
                      
                      },
                      onCancel() {},
                    })
                }else{
                  if (certificateInfo.value.certType == '1') {
                      signCertificateVisible.value = true;
                      return
                  }
                  if( certificaResult?.data?.result.returnCode==4){
                      //证书有效
                      goSign()
                    }else{
                      signCertificateVisible.value = true;
                    }
                }
            }else{
              msg.warning(certificaResult.data.message)
              compState.loading = false;
            }
      }
      // 自定义的textarea数据  
      const textareaValue = ref('');  
      function handleInput(e){
        textareaValue.value = e.target.value;
      }
      // 拒签
      async  function handleRejectSign(){
        confirmAction.value = 'reject';
        textareaValue.value = '';
        Modal.confirm({
          title: '拒绝签署',
          content: createVNode(Input.TextArea, {
            text: textareaValue.value,
            placeholder:'请填写拒签原因，200字以内',
            maxlength:200,
            onChange: handleInput,
          }),
          onOk() {
            return new Promise((resolve,reject)=>{
              if(!textareaValue.value){
                    msg.warning('请输入原因')
                    reject('请输入原因')
                }else if(textareaValue.value.length < 1 || textareaValue.value.length>200){
                  msg.warning('字数不符合要求')
                  reject('字数不符合要求')
                }else{
                  submitRejectSignData()
                  resolve('success');
                }
            })
          }
        });
      }
       //拒绝签署提交数据
       async function submitRejectSignData(){
        createConfirm({
              title: '是否确认拒绝签署？', 
              okText:'确认',
              cancelText:"取消",
              iconType: 'warning',
              onOk() {
                compState.loading = true;
                rejectSign({signRuId:signRuId,signConfirmOrderNo:orderNo.value,comment:textareaValue.value}).then(result=>{
                  if(result){
                    createSuccessModal({
                      title: '拒签成功，点击确定关闭', 
                      okText:'确定',
                      iconType: 'warning',
                      onOk:async()=> {
                        if(callbackPage && typeof callbackPage == 'string'){ 
                          window.open(decodeURIs(callbackPage),'_self')
                        }else{
                            compState.loading = false;
                            window.history.go(-1);
                            window.close();
                        }
                      
                      }
                    })
                  }
                  compState.loading = false;
                });
                
              }
          })
      }
      function setSignControlType(info){
        //确保拿到signerId 再去请求控件
        signControlInfo.value = info;
        getDocs();
        checkCaAndAuthStatus()
      }
      async function handleCancel(){
        if(callbackPage && typeof callbackPage == 'string'){ 
          window.open(decodeURIs(callbackPage),'_self')
        }else{
          router.push({
            path: '/contract/doc'
          })
        }
      }
     
      //校验成功后继续签署
      function handleConfirmSuccess(info){
        confirmTypeInfo.value.confirmType = info.confirmType;
        if(confirmAction.value == 'sign'){
          willResult.value = true;
          submitSignData()
        }
        if(confirmAction.value == 'reject'){
          willResult.value = true;
          submitRejectSignData()
        }

      }
      
      return {
          docs,
          docId,
          needOpenPersonlAccountVisible,
          faceOpenPersonalAccount,
          height,
          actions,
          isDetail,
          defaultHasControl,
          nowDocument,
          documentList,
          signerList,
          controlsList,
          controlList,
          controlMove,
          controlMousedown,
          controlsDragOver,
          refreshControlPosition,
          controlDelete,
          signers,
          handleDocChange,
          handleScroll,
          signType,minTargetInfo,
          signControlClick,
          signDatevisible,
          handleSignDateFormat,
          signDateType,
          registerSeal,
          registerWish,
          registerSignature,
          handleSetSignature,
          handleWriteSignature,
          signControlInfo,
          setSignControlType,
          handleCancel,
          handleAuth,
          currentControl,
          signCertificateVisible,
          personalAuth,
          certificateInfo,
          updateCertificate,
          handleUnSign,
          goSign,
          authCertificateVisible,
          signerCaAuthInfo,
          authCerInfo,
          ...toRefs(compState),
          handlePersonAuth,
          handleFace,
          openPersonalAccount,
          tenantAuthInfo,
          handleSetSeal,
          handleSign,
          taskVisible,
          taskInfo,
          handleNoTask,
          handleGoNextTask,
          handleConfirmSuccess,
          handleSetControl,
          signDateFormat,
          doContent,
          controlChangeFlag,
          chopStampUseFlag,
          personalSignAuth,
      }  
    }
})
</script>

<style lang="less" scoped>

.red-tip{
  color:red;
}
.position-container{
  margin-top:-20px;
  .full-loading{
    background-color: rgb(0 0 0 / 20%);
  }
}
.position-params-body{
  overflow: hidden;
  height: 100%;
}
.doc-select{
  min-width:100px;
  :deep(.ant-select-selector){
    border:none;
  }
  :deep(.ant-select-selection-item){
    font-size: 18px;
    font-weight: 550;
  }
}
.ca-auth-info{
  position: fixed;
  top: 0;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  :deep(.ant-alert){
    flex:1;
  }
  :deep(.ant-btn){
    position:absolute;
    right:10px;

  }
}

</style>

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
      </DocLeft>
      <DocContent ref="doContent" :nowDocument="nowDocument"  :docs="docs" :showControl="true" :isSign="true"  :minTargetInfo="minTargetInfo" >
      </DocContent>
      <DocRight title="签约详情">
        <SignDetail />
      </DocRight> 

    </div>
    <a-modal v-model:visible="taskVisible" title="任务提醒" :closable="false" :maskClosable="false">
           <p>监测到该签约文档中您还有【{{ taskInfo.taskType == 'sign' ? '签署 ' : taskInfo.taskType == 'approval' ? '审批 ' : '填写' }}】任务，是否前去处理？</p>
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
import SignDetail from '../components/SignDetail.vue';
import SignStatus from '../components/SignStatus.vue';
import { useMessage } from '/@/hooks/web/useMessage';
import { useUserStore } from '/@/store/modules/user';
import { useModal } from '/@/components/Modal';

import { createLocalStorage } from '/@/utils/cache';

import { 
    checkOperatorStatus, 
    getDocFiles, 
    getDocImgs, 
    getSigners,
    rejectApproval,
    submitApproval,
} from '/@/api/contract/index';
import { pageScaling } from "/@/views/contract/components/data/ControlerMoveRange"
import { getAppEnvConfig } from '/@/utils/env';
import { getHashQueryString,decodeURIs } from '/@/utils';
import { CanvasZoom } from '/@/views/contract/components/data/ControlData';


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
    SignDetail,
    SignStatus,
    Loading,
    Modal, Input, Button,

  },
  setup(props,{emit}) {
      const ls = createLocalStorage();
      const docId = ref('');
      const docs:any = ref([]); 
      const doContent = ref()
      const minTargetInfo = ref();
 
      const compState = reactive({
        absolute: false,
        loading: false,
        tip:'加载中...'
      })

      
      const {VITE_GLOB_API_URL} = getAppEnvConfig();
      const signers:any = ref({});
      const documentList:any = ref([])
      const nowDocument:any = ref({
        activeControl:[]
      });
      const signerList:any = ref([]);
      const images:any = ref([])
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const taskId = route.query?.taskId;
      const signRuId = route.query?.signRuId;
      const callbackPage = getHashQueryString('callbackPage');
      const partyName = getHashQueryString('partyName');
      const taskVisible = ref(false)
      const taskInfo:any = ref({})

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
      const actions = ref(<ButtonItem[]>[
        {
          type:'default',
          text:'驳回',
          callBack:handleRejectApproval,
          show: true,
        },
        {
          type:'primary',
          text:'审批通过',
          callBack:handleApproved,
          show: true,
        },
      ])

    
      ls.set('Sign-Task-Id',taskId)

      const userStore = useUserStore();
      const tenantAuthInfo =ref({
        authStatus:0,
      });
      const userInfo = userStore.getUserInfo;

      const { createMessage: msg, createSuccessModal , createConfirm, createWarningModal,createInfoModal} = useMessage();

      onMounted(async ()=>{
        try{
          compState.tip = '数据加载中...';
          compState.loading = true;
          await checkStatus()
          await initData()
          compState.loading = false
          compState.tip = '数据提交中...';
        } catch(e){
          console.log(e)
          compState.loading = false
          compState.tip = '数据提交中...';
        }
        
      })


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
                   taskType:'approval',
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
                callbackPage:callbackPage
              }
            })
          }
        }   
      }

      function initWindow() {
        var h = document.documentElement.clientHeight || document.body.clientHeight;
      	return h;
      }
      const height = ref(initWindow());
      async function initData(){
        getSignerList();
        getDocs();
      }

      //获取参与方
      async function getSignerList(){
        let result = await getSigners({signRuId:signRuId});
        if(result){
          signerList.value = result;
        }
      }
      //获取文档
      async function getDocs(){
        let result = await getDocFiles({signRuId:signRuId})
        if(result){
            docs.value  = result;
            docId.value = result[0].id;
            docs.value.map(async(v,i)=>{
              v.pageSize = v.docPage;
              let result = await getDocImgs({ signFileId:  v.id });
              v.images = result.sort((a, b) => a.page - b.page);
              if(i==0){
                images.value = result;
              }
              v.images = result;
              const {targets,maxWidth} = pageScaling(result);
              v.targets = targets;
              v.maxWidth = maxWidth;
              v.imageLoading=true;
              v.imageAllHeight=targets[targets.length-1].transformHeight + targets[targets.length-1].height + (result.length * CanvasZoom.space);
            if(v.id==docId.value){
              nowDocument.value = v;
            }
            // console.log(docs.value,nowDocument.value,'nowDocument.value.images');
            })  
        }
      }

        //文档切换
      async function handleDocChange(val){
        // console.log(val,'val-----');
        // console.log( docs.value,' docs.value');
        let matchDoc = docs.value.find(item=>item.id ==  val);
        console.log(matchDoc,'matchDoc----');
        nowDocument.value = matchDoc;
        docId.value = val;
        document.getElementsByClassName('doc-content')[0].scrollTop = 0;

      }

      // 自定义的textarea数据  
      const textareaValue = ref('');  
      function handleInput(e){
        textareaValue.value = e.target.value;
      }
      // 审批通过
      async function handleApproved(){
        textareaValue.value = '';
        Modal.confirm({
          title: '审批通过',
          content: createVNode(Input.TextArea, {
            text: textareaValue.value,
            placeholder:'请填写审批原因，200字以内（非必填）',
            maxlength:200,
            onChange: handleInput,
          }),
          onOk() {
            return new Promise((resolve,reject)=>{
              if(textareaValue.value && textareaValue.value.length>200){
                   reject('字数不符合要求')
                }else{
                  submitApprovalData('pass');
                  resolve('success');
                }
            })
          }
        });
      }
      // 审批驳回
      async function handleRejectApproval(){
        textareaValue.value = '';
        Modal.confirm({
          title: '审批驳回',
          content: createVNode('div', {}, [
            createVNode('p', {}, '请注意，驳回操作将导致整个签署流程终止，请谨慎处理。'),
            createVNode(Input.TextArea, {
              text: textareaValue.value,
              placeholder:'请填写审批原因，200字以内（非必填）',
              maxlength:200,
              onChange: handleInput,
            })
          ]),
          onOk() {
            return new Promise((resolve,reject)=>{
              if(textareaValue.value && textareaValue.value.length>200){
                   reject('字数不符合要求')
                }else{
                  submitApprovalData('reject');
                  resolve('success');
                }
            })
          }
        });
      }

      async function submitApprovalData(type:string){ 
        compState.loading = true;
        let result ;
        let title = '';
        if(type === 'reject'){
          result = await rejectApproval({signRuId:signRuId,comment:textareaValue.value});
          title = '审批已驳回，点击确定关闭当前页面';
        }else{
          result = await submitApproval({signRuId:signRuId,comment:textareaValue.value});
          title = '审批已通过，点击确定关闭当前页面';
        }
        if(result && result.code == 200){
          if(result.taskId && !callbackPage){
            taskVisible.value = true;
            taskInfo.value = result;
          }else{
            createSuccessModal({
            title: title, 
            okText:'确定',
            iconType: 'success',
            onOk:async()=> {
              if(callbackPage && typeof callbackPage == 'string'){ 
                window.open(decodeURIs(callbackPage),'_self')
              }else{
                  compState.loading = false;
                  // window.history.go(-1);
                  // window.close();
                  router.push({
                    path: '/contract/doc',
                  });
              }
            
            }
          })
          }
          compState.loading = false;
        }else{
          compState.loading = false;
          msg.error(result.message);
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
        if (taskInfo.value.taskType == 'approval') {
            router.push({
                path: '/contract/approval',
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
          if(callbackPage && typeof callbackPage == 'string'){ 
            window.open(decodeURIs(callbackPage),'_self')
          }else{
            router.push({
              path: '/contract/doc'
            })
          }
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

      
      return {
          docs,
          docId,
          height,
          actions,
          nowDocument,
          documentList,
          signerList,
          signers,
          handleDocChange,
          minTargetInfo,
          handleCancel,
          signerCaAuthInfo,
          ...toRefs(compState),
          tenantAuthInfo,
          taskVisible,
          taskInfo,
          handleNoTask,
          handleGoNextTask,
          doContent,
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

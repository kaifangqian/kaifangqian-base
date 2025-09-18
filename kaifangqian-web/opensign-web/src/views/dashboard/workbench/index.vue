<template>
  <!-- 
   实名认证message显示说明：
   1、个人未实名，直接显示；
   2、企业未实名，且当前用户是企业管理员（当前用户是企业创建人），显示；
  -->
  <div v-if="tenantInfo.authStatus !== 2 && (tenantInfo.tenantType=='2' || 
  (tenantInfo.tenantType=='1' && userInfo.tenantUserId == tenantInfo.applyTenantUser))">
    <a-alert type="warning" class="text-center">
      <template #message>
        <a-space>
          <span style="color: sandybrown;">根据《电子签名法》规定，签发合同需完成实名认证，以保障合同法律效率</span>
          <!-- <a-button @click="handleAuth(userInfo.loginTenantType as number)" size="small" type="primary" class="goto-auth">立即认证</a-button> -->
          <a-button @click="toAuth()" size="small" type="primary" class="goto-auth">立即认证</a-button>
        </a-space>
      </template>
    </a-alert>
  </div>
  <div :class="['workbench-content',userInfo.loginTenantType=='1'?'workbench-content-top':'']">

    <div class="workbench-content-bg" v-if="userInfo.loginTenantType=='1'"></div>
    <div class="workbench-area workbench-area2">
      <div class="" v-if="userInfo.loginTenantType=='2'">
        <PersonIndex/>
      </div>
    </div>
    <div class="workbench-area workbench-area1">
    <div :class="[userInfo.loginTenantType=='1'?'ent-card':'']" v-if="userInfo.loginTenantType=='1'">
      <div title="" size="small" class="ent-statistics">
        <!-- <div class="account-info">
          <CropperAvatar
              :uploadApi="(uploadAvatarApi as any)"
              :value="userInfo.avatarImg"
              btnText=""
              :showBtn="false"
              :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
              @change="updateAvatar"
              width="80"
            />
            <div class="user-info">
              <div>
                <a-tooltip>
                  <template #title>{{  tenantInfo.name }}</template>
                  <span class="user-name">{{ tenantInfo.name }}</span>
                </a-tooltip>
                <a-tag :color="loadCertificationStatus(tenantInfo.authStatus)" >{{loadCertificationText(tenantInfo.authStatus)}}</a-tag>
              </div>
            </div>
          </div> -->
        <div class="statistics-info">
          <ul>
            <!-- <span class="statistics-tag">我发起的</span> -->
            <li>
              <div>
                <p class="statistics-count">{{ statisticsInfo.statusCount || 0}}</p>
                <p class="statistics-title">进行中</p>
              </div>
              <span class="line-text"></span>

            </li>
            <li>
              <div>
                <p class="statistics-count">{{ statisticsInfo.status6Count || 0 }}</p>
                <p class="statistics-title">已拒填</p>
              </div>
              <span class="line-text"></span>
            </li>
            <li>
              <div>
                <p class="statistics-count">{{ statisticsInfo.status8Count || 0 }}</p>
                <p class="statistics-title">已拒签</p>
              </div>
              <span class="line-text"></span>

            </li>
            <li>
              <div>
                <p class="statistics-count">{{ statisticsInfo.status10Count || 0}}</p>
                <p class="statistics-title">已撤销</p>
              </div>
              <span class="line-text"></span>

            </li>
            <li>
              <div>
                <p class="statistics-count">{{ statisticsInfo.status9Count || 0}}</p>
                <p class="statistics-title">已逾期</p>
              </div>
              <span class="line-text"></span>

            </li>
            <li>
              <div>
                <p class="statistics-count">{{ statisticsInfo.status11Count || 0}}</p>
                <p class="statistics-title">已完成</p>
              </div>
            </li>
          </ul>

        </div>
      </div>
    </div>
    <card size="small" class="sign-intro-area"  v-if="userInfo.loginTenantType=='1'" style="padding-bottom: 30px;">
      <div class="sign-intro">
        <div class="intro-item">
          <div class="intro-img">
            <img src="~@/assets/images/bench1.png" alt=""/>
          </div>
          <div class="home-enter-item"> 
            <h2>5步完成电子文件签署</h2>
            <p>快速完成文件签署</p>
            <a-button type="link" @click="handleDoc">查看指南</a-button>
            <img src="~@/assets/images/home-enter.png"  @click="handleDoc"/>
          </div>
        </div>
        <div class="intro-item">
          <div class="intro-img">
            <img src="~@/assets/images/bench2.png" alt=""/>
          </div>
          <div class="home-enter-item"> 
            <h2>电子签约场景示例</h2>
            <p>支持B2C、B2B、企业内部盖章/签字场景</p>
            <a-button type="link" @click="handleDemo">查看示例</a-button>
            <img  @click="handleDemo" src="~@/assets/images/home-enter.png"/>
          </div>
        </div>
      </div>
      <!-- <a-button type="primary" class="sign-start-btn" @click="handleSelectLine" v-if="userType == 'core'">
        <Icon icon="ant-design:plus-outlined"/>发起签署</a-button> -->
      <a-button type="primary" class="sign-start-btn" @click="handleSelectLine">
    <Icon icon="ant-design:plus-outlined"/>发起签署</a-button>
      <!-- <a-tooltip placement="bottom" v-else>
        <template #title>
          <span>您所在的企业暂无发起权限，请联系系统管理员进行企业账号升级</span>
        </template>
        <a-button type="primary" class="sign-start-btn" :disabled="true" @click="handleSelectLine">
          <Icon icon="ant-design:plus-outlined"/>发起签署</a-button>
      </a-tooltip> -->
    </card>
    <PendingHandle v-if="userInfo.loginTenantType=='1'"/>
    <!-- <Card title="待办事项" size="small">
        <template #extra> 
          <a-button type="link" size="small" @click="handleMore">更多</a-button>
        </template>
        <a-tabs @change="handleTabChange" v-model:activeKey="activeTabKey" :tabBarGutter="70">
          <a-tab-pane key="1" >
            <template #tab>
              <span>
                <a-badge :count="upsignCount" :offset="[20,0]">
                  待我签署
                </a-badge>
              </span>
            </template>
          </a-tab-pane>
          <a-tab-pane key="2">
            <template #tab>
              <span>
                <a-badge :count="upwriteCount" :offset="[20,0]">
                  待我填写
                </a-badge>
              </span>
            </template>
          </a-tab-pane>
        </a-tabs>
        <div class="upcoming-list">
          <ul>
            <li v-for="(item,index) in  upcomingList" :key="index" v-if="upcomingList&&upcomingList.length">
              <div class="upcoming-info doc-"> 
                <p class="upcoming-title"  @click="handleSign(item)">{{ item.subject}}</p>
                <div class="upsign-info">
                  <span class="signatory-line">签署方：{{item.participateNames}}</span>
                  <span>{{ loadRuStatus(item.status) }}</span>
                  <span>由 {{ item.fromTenantName }} 于 {{ item.createTime}} 发起</span>
                </div>
              </div>
            </li>
            <div v-else>
              <p class="no-data">
                  <img src="~@/assets/images/no-data.png" alt="">
              </p>
            </div>

          </ul>

        </div>

    </Card> -->
    <BusinessLineModal @register="registerLineModal" @success="handleStart"/>
  </div>
</div>
</template>

<script lang="ts">
import type { UserInfo } from '/#/store';
import {ref,defineComponent, onMounted, unref, h} from "vue"
import { Tag, Tabs, Card } from 'ant-design-vue';
import { CropperAvatar } from '/@/components/Cropper';
import { useUserStore } from '/@/store/modules/user';
import { useModal } from '/@/components/Modal';
import { getUserInfo, } from '/@/api/sys/user'; 
import { uploadAvatarApi, getImgBase64 } from '/@/api/sys/upload';
import Icon from '/@/components/Icon';
import { useMessage } from '/@/hooks/web/useMessage';
// import Invitation from "/@/layouts/default/header/components/change-account/Invitation.vue";
import  { getMyStastics, getListMyFillInJob } from '/@/api/contract';
// import { companyAuthApi,personAuthApi } from '/@/api/auth/userAuth';
import { useGo } from '/@/hooks/web/usePage';
import { useRouter } from 'vue-router';
import BusinessLineModal from '/@/layouts/default/header/components/sign/modal/BusinessLineModal.vue';
import { getBusinessLine } from '/@/api/contract';
import PersonIndex from "./PersonIndex.vue";
import PendingHandle from "./PendingHandle.vue"
import {createBusinessLineAuth} from "/@/api/license"
import {handleAuth} from '/@/views/sys/user'
export default defineComponent({
  name:"Workbench",
  components:{
    CropperAvatar,
    Tag,
    Icon,
    Tabs,
    TabPane: Tabs.TabPane,
    // Invitation,
    PendingHandle,
    Card,PersonIndex,
    BusinessLineModal
  },
  setup() {
    
    const go = useGo();
    const userStore = useUserStore();
    const router = useRouter();

    
   const { createMessage: msg,createConfirm } = useMessage();
   
   const [registerLineModal,{openModal:openLineModal,closeModal:closeLineModal}] = useModal();

    

    const statisticsInfo:any = ref({});

    const userInfo = ref<UserInfo>({
      phone:'',
      avatarImg:''
    });

    const tenantInfo = userStore.getTenantInfo;
    const userType = userStore.userInfo?.sysType;
    onMounted(()=>{
     
      fetch();

      document.addEventListener("visibilitychange", function() {
        if(!document.hidden){
          fetch();
        }
      })
    })

    async  function handleSelectLine(){
      
      const apiLimit = await createBusinessLineAuth();
      if(apiLimit.flag == 3){
        msg.warning('软件授权已过期，无法发起签署');
        return;
      }
      router.push("/business")
      // let result = await getBusinessLine({});
      // if(result.length>1){
      //   openLineModal(true,{
      //     isUpdate:false,
      //     record:{
      //       list: result
      //     }
      //   })
      // }else if(result.length==1){  
      //   // window.open('/#/contract/start?__full__&signReId=' + result[0].id)
      //   router.push({
      //     path:"/contract/start",
      //     query:{
      //       __full__:"",
      //       signReId:result[0].id
      //     }
      //   })
      // }else if(result.length==0){
      //   msg.warning('您暂无发起权限，请联系企业管理员')
      // }

     
    }
    // async function handleAuth() {
    //   const asyncPage = window.location.origin + '/#/user/centerInfo';
    //   if (userInfo.value.loginTenantType === 1) {
    //     const result = await companyAuthApi({ callbackPage: asyncPage });
    //     if (result.authStatus === 0) {
    //       window.open(result.authUrl, '_self');
    //     } else if (result.authStatus === 1) {
    //       await userStore.getTnantInfo();
    //       window.open(asyncPage, '_self');
    //     }
    //   } else {
    //     const result = await personAuthApi({ callbackPage: asyncPage });
    //     if (result.authStatus === 0) {
    //       // userStore.clearUserInfo();
    //       window.open(result.authUrl, '_self');
    //     } else if (result.authStatus === 1) {
    //       await userStore.getTnantInfo();
    //       window.open(asyncPage, '_self');
    //     }
    //   }
    // }
    function handleStart(val){
      closeLineModal()
      // window.open('/#/contract/start?__full__&signReId=' + val,'_blank','noopener,noreferrer')
      router.push({
        path:"/contract/start",
        query:{
          __full__:"",
          signReId:val
        }
      })
    }
    
    
    const fetch = async ()=> {
      userInfo.value = await getUserInfo();
      if(userInfo){
        userStore.setUserInfo(unref(userInfo));
        if(userInfo.value.avatar){
          // getAvatar(userInfo.value.avatar)
        }
      }
      if(tenantInfo.tenantType=='1'){
      let result = await getMyStastics({});
        if(result){
          statisticsInfo.value = result;
        }
      }
      
    }
    
    // async function getAvatar(id){
    //   let result1 = await getImgBase64({imgId:id});
    //   if(result1){
    //     userInfo.value.avatarImg = result1.image;
    //   }
    // }
    
   

    async  function updateAvatar(src: string, data) {
      console.log(src, data, '上传成功')
     
       fetch()
      
    }
    
   
    function handleMore(){
      // go('/contract/doc?key=7')
      router.push({
        path:'/contract/doc',
        // name:'文档管理',
        query:{
          key:'7',
        }
      })
    }
    function handleDoc(){
      window.open('https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/%E6%88%91%E6%98%AF%E4%BC%81%E4%B8%9A%E7%AE%A1%E7%90%86%E5%91%98')
    }
    function handleDemo(){
      window.open('https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/%E7%94%B5%E5%AD%90%E5%90%88%E5%90%8C%E7%AD%BE%E7%BA%A6%E5%9C%BA%E6%99%AF%E7%A4%BA%E4%BE%8B/B2C%E7%AD%BE%E7%BA%A6%E5%9C%BA%E6%99%AF%E7%A4%BA%E4%BE%8B')
    }

    async function toAuth() {
        if(userInfo.value.loginTenantType === 2){
          //个人
          createConfirm({
            iconType: 'info',
            width: '600px',
            title: '开启安全可靠的电子签章',
            content: h('div', [
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
                }, '请完成以下步骤：'),
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
              handleAuth(userInfo.value.loginTenantType as number);
            },
          });
        }
        if(userInfo.value.loginTenantType === 1){
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
                }, '请完成以下步骤：'),
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
              handleAuth(userInfo.value.loginTenantType as number);
            },
          });
        }

      }

    return {
        handleDemo,
        updateAvatar,
        uploadAvatarApi,
        userInfo,
        tenantInfo,
        statisticsInfo,
        handleMore,
        registerLineModal,
        handleSelectLine,
        handleStart,
        handleDoc,
        userType,
        handleAuth,
        toAuth
    }
  }
})
</script>

<style lang="less" scoped>
.workbench-content-top{
  margin-top:-20px;
}
.workbench-content{
 
  .workbench-content-bg{
    height: 362px;
    width:100%;
    background: linear-gradient(90deg, rgba(22, 129, 177, 1) 50%, rgba(20, 126, 147, 1) 94%)
  }
  .ent-card{
    margin-top:-360px;
  }
}
.workbench-area1{
  width: 1080px;
}
.workbench-area2{
  width: 1366px;
}
.workbench-area{
  margin:0 auto;
  .sign-intro-area{
    :deep(.ant-card-body){
      text-align: center;
    }
    .sign-start-btn{
      width:200px;
      height:50px;
      font-size: 18px;
    }
  }
  .sign-intro{
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding:30px 80px;
    text-align: center;
    .intro-item{
      display: flex;
      height: 122px;
      width:430px;
      align-items: center;
      text-align: left;
      // justify-content: space-between;
      padding-right:20px;
      box-shadow: 5px 5px 20px #0000001a;
      border-radius: 4px;
      .home-enter-item{
        img{
          width:24px;
          visibility:hidden;
          cursor:pointer;
        }
      }
      &:hover{
          img{  
            visibility:visible;
          }
        }
      h2{
        font-size: 16px;
        font-weight: 550;
      };
      :deep(.ant-btn){
        padding-left:0
      }
      .intro-img{
        display: flex;
        justify-content: center;
        align-items: center;
        width: 122px;
        height: 100%;
        background:#f4f4f4;
        margin-right:20px;
        img{
          width: 60px;
          height: 60px;
        }
      }
     
    }
  }
  // margin-top: 20px;
  :deep(.ant-card-head-title){
    font-size: 16px;
    font-weight: 600;
  }
  :deep(.ant-card){
    box-shadow: 5px 5px 30px #0000000e;
    margin-bottom: 10px;
  }
  :deep(.ant-card-extra){
    .ant-btn{
      // font-size: 12px;
      margin:0 5px;
      // padding:2px 10px;
    }
    
  }
  .account-info{
    padding:24px;
    display: flex;
    align-items: center;
    .user-info{
      margin-left:20px;
      
      &>div{
        display: flex;
        align-items: center;
        margin-bottom: 15px;
      }
      .user-name{
        font-size: 16px;
        font-weight: 600;
        margin-right: 20px;
        display: inline-block;
        width:70px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      .info-title{
        margin-right: 20px;
        font-size: 14px;
      }
    }
  }
  .statistics-info{
    width: 90%;
    margin:0 auto;
    position: relative;
    .statistics-tag{
      position: absolute;
      left:0;
      top:0;
      color: #43c7d0;
      background: #e1fdff;
      padding:0 5px;
    }
    p{
      margin-bottom: 0;
    }
    ul{
      padding:22px 70px 10px;
      // background:#70f6ff12;
      display: flex;
      align-items: center;
      margin-bottom:40px;
      justify-content: space-between;
      li{
        text-align: left;
        width:150px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin:0 5px;
        box-sizing: border-box;
        padding:0 5px;
        .statistics-title{
          font-size: 14px;
          // margin-bottom: 15px;
          color: #fff;
        }
        .line-text{
          display: inline-block;
          width: 3px;
          height:50px;
          background: #fff;
        }
        .statistics-count{
          font-size: 32px;
          color: #fff;
          font-weight: 600;
          
        }
      }
    }
  }
}

  .text-center {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0px auto 36px auto;
    width: 96%;
    min-height: 56px;
    padding: 0 24px;
    font-size: 16px;
    font-weight: 600;
    color: #d4380d;
    background: linear-gradient(135deg, #fff1f0 70%, #ffe6e6 100%);
    border-left: 5px solid #ff7875;
    border-radius: 8px;
    box-shadow: 0 4px 18px rgba(255, 122, 122, 0.08), 0 1.5px 6px rgba(0,0,0,0.04);
    letter-spacing: 0.5px;
    position: relative;
    z-index: 2;
    animation: alert-flash 1.8s infinite alternate;
  }

  @keyframes alert-flash {
    0% {
      box-shadow: 0 4px 18px rgba(255, 122, 122, 0.08), 0 1.5px 6px rgba(0,0,0,0.04);
      background: linear-gradient(135deg, #fff1f0 70%, #ffe6e6 100%);
      color: #d4380d;
      // border-left-color: #ff7875;
    }
    60% {
      box-shadow: 0 8px 32px rgba(255, 122, 122, 0.18), 0 3px 12px rgba(0,0,0,0.08);
      background: linear-gradient(135deg, #ffe6e6 70%, #fff1f0 100%);
      color: #ff4d4f;
      // border-left-color: #ff4d4f;
    }
    100% {
      box-shadow: 0 4px 18px rgba(255, 122, 122, 0.08), 0 1.5px 6px rgba(0,0,0,0.04);
      background: linear-gradient(135deg, #fff1f0 70%, #ffe6e6 100%);
      color: #d4380d;
      // border-left-color: #ff7875;
    }
  }

  .goto-auth {
    margin-left: 8px;
    font-size: 14px;
    font-weight: 500;
    color: #fff;
    background-color: #ff4d4f;
    border-color: #ff7875;
    &:hover {
      background-color: #ff7875;
      border-color: #ff4d4f;
    }
  }



</style>

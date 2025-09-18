<template>
  <div class="main-overflow">
    <div class="overflow-top">
      <Card size="small">
        <template #title>
          <div class="overflow-enterprise-title">
            <div style="display:flex;align-items:center"> 
                <a-tooltip>
                  <template #title>{{ userInfo.loginTenantName}}</template>
                  <span class="depart-name">{{ userInfo.loginTenantName }}</span>
                </a-tooltip>
              <a-tag :color="loadCertificationStatus(tenantInfo.authStatus)" style="margin-left:20px">{{loadCertificationText(tenantInfo.authStatus)}}</a-tag>
              </div>
            <span>
              <!-- <div>

              </div> -->
              <a-tooltip>
                <template #title>{{ tenantInfo.authStatus==1?'变更审核中':'变更审核失败' }}</template>
                 <Icon icon="ant-design:question-circle-outlined" v-if="tenantInfo.authType == 2 && tenantInfo.authStatus != 2" />
              </a-tooltip>
              <a-button @click="toAuth" v-if="userInfo.tenantUserId == tenantInfo.applyTenantUser" type="link">{{ loadAuthBtnText(tenantInfo.authStatus) }}  </a-button>
            <!-- <span class="auth-record" @click="handleAuthRecord" v-if="tenantInfo.authStatus!=0">认证记录</span> -->
            </span>
            
          </div>
        </template>
        <div class="overflow-data">
            <ul class="member-data">
              <li>
                <p class="data-title">组织总人数</p>
                <p class="data-value blue">{{ stasticsData.userAllCount }}</p>
              </li>
              <li>
                <p  class="data-title">未激活人数</p>
                <p class="data-value">
                  {{ stasticsData.user0Count }}
                  <span class="send-warn" @click="handleWarn" v-if="stasticsData.user0Count>0">发送提醒</span>
                </p>
              </li>
              <li>
                <p  class="data-title">部门数</p>
                <p class="data-value">{{ stasticsData.departAllCount }}</p>
              </li>
            </ul>
            <ul class="overfolw-option">
              <li @click="handleAddUser('user')">
                <Icon icon="ant-design:user-add-outlined" color="#127fd2" size="24"/>
                <span>添加成员</span>
              </li>
              <li @click="handleUserJoin">
                <Icon icon="ant-design:solution-outlined" color="#127fd2"  size="24"/>
                <span>邀请成员</span>
              </li>
              <li @click="handleAddUser('dept')">
                <Icon icon="ant-design:partition-outlined" color="#127fd2"  size="24"/>
                <span>添加部门</span>
              </li>
            </ul>
          </div>
      </Card>
      <Card size="small" title="应用管理">
        <template #extra> 
          <a-button type="link" size="small" style="padding:12px 0;height:50px" @click=handleAppMore>更多 </a-button>
        </template>
        <ul class="app-list">
          <li v-for="(record,index) in appList" :key="index">
            <div class="app-info">
              <div class="app-avatar">
                <img  src="@/assets/images/app-icon.png" alt=""  v-if="!record.appIcon" /> 
                <img  :src="record.appIcon" alt="" v-else />
              </div>
              <div>
                <p class="app-name">{{ record.appName }}</p>
                <p class="app-desc">{{ record.appDesc }}</p>
              </div>
            </div>
            <a-button type="link" @click="handleConfig(record)">配置</a-button>
          </li>
        </ul>
      </Card>
    </div>
    <Card size="small" title="成员加入申请">
      <BasicTable @register="registerTable">
        <template #status="{record}">
          <span>{{ loadStatus( record.status) }}</span>
        </template>
        <template #action="{record}">
          <a-button type="link" :disabled="record.status != 0" @click="handlePass(record)"> 通过</a-button>
          <a-button type="link" :disabled="record.status != 0"  @click="handleReject(record)" danger>驳回</a-button>
        </template>
      </BasicTable>
    </Card>
    <AppConfig @register="registerAppModal" />
    <CertificateRecordModal @register="registerModal" />
    

  </div>
</template>

<script lang="ts">
import {ref, defineComponent, onMounted, h} from "vue"
import { useUserStore } from '/@/store/modules/user';
import { Card } from 'ant-design-vue'
import Icon from "/@/components/Icon";
import { getTenantApp } from '/@/api/tenant';
import { useGo } from '/@/hooks/web/usePage';
import { BasicTable,useTable } from '/@/components/Table';
import { memberJoinColumn } from './data';
import { useMessage } from '/@/hooks/web/useMessage';
import { getJoinMemberData, checkTenantUser, getEnterpriseAuthInfo } from '/@/api/sys/user';
import { getDepartStastics, getSendInviteMes} from '/@/api/sys/dept';
import { useModal } from '/@/components/Modal';
import {loadCertificationStatus, loadCertificationText} from '/@/utils/StatusToName'
import AppConfig from '/@/views/tenant/application/modal/ConfigModal.vue';
import CertificateRecordModal from './modal/CertificateRecordModal.vue';
import { handleAuth,handleUpdateAuth } from '/@/views/sys/user';

interface AppItem {
  appIcon:string;
  appName:string;
  appDesc:string;
}

export default defineComponent({
  name:"Overflow",
  components:{
    Icon,
    BasicTable,
    AppConfig,
    CertificateRecordModal,
    Card
  },
  setup() {
    const appList = ref(<AppItem[]>[]);
    const stasticsData:any = ref({});
    const userStore = useUserStore();
    const userInfo =  userStore.getUserInfo;
    const tenantInfo:any = ref({...userStore.getTenantInfo,
      memberCount:50,
      noactiveCount:2,
      departCount:7
    });
    const go = useGo();
    const { createMessage:msg, createConfirm } = useMessage();
    const [registerModal, { openModal }] = useModal();
    const [registerAppModal, { openModal:openAppModal }] = useModal();
    const [registerTable,{reload }] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getJoinMemberData,
        columns:memberJoinColumn,
        immediate:true,
        fetchSetting:{
          listField:'records'
        },
        searchInfo:{
          status:0
        },
        dataSource:[],
        rowKey:'id',
        useSearchForm: false,
        showIndexColumn: false,
        canResize: false,
        isTriggerSelect:false,
        striped:false,
        bordered:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        pagination:true
    });

    onMounted(()=>{
      fetch();
      getAuthInfo();
      gettasticsData()
    })

    

    async function getAuthInfo(){
      console.log(tenantInfo,'----信息项目')
      if(!tenantInfo.id) return;
      let result  = await getEnterpriseAuthInfo({id:tenantInfo.id});
      if(result){

      }
    }
    async function fetch(){
      let result  = await getTenantApp({});
      if(result){
        appList.value = result.records.splice(0,5);
      }
    }
    async function gettasticsData (){
      let result = await getDepartStastics({});
      if(result){
        stasticsData.value = result;
      }
    }
    async  function handleWarn(){
      let result = await getSendInviteMes({});
      if(result){
        msg.success('操作成功')
      }
    }
    function handlePass(record){
      createConfirm({
        title: '确认', 
        content: "请确认是否通过该成员加入申请？",
        okText:'确定',
        iconType: 'info',
        onOk() {
          tenantUserCheck(record,1)
        },
      })

    }
    function handleReject(record){
      createConfirm({
        title: '确认', 
        content: "请确认是否驳回该成员加入申请？",
        okText:'确定',
        iconType: 'warning',
        onOk() {
          tenantUserCheck(record,2)
        },
      })
    }
    async function tenantUserCheck(record,type){
      let result = await checkTenantUser({
        "id":record.id,
        "status":type
      })
      if(result){
        msg.success('审核成功')
        reload();
      }
    }
    function loadStatus(status){
          switch(status){
            case 0:
              return '待审核';
            case 1:
              return '已通过';
            case 2:
              return '未通过';
            default:
              return '待审核';
          }
    }
    function loadAuthBtnText(status){
          switch(status){
            case 0:
              return '立即认证';
            case 1:
              return '认证详情';
            case 2:
              return '认证变更';
            case 3:
              return '继续认证';
            default:
              return '立即认证';
          }
    }

  async function toAuth() {
    if(tenantInfo.value.authStatus != 2){
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
              h('div', `2. 您将作为认证申请人，在「云盾系统」完成对「${userInfo.loginTenantName}」的企业实名认证`)
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
          handleAuth(1, `${window.location.origin}${window.location.pathname}#/overview`);
        },
      });
       
    }
    if(tenantInfo.value.authStatus == 2){
       handleUpdateAuth(1, `${window.location.origin}${window.location.pathname}#/overview`);
    }
  }

    // function handleAuth(){
    //     const token = userStore.getToken;
    //     let authInfo = userInfo.authInfo
    //     let appInfo = {
    //         token:token,
    //         appCode:authInfo.appCode,
    //         appId:authInfo.appId,
    //         departId:userInfo.loginDepartId,
    //       }
    //       let paramsString = new URLSearchParams(appInfo).toString();
    //       let path = '';
    //       if(tenantInfo.value.authStatus == 1 ){
    //         path = '/detail'
    //       }else if(tenantInfo.value.authStatus == 2){
    //         path = '/change'
    //       }else if(tenantInfo.value.authStatus == 3 && tenantInfo.value.authType == 2){
    //         path = '/change'
    //       }
    //       window.open(authInfo.appAddress + '/#/enterprise' + path + '?' + paramsString,'_self')

    // }
    function handleAuthRecord(){
      openModal(true,{
        isUpdate:false,
        record:{
          tenantId:userInfo.loginTenantId
        }
      })
    }
    function handleConfig(record){
      openAppModal(true,{
        isUpdate:false,
        record:{
          ...record
        }
      })
    }
    function handleAddUser(type){
      go('/organize/' + type)
    }
    function handleUserJoin(){
      go('/user/join')
    }
    function handleAppMore(){
      go('/tenant/application')
    }
    return {
      userInfo,
      handleUserJoin,
      loadCertificationStatus,
      loadCertificationText,
      tenantInfo,
      handleAuth,
      loadAuthBtnText,
      handleAuthRecord,
      appList,
      handleWarn,
      handleAddUser,
      registerTable,
      handlePass,
      handleReject,
      registerAppModal,
      registerModal,
      handleConfig,
      loadStatus,
      stasticsData,
      handleAppMore,
      toAuth,

      
    }
  }
})
</script>

<style lang="less" scoped>
.main-overflow{
  // width:85%;
  margin:0 auto;
  padding: 25px;
  :deep(.ant-card){
    margin-bottom: 25px;
    box-shadow: 5px 5px 30px #0000000e;
    .ant-card-head-title{
      font-size: 16px;
      font-weight: 600;
    }
  }
  :deep(.ant-pagination){
    position: relative;
    width: 100%;
  }
}
.overflow-top{
  display: flex;
  justify-content: space-between;
  :deep(.ant-card-head-wrapper){
    height:50px;
  }
  .overflow-enterprise-title{
    display: flex;
    justify-content: space-between;
    align-items: center;
    .depart-name{
      font-size: 16px;
      font-weight: 600;
    }
    .auth-record{
      font-size: 14px;
      cursor: pointer;
      color:#127fd2;
      font-weight: 500;
    }
  }
  .overflow-data{
    margin:40px 20px;
    .member-data{
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 10px;
      box-sizing: border-box;
      li{
        text-align: left;
        width: 25%;
        P{
          margin-bottom: 0;
        }
        .data-title{
          font-size: 14px;
        }
        .data-value{
          font-size: 32px;
          font-weight: 600;
          margin-top:15px;
          color: #000;
        }
        .data-value.blue{
          color: #127fd2;
        }
        .send-warn{
          color: #127fd2;
          cursor: pointer;
          font-size: 14px;
        }
      }
    }
  }
  .overfolw-option{
    margin-top:70px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    li{
      background: #f5f5f5;
      width:30%;
      cursor: pointer;
      display: flex;
      align-items: center;
      padding:15px 20px;
      box-sizing: border-box;
      border-radius: 2px;
      :deep(.app-iconify){
        margin-right: 10px;
      }

    }

  }
  .app-list{
    height: 300px;
    overflow: auto;
    li{
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 15px;
    }
  }
  :deep(.ant-card){
    width:49%;
  }
}
</style>

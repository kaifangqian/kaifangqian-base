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
  <div class="doc-list-container">
    <div class="doc-content">
      <div class="doc-menu">
        <a-button  v-if="tenantInfo.tenantType=='1'" type="primary" @click="handleStart" style="width:160px;height:40px">
          <Icon icon="ant-design:plus-outlined" />发起签署</a-button>
        <!-- <a-button  v-if="tenantInfo.tenantType=='1' && userType == 'core'" type="primary" @click="handleStart">
        <Icon icon="ant-design:plus-outlined" />发起签署</a-button> -->
        <!-- <a-tooltip placement="bottom" v-else-if="tenantInfo.tenantType=='1' && (!userType || userType == 'base')">
          <template #title>
            <span>您所在的企业暂无发起权限，请联系系统管理员进行企业账号升级</span>
          </template>
          <a-button disabled type="primary" @click="handleStart">
            <Icon icon="ant-design:plus-outlined" />发起签署</a-button>
        </a-tooltip> -->
        <a-menu
            v-model:selectedKeys="selectedKeys"
            style="width: 256px"
            mode="inline"
            :open-keys="openKeys"
            @click="handleMenuClick"
          >
            <a-sub-menu key="sub1">
              <template #title>
                信封
              </template>
            </a-sub-menu>
            <a-menu-item key="1" title="收件箱"><template #icon> <SvgIcon name="inbox" size="16" /></template>收件箱</a-menu-item>
            <a-menu-item key="2" title="已发送" v-if="tenantInfo.tenantType=='1'"><template #icon> <SvgIcon name="send" size="16" /></template>已发送</a-menu-item>
            <a-menu-item key="3" title="抄送给我"><template #icon> <SvgIcon name="copyme" size="16" /></template>抄送给我</a-menu-item>
            <a-menu-item key="4" title="草稿" v-if="tenantInfo.tenantType=='1'"><template #icon> <SvgIcon name="draft" size="16" /></template>草稿</a-menu-item>
            <a-menu-item key="5" title="回收站" v-if="tenantInfo.tenantType=='1'"><template #icon><SvgIcon name="backage" size="16" /></template>回收站</a-menu-item>
            <a-sub-menu key="sub2">
              <template #title>
                快捷操作
              </template>
            </a-sub-menu>
            <a-menu-item key="6" title="全部文档">
              <SvgIcon name="alldoc" size="16" />
              <span class="sub-menu-name">全部文档</span> 
              <span class="sub-menu-count">{{menuStastics.allCount}}</span>
            </a-menu-item>
              <a-menu-item key="7" title="待我处理">
                <SvgIcon name="wait-me" size="16" />
                <span class="sub-menu-name">待我处理</span>
                <span class="sub-menu-count">{{menuStastics.myCount}}</span>
              </a-menu-item>
              <a-menu-item key="8" title="待他人处理">
                <SvgIcon name="wait-other" size="16" />
                <span class="sub-menu-name">待他人处理</span>
                <span class="sub-menu-count">{{menuStastics.otherCount}}</span>
              </a-menu-item>
              <a-menu-item key="9" title="未完成">
                <SvgIcon name="undone" size="16" />
                <span class="sub-menu-name">未完成</span>
                <span class="sub-menu-count">{{menuStastics.runningCount}}</span>
              </a-menu-item>
              <a-menu-item key="10" title="已完成">
                <SvgIcon name="has-done" size="16" />
                <span class="sub-menu-name">已完成</span>
                <span class="sub-menu-count">{{menuStastics.finishCount}}</span>
              </a-menu-item>
              <a-menu-item key="11" title="已失效">
                <SvgIcon name="unused" size="16" />
                <span class="sub-menu-name">已失效</span>
                <span class="sub-menu-count">{{menuStastics.invalidCount}}</span>
              </a-menu-item>
              <!-- const menuStastics = ref({
                allCount:0,
                finishCount:0,
                invalidCount:0,
                myCount:0,
                otherCount:0,
                runningCount:0
              }) -->
          </a-menu>
      </div>
      <div class="doc-list">
        <div class="sign-filter-area">
          <div class="list-title">
            <h1 class="record-title">{{ listTitle }}</h1>
            <a-button class="clear-btn" v-if="selectedKeys.includes('5')" @click="handleClear"><SvgIcon name="clear" size="22"  />清空回收站</a-button>
          </div>
          <div class="search-enter">
            <a-input-search v-model:value="formState.subject"  placeholder="请输入签约主题"  style="width: 300px"  @search="handelSearch" :allowClear="true"/>
            <a-badge :count="filterCount">
              <SvgIcon name="filter" size="26"  @click="handleFilter" class="advanced-filter"/>
            </a-badge>
            <div class="search-panel" v-if="popVisible">
              <a-form :model="formState" style="width:328px" :label-col="{ span: 4 }" :wrapper-col="{ span: 22 }" layout="vertical">
                  <a-form-item  label="签约主题" name="status">
                    <a-input-search v-model:value="formState.subject"  placeholder="请输入签约主题"   @search="handelSearch" :allowClear="true"/>
                      <SvgIcon name="filter" size="26"  @click="handleFilter" class="advanced-filter"/>
                  </a-form-item>
                  <a-form-item  label="签约状态" name="status">
                    <a-select v-model:value="formState.status" :options="options" :allowClear="true"></a-select>
                  </a-form-item>
                  <a-form-item  label="文档编号" name="code">
                    <a-input v-model:value="formState.code" :allowClear="true"></a-input>
                  </a-form-item>
                  <a-form-item  label="创建时间" name="createTime">
                    <a-range-picker  v-model:value="formState.createTime" style="width:100%" :allowClear="true"></a-range-picker >
                  </a-form-item>
                  <a-form-item  label="发起时间" name="startTime">
                    <a-range-picker  v-model:value="formState.startTime" style="width:100%" :allowClear="true"></a-range-picker >
                  </a-form-item>
                  <a-form-item  label="完成时间" name="finishTime">
                    <a-range-picker  v-model:value="formState.finishTime" style="width:100%" :allowClear="true"></a-range-picker >
                  </a-form-item>
                </a-form>
                <div class="filter-footer" style="text-align: right;">
                  <a-button type="default" @click="handelCancel">取消</a-button>
                  <a-button type="default" style="margin:0 10px;" @click="handelReset">重置</a-button>
                  <a-button type="primary" @click="handelSearch">查询</a-button>
                </div>
            </div>
          </div>
          
        </div>
        <div class="filter-tags">
          <div class="tag-list">
            <a-tag  @close.prevent v-if="formState.subject" >签约主题：{{formState.subject}}<Icon icon="ant-design:close-outlined" @click="handleDelete('subject')"/></a-tag>
            <a-tag  @close.prevent v-if="formState.status" >签约状态：{{loadRuStatus(formState.status)}}<Icon icon="ant-design:close-outlined" @click="handleDelete('status')"/></a-tag>
            <a-tag  @close.prevent v-if="formState.code"  >文档编号：{{formState.code}}<Icon icon="ant-design:close-outlined" @click="handleDelete('code')"/></a-tag>
            <a-tag  @close.prevent v-if="formState.createTime&&formState.createTime.length">创建时间：{{loadDate(formState.createTime)}}<Icon icon="ant-design:close-outlined"  @click="handleDelete('createTime')"/></a-tag>
            <a-tag  @close.prevent v-if="formState.startTime&&formState.startTime.length"  >发起时间：{{loadDate(formState.startTime)}}<Icon icon="ant-design:close-outlined" @click="handleDelete('startTime')"/></a-tag>
            <a-tag  @close.prevent v-if="formState.finishTime&&formState.finishTime.length">完成时间：{{loadDate(formState.finishTime)}}<Icon icon="ant-design:close-outlined"  @click="handleDelete('finishTime')"/></a-tag>
          </div>
          <a-button type="link" @click="handelReset" v-if="filterCount">清空</a-button>
        </div>
        <All  ref="allRef" :pageinfo="pageinfo"></All>
      </div>
    </div>
    <BusinessLineModal @register="registerModal" @success="handleStartSuccess"/>
  </div>  
</template>

<script lang="ts">
import {ref, unref, toRefs, defineComponent, reactive, onMounted, watch } from "vue";
import { Icon,SvgIcon } from '/@/components/Icon';
import All from './pages/All.vue';
import { useUserStore } from '/@/store/modules/user';
import { useRouter, useRoute } from 'vue-router';
import { loadRuStatus } from './transform';
import { useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import dayjs from 'dayjs';
import { getBusinessLine, cleanupAll,getMenuStastics } from '/@/api/contract';
import BusinessLineModal from '/@/layouts/default/header/components/sign/modal/BusinessLineModal.vue';
import {createBusinessLineAuth} from "/@/api/license"

interface FormState {
  subject: string;
  status: string;
  code: string;
  createTime: [];
  startTime: [];
  finishTime: [];
}
export default defineComponent({
  name:"Document",
  components:{
    Icon,
    All,
    SvgIcon,
    BusinessLineModal
  },
  setup() {
    const popVisible = ref(false);
    const clickSearch = ref(false);
    const formState = reactive<FormState>({
      subject:'',
      status:'',
      code:'',
      createTime:[],
      startTime:[],
      finishTime:[],
    });
    const listTitle = ref('待我处理');
    
    
    const allRef =  ref<any>();
    const options = ref([
      {label:'草稿',value:1},
      // {label:'发起审批中',value:2},
      // {label:'发起审批不通过',value:3},
      {label:'已删除',value:4},
      {label:'填写中',value:5},
      {label:'已拒填',value:6},
      {label:'签署中',value:7},
      {label:'已拒签',value:8},
      {label:'已失效',value:9},
      {label:'已撤回',value:10},
      {label:'已完成',value:11},
      // {label:'已发起',value:'12'},
    ])
    
    const state = reactive({
      rootSubmenuKeys: ['sub1', 'sub2', 'sub4'],
      openKeys: ['sub2'],
      selectedKeys: ['7'],
      activeMenuKey:'7'
    });
    const filterCount = ref(0);
    const pageinfo = ref({
      pageNo:1,
      pageSize:10,
      current:1,
      total:0,
    });
    const userStore = useUserStore();
    const tenantInfo = userStore.getTenantInfo;
    const router = useRouter();
    const route = useRoute();
    const [registerModal,{openModal,closeModal}] = useModal();
    const { createMessage: msg, createConfirm } = useMessage();
    const { currentRoute } = router;
    if(route.query.key && route.query.key == '7'){
      state.selectedKeys = ['7'];
      listTitle.value = '待我处理'
    }
    if(route.query.key && route.query.key == '2' && tenantInfo.tenantType=='1' ){
      state.selectedKeys = ['2'];
      listTitle.value = '已发送'
    }
    const userType = userStore.userInfo?.sysType;
    watch(
      ()=> route,
      (val)=>{
        console.log(val,'1111111')
        // if(val.query.key){
        //   (allRef.value as any).loadData({...unref(formState),pageNo:1,pageSize:10,type:1});
        // }
      },
      {immediate:true}
    )
     watch(
      ()=>unref(formState),
      (val)=>{
        filterCount.value = countNonEmptyProperties(val);
        if(!filterCount.value){
          clickSearch.value = false;
        }
      },
      {deep:true}
     )
     
     function countNonEmptyProperties(obj) {
        return Object.keys(obj).reduce((count, key) => {
          const value = obj[key];
          // 判断属性不为空或者不为一个空数组
          if (value != "" && value !== null && value !== undefined && (!(Array.isArray(value) && value.length === 0))) {
            return count + 1;
          }

          return count;
        }, 0);
    }
    const menuStastics = ref({
      allCount:0,
      finishCount:0,
      invalidCount:0,
      myCount:0,
      otherCount:0,
      runningCount:0
    })
    onMounted(()=>{
      // var pageNo = 1;var pageSize=10
      // if(allRef.value.pageinfo){
      //   pageNo = allRef.value.pageinfo.pageNo;
      //   pageSize = allRef.value.pageinfo.pageSize;
      // }
      (allRef.value as any).loadData({...unref(formState),pageNo:pageinfo.value.pageNo,pageSize:pageinfo.value.pageSize,type:route.query.key && tenantInfo.tenantType=='1'?route.query.key:'7'});
      console.log(state.activeMenuKey,'state.activeMenuKey')
      document.addEventListener("visibilitychange", function() {
        if(!document.hidden){
          (allRef.value as any)&&(allRef.value as any)?.loadData({...unref(formState),pageNo:pageinfo.value.pageNo,pageSize:pageinfo.value.pageSize,type:state.activeMenuKey});
        }
      })
      getMenuStastics({}).then(res=>{
        menuStastics.value = {...res}
      });
    })

    //菜单点击
    function handleMenuClick({ item, key, keyPath }){
      // if(route.query.key){
      //   router.replace({
      //     query:{
      //       key:key
      //     }
      //   })
      // }
      console.log(item,keyPath,keyPath,'当前点击菜单-')
      listTitle.value = item.title
      state.activeMenuKey = key;
      console.log(key,'当前点击菜单-')
      if(allRef.value){
        console.log(key,'当前点击菜单----');
        (allRef.value as any).loadData({...unref(formState),pageNo:1,pageSize:10,type:key},true);
      }

    }
    //搜索
    function handelSearch(){
      popVisible.value = false;
      clickSearch.value = true;
      if(allRef.value){
        (allRef.value as any).loadData({...unref(formState),pageNo:1,pageSize:10,type:state.activeMenuKey });
      }
      
    }
    //筛选条件面板
    function handleFilter(){
      // resetForm()
      popVisible.value = !popVisible.value;
    }
    //重置
    function handelReset(){
      resetForm()
      popVisible.value = false;
      clickSearch.value = false;
      if(allRef.value){
        (allRef.value as any).loadData({...unref(formState),type:state.activeMenuKey });
      }
    }
    //重置方法
    function resetForm(){
      Object.keys(formState).forEach(key => {
        if(key=='createTime' || key=='startTime' || key=='finishTime'){
          formState[key] = [];
        }else{
          formState[key] = '';
        }
      });
    }
    //取消
    function handelCancel(){
      resetForm();
      popVisible.value = false;
    }
    function handleBeforeVisibleChange(v){
      console.log(v,'----')
    }
    function loadDate(date){
      const startOfDay = dayjs(date[0]).startOf('day').format('YYYY-MM-DD');
      const endOfDay = dayjs(date[1]).endOf('day').format('YYYY-MM-DD');;
      return startOfDay + '   ,    ' + endOfDay;
    }
    function handleDelete(key){
      if(key=='createTime' || key=='startTime' || key=='finishTime'){
          formState[key] = [];
        }else{
          formState[key] = '';
        }
        if(allRef.value){
          (allRef.value as any).loadData({...unref(formState),pageNo:1,pageSize:10,type:state.activeMenuKey });
        }
    }
    async function handleStart(){
      const apiLimit = await createBusinessLineAuth();
      if(apiLimit.flag == 3){
        msg.warning('软件授权已过期，无法发起签署');
        return;
      }
      router.push("/business")
      // let result = await getBusinessLine({});
      // if(result.length>1){
      //   openModal(true,{
      //     isUpdate:false,
      //     record:{
      //       list: result
      //     }
      //   })
      // }else if(result.length==1){
      //   router.push({
      //     path:"/contract/start",
      //     query:{
      //       __full__:"",
      //       signReId:result[0].id
      //     }
      //   })
      //   // window.open('/#/contract/start?__full__&signReId=' + result[0].id)
      // }else if(result.length==0){
      //   msg.warning('您暂无发起权限，请联系企业管理员')
      // }
    }
    function handleStartSuccess(val){
      closeModal()
      // window.open('/#/contract/start?__full__&signReId=' + val)
      router.push({
        path:"/contract/start",
        query:{
          __full__:"",
          signReId:val
        }
      })
    }

    function handleClear(){
      createConfirm({
            title: '温馨提示', 
            content: `${'您是否确定执行清空回收站操作 ? 执行后数据将无法恢复!'}`,
            okText:'确定',
            cancelText:'取消',
            iconType: 'warning',
            onCancel(){

            },
            onOk:async ()=>{
              let result = await cleanupAll({})
              if(result){
                msg.success('清空回收站成功')
                if(allRef.value){
                  (allRef.value as any).loadData({...unref(formState),pageNo:1,pageSize:10,type:state.activeMenuKey });
                }
              }
            }
            
          })
    }

    return {
      ...toRefs(state),
      handleMenuClick,
      tenantInfo,
      handleStartSuccess,
      formState,
      options,
      handelSearch,
      clickSearch,
      allRef,
      popVisible,
      handleFilter,
      handelReset,
      handleBeforeVisibleChange,
      handelCancel,
      listTitle,
      filterCount,
      loadRuStatus,
      loadDate,
      handleDelete,
      handleStart,pageinfo,
      registerModal,userType,
      handleClear,menuStastics
    }
  }
})
</script>

<style lang="less" scoped>
.doc-content{
  width:1506px;
  margin:0 auto;
  display: flex;
  .doc-menu{
    width:220px;
    background:#f4f4f4;
    text-align: center;
    padding:20px 0px;
    box-sizing: border-box;
    border-radius: 5px;
    /* height: 100vh; */
  }
  :deep(.ant-menu){
    background:#f4f4f4;
    margin-top:20px;
    width:220px!important;
    
    li{
      font-size: 13px;
      height:30px;
      margin:0;
      line-height:30px;
      margin-bottom:2px;
      .ant-menu-title-content{
        display: flex;
        align-items: center;
        line-height:28px;
        .sub-menu-name{
          margin-left: 10px;
          display: inline-block;
          flex: 1;
        }
        .sub-menu-count{
          width: 50px;
          text-align: center;
        }
      }
    }
    .ant-menu-submenu, .ant-menu-submenu-inline{
      margin-bottom:15px;
      color:#1e1e1e;
      font-weight:700;
        
    }
    &:not(.ant-menu-horizontal) .ant-menu-item-selected{
      border-right:none;    
      background:#e9e9e9;
      color:#1e1e1e;
      font-weight:700;
    }
    &.ant-menu-light .ant-menu-item:hover{
      background:#e9e9e9;
      color:#1e1e1e;
    }
    &.ant-menu-light .ant-menu-submenu-title:hover{
      background:#f4f4f4;
      color:#1e1e1e;
      cursor:auto;
    }
  }
  .doc-list{
    flex:1;
    // margin-left:10px;
    margin-top:20px;
  }
}
.sign-filter-area{
  text-align: right;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
  padding:0 20px;
  .record-title{
    font-size: 22px;
    font-weight:550;
    color: #1e1e1e;
    margin-bottom:0;
  }
  :deep(.ant-input-affix-wrapper){
    height:32px;
  }
}
.advanced-filter{
  cursor: pointer;
}
:deep(.ant-pagination){
  width:100%;
}
.search-enter{
  position:relative;
  .search-panel{
    background:#fff;
    border:1px solid #f4f4f4;
    box-shadow: 2px 0 8px 5px rgba(29, 35, 41, 0.05);
    padding:25px 20px;
    position: absolute;
    z-index: 999;
    left: -22px;
    top: -56px;
    -webkit-user-select: none; /* Safari 和 Chrome */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* Internet Explorer */
    user-select: none; /* 标准语法 */
    cursor: grab;
    :deep(.ant-form-item){
      margin-bottom:4px;
    }
    :deep(.ant-select-selector){
      text-align:left;
    }
    :deep(.ant-form-item-control-input-content){
      display:flex;
      :deep(.ant-input-group-wrapper){
        flex:0 0 100%;
      }
      .advanced-filter{
        position: absolute;
        right: -27px;
      }
    }
    .filter-footer{
      margin-top:20px;
    }
  }
}
.filter-tags{
  padding:10px 40px;
  display:flex;
  align-items:center;
  :deep(.app-iconify){
    color:#999;
    margin-top:2px;
    cursor:pointer;
    font-size: 14px;
    margin-left: 5px;
  }
}
.list-title{
  display:flex;
}
.clear-btn{
  display: flex;
  margin-left:10px;
}
</style>

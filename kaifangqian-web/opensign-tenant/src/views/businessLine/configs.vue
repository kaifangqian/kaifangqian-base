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
  <div :class="prefixCls">
    <DocHeader :actions="actions" :docs="[{name:lineFormData.name}]" @cancel="handleCancel"/>
    <a-tabs v-model:activeKey="activeTabKey" tab-position="left" @change="handleTabSaveChange">
      <a-tab-pane key="basic" tab="基本信息设置" >
        <Basic ref="basicRef" :lineFormData="lineFormData" @change="handleFormData"/>
      </a-tab-pane>
      <a-tab-pane key="auth" tab="权限管理" >
        <AuthManage ref="authRef" :lineFormData="lineFormData" @change="handleFormData" />
      </a-tab-pane>
      <a-tab-pane key="position">
        <template #tab>
          <span>位置及参数设置  
          <a-tooltip>
            <template #title v-if="lineFormData.errorStatus==1">组织签章【自动盖章】的签署节点未指定签署位置，请先指定签署位置</template>
            <template #title v-if="lineFormData.errorStatus==3">文档填写参数存在部分参数未指定填写方，请先指定</template>
            <template #title v-if="lineFormData.errorStatus==4">
              <p>1、组织签章【自动盖章】的签署节点未指定签署位置，请先指定签署位置</p>
              <p>2、文档填写参数存在部分参数未指定填写方，请先指定</p>
            </template>
            <Icon icon="ant-design:info-circle-outlined" color="#f0a127" v-if="lineFormData.errorStatus!=2"/>
          </a-tooltip>
          </span>
        </template>
        <PositionParams ref="positionRef" :lineFormData="lineFormData" @change="handleFormData"/>
      </a-tab-pane>
      <a-tab-pane key="notice" tab="通知设置">
        <Notice :lineFormData="lineFormData" ref="noticeRef"/>
      </a-tab-pane>
      <!-- <a-tab-pane key="approve" tab="内部审批设置">
        <Approve ref="approveRef" :lineFormData="lineFormData" @change="handleFormData" />
      </a-tab-pane> -->
      <!-- <a-tab-pane key="approve" tab="回调设置">
        <CallBack ref="approveRef" :lineFormData="lineFormData" @change="handleFormData" />
      </a-tab-pane> -->
    </a-tabs>
  </div>
</template>

<script lang="ts">
import {ref, defineComponent, unref, onMounted, h, nextTick} from "vue"
import { useRouter } from 'vue-router';
import { useAppStore } from '/@/store/modules/app';
import { useDesign } from '/@/hooks/web/useDesign';
import { cloneDeep } from 'lodash-es';
import { useMessage } from '/@/hooks/web/useMessage';
import { BUSINESS_LINE_KEY } from '/@/enums/cacheEnum';
import { createLocalStorage } from '/@/utils/cache';
import { getLineConfigInfo, saveLineConfig } from '/@/api/line';
import Basic from './tabs/Basic.vue';
import AuthManage from './tabs/AuthManage.vue';
import CallBack from './tabs/CallBack.vue';
import PositionParams from './tabs/PositionParams.vue';
import Approve from './tabs/Approve.vue';
import Notice from './tabs/Notice.vue';

import DocHeader from './components/layouts/DocHeader.vue';

import { Icon } from '/@/components/Icon'


interface BtnFun {
 	 ():void   
}
interface ButtonItem {
  type:string;
  text:string;
  disabled?:boolean;
  callBack:BtnFun;
  show?:boolean;
}

export default defineComponent({
  name:"BusinessLineConfig",
  components:{
    Basic,
    AuthManage,
    PositionParams,
    Approve,
    DocHeader,
    Icon,
    CallBack,Notice
  },
  props:{

  },
  setup() {
    const recordId = ref('');
    const basicRef = ref();
    const authRef = ref();
    const positionRef = ref();
    const approveRef = ref();
    const noticeRef = ref();
    const activeTabKey = ref('basic');
    const preTabKey = ref('basic');
    const nextTabKey = ref('basic');


    const lineFormData = ref({
        name:'',
        lineNoType:'auto',
        fileList:[],
        errorStatus:0,
    })
    const actions = ref(<ButtonItem[]>[
        {
          type:'primary',
          text:'保存',
          callBack:handleSaveCurrentTab,
          show:true
        }
       
      ])
     
    const { prefixCls } = useDesign('businessline-config');
    const { createMessage: msg, createConfirm } = useMessage();
    const router = useRouter();
    const { currentRoute } = router;
    const route = unref(currentRoute);
    const query = route.query;
    const signReId = ref(query.signReId);
    // lineFormData.value.name = query.name as string;
    console.log(route,'路由信息--')
      
    const appStore = useAppStore();
    const ls = createLocalStorage();
    

    onMounted(()=>{
      console.log(route.params.signReId, '3333333')
      if(route.query.signReId){
        recordId.value = route.params.signReId as string;
        // fetch()
      }else{
        // init()
      } 
    })

    function init(){
      appStore.setProjectConfig({
        headerSetting: {
          businessTitle:'测试业务线名称1'
        },
      })
      setCacheInfo(lineFormData.value)
    }

    async function fetch(){
      let result  = await getLineConfigInfo({id:unref(recordId)});
      if(result){
        lineFormData.value = {
          ...result
        }
        appStore.setProjectConfig({
          headerSetting: {
            businessTitle:result.name
          },
        })
        setCacheInfo(result)
      }
    }

    function setCacheInfo(info){
      ls.set(BUSINESS_LINE_KEY,info)
    }

    function handleFormData(form){
      lineFormData.value = {
        ...lineFormData.value,
        ...form,
      }
    }
    //返回保存
    function handleCancel(){
      handleTabSaveChange(activeTabKey.value)
      // window.open(window.location.origin + window.location.pathname +'#/businessline/manage', '_self');
      window.open(`${window.location.origin}${window.location.pathname}#/businessline/manage`, '_self');
      // router.go(-1);
      // console.log(window.opener);
    }
    //切换保存
    function handleTabSaveChange(key){
      console.log(activeTabKey.value,key,preTabKey.value,'tabkey===== ')
      if(preTabKey.value=='basic' && key=='position'){
         activeTabKey.value = 'basic';
         basicRef.value.saveBsicData(true).then(res=>{
          if(res){
            if(basicRef.value&&basicRef.value.basicInfo.signerType==2){
                if(lineFormData.value?.fileList&&lineFormData.value.fileList.length){
                  window.open(import.meta.env.VITE_PUBLIC_PATH + '#/businessLine/position?__full__&signReId=' + unref(signReId),'_self')
                }else{
                  // activeTabKey.value = key;
                  // preTabKey.value = activeTabKey.value;
                  // activeTabKey.value =  preTabKey.value;
                  // preTabKey.value = activeTabKey.value;
                  preTabKey.value = activeTabKey.value;
                  activeTabKey.value = key;
                  // msg.warning('暂无签约文件，无法指定位置及设定参数，请上传签约文件后操作')
                  return
                }
                activeTabKey.value =  preTabKey.value;
                preTabKey.value = activeTabKey.value;
                return false
              }else{
                preTabKey.value = activeTabKey.value;
                activeTabKey.value = key;
              }
          }
         },(error)=>{
          console.log(error)
          activeTabKey.value =  preTabKey.value;
         })
      }else{
         if(preTabKey.value=='basic'){
          const basePromise = basicRef.value.saveBsicData(true)
                basePromise.then(res=>{
                  if(res){
                    preTabKey.value = activeTabKey.value;
                  }
                },(error)=>{
                  console.log(error)
                  activeTabKey.value =  preTabKey.value;
                })
                basePromise.catch(err => {
                console.log(err)
                preTabKey.value =  'basic';
                activeTabKey.value =  'basic';
              })
          }
          if(preTabKey.value=='auth'){
            authRef.value.saveAuthData(true).then(res=>{
              if(res){
                preTabKey.value = activeTabKey.value;
              }
            },(error)=>{
              console.log(error)
              activeTabKey.value =  preTabKey.value;
            });
          }
          if(key=='basic'){
            basicRef.value&&basicRef.value.getContractReInfo();
            preTabKey.value = key
          }
          if(key=='auth'){
            authRef.value&&authRef.value.getAuthInfo();
            preTabKey.value = key
          }
          if(key=='position'){
            if(basicRef.value&&basicRef.value.basicInfo.signerType==2){
              activeTabKey.value =  preTabKey.value;
              preTabKey.value = activeTabKey.value;
              if(lineFormData.value.fileList&&lineFormData.value.fileList.length){
                window.open(import.meta.env.VITE_PUBLIC_PATH +'#/businessLine/position?__full__&signReId=' + unref(signReId),'_self')
              }else{
                activeTabKey.value = key;
                preTabKey.value = activeTabKey.value;
                // msg.warning('暂无签约文件，无法指定位置及设定参数，请上传签约文件后操作')
              }

              return false
            }else{
              preTabKey.value = activeTabKey.value;
            }
          }
      }
    }
    //当前页保存
    function handleSaveCurrentTab(){
      if(activeTabKey.value=='basic'){
        nextTick(()=>{
          basicRef.value.saveBsicData().then(res=>{

          });
        })
      }
      if(activeTabKey.value=='auth'){
        nextTick(()=>{
          authRef.value.saveAuthData().then(res=>{
            
          });
        })
      }
      if(activeTabKey.value=='notice'){
        nextTick(()=>{
          noticeRef.value.saveNoticeData().then(res=>{
            
          });
        })
      }
      
    }

    function handleTabChange(activeKey){
      console.log(activeKey,'-----------')
      const cacheData = ls.get(BUSINESS_LINE_KEY)
      let lineData = getAllFormData()
      nextTabKey.value = activeKey;
      console.log(basicRef.value,JSON.stringify(cacheData),JSON.stringify(lineData))
      console.log(JSON.stringify(cacheData)==JSON.stringify(lineData))
      if(JSON.stringify(cacheData)==JSON.stringify(lineData)){
        preTabKey.value = activeTabKey.value;
        saveConfig()
      }else{
        activeTabKey.value = preTabKey.value
        createConfirm({
          iconType: 'warning',
          title: () => h('span', '操作提醒'),
          content: () => h('span','当前页面有更新，是否保存？'),
          okText:'确定',
          cancelText:'取消',
          onOk: async () => {
            saveConfig()
          },

        })
      }
    }
   

    async  function saveConfig(type?:string){
      let params = getAllFormData()
      // let result = await saveLineConfig(params);
      // if(result){
      //   if(type&&type=='back'){
          
      //   }else{
      //     activeTabKey.value = nextTabKey.value
      //   }
      //   if(params.id){
      //     delete params.id
      //   }
      //   ls.set(BUSINESS_LINE_KEY,params)
      //   msg.success('保存成功')
      // } 
      activeTabKey.value = nextTabKey.value;
      ls.set(BUSINESS_LINE_KEY,params)
      // msg.success('保存成功')
      console.log(  activeTabKey.value  , '00000000')
      if(preTabKey.value == 'basic'){
        basicRef.value.saveData(true);
      }
      if(activeTabKey.value == 'position'){
        window.open(import.meta.env.VITE_PUBLIC_PATH +'#/businessLine/position?__full__&signReId=' + unref(signReId))
      }
      preTabKey.value = cloneDeep(activeTabKey.value);
    }

    function getAllFormData(){
      let basicData = basicRef.value.getFormData();
      let authData = authRef.value&&authRef.value.getFormData();
      let positionData = positionRef.value&&positionRef.value.getFormData();
      let approveData =  approveRef.value&&approveRef.value.getFormData();
      let resultData = {
        ...basicData,
        ...authData,
        ...positionData,
        ...approveData,
      }
      delete resultData.isTrusted;
      delete resultData._vts;
      delete resultData.shiftKey;
      return resultData
    }
    
    return {
      prefixCls,
      activeTabKey,
      lineFormData,
      handleFormData,
      handleTabChange,
      basicRef,
      authRef,
      approveRef,
      positionRef,
      saveConfig,
      actions,
      handleTabSaveChange,
      handleCancel,noticeRef
    }
  }
})
</script>

<style lang="less" scoped>
 @prefix-cls: ~'@{namespace}-businessline-config';
 
 .@{prefix-cls}{
  :deep(.ant-tabs-tabpane){
    padding:0 24px;
  }
  // padding:0 25px;
  overflow: hidden;
  height: 92vh;
  :deep(.ant-tabs){
    min-height: 100vh;
    padding:20px;
    .ant-tabs-tab{
      font-size: 16px;
      /* font-weight: 550; */
    }
    .ant-card-head-title{
      font-weight: 600;
      font-size: 16px;
    }
  }
  :deep(.ant-tabs-content ){
      height:88vh;
      overflow:auto;
      padding-bottom:80px;
    }
 }
</style>

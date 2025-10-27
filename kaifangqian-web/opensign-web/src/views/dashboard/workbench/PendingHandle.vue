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
  <div>
    <Card title="待办事项" size="small">
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
          <Scrollbar width="100%" height="100%" :native="true" :noresize="true">
          <ul>
            <li v-for="(item,index) in  upcomingList" :key="index" v-if="upcomingList&&upcomingList.length">
              <div class="upcoming-info doc-"> 
                <p class="upcoming-title"  @click="handleSign(item)">{{ item.subject}}</p>
                <p class="upcoming-schedule">
                  <a-space :size="10">
                    <SvgIcon name="sign-schedule" style="line-height:20px"></SvgIcon>
                    <a-tooltip placement="top" color="#fff" :mouseEnterDelay="0.5"
                     :getPopupContainer="(triggerNode: any) => {return triggerNode.parentNode}"
                     :overlay-style="{'max-width':'800px'}">
                      <template #title>
                        <ContractListStatus :signRuId="item.signRuId" :signOrder="2" />
                      </template>
                        <span>签署方：{{item.participateNames}}</span>
                    </a-tooltip>
                  </a-space>
                </p>
                <div class="upsign-info">
                  <span class="signatory-line">
                    <div class="info-filed" >
                      <SvgIcon name="end-time" style="line-height:20px"></SvgIcon>
                      <span>签署截止时间：{{item.expireDate?item.expireDate:'--'}}</span>
                    </div>
                  </span>
                  <span class="signatory-line">
                    <div class="info-filed">
                      <SvgIcon name="contract-status" style="line-height:20px"></SvgIcon>
                      <span>{{ loadRuStatus(item.status) }}</span>
                    </div>
                  </span>
                  <span style="flex:1;">
                    <div class="info-filed" style="justify-content:right">
                      <SvgIcon name="send-line" style="line-height:20px"></SvgIcon>
                      由 {{ item.fromTenantName }} 于 {{ item.createTime}} 发起
                    </div>
                  </span>
                </div>
              </div>
              <!-- <a-button type="link" @click="handleSign(item)">{{activeTabKey=='1'?'签署':'填写'}}</a-button> -->
            </li>
            <div v-else>
              <p class="no-data">
                  <img src="~@/assets/images/no-data.png" alt="">
              </p>
            </div>
    
          </ul>
    </Scrollbar>
        </div>
    </Card>
  </div>
</template>

<script lang="ts" setup>
  
  import {ref,defineComponent, onMounted, unref} from "vue"
  import { Tag, Tabs, Card } from 'ant-design-vue';
  import { useRouter } from 'vue-router';
  import  { getMyStastics, getListMySignJob, getListMyFillInJob } from '/@/api/contract';
  import { loadRuStatus } from '/@/views/contract/document/transform';
  import { SvgIcon } from '/@/components/Icon';
  import ContractListStatus from "/@/views/contract/components/ContractListStatus.vue";
  import { Scrollbar } from '/@/components/Scrollbar';
  interface UpcomingItem{
    fromTenantName:string;
    participateNames:string;
    subject:number;
    status:string;
    createTime:string;
    expireDate:string;
  };
  
  const router = useRouter();
  const activeTabKey = ref('1')
  const upsignCount = ref(0)
  const upwriteCount = ref(0)
  const upcomingList = ref(<UpcomingItem[]>[])
  
  onMounted(()=>{
    handleTabChange('1');
    getWriteCount()
    document.addEventListener("visibilitychange", function() {
        if(!document.hidden){
            handleTabChange('1');
            getWriteCount()
        }
      })
  })
  
  function handleMore(){
    // go('/contract/doc?key=7')
    router.push({
      path:'/contract/doc',
      // name:'文档管理',
      // query:{
      //   key:'7',
      // }
    })
  }
  
  async function handleTabChange(val){
    activeTabKey.value = val;
    let result;
    if(val==1){
      result = await getListMySignJob({});
      upsignCount.value = result.total;
    }else{
      result = await getListMyFillInJob({});
      upwriteCount.value = result.total;
    }
    if(result){
      upcomingList.value = result.records;
    }
  
  }
  
  //获取填写数量
  async function getWriteCount(){
    let result  = await getListMyFillInJob({});
    if(result){
      upwriteCount.value = result.total;
    }
  }
  
  
  function handleSign(row){
    if(activeTabKey.value=='1'){
      // window.open('/#/contract/sign?__full__&signReId='+'&signRuId=' + row.signRuId + '&taskId=' + row.taskId + '&from=list')
      router.push({
        path:'/contract/sign',
        query:{
           __full__:"",
          signRuId:row.signRuId,
          taskId:row.taskId,
          from:'list'
        }
      })
      
    }else{
      router.push({
        path:'/contract/params',
        query:{
           __full__:"",
          signRuId:row.signRuId,
          taskId:row.taskId,
          from:'list',
          type:'receive'
        }
      })
      // window.open('/#/contract/params?__full__&signReId='+'&signRuId=' + row.signRuId + '&taskId=' + row.taskId +'&type=receive' + '&from=list')
    }
  }
  
  
  
  
</script>

<style lang="less" scoped>
  .upcoming-list{
    ul{
      max-height: 350px;
      /* overflow: auto; */
      padding-bottom: 40px;
      width: calc(100% - 10px);
    }
    li:nth-child(n+2){
      margin-top:10px;
    }
    li{
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 10px 15px;
      border-left: 2px solid #eee;
      box-shadow: 5px 5px 30px #0000000e;
      user-select: none;
      // background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(135deg, #EFF6FF -4%, #FFFFFF 100%);
      background: linear-gradient(135deg, #ffffff 0%, #f8faff 100%);
      &:hover{
        border-left: 2px solid #127fd2;
        box-shadow: 5px 5px 40px #0000000e;
      }
      .upcoming-title:hover{
        color: #127fd2 !important;
      }
      .upcoming-schedule:hover{
        color: #127fd2;
        cursor: pointer;
      }
      .upcoming-schedule{
        font-size:14px;
      }
     
      .upcoming-info{
        flex:1;
        .upcoming-title{
          font-size: 14px;
          color: #000;
          font-weight:550;
          cursor:pointer;
        }
        .signatory-line{
          width:220px;
          font-size: 14px;
          display: inline-block;
          line-height: 20px;
        }
        .info-filed{
          gap: 10px;
          line-height:20px;
          display: flex;
          align-items: center;
        }
        .upsign-info{
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 14px;
        }
      }
    }
  }
</style>

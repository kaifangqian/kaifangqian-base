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
  <a-spin  :spinning="loadingRef" :absolute="true" tip="加载中...">
  <div class="contract-schedule">
    <div class="title">
      <p class="pm0">签约进度</p>
    </div>
    <Scrollbar width="100%" height="100%" :native="true" :noresize="true">
    <div class="body" v-if="!loadingRef">
      <ul>
        <li class="flex">
          <span class="name">签署顺序：</span>
          <span class="value" v-if="signOrderType == 2">无序签署</span>
          <span class="value" v-if="signOrderType == 1">顺序签署</span>
        </li>
        <li v-if="(sender && sender.senderList&&sender.senderList.length>0) || sender?.writeStatus>-1">
          <span class="name">发起方</span>
          <div class="start-paty">
            <div class="sender-name">
              <a-space :size="20">
                <span class="sub-name" style="width: 250px;">{{sender.signerName}}</span>
                <a-tag v-if="sender.writeStatus>-1" class="sign-status" :color="loadSignColor(sender.writeStatus)">{{ loadWriteStatus(sender.writeStatus) }}</a-tag>
              </a-space>
            </div>
            <div class="sneder-sub">
              <li v-for="item in sender.senderList">
                <a-space :size="20">
                  <span class="sub-name" style="width: 200px;">{{item.senderName}}（{{item.senderSignType==1? '自动盖章': item.senderUserName }}）</span>
                  <!-- <a-tag color="success">待签署</a-tag> -->
                  <a-tag v-if="item.senderType == 5" class="sign-status" :color="loadApprovalColor(item.signStatus)" >{{ loadApprovalStatus(item.signStatus) }}</a-tag>
                  <a-tag v-if="item.senderType != 5" class="sign-status" :color="loadSignColor(item.signStatus)" >{{ loadSignStatus(item.signStatus) }}</a-tag>
                </a-space>
              </li>
            </div>
          </div>
        </li>
        <li v-for="(item,index) in signers">
          <span class="name" >接收方{{index+1}}</span>
          <div class="have-paty" v-if="item.signerType == 2">
            <div class="sneder-sub">
              <li>
                <a-space :size="20">
                  <span class="sub-name" style="width: 230px;">{{item.signerName}}&nbsp;&nbsp;({{item.signerExternalValue}})</span>
                  <a-tag v-if="item.writeStatus>-1" class="sign-status" :color="loadSignColor(item.writeStatus)">{{ loadWriteStatus(item.writeStatus) }}</a-tag>
                  <a-tag class="sign-status" :color="loadSignColor(item.signStatus)">{{ loadSignStatus(item.signStatus) }}</a-tag>
                  
                </a-space>
              </li>
            </div>
          </div>
          <template v-else>
            <div class="start-paty"  >
              <div class="sender-name">
                <a-space :size="20">
                  <span class="sub-name" style="width: 250px;">{{item.signerName}}</span>
                  <!-- <a-tag color="success">待签署</a-tag> -->
                  <a-tag v-if="item.writeStatus>-1" class="sign-status" :color="loadSignColor(item.writeStatus)">{{ loadWriteStatus(item.writeStatus) }}</a-tag>
                </a-space>
              </div>
              <div class="sneder-sub">
                <li v-for="subItem in item.senderList">
                  <a-space :size="20">
                    <span class="sub-name" style="width: 200px;">{{subItem.senderName}}（{{subItem.senderExternalValue}}）</span>
                    <!-- <a-tag color="success">待签署</a-tag> -->
                    <a-tag class="sign-status" :color="loadSignColor(subItem.signStatus)" >{{ loadSignStatus(subItem.signStatus) }}</a-tag>
                  </a-space>
                </li>
              </div>
            </div>
          </template>
          
        </li>
      </ul>
    </div>
    </Scrollbar>
  </div>
  </a-spin>
</template>

<script lang="ts" setup>
  import {ref,defineComponent, onMounted} from "vue"
  import {Loading} from "/@/components/Loading"
  import {  getScheduleStatus } from '/@/api/contract';
  import { loadRuStatus, loadSignColor, loadSignStatus,loadWriteStatus,loadApprovalColor,loadApprovalStatus } from '/@/views/contract/document/transform';
  import { Scrollbar } from '/@/components/Scrollbar';
  const props = defineProps({
    signRuId:{
    	type: String,
      default:""
    },
  });
  const loadingRef = ref(true);
  // if(loadingRef){
  //   return;
  // }
  const signOrderType = ref(0);
  const sender = ref();
  const signers = ref();
  getScheduleStatus({signRuId:props.signRuId}).then(res=>{
    // operatorStatus.value = res;
    // sender
    sender.value = res.signerVoList.find(e=>e.signerType == 1);
    signers.value = res.signerVoList.filter(e=>e.signerType == 2 || e.signerType == 3);
    loadingRef.value= false;
    signOrderType.value = res.signOrderType;
    
  })
  
</script>

<style lang="less" scoped>
  
  .contract-schedule{
    width: 500px;
    min-height: 200px;
    padding-bottom: 20px;
    color:#000;
    .title{
      font-size: 16px;
      font-weight: 600;
      line-height: 30px;
      border-bottom: 1px solid rgba(0,0,0,0.3);
    }
    .body{
      padding:0 20px;
      max-height: 350px;
    }
    .body ul li{
      line-height: 40px;
      font-size: 14px;
      .name{
        display: block;
        width: 80px;
        font-weight: 600;
      }
      .start-paty,.have-paty{
        background-color: rgba(0,0,0,0.05);
        height: 100%;
        padding: 10px;
        // border-radius: 5px;
      }
      .start-paty .sneder-sub{
        padding-left: 50px;
        li{
          list-style: disc;
          line-height: 30px;
        }
      }
      .sender-name{
        line-height: 30px;
      }
      .have-paty .sneder-sub{
        padding-left: 20px;
        li{
          list-style: disc;
          line-height: 30px;
        }
      }
      .sub-name{
        display: block;
        white-space: nowrap; /* 确保文本在一行内显示 */
        overflow: hidden; /* 隐藏超出容器的内容 */
        text-overflow: ellipsis; /* 使用省略号表示被截断的文本 */
      }
    }
  }
  
</style>

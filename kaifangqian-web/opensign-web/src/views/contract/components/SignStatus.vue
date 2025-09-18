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
  <div class="sign-status-container">
      <ul>
        <li v-for="(item,index) in signerSignList" :key="index">
          <div v-if="item.signerType==1">
            <div class="signer-name sender-type">
              <span class="sender-line"></span>
              <span>发起方：{{item.signerName}}</span>
            </div>
            <div v-for="(sendItem,sendIndex) in item.senderList"  :key="sendIndex" class="signer-control-info">
              <div class="signer-head">
                <div>
                  <a-badge status="default" />
                  <span>{{ sendItem.senderName +'  —  ' + '[' + (sendItem.senderSignType==1? '自动盖章': sendItem.senderUserName) + ']' }}</span>
                </div>
                <a-tag class="sign-status" :color="loadSignColor(sendItem.signStatus)">{{ loadSignStatus(sendItem.signStatus) }}</a-tag>
              </div>
            </div>
          </div>
          <div v-if="item.signerType==2">
            <div class="signer-name receive-type">
              <span class="sender-line"  style="background-color:#e48523"></span>
              <span>接收方{{ index }}：{{'个人'}}</span>
            </div>
            <div class="signer-info">
                <div>
                  <a-badge status="default" />
                  <span class="operater-name"> {{item.signerName }} </span>
                  <span> {{item.signerExternalValue  }} </span>
                </div>
                <a-tag class="sign-status" :color="loadSignColor(item.signStatus)">{{ loadSignStatus(item.signStatus) }}</a-tag>
              </div>
          </div>
          <div v-if="item.signerType==3">
            <div class="signer-name sender-type">
              <span class="sender-line" style="background-color:#48b931"></span>
              <span>接收方：{{item.signerName}}</span>
            </div>
            <div v-for="(sendItem,sendIndex) in item.senderList"  :key="sendIndex" class="signer-control-info">
              <div class="signer-head">
                <div>
                  <a-badge status="default" />
                  <!-- <span>{{ sendItem.senderName }}</span> -->
                  <span>{{ (sendItem.senderType==1?'经办人签字':'组织签章') +'  —  '  +'['+sendItem.senderName + ']'}}</span>
                </div>
                <a-tag class="sign-status" :color="loadSignColor(sendItem.signStatus)">{{ loadSignStatus(sendItem.signStatus) }}</a-tag>
              </div>
            </div>
          </div>
        </li>
      </ul>
  </div>
</template>

<script lang="ts">
import {ref, unref, defineComponent, onMounted} from "vue";
import { getOperator, getOperatorStatus } from '/@/api/contract';
import { useRouter } from 'vue-router';
import { loadSignStatus, loadSignColor } from '../document/transform';

export default defineComponent({
  name:"SignStatus",
  props:{
    signerList:{
      type:Object,
    },
  },
  setup() {
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const signRuId = route.query.signRuId;
      const signerSignList:any = ref([]);

    onMounted(()=>{
      fetch()
    })
    async function fetch(){
      // let result = await getOperator({signRuId:signRuId});
      // if(result){
      //   let receiveList = result.filter(v=>v.signerType==2 && v.operateType==2).sort((a, b) => a.operateOrder - b.operateOrder);
      //   let senderList = result.filter(v=>v.signerType == 1 && v.operateType==2).sort((a, b) => a.operateOrder - b.operateOrder);
      //   let senderOrg =  result.filter(v=>v.signerType==1 && v.operateType == 1).sort((a, b) => a.operateOrder - b.operateOrder);
      //   if(senderOrg.length){
      //     senderOrg[0].senderList = senderList;
      //   }
      //   signerSignList.value = [
      //     ...senderOrg,
      //     ...receiveList
      //   ]
      // }
        let result = await getOperatorStatus({signRuId:signRuId});
        if(result){
          signerSignList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
          signerSignList.value.map(item=>{
            if(item.signerType==1){
              item.senderList =  item.senderList.sort((a, b) => a.senderOrder - b.senderOrder)
            }
          })
        }
    }

    return {
         signerSignList,
         loadSignColor,
         loadSignStatus  
    }
  }
})
</script>

<style lang="less" scoped>
.sign-status-container{
  margin-bottom: 40px;
}


.signer-item{
  margin-bottom: 15px;
}
.signer-name{
  display: flex;
  .sign-status{
    margin-left:20px;
    padding:2px 15px;
  }
}
.sender-line{
    width:8px;
    height:20px;
    border-radius: 2px;
    margin-right:10px;
  }
.sender-type{
  font-weight: 550;
  .sender-line{
    background:#6ea9d7;
  }
}
.receive-type{
  font-weight: 550;
  .sender-line{
    background:#faa573;
  
  }
}
.signer-info{
  font-size: 12px;
  margin:15px 2px;
  display: flex;
  justify-content: space-between;
  .operater-name{
    margin:0 20px 0 5px;
  }
}
.signer-head{
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin:15px 0;
}
</style>

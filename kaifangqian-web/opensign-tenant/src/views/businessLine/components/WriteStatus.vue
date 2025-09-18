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
        <li v-for="(item,index) in signerWriteList" class="signer-item">
          <div v-if="item.signerType==1" >
            <div class="signer-name sender-type">
              <span class="sender-line"></span>
              <span>发起方：{{item.signerName}}</span>
              <a-tag v-if="item.writeStatus>-1" class="write-status" :color="loadSignColor(item.writeStatus)">{{ loadWriteStatus(item.writeStatus) }}</a-tag>
            </div>
          </div>
          <div v-else >
              <div class="signer-name receive-type">
                <span class="sender-line"></span>
                <span>接收方{{ index }}：{{ item.signerName}}</span>
                <a-tag v-if="item.writeStatus>-1" class="write-status" :color="loadSignColor(item.writeStatus)">{{ loadWriteStatus(item.writeStatus) }}</a-tag>
              </div>
              <div class="signer-info">
                <a-badge status="default" />
                <span class="operater-name"> {{item.signerName}} </span>
                <span> {{item.signerExternalValue }} </span>
              </div>
          </div>
        </li>
      </ul>
  </div>
</template>

<script lang="ts">
import {ref, defineComponent, onMounted, unref} from "vue";
import { getOperator, getOperatorStatus } from '/@/api/contract';
import { useRouter } from 'vue-router';
import { loadWriteStatus, loadSignColor } from '../document/transform';
export default defineComponent({
  name:"WriteStatus",
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
      const signerWriteList:any = ref([]);

    onMounted(()=>{
      fetch()
    })
    async function fetch(){
      let result = await getOperatorStatus({signRuId:signRuId});
      if(result){
        signerWriteList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
      }

    }

    return {
         signerWriteList,
         loadWriteStatus,
         loadSignColor 
    }
  }
})
</script>

<style lang="less" scoped>
.signer-item{
  margin-bottom: 15px;
}
.signer-name{
  display: flex;
  .write-status{
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
  margin:10px 2px;
  .operater-name{
    margin:0 20px 0 5px;
  }
}
</style>

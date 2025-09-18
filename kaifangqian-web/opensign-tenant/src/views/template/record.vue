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
  <div class="const-box">
     <OperateRecord :templateId="templateId"/>
   <!-- <a-tabs @tabClick="tabClick" v-model:active-key="tabKey" :destroyInactiveTabPane="true">
      <a-tab-pane key="Operate" tab="操作记录" :force-render="true">
        <OperateRecord :templateId="templateId"/>
      </a-tab-pane>
      <a-tab-pane key="Approve" tab="审批记录" :force-render="true">
       <ApproveRecord :templateId="templateId"/>
      </a-tab-pane>
      <a-tab-pane key="Use" tab="引用记录" >
       <SealChangeRecord :sealId="currentSeal.sealId"/>
      </a-tab-pane>
    </a-tabs> -->
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref,createVNode,h,getCurrentInstance} from 'vue';
  // import {getDefProcess} from "/@/api/bpmns"
  // import {Process} from "/@/views/signature/data/ProcessConfig";
  // import {getDocStat} from "./common/DocStatus";
  // import {getTemplateList,templateDelete,getFolderList,
  //   folderDelete,updateTemplateStatus,downloadParam,templateChange} from "./api"
  
  import {useRouter} from "vue-router"
  
  import OperateRecord from "./record/OperateRecord.vue"
  // import ApproveRecord from "./record/ApproveRecord.vue"
  
  
  export default defineComponent({
    name:'record',
    components: { 
      OperateRecord
    },
    setup() {
      const router = useRouter();
      const tabKey = ref("Operate");
      const templateId = ref("");
      function tabClick(){
        
      }
      onMounted(()=>{
        const query = router.currentRoute.value.query;
        templateId.value = query.templateId
        // console.log("templateId.value：",templateId.value);
        // fetch();
        console.log("record onMounted：");
      })
      function fetch(){
        
      }
      return{
        tabKey,tabClick,templateId
      }
    }
  })
</script>

<style lang="less" scoped>
  
  .const-box{
    padding: 0 20px;
  }
</style>

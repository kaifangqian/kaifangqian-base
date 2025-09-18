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
  <div class="seal-main" v-if="tenantInfo.authStatus === 2">
    <BasicTable @register="registerTable">
    <template #toolbar>
      <div style="text-align: right;width: 100%;">
        <a-button type="primary" @click="handleAdd">添加签名</a-button>
      </div>
    </template>
      <template #annexId="{record}">
        <div class="preview-img">
          <img :src="record.image"/>
        </div>
      </template>
      <!-- <template #isDefault="{record}">
        {{record.isDefault === 1?'是':'否'}}
      </template> -->
      
      <template #action="{ record }">
        <!-- <a-button type="link" size="small" @click="setSignDefault(record.sealId)">设为默认</a-button> -->
        <a-button type="link" size="small" @click="signDeleteInfo(record.sealId)">删除</a-button>
      </template>
    </BasicTable>
     <AddSignatureModal  @register="registerModal" @success="handleSuccess"/>
     
  </div>
  <Unauthenticated :index="2"  class="seal-main" v-if="tenantInfo.authStatus !== 2" />
    
</template>


<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { BasicTable, useTable} from '/@/components/Table';
  import {registerColumns,registerSearchFormSchema} from "./data"
  import { message } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  // import {getDocList} from "/@/api/signature/doc"
  
  // import {getDocStat} from "./common/DocStatus";
  import {useRouter} from "vue-router";
  import AddSignatureModal from "./modal/AddSignatureModal.vue"
  import { getImgBase64} from '/@/api/sys/upload'; 
  
  import {signList,signDefault,signDelete} from "./api"
  import Unauthenticated from "/@/views/signature/authorisation/modal/Unauthenticated.vue";
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    name:'App',
    components: { 
       BasicTable,AddSignatureModal,Unauthenticated
    },
    setup() {
      const router = useRouter();
      const [registerTable,{reload}] = useTable({
        title: '',
        api: signList,
        columns:registerColumns,
        fetchSetting:{
          listField:'records'
        },
        immediate:true,
        useSearchForm: false,
        isTriggerSelect:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: false,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
        afterFetch:beforeFetch
      })
      const userStore = useUserStore();
      const tenantInfo = ref({ ...userStore.getTenantInfo });
      async function beforeFetch(params){
       for(var i = 0;i<params.length ;i++){
         const result = await getImgBase64({imgId:params[i].annexId});
         params[i].image = result.image;
       }
      }
      const [registerModal, { openModal,closeModal }] = useModal();

      function handleShowInfo(data){
          openModal(true,{
            isUpdate:true,
            record:{
              ...data,
              view:true
            }
          })
      }
      function handleRecord(data){
        openModal(true,{
          isUpdate:true,
          record:{
            ...data,
          }
        })
      }
      function enableDisableApp(flag,id){
        
      }
      function handleSuccess(){
        reload();
      }
      function handleAdd(){
        openModal(true,{
          isUpdate:false,
        })
      }
      async function setSignDefault(sealId){
        // const result = await getImgBase64({imgId:annexId});
        // console.log(result);
        // return result.image;
        const result = await signDefault({sealId:sealId});
        if(result.code == 200){
          reload();
           message.success("设置默认签名成功！");
         }
      }
      async function signDeleteInfo(sealId){
        const result = await signDelete({sealId:sealId});
        if(result.code == 200){
          reload();
           message.success("设置删除成功！");
         }
      }
      return{
        registerTable,handleShowInfo,handleRecord,handleAdd,setSignDefault,
        enableDisableApp,registerModal,handleSuccess,signDeleteInfo,userStore,tenantInfo
      }
    }
  })
</script>

<style lang="less" scoped>
  .seal-main{
    width: 1080px;
    margin: 0 auto;
    height:calc(100vh - 160px);
    overflow:auto;
    :deep(.ant-pagination){
      position:sticky;
      width:100%;
      bottom:-1px;
    }
  }
  .preview-img img{
   height: 80px;
   padding: 8px;
  //  width: 80px;
  }
  
</style>

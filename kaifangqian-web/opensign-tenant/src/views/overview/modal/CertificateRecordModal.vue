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
     <BasicModal @register="registerModal" title="企业认证记录"> 
      <BasicTable @register="registerTable">
        <template #authStatus="{record}">
          <a-tag :color="loadCertificationStatus(record.authStatus)">{{ loadCertificationMixText(record) }}</a-tag>
        </template>
        <template #authType="{record}">
         <span>{{ loadCertificationAuthType(record.authType) }}</span>
        </template>
        <template #realItem="{record}">
         <span>{{ loadCertificationRealItemType(record.realItem) }}</span>
        </template>
        <template #action="{record}">
        <a-button type="link" @click="handleSee(record)">查看</a-button>
        </template>
        
      </BasicTable>
     </BasicModal>
  </div>
</template>

<script lang='ts'>
  import { defineComponent,ref, unref  } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useUserStore } from '/@/store/modules/user';
  import { BasicTable, useTable, } from '/@/components/Table';
  import { recordColumn } from '../data';
  import { getEnterpriseAuthLog } from '/@/api/sys/user'; 
  import { loadCertificationStatus,  loadCertificationAuthType,loadCertificationRealItemType } from '/@/utils/StatusToName'

  export default defineComponent({
    name: 'RecordCer',
    components:{
      BasicModal,
      BasicTable
    },
    setup(_, { emit }){
      const tenantId = ref('');

      const userStore = useUserStore();
      const userInfo =  userStore.getUserInfo;
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1400,
          cancelText:'关闭' 
        });
        tenantId.value = data.record.tenantId;
        console.log(tenantId,'租户oid')
        reload({
          //   searchInfo:{
          //   tenantId:unref(tenantId)
          // }
        })
      });
      const [registerTable,{ reload }] = useTable({
        title: '',
        titleHelpMessage: [],
        immediate:false,
        columns: recordColumn,
        api:getEnterpriseAuthLog,
        fetchSetting:{
          listField:'records'
        },
        rowKey:'id',
        useSearchForm: false,
        dataSource:[],
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        isTriggerSelect:false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:false,setting:false,size:false},
        beforeFetch:beforeFetch
      });
      function beforeFetch(params){
        params.tenantId = tenantId.value;
      }
      function handleSee(record){
        const token = userStore.getToken;
        let authInfo = userInfo.authInfo
        let appInfo = {
            token:token,
            appCode:authInfo.appCode,
            appId:authInfo.appId,
            departId:userInfo.loginDepartId,
            id:record.id
          }
          let paramsString = new URLSearchParams(appInfo).toString();
          window.open(authInfo.appAddress + '/#/enterprise/detail' + '?' + paramsString,'_self')
      }

      function loadCertificationMixText(record){
        if(record.authStatus==0 || !record.authStatus ){
          return '未认证'
        }else if(record.authStatus == 1 && (record.authType == 1 ||  record.authType == 3) && record.realItem == 1){
          return '认证审核中'
        }else if(record.authStatus == 1 && (record.authType == 1 ||  record.authType == 3) && record.realItem != 1){
          return '变更认证审核中'
        }else if(record.authStatus == 1 && record.authType == 2){
          return '变更认证审核中'
        }else if(record.authStatus == 2 && record.authType == 1){
          return '认证通过'
        }else if(record.authStatus == 2 && record.authType == 2){
          return '变更认证通过'
        }else if(record.authStatus == 2 && record.authType == 3){
          return '认证通过'
        }else if(record.authStatus == 3 && record.authType == 1){
          return '认证审核失败'
        }else if(record.authStatus == 3 && record.authType == 2){
          return '变更审核失败'
        }else if(record.authStatus == 3 && record.authType == 3){
          return '认证审核失败'
        }
      }

      return { 
        registerModal,
        registerTable,
        loadCertificationStatus,
        loadCertificationMixText,
        loadCertificationAuthType,
        handleSee,
        loadCertificationRealItemType,
      }
    }
  })

</script>
<style lang="less" scoped>
:deep(.ant-pagination){
  position: absolute;
}
 
</style>

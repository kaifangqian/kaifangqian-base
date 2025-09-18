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
  <div class="ent-container" style="padding:0 0;">
    <a-alert type="info" show-icon>
        <template #message>
          <h1>温馨提示</h1>
          <ul>
            <li>API接口使用RSA算法（2048位）对请求数据进行签名和验签，在为开发者开通授权凭证时，需要开发者先自行生成 RSA 密钥对（包括公钥和私钥），其中私钥保留在开发者处，公钥绑定在开发者授权凭证上；</li>
            <li>RSA算法密钥对生成：开发者可使用密钥对生成工具自行生成；</li>
            <li>推荐工具：<a href="https://opendocs.alipay.com/common/02kipk?pathHash=0d20b438" target="_blank">支付宝开放平台密钥工具</a></li>
          </ul>
        </template>
      </a-alert>
    <BasicTable @register="registerTable">
      <template #toolbar>
          <a-button type="primary" @click="handleAdd">新增凭证</a-button>
      </template>
        <template #developerName="{record}">
          <div style="display: flex;">
            <div style="flex: 1;display:flex;align-items: center;">
            {{record.developerName}}
           <!-- <a-button type="link" @click="copyToken(record)">
              <template #icon><CopyOutlined /></template>
            </a-button> -->
            <a-tooltip placement="top" trigger="hover">
                <template #title>
                 <span>点击复制token</span>
               </template>
              <a-button type="link" @click="copyToken(record)">
                <template #icon><CopyOutlined /></template>
              </a-button>
            </a-tooltip>
            </div>
            <!-- <div style="width: 50px;">
              <a-button type="link" @click="copyToken(record)">
                <template #icon><CopyOutlined /></template>
              </a-button>
            </div> -->
          </div>
        </template>
        
        <template #status="{record}">
            <template v-if="record.status == 1">
              <a-tag color="green">启用</a-tag>
            </template>
            <template v-if="record.status == 0">
              <a-tag color="red">停用</a-tag>
            </template>
        </template>
        <template #action="{record}">
          
          <a-popconfirm
            v-if="record.status == 1"
            title="是否停用该授权?"
            ok-text="是"
            cancel-text="否"
            @confirm="updateStatus(record,0)">
              <a-button type="link">停用</a-button>
          </a-popconfirm>
          <a-popconfirm
            v-if="record.status == 0"
            title="是否启用该授权?"
            ok-text="是"
            cancel-text="否"
            @confirm="updateStatus(record,1)">
              <a-button type="link">启用</a-button>
          </a-popconfirm>
          <a-button type="link" @click="handleEdit(record)">编辑</a-button>
        </template>
        
    </BasicTable>
  </div>
  <AddAuthorizationModal @register="registerModal" @success="handleSuccess"></AddAuthorizationModal>
</template>

<script lang="ts">
  import {ref,defineComponent,onMounted} from "vue"
  import { BasicTable,useTable } from '/@/components/Table';
  import Icon from "/@/components/Icon";
  import { message  } from 'ant-design-vue';
  import { authorizationColumn,authorizationFormSchema } from './data';
  import { developerManageList,developerManageEdit } from '/@/api/interface';
  import AddAuthorizationModal from "./modal/AddAuthorizationModal.vue"
  import { useModal } from '/@/components/Modal';
  import { cloneDeep } from 'lodash-es';
  import { CopyOutlined } from '@ant-design/icons-vue';
  
  import { getSystemLimit} from '/@/api/license';
  export default defineComponent({
    name:"authorization",
    components:{
      Icon,CopyOutlined,
      BasicTable,AddAuthorizationModal
    },
    setup() {
      
      const [registerModal, { openModal }] = useModal();
      const [registerTable,{reload,getPaginationRef}] = useTable({
          title: '',
          titleHelpMessage: [],
          api: developerManageList,
          columns:authorizationColumn,
          immediate:true,
          fetchSetting:{
            listField:'records'
          },
          formConfig: {
            labelWidth: 80,
            schemas: authorizationFormSchema,
          },
          rowKey:'id',
          useSearchForm: false,
          showIndexColumn: false,
          canResize: false,
          isTriggerSelect:false,
          striped:false,
          bordered:false,
          showTableSetting: false,
          tableSetting: { fullScreen: false ,redo:true,setting:false,size:false,align:'right'},
          pagination:true,
      });
      const apiLimit:any = ref();
      onMounted(() => {
        // console.log(props.signerList,'参与人---')
        initData();
      });
      async function initData(){
        const sysLimit = await getSystemLimit();
        apiLimit.value = sysLimit;
      }
      async function handleAdd(){
        const pagination:any = getPaginationRef();
        if(!apiLimit.value.apiAuthorizationUseFlag){
          window.open("https://www.yuque.com/huxin-ch41t/kaifangqian/gwp688fg4i7sivu1");
          return;
        }
        if(apiLimit.value.apiAuthorizationCeiling >=pagination.total){
           openModal(true,{
             isUpdate:false
           })
        }else{
          message.warning(`只能创建${apiLimit.value.apiAuthorizationCeiling}个接口授权凭证`)
        }
      }
      async function updateStatus(record,status){
        // record.status = status;
        const data = cloneDeep(record);
        data.status = status;
        const result = await developerManageEdit(data);
        if(result.success){
          reload();
          const msg = `${status==1?"启用成功":'停用成功'}`;
          message.success(msg);
        }else{
          message.warning(result.message);
        }
        console.log(result);
      }
      function handleEdit(item){
        openModal(true,{
          isUpdate:true,
          record:item
        })
      }
      function handleSuccess(){
        reload();
      }
      function copyToken(record){
        const range = document.createRange();
        const selection:any = window.getSelection();
        const element = document.createElement('span');
        element.textContent = record.token;
        document.body.appendChild(element);
        range.selectNodeContents(element);
        selection.removeAllRanges();
        selection.addRange(range);
        document.execCommand('copy');
        document.body.removeChild(element);
        message.success("复制成功");
      }
      return {
        registerTable,registerModal,handleSuccess,handleAdd,handleEdit,updateStatus,copyToken,
        // loadCerStatus, loadCerAlgorithmType, loadCerType

      }
    }
  })
</script>

<style lang="less" scoped>
</style>

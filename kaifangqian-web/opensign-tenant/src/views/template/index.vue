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
  <div class="template-main">
    <div class="template-folder">
      <BasicTree
        ref="basicTree"
        title=""
        :toolbar="false"
        :checkable="false"
        :isRemoteSearch="true"
        :toggleSwitch="true"
        :treeData="treeData"
        :fieldNames="fieldNames"
        @changePanelShow="changePanelShow"
        @select="onTreeSelect"
        :actionList="actionList"
        v-model:expandedKeys="treeSelectedKeys"
        :beforeRightClick="getRightMenuList"
      >
      <template #action>
          <!-- <a-button  type="default"  :class="[prefixCls+'-action']">
            <Icon icon="ant-design:apartment-outlined" />
          添加组织</a-button>
          <a-button type="default"  :class="[prefixCls+'-action']">
            <Icon icon="ant-design:deployment-unit-outlined" />
          添加部门</a-button> -->
          <div class="file-group-add">
            <span>文件分组</span>
            <span class="add-folder" @click="handleAdd">
              <PlusOutlined />
            </span>
          </div>
      </template>
      
      </BasicTree>
    </div>
    <div class="template-table">
      <BasicTable @register="registerTable">
        <template #toolbar>
          <div style="text-align: right;width: 100%;">
            <a-button type="primary" @click="createdTemplate">模板制作</a-button>
            <!-- <a-button type="info">导出列表</a-button> -->
          </div>
        </template>
        <template #developerName="{record}">
          <div style="display: flex;">
            <div style="flex: 1;">{{record.developerName}}</div>
            <div style="width: 100px;">
              <a href="javascript:;" @click="copyToken(record)">复制</a>
            </div>
          </div>
        </template>
        
        <template #templateStatus="{ record }">
          <a-tag :color="getDocStat(record.templateStatus).color">{{getDocStat(record.templateStatus).label}}</a-tag>
        </template>
        <template #templateType="{ record }">
          <a-tag v-if="record.templateType==1" color="success">有参数</a-tag>
          <a-tag v-if="record.templateType==2">无参数</a-tag>
        </template>
        
        <template #templateName="{ record }">
        <!--  <a-space :size="5">
            <a-tag v-if="record.templateType==1" color="success">有参数</a-tag>
            <a-tag v-if="record.templateType==2">无参数</a-tag>
            <span>{{record.templateName}}</span>
          </a-space> -->
          
          <div style="display: flex;">
            <div style="flex: 1;display:flex;align-items: center;">
              <span>{{record.templateName}}</span>
              <a-tooltip placement="top" trigger="hover">
                  <template #title>
                   <span>点击复制ID</span>
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
        
        
       <!-- <template #businessType="{ record }">
          {{getBusinessType(record.businessType).label}}
        </template> -->
        
        <template #action="{ record }">
          <!-- 查看 ｜撤回｜作废｜下载文档｜下载审批单｜删除 -->
          <a-button type="link" size="small" @click="templatePreview(record)">查看</a-button>
          <a-button type="link" size="small" @click="updateTemplate(record)">编辑</a-button>
          <!-- <a-button type="link" size="small" @click="changeTemplate(record)" :disabled="!(record.templateStatus == 3 ||record.templateStatus == 4)" >变更</a-button> -->
          <a-button type="link" size="small" @click="moveTemplate(record)" :disabled="record.templateStatus == 1">移动</a-button>
          <a-button type="link" size="small" @click="templateLog(record)">日志</a-button>
          <a-popconfirm v-if="record.templateStatus == 3"
              title="确认是否启用？"
              ok-text="启用"
              cancel-text="取消"
              @confirm="enabledTemplate(record)">
              <a-button type="link" size="small">启用</a-button>
          </a-popconfirm>
          <a-popconfirm v-else
              title="确认是否停用"
              ok-text="停用"
              cancel-text="取消" :disabled="record.templateStatus != 4"
              @confirm="unenabledTemplate(record)">
              <!-- <a-button type="link" size="small" v-if="record.templateStatus == 4">停用</a-button> -->
              <a-button type="link" size="small"  :disabled="record.templateStatus !== 4">停用</a-button>
          </a-popconfirm>
          <a-button type="link" size="small" :disabled="!(record.templateType ==1 &&(record.templateStatus == 4 || record.templateStatus == 3)) " @click="downloadParamInfo(record)">下载参数</a-button>
          <a-popconfirm
          :disabled="!(record.templateStatus == 3 || record.templateStatus == 2 ||record.templateStatus == 1)"
              title="确认是否删除？"
              ok-text="确定"
              cancel-text="取消"
              @confirm="deleteTemplateInfo(record)">
              <a-button type="link" size="small"  :disabled="!(record.templateStatus == 3 || record.templateStatus == 2 || record.templateStatus == 1)" >删除</a-button> 
            </a-popconfirm>
        </template>
      </BasicTable>
    </div>
  </div>
  <!-- <AppAuthFormModal  @register="registerModal" @success="handleSuccess"></AppAuthFormModal> -->
  <EditTemplateModal @register="registerEditTemplateModal" @success="handleSuccess"></EditTemplateModal>
  <AddTemplateModal @register="registeraddTemplateModal" @success="handleSuccess"></AddTemplateModal>
  <MoveTemplateModal @register="registerMoveTemplateModal" @success="handleSuccess"></MoveTemplateModal>
  
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref,createVNode,h,getCurrentInstance} from 'vue';
  import { BasicTable, useTable} from '/@/components/Table';
  import {registerColumns,registerSearchFormSchema,getDocStat} from "./data"
  import { message,Modal } from 'ant-design-vue';
  import { BasicTree,TreeItem,TreeActionItem,ContextMenuItem} from '/@/components/Tree/index';
  import { DownOutlined, EllipsisOutlined ,TeamOutlined,ExclamationCircleOutlined,CopyOutlined } from '@ant-design/icons-vue';
  import { useDesign } from '/@/hooks/web/useDesign'
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { useModal } from '/@/components/Modal';
  import {getDefProcess} from "/@/api/bpmns"
  import {Process} from "/@/views/signature/data/ProcessConfig";
  // import {getDocStat} from "./common/DocStatus";
  import {useRouter} from "vue-router";
  import {getTemplateList,templateDelete,getFolderList,
    folderDelete,updateTemplateStatus,downloadParam,templateChange,unenabled,enabled} from "./api"
  // import {getBusinessType} from "../doc/data"
  import { getToken } from '/@/utils/auth';
  
  import { createTemplateAuth } from '/@/api/license';
  
  import AddTemplateModal from "./modal/AddTemplateModal.vue";
  import MoveTemplateModal from "./modal/MoveTemplateModal.vue";
  import EditTemplateModal from "./modal/EditTemplateModal.vue";
  
  export default defineComponent({
    name:'App',
    components: { 
       BasicTable,BasicTree,PlusOutlined,AddTemplateModal,MoveTemplateModal,EditTemplateModal,
       CopyOutlined
    },
    setup() {
      window.name = "TemplateManage"
      const { prefixCls } = useDesign('organize');
      const router = useRouter();
      const [registerTable,{reload,setProps}] = useTable({
        title: '',
        api: getTemplateList,
        columns:registerColumns,
        fetchSetting:{
          listField:'records'
        },
        formConfig: {
          labelWidth: 120,
          schemas:registerSearchFormSchema,
        },
        immediate:true,
        useSearchForm: true,
        isTriggerSelect:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: false,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
        beforeFetch:beforeFetch
      })
      const selectValue = ref("");
      const fieldNames ={children:'children', title:'name', key: 'key' }
      const checkedKeys = ref<Array<string | number>>([]);
      const expandedKeys = ref([]);
      const treeSelectedKeys = ref<string[]>([""]);
      const baseTree:TreeItem = {
        name:"全部",
        key:"",
        value:"",
        children:[]
      };
      const treeData = ref<TreeItem[]>([]);
      const [registeraddTemplateModal, { openModal,closeModal }] = useModal();
      const [registerMoveTemplateModal, { openModal:openMoveModal,closeModal:closeMoveModal }] = useModal();
      const [registerEditTemplateModal, { openModal:openEditModal,closeModal:closeEditModal }] = useModal();
      
      
      function beforeFetch(params){
        if(params.recordTime){
          params.beginTime = params.recordTime[0];
          params.endTime = params.recordTime[1];
          params.recordTime = undefined;
        }
      }
      // const [registerModal, { openModal,closeModal }] = useModal();
      function handleShowInfo(data){
          openModal(false,{
            isUpdate:true,
            record:{
              ...data
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
      async function handleSuccess(){
        await loadFolder();
      }
      function handleAdd(){
        openModal(true,{
          record:{},
          treeData:treeData.value,
          isUpdate:false
        })
      }
      async function createdTemplate(){
        const res = await createTemplateAuth();
        // console.log("start",result);
        if(res.flag){
          router.push({
            path: '/template/created',
            query:{
              from:"list",
              __full__:""
            }
          })
        }else{
          // 监控模板数量，到达上限后不允许新增，提示：“只能创建N个模板”
          message.warning(`只能创建${res.count}个模板`)
        }
        
        
      }
      
      function editTemplate(row){
        const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
        const token = getToken() as string;
        // //bpmns/form/detail?taskId=ac6ea435-2b16-4172-aa24-7c98ded60357&disabled=false token=${token}
        const url = `${base_npmns}/#/bpmns/form/detail?businessId=${row.id}&disabled=true&type=track&token=${token}`; 
        window.open(url);
      }
      function changePanelShow(val){
          //emit('changePanelShow',val)
      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
          checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(key,e){
        //console.log(key,e);
        const templateFolderId = key[0]?key[0]:null;
          setProps({
            searchInfo:{
              templateFolderId:templateFolderId
            }
          })
        reload();
      }
      function moveTemplate(row){
        openMoveModal(true,{
          record:row,
          treeData:treeData.value[0].children,
          isUpdate:false
        })
      }
      async function loadFolder(){
        const result = await getFolderList({});
        treeData.value = [];
        baseTree.children = result;
        treeData.value = [baseTree];
        expandedKeys.value = []
        reload();
      }
      const actionList: TreeActionItem[] = [
        {
          render: (node) => {
            return h(EllipsisOutlined, {
              class: 'ml-2',
              onClick: () => {
                getRightMenuList(node)
              },
            });
          },
        },
      ];
      function getRightMenuList(node: any,e?): ContextMenuItem[] {
        console.log(node,'----ssss---')
        return [
          {
            label: '删除',
            handler: () => {
              Modal.confirm({
                title: '提示',
                icon: createVNode(ExclamationCircleOutlined),
                content: createVNode('div', { style: 'color:red;' }, `确认删这个文件夹吗？删除后将无法恢复`),
                onOk() {
                  folderDelete({folderIdList:node.key}).then(res=>{
                    if(res.code == 200){
                      message.success(`文件夹删除成功`);
                      loadFolder();
                    }else{
                      message.warning(res.message);
                    }
                  });
                },
                onCancel() {
                },
                class: 'custom-confirm',
              });
            },
            //auth:['authcategory:edit']
            icon: 'ant-design:delete-filled',
          },
          {
            label: '编辑',
            handler: () => {
              console.log(node,"---");
              openModal(true,{
                record:{
                  name:node.label,
                  parentFolderId:node.parentFolderId,
                  key:node.key
                },
                treeData:treeData.value,
                isUpdate:true
              })
            },
            icon: 'ant-design:edit-filled',
          },
        ];
      }
      async function changeTemplate(row:any){
        const changeResult = await templateChange({templateId:row.id});
        if(changeResult.code == 200){
          getDefProcess({processCode:Process.TemplateChange}).then(result=>{
            if(result && result.length>0){
              const token = getToken() as string;
              const flowId = result[0].id;
              const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
              let formMap = JSON.stringify({
                "RESRUN_FLOW_OPERATE_TYPE":"",
                "RESRUN_FLOW_OPERATE_ID":"",
                "signTemplateLogApply":changeResult.result.templateApplyId,
              })
              const url = `${base_npmns}/#/bpmns/initiate/index?flowId=${flowId}&formMap=${formMap}&token=${token}`;
              window.open(url);
            }else{
              message.warning("流程未启用或暂无可用流程使用")
            }
          })
        }
      }
      function templatePreview(row){
        router.push({
          path: '/template/preview',
        	query:{
            __full__:"",
            templateId:row.id,
        	}
        })
        // window.open(routeUrl.href, '_blank');
      }
      function updateTemplate(row){
        /* router.push({
          path:"/template/created",
          query:{
            templateId:row.id,
             __full__:"",
          }
        }); */
        router.push({
          path: '/template/created',
        	query:{
            __full__:"",
            templateId:row.id,
        	}
        })
        // window.open(routeUrl.href, '_blank');
        
      }
      function deleteTemplateInfo(row){
        templateDelete({templateId:row.id}).then(res=>{
          message.success("删除成功");
          reload();
        })
      }
      function disabledTemplate(row){
        var state = row.templateStatus==3?4:3;
        // sign/template/status
        updateTemplateStatus({
          id:row.id,
          templateStatus:state
        }).then(res=>{
          message.success(`${state==3?'启用成功':'停用成功'}`);
          reload();
        })
      }
      
      function unenabledTemplate(row){
        unenabled({
          templateId:row.id
        }).then(res=>{
          message.success(`停用成功`);
          reload();
        })
      }
      function enabledTemplate(row){
        enabled({
          templateId:row.id
        }).then(res=>{
          message.success(`启用成功`);
          reload();
        })
      }
      
      
      function templateLog (row){
        router.push({
          path: '/template/record',
        	query:{
            templateId:row.id,
        	}
        })
        // window.open(routeUrl.href, '_blank');
        
      }
      async function downloadParamInfo(row){
        const result = await downloadParam({templateId:row.id})
        let link = document.createElement('a');
        link.href = 'data:application/json;charset=utf-8,' + JSON.stringify(result);
        // link.download = row.templateCode + ".json";
        link.setAttribute('download', row.templateCode + '.json')
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }
      onMounted(()=>{
        loadFolder();
      })
      function copyToken(record){
        const range = document.createRange();
        const selection:any = window.getSelection();
        const element = document.createElement('span');
        element.textContent = record.id;
        document.body.appendChild(element);
        range.selectNodeContents(element);
        selection.removeAllRanges();
        selection.addRange(range);
        document.execCommand('copy');
        document.body.removeChild(element);
        message.success("复制成功");
      }
      return{
        registerTable,handleShowInfo,handleRecord,handleAdd,fieldNames,deleteTemplateInfo,
        enableDisableApp,registeraddTemplateModal,handleSuccess,getDocStat,
        createdTemplate,checkedKeys,onSelectChange,changePanelShow,registerEditTemplateModal,
        treeData,onTreeSelect,prefixCls,expandedKeys,editTemplate,selectValue,disabledTemplate,
        moveTemplate,registerMoveTemplateModal,actionList,getRightMenuList,updateTemplate,templatePreview,
        downloadParamInfo,changeTemplate,templateLog,unenabledTemplate,enabledTemplate,treeSelectedKeys,
        copyToken
      }
    }
  })
  
  
</script>

<style lang="less" scoped>
  .template-main{
    display: flex;
    
    .template-folder{
      /* width: 300px; */
      height: calc(100% - 118px);
      border: 1px;
    }
    .template-table{
      flex: 1;
    }
    .file-group-add{
      width:100%;
      height: 100%;
      // line-height: 30px;
      font-weight: 600;
      padding-left:20px;
      padding-bottom: 8px;
      text-align: left;
      border-bottom: 1px solid #f6f6f6;
      position: relative;
    }
    .add-folder{
      position: absolute;
      right: 10px;
      top:0;
      cursor: pointer;
    }
  }
  
</style>

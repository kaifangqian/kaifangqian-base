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
  <div class="apply-content">
    <Loading :loading="loading" :absolute="false" />
    <a-form  :model="applyFrom" name="basic" :labelCol="{ style: 'width: 100px' }"
      aut="off" ref="selaFromRef">
      <a-row>
        <a-col :span="7">
            <a-form-item label="模板编号" name="templateCode"
                :rules="[{ required: true, message: '请输入模板编号' }]" >
              <a-input v-model:value="applyFrom.templateCode" size="middle" class="input-width"
               placeholder="请输入模板编号" />
            </a-form-item>
        </a-col>
        <a-col :span="7">
            <a-form-item label="模板名称" name="templateName"
                :rules="[{ required: true, message: '请输入模板名称' }]" >
              <a-input v-model:value="applyFrom.templateName" size="middle" class="input-width" 
              placeholder="请输入模板名称" />
            </a-form-item>
        </a-col>
      </a-row>
        <a-row>
          <a-col :span="7">
              <a-form-item label="模板类型" name="templateType"
                  :rules="[{ required: true, message: '请选择模板类型' }]" >
                <a-radio-group v-model:value="applyFrom.templateType" >
                    <a-radio :value="1">有参数模板</a-radio>
                    <a-radio :value="2">无参数模板</a-radio>
                  </a-radio-group>
              </a-form-item>
          </a-col> 
        </a-row>
        <a-row>
          <a-col :span="7">
              <a-form-item label="文件上传" name="annexId"
                  :rules="[{ required: true, message: '请选上传模板文件' }]">
                <a-upload  :max-count="1" class="custom-upload-file-list"
                   v-model:file-list="fileList"
                   name="file"
                   list-type="picture"
                   :isImageUrl="showUploadImg"
                   action="/resrun-paas/file"
                   :headers="uploadHeaders"
                   :before-upload="docBeforeUpload"
                   @change="handleChange"
                   :accept="docAccept">
                   <a-button type="primary">
                     <UploadOutlined/>
                     <span v-if="fileList.length == 0">本地上传</span>
                     <span v-else>重新上传</span>
                   </a-button>
                 </a-upload>
              </a-form-item>
          </a-col>
          <a-col :span="7">
            <a-button type="link" @click="setControl" v-if="applyFrom.templateType == 1">设置模板控件</a-button>
          </a-col>
        </a-row>
        <a-row >
          <a-col :span="7">
            <a-form-item label="管理员" name="managerList"
                :rules="[{ required: true, message: '请选择管理员' }]" >
              
              <template :key="item" v-for="(item,index) in applyFrom.managerList" >
                <a-tag  :closable="!formDisabled" @close="tagHandleClose(1,item)">{{item.authRelationName}}</a-tag>
              </template>
              <a-button type="link" @click="selectAdmin(1,'模板管理员')" v-if="!formDisabled">选择管理员</a-button>
              
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col :span="7">
            <a-form-item label="模板使用者" name="userList"
                :rules="[{ required: true, message: '请选择模板使用者' }]" >
              <template :key="item" v-for="(item,index) in applyFrom.userList" >
                <a-tag  :closable="!formDisabled" @close="tagHandleClose(2,item)" >{{item.authRelationName}}</a-tag>
              </template>
              <a-button type="link"  @click="selectAdmin(2,'模板使用者')" v-if="!formDisabled">选择使用者</a-button>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row >
          <a-col :span="7">
            <a-form-item label="模板说明" name="note">
              <a-textarea  v-model:value="applyFrom.note" size="middle" :autosize="{ minRows: 3, maxRows: 3 }" :rows="3" 
               class="input-width" />
            </a-form-item>
          </a-col>
        </a-row>
    </a-form>
    <div style="padding-left:100px">
      <a-space :size="10">
        <a-button type="info" @click="cancelPage">取消</a-button>
        <a-button type="primary" @click="saveDataLoading">保存</a-button>
        <!-- <a-button type="primary" @click="startProcess">制作完成</a-button> -->
      </a-space>
    </div>
  </div>
  <TabTreeModal @register="registerUserModal" @success="userHandleSuccess" />
</template>  

<script lang="ts">
  import { defineComponent,reactive, onMounted, ref } from 'vue';
  
  import { useModal } from '/@/components/Modal';
  import { UploadOutlined } from '@ant-design/icons-vue';
  import { message,Upload } from 'ant-design-vue';
  import { getToken } from '/@/utils/auth';
  import {useRouter} from "vue-router";
  import { enabledList } from '../signature/seal/api';
  import { Loading } from '/@/components/Loading';
  import type { UserInfo } from '/#/store';
  
  // import {businessType} from "../doc/data"
  import { useUserStore } from '/@/store/modules/user';
  import { useAppStore } from '/@/store/modules/app';
  import { getAppEnvConfig } from '/@/utils/env';
  import {saveTemplate,getTemplateInfo,templateVerifyControl} from "./api"
  import {docAccept} from "/@/utils/DocumentUploadAccept"
  
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import { getRoleTreeList,getAllRoleTreeListForSelect, getUserByRoleId } from '/@/api/sys/role'; 
  import { getUserList } from '/@/api/demo/system'; 
  import { getAllDeptTreeForSelect, getDeptNoCountTree} from '/@/api/sys/dept';
  
  export default defineComponent({
    name:"TemplateCreated",
    components:{
      UploadOutlined,TabTreeModal,Loading
    },
    props:{
      formKey:{
        type:String,
      },
      formId:{
        type:String,
      }
    },
    setup(props){
      const applyFrom = ref<any>({
        templateCode:null,
        templateName:"",
        templateType:1,
        annexId:"",
        templateId:null,
        authVoList:[],
        managerList:[],
        userList:[],
        submitFlag:2,
        note:""
      });
      const loading = ref(false);
      const appUrl = ref("");
      const fileList = ref<any>([]);
      const seals = ref([]);
      const {VITE_GLOB_APP_CODE} = getAppEnvConfig();
      const uploadHeaders = reactive({
        'X-Access-Token':getToken(),
        'resrun-app-code':VITE_GLOB_APP_CODE
      })
      const userStore = useUserStore();
      const loginUser = userStore.getUserInfo as UserInfo;
      const selaFromRef = ref();
      const appStore = useAppStore();
      async function handleChange (info: any){
        if(info.file.status == 'done'){
          applyFrom.value.annexId = info.file.response.result
          // applyFrom.value.annexId = [];
        }
      }
      const router = useRouter();
      async function setControl(){
        // router.push({
        //   path:"/template/setting/control",
        //   query:{
        //     __full__:"",
        //     templateId:applyFrom.value.templateId,
        //     annexId:applyFrom.value.annexId
        //   }
        // })
        
        if(fileList.value.length > 0){
          applyFrom.value.submitFlag = 2;
          const response = await saveTemplate(applyFrom.value);
          if(response.code == 200){
            applyFrom.value.templateId = response.result.templateId;
            let routeUrl = router.resolve({
                path: '/template/setting/control',
            	query:{
                __full__:"",
                templateId:applyFrom.value.templateId,
                annexId:applyFrom.value.annexId
            	}
            })
            window.open(routeUrl.href, '_blank');
          }
        }else{
          message.warning("请先上传要模板文件")
        }
      }
      const showUploadImg=(file:any)=>{
        return false;
      }
      function docBeforeUpload (file:File,fileList){
        const fileName = file.name;
        var index= fileName.lastIndexOf(".");
        var ext = fileName.substr(index+1);
        if(ext !== "pdf"){
          message.warning("请上传PDF格式的文件")
          return Upload.LIST_IGNORE;
        }
      }
      function cancelPage(){
        router.push("/template/manage")
      }
      async function saveDataLoading(){
        loading.value = true;
        try {
          applyFrom.value.submitFlag = 1;
          const values = await selaFromRef.value.validateFields();
          if(!applyFrom.value.templateId && applyFrom.value.templateType == 1){
            clearLoading();
            message.warning("尚未设置模板参数，请设置后再进行保存")
            return;
          }
          if(applyFrom.value.templateType == 1){
           const result = await templateVerifyControl({templateId:applyFrom.value.templateId });
           if(!result){
             clearLoading();
             message.warning("尚未设置模板参数，请设置后再进行保存")
             return;
          }
         } 
          
          const response = await saveTemplate(applyFrom.value);
          if(response.code == 200){
            message.success("保存成功！")
            router.push("/template/manage")
          }
        }catch(e){
        }
        clearLoading();
      }
      function clearLoading(){
        setTimeout(function(){
          loading.value = false;
        },200)
      }
      async function initData(){
        window.name = "templateCreated";
        if(applyFrom.value.templateId){
          const result = await getTemplateInfo({templateId:applyFrom.value.templateId});
          applyFrom.value.templateCode = result.templateVo.templateCode;
          applyFrom.value.templateName = result.templateVo.templateName;
          applyFrom.value.templateType = result.templateVo.templateType;
          applyFrom.value.annexId = result.templateVo.annex?.id;
          applyFrom.value.templateId = result.templateVo.templateId;
          applyFrom.value.note = result.templateVo.note;
          applyFrom.value.userList = result.userList;
          applyFrom.value.managerList = result.managerList;
          if(result.templateVo.annex){
            fileList.value = [{
              name: result.templateVo.annex?.name,
              annexId: result.templateVo.annex?.id
            }]
          }
        }
      }
      const formColumn = ref<any>({});
      function buildFormColumn(data,taskId){
         
      }
      
      const [registerUserModal, { openModal,closeModal }] = useModal();
      const selectUserType = ref();
      function userHandleSuccess(list){
        closeModal();
        if(!list){
          message.warning("请选择用户")
        }else if(list.length > 0){
          loading.value = true;
          list.forEach(item=>{
            var data:any =  getSelectUser(item);
            if(selectUserType.value == 1){
              const isRepeat = applyFrom.value.managerList.find((item:any)=>{return  item.authRelationId == data.authRelationId})
              if(!isRepeat){
                // data.authType = 1;
                applyFrom.value.managerList.push(data)
              }
            }
            if(selectUserType.value == 2){
              const isRepeat = applyFrom.value.userList.find((item:any)=>{return  item.authRelationId == data.authRelationId})
              if(!isRepeat){
                // data.authType = 2;
                applyFrom.value.userList.push(data)
              }
            }
          })
          setTimeout(function(){
            loading.value = false;
          },300)
        }
      }
      function getSelectUser(item){
        if(item.type == 'user'){
          return {
            authRelationId: item.tenantUserId,
            authRelationName:item.nickName,
            authType:1
          };
        }
        if(item.type == 'dept'){
          return {
            authRelationId: item.id,
            authRelationName:item.departName,
            authType:2
          };
        }
        if(item.type == 'role'){
          return {
            authRelationId: item.id,
            authRelationName:item.name,
            authType:3
          };
        }
      }
      function initTemplateAdmin(){
        applyFrom.value.managerList.push({
          authRelationId: loginUser.tenantUserId,
          authRelationName:loginUser.realname,
          authType:1
        });
        applyFrom.value.userList.push({
          authRelationId: loginUser.tenantUserId,
          authRelationName:loginUser.realname,
          authType:1
        });
      }
      function selectAdmin(type,name){
        selectUserType.value = type;
        openModal(true,{
          isUpdate: true,
          // records: toRaw(getRawDataSource()).records,
          title:'选择'+name,
          tabs:[
            {
              title:'组织',
              type:'dept',
              api:getDeptNoCountTree,
              asyncTree:true,
              asyncFieldNames:{
                title:'name',
                key:'id'
              },
              fieldNames:{
                title:'departName',
                key:'id'
              }
            },
            { 
              title:'角色',
              type:'role',
              api:getRoleTreeList,
              asyncTree:true,
              fieldNames:{
                children:'children',
                title:'name',
                key:'id'
              }
            },
              {
                    title:'根据组织选人',
                    type:'deptUser',
                    api:getAllDeptTreeForSelect,
                    asyncTree:true,
                    asyncApi:getUserList,
                    asyncFieldNames:{
                      children:'children',
                      title:'nickName',
                      key:'id'
                    },
                    fieldNames:{
                      children:'children',
                      title:'departName',
                      key:'id'
                    }
                  },
              {
                title:'根据角色选人',
                type:'roleUser',
                api:getRoleTreeList,
                asyncTree:true,
                asyncApi:getUserByRoleId,
                asyncFieldNames:{
                  children:'children',
                  title:'nickName',
                  key:'id'
                },
                fieldNames:{
                  children:'children',
                  title:'name',
                  key:'id'
                }
              }
            ]
        })
      }
      function tagHandleClose(type,tag){
        loading.value = true;
        //applyFrom.value.authVoList = applyFrom.value.authVoList.filter(item=> !(item.authType == type && item.tenantUserId == tag.tenantUserId));
        if(type == 1){
          applyFrom.value.managerList = applyFrom.value.managerList.filter(item=>  item.authRelationId !== tag.authRelationId);
        }else{
          applyFrom.value.userList = applyFrom.value.userList.filter(item=>  item.authRelationId !== tag.authRelationId);
        }
        
        setTimeout(function(){
          loading.value = false;
        },300)
      }
      const formDisabled = ref(false);
      onMounted(()=>{
        const query = router.currentRoute.value.query;
        if(query.templateId){
          applyFrom.value.templateId =  query.templateId
        }else{
          initTemplateAdmin();
          console.log("initTemplateAdmin:",query);
        }
        
        initData();
      })
      return {
        applyFrom,uploadHeaders,handleChange,setControl,showUploadImg,saveDataLoading,cancelPage,
        seals,fileList,selaFromRef,docBeforeUpload,docAccept,formColumn,formDisabled,selectAdmin,registerUserModal,
        userHandleSuccess,tagHandleClose,loading
      }
    }
  });
</script>

<style lang="less" scoped>
  .apply-content{
    padding:20px 10px;
  }
  .doc-upload{
    width:300px;
    height: 30px;
    line-height: 30px;
    text-align: center;
  }
  :deep{
    .ant-upload-list-text .ant-upload-list-item-name, .ant-upload-list-picture .ant-upload-list-item-name{
      /* padding-left:48px; */
    }
  }
  
</style>

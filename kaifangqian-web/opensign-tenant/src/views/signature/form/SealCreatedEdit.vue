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
  <a-spin :spinning="spinning">
  <div class="page-content">
    <div class="custom-card">
      <div class="card-title">
        <span class="title">基本信息</span>
      </div>
      <div class="card-body">
        <a-form  :model="sealFrom" name="basic" :labelCol="{ style: 'width: 100px' }"
          aut="off" ref="selaFromRef" >
          <a-row style="width: 100%;padding-top: 10px;">
            <a-col :span="6">
              <a-form-item label="印章名称" name="sealName"
                  :rules="[{ required: true, message: '请输入印章名称' }]" >
                <a-input v-model:value="sealFrom.sealName"  size="middle" placeholder="请输入印章名" class="input-width" />
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item label="印章类型" name="sealType"
                  :rules="[{ required: true, message: '请选择印章类型' }]" >
                  <a-select v-model:value="sealFrom.sealType"  class="input-width"  size="middle" 
                    placeholder="请选择印章类型" :options="sealType">
                  </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row style="width: 100%;padding-top: 0px;">
            <a-col :span="6">
              <a-form-item label="所属组织" name="sealOrg">
                <a-input v-model:value="sealFrom.entpName" disabled size="middle" class="input-width" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row style="width: 100%;padding-top: 0px;">
            <a-col :span="12">
              <a-form-item label="印章管理员" name="managerList" 
                  :rules="[{ required: true, message: '请选择印章管理员' }]">
                  <!-- <a-input v-model:value="sealFrom.adminName" disabled size="middle" class="input-width" /> -->
                  <span>
                    <a-tag :closable="true" @close="tagHandleClose(1,item)" :key="item" v-for="(item,index) in sealFrom.managerList">{{item.authRelationName}}</a-tag>
                  </span>
                  
                  <a-button type="link" @click="selectAdmin(1,'印章管理员')" >选择管理员</a-button>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row style="width: 100%;padding-top: 0px;">
            <a-col :span="12">
              <a-form-item label="印章使用者" name="userList"
                  :rules="[{ required: true, message: '请选择印章使用者' }]">
                  <!-- <a-input v-model:value="sealFrom.adminName" disabled size="middle" class="input-width" /> -->
                  <span>
                    <a-tag :closable="true" @close="tagHandleClose(2,item)" :key="item" v-for="(item,index) in sealFrom.userList">{{item.authRelationName}}</a-tag>
                  </span>
                  <a-button type="link" @click="selectAdmin(2,'印章使用者')" >选择使用者</a-button>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row style="width: 100%;padding-top: 0px;">
            <a-col :span="12">
              <a-form-item label="印章审计者" name="auditorList" >
                  <!-- <a-input v-model:value="sealFrom.adminName" disabled size="middle" class="input-width" /> -->
                  <template  v-for="(item,index) in sealFrom.auditorList" :key="item">
                    <a-tag :closable="true" @close="tagHandleClose(3,item)">{{item.authRelationName}}</a-tag>
                  </template>
                  <a-button type="link" @click="selectAdmin(3,'印章审计者')" >选择审计者</a-button>
              </a-form-item>
            </a-col>
          </a-row>
          <!-- <a-row>
            <a-col :span="12" style="width: 100%;padding-top: 00px;">
              <a-form-item label="用途说明" name="description">
                <a-textarea v-model:value="sealFrom.description" size="middle" :auto-size="{ minRows: 3, maxRows: 3 }" :rows="3"  class="input-width" />
              </a-form-item>
            </a-col>
          </a-row> -->
        </a-form>
        <div class="line"></div>
      </div>
    </div>
     
    <div style="padding: 10px 0;"> 
      <a-space :size="10">
        <a-button type="primary" @click="editSealInfo">保存</a-button>
        <a-button @click="backPage">取消</a-button>
      </a-space>
    </div>
  </div>
  </a-spin>
   <TabTreeModal @register="registerUserModal" @success="handleSuccess" />
</template>

<script lang="ts">
  import { defineComponent,ref,unref,computed,reactive,onMounted } from 'vue';
  import { message} from 'ant-design-vue'; 
  import { getToken } from '/@/utils/auth';
  import { createLocalStorage } from '/@/utils/cache';
  import { APP_ID } from '/@/enums/cacheEnum';
  import type { UserInfo } from '/#/store';
  import Icon from '/@/components/Icon';
  import "/@/assets/style/custom-card-module.css"
  
  import { useUserStore } from '/@/store/modules/user';
  
  import {useRouter} from "vue-router";
  
  import { editSeal,getSealDetailes } from '../seal/api';
  import { getUserByDeptId } from '/@/api/sys/user';
  import {sealType,getSealType} from "../seal/data"
 import { useModal } from '/@/components/Modal';
 
 import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
 import { getRoleTreeList, getUserByRoleId } from '/@/api/sys/role'; 
 import { getUserList } from '/@/api/demo/system'; 
 import { getAllDeptTreeForSelect,getDeptNoCountTree } from '/@/api/sys/dept';
 
  export default defineComponent({
    name: 'sealCreated',
    components:{
      Icon,TabTreeModal
    },
    setup(props){
      let router = useRouter();
      const spinning = ref(false);
      const sealFrom = ref({
        sealType:null,
        sealName:"",
        createType:1,
        sealStyle:1,
        entpName:"",
        middleText:"",
        annexId:"",
        sealBackground:0,
        description:"",
        sealId:"",
        adminId:null,
        adminName:"",
        managerList:[] as any,
        userList:[] as any,
        auditorList:[] as any
      });
      const auditorList = reactive([]);
      const editType = ref<number>(0);
      const userStore = useUserStore();
      const loginUser = userStore.getUserInfo as UserInfo;
      sealFrom.value.entpName = loginUser.loginTenantName;
      const sealRef = ref(null);
      const selaFromRef = ref({})
      const deptUserList = ref([]);
      
      async function editSealInfo(){
        try {
          spinning.value = true;
          const values = await selaFromRef.value.validateFields();
          const data = {
            "description": sealFrom.value.description,
            "id": sealFrom.value.sealId,
            "sealName": sealFrom.value.sealName,
            "sealType":sealFrom.value.sealType,
            "adminId":sealFrom.value.adminId,
            "auditorList":sealFrom.value.auditorList,
            "managerList":sealFrom.value.managerList,
            "userList":sealFrom.value.userList
          }
          const response = await editSeal(data);
         if(response.code == 200){
            message.success("印章保存成功！");
             spinning.value = false;
            router.push("/seals/manage")
          }
        } catch (errorInfo) {
          console.log('Failed:', errorInfo);
          message.warning("有必填参数未填");
        }
        spinning.value = false;
      }
      function backPage(){
        router.go(-1);
      }
      function loadDeptUser(){
        getUserByDeptId({id:loginUser.loginDepartId,pageSize:999}).then(res=>{
          deptUserList.value = res.records;
        });
      }
      function sealEditHandle(query:any){
        editType.value = 1;
        const sealId = query.sealId as string;
        if(!sealId){
          message.warning("参数错误！！！")
        }else{
          getSealDetailes({sealId:sealId}).then(res=>{
            sealFrom.value.sealName = res.sealName;
            sealFrom.value.description = res.description;
            sealFrom.value.sealType = res.sealType;
            sealFrom.value.sealId = sealId;
            sealFrom.value.adminId = res.adminId;
            sealFrom.value.adminId = res.adminId;
            sealFrom.value.adminName = res.adminName;
            sealFrom.value.managerList = res.managerList;
            sealFrom.value.userList = res.userList;
            sealFrom.value.auditorList = res.auditorList;
          })
        }
      }
      const [registerUserModal, { openModal,closeModal }] = useModal();
      const selectType = ref(0);
      function selectAdmin(type,name){
        selectType.value = type;
        openModal(true,{
          isUpdate: false,
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
        if(type == 1){
          sealFrom.value.managerList = sealFrom.value.managerList.filter(item=>item.authRelationId != tag.authRelationId);
        }
        if(type == 2){
          sealFrom.value.userList = sealFrom.value.userList.filter(item=>item.authRelationId != tag.authRelationId);
        }
        
        if(type == 3){
          sealFrom.value.auditorList = sealFrom.value.auditorList.filter(item=>item.authRelationId != tag.authRelationId);
        }
      }
      async function handleSuccess(list) {
        if(!list){
          message.warning("请选择用户")
        }else if(list.length > 0){
          list.forEach(item=>{
            const data:any =  getSelectUser(item);
            if(selectType.value == 1){
              const isRepeat = sealFrom.value.managerList.find(item=>{return item.authRelationId == data.authRelationId})
              if(!isRepeat){
                sealFrom.value.managerList.push(data)
              }
            }
            if(selectType.value == 2){
              const isRepeat = sealFrom.value.userList.find(item=>{return item.authRelationId == data.authRelationId})
              if(!isRepeat){
                sealFrom.value.userList.push(data)
              }
            }
            if(selectType.value == 3){
              const isRepeat = sealFrom.value.auditorList.find(item=>{return item.authRelationId == data.authRelationId})
              if(!isRepeat){
                sealFrom.value.auditorList.push(data)
              }
            }
          })
          closeModal();
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
      onMounted(() => {
        const query = router.currentRoute.value.query as any;
         sealEditHandle(query);
         // loadDeptUser();
      });
      return{
        selectAdmin,registerUserModal,handleSuccess,tagHandleClose,auditorList,
        sealFrom, sealRef,sealType,selaFromRef,spinning,editType,editSealInfo,backPage,deptUserList
      }
    }
  })
</script>

<style lang="less" scoped>
  .page-content{
    padding: 5px 20px;
  }
  .input-width{
    // width:300px !important;
  }
  .template-make{
    width:500px;
    border: 1px solid #ededed;
    margin-top: 10px;
    padding: 5px 20px;
    .temlate-item .title {
      font-weight: 600;
    }
    .seal-style{
      //height:100px;
      padding-top: 15px;
      ul {
        display:flex;
        padding:0;
        margin: 0;
      }
      ul li{
        user-select: none;
        cursor: pointer;
        .seal-img{
          padding:6px;
          width: 100px;
          height: 100px;
          border: 1px solid #ededed;
          border-radius: 4px;
        }
        .signature-style{
          display: flex;
          width:116px;
          height: 116px;
          padding: 8px 6px;
          justify-items: center;
          align-items: center;
          border: 1px solid;
          border-color: inherit;
          border-radius: 4px;
        }
        .signature-style img{
        	user-select: none;
        }
        .seal-name{
          text-align: center;
          line-height: 30px;
          color:#9e9e9e;
        }
      }
      ul li.active{
        .seal-img{
          border-color: #1890ff;
        }
        .seal-name{
          color: #1890ff;
        }
      }
      ul li:nth-child(n + 2){
        margin-left: 10px;
      }
    }
  }
  .seal-make-layout{
    display: flex;
    .template-make{
      
    }
    .seal-preview{
      padding-left: 40px;
      .title{
        font-size: 14px;
        font-weight: 600;
        height: 30px;
      }
    }
     
    .seal-upload{
      padding:20px 0;
    }
    .seal-preview-img{
      width:160px;
      padding: 10px;
      border: 1px solid #ededed;
    }
  }
  .local-upload{
    width:160px;
    height:160px;
    padding: 10px;
    border: 1px dashed rgba(0,0,0,0.3);
    display: flex;
    align-items: center;
    justify-items: center;
    text-align: center;
  }
  .upload-img{
    width: 300px;
    height: 300px;
  }
  .SealActions{
    width:300px;
    padding: 8px 15px;
    background-color: rgba(0,0,0,0.07);
  }
  .center{
    text-align: center;
    line-height: 32px;
    
  }
  .pointer{
    cursor: pointer;
  }
</style>

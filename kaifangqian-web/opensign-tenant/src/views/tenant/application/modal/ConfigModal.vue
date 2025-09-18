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
      <BasicModal v-bind="$attrs" @register="registerModal" title="应用配置" @ok="handleSubmit">
        <div class="info-item">
          <span class="title">应用名称：</span>
          <span class="value">{{ appInfo.appName }}</span>
        </div>
        <div class="info-item">
          <span class="title">应用简介：</span>
          <span class="value">{{ appInfo.appDesc }}</span>
        </div>

        <div class="basic-body info-item">
          <div>
            <span class="user-scope">可见范围</span>
              <a-radio-group v-model:value="scopeType" name="radioGroup" @change="(e)=>handleScope(e)">
              <!-- <a-radio-group v-model:value="scopeType" name="radioGroup"> -->
                <a-radio value="all">所有成员</a-radio>
                <a-radio value='assign'>指定成员</a-radio>
              </a-radio-group>
              <a-button v-if="scopeType=='assign'" type="link" size="small" @click="openSelectUser">添加</a-button>
              <div class="user-select-container" v-if="scopeType=='assign'">
                <div class="user-list">
                  <a-tag color="blue" v-for="(item,index) in scopeUser" :key="item.id" :closable="scopeUser.length > 1" @close="deleteUser(item)">{{ item.name || item.realname}}</a-tag>
                </div>
              </div>
          </div>
      </div>
      <TabTreeModal @register="registerUserModal" @success="handleSuccess" />    
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref  } from 'vue';
  import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
  import { updateAppStatus, updateAppUseful, getApplicationInfo } from '/@/api/tenant';
  import { getDeptNoCountTree } from '/@/api/sys/dept'; 
  import { getUserList } from '/@/api/demo/system'; 
  import TabTreeModal from '../../organize/modal/TabTreeModal.vue';
  // import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage'; 
 
  interface userItem {
    id:string;
    name?:string;
    username?:string;
    realname?:string;
  }
  export default defineComponent({
    name: 'DictModal',
    components:{
      BasicModal,
      TabTreeModal,
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const rowId = ref('');

      const scopeType = ref<string>('all');
      const scopeUser = ref(<userItem[]>[]);

      const { createMessage: msg } = useMessage();

      const appInfo = ref({
        users:[],
        useful:'',
        appName:'',
        appDesc:''

      })

      const [registerUserModal, { openModal,closeModal:closeUserModal }] = useModal();

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        rowId.value = data.record.id;
        if (unref(rowId)) {
          getAppInfo()
        }
      });

      async function getAppInfo(){
        appInfo.value = {users:[],useful:'', appName:'',
        appDesc:''};
        let result = await getApplicationInfo({id:unref(rowId)});
          if(result){
              appInfo.value = result;
              scopeUser.value =  appInfo.value.users || [];
              scopeType.value =  appInfo.value.useful?'all':'assign';
          }
      }
      

      async function handleSubmit() {
        if( scopeType.value !== 'all' && (!scopeUser.value || scopeUser.value.length == 0)){
          msg.warning("指定成员为空，请指定后再进行保存")
          return;
        }
        
        try {
          await submitAppConfig();
          closeModal();
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      async function handleAppStatus(){
        let result = await updateAppStatus({id:unref(rowId)});
        if(result){
          msg.success('操作成功');
        }

      }
      function handleSuccess(list){
        if(list && list.length>0){
          
          
          list.forEach(item=>{
            const isExist = scopeUser.value.find((user:any)=>user.id == item.id);
            if(!isExist){
              scopeUser.value.push(item);
            }
          })
          // scopeUser.value = list;
          closeUserModal();
        }else{
          msg.warning("请选择要添加的成员")
        }
      }

      async function submitAppConfig(){
        let users:userItem[] = [];
        scopeUser.value.map(item =>{
          users.push({id:item.id});
        })
        let params = {
          useful:unref(scopeType)==='all'?true:false,
          users:users,
          id:unref(rowId)
        }
        let result = await updateAppUseful(params);
        if(result){
          msg.success('操作成功')
        }
        
      }

      async function handleScope(e){
        // console.log("eeee:",e);
        
        if(e.target.value !=='all'){   // 选择全部时直接调用
          // showUserModal()
        }
      }
      function deleteUser(row){
        // scopeUser.value.forEach(item=>{
        //   if(item.id == tag.id)
        // })
     
        
        const tem = scopeUser.value.filter((item:any)=>{ return item.id != row.id});
        scopeUser.value = tem;
        console.log(scopeUser.value)
      }
      function openSelectUser(){
        showUserModal();
      }
      function showUserModal(){
          openModal(true,{
            isUpdate: true,
              title:'人员选择',
              tabs:[
                  { 
                    title:'根据组织选人',
                    type:'deptUser',
                    api:getDeptNoCountTree,
                    asyncTree:true,
                    asyncApi:getUserList,
                    asyncFieldNames:{
                      title:'realname',
                      key:'id'
                    },
                    fieldNames:{
                      children:'children',
                      title:'departName',
                      key:'id'
                    }
                  },
                ]
            })
        }

      return { appInfo, registerModal, registerUserModal, handleSubmit, handleSuccess, scopeType, scopeUser, handleAppStatus, handleScope,openSelectUser,deleteUser};
    }
  })
</script>
<style lang="less" scoped>
.info-item{
  margin-bottom:10px;
}
.basic-body{
  margin-top:20px;
  .config-icon{
    margin-left:10px;
  }
}
.user-scope{
  font-size: 14px;
  margin-right:20px;
}
.user-select-container{
  display: flex;
  margin-top:30px;
  .user-list{
    /* width:600px; */
    padding:15px;
    /* min-height: 300px; */
    /* border:1px solid #ddd; */
    border-radius: 5px;
    margin-right: 10px;
    /* background:#f4f4f4; */

  }
}
 
</style>

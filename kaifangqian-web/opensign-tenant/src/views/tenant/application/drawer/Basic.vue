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
  <div class="app-basic-info">
      <div class="basic-header">
        <div class="header-left">
          <img :src="avatarBase64" alt="" v-if="avatarBase64" class="app-avatar"/>
          <img class="app-avatar" src="../../../../assets/images/app-icon.png" alt="" v-if="!avatarBase64">
          <div class="app-name-info">
            <p class="title">{{appInfo.appName}}</p>
            <p class="desc">{{appInfo.appDesc}}</p>
          </div>
        </div>
        <div class="header-right">
          <span>{{ checked?'已启用':'已禁用' }}</span>
          <a-switch v-model:checked="checked" @change="handleAppStatus"/>
        </div>
      </div>
      <div class="basic-body">
        <div>
          <span class="user-scope">可见范围</span>
            <a-radio-group v-model:value="scopeType" name="radioGroup" @change="(e)=>handleScope(e)">
              <a-radio value="all">所有成员</a-radio>
              <a-radio value='assign'>指定成员</a-radio>
            </a-radio-group>
            <!-- <a-tooltip placement="top" title="指定成员后方可生效">
              <a href="javascript:;" @click="showUserModal"  v-if="scopeType=='assign'">配置</a>
              <Icon icon="ant-design:info-circle-outlined" v-if="scopeType=='assign'" color="#127fd2" class="config-icon"/>
            </a-tooltip> -->
            <div class="user-select-container" v-if="scopeType=='assign'">
              <div class="user-list">
                <a-tag color="blue" v-for="item in scopeUser">{{ item.name || item.realname}}</a-tag>
              </div>
            </div>
        </div>
      </div>
  </div>
  <TabTreeModal @register="registerModal" @success="handleSuccess" />
</template>

<script lang="ts">
import { ref, watch, unref,  onBeforeUpdate, computed} from "vue";
import TabTreeModal from '../../organize/modal/TabTreeModal.vue';
import { useModal } from '/@/components/Modal';
import { getDeptNoCountTree } from '/@/api/sys/dept'; 
import { getUserList } from '/@/api/demo/system'; 
import { updateAppStatus, updateAppUseful } from '/@/api/tenant';
import { useMessage } from '/@/hooks/web/useMessage';
import { getImgBase64 } from '/@/api/sys/upload';
import Icon from '/@/components/Icon';

interface Info {
  appName:string;
  id:string;
  [key:string]:string|number;
}
interface userItem {
  id:string;
  name?:string;
  username?:string;
  realname?:string;
}
export default{
  name:"AppBasic",
  components:{
    TabTreeModal,
    Icon
  },
  props:{
    appInfo:{
      type:Object,
    }
  },
  setup(props,{}) {
    const checked = ref<boolean>(false);
    const scopeType = ref<string>('all');
    const scopeUser = ref(<userItem[]>[]);
    const avatarBase64 = ref('');
    const [registerModal, { openModal,closeModal }] = useModal();
    const { createMessage: msg } = useMessage();
    const appInfo = computed(():Info =>  {
      console.log( props?.appInfo,'应用信息')
      checked.value = props?.appInfo?.status===1?true:false;
      scopeType.value =  props?.appInfo?.useful?'all':'assign';
      scopeUser.value =  props?.appInfo?.users || [];
      if(props?.appInfo?.appIconAddress){
        getImageBase64File(props?.appInfo.appIconAddress);
      }
      return props.appInfo as Info
    });
    watch(
      scopeType,
      (val) => {
        if(val=='assign'){
          // showUserModal()
        }else{
          scopeUser.value = [];
        }
      }
    )
    onBeforeUpdate(()=>{
    })
    async function getImageBase64File(id){
      avatarBase64.value = await getImgBase64({imgId:id}).image;
    }

    async function handleAppStatus(checked){
      console.log(checked,'--状态-')
      console.log(appInfo.value,'00000000ssss000')
      let result = await updateAppStatus({id:appInfo.value.id});
      if(result){
        msg.success('操作成功');
      }

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


    async function handleScope(e){
      console.log(e,'333333333333333333333333')
      let users:userItem[] = [];
      scopeUser.value.map(item =>{
        users.push({id:item.id});
      })
      let params = {
        useful:unref(scopeType)==='all'?true:false,
        users:users,
        id:appInfo.value.id
      }
      if(e.target.value==='all'){   // 选择全部时直接调用
        let result = await updateAppUseful(params);
        if(result){
          msg.success('操作成功')
        }
      }else{
        showUserModal()
      }
    }
    function handleSuccess(list){
       console.log(list);
       scopeUser.value = list;
       closeModal();
       handleScope({target:{value:'all'}})
    }


    return {
      appInfo,
      handleSuccess,
      handleScope,
      checked,
      scopeUser,
      scopeType,
      registerModal,
      showUserModal,
      avatarBase64,
      handleAppStatus
    }
  }
}
</script>

<style lang="less" scoped>
.app-basic-info{
  padding:20px 30px;
}
.basic-header{
  display: flex;
  justify-content: space-between;
  
  padding-bottom: 50px;
  border-bottom: 1px solid #ddd;
  .header-left{
    display: flex;
    align-items: center;
    .app-avatar{
      width:100px;
      height: 100px;
      display: inline-block;
      border-radius: 5px;
      margin-right:15px;
    }
  }
  .header-right{
    display: flex;
    align-items: center;
    span{
      margin:0 10px;
    }
  }
}
.app-name-info{
  .title{
    font-size: 18px;
    font-weight: 600;
    color: #000;
  }
  .desc{
    font-size: 14px;
  }
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
    width:600px;
    padding:15px;
    min-height: 300px;
    border:1px solid #ddd;
    border-radius: 5px;
    margin-right: 10px;
    background:#f4f4f4;

  }
}
</style>

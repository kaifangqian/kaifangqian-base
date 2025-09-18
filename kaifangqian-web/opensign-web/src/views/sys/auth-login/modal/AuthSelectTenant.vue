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

/*
 * @Author: ningw 
 * @Date: 2022-06-28 18:22:10 
 * @Last Modified by: ningw
 * @Last Modified time: 2024-01-15 11:35:12
 */
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="请选择部门" @ok="handleSubmit" :footer="null">
        <template #title>
          <div  >
            <div class="select-header">
              <span class="login-select-title">请选择身份进行登录</span>
              <a-button type="text" @click="handleBack" id="login-out">退出</a-button>
            </div>
          </div>
        </template>
        <div class="account-list">
          <div v-for="(item,index) in accountList.list" @click="handleSubmit(item)">
              <div :class="['account-item',item.useFlag?'':'no-use']" >
                <p :key="index" class="company-header">
                  <span>{{item.tenantName}}</span>
                  <a-tag color="#f50" v-if="!item.useFlag">已停用</a-tag>
                  <div>
                    <a-tag color="blue" v-if="item.selectFlag">上次登录</a-tag>
                    <Icon icon="ant-design:arrow-right-outlined" class="login-arrow"/>
                  </div>
                </p>
              </div>
          </div>
        </div>
    </BasicModal>
  </div>
</template>
<script lang='ts'>
import type { TenantDepartList } from '/#/store';
import { defineComponent,ref, reactive } from 'vue'
import { BasicModal,useModalInner } from '/@/components/Modal';
import { Icon } from '/@/components/Icon';
// import { useUserStore } from '/@/store/modules/user';
// import { useMessage } from '/@/hooks/web/useMessage';
import { getMyTenantDepartsByLoginToken } from '/@/api/sys/user'; 
import { getHashQueryString, removeQueryParam } from '/@/utils';
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';

interface Dept {
  [propName: string]: string | number
}
export default defineComponent({
  name: 'LoginSelectAccount',
  components:{
    BasicModal,
    // BasicForm,
    Icon
  },
  setup(_,{emit}){
      const userStore = useUserStore();
      const isUpdate = ref(true);
      const accountList = reactive({list:<TenantDepartList[]>[]});
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
          setModalProps({ confirmLoading: false,width:500,canFullscreen:false, closable:false,maskClosable:false,centered:true });
          isUpdate.value = !!data?.isUpdate;
          const urlParams = ref({
            appId:getHashQueryString('appId')|| '',
            appCode:getHashQueryString('appCode')|| '',
            departId:getHashQueryString('departId')|| '',
            redirect:getHashQueryString('redirect') || '',
            directly:getHashQueryString('directly') || false
          })
          let result = await getMyTenantDepartsByLoginToken({appCode:urlParams.value.appCode});
          if(result){
            accountList.list = result;
          }
      });
      function handleSubmit(item){
          emit('success',item.departId);
          closeModal()
      }
      function handleBack(){
        userStore.clearToken();
        closeModal();
      }
    return {
      registerModal,
      handleSubmit,
      accountList,
      handleBack
    }
  },
})
</script>
<style lang="less" scoped>
.login-title{
  font-size: 18px;
}
.login-select-title{
  font-size: 18px;
  font-weight: 600;
}
.select-header{
  display:flex;
  justify-content: space-between;
  align-items: center;
}
.account-list{
  overflow: auto;
    max-height: 350px;
    .account-item:hover .company-header{
      background: #efefef;
    }
    .account-item{
      font-size: 14px;
      border: 1px solid #f9f9f9;
      border-radius: 8px;
      cursor: pointer;
      align-items: center;
      justify-content: space-between;
      margin-right: 10px;
      margin-bottom: 10px;
      .company-header{
        background: #f9f9f9;
        padding: 15px 10px;
        display: flex;
        justify-content: space-between;
        margin: 0 !important;
      }
      .account-depart{
        display: flex;
        margin-bottom: 10px;
        justify-content: space-between;
        padding: 0 20px;
        &:hover{
          color: #094aee ;
        }
      }
      &:active{
        box-shadow: inset -7px -6px 12px rgb(255 255 255 / 90%), inset 0px -1px 10px rgb(0 0 0 / 40%) 
      }
    }
    .no-use{
      pointer-events: none;
    }
}
 
</style>

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
 * @Last Modified time: 2024-02-25 18:30:19
 */
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="输入企业邀请码" @ok="handleSubmit" :destroyOnClose="true" >
      <div class="code-body">
        <p>企业邀请码（即团队码）是一个 8 位字母编码，可联系企业管理员获取</p>
        <a-input v-model:value="invitationCode" class="code-input" placeholder="请输入企业邀请码"></a-input>
      </div>
     
    </BasicModal>
  </div>
</template>
<script lang='ts'>
import { defineComponent, computed, ref,unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { Icon } from '/@/components/Icon';
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';
import { jionTenant } from '/@/api/tenant';

export default defineComponent({
  name: 'Invitation',
  components:{
    BasicModal,
    Icon
  },
  setup(_,{emit}){
      const userStore = useUserStore();
      const { createMessage: msg, } = useMessage();
      const invitationCode = ref('');
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
          setModalProps({ confirmLoading: false,width:500,canFullscreen:false, closable:false,maskClosable:false,centered:true,minHeight:70 });

      });
      const loginDepartId =  computed(()=>userStore.getUserInfo.loginDepartId);
      async function handleSubmit(){
        let response = await jionTenant({invitationCode:unref(invitationCode)});
        if(response.data.code ==200){
          if(response.data.result ==1 ){
            msg.success('加入成功');
          }else{
            msg.success('已申请加入，待审核');
          }
          closeModal();
          emit('success')
        }
      
      }
      return {
        invitationCode,
        registerModal,
        handleSubmit,
        loginDepartId
      }
  },
})
</script>
<style lang="less" scoped>
.login-title{
  font-size: 18px;
}
// .code-body{
//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
// }
.code-input{
  // width:420px;
  height: 40px;
  margin-top:15px;
}
.account-list{
  overflow: auto;
    max-height: 250px;
    .account-item{
      padding: 15px 24px;
      font-size: 14px;
      border: 1px solid #f9f9f9;
      border-radius: 8px;
      cursor: pointer;
      background: #f9f9f9;
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 15px;
      margin-right: 10px;
      &:active{
        box-shadow: inset -7px -6px 12px rgb(255 255 255 / 90%), inset 0px -1px 10px rgb(0 0 0 / 40%) 
      }
    }
    .active-depart{
      color: @primary-color;
    }
}
 
</style>

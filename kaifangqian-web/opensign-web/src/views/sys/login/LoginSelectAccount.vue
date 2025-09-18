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
 * @Last Modified by: mikey.zhaopeng
 * @Last Modified time: 2024-05-30 18:38:28
 */
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" title="请选择部门" @ok="handleSubmit" :footer="null">
        <template #title>
          <div  @click="handleBack">
            <div class="select-header">
              <span class="login-select-title">请选择身份进行登录</span>
              <Button type="text" id="login-out" >退出</Button>
            </div>
           
          </div>
        </template>
        <div class="account-list">
          <div v-for="(item,index) in accountList.list" @click="handleSubmit(item)" :key="index" >
              <div :class="['account-item',item.useFlag?'':'no-use']" >
                <div class="company-header">
                  <div>
                    <Tag :color="item.tenantType == 1?'blue':'orange'">{{  item.tenantType==1?'企业':'个人' }}</Tag>
                    <span>{{item.tenantName}}</span>
                  </div>
                  <Tag color="#f50" v-if="!item.useFlag">已停用</Tag>
                  <div>
                    <Tag color="blue" v-if="item.selectFlag">上次登录</Tag>
                    <Icon icon="ant-design:arrow-right-outlined" class="login-arrow"/>
                  </div>
                </div>
                <!-- <div class="account-depart" v-for="(deptItem,deptIndex) in item.departs" :key="deptIndex" @click="handleSubmit(deptItem)">
                  <span >{{ deptItem.departName }}</span>
                  <div>
                    <Tag color="blue" v-if="deptItem.selectFlag">上次登录</Tag>
                    <Icon icon="ant-design:arrow-right-outlined"/>
                  </div>
                </div> -->
              </div>
          </div>
        </div>
    </BasicModal>
  </div>
</template>
<script lang='ts'>
import type { TenantDepartList } from '/#/store';
import { defineComponent,ref,unref, reactive } from 'vue'
import { BasicModal,useModalInner } from '/@/components/Modal';
import { Icon } from '/@/components/Icon';
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';
import { Tag } from 'ant-design-vue';

interface Dept {
  [propName: string]: string | number
}
export default defineComponent({
  name: 'LoginSelectAccount',
  components:{
    BasicModal,
    // BasicForm,
    Icon,
    Tag
  },
  setup(_,{emit}){
      const userStore = useUserStore();
      const { notification } = useMessage();
      const isUpdate = ref(true);
      const token = ref();
      const type = ref('');
      const accountList = reactive({list:<TenantDepartList[]>[]});
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
          // resetFields();
          setModalProps({ confirmLoading: false,width:500,canFullscreen:false, closable:false,maskClosable:false,centered:true });
          isUpdate.value = !!data?.isUpdate;
          const tokenValue = unref(data).record.token;
          token.value = tokenValue;
          accountList.list = unref(data).record.user_tenant_depart;
          type.value = data?.type;

        
      });
      async function handleSubmit(item){
        setModalProps({ loading: true });
        console.log(item.departId,'部门信息')
        // 获取表单信息
        // const formData = await validate()
        const formData = {
          departId:item.departs[0].departId
        }
        //判断是否为授权时选择租户
        console.log(type.value,'授权类型')
        const userInfo = type.value === 'authSelect'?  await userStore.authSelectTenant(formData) : await userStore.selectTenant(formData);
        if(userInfo){
          emit('success');
          closeModal()
        }
        setModalProps({ loading: false });
      }
      function handleBack(){
        userStore.clearToken();
        emit('success')
        
      }
    return {
      registerModal,
      // registerForm,
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
    .account-item:hover .company-header{
      background: #efefef;
    }
    overflow: auto;
    max-height: 350px;
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
        .login-arrow{
          visibility: hidden;
        }
        &:hover{
          .login-arrow{
            visibility: visible;
            color: #094aee ;
           }
          
        }
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

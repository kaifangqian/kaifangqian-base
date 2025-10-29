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
  <div class="account-scope">
    <Dropdown
      placement="bottomLeft"
      :overlayClassName="`${prefixCls}-dropdown-overlay`"
      style="top:63px"
      :trigger="['hover']"
      :getPopupContainer="getPopupContainer"
      @visibleChange="visibleChange"
    >
      <span class="drop-title" v-if="storeUserInfo.loginTenantType == '2' ">
        <span class="tenant-name">{{ storeUserInfo.loginTenantName }}的个人空间</span>
          <a-tag :style="{
                    color: loadCertificationStatus(tenantInfo.authStatus),
                    backgroundColor: loadCertificationStatus(tenantInfo.authStatus) + '20',
                    borderColor: loadCertificationStatus(tenantInfo.authStatus) + '20'
                }" size="small" class="tenant-tag">{{
                    loadCertificationText(tenantInfo.authStatus)
                  }}</a-tag>
        <SvgIcon name="tenant-show" size="22" />
      </span>
      <span class="drop-title" v-else>
        <span class="tenant-name">{{ storeUserInfo.loginTenantName }}</span>
        <a-tag :style="{
                    color: loadCertificationStatus(tenantInfo.authStatus),
                    backgroundColor: loadCertificationStatus(tenantInfo.authStatus) + '20',
                    borderColor: loadCertificationStatus(tenantInfo.authStatus) + '20'
                }" size="small" class="tenant-tag">{{
                    loadCertificationText(tenantInfo.authStatus)
                  }}</a-tag>
        <SvgIcon name="tenant-show" size="22" />
      </span>
      <template #overlay>
        <div class="account-panel">
          <div class="account-list">
            <div class="ent-list">
              <div class="account-title" v-if="entAccountList.length">企业账户</div>
              <div
                :class="['account-panel-item', item.useFlag ? '' : 'no-use']"
                v-for="item in entAccountList"
                :key="item.tenantId"
              >
                <p
                  class="account-header"
                  :class="{ disabled: !item.useFlag }"
                  @click="!item.selectFlag && handleMenuClick(item)"
                >
                  <span :class="[item.selectFlag ? 'current-login' : '', 'ellipsis']">{{ item.tenantName }}</span>
                  
                  <a-tag color="red" v-if="!item.useFlag">已停用</a-tag>
                  <!-- <a-tag color="blue" v-if="item.selectFlag">当前登录</a-tag> -->
                  <a-tag :style="{
                    color: loadCertificationStatus(item.authStatus),
                    backgroundColor: loadCertificationStatus(item.authStatus) + '20',
                    borderColor: loadCertificationStatus(item.authStatus) + '20'
                }"  class="tenant-tag">{{
                    loadCertificationText(item.authStatus)
                  }}</a-tag>
                </p>
              </div>
            </div>
            <div class="personal-list" v-if="personalAccountList.length">
              <div class="account-title">个人账户</div>
              <div
                :class="['account-panel-item', item.useFlag ? '' : 'no-use']"
                v-for="item in personalAccountList"
                :key="item.tenantId"
              >
                <p
                  class="account-header"
                  :class="{ disabled: !item.useFlag }"
                  @click="!item.selectFlag && handleMenuClick(item)"
                 
                >
                  <span class="ellipsis">{{ item.tenantName }}的个人空间</span>
                  
                  <a-tag color="red" v-if="!item.useFlag">已停用</a-tag>
                  <!-- <a-tag color="blue" v-if="item.selectFlag">当前登录</a-tag> -->
                  <a-tag :style="{
                    color: loadCertificationStatus(item.authStatus),
                    backgroundColor: loadCertificationStatus(item.authStatus) + '20',
                    borderColor: loadCertificationStatus(item.authStatus) + '20'
                }" size="small" class="tenant-tag">{{
                    loadCertificationText(item.authStatus)
                  }}</a-tag>
                </p>
              </div>
            </div>
          </div>
          <div class="join-container">
            <div class="join-enter" @click.stop="handleJoinEnterprise">
              <Icon icon="ant-design:usergroup-add-outlined" :size="18" />
              <span>加入/创建企业</span>
            </div>
            <div
              class="join-enter"
              v-if="!storeUserInfo.personalTenantFlag"
              @click.stop="handleOpenPersonalTenant"
            >
              <Icon icon="ant-design:user-add-outlined" :size="18" />
              <span>开通个人空间</span>
            </div>
          </div>
        </div>
      </template>
    </Dropdown>
  </div>
  <Invitation @register="registerModal" @success="handleSuccess" />
  <CompanyQueryModal @register="registerQueryCompanyModal" @success="handleSuccess" />
</template>

<script lang="ts">
import type { TenantDepartList } from '/#/store';
import {ref,onMounted,computed} from "vue";
import Invitation from "./Invitation.vue";
import { useModal } from '/@/components/Modal';
import { useUserStore } from '/@/store/modules/user';
import { Dropdown, Menu } from 'ant-design-vue';
import { useDesign } from '/@/hooks/web/useDesign';
import { createAsyncComponent } from '/@/utils/factory/createAsyncComponent';
import { getPopupContainer } from '/@/utils';
import {Icon, SvgIcon} from '/@/components/Icon/index';
import { getMyTenantDeparts, updateTenantDepart } from '/@/api/sys/user'; 
import { openPersonalTenant } from '/@/api/tenant'
import { useMessage } from '/@/hooks/web/useMessage';
import CompanyQueryModal from '/@/views/sys/user/modal/CompanyQueryModal.vue';
import {
    loadCertificationText,
    loadCertificationStatus,
  } from '/@/utils/StatusToName';


export default{
  name:"ChangeAccount",
  components:{
    Invitation,
    Dropdown,
    Menu,
    MenuItem: createAsyncComponent(() => import("../user-dropdown/DropMenuItem.vue")),
    MenuDivider: Menu.Divider,
    Icon,
    SvgIcon,
    CompanyQueryModal
    
  },
  setup() {
   const accountScope = ref('');
   const personalTenantFlag = ref(false);
   const { prefixCls } = useDesign('header-account');
   const isOpenPersonalScope = ref(false);
   const entAccountList = ref(<TenantDepartList[]>[]);
   const personalAccountList = ref(<TenantDepartList[]>[]);
   
   const { createMessage: msg, notification, createConfirm } = useMessage();
   


  const [registerModal,{openModal}] = useModal();
  const [registerQueryCompanyModal,{ openModal: openQueryCompanyModal, closeModal: closeQueryCompanyModal }] = useModal();
   
   const userStore = useUserStore();
   const storeUserInfo =  computed(() => userStore.getUserInfo);
   const tenantInfo = ref({ ...userStore.getTenantInfo });

   console.log(storeUserInfo,'storeUserInfo------')

 
   function handleAccountChange(){
    openModal(true,{
      isUpdate:false,
      record:{

      }
    })
   }
   onMounted(()=>{
    fetch()
   })
   async function fetch(){
    let result = await getMyTenantDeparts();
    if(result){
      entAccountList.value = result.filter(v=>v.tenantType == 1);
      personalAccountList.value = result.filter(v=>v.tenantType == 2);
    }
   } 
    function handleJoinEnterprise(){
    openQueryCompanyModal(true, { isUpdate: false });
   }
   function handleSuccess(){
    fetch();
   }

   async function handleMenuClick(depart){
    let params = {
        departId:depart.departId
      }
      //切换租户 不需要跳转首页，进行 window.location.reload();
      const result = await userStore.selectTenantAuth(params);
      // if(result){
      //       notification.success({
      //         message: '切换成功',
      //         duration: 3,
      //       });
      //       setTimeout(()=>{
      //         location.reload();
      //       },0)
      //   }
   }
   async function handleOpenPersonalTenant(){
    let result = await openPersonalTenant({});
    if(result){
      msg.success('开通成功');
      const userInfo = userStore.getUserInfoAction();
      console.log(userInfo,'跟新后的信息')
      fetch()
    }
   }
   function visibleChange(visible){
    if(visible){
      fetch()
    }
   }
    return {
      prefixCls,
      storeUserInfo,
      handleMenuClick,
      accountScope,
      handleOpenPersonalTenant,
      personalTenantFlag,
      registerModal,
      entAccountList,
      handleAccountChange,
      getPopupContainer,
      isOpenPersonalScope,
      handleSuccess,
      visibleChange,
      personalAccountList,
      registerQueryCompanyModal,
      handleJoinEnterprise,
      tenantInfo,
      loadCertificationStatus,
      loadCertificationText,
    }
  }
}
</script>

<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-header-account-dropdown';

.account-scope {
  margin-right: 30px;
  cursor: pointer;
  box-sizing: border-box;
  border-radius: 24px;
  // background: linear-gradient(90deg, #f8fafc 0%, #f2f6fa 100%);
  background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(135deg, #EFF6FF -4%, #FFFFFF 100%);
  height: 44px;
  min-width: 260px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);

  .drop-title {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 22px;
    color: #222;
    font-weight: 500;
    font-size: 15px;
    .tenant-name {
      // max-width: 170px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      display: inline-block;
      vertical-align: middle;
      // margin-right: 10px;
    }
    .tenant-tag{
      margin-right: 20px;
      margin-left: 20px;
      font-size: 12px;
      font-weight: 400;
      
      // padding: 0px 4px;
      // align-items: center;
      // justify-content: center;
      // text-align: center;
      // height: 16px;
      // border-bottom-left-radius: 8px;

    }
    :deep(svg) {
      margin-left: 10px;
      vertical-align: middle;
    }
  }

  :deep(.ant-dropdown-content) {
    border: 1px solid #e6e6e6;
    background-color: #fff;
    min-height: 80px;
    overflow: auto;
    padding-bottom: 80px;
    // border-radius: 8px;
    box-shadow: 0 4px 24px rgba(0,0,0,0.08);
  }

  .account-panel {
    width: 400px;
    background: #fff;
    // border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  }

  .account-list {
    overflow: auto;
    padding-bottom: 50px;
    max-height: 400px;

    .account-title {
      padding: 14px 16px 14px 24px;
      background: #f7fafd;
      border-bottom: 1px solid #f0f0f0;
      color: #000;
      font-size: 13px;
      font-weight: 600;
      letter-spacing: 0.5px;
    }

    .no-use {
      pointer-events: none;
      opacity: 0.6;
    }
  }

  .account-panel-item {
    transition: background 0.2s;
    cursor: pointer;
    &:hover:not(.no-use) .account-header {
      background: #eaf6ff;
    };

  }

  .account-header {
    margin-bottom: 0;
    font-size: 14px;
    background: #fff;
    padding: 14px 16px 14px 32px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #f4f4f4;
    color: #333;
    border-radius: 0;
    cursor: pointer;
    transition: background 0.2s;
    .current-login {
      color: #127fd2;
      font-weight: 600;
    }
    .ellipsis {
      max-width: 240px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      display: inline-block;
      vertical-align: middle;
    }
    &.disabled {
      cursor: not-allowed;
      opacity: 0.7;
    }
    a-tag {
      margin-left: 8px;
      font-size: 12px;
      // border-radius: 4px;
      padding: 0 6px;
    }
  }

  .ant-menu > .ant-menu-item-divider {
    margin: 0;
  }

  :deep(.ant-menu-item-only-child) {
    padding-left: 20px !important;
    margin: 0;
  }

  :deep(.items-center) {
    display: flex;
    justify-content: space-between;
  }

  .join-container {
    background: #f9fbfc;
    position: absolute;
    bottom: 8px;
    left: 0;
    width: 100%;
    padding: 10px 0 0 0;
    border-top: 1px solid #f0f0f0;
    display: flex;
    flex-direction: column;
    gap: 8px;

    .join-enter {
      margin: 0 16px 8px 16px;
      border: 1px solid #e0e6ed;
      padding: 10px 14px;
      // border-radius: 8px;
      display: flex;
      align-items: center;
      cursor: pointer;
      background: #fff;
      transition: border-color 0.2s, color 0.2s, background 0.2s;
      .app-iconify {
        background: #f4f4f4;
        width: 30px;
        height: 30px;
        display: flex;
        justify-content: center;
        align-items: center;
        // border-radius: 5px;
        margin-right: 10px;
      }
      span {
        font-size: 14px;
        font-weight: 500;
      }
      &:hover {
        color: #127fd2;
        border-color: #127fd2;
        background: #f4faff;
      }
    }
  }

  &--dropdown-overlay {
    .ant-dropdown {
      background: #fff;
    }
    .ant-dropdown-menu-item {
      min-width: 250px;
    }
  }
}
</style>
<!--
  @description 账号身份切换组件

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
    <div class="header-identity">
        <div class="tenant-list tenant-list-keep-px">
            <!-- <van-dropdown-menu >
			    <van-dropdown-item v-model="tenantId" :options="columns" placeholder="请选择" />
			</van-dropdown-menu> -->
            <div class="tenant-name-view" @click="showSelectTenant">
                <div class="tenant-name">{{ userInfo.loginTenantName }}</div>
                <div class="exchange">
                    <van-icon name="exchange"  />
                </div>
            </div>
        </div>
        <div class="right_side">
            <slot></slot>
        </div>
    </div>
    <VanDialog v-model:show="joinEnterprise.show" confirm-button-text="加入" cancel-button-text="取消"
        @confirm="handleJoinSubmit" title="加入企业" :showCancelButton="true">
        <van-cell-group inset>
            <van-field v-model.trim="joinEnterprise.invitationCode" placeholder="请输入企业邀请码" input-align="center" />
            <p style="font-size: 12px; color: #999;">企业邀请码（即团队码）是一个 8 位字母编码，可联系企业管理员获取</p>
        </van-cell-group>
    </VanDialog>
    <VanDialog v-model:show="logout.show" confirm-button-text="退出系统" cancel-button-text="取消" @confirm="sysLogout"
        title="是否确认退出系统" :showCancelButton="true">
        <div style="padding: 10px;"></div>
    </VanDialog>
    <VanDialog
        v-model:show="selectDepart.show"
        confirm-button-text="关闭"
        @confirm="selectDepart.show = false"
        :closeOnClickOverlay="true" confirm-button-color="#000">
        <div class="select-tenant-header">
            <div class="title">
                <span>身份切换</span>
            </div>
            <div class="logout">
                <span 
                    @click="logout.show = true; 
                    selectDepart.show = false" 
                    class="custom-link"
                    style="font-size: 0.4rem !important;"
                    >退出登录
                </span>
            </div>
        </div>
        <div class="account-list">
            <div class="ent-list">
                <div class="account-title" v-if="tenantList && tenantList.length > 0">企业账户</div>
                <template v-for="(item, index) in tenantList">
                    <div :class="['account-item', item.useFlag ? '' : 'no-use']" v-if="item.tenantType == 1">
                        <p :key="index" class="company-header" @click="handleSubmit(item)">
                            <!-- <span>{{ item.tenantName }}</span> -->
                            <span :class="[item.selectFlag ? 'current-login' : '']">{{ item.tenantName }}</span>
                            <!-- <span class="current-login-tag" v-if="item.selectFlag">
                                <van-tag color="#127fd2">当前登录</van-tag>
                            </span> -->
                        </p>
                    </div>
                </template>
                <div class="account-title" v-if="personalList && personalList.length > 0">个人账户</div>
                <template v-for="(item, index) in personalList">
                    <div :class="['account-item', item.useFlag ? '' : 'no-use']" v-if="item.tenantType == 2"
                        @click="handleSubmit(item)">
                        <p :key="index" class="company-header">
                            <span>{{ item.tenantName }}</span>
                            <!-- <van-tag color="#127fd2" v-if="item.selectFlag">当前登录</van-tag> -->
                        </p>
                    </div>
                </template>
            </div>
            <div class="join-container">
                <div class="join-enter" @click.stop="handleJoinEnterprise">
                    <van-icon size="0.6rem" name="friends-o" />
                    <span>加入已有企业</span>
                </div>
                <div class="join-enter" v-if="!userInfo.personalTenantFlag" @click.stop="handleOpenPersonalTenant">
                    <van-icon size="0.6rem" name="friends-o" />
                    <span>开通个人空间</span>
                </div>
            </div>
        </div>
    </VanDialog>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, onUpdated, onBeforeUnmount, defineExpose } from "vue";
import { FieldType, Notify, Dialog } from 'vant';
import { useUserStore } from '@/store/modules/user';
import Api, { LoginParams } from '@/api/user';
import watermark from '@/utils/lib/watermark';
import copyPaste from '@/utils/lib/copy-paste';

const emit = defineEmits(["handleJoinSubmitCall"]);





interface JoinEntp {
    show: Boolean;
    invitationCode: string;
    value: string;
}
const loginFrom = ref();
const VanDialog = Dialog.Component;
const userStore = useUserStore();
const userInfo = ref({});

const tenantList = ref([]);
const personalList = ref([]);

const router = useRouter();
const selectDepart = ref({
    show: false,
});
const logout = ref({
    show: false,
});
const joinEnterprise = ref<JoinEntp>({
    show: false,
    invitationCode: "",
    value: ""
});

function onSubmit() {

}
function initData() {
    tenantList.value = userStore.getTenantDeparts.filter((item: any) => item.tenantType == 1);
    personalList.value = userStore.getTenantDeparts.filter((item: any) => item.tenantType == 2);
    userInfo.value = userStore.getUserInfo;
    console.log('reload initData', tenantList.value, personalList.value);
}
const onFailed = (errorInfo = {}) => {
    console.log('failed', errorInfo);
};
async function showSelectTenant() {
    await userStore.reloadTenantDeparts();
    await userStore.afterLoginAction();
    userInfo.value = userStore.getUserInfo;
    setTimeout(() => {
        selectDepart.value.show = true;
    })
}
function sysLogout() {
    userStore.logout();
    router.push("/login")
    logout.value.show = false;
}
onMounted(() => {
    copyPaste.enable();
});
onUpdated(() => {
    initData();
});
initData();
onBeforeUnmount(() => {
    copyPaste.disable();
});

const handleSubmit = async(item: any)=>{
    if (item.selectFlag) {
        return;
    }
    const formData = {
        departId: item.departs[0].departId
    }
    const result = await userStore.selectTenant(formData);
    if (result.status) {
        await userStore.reloadTenantDeparts();
        Notify({ type: 'success', message: '切换成功', duration: 1000 });
        window.location.reload();
    }
}

function handleJoinEnterprise() {
    selectDepart.value.show = false;
    joinEnterprise.value.show = true;
}
async function handleJoinSubmit() {
    if (joinEnterprise.value.invitationCode && joinEnterprise.value.invitationCode.length == 8) {
        const { result, code } = await Api.jionTenant({ invitationCode: joinEnterprise.value.invitationCode });
        if (code == 200) {
            if (result == 1) {
                Notify({ type: 'success', message: '加入企业成功', duration: 3000 });
                emit("handleJoinSubmitCall",{result: 1});
            } else {
                Notify({ type: 'success', message: '已申请加入，管理员开启了申请加入审核流程，请耐心等待或联系管理员及时审核', duration: 10000 });
                emit("handleJoinSubmitCall",{result: 0});
            }

            const deptRes = await userStore.reloadTenantDeparts();
            tenantList.value = deptRes.filter((item: any) => item.tenantType == 1);
            selectDepart.value.show = false;
        }
    } else {
        Notify({ type: 'warning', message: '企业邀请码（即团队码）是一个 8 位字母编码，可联系企业管理员获取', duration: 5000 });
    }
}
async function handleOpenPersonalTenant() {
    let result = await Api.openPersonalTenant();
    if (result.code == 200) {
        Notify({ type: 'success', message: '开通成功', duration: 1000 });
        tenantList.value = await userStore.reloadTenantDeparts();
        await userStore.afterLoginAction();
        userInfo.value = userStore.getUserInfo;
        selectDepart.value.show = false;
    }
}
defineExpose({
    handleSubmit,
    handleJoinEnterprise
})
</script>

<style lang="less" scoped>
.header-identity {
    width: 100%;
    padding: 10px 0px;
    height: 1.2rem;
    background-color: #fff;
    display: flex;

    // margin-bottom: 0.2rem;
    :deep(.van-dropdown-menu) {
        height: 100%;
    }

    :deep(.van-dropdown-menu__bar) {
        height: 100%;
    }
}

.tenant-list-keep-px {
    :deep(.van-dropdown-item__content) {
        padding: 0 10px;
        background: transparent;
    }
}

.header-identity .logout {
    width: 1.2rem;
    display: flex;
    text-align: center;
    align-items: center;
    justify-content: center;


}

.header-identity .right_side {
    width: auto;
    text-align: center;
    display: flex;
    align-items: center;
    text-align: center;
}

.header-identity .tenant-list {
    flex: 1;
    display: flex;
    align-items: center;
    padding-left: 10px;

    .tenant-name-view {
        width: 7rem;
        border: 1px solid #efefef;
        line-height: 0.8rem;
        padding-left: 20px;
        display: flex;
        border-radius: 24px;
        background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(135deg, #EFF6FF -4%, #FFFFFF 100%);
        color: #000;
        font-weight: 600;

        .tenant-name {
            flex: 1;
            white-space: nowrap;
            /* 确保文本在一行内显示 */
            overflow: hidden;
            /* 隐藏超出容器的内容 */
            text-overflow: ellipsis;
            /* 使用省略号表示被截断的文本 */
        }

        .exchange {
            width: 0.5rem;
            margin-right: 20px;
        }
    }
}

.account-list {
    // .account-item:hover .company-header {
    //     background: #efefef;
    // }
    text-align: left;
    margin-top: 1.3rem;
    padding-left: 10px;

    .ent-list {
        .account-title {
            padding: 0.3rem 0.3rem;
            background: #f9f9f9;
        }
    }

    overflow: auto;
    max-height: 6.6rem;

    .account-item {
        /* font-size: 14px; */
        border-bottom: 5px solid #f0f0f0;
        border-radius: 8px;
        cursor: pointer;
        align-items: center;
        justify-content: space-between;
        margin-right: 10px;
        margin-bottom: 10px;
        padding-left: 30px;

        .company-header {
            /* background: #f9f9f9; */
            padding: 0.3rem 0.3rem;
            display: flex;
            justify-content: space-between;
            margin: 0 !important;

            .login-arrow {
                visibility: hidden;
            }

            &:active {
                color: #127fd2;
            }

            .current-login-tag {
                width: 120px;
                justify-content: center;
                align-items: center;
                display: flex;
            }
        }

        .account-depart {
            display: flex;
            margin-bottom: 10px;
            justify-content: space-between;
            padding: 0 20px;
        }

        .current-login {
            color: #127fd2;
        }
    }

    .join-container {
        background-color: #fff;
        /* position: absolute; */
        bottom: 2px;
        width: 97%;

        .join-enter {
            margin: 20px;
            border: 2px solid #ddd;
            padding: 20px 20px;
            border-radius: 10px;
            align-items: center;
            text-align: center;

            &:hover {
                color: #127fd2;
                border-color: #127fd2;
            }
        }
    }

    .no-use {
        pointer-events: none;
    }
}

.select-tenant-header {
    position: absolute;
    top: 0;
    left: 0;
    height: 1.2rem;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 0.7rem; /* 添加内边距以确保内容不会贴边 */

    .title {
        font-size: 0.45rem;
        font-weight: 600;
        flex: 1;
        display: flex;
        align-items: center;
        text-align: center;
        justify-content: center;
        /* 添加轻微的文本阴影 */
        text-shadow: 0.3px 0 0 currentColor, 0 0.3px 0 currentColor;
    }

    .logout {
        width: 0 0 2.4rem;
        display: flex;
        align-items: center;
        justify-content: flex-end;
    }

}

.join-tenant {
    .title {
        font-size: 0.5rem;
        font-weight: 600;
        flex: 1;
        line-height: 1.2rem;
        padding-left: 20px;
    }
}
</style>
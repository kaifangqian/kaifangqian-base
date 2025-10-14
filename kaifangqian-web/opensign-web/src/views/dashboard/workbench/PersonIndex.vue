<template>
  <div class="person-content">
    <div class="main">
      <Card title="关联企业" style="width: 100%">
        <template #extra>
          <!-- <a-button type="default" size="small" @click="handleJoin">加入企业</a-button> -->
          <a-button type="primary" size="small" @click="handleAuthEnterPrise"
            >加入/创建企业</a-button
          >
        </template>
        <div>
          <div v-if="joinEnterprise && joinEnterprise.length" class="join-enterprise">
            <li
              v-for="(item, index) in joinEnterprise"
              :key="index"
              @click="handleChangeTenant(item)"
              class="enterprise-card"
            >
              <div class="enterprise-info">
                <span class="enterprise-name">{{ item.name }}</span>
                <a-tag :style="{
                    color: loadCertificationStatus(item.authStatus),
                    backgroundColor: loadCertificationStatus(item.authStatus) + '20',
                    borderColor: loadCertificationStatus(item.authStatus) + '20'
                }">{{
                  loadCertificationText(item.authStatus)
                }}</a-tag>
              </div>
              <div class="upcoming-info">
                <p>
                  <span class="upcoming-title">待处理文件</span>
                  <span class="upcoming-value">{{ item.myJobCount || 0 }}</span>
                  <Icon icon="ion:chevron-right" color="#666" />
                </p>
              </div>
            </li>
          </div>
          <div v-else class="no-data">
            <img src="~@/assets/images/no-data.png" alt="" />
            <p style="text-align: center">
              <a-button type="primary" size="small" style="margin-top: 15px" @click="handleAuthEnterPrise"
                >加入/创建企业</a-button
              >
            </p>
          </div>
        </div>
      </Card>
      <PendingHandle />
    </div>
    <div class="info">
      <Card style="width: 100%">
        <div class="account-info">
          <!-- <CropperAvatar
               :uploadApi="(uploadAvatarApi as any)"
               :value="userInfo.avatarImg"
               btnText=""
               :showBtn="false"
               :btnProps="{ preIcon: 'ant-design:cloud-upload-outlined' }"
               @change="updateAvatar"
               width="80"
             /> -->
          <div class="user-info">
            <div>
              <span class="user-name">{{ tenantInfo.name || userInfo.realname }}</span>
              <a-tag :style="{
                    color: loadCertificationStatus(tenantInfo.authStatus),
                    backgroundColor: loadCertificationStatus(tenantInfo.authStatus) + '20',
                    borderColor: loadCertificationStatus(tenantInfo.authStatus) + '20'
                }">{{
                loadCertificationText(tenantInfo.authStatus)
              }}</a-tag>
              <!-- <a-button type="link">去认证</a-button> -->
              <!-- <a-button type="link" size="small" @click="toPersonAuth" v-if="tenantInfo.authStatus == 0">去认证</a-button> -->
              <a-button
                type="link"
                @click="toPersonAuth"
                >{{ tenantInfo.authStatus != '2' ? '立即认证' : '认证变更' }}</a-button
              >
            </div>
            <div>
              <span class="info-title">用户账号：</span>
              <span>{{ userInfo.username }}</span>
            </div>
          </div>
        </div>
      </Card>

      <Card style="width: 100%">
        <div class="upgrade-content">
          <div class="title">
            <SvgIcon name="entp-vip"></SvgIcon>
            升级为企业用户，可享受以下权益
          </div>
          <ul class="content">
            <li class="content-li border-buttom">
              <div class="icon">
                <SvgIcon size="60" name="entp-manage"></SvgIcon>
              </div>
              <div class="upgrade-descriptions">
                <p class="title">企业管理</p>
                <div class="description-list">
                  <li>组织及成员管理</li>
                  <li>角色及权限管理</li>
                </div>
              </div>
            </li>
            <li></li>
          </ul>
          <ul class="content">
            <li class="content-li">
              <div class="icon">
                <SvgIcon size="60" name="file-sign"></SvgIcon>
              </div>
              <div class="upgrade-descriptions">
                <p class="title">签发文件管理</p>
                <div class="description-list flex">
                  <li>企业签发文件</li>
                  <li>文件模板管理</li>
                  <li>企业印章管理</li>
                  <li>业务线管理</li>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </Card>

      <Card style="width: 100%">
        <div class="help-center">
          <div class="title">
            <div class="title-left">
              <SvgIcon name="help-center"></SvgIcon>
              帮助中心
            </div>
            <div class="more">
              <a-space :size="20">
                <div
                  class="video pointer"
                  @click="
                    helpJump(
                      'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E6%93%8D%E4%BD%9C%E8%A7%86%E9%A2%91',
                    )
                  "
                >
                  <a-tooltip>
                    <template #title>视频讲解</template>
                    <SvgIcon name="video-des"></SvgIcon>
                  </a-tooltip>
                </div>
                <div class="pointer" @click="helpJump('https://docs.kaifangqian.com/')"> 更多 </div>
              </a-space>
            </div>
          </div>
          <div class="content">
            <a-space :size="40">
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%AE%9E%E5%90%8D%E8%AE%A4%E8%AF%81/%E4%B8%AA%E4%BA%BA%E5%AE%9E%E5%90%8D%E8%AE%A4%E8%AF%81',
                  )
                "
                >个人实名认证</span
              >
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/%E6%88%91%E6%98%AF%E4%BC%81%E4%B8%9A%E7%AE%A1%E7%90%86%E5%91%98',
                  )
                "
                >5步完成电子文件签署</span
              >
            </a-space>
            <a-space :size="40">
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%AE%9E%E5%90%8D%E8%AE%A4%E8%AF%81/%E4%BC%81%E4%B8%9A%E5%AE%9E%E5%90%8D%E8%AE%A4%E8%AF%81',
                  )
                "
                >企业实名认证</span
              >
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8/%E7%94%B5%E5%AD%90%E5%90%88%E5%90%8C%E7%AD%BE%E7%BA%A6%E5%9C%BA%E6%99%AF%E7%A4%BA%E4%BE%8B/B2C%E7%AD%BE%E7%BA%A6%E5%9C%BA%E6%99%AF%E7%A4%BA%E4%BE%8B',
                  )
                "
                >电子签约场景示例</span
              >
            </a-space>
            <a-space :size="40">
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E4%BC%81%E4%B8%9A%E6%9D%83%E9%99%90%E7%AE%A1%E7%90%86/%E5%A6%82%E4%BD%95%E8%AE%BE%E7%BD%AE%E6%88%90%E5%91%98%E6%9D%83%E9%99%90',
                  )
                "
                >企业权限管理</span
              >
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E6%A8%A1%E6%9D%BF%E7%AE%A1%E7%90%86/%E6%A8%A1%E6%9D%BF%E4%BB%8B%E7%BB%8D',
                  )
                "
                >企业文档模板管理</span
              >
            </a-space>
            <a-space :size="40">
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E5%8D%B0%E7%AB%A0%E7%AE%A1%E7%90%86/%E4%BC%81%E4%B8%9A%E5%8D%B0%E7%AB%A0/%E5%8D%B0%E7%AB%A0%E5%88%B6%E4%BD%9C',
                  )
                "
                >企业印章管理</span
              >
              <span
                class="pointer help-item"
                style="color: #999; font-size: 12px"
                @click="
                  helpJump(
                    'https://docs.kaifangqian.com/docs/%E4%BA%A7%E5%93%81%E6%89%8B%E5%86%8C/%E4%B8%9A%E5%8A%A1%E7%BA%BF%E7%AE%A1%E7%90%86/%E4%B8%9A%E5%8A%A1%E7%BA%BF%E4%BB%8B%E7%BB%8D',
                  )
                "
                >签发业务线配置</span
              >
            </a-space>
          </div>
        </div>
      </Card>
    </div>
    <Invitation @register="registerModal" @success="handleSuccess" />
    <CompanyQueryModal @register="registerQueryCompanyModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts" setup>
  import type { UserInfo } from '/#/store';
  import { ref, defineComponent, onMounted, unref, onUpdated, h } from 'vue';
  import { Tag, Tabs, Card } from 'ant-design-vue';
  import { useUserStore } from '/@/store/modules/user';
  import { useModal } from '/@/components/Modal';
  import { useMessage} from '/@/hooks/web/useMessage';
  import Invitation from '/@/layouts/default/header/components/change-account/Invitation.vue';
  import PendingHandle from './PendingHandle.vue';
  import { useRouter } from 'vue-router';
  import { getJoinedEnterprise } from '/@/api/sys/user';
  import { SvgIcon, Icon } from '/@/components/Icon';
  import { CropperAvatar } from '/@/components/Cropper';
  import { uploadAvatarApi, getImgBase64 } from '/@/api/sys/upload';
  import { getUserInfo } from '/@/api/sys/user';
  import { getMyStastics, getListMyFillInJob } from '/@/api/contract';
  import CompanyQueryModal from '/@/views/sys/user/modal/CompanyQueryModal.vue';
  // import { personAuthApi } from '/@/api/auth/userAuth';
  import {handleAuth,handleUpdateAuth} from '/@/views/sys/user'
  interface joinEnterpriseItem {
    name: string;
    authStatus: number;
    myJobCount: number;
    approveCount: number;
  }
  const { createMessage: msg,createConfirm } = useMessage();
  const userStore = useUserStore();
  const router = useRouter();
  const tenantInfo = userStore.getTenantInfo;

  const userInfo = ref<UserInfo>({
    phone: '',
    avatarImg: '',
    loginTenantType: 0,
  });

  const joinEnterprise = ref(<joinEnterpriseItem[]>[]);

  const [
    registerQueryCompanyModal,
    { openModal: openQueryCompanyModal, closeModal: closeQueryCompanyModal },
  ] = useModal();
  const [registerModal, { openModal, closeModal }] = useModal();
  onMounted(() => {
    document.addEventListener('visibilitychange', function () {
      if (!document.hidden) {
        fetch();
      }
    });

    loadJoinedEnterprise();
    userInfo.value = userStore.getUserInfo;
  });
  onUpdated(() => {
    loadJoinedEnterprise();
  });

  async function updateAvatar(src: string, data) {
    // fetch()
  }
  const fetch = async () => {
    userInfo.value = await getUserInfo();
    if (userInfo) {
      userStore.setUserInfo(unref(userInfo));
      if (userInfo.value.avatar) {
        getAvatar(userInfo.value.avatar);
      }
    }
  };

  async function getAvatar(id) {
    let result1 = await getImgBase64({ imgId: id });
    if (result1) {
      userInfo.value.avatarImg = result1.image;
    }
  }

  function handleAuthEnterPrise() {
    openQueryCompanyModal(true, { isUpdate: false });
  }

  async function toPersonAuth() {
    
    if(tenantInfo.authStatus === 2){
      handleUpdateAuth(userInfo.value.loginTenantType as number);
    }else{
      createConfirm({
            iconType: 'info',
            width: '600px',
            title: '开启安全可靠的电子签章',
            // content: '为保证签署真实有效，请先完成个人实名认证校验',
            content: h('div', [
              // 第一部分：标题
              // h('div', {
              //   style: {
              //     fontSize: '16px',
              //     fontWeight: '600',
              //     marginBottom: '12px',
              //     color: '#333'
              //   }
              // }, '开启安全可靠的电子签章'),
    
              // 第二部分：操作指引
              h('div', {
                style: {
                  marginBottom: '16px'
                }
              }, [
                h('div', {
                  style: {
                    marginBottom: '8px',
                    fontWeight: '500'
                  }
                }, '请完成以下步骤：'),
                h('div', {
                  style: {
                    paddingLeft: '8px'
                  }
                }, [
                  h('div', '1. 点击【立即认证】'),
                  h('div', '2. 在「云盾系统」完成个人实名认证')
                ])
              ]),
              
              // 第三部分：服务说明（优化样式）
              h('div', {
                style: {
                  background: '#f0f8ff',
                  border: '1px solid #d0e9ff',
                  borderRadius: '4px',
                  padding: '12px',
                  fontSize: '13px',
                  color: '#31708f',
                  marginRight: '38px'
                }
              }, [
                h('div', {
                  style: {
                    display: 'flex',
                    alignItems: 'flex-start',
                    marginBottom: '6px'
                  }
                }, [
                  h('span', {
                    innerHTML: '&#128274;', // 锁图标
                    style: {
                      marginRight: '8px',
                      fontSize: '14px'
                    }
                  }),
                  h('span', {
                    style: {
                      fontWeight: '600',
                      fontSize: '14px'
                    }
                  }, '安全保障')
                ]),
                h('div', {
                  style: {
                    lineHeight: '1.6',
                    paddingLeft: '24px'
                  }
                }, '云盾系统为电子签章提供核心保障服务，包括实名认证、权威CA证书签发及签署意愿验证，确保签署过程安全合规。')
              ])
            ]),
            okText: '立即认证',
            async onOk() {
              handleAuth(userInfo.value.loginTenantType as number);
            },
          });
      
    }
  }

  function helpJump(path) {
    window.open(path, '_blank');
  }
  function handleJoin() {
    openModal(true, {
      idUpdate: false,
    });
  }
  function handleSuccess() {
    closeModal();
    loadJoinedEnterprise();
  }
  async function loadJoinedEnterprise() {
    let result = await getJoinedEnterprise({});
    if (result) {
      joinEnterprise.value = result;
    }
  }

  async function handleChangeTenant(tenant) {
    let params = {
      departId: tenant.departId,
    };
    if (!tenant.departId) {
      msg.warning('信息缺失（部门id不存在）暂时无法切换');
      return;
    }
    let result = await userStore.selectTenant(params);
    if (result) {
      setTimeout(() => {
        location.reload();
      }, 1000);
    }
  }
  function loadCertificationStatus(status) {
    switch (status) {
      case 0:
        return '#ff9900';
      case 1:
        return '#1891ff';
      case 2:
        return '#19be6b';
      case 3:
        return '#f56c6c';
      default:
        return '#ff9900';
    }
  }
  function loadCertificationText(status) {
    switch (status) {
      case 0:
        return '未认证';
      case 1:
        return '审核中';
      case 2:
        return '已认证';
      case 3:
        return '未通过';
      default:
        return '未认证';
    }
  }
</script>

<style lang="less" scoped>
  .person-content {
    position: relative;
    display: flex;
    min-height: 300px;
    .main {
      width: 1000px;
      height: 100px;
    }
    .info {
      width: 400px;
      height: 100px;
      padding: 0 10px;
      .account-info {
        padding: 24px;
        display: flex;
        align-items: center;
        .user-info {
          margin-left: 20px;

          & > div {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
          }
          .user-name {
            font-size: 16px;
            font-weight: 600;
            margin-right: 20px;
            display: inline-block;
            width: 70px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .info-title {
            margin-right: 20px;
            font-size: 14px;
          }
        }
      }
      .userinfo {
        display: flex;
        padding: 20px;
        .avatar-img {
          width: 100px;
        }
        .username {
          max-width: 100px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
      .upgrade-content {
        .title {
          padding: 10px;
        }
        .content {
          .content-li {
            display: flex;
            height: 80px;
            padding-left: 20px;
            position: relative;
            .icon {
              width: 80px;
              display: flex;
              justify-content: center;
              align-items: center;
            }
            .upgrade-descriptions {
              flex: 1;
              padding-left: 20px;
              .title {
                padding: 0;
                margin: 0;
                font-weight: 600;
              }
              .description-list {
                flex-wrap: wrap;
                li {
                  color: #999;
                  line-height: 28px;
                  font-size: 12px;
                  padding: 0 10px;
                  position: relative;
                }
                li::after {
                  content: '';
                  position: absolute;
                  top: 12px;
                  left: 0;
                  height: 5px;
                  width: 5px;
                  background-color: #eee;
                  border-radius: 50%;
                }
              }
            }
          }

          .content-li.border-buttom::after {
            content: '';
            position: absolute;
            bottom: -5px;
            left: 5%;
            height: 1px;
            background-color: #eee;
            width: 90%;
          }
        }
      }

      .help-center {
        .title {
          padding: 15px 10px;
          display: flex;
          .title-left {
            flex: 1;
          }
          .more {
            text-align: right;
            padding-right: 10px;
          }
        }
        .content {
          padding-left: 40px;
          padding-bottom: 10px;
          .help-item {
            line-height: 30px;
          }
        }
      }
    }

    .join-enterprise {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
      padding: 20px;

      .enterprise-card {
        height: 138px;
        border-radius: 8px;
        opacity: 1;
        flex-direction: column;
        padding: 24px;
        gap: 0px 10px;
        flex-wrap: wrap;
        background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)),
          linear-gradient(135deg, #eff6ff -4%, #ffffff 100%);
        box-sizing: border-box;
        border: 1px solid #f3f4f6;
        width: calc(33% - 16px);
        cursor: pointer;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
        }

        .enterprise-info {
          display: flex;
          align-items: center;

          .enterprise-name {
            font-size: 16px;
            margin-right: 5px;
            color: #000;
            flex: 1;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
        }

        .upcoming-info {
          display: flex;
          align-items: center;
          margin-top: 30px;
          margin-right: 10px;

          .upcoming-title {
            font-size: 14px;
            color: #666;
            margin-right: 100px;
          }

          .upcoming-value {
            font-size: 18px;
            margin-right: 10px;
            // margin-left: 100px;
            // color: #127fd2;
          }
        }

        .anticon {
          font-size: 14px;
          // margin-right: 10px;
        }
      }

      @media (max-width: 768px) {
        .enterprise-card {
          width: calc(50% - 16px);
        }
      }

      @media (max-width: 480px) {
        .enterprise-card {
          width: 100%;
        }
      }
    }

    .enterprise-info .ant-tag {
      // margin-left: 10px;
      font-size: 12px;
      border-bottom-left-radius: 8px;
      margin-right: 5px;
    }

    .ant-btn-primary {
      background: linear-gradient(90deg, #1890ff, #40a9ff);
      border-color: transparent;
      width: 120px;
      height: 30px;
      border-radius: 4px;
    }

    .ant-btn-default {
      background: #fff;
      border-color: #d9d9d9;
      color: #333;
    }
  }
</style>

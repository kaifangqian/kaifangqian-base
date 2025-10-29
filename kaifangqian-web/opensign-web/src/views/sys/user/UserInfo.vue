<template>
  <div class="main-center">
    <Card title="基本信息" class="card-title" size="small">
      <Row>
        <Col :span="24">
          <div class="info-list">
            <div v-if="tenantInfo.tenantType == '1'" class="flex w-full">
              <div style="width: 60px; height: 60px; margin-right: 20px">
                <img src="@/assets/images/company-icon.png" />
              </div>
              <div class="text-sm">
                <div class="flex gap-2 py-1">
                  <!-- <div class="w-[100px] text-justify">企业名称：</div> -->
                  <div style="font-weight: 500; font-size: 18px; margin-right: 8px; color: #000">{{
                    tenantInfo.name
                  }}</div>
                  <a-tag :style="{
                    color: loadCertificationStatus(tenantInfo.authStatus),
                    backgroundColor: loadCertificationStatus(tenantInfo.authStatus) + '20',
                    borderColor: loadCertificationStatus(tenantInfo.authStatus) + '20'
                }"  size="small">{{
                    loadCertificationText(tenantInfo.authStatus)
                  }}</a-tag>
                </div>
                <div
                  class="flex gap-2 py-1"
                  style="font-size: 14px; color: #a0a1a3; margin-top: 4px"
                >
                  <div class="text-justify">统一社会信用代码：</div>
                  <div>{{ tenantInfo.organizationNo ? tenantInfo.organizationNo : '--' }}</div>
                  <div style="margin: 0 18px">|</div>
                  <div class="text-justify">法人姓名：</div>
                  <!-- <div>{{ tenantInfo.corporation }}</div> -->
                  <div>{{ tenantInfo.corporation ? tenantInfo.corporation : '--' }}</div>
                </div>
              </div>
              <div class="flex-1 flex justify-end items-center">
                <a-button
                  type="primary"
                  v-if="
                    tenantInfo.authStatus === 2 &&
                    userInfo.tenantUserId == tenantInfo.applyTenantUser
                  "
                  @click="reloadAuth"
                  >认证变更</a-button
                >
                <a-button
                  type="primary"
                  v-if="
                    tenantInfo.authStatus !== 2 &&
                    userInfo.tenantUserId == tenantInfo.applyTenantUser
                  "
                  @click="toAuth"
                  >立即认证</a-button
                >
              </div>
            </div>
            <div v-if="tenantInfo.tenantType == '2'" class="flex w-full" style="width: 100%">
              <div style="width: 60px; height: 60px; margin-right: 20px">
                <img src="@/assets/images/profile-picture-icon.png" />
              </div>
              <div class="text-sm">
                <div class="flex gap-2 py-1">
                  <div style="font-weight: 500; font-size: 18px; margin-right: 8px; color: #000">{{ tenantInfo.name || userInfo.realname }}
                  </div>
                  <a-tag :style="{
                    color: loadCertificationStatus(tenantInfo.authStatus),
                    backgroundColor: loadCertificationStatus(tenantInfo.authStatus) + '20',
                    borderColor: loadCertificationStatus(tenantInfo.authStatus) + '20'
                }"  size="small">{{
                    loadCertificationText(tenantInfo.authStatus)
                  }}</a-tag>
                </div>
                <div
                  class="flex gap-2 py-1"
                  style="font-size: 14px; color: #a0a1a3; margin-top: 4px"
                >
                  <div class="text-justify">证件类型：</div>
                  <div>
                    {{
                      {
                        '1': '中国居民身份证',
                        '2': '港澳居民居住证',
                        '3': '台湾居民居住证',
                        '4': '港澳居民来往内地通行证',
                        '5': '台湾居民来往大陆通行证',
                        '6': '外国人永久居留居住证',
                        '7': '国际护照',
                      }[tenantInfo.idCardType] || '--'
                    }}
                  </div>
                  <div style="margin: 0 18px">|</div>
                  <div class="text-justify">证件号：</div>
                  <div>{{ tenantInfo.organizationNo ? tenantInfo.organizationNo : '--' }}</div>
                </div>
              </div>
              <div class="flex-1 flex justify-end items-center">
                <a-button type="primary" v-if="tenantInfo.authStatus === 2" @click="reloadAuth"
                  >认证变更</a-button
                >
                <a-button type="primary" v-if="tenantInfo.authStatus !== 2" @click="toAuth"
                  >立即认证</a-button
                >
              </div>
            </div>
          </div>
        </Col>
      </Row>
    </Card>
    <Card title="安全配置" size="small" style="margin-top: 20px" class="content-info">
      <div class="info-line-list">
        <div class="line-item">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">用户账号</span>
                <span class="info">{{ userInfo.username }} </span>
              </div>
              <div class="info-tip"
                >账号支持多种登录方式，默认使用账号密码登录，可以通过绑定手机号、邮箱进行登录
              </div>
            </Col>
          </Row>
        </div>
        <div class="line-item">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">登录密码</span>
                <span class="info">********** </span>
                <span class="password-state-bar">
                  <Tag :color="tagBar.weakBarColor"></Tag>
                  <Tag :color="tagBar.averageBarColor"></Tag>
                  <Tag :color="tagBar.strongBarColor"></Tag>
                  <span class="password-state-text">{{ tagBar.strongText }}</span>
                </span>
              </div>
              <div class="info-tip"
                >安全性高的密码可以使帐号更安全，建议您定期更换密码以保护帐号安全。
              </div>
            </Col>
            <Col :span="4">
              <a-button type="link" class="action-btn" @click="handleChangePassword"
                >修改密码</a-button
              >
            </Col>
          </Row>
        </div>
        <div class="line-item">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">手机号</span>
                <span class="info"
                  >{{
                    userInfo.phone
                      ? '+86 ' +
                        (userInfo.phone &&
                          userInfo?.phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2'))
                      : '暂无绑定手机号'
                  }}
                </span>
              </div>
              <div class="info-tip"
                >手机号可以用于登录、找回密码等，若该号码已丢失或停用，请立即更换以保护帐号安全。
              </div>
            </Col>
            <Col :span="4">
              <a-button
                type="link"
                class="action-btn"
                :disabled="!userInfo.phoneEditFlag"
                @click="handleChangePhone(userInfo.phone)"
                >{{ userInfo.phone ? '解绑' : '绑定' }}</a-button
              >
            </Col>
          </Row>
        </div>
        <div class="line-item">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">邮箱</span>
                <span class="info">{{ userInfo.email || '暂未绑定邮箱' }}</span>
              </div>
              <div class="info-tip"
                >邮件地址可以用于找回密码，若该邮件地址已丢失或停用，请立即更换以保护帐号安全。
              </div>
            </Col>
            <Col :span="4">
              <a-button
                type="link"
                class="action-btn"
                :disabled="!userInfo.emailEditFlag"
                @click="handleChangeEmail(userInfo.email)"
                >{{ userInfo.email ? '解绑' : '绑定' }}</a-button
              >
            </Col>
          </Row>
        </div>
        <!--         
        <div class="line-item">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">签署密码</span>
                <span class="info">
                  {{ signConfig.password ? '**********' : '暂未设置签署密码' }}</span
                >
              </div>
              <div class="info-tip">在合同签署环节进行验证</div>
            </Col>
            <Col :span="4">
              <a-button type="link" class="action-btn" @click="handleChangeSignPassword">{{
                signConfig.password ? '修改' : '设置'
              }}</a-button>
            </Col>
          </Row>
        </div>
        <div class="line-item">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">优先签署密码校验</span>
              </div>
              <div class="info-tip"
                >开启后，未指定特殊校验方式（如：人脸识别签署校验）的合同优先推荐签署密码校验，开启后会自动关闭“双重校验”
              </div>
            </Col>
            <Col :span="4" class="info-action">
              <a-switch
                class="action-switch"
                v-model:checked="isPasswordValidate"
                @click="(e) => handleSetValidate(e, 'password')"
              ></a-switch>
            </Col>
          </Row>
        </div>
        <div class="line-item" style="margin-bottom: 20px">
          <Row>
            <Col :span="20">
              <div class="info-item">
                <span class="title">双重校验</span>
              </div>
              <div class="info-tip"
                >开启后，未指定特殊校验方式（如：人脸识别签署校验）的合同需要进行签署密码+验证码双重校验，开启后会自动关闭“优先签署密码校验”
              </div>
            </Col>
            <Col :span="4" class="info-action">
              <a-switch
                class="action-switch"
                v-model:checked="isDoubleValidate"
                @click="(e) => handleSetValidate(e, 'double')"
              ></a-switch>
            </Col>
          </Row>
        </div> -->
      </div>
    </Card>

    <ResetPassword @register="registerModal" @success="handleSuccess" @back="handleBack" />
    <ResetPhone @register="registerPhoneModal" @success="handleSuccess" @back="handleBack" />
    <ResetEmail @register="registerEmail" @success="handleSuccess" @back="handleBack" />

    <SignPassword @register="registerSignModal" @success="handleSignSuccess" />
    <SignFreeSet @register="registerSignFeeModal" @success="handleSuccess" />
    <ResetUserName @register="registerUserName" @success="handleSuccess" />
    <MobileOrEmail @register="registerPhoneEmailModal" @success="handleMethodsSuccess" />
    <!-- <PersonalModal @register="registerPersonalModal" @success="handleAuthSuccess" /> -->
    <!-- <EnterpriseModal @register="registerEnterpriseModal" @success="handleAuthSuccess" /> -->

    <a-modal
      :visible="previewVisible"
      title="证件照片"
      :footer="null"
      @cancel="handleCancel"
      width="1200px"
    >
      <div class="paperwork-area" v-if="tenantInfo.tenantType == '1'">
        <img :src="idPic1" />
        <img :src="idPic2" />
      </div>
      <div class="paperwork-area" v-else>
        <img :src="organizationPic" />
      </div>
    </a-modal>
  </div>
</template>
<script lang="ts">
  import type { UserInfo } from '/#/store';
  import { defineComponent, onMounted, reactive, ref, unref, computed, watch, h } from 'vue';
  import { Card, Row, Col, Tag } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  import { useUserStore } from '/@/store/modules/user';
  import ResetPassword from './modal/ResetPassword.vue';
  import ResetPhone from './modal/ResetPhone.vue';
  import ResetEmail from './modal/ResetEmail.vue';
  import ResetUserName from './modal/ResetUserName.vue';
  // import PersonalModal from './modal/PersonalModal.vue';
  import SignPassword from './modal/SignPassword.vue';
  import SignFreeSet from './modal/SignFreeSet.vue';
  // import EnterpriseModal from './modal/EnterpriseModal.vue';
  import { getUserInfo, getSignConfig, setSignValidateType } from '/@/api/sys/user';
  import MobileOrEmail from '/@/views/sys/login/MobileOrEmail.vue';
  import { CropperAvatar } from '/@/components/Cropper';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRouter } from 'vue-router';
  import { uploadAvatarApi, getImgBase64 } from '/@/api/sys/upload';
  import { handleAuth, handleUpdateAuth } from '/@/views/sys/user';
  import {
    loadCertificationText,
    loadCertificationStatus,
    loadCertificationIcon,
  } from '/@/utils/StatusToName';

  export default defineComponent({
    name: '用户中心',
    components: {
      Card,
      Row,
      Col,
      ResetPassword,
      ResetPhone,
      ResetEmail,
      ResetUserName,
      SignPassword,
      SignFreeSet,
      Tag,
      CropperAvatar,
      MobileOrEmail,
    },
    setup() {
      let userInfo = ref<UserInfo>({
        phone: '',
        avatarImg: '',
      });
      const signConfig = ref({
        password: '',
        validatetype: '',
      });
      const isPasswordValidate = ref(false);
      const isDoubleValidate = ref(false);
      const route = useRouter();
      const idPic1 = ref('');
      const idPic2 = ref('');
      const organizationPic = ref('');
      // 登录信息修改类型（密码修改、解绑手机、解绑邮箱）
      const methodsModalType = ref('password');
      //解绑邮箱或者手机默认值
      const defaultUnbindValue = ref('');

      const tagBar = reactive({
        passwordLevel: 'WEAK',
        weakBarColor: '#cdc0c0',
        averageBarColor: '#f50',
        strongBarColor: '#cdc0c0',
        strongText: '弱',
      });
      const previewVisible = ref(false);
      const userStore = useUserStore();
      const paperImgs = ref([]);
      const confirmType = ref(''); //没有设置密码时想要打开的校验方式
      const { createMessage: msg, createConfirm } = useMessage();
      const [registerModal, { openModal, closeModal }] = useModal();
      const [registerSignModal, { openModal: openSignModal, closeModal: closeSignModal }] =
        useModal();
      const [registerPhoneModal, { openModal: openPhoneModal }] = useModal();
      const [registerSignFeeModal, { openModal: openSignFreeModal }] = useModal();
      const [registerEmail, { openModal: openEmailModal }] = useModal();
      //用户名修改
      const [registerUserName, { openModal: openUserModal }] = useModal();
    
      const [
        registerPhoneEmailModal,
        { openModal: openMethodModal, closeModal: closePhoneEmailModal },
      ] = useModal();
      const storeUserInfo = userStore.getUserInfo;
      const tenantInfo = ref({ ...userStore.getTenantInfo });

      // console.log(tenantInfo, '---租户信息--');
      // userStore.getTenantInfo
      tagBar.passwordLevel = storeUserInfo.passwordLevel as string;
      // userInfo.value = storeUserInfo.value;
      setPasswordBarColor(tagBar.passwordLevel);

      const fetch = async () => {
        userInfo.value = await getUserInfo();
        if (userInfo) {
          userStore.setUserInfo(unref(userInfo));
          // setTableData(userInfo.value.authGroups)
          if (userInfo.value.avatar) {
            // getAvatar(userInfo.value.avatar);
          }
        }
        // if(tenantInfo.tenantType == '1'){
        // if(!tenantInfo.idPic1) return;
        //   getPicBase64Img('idPic1',tenantInfo.idPic1?.id)
        //   getPicBase64Img('idPic2',tenantInfo.idPic2?.id)
        // }else{
        //   if(!tenantInfo.organizationPic) return;
        //   getPicBase64Img('organizationPic',tenantInfo.organizationPic?.id)
        // }
      };
      const avatar = computed(() => {
        const { avatar } = userStore.getUserInfo;
        return avatar;
      });

      watch(
        () => signConfig.value.validatetype,
        (val) => {
          if (!val) {
            isPasswordValidate.value = false;
            isDoubleValidate.value = false;
          }
          if (val == 'password') {
            isPasswordValidate.value = true;
            isDoubleValidate.value = false;
          }
          if (val == 'double') {
            isPasswordValidate.value = false;
            isDoubleValidate.value = true;
          }
        },
      );

      async function getAvatar(id) {
        let result1 = await getImgBase64({ imgId: id });
        if (result1) {
          userInfo.value.avatarImg = result1.image;
        }
      }

      async function getPicBase64Img(key, id) {
        let result = await getImgBase64({ imgId: id });
        if (result) {
          if (key == 'idPic1') {
            idPic1.value = result.image;
          }
          if (key == 'idPic2') {
            idPic2.value = result.image;
          }
          if (key == 'organizationPic') {
            organizationPic.value = result.image;
          }
          // console.log(key, unref(idPic1), result, '图片');
        }
      }
      function handleChange(list: string[]) {
        msg.info(`已上传文件${JSON.stringify(list)}`);
      }
      function handleCancel() {
        previewVisible.value = false;
      }
      async function updateAvatar(src: string, data) {
        // console.log(src, data, '上传成功');
        // console.log('fetch3----')
        fetch();
        // const userinfo = userStore.getUserInfo;
        // userinfo.avatar = src;
        // userStore.setUserInfo(userinfo);
      }
      function setPasswordBarColor(val) {
        // console.log(val);
        switch (val) {
          case 'WEAK':
            tagBar.weakBarColor = '#f50';
            tagBar.averageBarColor = '#cdc0c0';
            tagBar.strongBarColor = '#cdc0c0';
            tagBar.strongText = '弱';
            break;
          case 'AVERAGE':
            tagBar.weakBarColor = '#c59631';
            tagBar.averageBarColor = '#c59631';
            tagBar.strongBarColor = '#cdc0c0';
            tagBar.strongText = '中';
            break;
          case 'STRONG':
            tagBar.weakBarColor = '#87d068';
            tagBar.averageBarColor = '#87d068';
            tagBar.strongBarColor = '#87d068';
            tagBar.strongText = '强';
            break;
          default:
            tagBar.weakBarColor = '#cdc0c0';
            tagBar.averageBarColor = '#cdc0c0';
            tagBar.strongBarColor = '#cdc0c0';
        }
      }
      function handleSuccess() {
        fetch();
        fetchSignConfig();
      }
      async function handleSignSuccess(needChangeConfirmType) {
        if (needChangeConfirmType) {
          let result = await setSignValidateType({
            confirmType: confirmType.value,
            value: confirmType.value,
          });
          if (result) {
          }
        }
        fetchSignConfig();
      }

      onMounted(() => {
        // console.log(route.currentRoute, route.currentRoute.value?.params, '----路由--');
        if (
          route.currentRoute.value?.params &&
          route.currentRoute.value?.params?.type?.includes('change')
        ) {
          openMethodModal(true, {
            isUpdate: true,
            record: {
              title: '请选择修改方式',
              showPasswordType: true,
            },
          });
        }
        // console.log('fetch1----');
        fetch();
        fetchSignConfig();
      });

      async function fetchSignConfig() {
        let signPassword = await getSignConfig({ type: 'user_sign_password' });
        let signValidateType = await getSignConfig({ type: 'user_sign_confirm_type' });
        if (signPassword) {
          signConfig.value.password = signPassword.value;
        }
        if (signValidateType) {
          signConfig.value.validatetype = signValidateType.value;
        }
      }

      function handleChangePassword() {
        methodsModalType.value = 'password';
        openMethodModal(true, {
          isUpdate: true,
          record: {
            title: '请选择修改方式',
            showPasswordType: true,
          },
        });
      }
      function handleChangePhone(val) {
        methodsModalType.value = 'phone';
        defaultUnbindValue.value = val;
        //根据是否有值判断是解绑还是绑定
        if (val) {
          openMethodModal(true, {
            isUpdate: true,
            record: {
              title: '请选解绑方式',
              showPasswordType: true,
            },
          });
        } else {
          openPhoneModal(true, {
            isUpdate: false,
            record: {
              phone: '',
              title: '绑定手机号',
              type: 'phone',
            },
          });
        }
      }
      function handleChangeEmail(val) {
        methodsModalType.value = 'email';
        defaultUnbindValue.value = val;
        // console.log(val, '绑定得邮箱--');
        if (val) {
          openMethodModal(true, {
            isUpdate: true,
            record: {
              title: '请选解绑方式',
              showPasswordType: true,
            },
          });
        } else {
          openEmailModal(true, {
            isUpdate: false,
            record: {
              email: '',
              title: '绑定邮箱',
            },
          });
        }
      }
      function handleChangeUserName(val) {
        openUserModal(true, {
          isUpdate: true,
          record: {
            username: val ? val : '',
          },
        });
      }
      function handleMethodsSuccess(moduleName, methodName) {
        // console.log(methodName, userInfo, methodsModalType, '修改类型');
        closePhoneEmailModal();
        if (unref(methodsModalType) === 'password') {
          openModal(true, {
            isUpdate: true,
            record: {
              title: '修改密码',
              type: methodName,
              showBackBtn: true,
            },
          });
        }
        if (unref(methodsModalType) === 'phone') {
          openPhoneModal(true, {
            isUpdate: true,
            record: {
              phone: userInfo.value.phone,
              email: userInfo.value.email,
              title: '解绑手机号',
              type: methodName,
            },
          });
        }
        if (unref(methodsModalType) === 'email') {
          openEmailModal(true, {
            isUpdate: true,
            record: {
              phone: userInfo.value.phone,
              email: userInfo.value.email,
              title: '解绑邮箱',
              type: methodName,
            },
          });
        }
      }
      function handleBack() {
        closeModal();
        openMethodModal();
      }
      async function handleAuthSuccess() {
        try {
          await userStore.getTnantInfo();
          tenantInfo.value = userStore.getTenantInfo;
        } catch (err) {
          return;
        }
      }
      function handleChangeSignPassword() {
        openSignModal(true, {
          isUpdate: false,
          record: {
            needChangeConfirmType: false,
          },
        });
      }
      async function handleSetValidate(val, type) {
        if (val && !signConfig.value.password) {
          isPasswordValidate.value = false;
          isDoubleValidate.value = false;
          createConfirm({
            title: '温馨提示',
            content: '您暂未设置签署密码,点击确定前往设置密码',
            okText: '确定',
            cancelText: '取消',
            iconType: 'warning',
            wrapClassName: 'sign-config-modal',
            onOk() {
              confirmType.value = type;
              openSignModal(true, {
                isUpdate: false,
                record: {
                  needChangeConfirmType: true,
                },
              });
            },
            onCancel() {
              // isPasswordValidate.value  = false;
              // isDoubleValidate.value = false;
            },
          });
          return;
        }

        if (val) {
          signConfig.value.validatetype = type;
        }
        let result = await setSignValidateType({
          confirmType: type,
          value: val ? type : 'phone_email',
        });
        if (result) {
          msg.success('设置成功');
          fetchSignConfig();
        }

        // openSignFreeModal(true,{
        //   isUpdate:false
        // })
      }
      function handleVerified() {
        const token = userStore.getToken;
        let authInfo = window.appInfo.auth_app_info;
        let appInfo = {
          token: token,
          appCode: authInfo.appCode,
          appId: authInfo.appId,
          departId: userInfo.value.loginDepartId,
        };
        let paramsString = new URLSearchParams(appInfo).toString();
        window.open(authInfo.url + '/#/personal' + '?' + paramsString, '_self');
      }
      async function reloadAuth() {
        handleUpdateAuth(userInfo.value.loginTenantType as number);
      }
      async function toAuth() {
        if(userInfo.value.loginTenantType === 2){
          //个人
          createConfirm({
            iconType: 'info',
            width: '600px',
            title: '开启安全可靠的电子签章',
            content: h('div', [
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
        if(userInfo.value.loginTenantType === 1){
          const tenantInfo = userStore.getTenantInfo;
          createConfirm({
            iconType: 'info',
            width: '600px',
            title: '开启安全可靠的电子签章',
            // content: `您将作为认证申请人，对 「${tenantInfo.name}」 进行实名认证`,
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
                  h('div', `2. 您将作为认证申请人，在「云盾系统」完成对「${tenantInfo.name}」的企业实名认证`)
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
            onOk() {
              handleAuth(userInfo.value.loginTenantType as number);
            },
          });
        }

      }
      return {
        previewVisible,
        userInfo,
        handleChangeSignPassword,
        handleSetValidate,
        handleVerified,
        registerModal,
        handleSignSuccess,
        handleSuccess,
        handleChangePassword,
        handleChangePhone,
        registerPhoneModal,
        registerEmail,
        handleChangeEmail,
        registerUserName,
        handleChangeUserName,
        registerPhoneEmailModal,
        registerSignModal,
        tagBar,
        registerSignFeeModal,
        handleMethodsSuccess,
        handleBack,
        signConfig,
        handleChange,
        uploadAvatarApi: uploadAvatarApi as any,
        updateAvatar,
        avatar,
        handleCancel,
        paperImgs,
        loadCertificationStatus,
        loadCertificationText,
        loadCertificationIcon,
        // registerPersonalModal,
        // registerEnterpriseModal,
        tenantInfo,
        idPic1,
        idPic2,
        organizationPic,
        handleAuthSuccess,
        isPasswordValidate,
        isDoubleValidate,
        reloadAuth,
        toAuth,
      };
    },
  });
</script>
<style lang="less" scoped>
  .content-info {
    height: calc(100vh - 350px);
    overflow: auto;
  }

  .main-center {
    width: 1286px;
    margin: 0 auto;
  }
  .info-list.left {
    border-right: 1px dashed #e4e4e4;
  }
  :deep(.ant-card) {
    box-shadow: 5px 5px 30px #0000000e;
    margin-bottom: 25px;
  }
  :deep(.ant-card-head) {
    min-height: 40px;
    background: #f8f8f8;
    // padding: 7px 24px;
  }
  :deep(.ant-card-head-title) {
    // font-size:14px!important;
    color: #333;
    padding: 9px 0;
  }
  :deep(.ant-card-body) {
    padding: 20px;
  }
  .info-item {
    display: flex;
    margin: 20px 0;
    align-items: center;
    .title {
      width: 120px;
      font-size: 12px;
      font-weight: 600;
    }
  }
  .info-action {
    display: flex;
    justify-content: center;
    align-items: center;
    .action-btn {
      cursor: pointer;
      margin-right: 10px;
      &:hover {
        color: #127fd2;
      }
    }
  }
  .info-item-auth .title {
    width: 90px;
    font-size: 12px;
    font-weight: 600;
  }
  .info-list {
    padding: 20px 25px;
    display: flex;
    background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)),
      linear-gradient(345deg, #eff6ff -4%, #ffffff 100%);
    // background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(135deg, #eff6ff -4%, #ffffff 100%); // 新增背景色，提升视觉层次
    border-radius: 8px; // 增加圆角，与卡片风格统一
    .avatar-info {
      margin-right: 20px;
      display: inline-block;
    }
    .info-item {
      .info {
        min-width: 250px;
        font-size: 14px;
        color: #000;
      }
      .action {
        cursor: pointer;
        margin-bottom: 0;
        &:hover {
          color: #127fd2;
        }
      }
    }
  }
  .card-tip,
  .info-tip {
    font-size: 12px;
    color: #a0a1a3;
    margin: 5px 0;
  }
  .info-line-list {
    margin-left: 10px;
    .line-item {
      border-bottom: 1px dashed #a0a1a3;
      padding: 12px 0;
      transition: background-color 0.3s ease;
    }
    .action-btn {
      height: 100%;
      width: 100%;
    }
  }
  .password-state-bar {
    display: flex;
    margin-left: 50px;
    :deep(.ant-tag) {
      margin-right: 1px;
      border: none;
      border-radius: 0;
      padding: 2px 25px;
      height: 22px;
    }
    .password-state-text {
      margin-left: 5px;
    }
  }
  .paperwork-area {
    display: flex;
    align-items: center;
    img {
      display: inline-block;
      margin: 0 10px;
      width: 50%;
      height: 400px;
    }
  }
  :deep(.ant-card-head-title) {
    font-size: 16px;
    color: #000;
  }
  .card-title {
    font-size: 16px !important;
    color: 000;
  }

  .ant-btn-primary {
    // background: linear-gradient(90deg, #1890ff, #40a9ff);
    border-color: transparent;
    min-width: 120px;
    // height: 30px;
    // border-radius: 4px;
  }
</style>

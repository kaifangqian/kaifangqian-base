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
  <!-- <LoginFormTitle v-show="getShow" class="enter-x" /> -->
  <Form
    class="p-3 enter-x"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    @keypress.enter="handleLogin"
  >
    <FormItem name="username" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.username"
        placeholder="用户名"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="password" class="enter-x">
          <Input
            size="large"
            :type="formData.inputPassword?'password':'text'"
            visibilityToggle
            v-model:value="formData.password"
            placeholder="密码"
          >
            <template #suffix>
              <div class="icon-eye" @click="handleChangePassword('inputPassword')">
                <Icon icon="ant-design:eye-invisible-outlined" v-if="formData.inputPassword"></Icon>
                <Icon icon="ant-design:eye-outlined" v-else></Icon>
              </div>
            </template>
          </Input>
    </FormItem>
    <Row class="enter-x">
      <Col :span="16">
        <FormItem name="captcha">
         <Input
            size="large"
            type="text"    
            visibilityToggle
            v-model:value="formData.captcha"
            placeholder="验证码"
          />
        </FormItem>
      </Col>
      <Col :span="8" style="text-align: right">
         <img  v-if="randCodeImage" style="float:right;margin:2px 0 0 5px;min-width:105px" :src="randCodeImage" @click="handleChangeCheckCode"/>
         <img v-else style="float:right;margin:2px 0 0 5px;min-width:105px" src="../../../assets/images/checkcode.png" @click="handleChangeCheckCode"/>
      </Col>
    </Row>
    <FormItem class="enter-x">
      <a-button type="primary" size="default" block @click="handleLogin" :loading="loading">
        登录
      </a-button>
    </FormItem>
      <Row>
        <!-- <Col :span="12">
          <FormItem :style="{ 'text-align': 'left' }">
            <a-button type="link" size="small"  @click="setLoginState(LoginStateEnum.RESET_ACCOUNT)">
              忘记账号
            </a-button>
          </FormItem>
        </Col> -->
        <Col :span="24">
          <FormItem :style="{ 'text-align': 'right' }">
            <a-button type="link" size="small" @click="showPhoneEmail('FIND_ACCOUNT')">
              忘记账号？
            </a-button>
            <a-button type="link" size="small" @click="showPhoneEmail('RESET_PASSWORD')">
              忘记密码？
            </a-button>
          </FormItem>
        </Col>
      </Row>
  </Form>
  <LoginSelectAccount @register="registerModal" @success="handleSuccess"/>
  <MobileOrEmail  @register="registerPhoneEmailModal" @success="handlePhoneEmailSuccess"/>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed ,onMounted} from 'vue';
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useModal } from '/@/components/Modal';
  import Icon from '/@/components/Icon';

  import { useUserStore } from '/@/store/modules/user';
  // import LoginFormTitle from './LoginFormTitle.vue';
  import LoginSelectAccount from './LoginSelectAccount.vue';
  import MobileOrEmail from './MobileOrEmail.vue';
  import { getImgCode } from '/@/api/sys/user'
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Md5 } from "ts-md5";

  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();
  const currdatetime = ref();

  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();
  const [registerModal, { openModal,closeModal }] = useModal();
  const [registerPhoneEmailModal, { openModal:openPhoneEmailModal,closeModal:closePhoneEmailModal }] = useModal();
  const FormItem = Form.Item;
  // const InputPassword = Input.Password;
  const formRef = ref();
  const loading = ref(false);
  // const moduleName = ref('');
  const randCodeImage = ref();
  const formData = reactive({
    username: '',
    password: '',
    captcha:'',
    inputPassword:true
  });

  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  onMounted(()=>{
    userStore.clearChangePasswordTimer();
    //获取验证码
    handleChangeCheckCode()

  })
  async function handleChangeCheckCode(){
    currdatetime.value = new Date().getTime();
    const img = await getImgCode({key:unref(currdatetime)});
    // const img =  userStore.getLoginCode();
    randCodeImage.value = img;
    formData.captcha = '';
  }
  function handleChangePassword(passwordKey){
    formData[passwordKey] = !formData[passwordKey];
  }
  
  function handleSuccess(){
    closeModal();
    formData.captcha = '';
  }
  function showPhoneEmail(moduleName){
    openPhoneEmailModal(true,{
      isUpdate:false,
      moduleName
    })
  }

  function handlePhoneEmailSuccess(moduleName,methodName){
    console.log(methodName)
    closePhoneEmailModal();
    if(moduleName === 'RESET_PASSWORD'){
      setLoginState(LoginStateEnum.RESET_PASSWORD);
    }else if(moduleName === 'FIND_ACCOUNT'){
      setLoginState(LoginStateEnum.FIND_ACCOUNT);
    }
  }

  
  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      const userInfo = await userStore.login({
        password: Md5.hashStr(data.password),
        username: data.username,
        captcha: data.captcha,
        checkKey: unref(currdatetime),
        mode: 'none', 
      });
      if(userInfo?.multi_depart ===2){
          openModal(true,{
            isUpdate:false,
            record:userInfo
          })
        return  
      }
      if (userInfo) {
        notification.success({
          message: '登录成功',
          description: `${'登录成功'}: ${userInfo.username}`,
          duration: 3,
        });
      }else{
        handleChangeCheckCode();
        console.log(userInfo,'--返回的信息--')
      }
    } catch (error) {
      // createMessage.error((error as unknown as Error).message || '网络异常');
      createErrorModal({
        title: '错误提示',
        content: (error as unknown as Error).message || '网络异常',
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      });
      handleChangeCheckCode();
      formData.captcha = ''
    } finally {
      loading.value = false;
    }
  }
</script>
<style lang="less" scoped>
  .form-footer{
    // border-top: 1px dashed #d9d9d9;
    // border-bottom: 1px dashed #d9d9d9d9;
  }
  // .ant-tooltip{
    :deep(.ant-tooltip-inner){
      background-color:'#fff';
    // }
  }
</style>

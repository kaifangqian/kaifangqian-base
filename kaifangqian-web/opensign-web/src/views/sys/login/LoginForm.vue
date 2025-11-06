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
  <!-- <LoginFormTitle v-show="getShow" class="" /> -->
  <Form
    class="p-3 "
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    @keypress.enter="handleLogin"
  >
    <FormItem name="username" class="">
      <Input
        size="large"
        v-model:value="formData.username"
        placeholder="用户名"
        class="fix-auto-fill"
      >
      <template #prefix>
        <Icon icon="ant-design:user-outlined"  color="#bfbfbf"></Icon>
      </template>
    </Input>
    </FormItem>
    <FormItem name="password" class="">
          <Input
            size="large"
            :type="formData.inputPassword?'password':'text'"
            visibilityToggle
            v-model:value="formData.password"
            placeholder="密码"
          >
            <template #prefix>
                <Icon icon="ant-design:lock-outlined" color="#bfbfbf"></Icon>
              </template>
            <template #suffix>
              <div class="icon-eye" @click="handleChangePassword('inputPassword')">
                <Icon icon="ant-design:eye-invisible-outlined" v-if="formData.inputPassword"></Icon>
                <Icon icon="ant-design:eye-outlined" v-else></Icon>
              </div>
            </template>
          </Input>
    </FormItem>
    <Row class="">
      <Col :span="15">
        <FormItem name="captcha">
         <Input
            size="large"
            type="text"    
            visibilityToggle
            v-model:value="formData.captcha"
            placeholder="验证码"
          >
            <template #prefix>
              <Icon icon="ant-design:check-circle-outlined"  color="#bfbfbf"></Icon>
            </template>
          </Input>
        </FormItem>
      </Col>
      <Col :span="8" style="text-align: right">
         <img  v-if="randCodeImage" style="float:right;margin:2px 0 0 5px;min-width:105px" :src="randCodeImage" @click="handleChangeCheckCode"/>
         <img v-else style="float:right;margin:2px 0 0 5px;min-width:105px" src="../../../assets/images/checkcode.png" @click="handleChangeCheckCode"/>
      </Col>
    </Row>
    <Row class="policy-area">
      <Col :span="24">
        <FormItem :style="{fontSize: '14px' }">
          <p style="margin: 0;">
            <a-checkbox v-model:checked="registerCheck">我已阅读并同意</a-checkbox>
            <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
            <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
            <!-- <a href="javascript:;" @click="handleTerm('certificate')">《数字证书使用协议》</a> -->
          </p>
        </FormItem>
      </Col>
    </Row>
    <FormItem class="">
      <a-button type="primary" size="large" block @click="handleLogin" :loading="loading"  style="background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%); height:48px;border-color: #00000000;">
        登录
      </a-button>
    </FormItem>
  </Form>
  <LoginSelectAccount @register="registerModal" @success="handleSuccess"/>
  <MobileOrEmail  @register="registerPhoneEmailModal" @success="handlePhoneEmailSuccess"/>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed ,onMounted, h} from 'vue';
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useModal } from '/@/components/Modal';
  import Icon from '/@/components/Icon';
  import { useGo } from '/@/hooks/web/usePage';

  import { useUserStore } from '/@/store/modules/user';
  // import LoginFormTitle from './LoginFormTitle.vue';
  import LoginSelectAccount from './LoginSelectAccount.vue';
  import MobileOrEmail from './MobileOrEmail.vue';
  import { getImgCode } from '/@/api/sys/user'
  import { LoginStateEnum, useLoginState, useFormRules, useFormValid } from './useLogin';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { Md5 } from "ts-md5";
  import { getAppEnvConfig } from '/@/utils/env';
  const emit = defineEmits(["change"]);
  const { notification, createErrorModal,createMessage,createConfirm } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();
  const currdatetime = ref();
  const registerCheck = ref(false);
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
  const go = useGo();

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  onMounted(()=>{
    userStore.clearChangePasswordTimer();
    //获取验证码
    handleChangeCheckCode()

  })
  function handleTerm(type){
    window.open('/#/terms/service/'+ type)
  }
  async function handleChangeCheckCode(){
    currdatetime.value = new Date().getTime();
    const result = await getImgCode({key:unref(currdatetime)});
    // const img =  userStore.getLoginCode();
    randCodeImage.value = result.captchaImg;
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
    // openPhoneEmailModal(true,{
    //   isUpdate:false,
    //   moduleName
    // })
    if(moduleName === 'RESET_PASSWORD'){
      setLoginState(LoginStateEnum.RESET_PASSWORD);
    }else if(moduleName === 'FIND_ACCOUNT'){
      setLoginState(LoginStateEnum.FIND_ACCOUNT);
    }
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
  function handleRegister(){
    emit('change',"LOGIN_MOBILE")
  }
  
  async function handleLogin() {
    
     const data = await validForm();
     if (!data) return;
    let {VITE_GLOB_APP_CODE} = getAppEnvConfig()
    VITE_GLOB_APP_CODE = VITE_GLOB_APP_CODE || 'opensign_0001';
    
    const isAgreed = await checkAgreement();
      if (!isAgreed) {
        return;
      }
    try {
      loading.value = true;
      const userInfo = await userStore.login({
        password: Md5.hashStr(data.password),
        username: data.username,
        captcha: data.captcha,
        checkKey: unref(currdatetime),
        mode: 'none', 
        appCode:VITE_GLOB_APP_CODE
      });
      console.log(userInfo,'-----userInfo-----');
      if(userInfo&&userInfo?.user_tenant_depart){
        if(userInfo?.user_tenant_depart.length>1){
          // 从depts中选择selectFlag为true的部门自动进行登录
          if (userInfo?.user_tenant_depart.some((item: any) => item.selectFlag)) {
            console.log('登录上次登录身份');
            const selectDept = userInfo?.user_tenant_depart.find((item: any) => item.selectFlag);
            // console.log('selectDept-----', selectDept);
            if (selectDept?.departs && selectDept.departs.length > 0 && selectDept.departs[0].departId) {
              await userStore.selectTenant({
                departId: selectDept.departs[0].departId,
                departName: selectDept.departs[0].departName
              });
            } else {
              createMessage.error('部门信息不完整，无法自动登录');
            }
            // handleSubmit(selectDept);
          }else {
            console.log('选择登录身份');
            //如果多个部门则需要选择登录
            openModal(true,{
              isUpdate:false,
              record:userInfo
            })
          }
          
        }else{
          // if(userInfo?.user_tenant_depart[0].departs.length>1){
          //   openModal(true,{
          //     isUpdate:false,
          //     record:userInfo
          //   })
          // }else{
            const formData = {
              departId:userInfo?.user_tenant_depart[0].departs[0].departId
            }
            // 修复类型错误：确保 departId 存在且不为空
            if (formData.departId) {
              const result = await userStore.selectTenant(formData);
              if(result){
                  // notification.success({
                  //   message: '登录成功',
                  //   description: `${'登录成功'}: ${result.username}`,
                  //   duration: 3,
                  // });
              }
            } else {
              createMessage.error('部门信息不完整，无法登录');
            }
          // }
        }
          
        return  
      }
      if (userInfo) {
        createMessage.success('欢迎进入');
      }else{
        handleChangeCheckCode();
        console.log(userInfo,'--返回的信息--')
      }
    } catch (error) {
      // createMessage.error((error as unknown as Error).message || '网络异常');
      // createErrorModal({
      //   title: '错误提示',
      //   content: (error as unknown as Error).message || '网络异常',
      //   getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      // });
      handleChangeCheckCode();
      formData.captcha = ''
    } finally {
      loading.value = false;
    }
  }

  async function checkAgreement(): Promise<boolean> {
        if (registerCheck.value) {
          return true;
        }
        return new Promise((resolve) => {
          createConfirm({
            iconType: 'warning',
            title: '阅读并同意协议',
            content: h(
              'span',
              {},
              [
              '我已阅读并同意',
              h(
                'a',
                {
                href: 'javascript:;',
                onClick: () => handleTerm('serve'),
                style: 'color: #1890ff; margin: 0 4px;',
                },
                '《服务协议》'
              ),
              h(
                'a',
                {
                href: 'javascript:;',
                onClick: () => handleTerm('privacy'),
                style: 'color: #1890ff; margin: 0 4px;',
                },
                '《隐私政策》'
              ),
              ]
            ),
            cancelText: '取消',
            okText: '同意并继续',
            onOk: () => {
              registerCheck.value = true;
              resolve(true);
            },
            onCancel: () => resolve(false),
          });
        });
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
  :deep(.ant-tabs-content){
    width:80%;
    margin:0 auto;
  }
  .resrun-login input:not([type='checkbox']){
    min-width: 100px;
  }
  .ant-form-item{
    margin-bottom:22px;
  }
  .ant-form-item-with-help{
    margin-bottom: 0;
  }
</style>
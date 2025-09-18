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
         <img v-else style="float:right;margin:2px 0 0 5px;min-width:105px" src="~@/assets/images/checkcode.png" @click="handleChangeCheckCode"/>
      </Col>
    </Row>
    <Row class="policy-area">
        <Col :span="24">
          <FormItem :style="{ fontSize: '14px' }">
            <p style="margin: 0">
              <a-checkbox v-model:checked="registerCheck">我已阅读并同意</a-checkbox>
              <a href="javascript:;" @click="handleTerm('serve')">《服务协议》</a>
              <a href="javascript:;" @click="handleTerm('privacy')">《隐私政策》</a>
              <!-- <a href="javascript:;" @click="handleTerm('certificate')">《数字证书使用协议》</a> -->
            </p>
          </FormItem>
        </Col>
      </Row>
    <FormItem class="">
      <a-button type="primary" size="large" block @click="handleLogin" :loading="loading" style="background: linear-gradient(135deg, #1890FF 0%, #40A9FF 100%); height:48px;border-color: #00000000;">
        登录
      </a-button>
    </FormItem>
    <!-- <Row>
      <Col :span="24">
        <FormItem :style="{ 'text-align': 'right' }">
          <a-button type="link" size="small" @click="handleRegister">
            快速注册
          </a-button>
        </FormItem>
      </Col>
    </Row> -->
  </Form>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed ,onMounted, defineEmits, h } from 'vue';
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {useRouter} from "vue-router";
  import Icon from '/@/components/Icon';
  import { useGo } from '/@/hooks/web/usePage';

  import { useUserStore } from '/@/store/modules/user';
  import { getImgCode } from '/@/api/sys/user'
  import {  useFormRules, useFormValid } from '/@/views/sys/login/useLogin';

  import { Md5 } from "ts-md5";

  import { getAppEnvConfig } from '/@/utils/env';


  const {createMessage } = useMessage();
  
  const emit = defineEmits(['success'])

  const userStore = useUserStore();
  const currdatetime = ref();
  const { getFormRules } = useFormRules();
  const FormItem = Form.Item;
  let router = useRouter();
  const registerCheck = ref(false);
  const {createConfirm } = useMessage();

  const formRef = ref();
  const loading = ref(false);
  const randCodeImage = ref();
  const formData = reactive({
    username: '',
    password: '',
    captcha:'',
    inputPassword:true
  });

  const { validForm } = useFormValid(formRef);
  const go = useGo();

  onMounted(()=>{
    userStore.clearChangePasswordTimer();
    //获取验证码
    handleChangeCheckCode()

  })
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

  async function handleLogin() {
    const data = await validForm();
    let {VITE_GLOB_APP_CODE} = getAppEnvConfig()
    VITE_GLOB_APP_CODE = VITE_GLOB_APP_CODE || 'opensign_0001';
    if (!data) return;
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
      
      if (userInfo) {
        emit('success')
      }else{
        handleChangeCheckCode();
        console.log(userInfo,'--返回的信息--')
      }
    } catch (error) {
     
      handleChangeCheckCode();
      formData.captcha = ''
    } finally {
      loading.value = false;
    }
  }
  function handleRegister(){
    const routeUrl = router.resolve({
    	path: "/register",
      query:{
        type:"authLogin"
      }
    })
    window.open(routeUrl.href, '_blank');
  }

  function handleTerm(type) {
        window.open('/#/terms/service/' + type);
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

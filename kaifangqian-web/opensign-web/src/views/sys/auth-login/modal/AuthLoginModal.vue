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
  <div class="main-auth-lognin-modal">
    <BasicModal
      v-bind="$attrs"
      @register="registerModal"
      title="授权登录"
      @ok="handleSubmit"
      :destroyOnClose="true"
    >
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
          >
            <template #prefix>
              <Icon icon="ant-design:user-outlined" color="#bfbfbf"></Icon>
            </template>
          </Input>
        </FormItem>
        <FormItem name="password" class="enter-x">
          <Input
            size="large"
            :type="formData.inputPassword ? 'password' : 'text'"
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
        <Row class="enter-x">
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
                  <Icon icon="ant-design:check-circle-outlined" color="#bfbfbf"></Icon>
                </template>
              </Input>
            </FormItem>
          </Col>
          <Col :span="8" style="text-align: right">
            <img
              v-if="randCodeImage"
              style="float: right; margin: 2px 0 0 5px; min-width: 105px"
              :src="randCodeImage"
              @click="handleChangeCheckCode"
            />
            <img
              v-else
              style="float: right; margin: 2px 0 0 5px; min-width: 105px"
              src="~@/assets/images/checkcode.png"
              @click="handleChangeCheckCode"
            />
          </Col>
        </Row>
        <FormItem class="enter-x">
          <a-button type="primary" size="large" block @click="handleLogin"> 登录 </a-button>
        </FormItem>
      </Form>
    </BasicModal>
    <LoginSelectAccount @register="registerTenantModal" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, reactive, unref } from 'vue';
  import { BasicModal, useModalInner, useModal } from '/@/components/Modal';
  import { Form, Input, Row, Col } from 'ant-design-vue';
  import { getImgCode } from '/@/api/sys/user';
  import { Icon } from '/@/components/Icon';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Md5 } from 'ts-md5';
  import { useUserStore } from '/@/store/modules/user';

  import { useFormRules, useFormValid } from '/@/views/sys/login/useLogin';

  import LoginSelectAccount from '/@/views/sys/login/LoginSelectAccount.vue';

  export default defineComponent({
    name: 'AuthLoginModal',
    components: {
      BasicModal,
      Form,
      Input,
      Row,
      Col,
      Icon,
      FormItem: Form.Item,
      LoginSelectAccount,
    },
    setup() {
      const isUpdate = ref(false);
      const randCodeImage = ref();
      const currdatetime = ref();
      const formRef = ref();
      const { validForm } = useFormValid(formRef);
      const userStore = useUserStore();

      const { notification, createMessage } = useMessage();

      const formData = reactive({
        username: '',
        password: '',
        captcha: '',
        inputPassword: true,
      });

      const { getFormRules } = useFormRules();

      const [registerTenantModal, { openModal, closeModal: closeTenantModal }] = useModal();

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false, width: 420, maskClosable: false, footer: null });
        isUpdate.value = !!data?.isUpdate;
        handleChangeCheckCode();
      });
      async function handleChangeCheckCode() {
        currdatetime.value = new Date().getTime();
        const result = await getImgCode({ key: unref(currdatetime) });
        // const img =  userStore.getLoginCode();
        randCodeImage.value = result.captchaImg;
        formData.captcha = '';
      }
      function handleChangePassword(passwordKey) {
        formData[passwordKey] = !formData[passwordKey];
      }
      async function handleLogin() {
        const data = await validForm();
        if (!data) return;
        try {
          const userInfo = await userStore.login({
            password: Md5.hashStr(data.password),
            username: data.username,
            captcha: data.captcha,
            checkKey: unref(currdatetime),
            mode: 'none',
          });
          if (userInfo && userInfo?.user_tenant_depart) {
            if (userInfo?.user_tenant_depart.length > 1) {
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
                  record:userInfo,
                  type: 'authSelect',
                })
              }
            } else {
              // if (userInfo?.user_tenant_depart[0].departs.length > 1) {
              //   openModal(true, {
              //     isUpdate: false,
              //     record: userInfo,
              //     type: 'authSelect',
              //   });
              // } else {
                const formData = {
                  departId: userInfo?.user_tenant_depart[0].departs[0].departId,
                };
                await userStore.authSelectTenant(formData);
              // }
            }

            return;
          }
          if (userInfo) {
            createMessage.success('欢迎进入');
          } else {
            handleChangeCheckCode();
            console.log(userInfo, '--返回的信息--');
          }
        } catch (error) {
          // createMessage.error((error as unknown as Error).message || '网络异常');
          // createErrorModal({
          //   title: '错误提示',
          //   content: (error as unknown as Error).message || '网络异常',
          //   getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
          // });
          handleChangeCheckCode();
          formData.captcha = '';
        } finally {
        }
      }
      function handleSubmit() {}
      function handleSuccess() {
        console.log('授权成功-----------');
      }
      return {
        handleSuccess,
        registerModal,
        registerTenantModal,
        formData,
        getFormRules,
        randCodeImage,
        handleChangePassword,
        handleChangeCheckCode,
        handleLogin,
        handleSubmit,
        formRef,
      };
    },
  });
</script>

<style lang="less" scoped></style>

/*
 * @description 开放签
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

// import { ValidationRule } from 'ant-design-vue/es/form/Form';
import type { RuleObject } from 'ant-design-vue/lib/form/interface';
import type { Rule } from 'ant-design-vue/es/form';
import { ref, computed, unref, Ref } from 'vue';
import { isMobile, isEmail } from '/@/utils/validate';

export enum LoginStateEnum {
  LOGIN,
  REGISTER,
  LOGIN_MOBILE,
  LOGIN_VERIFY,
  RESET_PASSWORD,
  RESET_ACCOUNT,
  MOBILE,
  QR_CODE,
  FIND_ACCOUNT,
  EXPERIENCE,

}
export enum ResetPassEnum {
  MOBILE,
  EMAIL,
  PASSWORD,
}

const currentState = ref(LoginStateEnum.LOGIN);
const resetPassType = ref(ResetPassEnum.MOBILE);

export function useLoginState() {
  function setLoginState(state: LoginStateEnum) {
    currentState.value = state;
  }

  const getLoginState = computed(() => currentState.value);

  function handleBackLogin() {
    setLoginState(LoginStateEnum.LOGIN);
  }

  function setResetPass(state: ResetPassEnum) {
    resetPassType.value = state;
  }

  const getResetPasswordType = computed(()=> resetPassType.value);


  return { setLoginState, getLoginState, handleBackLogin,getResetPasswordType,setResetPass };
}


export function useFormValid<T extends Object = any>(formRef: Ref<any>) {
  async function validForm() {
    const form = unref(formRef);
    if (!form) return;
    const data = await form.validate();
    return data as T;
  }

  return { validForm };
}

export function useFormRules(formData?: Recordable) {

  const validateMobile =  (_rule: Rule, value: string) =>{
    if(!value){
      return Promise.reject('请输入手机号');
    }
    if(!isMobile(value)){
      return Promise.reject('手机号格式不正确');
    }else{
      return Promise.resolve(); 
    }
  }
  const validateEmail =  (_rule: Rule, value: string) =>{
    if(!value){
      return Promise.reject('请输入邮箱');
    }
    if(!isEmail(value)){
      return Promise.reject('邮箱格式不正确');
    }else{
      return Promise.resolve(); 
    }
  }
  const validatePassword =  (_rule: Rule, value: string) =>{
    if(!value){
      return Promise.reject('请输入密码');
    }
    return Promise.resolve(); 
  }

  const getAccountFormRule = computed(() => createRule('请输入账号'));
  const getPasswordFormRule = computed(() => createRule(undefined,validatePassword));
  const getSmsFormRule = computed(() => createRule('请输入验证码'));
  const getMobileFormRule = computed(() => createRule(undefined, validateMobile));
  const getEmailFormRule = computed(() => createRule(undefined, validateEmail));

  const validatePolicy = async (_: RuleObject, value: boolean) => {
    return !value ? Promise.reject('勾选后才能注册') : Promise.resolve();
  };

  const validateConfirmPassword = (password: string) => {
    return async (_: RuleObject, value: string) => {
      if (!value) {
        return Promise.reject('请输入密码');
      }
      if (value !== password) {
        return Promise.reject('两次密码输入不一致');
      }
      return Promise.resolve();
    };
  };

  const getFormRules = computed((): { [k: string]: {} } => {
    const accountFormRule = unref(getAccountFormRule);   
    const passwordFormRule = unref(getPasswordFormRule);
    const smsFormRule = unref(getSmsFormRule);
    const mobileFormRule = unref(getMobileFormRule);
    const emailFormRule = unref(getEmailFormRule);

    const mobileRule = {
      phone: mobileFormRule,
      captcha: smsFormRule,
      newpassword:passwordFormRule,
      confirmpassword:passwordFormRule

    };
    switch (unref(currentState)) {
      // register form rules
      case LoginStateEnum.REGISTER:
        return {
          username: accountFormRule,
          password: passwordFormRule,
          confirmPassword: [
            { validator: validateConfirmPassword(formData?.password), trigger: 'change' },
          ],
          policy: [{ validator: validatePolicy, trigger: 'change' }],
          ...mobileRule,
        };

      // reset password form rules
      case LoginStateEnum.RESET_PASSWORD:
        return {
          ...mobileRule,
          email:emailFormRule,
        };
      // find account 
      case LoginStateEnum.FIND_ACCOUNT:
        return {
          email:emailFormRule,
          phone:mobileFormRule,
          captcha: smsFormRule,
        };
      // login by mobile
      case LoginStateEnum.LOGIN_MOBILE:
        return {
          phone:mobileFormRule,
          captcha: smsFormRule,
          
        };

      // mobile form rules
      case LoginStateEnum.MOBILE:
        return {
          ...mobileRule,
          
        };

      // login form rules
      default:
        return {
          username: accountFormRule,
          password: passwordFormRule,
          captcha: smsFormRule,
        };
    }
  });
  console.log(unref(currentState), getFormRules,'-----规则23----')
  return { getFormRules };
}

function createRule(message?: string, validator?:Function) {
  return [
    {
      required: true,
      message,
      trigger: 'blur',
      validator:validator
    },
  ];
}

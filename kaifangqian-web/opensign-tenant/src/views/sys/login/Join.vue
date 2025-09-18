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
 <div :class="prefixCls" class="relative w-full h-full ">
    <div :class="prefixCls+ '-header'">
      <div class="header-left">
        <img src="../../../assets/images/logo.png" alt="" style="width:180px"> 
        <!-- <span class="header-title">首版科技</span> -->
      </div>
      <div class="header-right">
        <!-- <a href="https://www.yuque.com/huxin-ch41t/resrun/akk7i0" target="_blank">帮助文档</a>
        <a href="https://resrun.cn/" target="_blank">官网</a> -->
      </div>
    </div>
    <div class="container relative  py-2 mx-auto sm:px-10">
      <div class="flex w-full h-full py-5  xl:py-5 xl:my-0 xl:w-12/12">
        <div
          :class="`${prefixCls}-form`"
          class="relative w-full px-5 py-8 mx-auto my-auto rounded-md shadow-md xl:bg-transparent sm:px-8 xl:p-4 xl:shadow-none sm:w-3/4 lg:w-2/4 xl:w-auto enter-x"
        >
          <div class="invite-header">
            <img  class="app-icon" src="../../../assets/images/join.png" alt="">
            <p>{{ inviter + '邀请你加入'}}</p>
            <p>{{ organizeName }}</p>
          </div>
          <div class="resrun-invite-form">
            <Form  class="p-3 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
                  <FormItem name="phone">
                    <Input
                      size="large"
                      v-model:value="formData.name"
                      placeholder="请填写你的真实姓名"
                      class="fix-auto-fill"
                    />
                  </FormItem>
                  <FormItem name="phone">
                    <Input
                      size="large"
                      v-model:value="formData.phone"
                      placeholder="请填写你的手机号"
                      class="fix-auto-fill"
                    >
                    <template #prefix>
                      <span>+86 </span>
                    </template>
                  </Input>
                  </FormItem>
                  <FormItem name="captcha" >
                    <CountdownInput
                      size="default"
                      class="fix-auto-fill"
                      v-model:value="formData.captcha"
                      placeholder="获取验证码"
                      ref="smsCount"
                      :sendCodeApi="(sendCodeApi as any)"
                    />
                  </FormItem>
                <FormItem >
                  <a-button type="primary"  block @click="handleSubmit">
                    立即加入
                  </a-button>
                </FormItem>
              </Form>
          </div>
          <!-- <div class="jon-footer">
            点击立即加入代表你已阅读并同意
            <a href="javascript:;">服务协议</a>
            和<a href="javascript:;">《隐私政策》</a>
          </div> -->
        </div>
      </div>
    </div>
  </div>
   
</template>
<script lang='ts'>
import { defineComponent,ref,reactive,unref } from 'vue';
import { CountdownInput } from '/@/components/CountDown';
import { useMessage } from '/@/hooks/web/useMessage'; 
import { Form, Input, Row, Col } from 'ant-design-vue';
import { useFormRules } from '/@/views/sys/login/useLogin';
import { useDesign } from '/@/hooks/web/useDesign';

export default defineComponent({
name: 'InviteUser',
components:{
  Form,
  FormItem:Form.Item,
  Input,
  Row, Col,
  CountdownInput
},
setup(_,{}){
  const getTitle = ref('邀请成员加入');
  const slideCodeShow = ref(false);
  const { getFormRules } = useFormRules();
  const formData = reactive({
    name:'',
    phone:'',
    captcha:''
  })
  const inviter = ref('测试');
  const organizeName = ref('资源律动');
  const flag = ref(false);
  const { prefixCls } = useDesign('join');
  const { createMessage: msg } = useMessage();
  async function handleSubmit(){
   
   

  }
  function sendCodeApi(){
  return new Promise((resolve)=>{
      if(!formData.phone){
        msg.warning('请输入手机号');
        return false;
      }
      slideCodeShow.value = true;
      resolve(!unref(slideCodeShow));
      return !unref(slideCodeShow);
    })
  }
 

  return {
    prefixCls,
    sendCodeApi,
    handleSubmit,
    getTitle,
    formData,
    getFormRules,
    inviter,
    flag,
    organizeName
  }
},
})
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-join';
  @logo-prefix-cls: ~'@{namespace}-app-logo';
  @countdown-prefix-cls: ~'@{namespace}-countdown-input';
  @dark-bg: #293146;
  .@{prefix-cls} {
    min-height: 100%;
    overflow: hidden;
    background: #f5f5f5;
    @media (max-width: @screen-xl) {
      background-color: #293146;

      .@{prefix-cls}-form {
        background-color: #fff;
      }
    }
    &-header{
        width: 100%;
        height: 80px;
        padding: 0 30px;
        border-bottom: 1px solid #e4e4e4;
        display: flex;
        justify-content: center;
        align-items: center;
        .header-left{
          display: flex;
          align-items: center;
          height: 100%;
        }
        .header-right{
          a{
            font-size: 14px;
            font-weight: bold;
            margin:0 5px;
            color: #333;
          }
        }
        .header-title{
          color: #127fd2;
          font-size: 20px;
          font-weight: 600;
          margin-left: 5px;
        }
    }
    &-form{
      background:#fff;
    }
    .container{
      margin-top:80px;
    }
  }
  .@{logo-prefix-cls} {
      position: absolute;
      top: 12px;
      height: 30px;

      &__title {
        font-size: 16px;
        color: #fff;
      }

      img {
        width: 32px;
      }
    }
.resrun-invite-form{
  font-size: 14px;
  margin-top:50px;
}
.invite-header{
  text-align: center;
  margin-top: -45px;
  img{
    width:50px;
    height:50px;
    border-radius: 4px;
    margin-bottom: 10px;
    display: inline-block;
    background-color: #fff;
    border:1px solid #f4f4f4;
  }
}
.join-container{
  width:400px;
}
.jon-footer{
  margin-top:100px;
}
</style>

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

/*
 * @Author: ningw 
 * @Date: 2022-06-28 18:22:10 
 * @Last Modified by: ningw
 * @Last Modified time: 2022-09-06 10:31:16
 */
<template>
  <div>
    <BasicModal v-bind="$attrs" @register="registerModal" :title="title" @ok="handleSubmit" :destroyOnClose="true">
        <RadioGroup v-model:value="findType" class="radio-list" @change="handleSubmit">
            <Radio :style="radioStyle" value="mobile">
              <span class="title">手机号</span>
            </Radio>
            <Radio :style="radioStyle" value="email">
              <span class="title">邮箱</span>
            </Radio>
            <Radio :style="radioStyle" value="password" v-if="showPasswordType">
              <span class="title">原密码</span>
            </Radio>
        </RadioGroup>
    </BasicModal>
  </div>
</template>
<script lang='ts'>
import { defineComponent,ref, reactive,unref } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { RadioGroup, Radio } from 'ant-design-vue'; 
import { ResetPassEnum, useLoginState } from './useLogin';
// import { getAccountList } from '/@/api/sys/user'
import { Icon } from '/@/components/Icon';

export default defineComponent({
  name: 'LoginSelectAccount',
  components:{
    BasicModal,
    RadioGroup,
    Radio,
    Icon
  },
  setup(_,{emit}){
      const isUpdate = ref(true);
      const title = ref('');
      const showPasswordType = ref(false);
      const radioStyle = reactive({
          display: 'flex',
          height: '30px',
          lineHeight: '30px',
      });
      const findType = ref('');
      const moduleName = ref('password');
      const accountList = reactive({list:[]});
      const { setResetPass } = useLoginState();
      const [registerModal, { setModalProps }] = useModalInner(async (data) => {
          setModalProps({ confirmLoading: false,width:500,canFullscreen:false, closable:true,maskClosable:false,centered:true, footer:null });
          isUpdate.value = !!data?.isUpdate;
          title.value = data?.record?.title || '请选择找回方式';
          showPasswordType.value = data?.record?.showPasswordType || false;
          moduleName.value = data.moduleName;
          findType.value = '';
      });
   
      async function handleSubmit(){
        if(unref(findType)==='mobile'){
          setResetPass(ResetPassEnum.MOBILE);
        }else if(unref(findType) === 'emial'){
          setResetPass(ResetPassEnum.EMAIL);
        }else{
          setResetPass(ResetPassEnum.PASSWORD);
        }
        emit('success',unref(moduleName),unref(findType));
      }
      function handleBack(){
        emit('success')
      }
      return {
        registerModal,
        handleSubmit,
        accountList,
        handleBack,
        radioStyle,
        findType,
        title,
        showPasswordType,
      }
  },
})
</script>
<style lang="less" scoped>
.login-title{
  font-size: 18px;
}
.radio-list{
  width:100%;
  :deep(.ant-radio-wrapper){
    display: flex;
    line-height: 30px;
    height: auto !important;
    width: 100%;
    box-shadow: 0 0 2px 2px #e4e4e4;
    padding: 20px 25px;
    border-radius: 5px;
    margin-bottom: 10px;
  }
  .title{
    font-size: 16px;
    margin-left:20px;
  }
}
 
</style>

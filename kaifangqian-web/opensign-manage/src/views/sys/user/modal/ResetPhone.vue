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
  <div>
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
           <Form  class="p-2 enter-x" :model="formData" :rules="getFormRules" ref="formRef" :labelCol="{span:3}">
              <FormItem name="mobile" class="enter-x" label="手机号">
                  <Input
                    size="large"
                    :disabled="formData.disabled"
                    v-model:value="formData.phone"
                    placeholder="手机号"
                    class="fix-auto-fill"
                  />
                </FormItem>
                <FormItem name="sms" class="enter-x" label="验证码">
                  <CountdownInput
                    size="large"
                    class="fix-auto-fill"
                    v-model:value="formData.sms"
                    placeholder="短信验证码"
                    ref="smsCount"
                    :sendCodeApi="sendCodeApi"
                  />
                </FormItem>
          </Form>
          <PuzzleVerification :w="356" :style="positionStyle" @success="handleSildeSuccess" :sliderText="sliderText" @fail="handleFail"  v-if="slideCodeShow"/>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed,reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { CountdownInput } from '/@/components/CountDown';
  import PuzzleVerification from '/@/components/verification/index.vue';
  import { getSmsCode,unBindTelephone,bindTelephone, getTokenSms, getTokenEmail  } from '/@/api/sys/user';
  import { Form, Input,Row, Col } from 'ant-design-vue';
  import { validateSmscode } from '/@/utils/rules';
  import {useFormValid} from '/@/views/sys/login/useLogin';


  export default defineComponent({
    name: 'AnnounceFormModal',
    components:{
      BasicModal,
      CountdownInput,
      PuzzleVerification,
      Form,
      FormItem:Form.Item,
      Input,
      Row, Col
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const sliderText = ref('向右滑动完成拼图');
      const smsCount = ref(null);
      const slideCodeShow = ref(false);
      const formData = reactive({
        phone:'',
        sms:'',
        disabled:false,
        captchaKey:''
      })
      const positionStyle = {
        left: '50%',
        transform: `translate(${-50 + '%' }, ${-50 + '%'})`,
        top: '50%'
      }
      const  getFormRules  = ref({
        sms:[{ required: true, trigger:'blur', validator:validateSmscode}],
      })
      const formRef = ref();
      const { validForm } = useFormValid(formRef);
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false,width:550 });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          formData.phone = data.record.phone;
          formData.disabled =true;
          formData.sms = '';
        }else{
          formData.phone = '';
          formData.disabled = false;
          formData.sms = '';

        }
      });

      const getTitle = computed(() => (unref(isUpdate) ? '解绑手机号' : '绑定手机号'));

      async function handleSubmit() {
        try {
          const data = await validForm();
          if(!data) return;
          setModalProps({ confirmLoading: true });
          // TODO custom api
          let params = {
            phone:formData.phone,
            captcha:formData.sms,
            captchaKey:formData.captchaKey,
          }
          let result:any = {};
          if(unref(isUpdate)){
            result = await unBindTelephone(params);
          }else{
            result = await bindTelephone(params);
          }
          if(result){
            closeModal();
            emit('success' );
          }
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      function sendCodeApi(){
        return new Promise(async (resolve)=>{
          // let data = await validate()
          // if(!data.phone){
          //   createMessage.warning('请输入手机号');
          //   return false;
          // }
          slideCodeShow.value = true;
          resolve(!unref(slideCodeShow));
          return !unref(slideCodeShow);
        })
      }
      function handleFail(){
        sliderText.value = '验证失败';
        setTimeout(()=>{
          sliderText.value = '向右滑动完成拼图';
        },1000)
      }
      function handleSildeSuccess(){
        sliderText.value = '验证成功';
        setTimeout(async ()=>{
          if(smsCount.value){
            slideCodeShow.value = false;
            let result = await getTokenSms({phone:formData.disabled?undefined:formData.phone,type:'mobile_login'});
            if(result){
             formData.captchaKey = result;
            }
            smsCount.value.triggerClick();
          }
        },2000)
      }

      return {smsCount,formRef, registerModal,positionStyle,formData,getFormRules, getTitle, handleSubmit,handleTinymceChange,handleSildeSuccess,sliderText,slideCodeShow,sendCodeApi,
      handleFail };
    }
  })
</script>
<style >
 
</style>

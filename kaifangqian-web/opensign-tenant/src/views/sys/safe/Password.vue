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
    <a-alert type="info" show-icon>
        <template #message>
          <h1>须知</h1>
          <ul>
            <li>您在该模块所设定的密码规则适用于使用登录密码的用户。</li>
            <li>登录密码失效后，再次登录后，提示用户修改密码，修改成功后，须重新登录。</li>
          </ul>
        </template>
      </a-alert>

      <BasicForm   @register="register" 
      >
      </BasicForm>
  </div>
</template>
<script lang='ts'>
import { defineComponent,onMounted,reactive } from 'vue'
import { BasicForm,useForm} from '/@/components/Form/index';
import { passwordSchema } from './data';
import { useMessage } from '/@/hooks/web/useMessage';
import {getSafeConfig,setSafeConfig} from '/@/api/sys/safe';

interface ConfigItem {
  type:string;
  value:string|number
}
export default defineComponent({
  name: 'Password',
  components: { BasicForm },
  setup(){
    const { createMessage:msg } = useMessage();
    const state = reactive({
      list:[] as ConfigItem[]
    })
    const [  register , {validate,setFieldsValue}] = useForm({
        labelWidth: 120,
        actionColOptions: {
          span:24,
        },
        showResetButton:false,
        baseColProps: { lg: 24, md: 24},
        wrapperCol: {
          span: 24,
        },
        schemas: passwordSchema,
        submitButtonOptions: {
          text: '保存修改',
        },
        submitFunc: customSubmitFunc,
       
    });
    async function fetch(){
      let params = {
        types:'password_composition,password_minimum_len,password_invalid_time,password_repeat_limit'
      }
      let result = await getSafeConfig(params);
      if(result){
        state.list = result;
        result.map(item=>{
          if(item.type==='password_composition'){
            setFieldsValue({
              password_composition:item.value.split(',')
            })
          }else{
            setFieldsValue({
              [item.type]:item.value
            })
          }
        })
      }
    }
    onMounted(() => {
        fetch();
      });
    async function customSubmitFunc(){
       try {
          const values = await validate();
          state.list.map(item=>{
            if(item.type==='password_composition'){
              item.value =  values[item.type].join(',')
            }else{
              item.value = values[item.type]
            }
          })
          // TODO custom api
          let result = await setSafeConfig({sysConfigs:state.list});
          if(result){
              msg.success('操作成功');
              fetch()
          }
        } finally {
          
        } 
    }

    return {
      register
    }
  }
})
</script>
<style lang="less" scoped>
 .ant-form{
  margin-top:20px;
 }
</style>

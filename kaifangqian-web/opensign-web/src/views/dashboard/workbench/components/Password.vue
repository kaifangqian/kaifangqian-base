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
  <div class="app-password-init">
    <BasicModal v-bind="$attrs" @register="registerModal" title="初始化密码" @ok="handleSubmit" :footer="null">
        
    </BasicModal>
  </div>
</template>

<script lang="ts">

import {ref} from "vue";
import { BasicModal,useModalInner } from '/@/components/Modal';
import { Form, Input, Button, Checkbox ,Row, Col} from 'ant-design-vue';
import { useMessage } from '/@/hooks/web/useMessage';

interface VersionItem {
  id:string;
  versionName:string;
  versionDesc:string; 
}
interface AppItem {
   appVersionVOS:VersionItem[];
   loading:boolean;
   appName:string;
   versionId:string;
   containsFlag:boolean;
   appDesc:string;
   appIcon:string;
}

export default{
  name:"AppPassword",
  components:{
    BasicModal,
    Form,
    FormItem: Form.Item,
    Input,
    Button,
    Checkbox,
    Row,
    Col
  },
  setup(_,{emit}) {
   const appList = ref(<AppItem[]>[]);
  const { createMessage: msg } = useMessage();

   const [registerModal, { setModalProps }] = useModalInner(async (data) => {
      setModalProps({ confirmLoading: false,width:900,canFullscreen:false, closable:true,maskClosable:false,centered:false });
     
   })

   function handleSubmit(){
    msg.success('操作成功')
    emit('success')
   }
   
    return {
      appList,
      registerModal,
      handleSubmit
    }
  }
} 
</script>

<style lang="less" scoped>

</style>

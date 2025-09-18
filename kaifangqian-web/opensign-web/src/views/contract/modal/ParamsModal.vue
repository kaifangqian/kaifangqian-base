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
    <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true" wrapClassName='sign-modal'>
      <Params @cancel="handelCancel"></Params>
    </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed  } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useRouter } from 'vue-router';
  import Params from '@/views/contract/params/index.vue';


  export default defineComponent({
    name: 'ParamsModal',
    components:{
      BasicModal,
      Params
    },
    setup(_, { emit }){

      const isUpdate = ref(true);
      const signReId = ref('');
      const signRuId = ref('');
      const taskId = ref('');

      const router = useRouter();
      const { createMessage: msg } = useMessage();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭',
          footer:null,
          defaultFullscreen:true,
        });
        signReId.value = data.record?.signReId;
        signRuId.value = data.record?.signRuId;
        taskId.value = data.record?.taskId;
        router.replace({
            query:{
              signReId:unref(signReId),
              signRuId:unref(signRuId),
              taskId:unref(taskId),
            }
          })
        
      });
     

      
      const getTitle = computed(() => (!unref(isUpdate) ? '填写文档参数' : '填写文档参数'));

      function handelCancel(){
        closeModal()
      }

      async function handleSubmit() {
        try {

            msg.success('保存成功');
            closeModal();
            emit('success', );
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        handelCancel
      };
    }
  })
</script>
<style lang="less" scoped>
  
</style>

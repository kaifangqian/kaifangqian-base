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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
         <div v-html="content"></div>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Icon } from '/@/components/Icon';
  import { addArticleAdd, editArticle, getArticleInfo } from '/@/api/article';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'PreviewModal',
    components:{
      BasicModal,
      PlusOutlined,
      Icon,
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const recordId = ref('');
      const content = ref()
      const { createMessage: msg } = useMessage();
      

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        recordId.value = data.record.id;

        let result = await getArticleInfo({id:recordId.value })
        if(result){
          content.value = result.content;
        }
          
      });

  
      const getTitle = computed(() => (!unref(isUpdate) ? '文章预览' : '文章预览'));

      async function handleSubmit() {
        try {
         
            emit('success');
            closeModal();
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { 
          registerModal,
          getTitle,
          handleSubmit,
          content
      
      };
    }
  })
</script>
<style>
 
</style>

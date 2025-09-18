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
       <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true" wrapClassName="download-modal">
          <a-checkbox
            v-model:checked="state.checkAll"
            :indeterminate="state.indeterminate"
            @change="onCheckAllChange"
          >
            全部文件
          </a-checkbox>
          <a-divider />
          <a-checkbox-group v-model:value="state.checkedList">
            <template v-for="(item,index) in fileList" :key="index">
            <a-checkbox :value="item.id" >{{item.name}}</a-checkbox>
            </template>
          </a-checkbox-group>
        </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed, reactive ,watch } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { handleRuDownload } from '/@/utils';



  export default defineComponent({
    name: 'DownloadModal',
    components:{
      BasicModal,
    },
    setup(_, { emit }){

      const isUpdate = ref(true);
      const signRuId = ref('');

      const fileList:any = ref([]);
      const allKeys = ref([])

      const state = reactive({
        indeterminate: false,
        checkAll: false,
        checkedList: [],
      });

      const onCheckAllChange = (e: any) => {
          // state.checkedList = e.target.checked? allKeys.value:[]
          Object.assign(state, {
            checkedList: e.target.checked ? allKeys.value : [],
            indeterminate: false,
          });
      };

      watch(
        ()=>state.checkedList,
        ()=>{
          state.checkAll = state.checkedList.length === allKeys.value.length;
        }
      )
     
      const { createMessage: msg } = useMessage();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:600,
          cancelText:'关闭',
          // showOkBtn:false, 
        });
        signRuId.value = data.record?.signRuId;
        fileList.value = data.record?.fileList;
        allKeys.value = []
        state.checkedList =  [];
        fileList.value.map(v=>{
          allKeys.value.push(v.id)
        })
        
      });
      const getTitle = computed(() => (!unref(isUpdate) ? '文件下载' : '文件下载'));

      async function handleSubmit() {
        if(state.checkedList.length>0){
          try {
            handleRuDownload({signRuId:signRuId.value,ruDocIdList:state.checkedList.join(',')})
              closeModal();
              emit('success');
          } finally {
            setModalProps({ confirmLoading: false });
          }
        }else{
          msg.warning("请至少选择一个文件再下载");
        }
        
      }
      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        onCheckAllChange,
        fileList,
        state
      };
    }
  })
</script>
<style lang="less" scoped>
.download-modal{
  .ant-divider-horizontal{
    margin:10px 0 25px;
  }
  .ant-checkbox-wrapper{
    margin-left:0;
    display:flex;
  }
}

  
</style>

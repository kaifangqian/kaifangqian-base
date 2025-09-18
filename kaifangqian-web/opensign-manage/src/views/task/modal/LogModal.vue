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
    <BasicModal v-bind="$attrs" @register="registerModal" title="实例日志" @ok="handleSubmit">
      <div class="action">
        <a-button type="primary">下载</a-button>
      </div>
      <div class="log-detail">
        <span>{{instanceState.info}}</span>
        <!-- <a-list item-layout="vertical" size="small" :pagination="pagination" :data-source="instanceState.list">
         <template #renderItem="{ item }">
            <span>{{item}}</span>
         </template>
        </a-list> -->
      </div>
    </BasicModal>
  </div>
</template>
<script lang='ts'>

import { defineComponent, reactive,computed } from 'vue';
import { BasicModal, useModalInner } from '/@/components/Modal';
import { getInstanceLog } from '/@/api/task';
import { useUserStore } from '/@/store/modules/user';

export default defineComponent({
  name: 'LogModal',
  components:{
    BasicModal
  },
  setup(){

    const instanceState = reactive({info:{},instanceId:''});
    const pagination = {
      onChange: (page: number) => {
        getLogList(page)
      },
      pageSize: 3,
    };
    const userStore = useUserStore();
    const appId =  userStore.getUserInfo.jobAppId;
    const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        if(!data.record.instanceId) return; 
        instanceState.instanceId = data.record.instanceId;
        getLogList()
       
    });

    async function getLogList(page=0){
      if(!instanceState.instanceId) return;
       let resultInfo = await getInstanceLog({instanceId:instanceState.instanceId,index:page,appId:appId})
        if(resultInfo){
            instanceState.info = resultInfo.data;

        }
    }

      function handleSubmit(){
          closeModal()
      }

      return {
        registerModal,
        handleSubmit,
        instanceState,
        pagination
      }
  }
})
</script>
<style lang="less" scoped>
.action{
  text-align: right;
  margin-bottom: 15px;
}
</style>

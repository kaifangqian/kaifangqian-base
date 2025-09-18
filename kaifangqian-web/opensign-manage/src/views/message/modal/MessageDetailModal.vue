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
    <BasicModal v-bind="$attrs" @register="registerModal" title="详情" @ok="handleSubmit" :destroyOnClose="true">
        <Descriptions   size="middle"  :title="state.detailInfo.mesTitle"  class="detail-header"
        >
          <DescriptionsItem>
              <div>
                <span class="detail-item"><span class="item-title">发布时间： </span> {{state.detailInfo.createTime}}</span>
              </div>
          </DescriptionsItem>
        </Descriptions>
        <span v-html="state.detailInfo.mesContent"></span>

        <div class="detail-button">
          <template v-for="item in  state.detailInfo.buttons" >
            <a-button @click="handleButton(item)" :type="item.buttonStyle">{{item.buttonName}}</a-button>
          </template>
        </div>
        <template #footer>
          <a-button @click="closeModal()">关闭</a-button>
        </template>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref, computed, reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Descriptions,DescriptionsItem } from 'ant-design-vue';
  import { getMyMessageInfo} from '/@/api/message';
  import { useGo } from '/@/hooks/web/usePage';
  
  
  interface MsgButton {
    buttonStyle?:string;
    buttonName:string;
  }

  export default defineComponent({
    name:'MessageDetailModal',
    components: { Descriptions,BasicModal,DescriptionsItem},
    setup() {
      const state = reactive({
          detailInfo:{
            mesTitle:'',
            sender:'',
            createTime:'',
            mesContent:'',
            buttons:Array<MsgButton>
          }
        });
      const msgTitle = ref('查看详情');
      const go = useGo();

      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false,width:1200 });
        let recordId = data.record.id;
        if(recordId){
          let result = await getMyMessageInfo({id:recordId});
          if(result){
              state.detailInfo = result;
          }
        }
      });

      const getTitle = computed(()=>msgTitle)
      function handleSubmit(){
        closeModal()

      }
      function handleClose(){
        closeModal()
      }
      function handleButton(item){
        console.log(item)
        let routerParams = JSON.parse(item.buttonPara); 
        closeModal();
        go(item.buttonRoute+ routerParams.infoId);
      }

      return {
        registerModal,
        getTitle,
        state,
        closeModal,
        handleSubmit,
        handleClose,
        handleButton
      };
    },
  });
</script>
<style lang="less" scoped>
  .detail-header{
    border-bottom: 1px solid #f0f0f0;
    .detail-item{
      margin-right: 15px;
    }
  }
  .detail-button{
    position: absolute;
    bottom: 5px;
    right: 14px;
    :deep(.ant-btn){
      margin:0 4px;
    }
  }
  </style>

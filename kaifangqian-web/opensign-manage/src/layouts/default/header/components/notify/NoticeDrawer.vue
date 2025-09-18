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
    <BasicDrawer
      @register="registerDrawer"
      :showFooter="false"
      :title="getTitle"
      width="30%"
      @ok="handleSubmit"
    >
    <div>

        <div class="notify-header">
          <a-tabs v-model:activeKey="activeKey" @change="handleTabChange">
            <a-tab-pane key="1" tab="公告">
              <a-tree-select v-model:value="announceType"  @change="handleAnncounceType" :allowClear="true"
               :tree-data="announceTypeData" 
               :fieldNames="{children:'children', label:'name', value: 'id'}"
              />
              <a-spin :spinning="spinning">
                <NoticeList :list="announceListData" v-if="activeKey==1" @read="handleAnnounceRead" @router="handleAnnounceRouter"/> 
              </a-spin>
              
            </a-tab-pane>
            <a-tab-pane key="2" tab="消息" force-render>
              <a-tree-select v-model:value="msgType"  :allowClear="true" @change="handleMsgType"
               :tree-data="msgTypeData" 
               :fieldNames="{children:'children', label:'name', value: 'id'}"
              />
              <a-spin :spinning="spinning">
                <NoticeList :list="msgListData" v-if="activeKey==2"  @read="handleMessageRead" @router="handleMessageRouter"/>
              </a-spin>
            </a-tab-pane>
          </a-tabs>
        </div>
    </div>
     
    </BasicDrawer>
</template>
<script lang='ts'>
import { defineComponent, computed, ref, onMounted, unref } from 'vue';
import { BasicDrawer, useDrawerInner } from '/@/components/Drawer';
import { Select } from 'ant-design-vue';
import { listData } from './data';
import NoticeList from './NoticeList.vue';
import { useGo } from '/@/hooks/web/usePage';
import { getMyMessage, getTemplateTypeList,readMyMessage } from '/@/api/message';
import {getAnnounceTree, getMyAnnounceList, myAnnounceSendRead } from '/@/api/announce';


export default defineComponent({
  name: 'NoticeDrawer',
  components: { BasicDrawer, NoticeList, Select},
  props:{

  },
  setup(_,{ emit }){
    
    const msgListData = ref([]);
    const msgTypeData = ref([]);
    const announceListData = ref([]);
    const announceTypeData = ref([]);
    const announceType = ref('');
    const msgType = ref('');
    const announceCount = ref(0);
    const msgCount = ref(0);
    const spinning = ref<boolean>(false);
    const getTitle = computed(() => ('站内消息'));
    const activeKey = ref('1');
    const go = useGo();

    const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
      // setDrawerProps({ confirmLoading: false });
      getAnnounceType();
      getMsgType();
      getAnnounceList();
      getMessageList();

    })
    // const bellCount = computed(() => {
    //   let count = msgCount.value;
    //   emit('changeCount',count);
    //   return count;
    // })


    onMounted(()=>{
      // getAnnounceList()
      
    })

    async function getAnnounceType(){
      let result = await getAnnounceTree();
      if(result){
        announceTypeData.value = result;
      }
    }
    async function getMsgType(){
      let result = await getTemplateTypeList();
      if(result){
        msgTypeData.value = result;
      }
    }

    function handleSubmit(){

    }
    function handleAnncounceType(val) {
      getAnnounceList({announcementType:val})

    }
    function handleMsgType(val) {
      getMessageList({templateType:val})
    }
    function handleTabChange(val){
      // if(val==2){
      //   getMessageList()
      // }
    }
    async function handleAnnounceRead(record){
      let result = await myAnnounceSendRead({ids:record.id});
      if(result){
        getAnnounceList();
      }

    }
    async function handleMessageRead(record){
      let result = await readMyMessage({ids:record.id});
      if(result){
        getMessageList();
      }

    }

    async function getMessageList(params){
      spinning.value = true;
      let result = await getMyMessage({pageNo:1,pageSize:20,...params,readFlag:0});
      if(result){
        spinning.value = false;
        result.records.map(item=>{
          item.title = item.mesTitle; 
        });
        msgListData.value = result.records;
        msgCount.value = result.total;
        emit('changeCount',unref(msgCount));

      }
    }
    async function getAnnounceList(params){
      spinning.value = true;
      let result = await getMyAnnounceList({pageNo:1,pageSize:20,...params,isRead:0});
      if(result){
        spinning.value = false;
        announceListData.value = result.records;
        announceCount.value = result.total;

      }
    }

    function handleAnnounceRouter(item){
      closeDrawer();
      go('/myannounce/'+ item.anntId);
    }

    function handleMessageRouter(item){
      closeDrawer();
      // 调用标记已读接口更新提醒数量
      handleMessageRead(item);
      console.log('触发1')
      emit('route',item)
      // go('/message/my/'+ item.id);
    }
    return {
      msgType,
      announceType,
      msgTypeData,
      announceTypeData,
      listData,
      msgListData,
      announceListData,
      registerDrawer,
      getTitle,
      handleSubmit,
      activeKey,
      handleAnncounceType,
      handleMsgType,
      handleTabChange,
      handleAnnounceRead,
      handleMessageRead,
      spinning,
      handleMessageRouter,
      handleAnnounceRouter
    }
  }
})
</script>
<style lang="less" scoped>
:deep(.ant-drawer-header-title){
  flex-direction: row-reverse;
}
 .notify-header{
  margin-bottom: 10px;
 }
 :deep(.ant-select){
    width:200px;
 }
</style>

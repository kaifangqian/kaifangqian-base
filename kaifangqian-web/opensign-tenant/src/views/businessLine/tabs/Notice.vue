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
  <div class="notice-content">
    <a-alert type="info">
      <template #message>
        <h1>温馨提示</h1>
        <ul style="padding-left: 20px;">
          <li style="list-style: disc;">合同文件发起后，电子签章系统将根据签署顺序，发送短信通知接收方进行文件填写和签署，如需关闭接收方短信通知，请进行配置</li>
        </ul>
      </template>
    </a-alert>
    <br/>
    <a-card title="短信通知" size="small">
      <div class="notice-box">
        <div class="notice-item">
          <div class="item-text"><p>文件填写</p></div>
          <div class="item-switch">
            <li>
              <a-space :sise="10">
                <spam>短信</spam>
                <a-switch v-model:checked="noticeItems.writeTaskFlagPhone" />
              </a-space>
            </li>
            <li style="padding-left:20px ;">
              <a-space>
                <spam>邮箱</spam>
                <a-switch v-model:checked="noticeItems.writeTaskFlagEmail" />
              </a-space>
            </li>
          </div>
        </div>
        
        <div class="notice-item">
          <div class="item-text"><p>文件签署（发起方内部）</p></div>
          <div class="item-switch">
            <li>
              <a-space :sise="10">
                <spam>短信</spam>
                <a-switch v-model:checked="noticeItems.signTaskInFlagPhone" />
              </a-space>
            </li>
            <li style="padding-left:20px ;">
              <a-space>
                <spam>邮箱</spam>
                <a-switch v-model:checked="noticeItems.signTaskInFlagEmail" />
              </a-space>
            </li>
          </div>
        </div>
        
        <div class="notice-item">
          <div class="item-text"><p>文件签署（外部接收方）</p></div>
          <div class="item-switch">
             <li>
               <a-space :sise="10">
                 <spam>短信</spam>
                 <a-switch v-model:checked="noticeItems.signTaskOutFlagPhone" />
               </a-space>
             </li>
             <li style="padding-left:20px ;">
               <a-space>
                 <spam>邮箱</spam>
                 <a-switch v-model:checked="noticeItems.signTaskOutFlagEmail" />
               </a-space>
             </li>
          </div>
        </div>
        
        <div class="notice-item">
          <div class="item-text"><p>文件抄送（发起方内部）</p></div>
          <div class="item-switch">
             <li>
               <a-space :sise="10">
                 <spam>短信</spam>
                 <a-switch v-model:checked="noticeItems.copyBeginFlagPhone" />
               </a-space>
             </li>
             <li style="padding-left:20px ;">
               <a-space>
                 <spam>邮箱</spam>
                 <a-switch v-model:checked="noticeItems.copyBeginFlagEmail" />
               </a-space>
             </li>
          </div>
        </div>
        
        <div class="notice-item">
          <div class="item-text"><p>文件抄送（外部）</p></div>
          <div class="item-switch">
             <li>
               <a-space :sise="10">
                 <spam>短信</spam>
                 <a-switch v-model:checked="noticeItems.copySignFlagPhone" />
               </a-space>
             </li>
             <li style="padding-left:20px ;">
               <a-space>
                 <spam>邮箱</spam>
                 <a-switch v-model:checked="noticeItems.copySignFlagEmail" />
               </a-space>
             </li>
          </div>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script lang="ts">
  import {ref, defineComponent, unref, onMounted, h, nextTick} from "vue"
  import { getReNotice,saveReNotice } from '/@/api/businessLine';
  import { useRouter } from 'vue-router';
  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    name:"BusinessNotice",
    components:{
    },
    props:{
      lineFormData:{
        type:Object
      }
    },
    setup(props) {
      const { createMessage: msg } = useMessage();
      const noticeItems = ref({
        writeTaskFlagPhone:false,
        writeTaskFlagEmail:false,
        signTaskInFlagPhone:false,
        signTaskInFlagEmail:false,
        signTaskOutFlagPhone:false,
        signTaskOutFlagEmail:false,
        copyBeginFlagPhone:false,
        copyBeginFlagEmail:false,
        copySignFlagPhone:false,
        copySignFlagEmail:false
      })
      
      const router = useRouter();
      const signReId = router.currentRoute.value.query.signReId;
      
      onMounted(()=>{
        initData();
      })
      async function initData(){
        console.log("props.lineFormData,",props.lineFormData);
        const noticeResponse = await getReNotice({singReId:signReId})
        noticeItems.value = {...noticeResponse}
      }
      async function saveNoticeData(){
        const {code} = await saveReNotice(noticeItems.value);
        if(code == 200){
          msg.success("保存成功");
        }
        // console.log("saveNoticeData",result);
      }
      return {
        noticeItems,saveNoticeData
      }
    }
  })
</script>

<style lang="less" scoped>
  .notice-box{
    
  }
  .notice-item{
    display: flex;
    height: 60px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    .item-switch{
      padding-right: 10px;
      align-content: center;
      display: flex;
    }
    .item-text{
      flex:1;
      font-weight: 600;
      font-size: 14px;
      line-height: 60px;
      padding-left: 10px;
      p{
        padding:0;
        margin: 0;
      }
    }
  }
</style>

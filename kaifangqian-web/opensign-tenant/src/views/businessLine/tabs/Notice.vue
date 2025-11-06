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
        <ul style="padding-left: 20px">
          <li style="list-style: disc"
            >合同发起后，系统将按签署顺序自动发送短信/邮件通知。您可在此管理具体通知项，按需开启或关闭。</li
          >
        </ul>
      </template>
    </a-alert>
    <br/>
    <a-card title="短信 / 邮件通知" size="small">
      <div class="notice-box">
        <div class="notice-item">
          <div class="item-text"><p>文件填写</p></div>
          <div class="item-switch">
            <li>
              <a-space :sise="10">
                <span>短信</span>
                <a-switch v-model:checked="noticeItems.writeTaskFlagPhone" />
              </a-space>
            </li>
            <li style="padding-left: 40px">
              <a-space>
                <span>邮件</span>
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
                <span>短信</span>
                <a-switch v-model:checked="noticeItems.signTaskInFlagPhone" />
              </a-space>
            </li>
            <li style="padding-left:40px ;">
              <a-space>
                <span>邮件</span>
                <a-switch v-model:checked="noticeItems.signTaskInFlagEmail" />
              </a-space>
            </li>
          </div>
        </div>

        <div class="notice-item">
          <div class="item-text"><p>文件审批（发起方内部）</p></div>
          <div class="item-switch">
            <li>
              <a-space :sise="10">
                <span>短信</span>
                <a-switch v-model:checked="noticeItems.approvalTaskFlagPhone" />
              </a-space>
            </li>
            <li style="padding-left:40px ;">
              <a-space>
                <span>邮件</span>
                <a-switch v-model:checked="noticeItems.approvalTaskFlagEmail" />
              </a-space>
            </li>
          </div>
        </div>
        
        <div class="notice-item">
          <div class="item-text"><p>文件签署（外部接收方）</p></div>
          <div class="item-switch">
             <li>
               <a-space :sise="10">
                 <span>短信</span>
                 <a-switch v-model:checked="noticeItems.signTaskOutFlagPhone" />
               </a-space>
             </li>
             <li style="padding-left:40px ;">
               <a-space>
                 <span>邮件</span>
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
                 <span>短信</span>
                 <a-switch v-model:checked="noticeItems.copyBeginFlagPhone" />
               </a-space>
             </li>
             <li style="padding-left:40px ;">
               <a-space>
                 <span>邮件</span>
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
                 <span>短信</span>
                 <a-switch v-model:checked="noticeItems.copySignFlagPhone" />
               </a-space>
             </li>
             <li style="padding-left:40px ;">
               <a-space>
                 <span>邮件</span>
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
        approvalTaskFlagPhone:false,
        approvalTaskFlagEmail:false,
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
        // console.log("noticeItems.value,",noticeItems.value);
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
      padding-right: 30px;
      align-content: center;
      display: flex;
      align-items: center;
    }
    .item-text{
      flex:1;
      // font-weight: 600;
      font-size: 14px;
      line-height: 60px;
      padding-left: 30px;
      p{
        padding:0;
        margin: 0;
      }
    }
  }

  :deep(.ant-card-head){
    background-color: #f7f8fb;
  }
  :deep(.ant-card-body) {
    padding: 0;
  }
</style>

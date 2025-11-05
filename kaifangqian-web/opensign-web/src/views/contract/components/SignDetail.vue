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
  <div class="sign-detail-container">
    <ul>
      <li>
        <span>编号：</span>
        <span>{{ signInfo.baseVo.code || '无' }}</span>
      </li>
      <li>
        <span>主题：</span>
        <span>{{ signInfo.baseVo.subject }}</span>
      </li>
      <li>
        <span>状态：</span>
        <a-tag class="sign-status" :color="loadSignColor(signInfo.baseVo.status)">{{ loadRuStatus(signInfo.baseVo.status) }}</a-tag>
      </li>
      <li>
        <span>发起时间：</span>
        <span>{{ signInfo.baseVo.startDateString }}</span>
      </li>
      <li>
        <span>签署截止日期：</span>
        <span>{{ signInfo.baseVo.expireDateString  || '无'}}</span>
      </li>
      <li >
        <span>附件：</span>
        <div class="contract-files" v-if="signInfo.otherFileList&&signInfo.otherFileList.length">
          <BasicFileList :fileList="signInfo.otherFileList" />
        </div>
        <span v-else>无</span>
      </li>
      <li>
        <span>抄送：</span>
          <div class="flex-item ccerList-area"  v-if="signInfo.ccerList && signInfo.ccerList.length">
            <ul class="ccerList-ul">
              <li v-for="(item,index) in signInfo.ccerList" :key="index">
                <a-tag>{{item.ccerType == 1? item.internalCcerName :(item.externalCcerName +'（' + item.externalCcedValue + '）')}}</a-tag>
              </li>
            </ul>
          </div>
          <span v-else>无</span>
      </li>
    </ul>
    <PreviewModal @register="registerModal"></PreviewModal>
  </div>
</template>

<script lang="ts">
import {ref, unref, defineComponent,onMounted} from "vue";
import { useRouter } from 'vue-router';
import { useModal } from '/@/components/Modal';
import {  getBusinessRuInfo} from '/@/api/contract';
import { loadRuStatus, loadSignColor } from '../document/transform';
import { Icon, SvgIcon } from '/@/components/Icon';
import PreviewModal from '/@/views/contract/modal/PreviewModal.vue';
import { BasicFileList } from '/@/components/FileInfoList/index';

export default defineComponent({
  name:"SignDetail",
  components:{
    Icon, SvgIcon, PreviewModal, BasicFileList
  },
  setup() {
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const signRuId = route.query.signRuId;
      const signInfo:any = ref({
        baseVo:{},
        fileList:[]
      })

      const [registerModal, { openModal }] = useModal();

      onMounted(()=>{
        fetch()
      })

      async function fetch() {
        let result = await getBusinessRuInfo({signRuId});
        if(result){
          signInfo.value = result;
        }
      } 

      function handlePreview(row){
        openModal(true,{
          isUpdate:false,
          record:{
            docId:row.annexId,
            docName:row.name
          }
        })
      }
    return {
      signInfo,
      loadRuStatus,
      registerModal,
      handlePreview,
      loadSignColor,
    }
  }
})
</script>

<style lang="less" scoped>
.contract-files{
  // padding:5px;
  ul{
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    width: 800px;
    // border: 1px solid #e9e9e9;
    li{
      width:170px;
      height:220px;
      background:#fff;
      text-align: center;
      margin:10px;
      position:relative;
      box-sizing: border-box;
      display: flex;
      padding:0;
      flex-direction: column;
      border:1px solid #d3d1d1;
     
      .file-icon{
        border-bottom: 1px solid #d3d1d1;
        position:relative;
        height:80%;
        &:hover{
          border-radius: 2px;
          .file-mask{
            display: block;
          }
          :deep(.app-iconify){
            visibility: visible;;
          }
        }
        .file-tag{
          position:absolute;
          left:2px;
          top:0;
        }
        .file-img{
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate(-50%,-50%);
        }
      }
      .file-footer{
        display: flex;
        align-items: center;
        height: 20%;
      }
      .file-mask{
        display: none;
        position: absolute;
        left:0;
        top:0;
        right:0;
        bottom:0;
        background-color: rgba(12, 12, 14, 0.7);
        :deep(.ant-btn-link){
          color:#fff;
          height: 24px;
          padding:0px 15px;
          border:1px solid #fff;
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate(-50%,-50%);
            &:hover{
              background: #fff;
              color:#000;
            }
        }
       
      }
      :deep(.app-iconify){
        position:absolute;
        top:-5px;
        right:-5px;
        cursor: pointer;
        z-index: 800;
        background: #fff;
        border-radius: 50%;
        visibility: hidden;;
      }
      .file-img{
        width:100%;
        height:100%;
      }
      .file-name{
        font-size: 14px;
        font-weight:550;
        white-space: nowrap;
        width:100%;
        overflow: hidden;
        // text-overflow: ellipsis;
        margin-bottom: 0;
        color: #333;
      }
    }
  }
}
.sign-detail-container{
  ul li{
    margin-bottom: 20px;
  }
  .ccerList-area{
    margin-top:20px;
    li{
      margin:5px;
    }
    .ccerList-ul{
      display: flex;
      justify-content: flex-start;
      flex-wrap: wrap;
    }
  }
}

</style>

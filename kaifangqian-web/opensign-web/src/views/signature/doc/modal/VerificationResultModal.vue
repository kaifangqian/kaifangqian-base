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
        <a-form  :model="fromData" name="basic" :labelCol="{ style: 'width: 100px' }"
          autocomplete="off" labelAlign="left" layout="horizontal">
        <div class="result-content bottom-border">
          <div class="title">
            电子文件信息
          </div>
          <div class="body" :class="[fromData.pdfSingResult == 1 || fromData.pdfSingResult == 2 ?'':(fromData.pdfSingResult == 4?'validate-success':'validate-error')]">
            <div>
              <a-form-item label="文件名称" >
                <span class="form-content-text">{{fromData.pdfName}}</span>
              </a-form-item>
              <a-form-item label="文件大小">
                <span class="form-content-text">{{fromData.pdfSize}}KB</span>
              </a-form-item>
            </div>
          </div>
        </div>
        <div class="result-content" v-if="fromData.pdfSingResult == 4 ||fromData.pdfSingResult == 3">
          <div class="title">
            文件中共包含{{fromData.signatureDetails?fromData.signatureDetails.length:0}}个电子签名
          </div>
          <div  v-if="fromData.signatureDetails?.length>0">
              <div class="body body-border" v-for="item in fromData.signatureDetails">
            <div class="left">
                <div class="item-label" style="color:rgba(0,0,0,0.5)">签名信息</div>
                <a-form-item label="签名方" >
                  <span class="form-content-text">{{item.certName}}</span>
                </a-form-item>
                <a-form-item label="签署时间" >
                  <span class="form-content-text">{{item.signTime}}</span>
                </a-form-item>
                <a-form-item label="签署时间来源" >
                  <span class="form-content-text">{{item.location}}</span>
                </a-form-item>
                <div style="height: 20px;"></div>
                <div class="item-label" style="color:rgba(0,0,0,0.5)">证书信息</div>
                <a-form-item label="序列号" >
                  <span class="form-content-text">{{item.serialNumber}}</span>
                </a-form-item>
                <a-form-item label="颁发机构" >
                  <span class="form-content-text">{{item.userDnName}}</span>
                </a-form-item>
                <a-form-item label="有效期" >
                  <span class="form-content-text">{{item.validStartTime}} - {{item.validEndTime}}</span>
                </a-form-item>
            </div>
            <div class="right" v-if="fromData.pdfSingResult == 4">
              <a-form-item label="" >
                <span class="form-content-success">签名有效</span>
              </a-form-item>
              <a-form-item label="" >
                <span class="form-content-success">自签名以来，内容未被修改</span>
              </a-form-item>
              <a-form-item label=""  v-if="item.sealBase64">
                <img class="seal-image" :src="'data:image/png;base64,' + item.sealBase64"/>
              </a-form-item>
            </div>
            <div class="right" v-if="fromData.pdfSingResult == 3">
              <a-form-item label="" >
                <span class="form-content-error">签名无效</span>
              </a-form-item>
              <a-form-item label="" >
                <span class="form-content-error">自签名以来，内容已被篡改</span>
              </a-form-item>
              <a-form-item label=""  v-if="item.sealBase64">
                <img class="seal-image" :src="'data:image/png;base64,' + item.sealBase64"/>
              </a-form-item>
            </div>
            </div>
          </div>
        </div>
        <div class="result-content" v-else>
          <div class="title">
            文件验签失败
          </div>
          <div class="body" style="color: #ff4d4f;" v-if="fromData.pdfSingResult == 2">
            PDF签名验证时发生错误，可能上传的文件格式不正确、签名包含不正确的、无法识别的、已损坏的或可疑的数据
          </div>
          <div class="body" style="color: #faad14;" v-if="fromData.pdfSingResult == 1">
            PDF文件未发现任何数字签名信息
          </div>
        </div>
         </a-form>
        <template #footer>
          <a-button type="primary" @click="closeModal">关闭</a-button>
        </template>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref } from 'vue';
  import { BasicTree,TreeItem } from '/@/components/Tree/index';
  import { BasicTable, useTable} from '/@/components/Table';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { getAuthGroup,setOrgAuthData } from '/@/api/auth/group';
  import { useMessage } from '/@/hooks/web/useMessage'; 

  import { searchFormSchema,tableColumns, } from '/@/views/auth/data';

  export default defineComponent({
    name: 'AddTemplateModal',
    components:{
      BasicModal,
      BasicTree,
      BasicTable
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const recordId = ref('');
      const { createMessage: msg } = useMessage();
      const getTitle = ref('查验结果');
      const fromData = ref<any>({});
      const validateResult = ref(0);
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1000,
          // cancelText:'关闭' ,
          centered:true,
        });
        isUpdate.value = !!data?.isUpdate;
        fromData.value = data.record;
        console.log(data.record);
        if(data.record.pdfSingResult == 4 &&data.record.signatureDetails.length>0){
          // data.record.signatureDetails.forEach(item=>{
            // if(!!item.validate && validateResult.value == 0){
            //   validateResult.value = 1;
            // }else{
            //   validateResult.value = 2;
            // }
            // if(!item.validate){
            //   validateResult.value = 2;
            // }
            
          // })
        }
        // recordId.value = data.record.recordId;
        //fetch()
      });
 
      async function handleSubmit(){
        

      }
      

      return {
        registerModal,
        handleSubmit,fromData,
        getTitle,closeModal,validateResult
      }
    },
  })
</script>
<style lang="less">
  
  .result-content{
    .title{
      padding:0 15px;
      position: relative;
      line-height: 30px;
    }
    .title::after{
      content:'';
      position: absolute;
      width: 4px;
      height: 12px;
      top:9px;
      left:0;
      background-color: red;
    }
    .body{
      padding:20px 0;
      display: flex;
      .left{
        width:70%;
      }
      .right{
        width: 30%;
        height: 100%;
      }
    }
    .body-border{
      border: 1px solid rgba(0,0,0,0.1);
      border-radius: 5px;
      padding: 10px 20px;
      margin-top: 20px;
    }
    
    .text-label,.text-value{
      line-height: 30px;
    }
    .text-label{
      text-align: left;
    }
  }
  .bottom-border{
    border-bottom: 1px solid rgba(0,0,0,0.2);
    margin-bottom: 20px;
  }
  .form-content-success{
    color:#52c41a;
  }
  .form-content-error{
    color:red;
  }
  .seal-image{
    max-width: 140px;
  }
  .validate-success{
    background-image: url(/@/assets/icons/validate-success.svg);
    background-repeat: no-repeat;
    background-size: 150px 150px;
    background-position-x: 86px;
    background-position-y: -23px;
  }
  .validate-error{
    background-image: url(/@/assets/icons/validate-error.svg);
    background-repeat: no-repeat;
    background-size: 150px 150px;
    background-position-x: 86px;
    background-position-y: -23px;
  }
</style>

<style>
  .result-content .ant-form-item{
    margin-bottom: 0;
  }
</style>

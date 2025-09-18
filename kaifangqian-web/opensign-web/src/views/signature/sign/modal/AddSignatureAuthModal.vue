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
      <BasicModal v-bind="$attrs" @register="registerModal" title="新增签名授权" @ok="handleSubmit">
          
         <a-form :model="fromData" ref="signatureFormRef" :labelCol="{span: 5, offset: 0}">
         	<a-form-item label="授权企业"  name="tenantId" :rules="[{ required: true, message: '请输入签名名称' }]" >
         		<a-select v-model:value="fromData.tenantId"  placeholder="请选择授权企业" @change="tenantChange">
              <template v-for="item in tenantList">
                <a-select-option v-if="item.tenantType == 1"  :value="item.tenantId">{{item.tenantName}}</a-select-option>
              </template>
         		</a-select>
         	</a-form-item>
          <a-form-item label="签署业务"  name="signReId" :rules="[{ required: true, message: '请选择签署业务' }]" >
          	<!-- <a-input  v-model:value="fromData.businessLineId" placeholder="请输入签章名称" class="input-width"/> -->
            
            <a-select v-model:value="fromData.signReId"  :placeholder="fromData.tenantId?'请选择签署业务':'请先选择授权企业'"
              @change="businessLineSelect">
              <template v-for="item in businessLineList">
                <a-select-option  :value="item.id">{{item.name}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>
          <a-form-item label="授权截止日期"  name="expireTime" :rules="[{ required: true, message: '请选择授权截止日期' }]" >
            <a-date-picker v-model:value="fromData.expireTime" :disabled-date="disabledDate"  placeholder="请选择授权截止日期" 
             format="YYYY-MM-DD" style="width:100%"/>
          </a-form-item>
          <a-form-item label="授权签名"  name="sealId" :rules="[{ required: true, message: '请选择签名' }]" >
          	<a-button type="link" @click="openSelectSeal">{{selectSignatureImage?'重新选择':'选择签名'}}</a-button>
            <div style="height: 80px; border: 1px solid #e5e5e5;display:table;" v-if="selectSignatureImage">
              <img :src="selectSignatureImage" style="height: 80px"/>
            </div>
          </a-form-item>
          <div class="signature-inform">
            <a-checkbox v-model:checked="signatureInform">授权完成后，在授权有效期内，被授权公司的{{businessLineName?'【'+businessLineName+'】':''}}电子文件可直接调用本人的数字证书以及个人签名自动签署文件，且代表本人真实的签署意愿。</a-checkbox>
          </div>
         </a-form>
         <template #footer>
         	<div>
         		<a-button @click="closeModal">取消</a-button>
         		<a-button type="primary" :disabled="!signatureInform" @click="handleSubmit">确定</a-button>
         	</div>
         </template>
      </BasicModal>
      <SignatureTableModal @register="registerSignatureTableModal" @success="selectSealSuccess"/>
      <WishModal @register="registerWish" @success="handleConfirmSuccess"></WishModal>
  </div>
</template>
<script lang='ts'>
  
  import { defineComponent,ref,unref,onMounted } from 'vue';
  import { BasicTable, useTable} from '/@/components/Table';
  import { BasicModal, useModalInner,useModal} from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage'; 
  import { message} from 'ant-design-vue'; 
  import { useUserStore } from '/@/store/modules/user';
  
  import { SvgIcon } from '/@/components/Icon';
  import {signGenerate,signBase64,signSave} from "../api"
  import { BasicForm, useForm } from '/@/components/Form/index'
  
  import {formSchema} from "../data"
  import {saveSealAuth,getListSignRe} from "../api/signatureAuth"
  
  import {getMyTenantDeparts} from "/@/api/sys/user"
  import { getBusinessLine } from '/@/api/contract';
  import SignatureTableModal from "./SignatureTableModal.vue"
  import WishModal from './WishModal.vue';
  import dayjs, { Dayjs } from 'dayjs';
  export default defineComponent({
    name: 'AddSignatureModal',
    components:{
      BasicModal,SvgIcon,SignatureTableModal,
      BasicTable,BasicForm,WishModal
    },
    setup(_,{emit}){
      const recordId = ref('');
      const { createMessage: msg } = useMessage();
      const fromData = ref({
        sealId:"",
        authTime:"",
        expireTime:"",
        signReId:null,
        tenantId:null,
        confirmType:"",
      });
      const signatureFormRef = ref({});
      const signatureInform = ref(true);
      const selectSignatureImage = ref("");
      const userStore = useUserStore();
      const userInfo =  userStore.getUserInfo;
      const tenantList = ref([]);
      const businessLineList = ref([]);
      const businessLineName = ref("");
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:500,
          //cancelText:'关闭' ,
          centered:true,
          canFullscreen:false
        });
        tenantList.value = await getMyTenantDeparts();
        // businessLineList.value = await getBusinessLine({});
        // const list = await getListSignRe({pageSize:1000,tenantId:});
        
        // console.log("tenantList.value",list)
      });
      // const [registerSignatureTableModal, { openModal,closeModal }] = useModal();
      const [registerSignatureTableModal, { openModal:openTableModal,closeModal:closeTableModal }] = useModal();
      const [registerWish, { openModal:openWishModal,closeModal:closeWishModal }] = useModal();
      async function handleSubmit(data){
        // if((confirmTypeInfo.value.confirmType == 'password' || confirmTypeInfo.value.confirmType == 'phone_email' || confirmTypeInfo.value.confirmType == 'double') && !willResult.value){
        try{
          const data = await signatureFormRef.value.validateFields();
          // const  confirmType = await getMyConfirmType({});
          // const confirmType = "phone_email";
          // fromData.value.confirmType = confirmType;
          fromData.value.authTime = dayjs(fromData.value.expireTime).format("YYYY-MM-DD");
          // closeModal();
          // openWishModal(true,{
          //   isUpdate:false,
          //   record:{
          //     confirmType:confirmType,
          //     fromData:fromData.value
          //   }
          // })
          const result = await saveSealAuth({
            ...fromData.value
          });
          if(result.code == 200){
            msg.success('授权添加成功');
            handleConfirmSuccess(true);
            closeModal();
          }
          
        }catch(e){
          
        }
      }
      function openSelectSeal(){
        openTableModal(true,{})
        console.log("dayjs",dayjs().startOf('day'));
      }
      function selectSealSuccess(seal:any){
        fromData.value.sealId =  seal.sealId;
        selectSignatureImage.value = seal.image;
        signatureFormRef.value.validate("sealId");
      }
      async function tenantChange(tenantId){
        const result = await getListSignRe({pageSize:1000,tenantId:tenantId});
        businessLineList.value = result.records;
        // console.log("getListSignRe：",list);
      }
      function handleConfirmSuccess(flag:boolean){
        if(signatureFormRef.value){
          signatureFormRef.value.resetFields();
        }
        selectSignatureImage.value = "";
        businessLineName.value="";
        if(flag){
          emit("success");
        }
      }
      const disabledDate = (current: Dayjs) => {
        
        return current && current <= dayjs().startOf('day');
      };
      function businessLineSelect(val){
        const businessLine:any = businessLineList.value.find(b=>{return b.id == val});
        businessLineName.value = businessLine.name;
      }
      return {
        fromData,registerModal,handleSubmit,tenantList,businessLineList,selectSealSuccess,openSelectSeal,
        registerSignatureTableModal,selectSignatureImage,signatureInform,registerWish,signatureFormRef,
        tenantChange,handleConfirmSuccess,closeModal,disabledDate,businessLineSelect,businessLineName,dayjs
      }
    },
  })
</script>
<style lang="less">
  .signature-inform{
    width: 90%;
    margin: 0 auto;
  }
</style>

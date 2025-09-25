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
         <div class="signature-dialog-item">
         	<div class="title">
         		<span>基本信息</span>
         	</div>
         <a-form :model="fromData" ref="signatureFormRef">
         	<a-form-item label="签名名称"  name="sealName" :rules="[{ required: true, message: '请输入签名名称' }]" >
         		<a-input  v-model:value="fromData.sealName" placeholder="请输入签章名称" class="input-width"/>
         	</a-form-item>
         </a-form>
         <a-divider />
        </div>
         
         <div class="signature-dialog-item">
          	<div class="title">
          		<span>签名图案</span>
          	</div>
            <a-tabs v-model:activeKey="fromData.activeKey" @change="tabChange">
                <a-tab-pane :key="1" tab="艺术字体">
            		<div class="wordart-content-title">
            			选择签名样式
            		</div>
            		 <div class="wordart-content">
            			<ul>
            				<li @click="signatureStyleSelect(1)" :class="fromData.sealStyle == 1?'hover':''">
            					<div class="signature-style">
            						<div>
                          <SvgIcon name="personal-rect" size="100"></SvgIcon>
            						</div>
            					</div>
            					<div class="signature-style-name">矩形</div>
            				</li>
            				<li @click="signatureStyleSelect(2)" :class="fromData.sealStyle == 2?'hover':''">
            					<div class="signature-style">
            						<div>
                          <SvgIcon name="personal-rect-border" size="100"></SvgIcon>
            						</div>
            					</div>
            					<div class="signature-style-name">矩形（边框）</div>
            				</li>
            				<li @click="signatureStyleSelect(3)" :class="fromData.sealStyle == 3?'hover':''">
            					<div class="signature-style">
            						<div>
                          <SvgIcon name="personal-xy" size="100"></SvgIcon>
            						</div>
            					</div>
            					<div class="signature-style-name">方形</div>
            				</li>
            				<li @click="signatureStyleSelect(4)" :class="fromData.sealStyle == 4?'hover':''">
            					<div class="signature-style">
            						<div>
                          <SvgIcon name="personal-xy-border" size="100"></SvgIcon>
            						</div>
            					</div>
            					<div class="signature-style-name">方形（边框）</div>
            				</li>
            			</ul>
            		</div>
            		<!-- <a-divider /> -->
            		<div style="display: flex;">
            			<div>
            				<div class="wordart-content-title">
            					选择签名内容
            				</div>
            				<a-radio-group  v-model:value="fromData.addContext" @change="addContextChange">
            					<a-radio :value="1">姓名</a-radio>
            					<a-radio :value="2">姓名+印</a-radio>
            					<a-radio :value="3">姓名+之印</a-radio>
            				</a-radio-group>
            			</div>
            			<div class="signature-preview">
            				<div class="wordart-content-title">
            					签名预览
            				</div>
            				<div class="signature-preview-img">
            					<div>
            						<!-- <img v-if="visibleData.sealStyle == 1" src="@/assets/images/seal/personal-rect.svg"/>
            							<img v-if="visibleData.sealStyle == 2" src="@/assets/images/seal/personal-rect-border.svg"/>
            							<img v-if="visibleData.sealStyle == 3" src="@/assets/images/seal/personal-xy.svg"/>
            							<img v-if="visibleData.sealStyle == 4" src="@/assets/images/seal/personal-xy-border.svg"/>
            						 -->
            						<img :src="fromData.previewImg"/>
                        <!-- <SvgIcon name="personal-xy-border" size="100"></SvgIcon> -->
            					</div>
            				</div>
            			</div>
            		</div>
            	</a-tab-pane>
            	<a-tab-pane :key="2" tab="手绘签名" force-render>
            		<div style="padding-bottom: 20px;">
            			<div class="signature-box" :class="[!startSignature?'signature-bg':'']">
            				<VueSignaturePad ref="signaturePad" :images="[{ src: 'A.png', x: 0, y: 0 }, 
                    { src: 'B.png', x: 0, y: 10 }, { src: 'C.png', x: 0, y: 20 }]" :options="options" />
            			</div>
            		</div>
            	</a-tab-pane>
              
              <a-tab-pane :key="3" tab="扫描签名" force-render>
              	<div class="signature-qr" v-if="!QRsignatureBase64">
              	  <div class="qr-area">
              	    <img :src="signatureQC" alt="QR Code" v-if="signatureQC">
              	    <div class="QR-mask" v-if="!isQRValid">
              	      <!-- <span class="refresh-qr" @click="handleRefresh"><Icon icon="ant-design:undo-outlined"></Icon>二维码已失效点击刷新</span> -->
              	    </div>
              	    <div class="QR-success" v-if="QRSuccess">
              	      <span class="qr-tip"><Icon icon="ant-design:check-circle-filled" color="#25dd13" size="28"></Icon><p>扫码成功</p></span>
              	    </div>
              	  </div>
              	  <p class="qr-time">该二维码有效期为5分钟</p>
              	 
              	</div>
              	<div v-if="QRsignatureBase64" class="QR-base64">
              	  <img :src="QRsignatureBase64" alt="QR Code" >
              	</div>
              	<div class="qr-actions" v-if="QRsignatureBase64">
              	  <!-- <a-button type="primary"  style="margin-left:20px" @click="reQRSignature">重新签名</a-button>
              	  <a-button type="primary"  style="margin-left:20px" @click="save">保存</a-button> -->
              	</div>
              </a-tab-pane>
            </a-tabs>
         </div>
         <template #footer>
         	<div>
         		<div style="display: inline-block;width: 400px;text-align: left;"
         			v-if="fromData.activeKey === 2">
         			<a-button type="link" @click="undoSignature">撤销</a-button>
         			<a-button type="link" @click="clearSignature">重写</a-button>
         		</div>
         		<a-button @click="closeModal">取消</a-button>
         		<a-button type="primary" @click="handleSubmit">确定</a-button>
         	</div>
         </template>
         
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  
  import { defineComponent,ref,unref,onMounted } from 'vue';
  import { BasicTree,TreeItem } from '/@/components/Tree/index';
  import { BasicTable, useTable} from '/@/components/Table';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage'; 
  import { message} from 'ant-design-vue'; 
  import { useUserStore } from '/@/store/modules/user';
  
  import { SvgIcon } from '/@/components/Icon';
  // import { searchFormSchema,tableColumns, } from '/@/views/auth/data';
  import { VueSignaturePad } from 'vue-signature-pad';
  import {signGenerate,signBase64,signSave} from "../api"
  import { getImgBase64} from '/@/api/sys/upload'; 
  
  import { getSignKey} from '/@/api/contract';
  import QRCode from 'qrcode';
  import { useWebSocketService } from '@/utils/socket.ts';
    
  
  export default defineComponent({
    name: 'AddSignatureModal',
    components:{
      BasicModal,SvgIcon,
      BasicTree,VueSignaturePad,
      BasicTable
    },
    setup(_,{emit}){
      const isUpdate = ref(true);
      const recordId = ref('');
      const { createMessage: msg } = useMessage();
      const getTitle = ref('新增签名');
      const startSignature = ref(false);
      const fromData = ref({
        sealName:"",
        activeKey:1,
        sealType:1,
        addContext:1,
        sealStyle:1,
        sealStyleTem:4,
        previewImg:"",
        annexId:""
      });
      
      const userStore = useUserStore();
      const userInfo =  userStore.getUserInfo;
      
      const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:800,
          //cancelText:'关闭' ,
          centered:true,
        });
        isUpdate.value = !!data?.isUpdate;
        fromData.value.sealName = "";
        fromData.value.activeKey = 1;
        fromData.value.annexId = "";
        fromData.value.previewImg = "";
        fromData.value.sealType = 1;
        fromData.value.addContext = 1;
        fromData.value.sealStyle = 1;
        fromData.value.sealStyleTem = 4;
        signatureStyleSelect(1);
        clearSignature();
        // recordId.value = data.record.recordId;
        //fetch()
      });
      const signatureFormRef = ref();
      const signaturePad:any = ref();
      const options = ref({
          penColor: "#000",
          dotSize: (1 + 4) / 2,
          minWidth: 1,
          maxWidth: 4,
          throttle: 16,
          minDistance: 5,
          backgroundColor: 'rgba(0,0,0,0)',
          velocityFilterWeight: 0.5,
          onBegin: () => {
      		startSignature.value = true;
          },
          onEnd: () => {
          }
      });
      onMounted(() => {
          signatureStyleSelect(1);
      });
      const timer = ref<any>();
      async function tabChange(activeKey:number){
      	if(activeKey == 2){
      		setTimeout(function(){
            signaturePad.value.resizeCanvas();
      		},100)
      	}else if(activeKey == 3){
          let result = await getSignKey({})
          if(result){
            isQRValid.value = true;
            QRSuccess.value = false;
            QRsignatureBase64.value = '';
            isQRSignature.value = false;
            QRKey.value = result;
            //scan_code_service
            
            signatureUrl.value = window.appInfo.scan_code_service.url+'?key=' + result + '&userId=' + userInfo.id;
            generateQRCode(signatureUrl.value)
            connectSocket();
            timer.value = setTimeout(()=>{
              isQRValid.value = false;
              QRSuccess.value = false
              socket.value.close();
            },5*60*1000)
          }
        }
        activeKey !== 3?clearSocket():null;
      }

      function clearSocket(){
        socket.value.close();
        timer.value?clearTimeout(timer.value):null
      }
      async function signatureStyleSelect(index:number){
      	fromData.value.sealStyle = index;
      	//fromData.value.sealStyleTem = 4;
      	await previewSignature();
      }
      async function annexEcho(annexId){
        const result = await getImgBase64({imgId:annexId})
        fromData.value.previewImg = result.image;
        fromData.value.annexId = annexId;
        //sealPreview.value = result.image;
        //sealFrom.value.annexId = annexId
      }
      async function previewSignature(){
        const data = {
          addContext:fromData.value.addContext,
          border:2, //是否有边框,1为有边框，2为无边框
          color:1, //颜色,1红色，2蓝色，3黑色
          shapeStyle:fromData.value.sealStyle,//签章形状，1为正方形，2为长方形
        }
        const response = await signGenerate(data);
        await annexEcho(response);
        // doPost("/sign/user/seal/manage/generatePrivateSeal",data).then((res:any)=>{
        //   visibleData.value.fileId = res.result.fileId;
        //   getImgBase64(visibleData.value.fileId).then((img:any)=>{
        //     visibleData.value.previewImg = img.result;
        //   })
        // })
       }
       
      async function handleSubmit(){
        setModalProps({
          confirmLoading: true,
        });
        try {
          // spinning.value = true;
          const values = await signatureFormRef.value.validateFields();
          let params = {
            annexId:fromData.value.annexId,
            sealName: fromData.value.sealName,
          }
          if(fromData.value.activeKey == 2){
            const { isEmpty, data } = signaturePad.value.saveSignature();
            if(isEmpty){
            	message.warning("签名为空，请书写后再进行添加")
            	return;
            }
            const base64 = data.split(",");
            const result = await signBase64({image:base64[1]});
            params.annexId = result;
          }
          if(fromData.value.activeKey == 3){
            if(!QRsignatureBase64.value){
              message.warning("签名为空，扫码签名后再进行添加")
              return;
            }
            
            const base64 = QRsignatureBase64.value.split(",");
            const result = await signBase64({image:base64[1]});
            params.annexId = result;
          }
          const response = await signSave(params);
          //const res = (await addSeal(sealFrom.value)) as any;
         if(response.code == 200){
            message.success("新增签名成功！");
             // spinning.value = false;
            // router.push("/seals/manage")
            closeModal();
            emit("success");
          }
        } catch (errorInfo) {
          console.log('Failed:', errorInfo);
          // message.warning("有必填参数未填");
        }
        
      }
      
      function clearSignature(){
        if(signaturePad.value){
          signaturePad.value.clearSignature();
          startSignature.value = false;
        }
      }
      function undoSignature(){
      	signaturePad.value.undoSignature();
      }
      async function addContextChange(){
        await previewSignature();
      }
      previewSignature();
      
      
      //二维码签名
      const signatureQC = ref('');
      const isQRValid = ref(true);
      const QRSuccess = ref(false);
      const isQRSignature = ref(false);
      const QRsignatureBase64 = ref('');
      const QRKey = ref('');
      const signatureUrl = ref('');
      const socket = ref();
      
      async function generateQRCode (qrCodeData){
        try{
          const qrCodeDataURL = await QRCode.toDataURL(qrCodeData);
          signatureQC.value = qrCodeDataURL;
        // 可以在这里执行一些额外的逻辑，比如保存图片、显示提示等
          } catch (error) {
            console.error('Error generating QR code:', error);
          }
      }
      
      function connectSocket(){
        const websocketInfo =  {
            url:location.origin
        }
        let socketUrl = websocketInfo.url.replace('https','wss').replace('http','ws') + '/resrun-paas/websocket/'+ userInfo.id + QRKey.value
        socket.value = useWebSocketService({url:socketUrl})
        socket.value.setMessageCallback((info)=>{
          console.log(info,'接收到了推送校验信息')
          if(info.type=='check' && info.value=='success'){
            QRSuccess.value = true
          }
          if(info.type=='signature' && info.value){
            QRsignatureBase64.value = 'data:image/png;base64,' + info.value;
            // socket.value.close();
            clearSocket();
          }
        })
      }
      function handleRefresh(){
        tabChange(3)
      }
      
      return {
        registerModal,tabChange,signatureStyleSelect,
        handleSubmit,fromData,startSignature,signatureFormRef,
        getTitle,closeModal,signaturePad,options,
        clearSignature,undoSignature,addContextChange,
        QRsignatureBase64,signatureQC,isQRValid,handleRefresh,QRSuccess
      }
    },
  })
</script>
<style lang="less">
  .input-width{
    // width:200px;
  }
  .signature-dialog-item{
  	width: 100%;
  	padding-top: 10px;
  	.title{
  		line-height: 40px;
  		padding: 0 10px;
  		position: relative;
  	}
  	.title:after{
  		content: "";
  		position: absolute;
  		width: 2px;
  		height: 14px;
  		background-color: #1890ff;
  		top:13px;
  		left:0px;
  	}
  	.body{
  		padding: 0 30px;
  	}
  }
  .wordart-content{
  	ul{width: 100%;
  		display: flex;
  	}
  	ul li{
  		margin-left: 10px;
  		cursor: pointer;
  		border-color: #ededed;
  	}
  	ul li:first-child{
  		margin-left: 0px;
  	}
  	ul li:hover,ul li.hover{
  		border-color: #1890ff;
  		color: #1890ff;
  	}
  .signature-style{
  	display: flex;
  	width:116px;
  	height: 116px;
  	padding: 8px 6px;
  	justify-items: center;
  	align-items: center;
  	border: 1px solid;
  	border-color: inherit;
  	border-radius: 4px;
  }
  .signature-style img{
  	user-select: none;
  }
  .signature-style-name{
  	width:116px;
  	text-align: center;
  	line-height: 30px;
  	color: inherit;
  }
  }
  .signature-box{
  	width:100%;
  	height: 360px;
  	border:1px solid #dedede;
  }
  .signature-bg{
  	background: url('/@/assets/images/signature-bg.png') no-repeat;
  	background-size: 70% 70%;
  	background-position: 50% 50%;
  }
  .signature-preview img{
  	border: 1px solid #ededed;
  	padding: 8px;
  	max-width: 150px;
  }
  
  .signature-qr{
    display: flex;
    flex-direction: column;
    align-items: center;
   
    .qr-area{
      width: 200px;
      height: 200px;
      margin: 0 auto;
      position: relative;
      img{
        width:100%;
        height: 100%;
      }
      .QR-mask{
        position: absolute;
        left:0;right: 0;
        top:0;
        bottom:0;
        background-color: rgba(64, 64, 64, 0.8);
        display: flex;
        align-items: center;
        justify-content: center;
        .refresh-qr{
          background: #fff;
          width: 80%;
          margin: 0 auto;
          display: inline-block;
          text-align: center;
          cursor: pointer;
          font-weight: 550;
        }
      }
      .QR-success{
        position: absolute;
        left:0;right: 0;
        top:0;
        bottom:0;
        background-color: rgba(0, 0, 0, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        .qr-tip{
          text-align: center;
        }
        p{
          color:#fff;
          font-size: 14px;
        }
      }
  
    }
    p{
      font-size: 12px;
      color:#999;
    }
   
  }
  .QR-base64{
    width: 100%;
    border: 1px solid #dedede;
    text-align: center;
    margin: 0 auto;
  }
  
</style>

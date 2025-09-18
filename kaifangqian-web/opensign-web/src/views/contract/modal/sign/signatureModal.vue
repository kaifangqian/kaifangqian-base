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
  <div class="signature">
       <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true" @cancel="handleClose">

          <a-tabs v-model:activeKey="activeKey" @change="handleTabChange">
            <a-tab-pane :key="1" tab="常用签名">
                <ul class="signer-list" v-if="signerList.length">
                    <li v-for="(item,index) in signerList" >
                        <div :key="index" class="title-img">   
                            <span class="signer-title">{{ item.sealName }}</span>
                            <img  v-if="item.base64" :src="item.base64" class="signature-img"/>
                        </div>
                        <a-button  type="link" @click="handleUseSign(item)">使用</a-button>
                    </li>
                </ul>
                <div v-else>
                    <p class="no-data">
                        <img src="~@/assets/images/no-data.png" alt="">
                    </p>
                  </div>
            </a-tab-pane>
            <a-tab-pane :key="2" tab="手绘签名" forceRender>
              <div class="signature-qr" v-if="!QRsignatureBase64"> 
                <div class="qr-area">
                  <img :src="signatureQC" alt="QR Code" v-if="signatureQC">
                  <div class="QR-mask" v-if="!isQRValid">
                    <span class="refresh-qr" @click="handleRefresh"><Icon icon="ant-design:undo-outlined"></Icon>二维码已失效点击刷新</span>
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
                <a-button type="primary"  style="margin-left:20px" @click="reQRSignature">重新签名</a-button>
                <a-button type="primary"  style="margin-left:20px" @click="save">保存</a-button>
              </div>
              <!-- <qrcode-vue :value="signatureUrl" :size="100" level="H" v-if="signatureUrl"/> -->
                <!-- <div class="signature-pad">
                    <VueSignaturePad
                        width="750px"
                        height="250px"
                        ref="signaturePad"
                        :options="options"
                    />
                </div>
                <div class="signature-footer">
                    <footer>
                        <div class="gtnGroup">
                            <a-button type="primary"  @click="undo">撤销</a-button>
                            <a-button type="primary"  style="margin-left:20px" @click="clear">清屏</a-button>
                        </div>
                        <div class="otherSet">
                            <div class="penTxt">笔刷大小：</div>
                            <div class="circleWrap" :class="{ active: isActive1 }" @click="selSize(1)"><b class="b1"></b></div>
                            <div class="circleWrap" :class="{ active: isActive2 }" @click="selSize(2)"><b class="b2"></b></div>
                            <div class="circleWrap" :class="{ active: isActive3 }" @click="selSize(3)"><b class="b3"></b></div> 
                        </div>
                        
                    </footer>
                </div> -->
                
            </a-tab-pane>
           
        </a-tabs>
       
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed } from 'vue'
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Icon } from '/@/components/Icon';
  import { getSignatureList, getImgBase64ById, getSignKey, getCheckSignKey, submitSignature} from '/@/api/contract';
  import { VueSignaturePad } from 'vue-signature-pad';
  import QrcodeVue from 'qrcode.vue'
  import { useWebSocketService } from '@/utils/socket.ts';
  import { useUserStore } from '/@/store/modules/user';

  import QRCode from 'qrcode';




  export default defineComponent({
    name: 'SignatureModal',
    components:{
      BasicModal,
      VueSignaturePad,
      QrcodeVue,
      Icon
    },
    setup(_, { emit }){

      const isUpdate = ref(true);
      const rowId = ref('');
      const showPad = ref(false);
      const isActive1 = ref(true);
      const isActive2 = ref(false);
      const isActive3 = ref(false);
      const activeKey = ref(1);
      const signerList:any = ref([]);
      const signaturePad = ref();
      const signatureUrl = ref('');
      const signatureQC = ref('');
      const isQRValid = ref(true);
      const QRSuccess = ref(false);
      const isQRSignature = ref(false);
      const QRsignatureBase64 = ref('');
      const QRKey = ref('');
      const socket = ref();
      const options = ref({
          penColor: "#000",
          dotSize: 1,
          minWidth: 1,
          maxWidth: 4,
          throttle: 16,
          minDistance: 5,
          backgroundColor: 'rgba(0,0,0,0)',
          velocityFilterWeight: 0.5,
          onBegin: () => {
          },
          onEnd: () => {
          }
      });

      const userStore = useUserStore();
      const userInfo =  userStore.getUserInfo;

      async function generateQRCode (qrCodeData){
        try{
          const qrCodeDataURL = await QRCode.toDataURL(qrCodeData);
          signatureQC.value = qrCodeDataURL;
        // 可以在这里执行一些额外的逻辑，比如保存图片、显示提示等
          } catch (error) {
            console.error('Error generating QR code:', error);
          }
      }
      function  selSize(val){
            options.value = {
              penColor: "#000",
              dotSize: 1,
              minWidth: 1 * val,
              maxWidth: 4 * val,
              throttle: 16,
              minDistance: 5,
              backgroundColor: 'rgba(0,0,0,0)',
              velocityFilterWeight: 0.5,
              onBegin: () => {
              },
              onEnd: () => {
              }
            }
            if(val==1){
                isActive1.value=true;
                isActive2.value=false;
                isActive3.value=false;
            }else if(val==2){
                isActive1.value=false;
                isActive2.value=true;
                isActive3.value=false;
            }else if(val==3){
                isActive1.value=false;
                isActive2.value=false;
                isActive3.value=true;
            }
            signaturePad.value.openSignaturePad();
        }

        //撤销
        function undo(){
            signaturePad.value.undoSignature();
        }
        //清除
        function clear(){
          signaturePad.value.clearSignature();
        }
        //保存
        function save(){
            // const { isEmpty, data } = signaturePad.value.saveSignature();
            // this.imgSrc = data;
            if(!QRsignatureBase64.value){
              msg.warning('请扫码签名')
              return 
            }
            socket.value.close();
            emit('base64Save',QRsignatureBase64.value)
            closeModal()
        }

      const checkedKeys = ref<Array<string | number>>([]);
     
      const { createMessage: msg } = useMessage();
     

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:800,
          cancelText:'关闭',
          showOkBtn:false,
          showCancelBtn:false,
          // getContainer: () => document.body.querySelector(`.signature`) || document.body, 
        });
        rowId.value = data.record?.sealId;
        signatureUrl.value = '';
        activeKey.value = 1;
        setTimeout(()=>{
          showPad.value = true;
        })
        let result = await getSignatureList({});
        console.log("signerList.value",result);
        if(result && result.length>0){
          signerList.value = result;
          
          signerList.value.map(item=>{
            item.base64 = '';
            getImgBase64ById({id:item.annexId}).then(result=>{
                if(result){
                  item.base64 = result.image;
                }
            })
          });
        }else{
          handleTabChange(2)
          activeKey.value = 2;
        }
      });
     

      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      
      const getTitle = computed(() => (!unref(isUpdate) ? '使用签名' : '使用签名'));

      function handleUseSign(row){
        emit('signature',row)
      }

      async function handleSubmit() {
        try {
          
            msg.success('保存成功');
            closeModal();
            emit('success', { });
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      async function handleTabChange(val){
        if(val==2){
          let result = await getSignKey({})
          if(result){
            isQRValid.value = true;
            QRSuccess.value = false;
            QRsignatureBase64.value = '';
            isQRSignature.value = false;
            QRKey.value = result;
            signatureUrl.value = window.appInfo.scan_code_service.url+'?key=' + result + '&userId=' + userInfo.id;
            generateQRCode(signatureUrl.value)
            connectSocket();
            setTimeout(()=>{
              isQRValid.value = false;
              QRSuccess.value = false
              socket.value.close();
            },5*60*1000)
          }
        }else{
          QRsignatureBase64.value = '';
          isQRValid.value = true;
          socket.value.close();
        }
      }
      function handleClose(){
        socket.value&&socket.value.close();
      }

      function connectSocket(){
        const websocketInfo =  {
            url:location.origin
        }
        if(!userInfo || !userInfo.id){
          msg.warning("请刷新页面后再进行扫码");
          return;
        }
        let socketUrl = websocketInfo.url.replace("https://","wss://").replace("http://","ws://") + '/resrun-paas/websocket/'+ userInfo.id + QRKey.value
        socket.value = useWebSocketService({url:socketUrl})
        socket.value.setMessageCallback((info)=>{
          console.log(info,'接收到了推送校验信息')
          if(info.type=='check' && info.value=='success'){
            QRSuccess.value = true
          }
          if(info.type=='signature' && info.value){
            QRsignatureBase64.value = 'data:image/png;base64,' + info.value;
            socket.value.close();
          }
        })
      }

      function handleRefresh(){
        handleTabChange(2)
      }
      function reQRSignature(){
        handleTabChange(2)
      }

      return { 
        registerModal, 
        getTitle, 
        handleSubmit,
        onSelectChange,
        checkedKeys,
        activeKey,
        signerList,
        options,
        handleUseSign,
        signaturePad,
        undo,
        clear,
        save,
        selSize,
        isActive1,
        isActive2,
        isActive3,showPad,
        signatureUrl,
        handleTabChange,
        signatureQC,
        isQRValid,
        handleRefresh,
        QRSuccess,QRsignatureBase64,
        reQRSignature,
        handleClose
      };
    }
  })
</script>
<style lang="less" scoped>
 .signature-pad{
      border:1px dashed #fafafa;
      canvas{
          border:1px dashed #999;
      }
  }
  .signature{
    canvas{
      border:1px dashed #999;
    }
  }
  .QR-base64{
    width: 70%;
    // height: 190px;
    border: 1px solid #999;
    text-align: center;
    margin:0 auto;
  }
  .qr-actions{
    margin-top:10px;
    text-align: center;
  }
  .qr-time{
    margin-top:10px;
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
        background-color: rgba(64, 64, 64, 0.9);
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
.signer-list{
        padding:20px 25px;
        padding-bottom:40px;
        min-height:280px;
        li{
            display:flex;
            justify-content:space-between;
            align-items: center;
            margin:10px 0;
            .title-img{
                display: flex;
                align-items: center;
                .signer-title{
                    width: 80px;
                }
                img{
                    width:100px;
                }
            }
            // .signature-img{

            // }
        }
    }
   
    .signature-footer{
        footer{
            height:50px;
            // border:1px solid #999;
            display:flex;
            justify-content: space-between;
            align-items: center;
            border-top:none;
            padding:0 10px;
        }
       
        .gtnGroup{
            width:50%;
            // margin-left: 20px;
        }
        .otherSet{
            width:50%;
            display:flex;
            align-items: center;
            .penTxt{
                width:70px;
            }
            .selSize{
                width:70px;
            }
            .a-select__caret{
                position: absolute;
                right: -3px;
            }
            .b1,.b2,.b3{
                display: inline-block;
                background: #000;
                border-radius: 50%;
            }
            .circleWrap{
                display: flex;
                justify-content: center;
                align-items: center;
                width:18px;
                height:18px;
                cursor:pointer;
                margin-right:20px;
            }
            .active{ border:1px dashed #0074d9; }
            .b1{width:4px;height:4px}
            .b2{width:6px;height:6px}
            .b3{width:8px;height:8px}
        }
    }
  
</style>

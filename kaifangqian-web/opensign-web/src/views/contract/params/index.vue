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
  <div class="position-container"  :style="'height: '+ (height-63) +'px'">
    <Loading :loading="loading || signLoading" :absolute="false"/>
    <DocHeader :actions="actions" :docs="docs" :docId="docId" @change="handleDocChange"  @cancel="handlePosCancel">
    </DocHeader>
      <div class="position-params-body">
        <DocLeft :title="'填写状态'">
            <WriteStatus  :signerList="signerList" />
        </DocLeft>
          <DocContent :nowDocument="nowDocument" :showControl="true" @controlDelete="controlDelete"  @controlFocus="controlFocus"
            @controlMousedown="controlMousedown" :spinning="spinning">
          </DocContent>
        <DocRight title="" >
          <FullParams  :controlList="controlList" :isDetail="isDetail" :signerList="signerList" :documentList="documentList" :docId="docId" @scroll="handleScroll">
          </FullParams>
        </DocRight> 
    </div>
    <a-modal v-model:visible="taskVisible" title="任务提醒" :closable="false" :maskClosable="false">
          <p>监测到该签约文档中您还有【{{ taskInfo.taskType == 'sign' ? '签署 ' : '填写' }}】任务，是否前去处理？</p>
          <template #footer>
                <a-button type="default" @click="handleNoTask">暂不处理</a-button>
                <a-button type="primary" @click="handleGoNextTask">立即处理</a-button>
          </template>
      </a-modal>
      <WishModal @register="registerWish" @success="handleConfirmSuccess"></WishModal>
  </div>
</template>

<script lang="ts">
import {ref, unref, defineComponent, onMounted, reactive, toRefs, nextTick, createVNode} from "vue";
import { Icon } from '/@/components/Icon';
import { useRouter } from 'vue-router';
import { Modal, Input, Button } from 'ant-design-vue'; 
import DocHeader from '../components/layouts/DocHeader.vue';
import DocLeft from '../components/layouts/DocLeft.vue';
import DocContent from '../components/layouts/DocContent.vue';
import DocRight from '../components/layouts/DocRight.vue';
import Controls from '../components/Controls.vue';
import FullParams from '../components/FullParams.vue';
import WriteStatus from '../components/WriteStatus.vue';
import { Loading } from '/@/components/Loading';
import { getColtrolByType } from '../components/data/ControlData';
import WishModal from '/@/views/contract/modal/wish/WishModal.vue';
import { useModal } from '/@/components/Modal';
import { useMessage } from '/@/hooks/web/useMessage';
import { getControlList, getDocFiles, checkOperatorStatus ,
   getSigners, saveWrite, rejectWrite, submitWrite, getConfirmNo, 
   startContract, getSignControlType, getAnnexImgs, 
   getCallbackPage, getConfirmType } from '/@/api/contract/index';
import { cloneDeep } from 'lodash-es';
// import { CanvasZoom } from '/@/views/signature/doc/control/src/data/ControlData';
import { CanvasZoom } from '/@/views/contract/components/data/ControlData';
import {currentPosition,recaculateBatchControlPosInPage, currentPositionReverse, pageScaling, paramBuildWidgets } from "/@/views/contract/components/data/ControlerMoveRange"
import { createLocalStorage } from '/@/utils/cache';
import { getHashQueryString, retainDecimals,decodeURIs } from '/@/utils';

interface BtnFun {
 	 ():void   
}

interface ButtonItem {
  type:string;
  text:string;
  disabled?:boolean;
  show?:boolean;
  callBack:BtnFun
}

export default defineComponent({
  name:"PositionParams",
  components:{
    Icon,
    DocHeader,
    WishModal,
    DocLeft,
    DocContent,
    DocRight,
    Controls,
    FullParams,
    WriteStatus,
    Loading,
    Modal, Input, Button
  },
  setup() {

      const ls = createLocalStorage();
      const docId = ref('');
      const annexId = ref('');
      const docs:any = ref([]); 
      const spinning = ref(false);
      const compState = reactive({
        absolute: false,
        loading: false,
        tip:''
      })
      const willResult = ref(false);
      const signLoading = ref(false);
      const confirmAction = ref('write');
      const orderNo = ref('');
          
      const controlList = ref(<any[]>[])
      const signers:any = ref({})
      const documentList:any = ref([])
      const nowDocument:any = ref({});
      const signerList:any = ref([]);
      const controlsList:any = ref([]);
      const images:any = ref([])
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      
      const signRuId = route.query.signRuId;
      const callbackPage = getHashQueryString('callbackPage');
      const partyName = getHashQueryString('partyName');
      const taskId = route.query?.taskId;
      const from = route.query.from;
      // const pageSource = route.query.from;
      const isDetail = ref(false);
      ls.set('Sign-Task-Id',taskId)

      const confirmTypeInfo:any = ref({
        confirmType:'face',
        personalAccountStatus:'',
      })

      const deleteIdList = ref(<string[]>[]);
      const signType = route.query.type;

      const taskVisible = ref(false)
      const taskInfo:any = ref({})

      const { createMessage: msg, createSuccessModal,createConfirm } = useMessage();

      const [registerWish, { openModal:openWishModal,closeModal:closeWishModal }] = useModal();
    

      onMounted(()=>{
        //TODO  若果删除form参数 回跳过
        console.log(from &&from=='list');
        if(from &&from=='list'){
          checkStatus()
        }
        initData()
        if(taskId){
          generateOrderNo()
        }
      })

      async function generateOrderNo(){
          let result = await getConfirmNo({mainId:taskId})
          if(result){
            orderNo.value = result;
          }
      }

      function getConfirmTypeForAction(params){
          return new Promise(async (resolve,reject)=>{
            let result = await getConfirmType(params)
            if(result){
                resolve(result)
            }else{
                reject(result)
            }
          })
        }
       //根据状态和当前操作人是否进行过签署进行路由重定向到签署详情或者文档详情页
     async  function checkStatus(){
        let result = await checkOperatorStatus({signRuId:signRuId});
        if(result){
          if(result.checkStatus !=1 ){
            router.replace({
                name:'ContractNoauth',
                query:{
                  signRuId,
                  taskId,
                  taskType:'write',
                  partyName:partyName,
                  callbackPage:callbackPage
                }
              })
            return 
          }
          if(result.ruStatus == 6 || result.ruStatus == 8 ||  result.ruStatus == 9 || result.ruStatus == 10 || result.ruStatus == 11){
              router.replace({
                name:'签约详情',
                query:{
                  ...route.query,
                }
              })
              return
          }
          //增加 result.ruStatus !=1 用于判断发起前x
          if(result.operatorStatus==2 &&  result.ruStatus !=1){
            router.replace({  
              
              query:{
                ...route.query,
                isDetail:'true'
              }
            })
          }
          setTimeout(()=>{
            isDetail.value =  route.query?.isDetail=='true'? true : false;
            if(unref(isDetail)){
              actions.value = [];
            }
          })
        }   
      }

      function initWindow() {
          var h = document.documentElement.clientHeight || document.body.clientHeight;
      	return h;
      }
      const height = ref(initWindow());
      
      const actions = ref(<ButtonItem[]>[
        {
          type:'default',
          text:'保存',
          callBack:handleSave,
          show:true
        },
        {
          type:'primary',
          text:'发起',
          callBack:handleStart,
          show:true
        },
      ])
      if(signType=='receive'){
        actions.value = [
          {
            type:'default',
            text:'拒填',
            callBack:handleRejectWrite,
            show:true
          },
          {
            type:'primary',
            text:'提交',
            callBack:handleSubmitWrite,
            show:true
          },
        ]
      }
      async function initData(){
        documentList.value = [];
        getDocs();
        getSignerList();
        // loadSignControlType()
      }
      async function loadSignControlType(){
        let result = await getSignControlType({ruId:signRuId});
        if(result){
          
        }
      }
      //获取文档
      async function getDocs(){
        try{
          spinning.value = true;
          let result = await getDocFiles({signRuId:signRuId})
          if(result){
            controlList.value = [];
            docs.value  = result;
            docId.value = result[0].id;
            annexId.value = result[0].annexId;
            docs.value.map(async(v,i)=>{
                v.pageSize = v.docPage;
                let result = await getAnnexImgs({ annexId:  v.annexId });
                if(result){
                  v.images = result.sort((a, b) => a.page - b.page);
                  if(i==0){
                    images.value = result;
                  }
                  getDocControl(v,i)
                }
            })
          }
        }catch(erroe){
          spinning.value = false;
        }
       
      }
      //获取图片列表
      async function getDocImages(){
        let result = await getAnnexImgs({annexId:annexId.value});
        if(result){
          images.value = result;
          for(let i = 0; i < docs.value.length;i++){
            getDocControl(docs.value[i],i)
          }

          setTimeout(()=>{
            documentList.value.sort((a,b)=>{
              return a.docOrder - b.docOrder;
            })
          })

        
          // docs.value.map(item=>{
          //   getDocControl(item)
          // })
        }
        
      }
      //获取参与方
      async function getSignerList(){
        let result = await getSigners({signRuId:signRuId});
        if(result){
          signerList.value = result;
        }
        
      }
      //根据文档id获取对应文档的控件
      async function getDocControl(doc,docindex){
        let result =  await getControlList({signRuId:signRuId,controlTypeList:'date,line-text,multiline-text,number'});
        let currentDocControl =  result.controlList;
        currentDocControl = currentDocControl.filter(item=>item.signRuDocId == doc.id && (item.controlType == 'date' || item.controlType == 'line-text' || item.controlType == 'multiline-text' || item.controlType == 'number'  || item.controlType == 'radio'  || item.controlType == 'check-box' || item.controlType == 'dropdown-list' || item.controlType == 'image')) 
        if(currentDocControl.length){
          if(docindex==0){
            currentDocControl.map((v,colIndex)=>{
              v.posControlOrder = colIndex;
            })
          }
          if(docindex>0){
            currentDocControl.map((v,colIndex)=>{
              v.posControlOrder = controlList.value.length + colIndex;
            })
          }
          controlList.value = controlList.value.concat(currentDocControl);
        }else{

        }
      
        setDocumentList(doc,currentDocControl);
        spinning.value = false;
      }
        //文档切换
      async function handleDocChange(val){

        let matchDoc = documentList.value.find(item=>item.signRuDocId ==  val);
              nowDocument.value = matchDoc;
              docId.value = val;
              images.value = matchDoc.images;
              document.getElementsByClassName('doc-content')[0].scrollTop = 0;

        // docId.value = val;
        // images.value = [];
        // annexId.value = docs.value.filter(v=>v.id == docId.value)[0].annexId;
        // let result = await getAnnexImgs({annexId:annexId.value});
        // if(result){
        //   let matchDoc = documentList.value.find(item=>item.signRuDocId ==  docId.value);
        //   matchDoc.images = result;
        //   //更新文档图片数量用于重新计算拖拽范围
        //   matchDoc.pageSize = result.length;
        //   nowDocument.value = matchDoc;
        //   nowDocument.value.activeControl.map((item)=>{
        //   item.hasLoad = true;
        // })
        //   document.getElementsByClassName('doc-content')[0].scrollTop = 0;
        //   // console.log(documentList,docId.value,  matchDoc,'匹配文档')
        // }
      }

      //整理文档列表
      function setDocumentList(doc,controls){
        // console.log(doc, controls,'当前文档控价')
        const {targets,maxWidth} = pageScaling(doc.images);
        documentList.value.push({
          active:false,
          documentName: doc.name,
          annexFileId:"",
          targets:targets,
          maxWidth:maxWidth,
          docOrder:doc.docOrder,
          signRuDocId:doc.id,
          activeControl:cloneDeep(buildResponseControl(controls)),
          pageSize:images.value.length,
          imageLoading:true,
          images: cloneDeep(doc.images),
          imageAllHeight:targets[targets.length-1].transformHeight + targets[targets.length-1].height + (images.value.length * CanvasZoom.space),
        })
        
        nowDocument.value = documentList.value.find(v=>v.signRuDocId == docId.value);
        nowDocument.value?.activeControl.map((item)=>{
          item.hasLoad = true;
        })

        // controlMove.activeControl = buildResponseControl(controls);
        //receiveActive(signers.value);fwidgetList

        console.log(buildResponseControl(controls),'控件展示')
        console.log(nowDocument.value,'控件展示22222222222')
      }

      // 构建文档控件
      function buildResponseControl(controls:any){
      	const controlTem:any = [];
      	if(controls && controls.length>0){
      		controls.forEach((item:any)=>{

            let matchDoc = docs.value.filter(v =>v.id == item.signRuDocId)[0]
            let { targets, maxWidth } = pageScaling(matchDoc.images);
            let target = targets[item.page];
            let offsetWidth = (maxWidth - target.width) / 2;

            if(['image','dropdown-list','radio','check-box'].includes(item.controlType) && item.properties && item.properties !='"[]"'){
              // item.widgetList  = item.properties && (typeof item.properties == 'string')?JSON.parse(item.properties):[];
              item.widgetList  = item.properties;
            }
      			const basaeColtrol = getColtrolByType(item.controlType);
      			controlTem.push({
      				id :parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
      				uid : item.id,
              posControlOrder:item.posControlOrder,
      				icon:basaeColtrol.icon,
      				name:item.name,
              uploadImg:item.uploadImg || '',
              properties:item.properties,
              widgetList:paramBuildWidgets(item.widgetList,target,offsetWidth),
              controlOrder:item.controlOrder,
      				title:basaeColtrol.title,
      				isMineFlag:item.isMineFlag,
      				isRequired:item.isRequired,
              controlType:item.controlType,
      				placeholder:item.placeholder,
      				controlClick:false,
              offsetX:item.offsetX,
              offsetY:item.offsetY,
      				disabled:item.isMineFlag==1?false:true,
      				zoom:false,
      				// move:unref(isDetail)?false:basaeColtrol.move,
      				move:unref(isDetail)?false:false,
      				required:item.isRequired == 1,
      				attr:basaeColtrol.attr,
              signRuDocId:item.signRuDocId,
              signerId:item.signerId,
              value:item.value,
              values:item.value&&item.value.includes(',')? item.value.split(','):[],
              signerType:item.signerType,
      				format:item.format,
              fontSize:parseInt(item.fontSize),
              fontFamily:item.fontFamily,
              textAlign:item.textAlign,
      				style:{
      					fontSize:parseInt(item.fontSize),
      					fontFamily:item.fontFamily,
      					textAlign:item.textAlign,
      				},
      				size:{
      					width:parseInt(item.width) || basaeColtrol.width,
      					height:parseInt(item.height) || basaeColtrol.height,
      					minWidth:basaeColtrol.size.minWidth,
      					minHeight:basaeColtrol.size.minHeight,
      				},
      				position:{
      					left: parseInt(item.offsetX) + offsetWidth,
                top:  currentPositionReverse(item.offsetY, target),
      					page:parseInt(item.page),
      				},
              // position: {
              //     left: ((offsetWidth + item.offsetX) >= ((offsetWidth + target.width) - item.width)?(offsetWidth + target.width) - item.width:offsetWidth + item.offsetX),
              //     top:  currentPositionReverse(item.offsetY, target),
              //     page: item.page,
              // },
              user:{}
      			})
      		})
      	}
      	return controlTem;
      }

      const controlMove:any = reactive({
      	activeControl:[],
      	controlList:[],
      	disabled:false,
      	activeDom:{},
      	elementMove: {
      		id:"",
      		controlClick:false,
      		position:{
      			left:0,
      			top:0
      		},
      		size:{
      			width:0,
      			height:0,
      			minWidth:0,
      			minHeight:0,
      		},
      	},
      	nowPoint:{
      		x:0,
      		y:0
      	},
      });
     async  function handlePosCancel(isHome=false){
        if(callbackPage && typeof callbackPage == 'string'){  
          window.open(decodeURIs(callbackPage),'_self')
          return
        }
        if(window.opener){
          window.close()
        }else{
          if(isHome){
            router.push("/contract/doc");
          }else{
            router.go(-1);
          }
        }
      }
       //刷新控件状态
       function refreshControls(){ 
      	if(signers.value){
      		controlMove.activeControl = nowDocument.value.activeControl.filter((item:any) => {
      			return item.user.index == signers.value.index;
      		})
      	}else{
      		controlMove.activeControl = nowDocument.value.activeControl;
      	}
        controlMove.activeControl = nowDocument.value.activeControl;
      }

      
        //控件拖拽完成事件
        function controlsDragOver(e:any,element:any) {
          controlMove.elementMove = element;
          refreshControls();
          
        }
        //删除控件
        function controlDelete(element:any){
      	element.controlClick = false;
      	console.log("delete control:",element,nowDocument.value.activeControl)
        deleteIdList.value.push(element.id);
      	nowDocument.value.activeControl = nowDocument.value.activeControl.filter((item:any) => {
      		return item.uid !== element.uid 
      	})
      	refreshControls();
      }

       //继续任务处理
       function handleGoNextTask() {
            if (taskInfo.value.taskType == 'sign') {
                router.push({
                    path: '/contract/sign',
                    query: {
                        signRuId: taskInfo.value.ruId,
                        taskId: taskInfo.value.taskId,
                        __full__:'',
                        callbackPage:callbackPage
                    }
                })
                setTimeout(() => {
                    location.reload()
                }, 500)
            }
            if (taskInfo.value.taskType == 'write') {
                router.replace({
                    path: '/contract/params',
                    query: {
                        signRuId: taskInfo.value.ruId,
                        taskId: taskInfo.value.taskId,
                        __full__:''
                    }
                })
                setTimeout(() => {
                    location.reload()
                }, 500)
            }

        }
        //暂不处理业务
        function handleNoTask() {
            taskVisible.value = false;
            setTimeout(() => {
              router.push('/dashboard/workbench')
            }, 1000)
        }
      //控件点击操作
      function controlMousedown(element:any){
        // console.log(element,'点击控件-------')
      	controlMove.elementMove = element;
      	nowDocument.value.activeControl.forEach((item:any)=>{
      		item.controlClick = false
      	})
      	element.controlClick = true;
      }
     
      //文档控件滚动定为
      function handleScroll(doc,el){
        compState.loading = true;
        if(doc.signRuDocId != docId.value){
          handleDocChange(doc.signRuDocId)
          setTimeout(()=>{
            document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
            // document.getElementsByClassName('doc-content')[0].scrollTo(0,el.position.page * 1147);
          },2000)  
        }else{
          // document.getElementsByClassName('doc-content')[0].scrollTo(0,el.position.page * 1147);
          document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
        }
        documentList.value.map(v => {
          v.activeControl.forEach(item => {
              item.controlClick = false;
              item.focus = false;
              if(item.uid == el.uid){
                item.controlClick = true;
                item.focus = true;
              }
          });
        })  
        compState.loading = false;
       
      }
      //整理提交控件
      function formatSubmitControl(){
        let controls =  documentList.value.flatMap(item => item.activeControl);
        console.log(controls,nowDocument.value.activeControl,'整理之前的数据位置')
        let paramsControl:any = [];
        controls.map(item=>{
          const matchDoc = documentList.value.filter(v =>v.signRuDocId == item.signRuDocId)[0];
          const targets = matchDoc.targets;
          const target = targets[item.position.page];
          const offsetWidth = (matchDoc.maxWidth - target.width) /2;
          item.signerType = signerList.value.find(m=>m.id == item.signerId)?.signerType?2:1;
          if(item.controlType == 'date' && item.value){
            item.value = formatDateString(item.value)
          }
          paramsControl.push({
        				"fontFamily": item.style.fontFamily,
        				"fontSize": item.style.fontSize,
        				"height": item.size.height,
        				"name": item.name,
        			  "offsetX": item.position?.left - retainDecimals(offsetWidth),
                "offsetY": currentPosition(item.position.top, target),//计算成为每页的位置
                "pageWidth": target.width,
                "pageHeight": target.height,
        				"page": item.position.page,
        				"placeholder": item.placeholder,
        				"signReDocId": item.signReDocId,
        				"relatedDocId": 0,
                "properties":item.properties,
                "signerId": item.signerId,
        				"signerType": item.signerType,
        				"relatedDocType": 0,
        				"required": item.required?1:2,
        				"interfaceParamName": item.interfaceParamName,
        				"textAlign": item.style.textAlign,
        				"signRuDocId": item.signRuDocId,
        				"controlType": item.controlType,
        				"value": item.values&&item.values.length ? item.values.join(','):item.value,
                // "value": item.controlType =='’' ?'':item.value,
        				"width": item.size.width,
        				"written": 1,
        				"format":item.format,
        				"id":item.uid,
        				"tenantId": item.user.tenantId,
        		})
          })


        return paramsControl;
      }
      function formatDateString(dateStr) {
        // 用正则表达式匹配日期字符串并替换为统一格式
        return dateStr
          .replace(/(\d{4})年(\d{2})月(\d{2})日/, '$1-$2-$3')  // 处理 "2023年01月01日" 格式
          .replace(/(\d{4})\/(\d{2})\/(\d{2})/, '$1-$2-$3');     // 处理 "2023/01/01" 格式
      }
      //保存数据
      function saveFormData(start?:boolean){
        return new Promise(async (resolve, reject) => {
          let paramsControl = formatSubmitControl();
          compState.loading = true;
          let result 
          if(signType=='receive'){
            result   = await submitWrite({signRuId:signRuId,controlList:paramsControl,deleteIdList:unref(deleteIdList)});
          }else{
            result = await saveWrite({signRuId:signRuId,controlList:paramsControl,deleteIdList:unref(deleteIdList)}); 
          }
        
          if(result){
            resolve(result)
            if(!start){
              delayColseLoading();
            }
          }else{
            reject(result)
            delayColseLoading();
          }
        })
      }
      function handleSave(){
        compState.loading = true;
        saveFormData().then(res=>{
            if(res){
              msg.success('保存成功')
            }
        }).catch(e=>{
          compState.loading = false;
        })
      }
      function delayColseLoading(time?:number){
        setTimeout(function(){
          compState.loading = false;
        },time?time:500)
      }
       // 自定义的textarea数据  
      const textareaValue = ref('');  
      function handleInput(e){
          textareaValue.value = e.target.value;
      }
      //拒绝填写
      async function handleRejectWrite(){
        confirmAction.value = 'reject';
        Modal.confirm({
          title: '拒绝填写',
          content: createVNode(Input.TextArea, {
            text: textareaValue.value,
            placeholder:'请填写拒签原因，200字以内',
            maxlength:200,
            onChange: handleInput,
          }),
          onOk() {
            return new Promise((resolve,reject)=>{
              if(!textareaValue.value){
                    msg.warning('请输入原因')
                    reject('请输入原因')
                }else if(textareaValue.value.length < 1 || textareaValue.value.length>200){
                  msg.warning('字数不符合要求')
                  reject('字数不符合要求')
                }else{
                  submitRejectData()
                  resolve('success');
                }
            })
          }
        })
      }
      
      //拒绝填写提交数据
      async function submitRejectData(){
        createConfirm({
              title: '是否确认拒绝填写？', 
              okText:'确认',
              cancelText:"取消",
              iconType: 'warning',
              onOk() {
                compState.loading = true;
                rejectWrite({signRuId:signRuId,signConfirmOrderNo:orderNo.value,comment:textareaValue.value}).then(result=>{
                  if(result){
                    compState.loading = false;
                    createSuccessModal({
                      title: '拒填成功，点击确定关闭此页面', 
                      okText:'确定',
                      iconType: 'success',
                      onOk: async ()=> {
                        handlePosCancel(true);
                      }
                    })
                  }
                })
              },
              onCancel() {
                compState.loading = false;
              }
            })
      }
       //校验成功后继续填写或拒填
       function handleConfirmSuccess(){
        if(confirmAction.value == 'write'){
          willResult.value = true;
          submitWriteData()
        }
        if(confirmAction.value == 'reject'){
          willResult.value = true;
          submitRejectData()
        }

      }
      // 校验填写
      function checkMustWrite(){
        let checkResult = true;
        let allWriteControls = documentList.value.flatMap(item=>item.activeControl).filter(v=>(v.controlType=='date' || v.controlType=='line-text' || v.controlType=='multiline-text' ||  v.controlType=='number'));
        allWriteControls.some(v=>{
          console.log(v,'填写控件', v.name, v.isRequired, v.value)
          if(v.isRequired == 1 && !v.value && v.isMineFlag ==1){
            checkResult = false;
            return false
          }
        })
        return checkResult
      }
      //提交填写
      function handleSubmitWrite(){
        confirmAction.value = 'write';
        //有填写控件未填写不允许提交
        if(!checkMustWrite()){
          msg.warning('有必填参数未填写')
          return
        }
        submitWriteData();
        // getConfirmTypeForAction({operateType:'submit_write'}).then(async(res)=>{
        //     confirmTypeInfo.value = res;
        //     if(!willResult.value ){
        //       openWishModal(true,{
        //         isUpdate:false,
        //         record:{
        //           confirmType:confirmTypeInfo.value.confirmType,
        //           orderNo:orderNo.value
        //         }
        //       })
        //       return
        //     }
        //     submitWriteData()
        //   })
      }

      //填写提交数据
      async function submitWriteData(){
        createConfirm({
            title: '是否确认提交，提交后无法进行修改', 
            okText:'确认',
            cancelText:"取消",
            iconType: 'warning',
            onOk() {
              compState.loading = true;
              let paramsControl = formatSubmitControl();
              submitWrite({signRuId:signRuId,controlList:paramsControl,deleteIdList:[],signConfirmOrderNo:orderNo.value}).then(result=>{
                if(result){
                    if(result.taskId){
                      taskVisible.value = true;
                      taskInfo.value = result;
                    }else{
                      createSuccessModal({
                        title: '填写成功，点击关闭', 
                        okText:'确定',
                        iconType: 'warning',
                        onOk:async()=> {
                          compState.loading = false;
                         
                          handlePosCancel(true);
                        }
                      })
                  }
                }
                compState.loading = false;
              })
            }
          })
      }
      
      //发起
     async function handleStart(){
        createConfirm({
              title: '温馨提示', 
              content: '请确认是否发起签署，发起后不可更改',
              okText:'确定',
              cancelText:'取消',
              iconType: 'warning',
              onOk:async ()=> {
                const res = await saveFormData(true);
                if(res){
                  signLoading.value = true;
                  compState.loading = false;
                  let result = await startContract({signRuId:signRuId});
                  if(result){
                    signLoading.value = false;
                    handlePosCancel(true);
                  }else{
                    signLoading.value = false;
                  }
                }
                delayColseLoading(5000);
              }
            })
      }

      function setControlScroll(index){
          let paramsContainer = document.getElementsByClassName('scrollbar__wrap scrollbar__wrap--hidden-default')[2]
          if(paramsContainer){
            nextTick(()=>{
             document.getElementsByClassName('writer-li')[index].scrollIntoView({ behavior: 'smooth', block: 'start' })
            })
          }
        }

        function controlFocus(element){
          let controls =  documentList.value.flatMap(item => item.activeControl);
          controls.map(el=>{
            el.focus = false;
            el.controlClick = false;
            el.zoom = false;
          })
          element.focus = true;
          element.controlClick = true;
          if(element.posControlOrder){
            setControlScroll(element.posControlOrder)
          }
        }
      return {
        controlFocus,
          docs,
          docId,
          height,
          actions,
          nowDocument,
          documentList,
          handleConfirmSuccess,
          signerList,
          controlsList,
          controlList,
          controlMove,
          controlMousedown,
          controlsDragOver,
          signLoading,
          controlDelete,
          signers,
          registerWish,
          handleDocChange,
          handleScroll,
          spinning,
          signType,
          handlePosCancel,
          isDetail,
          taskVisible,
          handleNoTask,
          handleGoNextTask,
          taskInfo,
          ...toRefs(compState),
      }  
    }
})
</script>

<style lang="less" scoped>
.position-container{
  margin-top:-20px;
}
.position-params-body{
  overflow: hidden;
  height: 100%;
}
.doc-select{
  min-width:100px;
  :deep(.ant-select-selector){
    border:none;
  }
  :deep(.ant-select-selection-item){
    font-size: 18px;
    font-weight: 550;
  }
}
</style>

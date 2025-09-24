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
      <Loading :loading="loading || signLoading" :absolute="false" />
      <DocHeader :actions="actions"  :docs="docs" :docId="docId" @change="handleDocChange" @cancel="handlePosCancel">
      </DocHeader>
        <div class="position-params-body">
          <DocLeft title="设置签署位置">
            <template #icon>
              <a-tooltip>
                <template #title>{{ leftHeadTip }}</template>
                    <Icon icon="ant-design:question-circle-outlined" style="margin-left:10px;"/>
              </a-tooltip>
            </template>
            <div class="signer-node-set">
              <p>签署位置设置方式</p>
              <div class="select-with-tooltip" 
                @mouseenter="showSelectTooltip = true" 
                @mouseleave="showSelectTooltip = false">
                <a-select 
                  ref="select"  
                  v-model:value="controlChangeFlag" 
                  style="width: 100%;margin:10px 0"
                >
                  <a-select-option value="necessary_and_add">强制签署（允许新增）​​</a-select-option>
                  <a-select-option value="necessary_no_add">强制签署（禁止新增）​​</a-select-option>
                  <a-select-option value="not_necessary">自由签署​</a-select-option>
                </a-select>
                <a-tooltip 
                  :visible="showSelectTooltip"
                  :title="getControlChangeFlagTitle(controlChangeFlag)"
                  placement="topLeft"
                  overlayClassName="instant-tooltip"
                  :mouseEnterDelay="0"
                  :mouseLeaveDelay="0"
                  :destroyTooltipOnHide="true"
                >
                  <div class="tooltip-placeholder"></div>
                </a-tooltip>
              </div>
            </div>
            <div v-if="showSendSignControlTip" class="sign-control-tip-below">
              <div class="tip-content">
                <div class="tip-message">
                  <Icon icon="ant-design:info-circle-outlined" class="tip-icon" />
                  <span class="tip-text">{{ leftHeadTip }}</span>
                </div>
                <div class="tip-actions">
                  <a-button type="primary" size="small" @click="handleAcknowledge" class="acknowledge-btn">
                    已知晓，不再提示
                  </a-button>
                </div>
              </div>
            </div>
            <Controls   :documentList="documentList" 
                        :signerList="signerList" 
                        :nowDocument="nowDocument" 
                        :user="signers"  
                        :originType="2"
                        :chopStampUseFlag="chopStampUseFlag"
                        @dragOver="controlsDragOver"
                        @scroll="handleScroll">
            </Controls>
          </DocLeft>
            <DocContent :nowDocument="nowDocument" :showControl="true" :minTargetInfo="minTargetInfo" :docs="docs" @controlDelete="controlDelete"  :isSubmitControl="isSubmitControl" @controlFocus="controlFocus"
              @controlMousedown="controlMousedown" :spinning="spinning" :hasWriteControl="hasWriteControl"  @handleSetControl="handleSetControl" @refreshControlPosition="refreshControlPosition" @signDateFormat="signDateFormat">
            </DocContent>
          <DocRight title="设置模板参数填写方" v-show="hasWriteControl">
            <SetParamsWriter  :controlList="controlList"   :documentList="documentList"   :signerList="signerList" :docId="docId" @scroll="handleScroll" @needInitiatorWrite="monitorWriter">
            </SetParamsWriter>
          </DocRight> 
      </div>
    </div>
  </template>
  
  <script lang="ts">
  import {ref, unref, defineComponent, onMounted, reactive, toRefs, h, onBeforeMount} from "vue";
  import { Icon } from '/@/components/Icon';
  import { useRouter } from 'vue-router';
  import { Button,Modal } from 'ant-design-vue';
  import DocHeader from '../components/layouts/DocHeader.vue';
  import DocLeft from '../components/layouts/DocLeft.vue';
  import DocContent from '../components/layouts/DocContent.vue';
  import DocRight from '../components/layouts/DocRight.vue';
  import Controls from '../components/Controls.vue';
  import SetParamsWriter from '../components/SetParamsWriter.vue';
  import { getColtrolByType } from '../components/data/ControlData';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Loading } from '/@/components/Loading';
  import { getControlList, getDocFiles,  checkOperatorStatus, getSigners, savePosAndParams, getIsWrite, startContract, getAnnexImgs } from '/@/api/contract/index';
  import { cloneDeep } from 'lodash-es';
  import {currentPosition,recaculateBatchControlPosInPage, currentPositionReverse, pageScaling, paramBuildWidgets, findMinImageSize} from "/@/views/contract/components/data/ControlerMoveRange"
  // import { CanvasZoom, } from '/@/views/signature/doc/control/src/data/ControlData';
  import { CanvasZoom } from '/@/views/contract/components/data/ControlData';
  import { createLocalStorage } from '/@/utils/cache';
  import { parseInputPages, retainDecimals } from '/@/utils';
  import { buildUUID } from '/@/utils/uuid';
  import { getSystemLimit} from '/@/api/license';
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
      DocLeft,
      DocContent,
      DocRight,
      Controls,
      SetParamsWriter,
      Loading
    },
    setup(props,{emit}) {
        const docId = ref('');
        const annexId = ref('');
        const docs:any = ref([]); 
        const spinning = ref(false)
        const isSubmitControl  = ref(false);
            
        const controlList = ref(<any[]>[])
        const signers:any = ref({})
        const needWrite = ref(1);
        const documentList:any = ref([])
        const nowDocument:any = ref({});
        const signerList:any = ref([]);
        const controlsList:any = ref([]);
        const hasWriteControl = ref(false);
        const minTargetInfo = ref();
        const allDocMinWidth = ref(0);  //所有文档中最小的宽度

       
        const images:any = ref([])
        const controlChangeFlag = ref('necessary_and_add');
        const leftHeadTip = ref('通过拖拽每个签署方对应的控件到正文区域，即可为其设定专属签署位置。')
        const router = useRouter();
        const { currentRoute } = router;
        const route = unref(currentRoute);
        const signReId = route.query.signReId;
        const signRuId = route.query.signRuId;
        const taskId = route.query?.taskId;
        const isDetail = ref(false);
        const deleteIdList = ref(<string[]>[])
        const ls = createLocalStorage();
        ls.set('Sign-Task-Id',taskId)
        const showSelectTooltip = ref(false);

        // 添加控制提示显示的响应式变量
        const showSendSignControlTip = ref(true);

        showSendSignControlTip.value = (localStorage.getItem("showSendSignControlTip") == 'false') ? false : true;
        
        const compState = reactive({
          absolute: false,
          loading: false,
          tip:''
        })
        const signLoading = ref(false)
  
        const actions = ref(<ButtonItem[]>[
          // {
          //   type:'default',
          //   text:'保存',
          //   callBack:handleSave,
          //   show:true,
          //   disabled:compState.loading?true:false
          // },
          // {
          //   type:'primary',
          //   text:'填写',
          //   callBack:handleWrite,
          //   show:true,
          //   disabled:compState.loading?true:false
          // }
         
        ])
        // if(unref(isDetail)){
        //   actions.value = []
        // }
  
        const { createMessage: msg, createSuccessModal,createConfirm } = useMessage();
      
        const chopStampUseFlag = ref(false);
        onMounted(()=>{
          // checkStatus()
          initData()
          initSysLimit();
        })

        onBeforeMount(()=>{
          compState.loading = true;
        })

        // 添加处理"已知晓，不再提示"按钮点击的函数
        function handleAcknowledge() {
          showSendSignControlTip.value = false;
          localStorage.setItem("showSendSignControlTip", String(showSendSignControlTip.value));
        }

        // 添加获取 tooltip 内容的方法
        function getControlChangeFlagTitle(value) {
          const tooltips = {
            'necessary_and_add': '签署方须完成全部指定位置的签署，不可修改原位置，但允许自行新增签署区域。',
            'necessary_no_add': '签署方须完成全部指定位置的签署，不可修改原位置，且禁止新增任何签署区域。',
            'not_necessary': '签署方可修改、删除既有签署位置，亦可自由新增签署区域。'
          };
          return tooltips[value] || '';
        }

        async function initSysLimit(){
          const limit = await getSystemLimit();
          chopStampUseFlag.value = limit.pagingSealUseFlag;
        }
        
        function monitorWriter(val){
          if(val){
            actions.value.map((v:any)=>{
              if(v.text == '发起'){
                v.show = false
              }
              if(v.text == '填写'){
                v.show = true
              }
            })
          }else{
            actions.value.map((v:any)=>{
              if(v.text == '发起'){
                v.show = true
              }
              if(v.text == '填写'){
                v.show = false
              }
            })
          }
        }
  
  
        //根据状态和当前操作人是否进行过签署进行路由重定向到签署详情或者文档详情页
       async  function checkStatus(){
          isDetail.value =  route.query?.isDetail=='true'? true : false;
          if(unref(isDetail)){
            actions.value = [];
          }
          //return
          let result = await checkOperatorStatus({signRuId:signRuId});
          if(result){
            if(result.ruStatus == 6 || result.ruStatus == 8 ||  result.ruStatus == 9 || result.ruStatus == 11){
                router.replace({
                  name:'签约详情',
                  query:{
                    ...route.query,
                  }
                })
                return
            }
            if(result.operatorStatus==2){
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
              initData()
  
            })
          }   
        }
  
        function initWindow() {
            var h = document.documentElement.clientHeight || document.body.clientHeight;
            return h;
        }
        const height = ref(initWindow());
        
        
  
        function handlePosCancel(){
          // emit('cancel')
          window.history.go(-1);
          // window.close();
        }
      
        async function initData(){
          checkIsWrite()
          getDocs();
          getSignerList();
        }
        //判断是否需要填写
        async function checkIsWrite(){
            let res = await getIsWrite({signRuId:signRuId});
            if(res){
              needWrite.value = res;
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
                  v.images = result.sort((a, b) => a.page - b.page);
                  if(i==0){
                    images.value = result;
                  }
              })
              // const allImages = docs.value.flatMap(v=>v.images);
              // const  minWidth = allImages.reduce((min, item) => {
              //   return   parseFloat(item.imageWidth) < parseFloat(min.imageWidth) ? item : min;
              // }, allImages[0]).imageWidth;
              // allDocMinWidth.value = Number(minWidth.imageWidth);

              setTimeout(()=>{
                  getAllDocControls();
              },2000)
            }
          }catch(erroe){
            spinning.value = false;
          }
         
        }
        //获取图片列表
        async function getDocImages(){
          controlList.value = [];
          documentList.value = [];
          let result = await getAnnexImgs({annexId:annexId.value});
          if(result.length){
            result = result.sort((a, b) => a.page - b.page);
            images.value = result;
            documentList.value.map(v=>{
              if(v.id == docId.value){
                  v.images = result;
                  nowDocument.value.images = result;
              }
          })
          }else{
            reloadImg()
          }
        }
        function reloadImg(){
          const content = h('span', [
              h('span',null, {default: ()=> '文件加载失败？'}),
              h(Button, { type: 'link', onClick: getDocImages }, {default: ()=> '重新加载'}),
              h(Icon,   { icon: 'ant-design:close-outlined',color:'#666',style:{cursor: 'pointer'}, onClick: handleClose }),
            ]);
          msg.warning({
            content,
            // duration: 20,
          })
        }
        function handleClose(){
            msg.destroy()
        }
        //获取参与方
        async function getSignerList(){
          let result = await getSigners({signRuId:signRuId});
          if(result){
            signerList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
            signerList.value.map(item=>{
              if(item.signerType==1 || item.signerType==3){
                item.senderList =  item.senderList.sort((a, b) => a.senderOrder - b.senderOrder)
              }
            })
            signerList.value.map((v,index)=>{
                  if(v.signerType==1){
                      v.senderList.forEach((m,mIndex)=>{
                        m.colorIndex = mIndex;
                      })
                  }
                  if(v.signerType==2){
                      const temRes = signerList.value.filter(item=>item.signerType==1)
                      let  senderLength = 0;
                      if(temRes && temRes.length>0){
                          senderLength = temRes[0].senderList.length;
                      }
                  }
                  if(v.signerType == 3){
                      const temRes = signerList.value.filter(item=>item.signerType==1)
                      let  senderLength = 0;
                      if(temRes && temRes.length>0){
                          senderLength = temRes[0].senderList.length;
                      }
                      let  receivePersonalLength = signerList.value.filter(item=>item.signerType==2).length;
                      v.senderList.forEach((m,mIndex)=>{
                          m.colorIndex = senderLength + receivePersonalLength + mIndex;
                      })
                  }
              })
          }
        }
  
  
         //获取所有文档控件并整理结构
         async function getAllDocControls() {
              
              // let allControls = controllerListData;
              if(!unref(isDetail)){
                  actions.value = [
                      {
                          type:'default',
                          text:'保存',
                          callBack:handleSave,
                          show:true
                      },
                      {
                          type:'primary',
                          text:'填写',
                          callBack:handleWrite,
                          show:unref(needWrite)==1?true:false
                      },
                      {
                          type:'primary',
                          text:controlList.value.length?'直接发起':'发起',
                          callBack:handleStartDirect,
                          show:true,
                          // disabled:compState.loading?true:false
                      }
                  ]
              }
              let result = await getControlList({ signRuId: signRuId });
              let allControls = result.controlList;
              controlChangeFlag.value = result.controlChangeFlag
              if(!allControls.length){
                hasWriteControl.value = false;
              }
              let matchWriteControl = allControls.filter(v=>['date','line-text','multiline-text','number','image','dropdown-list','radio','check-box'].includes(v.controlType))
                if(matchWriteControl.length){
                    hasWriteControl.value = true;
                }else{
                  hasWriteControl.value = false;
              }
              let flatControls:any = []
            //   documentList.value = []
              //将控件按文档拆分
              allControls.map((item:any)=>{
                  if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(item.controlType) && item.propertyVoList){
                        //每个控件propertyVoList 内只可能存在一个类型为page_config 的配置项
                      let pageConfig =  item.propertyVoList.filter(u=>u.propertyType == 'page_config')[0];
                      let pageCustomConfig =  item.propertyVoList.filter(u=>u.propertyType == 'custom')[0];
                      const frontUid = 'api_uid_'+ parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                      item.propertyVoList.map((v,i)=>{
                          if(v.propertyType == 'relation_doc' ){
                              //将每个relation_doc 控件都推入flatControls数组
                              flatControls.push({
                                  ...item,
                                  isBatch:false,
                                  fontFamily:item.fontFamily,
                                  propertyVo:v,
                                  unequalId:v.id, //用于渲染  
                                  uid:frontUid,
                                  signRuDocId:v.propertyValue,
                                  controlDocId:v.propertyValue,
                                  propertyVoList:item.propertyVoList,
                                  controlPageInfo:{
                                      ...pageConfig,
                                      controlDocId:v.propertyValue,
                                  },
                                  pageCustomConfig:{
                                      ...pageCustomConfig,
                                      
                                  },
                              })
                          }
                      })
                  }else{
                      //如果是填写控件，则controlDocId 为 signRuDocId
                      item.controlDocId = item.signRuDocId;
                      item.unequalId = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(10);
                      item.propertyVoList = item.propertyVoList || [];
                      item.controlPageInfo = {};
                      flatControls.push(item);
                  }
                
              })
              console.log(flatControls,'flatControls-------------')
              //将控件按文档分类
              let groupControls:any = [];
  
              // 遍历flatControls数组
              flatControls.forEach((m:any,i)=>{
                let matchSameUid = flatControls.filter(u=>u?.uid == m.uid && ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType))
                if(matchSameUid.length>1){
                    matchSameUid.map((u:any)=>{
                        u.isBatch = true;
                    })
                }
                 // 在groupControls数组中查找与m.controlDocId匹配的控件
                 let matchControl =  groupControls.find(v=>v.controlDocId == m.controlDocId);
                  // 如果没有找到匹配的控件
                  if(!matchControl){
                      // 将m添加到groupControls数组中
                      let controlItem = {
                          controlDocId:m.controlDocId,
                          id:m.controlDocId, //分组id，取文档id
                          propertyVoList:m.propertyVoList,
                          controlPageInfo:m.controlPageInfo,
                          pageCustomConfig:m.pageCustomConfig,
                          controls:buildResponseControl([{...m,propertyVo:undefined, signRuDocId:m.controlDocId,propertyVoList:m.propertyVoList}])
                      }
                      groupControls.push(controlItem)
                  }else{
                      // 将m添加到matchControl的controls数组中
                      matchControl.controls.push({
                          ...m,
                          controlDocId:m.controlDocId,
                          propertyVoList:m.propertyVoList,
                      })
                      // 对matchControl的controls数组进行处理
                      matchControl.controls = buildResponseControl(matchControl.controls)
                  }
              })

             //根据文档排序
              let docIdToIndexMap = {};
              docs.value.forEach((item, index) => {
                  docIdToIndexMap[item.id] = index;
              });
              groupControls.sort((a, b) => {
                  return docIdToIndexMap[a.controlDocId] - docIdToIndexMap[b.controlDocId];
              })
              //将控件按页码配置按文档进行设置
              groupControls.map((item, docIndex)=>{
                let currentDoc = docs.value.find(v=>v.id == item.controlDocId);
                const { targets, maxWidth } = pageScaling(currentDoc.images);
                  item.controls.forEach((v,colIndex)=>{
                    v.offsetX = Number(v.offsetX)
                    v.offsetY = Number(v.offsetY)
                      //计算文档控件序号  
                      if( docIndex == 0){
                        v.posControlOrder = colIndex;
                      }
                      if( docIndex > 0){
                        v.posControlOrder = groupControls[docIndex-1].controls.length + colIndex;
                      }
                      if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(v.controlType)){
                          item.controls = item.controls&&item.controls.filter(m => m.uid != v.uid);
                          if(v.controlPageInfo?.propertyType == 'page_config' && v.controlPageInfo?.propertyValue != 'custom'){
                              for (let i = 0; i < currentDoc?.docPage; i++) {
                                  let unequalId = buildUUID() + '_' + i; 
                                  let shouldAddControl = false;
                                  let isBatch =false;
                                  const target = targets[i];
                                  const offsetWidth = (maxWidth - target.width) / 2;
  
                                  if (v.controlPageInfo.propertyValue === 'all') {
                                      shouldAddControl = true;
                                      isBatch =  currentDoc.docPage>1?true:false;
                                  } else if (v.controlPageInfo.propertyValue === 'odd_number' && (i + 1) % 2 !== 0) {
                                        isBatch =  currentDoc.docPage>2?true:false;
                                      shouldAddControl = true;
                                  } else if (v.controlPageInfo.propertyValue === 'even_number' && (i + 1) % 2 === 0) {
                                        isBatch =  currentDoc.docPage>3?true:false;
                                      shouldAddControl = true;
                                  }
                                  console.log(v.offsetY,v.controlType,'======控件位置')
  
                                  if (shouldAddControl) {
                                      item.controls.push({
                                          ...v, 
                                          unequalId,
                                          fontFamily:v.fontFamily,
                                          
                                          isBatch,
                                          propertyVoList:v.propertyVoList,
                                          uid: v.uid,
                                          signRuDocId: v.signRuDocId,
                                          signerId: v.signerId,
                                          controlClick: false,
                                          // position: {
                                          //     left: v.position.left,
                                          //     top: currentPositionReverse(v.offsetY,i),
                                          //     page: i,
                                          // },
                                          position: {
                                            left: v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width:offsetWidth + v.offsetX),
                                            top:  currentPositionReverse(v.offsetY, target),
                                            page: i,
                                          }
                                      });
                                  }
                              }
                          
                          }  else if(v.controlPageInfo?.propertyValue == 'custom'){
                              let pages = parseInputPages(v.pageCustomConfig.propertyValue.replace(/\s/g, ''));
                              pages.map(page => {
                                if(page <= currentDoc?.docPage){
                                  const target = targets[page-1];
                                  const offsetWidth = (maxWidth - target.width) / 2;
                                  item.controls.push({
                                      ...v,
                                      uid: v.uid,
                                      page:page-1,
                                      signRuDocId: v.signRuDocId,
                                      signerId: v.signerId,
                                      propertyVoList:v.propertyVoList,
                                      controlClick: false,
                                      position: {
                                          left: v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width:offsetWidth + v.offsetX),
                                          top:  currentPositionReverse(v.offsetY, target),
                                          page: page-1,
                                      },
                                  });
                                }else{
                                //     //超出后删除并去重
                                //     deleteIdList.value.push(v.id)
                                //     deleteIdList.value = [...new Set(deleteIdList.value)]
                                }
                              })
                            }
                      }else{
                        // v.position.top = currentPositionReverse(parseInt(v.offsetY) + ( 0 * v.page), v.page)
                        const target = targets[v.page];
                        const offsetWidth = (maxWidth - target.width) / 2;
                        //填写控件位置
                        v.widgetList = paramBuildWidgets(v.widgetList,target,offsetWidth);
                        // v.position.top = currentPositionReverse(v.offsetY, v.page)
                          v.position =  {
                            left:  parseInt(v.offsetX) + offsetWidth,
                            top:  currentPositionReverse(v.offsetY, target),
                            page: v.page,
                          }
                      }
                  })
              })
              let docsControl = groupControls.flatMap(v=>v.controls);
                docsControl.map((v:any)=>{
                    let matchSameUid = docsControl.filter((u:any)=>u?.uid == v.uid && ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType))
                    if(matchSameUid.length>1){
                        matchSameUid.map((u:any)=>{
                            u.isBatch = true;
                        })
                    }
                })
              console.log(groupControls,'分组后的控件--')
              //按文档进行控件设置
              for (let i = 0; i < docs.value.length; i++) {
                  let matchControl = groupControls.find(v=>v.controlDocId == docs.value[i].id)
                  setDocumentList(docs.value[i], matchControl?.controls || []);
              }
  
  
              // controlList.value = controlList.value.concat(allControls);
              if (unref(isDetail)) {
                  actions.value = []
              }
              spinning.value = false;
              compState.loading = false;
          }
         
       
          //文档切换
        async function handleDocChange(val){
          let matchDoc = documentList.value.find(item=>item.signRuDocId ==  val);
              nowDocument.value = matchDoc;
              docId.value = val;
              images.value = matchDoc.images;
              document.getElementsByClassName('doc-content')[0].scrollTop = 0
          // images.value = [];
          // annexId.value = docs.value.filter(v=>v.id==val)[0].annexId
          // let result = await getAnnexImgs({annexId:annexId.value});
          // if(result){
          //   result = result.sort((a, b) => a.page - b.page);
          //   let matchDoc = documentList.value.find(item=>item.signRuDocId ==  val);
          //   //更新文档图片数量用于重新计算拖拽范围
          //   matchDoc.pageSize = result.length;
          //   nowDocument.value = matchDoc;
          //   //重置滚动条
          //   document.getElementsByClassName('doc-content')[0].scrollTop = 0;
          //   // console.log(documentList,docId.value,  matchDoc,'匹配文档')
          // }
        }
  
        //整理文档列表
        function setDocumentList(doc,controls){
            let machDoc = documentList.value.find(v => v.signRuDocId == doc.id)
            if (machDoc) {
                machDoc.activeControl = controls
            } else {
                const {targets,maxWidth,minWidth} = pageScaling(doc.images, allDocMinWidth.value);
                documentList.value.push({
                    active:false,
                    documentName: doc.name,
                    annexFileId:"",
                    targets:targets,
                    signRuDocId:doc.id,
                    maxWidth:maxWidth,
                    minWidth:minWidth,
                    allDocMinWidth:allDocMinWidth.value,
                    activeControl: controls,
                    pageSize: doc.docPage,
                    images: cloneDeep(doc.images),
                    imageAllHeight:targets[targets.length-1].transformHeight + targets[targets.length-1].height + (images.value.length * CanvasZoom.space),
                    imageLoading: true
                })
            }
          nowDocument.value = documentList.value.find(v=>v.signRuDocId == docId.value);
        }
        // 构建文档控件
        function buildResponseControl(controls:any){
            const controlTem:any = [];
            if(controls && controls.length>0){
                controls.map((item:any)=>{
                    // let matchDoc = docs.value.filter(v =>v.id == item.signRuDocId)[0]
                    // if(!matchDoc) return;
                    // let { targets, maxWidth } = pageScaling(matchDoc.images);
                    // let target = targets[item.page];
                    // let offsetWidth = (maxWidth - target.width) / 2;
                    item.signerInfo = {};
                    const basaeColtrol = getColtrolByType(item.controlType);
                    const isSignControl = ['signature','sign-date','seal','chop-stamp'].filter(v=>v==item.controlType);
                    let signer2Count = 0;
                    signerList.value.map((v,index)=>{
                            if(v.signerType==1){
                                v.senderList.forEach((m,mIndex)=>{
                                  if(m.id == item.signerId){
                                      item.colorIndex = m.colorIndex;
                                      item.signerInfo = {
                                        ...m,
                                        startName:v.signerName,
                                        signerType:1,
                                        nodeName:m.senderName,
                                        receiverOrder:mIndex
                                      }
                                  }
                                 
                                })
            
                            }
                            if(v.signerType==2){
                                signer2Count +=1;
                                const temRes = signerList.value.filter(item=>item.signerType==1)
                                let  senderLength = 0;
                                if(temRes && temRes.length>0){
                                    senderLength = temRes[0].senderList.length;
                                }
                                if(v.id == item.signerId){
                                    // item.colorIndex = senderLength + v.signerOrder-2;
                                    item.colorIndex = (senderLength + index) -1;
                                    item.signerInfo = {
                                      ...v,
                                      receiveName:v.signerName,
                                      signerType:2,
                                      receiverOrder:signer2Count
                                    }
                                }
                               
                            }
                            if(v.signerType == 3){
                                signer2Count +=1;
                                const temRes = signerList.value.filter(item=>item.signerType==1)
                                let  senderLength = 0;
                                if(temRes && temRes.length>0){
                                    senderLength = temRes[0].senderList.length;
                                }
                            // let  senderLength = signerList.value.filter(item=>item.signerType==1)[0].senderList.length;
                                // let  receivePersonalLength = signerList.value.filter(item=>item.signerType==2).length;
                                v.senderList.forEach((m,mIndex)=>{
                                    // m.colorIndex = senderLength + receivePersonalLength + mIndex;
                                    if(m.id == item.signerId){
                                      item.colorIndex = m.colorIndex;
                                      item.signerInfo = {
                                        ...m,
                                        receiveName:v.signerName,
                                        nodeName:m.senderType==1?'经办人签字':'组织签章',
                                        signerType:3,
                                        receiverOrder:  signer2Count,
                                      }
                                    }
                                })
                            }
                        })
                    if(!basaeColtrol){
                        return []
                    }
                    if(['image','dropdown-list','radio','check-box'].includes(item.controlType) && item.properties && item.properties !='"[]"'){
                      // item.widgetList  = item.properties && (typeof item.properties == 'string')?JSON.parse(item.properties):[];
                      item.widgetList  = item.properties;
                    }
                    // item.size = {
                    //     width: parseInt(item.width) || basaeColtrol.width,
                    //     height: parseInt(item.height) || basaeColtrol.height,
                    //     minWidth: basaeColtrol.size.minWidth,
                    //     minHeight: basaeColtrol.size.minHeight,
                    // }
                    controlTem.push({
                        // id :parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        signerInfo:item.signerInfo,
                        controlOrder:item.controlOrder,
                        // widgetList:paramBuildWidgets(item.widgetList,target,offsetWidth),
                        widgetList:item.widgetList,
                        properties:item.properties,
                        id : item.id || '',
                        uid : item.id,
                        propertyVoList:item.propertyVoList,
                        controlPageInfo:item.controlPageInfo,
                        pageCustomConfig:item.pageCustomConfig,
                        icon:basaeColtrol.icon,
                        name:item.name,
                        showPopover: false,
                        title:basaeColtrol.title,
                        controlType:item.controlType,
                        placeholder:item.placeholder,
                        width:item.width,
                        height:item.height,
                        isRequired:item.isRequired,
                        originType:item.originType,
                        focus:false,
                        value:item.value,
                        offsetX:item.offsetX,
                        offsetY:item.offsetY,
                        page:item.page,
                        controlClick:false,
                        disabled:unref(isDetail)?true:(isSignControl.length?false:true),
                        zoom:basaeColtrol.zoom,
                        move:unref(isDetail) || item.originType==1?false:(isSignControl.length?true:false),
                        delete:(unref(isDetail) || item.originType==1)?false:(isSignControl.length?true:false),
                        required:item.isRequired == 1,
                        attr:basaeColtrol.attr,
                        signRuDocId:item.signRuDocId,
                        signerId:item.signerId,
                        signerType:item.signerType,
                        // format:(item.controlType=='sign-date'?item.format:'yyyy年MM月dd日') || 'yyyy年MM月dd日',
                        format: item.controlType === 'sign-date' 
                                    ? (item.format && item.format.trim() ? item.format : 'yyyy年MM月dd日')
                                    : '',
                        colorIndex:item.colorIndex,
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
                        // position:{
                        //     left: parseInt(item.offsetX),
                        //     top: parseInt(item.offsetY),
                        //     page:parseInt(item.page),
                        // },
                        position:{
                          left:0,
                          top:0,
                          page:0
                        },
                        user:{}
                    })
                })
            }
            console.log(controlTem,'当前控件000000')
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
        //控件设置应用于选定文档的指定页码
        function handleSetControl(docKeys, element) {

              //将当前点击的控件设置为批量控件
              element.isBatch = true;
              //根据页面配置固化propertyVoList已知项
              if(element.oddEventType == 'custom'){
                  element.propertyVoList = [
                      {
                          controlId: element.id,
                          id:'page_config_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                          propertyType:"page_config",
                          propertyValue:'custom',
                      },
                      {
                          controlId: element.id,
                          id:'page_config_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                          propertyType:"custom",
                          propertyValue:element.custom,
                      }
                  ];
              }else{
                  // 重置propertyVoList
                  element.propertyVoList = [
                      {
                          controlId: element.id,
                          id:'page_config_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                          propertyType:"page_config",
                          propertyValue:element.oddEventType,
                      },
                  ];
              }

                //更新各个文档的propertyVoList
                documentList.value.map(v => {
                    if (docKeys.includes(v.signRuDocId)) {
                        // element.propertyVoList = element.propertyVoList.filter(v => v.propertyType !== 'relation_doc');
                        element.propertyVoList.push(
                            {
                                controlId: element.id,
                                id:'relation_doc_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                                propertyType:"relation_doc",
                                propertyValue:v.signRuDocId,
                            }
                        )
                    }
                })
                let pages:number[] = []
                documentList.value.map(v => {
                    if (element.oddEventType == 'custom') {
                      pages = parseInputPages(element.custom.replace(/\s/g, ''));
                    }else{
                      for (let i = 0; i < v.pageSize; i++) {
                        if (element.oddEventType == 'all') {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 != 0)) {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 == 0)) {
                          pages.push(i)
                        }
                      }
                    }
                })
                let minTarget  = findMinImageSize(documentList.value, docKeys,Array.from(new Set(pages)))
                minTargetInfo.value = minTarget;
                const elementTarget = nowDocument.value.targets[element.position.page];
                const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;
                const elementOffsetX = element.position.left - elementOffsetWidth;
                const elementOffsetY = currentPosition(element.position.top,elementTarget);
                documentList.value.map(v => {
                  if (docKeys.includes(v.signRuDocId)) {
                      v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
                      // console.log(element.uid,  v.signRuDocId,v.images,v.activeControl.length,'当前文档id,页数,控件')
                      if (element.oddEventType == 'custom') {
                          let pages = parseInputPages(element.custom.replace(/\s/g, ''));
                          const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                          pages.map(page => {
                                if(page <= v.pageSize){
                                  v.activeControl.push({
                                  ...element,
                                  isBatch:(pages.length>1 || docKeys.length>1)?true:false,
                                  isMultipleDocBatch: docKeys.length > 1?true:false,
                                  // offsetX: element.position.left,
                                  // offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                  signRuDocId:v.signRuDocId,
                                  signerId:element.signerId,
                                  controlClick: false,
                                  uid: element.uid || frontUid,
                                  controlPageInfo:{
                                      controlDocId:v.signRuDocId,
                                      propertyType:"page_config",
                                      propertyValue:'custom',
                                      id:element.uid + '_page_config',
                                      controlId:element.uid 
                                  },
                                  minTarget,
                                  position:{
                                      left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:page - 1}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                      top: recaculateBatchControlPosInPage({...element,position:{...element.position,page:page - 1}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth , minTarget).y,
                                      page:page - 1,
                                  },
                              })
                            }else{
                                pages = pages.filter(v=>v != page)
                                //  //超出后删除并去重
                                // deleteIdList.value.push(element.id)
                                // deleteIdList.value = [...new Set(deleteIdList.value)]
                            }
                            element.propertyVoList.map(v=>{
                                if(v.propertyType == "custom"){
                                    v.propertyValue = pages.join(',')
                                }
                            })
                           
                          })
                      }else{
                          const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                          for (let i = 0; i < v.pageSize; i++) {
                              if (element.oddEventType == 'all') {
                                console.log(recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,'批量横向位置-----')
                                  v.activeControl.push({
                                      ...element,
                                      minTarget,
                                      isBatch: (v.pageSize>1 || docKeys.length>1)?true:false,
                                      isMultipleDocBatch: docKeys.length > 1?true:false,
                                      signRuDocId:v.signRuDocId,
                                      signerId:element.signerId,
                                      // offsetX: element.position.left,
                                      // offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                      controlClick: false,
                                      uid: element.uid || frontUid,
                                      controlPageInfo:{
                                          controlDocId:v.signRuDocId,
                                          propertyType:"page_config",
                                          propertyValue:element.oddEventType,
                                          id:element.uid + '_page_config',
                                          controlId:element.uid 
                                      },
                                      // position: {
                                      //     left: element.position.left,
                                      //     top: currentPosition(element.position.top, element.position.page) + (CanvasZoom.height + 16) * i + 16,
                                      //     page: i,
                                      // },
                                      position: {
                                        left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                        top: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                                        page:i,
                                      },
                                  })
                              }
                              if (element.oddEventType == 'odd_number') {
                                  if ((i + 1) % 2 != 0) {
                                      v.activeControl.push({
                                          ...element,
                                          isBatch: (v.images.length>2 || docKeys.length>1)?true:false,
                                          isMultipleDocBatch: docKeys.length > 1?true:false,
                                          uid: element.uid || frontUid,
                                          signRuDocId:v.signRuDocId,
                                          signerId:element.signerId,
                                          controlClick: false,
                                          minTarget,
                                          // offsetX: element.position.left,
                                          // offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                          controlPageInfo:{
                                              controlDocId:v.signRuDocId,
                                              propertyType:"page_config",
                                              propertyValue:element.oddEventType,
                                              id:element.uid + '_page_config',
                                              controlId:element.uid 
                                          },
                                          // position: {
                                          //     left: element.position.left,
                                          //     top: currentPosition(element.position.top, element.position.page) + (CanvasZoom.height + 16) * i + 16,
                                          //     page: i,
                                          // },
                                          position: {
                                            left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                            top:  recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                            page: i,
                                        },
                                      })
                                  }
                              }
                              if (element.oddEventType == 'even_number') {
                                  if ((i + 1) % 2 == 0) {
                                      v.activeControl.push({
                                          ...element,
                                          isBatch: (v.images.length>3 || docKeys.length>1)?true:false,
                                          isMultipleDocBatch: docKeys.length > 1?true:false,
                                          // offsetX: element.position.left,
                                          // offsetY: currentPosition(element.position.top,element.position.page),//计算成为每页的位置
                                          controlClick: false,
                                          signerId:element.signerId,
                                          minTarget,
                                          signRuDocId:v.signRuDocId,
                                          uid: element.uid || frontUid,
                                          controlPageInfo:{
                                              controlDocId:v.signRuDocId,
                                              propertyType:"page_config",
                                              propertyValue:element.oddEventType,
                                              id:element.uid + '_page_config',
                                              controlId:element.uid 
                                          },
                                          // position: {
                                          //     left: element.position.left,
                                          //     top: currentPosition(element.position.top, element.position.page) + (CanvasZoom.height + 16) * i + 16,
                                          //     page: i,
                                          // },
                                          position: {
                                              left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                              top:  recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                              page: i,
                                          },
                                      })
                                  }
                              }
                          
                          }
                      }
                     
                  }else{
                      //其他文档的当前控件删除掉
                      v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
                      //自动切换到另一有该控件的文档
                    //   if(v.signRuDocId != docId.value){
                    //       handleDocChange(v.signRuDocId)
                    //   }
                  }
              })

              let controls = documentList.value.flatMap(item => item.activeControl);
                controls.map(item=>{
                    item.isBatch = false;
                    let matchSameUid = controls.filter(u=>u?.uid==item?.uid && ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType))
                    if(matchSameUid.length>1){
                        matchSameUid.map((u:any)=>{
                            u.isBatch = true;
                        })
                    }
                })
  
              //更新当前文档
              nowDocument.value = documentList.value.find(v => v.signRuDocId == docId.value);
          }
  
         //刷新控件状态
         function refreshControls(element:any,isAdd:boolean){ 
            if(signers.value){
                controlMove.activeControl = nowDocument.value.activeControl.filter((item:any) => {
                    return item.user.index == signers.value.index;
                })
            }else{
                controlMove.activeControl = nowDocument.value.activeControl;
            }
            controlMove.activeControl = nowDocument.value.activeControl;
            if(element.controlType == 'chop-stamp' && isAdd){

                const minImageHeightItem = nowDocument.value.images.reduce((minItem, currentItem) => {
                  return parseFloat(currentItem.imageHeight) < parseFloat(minItem.imageHeight) ? currentItem : minItem;
                });
                const elementTarget = nowDocument.value.targets[element.position.page];
                let elementOffsetY = currentPosition(element.position.top,elementTarget);
                if(elementOffsetY > Number(minImageHeightItem.imageHeight) - element.size.height){
                  elementOffsetY = Number(minImageHeightItem.imageHeight) - element.size.height;
                  element.position.top = currentPositionReverse(elementOffsetY,elementTarget);
                }
                for(let i=0;i<nowDocument.value.pageSize;i++){
                  const target = nowDocument.value.targets[i];
                  const offsetWidth = ( nowDocument.value.maxWidth - target.width) / 2;
                    let unequalId = buildUUID() + '_' + i; 
                    if(i != element.position.page){
                        nowDocument.value.activeControl.push({
                            ...element,
                            unequalId,
                            position:{
                                left:  (offsetWidth + target.width) - element.size.width, 
                                top:  currentPositionReverse(elementOffsetY, target),
                                page:i,
                            }
                        })
                    }
                }
            }
        }
          //控件拖拽完成事件
          function controlsDragOver(e:any,element:any) {
            controlMove.elementMove = element;
            refreshControls(element,true);
            
          }
          //删除控件
          function controlDelete(element:any){
            element.controlClick = false;
            deleteIdList.value.push(element.id);
            documentList.value.map(v => {
                v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
            })
            nowDocument.value = documentList.value.find(v => v.signRuDocId == docId.value)
            refreshControls(element,false);
        }
        //控件点击操作
        function controlMousedown(element:any){
            controlMove.elementMove = element;
            documentList.value.map(v => {
                v.activeControl.forEach(item => {
                    item.controlClick = false
                    element.focus = false;
                });
            })
            element.controlClick = true;
            element.focus = true;
        }
         //控件日期格式
         function signDateFormat(element){
              documentList.value.map(v => {
                  v.activeControl.map(item => {
                      if(item.controlType == 'sign-date'){
                          item.format = element.format;
                      }
                  })
              })
          }
          // 重新计算控件位置
        function refreshControlPosition(element, moveOpt) {
            if (!moveOpt) return;
            let pages:number[] = []
            let docKeys:string[] = []
                documentList.value.map(v => {
                    if (element.oddEventType == 'custom') {
                      pages = parseInputPages(element.custom.replace(/\s/g, ''));
                    }else{
                      for (let i = 0; i < v.pageSize; i++) {
                        if (element.oddEventType == 'all') {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 != 0)) {
                          pages.push(i)
                        }
                        if (element.oddEventType == 'odd_number' && ((i + 1) % 2 == 0)) {
                          pages.push(i)
                        }
                      }
                    }
                })
            docKeys =  element.propertyVoList.filter(item => item.propertyType === "relation_doc").map(item => item.propertyValue);
            const minTarget  = findMinImageSize(documentList.value, docKeys, Array.from(new Set(pages)))
            const elementTarget = nowDocument.value.targets[element.position.page];
            const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;
            const elementOffsetX = element.position.left - elementOffsetWidth;
            const elementOffsetY = currentPosition(element.position.top,elementTarget); 
            documentList.value.map(v => {
                v.activeControl.map(item => {
                    //过滤掉当前移动的控件计算其他复制的控件的位置
                    if (item.uid == element.uid && !item.controlClick) {
                        const target = v.targets[item.position.page];
                        const pageElementOffsetWidth =  ( v.maxWidth - target.width) / 2;
                        item.position = {
                            left: element.controlType == 'chop-stamp' ? (pageElementOffsetWidth + target.width) - element.size.width : ((pageElementOffsetWidth + elementOffsetX) >= ((pageElementOffsetWidth + target.width) - element.size.width)?(pageElementOffsetWidth + target.width) - element.size.width : pageElementOffsetWidth + elementOffsetX),
                            top: recaculateBatchControlPosInPage(item, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                            page: item.position.page,
                        }
                    }
                })
            })
            nowDocument.value = documentList.value.find(v => v.signRuDocId == docId.value)
        }
       
        //文档控件滚动定为
        function handleScroll(doc,el){
          compState.loading = true;
          if(doc.signRuDocId != docId.value){
            handleDocChange(doc.signRuDocId)
            setTimeout(()=>{
              // document.getElementsByClassName('doc-content')[0].scrollTo(0,el.position.page * 1147);
              document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
            },2000)  
          }else{
            // document.getElementsByClassName('doc-content')[0].scrollTo(0,el.position.page * 1147);
            document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
          }
          documentList.value.map(v => {
              v.activeControl.forEach(item => {
                  item.controlClick = false
                  item.focus = false;
                  item.zoom = false;
                  if(item.uid == el.uid){
                    item.controlClick = true
                    item.focus = true;
                  }
              });
            })
          setTimeout(()=>{
            compState.loading = false;
          },500)
        }
        function mergedDataFn(inputData){
          const mergedObjects = {};
  
          inputData.forEach(obj => {
          const id = obj.id;
  
          if (!mergedObjects[id]) {
              // 如果 mergedObjects 中不存在该 ID，则创建一个新的对象
              mergedObjects[id] = { ...obj };
          } else {
              // 如果已经存在该 ID，则将属性合并到现有对象中
              Object.assign(mergedObjects[id], obj);
          }
          });
          // 将合并后的对象转换为数组
          const resultArray = Object.values(mergedObjects);
  
          return resultArray;
        }
        //保存基本数据
        async function saveFormData(){
          return new Promise(async (resolve, reject) => {
            let controls =  documentList.value.flatMap(item => item.activeControl);
            // console.log(controls,nowDocument.value.activeControl)
            let paramsControl:any = [];
            controls.map(item=>{
              const matchDoc = documentList.value.filter(v =>v.signRuDocId ==item.signRuDocId)[0];
              const targets = matchDoc.targets;
              const target = targets[item.position.page];
              const offsetWidth = (matchDoc.maxWidth - target.width) /2;
              //匹配到为接收方 否则为发起方
              let matchSigner = signerList.value.find(m=>m.id == item.signerId);
              item.signerType = matchSigner?.signerType;
              paramsControl.push({
                    "propertyVoList": item.propertyVoList,
                    "controlDocId":item.controlDocId,
                    "fontFamily": item.style.fontFamily,
                    "fontSize": item.style.fontSize,
                    "height": item.size.height,
                    "name": item.name,
                    "offsetX": item.position?.left - retainDecimals(offsetWidth),
                    "offsetY": currentPosition(item.position.top,target),//计算成为每页的位置
                    "pageWidth": target.width,
                    "pageHeight": target.height,
                    "page": item.position.page,
                    "placeholder": item.placeholder,
                    "signReDocId": item.signReDocId,
                    "originType": item.originType,
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
                    "value": item.value,
                    // "format":(item.controlType=='sign-date'?item.format:'yyyy年MM月dd日') || 'yyyy年MM月dd日',
                    // "format": (item.controlType === 'sign-date' && item.format && item.format.trim()) || 'yyyy年MM月dd日',
                    "format": item.controlType === 'sign-date' 
                                    ? (item.format && item.format.trim() ? item.format : 'yyyy年MM月dd日')
                                    : '',
                    "width": item.size.width,
                    "written": 1,
                    "id":item.id || '',
                    "uid":item.uid,
                    "tenantId": item.user.tenantId,
                })
              })
              let canSubmit = true;
              for(const item of paramsControl){
                if(!item.signerId){
                  canSubmit = false
                  msg.warning('模板参数填写方未全部设置');
                  delayColseLoading(500)
                  break;
                }
              }
              if(!canSubmit){
                compState.loading = false;
                return 
              }
  
              // console.log(paramsControl,'提交的控件--')
              // console.log(mergedDataFn(paramsControl),'提交整理的控件--')
              let result  = await savePosAndParams({controlChangeFlag:unref(controlChangeFlag),signRuId:signRuId,controlList:mergedDataFn(paramsControl),deleteIdList:unref(deleteIdList)});
              if(result){
                resolve(result)
              }else{
                compState.loading = false;
                reject(result)
              }
          })
        }
        function delayColseLoading(time?:number){
          setTimeout(function(){
            compState.loading = false;
            signLoading.value = false;
          },time?time:300)
        }
  
      //更新控件id防止一个控件添加多次
      async function updateControlsId(){
          isSubmitControl.value= true;
          getAllDocControls()

          return;
          let apiControls =  await getControlList({signRuDocId:docId.value});
          if(apiControls){
  
  
            // 创建一个 Set 用于存储已经赋值的 id
            const assignedIds = new Set(nowDocument.value.activeControl.filter(item => item.id !== '').map(item => item.id));
  
            // 遍历 a 数组
            const resultArray = nowDocument.value.activeControl.map(itemA => {
              // 在 b 数组中查找与当前 a 数组项相同 type 的项
              const matchingItemsB = apiControls.filter(itemB => itemB.controlType === itemA.controlType &&  itemB.signerId === itemA.signerId && itemB.page === itemA.position.page);
  
              // 如果 a 数组项没有 id 且存在与之 type 相同的项，则将第一个匹配项的 id 赋值给 a 数组项
              if (!itemA.id && matchingItemsB.length > 0) {
                const matchingIdB = matchingItemsB.find(itemB => !assignedIds.has(itemB.id));
  
                if (matchingIdB) {
                  itemA.id = matchingIdB.id;
                  assignedIds.add(matchingIdB.id);
                }
              }
  
              return itemA;
            });
            nowDocument.value.activeControl = resultArray;
  
            return 
          }
        }
        async  function handleSave(){
          if( compState.loading){
            return
          }
         compState.loading = true;
          saveFormData().then(res=>{
            if(res){
              updateControlsId()
              compState.loading = false;
              msg.success('保存成功')
            }else{
              compState.loading = false;
            }
          })
        }
        //填写
        async  function handleWrite(){
          if( compState.loading){
            return
          }
          compState.loading = true;
          saveFormData().then(res=>{
            if(res){
              updateControlsId()
              compState.loading = false;
              window.open('/#/contract/params?__full__&signReId=' + signReId + '&signRuId=' + signRuId, '_self')
            }else{
              compState.loading = false;
            }
          })
        }
        function handleStartDirect() {
          if( compState.loading){
            return
          }
          
          createConfirm({
              title: '温馨提示', 
              content: '请确认是否发起签署，发起后不可更改',
              okText:'确定',
              cancelText:'取消',
              iconType: 'warning',
              onOk:async ()=> {
                saveFormData().then(async(res)=>{
                  if(res){
                    updateControlsId();
                    signLoading.value = true;
                    let result = await startContract({signRuId:signRuId});
                    if(result){
                      signLoading.value = false;
                        Modal.destroyAll();
                        setTimeout(()=>{
                          router.push("/contract/doc");
                          return;
                          if(window.opener){
                            window.close();
                          }else{
                            router.push("/contract/doc")
                          }
                        })
                    }else{
                      setTimeout(() => {
                        signLoading.value = false;
                      },500)
                    }
                  }
                })
              }
            })
          
        }
        function setControlScroll(index){
          let paramsContainer = document.getElementsByClassName('scrollbar__wrap scrollbar__wrap--hidden-default')[2]
          if(paramsContainer){
            document.getElementsByClassName('writer-li')[index].scrollIntoView({ behavior: 'smooth', block: 'start' })
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
            docs,
            docId,
            height,
            actions,
            nowDocument,
            documentList,
            signerList,
            controlsList,
            monitorWriter,
            controlList,
            controlMove,
            controlMousedown,
            controlsDragOver,
            refreshControlPosition,
            controlDelete,
            handleSetControl,
            hasWriteControl,
            signers,
            handleDocChange,
            controlFocus,
            minTargetInfo,
            signLoading,
            handleScroll,
            spinning,
            handlePosCancel,
            ...toRefs(compState),
            needWrite,
            isSubmitControl,
            signDateFormat,
            controlChangeFlag,
            leftHeadTip,
            chopStampUseFlag,
            handleAcknowledge,
            showSendSignControlTip,
            getControlChangeFlagTitle,
            showSelectTooltip,

        }  
      }
  })
  </script>
  
  <style lang="less" scoped>
  .position-container{
    margin-top:-20px;
  }
  .signer-node-set {
    p{
      margin-bottom:0;
      font-size:12px;
    }

    .select-with-tooltip {
      position: relative;
      
        .tooltip-placeholder {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          pointer-events: none; // 确保不会拦截点击事件
        }
      }
  }

  .instant-tooltip {
    :deep(.ant-tooltip-inner) {
      white-space: normal;
      max-width: 300px;
      text-align: justify;
    }
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

  .sign-control-tip-below {
    background: linear-gradient(135deg, #ffffff, #f9f9f9);
    border: 1px solid #e1e8ed;
    border-radius: 8px;
    // box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12), 0 2px 4px rgba(0, 0, 0, 0.08);
    padding: 16px 20px;
    // margin-top: 20px;
    margin-bottom: 20px;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 4px;
      height: 100%;
      background: linear-gradient(to bottom, #1890ff, #40a9ff);
    }
    
    .tip-content {
      display: flex;
      flex-direction: column;
      gap: 12px;
      
      .tip-message {
        display: flex;
        align-items: flex-start;
        gap: 10px;
        
        .tip-icon {
          color: #1890ff;
          font-size: 18px;
          flex-shrink: 0;
          margin-top: 2px;
        }
        
        .tip-text {
          font-size: 14px;
          color: #333;
          line-height: 1.5;
          flex: 1;
        }
      }
      
      .tip-actions {
        display: flex;
        justify-content: center;
        margin-top: 8px;
        
        .acknowledge-btn {
          border-radius: 16px;
          padding: 0 20px;
          height: 32px;
          font-size: 13px;
          box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
          transition: all 0.3s ease;
          
          &:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
          }
        }
      }
    }
  }

  /* 响应式处理 */
  @media (max-width: 768px) {
    .sign-control-tip-below {
      padding: 14px 16px;
      
      .tip-content {
        .tip-message {
          .tip-text {
            font-size: 13px;
          }
        }
        
        .tip-actions {
          .acknowledge-btn {
            width: 100%;
            max-width: 200px;
          }
        }
      }
    }
  }


  </style>

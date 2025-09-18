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
    <div class="businessline-position-container" :style="'height: ' + (height - 63) + 'px'">
        <Loading :loading="loading" :absolute="false" />
        <DocHeader :actions="actions" :docs="docs" :docId="docId" @change="handleDocChange" @cancel="handlePosCancel">
        </DocHeader>
        <div class="position-params-body">
            <DocLeft title="设置签署位置">
                  <div class="signer-node-set">
                    <p>签署位置设置方式</p>
                    <a-select ref="select"  v-model:value="controlChangeFlag" style="width: 90%;margin:10px 0">
                      <a-select-option value="necessary_and_add">强制签署（允许新增）​​</a-select-option>
                      <a-select-option value="necessary_no_add">强制签署（禁止新增）</a-select-option>
                      <a-select-option value="not_necessary">自由签署​</a-select-option>
                    </a-select>
                    <a-tooltip>
                      <template #title>
                        <span v-if="controlChangeFlag=='necessary_and_add'">签署方须完成全部指定位置的签署，不可修改原位置，但允许自行新增签署区域。</span>
                        <span v-if="controlChangeFlag=='necessary_no_add'">签署方须完成全部指定位置的签署，不可修改原位置，且禁止新增任何签署区域。</span>
                        <span v-if="controlChangeFlag=='not_necessary'">签署方可修改、删除既有签署位置，亦可自由新增签署区域。</span>
                      </template>
                      <Icon icon="ant-design:question-circle-outlined" style="margin-left:10px;"/>
                    </a-tooltip>
                  </div>
                  <Controls :documentList="documentList" :signerList="signerList" :nowDocument="nowDocument" :user="signers"
                      @dragOver="controlsDragOver" @scroll="handleScroll" :chopStampUseFlag="chopStampUseFlag">
                  </Controls>
            </DocLeft>
            <DocContent :nowDocument="nowDocument" :docs="docs" :showControl="true" :minTargetInfo="minTargetInfo" :hasWriteControl="hasWriteControl" :showRange="showRange" @controlDelete="controlDelete"
                @controlFocus="controlFocus"
                :isSubmitControl="isSubmitControl" @controlMousedown="controlMousedown" :spinning="spinning"
                @handleSetControl="handleSetControl" ref="docContentRef" @refreshControlPosition="refreshControlPosition" @signDateFormat="signDateFormat">
            </DocContent>
            <DocRight title="设置模板参数填写方"  v-if="hasWriteControl">
                <SetParamsWriter :documentList="documentList" :signerList="signerList" :hasWriteControl="hasWriteControl"
                    :docId="docId" @scroll="handleScroll" ref="paramsWriterRef">
                </SetParamsWriter>
            </DocRight>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, unref, defineComponent, onMounted, reactive, onBeforeMount, toRefs, h, watch } from "vue";
import { Icon } from '/@/components/Icon';
import { useRouter } from 'vue-router';
import DocHeader from '../components/layouts/DocHeader.vue';
import DocLeft from '../components/layouts/DocLeft.vue';
import DocContent from '../components/layouts/DocContent.vue';
import DocRight from '../components/layouts/DocRight.vue';
import Controls from '../components/Controls.vue';
import SetParamsWriter from '/@/views/businessLine/components/SetParamsWriter.vue';
import { getColtrolByType } from '/@/views/businessLine/components/data/ControlData';
import { useMessage } from '/@/hooks/web/useMessage';
import { Loading } from '/@/components/Loading';
import { Button } from 'ant-design-vue';
// import { controllerListData} from '../mock';
// import { getControlList, getDocFiles, getDocImgs, getSigners, savePosAndParams, getIsWrite, startContract } from '/@/api/contract/index';
import { getIsWrite } from '/@/api/contract/index';
import { getBusinessLineFiles, getBusinessLineSignerList, getBusinessLineImages, setBusinessLineControl, getBusinessLineControl } from '/@/api/businessLine';
import { cloneDeep } from 'lodash-es';
import { currentPosition, currentPositionReverse, recaculateBatchControlPosInPage, findMinImageSize} from "/@/views/businessLine/components/data/ControlerMoveRange"
import { CanvasZoom, pageScaling, formatControls, paramBuildWidgets } from '/@/views/businessLine/components/data/ControlData'
import { parseInputPages, getAccumulatedHeight, retainDecimals } from '/@/utils/index';
import { buildUUID } from '/@/utils/uuid';
import { getSystemLimit} from '/@/api/license';
interface BtnFun {
    (): void
}

interface ButtonItem {
    type: string;
    text: string;
    disabled?: boolean;
    show?: boolean;
    callBack: BtnFun
}

export default defineComponent({
    name: "PositionParams",
    components: {
        Icon,
        DocHeader,
        DocLeft,
        DocContent,
        DocRight,
        Controls,
        SetParamsWriter,
        Loading
    },
    setup( ) {
        const docId = ref('');
        const docAnnexId = ref('');
        const docs: any = ref([]);
        const spinning = ref(false)
        const isSubmiting = ref(false);
        const controlChangeFlag = ref('necessary_and_add');
        const paramsWriterRef = ref()

        const signers: any = ref({})
        const needWrite = ref(1);
        const documentList: any = ref([])
        const nowDocument: any = ref({});
        const signerList: any = ref([]);
        const controlsList: any = ref([]);
        const showRange = ref(false);
        const minTargetInfo = ref();
        const images: any = ref([])
        const router = useRouter();
        const { currentRoute } = router;
        const route = unref(currentRoute);
        const signReId = route.query.signReId;
        const signRuId = route.query.signRuId;
        const isDetail = route.query?.isDetail;
        const deleteIdList = ref(<string[]>[])
        const docContentRef = ref();
        const hasWriteControl = ref(true);
        const isSubmitControl = ref(false);
        const chopStampUseFlag = ref(false);
        const compState = reactive({
            absolute: false,
            loading: false,
            tip: ''
        })

        const { createMessage: msg, createSuccessModal } = useMessage();


        onMounted(() => {
            init()
            initSysLimit();
        })
        onBeforeMount(()=>{
          compState.loading = true;
        })
        async function initSysLimit(){
          const limit = await getSystemLimit();
          chopStampUseFlag.value = limit.pagingSealUseFlag;
        }
        watch(
            ()=>isSubmiting.value,
            (value)=>{
                    actions.value = [
                        {
                            type: 'default',
                            text: '保存并返回',
                            callBack: handleSaveAndClose,
                            show: true,
                            disabled:false
                        },
                        {
                            type: 'primary',
                            text: '保存',
                            callBack: handleSave,
                            show: true,
                            disabled:value
                        },
                    ]
               
            }
        )

        function initWindow() {
            var h = document.documentElement.clientHeight || document.body.clientHeight;
            return h;
        }
        const height = ref(initWindow());

        const actions = ref(<ButtonItem[]>[
            {
                type: 'default',
                text: '保存并返回',
                callBack: handleSaveAndClose,
                show: true
            },
            {
                type: 'primary',
                text: '保存',
                callBack: handleSave,
                show: true
            },


        ])
        if (unref(isDetail)) {
            actions.value = []
        }

        function handlePosCancel() {
            // emit('cancel')
            window.history.go(-1);
            // window.close();
        }

        async function init() {
            // checkIsWrite()
            getDocs();
            getSignerList();
        }
        //判断是否需要填写
        async function checkIsWrite() {
            let res = await getIsWrite({ signRuId: signRuId });
            if (res) {
                needWrite.value = res;
            }
        }
        //获取文档
        async function getDocs() {
            try {
                spinning.value = true;
                let result = await getBusinessLineFiles({ signReId: signReId })
                if (result) {
                    docs.value = result;
                    docId.value = result[0].id;
                    docAnnexId.value = result[0].annexId;
                    docs.value.map(async(v,i)=>{
                        v.pageSize = v.docPage;
                        let result = await getBusinessLineImages({ annexId:  v.annexId });
                        v.images = result.sort((a, b) => a.page - b.page);
                        if(i==0){
                          images.value = result;
                        }
                    })
                    setTimeout(()=>{
                        getAllDocControls(false);
                    },1000)
                }
            } catch (erroe) {
                spinning.value = false;
            }

        }
        //获取图片列表
        async function getDocImages(docId, docAnnexId) {
            // documentList.value = [];
            let result = await getBusinessLineImages({ annexId:  docAnnexId });
            if (result.length) {
                result = result.sort((a, b) => a.page - b.page);
                images.value = result;
                documentList.value.map(v=>{
                    if(v.id == docId.value){
                        v.images = result;
                        nowDocument.value.images = result;
                    }
                })
            } else {
                reloadImg()
            }
        }
        function reloadImg() {
            const content = h('span', [
                h('span', null, { default: () => '文件加载失败？' }),
                h(Button, { type: 'link', onClick: getDocImages(docId.value, docAnnexId.value) }, { default: () => '重新加载' }),
                h(Icon, { icon: 'ant-design:close-outlined', color: '#666', style: { cursor: 'pointer' }, onClick: handleClose }),
            ]);
            msg.warning({
                content,
                duration: 20,
            })
        }
        function handleClose() {
            msg.destroy()
        }
         //文档切换
         async function handleDocChange(val) {
            docId.value = val;
            docAnnexId.value = docs.value.filter(v => v.id == val)[0].annexId;
            // images.value = [];
            let matchDoc = documentList.value.find(item => item.signReDocId == docId.value);
            nowDocument.value = matchDoc;
            document.getElementsByClassName('doc-content')[0].scrollTop = 0;
            // docContentRef.value.scrollbarRef.resize.scrollTop = 0;

            // let result = await getBusinessLineImages({ annexId: docAnnexId.value });
            // if (result) {
            //     result = result.sort((a, b) => a.page - b.page);
            //     debugger
            //     let matchDoc = documentList.value.find(item => item.signReDocId == docId.value);
            //     matchDoc.images = result;
            //     //更新文档图片数量用于重新计算拖拽范围
            //     matchDoc.pageSize = result.length;
            //     nowDocument.value = matchDoc;
            //     docContentRef.value.scrollbarRef.resize.scrollTop = 0;
            //     console.log(matchDoc,'匹配文档')
            // }
        }

        //获取参与方
        async function getSignerList() {
            let result = await getBusinessLineSignerList({ signReId: signReId });
            if (result) {
                signerList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
                signerList.value.map(item => {
                    if (item.signerType == 1 || item.signerType == 3) {
                        item.senderList = item.senderList.sort((a, b) => a.senderOrder - b.senderOrder)
                    }
                })
            }

        }
        
        //获取所有文档控件并整理结构
        async function getAllDocControls(isChangeDoc: boolean = false) {
            //  documentList.value = [];
            let result = await getBusinessLineControl({ signReId: signReId  });
            let allControls = result.controlList;
            controlChangeFlag.value = result.controlChangeFlag
            if(!allControls.length){
              hasWriteControl.value = false;
            }
            let flatControls:any = []
            let matchWriteControl = allControls.filter(v=>['date','line-text','multiline-text','number','image','dropdown-list','radio','check-box'].includes(v.controlType))
            if(matchWriteControl.length){
                  hasWriteControl.value = true;
              }else{
                hasWriteControl.value = false;
            }
            //将控件按文档拆分
            allControls.map((item:any)=>{
                if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(item.controlType) && item.propertyVoList){
                      //每个控件propertyVoList 内只可能存在一个类型为page_config 的配置项
                    let pageConfig =  item.propertyVoList.filter(u=>u.propertyType == 'page_config')[0];
                    let pageCustomConfig =  item.propertyVoList.filter(u=>u.propertyType == 'custom')[0];
                    const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                    item.propertyVoList.map((v,i)=>{
                        if(v.propertyType == 'relation_doc'){
                            let unequalId = buildUUID() + '_' + i; 
                            //将每个relation_doc 控件都推入flatControls数组
                            console.log(unequalId,'控件unequalId')
                            flatControls.push({
                                ...item,
                                // page:parseInt(pageCustomConfig?.propertyValue)-1 || item.page,
                                propertyVo:v,
                                isBatch:false,
                                unequalId:v.id, //用于渲染  
                                uid:frontUid,
                                controlDocId:v.propertyValue,
                                signReDocId:v.propertyValue,
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
                        // if(v.propertyType == 'custom'){
                        //     v.propertyValue = item.page+1+'';
                        // }
                    })
                }else{
                    //如果是其他类型，则controlDocId 为 signReDocId
                    item.controlDocId = item.signReDocId;
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
                let matchSameUid = flatControls.filter(u=>u?.uid==m.uid && ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType))
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
                    groupControls.push({
                        controlDocId:m.controlDocId,
                        id:m.controlDocId, //分组id，取文档id
                        propertyVoList:m.propertyVoList,
                        controlPageInfo:m.controlPageInfo,
                        controls:buildResponseControl([{...m,propertyVo:undefined, signReDocId:m.controlDocId,propertyVoList:m.propertyVoList}])
                    })
                }else{
                    // 将m添加到matchControl的controls数组中
                    matchControl.controls.push({
                        ...m,
                        propertyVoList:m.propertyVoList,
                        // id:m.controlDocId,//分组id，取文档id
                        // propertyVo:undefined,
                        // signReDocId:m.controlDocId,
                    })
                    // 对matchControl的controls数组进行处理
                    matchControl.controls = buildResponseControl(matchControl.controls)
                }
            })

            console.log(groupControls,'分组控件--')

            //将控件按页码配置按文档进行设置

            groupControls.map((item,docIndex)=>{
                let currentDoc = docs.value.find(v=>v.id == item.controlDocId);
                const { targets, maxWidth } = pageScaling(currentDoc.images);
                item.controls.forEach((v,colndex)=>{
                    v.offsetX = Number(v.offsetX)
                    v.offsetY = Number(v.offsetY)
                    //计算文档控件序号  
                    if( docIndex == 0){
                      v.posControlOrder = colndex;
                    }
                    if( docIndex > 0){
                      v.posControlOrder = groupControls[docIndex-1].controls.length + colndex;
                    }
                    if(['signature', 'sign-date', 'seal', 'chop-stamp'].includes(v.controlType)){
                        item.controls = item.controls&&item.controls.filter(m => ((m.uid != v.uid) || (m.id != v.id)));
                        if(v.controlPageInfo?.propertyType == 'page_config' && v.controlPageInfo?.propertyValue != 'custom'){
                            for (let i = 0; i < currentDoc.docPage; i++) {
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
                                
                                if (shouldAddControl) {
                                    item.controls.push({
                                        ...v,
                                        // isBatch: currentDoc.docPage>1?true:false,  
                                        isBatch,
                                        unequalId,
                                        page:i,
                                        propertyVoList:v.propertyVoList,
                                        uid: v.uid,
                                        signReDocId: v.signReDocId,
                                        signerId: v.signerId,
                                        controlClick: false,
                                        // position: {
                                        //     left: v.position.left,
                                        //     top: currentPositionReverse(v.offsetY, i),
                                        //     page: i,
                                        // },
                                        position: {
                                            left: v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width:offsetWidth + v.offsetX),
                                            top:  currentPositionReverse(v.offsetY, target),
                                            page: i,
                                        },
                                    });
                                }
                            }
                        
                        }  else if(v.controlPageInfo?.propertyValue == 'custom'){
                            let pages = parseInputPages(v.pageCustomConfig.propertyValue.replace(/\s/g, ''));
                            pages.map(page => {
                                let unequalId = buildUUID() 
                                const target = targets[page-1];
                                const offsetWidth = (maxWidth - target.width) / 2;
                                // if(v.controlType == 'seal'){
                                //   console.log( page, 'offsetWidth---'+ offsetWidth, 'arget.width---'+ target.width, 'v.offsetX---'+ v.offsetX, 'v.width====='+ v.width, '控件x信息')
                                //   console.log(v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width : offsetWidth + v.offsetX) ,'最终位置')
                                // }
                                    // let docLength = item.propertyVoList.filter(l=>l.propertyType == 'relation_doc').length;
                                    if(page <= currentDoc.docPage){
                                        item.controls.push({
                                        ...v,
                                        // isBatch: (pages.length>1 || docLength>1) ?true:false,
                                        unequalId,
                                        uid: v.uid,
                                        page:page-1,
                                        signReDocId: v.signReDocId,
                                        signerId: v.signerId,
                                        propertyVoList:v.propertyVoList,
                                        controlClick: false,
                                        position: {
                                            left: v.controlType == 'chop-stamp' ? (offsetWidth + target.width) - v.width : ((offsetWidth + v.offsetX) >= ((offsetWidth + target.width) - v.width)?(offsetWidth + target.width) - v.width:offsetWidth + v.offsetX),
                                            top:  currentPositionReverse(v.offsetY, target),
                                            page: page-1,
                                        },
                                    });
                                }
                            })
                        }
                    }else{
                          const target = targets[v.page];
                          const offsetWidth = (maxWidth - target.width) / 2;
                        //填写控件位置
                        // v.position.top = currentPositionReverse(v.offsetY, v.page)
                        v.position =  {
                              left:  parseInt(v.offsetX) + offsetWidth,
                              top:  currentPositionReverse(v.offsetY, target),
                              page: v.page,
                        }
                    }
                })
            })
            console.log(groupControls,'分组后的控件--')
            //按文档进行控件设置
            for (let i = 0; i < docs.value.length; i++) {
                let matchControl = groupControls.find(v=>v.controlDocId == docs.value[i].id)
                setDocumentList(docs.value[i], matchControl?.controls || []);
            }
            if (unref(isDetail)) {
                actions.value = []
            }
            spinning.value = false;
            compState.loading = false;
        }
       

        //整理文档列表
        function setDocumentList(doc, controls) {
            console.log(doc, controls, '整理文档列表')
            let machDoc = documentList.value.find(v => v.signReDocId == doc.id)
            if (machDoc) {
                machDoc.activeControl = controls
            } else {
                const {targets,maxWidth} = pageScaling(doc.images);
                documentList.value.push({
                    active: false,
                    documentName: doc.name,
                    annexFileId: "",
                    targets:targets,
                    maxWidth:maxWidth,
                    signReDocId: doc.id,
                    activeControl: controls,
                    pageSize: doc.docPage,
                    // images: cloneDeep(images.value),
                    images: cloneDeep(doc.images),
                    imageAllHeight:targets[targets.length-1].transformHeight + targets[targets.length-1].height + (images.value.length * CanvasZoom.space),
                    imageLoading: true
                })
            }
            nowDocument.value = documentList.value.find(v => v.signReDocId == docId.value);
            console.log(documentList.value, docId.value, nowDocument.value,'当前文档')

        }
        // 构建文档控件
        function buildResponseControl(controls: any) {
            const controlItem: any = [];
            if (controls && controls.length > 0) {
                // hasWriteControl.value = false;
                
                controls.forEach((item: any) => {
                    let matchDoc = docs.value.filter(v =>v.id == item.signReDocId)[0]
                    let { targets, maxWidth } = pageScaling(matchDoc.images);
                    let target = targets[item.page];
                    let offsetWidth = (maxWidth - target.width) / 2;
                    item.signerInfo = {};
                    const basaeColtrol = getColtrolByType(item.controlType);
                    const isSignControl = ['signature', 'sign-date', 'seal', 'chop-stamp'].filter(v => v == item.controlType);
                    let signer2Count = 0;
                    signerList.value.map((v, index) => {
                        if (v.signerType == 1) {
                            v.senderList.forEach((m, mIndex) => {
                                m.colorIndex = mIndex;
                                if (m.id == item.signerId) {
                                    item.colorIndex = mIndex;
                                    item.signerInfo = {
                                      ...m,
                                      signerType:1,
                                      receiverOrder:mIndex
                                    }
                                }

                            })
                        }
                        if (v.signerType == 2) {
                          signer2Count += 1;
                           let senderLength = 0;
                            const temRes = signerList.value.filter(item => item.signerType == 1 )
                          
                            if (temRes && temRes.length > 0) {
                                senderLength = temRes[0].senderList.length;
                            }
                            
                            if (v.id == item.signerId) {
                                item.colorIndex = (senderLength + index) - 1;
                                item.signerInfo = {
                                  ...v,
                                  signerType:2,
                                  receiverOrder:signer2Count 
                                }
                            }
                        }
                        if (v.signerType == 3) {
                           signer2Count += 1;
                            let senderLength = 0;
                            const temRes = signerList.value.filter(item => item.signerType == 1)
                            let count  = signerList.value.filter(item => item.signerType == 1 || item.signerType == 2).length;;
                            count += 1
                            if (temRes && temRes.length > 0) {
                                senderLength = temRes[0].senderList.length;
                            }
                            let receivePersonalLength = signerList.value.filter(item => item.signerType == 2).length;
                            v.senderList.forEach((m, mIndex) => {
                                m.colorIndex = senderLength + receivePersonalLength + mIndex;
                                if (m.id == item.signerId) {
                                    item.colorIndex = senderLength + receivePersonalLength + mIndex;
                                    item.signerInfo = {
                                    ...m,
                                    signerType:3,
                                    receiverOrder:signer2Count
                                  }
                                }
                            })
                        }
                    })
                    if (!basaeColtrol) {
                        return []
                    }
                    if(['image','dropdown-list','radio','check-box'].includes(item.controlType) && item.properties && item.properties !='"[]"'){
                      // item.widgetList  = item.properties && (typeof item.properties == 'string')?JSON.parse(item.properties):[];
                      item.widgetList  = item.properties;
                    }
                    // item.size={
                    //     width: parseInt(item.width) || basaeColtrol.width,
                    //     height: parseInt(item.height) || basaeColtrol.height,
                    //     minWidth: basaeColtrol.size.minWidth,
                    //     minHeight: basaeColtrol.size.minHeight,
                    // }

                    
                    
                    controlItem.push({
                        // id :parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                        ...item,
                        propertyVoList:item.propertyVoList,
                        properties:item.properties,
                        widgetList:paramBuildWidgets(item.widgetList,target,offsetWidth),
                        id: item.id || '',
                        uid: item.id,
                        unequalId:parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(10),
                        icon: basaeColtrol.icon,
                        showPopover: false,
                        oddEventType: null,
                        isBatch: item.isBatch || false,
                        name: item.name,
                        focus:false,
                        title: basaeColtrol.title,
                        controlType: item.controlType,
                        placeholder: item.placeholder,
                        isRequired: item.isRequired,
                        interfaceParamName: item.interfaceParamName,
                        originType: item.originType,
                        value: item.value,
                        controlClick: false,
                        disabled: isDetail ? true : (isSignControl.length ? false : true),
                        zoom: basaeColtrol.zoom,
                        move: isDetail ? false : (isSignControl.length ? true : false),
                        delete: isDetail ? false : (isSignControl.length ? true : false),
                        required: item.isRequired == 1,
                        attr: basaeColtrol.attr,
                        signReDocId: item.signReDocId,
                        signerId: item.signerId,
                        signerType: item.signerType,
                        // format: (item.controlType == 'sign-date' ? 'yyyy年MM月dd日' : ''),
                        format: item.controlType === 'sign-date' 
                                    ? (item.format && item.format.trim() ? item.format : 'yyyy年MM月dd日')
                                    : '',
                        colorIndex: item.colorIndex,
                        offsetX:item.offsetX,
                        offsetY:item.offsetY,
                        style: {
                            fontSize: parseInt(item.fontSize),
                            fontFamily: item.fontFamily,
                            textAlign: item.textAlign,
                        },
                        size: {
                            width: parseInt(item.width) || basaeColtrol.width,
                            height: parseInt(item.height) || basaeColtrol.height,
                            minWidth: basaeColtrol.size.minWidth,
                            minHeight: basaeColtrol.size.minHeight,
                        },
                        // position: {
                        //     left: item.controlType == 'chop-stamp' ? (offsetWidth + target.width) - item.size.width : ((offsetWidth + item.offsetX) >= ((offsetWidth + target.width) - item.size.width)?(offsetWidth + target.width) - item.size.width:offsetWidth + item.offsetX),
                        //     top:  currentPositionReverse(item.offsetY, target),
                        //     page: parseInt(item.page),
                        // },
                        // position: {
                        //     left:recaculateBatchControlPosInPage(item, targets, item.offsetX, item.offsetY, maxWidth).x,
                        //     top: recaculateBatchControlPosInPage(item, targets, item.offsetX, item.offsetY, maxWidth).y,
                        //     page: parseInt(item.page),
                        // },
                        position:{
                          left:0,
                          top:0,
                          page:0
                        },
                        user: {}
                    })
                })
            }
            return controlItem;
        }

        const controlMove: any = reactive({
            activeControl: [],
            controlList: [],
            disabled: false,
            activeDom: {},
            elementMove: {
                id: "",
                controlClick: false,
                position: {
                    left: 0,
                    top: 0
                },
                size: {
                    width: 0,
                    height: 0,
                    minWidth: 0,
                    minHeight: 0,
                },
            },
            nowPoint: {
                x: 0,
                y: 0
            },
        });


        //刷新控件状态
        function refreshControls(element:any,isAdd:boolean) {
            if (signers.value) {
                controlMove.activeControl = nowDocument.value.activeControl.filter((item: any) => {
                    return item.user.index == signers.value.index;
                })
            } else {
                controlMove.activeControl = nowDocument.value.activeControl;
            }
            controlMove.activeControl = nowDocument.value.activeControl;
            if(element.controlType == 'chop-stamp' && isAdd){
                const elementTarget = nowDocument.value.targets[element.position.page];
                const elementOffsetY = currentPosition(element.position.top,elementTarget);
                
                for(let i=0;i<nowDocument.value.pageSize;i++){
                  const target = nowDocument.value.targets[i];
                  const offsetWidth = ( nowDocument.value.maxWidth - target.width) / 2;
                    if(i != element.position.page){
                        nowDocument.value.activeControl.push({
                            ...element,
                            position:{
                                left:  (offsetWidth + target.width) - element.size.width, 
                                top:  currentPositionReverse(elementOffsetY, target),
                                page:i,
                            }
                        })
                    }
                }
            }

            //计算批量控件位置

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
          if(element.showXRange || element.showYRange){
            showRange.value = true
          }else{
            showRange.value = false
          }
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
            docKeys =   element.propertyVoList.filter(item => item.propertyType === "relation_doc").map(item => item.propertyValue);
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
            nowDocument.value = documentList.value.find(v => v.signReDocId == docId.value)
        }

        //控件设置应用于选定文档的指定页码
        function handleSetControl(docKeys, element) {
            // if (element.isBatch) return;
            //将当前点击的控件设置为批量控件
            console.log(element,'指定页数')
            // element.isBatch = true;
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
            //过滤掉关联文档配置
            element.propertyVoList = element.propertyVoList.filter(v => v.propertyType != 'relation_doc');
            //更新各个文档的propertyVoList
            documentList.value.map(v => {
                if (docKeys.includes(v.signReDocId)) {
                    element.propertyVoList.push(
                        {
                            controlId: element.id,
                            id:'relation_doc_id_' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                            propertyType:"relation_doc",
                            propertyValue:v.signReDocId,
                        }
                    )
                }
            })
            console.log(element.propertyVoList, '控件propertyVoList')
            //多个文档指定同一单页码
            let docsUid
            if(docKeys.length>1){
                docsUid =  'common_doc_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
            }

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
                const minTarget  = findMinImageSize(documentList.value, docKeys, Array.from(new Set(pages)));
                minTargetInfo.value = minTarget;
                const elementTarget = nowDocument.value.targets[element.position.page];
                const elementOffsetWidth = ( nowDocument.value.maxWidth - elementTarget.width) / 2;
                const elementOffsetX = element.position.left - elementOffsetWidth;
                const elementOffsetY = currentPosition(element.position.top,elementTarget);

            documentList.value.map(v => {
                

                if (docKeys.includes(v.signReDocId)) {
                    v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
                    if (element.oddEventType == 'custom') {
                        let pages = parseInputPages(element.custom.replace(/\s/g, ''));
                        const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                        pages.map(page => {
                                const target = v.targets[page-1];
                                const offsetWidth = ( v.maxWidth - target.width) / 2;
                                let unequalId = buildUUID(); 
                                if(page <= v.pageSize){
                                    v.activeControl.push({
                                        ...element,
                                        minTarget,
                                        isBatch:(pages.length>1 || v.pageSize>1)   ?true:false,
                                        unequalId,
                                        signReDocId:v.signReDocId,
                                        signerId:element.signerId,
                                        controlDocId:v.signReDocId,
                                        controlClick: false,
                                        uid:docKeys.length>1?docsUid:element.uid || frontUid,
                                        page: page - 1,
                                        controlPageInfo:{
                                            controlDocId:v.signReDocId,
                                            propertyType:"page_config",
                                            propertyValue:'custom',
                                            id:element.uid + '_page_config',
                                            controlId:element.uid 
                                        },
                                        propertyVoList:element.propertyVoList,
                                        position:{
                                          left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:page - 1}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                          top: recaculateBatchControlPosInPage({...element,position:{...element.position,page:page - 1}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                                          page:page - 1,
                                        },
                                    })
                                }else{
                                    pages = pages.filter(v=>v != page)
                                }
                                element.propertyVoList.map(v=>{
                                    if(v.propertyType == "custom"){
                                        console.log(pages,'页码')
                                        v.propertyValue = pages.join(',')
                                    }
                                })
                        })
                        return
                    }else{
                        const frontUid = 'api_uid_'+parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
                        for (let i = 0; i < v.pageSize; i++) {
                            if (element.oddEventType == 'all') {
                                let unequalId = buildUUID() + '_' +i; 
                                // 计算当前控件在当前页的偏移量
                                const target = v.targets[i];
                                const offsetWidth = ( v.maxWidth - target.width) / 2;
                                const offsetY = currentPosition(element.position.top,target); 
                                console.log(offsetY,recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget),'图片信息')
                                v.activeControl.push({
                                    ...element,
                                    minTarget,
                                    isBatch: (v.pageSize>1 || docKeys.length>1)?true:false,
                                    unequalId,
                                    signReDocId:v.signReDocId,
                                    signerId:element.signerId,
                                    controlDocId:v.signReDocId,
                                    controlClick: false,
                                    page: i,
                                    propertyVoList:element.propertyVoList,
                                    uid: element.uid || frontUid,
                                    controlPageInfo:{
                                        controlDocId:v.signReDocId,
                                        propertyType:"page_config",
                                        propertyValue:element.oddEventType,
                                        id:element.uid + '_page_config',
                                        controlId:element.uid 
                                    },
                                    position: {
                                      left: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).x,
                                      top: recaculateBatchControlPosInPage({...element,position:{...element.position,page:i}}, v.targets, elementOffsetX, elementOffsetY, v.maxWidth, minTarget).y,
                                      page:i,
                                    },
                                })

                            }
                            if (element.oddEventType == 'odd_number') {
                                if ((i + 1) % 2 != 0) {9
                                    let unequalId = buildUUID() 
                                    const target = v.targets[i];
                                    const offsetWidth = ( v.maxWidth - target.width) / 2
                                    v.activeControl.push({
                                        ...element,
                                        minTarget,
                                        unequalId,
                                        isBatch: (v.pageSize>2 || docKeys.length>1)?true:false,
                                        uid: element.uid || frontUid,
                                        signReDocId:v.signReDocId,
                                        signerId:element.signerId,
                                        controlClick: false,
                                        controlDocId:v.signReDocId,
                                        propertyVoList:element.propertyVoList,
                                        page: i,
                                        controlPageInfo:{
                                            controlDocId:v.signReDocId,
                                            propertyType:"page_config",
                                            propertyValue:element.oddEventType,
                                            id:element.uid + '_page_config',
                                            controlId:element.uid 
                                        },
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
                                    let unequalId = buildUUID() 
                                    const target = v.targets[i];
                                    const offsetWidth = ( v.maxWidth - target.width) / 2
                                    v.activeControl.push({
                                        ...element,
                                        minTarget,
                                        unequalId,
                                        isBatch: (v.pageSize>3 || docKeys.length>1)?true:false,
                                        controlClick: false,
                                        signerId:element.signerId,
                                        propertyVoList:element.propertyVoList,
                                        signReDocId:v.signReDocId,
                                        page: i,
                                        controlDocId:v.signReDocId,
                                        controlPageInfo:{
                                            controlDocId:v.signReDocId,
                                            propertyType:"page_config",
                                            propertyValue:element.oddEventType,
                                            id:element.uid + '_page_config',
                                            controlId:element.uid 
                                        },
                                        uid: element.uid || frontUid,
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
                    // 自动切换到另一有该控件的文档
                    // if(v.signReDocId != docId.value){
                    //     handleDocChange(v.signReDocId)
                    // }
                }
            })
            let controls = documentList.value.flatMap(item => item.activeControl);
            controls.map(item=>{
                item.isBatch = false;
                let matchSameUid = controls.filter(u=>u?.uid==item?.uid && ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType))
                console.log(matchSameUid,'相同数量的控件个数')
                if(matchSameUid.length>1){
                    matchSameUid.map((u:any)=>{
                        u.isBatch = true;
                    })
                }
            })

            //更新当前文档
            nowDocument.value = documentList.value.find(v => v.signReDocId == docId.value);
            console.log(documentList.value, '当前文档')
        }


        //控件拖拽完成事件
        function controlsDragOver(e: any, element: any) {
          console.log(element,'当前拖拽的控件---')
            controlMove.elementMove = element;
            refreshControls(element,true);
        }
        //删除控件
        function controlDelete(element: any) {
            element.controlClick = false;
            console.log("delete control:", element, nowDocument.value.activeControl)
            deleteIdList.value.push(element.uid);
            
            documentList.value.map(v => {
                v.activeControl = v.activeControl.filter(item => (item.uid != element.uid));
            })
            nowDocument.value = documentList.value.find(v => v.signReDocId == docId.value)
            refreshControls(element,false);
        }
        //控件点击操作
        function controlMousedown(element: any) {
            controlMove.elementMove = element;
            documentList.value.map(v => {
                v.activeControl.forEach(item => {
                    item.controlClick = false
                });
            })
            element.controlClick = true;
        }

        //文档控件滚动定为
        function handleScroll(doc, el) {
          console.log(doc,'当前文档---')
          console.log(el,'当前控件---')
            compState.loading = true;
            if (doc.signReDocId != docId.value) {
                handleDocChange(doc.signReDocId)
                setTimeout(() => {
                    let scrollDis = getAccumulatedHeight(doc.targets,  el.position.page==0?0:el.position.page-1)
                    // document.getElementsByClassName('doc-content')[0].scrollTo(0, el.position.top);
                    document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
                }, 2000)
            } else {
                let scrollDis = getAccumulatedHeight(doc.targets,  el.position.page==0?0:el.position.page-1)
                document.getElementsByClassName('pos-'+ el.position.top)[0].scrollIntoView({ behavior: 'smooth', block: 'center' })
                // document.getElementsByClassName('doc-content')[0].scrollTo(0,  el.position.top);
            }
            documentList.value.map(v => {
                v.activeControl.forEach(item => {
                    item.controlClick = false
                    item.zoom = false
                    item.focus = false;
                    if(item.uid == el.uid){
                      item.controlClick = true
                      item.focus = true;
                    }
                });
                
            })
            setTimeout(() => {
                compState.loading = false;
            }, 500)
        }

        // 合并提交数据
        function mergedDataFn(inputData){

            const mergedObjects = {};

            inputData.forEach(obj => {
            const uid = obj.uid;

            if (!mergedObjects[uid]) {
                // 如果 mergedObjects 中不存在该 ID，则创建一个新的对象
                mergedObjects[uid] = { ...obj };
            } else {
                // 如果已经存在该 ID，则将属性合并到现有对象中
                Object.assign(mergedObjects[uid], obj);
            }
            });

            // 将合并后的对象转换为数组
            const resultArray = Object.values(mergedObjects);

            return resultArray;
        } 
        //保存基本数据
        async function saveFormData() {

            return new Promise(async (resolve, reject) => {
                let controls = documentList.value.flatMap(item => item.activeControl);
                console.log(controls,)
                let paramsControl: any = [];
                controls.map(item => {
                  const matchDoc = documentList.value.filter(v =>v.signReDocId ==item.controlDocId)[0];
                  const targets = matchDoc.targets;
                  const target = targets[item.position.page];
                  const offsetWidth = (matchDoc.maxWidth - target.width) /2;
                    //匹配到为接收方 否则为发起方
                    // console.log(signerList.value.find(m=>m.id == item.signerId),'填写方信息')
                    let matchSigner = signerList.value.find(m => m.id == item.signerId);
                    item.signerType = matchSigner?.signerType;
                    // if(item.propertyVoList){
                    //     item.propertyVoList.map(v=>{
                    //         if(v.propertyType == 'custom'){
                    //             if(!item.isBatch){
                    //                  v.propertyValue = item.position.page+1;
                    //             }
                    //             // item.page = v.propertyValue;
                    //             // 还原为最近使用的值
                               
                    //         }
                    //     })
                    // }
                    if(!item){
                        return
                    }
                    console.log(item.propertyVoList,'控件信息---')
                    paramsControl.push({
                        // "controlPageInfo":item.controlPageInfo,
                        "propertyVoList": item.propertyVoList,
                        "properties": item.properties,
                        "controlDocId":item.controlDocId,
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
                        "signReDocId": item.signReDocId || unref(docId),
                        "originType": item.originType,
                        "relatedDocId": 0,
                        "signerId": item.signerId,
                        "signerType": item.signerType,
                        "relatedDocType": 0,
                        "required": item.required ? 1 : 2,
                        "interfaceParamName": item.interfaceParamName || null,
                        "textAlign": item.style.textAlign,
                        "controlType": item.controlType,
                        "value": item.value,
                        // "format": item.controlType == 'sign-date' ? 'yyyy年MM月dd日' : '',
                        "format": item.controlType === 'sign-date' 
                                    ? (item.format && item.format.trim() ? item.format : 'yyyy年MM月dd日')
                                    : '',
                        "width": item.size.width,
                        "written": 1,
                        "uid":item.uid,
                        "id": item.id || '',
                        "tenantId": item.user.tenantId,
                    })
                })
                console.log(paramsControl,'提交的控件--')
                console.log(mergedDataFn(paramsControl),'提交整理的控件--')
                // return
                let canSubmit = true;
                for (const item of paramsControl) {
                    if (!item.signerId) {
                        canSubmit = false
                        isSubmiting.value = false;
                        msg.warning('模板参数填写方未全部设置');
                        break;
                    }
                }
                if (!canSubmit) {
                    isSubmiting.value = false;
                    return
                }
                // isSubmiting.value = false;
                

              
                compState.loading = true;
                let result = await setBusinessLineControl({ controlChangeFlag:unref(controlChangeFlag),signReDocId: unref(docId), signReId: signReId, controlList: mergedDataFn(paramsControl), deleteIdList: unref(deleteIdList) });
                if (result) {
                    compState.loading = false;
                    paramsControl = []
                    setTimeout(() => {
                        isSubmiting.value = false;
                    }, 1000)
                    resolve(result)
                } else {
                    isSubmiting.value = false;
                    compState.loading = false;
                    reject(result)
                }
            })
        }
        //更新控件id防止一个控件添加多次
        async function updateControlsId() {
            isSubmitControl.value = true;
            getAllDocControls(true)
        }

        //保存并返回
        function handleSaveAndClose() {
            handleSave('close')
        }
        //保存控件
        async function handleSave(type) {
            if (isSubmiting.value) {
                return;
            }
            isSubmiting.value = true;
            saveFormData().then(async (res) => {
                if (res) {
                    // handleDocChange(docId.value)
                    updateControlsId()
                    if (type == 'close') {
                        handlePosCancel()
                    }
                    msg.success('保存成功')
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
            controlFocus,
            docs,
            docId,
            height,
            actions,
            nowDocument,
            documentList,
            signerList,
            controlsList,
            controlMove,
            controlMousedown,
            controlsDragOver,
            controlChangeFlag,
            controlDelete,
            signers,
            handleDocChange,
            handleScroll,
            spinning,
            paramsWriterRef,
            handlePosCancel,
            ...toRefs(compState),
            needWrite, docContentRef,
            isSubmitControl,
            handleSetControl,
            hasWriteControl,
            refreshControlPosition,
            signDateFormat,chopStampUseFlag,
            showRange,
            minTargetInfo
        }
    }
})
</script>

<style lang="less" scoped>
// .businessline-position-container{
//   margin-top:-20px;
// }
.position-params-body {
    overflow: hidden;
    height: 100%;
}
.signer-node-set {
  p{
    margin-bottom:0;
    font-size:12px;
  }
}

.doc-select {
    min-width: 100px;

    :deep(.ant-select-selector) {
        border: none;
    }

    :deep(.ant-select-selection-item) {
        font-size: 18px;
        font-weight: 550;
    }
}

</style>

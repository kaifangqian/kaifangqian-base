<!--
  @description 文档签署页面

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
  <div class="sign-contract">
    <Loading :loading="signLoading" text="数据提交中..." />
    <!-- 添加签署控件提示区域 -->
    <!-- <div class="sign-notice-bar-fixed" v-if="showSignControlTip && signControlTip">
      <van-notice-bar
        mode="closeable"
        wrapable
        :scrollable="false"
        color="#1989fa"
        background="#ecf9ff"
        @close="closeSignControlTip"
      >
        {{ signControlTip }}
      </van-notice-bar>
    </div> -->
    <div class="contract-header">
       <!-- 当只有一个文件时，显示普通文本 -->
      <div v-if="documentList && documentList.length === 1" class="single-doc-display">
          {{ documentList[0].name }}
      </div>
      <!-- 当有多个文件时，显示下拉列表 -->
      <div class="doc-list" v-else>
        <van-dropdown-menu>
          <van-dropdown-item v-model="docId" :options="documentList" placeholder="请选择" @change="handleDocChange" />
        </van-dropdown-menu>
      </div>
      <div class="doc-operation">
        <van-space>
          <van-button plain hairline type="primary" size="mini" style="width: 60px"
            @click="handleWriteProcess">进度</van-button>
          <van-button plain hairline type="primary" size="mini" style="width: 60px"
            @click="handleDetail">详情</van-button>
        </van-space>
      </div>
    </div>
    <div class="doc-content">
      <div class="zoom-container" :style="[
        'width:' + (nowDocument && nowDocument.maxWidth * (zoom / 100)) + 'px',
        'height: ' + (nowDocument && nowDocument.imageAllHeight) * (zoom / 100) + 'px;',
      ]">
        <div class="document-list" :style="[
          'height: ' + nowDocument.imageAllHeight + 'px;',
          'width:' + nowDocument.maxWidth + 'px;',
          'transform:scale(' + zoom / 100 + ')',
          'transform-origin: 0% 0%',
        ]" v-if="nowDocument && nowDocument.images && nowDocument.imageLoading">
          <template v-for="(item, index) in nowDocument.images">
            <div class="document-page group" v-if="item" :style="[
              'transform: translate(' +
              '-50%,' +
              (item.target.transformHeight + (item.page + 1) * 16) +
              'px);',
              'width:' + item.target.width + 'px;',
              'height:' + item.target.height + 'px;',
            ]" :key="index">
              <img style="width: 100%" v-lazy="baseUrl + '/file/downloadFileStream/' + item.annexId" />
              <div class="img-range-line" v-if="item.showRange" :style="[
                'height:' + minTargetInfo.minHeight + 'px',
                'width:' + minTargetInfo.minWidth + 'px',
                'position: absolute;',
                'left: 0;',
                'top: 0;',
                'border: 1px dashed #00a0e8',
              ]"></div>

              <div class="img-range-line" v-if="item.showChopStampRange" :style="[
                'height:' + 1 + 'px',
                'width:' + 100 + '%',
                'position: absolute;',
                'left: 0;',
                'top:' + item.rangHeight + 'px',
                'border-bottom: 1px dashed #00a0e8',
              ]"></div>
              <!-- 页面编号角标 -->
                <div class="page-corner">
                </div>
                <div class="page-num">
                  {{ item.page + 1 }}
                </div>
            </div>
          </template>
          <!-- <draggable :list="nowDocument.activeControl" ghost-class="ghost" chosen-class="chosenClass" animation="300"
            :fallback-on-body="false" :force-fallback="true" :group="group" :scroll="false" :touch-start-threshold="50"
            :fallback-tolerance="50" style="width: 100%; height: 100%" key="unequalId">
            <control-item ref="controlItemRef" :doc="nowDocument" :zoom="zoom / 100" :element="element"
              @controlDelete="controlDelete" :isSign="true" v-for="(element, index) in nowDocument.activeControl"
              :key="index" @controlClick="controlShowMenu" @controlMenu="controlMenu"
              @controlMousedown="controlMousedown" @refreshControlPosition="refreshControlPosition">
            </control-item>
          </draggable> -->
        </div>
        <div class="document-control"> </div>
      </div>
    </div>

    <van-action-sheet v-model:show="processVisible" title="签署进度" :closeable="false" cancel-text="关闭">
      <div class="process-content">
        <ul>
          <li class="signer-item" v-for="(item, index) in signerList" :key="index">
            <van-tag class="signer-tag" primary type="primary">{{
              item.signerType == 1
                ? '发起方'
                : '接收方' + (hasSenderSigner ? (index == 0 ? index + 1 : index) : index + 1)
            }}
            </van-tag>
            <div class="signer-info">
              <span class="signer-title" v-if="item.signerType != 2">{{ item.signerName }}</span>
              <!-- <span class="signer-title" v-if="item.signerType == 2"> 个人 </span> -->
            </div>
            <template v-for="(senderItem, senderIndex) in item.senderList" :key="senderIndex">
              <div class="sender-info" v-if="item.signerType == 1">
                <!-- <span class="sender-title">{{ senderItem.senderName }}</span> -->
                <span class="sign-flow-name sender-title">{{
                  senderItem.senderName +
                  ' — ' +
                  '[' +
                  (senderItem.senderSignType == 1 ? '自动盖章' : senderItem.senderUserName) +
                  ']'
                  }}</span>
                <van-tag v-if="senderItem.senderType == 5" :color="loadApprovalColor(senderItem.signStatus)">{{
                  loadApprovalStatus(senderItem.signStatus)
                  }}</van-tag>
                <van-tag v-if="senderItem.senderType != 5" :color="loadSignColor(senderItem.signStatus)">{{
                  loadSignStatus(senderItem.signStatus)
                  }}</van-tag>
              </div>
            </template>
            <template v-if="item.signerType == 2">
              <div class="sender-info">
                <span class="sender-title">{{ item.signerName }}</span>
                <span>{{ item.signerExternalValue }} </span>
                <van-tag :color="loadSignColor(item.signStatus)">{{
                  loadSignStatus(item.signStatus)
                  }}</van-tag>
              </div>
            </template>
            <template v-for="(senderItem, senderIndex) in item.senderList" :key="senderIndex">
              <div class="sender-info" v-if="item.signerType == 3">
                <!-- <span class="sender-title">{{ senderItem.senderType == 1 ? '经办人签字' : '组织签章' }}</span> -->

                <span class="sign-flow-name sender-title">{{
                  (senderItem.senderType == 1 ? '经办人签字' : '组织签章') +
                  ' — ' +
                  '[' +
                  senderItem.senderName +
                  ']'
                  }}</span>
                <van-tag :color="loadSignColor(senderItem.signStatus)">{{
                  loadSignStatus(senderItem.signStatus)
                  }}</van-tag>
              </div>
            </template>
          </li>
        </ul>
      </div>
    </van-action-sheet>
    <van-action-sheet v-model:show="detailVisible" title="文档详情" :closeable="false" cancel-text="关闭">
      <SignDetail :signRuId="signRuId" ref="signDetailRef" />
    </van-action-sheet>

    <!-- 文档切换icon -->
    <div class="sign-document-update" v-show="documentList.length > 1"> 
      <van-button
      class="custom-buttons"
      type="primary"
      @click="quickHandleDocChange"
      size="small"
      >
      <SvgIcon name="doc-update" size="28" style="text-align: center;display: flex;align-items: center;justify-content: center;" />
      <!-- 文档数量气泡 -->
      <van-badge :content="documentList.length" class="count-badge" />
      </van-button>
      <div class="doc-footer-operation-tools">
        <span class="doc-footer-operation-text">文档切换</span>
      </div>
    </div>
    <div class="doc-footer-operation">
      <div style="text-align: center">
        <van-space :size="20">
          <van-button class="custom-button-minor" type="primary" size="small" @click="wakeUpComment('reject')"
            :disabled="isDetail">驳回</van-button>
          <van-button class="custom-button-primary" type="primary" size="small"  @click="wakeUpComment('pass')"
            :disabled="isDetail">审批通过</van-button>
        </van-space>
      </div>
    </div>
  
    <v-dialog v-model:show="taskVisible" title="任务提醒" :showCancelButton="false" :show-confirm-button="false"
      className="sign-task-tip-modal">
      <p>监测到该签约文档中您还有【{{ taskInfo.taskType == 'sign' ? '签署 ' : taskInfo.taskType == 'approval' ? '审批 ' : '填写' }}】任务，是否前去处理？</p>
      <div :style="{ textAlign: 'center' }">
        <van-button type="default" @click="handleNoTask">暂不处理</van-button>
        <van-button type="primary" @click="handleGoNextTask">立即处理</van-button>
      </div>
    </v-dialog>
  </div>
  <!-- <ConfirmModal ref="confirmRef" @success="handleConfirmSuccess" /> -->
  <v-dialog v-model:show="commentRejectVisible" title="审批驳回" show-cancel-button @confirm="submitApprovalData('reject')" >
    <van-field class="comment-textarea" v-model="comment" rows="4" autosize label="" type="textarea" maxlength="200"
      placeholder="请填写审批原因，200字以内（非必填）" show-word-limit />
      <div class = "comment-notice">
        <p>请注意，驳回操作将导致整个签署流程终止，请谨慎处理。</p>
      </div>
  </v-dialog>
  <v-dialog v-model:show="commentVisible" title="审批通过" show-cancel-button @confirm="submitApprovalData('pass')" >
    <van-field class="comment-textarea" v-model="comment" rows="4" autosize label="" type="textarea" maxlength="200"
      placeholder="请填写审批原因，200字以内（非必填）" show-word-limit />
      <div class = "comment-notice">
        <p>审批通过后，流程将进入到下一环节，无法修改，请知悉。</p>
      </div>
  </v-dialog>
</template>

<script lang="ts">
import {
  ref,
  defineComponent,
  onMounted,
  nextTick,
  onActivated,
  onBeforeUnmount,
  watch,
  h,
} from 'vue';
import { getCanvasZoom } from '@/utils/ControlData';

import { ImagePreview, Notify, Dialog, NoticeBar } from 'vant';
import Api from '@/api/contract/index';
import ControlItem from '@/pages/components/items/ControlMoveItem.vue';
import SignDetail from '@/pages/components/SignDetail.vue';
// import ConfirmModal from '@/pages/components/ConfirmModal.vue';
import { loadSignColor, loadSignStatus, loadApprovalColor, loadApprovalStatus} from '../transform';
import { VueDraggableNext } from 'vue-draggable-next';
import { decodeURIs } from '@/utils';
import { getHashQueryString } from '@/utils/util';
import { useUserStore } from '@/store/modules/user';
import {
  pageScaling,
} from '@/pages/components/data/ControlerMoveRange';
import {
  CanvasZoom,
} from '@/pages/components/data/ControlData';
import session from '@/utils/cache/session';

interface SignItem {
  signerType: number;
  signerName: string;
  signerExternalValue: string;
  writeStatus: string;
  senderList: any[];
}

enum TenantTypeEnum {
  PERSONAL = 1,
  ENTERPRISE = 2,
}

export default defineComponent({
  name: 'signContract',
  components: {
    draggable: VueDraggableNext,
    ControlItem,
    SignDetail,
    'v-dialog': Dialog.Component,
    // ConfirmModal,
  },
  setup() {
    // console.log(CanvasZoom, 'sdsfd');

    const userStore = useUserStore();
    const docs: any = ref([]);
    const docKeys = ref(<string[]>[]);
    const checkboxRefs = ref([]);
    const toggle = (index: number) => {
      checkboxRefs.value[index].toggle();
    };
    const commentVisible = ref(false);
    const commentRejectVisible = ref(false);
    const comment = ref('');
    const docId = ref('');
    const annexId = ref('');
    const images = ref([]);
    const isDetail = ref(false);
    const taskVisible = ref(false);
    const taskType = ref('');
    const taskInfo: any = ref({});
    const signLoading = ref(false);
    const detailVisible = ref(false);
    const processVisible = ref(false);
    const hasSenderSigner = ref(false);
    const signerList = ref(<SignItem[]>[]);
    const signDetailRef = ref(null);
    const callbackPage = getHashQueryString('callbackPage');
    const partyName = getHashQueryString('partyName')
      ? decodeURIComponent(getHashQueryString('partyName') as string)
      : '';
    
    const baseImgWidth = window.innerWidth - 20;

    const zoom = ref(100);
    const group = ref({
          name: 'itxst',
          put: true, //允许拖入
          pull: 'clone', //允许拖出
        });

    // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
    let baseUrl = import.meta.env.VITE_APP_API_BASE_URL;
    const documentList: any = ref([]);

    //   const tenantInfo = session.getItem('login_tenant_info');
    const tenantInfo = userStore.getTenantInfo;

    const userInfo = userStore.getUserInfo;


    const clientWidth = ref(document.body.clientWidth);
    const clientHeight = ref(document.body.clientHeight);
    // console.log('clientHeight----',clientHeight.value);
    const docList = ref([{}]);
    const nowDocument: any = ref({
      selectValue: '',
    });
    const minTargetInfo = ref();
    const fromPath = ref('');

    const router = useRouter();
    const route = useRoute();
    const signRuId = route.query.signRuId as string;
    const taskId = route.query.taskId as string;
    session.setItem('sign_task_id', taskId);


    onBeforeUnmount(() => {
      Notify.clear();
    });
    onMounted(() => {
      checkStatus();
    });

    watch(
      () => nowDocument.value.maxWidth,
      (val) => {
        if (val) {
          zoom.value = Math.round(((window.innerWidth - 20) / val) * 100);
        }
      },
      { deep: true }
    );

    //根据状态和当前操作人是否进行过签署进行路由重定向到签署详情或者文档详情页
    async function checkStatus() {
      let { result, code } = await Api.checkOperatorStatus({ signRuId: signRuId });
      if (code == 200) {
        if (result.checkStatus != 1) {
          router.replace({
            name: '无权访问',
            query: {
              signRuId,
              taskId,
              taskType: 'approval',
              callbackPage: callbackPage,
              partyName: partyName,
            },
          });
        }
        if (
          result.ruStatus == 6 ||
          result.ruStatus == 8 ||
          result.ruStatus == 9 ||
          result.ruStatus == 11
        ) {
          router.replace({
            name: '详情',
            query: {
              ...route.query,
              callbackPage: callbackPage,
            },
          });
          return;
        }
        if (result.operatorStatus == 2) {
          router.replace({
            name: '详情',
            query: {
              ...route.query,
              isDetail: 'true',
              callbackPage: callbackPage,
            },
          });
        }
        setTimeout(async () => {
          isDetail.value = route.query?.isDetail == 'true' ? true : false;
          if (unref(isDetail)) {
            // 详情页左侧没有控件从这里请求文件
          } 
          init();
        });
      }
    }
    // 获取文档
    async function init() {
      // console.log(Api, '接口api');
      let { result } = await Api.getDocFiles({ signRuId: signRuId });
      if (result && result.length) {
        documentList.value = result;
        docId.value = result[0].id;
        annexId.value = result[0].annexId;
        documentList.value.map(async (v: any, i: number) => {
          v.pageSize = v.docPage;
          let { result } = await Api.getDocImgsInSign({ signFileId: v.id });
          if (result && Array.isArray(result)) {
            v.images = result.sort((a, b) => a.page - b.page);
            // if (i == 0) {
            //   images.value = result;
            // }
            v.images = result;
            const {targets,maxWidth} = pageScaling(result);
            v.targets = targets;
            v.maxWidth = maxWidth;
            v.imageLoading=true;
            v.imageAllHeight=targets[targets.length-1].transformHeight + targets[targets.length-1].height + (result.length * CanvasZoom.space);
            v.text = v.name;
            v.value = v.id;
          }
          if(v.id==docId.value){
              nowDocument.value = v;
            }
        });
        console.log('documentList.value----',documentList.value);
        // getDocImgs();
      }
    }

    //文档切换
    async function quickHandleDocChange() {
      // console.log('快速文档切换----');
      // 遍历文档列表，按照顺序进行切换
      // 若当前文档是最后一个，则切换到第一个文档
      // 若当前文档不是最后一个，则切换到下一个文档
      const currentIndex = documentList.value.findIndex((v: any) => v.id == docId.value);
      let nextIndex = 0;
      if (currentIndex < documentList.value.length - 1) {
        nextIndex = currentIndex + 1;
      }
      docId.value = documentList.value[nextIndex].id;
      nowDocument.value = documentList.value[nextIndex];
      handleDocChange(docId.value);
    }
    //文档切换
    async function handleDocChange(val: any) {
      console.log('文档切换----',val);
      let matchDoc = documentList.value.find((item) => item.id == val);
      nowDocument.value = matchDoc;

      docId.value = val;
      document.getElementsByClassName('doc-content')[0].scrollTop = 0;
    }

    //获取图片
    async function getDocImgs() {
      let { result } = await Api.getDocImgsInSign({ signFileId: docId.value });
      if (result) {
        images.value = result;
        documentList.value.map((v) => {
          if (v.id == docId.value) {
            v.images = result;
            nowDocument.value.images = result;
            const {targets,maxWidth} = pageScaling(result);
            v.targets = targets;
            v.maxWidth = maxWidth;
            v.imageLoading=true;
            v.imageAllHeight=targets[targets.length-1].transformHeight + targets[targets.length-1].height + (result.length * CanvasZoom.space);
          }
        });
      }
    }

    //进度
    function handleWriteProcess() {
      //进度
      processVisible.value = true;
      getWriteProcess();
    }
    //详情
    function handleDetail() {
      detailVisible.value = true;
      nextTick(() => {
        (signDetailRef.value as any) && (signDetailRef.value as any).getRuInfo();
      });
    }
    async function getWriteProcess() {
      let { result } = await Api.getOperators({ signRuId });
      if (result) {
        signerList.value = result.sort((a: any, b: any) => a.signerOrder - b.signerOrder);
        signerList.value.map((v: any) => {
          if (v.signerType == 1) {
            hasSenderSigner.value = true;
          }
          if (v.senderList) {
            v.senderList.sort((a: any, b: any) => a.senderOrder - b.senderOrder);
          }
        });
      }
    }
    // const
    const CanvasZoomBase = ref(getCanvasZoom(clientWidth.value - 20, 100));
    console.log(CanvasZoomBase, clientWidth.value, ' 00000000');
    function previewImages() {
      ImagePreview({
        images: [doc1, doc2],
        closeable: true,
        startPosition: 1,
      });
    }

    function initPage() {
      const params = {
        pageNo: 1,
        pageSize: 10,
      };
      // listMyJob(params);
      // console.log("listMyJob",Api.listMyJob(params));
    }
    onMounted(() => {
      initPage();
      //ubject=&status=&code=&pageNo=1&pageSize=10&_t=1706948188035
    });

    //继续任务处理
    function handleGoNextTask() {
      if (taskInfo.value.taskType == 'sign') {
        router.push({
          path: '/signContract',
          query: {
            signRuId: taskInfo.value.ruId,
            taskId: taskInfo.value.taskId,
            callbackPage:callbackPage,
          },
        });
        setTimeout(() => {
          location.reload();
        }, 500);
      }
      if (taskInfo.value.taskType == 'approval') {
        router.push({
          path: '/approval',
          query: {
            signRuId: taskInfo.value.ruId,
            taskId: taskInfo.value.taskId,
            callbackPage:callbackPage,
          },
        });
        setTimeout(() => {
          location.reload();
        }, 500);
      }
      if (taskInfo.value.taskType == 'write') {
        router.replace({
          path: '/write',
          query: {
            signRuId: taskInfo.value.ruId,
            taskId: taskInfo.value.taskId,
          },
        });
        setTimeout(() => {
          location.reload();
        }, 500);
      }
    }
    //暂不处理业务
    function handleNoTask() {
      taskVisible.value = false;
      if(callbackPage && typeof callbackPage == 'string'){ 
        window.open(decodeURIs(callbackPage),'_self')
      }else{
        router.push('/index');
      }
    }

    function wakeUpComment(type:string) {
      if (type === 'reject') {
        commentRejectVisible.value = true;
        commentVisible.value = false;
      }else {
        commentRejectVisible.value = false
        commentVisible.value = true;
      }
      comment.value = '';
    }

    // function beforeClose(action) {
    //   new Promise((resolve) => {
    //     if (
    //       (!comment.value && comment.value.length > 200)
    //     ) {
    //       Notify({ type: 'danger', message: '审批原因请控制在200字之内' });
    //       resolve(false);
    //     } else if (action === 'cancel') {
    //       commentVisible.value = false;
    //       comment.value = '';
    //       resolve(true);
    //     }
    //   });
    // }


    async function submitApprovalData(type:string){ 
      if (!comment.value && comment.value.length > 200) {
        return;
      }
      commentVisible.value = false;
      commentRejectVisible.value = false;
      let result ;
      let title = '';
      // console.log('comment.value---',comment.value);
      signLoading.value = true;
      if (type == 'pass') {
         result = await Api.submitApproval({
          signRuId,
          comment: comment.value,
        });
        title = '审批已通过!';
      } else {
        result = await Api.rejectApproval({
          signRuId,
          comment: comment.value,
        });
        title = '驳回成功！';
      } 
      if(result && result.code == 200){
        console.log('result---',result);
        Notify({ type: 'success', message: title });
        if(result.taskId && !callbackPage){
          signLoading.value = false;
          taskVisible.value = true;
          taskInfo.value = result;
        } else if (callbackPage && typeof callbackPage == 'string') {
          window.open(decodeURIs(callbackPage), '_self');
        } else {
          signLoading.value = false;
          router.push('/index');
        }
      }else { 
        Notify({ type: 'danger', message: result.message });
        signLoading.value = false;
      }

    }


    function checkElementCustom() {
      let reg = /^(?:(\d+)|(\d+(?:,\d+)*)(?:(?:,|-)\d+(?:,\d+)*)*)$/;
    }


    return {
      docList,
      documentList,
      nowDocument,
      docId,
      CanvasZoomBase,
      previewImages,
      baseUrl,
      signRuId,
      detailVisible,
      signDetailRef,
      handleDetail,
      loadSignColor,
      loadSignStatus,
      processVisible,
      signerList,
      handleWriteProcess,
      submitApprovalData,
      tenantInfo,
      zoom,
      hasSenderSigner,
      isDetail,
      handleDocChange,
      quickHandleDocChange,
      signLoading,
      taskVisible,
      taskType,
      handleGoNextTask,
      group,
      handleNoTask,
      taskInfo,
      docs,
      checkboxRefs,
      docKeys,
      toggle,
      checkElementCustom,
      minTargetInfo,
      commentVisible,
      commentRejectVisible,
      comment,
      // beforeClose,
      wakeUpComment,
      callbackPage,
      clientHeight,
      loadApprovalColor,
      loadApprovalStatus,
    };
  },
});
</script>

<style lang="less" scoped>
.sign-contract {
  height: 100%;
  padding-bottom: 10px;

  :deep(.signatur-modal.van-dialog) {
    z-index: 2001;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    transform: translate(0px, 0px);
    border-radius: 0;

    .van-dialog__footer,
    .van-dialog__header {
      display: none;
    }

    .signature-pad {
      position: relative;
      width: 100%;
      height: 100%;
      // height: 200px;
      overflow: hidden;
      border: 1px solid #ddd;
      background-color: white;
    }
  }

  .signature-context {
    position: absolute;
    top: 50%;
    // left: 50%;
    color: #ccc;
    // color: red;
    font-size: 1rem;
    text-align: center;
    user-select: none;
    pointer-events: none;
    z-index: 1;
    white-space: nowrap;
    transform: rotate(90deg) translate(0%, 100%);
    letter-spacing: 1rem; /* 适中的字符间距 */
    
  }
  /* 可选：让文字更清晰 */
  .signature-context br {
    display: block;
  }

  :deep(.van-dropdown-menu) {
    height: 100%;
  }

  :deep(.van-dropdown-menu__bar) {
    height: 100%;
  }

  .doc-content {
    // width: 100%;
    // // height: calc(100% - 220px);
    // overflow: hidden;
    // overflow-x: hidden;
    // margin-top:60px;
    // height: 100%;
    padding-bottom: 80px;
    overflow: hidden;
    overflow-x: hidden;
  }
}

.contract-header {
  display: flex;
  border-bottom: 1px solid #efefef;
  padding: 10px;
  background-color: #fff;
  height: 72px;
  position: fixed;
  z-index: 999;
  width: 100%;
  left: 0;
  right: 0;
  top: 92px;
}

.doc-list {
  flex: 1;

  // position: relative;
  :deep(.van-dropdown-menu) {
    width: 400px;
    // width: calc(100% - 60px);
  }
}
.single-doc-display {
    display: flex;
    align-items: center;
    height: 100%;
    // width: 100%;
    font-size: 28px;
    font-weight: 500;
    padding-left: 30px;
    color: #333;
    // 添加文本省略号样式
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 70%;
}

.doc-operation {
  width: 240px;
  display: flex;
  justify-content: right;
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
}

.document-list {
  position: relative;
  // margin: 10px auto;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.document-list .document-page {
  // width: calc(100vh - 1px);
  // width: 100%;
  position: absolute;
  top: 0;
  left: 50%;
  border: 1px solid #ccc;
  box-sizing: border-box;
  box-shadow: 0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1);
  // box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); // 添加阴影效果

  img {
    width: 100%;
    height: 100%;
  }
}

.document-zoom {
  // position: absolute;
  bottom: 130px;
  right: 30px;
}

.zoom-container {
  margin: 10px auto 60px;
}

.doc-footer-operation {
  // height: 1.5rem;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding-bottom: 16px;
  padding-top: 16px;
  border: 0.01333rem solid #efefef;

  .add-control {
    display: flex;
    height: 60px;
    line-height: 60px;
    justify-content: center;
    margin: 4px 0 20px;
    background: #1c84d411;

    .sign-control {
      display: flex;
      text-align: center;
      justify-content: center;
    }

    .sign-date-control {
      width: 50%;
      text-align: center;
    }
  }

  :deep(.van-button) {
    width: 300px;
    // margin: 0 20px;
  }
}

.process-content {
  margin-top: 40px;

  li {
    background: #eaf1f7;
    position: relative;
    width: 95%;
    padding: 40px 50px;
    margin: 20px auto;

    span {
      margin-right: 20px;
    }

    .signer-info {
      margin-bottom: 10px;
    }

    .sender-info {
      margin-bottom: 20px;
    }

    .signer-title {
      font-weight: 550;
    }

    .sender-title {
      font-size: 24px;
    }

    .signer-tag {
      position: absolute;
      left: 0;
      top: 0;
    }
  }
}

.certificate-info {
  padding: 10px 40px;
  font-size: 24px;
  text-align: center;

  p {
    text-align: left;
  }
}

.signature-info {
  display: flex;
  align-items: center;

  img {
    width: 100%;
    // margin-left: 30px;
    display: block;
    margin: 0 auto;
  }
}
/* 响应式设计，根据屏幕宽度调整 */
  // @media (max-width: 320px) {
  //   .signature-info {
  //     width: calc(50% - 5px);
  //   }
  // }

  // @media (min-width: 321px) and (max-width: 480px) {
  //   .signature-info {
  //     width: calc(50% - 5px);
  //   }
  // }

  // @media (min-width: 481px) {
  //   .signature-info {
  //     width: calc(33.333% - 5px);
  //   }
  // }

.signature-img{
  width: 180px;
  height: 180px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;

}

:deep(.van-cell__value--alone) {
  display: flex;
  // justify-content: space-between;
  // padding-right: 10px;
  justify-content: center;
}

.sign-seal-img-box {
  text-align: center;
}

.sign-seal-img {
  width: 400px;
}

:deep(.sign-certifcate-modal .van-button) {
  font-size: 24px;
  height: 60px;
}

:deep(.sign-certifcate-tip-modal .van-button) {
  font-size: 24px;
  height: 60px;
  margin-bottom: 20px;
}

:deep(.sign-certifcate-tip-modal) {
  p {
    padding: 0 30px;
    font-size: 24px;
  }
}

.signature-footer {
  position: absolute;
  left: 14px;
  top: -0.8rem;
  transform: rotate(90deg);
  transform-origin: bottom left;
  width: 100%;
  right: 0;
  display: flex;
  align-items: center;

  .signature-btns {
    display: flex;
    justify-content: flex-end; /* 按钮组靠右对齐 */
    gap: 0px; /* 设置按钮间距 */
    margin-top: 10px; /* 可选：增加顶部间距 */
    // padding: 0 10px; /* 可选：增加左右内边距 */
    width: 100%; /* 确保容器占满可用宽度 */
    box-sizing: border-box; /* 包含padding在总宽度内 */
  }

  /* 为按钮添加最小宽度以确保可点击性 */
.signature-btns .van-button {
  min-width: 60px;
  white-space: nowrap; /* 防止按钮文字换行 */
}

  :deep(.van-button) {
    width: 150px;
  }

  footer {
    height: 100px;
    // border:1px solid #999;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: none;
    padding: 0 10px;
  }

  .gtnGroup {
    width: 50%;
    // margin-left: 20px;
  }

  .otherSet {
    width: 50%;
    display: flex;
    align-items: center;

    .penTxt {
      width: 140px;
      font-size: 24px;
    }

    .selSize {
      width: 140px;
    }

    .a-select__caret {
      position: absolute;
      right: -3px;
    }

    .b1,
    .b2,
    .b3 {
      display: inline-block;
      background: #000;
      border-radius: 50%;
    }

    .circleWrap {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 36px;
      height: 36px;
      cursor: pointer;
      margin-right: 20px;
    }

    .active {
      border: 1px dashed #0074d9;
    }

    .b1 {
      width: 8px;
      height: 8px;
    }

    .b2 {
      width: 12px;
      height: 12px;
    }

    .b3 {
      width: 16px;
      height: 16px;
    }
  }
}

.no-seal-list {
  text-align: center;
  font-size: 26px;
  padding: 0 20px;
  min-height: 200px;
  margin-top: 100px;
}

// .signature-pad {
//     border: 1px dashed #fafafa;

//     canvas {
//         border: 1px dashed #999;
//     }
// }

// .signature {
//     canvas {
//         border: 1px dashed #999;
//     }
// }

:deep(.seal-set-dialog .van-dialog__footer) {
  display: none;
}

.seal-set-dialog {
  .seal-modal-action {
    display: flex;
    width: 100%;

    :deep(.van-button) {
      width: 50%;
      height: 100px;
      font-size: 32px;
    }
  }
}

.auth-notify {
  top: 60px !important;
}

:deep(.sign-task-tip-modal .van-button) {
  font-size: 24px;
  height: 60px;
  margin: 0 20px;
  margin-bottom: 20px;
}

:deep(.sign-task-tip-modal) {
  p {
    padding: 0 30px;
    font-size: 24px;
  }
}

:deep(.sign-cerType .van-dialog__message) {
  text-align: left;
}

.batch-content {
  padding: 10px 20px 40px;

  :deep(.van-button) {
    width: 100%;
    margin-top: 20px;
  }
}

.batch-config {
  .table-title {
    display: flex;
    font-weight: 550;
    justify-content: center;
    text-align: center;
  }

  .doc-page {
    width: 100%;
    text-align: center;
    display: inline-block;
  }

  .page-config {
    display: flex;
    justify-content: space-between;
  }
}

.sign-date-cell {
  P {
    text-align: center;
    font-size: 30px;
    font-weight: 500;
    border-bottom: 1px solid #eee;
    padding: 20px 0;
    margin: 0;
  }
}

.comment-textarea {
  :deep(.van-field__control) {
    border: 1px solid#ededed;
    padding: 5px;
  }

  :deep(.van-cell__value--alone) {
    display: block;
  }
}

.custom-button-primary {
  background: linear-gradient(90deg, #0084ff, #6ebeff);
  border-color: #ffffff;
  border-radius: 8px;
  width: 6.5rem !important;
  height: 1.2rem !important;
  font-size: 0.4rem;
}

.custom-button-minor {
  width: 1.5rem !important;
  background: #ffffff;
  border-color: #ffffff;
  height: 1.2rem !important;
  font-size: 0.4rem;
  color: #ee0a24;
}

.seal-list {
  .seal-row {
    display: flex;
    justify-content: flex-start;
    align-items: stretch;
    gap: 1.0rem;
    padding: 8px 0;
  }

  .seal-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    border: 2px solid #288cf226;
    border-radius: 10px;
    padding: 12px 18px 8px 18px;
    transition: border 0.2s, box-shadow 0.2s;
    position: relative;
    min-width: 240px;
  }

  .seal-item.active-seal-img {
    border: 2px solid #288cf2;
    box-shadow: 0 2px 12px #288cf233;
    background: #f5faff;
    margin: 0 auto; /* 居中显示 */
  }

  .seal-selected-icon {
    position: absolute;
    top: 8px;
    right: 8px;
    width: 20px;
    height: 20px;
    background: url('data:image/svg+xml;utf8,<svg fill="none" viewBox="0 0 24 24" stroke="green" xmlns="http://www.w3.org/2000/svg"><circle cx="12" cy="12" r="11" stroke="%23288cf2" stroke-width="2" fill="white"/><path d="M7 13l3 3 7-7" stroke="%23288cf2" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>') no-repeat center center;
    background-size: contain;
    pointer-events: none;
  }

  .add-signature-icon {
    font-size: 48px;
    font-weight: bold;
    color: #666;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }

  .add-signature-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    cursor: pointer;
    border: 2px dashed #288cf2;
    border-radius: 10px;
    padding: 12px 18px 8px 18px;
    transition: border 0.2s, box-shadow 0.2s;
    position: relative;
    min-width: 120px;
    width: calc(70%);
    background-color: #f8f9fa;
    margin: 0 auto ;
  }

  .add-signature-btn:hover {
    border: 2px dashed #288cf2;
    box-shadow: 0 2px 12px #288cf233;
    background: #e6f4ff;
  }

  .add-signature-img {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }


}

/* 明亮模式 */
// .signature-canvas :deep(canvas) {
//   background-color: #ffffff !important;
// }

/* 暗黑模式 */
@media (prefers-color-scheme: dark) {
  .signature-canvas :deep(canvas) {
    background-color: #999 !important;
  }
}

/* 如果使用了特定的暗黑模式类 */
.dark .signature-canvas :deep(canvas) {
  background-color: #999 !important;
}

.signature-tip {
  color: #333;
  font-size: 24px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  align-items: center;
  margin-right: 20px; /* 自动占据剩余空间，使元素靠左 */
  flex-shrink: 1;
  margin-left: 0; /* 确保无额外左边距 */
}

.disabled-button {
  opacity: 0.5;
  cursor: not-allowed;
}

.sign-notice-bar-fixed {
  margin-top:100px;
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  z-index: 2002;
  background: #ecf9ff;
}

/* 在样式部分添加相关样式 */
.sign-control-tip-wrapper {
  // 移除默认的固定定位
  position: relative;
  width: 100%;
  z-index: auto;
  
  // 确保 notice-bar 在正常文档流中显示
  :deep(.van-notice-bar) {
    position: relative;
    width: 100%;
  }
  
  // 移除可能存在的固定定位样式
  :deep(.van-notice-bar--wrapable) {
    position: relative;
  }
}

/* 如果需要添加一些额外的样式来改善外观 */
.sign-control-tip-wrapper {
  :deep(.van-notice-bar) {
    border-radius: 0;
    margin-bottom: 0;
  }
}

.page-corner {
    position: absolute;
    right: 0px;
    bottom: 0px;
    width: 0;
    height: 0;
    border-color: #ccc transparent;
    border-width: 0 0 1.5rem 1.5rem;
    border-style: solid;
    }

  .page-num {
    position: absolute;
    bottom: 5px;
    right: 0.2rem;
    font-size:36px;
    color: #fff;
    /* padding-right: 20px; */
  }

  .custom-button-primary-locate {
    position: fixed;
    left: 50px;
    bottom: 20%;
    z-index: 200;
    color: #fff;
    border: none;
    border-radius: 50%;
    font-size: 18px;
    font-weight: 600;
    height: 70px;
    width: 70px;
    // padding: 0px;
    transition: box-shadow 0.2s, background 0.2s;
    display: flex;
    align-items: center;
    justify-content: center;
    background: white;
    // background: linear-gradient(90deg, #0084ff, #6ebeff);
    box-shadow: 0 0 0.61538462em rgba(0, 0, 0, 0.4);
  }  
  


  .sign-document-update{
    position: fixed;
    left: 50px;
    bottom: 20%;
    z-index: 200;
    height: 110px;
    width: 70px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;  
    // border: 1px solid rgb(229, 16, 16);
    // padding: 10px 0;
  }
  // 添加文档数量气泡样式
  .count-badge {
    position: absolute;
    top: -2px;
    right: -2px;
  }

  .sign-position-location{
    position: fixed;
    left: 50px;
    bottom: 20%;
    z-index: 200;
    height: 110px;
    width: 70px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;  

  }

  .custom-buttons {
    position: fixed;
    margin-bottom: 10px;
    color: #fff;
    border: none;
    border-radius: 50%;
    font-size: 18px;
    font-weight: 600;
    height: 70px;
    width: 70px;
    // padding: 0px;
    transition: box-shadow 0.2s, background 0.2s;
    // display: flex;
    // align-items: center;
    justify-content: center;
    background: white;
    // background: linear-gradient(90deg, #0084ff, #6ebeff);
    box-shadow: 0 0 0.61538462em rgba(0, 0, 0, 0.4);
  }

  .doc-footer-operation-tools {
    margin-top: 1rem;
    // margin-bottom: 0;
    text-align: center;
    // z-index: 999;
  }

  .doc-footer-operation-text {
    font-size: 16px;
    color: #333;
    white-space: nowrap;
  }

  .comment-notice { 
    padding: 0px 40px 0px 40px;
    font-size: 28px;
    color: #000;

  }

</style>

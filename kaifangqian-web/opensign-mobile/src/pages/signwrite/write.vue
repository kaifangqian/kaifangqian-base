<!--
  @description 文档填写页面

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
  <div class="write-container">
    <Loading :loading="loading" text="数据提交中..." />
    <SignerHeader
      :docs="docs"
      :docId="docId"
      @process="handleWriteProcess"
      @detail="handleDetail"
      @docChange="handleDocChange"
    ></SignerHeader>
    <div class="doc-content">
      <DocContent :nowDocument="nowDocument" :doc="nowDocument" />
    </div>
    <div class="sign-footer">
      <div class="write-statistics">
        <div>
          <span class="write-label">必填项：</span>
          <span class="write-count" style="color: #f50c0c">{{
            mustWritedCount + '/' + allmustWriteCount
          }}</span>
        </div>
        <div>
          <span class="write-label">非必填项：</span>
          <span class="write-count">{{ optionalWritedCount + '/' + allOptionalWriteCount }}</span>
        </div>
        <!-- <a href='javascript:;' @click="handleWrite" v-if="!isDetail">快捷填写</a> -->
      </div>
      <van-button
        class="custom-button-primary-write"
        type="primary"
        @click="handleWrite"
        size="small"
        :disabled="isDetail"
      >
        <SvgIcon name="contract-write" size="32" />
      </van-button>
      <div class="footer-write-action">
        <van-button
          class="custom-button-minor"
          type="default"
          @click="wakeUpComment"
          size="small"
          :disabled="isDetail"
          >拒填</van-button
        >
        <!-- <van-button class="custom-button-primary" type="primary" @click="handleWrite" size="small" :disabled="isDetail"
                    v-if="hasWrite && !isDetail">修改</van-button> -->

        <van-button
          class="custom-button-primary"
          type="primary"
          @click="handleSubmitWrite"
          size="small"
          :disabled="isDetail"
          >提交填写</van-button
        >
      </div>
    </div>
    <van-action-sheet
      v-model:show="processVisible"
      title="填写进度"
      :closeable="false"
      cancel-text="关闭"
    >
      <div class="process-content">
        <ul>
          <li class="signer-item" v-for="(item, index) in signerList" :key="index">
            <van-tag class="signer-tag" primary type="primary"
              >{{
                item.signerType == 1
                  ? '发起方'
                  : '接收方' + (hasSenderSigner ? (index == 0 ? index + 1 : index) : index + 1)
              }}
            </van-tag>
            <div class="signer-info">
              <span class="signer-title">{{ item.signerName }}</span>
              <span v-if="item.signerType == 3"> {{ item.signerExternalValue }} </span>
              <van-tag :color="loadSignColor(item.writeStatus)">{{
                loadWriteStatus(item.writeStatus)
              }}</van-tag>
            </div>
          </li>
        </ul>
      </div>
    </van-action-sheet>
    <van-action-sheet
      v-model:show="detailVisible"
      title="文档详情"
      :closeable="false"
      cancel-text="关闭"
    >
      <SignDetail :signRuId="signRuId" ref="signDetailRef" />
    </van-action-sheet>
    <v-dialog
      v-model:show="taskVisible"
      title="任务提醒"
      :showCancelButton="false"
      :show-confirm-button="false"
      className="sign-task-tip-modal"
    >
      <p
        >监测到该签约文档中您还有【{{
          taskInfo.taskType == 'sign' ? '签署 ' : '填写'
        }}】任务，是否前去处理？</p
      >
      <div style="text-align: center">
        <van-button type="default" @click="handleNoTask">暂不处理</van-button>
        <van-button type="primary" @click="handleGoNextTask">立即处理</van-button>
      </div>
    </v-dialog>
    <van-action-sheet
      v-model:show="writeVisible"
      title="快捷填写"
      :closeable="false"
      cancel-text="保存"
      class="write-params-sheet"
      @close="(e) => handleSaveWrite(e)"
      @click-overlay="handleClickOverlay"
    >
      <div class="write-header">
        <van-radio-group v-model="isRequired" @change="handleWriteType">
          <!-- <van-radio :name="0" icon-size="15px">全部</van-radio>
                    <van-radio :name="1" icon-size="15px">仅看必填</van-radio>
                    <van-radio :name="2" icon-size="15px">仅看待我填写</van-radio> -->
        </van-radio-group>
      </div>
      <div class="write-params-content">
        <div class="doc-item" v-for="(docItem, docIndex) in documentList" :key="docIndex">
          <ul>
            <li v-for="(item, index) in docItem.activeControl" :key="index">
              <div
                v-if="
                  (item.controlType == 'line-text' ||
                    item.controlType == 'multiline-text' ||
                    item.controlType == 'date' ||
                    item.controlType == 'number' ||
                    item.controlType == 'check-box' ||
                    item.controlType == 'radio' ||
                    item.controlType == 'dropdown-list' ||
                    item.controlType == 'image') &&
                  loadShow(item)
                "
              >
                <div class="params-header">
                  <span class="params-name">
                    <SvgIcon
                      name="required"
                      v-show="item.isRequired == 1"
                      :size="16"
                      class="control-pos"
                    >
                    </SvgIcon>
                    <span
                      >{{ item.name
                      }}<span style="color: #f50c0c" v-if="item.isMineFlag == 2"
                        >(非我填写)</span
                      ></span
                    >
                  </span>
                  <!-- <SvgIcon name="location" size="18" /> -->
                  <span class="location-icon" @click="handlePosition(docItem, item)">定位</span>
                </div>
                <div class="params-body">
                  <div class="params-select">
                    <van-field
                      v-if="item.controlType == ControlType.LineText"
                      v-model="item.value"
                      q
                      :placeholder="item.value"
                      :clearable="true"
                      :disabled="item.isMineFlag == 2"
                    ></van-field>
                    <van-field
                      v-if="item.controlType == ControlType.Number"
                      type="number"
                      v-model="item.value"
                      @change="(e: any) => handleWriteChange(e, item)"
                      :placeholder="item.value"
                      :disabled="item.isMineFlag == 2"
                      :clearable="true"
                    ></van-field>
                    <van-field
                      v-if="item.controlType == ControlType.MultilineText"
                      type="textarea"
                      :autosize="true"
                      v-model="item.value"
                      @change="(e: any) => handleWriteChange(e, item)"
                      :placeholder="item.value"
                      :clearable="true"
                      :disabled="item.isMineFlag == 2"
                    ></van-field>
                    <van-field
                      class="vant-input"
                      v-if="item.controlType == ControlType.Date"
                      v-model="item.value"
                      @focus="handleWriteDatePickerChange(item)"
                      :placeholder="item.value"
                      :clearable="true"
                      :disabled="item.isMineFlag == 2"
                    />
                    <van-calendar
                      v-model:show="item.writeTimeVisible"
                      :min-date="minDate"
                      v-if="item.controlType == ControlType.Date"
                      @confirm="onWriteTimeTimeConfirm"
                    />
                    <van-dropdown-menu
                      v-if="item.controlType == ControlType.DropdownList"
                      class="control-select"
                    >
                      <van-dropdown-item v-model="item.value" :options="item.options" />
                    </van-dropdown-menu>

                    <van-radio-group
                      v-model="item.value"
                      v-if="item.controlType == ControlType.Radio"
                      class="control-radio"
                    >
                      <van-radio
                        v-for="(radionItem, radionIndex) in item.widgetList"
                        :key="radionIndex"
                        :name="radionItem.uid"
                        >{{ radionItem.n }}</van-radio
                      >
                    </van-radio-group>

                    <van-checkbox-group
                      v-model="item.values"
                      v-if="item.controlType == ControlType.CheckBox"
                      class="control-checkbox"
                      @change="(e: any) => handleWriteChangeCheckBox(e, item)"
                    >
                      <van-checkbox
                        v-for="(radionItem, radionIndex) in item.widgetList"
                        :key="radionIndex"
                        :name="radionItem.uid"
                        shape="square"
                        >{{ radionItem.n }}</van-checkbox
                      >
                    </van-checkbox-group>
                    <div v-if="item.controlType == ControlType.Image" class="control-img-area">
                      <v-uploader
                        upload-text="点击上传"
                        :max-count="1"
                        v-model="item.values"
                        :deletable="false"
                        :after-read="(file) => afterReadyyzz(file, item)"
                        class="control-img"
                      >
                        <van-image width="100" height="100" :src="item.uploadImg" />
                      </v-uploader>
                      <div v-if="item.uploadImg" class="control-img-action">
                        <v-uploader
                          :max-count="1"
                          :deletable="false"
                          :after-read="(file) => afterReadyyzz(file, item)"
                        >
                          <van-button type="default" size="small"
                            ><SvgIcon name="camrea" size="18" /><span>更换</span></van-button
                          >
                        </v-uploader>
                        <van-button type="default" size="small" @mousedown="handleDeleteImg(item)"
                          ><SvgIcon name="delete-file" size="16" /><span>删除</span></van-button
                        >
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </van-action-sheet>
    <ConfirmModal ref="confirmRef" @success="handleConfirmSuccess" />

    <v-dialog
      v-model:show="commentVisible"
      title="拒填"
      show-cancel-button
      @confirm="handleRejectWrite"
      :beforeClose="beforeClose"
    >
      <van-field
        class="comment-textarea"
        v-model="comment"
        rows="4"
        autosize
        label=""
        type="textarea"
        maxlength="200"
        placeholder="请填写拒填原因200字以内"
        show-word-limit
      />
    </v-dialog>
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, onMounted, nextTick } from 'vue';
  import SignerHeader from '../components/SignHeader.vue';
  import SignDetail from '../components/SignDetail.vue';
  import DocContent from '@/pages/components/DocContent.vue';
  import { loadSignColor, loadWriteStatus } from '../transform';
  import { ControlType } from '../components/data/ControlData';
  import Api from '@/api/contract/index';
  import { getColtrolByType } from '@/pages/components/data/ControlData';
  import {
    pageScaling,
    currentPositionReverse,
    buildWidgets,
    paramBuildWidgets,
    currentPosition,
  } from '@/pages/components/data/ControlerMoveRange';
  import { cloneDeep } from 'lodash-es';
  import { useRoute, useRouter } from 'vue-router';
  import { Dialog, Notify } from 'vant';
  import { CanvasZoom } from '@/pages/components/data/ControlData';
  import session from '@/utils/cache/session';
  import { uploadFile } from '@/api';
  import { getHashQueryString, retainDecimals, decodeURIs } from '@/utils/util';
  import ConfirmModal from '@/pages/components/ConfirmModal.vue';
  import { Uploader } from 'vant';

  interface SignItem {
    signerType: number;
    signerName: string;
    signerExternalValue: string;
    writeStatus: string;
  }
  export default defineComponent({
    name: 'Write',
    components: {
      SignerHeader,
      SignDetail,
      DocContent,
      'v-dialog': Dialog.Component,
      'v-uploader': Uploader,
      ConfirmModal,
    },
    setup() {
      const docs = ref([]);
      const docId = ref('');
      const signDetailRef = ref(null);
      const currentControlDate = ref();
      const writeTimeVisible = ref(false);
      const commentVisible = ref(false);
      const comment = ref('');
      const isClickOverlay = ref(false);
      const taskVisible = ref(false);
      const taskType = ref('');
      const taskInfo = ref({});
      const isRequired = ref(2);
      const hasWrite = ref(false);
      const annexId = ref('');
      const images = ref([]);
      const controlList = ref([]);
      const writeType = ref('1');
      const nowDocument = ref([]);
      const mustWritedCount = ref(0);
      const allmustWriteCount = ref(0);
      const optionalWritedCount = ref(0);
      const allOptionalWriteCount = ref(0);
      const list = ref([]);
      const loading = ref(false);
      const finished = ref(false);
      const deleteIdList = ref(<string[]>[]);
      const processVisible = ref(false);
      const hasSenderSigner = ref(false);
      const detailVisible = ref(false);
      const writeVisible = ref(false);
      const route = useRoute();
      const router = useRouter();
      const isDetail = ref(false);
      const signRuInfo = ref('');
      const signRuId = route.query.signRuId as string;
      const taskId = route.query.taskId as string;
      const callbackPage = getHashQueryString('callbackPage');
      const orderNo = ref('');
      const confirmRef = ref();

      const willResult = ref(false);
      const confirmAction = ref('write');
      const imgFile: any = ref();

      session.setItem('sign_task_id', taskId);

      const documentList: any = ref([{ activeControl: [] }]);
      const signerList = ref(<SignItem[]>[]);

      const confirmTypeInfo: any = ref({
        confirmType: 'face',
        personalAccountStatus: '',
      });

      const afterReadyyzz = async (file: any, control) => {
        const params = {
          dataCategory: 'organizationpic',
        };
        control.uploadImg = '';
        control.value = '';
        control.values = [];
        const response = await uploadFile(file.file, params);
        if (response.code == 200) {
          control.value = response.result;
          control.uploadImg = file.content;
        }
        setTimeout(function () {
          loading.value = false;
        }, 300);
      };

      function handleDeleteImg(item: any) {
        item.uploadImg = '';
        item.value = '';
        item.values = [];
      }
      onMounted(() => {
        checkStatus();
        // init()
        generateOrderNo();
      });

      async function generateOrderNo() {
        let { result, code } = await Api.getOrderNo({ mainId: taskId });
        if (code == 200) {
          orderNo.value = result;
        }
      }

      async function checkStatus() {
        let { result, code } = await Api.checkOperatorStatus({ signRuId: signRuId });
        if (code == 200) {
          if (result.checkStatus != 1) {
            router.replace({
              name: '无权访问',
              query: {
                signRuId,
                taskId,
                taskType: 'write',
                callbackPage: callbackPage,
              },
            });
            return;
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
              // name:'签约详情',
              query: {
                ...route.query,
                callbackPage: callbackPage,
                isDetail: 'true',
              },
            });
          }
          setTimeout(() => {
            isDetail.value = route.query?.isDetail == 'true' ? true : false;
            if (unref(isDetail)) {
              // 详情页左侧没有控件从这里请求文件
            } else {
            }
            init();
          });
        }
      }

      async function init() {
        console.log(Api, '接口api');
        let { result } = await Api.getDocFiles({ signRuId: signRuId });
        if (result.length) {
          controlList.value = [];
          docs.value = result;
          docId.value = result[0].id;
          annexId.value = result[0].annexId;
          docs.value.map(async (v: any, i: number) => {
            v.pageSize = v.docPage;
            let { result } = await Api.getDocImgsById({ annexId: v.annexId });
            if (result && Array.isArray(result)) {
              v.images = result.sort((a, b) => a.page - b.page);
              if (i == 0) {
                images.value = result;
              }
              getDocControl(v);
            }
          });
        }
      }
      //文档切换
      async function handleDocChange(val: any) {
        let matchDoc = documentList.value.find((item) => item.signRuDocId == val);
        nowDocument.value = matchDoc;
        docId.value = val;
        images.value = matchDoc.images;

        // docId.value = val;
        // images.value = [];
        // annexId.value = docs.value.filter(v => v.id == docId.value)[0].annexId;
        // let { result } = await Api.getDocImgsById({ annexId: annexId.value });
        // let matchDoc = documentList.value.find(item => item.signRuDocId == docId.value);
        // if (matchDoc) {
        //     console.log(matchDoc, '测试文档---')
        //     matchDoc.images = result;
        //     //更新文档图片数量用于重新计算拖拽范围
        //     matchDoc.pageSize = result.length;
        //     nowDocument.value = matchDoc;
        // }

        // document.getElementsByClassName('doc-content')[0].scrollTop = 0;
        //   // console.log(documentList,docId.value,  matchDoc,'匹配文档')
      }
      //控件定位
      function handlePosition(doc, control) {
        const baseImgWidth = window.innerWidth - 20;
        const aspecRatio = baseImgWidth / 800;
        if (doc.signRuDocId != docId.value) {
          handleDocChange(doc.signRuDocId);
          setTimeout(() => {
            document
              .getElementsByClassName('content')[0]
              .scrollTo(0, control.position.page * 1131 * aspecRatio);
          }, 2000);
        } else {
          document
            .getElementsByClassName('content')[0]
            .scrollTo(0, control.position.page * 1131 * aspecRatio);
        }
      }
      // 填写进度
      async function getWriteProcess() {
        let { result } = await Api.getOperators({ signRuId });
        if (result) {
          signerList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
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
      //获取图片
      async function getDocImg() {
        // let { result } = await Api.getDocImgsById({ annexId: annexId.value })
        // if (result) {
        //     images.value = result;
        //     for (let i = 0; i < docs.value.length; i++) {
        //         getDocControl(docs.value[i])
        //     }
        // }
      }
      // 获取控件
      async function getDocControl(doc: any) {
        let { result } = await Api.getDocControlsByDocId({
          signRuId: signRuId,
          controlTypeList:
            'date,line-text,multiline-text,number,image,dropdown-list,radio,check-box',
        });
        let controls = result.controlList;
        if (controls.length) {
          controls = controls.filter(
            (v) =>
              v.controlType == 'line-text' ||
              v.controlType == 'date' ||
              v.controlType == 'multiline-text' ||
              v.controlType == 'number' ||
              v.controlType == 'check-box' ||
              v.controlType == 'radio' ||
              v.controlType == 'dropdown-list' ||
              v.controlType == 'image'
          );
          controls = controls.filter((v: any) => v.signRuDocId == doc.id);
          controlList.value = controlList.value.concat(controls);
          calculate(controlList.value);
        }
        setDocumentList(doc, controls);
      }
      function calculate(list: any) {
        allmustWriteCount.value = list.filter(
          (v: any) => v.isRequired == 1 && v.isMineFlag == 1
        ).length;
        allOptionalWriteCount.value = list.filter(
          (v: any) => v.isRequired == 2 && v.isMineFlag == 1
        ).length;
      }

      //整理文档列表
      function setDocumentList(doc: any, controls: any) {
        const { targets, maxWidth } = pageScaling(doc.images);
        console.log(targets, maxWidth, 'maxWidth-------');
        documentList.value.push({
          active: false,
          documentName: doc.name,
          targets: targets,
          maxWidth: maxWidth,
          annexFileId: '',
          signRuDocId: doc.id,
          activeControl: cloneDeep(buildResponseControl(controls)),
          pageSize: images.value.length,
          imageLoading: true,
          images: cloneDeep(doc.images),
          imageAllHeight:
            targets[targets.length - 1].transformHeight +
            targets[targets.length - 1].height +
            images.value.length * CanvasZoom.space,
        });
        nowDocument.value = documentList.value.find((v: any) => v.signRuDocId == docId.value);
        calculateControlWriteCount();
      }

      // 构建文档控件
      function buildResponseControl(controls: any) {
        const controlTem: any = [];
        if (controls && controls.length > 0) {
          controls.forEach((item: any) => {
            const basaeColtrol = getColtrolByType(item.controlType);
            const baseImgWidth = window.innerWidth - 20;
            const aspecRatio = baseImgWidth / 800;

            let matchDoc = docs.value.filter((v: any) => v.id == item.signRuDocId)[0];
            let { targets, maxWidth } = pageScaling(matchDoc.images);
            let target = targets[item.page];
            let offsetWidth = (maxWidth - target.width) / 2;

            if (
              ['image', 'dropdown-list', 'radio', 'check-box'].includes(item.controlType) &&
              item.properties &&
              item.properties != '"[]"'
            ) {
              //   item.widgetList  = item.properties && (typeof item.properties == 'string')?JSON.parse(item.properties):[];
              if (item.controlType == 'dropdown-list') {
                let widgetList =
                  item.properties && typeof item.properties == 'string'
                    ? JSON.parse(item.properties)
                    : [];
                item.options = loadOptions(widgetList);
              }
              item.widgetList = item.properties;
            }
            if (item.controlType == 'date') {
              item.formatValue = '';
              if (item.value) {
                const [year, month, day] = item.value.split('-');
                if (item.format === 'YYYY/MM/DD') {
                  item.formatValue = `${year}/${month}/${day}`;
                }
                if (item.format === 'YYYY年MM月DD日') {
                  item.formatValue = `${year}年${month}月${day}日`;
                }
                if (item.format === 'YYYY-MM-DD') {
                  item.formatValue = `${year}-${month}-${day}`;
                }
              }
            }
            if (item.controlType == 'image') {
              console.log(
                target,
                item,
                (parseInt(item.offsetX) + offsetWidth) * aspecRatio,
                'ssssssssssssssssss'
              );
            }
            controlTem.push({
              id: parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
              uid: item.id,
              icon: basaeColtrol.icon,
              widgetList: paramBuildWidgets(item.widgetList, target, offsetWidth),
              uploadImg: item.uploadImg || '',
              properties: item.properties,
              name: item.name,
              formatValue: item.formatValue,
              options: item.options,
              pageHeight: Number(item.pageHeight),
              pageWidth: Number(item.pageWidth),
              title: basaeColtrol.title,
              isMineFlag: item.isMineFlag,
              isRequired: item.isRequired,
              controlType: item.controlType,
              placeholder: item.placeholder,
              value: item.value,
              values: item.value && item.value.includes(',') ? item.value.split(',') : [],
              controlClick: false,
              writeTimeVisible: false,
              disabled: true,
              zoom: basaeColtrol.zoom,
              move: unref(isDetail) ? false : basaeColtrol.move,
              required: item.isRequired == 1,
              attr: basaeColtrol.attr,
              signRuDocId: item.signRuDocId,
              signerId: item.signerId,
              signerType: item.signerType,
              format: item.format,
              offsetX: item.offsetX,
              offsetY: item.offsetY,
              originSize: {
                width: item.width,
                height: item.height,
                offsetX: item.offsetX,
                offsetY: item.offsetY,
                pageWidth: item.pageWidth,
                pageHeight: item.pageHeight,
              },
              style: {
                fontSize: parseInt(item.fontSize),
                fontFamily: item.fontFamily,
                textAlign: item.textAlign,
              },
              size: {
                width: parseInt(item.width),
                height: parseInt(item.height),
                minWidth: basaeColtrol.size.minWidth,
                minHeight: basaeColtrol.size.minHeight,
              },
              position: {
                left: Number(item.offsetX) + offsetWidth,
                top: target.transformHeight + (item.page + 1) * 16 + Number(item.offsetY),
                page: parseInt(item.page),
              },
              user: {},
            });
          });
        }
        return controlTem;
      }
      //进度
      function handleWriteProcess() {
        processVisible.value = true;
        getWriteProcess();
      }
      //详情
      function handleDetail() {
        detailVisible.value = true;
        nextTick(() => {
          console.log(signDetailRef.value, '子组件方法----');
          signDetailRef.value && signDetailRef.value.getRuInfo();
        });
      }
      //继续任务处理
      function handleGoNextTask() {
        if (taskInfo.value.taskType == 'sign') {
          router.push({
            path: '/signContract',
            query: {
              signRuId: taskInfo.value.ruId,
              taskId: taskInfo.value.taskId,
              callbackPage: callbackPage,
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
              callbackPage: callbackPage,
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
        setTimeout(() => {
          router.push('/index');
        }, 1000);
      }

      function getConfirmTypeForAction(params: any) {
        return new Promise(async (resolve, reject) => {
          let { result, code } = await Api.getConfirmType(params);
          if (code == 200) {
            resolve(result);
          } else {
            reject(result);
          }
        });
      }
      //提交填写
      function handleSubmitWrite() {
        if (mustWritedCount.value < allmustWriteCount.value) {
          Notify({ type: 'warning', message: '还有必填项未填写' });
          return;
        }
        getConfirmTypeForAction({ operateType: 'submit_write' }).then(async (res) => {
          // confirmTypeInfo.value = res;
          // confirmAction.value = 'write';
          // if (!willResult.value) {
          //   confirmRef.value?.showDialog({
          //     confirmType: confirmTypeInfo.value.confirmType,
          //     orderNo: orderNo.value,
          //   });
          //   return;
          // }
          Dialog.confirm({
            title: '提交填写',
            message: '提交后无法修改，确认提交？',
          })
            .then(async () => {
              let paramsControl = formatSubmitControl();
              loading.value = true;
              let { result, code } = await Api.submitWrite({
                signRuId: signRuId,
                controlList: paramsControl,
                deleteIdList: [],
                // signConfirmOrderNo: orderNo.value,
              });
              if (code == 200) {
                loading.value = false;
                if (result) {
                  taskVisible.value = true;
                  taskInfo.value = result;
                } else {
                  Notify({ type: 'success', message: '操作成功' });
                  setTimeout(async () => {
                    if (callbackPage && typeof callbackPage == 'string') {
                      window.open(decodeURIs(callbackPage), '_self');
                    } else {
                      router.push('/index');
                    }
                  }, 1000);
                }
              } else {
                loading.value = false;
              }
            })
            .catch(() => {
              // on cancel
              loading.value = false;
            });
        });
      }

      function wakeUpComment() {
        commentVisible.value = true;
        comment.value = '';
      }

      function beforeClose(action) {
        new Promise((resolve) => {
          if (
            (!comment.value || comment.value.length < 1 || comment.value.length > 200) &&
            action === 'confirm'
          ) {
            Notify({ type: 'danger', message: '请输入200字以内原因' });
            resolve(false);
          } else if (action === 'cancel') {
            commentVisible.value = false;
            comment.value = '';
            resolve(true);
          }
        });
      }
      //拒绝填写
      function handleRejectWrite() {
        if (!comment.value || comment.value.length < 1 || comment.value.length > 200) {
          return;
        }
        commentVisible.value = false;

        Dialog.confirm({
          title: '拒绝填写',
          message: '确认拒绝填写？',
        })
          .then(async () => {
            loading.value = true;
            let { code } = await Api.rejectWrite({
              signRuId,
              signConfirmOrderNo: orderNo.value,
              comment: comment.value,
            });
            if (code == 200) {
              Notify({ type: 'success', message: '操作成功' });
              loading.value = false;
              setTimeout(async () => {
                if (callbackPage && typeof callbackPage == 'string') {
                  window.open(decodeURIs(callbackPage), '_self');
                } else {
                  router.push('/index');
                }
              });
            }
          })
          .catch(() => {
            loading.value = false;
            // on cancel
          });
      }
      function handleClickOverlay(e) {
        if (hasWrite.value) {
          isClickOverlay.value = false;
          return;
        }
        isClickOverlay.value = true;
      }
      //填写保存
      async function handleSaveWrite(e) {
        let paramsControl = formatSubmitControl();
        let { result, code } = await Api.saveWrite({
          signRuId: signRuId,
          controlList: paramsControl,
          deleteIdList: unref(deleteIdList),
        });
        calculateControlWriteCount();
        rewriteValueToControl();
        if (code == 200) {
          hasWrite.value = isClickOverlay.value ? false : true;
          isClickOverlay.value = false;
          // Notify({ type: 'success', message: '保存成功' });
        }
        //保存完document
      }
      //将填写表单中控件的值赋值到渲染控件上
      function rewriteValueToControl() {
        let controls = documentList.value.flatMap((item: any) => item.activeControl);
        console.log(controls, nowDocument.value.activeControl, '控件2343');

        nowDocument.value.activeControl.map((item) => {
          let matchControl = controls.find((v) => v.id == item.id);
          if (matchControl) {
            item.value = matchControl.value;
          }
        });
      }
      //计算填写数量
      function calculateControlWriteCount() {
        let controls = documentList.value.flatMap((item: any) => item.activeControl);
        mustWritedCount.value = controls.filter(
          (v: any) => v.isRequired == 1 && v.value && v.isMineFlag == 1
        ).length;
        optionalWritedCount.value = controls.filter(
          (v: any) => v.isRequired == 2 && v.value && v.isMineFlag == 1
        ).length;
      }
      //数据整理
      function formatSubmitControl() {
        let controls = documentList.value.flatMap((item: any) => item.activeControl);
        let paramsControl: any = [];
        const baseImgWidth = window.innerWidth - 20;
        //单独复制一份防止保存后显示出错
        let newControls = cloneDeep(controls);
        newControls.map((item: any) => {
          let matchDoc: any = docs.value.filter((v: any) => v.id == item.signRuDocId)[0];
          //这里的maxImageWidthItem.imageWidth 为图片宽度原始最大值 maxWidth 是缩放后的图片宽度最大值
          // const maxImageWidthItem = matchDoc.images.reduce((max, item) => {
          //     return parseFloat(item.imageWidth) > parseFloat(max.imageWidth) ? item : max;
          // }, matchDoc.images[0]);
          let { targets, maxWidth } = pageScaling(matchDoc.images);
          let target = targets[item.position.page];
          let offsetWidth = (maxWidth - target.width) / 2;
          // const baseZoomRatio =  (maxImageWidthItem.imageWidth / baseImgWidth);  //图片缩放比
          // const targetControlZoomRatio =   target.width / item.pageWidth;   //当前页面控件缩放比

          item.signerType = signerList.value.find((m: any) => m.id == item.signerId)?.signerType
            ? 2
            : 1;
          // item.size = {
          //     width:item.size.width / targetControlZoomRatio,
          //     height:item.size.height / targetControlZoomRatio,
          // }
          // item.position = {
          //     left:((item.position.left - offsetWidth) / target.width ) * item.pageWidth,
          //     top:((item.position.top - target.transformHeight - (item.position.page+1) * 16) / target.height) * item.pageHeight,
          //     page:item.position.page
          // }
          paramsControl.push({
            fontFamily: item.style.fontFamily,
            fontSize: parseInt(item.style.fontSize),
            name: item.name,
            offsetX: item.position?.left - retainDecimals(offsetWidth),
            offsetY: currentPosition(item.position.top, target), //计算成为每页的位置
            pageWidth: item.pageWidth,
            pageHeight: item.pageHeight,
            page: item.position.page,
            placeholder: item.placeholder,
            signReDocId: item.signReDocId,
            relatedDocId: 0,
            signerId: item.signerId,
            signerType: item.signerType,
            relatedDocType: 0,
            properties: item.properties,
            value: item.values && item.values.length ? item.values.join(',') : item.value,
            required: item.required ? 1 : 2,
            interfaceParamName: item.interfaceParamName,
            textAlign: item.style.textAlign,
            signRuDocId: item.signRuDocId,
            controlType: item.controlType,
            width: item.size.width,
            height: item.size.height,
            written: 1,
            format: item.format,
            id: item.uid,
            tenantId: item.user.tenantId,
          });
        });

        return paramsControl;
      }
      //选择时间
      function handleWriteDatePickerChange(row) {
        console.log(row, '控件----');
        row.writeTimeVisible = true;
        currentControlDate.value = row;
      }
      //快速填写
      function handleWrite() {
        writeVisible.value = true;
      }
      //填写
      function handleWriteChange(e: any, item: any) {
        console.log(e, item, '变更值');
        item.value = e.target.value;
      }
      //填写复选框
      function handleWriteChangeCheckBox(e: any, item: any) {
        item.value = Object.values(e).join(',');
      }
      //填写选择时间
      function onWriteTimeTimeConfirm(value: any) {
        const formatDate = (date: any) =>
          `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
        currentControlDate.value.writeTimeVisible = false;
        currentControlDate.value.value = `${formatDate(value)}`;
      }

      function loadShow(row: any) {
        if (isRequired.value == 0) {
          return true;
        }
        if (isRequired.value == 1) {
          return row.isRequired == 1 ? true : false;
        }
        if (isRequired.value == 2) {
          return row.isMineFlag == 1 ? true : false;
        }
      }
      function handleWriteType(val: string) {
        console.log(val, 'dddddsd');
      }

      function handleConfirmSuccess(info: any) {
        confirmTypeInfo.value.confirmType = info.confirmType;
        if (confirmAction.value == 'write') {
          willResult.value = true;
          handleSubmitWrite();
        }
        if (confirmAction.value == 'reject') {
          willResult.value = true;
          handleRejectWrite();
        }
      }
      function loadOptions(list: []): [] {
        let result: any = [];
        list &&
          list.map((v: any) => {
            result.push({
              text: v.n,
              value: v.uid,
            });
          });

        return result;
      }
      return {
        docs,
        handleConfirmSuccess,
        loadShow,
        mustWritedCount,
        isRequired,
        confirmRef,
        allmustWriteCount,
        optionalWritedCount,
        allOptionalWriteCount,
        nowDocument,
        list,
        handleWriteChange,
        loading,
        finished,
        handleWriteProcess,
        wakeUpComment,
        processVisible,
        onWriteTimeTimeConfirm,
        afterReadyyzz,
        handleDeleteImg,
        signerList,
        loadWriteStatus,
        signRuId,
        loadSignColor,
        docId,
        hasSenderSigner,
        detailVisible,
        handleSaveWrite,
        loadOptions,
        handleDetail,
        writeVisible,
        writeType,
        handleWrite,
        signDetailRef,
        documentList,
        ControlType,
        CanvasZoom,
        images,
        imgFile,
        signRuInfo,
        controlList,
        hasWrite,
        handleDocChange,
        handleRejectWrite,
        handleSubmitWrite,
        handleWriteType,
        writeTimeVisible,
        handleWriteChangeCheckBox,
        handleWriteDatePickerChange,
        isDetail,
        minDate: new Date(1997, 0, 1),
        handlePosition,
        taskVisible,
        taskType,
        handleGoNextTask,
        handleNoTask,
        handleClickOverlay,
        taskInfo,
        commentVisible,
        comment,
        beforeClose,
      };
    },
  });
</script>

<style lang="less" scoped>
  .content {
    overflow: hidden;
  }
  .doc-content {
    overflow: hidden;
  }
  .write-container {
    position: relative;
    font-size: 24px;
  }

  .sign-footer {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding-bottom: 10px;
    z-index: 99;

    .write-statistics {
      display: flex;
      width: 100%;
      background: #1c84d411;
      justify-content: space-around;
      align-items: center;
      height: 60px;
      line-height: 60px;
      padding: 20px 0;
      margin-bottom: 20px;
      // margin: 4px 0 20px;
    }

    .footer-write-action {
      width: 100%;
      display: flex;
      justify-content: center;

      :deep(.van-button) {
        width: 300px;
        margin: 0 30px;
      }
    }
  }

  :deep(.van-action-sheet__header) {
    border-bottom: 2px solid #e4e4e4;
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

      .signer-title {
        font-weight: 550;
      }

      .signer-tag {
        position: absolute;
        left: 0;
        top: 0;
      }
    }
  }

  .write-header {
    :deep(.van-radio-group) {
      display: flex;
      justify-content: center;
      align-items: center;
      margin: 20px 0;

      .van-radio {
        display: flex;
        align-items: center;
        margin: 0 20px;
      }
    }
  }

  :deep(.write-params-sheet.van-popup--bottom.van-popup--round) {
    max-height: 50%;
  }

  .write-params-content {
    padding: 10px 20px;

    :deep(.van-cell__value) {
      border: 1px solid #e4e4e4;
      padding: 4px 15px;
    }

    .params-header {
      padding: 0 30px;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .location-icon {
        color: #1989fa;
        cursor: pointer;
        font-size: 22px;
      }

      .params-name {
        // width: 200px;
        text-align: left;
        display: inline-block;
      }
    }
  }

  .params-select {
    :deep(.van-field) {
      border-radius: 8px;
    }
    :deep(.van-field--disabled .van-cell__value) {
      background: #e4e4e4;
    }
    :deep(.van-dropdown-menu__bar) {
      box-shadow: none;
      border: 1px solid #e4e4e4;
      height: 61px;
      .van-dropdown-menu__item {
        justify-content: space-between;
        width: 100%;
      }
      .van-dropdown-menu__title {
        width: 95%;
      }
    }
    .control-select {
      padding: 10px 32px;
      :deep(.van-cell__value) {
        border: none;
      }
    }
    .control-radio {
      padding: 10px 32px;
      :deep(.van-radio) {
        margin: 4px 0;
        .van-radio__icon {
          display: flex;
          align-items: center;
        }
        .van-badge__wrapper {
          width: 30px;
          height: 30px;
        }
        .van-radio__icon .van-icon {
          line-height: 1;
        }
      }
    }
    .control-checkbox {
      padding: 10px 32px;
      :deep(.van-checkbox) {
        margin: 4px 0;
        .van-badge__wrapper {
          width: 30px;
          height: 30px;
          display: flex;
          align-items: center;
        }
        .van-radio__icon .van-icon {
          line-height: 1;
        }
      }
    }
    .control-img {
      padding: 10px 32px;
    }
    .control-img-area {
      display: flex;
    }
    .control-img-action {
      display: flex;
      flex-direction: column-reverse;
      .van-button {
        margin: 4px 0;
        height: 48px;
      }
      :deep(.van-button__text) {
        display: flex;
        align-items: center;
        color: #666;
      }
    }
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
  .comment-textarea {
    :deep(.van-field__control) {
      border: 1px solid#ededed;
      padding: 5px;
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

  .custom-button-primary-write {
    position: fixed;
    right: 32px;
    bottom: 20%;
    z-index: 200;
    color: #fff;
    border: none;
    border-radius: 50%;
    font-size: 18px;
    font-weight: 600;
    height: 80px;
    width: 80px;
    // padding: 0px;
    transition: box-shadow 0.2s, background 0.2s;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(90deg, #0084ff, #6ebeff);
    box-shadow: 0 0 0.61538462em rgba(0, 0, 0, 0.4);
  }
</style>

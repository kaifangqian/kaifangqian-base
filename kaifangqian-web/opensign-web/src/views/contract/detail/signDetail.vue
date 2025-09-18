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
    <DocHeader :actions="actions" @cancel="handleCancel" :showDoc="false">
      <a-popover
        title="截止时间设置"
        trigger="click"
        :destroyTooltipOnHide="true"
        :visible="popVisible"
      >
        <template #content>
          <a-date-picker
            format="YYYY-MM-DD"
            v-model:value="expireDate"
            style="width: 300px"
            :allowClear="true"
            :disabled-date="disabledDate"
          ></a-date-picker>
          <div class="expiredate-footer" style="text-align: right; margin-top: 10px">
            <a-button type="default" style="margin: 0 10px" @click="popVisible = false"
              >取消</a-button
            >
            <a-button type="primary" @click="handleSaveExpireDate">保存</a-button>
          </div>
        </template>
        <a-button type="default" v-if="showDatePickerBtn" @click="handleSetSignDate"
          ><SvgIcon name="limiteDate" style="margin-right: 5px" />设置签署截止日期</a-button
        >
      </a-popover>
    </DocHeader>
    <div class="sign-detail-card">
      <Card size="small" title="合同信息">
        <div class="info-conten">
          <!-- <img class="sign-detail-status-img" :src="loadSignStatusImg(signRuInfo.baseVo.status)"/> -->
          <img
            class="sign-detail-status-img"
            :src="getAssetsFile('sign-' + signRuInfo.baseVo.status + '.png')"
          />
          <ul>
            <li>
              <span class="info-title">编号：</span>
              <span>{{ signRuInfo.baseVo.code || '无' }}</span>
            </li>
            <li>
              <span class="info-title">主题：</span>
              <span>{{ signRuInfo.baseVo.subject || '无' }}</span>
            </li>

            <li>
              <span class="info-title">状态：</span>
              <span>{{ loadRuStatus(signRuInfo.baseVo.status) }}</span>
            </li>
            <li>
              <span class="info-title">发起时间：</span>
              <span>{{ signRuInfo.baseVo.startDateString }}</span>
            </li>
            <li>
              <span class="info-title">签署截止时间：</span>
              <span> {{ signRuInfo.baseVo.expireDateString || '无' }}</span>
            </li>
            <li>
              <span class="info-title">签约文件：</span>
              <div class="contract-files">
                <ul class="contract-files-ul">
                  <li
                    v-for="(item, index) in signRuInfo.fileList"
                    :key="index"
                    class="contract-files-li"
                  >
                    <div class="file-icon">
                      <!-- <a-tag class="file-tag" :color="item.docType==1?'#0079FE':'#FE9500'">{{ item.docType==1?'本地文件':'模板' }}</a-tag> -->
                      <SvgIcon size="100" class="file-img" v-if="item.docType == 1" name="pdf" />
                      <SvgIcon size="100" class="file-img" v-else name="tpl" />
                      <div class="file-mask">
                        <a-button type="link" @click="handlePreview(item)">查看</a-button>
                      </div>
                    </div>
                    <div class="file-footer">
                      <a-tooltip>
                        <template #title>{{ item.name || '签约文档' }}</template>
                        <p class="file-name">{{ item.name || '签约文档' }}</p>
                      </a-tooltip>
                      <a-dropdown>
                        <SvgIcon name="more-options" size="30" class="file-img" />
                        <template #overlay>
                          <a-menu>
                            <!-- <a-menu-item v-if="signRuInfo.baseVo.canDownload"> -->
                            <a-menu-item>
                              <a
                                href="javascript:;"
                                v-if="signRuInfo.baseVo && signRuInfo.baseVo.downloadFileType == 1"
                                @click="
                                  handleRuDownload({ ruDocIdList: item.id, signRuId: signRuId })
                                "
                                >下载文档</a
                              >
                            </a-menu-item>
                            <a-menu-item>
                              <a href="javascript:;" @click="handlePreview(item)">查看</a>
                            </a-menu-item>
                          </a-menu>
                        </template>
                      </a-dropdown>
                    </div>
                  </li>
                </ul>
              </div>
            </li>
            <!-- <li>
              <span class="info-title">附件：</span>
              <div
                class="contract-files"
                v-if="signRuInfo.otherFileList && signRuInfo.otherFileList.length"
              >
                <BasicFileList :fileList="signRuInfo.otherFileList" />
              </div>
              <div v-else> 无 </div>
            </li> -->
          </ul>
        </div>
      </Card>
      <Card size="small">
        <template #title>
          <div class="signer-card-title">
            <span>参与方</span>
            <a-button type="primary" @click="handleRecord"
              ><Icon icon="ant-design:file-search-outlined" />操作记录</a-button
            >
          </div>
        </template>

        <div class="info-conten">
          <ul>
            <li>
              <span class="info-title">签署顺序：</span>
              <span>{{ signRuInfo.baseVo.signOrderType == 1 ? '顺序签署' : '无序签署' }}</span>
            </li>
            <!-- <li>
              <span class="info-title">文件状态：</span>
              <span>{{ signRuInfo.baseVo.status}}</span>
            </li> -->
          </ul>
          <div>
            <div>
              <div v-for="(item, index) in operatorList" :key="index">
                <div v-if="item.signerType == 1">
                  <div class="signer-name sender-type">
                    <span>发起方</span>
                  </div>
                  <div class="signer-area">
                    <div class="sender-name">
                      <p>{{ item.signerName }}</p>
                      <a-tag
                        v-if="item.writeStatus > -1"
                        class="sign-status"
                        :color="loadSignColor(item.writeStatus)"
                        >{{ loadWriteStatus(item.writeStatus) }}</a-tag
                      >
                    </div>

                    <div
                      v-for="(sendItem, sendIndex) in item.senderList"
                      :key="sendIndex"
                      class="signer-control-info"
                    >
                      <div class="signer-head">
                        <div class="sender-status-item">
                          <a-badge status="default" color="#333" />
                          <span>{{
                            sendItem.senderName +
                            '  —  ' +
                            '[' +
                            (sendItem.senderSignType == 1 ? '自动盖章' : sendItem.senderUserName) +
                            ']'
                          }}</span>
                        </div>
                        <!-- <a-tag class="sign-status" :color="loadSignColor(sendItem.writeStatus)">{{ loadWriteStatus(sendItem.writeStatus) }}</a-tag> -->
                        <a-tag class="sign-status" :color="loadSignColor(sendItem.signStatus)">{{
                          loadSignStatus(sendItem.signStatus)
                        }}</a-tag>
                        <a-tooltip>
                          <template #title>{{ copyTitle }}</template>
                          <SvgIcon
                            name="link"
                            size="20"
                            class="copy-link"
                            v-if="
                              sendItem.signStatus == 2 &&
                              userInfo.tenantUserId == signRuInfo.startUserId &&
                              (signRuInfo.baseVo.status == 5 || signRuInfo.baseVo.status == 7)
                            "
                            @click="handleCopySignLink(sendItem)"
                          />
                        </a-tooltip>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-if="item.signerType == 2">
                  <div class="signer-name receive-type">
                    <span
                      >接收方
                      {{ hasSenderSigner ? (index == 0 ? index + 1 : index) : index + 1 }}</span
                    >
                  </div>
                  <div class="signer-info signer-area">
                    <div class="receive-name">
                      <a-badge status="default" color="#333" />
                      <span class="operater-name" style="margin-right: 10px">
                        {{ item.signerName }}
                      </span>
                      <span> {{ item.signerExternalValue }} </span>
                    </div>
                    <a-tag
                      v-if="item.writeStatus > -1"
                      class="sign-status"
                      :color="loadSignColor(item.writeStatus)"
                      >{{ loadWriteStatus(item.writeStatus) }}</a-tag
                    >
                    <a-tag class="sign-status" :color="loadSignColor(item.signStatus)">{{
                      loadSignStatus(item.signStatus)
                    }}</a-tag>
                    <a-tooltip>
                      <template #title>{{ copyTitle }}</template>
                      <SvgIcon
                        name="link"
                        size="20"
                        class="copy-link"
                        v-if="
                          item.writeStatus == 2 &&
                          userInfo.tenantUserId == signRuInfo.startUserId &&
                          (signRuInfo.baseVo.status == 5 || signRuInfo.baseVo.status == 7)
                        "
                        @click="handleCopySignLink(item)"
                      />
                      <SvgIcon
                        name="link"
                        size="20"
                        class="copy-link"
                        v-if="
                          item.signStatus == 2 &&
                          userInfo.tenantUserId == signRuInfo.startUserId &&
                          (signRuInfo.baseVo.status == 5 || signRuInfo.baseVo.status == 7)
                        "
                        @click="handleCopySignLink(item)"
                      />
                    </a-tooltip>
                  </div>
                </div>
                <div v-if="item.signerType == 3">
                  <div class="signer-name sender-type">
                    <span
                      >接收方
                      {{ hasSenderSigner ? (index == 0 ? index + 1 : index) : index + 1 }}</span
                    >
                  </div>
                  <div class="signer-area">
                    <div class="sender-name">
                      <p>{{ item.signerName }}</p>
                      <a-tag
                        v-if="item.writeStatus > -1"
                        class="sign-status"
                        :color="loadSignColor(item.writeStatus)"
                        >{{ loadWriteStatus(item.writeStatus) }}</a-tag
                      >
                      <a-tooltip>
                        <template #title>{{ copyTitle }}</template>
                        <SvgIcon
                          name="link"
                          size="20"
                          class="copy-link"
                          v-if="
                            item.writeStatus == 2 &&
                            userInfo.tenantUserId == signRuInfo.startUserId &&
                            (signRuInfo.baseVo.status == 5 || signRuInfo.baseVo.status == 7)
                          "
                          @click="handleCopySignLink(item)"
                        />
                      </a-tooltip>
                    </div>

                    <div
                      v-for="(sendItem, sendIndex) in item.senderList"
                      :key="sendIndex"
                      class="signer-control-info"
                    >
                      <div class="signer-head">
                        <div class="sender-status-item">
                          <a-badge status="default" color="#333" />
                          <!-- <span>{{ sendItem.senderType==1?'经办人签字':'组织签章' }}</span> -->
                          <span>{{
                            (sendItem.senderType == 1 ? '经办人签字' : '组织签章') +
                            '  —  ' +
                            '[' +
                            sendItem.senderName +
                            ']'
                          }}</span>
                        </div>
                        <!-- <a-tag class="sign-status" :color="loadSignColor(sendItem.writeStatus)">{{ loadWriteStatus(sendItem.writeStatus) }}</a-tag> -->
                        <a-tag class="sign-status" :color="loadSignColor(sendItem.signStatus)">{{
                          loadSignStatus(sendItem.signStatus)
                        }}</a-tag>
                        <a-tooltip @visibleChange="handleTooltipVisibleChange">
                          <template #title>{{ copyTitle }}</template>
                          <SvgIcon
                            name="link"
                            size="20"
                            class="copy-link"
                            v-if="
                              sendItem.signStatus == 2 &&
                              userInfo.tenantUserId == signRuInfo.startUserId &&
                              (signRuInfo.baseVo.status == 5 || signRuInfo.baseVo.status == 7)
                            "
                            @click="handleCopySignLink(sendItem)"
                          />
                        </a-tooltip>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </Card>
      <Card size="small" title="附件及其他">
        <div class="info-conten">
          <li v-if="signRuInfo.otherFileList && signRuInfo.otherFileList.length">
              <span class="info-title">附件：</span>
              <div
                class="contract-files" style="margin-left: 80px"
              >
                <BasicFileList :fileList="signRuInfo.otherFileList" />
              </div>
          </li>
          <ul>
            <li>
              <span class="info-title">文件抄送：</span>
              <div v-if="signRuInfo.ccerList && signRuInfo.ccerList.length">
                <div class="ccerList-ul">
                  <div v-for="(item, index) in signRuInfo.ccerList" :key="index">
                    <a-tag>{{
                      item.ccerType == 1
                        ? item.internalCcerName
                        : item.externalCcerName + '（' + item.externalCcedValue + '）'
                    }}</a-tag>
                  </div>
                </div>
              </div>
              <div v-else> 无 </div>
            </li>
          </ul>
          <!-- <div style="display: flex">
            <span class="info-title">签署方式：</span>
            <a-radio-group v-model:value="signRuInfo.baseVo.caSignType" disabled>
              <a-radio :style="radioStyle" :value="1" v-if="signRuInfo.baseVo.caSignType == 1"
                >使用CA数字证书（符合电子签名法）
                <a-tooltip>
                  <template #title>签署文件时，需要使用CA机构颁发的数字证书</template>
                  <Icon icon="ant-design:question-circle-outlined" />
                </a-tooltip>
              </a-radio>
              <a-radio :style="radioStyle" :value="2" v-if="signRuInfo.baseVo.caSignType == 2"
                >使用平台防篡改证书（保护文件）（无法律效力）
                <a-tooltip>
                  <template #title
                    >签署文件时，使用平台下发的防篡改证书，该证书非CA机构颁发，仅用于文件保护，避免文件被篡改，签署后的文件不具备法律效力，请慎重选择</template
                  >
                  <Icon icon="ant-design:question-circle-outlined" /> </a-tooltip
              ></a-radio>
              <a-radio :style="radioStyle" :value="3" v-if="signRuInfo.baseVo.caSignType == 3"
                >合成签名和印章图片（不保护文件）（无法律效力）
                <a-tooltip>
                  <template #title
                    >签署文件时，不使用任何数字证书，仅在文件上加盖合成签名和印章图片，无法保护文件也不具备法律效力，请慎重选择</template
                  >
                  <Icon icon="ant-design:question-circle-outlined" />
                </a-tooltip>
              </a-radio>
            </a-radio-group>
          </div> -->
        </div>
      </Card>
    </div>
    <PreviewModal @register="registerModal"></PreviewModal>
    <RecordModal @register="registerRecordModal"></RecordModal>
    <DownloadModal @register="registerDownloadModal"></DownloadModal>
    <WishModal @register="registerWish" @success="handleConfirmSuccess"></WishModal>
  </div>
</template>

<script lang="ts">
  import { ref, unref, defineComponent, onMounted, reactive, createVNode, h } from 'vue';
  import DocHeader from '/@/views/contract/components/layouts/DocHeader.vue';
  import { Modal, Input, Button, DatePicker } from 'ant-design-vue';
  import {
    getBusinessRuInfo,
    getOperatorStatus,
    checkOperatorStatus,
    getViewCheck,
    setRuExpireDate,
  } from '/@/api/contract';
  import PreviewModal from '/@/views/contract/modal/PreviewModal.vue';
  import { useRouter } from 'vue-router';
  import { useModal } from '/@/components/Modal';
  import {
    loadRuStatus,
    loadSignColor,
    loadSignStatus,
    loadWriteStatus,
  } from '/@/views/contract/document/transform';
  import { Card, Divider } from 'ant-design-vue';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import {
    getCopyLinkCode,
    getRecordExist,
    getConfirmType,
    getConfirmNo,
    revokeRuSignRu,
  } from '/@/api/contract';
  import { getCheckOperates } from '/@/api/doc';
  import { handleRuDownload, getHashQueryString, decodeURIs } from '/@/utils';
  import { BasicFileList } from '/@/components/FileInfoList/index';
  import { getAssetsFile } from '/@/utils/pub-use';
  import { createLocalStorage } from '/@/utils/cache';
  import { useCopyToClipboard } from '/@/hooks/web/useCopyToClipboard';
  import { useUserStore } from '/@/store/modules/user';
  import RecordModal from '/@/views/contract/modal/RecordModal.vue';
  import DownloadModal from '/@/views/contract/modal/DownloadModal.vue';
  import WishModal from '/@/views/contract/modal/wish/WishModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import type { Dayjs } from 'dayjs';
  import dayjs from 'dayjs';

  interface ButtonItem {
    type: string;
    text: string;
    svgIcon?: string;
    disabled?: boolean;
    show?: boolean;
    slot?: Element;
    ref?: string;
    callBack: BtnFun;
  }

  export default defineComponent({
    name: 'SignDetail',
    components: {
      DocHeader,
      Card,
      Divider,
      Icon,
      WishModal,
      RecordModal,
      SvgIcon,
      PreviewModal,
      BasicFileList,
      DownloadModal,
      Modal,
      Input,
      Button,
      DatePicker,
    },
    setup() {
      const operatorList: any = ref([]);
      const startUserId = ref('');
      const actionTaskId = ref('');
      const confirmTaskId = ref('');
      const orderNo = ref('');
      const willResult = ref(false);
      const popVisible = ref(false);
      const hasSenderSigner = ref(false);
      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo;
      const copyTitle = ref('点击可复制待办任务链接');
      const showDatePickerBtn = ref(false);
      const signRuInfo: any = ref({
        baseVo: {
          subject: '',
        },
      });
      const radioStyle = reactive({
        marginBottom: '15px',
      });

      const disabledDate = (current: Dayjs) => {
        return current && current < dayjs().endOf('day');
      };
      const expireDate = ref<Dayjs | null>(dayjs());

      const router = useRouter();
      const { createMessage: msg } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const [registerRecordModal, { openModal: openRecordModal }] = useModal();
      const [registerDownloadModal, { openModal: openDownLoadModal }] = useModal();
      const [registerWish, { openModal: openWishModal, closeModal: closeWishModal }] = useModal();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const signRuId = route.query.signRuId;
      const taskId = route.query?.taskId;
      const ls = createLocalStorage();
      const actions = ref(<ButtonItem[]>[
        {
          type: 'default',
          text: '下载签约文件',
          callBack: handleDownloadFiles,
          show: true,
          svgIcon: 'download',
          disabled: false,
        },
        // {
        //   type:'primary',
        //   text:'下载签署记录报告',
        //   callBack:handleDownloadRecords,
        //   show:true,
        //   svgIcon:'record',
        //   disabled:false
        // }
      ]);
      const callbackPage = getHashQueryString('callbackPage');
      ls.set('Sign-Task-Id', taskId);
      onMounted(() => {
        //checkStatus()
        viewCheck();
        // fetch()
        // recordCheck()
      });

      async function recordCheck() {
        let result = 0;
        if (signRuInfo.value.baseVo.caSignType == 1 && signRuInfo.value.baseVo.status == 11) {
          result = await getRecordExist({ signRuId: signRuId });
        }
        if (result == 1) {
          actions.value = [
            {
              type: 'default',
              text: '下载签约文件',
              callBack: handleDownloadFiles,
              show: true,
              svgIcon: 'download',
              disabled: false,
            },
            {
              type: 'primary',
              text: '下载签署记录报告',
              callBack: handleDownloadRecords,
              show: true,
              svgIcon: 'record',
              disabled: false,
            },
          ];
        } else {
          actions.value = [
            {
              type: 'default',
              text: '下载签约文件',
              callBack: handleDownloadFiles,
              show: true,
              svgIcon: 'download',
              disabled: false,
            },
          ];
        }
      }

      function handleRecord() {
        openRecordModal(true, {
          isUpdate: false,
          record: {
            signRuId: signRuId,
          },
        });
      }

      function handleDownloadFiles() {
        if (signRuInfo.value.fileList.length > 1) {
          openDownLoadModal(true, {
            isUpdate: false,
            record: {
              signRuId: signRuId,
              fileList: signRuInfo.value.fileList,
            },
          });
        } else {
          handleRuDownload({ ruDocIdList: signRuInfo.value.fileList[0].id, signRuId: signRuId });
        }
      }
      function handleDownloadRecords() {
        handleRuDownload({}, '/sign/ru/report/file/download?signRuId=' + signRuId);
      }

      async function fetch() {
        let result = await getBusinessRuInfo({ signRuId: signRuId });
        // console.log("signRuInfo.value",result)
        if (result) {
          signRuInfo.value = result;
          getSignerInfo();
          setTimeout(() => {
            statusAction(signRuInfo.value.baseVo.status);
          });
        }
      }
      async function viewCheck() {
        let result = await getViewCheck({ signRuId: signRuId });
        console.log('result.value', result);
        if (!result) {
          router.replace({
            name: 'ContractNoauth',
            query: {
              signRuId: signRuId,
              taskId: taskId,
              taskType: 'detail',
              callbackPage: callbackPage,
            },
          });
          return;
        }
        await fetch();
        await recordCheck();
      }
      async function checkStatus() {
        let result = await checkOperatorStatus({ signRuId: signRuId });
        if (result) {
          if (result.checkStatsu != 1) {
            router.replace({
              name: '无权访问',
              query: {
                signRuId: signRuId,
                taskId,
                taskType: 'copy',
              },
            });
            return;
          }
        }
      }

      function handlePreview(row) {
        openModal(true, {
          isUpdate: false,
          record: {
            docId: row.annexId,
            docName: row.name,
          },
        });
      }

      async function statusAction(status: number) {
        let result = await getCheckOperates({ signRuId: signRuInfo.value.baseVo.signRuId });
        if (result) {
          actionTaskId.value = result.taskId;
          confirmTaskId.value = result.confirmTaskId;
          if (status == 5 && actionTaskId.value) {
            actions.value = [
              {
                type: 'primary',
                text: '填写',
                callBack: handleWrite,
                show: true,
                svgIcon: 'write',
                disabled: false,
              },
              ...actions.value,
            ];
          }
          if (status == 7 && actionTaskId.value) {
            actions.value = [
              {
                type: 'primary',
                text: '签署',
                callBack: handleSign,
                show: true,
                svgIcon: 'sign-action',
                disabled: false,
              },
              ...actions.value,
            ];
          }
          if (
            status == 2 ||
            (status == 5 && result.startFlag) ||
            (status == 7 && result.startFlag)
          ) {
            actions.value = [
              {
                type: 'default',
                text: '撤回',
                callBack: handleRevoke,
                show: true,
                svgIcon: 'revoke-action',
                disabled: false,
              },
              ...actions.value,
            ];
          }
          if (result.startFlag && loadShowExpireDate(signRuInfo.value.baseVo)) {
            showDatePickerBtn.value = true;
            // actions.value = [
            //   {
            //     type:'default',
            //     text:'设置签署日期',
            //     callBack:handleSetSignDate,
            //     show:true,
            //     svgIcon:'limiteDate',
            //     disabled:false,
            //     ref:'date-picker-sign'
            //   },
            //   ...actions.value,
            // ]
          } else {
            showDatePickerBtn.value = false;
          }
        }
      }

      function handleSetSignDate() {
        popVisible.value = true;
      }

      async function handleSaveExpireDate(row) {
        if (!expireDate.value) {
          msg.warning('请设置签署截止日期');
          return;
        }
        const endOfDay = dayjs(expireDate.value).endOf('day');
        endOfDay.format('YYYY-MM-DD');
        let result = await setRuExpireDate({
          signRuId: signRuInfo.value.baseVo.signRuId,
          expireDate: endOfDay,
        });
        if (result) {
          popVisible.value = false;
          await fetch();
          await recordCheck();
        }
      }

      function handleWrite() {
        router.push({
          path: '/contract/params',
          query: {
            __full__: '',
            signRuId: signRuInfo.value.baseVo.signRuId,
            isDetail: 'false',
            type: 'receive',
            taskId: actionTaskId.value,
            from: 'list',
          },
        });
      }

      function handleSign() {
        router.push({
          path: '/contract/sign',
          query: {
            __full__: '',
            signRuId: signRuInfo.value.baseVo.signRuId,
            isDetail: 'false',
            type: 'receive',
            taskId: actionTaskId.value,
            from: 'list',
          },
        });
      }

      // 自定义的textarea数据
      const textareaValue = ref('');
      function handleInput(e) {
        textareaValue.value = e.target.value;
      }

      function handleRevoke() {
        Modal.confirm({
          title: '撤回',
          content: createVNode(Input.TextArea, {
            text: textareaValue.value,
            placeholder: '请填写撤回原因，200字以内',
            maxlength: 200,
            onChange: handleInput,
          }),
          onOk() {
            return new Promise((resolve, reject) => {
              if (!textareaValue.value) {
                msg.warning('请输入原因');
                reject('请输入原因');
              } else if (textareaValue.value.length < 1 || textareaValue.value.length > 200) {
                msg.warning('字数不符合要求');
                reject('字数不符合要求');
              } else {
                handleConfirmSuccess();
                resolve('ok')
                // resolve('');
                // getConfirmTypeForAction(
                //   { operateType: 'revoke' },
                //   signRuInfo.value.baseVo.signRuId,
                // ).then(async (res: any) => {
                //   if (!willResult.value && res) {
                //     openWishModal(true, {
                //       isUpdate: false,
                //       record: {
                //         confirmType: res.confirmType,
                //         orderNo: orderNo.value,
                //       },
                //     });
                //     return;
                //   }
                // });
              }
            });
          },
        });
      }

      function getConfirmTypeForAction(params, signRuId) {
        return new Promise(async (resolve, reject) => {
          let resultNo = await getConfirmNo({ mainId: signRuId });
          if (resultNo) {
            orderNo.value = resultNo;
          }
          let result = await getConfirmType(params);
          if (result) {
            resolve(result);
          } else {
            reject(result);
          }
        });
      }

      function loadShowExpireDate(record) {
        let show = false;
        if (record.status == 1 || record.status == 2 || record.status == 3 || record.status == 4) {
          show = true;
        }
        if ((record.status == 5 || record.status == 7) && !record.expireDate) {
          show = true;
        }
        return show;
      }

      async function handleConfirmSuccess() {
        let result = await revokeRuSignRu({
          signRuId: signRuInfo.value.baseVo.signRuId,
          // signConfirmOrderNo: orderNo.value,
          taskId: confirmTaskId.value,
          comment: textareaValue.value,
        });
        if (result) {
          msg.success('操作成功');
          closeWishModal();
          router.push({
            path: '/contract/doc',
            query: {},
          });
        }
      }

      async function getSignerInfo() {
        let result = await getOperatorStatus({ signRuId: signRuId });
        if (result) {
          operatorList.value = result.sort((a, b) => a.signerOrder - b.signerOrder);
          operatorList.value.map((item) => {
            if (item.signerType == 1 || item.signerType == 3) {
              item.senderList = item.senderList
                ? item.senderList.sort((a, b) => a.senderOrder - b.senderOrder)
                : [];
            }
            if (item.signerType == 1) {
              hasSenderSigner.value = true;
            }
          });
        }
      }
      const statusImg = ref();
      function loadSignStatusImg(status) {
        return new URL('../../../assets/images/sign-' + status + '.png', import.meta.url).href;
      }

      async function copyToClipboard(text: string) {
        // 检测是否为 Safari 浏览器
        const isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
        
        // 优先尝试使用现代 Clipboard API（但避免在 Safari 中使用）
        if (navigator.clipboard && !isSafari) {
          try {
            await navigator.clipboard.writeText(text);
            return true;
          } catch (err) {
            console.warn('Clipboard API failed, falling back to execCommand', err);
          }
        }
        
        // 为 Safari 和其他情况准备备选方案
        const textArea = document.createElement('textarea');
        textArea.value = text;
        
        // 隐藏文本区域但保持可操作性
        textArea.style.position = 'fixed';
        textArea.style.top = '-9999px';
        textArea.style.left = '-9999px';
        textArea.style.opacity = '0';
        textArea.style.zIndex = '-1';
        
        document.body.appendChild(textArea);
        
        // Safari 特殊处理
        if (isSafari) {
          textArea.contentEditable = 'true';
        }
        
        // 选择文本内容
        if (isSafari) {
          // Safari 需要不同的选择方法
          textArea.focus();
          textArea.select();
          
          // 额外的 Safari 选择方法
          const range = document.createRange();
          range.selectNodeContents(textArea);
          const sel = window.getSelection();
          if (sel) {
            sel.removeAllRanges();
            sel.addRange(range);
          }
        } else {
          textArea.select();
        }
        
        try {
          // 尝试执行复制命令
          const successful = document.execCommand('copy');
          document.body.removeChild(textArea);
          return successful;
        } catch (err) {
          // 清理
          document.body.removeChild(textArea);
          
          // 最后的备选方案
          try {
            // 再次尝试现代 API
            await navigator.clipboard.writeText(text);
            return true;
          } catch (finalErr) {
            console.error('All copy methods failed', finalErr);
            return false;
          }
        }
      }
      async function handleCopySignLink(row) {
        let appSignInfo = window.appInfo.sign_app_info;
        let result = await getCopyLinkCode({ signerId: row.id });
        if (result) {
          let linkStr = '';
          let type = 'phone';
          let typeValue = '';
          if (result.phone) {
            type = 'phone';
            typeValue = result.phone;
          }
          if (result.email) {
            type = 'email';
            typeValue = result.email;
          }
          if (row.writeStatus == 2) {
            linkStr =
              signRuInfo.value.startUserName +
              '给您发送了一份文件《' +
              signRuInfo.value.baseVo.subject +
              '》有部分内容需要您填写，请访问 ' +
              appSignInfo.url +
              '/#/contract/detail/base?taskType=write&signRuId=' +
              signRuId +
              '&' +
              type +
              '=' +
              typeValue +
              '&taskId=' +
              result.taskId +
              ' 填写。';
          }
          if (row.signStatus == 2) {
            linkStr =
              signRuInfo.value.startUserName +
              '给您发送了一份文件《' +
              signRuInfo.value.baseVo.subject +
              '》请访问 ' +
              appSignInfo.url +
              '/#/contract/detail/base?taskType=sign&signRuId=' +
              signRuId +
              '&' +
              type +
              '=' +
              typeValue +
              '&taskId=' +
              result.taskId +
              ' 完成签署。';
          }
          
          // 使用改进的复制方法
          const copyResult = await copyToClipboard(linkStr);
          if (copyResult) {
            copyTitle.value = '已复制';
            // 显示成功消息
            msg.success('链接已复制到剪贴板');
            
            // 重置提示文字
            setTimeout(() => {
              copyTitle.value = '点击可复制待办任务链接';
            }, 2000);
          } else {
            // 复制失败时显示弹窗展示链接
            Modal.info({
              title: '复制失败',
              content: h('div', {}, [
                h('p', '自动复制链接失败，请手动复制以下链接：'),
                h('textarea', {
                  value: linkStr,
                  style: {
                    width: '100%',
                    height: '150px',
                    marginTop: '10px',
                    padding: '8px',
                    border: '1px solid #d9d9d9',
                    borderRadius: '4px',
                    resize: 'none',
                    fontFamily: 'monospace'
                  },
                  readonly: true
                })
              ]),
              icon: null,
              width: 600,
              okText: '知道了',
              onOk() {
                // 用户点击确定后重置提示文字
                copyTitle.value = '点击可复制待办任务链接';
              },

            });
          }
        }
      }

      function handleTooltipVisibleChange(visible) {
        debugger;
        if (!visible) {
          setTimeout(() => {
            copyTitle.value = '点击可复制待办任务链接';
          }, 500);
        }
      }

      async function handleCancel() {
        if (callbackPage && typeof callbackPage == 'string' && callbackPage.length > 0) {
          window.open(decodeURIs(callbackPage), '_self');
        } else {
          console.log('go -1');
          // window.history.go(-1);
          // window.close();
          router.go(-1);
        }
      }
      return {
        radioStyle,
        actions,
        signRuInfo,
        loadRuStatus,
        handleTooltipVisibleChange,
        loadSignColor,
        loadSignStatus,
        handleRecord,
        registerDownloadModal,
        operatorList,
        loadWriteStatus,
        startUserId,
        handleCancel,
        handleConfirmSuccess,
        userInfo,
        registerModal,
        registerRecordModal,
        handlePreview,
        hasSenderSigner,
        handleRuDownload,
        statusImg,
        loadSignStatusImg,
        getAssetsFile,
        handleCopySignLink,
        copyTitle,
        registerWish,
        signRuId,
        disabledDate,
        showDatePickerBtn,
        popVisible,
        expireDate,
        handleSetSignDate,
        handleSaveExpireDate,
      };
    },
  });
</script>

<style lang="less" scoped>
  :deep(.ant-card) {
    margin-bottom: 20px;
    box-shadow: 2px 0 8px 5px rgba(29, 35, 41, 0.05);
    .ant-card-head-title {
      font-weight: 600;
      font-size: 16px;
    }
  }
  .info-title {
    font-size: 14px;
    font-weight: 550;
    width: 120px;
  }
  :deep(.ant-radio-wrapper) {
    display: block;
  }
  :deep(.ant-card .ant-card-body) {
    padding: 20px 25px;
  }
  .sign-detail-card {
    width: 1200px;
    margin: 0 auto;
    .info-conten {
      position: relative;
      ul li {
        margin-bottom: 20px;
        display: flex;
        align-items: center;
      }
      .sign-detail-status-img {
        width: 160px;
        position: absolute;
        right: 0;
      }
    }
  }
  .receive-name {
    min-width: 200px;
  }
  .signer-area {
    padding: 10px 20px;
    background: #f9f9f9;
    margin-bottom: 10px;
    .sender-name {
      display: flex;
      align-items: center;
      margin-bottom: 30px;
      p {
        margin-bottom: 0;
        min-width: 200px;
      }
      :deep(.ant-tag) {
        margin-left: 20px;
      }
    }
  }
  .ccerList-ul {
    display: flex;
    align-items: center;
  }
  .signer-head,
  .signer-info {
    display: flex;
    margin-bottom: 20px;
    :deep(.ant-tag) {
      margin-left: 20px;
    }
    .sender-status-item {
      min-width: 200px;
      padding-left: 40px;
    }
  }

  .contract-files {
    // padding:5px;
    .contract-files-ul {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      width: 800px;
      // border: 1px solid #e9e9e9;
      .contract-files-li {
        width: 170px;
        height: 220px;
        background: #fff;
        text-align: center;
        margin: 10px;
        position: relative;
        box-sizing: border-box;
        display: flex;
        padding: 0;
        flex-direction: column;
        border: 1px solid #d3d1d1;

        .file-icon {
          border-bottom: 1px solid #d3d1d1;
          position: relative;
          height: 80%;
          width: 100%;
          :deep(.app-iconify) {
            position: absolute;
            top: -5px;
            right: -5px;
            cursor: pointer;
            z-index: 800;
            background: #fff;
            border-radius: 50%;
            visibility: hidden;
          }
          &:hover {
            border-radius: 2px;
            .file-mask {
              display: block;
            }
            :deep(.app-iconify) {
              visibility: visible;
            }
          }
          .file-tag {
            position: absolute;
            left: 2px;
            top: 0;
          }
          .file-img {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
          }
        }
        .file-footer {
          display: flex;
          align-items: center;
          height: 20%;
        }
        .file-mask {
          display: none;
          position: absolute;
          left: 0;
          top: 0;
          right: 0;
          bottom: 0;
          background-color: rgba(12, 12, 14, 0.7);
          :deep(.ant-btn-link) {
            color: #fff;
            height: 24px;
            padding: 0px 15px;
            border: 1px solid #fff;
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
            &:hover {
              background: #fff;
              color: #000;
            }
          }
        }

        .file-img {
          width: 100%;
          height: 100%;
        }
        .file-name {
          font-size: 14px;
          font-weight: 550;
          white-space: nowrap;
          width: 140px;
          margin: 0 auto;
          padding-left: 10px;
          overflow: hidden;
          text-overflow: ellipsis;
          margin-bottom: 0;
          color: #333;
        }
      }
    }
  }
  .copy-link {
    cursor: pointer;
  }
  .sign-status {
    color: #fff;
  }
  .signer-card-title {
    display: flex;
    justify-content: space-between;
  }
</style>

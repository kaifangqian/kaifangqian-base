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
    <div class="sign-notice-bar-fixed" v-if="showSignControlTip && signControlTip">
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
    </div>
    <div class="contract-header">
       <!-- 当只有一个文件时，显示普通文本 -->
      <div v-if="documentList && documentList.length === 1" class="single-doc-display">
          {{ documentList[0].text }}
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
          <draggable :list="nowDocument.activeControl" ghost-class="ghost" chosen-class="chosenClass" animation="300"
            :fallback-on-body="false" :force-fallback="true" :group="group" :scroll="false" :touch-start-threshold="50"
            :fallback-tolerance="50" style="width: 100%; height: 100%" key="unequalId">
            <control-item ref="controlItemRef" :doc="nowDocument" :zoom="zoom / 100" :element="element"
              @controlDelete="controlDelete" :isSign="true" v-for="(element, index) in nowDocument.activeControl"
              :key="index" @controlClick="controlShowMenu" @controlMenu="controlMenu"
              @controlMousedown="controlMousedown" @refreshControlPosition="refreshControlPosition">
            </control-item>
          </draggable>
        </div>
        <div class="document-control"> </div>
      </div>
    </div>
    <van-action-sheet v-model:show="batchSetShow" title="批量设置">
      <div class="batch-content">
        <van-notice-bar color="#1989fa" background="#ecf9ff" left-icon="info-o">
          应用后该批控件将加盖同一个签章
        </van-notice-bar>
        <div class="batch-config">
          <p class="config-title">应用文档</p>
          <van-checkbox-group v-model="docKeys">
            <van-cell-group>
              <div class="table-title">
                <van-cell>文档名称</van-cell>
                <van-cell>文档页数</van-cell>
              </div>
              <van-cell v-for="(item, index) in docs" clickable :key="item.id" :title="`${item.name}`"
                @click="toggle(index)">
                <template #default>
                  <span class="doc-page">{{ item.docPage }}</span>
                </template>
                <template #right-icon>
                  <van-checkbox :name="item.id" :ref="el => checkboxRefs[index] = el as any || checkboxRefs[index]"
                    @click.stop />
                </template>
              </van-cell>
            </van-cell-group>
          </van-checkbox-group>
        </div>
        <div class="batch-config">
          <p class="config-title">应用页面</p>
          <van-radio-group v-model="elementControl.propertyPageType" class="page-config">
            <van-radio name="all">全部页</van-radio>
            <van-radio name="odd_number">奇数页</van-radio>
            <van-radio name="even_number">偶数页</van-radio>
            <van-radio name="custom">指定页</van-radio>
          </van-radio-group>
          <span v-if="elementControl.propertyPageType == 'custom'"
            style="font-size: 10px; color: #999; margin: 20px 0">（请输入页码或页码范围，以逗号分隔，如1,2,7-10）</span>
          <van-field v-model="elementControl.custom" label="" placeholder="请输入页码"
            v-if="elementControl.propertyPageType == 'custom'" @blur="checkElementCustom" />
          <van-button type="primary" size="small" @click="handleSetControl">确定</van-button>
        </div>
      </div>
    </van-action-sheet>

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
                <van-tag :color="loadSignColor(senderItem.signStatus)">{{
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

    <!-- 定位控件icon -->
    <div class="sign-position-location" v-show="hashSignControl"> 
      <van-button
        class="custom-buttons"
        type="primary"
        @click="locationSignControl"
        size="small"
      >
        <SvgIcon name="sign-location" size="28" style="text-align: center;display: flex;align-items: center;justify-content: center;" />
        <!-- 签署控件数量气泡 -->
                <!-- 签署控件数量气泡 -->
        <van-badge :content="sealCount + signatureCount + signDateCount + stampSealCount" class="count-badge" />
      </van-button>
      <div class="doc-footer-operation-tools">
        <span class="doc-footer-operation-text">定位签署位置</span>
      </div>
    </div>
    

    <div class="doc-footer-operation">
      <div class="add-control">
        <div class="sign-control">
          <van-space :size="10" v-if="signControlInfo.type == '2'">
            <span>签章：{{ sealCount }}</span>
            <span class="custom-link" @touchend="handleAddSeal" v-if="!disabledSignControls">添加</span>
          </van-space>

          <van-space :size="10" v-if="signControlInfo.type == '1'">
            <span>签名：{{ signatureCount }}</span>
            <span class="custom-link" @touchend="handleAddSignature" v-if="!disabledSignControls">添加</span>
          </van-space>

          <van-space :size="10" v-if="signControlInfo.type == '2' || signControlInfo.type == '1'">
            <span>日期：{{ signDateCount }}</span>
            <span class="custom-link" @touchend="handleAddSignDate" v-if="!disabledSignControls">添加</span>
          </van-space>

          <van-space :size="10" v-if="signControlInfo.type == '2'">
            <span>骑缝章：{{ stampSealCount }}</span>
            <span class="custom-link" @touchend="handleAddChopSeal" v-if="!disabledSignControls">添加</span>
          </van-space>
        </div>
        <!-- <div class="sign-date-control">
                    <van-space :size="10" v-if="signControlInfo.type == '2' || signControlInfo.type == '1'">
                        <span>日期：{{ signDateCount }}</span>
                        <span class="custom-link" @touchend="handleAddSignDate">添加</span>
                    </van-space>
                    <van-space :size="10" v-if="signControlInfo.type == '2'">
                        <span>骑缝章：{{ sealCount }}</span>
                        <span class="custom-link" @touchend="handleAddSeal">添加</span>
                    </van-space>

                </div> -->
      </div>
      <div style="text-align: center">
        <van-space :size="20">
          <van-button class="custom-button-minor" type="primary" size="small" @click="wakeUpComment"
            :disabled="isDetail">拒签</van-button>
          <van-button class="custom-button-primary" type="primary" size="small" v-if="!userAuth.status && personalSignAuth == 'required'"
            :disabled="isDetail" @click="handleAuth">实名认证</van-button>
          <van-button class="custom-button-primary" type="primary" size="small" v-if="userAuth.status || personalSignAuth == 'not_required'" :disabled="isDetail"
            @click="handleSign">{{ isSignText ? '签署' : '提交签署' }}</van-button>
        </van-space>
      </div>
    </div>
    <van-action-sheet v-model:show="signDatevisible" :close-on-click-action="true" :before-close="beforeCloseSeal">
      <template #default>
        <div class="sign-date-cell">
          <p @touchstart="onDateTypeSelect('YYYY年MM月DD日')">YYYY年MM月DD日</p>
          <p @touchstart="onDateTypeSelect('YYYY-MM-DD')">YYYY-MM-DD</p>
          <p @touchstart="onDateTypeSelect('YYYY/MM/DD')">YYYY/MM/DD</p>
        </div>
      </template>
    </van-action-sheet>
    <!-- <v-dialog v-model:show="signDatevisible" title="日期格式选择">
        </v-dialog> -->

    <van-action-sheet v-model:show="sealVisible" title="选择企业印章" class="seal-set-dialog" :showCancelButton="true"
      @confirm="handleSetSeals" :beforeClose="beforeCloseSeal">
      <van-list v-if="!signControlInfo.sealId">
        <div v-if="sealList && sealList.length" class="seal-list">
          <template v-for="(row, rowIndex) in sealRows" :key="rowIndex">
            <van-cell style="justify-content: center">
              <div class="seal-row">
                <template v-for="(item, colIndex) in row" :key="item.sealId">
                  <div class="signature-info seal-item" :class="{ 'active-seal-img': signSealId === item.sealId }"
                    @click.stop="selectSeal(item.sealId)">
                    <!-- <img v-if="item.sealImage" :src="item.sealImage" class="signature-img"
                      style="margin-bottom: 10px" /> -->
                    <div class="signature-img">
                      <img v-if="item.sealImage" :src="item.sealImage" 
                      />
                    </div>
                    <span class="signer-title" style="margin: 0">{{ item.sealName }}</span>
                    <span v-if="signSealId === item.sealId" class="seal-selected-icon"></span>
                  </div>
                </template>
              </div>
            </van-cell>
          </template>
        </div>
        <p class="no-seal-list" v-else>
          您尚未拥有可以签署的企业印章，请联系企业印章管理员为您授权相应印章的使用权限。
        </p>
      </van-list>
      <div style="padding: 3px 0; background: var(--van-action-sheet-cancel-padding-color)"></div>
      <div class="seal-modal-action" style="position: sticky; bottom: 0; background: #fff; z-index: 10">
        <van-button type="default" style="margin-left:0;border-left:none;width:100%;hei"
          @touchend="handleSetSeals">确定</van-button>
      </div>
    </van-action-sheet>
    <van-action-sheet v-model:show="personSignatureVisible" title="选择个人签名" class="seal-set-dialog" :showCancelButton="true"
      @confirm="handleSetSignature" :beforeClose="beforeCloseSeal">
      <van-list>
        <div class="seal-list"> 
          <div style="height: 1px; background-color: #e5e5e5; margin-bottom:15px;"></div>
          <div 
            class="signature-info seal-item add-signature-btn" 
            @click.stop="handleSignatureVisible"
          >
            <div class="signature-img add-signature-img">
              <div class="add-signature-icon">
                <!-- <SvgIcon name="contract-write" size="32" /> -->
                <van-icon name="edit" />
              </div>
            </div>
            <span class="signer-title" style="margin-bottom:10px">手写签名</span>
          </div>
          <!-- 添加横向分割线 -->
          <div style="height: 1px; background-color: #e5e5e5; margin: 15px 0;"></div>
          <div>
          <!-- <div v-if="!signatureList && signatureList.length"> -->
            <template v-for="(row, rowIndex) in personSignatureRows" :key="rowIndex">
              <van-cell :style="{ justifyContent: 'center' }">
                <div class="seal-row">
                  <template v-for="(item, colIndex) in row" :key="item.sealId">
                    <div class="signature-info seal-item" :class="{ 'active-seal-img': signSealId === item.sealId }"
                      @click.stop="selectSeal(item.sealId)">
                      <div class="signature-img">
                        <img v-if="item.sealImage" :src="item.sealImage" 
                        />
                      </div>
                      <span class="signer-title" :style="{ margin: '0' }">{{ item.sealName }}</span>
                      <span v-if="signSealId === item.sealId" class="seal-selected-icon"></span>
                    </div>
                  </template>
                </div>
              </van-cell>
            </template>
          </div>
          <!-- <p class="no-seal-list" v-else>
            暂无常用签名。
          </p> -->
        </div>
      </van-list>
      <div :style="{ padding: '3px 0', background: 'var(--van-action-sheet-cancel-padding-color)' }"></div>
      <div class="seal-modal-action" :style="{ position: 'sticky', bottom: '0', background: '#fff', zIndex: '10' }">
        <van-button type="default" :style="{ marginLeft: '0', borderLeft: 'none', width: '100%' }"
          @touchend="handleSetSignature">确定</van-button>
      </div>
    </van-action-sheet>

    <v-dialog v-model:show="signatureVisible" title="签名面板" :showCancelButton="true" className="signatur-modal"
      @confirm="handleSetSignature" @cancel="handleSignatureCancel">
      <div class="signature-pad" v-if="showPad">
        <!-- 居中提示文字 -->
        <div class="signature-context" v-show="!hasSignature">请在此区域签名</div>
        <VueSignaturePad class="signature-canvas" :width="signaturePadConfig.width + 'px'"
          :height="signaturePadConfig.height + 'px'" ref="signaturePad" :options="options"
          :style="{ border: '1px solid #e4e4e4', transform: 'translate(50px, 2px)' }" />
      </div>
      <div class="signature-footer" :style="{ width: signaturePadConfig.height - 40 + 'px' }">
        <div v-if="clientHeight >= 700">
          <span class="signature-tip">
            <svg width="16" height="16" :style="{ marginRight: '4px', verticalAlign: 'middle' }" fill="#faad14" viewBox="0 0 1024 1024"><path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.3 0-372-166.7-372-372s166.7-372 372-372 372 166.7 372 372-166.7 372-372 372zm-40-272h80c4.4 0 8-3.6 8-8v-8c0-4.4-3.6-8-8-8h-80c-4.4 0-8 3.6-8 8v8c0 4.4 3.6 8 8 8zm40-440c-35.3 0-64 28.7-64 64v240c0 35.3 28.7 64 64 64s64-28.7 64-64V236c0-35.3-28.7-64-64-64z"></path></svg>
            请正楷签署您的姓名，避免错别字或书写过于潦草导致无法律效力
          </span>
        </div>
        <!-- <div class="gtnGroup">
          <van-button type="primary" size="small" @touchend="undo">撤销</van-button>
          <van-button type="primary" size="small" style="margin-left: 20px" @click="clear">清屏</van-button>
        </div> -->
        <!-- <div class="otherSet">
          <div class="penTxt">笔刷大小：</div>
          <div class="circleWrap" :class="{ active: isActive1 }" @click="setPadSize(1)"><b class="b1"></b></div>
          <div class="circleWrap" :class="{ active: isActive2 }" @click="setPadSize(2)"><b class="b2"></b></div>
          <div class="circleWrap" :class="{ active: isActive3 }" @click="setPadSize(3)"><b class="b3"></b></div>
        </div> -->
        <div class="signature-btns">
          <van-button
            type="default"
            size="small"
            @click="closeSignaturePad"
            :style="{ marginRight: '15px' }"
            >关闭</van-button
          >
          <van-button
            type="default"
            size="small"
            @click="clear"
            :style="{ marginRight: '15px' }"
            v-show="hasSignature"
            >重写</van-button
          >
          <van-button
            type="primary"
            size="small"
            @click="saveSignature"
            :disabled="!hasSignature"
            :class="{ 'disabled-button': !hasSignature }"
            >完成</van-button
          >
        </div>
      </div>
    </v-dialog>
    <v-dialog v-model:show="taskVisible" title="任务提醒" :showCancelButton="false" :show-confirm-button="false"
      className="sign-task-tip-modal">
      <p>监测到该签约文档中您还有【{{
        taskInfo.taskType == 'sign' ? '签署 ' : '填写'
      }}】任务，是否前去处理？</p>
      <div :style="{ textAlign: 'center' }">
        <van-button type="default" @click="handleNoTask">暂不处理</van-button>
        <van-button type="primary" @click="handleGoNextTask">立即处理</van-button>
      </div>
    </v-dialog>
    <!-- 人脸识别开通个人账号 -->
    <v-dialog v-model:show="needOpenPersonlAccountVisible" title="签署提醒" :showCancelButton="false"
      :show-confirm-button="false" className="sign-certifcate-modal">
      <div class="certificate-info">
        <p>您暂未开通个人账号，请开通个人账号</p>
        <!-- <p>您暂未开通个人账号，开通个人账号后，会为您自动颁发平台防篡改证书，用于文件签署时，保护文件不被篡改。</p> -->
        <van-button type="default" @click="handleUnSign" :style="{ margin: '0 10px' }">暂不签署</van-button>
        <van-button type="primary" @click="faceOpenPersonalAccount" :style="{ margin: '0 10px' }">开通个人账号</van-button>
      </div>
    </v-dialog>

    <v-dialog v-model:show="signCertificateVisible" title="签署提醒" :showCancelButton="false" :show-confirm-button="false"
      className="sign-certifcate-modal">
      <div class="certificate-info" v-if="!certificateInfo.tenantId && certificateInfo.returnCode == '2'">
        <p>您暂未开通个人账号，请开通个人账号</p>
        <van-button type="default" @click="handleUnSign" :style="{ margin: '0 10px' }">暂不签署</van-button>
        <van-button type="primary" @click="openPersonalAccount" :style="{ margin: '0 10px' }">开通个人账号</van-button>
      </div>
      <!-- 不校验证书 -->
      <div class="certificate-info" v-if="certificateInfo.returnCode == '1'">
        <p>签署文件时，不使用任何数字证书，仅在文件上加盖合成签名和印章图片，<span :style="{ color: '#f20404fc' }">无法保护文件也不具备法律效力，</span>请知悉！</p>
        <van-button type="default" @click="handleUnSign">暂不签署</van-button>
        <van-button type="primary" @click="goSign" :style="{ margin: '0 10px' }">继续签署</van-button>
      </div>
      <!-- 测试证书 -->
      <div class="certificate-info" v-if="certificateInfo.certType == '2' && certificateInfo.returnCode == '2'">
        <p v-if="certificateInfo.holderType == '2'">您尚未拥有可用于电子签约的测试证书，请获取证书</p>
        <p v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus != '2'">您尚未拥有可用于电子签约的测试证书，请先实名认证</p>
        <p v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus == '2'">您尚未拥有可用于电子签约的测试证书，请获取证书</p>
        <p>
          <!-- <Icon icon="ant-design:sync-outlined" /> <van-button @click="updateCertificate">刷新</van-button> -->
        </p>
        <div>
          <van-button type="default" @click="handleUnSign">暂不签署</van-button>
          <van-button type="primary" @click="updateCertificate" :style="{ margin: '0 10px' }"
            v-if="certificateInfo.holderType == '2'">获取证书</van-button>
          <van-button type="primary" @click="handlePersonAuth" :style="{ margin: '0 10px' }"
            v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus != '2'">实名认证</van-button>
          <van-button type="primary" @click="updateCertificate" :style="{ margin: '0 10px' }"
            v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus == '2'">获取证书</van-button>
        </div>
      </div>
      <div class="certificate-info" v-if="certificateInfo.certType == '2' && certificateInfo.returnCode == '3'">
        <p v-if="certificateInfo.holderType == '2'">您签署所使用的企业证书已失效，如果您需要继续签署，请申请新的证书</p>
        <p v-if="certificateInfo.holderType == '1'">您签署所使用的证书已过期，如果您需要继续签署，请申请新的证书</p>
        <div class="sign-tip-footer">
          <van-button type="default" @click="handleUnSign">暂不签署</van-button>
          <van-button type="primary" @click="updateCertificate" :style="{ margin: '0 10px' }">更新证书</van-button>
        </div>
      </div>

      <!-- CA数字证书 （事件+ 长效）-->
      <div class="certificate-info" v-if="
        (certificateInfo.certType == '3' || certificateInfo.certType == '4') &&
        certificateInfo.returnCode == '2'
      ">
        <p v-if="certificateInfo.holderType == '2'">您尚未拥有可用于电子签约的数字证书，请联系企业管理员认证企业信息，企业认证通过后，为企业自动下发数字证书</p>
        <p v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus != '2'">您尚未拥有可用于电子签约的数字证书，请先实名认证</p>
        <p v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus == '2'">您尚未拥有可用于电子签约的数字证书，请获取证书</p>
        <p>
          <van-button @click="updateCertificate">刷新</van-button>
        </p>
        <van-button type="default" @click="handleUnSign" :style="{ margin: '0 10px' }">暂不签署</van-button>
        <van-button type="primary" @click="handlePersonAuth" :style="{ margin: '0 10px' }"
          v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus != '2'">实名认证</van-button>
        <van-button type="primary" @click="updateCertificate" :style="{ margin: '0 10px' }"
          v-if="certificateInfo.holderType == '1' && tenantInfo.authStatus == '2'">获取证书</van-button>
      </div>
      <div class="certificate-info" v-if="
        (certificateInfo.certType == '3' || certificateInfo.certType == '4') &&
        certificateInfo.returnCode == '3'
      ">
        <p v-if="certificateInfo.holderType == '2'">您签署所使用的企业证书已失效，如果您需要继续签署，请申请新的证书</p>
        <p v-if="certificateInfo.holderType == '1'">您签署所使用的证书已过期，如果您需要继续签署，请申请新的证书</p>
        <div>
          <van-button type="default" @click="handleUnSign">暂不签署</van-button>
          <van-button type="primary" @click="updateCertificate" :style="{ margin: '0 10px' }">更新证书</van-button>
        </div>
      </div>
      <!-- 平台防篡改证书 -->
      <div class="certificate-info" v-if="certificateInfo.certType == '1' && certificateInfo.returnCode == '2'">
        <p>您暂未开通个人账号，开通个人账号后，会为您自动颁发平台防篡改证书，用于文件签署时，保护文件不被篡改</p>
        <div>
          <van-button type="default" @click="handleUnSign">暂不签署</van-button>
          <van-button type="primary" @click="openPersonalAccount" :style="{ margin: '0 10px' }"
            v-if="certificateInfo.holderType == '1'">开通个人账号</van-button>
        </div>
      </div>
      <div class="certificate-info" v-if="certificateInfo.certType == '1' && certificateInfo.returnCode == '3'">
        <p>您签署所使用的防篡改证书已失效，如果您需要继续签署，请申请新的证书</p>
        <div>
          <van-button type="default" @click="handleUnSign">暂不签署</van-button>
          <van-button type="primary" @click="updateCertificate" :style="{ margin: '0 10px' }">更新证书</van-button>
        </div>
      </div>
      <div class="certificate-info" v-if="certificateInfo.certType == '1' && certificateInfo.returnCode == '4'">
        <p>您当前用于文件签署的证书是平台下发的防篡改证书，<span :style="{ color: '#f20404fc' }">该证书非CA机构颁发，</span>仅用于文件保护，避免文件被篡改，<span
            :style="{ color: '#f20404fc' }">签署后的文件不具备法律效力，</span>请知悉！</p>
        <div>
          <van-button type="default" @click="handleUnSign">暂不签署</van-button>
          <van-button type="primary" @click="goSign" :style="{ margin: '0 10px' }">继续签署</van-button>
        </div>
      </div>
    </v-dialog>
    <v-dialog v-model:show="authCertificateVisible" title="签署提醒" :showCancelButton="false" :show-confirm-button="false"
      className="sign-certifcate-tip-modal">
      <p>您已经申请新的证书，可用于电子签约</p>
      <p>证书有效期：<span :style="{ color: '#f20404fc', marginLeft: '20px' }">{{ authCerInfo }}</span></p>
      <van-button type="primary" @click="goSign"
        :style="{ position: 'relative', left: '50%', transform: 'translate(-50%)' }">继续签署</van-button>
    </v-dialog>
  </div>
  <ConfirmModal ref="confirmRef" @success="handleConfirmSuccess" />
  <v-dialog v-model:show="commentVisible" title="拒签" show-cancel-button @confirm="handleRejectSign"
    :beforeClose="beforeClose">
    <van-field class="comment-textarea" v-model="comment" rows="4" autosize label="" type="textarea" maxlength="200"
      placeholder="请填写拒签原因，200字以内" show-word-limit />
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
import {
  controlListSource,
  BaseSize,
  getColtrolByType,
  CanvasZoom,
} from '@/pages/components/data/ControlData';
import {
  currentPositionReverse,
  currentPosition,
  pageScaling,
  recaculateBatchControlPosInPage,
  findMinImageSize,
  moveRange,
} from '@/pages/components/data/ControlerMoveRange';
import { ImagePreview, Notify, Dialog, NoticeBar } from 'vant';
import Api from '@/api/contract/index';
import { handleAuth as handleUserAuth } from '@/pages/auth/index';
import { useRoute, useRouter } from 'vue-router';
import { cloneDeep, now } from 'lodash-es';
import ControlItem from '@/pages/components/items/ControlMoveItem.vue';
import SignDetail from '@/pages/components/SignDetail.vue';
import ConfirmModal from '@/pages/components/ConfirmModal.vue';
import { loadSignColor, loadSignStatus } from '../transform';
import session from '@/utils/cache/session';
import { VueSignaturePad } from 'vue-signature-pad';
import { VueDraggableNext } from 'vue-draggable-next';
import { APP_TOKEN } from '@/utils/cache/constant';
import { parseInputPages, buildUUID, getBase64Size, decodeURIs } from '@/utils';
import { getHashQueryString } from '@/utils/util';
import { useUserStore } from '@/store/modules/user';

import doc1 from '@/assets/images/doc_1.png';
import doc2 from '@/assets/images/doc_1.png';
import { computed } from 'vue';
import http from '@/utils/http';
import { ok } from 'assert';
import { getTodayDateByFormat } from "@/utils/util"

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
    VueSignaturePad,
    ConfirmModal,
  },
  setup() {
    // console.log(CanvasZoom, 'sdsfd');

    const userStore = useUserStore();
    const docs: any = ref([]);
    const docKeys = ref(<string[]>[]);
    const checkboxRefs = ref([]);
    const elementBatch = ref();
    const confirmRef = ref();
    const toggle = (index: number) => {
      checkboxRefs.value[index].toggle();
    };
    const commentVisible = ref(false);
    const comment = ref('');
    const controlChangeFlag = ref('necessary_and_add');
    const defaultHasControl = ref(false);
    const disabledSignControls = ref(false);
    const docId = ref('');
    const pageOptions = ref([
      { text: '全部页面', value: 'all' },
      { text: '奇数页', value: 'odd_number' },
      { text: '偶数页', value: 'even_number' },
      { text: '指定页面', value: 'custom' },
    ]);
    const pageColumns = ['全部页面', '奇数页', '偶数页', '指定页面'];
    const showPagePicker = ref();
    const elementControl = ref({
      propertyPageType: '',
      custom: '',
      isChopStamp: false,
    });
    const annexId = ref('');
    const signatureId = ref('');
    const socket = ref();
    const images = ref([]);
    const controlList = ref([]);
    const isDetail = ref(false);
    const batchSetShow = ref(false);
    const taskVisible = ref(false);
    const taskType = ref('');
    const taskInfo: any = ref({});
    const signLoading = ref(false);
    const isActive1 = ref(false);
    const isActive2 = ref(true);
    const isActive3 = ref(false);
    const activeKey = ref(1);
    const sealVisible = ref(false);
    const personSignatureVisible = ref(false);
    const signatureVisible = ref(false);
    const signDatevisible = ref(false);
    const detailVisible = ref(false);
    const processVisible = ref(false);
    const personalAuth = ref(false);
    const hasSenderSigner = ref(false);
    const signatureTabKey = ref(2);
    const authCertificateVisible = ref(false);
    const showPad = ref(false);
    const authCerInfo = ref({});
    const entSealId = ref(''); // 企业签章id
    const privateSeal = ref(''); // 个人签章base64
    const signCertificateVisible = ref(false);
    const signerList = ref(<SignItem[]>[]);
    const deleteIdList = ref(<string[]>[]);
    const signatureList = ref(<any[]>[]);
    const sealList = ref(<any[]>[]);
    const signSealId = ref('');
    const sealImgId = ref('');
    const signDetailRef = ref(null);
    const QRsignatureBase64 = ref('');
    const QRKey = ref('');
    const signaturePad = ref();
    const sealAnnexId = ref(''); //签章图片 文件id
    const singatureBae64 = ref(''); //签名图片
    const signatureUrl = ref('');
    const signatureQC = ref('');
    const isQRValid = ref(true);
    const isSignText = ref(true);
    const needLocation = ref(false); //是否需要定位
    const callbackPage = getHashQueryString('callbackPage');
    const partyName = getHashQueryString('partyName')
      ? decodeURIComponent(getHashQueryString('partyName') as string)
      : '';
      
    const personalSignAuth = ref('');
    const options = ref({
      penColor: '#000',
      dotSize: 1,
      minWidth: 3,
      maxWidth: 6,
      throttle: 16,
      minDistance: 5,
      backgroundColor: 'rgba(0,0,0,0)',
      velocityFilterWeight: 0.5,
      onBegin: () => { 
        // 用户开始签名时触发
        hasSignature.value = true;
      },
      onEnd: () => { },
    });

    const hasSignature = ref(false);

    const signaturePadConfig = ref({
      width: window.innerWidth - 80,
      height: window.innerHeight - 20,
    });
    const dateTypeActions = [
      { name: 'YYYY年MM月DD日' },
      { name: 'YYYY-MM-DD' },
      { name: 'YYYY/MM/DD' },
    ];
    const currentControl = ref({
      signature: '',
      sealId: '',
      signatureId: '',
    });
    const signDateElement = ref({ format: '' });
    const signDateType = ref('');
    const orderNo = ref('');
    const sealCount = ref(0);
    const stampSealCount = ref(0);
    const signatureCount = ref(0);
    const signDateCount = ref(0);
    const hashSignControl = ref(false);
    const currentLocationIndex = ref(0);
    const baseImgWidth = window.innerWidth - 20;
    const aspecRatio = baseImgWidth / 800;
    const baseImgZoomRatio = ref(1);

    const zoom = ref(100);
    const controlItemRef = ref();

    const newSealSize = ref({
      width: 120,
      height: 120,
    });
    const newSignatureSize = ref({
      width: 112,
      height: 52,
    });
    // const baseUrl = window.appInfo.mobile_app_info.url + '/resrun-paas';
    let baseUrl = import.meta.env.VITE_APP_API_BASE_URL;
    const documentList: any = ref([]);
    const signControlInfo = ref({
      signerId: '',
      type: '',
      sealId: '',
    });
    const certificateInfo = ref<any>({
      certType: '',
      returnCode: '',
      returnMsg: '',
      tenantId: '',
      departId: '',
      holderType: '',
    });
    //   const tenantInfo = session.getItem('login_tenant_info');
    const tenantInfo = userStore.getTenantInfo;

    const userInfo = userStore.getUserInfo;

    const group = ref({
      name: 'itxst',
      put: true, //允许拖入
      pull: 'clone', //允许拖出
    });

    const clientWidth = ref(document.body.clientWidth);
    const clientHeight = ref(document.body.clientHeight);
    // console.log('clientHeight----',clientHeight.value);
    const docList = ref([
      {
        text: '开放签API接口V1.0.0',
        value: '1',
        selectValue: '1',
        docImages: [
          {
            page: 0,
            url: doc1,
          },
          {
            page: 1,
            url: doc2,
          },
        ],
        pageSize: 2,
        docControl: [],
      },
      {
        text: '中银安心无忧意外保障',
        value: '2',
        selectValue: '',
        docImages: [],
        pageSize: 0,
        docControl: [],
      },
    ]);
    const nowDocument: any = ref({
      selectValue: '',
    });
    const minTargetInfo = ref();
    const fromPath = ref('');
    const confirmTypeInfo: any = ref({
      confirmType: 'face',
      personalAccountStatus: '',
    });
    const willResult = ref(false);
    const confirmAction = ref('sign');
    const needOpenPersonlAccountVisible = ref(false);

    const signerCaAuthInfo = ref({
      authStatus: '2',
    });
    const router = useRouter();
    const route = useRoute();
    const signRuId = route.query.signRuId as string;
    const taskId = route.query.taskId as string;
    session.setItem('sign_task_id', taskId);
    // 添加控制提示显示的响应式变量
    const signControlTip = ref();
    const showSignControlTip = ref(false);

    showSignControlTip.value = (localStorage.getItem("showSignControlTip") == 'false') ? false : true;

    const getFromPath = () => {
      if (route.history && route.history.length > 1) {
        fromPath.value = route.history[route.history.length - 2].fullPath;
      }
    };

    onActivated(() => {
      getSignerInfo();
      console.log(fromPath.value, '路由信息');
      signCertificateVisible.value = false;
    });
    onBeforeUnmount(() => {
      Notify.clear();
    });
    onMounted(() => {
      // getFromPath()
      checkStatus();
      generateOrderNo();
      getSignNodeInfo();
    });
    // onBeforeRouteUpdate(() => {
    //     getFromPath()
    // })
    watch(
      () => nowDocument.value.maxWidth,
      (val) => {
        if (val) {
          zoom.value = Math.round(((window.innerWidth - 20) / val) * 100);
        }
      },
      { deep: true }
    );

    watch(
      () => nowDocument.value,
      (doc) => {
        if (doc) {
          // let signControls = doc.activeControl.filter(v=>v.controlType==='seal' || v.controlType==='chop-stamp' || v.controlType==='signature');
          if (defaultHasControl.value && controlChangeFlag.value == 'necessary_no_add') {
            disabledSignControls.value = true;
            signControlTip.value = '发起方已为您预设签署位置，不可添加，请直接完成签署';
          } else {
            disabledSignControls.value = false;
            signControlTip.value = '请点击签署控件区域的“添加”按钮，设置对应的签署控件，并拖拽至所需的签署位置';
          }
        }
      },
      { deep: true }
    );
    watch(
      () => sealCount.value,
      (value) => {
        if (!value) {
          isSignText.value = true;
          entSealId.value = '';
          //控件全部删除后恢复控件大小位默认值
          newSealSize.value = {
            width: 120,
            height: 120,
          };
          newSignatureSize.value = {
            width: 112,
            height: 52,
          };
      }
    }
    );
    watch(
      () => entSealId.value,
      (value) => {
        if (!value) {
          isSignText.value = true;
        }
      }
    );
    watch(
      () => signatureCount.value,
      (value) => {
        if (!value) {
          isSignText.value = true;
          signatureId.value = '';
        }
      }
    );
    watch(
      () => signatureCount.value,
      (value) => {
        if (!value) {
          isSignText.value = true;
        }
      }
    );
    watch(
      () => documentList.value,
      (list) => {
        const allControls = list.flatMap((item: any) => item.activeControl || []);
        let allSignControls = allControls.filter(
            (v: { controlType: string; }) =>
              v.controlType == 'seal' ||
              v.controlType == 'signature' ||
              v.controlType == 'chop-stamp'
          );
        const signControls = allControls.filter((control: any) => 
          ['seal', 'signature', 'chop-stamp', 'sign-date'].includes(control.controlType)
        );
        hashSignControl.value = signControls.length > 0;
        let signBase64Count = allSignControls.filter((v) => {
          return v.sealId || v.signature;
        });
        if (signBase64Count.length) {
          isSignText.value = false;
        } else {
          isSignText.value = true;
          currentControl.value.signatureId = '';
          currentControl.value.sealId = '';
        }
      },
      {
        deep: true,
      }
    );

    // watch(
    //   () => documentList.value,
    //   (list) => {
    //     const allControls = documentList.value.flatMap((item: any) => item.activeControl || []);
    //     const signControls = allControls.filter((control: any) => 
    //       ['seal', 'signature', 'chop-stamp', 'sign-date'].includes(control.controlType)
    //     );
    //     hashSignControl.value = signControls.length > 0;
    //   },
    //   {
    //     deep: true,
    //   }
    // );

    // 获取签署节点的签署要求
    async function getSignNodeInfo(){

      const { result: userCheckResult }: any = await Api.getVerifyCertificate({signRuId});
      // 签署主体是个人
      if(userCheckResult.holderType === TenantTypeEnum.PERSONAL){
        let platePersonSignAuth = '';
        const personalSignAuthRes = await Api.getPlatePersonalSignAuth({});
        // 获取平台总的签署要求
        if (personalSignAuthRes.code === 200) {
          platePersonSignAuth = personalSignAuthRes.result.personalSignAuthType;
        }
        console.log('平台总的签署要求', platePersonSignAuth);
        
        // 如果平台配置要求个人签署必须认证或无需认证，则直接使用该值
        if(platePersonSignAuth === 'required' || platePersonSignAuth === 'not_required'){
          personalSignAuth.value = platePersonSignAuth;
        } else {
          // 查询该签署流程的总的签署要求
          const { result: ruInfo }: any = await Api.getDocInfoByRuId({signRuId});
          console.log('签署流程的总的签署要求', ruInfo.baseVo.personalSignAuth);
          if(ruInfo && ruInfo.baseVo.personalSignAuth === 'required' || ruInfo.baseVo.personalSignAuth === 'not_required'){
            personalSignAuth.value = ruInfo.baseVo.personalSignAuth;
          } else {
            // 查询该签署节点的签署要求
            const { result: signNodeResult }: any = await Api.getSignNodeConfig({taskId});
            console.log('签署节点的签署要求', signNodeResult.personalSignAuth);
            if(signNodeResult && signNodeResult.personalSignAuth){
              personalSignAuth.value = signNodeResult.personalSignAuth;
            } else {
              personalSignAuth.value = 'required';
            }
          }
        }
      } else {
        personalSignAuth.value = 'required';
      }
      
    }

    async function generateOrderNo() {
      let { result, code } = await Api.getOrderNo({ mainId: taskId });
      if (code == 200) {
        orderNo.value = result;
      }
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

    async function handleFace() {
      let authInfo = window.appInfo.auth_app_info;

      const token = session.getItem(APP_TOKEN);

      if (confirmTypeInfo.value.personalAccountStatus == '-1') {
        needOpenPersonlAccountVisible.value = true;
        return;
      }
      let { result, code } = await Api.getConfirmType({ operateType: 'submit_sign' });
      if (code == 200) {
        // confirmTypeInfo.value = result;
        if (token) {
          let appInfo = {
            token: token,
            appCode: authInfo.appCode,
            appId: authInfo.appId,
            taskId: taskId,
            departId: userInfo.loginDepartId,
            confirmType: confirmTypeInfo.value.confirmType,
            orderNo: orderNo.value,
            signRuId,
            authStatus: result.personalAccountStatus,
            certType: signerCaAuthInfo.value.certType,
            callbackPage: callbackPage,
          };
          let paramsString = new URLSearchParams(appInfo).toString();
          // console.log("window.open",authInfo.url +'/#/beforeCheck' + '?' + paramsString);
          window.open(authInfo.url + '/#/beforeCheck' + '?' + paramsString, '_self');
        }
      }
    }

    //选择批量页面类型
    function onConfirm(value) {
      elementControl.value.propertyPageType = value;
      showPagePicker.value = false;
    }

    //获取证书类型与签署人类型以及是否实名认证

    async function getCaTypeAndSignerInfo() {
      let { result, code }: any = await Api.verifyCertificate({ signRuId });
      if (code == 200) {
        signerCaAuthInfo.value = {
          ...signerCaAuthInfo.value,
          ...result,
        };
        if (result.tenantId && result.certType != 1) {
          let tenantResult = await Api.getSignAuthStatus({ tenantId: result.tenantId });
          if (tenantResult.code == 200) {
            signerCaAuthInfo.value = {
              ...signerCaAuthInfo.value,
              authStatus: tenantResult.result,
            };
            //个人 未实名 且证书类型为ca证书
            // if (signControlInfo.value.type == '1' && tenantResult.result != '2' && signerCaAuthInfo.value.certType != '1') {
            //     Notify({ type: 'danger', message: '您尚未完成实名认证，请先完成实名认证后再签署', duration: 0, className: 'auth-notify' })
            // }
            // if (signControlInfo.value.type == '2' && tenantResult.result != '2') {
            //     Notify({ type: 'danger', message: '您当前的企业尚未完成实名认证，请联系企业管理员完成实名认证后再签署', duration: 0, className: 'auth-notify' })
            // }
          }
        }
      }
    }

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
              taskType: 'sign',
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
          } else {
            await getSignerInfo();
          }
          init();
        });
      }
    }
    //获取签约人
    async function getSignerInfo() {
      let { result } = await Api.getSignerIdAndSealId({ ruId: signRuId });
      if (result) {
        signControlInfo.value = result;
        //   getCaTypeAndSignerInfo();
        await checkCaAndAuthStatus();
      }
    }
    // 获取文档
    async function init() {
      // console.log(Api, '接口api');
      let { result } = await Api.getDocFiles({ signRuId: signRuId });
      if (result && result.length) {
        controlList.value = [];
        docs.value = result;
        docId.value = result[0].id;
        annexId.value = result[0].annexId;
        docs.value.map(async (v: any, i: number) => {
          v.pageSize = v.docPage;
          let { result } = await Api.getDocImgsInSign({ signFileId: v.id });
          if (result && Array.isArray(result)) {
            v.images = result.sort((a, b) => a.page - b.page);
            if (i == 0) {
              images.value = result;
            }
          }
        });
        setTimeout(() => {
          getAllDocControl();
        }, 1000);
        getDocImgs();
      }
    }

    //文档切换
    async function quickHandleDocChange() {
      // console.log('快速文档切换----');
      // 遍历文档列表，按照顺序进行切换
      // 若当前文档是最后一个，则切换到第一个文档
      // 若当前文档不是最后一个，则切换到下一个文档
      const currentIndex = documentList.value.findIndex((v: any) => v.signRuDocId == docId.value);
      let nextIndex = 0;
      if (currentIndex < documentList.value.length - 1) {
        nextIndex = currentIndex + 1;
      }
      docId.value = documentList.value[nextIndex].signRuDocId;
      nowDocument.value = documentList.value[nextIndex];
      handleDocChange(docId.value);
    }
    //文档切换
    async function handleDocChange(val: any) {
      // console.log('文档切换----');
      let matchDoc = documentList.value.find((item) => item.signRuDocId == val);
      nowDocument.value = matchDoc;
      docId.value = val;
      images.value = matchDoc.images;
      // console.log(docId.value,'docId.value----');
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
          }
        });
      }
    }
    // 获取控件
    async function getAllDocControl() {
      documentList.value = [];
      let { result } = await Api.getDocControlsByDocId({
        signRuId,
        controlTypeList: 'seal,signature,sign-date,chop-stamp',
        signerId: signControlInfo.value.signerId,
      });
      let controlsResult = result.controlList;
      controlChangeFlag.value = result.controlChangeFlag;

      if (controlsResult.length) {
        defaultHasControl.value = true;
      } else {
        defaultHasControl.value = false;
      }

      controlsResult = controlsResult.filter(
        (v) =>
          v.controlType == 'seal' ||
          v.controlType == 'signature' ||
          v.controlType == 'sign-date' ||
          v.controlType == 'chop-stamp'
      );
      if (controlsResult) {
        controlList.value = controlList.value.concat(controlsResult);
        let flatControls: any = [];
        let allControls = cloneDeep(controlList.value);
        allControls.map((item: any) => {
          // if(item.controlType == 'chop-stamp'){
          //     item.offsetX = item.offsetX * aspecRatio;
          // }
          item.showPopover = false;
          if (
            ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(item.controlType) &&
            item.propertyVoList
          ) {
            let pageConfig = item.propertyVoList.filter(
              (u) => u.propertyType == 'page_config'
            )[0];
            let pageCustomConfig = item.propertyVoList.filter(
              (u) => u.propertyType == 'custom'
            )[0];
            const frontUid =
              'api_uid_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16);
            item.propertyVoList.map((v, i) => {
              if (v.propertyType == 'relation_doc') {
                //将每个relation_doc 控件都推入flatControls数组
                flatControls.push({
                  ...item,
                  propertyVo: v,
                  unequalId: v.id, //用于渲染
                  uid: frontUid,
                  signRuDocId: v.propertyValue,
                  controlDocId: v.propertyValue,
                  propertyVoList: item.propertyVoList,
                  controlPageInfo: {
                    ...pageConfig,
                    controlDocId: v.propertyValue,
                  },
                  pageCustomConfig: {
                    ...pageCustomConfig,
                  },
                });
              }
            });
          } else {
            //如果是填写控件，则controlDocId 为 signRuDocId
            item.controlDocId = item.signRuDocId;
            item.unequalId = parseInt(
              new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
            ).toString(10);
            item.propertyVoLis = item.propertyVoList || [];
            item.controlPageInfo = {};
            flatControls.push(item);
          }
        });
        // console.log(flatControls, 'flatControls-------------');

        //将控件按文档分类
        let groupControls: any = [];

        // 遍历flatControls数组
        flatControls.forEach((m: any, i) => {
          let matchSameUid = flatControls.filter(
            (u) =>
              u?.uid == m.uid &&
              ['signature', 'sign-date', 'seal', 'chop-stamp'].includes(u.controlType)
          );
          if (matchSameUid.length > 1) {
            matchSameUid.map((u: any) => {
              u.isBatch = true;
            });
          }
          // 在groupControls数组中查找与m.controlDocId匹配的控件
          let matchControl = groupControls.find((v) => v.controlDocId == m.controlDocId);
          // 如果没有找到匹配的控件
          if (!matchControl) {
            // 将m添加到groupControls数组中
            let controlItem = {
              controlDocId: m.controlDocId,
              id: m.controlDocId, //分组id，取文档id
              propertyVoList: m.propertyVoList,
              controlPageInfo: m.controlPageInfo,
              pageCustomConfig: m.pageCustomConfig,
              controls: buildResponseControl([
                {
                  ...m,
                  propertyVo: undefined,
                  signRuDocId: m.controlDocId,
                  propertyVoList: m.propertyVoList,
                },
              ]),
            };
            groupControls.push(controlItem);
          } else {
            // 将m添加到matchControl的controls数组中
            matchControl.controls.push({
              ...m,
              controlDocId: m.controlDocId,
              propertyVoList: m.propertyVoList,
            });
            // 对matchControl的controls数组进行处理
            matchControl.controls = buildResponseControl(matchControl.controls);
          }
        });

        // console.log(groupControls, aspecRatio, '分组控件--');
        //将控件按页码配置按文档进行设置
        groupControls.map((item: any) => {
          const currentDoc = docs.value.find((v) => v.id == item.controlDocId);
          const maxImageWidthItem = currentDoc.images.reduce((max, item) => {
            return parseFloat(item.imageWidth) > parseFloat(max.imageWidth) ? item : max;
          }, currentDoc.images[0]);
          // baseImgZoomRatio.value = Number((maxImageWidthItem.imageWidth / baseImgWidth).toFixed(3));
          // newSealSize.value = {
          //     width:160 / baseImgZoomRatio.value,
          //     height:160 / baseImgZoomRatio.value
          // }
          const { targets, maxWidth } = pageScaling(currentDoc.images);

          item.controls.forEach((v) => {
            v.offsetX = Number(v.offsetX);
            v.offsetY = Number(v.offsetY);
            const basaeColtrol = getColtrolByType(v.controlType);
            if (['signature', 'sign-date', 'seal', 'chop-stamp'].includes(v.controlType)) {
              item.controls = item.controls && item.controls.filter((m) => m.uid != v.uid);
              if (
                v.controlPageInfo?.propertyType == 'page_config' &&
                v.controlPageInfo?.propertyValue != 'custom'
              ) {
                for (let i = 0; i < currentDoc?.docPage; i++) {
                  const target = targets[i];
                  const offsetWidth = (maxWidth - target.width) / 2;
                  const baseZoomRatio = Number(
                    (maxImageWidthItem.imageWidth / baseImgWidth).toFixed(3)
                  ); //图片缩放比
                  const targetControlZoomRatio = target.width / v.pageWidth; //当前页面控件缩放比
                  let unequalId = buildUUID() + '_' + i;
                  let shouldAddControl = false;

                  if (v.controlPageInfo.propertyValue === 'all') {
                    shouldAddControl = true;
                  } else if (
                    v.controlPageInfo.propertyValue === 'odd_number' &&
                    (i + 1) % 2 !== 0
                  ) {
                    shouldAddControl = true;
                  } else if (
                    v.controlPageInfo.propertyValue === 'even_number' &&
                    (i + 1) % 2 === 0
                  ) {
                    shouldAddControl = true;
                  }
                  if (shouldAddControl) {
                    item.controls.push({
                      ...v,
                      isBatch: true,
                      unequalId,
                      propertyVoList: v.propertyVoList,
                      uid: v.uid,
                      signRuDocId: v.signRuDocId,
                      signerId: v.signerId,
                      page: i,
                      controlClick: false,
                      size: {
                        width: Number(v.width),
                        height: Number(v.height),
                        minWidth: basaeColtrol.size.minWidth,
                        minHeight: basaeColtrol.size.minHeight,
                      },
                      position: {
                        left:
                          v.controlType == 'chop-stamp'
                            ? offsetWidth + target.width - v.width
                            : offsetWidth + v.offsetX >= offsetWidth + target.width - v.width
                              ? offsetWidth + target.width - v.width
                              : offsetWidth + v.offsetX,
                        top: target.transformHeight + (i + 1) * 16 + v.offsetY,
                        page: i,
                      },
                    });
                  }
                }
              } else if (v.controlPageInfo?.propertyValue == 'custom') {
                let pages = parseInputPages(v.pageCustomConfig.propertyValue.replace(/\s/g, ''));
                pages.map((page) => {
                  const target = targets[page - 1];
                  const offsetWidth = Math.round((maxWidth - target.width) / 2);
                  console.log(
                    v.offsetY,
                    target,
                    page * 16,
                    Number(target.transformHeight) + page * 16 + v.offsetY,
                    '控件顶部距离'
                  );
                  item.controls.push({
                    ...v,
                    //   isBatch: true,
                    uid: v.uid,
                    signRuDocId: v.signRuDocId,
                    signerId: v.signerId,
                    propertyVoList: v.propertyVoList,
                    controlClick: false,
                    page: page - 1,
                    size: {
                      width: Number(v.width),
                      height: Number(v.height),
                      minWidth: basaeColtrol.size.minWidth,
                      minHeight: basaeColtrol.size.minHeight,
                    },
                    position: {
                      left:
                        v.controlType == 'chop-stamp'
                          ? offsetWidth + target.width - v.width
                          : offsetWidth + v.offsetX >= offsetWidth + target.width - v.width
                            ? offsetWidth + target.width - v.width
                            : offsetWidth + v.offsetX,
                      top: target.transformHeight + page * 16 + v.offsetY,
                      page: page - 1,
                    },
                  });
                });
              }
            } else {
              v.position = {
                left: parseInt(v.position.left),
                top:
                  parseInt(v.offsetY) * aspecRatio +
                  (16 * aspecRatio + 1131 * aspecRatio) * parseInt(v.page),
                page: v.page,
              };
            }
          });
        });

        // console.log(groupControls, '分组后的控件--');
        //按文档进行控件设置
        for (let i = 0; i < docs.value.length; i++) {
          let matchControl = groupControls.find((v) => v.controlDocId == docs.value[i].id);
          setDocumentList(docs.value[i], matchControl?.controls || []);
        }
        let allFlatControls = groupControls.flatMap((v) => v.controls);
        //计算控件数量
        allFlatControls.map((item: any) => {
          if (item.controlType == 'seal') {
            sealCount.value += 1;
          }
          if (item.controlType == 'chop-stamp') {
            stampSealCount.value += 1;
          }
          if (item.controlType == 'signature') {
            signatureCount.value += 1;
          }
          if (item.controlType == 'sign-date') {
            signDateCount.value += 1;
          }
        });
      }
    }
    //整理文档列表
    function setDocumentList(doc: any, controls: any) {
      let machDoc = documentList.value.find((v) => v.signRuDocId == doc.id);
      if (machDoc) {
        machDoc.activeControl = controls;
      } else {
        const { targets, maxWidth } = pageScaling(doc.images);
        documentList.value.push({
          active: false,
          text: doc.name,
          value: doc.id,
          documentName: doc.name,
          targets: targets,
          maxWidth: maxWidth,
          annexFileId: '',
          signRuDocId: doc.id,
          activeControl: cloneDeep(controls),
          pageSize: images.value.length,
          docPage: doc.docPage,
          images: cloneDeep(doc.images),
          imageAllHeight:
            targets[targets.length - 1].transformHeight +
            targets[targets.length - 1].height +
            images.value.length * CanvasZoom.space,
          imageLoading: true,
        });
      }
      // console.log(documentList.value,'documentList.value------');
      nowDocument.value = documentList.value.find((v: any) => v.signRuDocId == docId.value);
      nowDocument.value = {
        ...nowDocument.value,
        selectValue: docId.value,
      };
      const maxImageWidthItem = nowDocument.value.images.reduce((max, item) => {
        return parseFloat(item.imageWidth) > parseFloat(max.imageWidth) ? item : max;
      }, nowDocument.value.images[0]);
      baseImgZoomRatio.value = Number((maxImageWidthItem.imageWidth / baseImgWidth).toFixed(3));
      newSealSize.value = {
        width: 120,
        height: 120,
      };
      newSignatureSize.value = {
        width: 112,
        height: 52,
      };

      // controlMove.activeControl = buildResponseControl(controls);
      //receiveActive(signers.value);
    }

    // 构建文档控件
    function buildResponseControl(controls: any) {
      const controlTem: any = [];
      if (controls && controls.length > 0) {
        controls.forEach((item: any) => {
          const basaeColtrol = getColtrolByType(item.controlType);
          const baseImgWidth = window.innerWidth - 20;

          // let matchDoc:any = docs.value.filter((v:any) =>v.id == item.signRuDocId)[0]
          // const maxImageWidthItem = matchDoc.images.reduce((max, item) => {
          //     return parseFloat(item.imageWidth) > parseFloat(max.imageWidth) ? item : max;
          // }, matchDoc.images[0]);
          // let { targets, maxWidth } = pageScaling(matchDoc.images);
          // let target = targets[item.page];
          // let offsetWidth = (maxWidth - target.width) / 2;
          // const baseZoomRatio:number =  (maxImageWidthItem.imageWidth / baseImgWidth).toFixed(3);  //图片缩放比
          // const targetControlZoomRatio =   target.width / item.pageWidth;   //当前页面控件缩放比

          controlTem.push({
            id: parseInt(
              new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
            ).toString(16),
            uid: item.id,
            propertyVoList: item.propertyVoList,
            controlPageInfo: item.controlPageInfo,
            pageCustomConfig: item.pageCustomConfig,
            icon: basaeColtrol.icon,
            name: item.name,
            title: basaeColtrol.title,
            pageWidth: Number(item.pageWidth),
            pageHeight: Number(item.pageHeight),
            controlType: item.controlType,
            originType: item.originType,
            page: item.page,
            placeholder: item.placeholder,
            value: item.value,
            width: item.width,
            height: item.height,
            controlClick: controlChangeFlag.value == 'not_necessary' ? true : false,
            zoom: basaeColtrol.zoom,
            delete: controlChangeFlag.value == 'not_necessary' ? true : false,
            move: controlChangeFlag.value == 'not_necessary' ? true : false,
            disabled: unref(isDetail) ? true : false,
            required: item.isRequired == 1,
            signature: '', //用于同步不同文档的签名签章
            annexId: '',
            offsetX: item.offsetX,
            offsetY: item.offsetY,
            attr: basaeColtrol.attr,
            signRuDocId: item.signRuDocId,
            colorIndex: 7,
            signerId: item.signerId,
            signerType: item.signerType,
            format: item.format,
            today: item.controlType == 'sign-date' ? getTodayDateByFormat(item.format) : null,
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
            // position: {
            //     left: target.width * (item.offsetX / item.pageWidth) + offsetWidth,
            //     top:  target.transformHeight + (item.page+1) * 16 + target.height * (item.offsetY / item.pageHeight),
            //     page:parseInt(item.page),
            // },
            user: {},
          });
        });
      }
      return controlTem;
    }

    // 重新计算控件位置
    function refreshControlPosition(element: any) {
      let pages: number[] = [];
      let docKeys: string[] = [];
      documentList.value.map((v) => {
        if (element.oddEventType == 'custom') {
          pages = parseInputPages(element.custom.replace(/\s/g, ''));
        } else {
          for (let i = 0; i < v.pageSize; i++) {
            if (element.oddEventType == 'all') {
              pages.push(i);
            }
            if (element.oddEventType == 'odd_number' && (i + 1) % 2 != 0) {
              pages.push(i);
            }
            if (element.oddEventType == 'odd_number' && (i + 1) % 2 == 0) {
              pages.push(i);
            }
          }
        }
      });
      docKeys = element.propertyVoList
        .filter((item) => item.propertyType === 'relation_doc')
        .map((item) => item.propertyValue);
      const minTarget = findMinImageSize(documentList.value, docKeys, Array.from(new Set(pages)));
      const elementTarget = nowDocument.value.targets[element.position.page];
      const elementOffsetWidth = (nowDocument.value.maxWidth - elementTarget.width) / 2;
      const elementOffsetX = element.position.left - elementOffsetWidth;
      const elementOffsetY = currentPosition(element.position.top, elementTarget);

      documentList.value.map((v: any) => {
        const { targets, maxWidth } = v;
        v.activeControl.map((item: any) => {
          const target = targets[item.position.page];
          const offsetWidth = (maxWidth - target.width) / 2;

          //过滤掉当前移动的控件计算其他复制的控价的位置
          if (item.uid == element.uid && !item.controlClick) {
            // if (item.uid == element.uid) {
            const target = v.targets[item.position.page];
            const pageElementOffsetWidth = (v.maxWidth - target.width) / 2;
            item.position = {
              // left: element.controlType=='chop-stamp'?
              // parseFloat((target.width - element.size.width + offsetWidth).toFixed(2)):
              //         elementOffsetX + offsetWidth > target.width?parseFloat((target.width - element.size.width + offsetWidth).toFixed(2)):
              //         ( (elementOffsetX + offsetWidth < target.width)?parseFloat((elementOffsetX + offsetWidth).toFixed(2)):elementOffsetX + offsetWidth),
              // top:  target.transformHeight + (item.position.page + 1) * 16 + elementOffsetY,
              page: item.position.page,
              left:
                element.controlType == 'chop-stamp'
                  ? pageElementOffsetWidth + target.width - element.size.width
                  : pageElementOffsetWidth + elementOffsetX >=
                    pageElementOffsetWidth + target.width - element.size.width
                    ? pageElementOffsetWidth + target.width - element.size.width
                    : pageElementOffsetWidth + elementOffsetX,
              top: recaculateBatchControlPosInPage(
                item,
                v.targets,
                elementOffsetX,
                elementOffsetY,
                v.maxWidth,
                minTarget
              ).y,
            };
          }
        });
      });
      nowDocument.value = documentList.value.find((v) => v.signRuDocId == docId.value);
    }

    //控件点击操作
    function controlMousedown(element: any) {
      console.log(element,'点击控件-------')
      // controlMove.elementMove = element;
      nowDocument.value.activeControl.forEach((item: any) => {
        item.controlClick = false;
      });
      element.controlClick = true;
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

    function checkHasSignPos() {
      let flag = false;
      let controls = documentList.value.flatMap((item: any) => item.activeControl);
      //指定位置originType==2 或者拖拽空间originType==3 有一个即可签署
      let signControl = controls.filter(
        (v: any) =>
          (v.originType == 1 || v.originType == 3 || v.originType == 2) &&
          v.controlType != 'sign-date'
      );
      if (signControl.length) {
        flag = true;
      } else {
        flag = false;
      }
      return flag;
    }
    // 添加签章
    function handleAddSeal() {
      let scrollTop = document.getElementsByClassName('content')[0].scrollTop;
      let scrollPage = 0;
      let sealControl: any = controlListSource.filter((v: any) => v.type == 'seal')[0];

      const { targets, maxWidth } = nowDocument.value;

      targets.forEach((v) => {
        let zoomImgHeight = ((v.transformHeight + 16 * v.page) * zoom.value) / 100;
        let zoomScroll = scrollTop;
        if (
          zoomScroll + 200 > zoomImgHeight &&
          zoomScroll < zoomImgHeight + (v.height * zoom.value) / 100
        ) {
          scrollPage = v.page;
        }
      });

      const target = targets[scrollPage];
      const offsetWidth = (maxWidth - target.width) / 2;
      const offsetX = 80;
      const offsetY = 80;
      const pageWidth = target.width * baseImgZoomRatio.value;
      const pageHeight = target.height * baseImgZoomRatio.value;

      let uid = parseInt(
        new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
      ).toString(9);
      let unequalId = buildUUID();

      sealControl = {
        ...sealControl,
        uid: uid,
        unequalId,
        position: {
          left: target.width * (offsetX / pageWidth) + offsetWidth,
          top:
            target.transformHeight +
            (scrollPage + 1) * 16 +
            target.height * (offsetY / pageHeight),
          page: scrollPage,
        },
        title: '签章位置',
        propertyVoList: [
          {
            controlId: uid,
            id:
              'relation_doc_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'relation_doc',
            propertyValue: docId.value,
          },
          {
            controlId: uid,
            id:
              'page_config_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'page_config',
            propertyValue: 'custom',
          },
          {
            controlId: uid,
            id:
              'page_config_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'custom',
            propertyValue: scrollPage + 1,
          },
        ],
        // offsetX:80 * baseImgZoomRatio.value,
        // offsetY:80 * baseImgZoomRatio.value,
        pageWidth,
        pageHeight,
        colorIndex: 7,
        delete: true,
        showPopover: false,
        signerId: signControlInfo.value.signerId,
        countIndex: sealCount.value + signatureCount.value + signDateCount.value,
        setting: false,
        signRuDocId: docId.value,
        annexId: sealAnnexId.value,
        sealId: currentControl.value.sealId,
        originType: 3,
        // width:Number((sealControl.size.width / baseImgZoomRatio.value).toFixed(2)),
        // height:Number((sealControl.size.height / baseImgZoomRatio.value).toFixed(2)),
        size: {
          width: sealControl.size.width,
          height: sealControl.size.height,
        },
        controlType: 'seal',
      };
      sealCount.value += 1;
      nowDocument.value.activeControl.push(sealControl);
      controlList.value.push(sealControl);
    }
    //添加骑缝章
    function handleAddChopSeal() {
      let scrollTop = document.getElementsByClassName('content')[0].scrollTop;
      let basePageHeight = BaseSize.height * aspecRatio;
      let scrollPage = Math.floor(scrollTop / basePageHeight);
      let stampSealControl: any = controlListSource.filter((v: any) => v.type == 'chop-stamp')[0];
      let baseStampSealControl = cloneDeep(stampSealControl);
      //大于图片一半高度时加一页
      if (scrollTop % basePageHeight > basePageHeight / 2) {
        scrollPage += 1;
      }
      //最大页码不超过总页数
      if (scrollPage >= nowDocument.value.pageSize) {
        scrollPage = nowDocument.value.pageSize;
      }

      const { targets, maxWidth } = nowDocument.value;
      const offsetY = 80;

      let pages: number[] = [];
      let docKeys: string[] = [];
      nowDocument.value.images.map((v) => {
        pages.push(v.page);
      });
      docKeys = [nowDocument.value.signRuDocId];
      const minTarget = findMinImageSize(documentList.value, docKeys, Array.from(new Set(pages)));
      let uid = parseInt(
        new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
      ).toString(10);
      for (let i = 0; i < nowDocument.value.docPage; i++) {
        const target = targets[i];
        const offsetWidth = (maxWidth - target.width) / 2;
        const offsetX = target.width - baseStampSealControl.size.width;
        const pageWidth = target.width * baseImgZoomRatio.value;
        const pageHeight = target.height * baseImgZoomRatio.value;

        let unequalId = buildUUID() + '_' + i;
        stampSealCount.value += 1;
        stampSealControl = {
          ...stampSealControl,
          position: {
            left: offsetX + offsetWidth,
            top: target.transformHeight + (i + 1) * 16 + offsetY,
            page: i,
          },
          title: '骑缝章',
          colorIndex: 7,
          // page:scrollPage,
          uid: uid,
          unequalId,
          minTarget,
          offsetX,
          offsetY,
          pageWidth,
          pageHeight,
          page: i,
          isBatch: true,
          delete: true,
          showPopover: false,
          signerId: signControlInfo.value.signerId,
          countIndex: sealCount.value + signatureCount.value + signDateCount.value,
          setting: false,
          signRuDocId: docId.value,
          annexId: sealAnnexId.value,
          sealId: currentControl.value.sealId,
          propertyVoList: [
            {
              controlId: uid,
              id:
                'relation_doc_id' +
                parseInt(
                  new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
                ).toString(16),
              propertyType: 'relation_doc',
              propertyValue: docId.value,
            },
            {
              controlId: uid,
              id:
                'page_config_id' +
                parseInt(
                  new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
                ).toString(16),
              propertyType: 'page_config',
              propertyValue: 'all',
            },
          ],
          originType: 3,
          width: Number(baseStampSealControl.size.width),
          height: Number(baseStampSealControl.size.height),
          size: {
            width: newSealSize.value.width
              ? newSealSize.value.width
              : Number(baseStampSealControl.size.width),
            height: newSealSize.value.height
              ? newSealSize.value.height
              : Number(baseStampSealControl.size.height),
          },
          controlType: 'chop-stamp',
        };
        nowDocument.value.activeControl.push(stampSealControl);
        controlList.value.push(stampSealControl);
      }
    }
    // 添加签名
    function handleAddSignature() {
      let scrollTop = document.getElementsByClassName('content')[0].scrollTop;
      let basePageHeight = BaseSize.height * aspecRatio;
      let scrollPage = 0;
      let signControl: any = controlListSource.filter((v: any) => v.type == 'signature')[0];

      const { targets, maxWidth } = nowDocument.value;
      targets.forEach((v) => {
        let zoomImgHeight = ((v.transformHeight + 16 * v.page) * zoom.value) / 100;
        let zoomScroll = scrollTop;
        if (
          zoomScroll + 200 > zoomImgHeight &&
          zoomScroll < zoomImgHeight + (v.height * zoom.value) / 100
        ) {
          scrollPage = v.page;
        }
      });
      const target = targets[scrollPage];
      const offsetWidth = (maxWidth - target.width) / 2;
      const offsetX = 80;
      const offsetY = 80;
      const pageWidth = target.width;
      const pageHeight = target.height;

      // console.log(signControl.size.width, signControl.size.height, 'signControl.size-----');
      // console.log(newSignatureSize.value.width, newSignatureSize.value.height, 'newSignatureSize.value-----');

      let uid = parseInt(
        new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
      ).toString(11);
      let unequalId = buildUUID();
      signControl = {
        ...signControl,
        position: {
          left: offsetX + offsetWidth,
          top: target.transformHeight + (scrollPage + 1) * 16 + offsetY,
          page: scrollPage,
        },
        title: '签名位置',
        uid,
        unequalId,
        offsetX: 80,
        offsetY: 80,
        pageWidth,
        pageHeight,
        controlClick: false,
        colorIndex: 7,
        page: scrollPage,
        countIndex: sealCount.value + signatureCount.value + signDateCount.value,
        setting: false,
        delete: true,
        showPopover: false,
        originType: 3,
        signerId: signControlInfo.value.signerId,
        signature: singatureBae64.value,
        signRuDocId: docId.value,
        propertyVoList: [
          {
            controlId: uid,
            id:
              'relation_doc_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'relation_doc',
            propertyValue: docId.value,
          },
          {
            controlId: uid,
            id:
              'page_config_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'page_config',
            propertyValue: 'custom',
          },
          {
            controlId: uid,
            id:
              'page_config_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'custom',
            propertyValue: scrollPage + 1,
          },
        ],
        
        width: signControl.size.width,
        height: signControl.size.height,

        size: {
          width: newSignatureSize.value.width ? newSignatureSize.value.width : signControl.size.width,
          height: newSignatureSize.value.height ? newSignatureSize.value.height : signControl.size.height,
        },
        controlType: 'signature',
      };
      
      signatureCount.value += 1;
      nowDocument.value.activeControl.push(signControl);
      controlList.value.push(signControl);
    }
    // 添加日期
    function handleAddSignDate() {
      let scrollTop = document.getElementsByClassName('content')[0].scrollTop;
      let basePageHeight = BaseSize.height * aspecRatio;
      let scrollPage = Math.floor(scrollTop / basePageHeight);
      let signDateControl: any = controlListSource.filter((v: any) => v.type == 'sign-date')[0];
      //大于图片一半高度时加一页
      if (scrollTop % basePageHeight > basePageHeight / 2) {
        scrollPage += 1;
      }
      //最大页码不超过总页数
      if (scrollPage >= nowDocument.value.pageSize) {
        scrollPage = nowDocument.value.pageSize;
      }
      const { targets, maxWidth } = nowDocument.value;

      const target = targets[scrollPage];
      const offsetWidth = (maxWidth - target.width) / 2;
      const offsetX = 80;
      const offsetY = 80;
      // const pageWidth = target.width * baseImgZoomRatio.value;
      // const pageHeight = target.height * baseImgZoomRatio.value;
      const pageWidth = target.width;
      const pageHeight = target.height;

      let uid = parseInt(
        new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
      ).toString(12);
      let unequalId = buildUUID();
      signDateControl = {
        ...signDateControl,
        position: {
          left: offsetX + offsetWidth,
          top: target.transformHeight + (scrollPage + 1) * 16 + offsetY,
          page: scrollPage,
        },
        uid,
        unequalId,
        title: '签署日期',
        // format: 'YYYY-MM-DD',
        originType: 3,
        offsetX,
        offsetY,
        pageWidth,
        pageHeight,
        page: scrollPage,
        showPopover: false,
        colorIndex: 7,
        signRuDocId: docId.value,
        signerId: signControlInfo.value.signerId,
        countIndex: sealCount.value + signatureCount.value + signDateCount.value,
        setting: false,
        propertyVoList: [
          {
            controlId: uid,
            id:
              'relation_doc_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'relation_doc',
            propertyValue: docId.value,
          },
          {
            controlId: uid,
            id:
              'page_config_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'page_config',
            propertyValue: 'custom',
          },
          {
            controlId: uid,
            id:
              'page_config_id' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'custom',
            propertyValue: scrollPage + 1,
          },
        ],
        delete: true,
        width: signDateControl.size.width,
        height: signDateControl.size.height,
        size: {
          width: signDateControl.size.width,
          height: signDateControl.size.height,
        },
        controlType: 'sign-date',
      };
      signDateCount.value += 1;
      nowDocument.value.activeControl.push(signDateControl);
      controlList.value.push(signDateControl);
    }
    //删除控件
    function controlDelete(element: any) {
      let index = 0;
      if (element.id) {
        index = nowDocument.value.activeControl.filter((el: any) => el?.id == element?.id)[0]
          .countIndex;
      } else {
        index = nowDocument.value.activeControl.filter(
          (el: any) => el.countIndex == element.countIndex
        )[0].countIndex;
        controlList.value = controlList.value.filter((el) => el.countIndex != element.countIndex);
      }
      deleteIdList.value.push(element.uid);

      // nowDocument.value.activeControl = nowDocument.value.activeControl.filter((item:any) => {
      //     return item.uid !== element.uid
      // })
      documentList.value.map((v) => {
        v.activeControl.forEach((item: any) => {
          if (item.uid == element.uid) {
            if (element.controlType == 'seal' && sealCount.value > 0) {
              sealCount.value -= 1;
            }
            if (element.controlType == 'chop-stamp' && stampSealCount.value > 0) {
              stampSealCount.value -= 1;
            }
            if (element.controlType == 'signature' && signatureCount.value > 0) {
              signatureCount.value -= 1;
            }
            if (element.controlType == 'sign-date' && signDateCount.value > 0) {
              signDateCount.value -= 1;
            }
          }
        });
        v.activeControl = v.activeControl.filter((item: any) => {
          return item.uid !== element.uid;
        });
      });
      // console.log()
      // nowDocument.value = documentList.value.filter(item=>item.id == docId.value);
    }
    //控件点击
    function controlShowMenu(e, element) {
      // console.log(e, element);
      nowDocument.value.activeControl.forEach((el: any) => {
        if (el.uid == element.uid) {
          el.showPopover = true;
        } else {
          el.showPopover = false;
        }
      });
    }
    //菜单点击设置控件签章、签名、日期格式相关
    function controlMenu(e, element, isBatch) {
      if (isBatch) {
        if (element.propertyVoList) {
          docKeys.value = [];
          element.propertyVoList.forEach((item) => {
            if (item.propertyType == 'relation_doc') {
              docKeys.value.push(item.propertyValue);
            }
            if (item.propertyType == 'page_config') {
              elementControl.value.propertyPageType = item.propertyValue;
            }
            if (item.propertyType == 'custom') {
              elementControl.value.custom = item.propertyValue;
            }
          });
        }
        if (element.controlType == 'chop-stamp') {
          elementControl.value.isChopStamp = true;
        } else {
          elementControl.value.isChopStamp = false;
        }
        batchSetShow.value = true;
        element.showPopover = false;
        elementBatch.value = element;
        return;
      }
      if (unref(isDetail)) return;
      currentControl.value = element;
      if (element.controlType == 'seal' || element.controlType == 'chop-stamp') {
        //类型为企业签章 且签章id存在直接获取签章图片，签章id不存在弹出签章图片列表
        if (signControlInfo.value.type == '2' && !signControlInfo.value.sealId) {
          sealVisible.value = true;
          getSealIds();
        } else {
          checkSignSeal();
        }
        return;
      } else if (element.controlType == 'sign-date') {
        signDateElement.value = element;
        signDatevisible.value = true;
        signDateType.value = element.format;
        return;
      } else {
        getSignaturs();
        personSignatureVisible.value = true;
        // signatureVisible.value = true;
        // setTimeout(() => {
        //   showPad.value = true;
        // });
        // getSignaturs()
        return;
      }
    }
    //日期格式选择
    function onDateTypeSelect(val: string) {
      console.log(val,'选择日期格式',controlItemRef.value);
      signDatevisible.value = false;
      signDateElement.value.format = val;
      currentControl.value.format = val;
      signDateElement.value.today = getTodayDateByFormat(val);
    }
    //获取签章id
    async function getSealIds() {
      let { result } = await Api.getSignSealIds({});
      if (result && result.length) {
        sealList.value = result;
        loadSealBase64();
      } else {
        // Notify({ type: 'warning', message: '暂无可用印章' })
      }
    }
    //获取签章图片
    function loadSealBase64() {
      sealList.value.forEach(async (item: any) => {
        item.base64 = '';
        const { result } = await Api.getBase64ById({ id: item.annexId });
        if (result) {
          item.sealImage = result.image;
        }
      });
    }
    //获取个人签名图片
    function loadSignatureBase64() {
      signatureList.value.forEach(async (item: any) => {
        item.base64 = '';
        const { result } = await Api.getBase64ById({ id: item.annexId });
        if (result) {
          item.sealImage = result.image;
        }
      });
    }
    //校验签章id
    async function checkSignSeal() {
      let { result, code } = await Api.verifySignSeal({ sealId: unref(signControlInfo).sealId });
      if (code == 200) {
        sealImgId.value = result;
        handleSetSeals();
        // sealVisible.value = true;
      } else {
        Notify({ type: 'warning', message: result.message });
      }
    }
    //获取签名列表
    async function getSignaturs() {
      let { result } = await Api.getSignatureList({
            pageNo: 1,
            pageSize: 1000
          });
      // console.log(result,"result-------");
      if (result) {
        signatureList.value = result.records;
        loadSignatureBase64();
        // signatureList.value.map(async (item) => {
        //   item.base64 = '';
        //   const { result } = await Api.getBase64ById({ id: item.annexId });
        //   // console.log(result, '签名结果')
        //   if (result) {
        //     item.base64 = result.image;
        //   }
        // });
      }
    }
    //获取图片流宽高
    function getImageIdSize(imgId: string,controlType: string) {
      // console.log(imgId,'imgId-----------');
      let sealSize = {
          width: 120,
          height: 120,
        };
      let newSealSize = {
          width: 120,
          height: 120,
        };
      let newSignatureSize = {
          width: 112,
          height: 52,
        };
      // if(controlType === 'seal'){
      //   sealSize = {
      //     width: 120,
      //     height: 120,
      //   };
      //   newSealSize = {
      //     width: 120,
      //     height: 120,
      //   };
      // }
      if(controlType === 'signature'){
         sealSize = {
          width: 112,
          height: 52,
        };
      }
      // console.log(sealSize,'sealSize-----------');
      // 图片地址
      if (imgId) {
        let img_url: any =
          baseUrl + '/file/downloadFileStream/' + imgId + '?t=' + new Date().getTime();
        // 创建对象
        var img = new Image();
        // 改变图片的src
        img.src = img_url;
        return new Promise((resolve, reject) => {
          // 加载完成执行
          img.onload = function () {
            // 加载完成执行
            if (sealSize.width / sealSize.height == img.width / img.height) {
              newSealSize = sealSize;
              newSignatureSize = sealSize;
            } else if (sealSize.width / sealSize.height < img.width / img.height) {
              newSealSize = {
                width: sealSize.width,
                height: img.height / (img.width / sealSize.width),
              };
              newSignatureSize = {
                width: sealSize.width,
                height: img.height / (img.width / sealSize.width),
              };
            } else if (sealSize.width / sealSize.height > img.width / img.height) {
              newSealSize = {
                width: img.width / (img.height / sealSize.height),
                height: sealSize.height,
              };
              newSignatureSize = {
                width: img.width / (img.height / sealSize.height),
                height: sealSize.height,
              };
            }
             console.log(newSealSize,newSignatureSize,'newSealSize-----------');
            resolve(newSealSize);
            resolve(newSignatureSize);
          };
        });
      }
      
    }

    const sealRows = computed(() => {
      if (!sealList.value) return [];
      const rows = [];
      for (let i = 0; i < sealList.value.length; i += 2) {
        rows.push(sealList.value.slice(i, i + 2));
      }
      return rows;
    });

    const personSignatureRows = computed(() => {
      if (!signatureList.value) return [];
      const rows = [];
      for (let i = 0; i < signatureList.value.length; i += 2) {
        rows.push(signatureList.value.slice(i, i + 2));
      }
      return rows;
    });

    // 选中印章
    function selectSeal(sealId) {
      signSealId.value = sealId;
    }

    //使用签章
    function handleSetSeals() {
      if (signControlInfo.value.sealId) {
        entSealId.value = signControlInfo.value.sealId;
        currentControl.value.sealId = sealImgId.value;
        sealAnnexId.value = sealImgId.value;
        if (sealAnnexId.value) {
          getImageIdSize(sealAnnexId.value,'seal').then((res) => {
            updateAllSealImg(
              nowDocument.value.activeControl,
              unref(signControlInfo).sealId,
              sealImgId.value,
              res
            );
          });
          isSignText.value = false;
          sealVisible.value = false;
          signPosition('company');
        }
      } else {
        if (!signSealId.value) {
          Notify({ type: 'warning', message: '请选择签章' });
        } else {
          let sealInfo = sealList.value.filter((v) => v.sealId == signSealId.value)[0];
          if (sealInfo) {
            entSealId.value = signSealId.value;
            currentControl.value.sealId = signSealId.value;
            sealAnnexId.value = sealInfo.annexId;
            if (sealAnnexId.value) {
              getImageIdSize(sealAnnexId.value,'seal').then((res) => {
                updateAllSealImg(nowDocument.value.activeControl, signSealId.value, sealInfo.annexId,res);
              });
              isSignText.value = false;
              sealVisible.value = false;
              signPosition('company');
            }
          }
        }
      }
    }

    //签名图片大小计算
      function reCaculatImgSize(orisignSize){
        newSignatureSize.value = {
          width:112,
          height:52
        }
        if((newSignatureSize.value.width / newSignatureSize.value.height) == (orisignSize.width / orisignSize.height)){
          return orisignSize
        }
        if((newSignatureSize.value.width / newSignatureSize.value.height) < (orisignSize.width / orisignSize.height)){
          return {
            width: newSignatureSize.value.width,
            height: parseInt(orisignSize.height / (orisignSize.width / newSignatureSize.value.width))
          }
        }
        if((newSignatureSize.value.width / newSignatureSize.value.height) > (orisignSize.width / orisignSize.height)){
          return {
            width: parseInt(orisignSize.width / (orisignSize.height / newSignatureSize.value.height)),
            height: newSignatureSize.value.height
          }
        }

      }
     
    //选择签名
    function handleSetSignature() {
      let row = signatureList.value.filter((v: any) => v.sealId == signSealId.value)[0];
      // console.log(signatureList.value, '签名列表',signatureId.value);
      // console.log(row, '签名列表---');
      if (row) {
        privateSeal.value = row.sealImage.split('base64,')[1];
        // console.log(row.sealImage,'row.sealImage---');
        // console.log(privateSeal.value,'privateSeal.value---');
        currentControl.value.signatureId = row.annexId;
        currentControl.value.signature = '';
        singatureBae64.value = row.sealImage;
        getImageIdSize(row.annexId,'signature').then((res) => {
            updateAllSignatureImg(nowDocument.value.activeControl, signSealId.value, row.annexId,res);
         });
        
        personSignatureVisible.value = false;
        hasSignature.value = false; 
        signPosition('person');
      }

    }
    //关闭签名弹框
    function handleSignatureCancel() {
      signatureVisible.value = false;
    }

    //打开签名弹框
    function handleSignatureVisible() {
      personSignatureVisible.value = false;
      showSignControlTip.value = false;
      signatureVisible.value = true;
        setTimeout(() => {
          showPad.value = true;
        });
    }

    //更新所有签章图片
    function updateAllSealImg(controls: any, sealId: string, annexId: string, sealSize?: any) {
      controls.map((v: any) => {
        if (v.controlType == 'seal' || v.controlType == 'chop-stamp') {
          v.sealId = sealId;
          v.annexId = annexId;
          if (sealSize && sealSize.width) {
            newSealSize.value = {
              width: sealSize.width,
              height: sealSize.height,
            };
            v.size.width = sealSize.width;
            v.size.height = sealSize.height;
          }
        }
      });
      //更新其他控件
      controlList.value.map((v: any) => {
        if (v.controlType == 'seal' || v.controlType == 'chop-stamp') {
          v.sealId = sealId;
          v.annexId = annexId;
          if (sealSize && sealSize.width) {
            newSealSize.value = {
              width: sealSize.width,
              height: sealSize.height,
            };
            v.size = {
              width: sealSize.width,
              height: sealSize.height,
            };
            // v.size.width = sealSize.width;
            // v.size.height = sealSize.height;
          }
        }
      });
      //更新其他文档（存在重复更新，需过滤掉当前文档）
      documentList.value.map((m) => {
        m.activeControl.forEach((v: any) => {
          if (v.controlType == 'seal' || v.controlType == 'chop-stamp') {
            v.sealId = sealId;
            v.annexId = annexId;
            if (sealSize && sealSize.width) {
              newSealSize.value = {
                width: sealSize.width,
                height: sealSize.height,
              };
              v.size.width = sealSize.width;
              v.size.height = sealSize.height;
            }
          }
        });
      });
    }

    //更新所有签名图片
    function updateAllSignatureImg(controls:any, sealId: string, annexId: string, sealSize?: any) {
      // console.log(sealSize,'sealSize-----');
      controls.map((v: any) => {
        if (v.controlType == 'signature') {
          v.sealId = sealId;
          v.signatureId = annexId;
          if (sealSize && sealSize.width) {
            newSignatureSize.value = {
              width: sealSize.width,
              height: sealSize.height,
            };
            v.size.width = sealSize.width;
            v.size.height = sealSize.height;
          }
        }
      });
      // console.log()
      //更新其他文档
      controlList.value.map((v: any) => {
        if (v.controlType == 'signature') {
          v.sealId = sealId;
          v.signatureId = annexId;
          if (sealSize && sealSize.width) {
            newSignatureSize.value = {
              width: sealSize.width,
              height: sealSize.height,
            };
            v.size = {
              width: sealSize.width,
              height: sealSize.height,
            };
          }
        }
      });

      documentList.value.map((m) => {
        m.activeControl.forEach((v: any) => {
          if (v.controlType == 'signature') {
            v.sealId = sealId;
            v.signatureId = annexId;
            if (sealSize && sealSize.width) {
            newSignatureSize.value = {
              width: sealSize.width,
              height: sealSize.height,
            };
            v.size = {
              width: sealSize.width,
              height: sealSize.height,
            };
          }
          }
        });
      });
    }

    //撤销
    function undo() {
      signaturePad.value.undoSignature();
    }
    //清除
    function clear() {
      signaturePad.value.clearSignature();
      hasSignature.value = false; // 清除签名时重置状态
    }
    //关闭面板
    function closeSignaturePad() {
      signatureVisible.value = false;
      hasSignature.value = false; // 关闭时重置状态
      clear();
    }
    //更改大小
    function setPadSize(val) {
      options.value = {
        penColor: '#000',
        dotSize: 1,
        minWidth: 1 * val,
        maxWidth: 4 * val,
        throttle: 16,
        minDistance: 5,
        backgroundColor: 'rgba(0,0,0,0)',
        velocityFilterWeight: 0.5,
        onBegin: () => { },
        onEnd: () => { },
      };
      if (val == 1) {
        isActive1.value = true;
        isActive2.value = false;
        isActive3.value = false;
      } else if (val == 2) {
        isActive1.value = false;
        isActive2.value = true;
        isActive3.value = false;
      } else if (val == 3) {
        isActive1.value = false;
        isActive2.value = false;
        isActive3.value = true;
      }
      signaturePad.value.openSignaturePad();
    }

    function calcTotalLength(signaturePadInstance: any) {
      let totalLength = 0;
      // 获取签名数据
      const data = signaturePadInstance.toData();
      if (data && Array.isArray(data)) {
        data.forEach(stroke => {
          if (stroke && stroke.points && Array.isArray(stroke.points)) {
            for (let i = 1; i < stroke.points.length; i++) {
              const prevPoint = stroke.points[i - 1];
              const currPoint = stroke.points[i];
              if (
                prevPoint && currPoint &&
                typeof prevPoint.x === 'number' && typeof prevPoint.y === 'number' &&
                typeof currPoint.x === 'number' && typeof currPoint.y === 'number'
              ) {
                const distance = Math.sqrt(
                  Math.pow(currPoint.x - prevPoint.x, 2) +
                  Math.pow(currPoint.y - prevPoint.y, 2)
                );
                totalLength += distance;
              }
            }
          }
        });
      }
      return totalLength;
    }
    //保存
    function saveSignature() {
      // 首先检查 hasSignature 状态
      if (!hasSignature.value) {
        return;
      }
      const minLength = 600; // 最小长度阈值
      const result = signaturePad.value.saveSignature();
      const { isEmpty, data } = result;
      
      const totalLength = calcTotalLength(signaturePad.value);

      // console.log('totalLength:', totalLength);

      if (isEmpty) {
        Notify({ type: 'warning', message: '未书写签名，请签名' });
        // alert('未书写签名，请签名');
        return;
      }

      if (totalLength < minLength) {
        Notify({ type: 'warning', message: '签名内容太短，请重新签名' });
        // alert('签名内容太短，请重新签名');
        return;
      }


      // console.log(data,'data---------');
      currentControl.value.signature = data;
      currentControl.value.signatureId = '';

      //旋转base64
      // 创建一个虚拟的 canvas 元素用于旋转操作
      const canvas = document.createElement('canvas');
      const ctx: any = canvas.getContext('2d');

      // 创建一个 Image 元素
      const image = new Image();
      image.src = data;

      // 确保图片加载完成后执行旋转操作
      image.onload = () => {
        canvas.width = image.height; // 交换宽高
        canvas.height = image.width;
        ctx.rotate(-90 * (Math.PI / 180));
        // 将图像绘制到 Canvas
        ctx.drawImage(image, -image.width, 0);
        const rotatedBase64 = canvas.toDataURL();
        singatureBae64.value = rotatedBase64;
        privateSeal.value = rotatedBase64.split('base64,')[1];
        let orisignSize = {
          width:canvas.width,
          height:canvas.height
        }
        let signatureSize = reCaculatImgSize(orisignSize);
        console.log(signatureSize,'signatureSize----');
        newSignatureSize.value = {
              width: signatureSize.width,
              height: signatureSize.height,
            };
        nowDocument.value.activeControl.map((v: any) => {
          if (v.controlType == 'signature') {
            v.signature = rotatedBase64;
            v.signatureId = '';
            v.size.width = newSignatureSize.value.width;
            v.size.height = newSignatureSize.value.height;
          }
        });
        controlList.value.map((v: any) => {
          if (v.controlType == 'signature') {
            v.signature = rotatedBase64;
            v.signatureId = '';
            v.size.width = newSignatureSize.value.width;
            v.size.height = newSignatureSize.value.height;
          }
        });

        documentList.value.map((m) => {
          m.activeControl.forEach((v: any) => {
            if (v.controlType == 'signature') {
              v.signature = rotatedBase64;
              v.signatureId = '';
              v.size.width = newSignatureSize.value.width;
              v.size.height = newSignatureSize.value.height;
            }
          });
        });

        // if (needLocation.value) {
        //     document.getElementsByClassName('doc-content')[0].scrollTo(0, (currentControl.value.pageHeight * aspecRatio + 16) * currentControl.value.page)
        // }
      };
      signatureVisible.value = false;
      showPad.value = false;
      isSignText.value = false;
      hasSignature.value = false; 
      signPosition('person');

    }
    // 取消签署
    function handleUnSign() {
      signCertificateVisible.value = false;
    }
    //人脸识别开通个人账号
    async function faceOpenPersonalAccount() {
      let { result } = await Api.openPersonalTenant({});
      if (result) {
        needOpenPersonlAccountVisible.value = false;
        let { result, code } = await Api.getConfirmType({});
        if (code == 200) {
          confirmTypeInfo.value = result;
          setTimeout(() => {
            handleFace();
          }, 500);
        }
      }
    }

    //使用平台防篡改证书开通个人账号
    async function openPersonalAccount() {
      let { result } = await Api.openPersonalTenant({});
      if (result) {
        signCertificateVisible.value = false;
        handleSubmitSign();
      }
    }
    //校验文档是否指定了签章
    function checkSealCount() {
      let matchSignatures = controlList.value.filter((v: any) => v.controlType == 'seal');
      if (matchSignatures && matchSignatures.length) {
        currentControl.value = matchSignatures[0];
        needLocation.value = true;
      } else if (!sealCount.value && !stampSealCount.value) {
        handleAddSeal();
        needLocation.value = false;
      }
      return matchSignatures.length ? true : false;
    }
    //校验文档是否指定了签名控件
    function checkSignatureCount() {
      let matchSignatures = controlList.value.filter((v: any) => v.controlType == 'signature');
      if (matchSignatures && matchSignatures.length) {
        currentControl.value = matchSignatures[0];
        needLocation.value = true;
      } else if (!signatureCount.value) {
        handleAddSignature();
        needLocation.value = false;
      }
      return matchSignatures.length ? true : false;
    }

    //继续任务处理
    function handleGoNextTask() {
      if (taskInfo.value.taskType == 'sign') {
        router.push({
          path: '/signContract',
          query: {
            signRuId: taskInfo.value.ruId,
            taskId: taskInfo.value.taskId,
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
      setTimeout(() => {
        router.push('/index');
      }, 1000);
    }

    // 0 有文档未设置签名控件 1已设置
    const submitCheck = ref<any>({
      status: 0,
      errorDocs: [],
    });
    const ignoreControl = ref(false);

    //签署操作（添加控件或指定签名签章）
    async function handleSign() {
      if (!isSignText.value) {
        //   handleSubmitSign();
        checkDocumentSignControl();
        if (!ignoreControl.value && submitCheck.value.status === 0) {
          let errorText = '';
          submitCheck.value.errorDocs.forEach((item: any) => {
            errorText += `《${item.documentName}》`;
          });
          Dialog.confirm({
            title: '签署文件未全部指定签署位置',
            message: `文件${errorText}未指定签署位置，请检查确认！`,
            confirmButtonText: '继续签署',
            cancelButtonText: '取消',
          }).then(() => {
            // handleSubmitSign();
            ignoreControl.value = true;
            handleSign();
          });
          return;
        }
        // 判断是否是无实名认证签署
        if(personalSignAuth.value == 'not_required'){
          // 弹窗提示
          Dialog.confirm({
            title: '非实名认证签署风险告知书',
            message: () => h('div', {
              class: 'van-popup__content',
              style: {
                fontSize: '14px',
                lineHeight: '1.6',
                marginTop: '20px',
                textAlign: 'left'
              }
            }, [
              h('p', {
                class: 'van-notice-bar__text',
                style: { color: '#333' }
              }, '本次签署无需进行实名认证。如您同意，系统将使用平台防篡改证书（非CA权威数字证书）为您完成电子签名。'),
              h('p', {
              class: 'van-notice-bar__text',
              style: { color: '#333' }
              }, '如对实名认证要求有疑议，请联系签署发起方。'),
              h('p', {
                class: 'van-notice-bar__text',
                style: { color: 'red' }
              }, [h('strong', '重要声明：'), '平台防篡改证书仅能确保文件在签名后不被篡改，不具备《电子签名法》规定的法律效力。']),
              h('p', {
                class: 'van-notice-bar__text',
                style: { color: '#333' }
              }, ['点击“',h('strong', '同意，继续签署'), '”即代表您已知悉非实名认证签署的相关风险，并同意以该方式完成本次签署。']),
            ]),
            confirmButtonText: '同意，继续签署',
            cancelButtonText: '暂不签署',
          }).then(() => {
            submitSignData();
          });
        } else {
          submitSignData();
        }
      } else {
        //组织签章
        if (signControlInfo.value?.type == '2') {
          //检查文档是否有签章
          checkSealCount();
          //如果没有签章id获取签章列表，有的话直接盖章到签章位置
          if (!signControlInfo.value.sealId) {
            let { result, code } = await Api.getSignSealIds({});
            if (code == 200) {
              sealList.value = result;
              //签章列表只有一个时直接盖章
              if (result.length > 1) {
                sealVisible.value = true;
                loadSealBase64();
                // isSignText.value = false;
              } else if (result.length == 1) {
                entSealId.value = result[0].sealId;
                currentControl.value.sealId = result[0].sealId;
                sealAnnexId.value = result[0].annexId;
                isSignText.value = false;
                updateAllSealImg(
                  nowDocument.value.activeControl,
                  result[0].sealId,
                  result[0].annexId
                );
                signPosition('company');
              } else if (result.length == 0) {
                sealVisible.value = true;
              }
            }
          } else {
            let { result, code } = await Api.verifySignSeal({
              sealId: unref(signControlInfo).sealId,
            });
            if (code == 200) {
              //根据签章id校验签章是否可用
              sealImgId.value = result;
              handleSetSeals();
              //指定了签章盖章完成后定位到签章位置
              if (needLocation.value) {
                signPosition('company');
                // document
                //   .getElementsByClassName('doc-content')[0]
                //   .scrollTo(
                //     0,
                //     (currentControl.value.pageHeight * aspecRatio + 16) *
                //     currentControl.value.page
                //   );
              }
            } else {
              Notify({ type: 'warning', message: result.message });
            }
          }
        } else {
          checkSignatureCount();
          getSignaturs();
          personSignatureVisible.value = true;
          // showSignControlTip.value = false;
          // signatureVisible.value = true;
          // setTimeout(() => {
          //   showPad.value = true;
          // });
        }
      }
    }
    function checkDocumentSignControl() {
      submitCheck.value.errorDocs = [];
      documentList.value.forEach((doc: any) => {
        if (!doc.activeControl || doc.activeControl.length === 0) {
          submitCheck.value.errorDocs.push(doc);
        }
      });
      submitCheck.value.status = submitCheck.value.errorDocs.length === 0 ? 1 : 0;
    }

    //提交签署
    async function handleSubmitSign() {
      // if (!checkHasSignPos()) {
      //     Notify({ type: 'warning', message: '暂未指定签约位置，请先指定位置！' });
      //     return
      // }
      // if (signControlInfo.value?.type == '2') {
      //     if (!unref(entSealId)) {
      //         Notify({ type: 'warning', message: '签章控件上尚未指定签章，请指定后再签署！' });
      //         return
      //     }
      // } else {
      //     if (!unref(privateSeal)) {
      //         Notify({ type: 'warning', message: '签名控件上尚未指定签名，请指定后再签署！' });
      //         return
      //     }
      // }
      //验证证书
      let { result, code }: any = await Api.verifyCertificate({ signRuId });
      if (code == 200) {
        certificateInfo.value = result;
        // console.log('获取开通个人租户信息：', result);
        if (result.tenantId) {
          let tenantResult = await Api.getSignAuthStatus({ tenantId: result.tenantId });
          if (tenantResult.code == 200) {
            //   tenantInfo.value = {
            //     ...tenantInfo.value,
            //     authStatus: tenantResult.result,
            //   };
          }
        }
        // 测试证书 2 和 ca 事件证书 3
        if (certificateInfo.value.certType == '2' || certificateInfo.value.certType == '3') {
          let info = '';
          //holderType=1 个人 2 企业
          if (certificateInfo.value.holderType == '1') {
            if (certificateInfo.value.certType == '2') {
              info =
                '您当前用于文件签署的证书是平台下发的测试证书，该证书非CA机构颁发，仅用于测试，签署后的文件不具备法律效力，请知悉！\n系统即将使用您的个人信息申请数字证书并用于本次文件签署，是否同意？';
            }
            if (certificateInfo.value.certType == '3') {
              info = '系统即将使用您的个人信息申请数字证书并用于本次文件签署，是否同意？';
            }
          }
          if (certificateInfo.value.holderType == '2') {
            if (certificateInfo.value.certType == '2') {
              info =
                '您当前用于文件签署的证书是平台下发的测试证书，该证书非CA机构颁发，仅用于测试，签署后的文件不具备法律效力，请知悉！\n系统即将使用当前企业信息申请数字证书并用于本次文件签署，是否同意？';
            }
            if (certificateInfo.value.certType == '3') {
              info = '系统即将使用当前企业信息申请数字证书并用于本次文件签署，是否同意？';
            }
          }
          Dialog.confirm({
            title: '温馨提示',
            message: info,
            confirmButtonText: '同意',
            cancelButtonText: '不同意',
            className: 'sign-cerType',
          }).then(() => {
            if (certificateInfo.value.certType == '2' || certificateInfo.value.certType == '3') {
              goSign();
            } else {
              if (result.returnCode == 4) {
                //证书有效
                goSign();
              } else {
                signCertificateVisible.value = true;
              }
            }
          });
        } else {
          if (certificateInfo.value.certType == '1') {
            signCertificateVisible.value = true;
            return;
          }
          if (result.returnCode == 4) {
            //证书有效
            goSign();
          } else {
            signCertificateVisible.value = true;
          }
        }
      } else {
        Notify({ type: 'warning', message: result.message });
      }
    }

    function signPosition(signerType: string) {
        // 定义控件优先级顺序
        const controlPriority = signerType === 'person' 
          ? ['signature'] 
          : ['seal', 'chop-stamp'];

        // 在当前文档中查找控件
        const currentDocControls = nowDocument.value.activeControl || [];
        for (const controlType of controlPriority) {
          const targetControl = currentDocControls.find((item: any) => item.controlType === controlType);
          if (targetControl) {
            scrollToControl(targetControl);
            return;
          }
        }

        // 如果当前文档没有找到，在所有文档中查找
        const allControls = documentList.value.flatMap((item: any) => item.activeControl || []);
        for (const controlType of controlPriority) {
          const targetControl = allControls.find((item: any) => item.controlType === controlType);
          if (targetControl) {
            handleDocChange(targetControl.signRuDocId).then(() => {
              setTimeout(() => {
                scrollToControl(targetControl);
              }, 100);
            });
            return;
          }
        }
      }

      // 滚动到指定控件位置的辅助函数
      function scrollToControl(control: any) {
        const element = document.getElementsByClassName('pos-' + control.position.top)[0];
        if (element) {
          console.log(control,'controlMousedown-----')
          // 滚动事件
          element.scrollIntoView({ behavior: 'smooth', block: 'center' });
          // 点击事件
           controlMousedown(control);
        }
      }

      function locationSignControl(){
        const allControls = documentList.value.flatMap((item: any) => item.activeControl || []);
        const signControls = allControls.filter((control: any) => 
          ['seal', 'signature', 'chop-stamp', 'sign-date'].includes(control.controlType)
        );
        if (signControls.length === 0) return;
  
        // 获取当前要定位的控件
        const targetControl = signControls[currentLocationIndex.value % signControls.length];
        
        // 定位到目标控件
        if (targetControl) {
          // 如果目标控件在其他文档中，先切换文档
          if (targetControl.signRuDocId !== docId.value) {
            handleDocChange(targetControl.signRuDocId);
            nextTick(() => {
              scrollToControl(targetControl);
            });
          } else {
            scrollToControl(targetControl);
          }
        }
        // 更新索引，准备下次定位到下一个控件
        currentLocationIndex.value = (currentLocationIndex.value + 1) % signControls.length;

      }

    async function handlePersonAuth() {
      //已经开通个人空间
      if (certificateInfo.value.departId) {
        handleAuth();
      } else {
        //无个人空间去开通
        let { result } = await Api.openPersonalTenant({});
        if (result) {
          certificateInfo.value.departId = result;
          handleAuth();
        }
      }
    }
    async function checkPersonalTenant() {
      //已经开通个人空间
      const tenantInfo = userStore.getTenantInfo;
      // 在企业身份下，判断个人租户是否开通开通
      if (tenantInfo.tenantType === '1' && !certificateInfo.value.signerTenantId) {
        await Api.openPersonalTenant({});
      }
    }

    //个人实名认证
    async function handleAuth() {
      userHandleAuth();
    }
    function userHandleAuth() {
      // const asyncUrl = `${location.href}`;
      // console.log('userAuth', userAuth.value);
      if (userAuth.value.tenantType === TenantTypeEnum.PERSONAL) {
        Dialog.confirm({
          title: '开启安全可靠的电子签章',
          // message: '为保证签署真实有效，请先完成个人实名认证校验',
          message: h('div', { style: { textAlign: 'left' } }, [
            // 第一部分：标题
            // h('div', {
            //   style: {
            //     fontSize: '16px',
            //     fontWeight: '600',
            //     marginBottom: '12px',
            //     color: '#333'
            //   }
            // }, '开启安全可靠的电子签章'),

            // 第二部分：操作指引
            h(
              'div',
              {
                style: {
                  marginBottom: '16px',
                },
              },
              [
                h(
                  'div',
                  {
                    style: {
                      marginBottom: '8px',
                      fontWeight: '500',
                    },
                  },
                  '为保证签署真实有效，请完成以下步骤：'
                ),
                h(
                  'div',
                  {
                    style: {
                      paddingLeft: '0px',
                    },
                  },
                  [
                    h('div', '1. 点击【立即认证】'),
                    h('div', '2. 在「云盾系统」完成个人实名认证'),
                  ]
                ),
              ]
            ),

            // 第三部分：服务说明（优化样式）
            h(
              'div',
              {
                style: {
                  background: '#f0f8ff',
                  border: '1px solid #d0e9ff',
                  borderRadius: '4px',
                  padding: '12px',
                  fontSize: '13px',
                  color: '#31708f',
                },
              },
              [
                h(
                  'div',
                  {
                    style: {
                      display: 'flex',
                      alignItems: 'flex-start',
                      marginBottom: '6px',
                    },
                  },
                  [
                    h('span', {
                      innerHTML: '&#128274;', // 锁图标
                      style: {
                        marginRight: '8px',
                        fontSize: '14px',
                      },
                    }),
                    h(
                      'span',
                      {
                        style: {
                          fontWeight: '600',
                          fontSize: '14px',
                        },
                      },
                      '安全保障'
                    ),
                  ]
                ),
                h(
                  'div',
                  {
                    style: {
                      lineHeight: '1.6',
                      paddingLeft: '24px',
                    },
                  },
                  '云盾系统为电子签章提供核心保障服务，包括实名认证、权威CA证书签发及签署意愿验证，确保签署过程安全合规。'
                ),
              ]
            ),
          ]),
          confirmButtonText: '立即认证',
        }).then(async () => {
          // on close
          await checkPersonalTenant();
          handleUserAuth(2, location.href);
          userStore.setTenantInfo(null);
          console.log('个人立即认证');
        });
      } else if (userAuth.value.tenantType === TenantTypeEnum.ENTERPRISE) {
        Dialog.confirm({
          title: '开启安全可靠的电子签章',
          // message: `您将作为认证申请人，对 「${tenantInfo.name}」 进行实名认证`,
          message: h('div', { style: { textAlign: 'left' } }, [
            // 第一部分：标题
            // h('div', {
            //   style: {
            //     fontSize: '16px',
            //     fontWeight: '600',
            //     marginBottom: '12px',
            //     color: '#333'
            //   }
            // }, '开启安全可靠的电子签章'),

            // 第二部分：操作指引
            h(
              'div',
              {
                style: {
                  marginBottom: '16px',
                },
              },
              [
                h(
                  'div',
                  {
                    style: {
                      marginBottom: '8px',
                      fontWeight: '500',
                    },
                  },
                  '为保证签署真实有效，请完成以下步骤：'
                ),
                h(
                  'div',
                  {
                    style: {
                      paddingLeft: '0px',
                    },
                  },
                  [
                    h('div', '1. 点击【立即认证】'),
                    h(
                      'div',
                      `2. 您将作为认证申请人，在「云盾系统」完成对「${tenantInfo.name}」的企业实名认证`
                    ),
                  ]
                ),
              ]
            ),

            // 第三部分：服务说明（优化样式）
            h(
              'div',
              {
                style: {
                  background: '#f0f8ff',
                  border: '1px solid #d0e9ff',
                  borderRadius: '4px',
                  padding: '12px',
                  fontSize: '13px',
                  color: '#31708f',
                },
              },
              [
                h(
                  'div',
                  {
                    style: {
                      display: 'flex',
                      alignItems: 'flex-start',
                      marginBottom: '6px',
                    },
                  },
                  [
                    h('span', {
                      innerHTML: '&#128274;', // 锁图标
                      style: {
                        marginRight: '8px',
                        fontSize: '14px',
                      },
                    }),
                    h(
                      'span',
                      {
                        style: {
                          fontWeight: '600',
                          fontSize: '14px',
                        },
                      },
                      '安全保障'
                    ),
                  ]
                ),
                h(
                  'div',
                  {
                    style: {
                      lineHeight: '1.6',
                      paddingLeft: '24px',
                    },
                  },
                  '云盾系统为电子签章提供核心保障服务，包括实名认证、权威CA证书签发及签署意愿验证，确保签署过程安全合规。'
                ),
              ]
            ),
          ]),
          confirmButtonText: '立即认证',
        }).then(() => {
          // on close
          handleUserAuth(1, location.href);
          userStore.setTenantInfo(null);
          console.log('企业立即认证');
        });
      } else if (userAuth.value.tenantType === 3) {
        Dialog.alert({
          title: '温馨提示',
          message: `您当前的企业尚未完成实名认证，请联系企业管理员完成实名认证后再签署`,
          confirmButtonText: '知道了',
        }).then(() => {
          console.log('无权操作认证');
        });
      }
    }
    const userAuth = ref<{
      status: boolean;
      tenantType: number;
    }>({
      status: false,
      tenantType: 0,
    });
    // 判断实名状态
    async function checkCaAndAuthStatus() {
      // const tenantInfo = userStore.getTenantInfo;
      const { result: userCheckResult }: any = await Api.getVerifyCertificate({ signRuId });
      // console.log('userCheckResult', userCheckResult);
      certificateInfo.value = userCheckResult;
      if (userCheckResult.holderType === TenantTypeEnum.ENTERPRISE) {
        // 签署主体是否实名
        if (tenantInfo.authStatus === 2) {
          // 已实名判断签署人是否实名
          if (userCheckResult.signerAuthStatus === 2) {
            userAuth.value.status = true;
            console.log('企业经办人都已实名');
          } else {
            // 去个人实名
            userAuth.value.tenantType = TenantTypeEnum.PERSONAL;
            console.log('企业已实名，企业经办人未实名');
          }
        } else {
          // 未实名  判断是否当前企业管理员 0:不是管理员；1:是管理员
          if (userCheckResult.signerIsCompanyManager === 0) {
            // 提示信息：您当前的企业尚未完成实名认证，请联系企业管理员完成实名认证后再签署
            // 按钮：知道了
            userAuth.value.tenantType = 3;
            console.log('不是企业管理员，提示知道了');
          } else if (userCheckResult.signerIsCompanyManager === 1) {
            if (userCheckResult.signerAuthStatus !== 2) {
              // 提示信息：企业认证需要当前认证的申请人先完成个人实名认证校验
              // 按钮：取消，立即认证
              // 立即认证-动作：使用当前签署人的个人身份发起个人实名认证
              userAuth.value.tenantType = TenantTypeEnum.PERSONAL;
              console.log('企业未实名、个人未实名，去个人实名');
            } else {
              // 管理员已实名，可进行企业认证
              // 提示信息：您将作为认证申请人，对 「${企业名称}」 进行实名认证
              // 按钮：取消，立即认证
              // 立即认证-动作：使用当前企业身份发起企业实名认证
              userAuth.value.tenantType = TenantTypeEnum.ENTERPRISE;
              console.log('企业未实名、去企业实名');
            }
          }
        }
      } else if (userCheckResult.holderType === TenantTypeEnum.PERSONAL) {
        if (userCheckResult.signerAuthStatus === 2) {
          userAuth.value.status = true;
          console.log('个人已实名');
        } else {
          // 去个人实名
          userAuth.value.tenantType = TenantTypeEnum.PERSONAL;
          console.log('个人未实名，去实名');
        }
      }
    }

    //更新证书
    async function updateCertificate() {
      let { result, code } = await Api.updateCertificate({ ...certificateInfo.value });
      if (code == 200) {
        signCertificateVisible.value = false;
        authCertificateVisible.value = true;
        authCerInfo.value = result;
      } else {
        signCertificateVisible.value = true;
        authCertificateVisible.value = false;
        personalAuth.value = true;
      }
    }
    //继续签署
    function goSign() {
      getConfirmTypeForAction({ operateType: 'submit_sign' }).then(async (res) => {
        confirmTypeInfo.value = res;
        confirmAction.value = 'sign';
        if (
          (confirmTypeInfo.value.confirmType == 'password' ||
            confirmTypeInfo.value.confirmType == 'phone_email' ||
            confirmTypeInfo.value.confirmType == 'double') &&
          !willResult.value
        ) {
          signCertificateVisible.value = false;
          confirmRef.value?.showDialog({
            confirmType: confirmTypeInfo.value.confirmType,
            orderNo: orderNo.value,
          });
          return;
        }
        submitSignData();
      });
    }

    //提交签署数据
    async function submitSignData() {
      signLoading.value = true;
      let paramsControl: any = [];
      let controls = documentList.value.flatMap((item: any) => item.activeControl);
      let newControls = cloneDeep(controls);

      newControls.map((item: any) => {
        let matchDoc: any = docs.value.filter((v: any) => v.id == item.signRuDocId)[0];
        //这里的maxImageWidthItem.imageWidth 为图片宽度原始最大值 maxWidth 是缩放后的图片宽度最大值
        const maxImageWidthItem = matchDoc.images.reduce((max, item) => {
          return parseFloat(item.imageWidth) > parseFloat(max.imageWidth) ? item : max;
        }, matchDoc.images[0]);
        let { targets, maxWidth } = pageScaling(matchDoc.images);
        let target = targets[item.position.page];
        let offsetWidth = (maxWidth - target.width) / 2;
        const baseZoomRatio = maxImageWidthItem.imageWidth / baseImgWidth; //图片缩放比
        item.pageWidth = item.pageWidth ? item.pageWidth : target.width * baseZoomRatio;
        item.pageHeight = item.pageHeight ? item.pageHeight : target.height * baseZoomRatio;

        item.signerType = signerList.value.find((m: any) => m.id == item.signerId)?.signerType
          ? 2
          : 1;

        paramsControl.push({
          propertyVoList: item.propertyVoList,
          fontFamily: item.style.fontFamily,
          fontSize: item.style.fontSize,
          name: item.name,
          offsetX: (item.position.left - offsetWidth).toFixed(2),
          offsetY: (
            item.position.top -
            target.transformHeight -
            (item.position.page + 1) * 16
          ).toFixed(2), //计算成为每页的位置
          showPopover: false,
          pageWidth: Math.round(item.pageWidth * 100) / 100,
          pageHeight: Math.round(item.pageHeight * 100) / 100,
          page: item.position.page,
          placeholder: item.placeholder,
          signReDocId: item.signReDocId,
          relatedDocId: 0,
          signerId: item.signerId,
          signerType: item.signerType,
          relatedDocType: 0,
          required: item.required ? 1 : 2,
          interfaceParamName: item.interfaceParamName,
          textAlign: item.style.textAlign,
          signRuDocId: item.signRuDocId,
          controlType: item.controlType,
          value: item.value,
          width: Math.round(item.size.width * 100) / 100,
          height: Math.round(item.size.height * 100) / 100,
          written: 1,
          format: item.format || (item.controlType == 'sign-date' ? 'yyyy年MM月dd日' : ''),
          id: item.id,
          uid: item.uid,
          tenantId: item.user.tenantId,
        });
      });
      // authCertificateVisible.value = false;
      // console.log(paramsControl, '提交合并前的控件参数----');
      // console.log(mergedDataFn(paramsControl), '提交的控件参数----');

      // const callbackPage = `${location.origin}${location.pathname}#/wishCheck`;
      const callbackPageYd = `${location.origin}${location.pathname}#/wishCheck?orderNo=${orderNo.value}&signRuId=${signRuId}&callbackPage=${callbackPage}`;
      console.log(mergedDataFn(paramsControl),'mergedDataFn(paramsControl)');
      try {
        let { result, code } = await Api.submitSign({
          signRuId: signRuId,
          controlList: mergedDataFn(paramsControl),
          deleteIdList: unref(deleteIdList),
          entSealId: unref(entSealId),
          privateSeal: unref(privateSeal),
          signConfirmOrderNo: orderNo.value,
          callbackPage: callbackPageYd,
        });
        
        if (code == 200) {
          signCertificateVisible.value = false;
          if (result.signConfirmUrl) {
            setTimeout(() => {
              signLoading.value = false;
              window.open(result.signConfirmUrl, '_self');
            }, 500);
          }else{
            signLoading.value = false;
            Notify({ type: 'danger', message: '未成功获取到意愿校验订单', duration: 5000});
          }
        } else {
          setTimeout(() => {
            Notify({ type: 'danger', message: result.message });
            signLoading.value = false;
          }, 1000);
        }
      } catch (error) {
        setTimeout(() => {
          signLoading.value = false;
        }, 1000);
      }
    }

    //控件合并
    function mergedDataFn(inputData: []) {
      const mergedObjects: any = {};

      inputData.forEach((obj: any) => {
        const uid = obj.uid;
        //只传签署控件
        if (['signature', 'sign-date', 'seal', 'chop-stamp'].includes(obj.controlType)) {
          if (!mergedObjects[uid]) {
            // 如果 mergedObjects 中不存在该 ID，则创建一个新的对象
            mergedObjects[uid] = { ...obj };
          } else {
            // 如果已经存在该 ID，则将属性合并到现有对象中
            Object.assign(mergedObjects[uid], obj);
          }
        }
      });
      // 将合并后的对象转换为数组
      const resultArray = Object.values(mergedObjects);

      return resultArray;
    }
    //关闭签署页
    function closeSignPage() {
      Notify({ type: 'success', message: '签署成功！' });
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
    //拒签
    function handleRejectSign() {
      if (!comment.value || comment.value.length < 1 || comment.value.length > 200) {
        return;
      }
      commentVisible.value = false;

      Dialog.confirm({
        title: '拒签',
        message: '确定拒签？',
      })
        .then(async () => {
          signLoading.value = true;
          let { code } = await Api.rejectSign({
            signRuId,
            signConfirmOrderNo: orderNo.value,
            comment: comment.value,
          });
          if (code == 200) {
            Notify({ type: 'success', message: '拒签成功！' });
            signLoading.value = false;
            if (callbackPage && typeof callbackPage == 'string') {
              window.open(decodeURIs(callbackPage), '_self');
            } else {
              signLoading.value = false;
              router.push('/index');
            }
          }
        })
        .catch(() => {
          signLoading.value = false;
          // on cancel
        });
    }

    function checkElementCustom() {
      let reg = /^(?:(\d+)|(\d+(?:,\d+)*)(?:(?:,|-)\d+(?:,\d+)*)*)$/;
    }

    //控件设置应用于选定文档的指定页码
    function handleSetControl() {
      let element = elementBatch.value;
      let reg = /^(?:(\d+)|(\d+(?:,\d+)*)(?:(?:,|-)\d+(?:,\d+)*)*)$/;
      //校验页码格式
      if (elementControl.value.propertyPageType == 'custom') {
        if (!reg.test(elementControl.value.custom)) {
          Notify({ type: 'warning', message: '页码格式不正确' });
          return;
        }
      }
      //将当前点击的控件设置为批量控件
      element.isBatch = true;
      //根据页面配置固化propertyVoList已知项
      if (elementControl.value.propertyPageType == 'custom') {
        element.propertyVoList = [
          {
            controlId: element.id,
            id:
              'page_config_id_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'page_config',
            propertyValue: 'custom',
          },
          {
            controlId: element.id,
            id:
              'page_config_id_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'custom',
            propertyValue: elementControl.value.custom,
          },
        ];
      } else {
        // 重置propertyVoList
        element.propertyVoList = [
          {
            controlId: element.id,
            id:
              'page_config_id_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'page_config',
            propertyValue: elementControl.value.propertyPageType,
          },
        ];
      }
      //更新各个文档的propertyVoList
      documentList.value.map((v) => {
        if (docKeys.value.includes(v.signRuDocId)) {
          // element.propertyVoList = element.propertyVoList.filter(v => v.propertyType !== 'relation_doc');
          element.propertyVoList.push({
            controlId: element.id,
            id:
              'relation_doc_id_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16),
            propertyType: 'relation_doc',
            propertyValue: v.signRuDocId,
          });
        }
      });
      let pages: number[] = [];
      documentList.value.map((v) => {
        if (elementControl.value.propertyPageType == 'custom') {
          pages = parseInputPages(element.custom.replace(/\s/g, ''));
        } else {
          for (let i = 0; i < v.pageSize; i++) {
            if (elementControl.value.propertyPageType == 'all') {
              pages.push(i);
            }
            if (elementControl.value.propertyPageType == 'odd_number' && (i + 1) % 2 != 0) {
              pages.push(i);
            }
            if (elementControl.value.propertyPageType == 'odd_number' && (i + 1) % 2 == 0) {
              pages.push(i);
            }
          }
        }
      });
      let minTarget = findMinImageSize(
        documentList.value,
        docKeys.value,
        Array.from(new Set(pages))
      );
      // console.log(minTarget, 'minTarget================');
      minTargetInfo.value = minTarget;
      const elementTarget = nowDocument.value.targets[element.position.page];
      const elementOffsetWidth = (nowDocument.value.maxWidth - elementTarget.width) / 2;
      const elementOffsetX = element.position.left - elementOffsetWidth;
      const elementOffsetY = currentPosition(element.position.top, elementTarget);

      documentList.value.map((v: any) => {
        if (docKeys.value.includes(v.signRuDocId)) {
          v.activeControl = v.activeControl.filter((item: any) => item.uid != element.uid);
          const { targets, maxWidth } = pageScaling(v.images);
          // const elementTarget = v.targets[element.position.page];
          // const elementOffsetWidth = ( v.maxWidth - elementTarget.width) / 2;
          // const elementOffsetX = element.position.left - elementOffsetWidth;
          // const elementOffsetY = currentPosition(element.position.top,elementTarget);

          if (elementControl.value.propertyPageType == 'custom') {
            let pages = parseInputPages(elementControl.value.custom.replace(/\s/g, ''));
            const frontUid =
              'api_uid_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16);
            pages.map((page) => {
              const target = targets[page - 1];
              const offsetWidth = (maxWidth - target.width) / 2;

              if (element.controlType == 'seal') {
                sealCount.value += 1;
              }
              if (element.controlType == 'chop-stamp') {
                stampSealCount.value += 1;
              }
              if (element.controlType == 'signature') {
                signatureCount.value += 1;
              }
              if (element.controlType == 'sign-date') {
                signDateCount.value += 1;
              }
              if (page <= v.pageSize) {
                v.activeControl.push({
                  ...element,
                  isBatch: pages.length > 1 || docKeys.value.length > 1 ? true : false,
                  offsetX: elementOffsetX,
                  offsetY: elementOffsetY, //计算成为每页的位置
                  signRuDocId: v.signRuDocId,
                  signerId: element.signerId,
                  page: page - 1,
                  oddEventType: elementControl.value.propertyPageType,
                  controlClick: false,
                  uid: element.uid || frontUid,
                  controlPageInfo: {
                    controlDocId: v.signRuDocId,
                    propertyType: 'page_config',
                    propertyValue: 'custom',
                    id: element.uid + '_page_config',
                    controlId: element.uid,
                  },
                  minTarget,
                  position: {
                    page: page - 1,
                    //   left: target.width * ( elementOffsetX / element.pageWidth) + offsetWidth,
                    //   top:  target.transformHeight + page * 16 + target.height * (elementOffsetY / element.pageHeight),
                    left: recaculateBatchControlPosInPage(
                      { ...element, position: { ...element.position, page: page - 1 } },
                      v.targets,
                      elementOffsetX,
                      elementOffsetY,
                      v.maxWidth,
                      minTarget
                    ).x,
                    top: recaculateBatchControlPosInPage(
                      { ...element, position: { ...element.position, page: page - 1 } },
                      v.targets,
                      elementOffsetX,
                      elementOffsetY,
                      v.maxWidth,
                      minTarget
                    ).y,
                  },
                });
              } else {
                //超出后删除并去重
                // deleteIdList.value.push(element.id)
                // deleteIdList.value = [...new Set(deleteIdList.value)]
              }
            });
          } else {
            const frontUid =
              'api_uid_' +
              parseInt(
                new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000)
              ).toString(16);
            for (let i = 0; i < v.docPage; i++) {
              const target = targets[i];
              const offsetWidth = (maxWidth - target.width) / 2;
              if (elementControl.value.propertyPageType == 'all') {
                if (element.controlType == 'seal') {
                  sealCount.value += 1;
                }
                if (element.controlType == 'chop-stamp') {
                  stampSealCount.value += 1;
                }
                if (element.controlType == 'signature') {
                  signatureCount.value += 1;
                }
                if (element.controlType == 'sign-date') {
                  signDateCount.value += 1;
                }
                v.activeControl.push({
                  ...element,
                  isBatch: v.images.length > 1 || docKeys.value.length > 1 ? true : false,
                  signRuDocId: v.signRuDocId,
                  signerId: element.signerId,
                  //   offsetX: elementOffsetX,
                  //   offsetY: elementOffsetY,//计算成为每页的位置
                  controlClick: false,
                  minTarget,
                  page: i,
                  oddEventType: elementControl.value.propertyPageType,
                  uid: element.uid || frontUid,
                  controlPageInfo: {
                    controlDocId: v.signRuDocId,
                    propertyType: 'page_config',
                    propertyValue: elementControl.value.propertyPageType,
                    id: element.uid + '_page_config',
                    controlId: element.uid,
                  },
                  position: {
                    page: i,
                    left:
                      element.controlType == 'chop-stamp'
                        ? target.width - element.size.width + offsetWidth
                        : elementOffsetX + offsetWidth > target.width
                          ? target.width - element.size.width + offsetWidth
                          : elementOffsetX + offsetWidth < target.width
                            ? elementOffsetX + offsetWidth
                            : elementOffsetX + offsetWidth,
                    top: target.transformHeight + (i + 1) * 16 + elementOffsetY,
                  },
                });
              }

              if (elementControl.value.propertyPageType == 'odd_number') {
                if ((i + 1) % 2 != 0) {
                  if (element.controlType == 'seal') {
                    sealCount.value += 1;
                  }
                  if (element.controlType == 'chop-stamp') {
                    stampSealCount.value += 1;
                  }
                  if (element.controlType == 'signature') {
                    signatureCount.value += 1;
                  }
                  if (element.controlType == 'sign-date') {
                    signDateCount.value += 1;
                  }
                  v.activeControl.push({
                    ...element,
                    isBatch: v.images.length > 2 || docKeys.value.length > 1 ? true : false,
                    uid: element.uid || frontUid,
                    signRuDocId: v.signRuDocId,
                    signerId: element.signerId,
                    controlClick: false,
                    oddEventType: elementControl.value.propertyPageType,
                    page: i,
                    minTarget,
                    offsetX: element.position.left,
                    offsetY: currentPosition(element.position.top, element.position.page), //计算成为每页的位置
                    controlPageInfo: {
                      controlDocId: v.signRuDocId,
                      propertyType: 'page_config',
                      propertyValue: elementControl.value.propertyPageType,
                      id: element.uid + '_page_config',
                      controlId: element.uid,
                    },
                    position: {
                      // left: element.controlType=='chop-stamp'?
                      //     target.width - element.size.width + offsetWidth:
                      //         elementOffsetX + offsetWidth > target.width?target.width - element.size.width + offsetWidth:
                      //         ( (elementOffsetX + offsetWidth < target.width)?elementOffsetX + offsetWidth:elementOffsetX + offsetWidth),
                      // top:  target.transformHeight + (i+1) * 16 + elementOffsetY,
                      page: i,
                      left: recaculateBatchControlPosInPage(
                        { ...element, position: { ...element.position, page: i } },
                        v.targets,
                        elementOffsetX,
                        elementOffsetY,
                        v.maxWidth,
                        minTarget
                      ).x,
                      top: recaculateBatchControlPosInPage(
                        { ...element, position: { ...element.position, page: i } },
                        v.targets,
                        elementOffsetX,
                        elementOffsetY,
                        v.maxWidth,
                        minTarget
                      ).y,
                    },
                  });
                }
              }
              if (elementControl.value.propertyPageType == 'even_number') {
                if ((i + 1) % 2 == 0) {
                  if (element.controlType == 'seal') {
                    sealCount.value += 1;
                  }
                  if (element.controlType == 'chop-stamp') {
                    stampSealCount.value += 1;
                  }
                  if (element.controlType == 'signature') {
                    signatureCount.value += 1;
                  }
                  if (element.controlType == 'sign-date') {
                    signDateCount.value += 1;
                  }
                  v.activeControl.push({
                    ...element,
                    isBatch: v.images.length > 3 || docKeys.value.length > 1 ? true : false,
                    offsetX: element.position.left,
                    offsetY: currentPosition(element.position.top, element.position.page), //计算成为每页的位置
                    controlClick: false,
                    minTarget,
                    page: i,
                    signerId: element.signerId,
                    signRuDocId: v.signRuDocId,
                    oddEventType: elementControl.value.propertyPageType,
                    uid: element.uid || frontUid,
                    controlPageInfo: {
                      controlDocId: v.signRuDocId,
                      propertyType: 'page_config',
                      propertyValue: elementControl.value.propertyPageType,
                      id: element.uid + '_page_config',
                      controlId: element.uid,
                    },
                    position: {
                      // left: element.controlType=='chop-stamp'?
                      //     target.width - element.size.width + offsetWidth:
                      //         elementOffsetX + offsetWidth > target.width?target.width - element.size.width + offsetWidth:
                      //         ( (elementOffsetX + offsetWidth < target.width)?elementOffsetX + offsetWidth:elementOffsetX + offsetWidth),
                      // top:  target.transformHeight + (i+1) * 16 + elementOffsetY,
                      page: i,
                      left: recaculateBatchControlPosInPage(
                        { ...element, position: { ...element.position, page: i } },
                        v.targets,
                        elementOffsetX,
                        elementOffsetY,
                        v.maxWidth,
                        minTarget
                      ).x,
                      top: recaculateBatchControlPosInPage(
                        { ...element, position: { ...element.position, page: i } },
                        v.targets,
                        elementOffsetX,
                        elementOffsetY,
                        v.maxWidth,
                        minTarget
                      ).x,
                    },
                  });
                }
              }
            }
          }
        } else {
          //其他文档的当前控件删除掉
          v.activeControl = v.activeControl.filter((item) => item.uid != element.uid);
          //自动切换到另一有该控件的文档
          // if(v.signRuDocId != docId.value){
          //     handleDocChange(v.signRuDocId)
          // }
        }
      });
      //   refreshControlPosition(element)
      calculateControlRangClick(element);
      //更新当前文档
      nowDocument.value = documentList.value.find((v) => v.signRuDocId == docId.value);
      batchSetShow.value = false;
      updateControlCount();
    }
    //   更新当前控件位置
    function calculateControlRangClick(element: any) {
      let x = element.position.left;
      let y = element.position.top;

      const opt: any = {
        x: x,
        y: y,
        pageSize: nowDocument.value.pageSize,
        size: element.size,
        currentPage: element.position.page,
        allHeight: nowDocument.value.imageAllHeight,
        //所有页面最大的宽度
        maxWidth: nowDocument.value.maxWidth,
        isBatch: element.isBatch,
        minTarget: element?.minTarget,
      };
      moveRange(opt, nowDocument.value.targets, element.isBatch);
      nowDocument.value.activeControl.map((v) => {
        if (v.uid === element.uid && v.position.page == opt.currentPage) {
          v.position.left = opt.x;
          v.position.top = opt.y;
        }
      });
    }
    //更新统计数量
    function updateControlCount() {
      sealCount.value = 0;
      stampSealCount.value = 0;
      signatureCount.value = 0;
      signDateCount.value = 0;
      documentList.value.forEach((v) => {
        v.activeControl.map((element) => {
          if (element.controlType == 'seal') {
            sealCount.value += 1;
          }
          if (element.controlType == 'chop-stamp') {
            stampSealCount.value += 1;
          }
          if (element.controlType == 'signature') {
            signatureCount.value += 1;
          }
          if (element.controlType == 'sign-date') {
            signDateCount.value += 1;
          }
        });
      });
    }
    function handleCancelSealList() {
      sealVisible.value = false;
      if (!entSealId.value) {
        isSignText.value = true;
      }
    }

    function beforeCloseSeal(action, done) {
      return true;
    }

    function handleConfirmSuccess(info: any) {
      confirmTypeInfo.value.confirmType = info.confirmType;
      if (confirmAction.value == 'sign') {
        willResult.value = true;
        submitSignData();
      }
      if (confirmAction.value == 'reject') {
        willResult.value = true;
        handleRejectSign();
      }
    }
    // 关闭提示的方法
    function closeSignControlTip() {
      showSignControlTip.value = false;
      localStorage.setItem("showSignControlTip", String(showSignControlTip.value));
    }

    return {
      dateTypeActions,
      docList,
      documentList,
      openPersonalAccount,
      nowDocument,
      beforeCloseSeal,
      docId,
      CanvasZoomBase,
      previewImages,
      baseUrl,
      signRuId,
      handleConfirmSuccess,
      group,
      options,
      controlDelete,
      controlItemRef,
      controlMousedown,
      BaseSize,
      handleUnSign,
      detailVisible,
      signDetailRef,
      saveSignature,
      handleDetail,
      handleAddSeal,
      loadSignColor,
      loadSignStatus,
      processVisible,
      signatureTabKey,
      signerList,
      QRsignatureBase64,
      handleWriteProcess,
      signCertificateVisible,
      signaturePad,
      handleAuth,
      handleRejectSign,
      handleAddSignature,
      goSign,
      certificateInfo,
      handleSubmitSign,
      updateCertificate,
      handlePersonAuth,
      tenantInfo,
      handleSetSignature,
      signControlInfo,
      sealCount,
      signatureCount,
      zoom,
      needOpenPersonlAccountVisible,
      signDateCount,
      handleAddSignDate,
      showPad,
      controlShowMenu,
      sealVisible,
      signatureVisible,
      personSignatureVisible,
      signDatevisible,
      signatureList,
      signatureId,
      signatureQC,
      hasSenderSigner,
      sealImgId,
      onDateTypeSelect,
      handleSignatureCancel,
      handleSignatureVisible,
      handleSetSeals,
      signerCaAuthInfo,
      undo,
      isDetail,
      clear,
      batchSetShow,
      isActive1,
      isActive2,
      isActive3,
      sealList,
      authCerInfo,
      signSealId,
      handleDocChange,
      quickHandleDocChange,
      signLoading,
      authCertificateVisible,
      signaturePadConfig,
      closeSignaturePad,
      setPadSize,
      aspecRatio,
      isSignText,
      handleSign,
      taskVisible,
      disabledSignControls,
      taskType,
      confirmRef,
      handleGoNextTask,
      handleNoTask,
      taskInfo,
      controlMenu,
      faceOpenPersonalAccount,
      docs,
      checkboxRefs,
      docKeys,
      toggle,
      pageOptions,
      elementControl,
      pageColumns,
      showPagePicker,
      onConfirm,
      refreshControlPosition,
      stampSealCount,
      handleAddChopSeal,
      handleSetControl,
      checkElementCustom,
      handleCancelSealList,
      minTargetInfo,
      handleFace,
      commentVisible,
      comment,
      beforeClose,
      wakeUpComment,
      callbackPage,
      sealRows,
      personSignatureRows,
      selectSeal,
      userAuth,
      hasSignature,
      signControlTip,
      showSignControlTip,
      closeSignControlTip,
      hashSignControl,
      locationSignControl,
      currentLocationIndex,
      clientHeight,
      personalSignAuth,
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
  margin: 30px auto 80px;
}

.doc-footer-operation {
  // height: 1.5rem;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding-bottom: 16px;

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
    bottom: 28%;
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

</style>

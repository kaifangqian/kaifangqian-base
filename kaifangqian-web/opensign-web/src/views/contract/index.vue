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
  <div class="contract-start-container" loading-tip="提交中...">
    <Loading :loading="loading" :absolute="false" />
    <Loading :loading="uploadLoading" :absolute="false" />
    <div class="contract-header">
      <div class="contract-header-back" @click="handelBack">
        <Icon icon="ant-design:left-outlined" />
        <span>返回</span>
      </div>
      <div class="contract-header-title"></div>
      <div class="contract-header-action">
        <a-button type="primary" danger v-if="signRuId" @click="handelDeleteRu">删除</a-button>
        <a-button type="default" @click="handleSaveBaseInfo">保存</a-button>
        <a-button type="primary" @click="handleSetPositionAndParams">下一步</a-button>
        <!-- <a-button type="link"  @click="handleWriteParams" v-if="isWrite" :disabled="!signRuId">填写文档参数</a-button>
        <a-button type="primary" @click="handleStart">发起</a-button> -->
      </div>
    </div>
    <div class="contract-form-body">
      <div class="contract-form">
        <Card title="合同信息" size="small">
          <BasicForm @register="registerForm">
            <template #code="{ model, field }">
              <template v-if="model['codeType'] == 2">
                <span class="system-generated">系统自动生成</span>
              </template>
              <template v-else>
                <a-input v-model:value="model[field]" placeholder="请输入" />
              </template>
            </template>

            <template #subject="{ model, field }">
              <template v-if="model['subjectType'] == 2">
                <span class="system-generated">系统自动生成</span>
              </template>
              <template v-else>
                <a-input v-model:value="model[field]" placeholder="请输入" />
              </template>
            </template>
            <template #name="{ model, field }">
              <span>{{ model[field] }}</span>
            </template>
            <template #fileList>
              <div>
                <div
                  class="signFile-action"
                  v-if="signRuInfo.baseVo && signRuInfo.baseVo.addFileType == 1"
                >
                  <div>
                    <a-upload
                      :accept="buildFileType(isOffice)"
                      :showUploadList="false"
                      name="file"
                      :max-count="documentsCeiling"
                      action="/resrun-paas/sign/file/create"
                      :headers="uploadHeaders"
                      @change="handleChange"
                      :disabled="fileList.length >= documentsCeiling"
                    >
                      <div style="width: 260px" class="signFile-action-add">
                        <template v-if="fileList.length >= documentsCeiling">
                          <a-tooltip placement="top">
                            <template #title>
                              <span>最多只能选择{{ documentsCeiling }}个签约文件</span>
                            </template>
                            <Icon
                              icon="ant-design:cloud-upload-outlined"
                              size="16"
                              color="rgba(0, 0, 0, 0.25)"
                            />
                            <a-button type="link" :disabled="fileList.length >= documentsCeiling"
                              >文件上传</a-button
                            >
                          </a-tooltip>
                        </template>
                        <template v-else>
                          <Icon icon="ant-design:cloud-upload-outlined" size="16" />
                          <a-button type="link">文件上传</a-button>
                        </template>
                      </div>
                    </a-upload>
                  </div>
                  <Divider type="vertical" />
                  <div v-if="fileList.length >= documentsCeiling" class="signFile-action-add">
                    <a-tooltip placement="top">
                      <template #title>
                        <span>最多只能选择{{ documentsCeiling }}个签约文件</span>
                      </template>
                      <Icon
                        icon="ant-design:reconciliation-outlined"
                        size="16"
                        color="rgba(0, 0, 0, 0.25)"
                      />
                      <a-button type="link" :disabled="fileList.length >= documentsCeiling"
                        >选择在线模板</a-button
                      >
                    </a-tooltip>
                  </div>
                  <div
                    v-else-if="fileList.length < documentsCeiling"
                    class="signFile-action-add"
                    @click="handleChoseTpl"
                  >
                    <Icon icon="ant-design:reconciliation-outlined" size="16" />
                    <a-button type="link" @click="handleChoseTpl">选择在线模板</a-button>
                  </div>
                </div>
                <div>
                  <p
                    style="
                      color: rgba(0, 0, 0, 0.4);
                      font-size: 12px;
                      margin-bottom: 20px;
                      margin-top: 5px;
                    "
                    >文件上传仅支持 {{ buildFileType(isOffice) }} 格式，最多{{
                      documentsCeiling
                    }}个签约文件，单份文件不超过30MB</p
                  >
                </div>

                <div class="contract-files">
                  <ul>
                    <draggable
                      :list="fileList"
                      ghost-class="ghost"
                      chosen-class="chosenClass"
                      animation="300"
                      :sort="true"
                      class="drag-files"
                    >
                      <li v-for="(item, index) in fileList" :key="index">
                        <div class="file-icon">
                          <!-- <a-tag
                            class="file-tag"
                            :color="item.docType == 1 ? '#0079FE' : '#FE9500'"
                            >{{ item.docType == 1 ? '本地文件' : '在线模板' }}</a-tag
                          > -->
                          <Icon
                            icon="ant-design:close-circle-outlined"
                            color="#ed5220"
                            v-if="
                              (signRuInfo.baseVo &&
                                signRuInfo.baseVo.deleteFileType == 1 &&
                                item.originType == 1) ||
                              item.originType == 2
                            "
                            @click.stop="handleDelFile(index)"
                          />
                          <SvgIcon
                            :name="`${item.docType === 1 ? 'pdf' : 'tpl'}`"
                            size="100"
                            class="file-img"
                          />
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
                                    v-if="
                                      signRuInfo.baseVo && signRuInfo.baseVo.downloadFileType == 1
                                    "
                                    @click="handleRuDownload({ ruDocId: item.id, ruId: signRuId })"
                                    >下载文档</a
                                  >
                                </a-menu-item>
                                <a-menu-item
                                  v-if="
                                    (signRuInfo.baseVo &&
                                      signRuInfo.baseVo.deleteFileType == 1 &&
                                      item.originType == 1) ||
                                    item.originType == 2
                                  "
                                >
                                  <a href="javascript:;" @click.stop="handleDelFile(index)"
                                    >删除文档</a
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
                    </draggable>
                  </ul>
                </div>
              </div>
            </template>
            <!-- <template #addAnnexType="{ model }">
              <BasicUpload
                uploadTip="支持 .doc, .docx, .wps, .jpg, .png, .pdf, .xls, .xlsx, .zip, .rar 格式，最多上传10个，单份附件不超过20MB"
                :maxSize="4"
                :maxNumber="10"
                :api="uploadApi"
                :accept="[
                  '.doc',
                  '.docx',
                  '.wps',
                  '.jpg',
                  '.png',
                  '.pdf',
                  '.xls',
                  '.xlsx',
                  '.zip',
                  '.rar',
                ]"
                :uploadParams="{ dataCategory: 'worksfiles' }"
                @update="handleFilesSuccess"
                :dataSource="otherFileList"
              />
            </template> -->
          </BasicForm>
        </Card>
        <Card title="参与方" size="small">
          <BasicForm @register="registerSignerForm">
            <!-- <template #signOrderType="{ model, field }">
              <a-radio-group
                v-model:value="model[field]"
                class="sign-config"
                :disabled="signRuInfo.baseVo && signRuInfo.baseVo.signerType == 2"
              >
                <a-radio value="1">顺序签署</a-radio>
                <a-radio value="2">无序签署</a-radio>
              </a-radio-group>
              <span style="font-size: 14px; color: #666;">
              {{ model[field] === '1' ? '（填写默认同时开始，随后按顺序签署）' : '（任意一方可随时开始填写与签署）' }}
               </span>
            </template> -->
            <template #signOrderType="{ model, field }">
              <a-radio-group
                v-model:value="model[field]"
                class="sign-config"
                :disabled="signRuInfo.baseVo && signRuInfo.baseVo.signerType == 2"
              >
                <a-radio value="1">
                  顺序签署
                  <span style="font-size: 13px; color: #666">
                    （填写默认同时开始，随后按顺序签署）
                  </span>
                </a-radio>
                <a-radio value="2">
                  无序签署
                  <span style="font-size: 13px; color: #666">
                    （任意一方可随时开始填写与签署）
                  </span>
                </a-radio>
              </a-radio-group>
            </template>
            <template #signatory>
              <div class="signatory-area">
                <ul>
                  <a-form-item>
                    <li v-for="(item, index) in signerList" :key="index">
                      <div v-if="item.signerType == 1">
                        <p class="signatory-count">发起方</p>
                        <div class="initiator-info">
                          <div class="initiator-left">
                            <a-tag class="signer-initiator-tag">发起方</a-tag>
                            <p class="initiator-name weight-label">{{ item.signerName }}</p>
                            <div class="initiator-process">
                              <span class="weight-label">签署流程：</span>
                              <div class="initiator-list">
                                <div
                                  v-for="(signderItem, senderIndex) in item.senderList"
                                  :key="senderIndex"
                                  class="initiator-item"
                                >
                                  <span class="initiator-item-name">{{
                                    signderItem.senderName
                                  }}</span>
                                  <a-tooltip>
                                    <template #title>
                                      <div v-if="signderItem.senderType == 4">
                                        <p
                                          >印章：{{
                                            signderItem.senderSealId
                                              ? signderItem.senderSealName || signderItem.sealName
                                              : ''
                                          }}</p
                                        >
                                        <p
                                          >盖章方式：{{
                                            signderItem.senderSignType == 1
                                              ? '自动盖章'
                                              : '手动盖章'
                                          }}</p
                                        >
                                        <p v-if="signderItem.senderSignType == 2"
                                          >签署人：{{ signderItem.senderUserName }}</p
                                        >
                                      </div>
                                      <div v-else-if="signderItem.senderType == 1">
                                        <p>签署人：文档签署的发起人签字 </p>
                                      </div>
                                      <div v-else-if="signderItem.senderType == 5">
                                        <p>审批人：{{ signderItem.senderUserName }} </p>
                                      </div>
                                      <div v-else>
                                        <p>签署人：{{ signderItem.senderUserName }}</p>
                                      </div>
                                    </template>
                                    <Icon icon="ant-design:question-circle-outlined" />
                                  </a-tooltip>
                                  <div
                                    class="arrow-right"
                                    v-if="senderIndex + 1 != item.senderList.length"
                                  >
                                    <span class="arrow-line"></span>
                                    <Icon icon="ant-design:caret-right-filled" color="#d3d3d3" />
                                  </div>
                                </div>
                                <a-button type="link" @click="handleInitiator(item.senderList)">{{
                                  signRuInfo.baseVo && signRuInfo.baseVo.signerType == 2
                                    ? '查看'
                                    : '设置'
                                }}</a-button>
                              </div>
                            </div>
                          </div>
                          <div
                            class="signatory-action"
                            v-if="signRuInfo.baseVo && signRuInfo.baseVo.signerType != 2"
                          >
                            <SvgIcon
                              name="up"
                              size="20"
                              v-if="index != 0"
                              @click="handleArrowUp(index)"
                              style="margin: 0 10px"
                            />
                            <SvgIcon
                              name="down"
                              size="20"
                              v-if="index + 1 != signerList.length"
                              @click="handleArrowDown(index)"
                              style="margin: 0 10px"
                            />
                            <Icon
                              icon="ant-design:delete-outlined"
                              size="20"
                              @click="handleDelete(index)"
                              style="margin: 0 10px"
                            />
                          </div>
                        </div>
                      </div>
                      <div v-if="item.signerType == 2">
                        <p class="signatory-count">接收方{{ item.currentIndex }}</p>

                        <div class="signatory-item">
                          <div
                            class="signatory-person-info"
                            style="display: flex; flex-direction: column"
                          >
                            <a-tag class="signer-person-tag">个人</a-tag>
                            <div style="margin-top: 0px">
                              <span
                                class="receive-label weight-label"
                                style="width: 80px; display: inline-block; flex: 0 0 80px"
                                >个人信息:
                              </span>
                              <a-space :size="20">
                                <!-- <span class="receive-label weight-label">个人信息: </span> -->
                                <a-input
                                  v-model:value="item.signerName"
                                  placeholder="接收人真实姓名"
                                />
                                <a-input
                                  v-model:value="item.signerExternalValue"
                                  :maxlength="11"
                                  placeholder="接收方手机号"
                                  @blur="handleValidateAccount"
                                  v-if="webConfig.systenAccountType == 'phone'"
                                />
                                <a-input
                                  v-model:value="item.signerExternalValue"
                                  placeholder="接收方邮箱"
                                  @blur="handleValidateAccount"
                                  v-else-if="webConfig.systenAccountType == 'email'"
                                />
                                <a-input
                                  v-model:value="item.signerExternalValue"
                                  placeholder="接收方手机号或邮箱"
                                  @blur="handleValidateAccount"
                                  v-else
                                />
                              </a-space>
                            </div>
                            <div style="padding: 10px 0px 10px 0">
                              <p class="flex items-center" style="margin-bottom: 10px">
                                <span>是否允许免意愿快捷签署：</span>
                                <a-tooltip overlayStyle="width: 300px; max-width: 500px !important;">
                                  <template #title
                                    >签署人如果已开通免意愿快捷签署服务，则签署时，可无需进行额外的意愿认证</template
                                  >
                                  <Icon
                                    icon="ant-design:question-circle-outlined"
                                    style="margin-right: 10px; color: #888"
                                  />
                                </a-tooltip>
                                <a-switch
                                  :checked-value="1"
                                  :un-checked-value="0"
                                  v-model:checked="item.agreeSkipWillingness"
                                  :disabled="
                                    signRuInfo.baseVo && signRuInfo.baseVo.signerType === 2
                                  "
                                />
                              </p>
                              <a-space style="margin-bottom: 10px">
                                <!-- <span>人脸识别校验：</span>
                                    <a-switch :checked-value="1" :un-checked-value="0" v-model:checked="item.confirmType" /> -->
                                <span>签署意愿验证方式：</span>
                                <a-tooltip>
                                  <template #title
                                    >签署时，签署人可选择对应的意愿认证方式进行身份校验</template
                                  >
                                  <Icon
                                    icon="ant-design:question-circle-outlined"
                                    style="margin-right: 10px; color: #888"
                                  />
                                </a-tooltip>
                                <a-checkbox-group
                                  :value="item.verifyType"
                                  :options="confirmOptions"
                                  :disabled="
                                    signRuInfo.baseVo && signRuInfo.baseVo.signerType === 2
                                  "
                                  @change="(val) => handleVerifyTypeChange(val, item)"
                                />
                              </a-space>
                              <a-space class="flex items-center w-full" style="margin-bottom: 10px">
                                <span>签名方式：</span>
                                <a-tooltip  overlayStyle="width: 450px; max-width: 500px !important;">
                                  <template #title>
                                    <p>不限制：个人签署时，不限制其签名类型</p>
                                    <p>手写签名：个人手绘的自定义签名</p>
                                    <p>模板签名：系统根据签名模板生成的电子化的个人章，例如"张三之印"</p>
                                    <!-- </div> -->
                                  </template>
                                  <Icon
                                    icon="ant-design:question-circle-outlined"
                                    style="margin-right: 10px; color: #888"
                                  />
                                </a-tooltip>
                                <a-radio-group 
                                  v-model:value="item.sealType" 
                                  :disabled="
                                      signRuInfo.baseVo && signRuInfo.baseVo.signerType === 2
                                  ">
                                  <a-radio value="NOLIMIT"> 不限制 </a-radio>
                                  <a-radio value="TEMPLATE"> 模板签名 </a-radio>
                                  <a-radio value="HAND"> 手写签名 </a-radio>
                                </a-radio-group>
                              </a-space>
                              <a-space
                                class="flex items-center w-full"
                                v-show="personalSignAuth == 'allowed'"
                              >
                                <span>实名认证要求：</span>
                                <a-radio-group 
                                  v-model:value="item.personalSignAuth" 
                                  :disabled="
                                      signRuInfo.baseVo && signRuInfo.baseVo.signerType === 2
                                  ">
                                  <a-radio value="required">
                                    须实名认证
                                    <a-tooltip>
                                      <template #title>
                                        须实名认证：【强烈建议】使用个人电子签章前，必须完成实名认证，符合电子签名的合法性与安全性要求
                                      </template>
                                      <Icon icon="ant-design:question-circle-outlined" />
                                    </a-tooltip>
                                  </a-radio>
                                  <a-radio value="not_required">
                                    无需实名认证
                                    <a-tooltip>
                                      <template #title
                                        >无需实名认证：使用个人电子签章前，无需进行实名认证</template
                                      >
                                      <Icon icon="ant-design:question-circle-outlined" />
                                    </a-tooltip>
                                  </a-radio>
                                </a-radio-group>
                              </a-space>
                            </div>
                            <!-- <div v-else style="width:100%;height: 10px;"></div> -->
                          </div>
                          <div
                            class="signatory-action"
                            v-if="signRuInfo.baseVo && signRuInfo.baseVo.signerType != 2"
                          >
                            <SvgIcon
                              name="up"
                              size="20"
                              v-if="index != 0"
                              @click="handleArrowUp(index)"
                              style="margin: 0 10px"
                            />
                            <SvgIcon
                              name="down"
                              size="20"
                              v-if="index + 1 != signerList.length"
                              @click="handleArrowDown(index)"
                              style="margin: 0 10px"
                            />
                            <Icon
                              icon="ant-design:delete-outlined"
                              size="20"
                              @click="handleDelete(index)"
                              style="margin: 0 10px"
                            />
                          </div>
                        </div>
                      </div>
                      <div v-if="item.signerType == 3">
                        <p class="signatory-count">接收方{{ item.currentIndex }}</p>

                        <div class="signatory-item">
                          <div class="signatory-left receive-ent-left">
                            <a-tag class="signer-company-tag">企业</a-tag>
                            <!-- <p class="receive-type-name">企业</p> -->
                            <div class="signatory-receive-ent-info">
                              <div class="ent-info-item">
                                <span class="receive-label weight-label">企业名称: </span>
                                <a-input
                                  v-model:value="item.signerName"
                                  placeholder="请输入企业名称"
                                  class="ent-name"
                                ></a-input>
                                <span class="ent-name-tip"
                                  >营业执照的企业名称，完全一致才能进行签署</span
                                >
                              </div>
                              <div class="ent-info-item">
                                <span class="receive-label weight-label">经办人: </span>
                                <div class="signatory-form">
                                  <a-form-item-rest>
                                    <a-input
                                      v-model:value="item.manageName"
                                      placeholder="经办人真实姓名"
                                    />
                                  </a-form-item-rest>
                                  <a-form-item>
                                    <!-- <a-input v-model:value="item.managePhone"  :maxlength="11" placeholder="经办人手机号" @blur="handleValidatePhone"/> -->
                                    <a-input
                                      v-model:value="item.managePhone"
                                      :maxlength="11"
                                      placeholder="经办人手机号"
                                      @blur="handleValidateAccount"
                                      v-if="webConfig.systenAccountType == 'phone'"
                                    />
                                    <a-input
                                      v-model:value="item.managePhone"
                                      placeholder="经办人邮箱"
                                      @blur="handleValidateAccount"
                                      v-else-if="webConfig.systenAccountType == 'email'"
                                    />
                                    <a-input
                                      v-model:value="item.managePhone"
                                      placeholder="经办人手机号或邮箱"
                                      @blur="handleValidateAccount"
                                      v-else
                                    />
                                  </a-form-item>
                                </div>
                              </div>
                              <div class="ent-info-item">
                                <span class="receive-label weight-label">签署流程: </span>
                                <ul class="receive-node">
                                  <!-- <li class="initiator-item" v-for="(receiveItem, receiveIndex) in item.senderList" :key="receiveIndex">
                                      <span>{{ receiveItem.senderType==1?'经办人签字':'组织签章' }}</span>
                                      <Icon icon="ant-design:close-outlined" class="delete-cc-user" v-if="signRuInfo.baseVo && signRuInfo.baseVo.signerType!=2" @click="handleDeleteEntNode(item,receiveIndex)"/>
                                    </li> -->
                                  <div
                                    v-for="(receiveItem, receiveIndex) in item.senderList"
                                    :key="receiveIndex"
                                    class="initiator-item"
                                  >
                                    <span class="initiator-item-name">{{
                                      receiveItem.senderType == 1 ? '经办人签字' : '组织签章'
                                    }}</span>

                                    <div
                                      class="arrow-right"
                                      v-if="receiveIndex + 1 != item.senderList.length"
                                      style="padding-left: 8px"
                                    >
                                      <span class="arrow-line"></span>
                                      <Icon icon="ant-design:caret-right-filled" color="#d3d3d3" />
                                    </div>
                                  </div>

                                  <a-button type="link" @click="handleAcceptInitiator(item)">{{
                                    signRuInfo.baseVo && signRuInfo.baseVo.signerType == 2
                                      ? '查看'
                                      : '设置'
                                  }}</a-button>
                                  <!-- <a-dropdown trigger="hover">
                                        <a-button type="link" v-if="signRuInfo.baseVo && signRuInfo.baseVo.signerType!=2"><Icon icon="ant-design:plus-outlined" />添加</a-button>
                                        <template #overlay>
                                          <a-menu>
                                            <a-menu-item :disabled="!canAddEbtNode(item,1)">
                                              <a href="javascript:;" @click="handleAddEntNode(item,1)">经办人签字</a>
                                            </a-menu-item>
                                            <a-menu-item :disabled="!canAddEbtNode(item,4)">
                                              <a href="javascript:;" @click="handleAddEntNode(item,4)">组织签章</a>
                                            </a-menu-item>
                                          </a-menu>
                                        </template>
                                      </a-dropdown> -->
                                </ul>
                              </div>
                            </div>
                          </div>
                          <div
                            class="signatory-action"
                            v-if="signRuInfo.baseVo && signRuInfo.baseVo.signerType != 2"
                          >
                            <SvgIcon
                              name="up"
                              size="20"
                              v-if="index != 0"
                              @click="handleArrowUp(index)"
                              style="margin: 0 10px"
                            />
                            <SvgIcon
                              name="down"
                              size="20"
                              v-if="index + 1 != signerList.length"
                              @click="handleArrowDown(index)"
                              style="margin: 0 10px"
                            />
                            <Icon
                              icon="ant-design:delete-outlined"
                              size="20"
                              @click="handleDelete(index)"
                              style="margin: 0 10px"
                            />
                          </div>
                        </div>
                      </div>
                    </li>
                  </a-form-item>
                </ul>
              </div>
            </template>
          </BasicForm>
          <div class="signer-set" v-if="signRuInfo.baseVo && signRuInfo.baseVo.signerType != 2">
            <a-button type="default" v-if="isShowInitiatorBtn()" @click="handleAddInitiator"
              ><Icon icon="ant-design:plus-outlined" />添加发起方</a-button
            >
            <a-button type="default" @click="handleAddPersonal"
              ><Icon icon="ant-design:plus-outlined" />添加个人</a-button
            >
            <a-button type="default" @click="handleAddEnt"
              ><Icon icon="ant-design:plus-outlined" />添加企业
            </a-button>
          </div>
        </Card>

        <Card title="附件及其他" size="small">
          <BasicForm @register="registerApproveForm">
            <template #addAnnexType="{ model }">
              <BasicUpload
                uploadTip="支持 .doc, .docx, .wps, .jpg, .png, .pdf, .xls, .xlsx, .zip, .rar 格式，最多上传10个，单份附件不超过20MB"
                :maxSize="4"
                :maxNumber="10"
                :api="uploadApi"
                :accept="[
                  '.doc',
                  '.docx',
                  '.wps',
                  '.jpg',
                  '.png',
                  '.pdf',
                  '.xls',
                  '.xlsx',
                  '.zip',
                  '.rar',
                ]"
                :uploadParams="{ dataCategory: 'worksfiles' }"
                @update="handleFilesSuccess"
                :dataSource="otherFileList"
              />
            </template>

            <template #ccedType>
              <div class="flex-item ccerList-area">
                <ul class="ccerList-ul">
                  <li v-for="(item, index) in ccerList" :key="index">
                    <span>{{
                      item.ccerType == 1
                        ? item.internalCcerName
                        : item.externalCcerName + '（' + item.externalCcedValue + '）'
                    }}</span>
                    <Icon
                      v-if="item.ccerAddType != 1"
                      icon="ant-design:close-outlined"
                      class="delete-cc-user"
                      @click="handleDeleteCC(index)"
                    />
                  </li>
                </ul>
                <Icon
                  v-if="signRuInfo.baseVo && signRuInfo.baseVo.addCcerType == 1"
                  icon="ant-design:plus-circle-outlined"
                  size="24"
                  color="#94969c"
                  class="add-cc-user"
                  @click="handleFileCC"
                />
              </div>
            </template>
            <template #approveList>
              <div class="approve-info">
                <div class="approve-type">
                  <div class="flex-item">
                    <p class="approve-title weight-label">发起前审批</p>
                    <a-tooltip>
                      <template #title>
                        <span
                          >发起文件时，执行审批流程，企业内部对发起的文件及签署信息进行审核，审核通过后文件真正发送出去</span
                        >
                      </template>
                      <Icon icon="ant-design:question-circle-outlined" size="20" />
                    </a-tooltip>
                  </div>

                  <div class="approve-flow">
                    <div>
                      <span>{{ approveForm.initiateFlowName }}</span>
                      <a-button type="link">查看流程</a-button>
                      <a-button type="link">执行记录</a-button>
                    </div>
                    <p class="approve-desc">{{ approveForm.initiateFlowDesc }}</p>
                  </div>
                </div>
                <div class="approve-type">
                  <div class="flex-item">
                    <p class="approve-title weight-label">签署前审批</p>
                    <a-tooltip>
                      <template #title>
                        <span
                          >文件在内部签署之前，执行审批流程，审批通过后才可以进行签署操作，审批不通过，标记文件被拒绝签署</span
                        >
                      </template>
                      <Icon icon="ant-design:question-circle-outlined" size="20" />
                    </a-tooltip>
                  </div>

                  <div class="approve-flow">
                    <div>
                      <span>{{ approveForm.initiateFlowName }}</span>
                      <a-button type="link">查看流程</a-button>
                      <a-button type="link">执行记录</a-button>
                    </div>
                    <p class="approve-desc">{{ approveForm.initiateFlowDesc }}</p>
                  </div>
                </div>
              </div>
            </template>
            <template #caSignType="{ model, field }">
              <a-radio-group v-model:value="model[field]" :disabled="true">
                <a-radio :style="radioStyle" :value="1"
                  >使用CA数字证书（符合电子签名法）
                  <a-tooltip>
                    <template #title>签署文件时，需要使用CA机构颁发的数字证书</template>
                    <Icon icon="ant-design:question-circle-outlined" />
                  </a-tooltip>
                </a-radio>
                <a-radio :style="radioStyle" :value="2"
                  >使用平台防篡改证书（保护文件）（无法律效力）
                  <a-tooltip>
                    <template #title
                      >签署文件时，使用平台下发的防篡改证书，该证书非CA机构颁发，仅用于文件保护，避免文件被篡改，签署后的文件不具备法律效力，请慎重选择</template
                    >
                    <Icon icon="ant-design:question-circle-outlined" /> </a-tooltip
                ></a-radio>
                <a-radio :style="radioStyle" :value="3"
                  >合成签名和印章图片（不保护文件）（无法律效力）
                  <a-tooltip>
                    <template #title
                      >签署文件时，不使用任何数字证书，仅在文件上加盖合成签名和印章图片，无法保护文件也不具备法律效力，请慎重选择</template
                    >
                    <Icon icon="ant-design:question-circle-outlined" />
                  </a-tooltip>
                </a-radio>
              </a-radio-group>
            </template>
          </BasicForm>
        </Card>
      </div>
      <InitiatorModal
        @register="register"
        @saveData="saveDataPre"
        @success="handleInitiatorSuccess"
      ></InitiatorModal>
      <FileCCModal @register="registerFileCC" @success="handleFileCCSuccess"></FileCCModal>
      <TemplateModal @register="registerTpl" @success="handleTplSuccess"></TemplateModal>
      <PreviewModal @register="registerPreview"></PreviewModal>
      <PositionModal @register="registerPosition"></PositionModal>
      <ParamsModal @register="registerParams"></ParamsModal>
      <AcceptModal @register="registerAccept" @success="handleAcceptSuccess" />
    </div>
  </div>
</template>

<script lang="ts">
  import { ref, unref, defineComponent, reactive, toRefs, onMounted, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import { Card, Divider } from 'ant-design-vue';
  import { VueDraggableNext } from 'vue-draggable-next';
  import { Loading } from '/@/components/Loading';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicForm, useForm } from '/@/components/Form';
  import { BasicUpload } from '/@/components/Upload';
  import { useUserStore } from '/@/store/modules/user';
  import { uploadApi } from '/@/api/sys/upload';
  import { getToken } from '/@/utils/auth';
  import { handleRuDownload, uuid } from '/@/utils';
  import { BasicFileList } from '/@/components/FileInfoList/index';
  import { getAppEnvConfig } from '/@/utils/env';
  import dayjs from 'dayjs';
  import { cloneDeep } from 'lodash-es';
  import InitiatorModal from './modal/InitiatorModal.vue';
  import FileCCModal from './modal/FileCCModal.vue';
  import TemplateModal from './modal/TemplateModal.vue';
  import PreviewModal from '/@/views/contract/modal/PreviewModal.vue';
  import PositionModal from '/@/views/contract/modal/PositionModal.vue';
  import ParamsModal from '/@/views/contract/modal/ParamsModal.vue';
  import AcceptModal from '/@/views/contract/modal/AcceptModal.vue';
  import { lineBasicFormSchema, lineSignerFormSchema, approveFormSchema } from './data';
  import { isMobile, isEmail } from '/@/utils/validate';
  import { getSignConfirm, getPlatePersonalSignAuth } from '/@/api/sys/common';
  import { getUploadFileType, buildFileType } from '/@/api';
  import {
    saveBaseInfo,
    getBusinessReInfo,
    getBusinessRuInfo,
    startContract,
    getIsWrite,
    deleteSignRu,
    getSealInfo,
  } from '/@/api/contract';
  import { getSystemLimit } from '/@/api/license';
  import { silentQueryApi, silentOpenApi } from '/@/api/yundun';
import { sealType } from '../signature/seal/data';

  interface FileItem {
    id?: string;
    annexId?: string;
    realName?: string;
    status?: string;
    success?: boolean;
    name?: string;
    responseData?: {};
  }

  interface Participant {
    senderType:number;
    senderOrder:number;
    senderSignType?:number|boolean;
    senderName:string;
    edit?:boolean;
    id?:string;
    senderSealId?:string;
    sealName?:string;
    senderUserName?:string;
    senderUserId:string;
    signerId:string;
    verifyType?: Array<string>;
    agreeSkipWillingness?: number;
    sealType?: string;
  }

  export default defineComponent({
    name: 'ContractStart',
    components: {
      Icon,
      Card,
      Divider,
      BasicForm,
      BasicUpload,
      InitiatorModal,
      FileCCModal,
      draggable: VueDraggableNext,
      SvgIcon,
      TemplateModal,
      PreviewModal,
      PositionModal,
      ParamsModal,
      Loading,
      AcceptModal,
      BasicFileList,
    },
    setup() {
      const compState = reactive({
        absolute: false,
        loading: false,
        uploadLoading: false,
        tip: '',
      });
      const radioStyle = reactive({
        marginBottom: '15px',
      });
      const signConfirm = ref(false);
      const signRuInfo: any = ref({});
      const fileList = ref<any>([]);
      const isWrite = ref(true);
      const otherFileList = ref<any>([]);
      const signerList = ref<any>([]);
      const ccerList = ref<any>([]);
      const approveForm: any = ref({
        initiateFlowName: '',
        initiateFlowDesc: '',
        signFlowDesc: '',
      });
      const startForm: any = ref({
        initiatorName: '',
      });
      const confirmOptions = ref([
        { label: '验证码', value: 'CAPTCHA' },
        { label: '签署密码', value: 'PASSWORD' },
        { label: '双重校验（验证码 + 签署密码）', value: 'DOUBLE' },
        { label: '人脸识别', value: 'FACE', disabled: false },
      ]);

      const userStore = useUserStore();
      const webConfig = userStore.getWebConfig;
      const userInfo = userStore.getUserInfo;
      startForm.value.initiatorName = userInfo.loginDepartName;
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const query = route.query;
      const signReId = ref(route.query.signReId);
      const signRuId = ref(route.query.signRuId);
      const personalSignAuth = ref('');

      const { VITE_GLOB_APP_CODE } = getAppEnvConfig();

      const { createMessage: msg, createConfirm } = useMessage();
      const [register, { openModal }] = useModal();
      const [registerFileCC, { openModal: openFileCCModal }] = useModal();
      const [registerTpl, { openModal: openTplModal }] = useModal();
      const [registerPreview, { openModal: openPreviewModal }] = useModal();
      const [registerPosition, { openModal: openPositionModal }] = useModal();
      const [registerParams, { openModal: openParamsnModal }] = useModal();
      const [registerAccept, { openModal: openAcceptModal }] = useModal();

      const uploadHeaders = reactive({
        'X-Access-Token': getToken(),
        'resrun-app-code': VITE_GLOB_APP_CODE,
      });

      const [
        registerForm,
        { setFieldsValue: setBaseFieldsValue, clearValidate, validate, getFieldsValue },
      ] = useForm({
        labelWidth: 100,
        schemas: lineBasicFormSchema,
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });
      const [
        registerSignerForm,
        { setFieldsValue: setSignerFieldsValue, getFieldsValue: getSignFieldsValue },
      ] = useForm({
        labelWidth: 100,
        layout: 'vertical',
        schemas: lineSignerFormSchema,
        showActionButtonGroup: false,
      });
      const [registerApproveForm, { setFieldsValue, getFieldsValue: getApproveFieldsValue }] =
        useForm({
          labelWidth: 100,
          schemas: approveFormSchema,
          showActionButtonGroup: false,
        });

      onMounted(() => {
        getContractReInfo();
        initSysLimit();
        if (unref(signRuId)) {
          checkIsWrite();
        }
      });
      const documentsCeiling = ref(1);
      const isOffice = ref(false);


      watch(
        () => [fileList.value, signRuInfo.value.baseVo?.subjectType, signRuInfo.value.baseVo?.subject],
        ([newFileList, subjectType, subject]) => {
          // 检查条件：subjectType 为 1 且 subject 为空
          if (subjectType === 1 && (!subject || subject === '')) {
            // 检查 fileList 是否有文件
            if (newFileList && newFileList.length > 0) {
              // 获取第一个文件的名称
              const firstFileName = newFileList[0].name;
              if (firstFileName) {
                // 更新表单中的 subject 字段
                setBaseFieldsValue({
                  subject: firstFileName
                });
              }
            }
          }
        },
        {
          immediate: true, // 立即执行一次
          deep: true // 深度监听
        }
      );

      async function initSysLimit() {
        const office = await getUploadFileType();
        isOffice.value = office.result;
        const limit = await getSystemLimit();
        documentsCeiling.value = limit.documentsCeiling;

        const res = await getSignConfirm();
        if (res.code == 200) {
          signConfirm.value = res.result;
          confirmOptions.value[3].disabled = !signConfirm.value;
        }
      }
      function handlePreview(item) {
        console.log(item, 'dsdfdf');
        openPreviewModal(true, {
          isUpdate: false,
          record: {
            docId: item.annexId || item.docOriginId,
            docName: item.name,
            docType: item.docType,
          },
        });
      }
      //返回
      function handelBack() {
        if (window.opener) {
          window.close();
        } else {
          window.open(window.location.origin + '/#/contract/doc', '_self');
        }
      }
      async function getContractReInfo() {
        let result;
        if (route.query.signRuId) {
          result = await getBusinessRuInfo({ signRuId: route.query.signRuId });
        } else {
          result = await getBusinessReInfo({ signReId: route.query.signReId });
        }

        // 添加平台和业务线个人签署认证要求的判断逻辑
        let platePersonSignAuth = '';
        const personalSignAuthRes = await getPlatePersonalSignAuth();
        if (personalSignAuthRes.code == 200) {
          platePersonSignAuth = personalSignAuthRes.result.personalSignAuthType || 'required';
        }
        if(!result.baseVo.personalSignAuth){
          personalSignAuth.value = platePersonSignAuth;
        }else{
          personalSignAuth.value = result.baseVo.personalSignAuth;
        }

        console.log('personalSignAuth.value', personalSignAuth.value);

        if (result) {
          signRuInfo.value = result;
          if (!result.signerList) {
            result.signerList = [];
          }
          const resultSignerList =
            result.signerList && result.signerList.sort((a, b) => a.signerOrder - b.signerOrder);
          decodeSignerList(resultSignerList);
          signerList.value = resultSignerList;
          if (!result.signerList?.length) {
            handleAddInitiator();
          }
          signerList.value.map((item) => {
            // 未设置认证要求数据时，设置默认数据
            if (!item.personalSignAuth) {
              item.personalSignAuth =
                personalSignAuth.value === 'not_required' ? 'not_required' : 'required';
            }
            // 未设置签名方式数据时，设置默认数据
            if (!item.sealType) {
              item.sealType = 'NOLIMIT';
            }
            if (item.senderList && Array.isArray(item.senderList)) {
              item.senderList.map((v) => {
                // 未设置认证要求数据时，设置默认数据
                if (!v.personalSignAuth) {
                  v.personalSignAuth =
                    personalSignAuth.value === 'not_required' ? 'not_required' : 'required';
                }
                // 未设置签名方式数据时，设置默认数据
                if (!v.sealType) {
                  v.sealType = 'NOLIMIT';
                }
              });
            }
            if (item.signerType == 1) {
              // item.senderList.map(v=>{
              //   if(v.senderType==4){
              //     loadSealName(v);
              //   }
              // })
              item.senderList = item.senderList.sort((a, b) => a.senderOrder - b.senderOrder);
            }
            if (item.signerType == 3) {
              item.senderList = item.senderList
                ? item.senderList.sort((a, b) => a.senderOrder - b.senderOrder)
                : [];
              if (signRuId.value) {
                item.manageName = (item.senderList && item.senderList[0].senderName) || '';
              } else {
                item.manageName = '';
              }
              item.managePhone = (item.senderList && item.senderList[0].senderExternalValue) || '';
            }
            
          });
          ccerList.value = result.ccerList;
          fileList.value = result.fileList;
          otherFileList.value = result.otherFileList;

          setBaseFieldsValue({ ...result.baseVo });
          setFieldsValue({
            caSignType: result.baseVo.caSignType,
            ccedType: result.baseVo.ccedType,
            addAnnexType: result.baseVo.addAnnexType,
          });
          setSignerFieldsValue({ signOrderType: result.baseVo.signOrderType });
          if (result.fileList) {
            setBaseFieldsValue({
              fileList: result.fileList,
            });
            // clearValidate(['fileList'])
          }
          clearValidate();
          loadSignerIndex();
        }
      }
      //模板弹框
      function handleChoseTpl() {
        openTplModal(true, {
          isUpdate: false,
          record: {},
        });
      }
      //判断是否需要填写
      async function checkIsWrite() {
        let res = await getIsWrite({ signRuId: unref(signRuId) });
        if (res) {
          isWrite.value = res;
        }
      }
      //选择模板
      function handleTplSuccess(list) {
        console.log(list, '选择的文件');
        list.forEach((item) => {
          fileList.value.push({
            annexId: item?.annexId,
            docType: 2,
            name: item.templateName,
            originType: 2,
            id: '',
            docOriginId: item.id,
          });
          setBaseFieldsValue({
            fileList: fileList.value,
          });
          clearValidate(['fileList']);
        });
      }

      async function loadSealName(row) {
        let result: any = await getSealInfo({ sealId: row.senderSealId });
        if (result) {
          row.sealName = result?.sealName;
        }
      }

      function handleChange(info) {
        if (info.file.status == 'uploading') {
          compState.uploadLoading = true;
        }
        if (
          info.file.status == 'done' ||
          info.file.status == 'error' ||
          info.file.status == 'success' ||
          info.file.status == 'removed'
        ) {
          compState.uploadLoading = false;
        }
        if (info.file?.response?.code == 200) {
          const index = info.file.name.lastIndexOf('\.');
          const fileName = info.file.name.substring(index, -1);
          console.log(fileList.value, '文档');

          fileList.value.push({
            annexId: info.file.response.result,
            docType: 1,
            name: fileName,
            originType: 2,
            id: '',
            docOriginId: '',
          });
          setBaseFieldsValue({
            fileList: fileList.value,
          });
          clearValidate(['fileList']);

          console.log(fileList.value, '文档1111');
        }
      }
      function handleFilesSuccess(list) {
        let fileList = <FileItem[]>[];
        list.map((item) => {
          fileList.push({ annexId: item.responseData.result, realName: item.name, id: '' });
        });
        otherFileList.value = fileList;
      }
      //发起方签署设置
      function handleInitiator(senderList) {
        openModal(true, {
          isUpdate: false,
          record: {
            senderList: senderList,
            signRuId: unref(signRuId),
            signReId: unref(signReId),
            baseVo: signRuInfo.value.baseVo,
            signConfirm: signConfirm.value,
            signerType: signRuInfo.value.baseVo.signerType,
            personalSignAuth: personalSignAuth.value,
          },
        });
      }
      //抄送
      function handleFileCC() {
        openFileCCModal(true, {
          isUpdate: false,
          records: {},
        });
      }
      //参与人回调
      function handleInitiatorSuccess(list) {
        console.log(list, '参与人----');
        signerList.value.map((item) => {
          if (item.signerType == 1) {
            item.senderList = cloneDeep(list);
          }
          if (item.senderType == 4) {
            item.senderSignType = item.senderSignType == false ? 2 : 1;
          }
        });
      }
      //文件抄送
      function handleFileCCSuccess(list) {
        console.log(list, '抄送人-----');
        ccerList.value = [...ccerList.value, ...list];
      }
      //调整顺序
      function handleArrowDown(index) {
        if (index < signerList.value.length - 1) {
          // Swap the current item with the one below it
          [signerList.value[index], signerList.value[index + 1]] = [
            signerList.value[index + 1],
            signerList.value[index],
          ];
        }
        setSignerOrder();
      }
      function handleArrowUp(index) {
        if (index > 0) {
          [signerList.value[index], signerList.value[index - 1]] = [
            signerList.value[index - 1],
            signerList.value[index],
          ];
        }
        setSignerOrder();
      }
      //是否显示添加发起方按钮
      function isShowInitiatorBtn() {
        let result = signerList.value.filter((item) => item.signerType == 1);
        return result.length ? false : true;
      }
      function setSignerOrder() {
        signerList.value.map((item, index) => {
          item.signerOrder = index + 1;
          item.senderList.forEach((v, i) => {
            v.signerOrder = i + 1;
          });
        });
        loadSignerIndex();
      }
      //删除签约文件
      function handleDelFile(index) {
        const deletedFile = fileList.value[index];
        fileList.value.splice(index, 1);
        
        // 检查是否需要更新主题字段
        const baseVo = signRuInfo.value.baseVo;
        if (baseVo && baseVo.subjectType === 1 && 
            baseVo.subject === deletedFile.name) { // 如果删除的是当前主题对应的文件
          if (fileList.value.length > 0) {
            // 使用第一个文件名作为新主题
            setBaseFieldsValue({
              subject: fileList.value[0].name
            });
          } else {
            // 如果没有文件了，清空主题
            setBaseFieldsValue({
              subject: ''
            });
          }
        }
      }
      //删除抄送人
      function handleDeleteCC(index) {
        ccerList.value.splice(index, 1);
      }
      //删除签约方
      function handleDelete(index) {
        signerList.value.splice(index, 1);
        loadSignerIndex();
      }
      //添加签署方
      function handleAddInitiator() {
        signerList.value.unshift({
          signerName: userInfo.loginTenantName,
          signerExternalType: 0,
          signerExternalValue: '',
          signerType: 1,
          signerOrder: signerList.value.length + 1,
          senderList: [],
        });
        setSignerOrder();
      }
      //添加个人
      function handleAddPersonal() {
        signerList.value.push({
          signerName: '',
          signerExternalType: 1,
          signerExternalValue: '',
          signerType: 2,
          signerOrder: signerList.value.length + 1,
          senderList: [],
          verifyType: ['CAPTCHA', 'PASSWORD', 'DOUBLE'],
          personalSignAuth:
            personalSignAuth.value === 'not_required' ? 'not_required' : 'required',
          sealType: 'NOLIMIT',
        });
        setSignerOrder();
      }
      //添加企业
      function handleAddEnt() {
        signerList.value.push({
          signerName: '',
          signerExternalType: 1,
          signerExternalValue: '',
          signerType: 3,
          signerOrder: signerList.value.length + 1,
          senderList: [
            {
              senderType: 4,
              senderOrder: 1,
              senderName: '组织签章',
              verifyType: ['CAPTCHA', 'PASSWORD', 'DOUBLE'],
            },
          ],
          uid: uuid(),
        });
        setSignerOrder();
      }
      // 添加接收方企业节点
      function handleAddEntNode(row, type) {
        console.log(row, type);
        row.senderList.push({
          senderType: type,
        });
      }
      //是否可添加企业节点
      function canAddEbtNode(row, type) {
        let matchEntNode = row.senderList && row.senderList.filter((v) => v.senderType == type);
        return matchEntNode && matchEntNode.length ? false : true;
      }
      // 删除企业节点
      function handleDeleteEntNode(row, index) {
        row.senderList.splice(index, 1);
      }

      //指定位置和参数路由
      function handleSetPositionAndParams() {
        saveData().then((res: string) => {
          if (res) {
            // 使用Promise包装路由替换操作，确保路由更新完成后再执行后续操作
            const routeUpdatePromise = new Promise<void>((resolve, reject) => {
              if (!unref(signRuId)) {
                router.replace({
                  query: {
                    __full__: '',
                    signRuId: res,
                  },
                }).then(() => {
                  // 路由更新成功后，更新signRuId的值
                  signRuId.value = res;
                  resolve();
                }).catch((error) => {
                  console.error('路由替换失败:', error);
                  reject(error);
                });
              } else {
                resolve();
              }
            });

            // 等待路由更新完成后再跳转
            routeUpdatePromise.then(() => {
              window.open(
                '/#/contract/position?__full__&signRuId=' + res,
                '_self',
              );
            }).catch((error) => {
              msg.error('操作失败: ' + error.message);
            });
          }
        }).catch((error) => {
          // 添加错误处理
          console.error('保存数据失败:', error);
          msg.error('保存数据失败');
        });
      }

      //参数填写
      function handleWriteParams() {
        window.open(
          '/#/contract/params?__full__&signReId=' +
            unref(signReId) +
            '&signRuId=' +
            unref(signRuId),
          '_self',
        );
      }
      // 检查是否存在重复的手机号
      function hasDuplicatePhoneNumbers(users) {
        const phoneSet = new Set();
        for (const user of users) {
          if (phoneSet.has(user.signerExternalValue)) {
            return true; // 发现重复的手机号
          }
          phoneSet.add(user.signerExternalValue);
        }
        return false; // 没有重复的手机号
      }

      //业务线保存
      async function handleSaveBaseInfo(type?: string) {
        try {
          saveData().then((res) => {
            compState.loading = false;
            if (res) {
              if (!unref(signRuId)) {
                router.replace({
                  query: {
                    __full__: '',
                    signRuId: res as string,
                  },
                });
              }
              
              msg.success('保存成功');
              getContractReInfo();
            } else {
              compState.loading = false;
            }
          });
        } catch (error) {
          // 捕获并处理错误
          compState.loading = false;
          console.error('路由替换发生错误:', error);
          // 进行额外的错误处理，比如显示提示信息
        }
      }
      //业务线发起
      function handleStart() {
        try {
          compState.loading = true;
          saveData().then(async (res) => {
            if (res) {
              if (!unref(signRuId)) {
                router.replace({
                  query: {
                    __full__: '',
                    // signReId: route.query.signReId,
                    signRuId: res as string,
                  },
                });
              }
              signRuId.value = res as string;
              let result = await startContract({ signRuId: signRuId.value });
              if (result) {
                compState.loading = false;
                msg.success('发起成功',5000);
                setTimeout(() => {
                  window.close();
                }, 1000);
              } else {
                compState.loading = false;
              }
            }
          });
        } catch (error) {
          // 捕获并处理错误
          console.error('路由替换发生错误:', error);
          compState.loading = false;
          // 进行额外的错误处理，比如显示提示信息
        }
      }
      async function saveDataPre(){
        const resultRe = await saveData();
        const asyncPage = window.location.origin + '/#/contract/start?__full__=&signRuId=' + resultRe;
        const result = await silentOpenApi({ callbackPage: asyncPage });
        if (result.status === 0) {
          window.open(result.openSignNoVerifyUrl, '_self');
        } else if (result.status === 1) {
          window.open(asyncPage, '_self');
        }
      }
      function saveData() {
        return new Promise(async (resolve, reject) => {
          let baseVo = await validate();
          let approveData = await getApproveFieldsValue();
          let signConfig = await getSignFieldsValue();
          // 发起方和接收方企业名称不可重复
          let repeatSenderName = false;

          signerList.value.map((item) => {
            if (item.signerType == 1) {
              item.senderList.forEach((v, index) => {
                v.senderOrder = index + 1;
                if (v.senderSignType == false) {
                  v.senderSignType = 2;
                }
                if (v.senderSignType == true) {
                  v.senderSignType = 1;
                }
              });
            }
            if (item.signerType == 2) {
              item.signerExternalType = isMobile(item.signerExternalValue) ? 1 : 2;
            }
            //将企业接收方经办人信息注入到签署节点上
            if (item.signerType == 3) {
              let repeatNameMatch = signerList.value.filter(
                (v) => v.signerName == item.signerName && v.signerType == 1,
              );
              if (repeatNameMatch && repeatNameMatch.length) {
                repeatSenderName = true;
              } else {
                repeatSenderName = false;
              }
              item.signerExternalType = isMobile(item.managePhone) ? 1 : 2;
              item.signerExternalValue = item.managePhone;
              item.senderList &&
                item.senderList.some((v) => {
                  v.senderName = item.manageName;
                  v.senderSignType = 2;
                  v.senderExternalType = item.signerExternalType;
                  v.senderType = v.senderType == 1 ? 1 : 4; //修正历史数据中组织签章为2的数据
                  v.senderExternalValue = item.managePhone;
                });
            }
          });
          let receiveSigner = signerList.value.filter((v) => v.signerType == 2);
          let receiveSignerEnt = signerList.value.filter((v) => v.signerType == 3);
          let validateSignerName = true,
            validatePhone = true;
          let validateReceiveEntName = true,
            validateReceiveEntManageName = true,
            validateReceiveEntManagePhone = true,
            validateReceiveEntNodes = true;
          receiveSigner.some((v) => {
            if (!v.signerName) {
              validateSignerName = false;
              return false;
            }
            if (webConfig?.systenAccountType == 'phone') {
              if (!isMobile(v.signerExternalValue)) {
                validatePhone = false;
                return false;
              }
            } else if (webConfig?.systenAccountType == 'email') {
              if (!isEmail(v.signerExternalValue)) {
                validatePhone = false;
                return false;
              }
            } else {
              if (isMobile(v.signerExternalValue) || isEmail(v.signerExternalValue)) {
              } else {
                validatePhone = false;
                return false;
              }
            }
          });
          console.log(receiveSignerEnt, '企业接收方');
          receiveSignerEnt.some((v) => {
            console.log(v.senderList && !v.senderList.length, '23432432432432');
            //校验企业名称
            if (!v.signerName) {
              validateReceiveEntName = false;
              return false;
            }
            //校验签署节点
            if (v.senderList && !v.senderList.length) {
              validateReceiveEntNodes = false;
              return false;
            }
            let managerInfo = v.senderList[0];
            if (managerInfo) {
              //校验经办人名称
              if (!managerInfo.senderName) {
                validateReceiveEntManageName = false;
                return false;
              }
              //校验经办人手机号
              if (webConfig?.systenAccountType == 'phone') {
                if (!isMobile(managerInfo.senderExternalValue)) {
                  validateReceiveEntManagePhone = false;
                  return false;
                }
              } else if (webConfig?.systenAccountType == 'email') {
                if (!isEmail(managerInfo.senderExternalValue)) {
                  validateReceiveEntManagePhone = false;
                  return false;
                }
              } else {
                if (
                  isMobile(managerInfo.senderExternalValue) ||
                  isEmail(managerInfo.senderExternalValue)
                ) {
                } else {
                  validateReceiveEntManagePhone = false;
                  return false;
                }
              }
            }
          });
          if (!validateSignerName) {
            msg.warning('请输入接收方姓名');
            return;
          }
          if (!validatePhone) {
            if (webConfig?.systenAccountType == 'phone') {
              msg.warning('接收方手机号格式不正确');
            } else if (webConfig?.systenAccountType == 'email') {
              msg.warning('接收方邮箱格式不正确');
            } else {
              msg.warning('接收方手机号或邮箱格式不正确');
            }
            return;
          }
          if (!validateReceiveEntName) {
            msg.warning('接收方企业名称不能为空');
            return;
          }
          if (!validateReceiveEntNodes) {
            msg.warning('接收方企业签署节点不能为空');
            return;
          }
          if (!validateReceiveEntManageName) {
            msg.warning('接收方企业经办人不能为空');
            return;
          }
          if (!validateReceiveEntManagePhone) {
            if (webConfig?.systenAccountType == 'phone') {
              msg.warning('接收方企业经办人手机号格式不正确');
            } else if (webConfig?.systenAccountType == 'email') {
              msg.warning('接收方企业经办人邮箱格式不正确');
            } else {
              msg.warning('接收方企业经办人手机号或邮箱格式不正确');
            }
            return;
          }

          if (repeatSenderName) {
            msg.warning('外部企业名称不可与发起方企业名称重复');
            return;
          }

          // 使用方法
          const hasDuplicates = hasDuplicatePhoneNumbers(receiveSigner);

          if (hasDuplicates) {
            msg.warning('接收方存在重复的手机号！');
            return;
          }
          baseVo.fileList.map((v, i) => {
            v.docOrder = i + 1;
          });
          let params = {
            baseVo: {
              ...signRuInfo.value.baseVo,
              ...baseVo,
              ...approveData,
              ...signConfig,
              signReId: route.query.signReId,
              fileList: undefined,
              signRuId: route.query.signRuId ? route.query.signRuId : '',
              expireDate: baseVo.expireDate
                ? dayjs(baseVo.expireDate).endOf('day').format('YYYY-MM-DD HH:mm:ss')
                : '',
              personalSignAuth: personalSignAuth.value,
            },
            fileList: baseVo.fileList,
            signerList: cloneSignerList(),
            otherFileList: otherFileList.value,
            ccerList: ccerList.value,
          };
          compState.loading = true;
          // console.log('params----', params);
          let result = await saveBaseInfo(params);
          if (result) {
            compState.loading = false;
            resolve(result);
          } else {
            compState.loading = false;
            reject(result);
          }
        });
      }
      //删除实例
      async function handelDeleteRu() {
        createConfirm({
          title: '提醒',
          content: '确定删除该实例？',
          okText: '确定',
          cancelText: '取消',
          iconType: 'warning',
          onOk: async function () {
            compState.loading = true;
            let result = await deleteSignRu({ signRuId: signRuId.value });
            if (result) {
              msg.success('删除成功');
              //todo 不是window.open打开的无法通过window.close进行关闭
              if (window.opener) {
                window.close();
              } else {
                router.go(-1);
              }
            }
            compState.loading = false;
          },
        });
      }

      function loadSignerIndex() {
        let list = signerList.value.filter((m) => m.signerType !== 1);
        list.map((v, i) => {
          v.currentIndex = i + 1;
        });
      }

      function handleValidatePhone(e) {
        if (e.target.value && !isMobile(e.target.value)) {
          msg.warning('手机号格式不正确');
        }
      }
      function handleValidateEmail(e) {
        if (e.target.value && !isEmail(e.target.value)) {
          msg.warning('邮箱格式不正确');
        }
      }
      function handleValidateAccount(e) {
        console.log('handleValidateAccount', e);
        if (e.target.value) {
          if (webConfig.systenAccountType == 'phone') {
            return handleValidatePhone(e);
          } else if (webConfig.systenAccountType == 'email') {
            return handleValidateEmail(e);
          } else {
            if (!(isEmail(e.target.value) || isMobile(e.target.value))) {
              msg.warning('手机号或邮箱格式不正确');
            }
          }
        }
      }
      function handleAcceptSuccess(result, recordId) {
        signerList.value.forEach((item) => {
          if (item.id === recordId || item.uid === recordId) {
            item.senderList = result;
          }
        });
        console.log('add result:', result, recordId, signerList.value);
      }

      function handleAcceptInitiator(acceptItem) {
        openAcceptModal(true, {
          isUpdate: false,
          record: {
            data: acceptItem,
            signConfirm: signConfirm.value,
            signerType: signRuInfo.value.baseVo.signerType,
            personalSignAuth: personalSignAuth.value,
          },
        });
      }
      function cloneSignerList() {
        let newSignerList = JSON.parse(JSON.stringify(signerList.value));
        newSignerList.forEach((item: any) => {
          // item.signerType = 1;
          if (item.senderList && item.senderList.length > 0) {
            // 发起方
            item.senderList.forEach((element: any) => {
              if (element.verifyType && element.verifyType.length > 0) {
                element.verifyType = element.verifyType.join(',');
                console.log('join', element.verifyType);
              }
            });
          } else if (item.signerType === 2 && item.verifyType && item.verifyType.length > 0) {
            // 签署方个人
            item.verifyType = item.verifyType.join(',');
            console.log('join', item.verifyType);
          }
        });
        console.log('newSignerList', newSignerList);
        return newSignerList;
      }
      function decodeSignerList(data) {
        // let newSignerList = JSON.parse(JSON.stringify(signerList.value));
        data.forEach((item: any) => {
          // item.signerType = 1;
          if (item.senderList && item.senderList.length > 0) {
            // 发起方
            item.senderList.forEach((element: any) => {
              if (element.verifyType) {
                element.verifyType = element.verifyType.split(',');
                console.log('join', element.verifyType);
              }
            });
          } else if (item.signerType === 2 && item.verifyType && item.verifyType.length > 0) {
            // 签署方个人
            item.verifyType = item.verifyType.split(',');
            console.log('join', item.verifyType);
          }
        });
        // console.log('newSignerList', newSignerList);
        // return newSignerList;
      }

      // 添加验证签署意愿验证方式的函数
      function handleVerifyTypeChange(value: string[], item: Participant) {
        // 当用户尝试取消所有选项时给出提示并阻止操作
         if (value.length === 0) {
          msg.warning('签署意愿验证方式至少需要选中一个选项');
          return;
        }
        // 只有在有有效值时才更新 verifyType
        item.verifyType = value;
      }

      return {
        handelDeleteRu,
        handleSaveBaseInfo,
        handleDeleteCC,
        registerForm,
        uploadHeaders,
        handleChange,
        fileList,
        uploadApi,
        otherFileList,
        handleFilesSuccess,
        registerSignerForm,
        signerList,
        startForm,
        loadSealName,
        register,
        handleInitiatorSuccess,
        handleInitiator,
        registerApproveForm,
        ccerList,
        loadSignerIndex,
        approveForm,
        handleArrowDown,
        handleArrowUp,
        isShowInitiatorBtn,
        handleDelete,
        handleAddPersonal,
        handleAddEnt,
        handleAddInitiator,
        userInfo,
        handleFileCC,
        handleFileCCSuccess,
        registerFileCC,
        handleSetPositionAndParams,
        handleWriteParams,
        signRuInfo,
        handleDelFile,
        handleStart,
        registerTpl,
        handleTplSuccess,
        handleChoseTpl,
        registerPreview,
        handlePreview,
        registerPosition,
        registerParams,
        handleValidatePhone,
        signRuId,
        isWrite,
        handelBack,
        ...toRefs(compState),
        handleRuDownload,
        radioStyle,
        handleAddEntNode,
        canAddEbtNode,
        signConfirm,
        handleDeleteEntNode,
        registerAccept,
        handleAcceptSuccess,
        handleAcceptInitiator,
        documentsCeiling,
        webConfig,
        handleValidateEmail,
        handleValidateAccount,
        isOffice,
        confirmOptions,
        buildFileType,
        saveData,
        saveDataPre,
        handleVerifyTypeChange,
        personalSignAuth,
      };
    },
  });
</script>

<style lang="less" scoped>
  @import './index.less';
  .signFile-action {
    // display: flex;
    // align-items: center;
    // color: #127fd2;
    display: flex;
    flex-direction: row;
    width: 520px;
    height: 64px;
    border: 1px dashed #276ef9;
    // border-radius: 4px;
    :deep(.app-iconify) {
      color: #127fd2;
    }
    :deep(.ant-btn-link) {
      padding: 0 2px;
    }
  }
  .contract-header-action {
    :deep(.ant-btn) {
      margin: 0 5px;
    }
  }
  .initiator-process,
  .initiator-item,
  .arrow-right,
  .initiator-list,
  .flex-item {
    display: flex;
    align-items: center;
  }
  .contract-files {
    // padding:5px;
    .drag-files {
      display: flex;
    }
    ul {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      width: 800px;
      // border: 1px solid #e9e9e9;
      li {
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
            left: 0px;
            top: 0;
            border-radius: 0px;
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
        .file-img {
          width: 100%;
          height: 100%;
        }
        .file-name {
          font-size: 14px;
          // font-weight: 550;
          white-space: nowrap;
          width: 140px;
          margin: 0 auto;
          padding-left: 10px;
          overflow: hidden;
          text-overflow: ellipsis;
          margin-bottom: 0;
          color: #666;
        }
      }
    }
  }

  .initiator-info {
    background: #f7f8fb;
    padding: 15px 20px;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    // border-radius: 5px;
    border: 1px solid #ced2dc;
    .initiator-left {
      flex: 1;
    }

    .initiator-process {
      margin-top: 10px;
      align-items: baseline;
      .initiator-list {
        flex-wrap: wrap;
        width: 100%;
      }
      .initiator-item {
        :deep(.app-iconify) {
          margin: 0 2px;
        }

        .initiator-item-name {
          white-space: nowrap;
        }
      }
      .arrow-right {
        .arrow-line {
          width: 60px;
          height: 2px;
          background: #d3d3d3;
        }
      }
    }
  }
  .signatory-area {
    flex: 1;
  }
  :deep(.ant-picker-default) {
    width: 200px;
  }
  .signatory-count {
    color: #797979;
    margin-bottom: 5px;
  }
  .signatory-item {
    background: #f7f8fb;
    padding: 15px 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 20px;
    // border-radius: 5px;
    border: 1px solid #ced2dc;
    .signatory-form {
      display: flex;
      align-items: center;
      margin-left: 50px;
      :deep(.ant-input) {
        width: 200px;
        margin: 0 10px;
      }
      :deep(.ant-form-item) {
        margin-bottom: 0;
      }
    }
  }
  .receive-ent-left {
    .signatory-receive-ent-info {
      .ent-info-item {
        display: flex;
        align-items: center;
        justify-content: left;
        margin-bottom: 8px;
        .receive-label {
          width: 80px;
          display: inline-block;
          flex: 0 0 80px;
        }
        .ent-name-tip {
          white-space: nowrap;
          color: #999;
          font-size: 12px;
        }
        .ent-name {
          flex: 0 0 420px;
          margin: 0 10px;
        }
      }
      .signatory-form {
        margin-left: 0;
      }
    }
    .receive-node {
      display: flex;
      align-items: center;
      margin-left: 6px;
      .initiator-item {
        min-width: 60px;
        /* background:#f7f8fb;
      border:1px solid #e9e9eb;
      border-radius: 2px;
      margin:0 4px;
      padding:2px 8px; */
        color: #000;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: space-between;
      }
      .arrow-right .arrow-line {
        width: 60px;
        height: 2px;
        background: #d3d3d3;
      }
    }
  }

  .ccerList-area {
    .add-cc-user {
      cursor: pointer;
    }
    .ccerList-ul {
      display: flex;
      align-items: center;
      margin-bottom: 0;
      li {
        min-width: 90px;
        background: #f7f8fb;
        border: 1px solid #e9e9eb;
        border-radius: 2px;
        margin: 0 4px;
        padding: 2px 8px;
        color: #94969c;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .delete-cc-user {
        }
      }
    }
  }
  .approve-info {
    flex: 1;
    .approve-type {
      background: #f7f8fb;
      padding: 15px 20px;
      border-radius: 2px;
      margin-bottom: 10px;
      .approve-desc {
        color: #141e31af;
        font-size: 12px;
      }
      .approve-flow {
        margin-top: 15px;
      }
    }
    .approve-title {
      font-size: 16px;
      margin-right: 5px;
    }
  }

  .signer-set {
    display: flex;
    justify-content: center;
    border-top: 1px solid #e4e3e3; // 添加上边框样式
    padding-top: 30px; // 增加上部内边距以与上方内容隔开
    gap: 16px; // 添加按钮之间的水平间距
  }

  .signFile-action-add {
    flex: auto;
    position: relative;
    display: inline-block;
    font-size: 12px;
    color: #276ef9;
    background-color: rgba(39, 110, 249, 0.04);
    -webkit-text-decoration: none;
    text-decoration: none;
    outline: none;
    cursor: pointer;
    text-align: center;
    line-height: 64px;

    &:hover {
      background-color: #276ef920;
    }
  }
  :deep(.ant-divider-vertical) {
    margin: 0 0px;
    top: 12px;
    left: 0;
    width: 1px;
    height: 40px;
    background-color: #276ef9;
    opacity: 0.2;
  }

  .system-generated {
    padding: 8px 12px;
    background-color: #f0f8fc;
    border-radius: 4px;
    color: #666;
    font-size: 14px;
  }

  .signer-initiator-tag {
    margin-bottom: 10px;
    left: 2px;
    top: 0;
    // border-bottom-left-radius: 8px;
    // padding: 0 20px;
    width: 60px;
    text-align: center;
    background: #0079fe30;
    color: #0079fe;
    border: #0079fe 1px solid;
  }

  .signer-person-tag {
    margin-bottom: 10px;
    left: 2px;
    top: 0;
    // border-bottom-left-radius: 8px;
    // padding: 0 20px;
    width: 60px;
    text-align: center;
    background: #19be6b30;
    color: #19be6b;
    border: #19be6b 1px solid;
  }

  .signer-company-tag {
    margin-bottom: 10px;
    left: 2px;
    top: 0;
    // border-bottom-left-radius: 8px;
    // padding: 0 20px;
    width: 60px;
    text-align: center;
    background: #fe950030;
    color: #fe9500;
    border: #fe9500 1px solid;
  }
  .signatory-action {
    min-width: 120px;
    // cursor: pointer;
    // text-align: right;
    // :deep(.app-iconify) {
    //   margin: 0 10px;
    // }
  }
  :deep(.resrun-svg-icon) {
    // margin: 0 10px;
    color: #666;
  }
</style>

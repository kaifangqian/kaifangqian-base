<template>
  <div class="basic-line-container">
    <Loading :loading="loading" :absolute="false" />
    <Loading :loading="uploadLoading" :absolute="false" />
    <div class="line-header">
      <div class="line-header-title">
        <p v-if="!nameEdit">{{ basicInfo.name }}</p>
        <a-input v-else v-model:value="basicInfo.name" @change="handleNameChange" />
      </div>
      <Icon
        icon="ant-design:edit-outlined"
        size="18"
        v-if="!nameEdit"
        @click="handleEditLineName"
      />
      <Icon
        icon="ant-design:check-outlined"
        size="18"
        v-if="nameEdit"
        @click="handleEditLineName"
      />
    </div>
    <a-card title="编号" size="small">
      <a-radio-group v-model:value="basicInfo.codeType" class="basci-radio-type">
        <a-radio :value="1">经办人发起时自行填写</a-radio>
        <a-radio :value="2">按照预设的文件编号规则自动生成</a-radio>
      </a-radio-group>
      <div class="line-number-area" v-if="basicInfo.codeType == '2'">
        <div class="line-number-tip">
          <p style="margin-left: 30px">文件编号按照规则生成的示例：{{ codeExampleStr }}</p>
          <div class="line-number-rule-type" style="margin-left: 30px">
            <span>拼接规则：</span>
            <a-radio-group
              v-model:value="basicInfo.codeTypeRuleVo.link"
              @change="handleSetCodeExample"
            >
              <a-radio value="">无符号拼接</a-radio>
              <a-radio value="-">“-”拼接</a-radio>
              <a-radio value="_">“_”拼接</a-radio>
            </a-radio-group>
          </div>
          <ul v-if="basicInfo.codeType == '2'" class="rule-list">
            <draggable
              :list="basicInfo && basicInfo.codeTypeRuleVo && basicInfo.codeTypeRuleVo.detailVoList"
              ghost-class="ghost"
              chosen-class="chosenClass"
              animation="300"
              @end="handleSetCodeExample"
            >
              <template
                v-for="(element, index) in basicInfo.codeTypeRuleVo &&
                basicInfo.codeTypeRuleVo.detailVoList"
                :key="index"
              >
                <div class="item">
                  <div class="item-li">
                    <a-checkbox v-model:checked="element.checked" @change="handleSetCodeExample" />
                    <div class="item-li-info">
                      <div class="info-title">
                        <Icon icon="ant-design:holder-outlined" size="18" />
                        <span>{{ element.label }}</span>
                      </div>
                      <div v-if="element.edit" class="item-value">
                        <a-input
                          style="width: 100%"
                          v-model:value="element.content"
                          :placeholder="element.defaultContent"
                          :allowClear="true"
                          v-if="element.contentType == 'text'"
                        />
                        <a-select
                          style="width: 100%"
                          v-model:value="element.content"
                          :options="element.options"
                          :placeholder="element.defaultContent"
                          :allowClear="true"
                          v-if="
                            element.contentType == 'datetime' || element.contentType == 'serialnum'
                          "
                        />
                      </div>

                      <div v-else class="item-value">
                        <span v-if="element.contentType == 'timestamp'">{{
                          element.defaultContent
                        }}</span>
                        <span
                          v-else-if="element.contentType == 'datetime'"
                          :style="{ color: element.content ? ruleValueColor : ruleDefaultColor }"
                          >{{
                            element.content
                              ? loadDateTimeLabel(element.content)
                              : element.defaultContent
                          }}</span
                        >
                        <span
                          v-else-if="element.contentType == 'serialnum'"
                          :style="{ color: element.content ? ruleValueColor : ruleDefaultColor }"
                          >{{
                            element.content
                              ? loadSerialnumLabel(element.content)
                              : element.defaultContent
                          }}</span
                        >
                        <span v-else-if="element.content">{{ element.content }}</span>
                        <span v-else style="color: #999">{{ element.defaultContent }}</span>
                      </div>
                      <div class="no-action">
                        <a-button
                          type="link"
                          v-if="!element.edit && element.contentType !== 'timestamp'"
                          @click="handleEdit(element)"
                          >修改</a-button
                        >
                        <a-button type="link" v-if="element.edit" @click="handleSave(element)"
                          >保存</a-button
                        >
                        <!-- <a-divider type="vertical" />
                        <a-button type="link" v-if="element.edit" @click="handleCnacel(element)">取消</a-button> -->
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </draggable>
          </ul>
        </div>
      </div>
    </a-card>
    <a-card title="主题" size="small">
      <a-radio-group v-model:value="basicInfo.subjectType" class="basci-radio-type">
        <a-radio :value="1">经办人发起时自行填写</a-radio>
        <a-radio :value="2">按照预设的文件主题规则自动生成</a-radio>
      </a-radio-group>
      <div class="line-number-area" v-if="basicInfo.subjectType == '2'">
        <div class="line-number-tip">
          <p style="margin-left: 30px"
            >文件编号按照规则生成的示例：{{ subjectExampleStr }}
            <!-- <span style="font-weight:600">劳动合同-2023年新版</span>_年月日_000001_接收方姓名 -->
          </p>
          <div class="line-number-rule-type" style="margin-left: 30px">
            <span>拼接规则：</span>
            <a-radio-group
              v-model:value="basicInfo.subjectTypeRuleVo.link"
              @change="handleSetSubjectExample"
            >
              <a-radio value="">无符号拼接</a-radio>
              <a-radio value="-">“-”拼接</a-radio>
              <a-radio value="_">“_”拼接</a-radio>
            </a-radio-group>
          </div>
          <ul v-if="basicInfo.subjectType == '2'" class="rule-list">
            <draggable
              :list="
                basicInfo && basicInfo.subjectTypeRuleVo && basicInfo.subjectTypeRuleVo.detailVoList
              "
              ghost-class="ghost"
              chosen-class="chosenClass"
              animation="300"
              @end="handleSetSubjectExample"
            >
              <template
                v-for="(element, index) in basicInfo.subjectTypeRuleVo &&
                basicInfo.subjectTypeRuleVo.detailVoList"
                :key="index"
              >
                <div class="item">
                  <div class="item-li">
                    <a-checkbox
                      v-model:checked="element.checked"
                      @change="handleSetSubjectExample"
                    />
                    <div class="item-li-info">
                      <div class="info-title">
                        <Icon icon="ant-design:holder-outlined" size="18" />
                        <span>{{ element.label }}</span>
                      </div>
                      <div v-if="element.edit" class="item-value">
                        <a-input
                          style="width: 100%"
                          v-model:value="element.content"
                          :placeholder="element.defaultContent"
                          :allowClear="true"
                          v-if="element.contentType == 'text'"
                        />
                        <a-select
                          style="width: 100%"
                          v-model:value="element.content"
                          :allowClear="true"
                          :options="element.options"
                          :placeholder="element.defaultContent"
                          v-if="
                            element.contentType == 'datetime' || element.contentType == 'serialnum'
                          "
                        />
                      </div>
                      <div v-else class="item-value">
                        <span v-if="element.contentType == 'timestamp'">{{
                          element.defaultContent
                        }}</span>
                        <span
                          v-else-if="element.contentType == 'datetime'"
                          :style="{ color: element.content ? ruleValueColor : ruleDefaultColor }"
                          >{{
                            element.content
                              ? loadDateTimeLabel(element.content)
                              : element.defaultContent
                          }}</span
                        >
                        <span
                          v-else-if="element.contentType == 'serialnum'"
                          :style="{ color: element.content ? ruleValueColor : ruleDefaultColor }"
                          >{{
                            element.content
                              ? loadSerialnumLabel(element.content)
                              : element.defaultContent
                          }}</span
                        >
                        <span v-else-if="element.content">{{ element.content }}</span>
                        <span v-else style="color: #999">{{ element.defaultContent }}</span>
                      </div>
                      <div class="no-action">
                        <a-button
                          type="link"
                          v-if="
                            element.contentType != 'business_line_name' &&
                            !element.edit &&
                            element.contentType !== 'timestamp' &&
                            element.contentType !== 'sender_name' &&
                            element.contentType !== 'receiver_name'
                          "
                          @click="handleEdit(element)"
                          >修改</a-button
                        >
                        <a-button type="link" v-if="element.edit" @click="handleSave(element)"
                          >保存</a-button
                        >
                        <!-- <a-divider type="vertical" />
                        <a-button type="link" v-if="element.edit"  @click="handleCnacel(element)">取消</a-button> -->
                      </div>
                    </div>
                  </div>
                </div>
              </template>
            </draggable>
          </ul>
        </div>
      </div>
    </a-card>
    <a-card title="签署方" size="small">
      <a-radio-group v-model:value="basicInfo.signerType" class="basci-radio-type">
        <a-radio :value="1">经办人发起时自行设置</a-radio>
        <a-radio :value="2">经办人发起时，按照预设流程发起并签署</a-radio>
      </a-radio-group>
      <p v-if="basicInfo.signerType == '1'" style="margin-top: 25px; margin-left: 15px"
        >经办人在发起时，自己添加签署方及签署要求</p
      >
      <div>
        <div class="signatory-area" v-if="basicInfo.signerType == '2'">
          <ul>
            <li v-for="(item, index) in signerList" :key="index">
              <div v-if="item.signerType == 1">
                <p class="signatory-count">发起方</p>
                <div class="initiator-info">
                  <div class="initiator-left">
                    <a-tag class="signer-initiator-tag">发起方</a-tag>
                    <!-- <p class="initiator-name weight-label">{{ item.signerName }}</p> -->
                    <div class="initiator-process">
                      <span class="weight-label">签署流程：</span>
                      <div class="initiator-list">
                        <div
                          v-for="(signderItem, senderIndex) in item.senderList"
                          :key="senderIndex"
                          class="initiator-item"
                        >
                          <span class="initiator-item-name">{{ signderItem.senderName }}</span>
                          <a-tooltip>
                            <template #title>
                              <div v-if="signderItem.senderType == 4">
                                <p
                                  >印章：{{
                                    signderItem.senderSealId
                                      ? signderItem.sealName || signderItem.senderSealName
                                      : ''
                                  }}</p
                                >
                                <p
                                  >盖章方式：{{
                                    signderItem.senderSignType == 1 ? '自动盖章' : '手动盖章'
                                  }}</p
                                >
                                <p v-if="signderItem.senderSignType == 2"
                                  >签署人：{{ signderItem.senderUserName }}</p
                                >
                              </div>
                              <div v-else-if="signderItem.senderType == 1">
                                <p>签署人：文档签署的发起人签字 </p>
                              </div>
                              <div v-else>
                                <p>签署人：{{ signderItem.senderUserName }}</p>
                              </div>
                            </template>
                            <Icon icon="ant-design:question-circle-outlined" />
                          </a-tooltip>
                          <div class="arrow-right" v-if="senderIndex + 1 != item.senderList.length">
                            <span class="arrow-line"></span>
                            <Icon icon="ant-design:caret-right-filled" color="#d3d3d3" />
                          </div>
                        </div>
                        <a-button type="link" @click="handleInitiator(item.senderList)"
                          >设置</a-button
                        >
                      </div>
                    </div>
                  </div>
                  <div class="signatory-action">
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
                      style="margin: 0 10px"
                      v-if="index + 1 != signerList.length"
                      @click="handleArrowDown(index)"
                    />
                    <Icon
                      icon="ant-design:delete-outlined"
                      size="20"
                      style="margin: 0 10px"
                      @click="handleDelete(index)"
                    />
                  </div>
                </div>
              </div>
              <div v-if="item.signerType == 2">
                <p class="signatory-count">接收方{{ item.currentIndex }}</p>
                <div class="signatory-item">
                  <div class="signatory-person-info">
                    <!-- <span class="weight-label">个人</span> -->
                    <a-tag class="signer-person-tag">个人</a-tag>
                    <div class="signatory-form flex-col">
                      <p class="flex items-center w-full" style="margin-bottom: 10px">
                        <span>是否允许免意愿快捷签署：</span>
                        <a-tooltip>
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
                        />
                      </p>
                      <a-space class="flex items-center w-full" style="margin-bottom: 10px">
                        <!-- <span>人脸识别校验：</span>
                        <a-switch
                          :checked-value="1"
                          :un-checked-value="0"
                          v-model:checked="item.confirmType"
                        /> -->
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
                          @change="(val) => handleVerifyTypeChange(val, item)"
                        />
                      </a-space>
                      <a-space
                        class="flex items-center w-full"
                        style="margin-bottom: 10px"
                        v-show="authTypeShow && personalSignAuth == 'allowed'"
                      >
                        <span>实名认证要求：</span>
                        <a-radio-group v-model:value="item.personalSignAuth">
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
                  </div>

                  <div class="signatory-action">
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
                  <div class="signatory-ent-info">
                    <!-- <p class="weight-label">企业</p> -->
                    <a-tag class="signer-company-tag">企业</a-tag>
                    <div class="initiator-process">
                      <span class="weight-label">签署流程：</span>
                      <ul class="receive-node">
                        <!-- <li class="initiator-item" v-for="(receiveItem, receiveIndex) in item.senderList" :key="receiveIndex">
                            <span>{{ receiveItem.senderType==1?'经办人签字':'组织签章' }}</span>
                            <Icon icon="ant-design:close-outlined" class="delete-cc-user" @click="handleDeleteEntNode(item,receiveIndex)"/>
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

                        <a-button type="link" @click="handleAcceptInitiator(item)">设置</a-button>
                        <!-- <a-dropdown trigger="hover">
                              <a-button type="link"> <Icon icon="ant-design:plus-outlined" />添加</a-button>
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
                  <div class="signatory-action">
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
          </ul>
        </div>
        <div class="signer-set" v-if="basicInfo.signerType == '2'">
          <div class="set-action">
            <a-button type="link" @click="handleAddInitiator" v-if="isShowInitiatorBtn()">
              <Icon icon="ant-design:plus-circle-outlined" />添加发起方
            </a-button>
            <a-button type="link" @click="handleAddPersonal">
              <Icon icon="ant-design:plus-circle-outlined" />添加个人
            </a-button>
            <a-button type="link" @click="handleAddEnt">
              <Icon icon="ant-design:plus-circle-outlined" />添加企业
            </a-button>
          </div>
          <a-radio-group v-model:value="basicInfo.signOrderType" class="sign-config">
            <a-radio :value="1">顺序签署</a-radio>
            <a-radio :value="2">无序签署</a-radio>
          </a-radio-group>
        </div>
      </div>
    </a-card>
    <a-card size="small" class="cc-card">
      <template #title>
        <span
          >抄送<span style="color: #999; font-size: 12px"
            >（为该业务线设置抄送时机、抄送的用户类型、指定抄送人）</span
          ></span
        >
        <div>
          <span style="color: #999; font-size: 12px; margin-right: 10px"> 关闭后无法抄送</span>
          <a-switch v-model:checked="basicInfo.ccedType" />
        </div>
      </template>
      <BasicForm @register="registerCCForm" v-show="basicInfo.ccedType == true">
        <template #ccerType>
          <div class="ccType-area">
            <div class="ccType-item">
              <a-form-item>
                <a-checkbox v-model:checked="basicInfo.internalCcerType">内部个人用户</a-checkbox>
              </a-form-item>
              <div class="inside-cclist" v-if="basicInfo.internalCcerType">
                <div @click="handleInsideCC" style="margin-top: 5px"
                  ><Icon icon="ant-design:plus-circle-outlined" /><span>指定抄送人</span>
                </div>
                <ul class="ccerList-ul">
                  <template v-for="(item, index) in ccerList" :key="index">
                    <li v-if="item.ccerType == 1">
                      <span>{{ item.internalCcerName }}</span>
                      <Icon
                        icon="ant-design:close-outlined"
                        class="delete-cc-user"
                        @click="handleDeleteCC(index)"
                      />
                    </li>
                  </template>
                </ul>
              </div>
            </div>
            <div class="ccType-item">
              <a-form-item>
                <a-checkbox v-model:checked="basicInfo.externalCcerType">外部个人用户</a-checkbox>
              </a-form-item>
              <div class="inside-cclist" v-if="basicInfo.externalCcerType">
                <div @click="handleOutsideCC" style="margin-top: 5px"
                  ><Icon icon="ant-design:plus-circle-outlined" /><span>指定抄送人</span>
                </div>
                <ul class="ccerList-ul">
                  <template v-for="(item, index) in ccerList" :key="index">
                    <li v-if="item.ccerType == 2">
                      <span>{{
                        item.externalCcerName + '（' + item.externalCcedValue + '）'
                      }}</span>
                      <Icon
                        icon="ant-design:close-outlined"
                        class="delete-cc-user"
                        @click="handleDeleteCC(index)"
                      />
                    </li>
                  </template>
                </ul>
              </div>
            </div>
          </div>
        </template>
      </BasicForm>
    </a-card>
    <a-card size="small" class="sign-file-card">
      <template #title>
        <span
          >签约文件<span style="color: #999; font-size: 12px"
            >（为该业务线设置需要签约的文件）</span
          ></span
        >
        <div class="sign-file-switch">
          <div>
            <span style="color: #999; font-size: 12px"> 发起时允许添加其他的文件/模板</span>
            <a-switch v-model:checked="basicInfo.addFileType" @change="handleChangeAddFile" />
          </div>
          <div>
            <span style="color: #999; font-size: 12px"> 发起时允许删除预设的文件/模板</span>
            <a-switch v-model:checked="basicInfo.deleteFileType" />
          </div>
        </div>
      </template>
      <div class="sign-file-area">
        <div class="signFile-action">
          <div>
            <a-upload
              :accept="buildFileType(isOffice)"
              :showUploadList="false"
              name="file"
              action="/resrun-paas/sign/file/create"
              :headers="uploadHeaders"
              @change="handleChange"
              :disabled="fileList.length >= documentsCeiling"
              :max-count="documentsCeiling"
            >
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
            </a-upload>
          </div>
          <a-divider type="vertical" />
          <div v-if="fileList.length >= documentsCeiling">
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
                >选择公共模板</a-button
              >
            </a-tooltip>
          </div>
          <div v-else>
            <Icon icon="ant-design:reconciliation-outlined" size="16" />
            <a-button type="link" @click="handleChoseTpl">选择公共模板</a-button>
          </div>
        </div>
        <div>
          <p style="color: rgba(0, 0, 0, 0.4); font-size: 12px"
            >文件上传仅支持 {{ buildFileType(isOffice) }} 格式，最多上传{{
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
                <div class="file-item">
                  <div class="file-icon">
                    <a-tag class="file-tag" :color="item.docType == 1 ? '#0079FE' : '#FE9500'"
                      >{{ item.docType == 1 ? '本地文件' : '在线模板'
                      }}<span v-if="item.paramType == 1">（含参数）</span></a-tag
                    >
                    <!-- <Icon icon="ant-design:close-circle-outlined" color="#ed5220" v-if="(basicInfo.deleteFileType==2 && item.originType==1) || item.originType==2" @click.stop="handleDelFile(index)" /> -->
                    <Icon
                      icon="ant-design:close-circle-outlined"
                      color="#ed5220"
                      @click.stop="handleDelFile(index)"
                    />
                    <SvgIcon
                      :name="item.docType == 1 ? 'pdf' : 'tpl'"
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
                          <a-menu-item>
                            <a href="javascript:;" @click="handleDownload({ id: item.annexId })"
                              >下载文档</a
                            >
                          </a-menu-item>
                          <a-menu-item>
                            <a href="javascript:;" @click.stop="handleDelFile(index)">删除文档</a>
                          </a-menu-item>
                          <a-menu-item>
                            <a href="javascript:;" @click="handlePreview(item)">查看</a>
                          </a-menu-item>
                        </a-menu>
                      </template>
                    </a-dropdown>
                  </div>
                </div>
                <div class="file-copy">
                  <div class="copy-text" @click="copyItemFileId(item)">
                    <a-tooltip placement="top">
                      <template #title>用于API对接</template>
                      <div>
                        <span>ID:{{ item.docType == 1 ? item.annexId : item.docOriginId }}</span>
                      </div>
                    </a-tooltip>
                  </div>
                  <div class="copy-icon">
                    <Icon
                      icon="ant-design:copy-outlined"
                      color="rgba(0,0,0,0.4)"
                      @click="copyItemFileId(item)"
                    />
                  </div>
                </div>
              </li>
            </draggable>
          </ul>
        </div>
      </div>
    </a-card>
    <a-card size="small" class="other-file-card">
      <template #title>
        <span
          >附件<span style="color: #999; font-size: 12px"
            >（上传的附件只作为签署文件的补充，只能查看，不需要签署）</span
          ></span
        >
      </template>
      <div class="other-file-switch">
        <span>允许上传附件</span>
        <div>
          <span style="color: #999; font-size: 12px; font-weight: 600; margin-right: 10px"
            >文件发起时，发起人可上传附件</span
          >
          <a-switch v-model:checked="basicInfo.addAnnexType" />
        </div>
      </div>
    </a-card>
    <a-card title="个人签署实名认证要求" size="small" class="sign-ca-card" v-show="authTypeShow">
      <a-radio-group v-model:value="personalSignAuth">
        <a-radio :style="radioStyle" value="required">
          须实名认证
          <a-tooltip>
            <template #title>
              须实名认证：【强烈建议】使用个人电子签章前，必须完成实名认证，符合电子签名的合法性与安全性要求
            </template>
            <Icon icon="ant-design:question-circle-outlined" />
          </a-tooltip>
        </a-radio>
        <a-radio :style="radioStyle" value="allowed"
          >允许不实名认证
          <a-tooltip>
            <template #title
              >允许不实名认证：使用个人电子签章前，允许不进行实名认证。可在个人签署节点中指定具体的认证要求</template
            >
            <Icon icon="ant-design:question-circle-outlined" /> </a-tooltip
        ></a-radio>
        <a-radio :style="radioStyle" value="not_required">
          无需实名认证
          <a-tooltip>
            <template #title>无需实名认证：使用个人电子签章前，无需进行实名认证</template>
            <Icon icon="ant-design:question-circle-outlined" />
          </a-tooltip>
        </a-radio>
      </a-radio-group>
    </a-card>
    <!-- 弹窗确认组件 -->
    <a-modal
      v-model:visible="confirmModalVisible"
      title="非实名认证签署风险告知书"
      @ok="confirmAuthTypeChange"
      @cancel="cancelAuthTypeChange"
      okText="同意"
      cancelText="拒绝"
      width="600px"
    >
      <div class="confirm-modal-content">
        <p class="modal-title">尊敬的用户：</p>
        <p class="modal-text">
          为确保电子签名的合法性与安全性，在本平台使用个人电子签章前，必须完成实名认证。
        </p>
        <p class="modal-text">
          如您关闭实名认证要求，用户在签署时将不再进行实名认证。系统将自动使用平台防篡改证书（非CA权威数字证书）完成签名。该证书仅能保障文件在签名后不被篡改，不具备《电子签名法》所规定的法律效力。
        </p>
        <p class="modal-title">
          特别声明：建议您在确保掌握签署人真实身份的前提下，再关闭个人签署的实名认证要求。如因关闭该功能引发任何纠纷或损失，相关责任均由您所在的公司承担，与开放签（北京资源律动科技有限公司）无关。
        </p>
        <p class="modal-text modal-warning">
          若点击同意按钮，则代表您已阅读并知晓非实名认证签署的风险
        </p>
      </div>
    </a-modal>
    <!-- <a-card title="签署方式" size="small" class="sign-ca-card">
      <a-radio-group v-model:value="basicInfo.caSignType">
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
    </a-card> -->
    <SignerModal @register="register" @saveBsicData="saveBsicData" @success="handleSetSigner" />
    <FileCCModal @register="registerCC" @success="handleCCSuccess" />
    <TemplateModal @register="registerTpl" @success="handleTplSuccess" />
    <PreviewModal @register="registerPreview" />
    <AcceptModal @register="registerAccept" @success="handleAcceptSuccess" />
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, reactive, unref, onMounted, watch } from 'vue';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { useModal } from '/@/components/Modal';
  import { getToken } from '/@/utils/auth';
  // import { getBusinessLineBasicInfo } from '/@/api/businessLine';
  import { BasicForm, useForm } from '/@/components/Form';
  import { VueDraggableNext } from 'vue-draggable-next';
  import { ccFormSchema } from '../data';
  import { useUserStore } from '/@/store/modules/user';
  import { useRouter } from 'vue-router';
  import { cloneDeep } from 'lodash-es';
  import { getAppEnvConfig } from '/@/utils/env';
  import SignerModal from '/@/views/businessLine/modal/SignerModal.vue';
  import FileCCModal from '/@/views/businessLine/modal/FileCCModal.vue';
  import TemplateModal from '/@/views/businessLine/modal/TemplateModal.vue';
  import PreviewModal from '/@/views/businessLine/modal/PreviewModal.vue';
  import AcceptModal from '/@/views/businessLine/modal/AcceptModal.vue';

  import { handleDownload, uuid, copyClipboard } from '/@/utils';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Loading } from '/@/components/Loading';
  import { subjectData, codeData } from '/@/views/businessLine/components/data/OrderNumber';

  import { getBusinessLineInfoAll, setBusinessLineBasic } from '/@/api/businessLine';
  import { getSealInfo } from '/@/api/contract';

  import { getSignConfirm, getPlatePersonalSignAuth } from '/@/api/sys/common';
  import { getSystemLimit } from '/@/api/license';
  import { getUploadFileType, buildFileType } from '/@/api';

  export default defineComponent({
    name: 'BusinessBasic',
    components: {
      Icon,
      SignerModal,
      draggable: VueDraggableNext,
      BasicForm,
      SvgIcon,
      FileCCModal,
      TemplateModal,
      PreviewModal,
      Loading,
      AcceptModal,
    },
    props: {
      lineFormData: {
        type: Object,
      },
      businessId: {
        type: String,
      },
    },
    setup(props, { emit }) {
      console.log(props);
      const state = reactive({
        //需要拖拽的数据，拖拽后数据的顺序也会变化
        list: [
          { name: 'www.itxst.com', id: 0 },
          { name: 'www.baidu.com', id: 1 },
          { name: 'www.google.com', id: 2 },
        ],
      });
      const confirmOptions = ref([
        { label: '验证码', value: 'CAPTCHA' },
        { label: '签署密码', value: 'PASSWORD' },
        { label: '双重校验（验证码 + 签署密码）', value: 'DOUBLE' },
        { label: '人脸识别', value: 'FACE', disabled: true },
      ]);
      const signerList: any = ref([]);
      const ccerList: any = ref([]);
      const fileList: any = ref([]);
      const loading = ref(false);
      const radioStyle = reactive({
        marginBottom: '15px',
      });
      const uploadLoading = ref(false);
      const ruleDefaultColor = ref('#999');
      const ruleValueColor = ref('#000');
      const codeExampleStr = ref('KFQ-年月日-000001');
      const subjectExampleStr = ref('开放签-劳动合同-2023年新版-年月日-000001-接收方姓名');

      // 定义实名认证要求的常量
      // const required = 'required';
      // const allowed = 'allowed';
      // const not_required = 'not_required';

      // 添加弹窗相关的响应式变量
      const confirmModalVisible = ref(false);
      // 添加用于保存原始认证类型的变量
      const originalPendingAuthType = ref('');

      const personalSignAuth = ref('');

      // 添加认证要求是否可见可变更的类型变量
      const authTypeShow = ref(false);

      const basicInfo: any = ref({
        name: '',
        addCcerType: false,
        signOrderType: 1,
        caSignType: 1,
        addAnnexType: false,
        internalCcerType: false,
        externalCcerType: false,
        addFileType: false,
        deleteFileType: false,
        codeType: 1,
        subjectType: 1,
        codeTypeRuleVo: {
          ruleType: 1,
          id: '',
          link: '-',
          detailVoList: cloneDeep(codeData),
        },
        subjectTypeRuleVo: {
          ruleType: 1,
          id: '',
          link: '-',
          detailVoList: cloneDeep(subjectData),
        },
      });
      const { VITE_GLOB_APP_CODE } = getAppEnvConfig();
      const nameEdit = ref(false);
      const userStore = useUserStore();
      const userInfo = userStore.getUserInfo;

      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const query = route.query;
      const signReId = query.signReId;
      const signConfirm = ref(false);
      const uploadHeaders = reactive({
        'X-Access-Token': getToken(),
        'resrun-app-code': VITE_GLOB_APP_CODE,
      });

      const { createMessage: msg } = useMessage();

      const [register, { openModal }] = useModal();
      const [registerAccept, { openModal: openAcceptModal }] = useModal();

      const [registerCC, { openModal: openCCModal }] = useModal();
      const [registerTpl, { openModal: openTplModal }] = useModal();
      const [registerPreview, { openModal: openPreviewModal }] = useModal();

      const [registerCCForm, { setFieldsValue, getFieldsValue }] = useForm({
        labelWidth: 190,
        labelAlign: 'left',
        schemas: ccFormSchema,
        showActionButtonGroup: false,
      });

      // 监听 personalSignAuth 的变化
      watch(
        () => personalSignAuth.value,
        (newVal, oldVal) => {
          // 只有在原始值未设置时才保存，确保保存的是最初的值
          if (!originalPendingAuthType.value && oldVal) {
            originalPendingAuthType.value = oldVal;
          }

          // 如果用户尝试从 required 改为 allowed 或 not_required，则显示确认模态框
          if (oldVal === 'required' && (newVal === 'allowed' || newVal === 'not_required')) {
            confirmModalVisible.value = true;
          } else if (oldVal && newVal && oldVal !== 'required' && newVal === 'required') {
            // 如果用户从 allowed 或 not_required 改回 required，则重置原始值
            resetOriginalPendingAuthType();
          }

          // 当 personalSignAuth 为 'required' 或 'not_required' 时，
          // 同步更新 signerList 及其 senderList 中的认证要求
          if (newVal === 'required' || newVal === 'not_required') {
            // 更新 signerList 中的每个对象
            signerList.value.forEach((signer: any) => {
              // 更新 signer 对象本身的认证要求
              if (Object.prototype.hasOwnProperty.call(signer, 'personalSignAuth')) {
                signer.personalSignAuth = newVal;
              }

              // 更新 senderList 列表中的每个对象
              if (signer.senderList && Array.isArray(signer.senderList)) {
                signer.senderList.forEach((sender: any) => {
                  if (Object.prototype.hasOwnProperty.call(sender, 'personalSignAuth')) {
                    sender.personalSignAuth = newVal;
                  }
                });
              }
            });
          }
        },
      );

      onMounted(() => {
        getContractReInfo();
        initSysLimit();
        console.log(basicInfo.value.name, '业务线名称');
      });
      const documentsCeiling = ref(1);
      const isOffice = ref(false);
      async function initSysLimit() {
        const limit = await getSystemLimit();

        documentsCeiling.value = limit.documentsCeiling;
        const office = await getUploadFileType();
        isOffice.value = office.result;
      }
      async function getContractReInfo() {
        let result = await getBusinessLineInfoAll({ signReId: signReId });
        if (result) {
          //接口数据遍历
          //  let sortCodeRules = [];
          if (!result.baseVo.codeTypeRuleVo.link) {
            result.baseVo.codeTypeRuleVo.link = '-';
          }
          if (!result.baseVo.subjectTypeRuleVo.link) {
            result.baseVo.subjectTypeRuleVo.link = '-';
          }
          if (result.baseVo.codeTypeRuleVo && result.baseVo.codeTypeRuleVo.detailVoList) {
            // 遍历 array2 并修改 checked 属性
            const codeRules = result.baseVo.codeTypeRuleVo.detailVoList;
            basicInfo.value.codeTypeRuleVo.detailVoList.forEach((item) => {
              for (var i = 0; i < codeRules.length; i++) {
                const apiItem = codeRules[i];
                if (item.contentType === apiItem.contentType) {
                  item.checked = true;
                  item.content = apiItem.content;
                  break;
                } else {
                  item.checked = false;
                }
              }
            });
          }
          if (result.baseVo.codeTypeRuleVo?.detailVoList) {
            basicInfo.value.codeTypeRuleVo.detailVoList.push(
              ...result.baseVo.codeTypeRuleVo.detailVoList.filter(
                (apiItem) =>
                  !basicInfo.value.codeTypeRuleVo.detailVoList.some(
                    (originalItem) => originalItem.contentType === apiItem.contentType,
                  ),
              ),
            );
            basicInfo.value.codeTypeRuleVo.detailVoList.sort((a, b) => {
              // 将空值或 null 视为最大值，使其排在最后
              const orderA = a.contentOrder !== null ? a.contentOrder : Infinity;
              const orderB = b.contentOrder !== null ? b.contentOrder : Infinity;
              return orderA - orderB;
            });
          }
          if (result.baseVo.subjectTypeRuleVo && result.baseVo.subjectTypeRuleVo.detailVoList) {
            // 遍历 array2 并修改 checked 属性
            const subjectRules = result.baseVo.subjectTypeRuleVo.detailVoList;
            basicInfo.value.subjectTypeRuleVo.detailVoList.forEach((item) => {
              for (var i = 0; i < subjectRules.length; i++) {
                const apiItem = subjectRules[i];
                if (item.contentType === apiItem.contentType) {
                  item.checked = true;
                  item.content = apiItem.content;
                  break;
                } else {
                  item.checked = false;
                }
              }
            });
          }
          if (result.baseVo.subjectTypeRuleVo?.detailVoList) {
            basicInfo.value.subjectTypeRuleVo.detailVoList.push(
              ...result.baseVo.subjectTypeRuleVo.detailVoList.filter(
                (apiItem) =>
                  !basicInfo.value.subjectTypeRuleVo.detailVoList.some(
                    (originalItem) => originalItem.contentType === apiItem.contentType,
                  ),
              ),
            );
            basicInfo.value.subjectTypeRuleVo.detailVoList.sort((a, b) => {
              // 将空值或 null 视为最大值，使其排在最后
              const orderA = a.contentOrder !== null ? a.contentOrder : Infinity;
              const orderB = b.contentOrder !== null ? b.contentOrder : Infinity;
              return orderA - orderB;
            });
          }

          basicInfo.value.subjectTypeRuleVo.detailVoList.forEach((m) => {
            if (m.contentType == 'business_line_name') {
              m.content = result.baseVo.name;
            }
          });

          // 添加平台和业务线个人签署认证要求的判断逻辑
          let platePersonSignAuth = '';
          const personalSignAuthRes = await getPlatePersonalSignAuth();
          if (personalSignAuthRes.code == 200) {
            platePersonSignAuth = personalSignAuthRes.result.personalSignAuthType || 'required';
          }
          personalSignAuth.value = result.baseVo.personalSignAuth || 'required';
          // 根据平台认证要求设置业务线认证要求和显示状态
          switch (platePersonSignAuth) {
            case 'required':
              authTypeShow.value = false;
              personalSignAuth.value = 'required';
              break;
            case 'not_required':
              authTypeShow.value = false;
              personalSignAuth.value = 'not_required';
              break;
            case 'allowed':
              authTypeShow.value = true;
              // 保持 personalSignAuth.value  为 result.baseVo.personalSignAuth || 'required'
              break;
            default:
              authTypeShow.value = false;
              personalSignAuth.value = 'required';
          }

          basicInfo.value = {
            ...result.baseVo,
            codeType: result.baseVo.codeType || 1,
            subjectType: result.baseVo.subjectType || 1,
            signerType: result.baseVo.signerType || 1,
            addFileType:
              result.baseVo.addFileType == null
                ? true
                : result.baseVo.addFileType == 1
                ? true
                : false,
            deleteFileType: result.baseVo.deleteFileType == 1 ? true : false,
            internalCcerType: result.baseVo.internalCcerType == 1 ? true : false,
            externalCcerType: result.baseVo.externalCcerType == 1 ? true : false,
            addAnnexType: result.baseVo.addAnnexType == 1 ? true : false,
            ccedType: result.baseVo.ccedType == 1 ? true : false,
            subjectTypeRuleVo: {
              ...result.baseVo.subjectTypeRuleVo,
              detailVoList: basicInfo.value.subjectTypeRuleVo.detailVoList,
              link: result.baseVo?.subjectTypeRuleVo ? result.baseVo?.subjectTypeRuleVo?.link : '_',
            },
            codeTypeRuleVo: {
              ...result.baseVo.codeTypeRuleVo,
              detailVoList: basicInfo.value.codeTypeRuleVo.detailVoList,
              link: result.baseVo?.codeTypeRuleVo ? result.baseVo?.codeTypeRuleVo?.link : '_',
            },
          };
          setFieldsValue({
            ...result.baseVo,
            addCcerType: result.baseVo.addCcerType == 1 ? true : false,
          });
          signerList.value = decodeSignerList(result.signerList);
          ccerList.value = result.ccerList;
          fileList.value = result.fileList;
          // basicInfo.value.internalCcerType = result.ccerList.filter(v=>v.ccerType==1).length?true:false;
          // basicInfo.value.externalCcerType = result.ccerList.filter(v=>v.ccerType==2).length?true:false;
          signerList.value = result.signerList.sort((a, b) => a.signerOrder - b.signerOrder);
          if (!result.signerList.length) {
            handleAddInitiator();
          }
          signerList.value.map((item) => {
            // 未设置认证要求数据时，设置默认数据
            if (!item.personalSignAuth) {
              item.personalSignAuth =
                basicInfo.value.personalSignAuth === 'not_required' ? 'not_required' : 'required';
            }
            if (!item.senderList) {
              item.senderList = [];
            }
            if (item.senderList && Array.isArray(item.senderList)) {
              item.senderList.map((v) => {
                // 未设置认证要求数据时，设置默认数据
                if (!v.personalSignAuth) {
                  v.personalSignAuth =
                    basicInfo.value.personalSignAuth === 'not_required'
                      ? 'not_required'
                      : 'required';
                }
              });
            }

            if (item.signerType == 1) {
              item.senderList.map((v) => {
                if (v.senderType == 4) {
                  loadSealName(v);
                }
              });
              item.senderList = item.senderList.sort((a, b) => a.senderOrder - b.senderOrder);
            }
          });

          loadSignerIndex();

          // if (
          //   result.baseVo.codeTypeRuleVo &&
          //   result.baseVo.codeTypeRuleVo.detailVoList &&
          //   result.baseVo.codeTypeRuleVo.detailVoList.length
          // ) {
          //   handleSetCodeExample();
          // }
          // if (
          //   result.baseVo.subjectTypeRuleVo &&
          //   result.baseVo.codeTypeRuleVo.detailVoList &&
          //   result.baseVo.subjectTypeRuleVo.detailVoList.length
          // ) {
          //   handleSetSubjectExample();
          // }
          //初始化规则示例 无需判断直接计算
          handleSetSubjectExample();
          handleSetCodeExample();
          const res = await getSignConfirm();
          if (res.code == 200) {
            signConfirm.value = res.result;
          }
          console.log('signConfirmRes', signConfirm.value);
          emit('change', {
            name: basicInfo.value.name,
            errorStatus: basicInfo.value.errorStatus,
            fileList: fileList.value,
          });
          // setApproveFieldsValue({caSignType:result.baseVo.caSignType});
          // setSignerFieldsValue({signOrderType:result.baseVo.signOrderType});
          // if(result.fileList){
          //   setFieldsValue({
          //     fileList:result.fileList
          //   });
          //   // clearValidate(['fileList'])
          // }
          // clearValidate()
        }
      }
      // async function fetch() {
      //   let result = await getBusinessLineBasicInfo({ businessId: props.businessId });
      //   if (result) {
      //     // basicInfo.value = result;
      //   }
      // }
      //签章名称
      async function loadSealName(row) {
        let result: any = await getSealInfo({ sealId: row.senderSealId });
        if (result) {
          row.sealName = result?.sealName;
        }
      }
      //发起方签署设置
      function handleInitiator(senderList) {
        openModal(true, {
          isUpdate: false,
          record: {
            senderList: senderList,
            signConfirm: signConfirm.value,
            signReId: unref(signReId),
            personalSignAuth: personalSignAuth.value,
          },
        });
      }
      function handleAcceptInitiator(acceptItem) {
        openAcceptModal(true, {
          isUpdate: false,
          record: {
            data: acceptItem,
            signConfirm: signConfirm.value,
            personalSignAuth: personalSignAuth.value,
            // signReId: unref(signReId),
          },
        });
        // console.log("handleAcceptInitiator",senderList);
      }
      //模板弹框
      function handleChoseTpl() {
        openTplModal(true, {
          isUpdate: false,
          record: {},
        });
      }
      //规则日期
      function loadDateTimeLabel(content) {
        if (content) {
          let matchRule = subjectData.filter((v) => v.contentType == 'datetime')[0];
          let matchRuleOption =
            matchRule.options && matchRule.options.filter((m) => m.value == content)[0];
          return matchRuleOption && matchRuleOption.label;
        }
      }
      //流水号
      function loadSerialnumLabel(content) {
        if (content) {
          let matchRule = subjectData.filter((v) => v.contentType == 'serialnum')[0];
          let matchRuleOption =
            matchRule.options && matchRule.options.filter((m) => m.value == content)[0];
          return matchRuleOption && matchRuleOption.label;
        }
      }
      //编号规则实时更新
      function handleSetCodeExample() {
        codeExampleStr.value = '';
        let codeRules = cloneDeep(basicInfo.value.codeTypeRuleVo.detailVoList);
        let strArr: string[] = [];
        codeRules.map((m) => {
          if (m.checked) {
            if (m.contentType == 'datetime') {
              let matchRule = subjectData.filter((v) => v.contentType == 'datetime')[0];
              let matchRuleOption =
                matchRule.options && matchRule.options.filter((v) => v.value == m.content);
              if (matchRuleOption && matchRuleOption.length) {
                m.content = matchRuleOption[0].label.split('：')[1];
              }
            }
            if (m.contentType == 'serialnum') {
              let matchRule = subjectData.filter((v) => v.contentType == 'serialnum')[0];
              let matchRuleOption =
                matchRule.options && matchRule.options.filter((v) => v.value == m.content);
              if (matchRuleOption && matchRuleOption.length) {
                m.content = matchRuleOption[0].label.split('：')[1];
              }
            }
            if (m.contentType == 'timestamp') {
              let matchRule = subjectData.filter((v) => v.contentType == 'timestamp');
              if (matchRule.length) {
                m.content = matchRule[0].defaultContent.split('：')[1];
              }
            }
            strArr.push(m.content);
          }
        });
        // 使用join方法以指定字符拼接字符串
        codeExampleStr.value = strArr.join(basicInfo.value.codeTypeRuleVo.link);
        console.log(codeExampleStr.value, '编号回显');
      }
      //主题规则示例实时更新
      function handleSetSubjectExample() {
        subjectExampleStr.value = '';
        let codeRules = cloneDeep(basicInfo.value.subjectTypeRuleVo.detailVoList);
        let strArr: string[] = [];
        codeRules.map((m) => {
          if (m.checked) {
            if (m.contentType == 'datetime') {
              let matchRule = subjectData.filter((v) => v.contentType == 'datetime')[0];
              let matchRuleOption =
                matchRule.options && matchRule.options.filter((v) => v.value == m.content);
              if (matchRuleOption && matchRuleOption.length) {
                m.content = matchRuleOption[0].label.split('：')[1];
              }
            }
            if (m.contentType == 'serialnum') {
              let matchRule = subjectData.filter((v) => v.contentType == 'serialnum')[0];
              let matchRuleOption =
                matchRule.options && matchRule.options.filter((v) => v.value == m.content);
              if (matchRuleOption && matchRuleOption.length) {
                m.content = matchRuleOption[0].label.split('：')[1];
              }
            }
            if (m.contentType == 'timestamp') {
              let matchRule = subjectData.filter((v) => v.contentType == 'timestamp');
              if (matchRule.length) {
                m.content = matchRule[0].defaultContent.split('：')[1];
              }
            }
            strArr.push(m.content);
          }
        });
        subjectExampleStr.value = strArr.join(basicInfo.value.subjectTypeRuleVo.link);
      }
      //预览
      function handlePreview(item) {
        console.log(item, 'dsdfdf');
        openPreviewModal(true, {
          isUpdate: false,
          record: {
            docId: (item.annexId || item.docOriginId) as string,
            docName: item.name,
            docType: item.docType,
          },
        });
      }
      //选择模板
      function handleTplSuccess(list) {
        list.forEach((item) => {
          fileList.value.push({
            annexId: item.annexId,
            docType: 2,
            name: item.templateName,
            originType: 2,
            id: '',
            docOriginId: item.id,
            paramType: item.templateType,
          });
        });
      }
      function handleChange(info) {
        if (info.file.status == 'uploading') {
          uploadLoading.value = true;
        }
        if (
          info.file.status == 'done' ||
          info.file.status == 'error' ||
          info.file.status == 'success' ||
          info.file.status == 'removed'
        ) {
          uploadLoading.value = false;
        }
        if (info.file?.response?.code == 200) {
          const index = info.file.name.lastIndexOf('\.');
          const fileName = info.file.name.substring(index, -1);
          fileList.value.push({
            annexId: info.file.response.result,
            docType: 1,
            name: fileName,
            originType: 2,
            id: '',
            docOriginId: '',
          });
          emit('change', {
            name: basicInfo.value.name,
            errorStatus: basicInfo.value.errorStatus,
            fileList: fileList.value,
          });
        }
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
      //设置排序
      function setSignerOrder() {
        signerList.value.map((item, index) => {
          item.signerOrder = index + 1;
          item.senderList &&
            item.senderList.forEach((v, i) => {
              v.signerOrder = i + 1;
            });
        });
        loadSignerIndex();
      }
      //是否显示添加发起方按钮
      function isShowInitiatorBtn() {
        let result = signerList.value.filter((item) => item.signerType == 1);
        return result.length ? false : true;
      }
      //删除签约文件
      function handleDelFile(index) {
        fileList.value.splice(index, 1);
        if (!fileList.value.length) {
          basicInfo.value.addFileType = true;
        }
        emit('change', {
          name: basicInfo.value.name,
          errorStatus: basicInfo.value.errorStatus,
          fileList: fileList.value,
        });
      }
      //删除签约方
      function handleDelete(index) {
        signerList.value.splice(index, 1);
        loadSignerIndex();
      }
      //删除抄送人
      function handleDeleteCC(index) {
        ccerList.value.splice(index, 1);
      }
      //添加签署方
      function handleAddInitiator() {
        signerList.value.unshift({
          signerName: userInfo.loginDepartName,
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
          senderOrder: row.senderList.length + 1,
          senderName: type == 1 ? '经办人签字' : '组织签章',
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
      function loadSignerIndex() {
        let list = signerList.value.filter((m) => m.signerType !== 1);
        console.log(signerList, list, '接收方---');
        list.map((v, i) => {
          v.currentIndex = i + 1;
        });
      }
      //是否允许添加文件、模板
      function handleChangeAddFile(val) {
        console.log(val, '开关文件');
        if (!val && !fileList.value.length) {
          msg.warning('未添加签约文件，该项不可关闭');
          basicInfo.value.addFileType = true;
        }
      }
      //添加抄送人
      function handleInsideCC() {
        openCCModal(true, {
          isUpdate: false,
          record: {
            ccerType: 1,
          },
        });
      }
      //添加外部抄送人
      function handleOutsideCC() {
        openCCModal(true, {
          isUpdate: false,
          record: {
            ccerType: 2,
          },
        });
      }
      function handleAcceptSuccess(result, recordId) {
        console.log(signerList.value, recordId);
        signerList.value.forEach((item) => {
          if (item.id === recordId || item.uid === recordId) {
            item.senderList = result;
          }
        });
        console.log('add result:', result, recordId, signerList.value);
      }
      //添加抄送人
      function handleCCSuccess(list, type) {
        console.log(list, type, '抄送人=====');
        ccerList.value = [...ccerList.value, ...list];
      }
      function handleEditLineName() {
        if (nameEdit.value && !basicInfo.value.name) {
          msg.warning('业务线名称不能为空');
          return;
        }
        nameEdit.value = !nameEdit.value;
      }
      function handleNameChange(e) {
        basicInfo.value.subjectTypeRuleVo.detailVoList.forEach((m) => {
          if (m.contentType == 'business_line_name') {
            m.content = e.target.value;
          }
        });
        emit('change', { name: e.target.value });
      }
      function getFormData() {
        return basicInfo.value;
      }

      function handleSetSigner(list) {
        signerList.value.map((item) => {
          if (item.signerType == 1) {
            item.senderList = cloneDeep(list);
            item.senderList.forEach((v) => {
              if (v.senderType == 4) {
                // v.senderSignType = v.senderSignType==false?2:1;
                v.senderSealName = v.sealName;
              }
            });
          }
        });
        signerList.value = signerList.value;
        console.log('send', list);
      }
      function handleEdit(element) {
        element.edit = true;
      }
      function handleSave(element) {
        if (!element.content) {
          msg.warning('该项不能为空');
          return;
        }
        element.edit = false;
        if (element.checked) {
          handleSetSubjectExample();
          handleSetCodeExample();
        }
      }
      function handleCnacel(element) {
        element.edit = false;
      }
      function saveBsicData(isTabCall) {
        return new Promise(async (resolve, reject) => {
          let codeTypeRuleVoList = cloneDeep(basicInfo.value.codeTypeRuleVo.detailVoList).filter(
            (v) => v.checked == true,
          );
          let subjectTypeRuleVoList = cloneDeep(
            basicInfo.value.subjectTypeRuleVo.detailVoList,
          ).filter((v) => v.checked == true);
          let codeRight = true;
          let subjectRight = true;
          let signerReceiveEntNodes = 1; //接收方企业内部签署节点
          codeTypeRuleVoList.some((v, i) => {
            v.contentOrder = i + 1;
            v.options = undefined;
            v.edit = undefined;
            v.label = undefined;
            v.checked = undefined;
            v.defaultContent = undefined;
            if (v.contentType == 'serialnum') {
              v.contentLength = Number(v.content);
            }
            if (!v.content && v.contentType !== 'timestamp') {
              codeRight = false;

              return false;
            }
          });
          subjectTypeRuleVoList.some((v, i) => {
            v.contentOrder = i + 1;
            v.options = undefined;
            v.edit = undefined;
            v.label = undefined;
            v.checked = undefined;
            v.defaultContent = undefined;
            if (v.contentType == 'serialnum') {
              v.contentLength = Number(v.content);
            }
            if (
              !v.content &&
              v.contentType !== 'timestamp' &&
              v.contentType !== 'sender_name' &&
              v.contentType !== 'receiver_name'
            ) {
              subjectRight = false;
              return false;
            }
          });
          codeTypeRuleVoList.sort((a, b) => a.contentOrder - b.contentOrder);
          subjectTypeRuleVoList.sort((a, b) => a.contentOrder - b.contentOrder);
          console.log('333333333333333333', signerList.value);
          signerList.value.some((item) => {
            if (item.signerType == 3) {
              signerReceiveEntNodes = item.senderList ? item.senderList.length : 0;
              if (!signerReceiveEntNodes) {
                return false;
              }
            }
          });
          if (!basicInfo.value.name) {
            msg.warning('业务线名称不能为空');
            return;
          }
          if (basicInfo.value.codeTypeRuleVo.link == undefined && basicInfo.value.codeType == 2) {
            msg.warning('编号拼接规则未指定');
            return;
          }

          if (
            basicInfo.value.subjectTypeRuleVo.link == undefined &&
            basicInfo.value.subjectType == 2
          ) {
            msg.warning('主题拼接规则未指定');
            return;
          }
          if (!codeRight && basicInfo.value.codeType == 2) {
            msg.warning('单号规则有未填写项');
            return;
          }
          //校验
          if (!subjectRight && basicInfo.value.subjectType == 2) {
            msg.warning('主题规则有未填写项');
            return;
          }
          if (!signerReceiveEntNodes) {
            msg.warning('接收方企业必须添加签署节点');
            return reject('no_receive_ent');
          }
          if (basicInfo.value.signerType == 2 && !basicInfo.value.signOrderType) {
            msg.warning('请指定签署顺序');
            return;
          }

          if (!basicInfo.value.caSignType) {
            msg.warning('请指定签署方式');
            return;
          }
          let ccData = getFieldsValue();
          fileList.value.map((v, i) => {
            v.docOrder = i + 1;
          });
          let params = {
            baseVo: {
              signReId,
              ...basicInfo.value,
              ccedOpportunityType: basicInfo.value.addCcerType ? ccData.ccedOpportunityType : 2,
              ccedType: basicInfo.value.ccedType == true ? 1 : 2,
              addCcerType: ccData.addCcerType == true ? 1 : 2,
              addFileType: basicInfo.value.addFileType == true ? 1 : 2,
              deleteFileType: basicInfo.value.deleteFileType == true ? 1 : 2,
              addAnnexType: basicInfo.value.addAnnexType == true ? 1 : 2,
              internalCcerType: basicInfo.value.internalCcerType == true ? 1 : 2,
              externalCcerType: basicInfo.value.externalCcerType == true ? 1 : 2,
              codeTypeRuleVo:
                basicInfo.value.codeType == 2
                  ? {
                      detailVoList: codeTypeRuleVoList,
                      link: basicInfo.value.codeTypeRuleVo.link,
                      id: basicInfo.value.codeTypeRuleVo?.id,
                      ruleType: 1,
                    }
                  : {},
              subjectTypeRuleVo:
                basicInfo.value.subjectType == 2
                  ? {
                      detailVoList: subjectTypeRuleVoList,
                      link: basicInfo.value.subjectTypeRuleVo.link,
                      id: basicInfo.value.subjectTypeRuleVo?.id,
                      ruleType: 2,
                    }
                  : {},
              personalSignAuth: personalSignAuth.value,
            },
            fileList: fileList.value,
            signerList: cloneSignerList(),
            ccerList: unref(ccerList),
          };
          // cloneSignerList();
          loading.value = false;
          let result = await setBusinessLineBasic({ ...params });
          if (result) {
            if (!isTabCall) {
              loading.value = false;
              msg.success('基本信息保存成功');
              getContractReInfo();
            }
            resolve(result);
          } else {
            loading.value = false;
            reject(result);
          }
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
      function copyItemFileId(fileItem: any) {
        const text = fileItem.docType == 1 ? fileItem.annexId : fileItem.docOriginId;
        copyClipboard(text);
        msg.success('复制成功');
      }
      // 添加验证签署意愿验证方式的函数
      function handleVerifyTypeChange(value: string[], item: any) {
        // 当用户尝试取消所有选项时给出提示并阻止操作
        if (value.length === 0) {
          msg.warning('签署意愿验证方式至少需要选中一个选项');
          return;
        }
        // 只有在有有效值时才更新 verifyType
        item.verifyType = value;
      }

      // 重置原始认证类型值
      function resetOriginalPendingAuthType() {
        originalPendingAuthType.value = '';
      }

      // 确认变更认证类型
      function confirmAuthTypeChange() {
        // 用户确认变更，关闭模态框
        confirmModalVisible.value = false;
        // 重置原始值
        resetOriginalPendingAuthType();
      }

      // 取消变更认证类型
      function cancelAuthTypeChange() {
        // 恢复原来的值
        if (originalPendingAuthType.value) {
          personalSignAuth.value = originalPendingAuthType.value;
        }
        confirmModalVisible.value = false;
        // 重置原始值
        resetOriginalPendingAuthType();
      }

      return {
        basicInfo,
        nameEdit,
        handleEditLineName,
        handleNameChange,
        getFormData,
        register,
        handleChoseTpl,
        registerPreview,
        state,
        handleSetSigner,
        handleEdit,
        handleSave,
        registerCCForm,
        registerCC,
        signerList,
        handleInitiator,
        handleArrowUp,
        handleArrowDown,
        handleDelete,
        handleAddInitiator,
        handleAddPersonal,
        handleAddEnt,
        ccerList,
        handleDeleteCC,
        handleInsideCC,
        handleOutsideCC,
        handleCCSuccess,
        uploadHeaders,
        registerTpl,
        handleTplSuccess,
        handleChange,
        handlePreview,
        radioStyle,
        handleDownload,
        handleDelFile,
        isShowInitiatorBtn,
        saveBsicData,
        fileList,
        loadSealName,
        handleCnacel,
        getContractReInfo,
        loading,
        uploadLoading,
        handleChangeAddFile,
        handleSetCodeExample,
        handleSetSubjectExample,
        codeExampleStr,
        subjectExampleStr,
        loadDateTimeLabel,
        ruleDefaultColor,
        ruleValueColor,
        loadSerialnumLabel,
        handleAddEntNode,
        canAddEbtNode,
        handleDeleteEntNode,
        registerAccept,
        handleAcceptSuccess,
        handleAcceptInitiator,
        signConfirm,
        documentsCeiling,
        copyItemFileId,
        isOffice,
        buildFileType,
        confirmOptions,
        handleVerifyTypeChange,
        // 返回实名认证要求的常量
        // required,
        // allowed,
        // not_required,
        confirmModalVisible,
        originalPendingAuthType,
        resetOriginalPendingAuthType,
        confirmAuthTypeChange,
        cancelAuthTypeChange,
        authTypeShow,
        personalSignAuth,
      };
    },
  });
</script>

<style lang="less" scoped>
  @import '../index.less';
  .line-header {
    display: flex;
    align-items: center;

    :deep(.app-iconify) {
      cursor: pointer;
    }

    .line-header-title {
      font-size: 16px;

      p {
        margin-bottom: 0;
        margin-right: 10px;
        font-weight: 550;
      }

      :deep(.ant-input) {
        width: 250px;
        margin-right: 10px;
      }
    }
  }

  .basci-radio-type {
    font-weight: 550;
  }

  .line-number-area {
    padding: 25px;

    .line-number-tip,
    :deep(.ant-radio-wrapper) {
      font-size: 12px;
    }
  }

  .rule-list {
    margin-top: 25px;

    .item-li,
    .item-li-info,
    .no-action,
    .info-title {
      display: flex;
      align-items: center;
    }

    .item {
      .item-li {
        margin-bottom: 20px;

        .item-li-info {
          padding: 6px 10px;
          border-radius: 4px;
          border: 1px solid #e4e4e4;
          width: 700px;
          margin-left: 10px;
          justify-content: space-between;
          height: 46px;
          .info-title {
            cursor: pointer;
            width: 100px;
          }

          .item-value {
            text-align: left;
            width: 220px;
          }

          .no-action {
            display: flex;
            width: 70px;
          }
        }

        :deep(.ant-checkbox-input) {
          width: 18px;
          height: 18px;
        }
      }
    }
  }

  .signatory-area {
    flex: 1;
    margin-top: 20px;
    .signatory-count {
      color: #797979;
      margin-bottom: 5px;
    }
    .signatory-item {
      background: #f7f8fb;
      padding: 15px 20px;
      border-radius: 2px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 20px;
      border-radius: 5px;
      border: 1px solid #ced2dc;
      .signatory-form {
        display: flex;
        align-items: center;
        /* margin-left:80px; */
        :deep(.ant-input) {
          width: 200px;
          margin: 0 10px;
        }
        :deep(.ant-form-item) {
          margin-bottom: 0;
        }
      }
      .signatory-ent-info .receive-node .arrow-right .arrow-line {
        width: 60px;
        height: 2px;
        background: #d3d3d3;
      }
    }

    .initiator-info {
      background: #f7f8fb;
      padding: 15px 20px;
      border-radius: 2px;
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      border-radius: 5px;
      border: 1px solid #ced2dc;
      .initiator-left {
        flex: 1;
      }

      .initiator-process {
        // margin-top: 10px;
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
  }
  .initiator-process,
  .initiator-item,
  .arrow-right,
  .initiator-list,
  .flex-item,
  .receive-node {
    display: flex;
    align-items: center;
  }
  .signatory-person-info {
  }
  .signer-set {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .cc-card {
    :deep(.ant-select-selector) {
      width: 250px;
    }
    .ccType-area {
      .ccType-item {
        display: flex;
        // :deep(.ant-checkbox-wrapper){
        //   height:80px;
        // }
        .inside-cclist {
          margin-left: 20px;
          color: #999;
          cursor: pointer;
          display: flex;
          // height:80px;
        }
        .ccerList-ul {
          display: flex;
          align-items: center;
          // margin-top:10px;
          max-width: 700px;
          flex-wrap: wrap;
          // margin-bottom:10px;
          li {
            min-width: 90px;
            background: #f7f8fb;
            border: 1px solid #e9e9eb;
            border-radius: 2px;
            // margin:0 4px;
            padding: 2px 8px;
            color: #94969c;
            cursor: pointer;
            display: flex;
            margin: 5px 4px;
            align-items: center;
            justify-content: space-between;
            .delete-cc-user {
            }
          }
        }
      }
    }
  }

  :deep(.ant-picker-default) {
    width: 200px;
  }
  .sign-file-card {
    .sign-file-switch {
      display: flex;
      div {
        margin: 0 10px;
        span {
          margin-right: 10px;
        }
      }
    }
    .signFile-action {
      display: flex;
      align-items: center;
      color: #127fd2;
      :deep(.app-iconify) {
        color: #127fd2;
      }
      :deep(.ant-btn-link) {
        padding: 0 2px;
      }
    }
    .contract-files {
      .drag-files {
        display: flex;
      }
      // padding:5px;
      ul {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        width: 800px;
        // border: 1px solid #e9e9e9;
        li {
          width: 170px;
          // height: 220px;
          background: #fff;
          text-align: center;
          margin: 10px;
          position: relative;
          box-sizing: border-box;
          padding: 0;
          .file-item {
            display: flex;
            flex-direction: column;
            border: 1px solid #d3d1d1;
          }
          .file-copy {
            display: flex;
            flex-direction: row;
            line-height: 30px;
            color: rgba(0, 0, 0, 0.4);
            cursor: pointer;
            user-select: none;
            .copy-text {
              width: 140px;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              padding: 0 3px;
              &:hover {
                color: rgba(0, 0, 0, 0.6) !important;
              }
            }
            .copy-icon {
              width: 30px;
              height: 30px;
              &:hover {
                :deep(.app-iconify) {
                  color: rgba(0, 0, 0, 0.6) !important;
                }
              }
            }
          }
          .file-icon {
            border-bottom: 1px solid #d3d1d1;
            position: relative;
            height: 176px;
            &:hover {
              border-radius: 2px;
              .file-mask {
                display: block;
              }
              :deep(.app-iconify) {
                visibility: visible;
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
            height: 44px;
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
  }
  .sign-ca-card {
    :deep(.ant-radio-wrapper) {
      display: block;
    }
  }
  .other-file-card {
    .other-file-switch {
      display: flex;
      justify-content: space-between;
    }
  }

  :deep(.ant-card) {
    margin-top: 20px;
    box-shadow: none;
    .ant-card-head-title {
      display: flex;
      justify-content: space-between;
    }
  }

  .receive-node {
    .initiator-item {
      min-width: 60px;
      /* background:#f7f8fb; */
      /* border:1px solid #e9e9eb; */
      /* border-radius: 2px; */
      /* margin:0 4px;
    padding:2px 8px; */
      color: #000;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
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
  :deep(.ant-card-head) {
    background-color: #f7f8fb;
  }
  .confirm-modal-content {
    padding: 20px;
    border: 1px solid #e0e0e0;
    border-radius: 6px;

    .modal-title {
      font-weight: bold;
      font-size: 14px;
      margin-bottom: 15px;
    }

    .modal-text {
      font-size: 14px;
      line-height: 1.6;
      margin-bottom: 15px;
      text-align: justify;
    }

    .modal-warning {
      color: #ff4d4f;
      font-weight: bold;
    }
  }
  // .signatory-action {
  //   min-width: 100px;
  //   cursor: pointer;
  //   text-align: right;
  //   :deep(.app-iconify) {
  //     margin: 0 10px;
  //   }
  // }
  // :deep(.resrun-svg-icon) {
  //   margin: 0 10px;
  // }
</style>

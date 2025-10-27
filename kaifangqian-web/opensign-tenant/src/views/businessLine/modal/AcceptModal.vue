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
  <div>
    <BasicModal
      v-bind="$attrs"
      @register="registerModal"
      :title="getTitle"
      @ok="handleSubmit"
      :destroyOnClose="true"
      :maskClosable="false"
    >
      <p style="color: #888"
        >⚠️
        签署说明：企业接收方内部签署将严格按您设置的节点顺序执行。若存在多个签署节点且需改变顺序，点击节点后的调整顺序图标进行调整。</p
      >
      <div class="participants-senderUserId-area">
        <ul>
          <li v-for="(item, index) in senderList" :key="index">
            <!-- <p class="participants-senderUserId-type">{{ loadInitiatorSignerType(item.senderType) }}</p> -->
            <div class="participants-item-content">
              <div class="participants-senderUserId-content">
                <div class="participants-senderUserId-name">
                  <!-- <a-input v-model:value="item.senderName" v-if="item.edit" @keyup.enter="handlesenderUserIdName(item)"></a-input> -->
                  <span class="weight-label" style="color: #000; font-weight: 600">{{
                    item.senderName
                  }}</span>
                  <!-- <div>
                        <Icon icon="ant-design:edit-outlined" v-if="!item.edit" @click="handlesenderUserIdName(item)"/>
                        <Icon icon="ant-design:check-outlined" v-else  @click="handlesenderUserIdName(item)" />
                      </div> -->
                </div>
                <div class="org-seal">
                  <p class="flex items-center" v-if="item.senderType === 1">
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
                  <a-space>
                    <!-- <span>人脸识别校验：</span>
                        <a-switch :checked-value="1" :un-checked-value="0" v-model:checked="item.confirmType" /> -->
                    <span>签署意愿验证方式：</span>
                    <a-tooltip>
                      <template #title>签署时，签署人可选择对应的意愿认证方式进行身份校验</template>
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
                  <p v-show="personalSignAuth == 'allowed' && item.senderType === 1">
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
                  </p>
                </div>
              </div>
              <div class="signatory-action">
                <SvgIcon name="up" size="20" v-if="index != 0" @click="handleArrowUp(index)" />
                <SvgIcon
                  name="down"
                  size="20"
                  v-if="index + 1 != senderList.length"
                  @click="handleArrowDown(index)"
                />
                <Icon icon="ant-design:delete-outlined" size="20" @click="handleDelete(index)" />
              </div>
            </div>
          </li>
        </ul>
        <div class="senderUserId-action">
          <a-dropdown trigger="hover">
            <a-button type="link"> <Icon icon="ant-design:plus-outlined" />添加</a-button>
            <template #overlay>
              <a-menu>
                <a-menu-item :disabled="!canAddEbtNode(1)">
                  <a href="javascript:;" @click="handleAddEntNode(1)">经办人签字</a>
                </a-menu-item>
                <a-menu-item :disabled="!canAddEbtNode(4)">
                  <a href="javascript:;" @click="handleAddEntNode(4)">组织签章</a>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </div>
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Icon, SvgIcon } from '/@/components/Icon';
  // import SealModal from './SealModal.vue';
  import { loadInitiatorSignerType } from '../typeToName';
  import { cloneDeep } from 'lodash-es';


  interface Participant {
    senderType: number;
    senderOrder: number;
    senderSignType?: number | boolean;
    senderName: string;
    edit?: boolean;
    id?: string;
    senderSealId?: string;
    sealName?: string;
    senderUserName?: string;
    senderUserId: string;
    signerId: string;
    confirmType?: number;
    verifyType?: Array<string>;
    agreeSkipWillingness?: number;
    personalSignAuth?: string;
  }

  export default defineComponent({
    name: 'SignerModal',
    components: {
      SvgIcon,
      Icon,
      BasicModal,
    },
    setup(_, { emit }) {
      const recordId = ref('');
      const senderList = ref<any[]>([]);
      const signRuId = ref('');
      const signReId = ref('');
      const signConfirm = ref(false);
      const personalSignAuth = ref('');

      const confirmOptions = ref([
        { label: '验证码', value: 'CAPTCHA' },
        { label: '签署密码', value: 'PASSWORD' },
        { label: '双重校验（验证码 + 签署密码）', value: 'DOUBLE' },
        { label: '人脸识别', value: 'FACE', disabled: true },
      ]);

      const { createMessage: msg, createConfirm,createWarningModal } = useMessage();

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({
          confirmLoading: false,
          width: 1200,
          cancelText: '关闭',
          defaultFullscreen: false,
        });

        recordId.value = data.record.data.id ? data.record.data.id : data.record.data.uid;
        senderList.value = data.record?.data.senderList
          ? cloneDeep(data.record.data?.senderList)
          : [];
        signReId.value = data.record.data.signReId;
        signRuId.value = data.record.data.signRuId;
        signConfirm.value = data.record.signConfirm;
        personalSignAuth.value = data.record.personalSignAuth || 'required';
        confirmOptions.value[3].disabled = !signConfirm.value;
        // senderList.value.map(item=>{
        //   item.edit = false;
        //   item.senderSignType = item.senderSignType==1?true:false;
        //   if(item.senderType==4){
        //     loadSealName(item,item.senderSealId);
        //     sealId.value = item.senderSealId as string;
        //   }
        // })
        // let sealResult = await getSealList({})
        // if(sealResult){
        //   sealOptions.value = sealResult;
        // }
      });

      const getTitle = '签署流程设置';

      //调整顺序
      function handleArrowDown(index) {
        if (index < senderList.value.length - 1) {
          // Swap the current item with the one below it
          [senderList.value[index], senderList.value[index + 1]] = [
            senderList.value[index + 1],
            senderList.value[index],
          ];
        }
      }
      function handleArrowUp(index) {
        if (index > 0) {
          [senderList.value[index], senderList.value[index - 1]] = [
            senderList.value[index - 1],
            senderList.value[index],
          ];
        }
      }
      //删除参与方
      function handleDelete(index) {
        senderList.value.splice(index, 1);
      }
      //删除签字人
      function handleDeletesenderUserId(item) {
        item.senderUserId = '';
      }
      //经办人限制
      function loadManagerDisabled() {
        return senderList.value.filter((item) => item.senderType == 1).length ? true : false;
      }
      //法人限制
      function loadLegalDisabled() {
        return senderList.value.filter((item) => item.senderType == 2).length ? true : false;
      }

      async function handleSubmit() {
        try {
          let newSenderList: any = cloneDeep(senderList.value);
          emit('success', newSenderList, recordId.value);
        } finally {
          setModalProps({ confirmLoading: false });
          closeModal();
        }
      }
      //是否可添加企业节点
      function canAddEbtNode(type) {
        let matchEntNode = senderList.value && senderList.value.filter((v) => v.senderType == type);
        return matchEntNode && matchEntNode.length ? false : true;
      }
      // 添加接收方企业节点
      function handleAddEntNode(type) {
        senderList.value.push({
          senderType: type,
          senderOrder: senderList.value.length + 1,
          senderName: type == 1 ? '经办人签字' : '组织签章',
          verifyType: ['CAPTCHA', 'PASSWORD', 'DOUBLE'],
          personalSignAuth: personalSignAuth.value === 'not_required' ? 'not_required' : 'required',
        });
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
        registerModal,
        getTitle,
        handleSubmit,
        senderList,
        loadInitiatorSignerType,
        handleArrowDown,
        handleArrowUp,
        handleDelete,
        handleDeletesenderUserId,
        loadManagerDisabled,
        loadLegalDisabled,
        signConfirm,
        canAddEbtNode,
        handleAddEntNode,
        confirmOptions,
        handleVerifyTypeChange,
        personalSignAuth,
      };
    },
  });
</script>
<style lang="less" scoped>
  // @import '../index.less';
  .participants-senderUserId-area {
    li {
      margin-top: 10px;
      p {
        margin-top: 10px;
        line-height: 32px;
      }
      .participants-item-content {
        display: flex;
        align-items: center;
        background: #f7f8fb;
        padding: 10px 20px;
        justify-content: space-between;
        margin-right: 20px;
        border-radius: 5px;
        border: 1px solid #ced2dc;
      }
      .participants-senderUserId-content {
        .participants-senderUserId-name {
          display: flex;
          align-items: center;
          line-height: 32px;
          :deep(.ant-input) {
            width: 250px;
          }
          :deep(.app-iconify) {
            margin-left: 15px;
            cursor: pointer;
          }
        }
        .org-seal {
        }
      }
    }
    .participants-senderUserId-type {
      color: #797979;
    }
  }

  .signatory-action {
    min-width: 100px;
    cursor: pointer;
    text-align: right;
    :deep(.app-iconify){
      margin: 0 10px;
    }
  }

  :deep(.resrun-svg-icon){
    margin: 0 10px;
  }
</style>

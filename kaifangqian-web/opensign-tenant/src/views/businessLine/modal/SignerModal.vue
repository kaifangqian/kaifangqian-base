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
      <p style="color: #888;">⚠️ 签署说明：发起方内部签署将严格按您设置的节点顺序执行。若存在多个签署节点且需改变顺序，点击节点后的调整顺序图标进行调整。</p>
      <div class="participants-senderUserId-area">
        <ul>
          <li v-for="(item, index) in senderList" :key="index">
            <p class="participants-senderUserId-type">{{
              loadInitiatorSignerType(item.senderType)
            }}</p>
            <div class="participants-item-content">
              <div class="participants-senderUserId-content">
                <div class="participants-senderUserId-name">
                  <a-input
                    v-model:value="item.senderName"
                    v-if="item.edit"
                    @keyup.enter="handlesenderUserIdName(item)"
                  ></a-input>
                  <span class="weight-label" v-else style="color: #000;font-weight: 600">{{ item.senderName }} </span>
                  <div>
                    <Icon
                      icon="ant-design:edit-outlined"
                      v-if="!item.edit"
                      @click="handlesenderUserIdName(item)"
                    />
                    <Icon
                      icon="ant-design:check-outlined"
                      v-else
                      @click="handlesenderUserIdName(item)"
                    />
                  </div>
                </div>
                <div class="org-seal" v-if="item.senderType == 4">
                  <span>
                    <span>指定印章：</span>
                    <a-select
                      style="width: 200px"
                      :allowClear="true"
                      @change="($event) => handleSealSuccess($event, item)"
                      v-model:value="item.senderSealId"
                      :options="sealOptions"
                      :fieldNames="{ label: 'sealName', value: 'sealId' }"
                    >
                    </a-select>
                    <p>
                      <span>盖章方式：</span>
                      <a-checkbox
                        v-model:checked="item.senderSignType"
                        @change="handleCheckSealAUth(item)"
                        >自动盖章</a-checkbox
                      >
                    </p>
                  </span>
                  <p v-if="item.senderSignType != 1">
                    <span
                      >签署人：
                      <a-tag
                        closable
                        v-if="item.senderUserId"
                        @close="handleDeletesenderUserId(item)"
                      >
                        {{ item.senderUserName }}</a-tag
                      >
                    </span>
                    <a-button type="link" v-if="!item.senderUserId" @click="handleAddUser(item)">
                      <Icon icon="ant-design:plus-outlined" />添加</a-button
                    >
                  </p>
                  <p v-if="!item.senderSignType">
                    <span>签署意愿验证方式：</span>
                    <a-tooltip>
                        <template #title>签署时，签署人可选择对应的意愿认证方式进行身份校验</template>
                        <Icon icon="ant-design:question-circle-outlined"  style="margin-right: 10px;color: #888;"/>
                    </a-tooltip>
                    <a-checkbox-group :value="item.verifyType" :options="confirmOptions" @change="(val) => handleVerifyTypeChange(val, item)"/>
                  </p>
                </div>
                <div class="org-seal" v-else-if="item.senderType == 1">
                  <p>文档签署的发起人签字 </p>
                  <p class="flex items-center">
                    <span>是否允许免意愿快捷签署：</span>
                    <a-tooltip>
                          <template #title>签署人如果已开通免意愿快捷签署服务，则签署时，可无需进行额外的意愿认证</template>
                          <Icon icon="ant-design:question-circle-outlined" style="margin-right: 10px;color: #888;" />
                        </a-tooltip>
                    <a-switch
                      :checked-value="1"
                      :un-checked-value="0"
                      v-model:checked="item.agreeSkipWillingness"
                    />
                  </p>
                  <p>
                    <span>签署意愿验证方式：</span>
                    <a-tooltip>
                          <template #title>签署时，签署人可选择对应的意愿认证方式进行身份校验</template>
                          <Icon icon="ant-design:question-circle-outlined"  style="margin-right: 10px;color: #888;"/>
                        </a-tooltip>
                    <a-checkbox-group :value="item.verifyType" :options="confirmOptions" @change="(val) => handleVerifyTypeChange(val, item)"/>
                  </p>
                  <p v-show="personalSignAuth == 'allowed'">
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
                          <template #title>无需实名认证：使用个人电子签章前，无需进行实名认证</template>
                          <Icon icon="ant-design:question-circle-outlined" />
                        </a-tooltip>
                      </a-radio>
                    </a-radio-group>
                  </p>
                </div>
                <div class="org-seal" v-else-if="item.senderType == 2 || item.senderType == 3">
                  <p>
                    <span
                      >签字人：
                      <a-tag
                        v-if="item.senderUserId.length"
                        closable
                        @close="handleDeletesenderUserId(item)"
                      >
                        {{ item.senderUserName }}</a-tag
                      >
                    </span>
                    <a-button
                      type="link"
                      v-if="!item.senderUserId.length"
                      @click="handleAddUser(item)"
                    >
                      <Icon icon="ant-design:plus-outlined" />添加</a-button
                    >
                  </p>
                  <p class="flex items-center">
                    <span>是否允许免意愿快捷签署：</span>
                    <a-tooltip>
                        <template #title>签署人如果已开通免意愿快捷签署服务，则签署时，可无需进行额外的意愿认证</template>
                        <Icon icon="ant-design:question-circle-outlined" style="margin-right: 10px;color: #888;" />
                    </a-tooltip>
                    <a-switch
                      :checked-value="1"
                      :un-checked-value="0"
                      v-model:checked="item.agreeSkipWillingness"
                    />
                  </p>
                  <p>
                    <span>签署意愿验证方式：</span>
                    <a-tooltip>
                        <template #title>签署时，签署人可选择对应的意愿认证方式进行身份校验</template>
                        <Icon icon="ant-design:question-circle-outlined"  style="margin-right: 10px;color: #888;"/>
                    </a-tooltip>
                    <a-checkbox-group :value="item.verifyType" :options="confirmOptions" @change="(val) => handleVerifyTypeChange(val, item)" />
                  </p>
                  <p v-show="personalSignAuth == 'allowed'">
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
                          <template #title>无需实名认证：使用个人电子签章前，无需进行实名认证</template>
                          <Icon icon="ant-design:question-circle-outlined" />
                        </a-tooltip>
                      </a-radio>
                    </a-radio-group>
                  </p>
                </div>
                <div class="org-seal" v-else-if="item.senderType == 5">
                  <p>
                    <span
                      >审批人：
                      <a-tag
                        v-if="item.senderUserId.length"
                        closable
                        @close="handleDeletesenderUserId(item)"
                      >
                        {{ item.senderUserName }}</a-tag
                      >
                    </span>
                    <a-button
                      type="link"
                      v-if="!item.senderUserId.length"
                      @click="handleAddUser(item)"
                    >
                      <Icon icon="ant-design:plus-outlined" />添加</a-button
                    >
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
          <a-dropdown :trigger="['click']">
            <a-button type="link"
              ><Icon icon="ant-design:plus-outlined" />添加内部签署/审批节点</a-button
            >
            <template #overlay>
              <a-menu @click="handleAddInitiator">
                <a-menu-item :key="4">
                  <a href="javascript:;">组织签章</a>
                </a-menu-item>
                <a-menu-item :key="1" :disabled="loadManagerDisabled()">
                  <a href="javascript:;">经办人签字</a>
                </a-menu-item>
                <a-menu-item :key="3">
                  <a href="javascript:;">个人签字</a>
                </a-menu-item>
                <a-menu-item :key="5">
                  <a href="javascript:;">个人审批</a>
                </a-menu-item>
                <a-menu-item :key="2" :disabled="loadLegalDisabled()">
                  <a href="javascript:;">法人签字</a>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </div>
      <TabTreeModal @register="registerUserModal" @success="handleSuccess" />
      <!-- <SealModal @register="registerSeal" @success="handleSealSuccess" /> -->
    </BasicModal>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref, computed,onMounted } from 'vue';
  import { BasicModal, useModal, useModalInner } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import SealModal from './SealModal.vue';
  import { loadInitiatorSignerType } from '../typeToName';
  import { getDeptNoCountTree } from '/@/api/sys/dept';
  import { getRoleTreeList, getUserByRoleId } from '/@/api/sys/role';
  import { getUserList } from '/@/api/demo/system';
  import { getSealInfo, getSealUserList, getSealList } from '/@/api/contract';
  import { cloneDeep } from 'lodash-es';
  import { useUserStore } from '/@/store/modules/user';
  import { silentQueryApi, silentOpenApi } from '/@/views/silent/api';


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
      BasicModal,
      Icon,
      TabTreeModal,
      SealModal,
      SvgIcon,
    },
    setup(_, { emit }) {
      const recordId = ref('');
      const isUpdate = ref(true);
      const currentSender = ref();
      const senderList = ref(<Participant[]>[]);
      const sealAuthUsers = ref([]);
      const sealId = ref('');
      const signRuId = ref('');
      const signReId = ref('');
      const signConfirm = ref(false);
      const currentSealInfo = ref({
        senderSealId: '',
        sealName: '',
      });
      const sealOptions = ref([]);
      const autoSign = ref(false);
      const userStore = useUserStore();
      const tenantInfo = userStore.getTenantInfo;
      const userInfo = userStore.getUserInfo;
      const personalSignAuth = ref('');

      const confirmOptions = ref([
        { label: '验证码', value: 'CAPTCHA' },
        { label: '签署密码', value: 'PASSWORD' },
        { label: '双重校验（验证码 + 签署密码）', value: 'DOUBLE' },
        { label: '人脸识别', value: 'FACE', disabled: true },
      ]);


      const { createMessage: msg, createConfirm,createWarningModal } = useMessage();
      const [registerSeal, { openModal: openSealModal }] = useModal();
      const [registerUserModal, { openModal, closeModal: closeuserModal }] = useModal();

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({
          confirmLoading: false,
          width: 1200,
          cancelText: '关闭',
          defaultFullscreen: false,
        });
        recordId.value = data.record?.id;
        senderList.value = data.record?.senderList ? cloneDeep(data.record?.senderList) : [];
        signReId.value = data.record.signReId;
        signRuId.value = data.record.signRuId;
        signConfirm.value = data.record.signConfirm;
        personalSignAuth.value = data.record.personalSignAuth || 'required';
        // if (signConfirm.value) {
        console.log('data.record', data.record);
        console.log('signConfirm.value', signConfirm.value);
        confirmOptions.value[3].disabled = !signConfirm.value;

        
        // }
        senderList.value.map((item) => {
          item.edit = false;
          item.senderSignType = item.senderSignType == 1 ? true : false;
          if (item.senderType == 4) {
            loadSealName(item, item.senderSealId);
            sealId.value = item.senderSealId as string;
          }
          // 未设置认证要求数据时，设置默认数据
          if (!item.personalSignAuth) {
            item.personalSignAuth = personalSignAuth.value === 'not_required' ? 'not_required' : 'required';
          }
          // 当业务线设置认证要求为'required'和'not_required'时，强制签署节点的值与其保持一致
          if(personalSignAuth.value == 'required' || personalSignAuth.value == 'not_required'){
            item.personalSignAuth = personalSignAuth.value;
          }
        });
        console.log('senderList.value', senderList.value);
        let sealResult = await getSealList({});
        if (sealResult) {
          sealOptions.value = sealResult;
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '签署流程设置' : '签署流程设置'));

      onMounted(() => {
        handSilentAuthStatus();
      });

      // 获取发起方企业的静默授权开通状态
      async function handSilentAuthStatus(){
        const result = await silentQueryApi();
        console.log('result', result);
        if (result.status === 0) {
          // alert('未开通');
          // 未开通
          autoSign.value = false;
        } else if (result.status === 1) {
          autoSign.value = true;
        }
      }

      //编辑发起方签署人名称
      function handlesenderUserIdName(item) {
        item.edit = !item.edit;
      }

      function handleAddInitiator(val) {
        console.log(val.key, val.key, '签署节点--');
        senderList.value.push({
          id: '',
          senderName: loadInitiatorSignerType(val.key),
          senderOrder: senderList.value.length + 1,
          senderSealId: '',
          senderSignType: false,
          senderType: val.key,
          senderUserId: '',
          signerId: '',
          verifyType: ['CAPTCHA', 'PASSWORD', 'DOUBLE'],
          personalSignAuth: personalSignAuth.value === 'not_required' ? 'not_required' : 'required',
        });
      }
      //选择人
      function handleAddUser(item) {
        currentSender.value = item;
        if (item.senderType == 4) {
          if (!unref(sealId)) {
            msg.warning('请先指定印章');
            return;
          }
          openModal(true, {
            isupDate: false,
            title: '添加签署人',
            tabs: [
              {
                title: '印章授权使用范围',
                type: 'sealUser',
                api: getSealUserList,
                asyncTree: true,
                fieldNames: {
                  children: 'children',
                  title: 'nickName',
                  key: 'id',
                },
                params: {
                  businessRelationId: unref(sealId),
                },
              },
            ],
          });
        } else {
          openModal(true, {
            isupDate: false,
            title: item.senderType == 5 ? '添加审批人' : '添加签署人',
            tabs: [
              {
                title: '根据组织选人',
                type: 'deptUser',
                api: getDeptNoCountTree,
                asyncTree: true,
                asyncApi: getUserList,
                asyncFieldNames: {
                  title: 'nickName',
                  key: 'id',
                },
                fieldNames: {
                  children: 'children',
                  title: 'departName',
                  key: 'id',
                },
              },
              {
                title: '根据角色选人',
                type: 'roleUser',
                api: getRoleTreeList,
                asyncTree: true,
                asyncApi: getUserByRoleId,
                asyncFieldNames: {
                  title: 'nickName',
                  key: 'userId',
                },
                fieldNames: {
                  children: 'children',
                  title: 'name',
                  key: 'id',
                },
              },
            ],
          });
        }
      }
      //选择人员回调
      async function handleSuccess(list) {
        if (list.length != 1) {
          msg.warning('请选择一个签字人');
          return;
        }
        list.map((item) => {
          currentSender.value.senderUserId = item.tenantUserId;
          currentSender.value.senderUserName = item.nickName;
        });
        closeuserModal();
      }
      //选择组织章
      function handleSeal(item) {
        currentSealInfo.value = item;
        openSealModal(true, {
          isUpdate: false,
          record: {},
        });
      }
      //加载印章name
      async function loadSealName(row, id) {
        if (!id) return '';
        let result: any = await getSealInfo({ sealId: id });
        if (result) {
          row.sealName = result?.sealName;
        }
      }
      //指定印章回调
      function handleSealSuccess(val, sender) {
        currentSealInfo.value.senderSealId = val;
        // currentSealInfo.value.sealName = list[0].sealName;
        sealId.value = val;
        let matchSeal: any = sealOptions.value.find((v: any) => v.sealId == val);

        sender.senderSealId = val;
        if (matchSeal) {
          sender.sealName = matchSeal.sealName;
          // 更换印章后，自动盖章置为不勾选
          sender.senderSignType = false;
          getSealAuthUsers(unref(sealId)).then((res) => {
            if (!checkSealAuth() && sender.senderSignType) {
              msg.warning(
                '您尚无该印章的使用权限，无法设置自动盖章，请联系企业印章管理员为您授权印章权限',
              );
              sender.senderSealId = '';
              sealId.value = '';
              sender.sealName = '';
            }
          });
        }
      }
      // 获取签章对应权限人员
      function getSealAuthUsers(sealId) {
        return new Promise(async (resolve, reject) => {
          let result = await getSealUserList({ businessRelationId: sealId });
          if (result) {
            sealAuthUsers.value = result;
            resolve();
          } else {
            reject();
          }
        });
      }
      //检查当前操作用户是否有该章使用权限
      function checkSealAuth() {
        const tenantUserId = userInfo.tenantUserId;
        let hasAuth = sealAuthUsers.value.filter((v) => v.tenantUserId == tenantUserId).length
          ? true
          : false;
        return hasAuth;
      }

      //选择自动盖章时判断是否有权限
      function handleCheckSealAUth(row) {
        if (!row.senderSealId) {
          msg.warning('请先指定印章');
          row.senderSignType = false;
          return;
        }
        if (row.senderSignType && unref(sealId)) {
          if (!checkSealAuth() && row.senderSignType) {
            msg.warning(
              '您尚无该印章的使用权限，无法设置自动盖章，请联系企业印章管理员为您授权印章权限',
            );
            row.senderSignType = false;
            return false;
          }
        }
        // 判断是否有自动盖章的授权
        // const { status } = await silentQueryApi({});
        // autoSign.value = status === 1;

        // 判断是否是企业管理员
        // userInfo.tenantUserId == tenantInfo.applyTenantUser

        if (row.senderSignType && unref(sealId) && !autoSign.value) {
          row.senderSignType = false;
          if(userInfo.tenantUserId == tenantInfo.applyTenantUser){
            createConfirm({
              iconType: 'warning',
              title: '温馨提示',
              content:
                '企业尚未开通静默签署服务或静默签署授权已失效，为保证签署安全，请先开通静默签服务，开通后可使用自动盖章功能',
              okText: '保存并去开通',
              cancelText: '取消',
              async onOk() {
                // 第一步：先保存当前页面信息
                // 保存当前弹窗内容
                // await handleSubmit();
                emit('saveBsicData');
                // 第二步：跳转开通服务页面
                const asyncPage = window.location.href;
                const result = await silentOpenApi({ callbackPage: asyncPage });
                if (result.status === 0) {
                  window.open(result.openSignNoVerifyUrl, '_self');
                } else if (result.status === 1) {
                  window.open(asyncPage, '_self');
                }
              }
            });
          }else{
            createWarningModal({
              title: '温馨提示', 
              content: '企业尚未开通静默签署服务或静默签署授权已失效，为保证签署安全，请联系管理员开通静默签服务，开通后可使用自动盖章功能',
              okText:'知道了',
              iconType: 'warning',
              onCancel(){

              }
          })

          }
        }
      }

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
          let canSubmit = true;
          for (const item of newSenderList) {
            item.senderSignType = item.senderSignType == true ? 1 : 2;
            if (item.senderType == 4) {
              if (!item.senderSealId) {
                msg.warning('请指定签章');
                canSubmit = false;
                break;
              }
              if (item.senderSignType == 2 && !item.senderUserId) {
                canSubmit = false;
                msg.warning('请指定印章签署人');
                break;
              }
              //自动盖章
              if (item.senderSignType == 1) {
                item.senderUserId = '';
              }
            } else if ((item.senderType == 2 || item.senderType == 3) && !item.senderUserId) {
              canSubmit = false;
              msg.warning('请指定签字人');
              break;
            }else if (item.senderType == 5 && !item.senderUserId) {
              canSubmit = false;
              msg.warning('请指定审批节点对应的审批人');
              break;
            }
          }

          if (!canSubmit) {
            return;
          }
          // if(!newSenderList.length){
          //   msg.warning('请设置签署流程');
          //   return
          // }
          if (newSenderList.length) {
            msg.success('设置成功');
          }
          closeModal();
          emit('success', newSenderList);
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      function handlePos() {
        if (!unref(signRuId)) {
          msg.warning('请先保存基本数据');
          return;
        }
        window.open(
          import.meta.env.VITE_PUBLIC_PATH +
            '#/contract/position?__full__&signReId=' +
            unref(signReId) +
            '&signRuId=' +
            unref(signRuId),
        );
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
        handleAddInitiator,
        handlesenderUserIdName,
        handleArrowDown,
        handleArrowUp,
        handleDelete,
        registerUserModal,
        handleSuccess,
        handleAddUser,
        handleDeletesenderUserId,
        loadManagerDisabled,
        loadLegalDisabled,
        registerSeal,
        handleSealSuccess,
        handleSeal,
        handleCheckSealAUth,
        handlePos,
        loadSealName,
        sealOptions,
        signConfirm,
        confirmOptions,
        autoSign,
        checkSealAuth,
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
        // border-radius: 5px;
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

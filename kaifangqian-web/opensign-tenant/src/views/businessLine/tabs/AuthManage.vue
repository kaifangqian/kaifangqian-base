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
  <div class="businessline-auth-container">
    <a-card title="业务线权限设置" size="small">
      <div class="auth-item">
        <div class="auth-item-header">
          <SvgIcon name="required" />
          <h2>管理员</h2>
          <span class="auth-tip">业务线的管理员，拥有该业务线的管理权限</span>
        </div>
        <div class="select-user-area">
          <ul class="select-ul">
            <template v-for="(item, index) in manageAuthList" :key="index">
              <li v-if="item.authType == 1" class="select-user-item">
                <span>{{ item.userName }}</span>
                <Icon
                  icon="ant-design:close-outlined"
                  class="delete-cc-user"
                  @click="handleDeleteUser(1, index)"
                />
              </li>
            </template>
            <a-button type="link" @click="handleAddAuthType(1)">
              <Icon icon="ant-design:plus-outlined" />添加</a-button
            >
          </ul>
        </div>
      </div>

      <div class="auth-item">
        <div class="auth-item-header">
          <h2>使用范围</h2>
          <span class="auth-tip">使用范围内的成员，可以在发起签署时使用该业务线</span>
        </div>
        <div class="select-user-area">
          <ul class="select-ul">
            <template v-for="(item, index) in useAuthList" :key="index">
              <li v-if="item.authType == 2" class="select-user-item">
                <span>{{ item.userName }}</span>
                <Icon
                  icon="ant-design:close-outlined"
                  class="delete-cc-user"
                  @click="handleDeleteUser(2, index)"
                />
              </li>
            </template>
            <a-button type="link" @click="handleAddAuthType(2)">
              <Icon icon="ant-design:plus-outlined" />添加</a-button
            >
            <span class="auth-tip" v-if="loadAuthType2Length()"
              >使用范围为空时，默认组织内的所有人均可以在发起签署时使用该业务线</span
            >
          </ul>
        </div>
      </div>
    </a-card>
    <a-card title="签约文档权限设置" size="small">
      <div class="auth-item">
        <div class="auth-item-header">
          <h2> 查看权限</h2>
          <a-tooltip>
            <template #title
              >可查看该业务线发起的电子签约文档，默认所有参与人（即发起、填写、签署流程中的所有参与人）可查看</template
            >
            <Icon icon="ant-design:question-circle-outlined" />
          </a-tooltip>
        </div>
        <div class="select-user-area">
          <ul class="select-ul">
            <span>指定查看人：</span>
            <template v-for="(item, index) in seeAuthList" :key="index">
              <li v-if="item.authType == 3" class="select-user-item">
                <span>{{ item.userName }}</span>
                <Icon
                  icon="ant-design:close-outlined"
                  class="delete-cc-user"
                  @click="handleDeleteUser(3, index)"
                />
              </li>
            </template>
            <a-button type="link" @click="handleAddAuthType(3)">
              <Icon icon="ant-design:plus-outlined" />添加</a-button
            >
          </ul>
        </div>
      </div>
      <div class="auth-item">
        <div class="auth-item-header">
          <h2> 文档下载权限</h2>
        </div>
        <div class="select-user-area">
          <div class="auth-sub-item">
            <span>指定下载角色：</span>
            <div>
              <a-checkbox-group v-model:value="downloaderType" style="width: 100%">
                <a-row>
                  <a-col :span="12">
                    <a-checkbox value="1">
                      参与人
                      <a-tooltip>
                        <template #title>即发起、填写、签署流程中的所有参与人</template>
                        <Icon icon="ant-design:question-circle-outlined" />
                      </a-tooltip>
                    </a-checkbox>
                  </a-col>
                  <a-col :span="12">
                    <a-checkbox value="2">
                      查看人
                      <a-tooltip>
                        <template #title>仅指在查看权限中添加的指定查看人</template>
                        <Icon icon="ant-design:question-circle-outlined" />
                      </a-tooltip>
                    </a-checkbox>
                  </a-col>
                </a-row>
              </a-checkbox-group>
            </div>
          </div>
          <div class="auth-sub-item">
            <span>指定下载人：</span>
            <div>
              <ul class="select-ul">
                <template v-for="(item, index) in downloadAuthList" :key="index">
                  <li v-if="item.authType == 4" class="select-user-item">
                    <span>{{ item.userName }}</span>
                    <Icon
                      icon="ant-design:close-outlined"
                      class="delete-cc-user"
                      @click="handleDeleteUser(4, index)"
                    />
                  </li>
                </template>
                <a-button type="link" @click="handleAddAuthType(4)">
                  <Icon icon="ant-design:plus-outlined" />添加</a-button
                >
              </ul>
            </div>
          </div>
        </div>
      </div>
    </a-card>
    <TabTreeModal @register="register" @success="handleSuccess" />
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, computed, unref, onMounted } from 'vue';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  import { useModal } from '/@/components/Modal';
  import { getDeptNoCountTree } from '/@/api/sys/dept';
  import { getUserList } from '/@/api/demo/system';
  import { setBusinessLineAuth, getBusinessLineAuth } from '/@/api/businessLine';
  import { useRouter } from 'vue-router';
  import { useMessage } from '/@/hooks/web/useMessage';

  export default defineComponent({
    name: 'AuthManage',
    components: {
      Icon,
      SvgIcon,
      TabTreeModal,
    },
    props: {
      lineFormData: {
        type: Object,
      },
    },
    setup(props, { emit }) {
      const lineManagers = ref([]);
      const authType = ref(1);
      const authForm = computed(() => props.lineFormData);
      const authList: any = ref([]);
      const manageAuthList: any = ref([]);
      const useAuthList: any = ref([]);
      const seeAuthList: any = ref([]);
      const downloadAuthList: any = ref([]);
      const downloaderType: any = ref([]);

      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const query = route.query;
      const signReId = query.signReId;

      const { createMessage: msg, createConfirm } = useMessage();
      const [register, { openModal, closeModal }] = useModal();
      getAuthInfo();

      function getFormData() {
        return authForm.value;
      }
      onMounted(() => {});

      async function getAuthInfo(params: type) {
        let result = await getBusinessLineAuth({ signReId: signReId });
        if (result) {
          // authList.value = [];
          authList.value = result.authList;
          loadAuthType();
          if (result.downloaderType == 1) {
            downloaderType.value = ['1'];
          }
          if (result.downloaderType == 2) {
            downloaderType.value = ['2'];
          }
          if (result.downloaderType == 3) {
            downloaderType.value = ['1', '2'];
          }
        }
      }
      //过滤权限类型人员
      function loadAuthType() {
        manageAuthList.value = authList.value
          .filter((v) => v.authType == 1)
          .reduce((acc, curr) => {
            if (!acc.find((item) => item.userId === curr.userId)) {
              acc.push(curr);
            }
            return acc;
          }, []);
        useAuthList.value = authList.value
          .filter((v) => v.authType == 2)
          .reduce((acc, curr) => {
            if (!acc.find((item) => item.userId === curr.userId)) {
              acc.push(curr);
            }
            return acc;
          }, []);
        seeAuthList.value = authList.value
          .filter((v) => v.authType == 3)
          .reduce((acc, curr) => {
            if (!acc.find((item) => item.userId === curr.userId)) {
              acc.push(curr);
            }
            return acc;
          }, []);
        downloadAuthList.value = authList.value
          .filter((v) => v.authType == 4)
          .reduce((acc, curr) => {
            if (!acc.find((item) => item.userId === curr.userId)) {
              acc.push(curr);
            }
            return acc;
          }, []);
      }

      //选择人
      function handleAddAuthType(type) {
        authType.value = type;
        openModal(true, {
          isupDate: false,
          title: '添加权限所有人',
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
          ],
        });
      }
      async function handleSuccess(list) {
        list.map((item) => {
          let authItem = {
            authType: unref(authType),
            userId: item.tenantUserId,
            userName: item.nickName,
          };
          authList.value.push(authItem);
          loadAuthType();
        });
        closeModal();
      }

      function handleDeleteUser(type, index) {
        if (type == 1) {
          manageAuthList.value.splice(index, 1);
        }
        if (type == 2) {
          useAuthList.value.splice(index, 1);
        }
        if (type == 3) {
          seeAuthList.value.splice(index, 1);
        }
        if (type == 4) {
          downloadAuthList.value.splice(index, 1);
        }
        authList.value = [
          ...manageAuthList.value,
          ...useAuthList.value,
          ...seeAuthList.value,
          ...downloadAuthList.value,
        ];
      }
      //判断是否显示使用范围提示
      function loadAuthType2Length() {
        let matchs = authList.value.filter((v) => v.authType == 2);
        return !matchs.length;
      }

      //保存数据
      function saveAuthData(isTabCall) {
        return new Promise(async (resolve, reject) => {
          let managers = authList.value.filter((v) => v.authType == 1);
          if (!managers.length) {
            msg.warning('请选择权限管理员');
            return;
          }
          let result = await setBusinessLineAuth({
            signReId,
            downloaderType:
              downloaderType.value.join(',') == '1'
                ? 1
                : downloaderType.value.join(',') == '2'
                ? 2
                : downloaderType.value.length == 2
                ? 3
                : 0,
            authList: authList.value,
          });
          if (result) {
            if (!isTabCall) {
              msg.success('保存成功');
              getAuthInfo();
            }
            resolve(result);
          } else {
            reject(result);
          }
        });
      }
      return {
        getFormData,
        lineManagers,
        register,
        handleSuccess,
        authList,
        handleAddAuthType,
        handleDeleteUser,
        downloaderType,
        getAuthInfo,
        saveAuthData,
        loadAuthType2Length,
        manageAuthList,
        useAuthList,
        seeAuthList,
        downloadAuthList,
      };
    },
  });
</script>

<style lang="less" scoped>
  .businessline-auth-container {
    :deep(.ant-card) {
      margin-bottom: 28px;
    }
  }
  .auth-tip {
    font-size: 12px;
    color: #999;
    margin-left: 20px;
  }
  .auth-item {
    margin-left: 20px;
    margin-bottom: 20px;
    .auth-item-header {
      display: flex;
      align-items: center;
      h2 {
        margin-bottom: 0;
        font-size: 14px;
        font-weight: 550;
        margin-right: 10px;
      }
    }
    .auth-sub-item {
      display: flex;
      align-items: center;
      margin-top: 10px;
    }
    .select-ul {
      margin: 0;
      display: flex;
      align-items: center;
      .select-user-item {
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
      }
    }
  }

  .select-user-area {
    margin-top: 15px;
    .select-user-ul {
      display: flex;
      align-items: center;
      margin-bottom: 0;
      margin-top: 10px;
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
</style>

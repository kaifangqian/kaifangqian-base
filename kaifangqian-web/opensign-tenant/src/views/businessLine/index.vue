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
  <div :class="['resrun-tree-table business-line-list', `${prefixCls}`]">
    <BasicTree
      ref="basicTree"
      title=""
      :toolbar="false"
      :checkable="false"
      :isRemoteSearch="true"
      :toggleSwitch="true"
      :fieldNames="{ title: 'name' }"
      :treeData="treeData"
      :searchApiData="(filterData as any)"
      :beforeRightClick="getRightMenuList"
      :actionList="actionList"
      @rowClick="handleRowClick"
      @loadSearchData="handleLoadSearchData"
      @searchChange="searchData"
      @select="onTreeSelect"
    >
      <template #action>
        <a-button type="default" :class="[prefixCls + '-action']" @click="handleCreateGroup">
          <Icon icon="ant-design:deployment-unit-outlined" />
          添加分组</a-button
        >
      </template>
    </BasicTree>
    <div :class="prefixCls + '-table'">
      <BasicTable @register="registerTable">
        <template #toolbar>
          <a-button type="primary" @click="handleAdd">新增业务线 </a-button>
        </template>
        <template #status="{ record }">
          <span :style="{ color: record.status == '1' ? '#299B64' : '#FF3B30' }">
            <a-badge status="success" v-if="record.status == '1'" />
            <a-badge status="error" v-if="record.status == '2'" />
            {{ record.status == '1' ? '启用' : '停用' }}</span
          >
        </template>
        <template #name="{ record, text }">
          <div style="display: flex">
            <div style="flex: 1; display: flex; align-items: center">
              <span>{{ text }}</span>
              <a-tooltip>
                <template #title v-if="record.errorStatus == 1"
                  >组织签章【自动盖章】的签署节点未指定签署位置，请先指定签署位置</template
                >
                <template #title v-if="record.errorStatus == 3"
                  >文档填写参数存在部分参数未指定填写方，请先指定</template
                >
                <template #title v-if="record.errorStatus == 4">
                  <p>1、组织签章【自动盖章】的签署节点未指定签署位置，请先指定签署位置</p>
                  <p>2、文档填写参数存在部分参数未指定填写方，请先指定</p>
                </template>
                <Icon
                  icon="ant-design:info-circle-outlined"
                  color="#f0a127"
                  v-if="record.errorStatus != 2"
                />
              </a-tooltip>
              <a-tooltip placement="top" trigger="hover">
                <template #title>
                  <span>点击复制ID</span>
                </template>
                <a-button type="link" @click="copyToken(record)">
                  <template #icon><CopyOutlined /></template>
                </a-button>
              </a-tooltip>
            </div>
          </div>
        </template>
        <template #managerList="{ record, text }">
          <span>{{ text && Array.isArray(text) ? text.join(',') : '' }}</span>
        </template>

        <template #action="{ record }">
          <a-button type="link" size="small" @click="handleEdit(record)">配置</a-button>
          <a-button type="link" size="small" @click="handleStatus(record)">{{
            record.status == 1 ? '停用' : '启用'
          }}</a-button>
          <!-- <a-button type="link"  size="small" @click="handleCopy(record)">复制</a-button> -->
          <a-button type="link" size="small" @click="handleMove(record)">迁移至</a-button>
        </template>
      </BasicTable>
    </div>
    <BusinessModal @register="registerModal" @success="handleCreateSuccess" />
    <BusinessGroupModal @register="registerGroupModal" @success="handleCreatGroupeSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, onMounted, unref, createVNode, h } from 'vue';
  import { Divider, Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined, EllipsisOutlined, CopyOutlined } from '@ant-design/icons-vue';
  import { BasicTree, TreeItem, ContextMenuItem } from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { BasicTable, useTable, TableAction } from '/@/components/Table';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { useModal } from '/@/components/Modal';
  import { lineColumns, lineFormSchema } from './data';
  import {
    getBusinessLineList,
    getBusinessLineGroupList,
    statusBusinessLine,
    copyBusinessLine,
    deleteBusinessGroup,
  } from '/@/api/businessLine';
  import BusinessModal from './modal/BusinessModal.vue';
  import BusinessGroupModal from './modal/BusinessGroupModal.vue';
  import { router } from '/@/router';
  import { createBusinessLineAuth } from '/@/api/license';

  interface ResultItem {
    total: number;
    page: number;
    showMore: boolean;
    groupName?: string;
    type?: string;
    key?: string;
  }

  export default defineComponent({
    name: 'BusinessLine',
    components: {
      BasicTree,
      BasicTable,
      TableAction,
      Divider,
      Icon,
      CopyOutlined,
      BusinessModal,
      BusinessGroupModal,
    },

    setup() {
      const { prefixCls } = useDesign('business-line');

      const treeData = ref<TreeItem[]>([]);
      const isAll = ref(true);
      const filterData = ref<ResultItem[]>([]);
      const currentDeptId = ref(); //当前选中的部门id
      const treeGropId = ref(); // 部门 tree 右键操作的id
      // const go = useGo();
      const { createMessage: msg, createWarningModal, createConfirm } = useMessage();
      const searchParams = ref({});
      const [registerModal, { openModal }] = useModal();
      const [registerGroupModal, { openModal: openGroupModal }] = useModal();
      const businessAuth = ref(2);

      const actionList: TreeActionItem[] = [
        {
          render: (node) => {
            return h(EllipsisOutlined, {
              class: 'ml-2',
              onClick: () => {
                getRightMenuList(node);
              },
            });
          },
        },
      ];

      async function fetch() {
        treeData.value = [];
        let result = (await getBusinessLineGroupList({})) as unknown as TreeItem[];
        treeData.value = formatTreeData(result);
        if (treeData.value.length) {
          // searchParams.value = {
          //   folderId:treeData.value[0].id
          // }
          reload({ searchInfo: { ...unref(searchParams) } });
          setProps({
            searchInfo: { folderId: treeData.value[0].id },
          });
          currentDeptId.value = treeData.value[0].id;
        }
      }
      onMounted(() => {
        fetch();
      });

      const [registerTable, { reload, setProps }] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getBusinessLineList,
        immediate: true,
        columns: lineColumns,
        fetchSetting: {
          listField: 'records',
        },
        rowKey: 'id',
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: lineFormSchema,
        },
        dataSource: [],
        showDragColumn: false,
        showIndexColumn: false,
        bordered: false,
        isTriggerSelect: false,
        showTableSetting: false,
        canResize: false,
        striped: false,
        tableSetting: {
          fullScreen: false,
          redo: false,
          setting: false,
          size: false,
          align: 'right',
        },
        beforeFetch: beforeFetch,
      });
      function beforeFetch(params) {
        // if(treeGropId.value){
        params.folderId = isAll.value ? '' : treeGropId.value;
        // }
      }

      function formatTreeData(list) {
        list &&
          list.map((item) => {
            item.deparCountName = item.departName + '  ' + '(' + item.userCount + ')';
            if (item.children) {
              item.children = formatTreeData(item.children);
            }
          });
        return list;
      }

      //右键菜单
      function getRightMenuList(node: any): ContextMenuItem[] {
        return [
          {
            label: '编辑',
            icon: 'ant-design:edit-filled',
            handler: () => {
              treeGropId.value = node.id;
              openGroupModal(true, {
                isUpdate: true,
                record: {
                  ...node,
                  name: node.label,
                },
              });
            },
          },
          {
            label: '删除',
            icon: 'ant-design:delete-filled',
            handler: () => {
              Modal.confirm({
                title: '提示',
                icon: createVNode(ExclamationCircleOutlined),
                content: createVNode(
                  'div',
                  { style: 'color:red;' },
                  `确认删这个文件夹吗？删除后将无法恢复`,
                ),
                onOk: async () => {
                  treeGropId.value = node.id;
                  let result = await deleteBusinessGroup({ folderIdList: node.id });
                  if (result) {
                    msg.success('操作成功');
                    fetch();
                  }
                },
                onCancel() {},
                class: 'custom-confirm',
              });
            },
          },
        ];
      }
      //点击treeItem
      function handleRowClick() {}
      //迁移
      function handleMove(record) {
        openModal(true, {
          isUpdate: true,
          record: {
            ...record,
          },
        });
      }
      //选择节点
      function onTreeSelect(keys, e) {
        if (!keys.length) return;
        isAll.value = e.node.label == '全部' ? true : false;
        searchParams.value = {
          folderId: keys[0],
        };
        treeGropId.value = keys.length ? keys[0] : '';
        reload({ searchInfo: { ...unref(searchParams), pageNo: 1 } });
      }
      //搜索
      async function handleLoadSearchData(type, page) {
        console.log(type, page);
        msg.success('成功');
      }
      async function searchData(val) {
        console.log(val);
      }
      //新增业务线
      async function handleAdd() {
        const res = await createBusinessLineAuth();
        //res.flag  = 1 有效  = 0 超过数量限限制  = 2 业务线被禁用
        if (res.flag == 1) {
          openModal(true, {
            isUpdate: false,
            record: {},
          });
        } else if (res.flag == 0) {
          message.warning(`只能创建${res.count}个业务线`);
        } else if (res.flag == 2) {
          window.open('https://www.yuque.com/huxin-ch41t/kaifangqian/bmq3kzuavpudvxov');
        }
      }
      function handleEdit(record) {
        if (record.status == 1) {
          createConfirm({
            title: '温馨提示',
            iconType: 'warning',
            content: h(
              'div',
              {
                style: {
                  padding: '8px 0',
                  lineHeight: '1.8',
                  fontSize: '15px',
                  color: '#333',
                },
              },
              [
                h(
                  'div',
                  {
                    style: {
                      // fontWeight: 'bold',
                      marginBottom: '6px',
                    },
                  },
                  '处于启用状态的业务线无法直接配置，请停用后，再进行配置',
                ),
                h(
                  'div',
                  {
                    style: {
                      marginBottom: '6px',
                      color: '#d4380d',
                    },
                  },
                  '停用后，无法使用该业务线发起签署，请确认是否停用？',
                ),
                h(
                  'div',
                  {
                    style: {
                      background: '#fffbe6',
                      padding: '6px 10px',
                      borderRadius: '4px',
                      border: '1px solid #ffe58f',
                    },
                  },
                  '特别提示：配置完成后，请及时启用，以确保业务线可用',
                ),
              ],
            ),
            cancelText: '取消',
            okText: '停用并配置',
            width: 500,
            onOk() {
              // handleStatus(record);
              record.status == 2;
              router.push({
                path: '/businessLine/config',
                query: {
                  __full__: '',
                  from: 'list',
                  signReId: record.id,
                },
              });
            },
            onCancel() {
              Modal.destroyAll();
            },
          });
        } else {
          router.push({
            path: '/businessLine/config',
            query: {
              __full__: '',
              from: 'list',
              signReId: record.id,
            },
          });
        }
      }
      function handleCreateSuccess() {
        reload({ searchInfo: { ...unref(searchParams) } });
      }
      function handleCreatGroupeSuccess() {
        fetch();
      }
      function handleCreateGroup() {
        openGroupModal(true, {
          isUpdate: false,
          record: {},
        });
      }
      async function handleStatus(record) {
        if (record.errorStatus != 2 && record.status == 2) {
          let text = '';
          if (record.errorStatus == 1) {
            text = `组织签章【自动盖章】的签署节点未指定签署位置，请先指定签署位置`;
          }
          if (record.errorStatus == 3) {
            text = '文档填写参数存在部分参数未指定填写方，请先指定';
          }
          if (record.errorStatus == 4) {
            text = `<p>1.文档填写参数存在部分参数未指定填写方，请先指定</p><p>2.文档填写参数存在部分参数未指定填写方，请先指定</p>`;
          }
          createConfirm({
            title: '温馨提示',
            content: text,
            iconType: 'warning',
            okText: '更新',
            onOk() {
              // window.open(import.meta.env.VITE_PUBLIC_PATH +'#/businessLine/config?__full__&from=list&signReId='+ record.id);
              router.push({
                path: '/businessLine/config',
                query: {
                  __full__: '',
                  from: 'list',
                  signReId: record.id,
                },
              });
            },
          });
          return;
        }
        let result = await statusBusinessLine({
          signReId: record.id,
          status: record.status == 1 ? 2 : 1,
        });
        if (result) {
          msg.success('操作成功');
          reload({ searchInfo: { ...unref(searchParams) } });
        }
      }
      async function handleCopy(record) {
        let result = await copyBusinessLine({ signReId: record.id });
        if (result) {
          msg.success('操作成功');
          reload({ searchInfo: { ...unref(searchParams) } });
        }
      }
      function copyToken(record) {
        const range = document.createRange();
        const selection: any = window.getSelection();
        const element = document.createElement('span');
        element.textContent = record.id;
        document.body.appendChild(element);
        range.selectNodeContents(element);
        selection.removeAllRanges();
        selection.addRange(range);
        document.execCommand('copy');
        document.body.removeChild(element);
        message.success('复制成功');
      }
      return {
        treeData,
        registerTable,
        prefixCls,
        filterData,
        isAll,
        getRightMenuList,
        handleRowClick,
        handleLoadSearchData,
        onTreeSelect,
        searchData,
        handleAdd,
        registerModal,
        handleEdit,
        handleCreateSuccess,
        registerGroupModal,
        handleCreatGroupeSuccess,
        handleCreateGroup,
        handleStatus,
        handleCopy,
        handleMove,
        businessAuth,
        actionList,
        copyToken,
      };
    },
  });
</script>
<style lang="less" scoped>
  @import './index.less';
  @prefix-cls: ~'@{namespace}-business-line';
  .@{prefix-cls} {
    &-table {
      flex: 1;
    }
  }
</style>

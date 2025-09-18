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
  <div class="resrun-tree-table">
    <a-spin :spinning="spinning">
      <BasicTree
        ref="basicTree"
        title=""
        :toolbar="false"
        :checkable="false"
        :search="false"
        :toggleSwitch="true"
        :treeData="treeData"
        :beforeRightClick="getRightMenuList"
        @select="onTreeSelect"
      >
        <template #action>
          <a-button
            type="default"
            @click="handleGroupAdd"
            :class="[prefixCls + '-action']"
            v-if="hasPermission(['authcategory:add'])"
          >
            <Icon icon="ant-design:plus-outlined" /> 新增权限组分类
          </a-button>
        </template>
      </BasicTree>
    </a-spin>

    <div :class="prefixCls + '-table'" style="flex-grow: 1">
      <div class="resrun-table-top-header">
        <span class="table-tree-custom-title">{{ currentGroupName }}</span>
        <div :class="prefixCls + '-custom-btn'">
          <a
            href="javascript:;"
            @click="onEditAuthGroup"
            v-if="hasPermission(['authcategory:edit'])"
          >
            <Icon icon="ant-design:safety-certificate-twotone" />
            编辑权限组分类
          </a>
        </div>
      </div>
      <BasicTable
        @register="registerTable"
        @edit-change="onEditChange"
        :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }"
      >
        <template #toolbar>
          <a-button type="primary" @click="handleAuthAdd" v-if="hasPermission(['authgroup:add'])"
            >新建
          </a-button>
          <!-- <a-button type="danger" @click="handleAuthDelete" v-if="checkedKeys.length&&hasPermission(['authgroup:delete'])">删除 </a-button> -->
        </template>
        <template #groupName="{ text, record }">
          <Icon icon="ant-design:safety-certificate-outlined" color="#ea7270" />
          <span style="margin: 0 10px">{{ text }}</span>
          <a-tag v-if="record.groupType === 1 || record.groupType === 2" color="#f50">{{
            record.groupType == 1 ? '全部权限' : '基础权限'
          }}</a-tag>
        </template>
        <template #action="{ record }">
          <a-button type="link" size="small" @click="onRowClick(record)">编辑</a-button>
          <!-- <a-button type="link" v-if="record.groupType===3 || record.groupType===4" size="small" @click="onRowDelete(record)" >删除</a-button> -->
        </template>
      </BasicTable>
    </div>
    <GroupDrawer @register="registerDrawer" @success="handleSuccess" />
    <GroupModal @register="registerModal" @success="handleAuthGroupSuccess" />
    <AuthModal @register="registerAuthModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, onMounted, unref, h, toRaw } from 'vue';
  import { Row, Col, Divider, Menu, Dropdown, MenuItem } from 'ant-design-vue';
  import { DownOutlined, EllipsisOutlined } from '@ant-design/icons-vue';
  import { BasicTree, TreeItem, TreeActionItem, ContextMenuItem } from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { searchFormSchema, tableColumns } from './data';
  import { getAuthGroup, deleteBatchAuth } from '/@/api/auth/group';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { transferRoleType } from '/@/utils/system';
  import GroupDrawer from './drawer/GroupDrawer.vue';
  import GroupModal from './modal/GroupModal.vue';
  import AuthModal from './modal/AuthModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  import { BasicTable, useTable, TableAction, EditRecordRow } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  export default defineComponent({
    name: '权限组管理',
    components: {
      BasicTree,
      Row,
      Col,
      BasicTable,
      TableAction,
      Divider,
      Icon,
      GroupDrawer,
      GroupModal,
      Dropdown,
      DownOutlined,
      Menu,
      MenuItem,
      EllipsisOutlined,
      AuthModal,
    },
    props: {
      appId: {
        type: String,
      },
    },
    setup(props, {}) {
      const { prefixCls } = useDesign('auth-group');
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerModal, { openModal }] = useModal();
      const [registerAuthModal, { openModal: openAuthModal }] = useModal();
      const { createMessage: msg } = useMessage();
      const [registerDrawer, { openDrawer }] = useDrawer();

      const canDrag = ref(false);
      const appId = ref(props.appId);
      const spinning = ref(false);
      const currentGroupId = ref();
      const currentGroupName = ref();
      const { hasPermission } = usePermission();
      const basicTree = ref(null);
      const panelShow = ref(true);
      const treeData = ref<TreeItem[]>([]);

      async function fetch() {
        await loadTreeData();
        if (treeData.value.length) {
          setProps({
            searchInfo: { parentId: treeData.value[0].id, tenantAppId: unref(appId) },
          });
          reload();
          currentGroupId.value = treeData.value[0].id;
          currentGroupName.value = treeData.value[0].groupName;
        }
      }
      onMounted(() => {
        fetch();
      });
      async function loadTreeData() {
        spinning.value = true;
        treeData.value = []; //tree缓存问题
        treeData.value = (await getAuthGroup({
          tenantAppId: unref(appId),
        })) as unknown as TreeItem[];
        treeData.value.map((item) => {
          item.title = item.groupName;
          item.key = item.id;
          // item.icon = 'ant-design:folder-twotone';
        });
        console.log(treeData.value.length, '数据2');
        spinning.value = false;
      }
      const [registerTable, { reload, setProps }] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getAuthGroup,
        columns: tableColumns,
        immediate: false,
        useSearchForm: false,
        showDragColumn: false,
        showIndexColumn: false,
        isTriggerSelect: false,
        bordered: false,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
        },
        canResize: false,
        striped: false,
        showTableSetting: false,
        tableSetting: { fullScreen: false, redo: true, setting: false, size: false },
      });

      onMounted(() => {
        console.log(basicTree.value, '----------iiii--');
      });
      function handleMenuClick() {}
      function handleGroupAdd() {
        openModal(true, {
          isUpdate: false,
          appId: unref(appId),
        });
      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys, e) {
        console.log(keys, e);
        if (keys.length) {
          currentGroupId.value = keys[0];
          currentGroupName.value = e.node.groupName;
          setProps({
            searchInfo: { parentId: unref(currentGroupId), tenantAppId: unref(appId) },
          });
          reload();
        }
      }

      async function handleAuthDelete() {
        if (!unref(checkedKeys).length) {
          msg.warning('请选择操作数据');
          return;
        }
        let params = {
          ids: unref(checkedKeys).join(','),
        };
        let result = await deleteBatchAuth(params);
        if (result.success) {
          msg.success('操作成功');
          reload();
          checkedKeys.value = [];
        }
      }
      async function onRowDelete(row) {
        let params = {
          ids: row.id,
        };
        let result = await deleteBatchAuth(params);
        if (result) {
          msg.success('删除成功');
          reload();
        }
      }

      function handleAuthAdd() {
        openAuthModal(true, {
          isUpdate: false,
          appId: unref(appId),
        });
      }
      function handTableDataSet() {
        canDrag.value = !unref(canDrag);
        setProps({
          showDragColumn: unref(canDrag),
        });
      }
      function handleRowClick() {
        openDrawer(true, {
          isUpdate: false,
          appId: unref(appId),
        });
      }
      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }
      function onRowClick(record: EditRecordRow) {
        if (!hasPermission(['authcategory:edit'])) {
          msg.warning('没有该操作权限');
          return;
        }
        openDrawer(true, {
          isUpdate: true,
          appId: unref(appId),
          record: {
            ...record,
          },
        });
      }
      function handleEdit(record?: EditRecordRow) {
        if (record) {
          openModal(true, {
            isUpdate: true,
            appId: unref(appId),
            record: {
              ...record,
            },
          });
        } else {
          openModal(true, {
            isUpdate: false,
          });
        }
      }

      function handleSuccess() {
        reload();
      }
      function handleAuthGroupSuccess() {
        console.log('00000', '更新树');
        loadTreeData();
      }

      function createIcon({ level }) {
        if (level === 1) {
          return 'ion:git-compare-outline';
        }
        if (level === 2) {
          return 'ion:home';
        }
        if (level === 3) {
          return 'ion:airplane';
        }
        return '';
      }
      function loadManageName(level: string | number) {
        switch (level) {
          case 1:
            return '主管';
          case 2:
            return '经理';
          default:
            return '';
        }
      }
      function onEditAuthGroup() {
        openModal(true, {
          record: {
            id: unref(currentGroupId),
          },
          isUpdate: true,
        });
      }
      function getRightMenuList(node: any, e?): ContextMenuItem[] {
        console.log(toRaw(node), e, '----ssss---');
        return [
          {
            label: '编辑权限组分类',
            handler: () => {
              openModal(true, {
                record: {
                  ...toRaw(node),
                },
                isUpdate: true,
              });
            },
            auth: ['authcategory:edit'],
            // icon: 'ant-design:edit-filled',
          },
        ];
      }
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

      return {
        onRowDelete,
        onEditAuthGroup,
        treeData,
        getRightMenuList,
        handleAuthAdd,
        handleAuthDelete,
        registerModal,
        registerDrawer,
        registerAuthModal,
        onTreeSelect,
        actionList,
        canDrag,
        loadManageName,
        transferRoleType,
        createIcon,
        hasPermission,
        prefixCls,
        handTableDataSet,
        handleSuccess,
        handleAuthGroupSuccess,
        registerTable,
        handleEdit,
        panelShow,
        handleMenuClick,
        onEditChange,
        checkedKeys,
        currentGroupName,
        onSelectChange,
        handleGroupAdd,
        handleRowClick,
        onRowClick,
        spinning,
      };
    },
  });
</script>

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

<script lang="ts" setup>
  import { defineComponent, ref, unref, computed, onMounted } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTable, useTable, EditRecordRow } from '/@/components/Table';

  import { silentOpenApi, silentQueryRecordApi } from './api';

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false, width: 800, canFullscreen: false, draggable: false });
    reload();
  });

  const [registerTable, { reload, setProps }] = useTable({
    title: '',
    titleHelpMessage: [],
    api: silentQueryRecordApi,
    columns: [
      {
        title: '开通时间',
        dataIndex: 'openTime',
      },
      {
        title: '截止日期',
        dataIndex: 'deadline',
        slots: { customRender: 'deadline' },
      },
      {
        title: '开通人',
        dataIndex: 'contactName',
      },
      {
        title: '通知邮箱',
        dataIndex: 'email',
      },
      {
        title: '状态',
        dataIndex: 'status',
        slots: { customRender: 'status' },
      },
    ],
    immediate: false,
    fetchSetting: {
      listField: 'silentSignAuthorizes',
    },
    rowKey: 'id',
    useSearchForm: false,
    showIndexColumn: false,
    canResize: false,
    isTriggerSelect: false,
    striped: false,
    showTableSetting: false,
    // tableSetting: { fullScreen: false, redo: true, setting: false, size: false },
    pagination: false,
  });
  onMounted(() => {});
</script>
<template>
  <BasicModal v-bind="$attrs" @register="registerModal" title="开通记录">
    <BasicTable @register="registerTable">
      <template #deadline="{ record }">
        <span v-if="record.deadline == 'FOREVER_VALID'">长期有效</span>
        <span v-else>{{ record.deadline }}</span>
      </template>
      <template #status="{ record }">
        <a-tag :bordered="true" color="processing" v-if="record.status === 1">有效</a-tag>
        <a-tag v-else>失效</a-tag>
      </template>
    </BasicTable>
  </BasicModal>
</template>

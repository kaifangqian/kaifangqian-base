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
    <!-- <BasicTree
      ref="basicTree"
      title=""
      :toolbar="true"
      :checkable="false"
      search
      :toggleSwitch="true"
      :fieldNames="{ title: 'name', key: 'id' }"
      :treeData="treeData"
      @changePanelShow="changePanelShow"
      @select="onTreeSelect"
    >
    <template #action>
        <a-button type="default" @click="handleAnnouncerType" :class="[prefixCls+'-action']">
          <Icon icon="ant-design:container-outlined" />
        新增公告类型</a-button>
    </template>
    </BasicTree> -->
    <BasicTable @register="registerTable" @edit-change="onEditChange" 
    :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
      <template #action="{ record }">
          <a-button type="link"  size="small" @click="handleDetail(record)">查看</a-button>
      </template>
      <template #toolbar>
        <a-button type="primary" @click="handleRead"  v-if="hasPermission(['myAnnouncement:readBatch'])">标注已读 </a-button>
        <a-button type="primary" @click="handleReadAll"  v-if="hasPermission(['myAnnouncement:readAll'])">全部标注已读 </a-button>
      </template>
      <template #headerTop  v-if="checkedKeys.length > 0">
          <a-alert type="info" show-icon>
            <template #message>
              <template v-if="checkedKeys.length > 0">
                <span>已选中{{ checkedKeys.length }}条记录</span>
                <a-button type="link" @click="checkedKeys = []" size="small">清空</a-button>
              </template>
              <template v-else>
                <span>未选中任何项目</span>
              </template>
            </template>
          </a-alert>
      </template>
      <template #isRead="{record}">
        <span>{{record.isRead?'已读':'未读'}}</span>
      </template>
      <template #priority="{record}">
        <span>{{loadPriorityName(record.priority)}}</span>
      </template>
    </BasicTable>
    <AnnounceDetailModal @register="registerModal" @success="handleSuccess" />
    <AnnounceTypeModal @register="registerTypeModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref,onMounted ,unref} from 'vue';
  import { Row, Col , Divider,Menu ,Dropdown, MenuItem} from 'ant-design-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { BasicTree,TreeItem} from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { myAnnouncementColumns,myAnnounceSearchFormSchema } from './data';
  import { getMyAnnounceList,myAnnounceSendRead,myAnnounceSendReadAll } from '/@/api/announce';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { transferRoleType } from '/@/utils/system';
  import { useRoute } from 'vue-router';


  import AnnounceDetailModal from './modal/AnnounceDetailModal.vue';
  import AnnounceTypeModal from './modal/AnnounceTypeModal.vue';

  import {
    BasicTable,
    useTable,
    TableAction,
    EditRecordRow,
  } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    name:'MyAnnounce',
    components: { BasicTree, Row, Col,BasicTable,TableAction, Divider,Icon,AnnounceDetailModal,AnnounceTypeModal,
      Dropdown,
      DownOutlined,
      Menu,
      MenuItem
    },
    emits:['changePanelShow'],
    setup(_,{emit}) {
      const { prefixCls } = useDesign('announcement');
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerModal, { openModal:openDetailModal }] = useModal();
      const [registerTypeModal, { openModal:openTypeModal }] = useModal();

      const { createMessage: msg } = useMessage();
      const currentEditKeyRef = ref('');
      const canDrag = ref(false)
      const { hasPermission } = usePermission();
      const basicTree = ref(null)
      const panelShow = ref(true)
      const treeData = ref<TreeItem[]>([]);
      const route = useRoute();
     

      // async function fetch() {
      //   // treeData.value = (await getAnnounceTreeList()) as unknown as TreeItem[];
      // }
      onMounted(() => {
        // fetch();
        console.log(unref(route).params.infoId,'参数')
        if(unref(route).params.infoId && unref(route).params.infoId !== ':infoId'){
          openDetailModal(true,{
            isUpdate:false,
            record:{
            id:unref(route).params.infoId
            }
          })
        }
      });
      const [registerTable,{ reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getMyAnnounceList,
        columns: myAnnouncementColumns,
        useSearchForm: true,
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        rowKey: 'id',
        formConfig: {
          labelWidth: 120,
          schemas:myAnnounceSearchFormSchema
        },
        fetchSetting:{
          listField:'records'
        },
        isTriggerSelect:false,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
      

      onMounted(()=>{
        console.log(basicTree.value,'----------iiii--')
      })
      function handleMenuClick(){

      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys,e){
        console.log(keys,e)
      }
      function handleCancel(record: EditRecordRow) {
        currentEditKeyRef.value = '';
        record.onEdit?.(false, false);
      }
      function handTableDataSet(){
        canDrag.value = !unref(canDrag)
        setProps({
          showDragColumn:unref(canDrag)
        })
      }
      function changePanelShow(val){
         emit('changePanelShow',val)
      }
      async function handleRead(id?:string){
        if(!unref(checkedKeys).length && !id) {
          msg.warning('请勾选至少一条数据')
          return  
        }
        let result = await myAnnounceSendRead({ids:unref(checkedKeys).join(',') || id});
        if(result){
          if(!id){  // 点击查看不需要提示；
           msg.success('标记成功');
          }
          reload();
          checkedKeys.value = [];
        }
      }
      async function handleReadAll(){
        let result = await myAnnounceSendReadAll();
        if(result){
          msg.success('标记成功');
          reload()
        }
      }
      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }

      function handleSuccess() {
        reload();
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
      function loadPriorityName(level:string){
        switch(level){
          case 'L':
            return '低';
          case 'M':
            return '中';
          case 'H':
            return '高';
          default:
            return ''
        }
      }
     
      function handleAnnouncerType(){
        openTypeModal(true,{
          isUpdate:false
        })
      }
      function handleDetail(record){
        openDetailModal(true,{
          isUpdate:false,
          record:{
           ...record,
           id:record.anntId
          }
        })
        handleRead(record.id)
      }
      return { 
        handleRead,
        handleReadAll,
        handleDetail,
        loadPriorityName,
        handleAnnouncerType,
        treeData,
        registerModal,
        onTreeSelect, 
        canDrag,
        transferRoleType,
        createIcon ,
        hasPermission,
        prefixCls,
        handTableDataSet,
        changePanelShow,
        handleSuccess,
        registerTable,
        panelShow,
        handleMenuClick,
        onEditChange,
        checkedKeys,
        onSelectChange,
        registerTypeModal
      }
    },
  });
</script>

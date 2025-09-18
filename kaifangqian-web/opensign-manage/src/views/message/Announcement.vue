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
    <BasicTree
      ref="basicTree"
      title=""
      :toolbar="false"
      :checkable="false"
      :search="false"
      :toggleSwitch="true"
      :fieldNames="{ title: 'name', key: 'id' }"
      :treeData="treeData"
      :beforeRightClick="getRightMenuList"
      @changePanelShow="changePanelShow"
      @select="onTreeSelect"
    >
    <template #action>
        <a-button type="default" @click="handleAnnouncerType" :class="[prefixCls+'-action']">
          <Icon icon="ant-design:container-outlined" />
        新增公告类型</a-button>
    </template>
    </BasicTree>
    <div :class="prefixCls+'-table'">
        <div class="resrun-table-top-header">
              <span class="table-tree-custom-title">{{treeName}}</span>
              <div :class="prefixCls+'-custom-btn'">
                  <a href="javascript:;" @click="onEditTemplate">
                    <Icon icon="ant-design:edit-filled"/>
                    编辑
                  </a>
                  <Divider type="vertical"/>
                  <a href="javascript:;" @click="handleTreeDelete(currentTreeId)">
                    <Icon icon="ant-design:delete-filled"/>
                    删除
                  </a>
              </div>  
          </div>
        <BasicTable @register="registerTable" @edit-change="onEditChange">
          <template #action="{ record }">
              <span v-if="hasPermission(['announcement:reovke'])">
              <a-button type="link"  v-if="record.sendStatus === '2' || record.sendStatus === '1' "  size="small" @click="handleSendStatus(record.id,record.sendStatus)">撤销</a-button>
              </span>
              <span v-if="hasPermission(['announcement:release'])">
                <a-button type="link"  v-if="record.sendStatus == '0' || record.sendStatus == '3' "  size="small" @click="handleSendStatus(record.id,record.sendStatus)">发布</a-button>
              </span>
              <Divider type="vertical" />
              <Dropdown   :trigger="['hover']">
                <template #overlay>
                  
                  <Menu @click="handleMenuClick">
                      <MenuItem v-if="hasPermission(['announcement:edit'])">
                          <a href="javascript:;"  @click="handleEdit(record)" >编辑</a>
                      </MenuItem>
                      <MenuItem v-if="hasPermission(['announcement:info'])">
                          <a href="javascript:;" @click="handleDetail(record)">查看</a>
                      </MenuItem>
                      <MenuItem v-if="hasPermission(['announcement:delete'])">
                      <a-popconfirm
                          title="确定删除？"
                          ok-text="是"
                          cancel-text="否"
                          @confirm="handleDelete(record)"
                        >
                          <a href="javascript:;">删除</a>
                        </a-popconfirm>
                        
                      </MenuItem>
                      <!-- <MenuItem v-if="record.sendStatus === '1'" >
                          <a href="javascript:;"  @click="handleSendStatus(record.id,record.sendStatus)">撤销</a>
                      </MenuItem> -->
                  </Menu>
                </template>
              <a-button type="link"  size="small">更多<DownOutlined /></a-button>
            </Dropdown>
          </template>
          <template #toolbar>
            <a-button type="primary" @click="handleAdd" v-if="hasPermission(['announcement:add'])">新增 </a-button>
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
          <template #priority="{record}">
            <span>{{loadPriorityName(record.priority)}}</span>
          </template>
          <template #msgType="{record}">
            <span>{{loadMsgTypeName(record.msgType)}}</span>
          </template>
          <template #sendStatus="{record}">
            <span>{{loadSendStatusName(record.sendStatus)}}</span>
          </template>
        </BasicTable>
    </div>
    <AnnounceFormModal @register="registerModal" @success="handleSuccess" />
    <AnnounceDetailModal @register="registerDetailModal" @success="handleDetailSuccess" />
    <AnnounceTypeModal @register="registerTypeModal" @success="handleTypeSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref,onMounted ,unref, h, toRaw} from 'vue';
  import { Row, Col , Divider,Menu ,Dropdown, MenuItem} from 'ant-design-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { BasicTree, TreeItem, TreeActionItem, ContextMenuItem} from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { announcementColumns,announceSearchFormSchema } from './data';
  import { getAnnounceTree, announceList, announceDeleteBatch,announceRelease,announceRevoke ,announceTreeDelete} from '/@/api/announce';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { transferRoleType } from '/@/utils/system';
  import { EllipsisOutlined } from '@ant-design/icons-vue';
 
  import AnnounceFormModal from './modal/AnnounceFormModal.vue';
  import AnnounceTypeModal from './modal/AnnounceTypeModal.vue';
  import AnnounceDetailModal from './modal/AnnounceDetailModal.vue';

  import {
    BasicTable,
    useTable,
    TableAction,
    EditRecordRow,
  } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    name:'公告发布',
    components: { BasicTree, Row, Col,BasicTable,TableAction, Divider,Icon,AnnounceFormModal,AnnounceTypeModal,
      Dropdown,
      DownOutlined,
      Menu,
      MenuItem,
      EllipsisOutlined,
      AnnounceDetailModal
    },
    emits:['changePanelShow'],
    setup(_,{emit}) {
      const { prefixCls } = useDesign('announcement');
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerModal, { openModal }] = useModal();
      const [registerTypeModal, { openModal:openTypeModal }] = useModal();
      const [registerDetailModal, { openModal:openDetailModal }] = useModal();
      const currentTreeId = ref('');
      const treeName = ref('');
      const parentId = ref();

      const { createMessage: msg } = useMessage();
      const canDrag = ref(false)
      const { hasPermission } = usePermission();
      const basicTree = ref(null);
      const panelShow = ref(true);
      const treeData = ref<TreeItem[]>([]);
      const actionList: TreeActionItem[] = [
        {
          render: (node) => {
            return h(EllipsisOutlined, {
              class: 'ml-2',
              onClick: () => {
                showMenu(node);
              },
            });
          },
        },
      ];

      async function fetch() {
        treeData.value = [];
        let result = (await getAnnounceTree({})) as unknown as TreeItem[];
     
        treeData.value = setArrayItemProperty(result)
        if(!treeData.value.length) return;
        currentTreeId.value = treeData.value[0].id;
        treeName.value = treeData.value[0].name;
        reload({searchInfo:{announcementType:unref(currentTreeId)}});
      }
      onMounted(() => {
        fetch();
      });
      const [registerTable,  { reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: announceList,
        columns: announcementColumns,
        useSearchForm: true,
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        immediate:false,
        formConfig: {
          labelWidth: 120,
          schemas:announceSearchFormSchema
        },
        fetchSetting:{
          listField:'records'
        },
        rowKey: 'id',
        beforeFetch:handleFetch,
        isTriggerSelect:false,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });
      function setArrayItemProperty(list) {
        list.map(item=>{
         item.typeName = item.name
          if(item.children){
            setArrayItemProperty(item.children)
          }
        })
        return list;
      }
      function handleMenuClick(){

      }
      function handleFetch(params){
        params.announcementType = unref(currentTreeId);
      }
      
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys,e){
        console.log(keys,e)
        if(!keys.length) return;
        treeName.value = e.node.typeName;
        parentId.value = e.node.parentId;
        currentTreeId.value = keys[0];
        reload({searchInfo:{announcementType:unref(currentTreeId)}});
      }
      function handleCreateUser(){

      }
      function handTableDataSet(){
        canDrag.value = !unref(canDrag)
        setProps({
          showDragColumn:unref(canDrag)
        })
      }
      function handleEdit(record:EditRecordRow){
          openModal(true,{
            isUpdate:true,
              record:{
                ...record
            }
          })
      }
      function changePanelShow(val){
         emit('changePanelShow',val)
      }
      function showMenu(node: any) {
        getRightMenuList(node)
      }
      function onEditTemplate() {
        openTypeModal(true,{
          record:{
            parentId:unref(parentId),
            id:unref(currentTreeId),
            typeName:unref(treeName)
          },
          isUpdate:true
        })
      }
      function getRightMenuList(node: any): ContextMenuItem[] {
        return [
          {
            label: '编辑',
            handler: () => {
              openTypeModal(true,{
                record:{
                  ...toRaw(node),
                },
                isUpdate:true
              })
            },
          },
          {
            label: '删除',
            handler: () => {
              handleTreeDelete(toRaw(node).id)
            },
          },
        ]
      }
      
      async function handleTreeDelete(id){
        let result = await announceTreeDelete({ids:id});
        if(result){
          msg.success('删除成功');
          fetch()
        }
      }
      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }
      function handleDetail(record){
        openDetailModal(true,{
          isUpdate:false,
          record:{
           ...record
          }
        })
      }
      function handleAdd(){
        openModal(true,{
            isUpdate:false
          })
      }
      function handleDetailSuccess(){
      }

      function handleSuccess() {
        reload();
      }
      function handleTypeSuccess(){
        fetch()
      }
      function handleAnnouncerType(){
        openTypeModal(true,{
          isUpdate:false
        })
      }
      async function handleDelete(record){
        let result = await announceDeleteBatch({Ids:record.id});
        if(result){
          reload({searchInfo:{announcementType:unref(currentTreeId)}});
          msg.success('删除成功')
        }
      }
      async function handleSendStatus(id,status){
        let result = null;
        if(status==='0' || status==='3'){
          result = await announceRelease({id});
        }
        if(status==='1' || status==='2'){
          result = await announceRevoke({id});
        }
        if(result){
          msg.success('操作成功');
          reload({searchInfo:{announcementType:unref(currentTreeId)}});
        }
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
      function loadMsgTypeName(level:string){
        switch(level){
          case 'USER':
            return '指定用户';
          case 'ALL':
            return '全体用户';
          default:
            return ''
        }
      }
      function loadSendStatusName(level:string){
        switch(level){
          case '0':
            return '未发布';
          case '1':
            return '发布中';
          case '2':
            return '已发布';
          case '3':
            return '已撤销';
          default:
            return ''
        }
      }
      return { 
        treeName,
        currentTreeId,
        handleTreeDelete,
        onEditTemplate,
        handleDelete,
        handleDetailSuccess,
        registerDetailModal,
        handleDetail,
        handleSendStatus,
        loadPriorityName,
        loadMsgTypeName,
        loadSendStatusName,
        basicTree,
        actionList,
        getRightMenuList,
        registerTypeModal,
        handleAnnouncerType,
        treeData,
        registerModal,
        onTreeSelect, 
        canDrag,
        transferRoleType,
        hasPermission,
        prefixCls,
        handTableDataSet,
        changePanelShow,
        handleSuccess,
        registerTable,
        handleEdit,
        panelShow,
        handleMenuClick,
        onEditChange,
        handleCreateUser,
        checkedKeys,
        onSelectChange,
        handleTypeSuccess,
        handleAdd
      }
    },
  });
</script>

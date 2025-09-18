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
  <div class="resrun-tree-table" style="flex:0 0 100%">
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
            <a-button type="default" @click="handleGroupAdd" :class="[prefixCls+'-action']">
              <Icon icon="ant-design:plus-outlined" />  新增分组
            </a-button>
        </template>
      </BasicTree>
    </a-spin>

    <div :class="prefixCls+'-table'" style="flex-grow:1">
      <div class="resrun-table-top-header">
            <span class="table-tree-custom-title">{{currentGroupName}}</span>
            <div :class="prefixCls+'-custom-btn'">
                  <a href="javascript:;" @click="onEditAuthGroup">
                    <Icon icon="ant-design:safety-certificate-twotone"/>
                    编辑分类
                  </a>
              </div>  
        </div>
        <BasicTable @register="registerTable" @edit-change="onEditChange" 
          :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
          <template #toolbar>
            <a-button type="primary" @click="handleAuthAdd" >新增 </a-button>
          </template>
          <template #title="{record}">
            <a-badge-ribbon text="置顶" color="red"  v-if="record.upFlag">
              <span>{{ record.title }}</span>
            </a-badge-ribbon>
            <span v-if="!record.upFlag">{{ record.title }}</span>

          </template>
          <template #status="{record}">
           <span>{{ loadStatus(record.status) }}</span>
          </template>
          <template #action="{ record }">
                <a-button type="link"  size="small" @click="onRowPreview(record)">预览</a-button>
                <a-divider type="vertical"></a-divider>
                <a-button type="link"  size="small" @click="onRowEdit(record)">编辑</a-button>
                <a-divider type="vertical"></a-divider>
                <a-dropdown size="small">
                  <a-button type="link"  size="small">更多</a-button>
                  <template #overlay>
                    <a-menu>
                      <a-menu-item>
                        <a-button type="link"  size="small" @click="onRowUpdate(record)" >{{record.status==1?'下线':'发布'}}</a-button>
                      </a-menu-item>
                      <a-menu-item>
                        <a-button type="link"  size="small" @click="onRoweUp(record)" >{{record.upFlag?'取消置顶':'置顶'}}</a-button>
                      </a-menu-item>
                      <a-menu-item>
                        <a-button type="link"  size="small" @click="onRowMove(record)">移动至</a-button>
                      </a-menu-item>
                      <a-menu-item>
                        <a-popconfirm
                          title="确定删除该文章？"
                          ok-text="是"
                          cancel-text="否"
                          @confirm="onRowDelete(record)"
                        >
                        <a-button type="link" size="small" danger >删除</a-button>
                        </a-popconfirm>
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
               
          </template>
        </BasicTable>
    </div>
    <DynamicGroupModal @register="registerModal" @success="handleAuthGroupSuccess" />
    <DynamicModal @register="registerArticleModal" @success="handleSuccess" />
    <PreviewModal @register="registerPreviewModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref,onMounted ,unref,h,toRaw} from 'vue';
  import { Row, Col , Divider,Menu ,Dropdown, MenuItem,} from 'ant-design-vue';
  import { DownOutlined, EllipsisOutlined } from '@ant-design/icons-vue';
  import { BasicTree,TreeItem,TreeActionItem,ContextMenuItem} from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { searchFormSchema,tableColumns, } from './data';
  import { getArticleGroupList,deleteArticleGroup, getArticleList, deleteArticle, editArticle} from '/@/api/article'; 
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import DynamicGroupModal from './modal/DynamicGroupModal.vue';
  import DynamicModal from './modal/DynamicModal.vue';
  import PreviewModal from './modal/PreviewModal.vue';
  import { useMessage } from '/@/hooks/web/useMessage';

  import {
    BasicTable,
    useTable,
    TableAction,
    EditRecordRow,
  } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  export default defineComponent({
    name:'文章管理',
    props:{
      appId:{
        type:String
      }
    },
    components: { 
      BasicTree, 
      Row, 
      Col,
      BasicTable,TableAction, Divider,Icon,
      Dropdown,
      DownOutlined,
      Menu,
      MenuItem,
      EllipsisOutlined,
      DynamicModal,
      DynamicGroupModal,
      PreviewModal
    },
    setup(props,{}) {
      const { prefixCls } = useDesign('auth-group');
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerModal, { openModal }] = useModal();
      const [registerArticleModal, { openModal:openArticleModal }] = useModal();
      const [registerPreviewModal, { openModal:openPreviewModal }] = useModal();
      const { createMessage: msg } = useMessage();
      const [registerDrawer, { openDrawer}] = useDrawer();

      const canDrag = ref(false);
      const appId = ref(props.appId);
      const spinning = ref(false);
      const currentGroupId = ref()
      const currentGroupName = ref()
      const { hasPermission } = usePermission();
      const basicTree = ref(null)
      const panelShow = ref(true)
      const treeData = ref<TreeItem[]>([]);

      async function fetch() {
        await loadTreeData();
        if( treeData.value.length){
            setProps({
                searchInfo:{typeId:treeData.value[0].id}
            });
            reload();
            currentGroupId.value = treeData.value[0].id;
            currentGroupName.value = treeData.value[0].typeName;
          }
      }
      onMounted(() => {
        fetch();
      });
      async function loadTreeData(){
        spinning.value = true;
        treeData.value = [];  //tree缓存问题
        let result =  await getArticleGroupList({})
        treeData.value = result.records;
        treeData.value.map(item=>{
          item.title = item.typeName;
          item.key = item.id;
        })
        spinning.value = false;
      }
      const [registerTable,{ reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getArticleList,
        columns: tableColumns,
        immediate:false,
        useSearchForm: true,
        showDragColumn:false,
        showIndexColumn: false,
        isTriggerSelect:false,
        bordered: false,
        fetchSetting:{
          listField:'records'
        },
        rowKey:'id',
        formConfig: {
          labelWidth: 120,
          schemas:searchFormSchema
        },
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false,align:'right'},
      });
      

      onMounted(()=>{
        console.log(basicTree.value,'----------iiii--')
      })
      function handleMenuClick(){

      }
      function handleGroupAdd(){
          openModal(true, {
            isUpdate: false,
            appId:unref(appId)
          });
      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys,e){
        if(keys.length){
          currentGroupId.value = keys[0];
          currentGroupName.value = e.node.typeName;
          setProps({
            searchInfo:{typeId:unref(currentGroupId)}
          });
          reload();
        }
      }
      function onRowPreview (row){
        openPreviewModal(true,{
          isUpdate:false,
          record:{
            ...row
          }
        })
      }
      async function onRowDelete(row){
        let params = {
          id:row.id
        }
        let result = await deleteArticle(params);
        if(result){
          msg.success('删除成功');
          
          reload({
            searchInfo:{typeId:unref(currentGroupId)}
          });
        } 
      }
        
      function handleAuthAdd(){
          openArticleModal(true,{
            isUpdate:false,
          })
      }
      function handleRowClick(){
        openDrawer(true,{
          isUpdate:false,
          appId:unref(appId)
        })
      }
      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }
      function onRowEdit(record: EditRecordRow) {
        openArticleModal(true,{
          isUpdate:true,
          record:{
              ...record
          }
        })
      }
      
      function handleSuccess() {
        reload({
            searchInfo:{typeId:unref(currentGroupId)}
          });
      }
      function handleAuthGroupSuccess() {
        console.log('00000','更新树')
        loadTreeData();
      }
      function loadStatus(status){
        switch(status){
          case 0:
            return '草稿';
          case 1:
            return '已发布';
          case 2:
            return '已下架';
          default:
            return ''

        }
      }

     
      function onEditAuthGroup(){
        openModal(true,{
            record:{
              id:unref(currentGroupId)
            },
            isUpdate:true
          })
      }
      function getRightMenuList(node: any,e?): ContextMenuItem[] {
        return [
          {
            label: '编辑',
            handler: () => {
              openModal(true,{
                record:{
                  ...toRaw(node)
                },
                isUpdate:true
              })
            },
          },
        ];
      }
      async function onRoweUp(row){
        let result = await editArticle({id:row.id,upFlag:!row.upFlag,status:row.status});
        if(result){
          msg.success('操作成功');
          reload({
            searchInfo:{typeId:unref(currentGroupId)}
          })
        }
      }
      async function onRowUpdate(row){
        let result = await editArticle({id:row.id,status:row.status==0 || row.status==2?1:2});
        if(result){
          msg.success('操作成功');
          reload({
            searchInfo:{typeId:unref(currentGroupId)}
          })
        }
      }
      function onRowMove(row){
        openArticleModal(true,{
          isUpdate:true,
          record:{
            ...row
          }
        })
      }
     
      return {
        onRowPreview,
        loadStatus,
        onRowDelete, 
        onRowMove,
        onEditAuthGroup,
        treeData,
        getRightMenuList,
        handleAuthAdd,
        registerModal,
        registerPreviewModal,
        registerDrawer,
        registerArticleModal,
        onTreeSelect, 
        canDrag,
        hasPermission,
        prefixCls,
        handleSuccess,
        onRoweUp,
        handleAuthGroupSuccess,
        registerTable,
        panelShow,
        handleMenuClick,
        onEditChange,
        checkedKeys,
        currentGroupName,
        onSelectChange,
        handleGroupAdd,
        handleRowClick,
        onRowEdit,
        spinning,
        onRowUpdate
      }
    },
  });
</script>

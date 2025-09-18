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
        :fieldNames="{title: 'name', key: 'id', children:'children', }"
        :treeData="treeData"
        @changePanelShow="changePanelShow"
        :beforeRightClick="getRightMenuList"
        @select="onTreeSelect"
      >
      <template #action>
          <a-button type="default" @click="handleTemplateType" :class="[prefixCls+'-action']">
            <Icon icon="ant-design:container-outlined" />  新增模板类型
          </a-button>
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
        <BasicTable @register="registerTable" @edit-change="onEditChange" :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
            <template #action="{ record }">
                <a-button type="link"  size="small" @click="handleEdit(record)" v-if="hasPermission(['template:edit'])">编辑</a-button>
            </template>
            <template #toolbar>
              <a-button type="primary" @click="handleAdd" v-if="hasPermission(['template:add'])">新增 </a-button>
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
        </BasicTable>
      </div>
    <TemplateSendModal @register="registerSendModal" @success="handleSuccess" />
    <TemplateModal @register="registerModal" @success="handleSuccess" />
    <TemplateTypeModal @register="registerTemplateTypeModal" @success="handleTreeSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent,ref,onMounted ,unref,toRaw,h} from 'vue';
  import { Row, Col , Divider,Menu ,Dropdown, MenuItem} from 'ant-design-vue';
  import { DownOutlined } from '@ant-design/icons-vue';
  import { EllipsisOutlined } from '@ant-design/icons-vue';
  import { BasicTree,TreeItem,ContextMenuItem, TreeActionItem} from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { templateColumns,tplSearchFormSchema } from './data';
  import { getMessageList,getTemplateTypeList, deleteTemplateType } from '/@/api/message'; 
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useModal } from '/@/components/Modal';
  import { transferRoleType } from '/@/utils/system';
  import { cloneDeep } from 'lodash-es';

  import TemplateSendModal from './modal/TemplateSendModal.vue';
  import TemplateModal from './modal/TemplateModal.vue';
  import TemplateTypeModal from './modal/TemplateTypeModal.vue';

  import {
    BasicTable,
    useTable,
    TableAction,
    BasicColumn,
    ActionItem,
    EditRecordRow,
  } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useMessage } from '/@/hooks/web/useMessage';
  export default defineComponent({
    name:'消息模板',
    components: { 
      BasicTree, Row, Col,BasicTable,TableAction, Divider,Icon,
      TemplateSendModal,TemplateModal,TemplateTypeModal,
      Dropdown,
      DownOutlined,
      Menu,
     MenuItem,
     EllipsisOutlined
    },
    emits:['changePanelShow'],
    setup(_,{emit}) {
      const { prefixCls } = useDesign('template');
      const checkedKeys = ref<Array<string | number>>([]);
      const [registerModal, { openModal }] = useModal();
      const [registerSendModal, { openModal:openSendModal }] = useModal();
      const [registerTemplateTypeModal, { openModal:openTypeModal }] = useModal();

      const { createMessage: msg } = useMessage();
      const currentEditKeyRef = ref('');
      const canDrag = ref(false);
      const { hasPermission } = usePermission();
      const basicTree = ref(null);
      const panelShow = ref(true);
      const treeName = ref('');
      const currentTreeId = ref();
      const parentId = ref();
      const treeData = ref<TreeItem[]>([]);

      async function fetch() {
        treeData.value = [];
        let result = (await getTemplateTypeList({})) as unknown as TreeItem[];
          // result.map(item=>{
          //   item.title = item.name
          // })
        treeData.value = result;
        setArrayItemProperty( treeData.value );
        if(result){
          reload({searchInfo:{id:unref(result[0].id)}});
          treeName.value = result[0].name;
          currentTreeId.value = result[0].id;
        }
      }
      function setArrayItemProperty(list) {
        list.map(item=>{
          item.typeName = item.name;
          if(item.children){
            setArrayItemProperty(item.children);
          }
        })
      }
      async function loadTreeData(){
        treeData.value = [];
        let result = (await getTemplateTypeList({})) as unknown as TreeItem[];
          // result.map(item=>{
          //   item.title = item.name
          // })
        treeData.value = result;
      }
      onMounted(() => {
        fetch();
      });
      const [registerTable,{ reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getMessageList,
        columns: templateColumns,
        useSearchForm: true,
        showDragColumn:false,
        immediate:false,
        showIndexColumn: false,
        bordered: false,
        fetchSetting:{
          listField:'records'
        },
        rowKey: 'id',
        formConfig: {
          labelWidth: 100,
          schemas:tplSearchFormSchema
        },
        isTriggerSelect:false,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
      });

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
      onMounted(()=>{
        console.log(basicTree.value,'----------iiii--')
      })

      
      function showMenu(node: any) {
        getRightMenuList(node)
      }

      function handleTemplateType(){
        openTypeModal(true,{
          isUpdate:false
        })
      }
      function handleMenuClick(){

      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys,e){
        console.log(keys,e)
        if(!keys.length) return;
        treeName.value = e.node.typeName;
        currentTreeId.value = keys[0];
        parentId.value = e.node.parentId;
        setProps({searchInfo:{templateType:keys[0]}});
        reload();
      }
      function handleCancel(record: EditRecordRow) {
        currentEditKeyRef.value = '';
        record.onEdit?.(false, false);
      }
      function handleCreateUser(){

      }
      function handTableDataSet(){
        canDrag.value = !unref(canDrag)
        setProps({
          showDragColumn:unref(canDrag)
        })
      }
      function handleSend(record:EditRecordRow){
          openSendModal(true,{
            isUpdate:true,
              record:{
                ...record
            }
          })
      }

      async function handleSave(record: EditRecordRow) {
        // 校验
        msg.loading({ content: '正在保存...', duration: 0, key: 'saving' });
        const valid = await record.onValid?.();
        if (valid) {
          try {
            const data = cloneDeep(record.editValueRefs);
            console.log(data);
            //TODO 此处将数据提交给服务器保存
            // ...
            // 保存之后提交编辑状态
            const pass = await record.onEdit?.(false, true);
            if (pass) {
              currentEditKeyRef.value = '';
            }
            msg.success({ content: '数据已保存', key: 'saving' });
          } catch (error) {
            msg.error({ content: '保存失败', key: 'saving' });
          }
        } else {
          msg.error({ content: '请填写正确的数据', key: 'saving' });
        }
      }
      function changePanelShow(val){
         emit('changePanelShow',val)
      }

      function createActions(record: EditRecordRow, column: BasicColumn): ActionItem[] {
        if (!record.editable) {
          return [
            {
              label: '编辑',
              disabled: currentEditKeyRef.value ? currentEditKeyRef.value !== record.key : false,
              onClick: handleEdit.bind(null, record),
            },
          ];
        }
        return [
          {
            label: '保存',
            onClick: handleSave.bind(null, record, column),
          },
          {
            label: '取消',
            popConfirm: {
              title: '是否取消编辑',
              confirm: handleCancel.bind(null, record, column),
            },
          },
        ];
      }
      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }
      function handleEdit(record?: EditRecordRow){
          openModal(true,{
            isUpdate:true,
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

      function handleSuccess() {
        reload();
      }
      function handleTreeSuccess(data) {
        console.log(data,'回调数据')
        if(data.isUpdate){
          fetch();
        }else{
          loadTreeData();
        }
        
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
      function loadManageName(level:string|number){
        switch(level){
          case 1:
            return '主管';
          case 2:
            return '经理';
          default:
            return ''
        }
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
                  typeName:toRaw(node).typeName
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
        let result = await deleteTemplateType({id});
        if(result){
          msg.success('删除成功');
          fetch()
        }
      }


      return { 
        treeName,
        currentTreeId,
        handleTreeDelete,
        onEditTemplate,
        getRightMenuList,
        actionList,
        handleTemplateType,
        treeData,
        registerModal,
        registerSendModal,
        onTreeSelect, 
        canDrag,
        loadManageName,
        transferRoleType,
        createIcon ,
        hasPermission,
        prefixCls,
        registerTemplateTypeModal,
        handTableDataSet,
        changePanelShow,
        handleSuccess,
        registerTable,
        handleEdit,
        handleAdd,
        panelShow,
        createActions,
        handleMenuClick,
        onEditChange,
        handleCreateUser,
        checkedKeys,
        onSelectChange,
        handleSend,
        handleTreeSuccess
      }
    },
  });
</script>

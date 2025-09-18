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
      :toggleSwitch="true"
      :switchIcon="false"
      :fieldNames="{ title: 'name', key: 'id' }"
      :treeData="treeData"
      :beforeRightClick="getRightMenuList"
      @refresh="handleRefresh"
      @select="onTreeSelect"
      @expand="handleNodeExpand"
    >
      <template #action>
          <a-button type="default" @click="showTreeModal('group')" :class="[prefixCls+'-action']" v-if="hasPermission(['dictcategory:add'])">新增字典分类 </a-button>
          <a-button type="default" @click="showTreeModal('dict')" :class="[prefixCls+'-action']" v-if="hasPermission(['dict:add'])">新增字典 </a-button>
      </template>
    </BasicTree>
        <div :class="prefixCls+'-table'" style="flex-grow:1">
          <div class="resrun-table-top-header">
            <span class="table-tree-custom-title">{{data.dictTreeName}}</span>
            <div :class="prefixCls+'-custom-btn'">
                <a href="javascript:;" @click="onDetailDict">
                  <Icon icon="ant-design:file-text-filled"/>
                  详情
                </a>
                <Divider type="vertical" v-if="hasPermission([data.parentId?'dict:edit':'dictcategory:edit'])"/>
                <a href="javascript:;" v-if="hasPermission([data.parentId?'dict:edit':'dictcategory:edit'])"  @click="onEditDict">
                  <Icon icon="ant-design:edit-filled"/>
                  编辑
                </a>
                <Divider type="vertical" v-if="hasPermission([data.parentId?'dict:delete':'dictcategory:delete'])"/>
                <a href="javascript:;" v-if="hasPermission([data.parentId?'dict:delete':'dictcategory:delete'])" @click="handleDeleteGroup({id:data.dictTreeId})">
                  <Icon icon="ant-design:delete-filled"/>
                  删除
                </a>
            </div>  
        </div>
            <BasicTable @register="registerTable" @edit-change="onEditChange" @expand="handleExpand"
           >
              <template #toolbar>
                  <a-button type="primary" @click="showModal" v-if="hasPermission(['dictdata:add'])" :disabled="!data.parentId">新增 </a-button>
              </template>
              <template #status="{record}">
                <span>{{record.status?'有效':'无效'}}</span>
              </template>
              <template #action="{ record }">
                  <TableAction
                    :actions="[
                      {
                        label:'编辑',
                        onClick: handleRecordEdit.bind(null, record),
                        auth:'dictdata:edit'
                      },
                      {
                        label:'删除',
                        color: 'error',
                        popConfirm: {
                          title: '是否确认删除',
                          confirm: handleDelete.bind(null, record),
                        },
                        auth:'dictdata:delete'
                      },
                    ]"
                  />
                </template>
            </BasicTable>
        </div>
    <DictModal @register="register" @success="handleSuccess" />
    <DictTreeModal @register="registerTree" @success="handleTreeSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, h ,ref,onMounted ,toRaw, reactive} from 'vue';
  import { Row, Col , Divider } from 'ant-design-vue';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { BasicTree, TreeActionItem, ContextMenuItem,TreeItem} from '/@/components/Tree/index';
  import {
    BasicTable,
    useTable,
    EditRecordRow,
    TableAction,
  } from '/@/components/Table';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { Icon } from '/@/components/Icon';
  import { tableColumns } from './data';
  import { EllipsisOutlined } from '@ant-design/icons-vue';
  import { useDesign } from '/@/hooks/web/useDesign'
  import { transferRoleType } from '/@/utils/system';
  import { useModal } from '/@/components/Modal';

  import DictModal from './modal/DictModal.vue';
  import DictTreeModal from './modal/DictTreeModal.vue';
  import { deleteDictGroup, getDictTree,getDictItems,deleteDictItem } from '/@/api/dict';



  export default defineComponent({
    name:'数据字典',
    components: { BasicTree, Row, Col,BasicTable,TableAction, Divider,Icon,DictModal,DictTreeModal},

    setup() {
      const { prefixCls } = useDesign('dict');

      const checkedKeys = ref<Array<string | number>>([]);

      const currentEditKeyRef = ref('');
      const canDrag = ref(false)
      const organizeName = ref('运营部');
      const data = reactive({
          dictTreeId:null,
          dictTreeName:'',
          parentId:''
      })

      const { createMessage: msg } = useMessage();
      const [register, { openModal }] = useModal();

      const [registerTree, { openModal:openTreeModal }] = useModal();

      const { hasPermission } = usePermission();
      const basicTree = ref(null);
      const panelShow = ref(true);
      const treeData = ref<TreeItem[]>([]);

      async function fetch(isGetList:boolean) {
        treeData.value = [];
        let list = (await getDictTree({})) as unknown as TreeItem[];
        treeData.value = formatTreeData(list)
        if(treeData.value.length && isGetList){
            data.dictTreeId = treeData.value[0].id;
            data.dictTreeName = treeData.value[0].name;
            data.parentId = treeData.value[0].parentId;
            setProps({
                searchInfo:{dictId:treeData.value[0].id}
            });
            reload()
        }
      }
      function formatTreeData(list){
          list && list.map(item=>{
                item.children = item.children?item.children:[];
                item.expandedIcon = false;
                item.label = item.name;
                // if(item.type==='dict'){
                //   item.icon = 'ant-design:diff-outlined'
                // }
                // if(item.type==='group'){
                //   item.icon = 'ant-design:read-outlined';
                // }
                item.children = item.children?formatTreeData(item.children):[];
          })
          return list;
      }
      onMounted(() => {
        fetch(true);
      });
      const [registerTable,{ reload ,setProps}] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getDictItems,
        columns: tableColumns,
        isTreeTable: true,
        useSearchForm: false,
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        fetchSetting:{
          listField:'records'
        },
        afterFetch:handleAfterFeatchData,
        isTriggerSelect:false,
        rowKey:'id',
        immediate:false,
        canResize: false,
        striped:false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        actionColumn: {
          width: 150,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: 'right',
        },
      });
      

      onMounted(()=>{
        console.log(basicTree.value,'----------iiii--')
      })

      function handleEdit(record: EditRecordRow) {
        currentEditKeyRef.value = record.key;
        record.onEdit?.(true);
      }
      function handleRecordEdit(record){
         openModal(true,{
            isUpdate:true,
            dictId: data.dictTreeId,
            record:{
              ...record
            }
          })
      }
      async function handleDelete(record: EditRecordRow){
        let result = await deleteDictItem({id:record.id});
        if(result){
          msg.success('操作成功');
          fetch(true);
        }
      }
      function handleAfterFeatchData(list){
        console.log(list,'---请求数据---');
        list.map(item=>{
          item.children = item.childCount?[] : undefined;
        })
      }
    
      function onTreeSelect(keys,e){
        if(!keys.length) return;
        data.dictTreeName = e.node.label;
        data.parentId =  e.node.parentId;
        data.dictTreeId = keys[0];
        if(e.node.type==='group'){
          return 
        };
        setProps({searchInfo:{dictId:keys[0]}});
        reload();
        
      }
      function onEditChange({ column, value, record }) {
        // 本例
        if (column.dataIndex === 'id') {
          record.editValueRefs.name4.value = `${value}`;
        }
        console.log(column, value, record);
      }

      function showTreeModal(type){
        openTreeModal(true, {
          isUpdate: false,
          record:{
            type:type
          }
        });
      }
      async function handleExpand(expanded,record){
        console.log(expanded,record)
        if(!expanded) return;
        let result = await getDictItems({parentId:record.id})
        if(result){
          result.records.map(item=>{
            item.children = item.childCount?[] : undefined;
          })
          record.children = result.records;
        }
      }
      function showModal(){
          openModal(true,{
            isUpdate:false,
            dictId: data.dictTreeId
          })
      }

      function showMenu(node: any) {
        console.log(node);
        // basicTree.value.handleRightClick(node)
        getRightMenuList(node)
      }
      function onDetailDict(){
        openTreeModal(true,{
            record:{
              id:data.dictTreeId,
              parentId:data.parentId
            },
            isUpdate:true,
            detail:true
          })
      }
      function onEditDict(){
        openTreeModal(true,{
          record:{
            id:data.dictTreeId,
            parentId:data.parentId
          },
          isUpdate:true
        })
      }

      function getRightMenuList(node: any): ContextMenuItem[] {
        // 判断是字段分类还是字典组
        return [
          {
            label: '详情',
            handler: () => {
              console.log('点击了详情', node);
              openTreeModal(true,{
                record:{
                  ...toRaw(node)
                },
                isUpdate:true,
                detail:true
              })
            },
            // icon: 'ant-design:gold-filled',
          },
          {
            label: '编辑',
            handler: () => {
              openTreeModal(true,{
                record:{
                  ...toRaw(node)
                },
                isUpdate:true
              })
            },
            auth:node.type === 'group'?['dictcategory:edit']:['dict:edit'],
            // icon: 'ant-design:user-outlined',
          },
          {
            label: '删除',
            handler: () => {
              console.log('点击了删除', node);
              handleDeleteGroup(node)
            },
            auth:node.type === 'group'?['dictcategory:delete']:['dict:delete'],
            // icon: 'ant-design:user-add-outlined',
          },
        ];
      }
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
      function treeIconStatus(list,e){
        list.map(item=>{
          if(e.node.id === item.id){
            console.log(item,'匹配项')
            item.expandedIcon = e.expanded;
          }
          if(item.children){
              treeIconStatus(item.children,e);
            }
        })
      }
      function handleNodeExpand(key,e){
        console.log(key,e,'数据回调')
        treeIconStatus(treeData.value, e)
      }
      function handleSuccess() {
        reload();
      }
      function handleTreeSuccess(){
        fetch(false);
      }
      function handleRefresh(){
        fetch(false)
      }
      async function handleDeleteGroup(record){
        let result = await deleteDictGroup({id:record.id});
        if( result) {
          msg.success('操作成功');
          fetch(false)
        }
      }
      return {
        data,
        onDetailDict,
        onEditDict,
        handleDeleteGroup,
        treeData,
        handleNodeExpand,
        onTreeSelect,
        handleDelete,
        actionList,
        handleTreeSuccess,
        handleEdit,
        handleRecordEdit, 
        canDrag,
        handleRefresh,
        getRightMenuList, 
        transferRoleType,
        hasPermission,
        prefixCls,
        organizeName,
        handleSuccess,
        registerTable,
        panelShow,
        onEditChange,
        register,
        showTreeModal,
        checkedKeys,
        showModal,
        registerTree,
        handleExpand,
      }
    },
  });
</script>

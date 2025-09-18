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
  <div :class="['resrun-tree-table',`${prefixCls}`]">
    <BasicTree
      ref="basicTree"
      title=""
      :toolbar="true"
      :checkable="false"
       search
      :isRemoteSearch="true"
      :toggleSwitch="true"
      :fieldNames="{title:'deparCountName'}"
      :treeData="treeData"
      :searchApiData="(filterData as any)"
      :beforeRightClick="getRightMenuList"
      @changePanelShow="changePanelShow"
      @refresh="handleRefresh"
      @rowClick="handleRowClick"
      @loadSearchData="handleLoadSearchData"
      @searchChange="searchDept"
      @select="onTreeSelect"
    >
      <template #action>
          <a-button v-if="hasPermission(['organize:add'])" type="default" @click="showOrganizeDrawer" :class="[prefixCls+'-action']">
            <Icon icon="ant-design:apartment-outlined" />
          添加组织</a-button>
          <a-button type="default"  v-if="hasPermission(['dept:add'])"  @click="showDeptDrawer" :class="[prefixCls+'-action']">
            <Icon icon="ant-design:deployment-unit-outlined" />
          添加部门</a-button>
      </template>
    </BasicTree>
        <div :class="prefixCls+'-table'">
            <div :class="prefixCls+'-custom-header'">
                <span :class="prefixCls+'-custom-title'">{{organizeName}}</span>
                <div :class="prefixCls+'-custom-btn'">
                    <a href="javascript:;" @click="handleInviteUser(currentDeptId)" v-if="hasPermission(['invite:user'])">
                      <Icon icon="ant-design:link-outlined"/>
                      邀请成员加入
                    </a>
                    <a href="javascript:;" @click="handleUserManager(currentDeptId)" v-if="hasPermission(['dept:setmanager'])">
                      <Icon icon="ant-design:user-outlined"/>
                      设置主管
                    </a>
                    <Divider type="vertical"/>
                    <a href="javascript:;" @click="showDeptDrawerById" v-if="hasPermission(['dept:edit'])">
                      <Icon icon="ant-design:setting-filled"/>
                      编辑部门
                    </a>
                    <Divider type="vertical"/>
                    <a href="javascript:;" @click="showDeptDrawerAddChild" v-if="hasPermission(['dept:addchild'])">
                      <Icon icon="ant-design:plus-square-filled"/>
                      添加子部门
                    </a>
                </div>  
            </div>
            <BasicTable @register="registerTable" @edit-change="onEditChange" :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">
              <template #realname="{text,record}">
                <div class="realname-content">
                  <!-- <div class="avatar-img">
                    <img :src="record.avatarBase64" alt=""  v-if="record.avatarBase64"/>
                    <span v-else>{{text}}</span>
                  </div> -->
                  <span style="width: 50px;">{{text}}</span>
                  <a-tag color="blue" v-if="record.manageFlag">主管</a-tag>
                </div>
              </template>
              <template #status="{text}">
                <div>
                  {{loadStatus(text)}}
                </div>
              </template>
              <template #nickName="{text,record}">
                  <a-space :size="10">
                    <span>{{text}}</span>
                    <a-tag color="success" v-if="record.addType == 'system'">主管理员</a-tag>
                  </a-space>
              </template>
              <template #roleNames="{text}">
                  {{text}}
              </template>
              <template #authStatus="{text}">
                  {{text}}
              </template>
              <template #toolbar>
                  <a-button type="primary" @click="showUserDrawer" v-if="hasPermission(['user:add'])">添加成员 </a-button>
                  <!-- <a-button type="default" @click="handleCreateUser">批量导入/导出 </a-button> -->
                  <a-button type="default" @click="handleDeleteBatch" v-if="hasPermission(['user:deletebatch'])">批量删除</a-button>
                  <a-button type="default" @click="handleDeptChange" v-if="hasPermission(['user:changedepart'])">调整部门 </a-button>
                  <!-- <a-button type="default" @click="handTableDataSet">调整顺序 </a-button> -->
                  <a-button type="default" @click="handleUserStatus(2)" v-if="hasPermission(['user:freeze'])">冻结 </a-button>
                  <a-button type="default" @click="handleUserStatus(1)" v-if="hasPermission(['user:unfreeze'])">解冻 </a-button>
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
              <template #headerTop  v-if="canDrag">
                  <a-alert type="info" show-icon>
                    <template #message>
                      <span>上下移动调整位置</span>
                      <div class="header-top-action">
                          <a-button type="link" @click="canDrag = false" size="small">保存</a-button>
                          <a-button type="link" @click="canDrag = false" size="small">取消</a-button>
                      </div>
                    </template>
                  </a-alert>
              </template>
              <template #action="{ record }">
                  <a-button type="link"  size="small" @click="onRowClick(record)">编辑</a-button>
                  <a-button type="link"  size="small" @click="handleRemind(record)" v-if="record.status==0">发送提醒</a-button>
              </template>
            </BasicTable>
        </div>
    <DeptDrawer @register="registerDeptDrawer" @success="handleDeptSuccess" />
    <OrganizeDrawer @register="registerOrganizeDrawer" @success="handleDeptSuccess" />
    <RoleDrawer @register="registerRoleDrawer" @success="handleSuccess" />
    <UserDrawer @register="registerUserDrawer" @success="handleSuccess" />
    <TabTreeModal @register="registerUserModal" @success="handleSuccess" />
    <InviteUser @register="registerInviteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, h ,ref,onMounted ,toRaw,unref,reactive, getCurrentInstance} from 'vue';
  import { Row, Col , Divider } from 'ant-design-vue';
  import { Button } from '/@/components/Button';
  import { BasicTree, TreeActionItem, ContextMenuItem,TreeItem} from '/@/components/Tree/index';
  import { Icon } from '/@/components/Icon';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { userColumns } from './data';
  import { EllipsisOutlined } from '@ant-design/icons-vue';
  import { useDesign } from '/@/hooks/web/useDesign'
  import { useModal } from '/@/components/Modal';
  import { transferRoleType } from '/@/utils/system';
  import { cloneDeep } from 'lodash-es';
  import { useDrawer } from '/@/components/Drawer';
  import DeptDrawer from './drawer/DeptDrawer.vue';
  import OrganizeDrawer from './drawer/OrganizeDrawer.vue';
  import UserDrawer from './drawer/UserDrawer.vue';
  import RoleDrawer from './drawer/RoleDrawer.vue';
  import TabTreeModal from './modal/TabTreeModal.vue';
  import InviteUser from './modal/InviteUser.vue';
  import { useAppStoreWithOut } from '/@/store/modules/app';
  import { BasicTable, useTable,TableAction, BasicColumn, ActionItem, EditRecordRow} from '/@/components/Table';
  
  import { getUserForOrgList, getUserList,getDeptByKeywordList,getUserByKeywordList, getDeptTreeList } from '/@/api/demo/system'; 
  import { setBrozenBatch, changeDept, deleteUserBatch } from '/@/api/sys/user';
  import { getMyDeptTreeList,editDept,getDeptManagerList, remindUser } from '/@/api/sys/dept';

  import { useMessage } from '/@/hooks/web/useMessage';

  interface ResultChild {
    icon:string;
    title:string;
  }
  interface ResultItem {
    total:number;
    page:number;
    showMore:boolean;
    list:Array<ResultChild>,
    groupName?: string;
    type?:string;
    key?: string;
  }
  export default defineComponent({
    name:'User',
    components: { Button, BasicTree, Row, Col,BasicTable,TableAction, Divider,Icon,DeptDrawer,UserDrawer,TabTreeModal,InviteUser,OrganizeDrawer,RoleDrawer},
    emits:['changePanelShow'],
    setup(_,{emit}) {
      const appStore  = useAppStoreWithOut();
      const { prefixCls } = useDesign('organize');
      const checkedKeys = ref<Array<string | number>>([]);
      const { createMessage: msg, createConfirm } = useMessage();
      const currentEditKeyRef = ref('');
      const canDrag = ref(false);
      const treeModalType = ref('user');
      const organizeName = ref('');
      const searchText = ref('');
      const [registerDeptDrawer, { openDrawer:openDeptDrawer,closeDrawer }] = useDrawer();
      const [registerUserDrawer, { openDrawer:openUserDrawer,closeDrawer:closeUserDrawer }] = useDrawer();
      const [registerOrganizeDrawer, { openDrawer:openOrganizeDrawer }] = useDrawer();
      const [registerRoleDrawer, { openDrawer:openRoleDrawer }] = useDrawer();
      const [registerUserModal, { openModal,closeModal }] = useModal();
      const [registerInviteModal, {openModal:openInviteModal,closeModal:closeInviteModal }] = useModal();
      const { hasPermission } = usePermission();
      const basicTree = ref(null)
      const panelShow = ref(true)
      const treeData = ref<TreeItem[]>([]);
      const filterData = ref<ResultItem[]>([]);
      const currentDeptId = ref();  //当前选中的部门id
      const treeDeptId = ref(); // 部门 tree 右键操作的id 
      const instance = getCurrentInstance();
      const { eventHub } = instance?.proxy;

      async function fetch() {
        treeData.value = [];
        let result = (await getDeptTreeList()) as unknown as TreeItem[];
        treeData.value =   formatTreeData(result)
        if( treeData.value.length){
          let params = {
              id:treeData.value[0].id
          }
          reload({searchInfo:{...params}});
          setProps({
              searchInfo:{id:treeData.value[0].id}
          });
          // let currentUserList = await getUserList(params);
          // setTableData(currentUserList.records);
          organizeName.value =  treeData.value[0].departName;
          currentDeptId.value = treeData.value[0].id;

        }
        
      }
      onMounted(() => {
        fetch();
      });
      const [registerTable,{ reload ,setProps, getRawDataSource }] = useTable({
        title: '',
        titleHelpMessage: [],
        api: getUserForOrgList,
        immediate:false,
        columns: userColumns,
        fetchSetting:{
          listField:'records'
        },
        rowKey:'id',
        useSearchForm: false,
        dataSource:[],
        showDragColumn:false,
        showIndexColumn: false,
        bordered: false,
        isTriggerSelect:false,
        showTableSetting: false,
        canResize: false,
        striped:false,
        tableSetting: { fullScreen: false ,redo:false,setting:false,size:false},
      });
      

      onMounted(()=>{
        // console.log(basicTree.value,'----------iiii--')
      })
     async  function upDateTree(){
        treeData.value = [];
        let result = (await getDeptTreeList()) as unknown as TreeItem[];
        treeData.value =  formatTreeData(result)
      }
      function formatTreeData(list){
          list && list.map(item=>{
              item.deparCountName = item.departName + '  ' +'('+ item.userCount + ')';
              if(item.children){
                item.children = formatTreeData(item.children);
              }
          })
          return list;
      }

      async function handleUserStatus(status:number){
        if(!unref(checkedKeys).length){
          msg.warning('请选择操作数据');
          return;
        }
        let params = {
            ids:unref(checkedKeys),
            status:status
        }
        let result = await setBrozenBatch(params);
        if(result.success){
          msg.success('操作成功');
          reload({searchInfo:{id:unref(currentDeptId)}});
          checkedKeys.value = [];
        }

      }
      function handleUserManager(deptId?:string){
        treeDeptId.value = deptId;
        treeModalType.value = 'user';
        openModal(true,{
          isUpdate: true,
          records: toRaw(getRawDataSource()).records,
          title:'设置主管',
          dataRecordId: deptId,
          dataBackApi:getDeptManagerList,
          tabs:[
              { 
                title:'人员列表',
                type:'user',
                api:getUserList,
                asyncTree:false,
                params:{
                  id:deptId
                },
                resultField:'records',
                // asyncApi:getUserList,
                // asyncFieldNames:{
                //   children:'children',
                //   title:'realname',
                //   key:'id'
                // },
                fieldNames:{
                  children:'children',
                  title:'realname',
                  key:'id'
                }
              },
              // {
              //   title:'根据角色选人',
              //   type:'roleUser',
              //   api:getRoleTreeList,
              //   asyncTree:true,
              //   asyncApi:getUserByRoleId,
              //   asyncFieldNames:{
              //     children:'children',
              //     title:'realname',
              //     key:'id'
              //   },
              //   fieldNames:{
              //     children:'children',
              //     title:'name',
              //     key:'id'
              //   }
              // }
            ]
        })
      }
      async function searchDept(val){
        searchText.value = val;
        let deptResult = await getDeptByKeywordList({keyWord:val,pageSize:5});
        let userResult = await getUserByKeywordList({keyWord:val,pageSize:5});
        // let roleResult = await getRoleByKeywordList({keyWord:val,pageSize:5});
        
        filterData.value =  [
          {
            groupName:'部门',
            list:deptResult.records,
            total:deptResult.total,
            showMore:deptResult.total>5?true:false,
            page:deptResult.current,
            type:'dept',
          },
          {
            groupName:'成员',
            list:userResult.records,
            total:userResult.total,
            showMore:userResult.total>5?true:false,
            page:userResult.current,
            type:'user'
          },
          // {
          //   groupName:'角色',
          //   list:roleResult.records,
          //   total:roleResult.total,
          //   showMore:roleResult.total>5?true:false,
          //   page:roleResult.current,
          //   type:'role'
          // }
        ]
      }
      async function handleLoadSearchData(type,page){
        console.log(type)
        if(type==='dept'){
          let result = await getDeptByKeywordList({keyWord:unref(searchText), pageSize:5*page});
          filterData.value[0].list = result.records;
          filterData.value[0].page = result.size/5 ;
          filterData.value[0].showMore = (result.total>result.pages)?true:false;
        }
        if(type==='user'){
          let result = await getUserByKeywordList({keyWord:unref(searchText),pageSize:5*page});
          filterData.value[1].list = result.records;
          filterData.value[1].page = result.size/5 ;
          filterData.value[1].showMore = (result.total>result.pages)?true:false;
        }
        // if(type==='role'){
        //   let result = await getRoleByKeywordList({keyWord:unref(searchText),pageSize:5*page});
        //   filterData.value[2].list = result.records;
        //   filterData.value[2].page = result.size/5 ;
        //   filterData.value[2].showMore = (result.total>result.pages)?true:false;
        // }
        console.log( filterData.value, '----结果数据-----')

      }
      function handleEdit(record: EditRecordRow) {
        currentEditKeyRef.value = record.key;
        record.onEdit?.(true);
      }
      function onRowClick(record: EditRecordRow) {
        if(!hasPermission(['user:edit'])) {
          msg.warning('暂无该操作权限')
          return
        }
        openUserDrawer(true,{
          isUpdate:true,
          record:{
              ...record,
              departId:unref(currentDeptId)
          }
        })
      }
      function onSelectChange(selectedRowKeys: (string | number)[]) {
        console.log(selectedRowKeys);
        checkedKeys.value = selectedRowKeys;
      }
      function onTreeSelect(keys,e){
        console.log(keys,e)
        organizeName.value = e.node.departName;
        currentDeptId.value = e.node.id;
        if(!keys.length) return;
        setProps({searchInfo:{id:keys[0]}});
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

      function handleRefresh(){
        fetch()
      }
      function handleRowClick(row,type){
        if(type==='dept'){
          if(!hasPermission(['dept:edit'])) {
            msg.warning('暂无权限')
            return
          }
          openDeptDrawer(true,{
            record:{
              ...toRaw(row),
            },
            isUpdate:true
          })
        }
        if(type==='user'){
          if(!hasPermission(['user:edit'])) {
              msg.warning('暂无权限')
              return
          }
           openUserDrawer(true,{
              isUpdate:true,
              record:{
                  ...toRaw(row)
              }
            })
        }
        if(type==='role'){
           if(!hasPermission(['role:edit'])) {
            msg.warning('暂无权限')
            return
          }
          openRoleDrawer(true,{
              isUpdate:true,
              record:{
                  ...toRaw(row)
              }
            })
        }

      }

      function showDeptDrawer(){
        appStore.setAxiosRetryConfig({callback:closeDrawer})
        openDeptDrawer(true, {
          isUpdate: false,
        });
      }
      function showOrganizeDrawer(){
        openOrganizeDrawer(true, {
          isUpdate: false,
        });
      }
      function showDeptDrawerById(){
        openDeptDrawer(true, {
          isUpdate: true,
          record:{
            currentDeptId:unref(currentDeptId)
          }
        });
      }
      function handleInviteUser(deptId:string) {
        openInviteModal(true,{
          isUpdate: false,
          record:{
            currentDeptId:deptId,
            organizeName:unref(organizeName)
          }
        })

      }
      function showDeptDrawerAddChild(){
          treeDeptId.value = unref(currentDeptId)
          openDeptDrawer(true,{
            isUpdate:false,
            record:{
              isAddChildDept:true,
              id:unref(currentDeptId)
            }
        })
      }
      function showUserDrawer(){
        eventHub.$emit('btnCallback',[closeUserDrawer,reload])
          openUserDrawer(true,{
            isUpdate:false
          })
      }


      function getRightMenuList(node: any): ContextMenuItem[] {
        
        return [
          {
            label: '编辑部门信息',
            auth:['dept:edit'],
            handler: () => {
              treeDeptId.value = node.id;
              openDeptDrawer(true,{
                record:{
                  ...toRaw(node),
                  currentDeptId:toRaw(node).id   // 统一部门id 名称
                },
                isUpdate:true
              })
            },
            // icon: 'ant-design:edit-filled',
          },
          {
            label: '设置部门主管',
            auth:['dept:setmanager'],
            handler: () => {
              treeDeptId.value = node.id;
              console.log('点击了设置部门主管', node);
              handleUserManager(node.id);
            },
            // icon: 'ant-design:user-outlined',
          },
          {
            label: '添加子部门',
            auth:['dept:addchild'],
            handler: () => {
              treeDeptId.value = node.id;
              console.log('点击添加子部门', node);
              openDeptDrawer(true,{
                isUpdate:false,
                record:{
                  isAddChildDept:true,
                  id:node.id
                }
              })
            },
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
      function showMenu(node: any) {
        getRightMenuList(node)
      }
      async function handleSuccess(list) {
        let userIds:string[] = [];
        if(list){ // 人员选择
          list.map( item => {
            userIds.push(item.id)
          })
          let result = reactive({})
          if( treeModalType.value === 'user'){  
            // 设置主管
              let params = {
                  id:unref(treeDeptId),
                  userIds,
                  parentId:unref(currentDeptId)
              }
              result = await editDept(params);
          }else{
              // 调整部门
              // if(userIds.length >1){
              //     msg.warning('只能选择一个部门');
              //     return;
              // }
              if(userIds.length === 0){
                  msg.warning('目标部门不能为空');
                  return;
              }
              let params = {
                  ids:unref(checkedKeys),
                  hisDepartId:unref(currentDeptId),
                  newDepartIds:userIds
              }
              result = await changeDept(params);
              if(result){
                upDateTree();
              }
          }
          if(result){
            reload();
            closeModal();
            checkedKeys.value = [];
          }
        }else{
          reload();
          fetch();
        }
      }
      function handleDeptSuccess(){
        fetch();
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
      function loadStatus(status:string|number){
        switch(status){
          case -1:
            return '拒绝加入';
          case 0:
            return '未激活';
          case 1:
            return '正常';
          case 2:
            return '停用';
          default:
            return ''
        }
      }
      async function handleRemind(record){
        let result = await remindUser({id:record.id})
        if(result){
          msg.success('操作成功');
          reload();
        }
      }
      async function handleDeleteBatch(){
          if(!unref(checkedKeys).length){
            msg.warning('请勾选要删除的人员数据');
            return;
          }
          let params = {
            ids:unref(checkedKeys).join(',')
          }

          createConfirm({
              iconType: 'warning',
              title: () => h('span', '删除员工'),
              content: () => h('span','会将该员工从所在所有部门移除并删除此人,是否确认?'),
              okText:'确定',
              cancelText:'取消',
              onOk: async () => {
                let result = await deleteUserBatch(params);
                if(result){
                  msg.success('删除成功');
                  reload({searchInfo:{id:unref(currentDeptId)}});
                  checkedKeys.value = [];
                } 
              },
          })
      }
      function handleDeptChange(){
        treeModalType.value = 'dept'
        if(!unref(checkedKeys).length){
          msg.warning('请勾选要调整部门的人员');
          return;
        }
        openModal(true,{
          isUpdate: true,
          title:'调整部门',
          dataBackSource:[
            {id: unref(currentDeptId),name:unref(organizeName),iconName:'ant-design:apartment-outlined'}
          ],
          tabs:[
              { 
                title:'组织',
                type:'dept',
                api:getMyDeptTreeList,
                asyncTree:false,
                fieldNames:{
                  children:'children',
                  title:'departName',
                  key:'id'
                }
              }
            ]
        })
      }
      return { 
        treeData,
        handleRemind,
        loadStatus,
        handleDeptChange,
        showDeptDrawerById,
        filterData,
        onTreeSelect,
        actionList, 
        searchDept,
        canDrag,
        handleRowClick,
        loadManageName,
        getRightMenuList, 
        transferRoleType,
        createIcon ,
        hasPermission,
        prefixCls,
        handleUserManager,
        handTableDataSet,
        organizeName,
        changePanelShow,
        handleSuccess,
        handleDeptSuccess,
        registerTable,
        handleEdit,
        panelShow,
        createActions,
        onEditChange,
        handleCreateUser,
        registerDeptDrawer,
        showDeptDrawer,
        checkedKeys,
        showUserDrawer,
        registerUserDrawer,
        onRowClick,
        onSelectChange,
        handleUserStatus,
        registerUserModal,
        handleRefresh,
        handleDeleteBatch,
        registerOrganizeDrawer,
        showOrganizeDrawer,
        currentDeptId,
        handleLoadSearchData,
        registerRoleDrawer,
        showDeptDrawerAddChild,
        registerInviteModal,
        handleInviteUser
      }
    },
  });
</script>
<style lang="less" scoped>
 @prefix-cls: ~'@{namespace}-organize';
.@{prefix-cls} {
  &.panelHide{
    .ant-tabs-nav{
      margin-left:10px;
      transition: all 0.4s;
    }
  }
  .realname-content{
    display: flex;
    justify-content: left;
    // margin-left: 10px;
    align-items: center;
    .avatar-img{
      width: 40px;
      height: 40px;
      background: #127fd2;
      border-radius: 5px;
      color: #fff;
      white-space: nowrap;
      display: flex;
      justify-content: center;
      align-items: center;
      box-sizing: border-box;
      padding: 0 5px;
      span{
        overflow: hidden;
      }
    }
  }
 
  &-table{
    flex:1;
  }
  // &-action{
  //   width:90%;
  // }
  &-custom-header{
    height: 71px;
    background: #fff;
    padding: 2px 15px;
    // margin-left: 10px;
    /* margin-bottom: 10px; */
    border-bottom: 1px solid #f6f6f6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .ant-divider{
      color:#1890ff
    }
  }
  &-custom-title{
    font-size: 14px;
    font-weight: 600;
  }
  &-custom-btn a{
    font-size: 14px;
  }
  .ant-tabs-nav{
    margin:0;
    background-color: #fff;
    padding:0 15px;
  }
}
.resrun-tree-table{
  display: flex;
  height:100%;
}
// .resrun-tree{
//   flex: 0 0 250px
// }
.resrun-basic-table{
  flex: auto;
}

</style>

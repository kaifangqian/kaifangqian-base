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
  <div>
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit">
        <div class="auth-config">
          <BasicTree
            ref="basicTree"
              title=""
              :toolbar="false"
              :toggleSwitch="false"
              :checkable="true"
              :selectedKeys="selectedKeys"
              :checkedKeys="treeCheckedKeys"
              :search="false"
              :fieldNames="{ title: 'name', key: 'id' }"
              :treeData="treeData"
              :checkStrictly="true"
              :checkStrictlySwitch="true"
              @select="onTreeSelect"
              @check="onCheck"
            >
          </BasicTree>
          <div class="button-auth-area">
            <div class="button-auth-list">
                <div class="button-auth-item" v-for="(item,index) in buttonAuth" :key="index">
                  <div class="button-header">
                    <a-checkbox v-model:checked="item.checked" @change="handleBtnAuth(item)">{{ item.name }}</a-checkbox>
                  </div>
                  <!-- <a-checkbox-group v-model:value="item.checkedList" :options="item.rules" >
                      <template v-slot="{option}">
                        <span>{{ option.dataName }}</span>
                      </template>
                    </a-checkbox-group> -->
                  <div class="auth-check"> 
                    <span class="auth-title">数据范围：</span>
                    <a-checkbox-group v-model:value="item.checkedList" @change="handleAuth(item)">
                      <a-checkbox :value="auth.id" v-for="auth in item.rules">{{ auth.dataName }}</a-checkbox>
                    </a-checkbox-group> 
                  </div>
                 
                </div>
            </div>
          </div>
        </div>
      </BasicModal>
  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,unref,computed,reactive } from 'vue';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { BasicTree,TreeItem} from '/@/components/Tree/index';
  import { TreeSelect } from 'ant-design-vue';
  import { Icon } from '/@/components/Icon';
  import {eachTree } from '/@/utils/helper/treeHelper';
  import { PlusOutlined } from '@ant-design/icons-vue';
  import { addAuth,editAuth, getAllMenuBtnList, getTenantAppAuthData, getAuthDataChecked, setAuthPermission} from '/@/api/auth/group';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { cloneDeep } from 'lodash-es';


  interface RuleVosItem {
      permissionId:string;
      ruleIds: string[] | undefined;
  }
  interface AppPerVos {
      appId:string;
      ruleVOS: RuleVosItem[];
  }
  interface RuleItem {
    id:string;
    dataName:string;
    [key: string]: string | number
  }
  interface ButtonAuthItem{
    id: string;
    permissionId: string;
    name: string;
    checked?: boolean;
    checkedList?: string[];
    rules: RuleItem[];
  }
  export default defineComponent({
    name: 'AuthConfigModal',
    components:{
      BasicModal,
      BasicTree,
      PlusOutlined,
      Icon,
      TreeSelect
    },
    setup(_, { emit }){
      const treeData = ref<TreeItem[]>([]);
      const selectedKeys = ref<Array<string | number>>([]);
      const treeCheckedKeys = ref<Array<string | number>>([]);
      const isUpdate = ref(true);
      const recordId = ref('');
      const tinymceValue = ref();
      const currentPermissionId = ref(''); //当前选中的tree节点
      const ruleVOS = ref<RuleVosItem[]>([]);
      const permissionRuleVos = ref<RuleVosItem[]>([]);
      const appId = ref('');
      const { createMessage: msg } = useMessage();
      const buttonAuth = ref(<ButtonAuthItem[]>[]);
      const groupType = ref('');

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'取消',
        });
        isUpdate.value = !!data?.isUpdate;
        appId.value = data?.appId;
        if (unref(isUpdate)) {
          recordId.value = data.record.id;
          groupType.value = data.record.groupType;
          if(data.record.groupType == '1' || data.record.groupType == '2'){
            setModalProps({
              footer:null
            })
          }
         
        }else{
          recordId.value = "";
        }
        let result = await getAllMenuBtnList({tenantAppId:unref(appId)});
        if(result){
          treeData.value = result;
          treeData.value.map(item=>{
            item.disableCheckbox = true;
            item.name = item.appName;
            item.id = item.appId;
            item.children = item.permissions;
            assignRootId(item);
          })
          eachTree( treeData.value,(item, _parent)=>{
              // if(item.menuType===2){
              //   item.icon = 'ant-design:safety-certificate-outlined';
              //   item.color = '#E34D59';
              // }else{
              //   // item.disabled = true;
                item.icon = '';
              //   item.disableCheckbox = true;
              // }
          })
          //获取已有权限
          getCheckedData()
        }
      });

      function assignRootId(tree) {
        const rootId = tree.appId; // 假设树的根节点已知其id
        function traverse(node) {
          node.rootId = rootId; // 将当前节点的rootId属性设置为根节点的id
          if (node.children) {
            for (let i = 0; i < node.children.length; i++) {
              traverse(node.children[i]); // 递归遍历子节点
            }
          }
        }
        traverse(tree); // 从根节点开始遍历
      }

      function onTreeSelect(key,e){
        console.log(key,e,'----------点击数据---')
        if(!key.length) return;
        currentPermissionId.value = key[0];
        getAuthAndButton(key[0]);

      }
      // 获取选中菜单的按钮以及权限
      async function getAuthAndButton(permissionId?:string){
        let params = {
          groupId:unref(recordId),
          permissionId
        }
        let result = await getTenantAppAuthData(params);
        if(result){
          buttonAuth.value = result;
          buttonAuth.value.map(item=>{
            item.permissionId = permissionId as string;
            item.checked = false;
            item.checkedList = [];
            let matchAuthBtn = ruleVOS.value.filter(v=>v.permissionId === item.id);
            if(matchAuthBtn.length){
              item.checked = true;
              matchAuthBtn[0].ruleIds&&matchAuthBtn[0].ruleIds.map(i=>{
                if(i){
                  item.checkedList?.push(i)
                }
              })
            }
          })
        }
      }
      
      // 获取所有权限id
      async function getCheckedData(permissionId?:string){
        let params = {
          groupId:unref(recordId),
          permissionId
        }
        let result = await getAuthDataChecked(params);
        if(result){
          ruleVOS.value = result;
          permissionRuleVos.value = cloneDeep(result);
          //table选中默认权限
          if(result.length){
            treeCheckedKeys.value = [];
            result.map(item=>{
                item.ruleVOS.forEach(v=>{
                  treeCheckedKeys.value.push(v.permissionId);
                })
            })
            // checkedKeys.value = result[0].ruleIds;
          }
          console.log(ruleVOS,'---默认权限-----')
        }
      }
      function onCheck(keys, e){
        console.log(keys,'选中的key', e,'选中的数据')
        treeCheckedKeys.value = keys.checked;
        if(e.checked){
          // let matchRootNode = findRootNode(treeData.value,e.node.id);


        }
      }

      function handleAuth(item){
        console.log(item,'------dedddd----')
        if(item.checkedList.length){
          item.checked = true;
        }else{
          item.checked = false;
        }
      }
      function handleBtnAuth(item){
        console.log(item)
        if(!item.checked){
          item.checkedList = [];
        }
      }

      const getTitle = computed(() => ('权限配置'));

      async function handleSubmit() {
        let parasmPermission:RuleVosItem[] = [];
        let appPerVos:AppPerVos[] = [];
        //整理右侧按钮权限
        buttonAuth.value.map(item=>{
          if(item.checked){
            parasmPermission.push({
              permissionId:item.id,
              ruleIds:item.checkedList
            })
          }
        })
        // parasmPermission.map(item=>{
        //   let matchRootNode = findRootNode(treeData.value,item.id);
        //   if(matchRootNode){
        //     appPerVos.push({
        //       appId:matchRootNode.appId,
        //       ruleVOS:parasmPermission
        //     })
        //   }
        // })
      
        //过滤出除当前操作选中的按钮
        let filterTreeKey:string[] = [];
        treeCheckedKeys.value.map((key:string)=>{
          let matchButton = buttonAuth.value.filter(j => j.id === key );
          if(!matchButton.length){
            filterTreeKey.push(key)
          }
        })
        //整理树的权限数据
        filterTreeKey.map((key: string)=>{
          let matchTreeNode  = permissionRuleVos.value.filter(m=>m.permissionId === key);
          let matchNode = findRootNode(treeData.value,key);
            console.log(matchNode,'找到的节点');
            // parasmPermission.push({
            //   permissionId:key,
            //   ruleIds:matchTreeNode[0]?matchTreeNode[0].ruleIds : []
            // })
            if(matchNode){
              appPerVos.push({
                appId:matchNode.rootId,
                ruleVOS:[{
                  permissionId:key,
                  ruleIds:matchTreeNode[0]?matchTreeNode[0].ruleIds : []
                }].concat(parasmPermission)
              })
            }
           
        })
        // 根据应用合并
        const mergeAppPerVos =  Object.values(appPerVos.reduce((acc, cur) => {
          const { appId, ...rest } = cur;
          if (!acc[appId]) {
            acc[appId] = { appId, ...rest };
          } else {
            acc[appId].ruleVOS = [...acc[appId].ruleVOS, ...cur.ruleVOS];
          }
          return acc;
        }, {}));
        //遍历应用下的权限去重
          mergeAppPerVos.map((item:any)=>{
            console.log(item)
            const res = Array.from(new Set(item.ruleVOS.map( ruleItem => ruleItem.permissionId))).map(permissionId=>{
              return item.ruleVOS.find(ruleItem=> ruleItem.permissionId == permissionId)
            })
            console.log(res)
            item.ruleVOS = res;
          })

        try {
          let params = {
            groupId:unref(recordId),
            appPermissionVOS:mergeAppPerVos
          }
          let result = await setAuthPermission(params);
          if(result){
            closeModal();
            msg.success('设置成功')
          }
        
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }


      //根据选择的左侧permissionId 查找根节点（即所在应用）
      function findRootNode(arr, key) {
          // 遍历数组中的每一个 tree
          for (let i = 0; i < arr.length; i++) {
            const node = findRootNodeInTree(arr[i], key);
            if (node !== null) {
              return node;
            }
          }
          return null;
      }

      function findRootNodeInTree(tree, key) {
        // 查找根节点是否为 key 所代表的节点
        if (tree.id === key) {
          return tree;
        }
        if(tree.children){
           // 对于该节点的每个子节点，递归地调用该函数
          for (let i = 0; i < tree.children.length; i++) {
            const node = findRootNodeInTree(tree.children[i], key);
            if (node !== null) {
              return node;
            }
          }
        }
        // 如果不存在，返回 null
        return null;
      }


      function handleTinymceChange(value: string) {
        console.log(value);
      }
     
      return { 
          registerModal,tinymceValue,handleTinymceChange,
          getTitle,
          handleSubmit,
          treeData,
          onTreeSelect,
          selectedKeys,
          treeCheckedKeys,
          buttonAuth,
          onCheck,
          handleAuth,
          handleBtnAuth,
          ruleVOS
      
      };
    }
  })
  
</script>
<style lang="less" scoped>
.auth-config{
  display: flex;
  .button-auth-area{
    flex:1;
  }
  .button-auth-item{
    // margin-bottom: 10px;
    margin:4px 0;
    padding:0 10px;
    .button-header{
      background-color:#f8f8f8;
      padding:4px 10px;
      border-radius: 4px;
      box-sizing: border-box;
    }
   
    .auth-check{
      padding: 5px 25px;
      .auth-title{
      font-size: 12px;
      margin-right: 10px;
    }
    }
    .ant-checkbox-group{
      .ant-checkbox-wrapper{
        font-size: 12px;
        color: #127fd2;
      }
    }
  }
}
 
</style>

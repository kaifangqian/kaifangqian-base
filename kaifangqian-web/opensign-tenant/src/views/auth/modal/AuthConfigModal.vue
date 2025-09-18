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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true">
        <div class="auth-config">
          <BasicTree
              ref="permissionRef"
              title=""
              :toolbar="false"
              :toggleSwitch="false"
              :checkable="true"
              :selectedKeys="selectedKeys"
              :checkedKeys="treeCheckedKeys"
              :search="false"
              :fieldNames="{ title: 'name', key: 'id' }"
              :treeData="treeData"
              :checkStrictly="false"
              :checkStrictlySwitch="true"
              @select="onTreeSelect"
              @check="onCheck"
            >
          </BasicTree>
          <div class="button-auth-area">
            <div class="button-auth-list">
              <!-- {{ apiPermissionRuleVOS }} -->
                <div class="button-auth-item" v-for="(item,index) in currentButtonAuth" :key="index">
                  <div class="button-header">
                    <a-checkbox v-model:checked="item.checked" @change="handleBtnAuth(item)">{{ item.name }}</a-checkbox>
                  </div>
                  <!-- <a-checkbox-group v-model:value="item.checkedList" :options="item.rules" >
                      <template v-slot="{option}">
                        <span>{{ option.dataName }}</span>
                      </template>
                    </a-checkbox-group> -->
                  <div class="auth-check" v-if="item.rules&&item.rules.length"> 
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
  import { getAllMenuBtnList, getTenantAppAuthData, getAuthDataChecked, setAuthPermission} from '/@/api/auth/group';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { cloneDeep } from 'lodash-es';


  interface RuleVosItem {
      permissionId:string;
      ruleIds: string[] | undefined;
      appId?:string;
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
      const permissionRef = ref();
      const selectedKeys = ref<Array<string | number>>([]);
      const treeCheckedKeys = ref<Array<string | number>>([]);
      const isUpdate = ref(true);
      const recordId = ref('');
      const tinymceValue = ref();
      const currentPermissionId = ref(''); //当前选中的tree节点
      const permissionMenuRuleVOS:any = ref([]);        // 左侧树操作时权限
      const apiPermissionRuleVOS = ref<AppPerVos[]>([]);       //原始数据用于提交
      const permissionBackRuleVos = ref<RuleVosItem[]>([]);   //备份权限数据用于保存原始数据
      const appId = ref('');
      const { createMessage: msg } = useMessage();
      const currentButtonAuth = ref(<ButtonAuthItem[]>[]);
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
        currentButtonAuth.value = [];
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
          treeData.value = filterAuthVisible( treeData.value)
          //获取已有权限
          getCheckedData()
        }
      });

      function filterAuthVisible(list){
        return list.filter(item => {
          if (item.authVisible === false) {
            return false;
          }

          if (item.children && item.children.length > 0) {
            item.children = filterAuthVisible(item.children);
            return item.children.length > 0;
          }

          return true;
        });
      }
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
        getAuthAndButton(key[0],e);
       
      }

      function onCheck(keys, e){
        // console.log(keys,'选中的key', e,'选中的数据')
        treeCheckedKeys.value = keys;
          permissionMenuRuleVOS.value = [];
          e.checkedNodes.map(v=>{
            permissionMenuRuleVOS.value.push({
              permissionId:v.id,
              ruleIds:[],
              appId:v.rootId
            })
          })
          e.halfCheckedKeys.map(key=>{
            let matchRootNode = findRootNode(treeData.value,key);
            permissionMenuRuleVOS.value.push({
              permissionId:key,
              ruleIds:[],
              appId:matchRootNode.rootId
            })
          })
          updateMenuPermissionRuleVOS(e);
          console.log(permissionMenuRuleVOS.value,'23434324324234')
        //左右复选框联动
        if(e.checked && (e.node.id == currentPermissionId.value)){
          currentButtonAuth.value.map(item=>{
            item.checked = true
          })
        }
        if(!e.checked && (e.node.id == currentPermissionId.value)){
          currentButtonAuth.value.map(item=>{
            item.checked = false
          })
        }
        updateBtnPermissionRuleVOS()

        // console.log('Parent Keys:', parentKeys);
      }
     
      // 获取选中菜单的按钮以及权限
      async function getAuthAndButton(permissionId:string, e:any){
        let params = {
          groupId:unref(recordId),
          permissionId
        }
        let result = await getTenantAppAuthData(params);
        if(result){
          currentButtonAuth.value = result;
          //联动处理
          // if(e.node.menuType==1){
          //   if(e.selected){
          //     treeCheckedKeys.value.push(permissionId);
          //   }else{
          //     treeCheckedKeys.value = treeCheckedKeys.value.filter(v=>v != e.node.id)
          //   }
          //   if(e.selected && (e.node.id == currentPermissionId.value)){
          //     currentButtonAuth.value.map(item=>{
          //       item.checked = true
          //     })
          //   }
          //   if(!e.selected && (e.node.id == currentPermissionId.value)){
          //     currentButtonAuth.value.map(item=>{
          //       item.checked = false
          //     })
          //   }
          //   updateBtnPermissionRuleVOS()
          // }

          let appPermisson:any = [];
          let matchNode = findNodeById(treeData.value,currentPermissionId.value);
          if(matchNode){
            appPermisson = apiPermissionRuleVOS.value.find(v=>v.appId = matchNode.rootId)
          }
          currentButtonAuth.value.map(item=>{
            item.permissionId = permissionId as string;
            item.checked = false;
            item.checkedList = [];
            let matchAuthBtn = appPermisson.ruleVOS.filter(v=>v.permissionId === item.id);
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
          apiPermissionRuleVOS.value = result;
          permissionBackRuleVos.value = cloneDeep(result);
          //table选中默认权限
          if(result.length){
            treeCheckedKeys.value = [];
            let permissionMenus:any = [];
            result.map(item=>{
                item.ruleVOS.forEach(v=>{
                  let matchNode = findNodeById(treeData.value,v.permissionId)
                  //将所有菜单信息存入数组方便下面查找父节点下子节点数量
                  if(matchNode){
                    permissionMenus.push(matchNode)
                  }
                })
              })
            result.map(item=>{
                item.ruleVOS.forEach(v=>{
                  let matchNode = findNodeById(treeData.value,v.permissionId)
                  //如果是菜单组 将菜单children长度与返回的premissionId 中parentId 等于该菜单组id的数据长度比较
                  if(matchNode?.menuType==0){
                    let matchNodePermissionChilds = permissionMenus.filter(u=>u.parentId==matchNode.id);
                    if(matchNode?.children&&(matchNodePermissionChilds?.length == matchNode?.children.length)){
                      treeCheckedKeys.value.push(v.permissionId);
                    }
                  }else{
                      treeCheckedKeys.value.push(v.permissionId);
                  }
                 
                })
            })

          }
        }
      }

      function findNodeById(tree, id) {
        for (const node of tree) {
          if (node.id === id) {
            return node; // 找到节点
          }

          if (node.children && node.children.length > 0) {
            const childNode = findNodeById(node.children, id);
            if (childNode) {
              return childNode; // 在子节点中找到节点
            }
          }
        }

        return null; // 未找到节点
      }

      function mergeArrays(arrA, arrB) {
          const mergedArray:any = [];

          // 遍历数组A
          arrA.forEach(itemA => {
            const matchingItemB = arrB.find(itemB => itemB.appId === itemA.appId);

            if (matchingItemB) {
              // 合并ruleVOS，取并集
              const mergedRuleVOS = mergeRuleVOS(itemA.ruleVOS, matchingItemB.ruleVOS);
              
              // 将合并后的项添加到结果数组
              mergedArray.push({ appId: itemA.appId, ruleVOS: mergedRuleVOS });
            } else {
              // 数组B中没有相匹配的项，直接添加到结果数组
              mergedArray.push({ appId: itemA.appId, ruleVOS: itemA.ruleVOS });
            }
          });

          // 查找数组B中没有匹配的项，将其添加到结果数组
          arrB.forEach(itemB => {
            const isDuplicate = arrA.some(itemA => itemA.appId === itemB.appId);
            if (!isDuplicate) {
              mergedArray.push({ appId: itemB.appId, ruleVOS: itemB.ruleVOS });
            }
          });

          return mergedArray;
        }

        function mergeRuleVOS(ruleVOSA, ruleVOSB) {
          // 合并ruleVOS，取并集
          const mergedRuleVOS = [...ruleVOSA];

          ruleVOSB.forEach(itemB => {
            const existingIndex = mergedRuleVOS.findIndex(itemA => itemA.permissionId === itemB.permissionId);
            if (existingIndex !== -1) {
              // 在数组A中找到相同的permissionId，合并ruleIds，取并集
              mergedRuleVOS[existingIndex].ruleIds = [...new Set([...mergedRuleVOS[existingIndex].ruleIds, ...itemB.ruleIds])];
            } else {
              // 数组A中没有相同的permissionId，将项添加到数组A
              mergedRuleVOS.push(itemB);
            }
          });

          return mergedRuleVOS;
        }
        function removeDuplicateRuleVOS(arrayA, arrayB) {
          // 遍历数组A
          arrayA.forEach(itemA => {
            // 查找数组B中相同appId的项
            const matchingItemB = arrayB.find(itemB => itemB.appId === itemA.appId);

            if (matchingItemB) {
              // 过滤数组A中与数组B相同appId的项，同时具有相同permissionId的数据
              itemA.ruleVOS = itemA.ruleVOS.filter(ruleA =>
                !matchingItemB.ruleVOS.some(ruleB => ruleA.permissionId === ruleB.permissionId)
              );
            }
          });

          return arrayA;
        }



      //实时更新权限数据中的菜单
      function updateMenuPermissionRuleVOS(e){
        const groupedPermissionMenuRuleVOS = permissionMenuRuleVOS.value.reduce((acc, item) => {
            const existingGroup = acc.find(group => group.appId === item.appId);

            if (existingGroup) {
              existingGroup.ruleVOS.push({ permissionId: item.permissionId, ruleIds: item.ruleIds });
            } else {
              acc.push({ appId: item.appId, ruleVOS: [{ permissionId: item.permissionId, ruleIds: item.ruleIds }] });
            }

            return acc;
          }, []);
          apiPermissionRuleVOS.value = mergeArrays(apiPermissionRuleVOS.value,groupedPermissionMenuRuleVOS)
          // 勾选时通过上述方法合并添加到apiPermissionRuleVOS 数组
          // 取消勾选时根据下方方法判断取消勾选的是菜单目录还是菜单。如果是目录将该目录与其下的菜单id整理到一起 根据appId一并从apiPermissionRuleVOS中移除
          // 如果取消的是菜单只需将菜单id根据appId从apiPermissionRuleVOS中移除
          //非选中状态
        if(!e.checked){
          let removePermissionRules:RuleVosItem[] = [];
          if(e.node.menuType==0){
            e.node.children.map(v=>{
              removePermissionRules.push({
                permissionId:v.id,
                ruleIds:[]
              })
            })
            removePermissionRules.push({
              permissionId:e.node.id,
              ruleIds:[]
            })

          }else if(e.node.menuType==1){
            removePermissionRules.push({
              permissionId:e.node.id,
              ruleIds:[]
            })
          }
          let removePermissionRuleVOS = [{
              appId:e.node.rootId,
              ruleVOS:removePermissionRules
            }
          ]
          apiPermissionRuleVOS.value = removeDuplicateRuleVOS( apiPermissionRuleVOS.value, removePermissionRuleVOS)
        }
      }
      //实时更新权限数据中的按钮
      function updateBtnPermissionRuleVOS(){
        let matchNode = findNodeById(treeData.value,currentPermissionId.value);
        currentButtonAuth.value.map(item=>{
          apiPermissionRuleVOS.value.map(v=>{
              if(v.appId == matchNode.rootId){
                if(item.checked){
                  let matchBtnPermission = v.ruleVOS.find(u=>u.permissionId==item.id)
                  if(!matchBtnPermission){
                    v.ruleVOS.push({
                      permissionId:item.id,
                      ruleIds:item.checkedList,
                    })
                  }else{
                    matchBtnPermission.ruleIds = item.checkedList;
                  }
                }else{
                  v.ruleVOS = v.ruleVOS.filter(u=>u.permissionId != item.id)
                  item.checkedList = [];
                }
              }
          })
        })
      }
      

      function handleAuth(item){
        console.log(item,'------dedddd----')
        if(item.checkedList.length){
          item.checked = true;
        }else{
          // item.checked = false;
        }
        updateBtnPermissionRuleVOS()
      }
      function handleBtnAuth(item){
        console.log(item)
        if(!item.checked){
          item.checkedList = [];
        }
        updateBtnPermissionRuleVOS()
      }

      const getTitle = computed(() => ('权限配置'));

      async function handleSubmit() {
        // let parasmPermission:RuleVosItem[] = [];
        // let appPerVos:AppPerVos[] = [];
        // //整理右侧按钮权限
        // currentButtonAuth.value.map(item=>{
        //   if(item.checked){
        //     parasmPermission.push({
        //       permissionId:item.id,
        //       ruleIds:item.checkedList
        //     })
        //   }
        // })
        // //过滤出除当前操作选中的按钮
        // let filterTreeKey:string[] = [];
        // permissionMenuRuleVOS.value&&permissionMenuRuleVOS.value.map((item:any)=>{
        //   let matchButton = currentButtonAuth.value.filter(j => j.id === item.permissionId );
        //   if(!matchButton.length){
        //     filterTreeKey.push(item.permissionId)
        //   }
        // })
        // //整理树的权限数据
        // filterTreeKey.map((key: string)=>{
        //   let matchTreeNode  = permissionBackRuleVos.value.filter(m=>m.permissionId === key);
        //   let matchNode = findRootNode(treeData.value,key);
        //     console.log(matchNode,'找到的节点');
        //     // parasmPermission.push({
        //     //   permissionId:key,
        //     //   ruleIds:matchTreeNode[0]?matchTreeNode[0].ruleIds : []
        //     // })
        //     if(matchNode){
        //       appPerVos.push({
        //         appId:matchNode.rootId,
        //         ruleVOS:[{
        //           permissionId:key,
        //           ruleIds:matchTreeNode[0]?matchTreeNode[0].ruleIds : []
        //         }].concat(parasmPermission)
        //       })
        //     }
           
        // })
        // // 根据应用合并
        // console.log(appPerVos)
        // const mergeAppPerVos =  Object.values(appPerVos.reduce((acc, cur) => {
        //   const { appId, ...rest } = cur;
        //   if (!acc[appId]) {
        //     acc[appId] = { appId, ...rest };
        //   } else {
        //     acc[appId].ruleVOS = [...acc[appId].ruleVOS, ...cur.ruleVOS];
        //   }
        //   return acc;
        // }, {}));
        // //遍历应用下的权限去重
        //   mergeAppPerVos.map((item:any)=>{
        //     console.log(item)
        //     const res = Array.from(new Set(item.ruleVOS.map( ruleItem => ruleItem.permissionId))).map(permissionId=>{
        //       return item.ruleVOS.find(ruleItem=> ruleItem.permissionId == permissionId)
        //     })
        //     console.log(res)
        //     item.ruleVOS = res;
        //   })

        try {
          let params = {
            groupId:unref(recordId),
            appPermissionVOS:apiPermissionRuleVOS.value
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
          currentButtonAuth,
          onCheck,
          handleAuth,
          handleBtnAuth,
          permissionRef,
          apiPermissionRuleVOS,
          permissionMenuRuleVOS
      
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
    margin:10px 0;
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

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
      <Tree
          ref="asyncTreeRef"
          class="tab-tree-selected"
          title=""
          :toolbar="true"
          :showIcon="false"
          :checkable="true"
          :checkedKeys="selectCheckedKeys"
          :toggleSwitch="false"
          :checkStrictly="true"
          :fieldNames="fieldNames"
          :load-data="(onLoadData as any)"
          :treeData="treeData"
          @check="onCheckChange"
        >
            <template #icon="{iconName}">
                <Icon :icon="iconName" />
            </template>
        </Tree>
  </div>
</template>
<script lang='ts'>
import { defineComponent,onMounted, ref,unref,nextTick } from 'vue';
import { BasicTree, TreeItem, TreeActionType} from '/@/components/Tree/index';
import { Icon } from '/@/components/Icon';
import { Tree} from 'ant-design-vue';
import { SmileOutlined } from '@ant-design/icons-vue';
import { isArray, isFunction } from '/@/utils/is';
import { get } from 'lodash-es';
import { propTypes } from '/@/utils/propTypes';

export default defineComponent({
  name: 'TabTree',
  components:{
      BasicTree,
      Icon,
      Tree,
      SmileOutlined
  },
  emits: ['checkChange', 'itemSelect'],
  props: {
    api: { type: Function as PropType<(arg?: Recordable) => Promise<Recordable>> },
    asyncApi: { type: Function as PropType<(arg?: Recordable) => Promise<Recordable>> },
    type:{ type:String},
    asyncTree:{type: Boolean},
    params: { type: Object },
    asyncFieldNames:{
      type: Object as PropType<{ key: string,title:string }>,
      default:null
    },
    fieldNames:{
      type: Object as PropType<{ key: string,title:string}>,
      default:null
    },
    immediate: { type: Boolean, default: true },
    resultField: propTypes.string.def(''),
  },
  setup(props,{emit}){ 
      const asyncTreeRef = ref<Nullable<TreeActionType>>(null);
      const selectCheckedKeys = ref<Array<string | number>>([]);
      const treeData = ref<TreeItem[]>([]);
      const { type, asyncTree, asyncApi,fieldNames,asyncFieldNames,onlyRole  } = props;
      onMounted(() => {
        nextTick(()=>{
          fetch();
        })
      });
     async function fetch(){
      // console.log(22222,unref(props), props.params);
      let { params, api, resultField } = props;
      console.log(params,'参数')
      // selectCheckedKeys.value = [];
      if (!api || !isFunction(api)) return;
      let result;
        try {
          // console.log(params,'--参数--');
          result = await api({...params,pageSize:1000});
        } catch (e) {
          console.error(e);
        }
        if (!result) return;
        if (!isArray(result)) {
          result = get(result,resultField);
        }
        treeData.value = formatTreeData((result as TreeItem[]) || []);
     }
    function getTree() {
        const tree = unref(asyncTreeRef);
        if (!tree) {
          throw new Error('tree is null!');
        }
        return tree;
      }

      function setTreeCheckedKey(key){
        // console.log(getTree());
        selectCheckedKeys.value = selectCheckedKeys.value.filter(v=>v != key)
        // getTree().setCheckedKeys(keys);
      }
      function formatTreeData(list){
          list && list.map(item=>{
              // item.children = item.children?item.children:undefined;
              if(type =='dept' || type =='deptUser') {
                item.iconName = 'ant-design:apartment-outlined'
              }
              if(type=='role' || type == 'roleUser') {
                  item.iconName = 'ant-design:usergroup-add-outlined'
                  //选择角色时角色组不可选
                  if(type==='role' && !item.parentId){
                    item.checkable = false;
                  }
              }
              if(type==='deptUser' || type === 'roleUser') {
                  item.disableCheckbox = true;
                  item.checkable = false;
                  item.children = item.children?item.children:[];  //修复选人时部门和角色没有子集时不请求bug
              }
              if(type === 'user'){
                item.iconName = 'ant-design:user-outlined';
              }
              if(item.children){
                item.children = formatTreeData(item.children);
              }else{
                item.isLeaf = (type==='dept' ||  type==='role' || type === 'user')? true : false
              }
              // item.children = item.children?formatTreeData(item.children):[];
          })
          return list;
      }
       function onLoadData(treeNode){
        return new Promise(async (resolve: (value?: unknown) => void) => {
          if (!treeNode.children) {
            resolve();
            return;
          }
          // setTimeout(async() => {
            if (asyncTree && asyncApi) {
              let parentId = treeNode.id;
              if(!parentId){
                  resolve();
                  return;
              }
              let result = await asyncApi({id:parentId,pageSize:1000});
              result.records&&result.records.map(item=>{
                item.iconName = 'ant-design:user-outlined';
                item[fieldNames.title]= item[asyncFieldNames.title];
                item[fieldNames.key] = item[asyncFieldNames.key];
                item.disableCheckbox = false;
                item.isLeaf = true;
                item.key  = item.id;
                item.checkable = true;
                item.departId = type==='deptUser'?treeNode.id:'';  //根据组织选人时需要传递组织id
                item.authtype = type == 'dept'?'dept':(type=='role'?'role':'user');
              })
              treeNode.dataRef.children = treeNode.dataRef.children.concat(result.records);
              treeData.value = [...treeData.value];

            }
            resolve();
            return;
          // }, 0);
        })
      }
      function onCheckChange(checkedKeys,e){
        selectCheckedKeys.value = checkedKeys.checked;
        emit('checkChange',checkedKeys.checked,e,treeData, props.fieldNames);
      }
      return {
        onLoadData,
        treeData,
        asyncTreeRef,
        selectCheckedKeys,
        onCheckChange,
        setTreeCheckedKey
      }

  }
})
</script>
<style lang="less">
 .tree-select-panel{
    display: flex;
    justify-content: space-between;
    .resrun-tree.open{
      width:100%;
    }
    .panel-item{
      width: 50%;
      margin: 0 1px;
      border: 1px solid #e4e4e4;
      border-radius: 2px;
    }
    .selected-header{
      padding: 22px 10px;
      .header-title{
        font-weight: 600;
        margin-right: 5px;
      }
    }
    .resrun-tree-header{
      padding: 0 5px 15px;
    } 
 }

</style>

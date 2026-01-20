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
      <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true" @cancel="handleClose">
      <div class="tree-select-panel">
         <div class="source-panel panel-item">
            <Tabs @change="handelTabChange"> 
            <template v-for="item in tabs">
              <TabPane :key="item.type" :tab="item.title"  v-if="!(item.type == 'roleUser' || item.type == 'role') " >
                  <TabTree v-bind="item" @checkChange="checkChange" ref="tabtree"/>
              </TabPane>
            </template>
            </Tabs>
         </div>
          <div class="panel-line"></div>
         <div class="target-panel panel-item">
            <div class="selected-header">
              <span class="header-title">已选择</span>
              <span>{{'('+targetSource.length+')'}}</span>
            </div>
            <div class="selected-list">
              <div class="selected-item" v-for="(item,index) in targetSource" :key="index">
                  <p>
                    <Icon :icon="item.iconName" />
                    <span style="marginLeft:8px">{{item.title || item.name}}</span>
                  </p>
                  <span>
                    <Icon icon="ant-design:delete-twotone" @click="deleteItem(item,index)"/>
                  </span>
              </div>
            </div>
         </div>
      </div>
      </BasicModal>

  </div>
</template>
<script lang='ts'>
  import { defineComponent,ref,computed,unref, onMounted } from 'vue';
  import { BasicTree } from '/@/components/Tree/index';
  import { BasicModal, useModalInner } from '/@/components/Modal';
  import { Icon } from '/@/components/Icon';
  import { findMatchItemInList} from '/@/utils/helper/treeHelper';
  import { TreeItem} from '/@/components/Tree/index';
  import { BasicForm,} from '/@/components/Form';
  import { Transfer,Tabs} from 'ant-design-vue';
  import  TabTree  from './TabTree.vue';
  import { cloneDeep } from 'lodash-es';

  interface  TransferData {
    id: string;
    title: string;
    name?: string;
    iconName?: string;
    authType?:string;
  }
  interface TabItem {
    key?:string;
    title:string;
    type:string;
    fieldNames:{type: Object};
    api:{ type: Function};
  }
  export default defineComponent({
    name: 'TabTreeModal',
    components:{
      BasicModal,
      BasicForm,
      Transfer,
      BasicTree,
      Tabs, 
      TabPane: Tabs.TabPane,
      TabTree,
      Icon
    },
    setup(_, { emit }){
      const isUpdate = ref(true);
      const sourceDataLength =ref(0);
      const targetSource = ref<TransferData[]>([]);
      const cacheTargetKey = ref<string[]>([]);
      const targetKeys = ref<string[]>([]);
      const tabs = ref<TabItem[]>([]);
      const getTitle = ref('');
      const treeTabKey = ref('');
      const tabtree = ref(null);
      // const TreeComponent = loadComps('TabTree');


      const filterOption = computed((inputValue: string, option: TransferData) => {
          return option.title.indexOf(inputValue) > -1;
      })

      const [registerModal, { setModalProps }] = useModalInner(async (data) => {
        targetSource.value = []; //右侧数据置空
        targetKeys.value = [];
        tabs.value = [];
        if(data.tabs){
          tabs.value = data.tabs;
          treeTabKey.value = data.tabs[0];
        }
        //数据回显 api方式
        if(data?.dataBackApi&&data?.dataRecordId){
          let result = await data.dataBackApi({id:data?.dataRecordId});
          if(result){
            result.map(item=>{
              item.iconName = "ant-design:user-outlined"
            })
            targetSource.value = result;
            targetSource.value.map(item=>{
                targetKeys.value.push(item.id)
            })
          }
        }
        // 数据回显 传入dataBackSource方式
        if(data?.dataBackSource){
           targetSource.value = data?.dataBackSource
        }
        setModalProps({ 
          confirmLoading: false,
          width:1200,
          cancelText:'关闭' 
        });
        isUpdate.value = !!data?.isUpdate;
        getTitle.value = data?.title;
      });

      // function loadComps(path){
      //   return ()=> import(`@views/organize/modal/${path}.vue` );
      // }
      onMounted(()=>{
        

      })
      async function handleSubmit() {
        emit('success',targetSource.value);
        // tabs.value = []  //解决params 参数 id缓存问题
        handleClose();
      }
      function handleClose(){
        tabs.value = []
      }

      function handelTabChange(){
        cacheTargetKey.value = unref(targetKeys);
        // console.log(unref(cacheTargetKey),'缓存key')
      }
      function checkChange(checkedKeys:[], e:{checked: boolean, checkedNodes, node, event}, source:[], fieldNames) {
        // targetSource.value =  targetSource.value.concat([]);
        // targetKeys.value = unref(cacheTargetKey);
        // targetSource.value =  [];
        if(e?.checked){
          targetKeys.value = Array.from(new Set(targetKeys.value.concat(checkedKeys)));
        }else{
          targetKeys.value = targetKeys.value.filter(key=>key !== e.node.id);
          //去除取消勾选项
          targetSource.value =  cloneDeep(targetSource.value).filter(item => item.id !== e.node.id);
        }
        // targetKeys.value = targetKeys.value.concat(unref(cacheTargetKey))
        // targetKeys.value = Array.from(new Set(targetKeys.value));
        // console.log(targetKeys,'3333333目标key')
        targetKeys.value&&targetKeys.value.map(key=>{
          // let matchItem:TreeItem = unref(source).filter(item=>item?.[fieldNames.key]===key)[0];
          let matchItem:TreeItem = findMatchItemInList(unref(source),{id:key});
          // console.log(key, matchItem,'选中的项目')
          if(matchItem){
            targetSource.value.push({
              id: matchItem.id,
              ...matchItem,
              authType:matchItem.type,
              title: matchItem[fieldNames.title],
            });
          }
          // 去重
          targetSource.value = uniqueFunc(cloneDeep(targetSource.value),'id');
        })
        // console.log(checkedKeys, e, unref(source), fieldNames)
      }
      function uniqueFunc(arr, uniId){
        const res = new Map();
        return arr.filter((item) => !res.has(item[uniId]) && res.set(item[uniId], 1));
      }
      function handleTinymceChange(value: string) {
        console.log(value);
      }
      function deleteItem(item,index){
        targetSource.value.splice(index,1);
        // let keys = targetKeys.value.filter(key=>key !== item.id);
        tabtree.value&&tabtree?.value[0].setTreeCheckedKey(item.id);
      }
      function handleChange (){

      }
      function handleSearch(){

      }
      function onTreeSelect(keys,e){
        console.log(keys,e)
      }

      return { 
        registerModal,
        getTitle,
        onTreeSelect,
        handleSubmit,
        handleClose,
        handleTinymceChange,
        sourceDataLength,
        handleChange,
        targetKeys,
        filterOption,
        handleSearch,
        targetSource,
        tabs,
        checkChange,
        deleteItem,
        treeTabKey,
        // TreeComponent,
        tabtree,
        handelTabChange
      };
    }
  })
</script>
<style lang="less" scoped>
.tree-select-panel{
  min-height: 200px;
}

 :deep(.ant-tabs-nav-wrap){
  padding:0 15px;
 }
 .scroll-container{
  background:#f7f9fc;
 }
 .source-panel,.target-panel{
  background:#fff;
 }
 .selected-list{
  padding:5px 15px;
  .selected-item{
    margin: 2px;
    cursor: pointer;
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #f5f5f5;
    padding: 2px 4px;
    border-radius: 2px;
    p{margin: 0;}

  }
 }
</style>

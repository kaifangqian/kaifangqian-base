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
  <div class="main-quick-app">
    <BasicModal v-bind="$attrs" @register="registerModal" title="管理" @ok="handleSubmit" :footer="null">
      <a-alert message="如一个应用包内无任何应用或门户，则该应用包不可勾选" type="info" />
      <div class="app-menu-tree-content">
        <div class="tree-list">
          <a-input
            v-model:value="searchValue"
            placeholder="搜索应用/门户名称"
            style="width: 100%"
            @search="onSearch"
          >
          <template #prefix>
            <Icon icon="ant-design:search-outlined" size="22" color="#ddd"/>
          </template>
          </a-input>
          <p class="select-title">选择应用/门户</p>
              <div class="tree-content">
                <div v-for="item in treeList">
                    <BasicTree
                      ref="basicTree"
                      title=""
                      :value='checkeKeys'
                      :treeCheckStrictly="true"
                      :toolbar="false"
                      :checkable="true"
                      :search="false"
                      :labelInValue="true"
                      :isRemoteSearch="false"
                      :toggleSwitch="true"
                      tree-default-label-prop="label"
                      :fieldNames="{title:'name'}"
                      :treeData="item.data"
                      @select="onTreeSelect"
                      @check="onTreeCheck"
                    >
                    </BasicTree>
                </div>
            </div>
        </div>
        <div class="selected-list">
          <p class="select-title">已添加的应用/门户  <span>{{appCount}}</span></p>
          <ul>
            <span>{{ checkeKeys }}</span>
            <li v-for="item in seectedList" class="selected-item">
              <div class="selected-menu-info">
                <img :src="item.img" alt=""/>
                <div>
                  <p class="selected-menu">{{ item.menuName }}</p>
                  <p class="selected-app">{{ item.appName }}</p>
                </div>
                
              </div>
              <Icon icon="ant-design:close-circle-outlined" size="20" @click="handleDeleteQuickApp(item)"/>
             
            </li>
          </ul>
        </div>
       
      </div>
    </BasicModal>
  </div>
</template>

<script lang="ts">
import { ref,defineComponent,onMounted } from "vue";
import { BasicModal,useModalInner } from '/@/components/Modal';
import { BasicTree, TreeActionItem, ContextMenuItem,TreeItem } from '/@/components/Tree/index';
import Icon from '/@/components/Icon';
import { getQuickAppTree } from '@/api/tenant';

interface selectedMenu {
  img:string;
  menuName:string;
  [key:string]:string;
}

export default defineComponent({
  name:"QuickAppManage",
  components:{
    BasicModal,
    BasicTree,
    Icon
    
  },
  setup(_,{emit}) {

    const treeList = ref([
      { data:[
          {  name: 'parent 1',  value: 'parent 1', menuType:0, appId:'232323232323', children: [  {   name: 'parent 1-0', value: 'parent 1-0', menuType:0,children:[
             {   name: 'Child Node3',  value: '0-1-0', parentId:'233434223', menuType:1},
             {  name: 'Child Node4',  value: '0-1-1', },] },  {  name: 'parent 1-1', value: 'parent 1-1', menuType:1,parentId:'233434223'}]},
        ]
      },
      { data:[
          {  name: 'parent 2',  value: 'parent 3', menuType:0, appId:'3434343434',  children: [  {   name: 'parent 2-0', value: 'parent 2-0',  },  {  name: 'parent 1-1', value: 'parent 1-1', }]},
        ]
      },
    ]);

    const seectedList = ref<selectedMenu[]>([]);
    const checkeKeys = ref([])
    const searchValue = ref('');
    const appCount = ref('');

    onMounted(()=>{
     
    })
    



    const [registerModal, { setModalProps }] = useModalInner(async (data) => {
      setModalProps({ confirmLoading: false,width:1100,canFullscreen:false, closable:true,maskClosable:false,centered:false });
      fetchTree()
     
    }) 

    async function fetchTree(){
      setCheckbox(treeList.value);
      console.log(treeList.value);
      return; 
      let result  = await getQuickAppTree({});
      if(result){
        treeList.value = result.records?setCheckbox( result.records):[];
      }
    }

    function setCheckbox(list){
      list.map(item=>{
        console.log(item)
        item.disabled = false;
        if(item.data){
          item.data.map(iitem=>{
            setAppId(iitem,iitem.appId,iitem.name);
            item.menuName = item.name
            if(iitem.menuType == 0 || !iitem.parentId){
                iitem.disabled = true;
            }
            if(iitem.children){
              setCheckbox(iitem.children);
            }
          })
        }
        item.menuName = item.name;
        if(item.menuType==0 || !item.parentId){
            item.disabled = true;
        }
        if(item.children){
          setCheckbox(item.children);
        }
        // console.log(item)
      })
    }
    function setAppId(node,appId,appName){
      node.appId = appId;
      node.appName = appName;
      if(node.children && node.children.length > 0){
        node.children.forEach(child => {
          setAppId(child, appId, appName);     //递归处理子节点
        });
      } 
    }


    function onSearch(){  

    }
    function onTreeSelect(keys,e){
      

    }
    function onTreeCheck(keys,e){
        checkeKeys.value = keys;
        seectedList.value = []
        e.checkedNodes.forEach(item=>{
          seectedList.value.push({
            paramsId:item.appId + '#' + item.value
            ,
            appId:item.appId,
            id:item.value,
            appName:item.appName,
            menuName:item.menuName,
            img:''
          })
        })
    }
    function handleDeleteQuickApp(item){
      seectedList.value = seectedList.value.filter(v=>v.id !== item.id);
      checkeKeys.value = checkeKeys.value.filter(v=>v !== item.id);
      

    }
    function handleSubmit(){
      emit('success')
    }
    return {
      onTreeSelect,
      onTreeCheck,
      treeList,
      onSearch,
      searchValue,
      handleSubmit,
      registerModal,
      appCount,
      seectedList,
      handleDeleteQuickApp,
      checkeKeys  
    }
  }
})
</script>

<style lang="less" scoped>
.app-menu-tree-content{
  display:flex;
  margin-top:20px;
  .tree-list{
    flex-basis: 600px;
  }
  .selected-list{
    padding:0 20px;
    box-sizing: border-box;
    flex:1;
    ul{
       margin-top:30px;
    }
    .selected-item{
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 30px;
      .selected-menu-info{
        display: flex;
        align-items: center;
      }
      .selected-menu{
        font-size: 14px;
        color: #333;
        margin-bottom: 8px;
        line-height: 1;
      }
      .selected-app{
        font-size: 12px;
        color: #999;
        margin-bottom: 0px;
        line-height: 1;
      }
      .app-iconify{
        cursor: pointer;
      }

    }
  }
  .select-title{
    color: #000;
    font-size:16px;
    margin-bottom:0;
    margin-top:10px;
    font-weight: 550;
  }
  
  .tree-content{
    height:50vh;
    overflow: auto;
    .resrun-tree__panel{
      height: auto;
    }

    .resrun-tree.open{
      width:100%;
    }
    .scroll-container{
      height: auto!important;
      padding-bottom: 0;
    }
  }
}
</style>

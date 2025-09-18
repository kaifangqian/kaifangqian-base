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
    <a-spin :spinning="spinning">
      <BasicTree
        ref="basicTree"
        title=""
        :toolbar="false"
        :checkable="false"
        :search="false"
        :toggleSwitch="false"
        :treeData="treeData"
        :selectedKeys="treeSelectedKeys"
        @select="onTreeSelect">
        
        <!-- :beforeRightClick="getRightMenuList" -->
        <template #action>
            <a-button type="primary" @click="addSeal" :class="[prefixCls+'-action']" 
            style="width:80%">
              <Icon icon="ant-design:plus-outlined" />新增印章
            </a-button>
        </template>
      </BasicTree>
    </a-spin>

    <div :class="prefixCls+'-table'" style="flex-grow:1;" v-if="treeData.length>0">
      <div class="seal-header">
          <div class="seal-title">
              <span :class="prefixCls+'-custom-title'">{{currentSeal.sealName}}</span>
          </div>
          <div class="seal-detailes">
            <div style="padding: 20px 20px 20px 0">
              <div class="seal-img-box">
                <img class="seal-img" :src="sealBase64" v-if="sealBase64"/>
                <!-- <img class="seal-img" :src="templateGz"/> -->
                
              </div>
            </div>
            <div class="seal-info">
              <Descriptions :column="1">
                <DescriptionsItem label="印章主体">{{currentSeal.entpName}}</DescriptionsItem>
                <DescriptionsItem label="印章类型">{{getSealType(currentSeal.sealType).label}}</DescriptionsItem>
                <DescriptionsItem label="印章状态">
                  <div>
                    <Tag color="processing" v-if="currentSeal.sealStatus == 1">制作中</Tag>
                    <Tag color="error" v-if="currentSeal.sealStatus == 2">制作失败</Tag>
                    <Tag color="warning" v-if="currentSeal.sealStatus == 3">已停用</Tag>
                    <Tag color="success" v-if="currentSeal.sealStatus == 4">已启用</Tag>
                    <Tag color="warning" v-if="currentSeal.sealStatus == 5">已收缴</Tag>
                    <Tag color="error" v-if="currentSeal.sealStatus == 6">已销毁</Tag>
                  </div>
                  <div>
                    <!-- <span @click="stopSeal" class="span-link" type="warning" v-if="currentSeal.status == 1">停用</span>
                    <span @click="startSeal" class="span-link" v-if="currentSeal.status == 2">启用</span>
                    <span @click="cancelSeal" class="span-link" type="error" v-if="currentSeal.status == 2">注销</span> -->
                          <!-- <a href="#">章面变更</a> -->
                          <!-- <span class="span-link" >章面变更</span> -->
                      <a-button type="link" @click="sealEdit" :disabled="!(currentSeal.sealStatus == 4 || currentSeal.sealStatus == 3 || currentSeal.sealStatus == 5)">编辑</a-button>
                      <!-- <span class="span-link" @click="sealChange()">章面变更</span> -->
                      <a-popconfirm title="是否发起章面变更审批?"  ok-text="确定" cancel-text="取消" @confirm="sealChangeInfo" :disabled="!(currentSeal.sealStatus == 4 || currentSeal.sealStatus == 3)">
                          <!-- <a href="#">章面变更</a> -->
                          <!-- <span class="span-link" >章面变更</span> -->
                          <a-button type="link" :disabled="!(currentSeal.sealStatus == 4 || currentSeal.sealStatus == 3)">章面变更</a-button>
                      </a-popconfirm>
                      <a-popconfirm title="是否发起印章停用审批?"  ok-text="确定" cancel-text="取消" @confirm="sealStop(1)" :disabled="currentSeal.sealStatus != 4">
                          <a-button type="link" :disabled="currentSeal.sealStatus != 4">停用</a-button>
                      </a-popconfirm>
                      <a-popconfirm title="是否发起印章激活审批?"  ok-text="确定" cancel-text="取消" @confirm="sealStop(2)" :disabled="currentSeal.sealStatus !== 3">
                          <a-button type="link" :disabled="currentSeal.sealStatus !== 3">激活</a-button>
                      </a-popconfirm>
                      <a-popconfirm title="是否发起印章收缴审批?"  ok-text="确定" cancel-text="取消" @confirm="sealStop(3)" :disabled="currentSeal.sealStatus !== 4">
                           <a-button type="link" :disabled="currentSeal.sealStatus !== 4">收缴</a-button>
                      </a-popconfirm>
                      <a-popconfirm title="是否发起印章销毁审批?"  ok-text="确定" cancel-text="取消" @confirm="sealStop(4)" :disabled="currentSeal.sealStatus !== 3">
                          <a-button type="link" :disabled="!(currentSeal.sealStatus == 3)">销毁</a-button>
                      </a-popconfirm>
                  </div>
                </DescriptionsItem>
                <DescriptionsItem label="生成时间">{{currentSeal.createTime}}</DescriptionsItem>
              </Descriptions>
              <!-- <span>编辑印章</span>
              <span><a-button @click="editSeal(false)" type="link" style="font-size:12px">编辑</a-button></span> -->
            </div>
          </div>
      </div>
      <div class="seal-operation">
        <a-tabs @tabClick="tabClick" v-model:active-key="tabKey" :destroyInactiveTabPane="true">
          <a-tab-pane key="SealMake" tab="制作">
            <SealMakeRecord :sealId="currentSeal.sealId" @success="fetch"/>
          </a-tab-pane>
          <a-tab-pane key="SealEdit" tab="编辑">
           <SealEditRecord :sealId="currentSeal.sealId"/>
          </a-tab-pane>
          <a-tab-pane key="SealUpdate" tab="章面变更" >
           <SealChangeRecord :sealId="currentSeal.sealId"/>
          </a-tab-pane>
          <a-tab-pane key="SealState" tab="停用/激活" >
            <SealStateRecord :sealId="currentSeal.sealId"/>
          </a-tab-pane>
          <a-tab-pane key="SealCollection" tab="收缴" >
            <SealDivestedRecord :sealId="currentSeal.sealId"/>
          </a-tab-pane>
          <a-tab-pane key="SealDestroy" tab="销毁" >
            <SealDestructionRecord :sealId="currentSeal.sealId"/>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>
   <!-- <GroupDrawer @register="registerDrawer" @success="handleSuccess" />
    <EditSealDrawer @register="editSealDrawer" @success="handleSuccess" />
    <TabTreeModal @register="registerModal" @success="handleUserSuccess" />
    <AddAuthSeaModal @register="addAuthSealModal" @success="handleAuthSealSuccess" /> -->
  </div>
</template>
<script lang='ts'>
  
  import type { UserInfo } from '/#/store';
  
  import {useRouter} from "vue-router";
  import { defineComponent,ref,onMounted ,unref,h,toRaw,createVNode} from 'vue';
  import { Row, Col ,Tag, Divider,Menu ,Dropdown, MenuItem,Modal,Descriptions,DescriptionsItem,Space } from 'ant-design-vue';
  import { DownOutlined, EllipsisOutlined ,TeamOutlined,ExclamationCircleOutlined } from '@ant-design/icons-vue';
  
  import { BasicTree,TreeItem,TreeActionItem,ContextMenuItem} from '/@/components/Tree/index';
  import {BasicTable,useTable,TableAction,EditRecordRow,} from '/@/components/Table';
  import { Icon } from '/@/components/Icon';

  import TabTreeModal from '/@/views/organize/modal/TabTreeModal.vue';
  
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useDrawer } from '/@/components/Drawer';
  import { useModal } from '/@/components/Modal';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { usePermission } from '/@/hooks/web/usePermission';
  import { useUserStore } from '/@/store/modules/user';
  import { transferRoleType } from '/@/utils/system';
  
  import sealLoad from '/@/assets/images/seal-load.png';
  import { searchFormSchema,tableColumns, authLogTableColumns,getSealType,sealType} from './data';
  
  import {Process} from "/@/views/signature/data/ProcessConfig";
  
  import { getImgBase64} from '/@/api/sys/upload'; 
  import { getDeptNoCountTree } from '/@/api/sys/dept'; 
  import { getRoleTreeList,getUserByRoleId,  } from '/@/api/sys/role'; 
  import { getUserList } from '/@/api/demo/system'; 
  import { getAuthGroup ,deleteBatchAuth} from '/@/api/auth/group'; 
  import { Api,sealList,getSealDetailes,sealChange,getSealApplyId,sealAuthList } from './api';
  import { getToken } from '/@/utils/auth';
  // import { sealDelete,getSealAuthList,sealSave,addSealAuth,deleteSealAuth,applyCertPersonal} from '/@/api/console/seal'; 
  import templateGz from "/@/assets/images/template-gz.png";
  import {getDefProcess} from "/@/api/bpmns"
  
  import SealMakeRecord from "./record/SealMakeRecord.vue";
  import SealEditRecord from "./record/SealEditRecord.vue";
  import SealChangeRecord from "./record/SealChangeRecord.vue";
  
  import SealStateRecord from "./record/SealStateRecord.vue";
  
  import SealDestructionRecord from "./record/SealDestructionRecord.vue";
  
  import SealDivestedRecord from "./record/SealDivestedRecord.vue";
  
  
  
  
  export default defineComponent({
    name:'权限组管理',
    components: { 
      BasicTree, 
      Row, Descriptions,DescriptionsItem,
      Col,Tag,Space,
      BasicTable,TableAction, Divider,Icon,
      Dropdown,
      DownOutlined,
      Menu,
      MenuItem,
      EllipsisOutlined,
      TeamOutlined,SealStateRecord,
      SealEditRecord,
      TabTreeModal,SealChangeRecord,
      //AuthRecords
      SealMakeRecord,SealDestructionRecord,SealDivestedRecord
    },
    setup() {
      let router = useRouter();
      const userStore = useUserStore();
      const { prefixCls } = useDesign('auth-group');
      const checkedKeys = ref<Array<string | number>>([]);
      const storeUserInfo =  userStore.getUserInfo as UserInfo;
      const { createMessage: msg } = useMessage(); 
      
      const canDrag = ref(false);
      const spinning = ref(false);
      const tabKey = ref("SealMake");
      
      const currentSeal = ref({
        createTime: '',
        fileId: '',
        isDefault: '',
        sealId: '',
        sealName: '',
        sealStyle: '',
        sealType: '',
        sealStatus: 0,
        entpName:'',
        annexId:'',
        createType:'',
        middleText:'',
      })
      
      // const currentSealId = ref()
      // const currentSealName = ref()
      // const currentSealFileId = ref();
      const { hasPermission } = usePermission();
      const basicTree = ref(null)
      const panelShow = ref(true)
      const treeData = ref<TreeItem[]>([]);
      const sealBase64 = ref(null);
      const treeSelectedKeys = ref<string[]>([]);
       
    
      async function fetch() {
        await loadTreeData();
        if( treeData.value.length>0){
          await selfSealDetailes(currentSeal.value.sealId);
        }
        sealAuthList({}).then(res=>{
          console.log("test:",res);
        })
      }
      async function selfSealDetailes(sealId){
        try{
          // currentSeal.value = {
          //   createTime: "2023年8月17日17:51:51",
          //   fileId: "123456",
          //   isDefault:"1",
          //   sealId: "",
          //   sealName: "发票专用章",
          //   sealStyle: "",
          //   sealType: "1",
          //   status: 1,
          //   entpName: storeUserInfo.loginTenantName,
          // }
          getSealDetailes({sealId:sealId}).then((res:any)=>{
            if(res){
              currentSeal.value = {
                createTime: res.createTime,
                fileId: res.fileId,
                isDefault: res.isDefault,
                sealId: res.sealId,
                sealName: res.sealName,
                sealStyle: res.sealStyle,
                sealType: res.sealType,
                sealStatus: res.sealStatus,
                entpName: storeUserInfo.loginTenantName,
                annexId:res.annexId,
                createType:res.createType,
                middleText:res.middleText,
              }
              getImgBase64({imgId:res.annexId}).then(res=>{
                sealBase64.value = res.image;
              });
            }
            console.log("--111",res,currentSeal);
           /* setProps({
              searchInfo:{sealId:sealId}
            });
            reload();
            authLogReload({
              searchInfo:{
               sealId:sealId
              }
            }) */
            
          })
        }catch(e:any){
          console.log(e)
        }
      }
      onMounted(() => {
        fetch();
      });
      async function loadTreeData(){
        spinning.value = true;
        treeData.value = [];  //tree缓存问题
        treeData.value = (await sealList({})) as unknown as TreeItem[];
        // treeData.value.push({
        //   title:"发票专用章",
        //   key:"123456"
        // })
        treeData.value.map(item=>{
          item.title = item.sealName;
          item.key = item.sealId;
        })
        
        if(treeData.value.length>0){
          
          const sealId = treeData.value[0].key as string;
          //签章列表回显背景色
          treeSelectedKeys.value = [sealId];
          //印章id赋值
          currentSeal.value.sealId = sealId;
        }
        spinning.value = false;
      }
      
      function handleMenuClick(){

      }
      
       
      function onTreeSelect(keys,e){
        if(keys.length){
          selfSealDetailes(keys[0]);
          tabKey.value = "SealMake";
          console.log('OK1111111111111',keys);
          // currentSealId.value = keys[0];
          // currentSealName.value = e.node.sealName;
          // currentSealFileId.value =  e.node.fileId;
          // setProps({
          //   searchInfo:{sealId:unref(currentSealId)}
          // });
          // getImgBase64({imgId:currentSealFileId.value}).then(res=>{
          //   sealBase64.value = res;
          // });
        }
      }
      
  

      function handleSuccess() {
        //reload();
        fetch()
      }
      
      function handleAuthSealSuccess(){
        fetch()
      }
       
      function handleAuthGroupSuccess() {
        console.log('00000','更新树')
        loadTreeData();
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
      function editSeal(){
        try{
          checkSealCancel();
          /*router.push({
            path:"/seal/add",
            query:{
              sealId: currentSeal.value.sealId
            }
          });*/
          // editDrawer(true,{
          //   sealName:currentSeal.value.sealName,
          //   sealType:currentSeal.value.sealType,
          //   sealId: currentSeal.value.sealId,
          // }) 
        }catch(e:any){
          console.log(e);
        }
        
      }
      const actionList: TreeActionItem[] = [
        {
          render: (node) => {
            return h(EllipsisOutlined, {
              class: 'ml-2',
              onClick: () => {
                getRightMenuList(node)
              },
            });
          },
        },
      ];
      function addSeal(){
        getDefProcess({processCode:Process.SealMake}).then(result=>{
          if(result && result.length>0){
            const token = getToken() as string;
            const flowId = result[0].id;
            const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
            // 
            let formMap = JSON.stringify({
              "RESRUN_FLOW_OPERATE_TYPE":"",
              "RESRUN_FLOW_OPERATE_ID":"",
              // "signEntSealLogApply":currentSeal.value.sealId,
            })
            const url = `${base_npmns}/#/bpmns/initiate/index?flowId=${flowId}&formMap=${formMap}&token=${token}`;
            window.open(url);
            // window.open("http://192.168.0.122:8904/#/bpmns/initiate/index?flowId=bcca7e76-3ed9-4a86-ae90-16e8b9743e80&formMap=" + formMap + '&token=' + token)
          }else{
            msg.warning("流程未启用或暂无可用流程使用")
          }
        })
      }
      async function sealChangeInfo(){
        const data = {
          // "annexId": currentSeal.value.annexId,
          // "createType": currentSeal.value.createType,
          "sealId": currentSeal.value.sealId,
          // "middleText": currentSeal.value.middleText,
        }
        const response = await sealChange(data);
        // if(response.code == 200){
        //   console.log(response);
        // }
        
      getDefProcess({processCode:Process.SealChange}).then(result=>{
          if(result && result.length>0){
            const token = getToken() as string;
            const flowId = result[0].id;
            const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
            let formMap = JSON.stringify({
              "RESRUN_FLOW_OPERATE_TYPE":"",
              "RESRUN_FLOW_OPERATE_ID":"",
              "signEntSealLogApply":response.result.sealApplyId,
            })
            const url = `${base_npmns}/#/bpmns/initiate/index?flowId=${flowId}&formMap=${formMap}&token=${token}`;
            window.open(url);
          }else{
            msg.warning("流程未启用或暂无可用流程使用")
          }
        })
      }
      async function sealStop(type){
        var response:any = null;
        if(type == 1){
          response = await getSealApplyId(Api.seal_stop,{sealId:currentSeal.value.sealId});
        }else if(type == 2){
          response = await getSealApplyId(Api.seal_start,{sealId:currentSeal.value.sealId});
        }else if(type == 3){
          response = await getSealApplyId(Api.seal_divested,{sealId:currentSeal.value.sealId});
        }else if(type == 4){
          response = await getSealApplyId(Api.seal_destruction,{sealId:currentSeal.value.sealId});
        }
         
        if(response.sealApplyId){
          getDefProcess({processCode:Process.SealStatus}).then(result=>{
            if(result && result.length>0){
              const token = getToken() as string;
              const flowId = result[0].id;
              const base_npmns = import.meta.env.VITE_BPMS_BASE_URL
              let formMap = JSON.stringify({
                "RESRUN_FLOW_OPERATE_TYPE":"",
                "RESRUN_FLOW_OPERATE_ID":"",
                "signEntSealLogApply":response.sealApplyId,
              })
              const url = `${base_npmns}/#/bpmns/initiate/index?flowId=${flowId}&formMap=${formMap}&token=${token}`;
              window.open(url);
            }else{
              msg.warning("流程未启用或暂无可用流程使用")
            }
          })
        }
      }
      function sealEdit(){
        router.push({
          path:"/seals/edit",
          query:{
            sealId:currentSeal.value.sealId,
            // edit:edit,
          }
        });
      }
      function getRightMenuList(node: any,e?): ContextMenuItem[] {
        console.log(toRaw(node),e,'----ssss---')
        return [
          {
            label: '删除',
            handler: () => {
              Modal.confirm({
                title: '提示',
                icon: createVNode(ExclamationCircleOutlined),
                content: createVNode('div', { style: 'color:red;' }, `确认删“${node.sealName}”这个印章吗？删除后将无法恢复`),
                onOk() {
                  // deleteSeal({sealId:node.sealId}).then(res=>{
                  //   msg.success(`“${node.sealName}”印章删除成功`);
                  //   fetch();
                  // });
                },
                onCancel() {
                  //console.log('Cancel');
                },
                class: 'custom-confirm',
              });
            },
            //auth:['authcategory:edit']
            // icon: 'ant-design:edit-filled',
          },
          {
            label: '编辑',
            handler: () => {
              // console.log(node,"---");
              // editDrawer(true,{
              //   sealId: node.sealId,
              //   sealName : node.sealName,
              //   sealType : node.sealType,
              // })
              router.push({
                path:"/seal/add",
                query:{
                  sealId: node.sealId
                }
              });
            },
            auth:['authcategory:edit']
            // icon: 'ant-design:edit-filled',
          },
        ];
      }
       
      function checkSealCancel(){
        if(currentSeal.value.sealStatus == 3){
          msg.warning("印章已注销无法进行操作")
          throw new Error('印章已注销无法进行操作')
        }
      }
      function tabClick(val:string){
       
      }
      
      return { 
        treeData,currentSeal,fetch,
        //registerAuthModal,
        onTreeSelect, 
        actionList,
        canDrag,sealChangeInfo,
        loadManageName,
        transferRoleType,
        createIcon ,
        hasPermission,
        prefixCls,
        handleSuccess,
        handleAuthGroupSuccess,
        panelShow,
        handleMenuClick,
        checkedKeys,
        spinning,
        tabClick,
        editSeal,sealEdit,
        sealBase64,sealStop,
        getRightMenuList,handleAuthSealSuccess,
        addSeal,getSealType,sealType,
        treeSelectedKeys,templateGz,tabKey
      }
    },
  });
</script>

<style lang="less" scoped>
  .seal-header{
  }
  .seal-tips{
    font-size:12px;
    color:#999;
  }
  .seal-img-box{
    padding:10px;
    border: 1px dashed #e3e3e3;
    width: 140px;
    height: 140px;
    box-sizing: content-box;
    .seal-img{
      width: 100%;
      height: 100%;
    }
  }
  .seal-title{
    height: 40px;
    line-height: 40px;
    border-bottom: 1px solid #e3e3e3;
    padding:0 20px;
  }
  .seal-detailes{
    padding:0 20px;
    display: flex;
  }
  .seal-info{
    padding-top: 20px;
  }
  .seal-operation{
    padding:0 20px;
  }
  .resrun-auth-group-custom-title{
    font-weight: 600;
  }
</style>
<style>
  .custom-confirm{
    top:30% !important;
  }
</style>

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
  <div class="draft-container">
    <div class="container-list">
      <Scrollbar width="100%" height="100%" :native="true" :noresize="true">
        <a-spin  :spinning="loading" :absolute="false" tip="加载中...">
          <div v-if="!dataSource || dataSource.length == 0" class="data-empty">
            <a-empty :image="simpleImage"/>
          </div>
          <div class="contract-item" v-for="(item,recordIndex) in dataSource">
            <div class="item-top" style="display: flex;">
              <div class="item-top-left" style="flex:1">
                <div class="item-subject" @click="shortcutJump(item)">{{item.subject}}
                  <span style="margin-left: 10px;  color: #555; font-weight: 400;" v-if="item.code">（编号：{{item.code}}）</span>
                  
                </div>
                    <a-descriptions style="padding-bottom:0" :column="{ xxl: 24, xl: 24, lg: 24, md: 24}">
                      <a-descriptions-item :span="24">
                        <a-space :size="10">
                          <SvgIcon name="sign-schedule"></SvgIcon>
                          <span>签署方：</span>
                        </a-space>
                        <a-tooltip placement="top" color="#fff" :mouseEnterDelay="0.5"
                         :getPopupContainer="(triggerNode: any) => {return triggerNode.parentNode}"
                         :overlay-style="{'max-width':'800px'}">
                          <template #title>
                            <ContractListStatus :signRuId="item.signRuId" :signOrder="1" />
                          </template>
                          <span class="pointer schedule-hover">{{item.participateNames?item.participateNames:'张三'}}</span>
                        </a-tooltip>
                      </a-descriptions-item>
                    </a-descriptions>
                </div>
                <div style="width:200px;display: flex;justify-content: right;align-items: center;">
                  <div id="recordAct">
                    <a-button type="default" class="see-menu" @click="handleSee(item)">查看</a-button>
                    <a-dropdown trigger="click" :overlayStyle="overlayStyle" :getPopupContainer="getPopupContainer" 
                      >
                      <a class="ant-dropdown-link" @click="handleAction(item)">
                        <a-button class="down-menu">
                          <Icon icon="ant-design:down-outlined" />
                        </a-button>
                      </a>
                      <template #overlay>
                        <a-menu v-if="(!!actionList && actionList.length>0) || (item.startFlag && loadShowExpireDate(item)) || item.downloadFlag">
                          <a-menu-item v-for="(item,index) in actionList" :key="index" @click="item.callback(item.params,router)">
                            <a-button type="link" >{{ item.name }}</a-button>
                          </a-menu-item>
                          <a-menu-item v-if="item.startFlag && loadShowExpireDate(item)">
                            <a-popover title="截止时间设置" trigger="click" :visible="item.popVisible" :destroyTooltipOnHide="true">
                              <template #content>
                                <a-date-picker format="YYYY-MM-DD" v-model:value="expireDate" style="width:300px" :allowClear="true"  :disabled-date="disabledDate"></a-date-picker>
                                <div class="expiredate-footer" style="text-align: right;margin-top:10px">
                                  <a-button type="default" style="margin:0 10px" @click="handleCancelPop(item,recordIndex)">取消</a-button>
                                  <a-button type="primary" @click="handleSaveExpireDate(item)">保存</a-button>
                                </div> 
                              </template>
                              <a-button type="link" @click="handleShowPop(item)">{{loadExpireDateText(item)}}</a-button>
                            </a-popover>
                          </a-menu-item>
                          <a-menu-item v-if="item.downloadFlag">
                            <a-button type="link" @click="handleDownload(item)">下载</a-button>
                          </a-menu-item>
                        </a-menu>
                        <a-menu v-else>
                          <a-menu-item>
                            <span class="not_more_operations">没有更多操作</span>
                          </a-menu-item>
                        </a-menu>
                      </template>
                    </a-dropdown>
                  </div>
                </div>
              </div>
              
              
            <div class="item-bottom">
              <span class="signatory-line">
                <div class="info-filed" >
                  <SvgIcon name="end-time" style="line-height:20px"></SvgIcon>
                  <span>签署截止时间：{{item.expireDate?item.expireDate:'--'}}</span>
                </div>
              </span>
              <span class="signatory-line">
                <div class="info-filed">
                  <SvgIcon name="contract-status" style="line-height:20px"></SvgIcon>
                  <span>{{ loadRuStatus(item.status) }}</span>
                </div>
              </span>
              <span style="flex:1;">
                <div class="info-filed" style="justify-content:right">
                  <SvgIcon name="send-line" style="line-height:20px"></SvgIcon>
                  由 {{ item.fromTenantName }} 于 {{ item.createTime}} 发起
                </div>
              </span>
            </div>
          </div>
        </a-spin>
      </Scrollbar>
      <div class="container-pagination" >
        <a-pagination v-model:current="pageinfo.current" show-quick-jumper :total="pageinfo.total" v-model:page-size="pageinfo.pageSize"
         @change="onPaginationChange" :show-total="total => `共 ${total} 条数据`"  show-less-items hideOnSinglePage/>
      </div>
    </div>
    <!-- <WishModal @register="registerWish" @success="handleConfirmSuccess"></WishModal> -->
    
  </div>
</template>

<script lang="ts">
import { ref,defineComponent, onMounted, getCurrentInstance, createVNode,onBeforeUnmount,onUnmounted} from "vue";
import type { Dayjs } from 'dayjs';
import { BasicTable, useTable } from '/@/components/Table';
import { draftColumns } from './data';
import dayjs from 'dayjs';
import { setRuExpireDate, revokeRuSignRu } from '/@/api/contract';
import { useMessage } from '/@/hooks/web/useMessage';
import { useRouter } from 'vue-router';
import WishModal from '/@/views/contract/modal/wish/WishModal.vue';
import { useModal } from '/@/components/Modal';
import { Scrollbar } from '/@/components/Scrollbar';
import { Empty } from 'ant-design-vue';
import { Modal, Input, Button } from 'ant-design-vue'; 
import { 
   getCompanyAllList,
   getCompanyCopyMeList,
   getFinishList, 
   getCompanyDraftList, 
   getInvalidList,
   getCheckOperates, 
   getPendingList,
   getCompanyBoxList,
   getCompanyRecycleMeList,
   getCompanySendList,
   getRunningList,
   getOtherJobList

} from '/@/api/doc';
import { Icon,SvgIcon } from '/@/components/Icon';
import { loadRuStatus } from '../transform';
import { formatAction, tableReload,testRevoke, Action } from './actions';
import { getConfirmType, getConfirmNo, getBusinessRuInfo } from '/@/api/contract';
import ContractListStatus from "/@/views/contract/components/ContractListStatus.vue";
import { handleRuDownload } from '/@/utils';
import { any } from "vue-types";

interface TaskListInfoRes {
  taskId?: string;
  signRuId: string;
  status: number;
  code?: string;
  subject: string;
  fromTenantName: string;
  participateNames?: string;
  createTime: string;
  expireDate?: string;
  startFlag?: boolean;
  downloadFlag?: boolean;
  popVisible?: boolean;
  [key: string]: any; // 允许其他动态属性
}

export default defineComponent({
  name:"All",
  components:{
    BasicTable,
    Icon,Scrollbar,
    WishModal,SvgIcon,
    ContractListStatus,
    Modal, Input, Button,
  },
  props:{
    pageinfo:{
      type:Object,
      default:{
          pageNo:1,
          pageSize:10,
          current:1,
          total:0,
      }
    }
  },
  setup(props) {
    const router = useRouter();
    const actionList = ref<Action[]>([])
    const dataSource = ref<TaskListInfoRes[]>([])
    const requestMethod = ref()
    const currentParams = ref({})
    const popVisible = ref(false)
    const expireDate = ref<Dayjs | null>(dayjs());
    const orderNo = ref<string>('');
    const overlayStyle = ref({});

    const instance = getCurrentInstance();
    const eventHub = instance?.proxy?.$options?.setup?.['eventHub'] || { 
      $on: (event: string, callback: Function) => {},
      $off: (event: string, callback: Function) => {}
    };
    const rowParams = ref();
    const willResult = ref(false);
    const loading = ref(true);

    const { createMessage: msg,} = useMessage();
    const getPopupContainer = ()=> {
      return document.getElementById('recordAct') ||  document.body;
    }

    const [registerWish, { openModal:openWishModal,closeModal:closeWishModal }] = useModal();


    const [registerTable, { reload, getDataSource, setPagination }] = useTable({
        title: '',
        api: getCompanyAllList,
        columns:draftColumns,
        isTriggerSelect:false,
        immediate:false,
        formConfig: {
          labelWidth: 120,
        },
        
        fetchSetting:{
          listField:'records'
        },
        useSearchForm: false,
        showTableSetting: false,
        tableSetting: { fullScreen: false ,redo:true,setting:false,size:false},
        showIndexColumn: false,
        rowKey: 'id',
        striped: false,
        bordered: false,
        canResize: false,
        beforeFetch:beforeFetch,
      })

      const disabledDate = (current: Dayjs) => {
        return current && current < dayjs().endOf('day');
      };



      
      function beforeFetch(params){
        if(params.createTime && params.createTime.length){
          const startOfDay = dayjs(params.createTime[0]).startOf('day');
          const endOfDay = dayjs(params.createTime[1]).endOf('day')
          params.beginCreateTime = startOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.endCreateTime = endOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.createTime = undefined;
        }
        if(params.startTime &&params.startTime.length){
          const startOfDay = dayjs(params.startTime[0]).startOf('day');
          const endOfDay = dayjs(params.startTime[1]).endOf('day')
          params.beginStartTime = startOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.endStartTime = endOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.startTime = undefined;
        }
        if(params.finishTime && params.finishTime.length){
          const startOfDay = dayjs(params.finishTime[0]).startOf('day');
          const endOfDay = dayjs(params.finishTime[1]).endOf('day')
          params.beginFinishTime = startOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.endFinishTime =  endOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.finishTime = undefined;
        }
        // return params
      }
      // 自定义的textarea数据  
      const textareaValue = ref('');  
      function handleInput(e){
        textareaValue.value = e.target.value;
      }
      function triggerConfirmFun(params){
        rowParams.value = params;
        Modal.confirm({
          title: '撤回',
          content: createVNode(Input.TextArea, {
            text: textareaValue.value,
            placeholder:'请填写撤回原因，200字以内',
            maxlength:200,
            onChange: handleInput,
          }),
          onOk() {
            return new Promise((resolve,reject)=>{
            if(!textareaValue.value){
                  msg.warning('请输入原因')
                  reject('请输入原因')
              }else if(textareaValue.value.length < 1 || textareaValue.value.length>200){
                msg.warning('字数不符合要求')
                reject('字数不符合要求')
              }else{
                handleConfirmSuccess();
                resolve('ok')
              }
            })
          }
        })
      }
      onUnmounted(()=>{
        if(eventHub && typeof eventHub.$off === 'function') {
          eventHub.$off('triggerConfirm',triggerConfirmFun)
        }
      })
      onMounted(()=>{
        if(eventHub && typeof eventHub.$on === 'function') {
          eventHub.$on('triggerConfirm',triggerConfirmFun)
        }
      })

      function getConfirmTypeForAction(params,signRuId){
          return new Promise(async (resolve,reject)=>{
          let resultNo = await getConfirmNo({mainId:signRuId})
            if(resultNo){
              orderNo.value = resultNo;
            }
            let result = await getConfirmType(params)
            if(result){
                resolve(result)
            }else{
                reject(result)
            }
          })
      }
   

      async function loadData(params,resetPage?: number){
        currentParams.value = params;
        if(params.type==1){
          requestMethod.value = getCompanyBoxList
        }
        if(params.type==2){
          requestMethod.value = getCompanySendList
        }
        if(params.type==3){
          requestMethod.value = getCompanyCopyMeList
        }
        if(params.type==4){
          requestMethod.value = getCompanyDraftList
        }
        if(params.type==5){
          requestMethod.value = getCompanyRecycleMeList
        }
        if(params.type==6){
          requestMethod.value = getCompanyAllList
        }
        if(params.type==7){
          requestMethod.value = getPendingList
        }
        if(params.type==8){
          requestMethod.value = getOtherJobList
        }
        if(params.type==9){
          requestMethod.value = getRunningList
        }
        if(params.type==10){
          requestMethod.value = getFinishList
        }
        if(params.type==11){
          requestMethod.value = getInvalidList
        }
        loading.value = true;
        tableReload(requestMethod.value, params, setTableData, setPagination, setLoading)
        // setLoading(true)
        // setTableData([])
        if(resetPage){
            // setPagination({current:1})
        }
        beforeFetch(params)
        let result = await requestMethod.value({...params,type:undefined});
        if(result){
          result.records.map(v=>{
            if(v){
              v.popVisible = false;
            }
          })
          dataSource.value = result.records;
          // setTableData(result.records)
          props.pageinfo.total =  result.total;
        }
        loading.value = false;
        // let list = getDataSource();
        
      }
      function setTableData(result){
        dataSource.value = result.records;
        props.pageinfo.total =  result.total;
      }
      function setLoading(flag){
        loading.value = flag;
      }

      function handleWrite(row){
        // window.open('/#/contract/params?__full__&signRuId=' + row.signRuId)
        router.push({
          path:"/contract/params",
          query:{
            __full__:"",
            signRuId:row.signRuId
          }
        })
      }
      async function handleSee(row){
        if(row.status==1){
          // window.open('/#/contract/start?__full__&signRuId=' + row.signRuId + '&,')
          router.push({
            path:"/contract/start",
            query:{
              __full__:"",
              signRuId:row.signRuId
            }
          })
          return;
        }else{
          router.push({
            path:"/contract/detail/sign",
            query:{
              __full__:"",
              signRuId:row.signRuId,
              from:'list',
            }
          })
        }
        return;
        // if(row.status==5){
        //   // window.open('/#/contract/params?__full__&signRuId=' + row.signRuId + '&isDetail=true' + '&from=list')
        //   // 如果文档处于填写中，且当前用户无填写权限，则查看时，直接进入到【文档详情】页面
        //   let result = await getCheckOperates({signRuId:row.signRuId});
        //   if(result){
        //     if(result.taskId){
        //       // window.open('/#/contract/params?__full__&signRuId=' + row.signRuId + '&isDetail=false&type=receive&taskId='+result.taskId + '&from=list')
        //       router.push({
        //         path:"/contract/params",
        //         query:{
        //           __full__:"",
        //           signRuId:row.signRuId,
        //           isDetail:'false',
        //           type:'receive',
        //           taskId:result.taskId,
        //           from:'list'
        //         }
        //       })
              
        //     }else{
        //       // window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId + '&from=list')
        //       router.push({
        //         path:"/contract/detail/sign",
        //         query:{
        //           __full__:"",
        //           signRuId:row.signRuId,
        //           from:'list'
        //         }
        //       })
              
        //     }
        //   }
        //   return;
        // }
        // if(row.status==7){
        //   // 如果文档处于签署中，且当前用户无签署权限，则查看时，直接进入到【文档详情】页面
        //   let result = await getCheckOperates({signRuId:row.signRuId});
        //   if(result){
        //     if(result.taskId){
        //       // window.open('/#/contract/sign?__full__&signRuId=' + row.signRuId + '&from=list&taskId='+result.taskId)
        //       router.push({
        //         path:"/contract/sign",
        //         query:{
        //           __full__:"",
        //           signRuId:row.signRuId,
        //           from:'list',
        //           taskId:result.taskId
        //         }
        //       })
              
        //     }else{
        //       // window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId + '&from=list')
        //       router.push({
        //         path:"/contract/detail/sign",
        //         query:{
        //           __full__:"",
        //           signRuId:row.signRuId,
        //           from:'list',
        //         }
        //       })
        //     }
        //   }
        //   return;
        // }
        // router.push({
        //   path:"/contract/detail/sign",
        //   query:{
        //     __full__:"",
        //     signRuId:row.signRuId,
        //     from:'list',
        //   }
        // })
        
        // if(row.status==2 || row.status==3 ){
        //   window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId + '&from=list')
        // }
        // if(row.status==4){
        //   window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId + '&from=list')
        // }
        // if(row.status==6 || row.status==8 || row.status==9){
        //   window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId)
        // }
        // if(row.status==10){
        //   window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId + '&from=list')
        // }
        // if(row.status==11){
        //   window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId +  '&from=list')
        // }
        // if(row.status==12){
        //   window.open('/#/contract/detail/sign?__full__&signRuId=' + row.signRuId + '&from=list')
        // }
      }

      function handleChange(pagination){
        console.log(pagination,'分页信息')
        loadData({
          ...currentParams.value,
          pageNo:pagination.current,
          pageSize:pagination.pageSize
        })
      }

      function loadExpireDateText(record){
        let str = '';
        if(record.status==1 || record.status==2 || record.status==3  || record.status==4){
          if(record.expireDate){
            str = '修改签署截止日期';
          }else{
            str = '设置签署截止日期';
          }
        }
        if((record.status==5 || record.status==7) && !record.expireDate){
          str = '设置签署截止日期';
        }
        return str;
      }

      function loadShowExpireDate(record){
        let show = false;
        if(record.status==1 || record.status==2 || record.status==3 || record.status==4){
          show = true;
        }
        if((record.status==5 || record.status==7) && !record.expireDate){
          show = true;
        }
        return show;
      }
      

     async function shortcutJump(row){
       let result = await getCheckOperates({signRuId:row.signRuId});
       const tem = {...row,result};
       const action  = formatAction(tem,result)
       var operationFlag = false;
       if(action && action.length>0){
         for(var i=0;i<action.length;i++){
           const item = action[i];
           if(item.key == 'sign' || item.key == 'write'){
             item.callback(item.params,router)
             operationFlag = true;
             break;
           }
         }
         //return false;
       }
       if(!operationFlag){
         handleSee(row);
       }
       // console.log("shortcutJump:",action);
     }
     async function handleAction(row){
        expireDate.value = null;
        let result = await getCheckOperates({signRuId:row.signRuId});
        if(result){
          row.startFlag = result.startFlag;
          row.downloadFlag = result.downloadFlag;
          row ={
            ...row,
            result
          }
          actionList.value = formatAction(row,result)
        }
      }

     async function handleDownload(row){
        let result = await getBusinessRuInfo({ signRuId: row.signRuId  });
        if (result) {
          if (result.fileList.length > 1) {
            // 多文件，循环遍历，将文件id集合放入参数中
            const ruDocIdList: string[] = [];
            result.fileList.forEach((item: any) => {
              return ruDocIdList.push(item.id);
            });
            handleRuDownload({ ruDocIdList: ruDocIdList, signRuId: row.signRuId });
          } else {
            handleRuDownload({ ruDocIdList: result.fileList[0].id, signRuId: row.signRuId });
          }
        }
      }



      function searchReload(params){
        console.log("searchReload",params);
        reload({searchInfo:{
          ...params
        }})
      }

      async function handleSaveExpireDate(row){
        if(!expireDate.value){
            msg.warning('请设置签署截止日期')
            return
          }
        const endOfDay = dayjs(expireDate.value).endOf('day');
              endOfDay.format('YYYY-MM-DD')
              console.log(expireDate.value,242323232)
          
        let result = await setRuExpireDate({signRuId:row.signRuId,expireDate:endOfDay});
        if(result){
          loadData({
          ...currentParams.value,
          })
        }
      }
      function handleCancelPop(row,index){
        
        row.popVisible = false
        
      }
      function handleShowPop(row){
        row.popVisible = true
        
      }
      async function handleConfirmSuccess(){
        let result = await revokeRuSignRu({signRuId:rowParams.value.signRuId, signConfirmOrderNo: "123", comment:textareaValue.value});
        if(result){
          msg.success('操作成功')
            reload({searchInfo:{
                ...currentParams.value
              }
            })
            setLoading(false)
            closeWishModal()
        }
      }
      async function onPaginationChange(val){
        await loadData({
          ...currentParams.value,
          pageNo:val,
          pageSize:props.pageinfo.pageSize
        })
        props.pageinfo.current =  val;
        
      }
    return {
      registerTable,
      handleShowPop,
      loadRuStatus,
      handleWrite,
      handleAction,
      actionList,
      handleSee,
      searchReload,
      loadData,shortcutJump,
      disabledDate,
      handleChange,
      loadExpireDateText,
      loadShowExpireDate,
      dataSource,
      handleSaveExpireDate,
      expireDate,
      overlayStyle,
      getPopupContainer,
      popVisible,
      handleCancelPop,
      router,onPaginationChange,
      registerWish,loading,
      handleConfirmSuccess,
      simpleImage: Empty.PRESENTED_IMAGE_SIMPLE,
      handleDownload,
    }
  }
})
</script>

<style lang="less" scoped>

  .draft-container{
    height: calc(100vh - 200px);
    overflow: hidden;
    background-color: rgba(0,0,0,0.02);
    padding: 0 10px 10px 10px;
    :deep(.ant-pagination){
      position:sticky;
      width:100%;
      bottom:-1px;
    }
  }
  .container-list{
    height: calc(100% - 50px);
    .contract-item{
      width: calc(100% - 10px);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
      background-color: #fff;
      border-radius: 5px;
      margin-top: 10px;
      padding: 10px 20px;
      position: relative;
      .item-subject{
        font-size: 14px;
        font-weight: 600;
        &:hover{
          color: #379bde;
          cursor: pointer;
        }
      }
    }
    .contract-item:hover{
      // background-color: rgba(0, 0, 0, 0.03);;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2)
    }
    .contract-item:hover::before{
        content:'';
        left:0;
        top:0;
        height: 100%;
        width: 2px;
        background-color: #379bde;
        position: absolute;
        border-radius: 1px;
    }
    
    
    :deep(.ant-descriptions-item){
      padding-top: 8px !important;
      padding-bottom: 0px !important;
    }
  }
  .container-pagination{
    height: 50px;
    width: 100%;
    text-align: right;
  }
  .down-menu{
    border-left:none;
    border-top-left-radius: 0;
    border-bottom-left-radius: 0;
    background:#f9f9f9;
  }
  .see-menu{
    width:90px;
    // border-right: none;
    border-top-right-radius: 0;
    border-bottom-right-radius: 0;
    background:#f9f9f9;
  }
  #recordAct{
    position:relative;
    text-align: right;
    :deep(.ant-dropdown-menu-item){
      padding:0;
    }
  }
  .signatory-line{
    width:220px;
    font-size: 14px;
    display: inline-block;
    line-height: 30px;
  }
  .info-filed{
    gap: 10px;
    line-height:30px;
    display: flex;
    align-items: center;
  }
  .item-bottom{
    display:flex;
    span{
      display: blok;
    }
  }
  .data-empty{
    width: 100%;
    /* height: 500px; */
    /* background-color: #fff; */
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .not_more_operations{
    padding: 4px 10px;
  }
  .schedule-hover:hover{
    color: #379bde;
  }
</style>

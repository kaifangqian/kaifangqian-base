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
    <BasicTable @register="registerTable" @change="handleChange">
          <template #subject="{record}">
            <p  class="doc-thema">{{ record.subject }}</p>
            <p style="color:#666">发送方：{{ record.fromTenantName }}</p>
          </template>
          <template #status="{record}">
            <span>{{ loadRuStatus(record.status) }}</span>
          </template>
          <template #createTime="{record}">
            <p>{{ record.createTime }}</p>
            <p>截止时间：{{ record.expireDate || '--'}}</p>
          </template>
          <template #action="{record,recordIndex}">
              <div id="recordAct">
                <a-button type="default" class="see-menu" @click="handleSee(record)">查看</a-button> 
                <a-dropdown trigger="click" :overlayStyle="overlayStyle" :getPopupContainer="getPopupContainer">
                  <a class="ant-dropdown-link" @click="handleAction(record)">
                    <a-button class="down-menu">
                      <Icon icon="ant-design:down-outlined" />
                    </a-button>
                  </a>
                  <template #overlay>
                    <a-menu >
                      <a-menu-item v-for="(item,index) in actionList" :key="index" @click="item.callback(item.params)">
                        <a-button type="link" >{{ item.name }}</a-button>
                      </a-menu-item>
                      <a-menu-item v-if="record.startFlag && loadShowExpireDate(record)">
                        <a-popover title="截止时间设置" trigger="click" :visible="record.popVisible" :destroyTooltipOnHide="true">
                          <template #content>
                            <a-date-picker format="YYYY-MM-DD" v-model:value="expireDate" style="width:300px" :allowClear="true"></a-date-picker>
                            <div class="expiredate-footer" style="text-align: right;margin-top:10px">
                              <a-button type="default" style="margin:0 10px" @click="handleCancelPop(record,recordIndex)">取消</a-button>
                              <a-button type="primary" @click="handleSaveExpireDate(record)">保存</a-button>
                            </div> 
                          </template>
                          <a-button type="link" @click="handleShowPop(record)">{{loadExpireDateText(record)}}</a-button>
                        </a-popover>
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
              </div>
          </template>
    </BasicTable>
  </div>
</template>

<script lang="ts">
import { ref,defineComponent, onMounted} from "vue";
import type { Dayjs } from 'dayjs';
import { BasicTable, useTable } from '/@/components/Table';
import { draftColumns } from './data';
import dayjs from 'dayjs';
import { setRuExpireDate } from '/@/api/contract';
import { useMessage } from '/@/hooks/web/useMessage';

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
import { Icon } from '/@/components/Icon';
import { loadRuStatus } from '../transform';
import { formatAction, tableReload } from './actions';

export default defineComponent({
  name:"All",
  components:{
    BasicTable,
    Icon
  },
  setup() {
    const actionList:any = ref([])
    const requestMethod = ref()
    const currentParams = ref({})
    const popVisible = ref(false)
    const expireDate = ref<Dayjs>()
    const overlayStyle = ref({
      width:'137px',
      marginTop:'18px'
      // left:'1522px!important'
    })
    const { createMessage: msg,} = useMessage();
    const getPopupContainer = ()=> {
      return document.getElementById('recordAct') ||  document.body;
    }


    const [registerTable, { reload, getDataSource, setTableData, setPagination, setLoading }] = useTable({
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



      
      function beforeFetch(params){
        if(params.createTime.length){
          const startOfDay = dayjs(params.createTime[0]).startOf('day');
          const endOfDay = dayjs(params.createTime[1]).endOf('day')
          params.beginCreateTime = startOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.endCreateTime = endOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.createTime = undefined;
        }
        if(params.startTime.length){
          const startOfDay = dayjs(params.startTime[0]).startOf('day');
          const endOfDay = dayjs(params.startTime[1]).endOf('day')
          params.beginStartTime = startOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.endStartTime = endOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.startTime = undefined;
        }
        if(params.finishTime.length){
          const startOfDay = dayjs(params.finishTime[0]).startOf('day');
          const endOfDay = dayjs(params.finishTime[1]).endOf('day')
          params.beginFinishTime = startOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.endFinishTime =  endOfDay.format('YYYY-MM-DD HH:mm:ss');
          params.finishTime = undefined;
        }
        // return params
      }

      onMounted(()=>{

      })
   

      async function loadData(params){
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
        tableReload(requestMethod.value, params, setTableData, setPagination, setLoading)
        setLoading(true)
        setTableData([])
        beforeFetch(params)
        let result = await requestMethod.value({...params,type:undefined});
        if(result){
       
          setTableData(result.records)
          setPagination({total:result.total})
        }
        setLoading(false)
        let list = getDataSource();
        list.map(v=>{
          v.popVisible = false;
        })
      }

      function handleWrite(row){
        window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/params?__full__&signReId=&signRuId=' + row.signRuId)
      }
      function handleSee(row){
        if(row.status==1){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/start?__full__&signReId=&signRuId=' + row.signRuId + '&isDetail=true')
        }
        if(row.status==2 || row.status==3 ){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/detail/sign?__full__&signReId=&signRuId=' + row.signRuId + '&from=list')
        }
        if(row.status==4){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/detail/sign?__full__&signReId=&signRuId=' + row.signRuId + '&from=list')
        }
        if(row.status==5){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/params?__full__&signReId=&signRuId=' + row.signRuId + '&isDetail=true' + '&from=list')
        }
        if(row.status==6 || row.status==8 || row.status==9){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/detail/sign?__full__&signReId=&signRuId=' + row.signRuId)
        }
        if(row.status==7){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/sign?__full__&signReId=&signRuId=' + row.signRuId + '&isDetail=true'  + '&from=list')
        }
        if(row.status==10){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/detail/sign?__full__&signReId=&signRuId=' + row.signRuId + '&from=list')
        }
      
        if(row.status==11){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/detail/sign?__full__&signReId=&signRuId=' + row.signRuId +  '&from=list')
        }
        if(row.status==12){
          window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/detail/sign?__full__&signReId=&signRuId=' + row.signRuId + '&from=list')
        }
      
       
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

     async  function handleAction(row){
        expireDate.value= ''
        let result = await getCheckOperates({signRuId:row.signRuId});
        if(result){
          row.startFlag = result.startFlag;
          row ={
            ...row,
            result
          }
          actionList.value = formatAction(row,result)
        }
      }

      function searchReload(params){
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
      

    return {
      registerTable,
      handleShowPop,
      loadRuStatus,
      handleWrite,
      handleAction,
      actionList,
      handleSee,
      searchReload,
      loadData,
      handleChange,
      loadExpireDateText,
      loadShowExpireDate,
      handleSaveExpireDate,
      expireDate,
      overlayStyle,
      getPopupContainer,
      popVisible,
      handleCancelPop,
    }
  }
})
</script>

<style lang="less" scoped>

.down-menu{
  border-left:none;
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
  background:#f9f9f9;
}
.doc-thema{
  margin-bottom:2px;
  font-weight:550;
  font-size: 13px;
  color: #000;
}
.doc-thema:hover{
  color: #379bde;
  cursor:pointer;
}

:deep(.ant-table-thead){
  .ant-table-cell{
    background:#fff;
  }
  tr{
    th:first-child{
      padding-left:40px!important;
    }
  }
}
:deep(.ant-table-tbody){
  tr:hover{
    // background:rgb(216 237 250);
    td:first-child{
      border-left: 2px solid #379bde;
      box-sizing:border-box;
    }
    td{
      background:rgb(216 237 250);
    }
  }
  tr{
    td:first-child{
      padding-left:40px!important;
      border-left: 2px solid #fff;
    }
  }
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
  :deep(.ant-dropdown-menu-item){
    padding:0;
  }
}
</style>

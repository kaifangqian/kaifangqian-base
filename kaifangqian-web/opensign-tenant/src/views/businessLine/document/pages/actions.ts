/*
 * @description 开放签
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */


import { revokeRuSignRu, restoreRu, deleteSignRu } from '/@/api/contract';
import { useMessage } from '/@/hooks/web/useMessage';
const { createMessage: msg, createConfirm} = useMessage();

let requestFun,requestParams,setTable,setPage,setLoad

export  async function revoke(params) {
  let result = await revokeRuSignRu({signRuId:params.signRuId});
  if(result){
    msg.success('操作成功')
    setLoad(true)
    let reloadResult = await requestFun(requestParams);
    if(reloadResult){
      setTable(reloadResult.records);
      setPage({total:reloadResult.total})
    }
    setLoad(false)
  }
}
export  function send(params) {

}
export  async function deleteRow(params) {

  createConfirm({
    title: '', 
    content: '确定删除该记录？',
    iconType: 'warning',
    cancelText:'取消',
    onCancel() {
      console.log('Cancel');
    },
    onOk:async function  () {
      let result = await deleteSignRu({signRuId:params.signRuId});
      if(result){
        msg.success('操作成功')
        setLoad(true)
        let reloadResult = await requestFun(requestParams);
        if(reloadResult){
          setTable(reloadResult.records);
          setPage({total:reloadResult.total})
        }
        setLoad(false)
      }
    },
  })
  

}
export  function editRow(params) {
  window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/start?__full__&signReId='+'&signRuId=' + params.signRuId )
}
export  async function recover(params) {
  let result = await restoreRu({signRuId:params.signRuId});
  if(result){
    setLoad(true)
    msg.success('操作成功')
    let reloadResult = await requestFun(requestParams);
    if(reloadResult){
      setTable(reloadResult.records);
      setPage({total:reloadResult.total})
    }
    setLoad(false)
  }
}
export  function write(params) {
  window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/params?__full__&signReId='+'&signRuId=' + params.signRuId + '&taskId=' + params.result.taskId + '&type='+ (params.result.startFlag?'':'receive')  + '&from=list')
} 
export  function sign (params) {
  window.open(import.meta.env.VITE_PUBLIC_PATH +'#/contract/sign?__full__&signReId='+'&signRuId=' + params.signRuId + '&taskId=' + params.result.taskId  + '&from=list')
}

export const actions = [
  {
    key:'revoke',
    name:'撤回',
    callback:revoke,
    params:{}
  },
  {
    key:'send',
    name:'发送',
    callback:send,
    params:{}
  },
  {
    key:'edit',
    name:'编辑',
    callback:editRow,
    params:{}
  },
  {
    key:'recover',
    name:'恢复',
    callback:recover,
    params:{}
  },
  {
    key:'write',
    name:'填写',
    callback:write,
    params:{}
  },
  {
    key:'sign',
    name:'签署',
    callback:sign,
    params:{}
  },
  {
    key:'delete',
    name:'删除',
    callback:deleteRow,
    params:{}
  },
]


export function mapActions(keys){
  let acts = []
  keys.forEach(v=>{
    actions.map(m=>{
      if(m.key==v){
        acts.push(m)
      }
    })
  })
  return acts;
}
export function formatAction (row,result){
  //整合参数
  actions.map(m=>{
    m.params = {
      ...row,
      ...result
    }
  })
  let actionResult:any = [];
  //发起审批中
  if(row.status==1){
    actionResult = mapActions(['edit','delete'])
  }
  //发起审批中
  if(row.status==2){
    actionResult = mapActions(['revoke'])
  }
  //发起审批不通过
  if(row.status==3){
    actionResult = mapActions(['edit','delete'])
  }
  //已删除
  if(row.status==4){
    actionResult = mapActions(['recover']);
  }
  //发起审批不通过
  if(row.status==5){
    if(result.startFlag){
      if(result.taskId){
        actionResult = mapActions(['write']);
      }else{
        actionResult = mapActions(['revoke']);
      } 
    }else{
      if(result.taskId){
        actionResult = mapActions(['write']);
      }else{
        actionResult = mapActions([]);
      }
    }
  }
  //已拒填
  if(row.status==6){
    actionResult = []
  }
  //签署中
  if(row.status==7){
    if(result.startFlag){
      if(result.taskId){
        actionResult = mapActions(['sign']);
      }else{
        actionResult = mapActions([]);
      }
    }else{
      if(result.taskId){
        actionResult = mapActions(['sign']);
      }else{
        actionResult = mapActions([]);
      }
    }
  }
  //已拒签
  if(row.status==8){
    actionResult = []
  }
  //已失效
  if(row.status==9){
    actionResult = []
  }
  //已拒签
  if(row.status==10){
    if(result.startFlag){
      actionResult = mapActions(['delete']);
    }
  }
  return actionResult
}

export function tableReload(reloadCallback,params,setTableData,setPagination, setLoading){
  requestFun = reloadCallback;
  requestParams = params;
  setTable = setTableData;
  setPage = setPagination;
  setLoad = setLoading;
}

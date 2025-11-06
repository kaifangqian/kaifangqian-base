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
  <div class="doc-params-container">
    <div class="full-parameter-content">
      <div class="doc-params-set">
        <a-tabs>
          <a-tab-pane key="1" tab="参数填写">
            <a-radio-group v-model:value="isRequired" name="radioGroup" @change="handleFilterParams" :disabled="isDetail">
                <a-radio :value="0">全部</a-radio>
                <a-radio :value="1">仅看必填</a-radio>
                <a-radio :value="2">仅看待我填写</a-radio>
              </a-radio-group>
          </a-tab-pane>
         
        </a-tabs>
      </div>
      <template v-for="(docItem,docIndex) in documentList">
        <div class="doc-item"  v-if="hasWriteControl(docItem.activeControl)" :key="docIndex">
            <div class="doc-header" >
              <SvgIcon name="doc-control" :size="24"></SvgIcon>
              <span>{{ docItem.documentName }}</span>
            </div>
            <ul>
                <li v-for="(item,index) in docItem.activeControl" :key="index" class="writer-li">
                <div v-if="(((item.controlType=='line-text') || (item.controlType=='date') || (item.controlType=='number') ||  (item.controlType=='multiline-text')) || (item.controlType=='image') || (item.controlType=='radio') || (item.controlType=='image') || (item.controlType=='check-box') || (item.controlType=='dropdown-list')) && loadShow(item)">
                  <div :class="['params-header',item.focus?'control-focus-header':'']">
                      <a-tooltip>
                        <template #title>点击后，定位到该参数所在文档位置</template>
                        <SvgIcon name="location"  v-if="!item.focus" :size="20" class="control-pos" @click="handleControlPos(docItem,item)"></SvgIcon>
                        <SvgIcon name="location-focus" v-if="item.focus" :size="20" class="control-pos" @click="handleControlPos(docItem,item)"></SvgIcon>
                      </a-tooltip>
                      <span class="params-name">  <SvgIcon v-if="item.isRequired == 1" name="required" :size="16" class="control-pos"></SvgIcon>{{ item.name }}</span>
                    </div>
                    <div :class="['params-body',item.focus?'':'']"> 
                      <div class="params-select">
                        <a-input  :class="[item.focus?'control-focus-item':'']" v-if="item.controlType == ControlType.LineText" v-model:value="item.value" @change="(e)=>handleWriteChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-input>
                        <a-input :class="[item.focus?'control-focus-item':'']" :min="0" v-if="item.controlType == ControlType.Number"  type="number" v-model:value="item.value" @change="(e)=>handleWriteChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-input>
                        <a-textarea  :class="[item.focus?'control-focus-item':'']"  v-if="item.controlType == ControlType.MultilineText"  :auto-size="{ minRows: 2, maxRows: 5 }" :value="item.value" @change="(e)=>handleWriteChange(e, item)" :placeholder="item.value" :disabled="item.isMineFlag!=1 || isDetail"></a-textarea>
                        <a-datepicker style="width:100%"   :class="[item.focus?'control-focus-item':'']"  v-if="item.controlType == ControlType.Date" 
                        v-model:value="item.value" :format="item.format?.toUpperCase()" :value-format="item.format?.toUpperCase()" 
                        @change="(e)=>handleWriteDateChange(e, item)" placeholder="请选择日期" 
                        :disabled="item.isMineFlag!=1 || isDetail" ></a-datepicker>
                        <a-select :class="[item.focus?'control-focus-item':'']"  v-model:value="item.value" v-if="item.controlType  == ControlType.DropdownList"   :disabled="item.isMineFlag!=1 || isDetail">
                            <a-select-option   :value="radionItem.uid" v-for="(radionItem) in item.widgetList" :key="radionItem.uid">{{ radionItem.n }} </a-select-option>
                        </a-select>
                        <a-radio-group  :class="['radio-group-box',item.focus?'control-focus-item':'']"  v-model:value="item.value" v-if="item.controlType  == ControlType.Radio"  :disabled="item.isMineFlag!=1 || isDetail">
                          <template v-for="(radionItem,radionIndex) in item.widgetList" :key="radionIndex">
                              <div class="radio-item-box">
                                  <a-radio  :style="radioStyle" :value="radionItem.uid" >  {{ radionItem.n }} </a-radio>
                              </div>
                            </template>
                        </a-radio-group>
                        <a-checkbox-group :class="['radio-group-box',item.focus?'control-focus-item':'']"  v-model:value="item.values" v-if="item.controlType  == ControlType.CheckBox"  :disabled="item.isMineFlag!=1 || isDetail">
                          <template v-for="(radionItem,radionIndex) in item.widgetList" :key="radionIndex">
                              <div class="radio-item-box">
                                  <a-checkbox  :style="radioStyle" :value="radionItem.uid" >  {{ radionItem.n }} </a-checkbox>
                              </div>
                            </template>
                        </a-checkbox-group>
                        <div v-if="item.controlType  == ControlType.Image" style="flex:1" :class="[item.focus?'control-focus-item':'']" >
                          <span v-if="!item.uploadImg">{{loadImg(item)}}</span>
                          <div  class="upload-img-control"  v-if="!item.uploadImg"> 
                            <a-upload
                              :disabled="item.isMineFlag !=1 || isDetail"
                              v-model:file-list="fileList"
                              :showUploadList="false"
                              name="file"
                              accept="image/png"
                              action="/resrun-paas/file"
                              :headers="uploadHeaders"
                              @change="(e)=>handleImgChange(e,item)"
                            >
                              <a-button>
                                <upload-outlined></upload-outlined>
                                点击上传
                              </a-button>
                            </a-upload>
                          </div>
                          <div v-else class="reupload-area">
                            <img  :src="item.uploadImg" />
                            <div class="reload-action">
                              <a-button type="default" v-if="item.isMineFlag == 1" @click="handleDeleteImg(item)">删除</a-button>
                              <a-upload
                              :showUploadList="false"
                              :disabled="item.isMineFlag !=1 || isDetail"
                              v-model:file-list="fileList"
                              name="file"
                              accept="image/png"
                              action="/resrun-paas/file"
                              :headers="uploadHeaders"
                              @change="(e)=>handleImgChange(e,item)"
                            >
                              <a-button  class="reupload-btn" v-if="item.isMineFlag == 1 ">
                                <upload-outlined></upload-outlined>
                                重新上传
                              </a-button>
                            </a-upload>
                            </div>
                          </div>
                          
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
          </ul>
        </div>
      </template>

     
    </div>
  </div>
</template>

<script lang="ts">
import {ref,defineComponent, onMounted, reactive, watch} from "vue";
import { Icon,SvgIcon} from "/@/components/Icon";
import { controllerListData } from '../mock/json';
import {ControlType} from "./data/ControlData";
import type { Dayjs } from 'dayjs';
import { UploadOutlined } from '@ant-design/icons-vue';
import moment from 'moment';
import { getToken } from '/@/utils/auth';
import { getImgBase64 } from '/@/api/sys/upload';
import {DatePicker} from 'ant-design-vue';
import { getAppEnvConfig } from '/@/utils/env';

export default defineComponent({
  name:"FullParameter",
  components:{
    Icon,
    SvgIcon,DatePicker,
    UploadOutlined,
    'a-datepicker':DatePicker
  },
  props:{
    controlList:{
      type:Object,
    },
    signerList:{
      type:Object,
    },
    documentList:{
      type:Object,
    },
    signRuId:{
      type:String,
    },
    isDetail:{
      type:Boolean,
    },
  },
  setup(props,{emit}) {
    const docControl:any = ref([]);
    const options:any = ref([])
    const isRequired = ref(0);
    const {VITE_GLOB_APP_CODE} = getAppEnvConfig();
    const radioStyle = reactive({
      "margin":"0",
      "z-inde":"-1"
    });
    const fileList = ref([]);
    const uploadHeaders = ref({
      'X-Access-Token':getToken(),
      'resrun-app-code':VITE_GLOB_APP_CODE
    })
    const uploadImg = ref();

   
    onMounted(()=>{

    })
    function handleWriteChange(e,item){
      // console.log(e,item,'变更值')
      item.value = e.target.value;
    }
    function handleWriteDateChange(e:Dayjs,item){
      // console.log(item, moment(e).format(item.format),'变更时间')
      // item.value = moment(e,).format('YYYY-MM-DD');
    }
    //参数过滤
    function handleFilterParams(val){
      
    }
    function handleControlPos(doc,control){
      if(doc && control){
        emit('scroll',doc,control)
      }
    }
    function loadShow(row){
      if(isRequired.value==0){
        return true
      }
      if(isRequired.value==1){
        return row.isRequired ==1?true:false
      }
      if(isRequired.value==2){
        return row.isMineFlag ==1?true:false
      }
    }

    function hasWriteControl(control){
      let matchControls = control.filter(item=>(item.controlType =='line-text') || (item.controlType=='date') || (item.controlType=='number') ||  (item.controlType=='multiline-text') || (item.controlType=='check-box') || (item.controlType=='radio') || (item.controlType=='dropdown-list') || (item.controlType=='image'))
      return matchControls.length
    }
    function handleImgChange(info:any,item){
      if(info.file.status == 'done'){
        item.value = info.file.response.result
        getImgBase64({imgId:item.value}).then(res=>{
          item.uploadImg = res.image; 
        });
      }
    }
    function loadImg(item){
      if(!item.value) return;
      if(item.controlType != 'image') return;
        getImgBase64({imgId:item.value}).then(res=>{
          item.uploadImg = res.image; 
        });
    }
    function handleDeleteImg(item){
      item.value = '';
      item.uploadImg = ''; 
    }

    return {
      docControl,
      hasWriteControl,
      uploadImg,
      options,
      radioStyle,
      handleWriteChange,
      controllerListData,
      isRequired,
      handleFilterParams,
      ControlType,
      handleWriteDateChange,
      loadShow,
      handleControlPos,
      fileList,
      uploadHeaders,
      handleDeleteImg,
      loadImg,
      handleImgChange,
    }
  }
})
</script>

<style lang="less" scoped>
.full-parameter-content{
  .doc-params-set{
    box-shadow: 5px 5px 20px #0000001a;
    padding:10px 15px 20px;
    margin: 10px 5px 0px;
  }
  .doc-item{
    box-shadow: 5px 5px 20px #0000001a;
    margin: 10px 5px 40px;
    padding: 10px 15px;
    .doc-header{
      display: flex;
      align-items: center;
      margin-bottom:10px;
      font-size: 14px;
      font-weight: 550;
      span{
        margin-right:10px;
      }

    }
  }
  li{
    margin-bottom: 10px;
  }
  .params-header{
    display: flex;
    align-items: center;
      .params-name{
      font-size: 12px;
      font-weight: 550;
    }
  }
  .params-body{
    // background: #f9f9f9;
    padding:8px 0px;
    .params-select{
      display: flex;
      align-items: center;
      span{
        white-space: nowrap;
      }
      :deep(.ant-select){
        width:100%;
        flex:1;
      }
    }
  }
}
.radio-group-box{
  padding-left: 5px;
  .radio-item-box{
    margin:4px 0;
  };
}
.upload-img-control{
  width:100%;
  .ant-btn{
    width:100%;
    // color: #bfbfbf;
  }
  :deep(.ant-upload){
    width:100%;
  }
  :deep(.ant-upload-disabled){
    .ant-btn{
      background:#f5f5f5;
      cursor: not-allowed;
    }
    .ant-btn:hover{
      // color: #bfbfbf;
      border-color: #d9d9d9;
    }
  }
}
.reupload-area{
    width: 100%;
    display: flex;
    flex-direction: column;
    padding:10px 0;
    img{
      display:inline-block;
      width:120px;
      height:120px;
      margin:0 auto;
    }
  .reload-action{
    display:flex;
    justify-content: space-around;
    margin:5px;
    .ant-btn{
       width:40%;
       margin-right:10px;
      }
    :deep(.ant-upload){
      width:100%;
      .ant-btn{
       width:100%;
      }
    }
    &>span{
      flex:1;
    }
  }
}
.full-parameter-content{
  .control-focus-header{
    color:#127fd2;
  }
  .control-focus-item{
    // background-color: rgba(139 190 231 / 70%);
    border:1px solid #67a5d4;
    width:100%;
  }
}
</style>

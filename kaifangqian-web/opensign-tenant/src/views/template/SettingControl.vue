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
	<div class="position-container"  :style="'height: '+ (height-63) +'px'">
	  <Loading :loading="loading" :absolute="false"/>
    <TemplateHeader :actions="actions" :previousStep="previousStep">
      <GeneralControls  :nowDocument="template" 
      	@dragOver="controlsDragOver" :controlTypeList="controlTypeList" :highControls="highControl" ref="controlsRef" />
    </TemplateHeader>
    <div class="document-layout">
      <div class="document-layout-left">
        <TemplateControls :controls="template.activeControl" @controlMousedown="controlMousedown" @searchPosition="searchPosition" @delControl="delControl"/>
      </div>
      <div class="document-layout-center">
        <TemplateContent @controlMousedown="controlMousedown" :nowDocument="template"
          @controlDelete="delControl" :showControl="true" ref="contentRef"/>
      </div>
      <div class="document-layout-right">
        <TemplateControlDrawer :element="template.nowElement" :doc="template" ref="controlDrawerRef"/>
      </div>
    </div>
	</div>
</template>

<script lang="ts">
  import {ref, createVNode, defineComponent, onMounted, reactive, toRefs,onUnmounted,onBeforeMount} from "vue";
  import { Loading } from '/@/components/Loading';
  import {useRouter} from "vue-router"
  import { message,Modal,Form } from 'ant-design-vue';
  import {htmlNodeFilter} from "/@/utils/ClickOther";
  import { CheckCircleOutlined } from '@ant-design/icons-vue';
  
  import {getTemplateImage,getTemplateInfo,templateSaveControl} from "./api";
  import {currentPosition} from "./components/dist/ControlerMoveRange";
  import {CanvasZoom,pageScaling} from './components/dist/ControlData';
  import {controlBuildParams,paramBuildControls} from './components/dist/ControlParamsBuild';
  
  import {backParent} from "/@/utils";
  import GeneralControls from './components/GeneralControls.vue' ;
  import TemplateHeader from './components/TemplateHeader.vue' ;
  import TemplateContent from './components/TemplateContent.vue' ;
  import TemplateControls from './components/TemplateControls.vue' ;
  import TemplateControlDrawer from './components/TemplateControlDrawer.vue' ;
  import { forEach } from "/@/utils/helper/treeHelper";
  import { getSystemLimit} from '/@/api/license';
  
  
  interface BtnFun {
   	 ():void   
  }
  
  interface ButtonItem {
    type:string;
    text:string;
    disabled?:boolean;
    callBack:BtnFun,
    show:boolean
  }
  
  export default defineComponent({
    name:"PositionParams",
    components:{
      Loading,message,TemplateHeader,GeneralControls,TemplateContent,TemplateControls,TemplateControlDrawer
    },
    setup(props,{emit}) {
      const router = useRouter();
      
      const compState = reactive({
        absolute: false,
        loading: false,
        tip:''
      })
      const controlsRef:any = ref({});
      const contentRef:any = ref({});
      const controlDrawerRef:any = ref();
      const highControl = ref(false);
      const actions = ref(<ButtonItem[]>[
        {
          type:'default',
          text:'关闭',
          callBack:handleColse,
          show:true
        },
        {
          type:'primary',
          text:'保存',
          callBack:handleSave,
          show:true
        }
      ])
      const template:any = ref({
        templateName:"参数设置",
        activeControl:[],
        pageSize:2,
        images:[],
        nowElement:{}
      }); 
      const templateId = ref<string>('');
      const annexId = ref<string>('');
      const previousStep = ref({
        title:"模板控件设置",
        icon:"ant-design:left-outlined",
        callBack:backPage
      })
      function backPage(){
        router.push({
          path: '/template/created',
          query:{
            __full__:"",
            templateId:templateId.value,
          }
        })
      }
      const controlTypeList = []; 
      
      function controlsDragOver(e,element:any){
        template.value.nowElement = element;
      }
      const controlRules = reactive({
        name: [{
            required: true,
            message: '请输入控件名称',
        }],
        interfaceParamName:[{
          required: true,
          message: '请输入接口参数名'
        }]
      })
      
      const useForm = Form.useForm;
      async function controlDrawerVerify(){
        // const {resetFields, validate, validateInfos} = useForm(template.value.activeControl, controlRules);
        var result = true;
        
        for(var i = 0;i <template.value.activeControl.length;i++ ){
          const element = template.value.activeControl[i];
          try{
            const {validate} = useForm(element, controlRules);
            const vr = await validate();
          }catch(err){
            searchPosition(element,false);
            result = false;
            break;
          }
        }
         
        return result;
      }
      
      async function handleSave(){
        compState.loading = true;
        if(!template.value.activeControl || template.value.activeControl.length==0){
          message.warning("请先设置控件后再进行保存")
          cleanLoading(300);
          return;
        }
        if(!await controlDrawerVerify()){
          message.warning("请设置模板控件的必要参数");
          cleanLoading(300);
          return;
        }
        try{
          
          
          const result = await templateSaveControl({templateId:templateId.value,controlVoList:controlBuildParams(template.value.activeControl,template.value.targets,template.value.maxWidth,true)});
          if(result.code == 200){
             compState.loading = false;
             Modal.confirm({
              icon: createVNode(CheckCircleOutlined,{ style: 'color:#52c41a;' }),
              content: createVNode('div', { style: 'color:#52c41a;' }, '保存成功'),
              okText:"关闭此页",
              cancelText:"取消",
              centered:true,
              onOk() {
                handleColse();
              }
            });
          }else{
            message.warning(result.message);
            cleanLoading(300);
          }
        }catch(err){
          console.log(err);
          cleanLoading(300);
        }
        
      }
      function handleColse(){
        // window.opener.location.reload();
        // backParent("/template/manage",router,null);
        router.push("/template/manage");
      }
       
      function cleanLoading(time?:number){
        setTimeout(()=>{
          compState.loading = false; 
        },time?time:200)
      }
      function startLoading(timeOut?:number){
        compState.loading = true; 
        setTimeout(()=>{
          compState.loading = false; 
        },timeOut?timeOut:200)
      }
      function searchPosition(element:any,load=true){
        if(load){
          startLoading(300);
        }
        // compState.loading = load;
        const moveY = element.position.top - 100;
        // controlDrawerRef.value.verifyFrom();
        setTimeout(()=>{
          document.getElementsByClassName('document-content')[0].scrollTo(0,moveY);
          controlMousedown(element);
          // compState.loading = load;
        },600)
      }
      function delControl(element:any){
        element.controlClick = false;
        template.value.activeControl = template.value.activeControl.filter((item:any) => {
        	return item.uid !== element.uid 
        })
      }
      
      function controlMousedown(element:any){
      	template.value.activeControl.forEach((item:any)=>{
      		item.controlClick = element.uid == item.uid;
      	})
        template.value.nowElement = element;
        
        //清空表单验证
        controlDrawerRef.value?controlDrawerRef.value.verifyFrom():"";
      }
      
      
      
      //header文档切换
      function handleDocChange(){
        
      }
      //
      function initWindow() {
          var h = document.documentElement.clientHeight || document.body.clientHeight;
      	return h;
      }
      const height = ref(initWindow());
      
      onBeforeMount(async()=>{
        console.log("onBeforeMount template");
        const  systemLimit = await getSystemLimit();
        highControl.value = !!!(systemLimit.templateScope == 'base');
        console.log("new highControl:",highControl.value);
      })
      async function initData(){
        const query:any = router.currentRoute.value.query;
        if(!query.templateId || !query.annexId){
          message.warning("请求参数错误")
          return false;
        }
        annexId.value = query.annexId;
        templateId.value = query.templateId;
        
        var templateImg = await getTemplateImage({annexId:annexId.value});
        var result:any = await getTemplateInfo({templateId:templateId.value});
        
        //排序
        templateImg = templateImg.sort((a, b) => a.page - b.page);
        const {targets,maxWidth} = pageScaling(templateImg);
        
        template.value.targets = targets;
        template.value.images = templateImg; //.sort((a, b) => a.page - b.page);
        template.value.pageSize = templateImg.length;
        template.value.imageAllHeight = targets[targets.length-1].transformHeight + targets[targets.length-1].height + (templateImg.length * CanvasZoom.space);
        template.value.maxWidth = maxWidth;
        template.value.activeControl = paramBuildControls(result.templateControlVoList,template.value.targets,template.value.maxWidth);
        
        console.log("template.value:",template.value);
        
        const  systemLimit = await getSystemLimit();
        highControl.value = !!!(systemLimit.templateScope == 'base');
        controlsRef.value.loadData();
      }
      
      
      // function getTemplateInfo(data:any){
      //   return{
      //     imageVoList:[{img:docImg1,docPage:0},{img:docImg2,docPage:1}],
      //   }
      // }
      
      //清除控件选中事件
      function clearControlClick(e:any){
      	let flag =  htmlNodeFilter(e.target,"this-click");
        console.log("click class fliter：",flag);
      	if(flag){
      		return;
      	}else{
      		template.value.activeControl.forEach((item:any)=>{
      			item.controlClick = false
      		})
      	}
      	e.preventDefault();
      }
      const app:any = ref(document.getElementById("app-tenant"));
      app.value.addEventListener('click',clearControlClick);
      onUnmounted(()=>{
      	//页面销毁时解除全局click的监听
      	app.value.removeEventListener('click', clearControlClick)
      })
      
      onMounted(() => {
        initData();
      });
      return {
        ...toRefs(compState),height,actions,template,templateId,controlTypeList,controlsDragOver,controlMousedown,delControl,
        searchPosition,contentRef,controlDrawerRef,previousStep,highControl,controlsRef
      }
    }
  });
</script>

<style lang="less" scoped>
	 @import url("./data/DocumentDetail.css");
</style>
 

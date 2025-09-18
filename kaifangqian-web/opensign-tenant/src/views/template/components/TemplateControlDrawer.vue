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
	<Drawer
	    v-model:visible="element.controlClick"
	    class="custom-control-drawer"
	    :title="element.title+'属性'"
	    placement="right" 
		:closable="false"
		width="300px"
		:mask="false">
		<template #extra>
			<div class="drawer-close" @click="element.controlClick = false">
				<Icon icon="ant-design:close-outlined"></Icon>
			</div>
		</template>
	    <div>
			<Form :model="element" :labelWrap="true" layout="vertical" class="custom-control-from" ref="controlFromRef">
				<FormItem :rules="[{ required: true,message: '请输入控件名称'}]"
					name="name" label="控件名称" v-if="attrFilter('name')">
				    <Input v-model:value="element.name" class="template-control-attr" :disabled="element.disabled"/>
				</FormItem>
				<!-- <FormItem name="user" label="参与方设置" v-if="attrFilter('signer')">
				    <Select v-model:value="element.user.signerId" @change="signerChange"  style="width: 200px"> 
						<SelectOption :value="-1" >未设置</SelectOption>
						<SelectOption :value="item.uid" v-for="item in signerListDrawer">{{item.signerName}}</SelectOption>
					</Select>
				</FormItem> -->
        <FormItem name="interfaceParamName" v-if="attrFilter('interfaceParamName')"
        :rules="[{ required: true,message: '请输入接口参数名'}]">
          <template #label>
            <span style="margin-right: 5px;">接口参数</span>
            <a-tooltip>
              <template #title>供API接口参数填写使用</template>
              <Icon icon="ant-design:exclamation-circle-outlined" color="#999"></Icon>
            </a-tooltip>
          </template>
            <Input v-model:value="element.interfaceParamName" placeholder="请输入接口参数名"  class="template-control-attr" :disabled="element.disabled"/>
        </FormItem>
				<FormItem name="placeholder" label="文本提示" v-if="attrFilter('placeholder')">
				    <Input v-model:value="element.placeholder" placeholder="请输入文本提示"  class="template-control-attr" :disabled="element.disabled"/>
				</FormItem>
				<FormItem name="value" v-if="attrFilter('value')">
          <template #label>
            <span style="margin-right: 5px;">调试</span>
            <a-tooltip>
              <template #title>便于调整合适的控件位置及字体大小</template>
              <Icon icon="ant-design:exclamation-circle-outlined" color="#999"></Icon>
            </a-tooltip>
          </template>
          
				    <Input v-if="element.type == ControlType.LineText" v-model:value="element.value" placeholder="请输入内容" class="template-control-attr" :disabled="element.disabled"/>
					<a-textarea v-if="element.type == ControlType.MultilineText" 
						v-model:value="element.value" placeholder="请输入内容" 
						:autosize="{ minRows: 2, maxRows: 6 }" class="template-control-attr" :disabled="element.disabled"/>
					<InputNumber v-if="element.type == ControlType.Number"  v-model:value="element.value" class="template-control-attr"
						:placeholder="element.placeholder" :controls="false" :stringMode="true" :disabled="element.disabled"/>
					<DatePicker v-if="element.type == ControlType.Date"  v-model:value="element.value"
					 :placeholder="element.format" :format="element.format" :value-format="element.format" class="template-control-attr" :disabled="element.disabled"/>
				</FormItem> 
        
        <FormItem label="选项设置" v-if="attrFilter('select')">
          <template v-if="element.widgetList && element.widgetList.length>0">
           <Widget :element="element" @delControl="delControl"/>
          </template>
          <AddWidget :element="element"></AddWidget>
        </FormItem>
        <FormItem v-if="attrFilter('radio')">
          <template #label>
            <span style="margin-right: 5px;">选项设置</span>
            <a-tooltip>
              <template #title>选项内容不会填充在文档上，仅用于区分选项</template>
              <Icon icon="ant-design:exclamation-circle-outlined" color="#999"></Icon>
            </a-tooltip>
          </template>
          <template v-if="element.widgetList && element.widgetList.length>0">
           <Widget :element="element" @delControl="delControl"/>
          </template>
          <AddWidget :element="element" :doc="doc"></AddWidget>
        </FormItem>
        <FormItem  v-if="attrFilter('checkBox')">
          <template #label>
            <span style="margin-right: 5px;">选项设置</span>
            <a-tooltip>
              <template #title>选项内容不会填充在文档上，仅用于区分选项</template>
              <Icon icon="ant-design:exclamation-circle-outlined" color="#999"></Icon>
            </a-tooltip>
          </template>
          <template v-if="element.widgetList && element.widgetList.length>0">
            <Widget :element="element" @delControl="delControl"/>
          </template>
          <AddWidget :element="element" :doc="doc"></AddWidget>
        </FormItem>
        
        <!-- <FormItem label="选项设置" v-if="attrFilter('size')">
          <span>size</span>
        </FormItem> -->
        
        
				<FormItem name="user" label="日期格式" v-if="attrFilter('format')">
				    <Select v-model:value="element.format"  v-if="element.type == ControlType.SignDate"  class="template-control-attr" :disabled="element.disabled">
						<SelectOption value="yyyy年MM月dd日">YYYY年MM月DD日</SelectOption>
						<SelectOption value="yyyy-MM-dd">YYYY-MM-DD</SelectOption>
						<SelectOption value="yyyy/MM/dd">YYYY/MM/DD</SelectOption>
					</Select>
					<Select v-model:value="element.format"  v-if="element.type == ControlType.Date" class="template-control-attr" :disabled="element.disabled">
						<SelectOption value="YYYY年MM月DD日">YYYY年MM月DD日</SelectOption>
						<SelectOption value="YYYY-MM-DD">YYYY-MM-DD</SelectOption>
						<SelectOption value="YYYY/MM/DD">YYYY/MM/DD</SelectOption>
					</Select>
				</FormItem>
				<FormItem name="user" label="字体设置" v-if="attrFilter('fontFamily')">
				    <Select v-model:value="element.style.fontFamily"  class="template-control-attr" :disabled="element.disabled">
              <SelectOption value="宋体" style="font-family: 宋体;">宋体</SelectOption>
              <SelectOption value="黑体" style="font-family: 黑体;">黑体</SelectOption>
              <SelectOption value="楷体" style="font-family: 楷体;">楷体</SelectOption>
              <SelectOption value="仿宋" style="font-family: 仿宋;">仿宋</SelectOption>
              <!-- <SelectOption value="Arial" style="font-family: Arial;">Arial</SelectOption> -->
            </Select>
				</FormItem> 
				<FormItem name="fontSize" label="字号" v-if="attrFilter('fontSize')">
				    <Select v-model:value="element.style.fontSize" :options="selectOptions" class="template-control-attr"/>
				</FormItem>
				
				<FormItem name="align" label="对齐方式" v-if="attrFilter('textAlign')" >
				    <a-radio-group v-model:value="element.style.textAlign"   class="template-control-attr" :disabled="element.disabled">
						<a-radio-button value="left">居左</a-radio-button>
						<a-radio-button value="center">居中</a-radio-button>
						<a-radio-button value="right">居右</a-radio-button>
					</a-radio-group>
				</FormItem>
				<FormItem name="align" label="是否必填" v-if="attrFilter('required')">
				    <Checkbox v-model:checked="element.isRequired" :disabled="element.disabled">必填</Checkbox>
				</FormItem>
			</Form>
		</div>
	  </Drawer>
</template>

<script lang="ts">
	import {defineComponent,ref,watch} from 'vue';
	import {Input,InputNumber,Drawer,Form,FormItem,Radio,
	Select,RadioGroup,SelectOption,DatePicker,Checkbox} from 'ant-design-vue';
	/* import {SvgIcon,Icon} from "@/components/Icon"; */
  import Icon from '/@/components/Icon';
	import type { SelectProps } from 'ant-design-vue';
	import {ControlFontSizeList,ControlType} from "./dist/ControlData";
	// import {SignUserType} from "./enum"
  import Widget from "./widget/index.vue"
  import AddWidget from "./widget/AddWidget.vue"
	import {deepClone} from "/@/utils"
	export default defineComponent({
		name: 'ControlDrawer',
		components: {
			Input ,
			InputNumber,Drawer,Icon,
			Form,FormItem,Select,SelectOption,
			RadioGroup,DatePicker,Checkbox,
      Widget,Radio,AddWidget
		},
		props:{
			element:{
				type: Object
			},
      doc:{
      	type: Object,
        default:{}
      },
			// signerList:{
			// 	type:Object
			// },
			// user:{
			// 	type:Object,
			// 	default:{
			// 		signerType:null
			// 	}
			// }
		},
		emits:["controlMousedown","controlDelete"],
		setup(props:any,{emit}) {
			//element.controlClick
			watch(() => props.element.controlClick,
			(newValue,oldValue) => {
				if(newValue){
          /* controlFromRef.value.clearValidate(); */
					buildSelectSigner();
				}
			})
			
			const visible = ref(true);
			const selectOptions = ref<SelectProps['options']>(ControlFontSizeList);
			const controlFromRef = ref();
      
      function delControl(){
        emit("controlDelete",props.element)
      }
      
			function attrFilter(attr:string){
				if(props.element.attr){
					return props.element.attr.includes(attr)
				}else{
					return false;
				}
			}
      async function clearFromValidate(){
        try {
          const values = await controlFromRef.value.clearValidate();
        }catch(e){
        }
      }
      async function verifyFrom(){
        if(controlFromRef.value){
          try {
            await controlFromRef.value.clearValidate();
            const values = await controlFromRef.value.validateFields();
            //console.error("values:",values);
            return true;
          }catch(e){
            console.error("aaaaa",e);
          }
        }
        return false;
      }
			function signerChange(val:any){
				// if(val == -1){
				// 	props.element.user.index = -1;
				// }else{
				// 	const user = getSignerIndex(val);
				// 	props.element.user.index = user.index;
				// 	props.element.user.tenantId = user.tenantId;
				// 	props.element.user.signerName = user.signerName;
				// }
			}
			function getSignerIndex(signerId:string){
				return props.signerList.find((item:any)=>item.signerId == signerId);
			}
			const signerListDrawer = ref<any[]>([]);
			function buildSelectSigner(){
				//console.log(props.user);
				
				// if(props.element.type == ControlType.Seal){
				// 	//签章控件不能设置为个人
				// 	const newSigners = props.signerList.filter((item:any)=>item.signerType == SignUserType.enterprise);
				// 	signerListDrawer.value = deepClone(newSigners);
				// }else if(props.element.type == ControlType.Signature){
				// 	//签名控件不能设置为企业
				// 	const newSigners = props.signerList.filter((item:any)=>item.signerType == SignUserType.personal);
				// 	signerListDrawer.value = deepClone(newSigners);
				// }
			}
			return{
				visible,signerChange,verifyFrom,
				selectOptions,signerListDrawer,clearFromValidate,
				attrFilter,ControlType,controlFromRef,delControl
			}
		}
	})
	
</script>
<style lang="less" scoped>
	.drawer-close{
		margin-right: 20px;
		cursor: pointer;
		opacity: 0.5;
	}
	.drawer-close:hover{
		opacity: 1;
	}
  
  .resrun-widget{
    display: flex;
    height: 40px;
    .item-order,.item-del{
      width: 30px;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items:center;
    }
    .item-name{
      flex: 1;
      padding: 5px;
      box-sizing: content-box;
    }
  }
  .resrun-widget:hover,.resrun-widget:active{
      background-color: rgba(0, 0, 0,0.06) !important;
  }
  .control-prop-tips{
    font-size: 12px;
    color:#aaa;
  }
  
</style>
<style lang="less">
	.custom-control-drawer{
		height: calc(100% - 64px);
		bottom: 0;
		top:inherit;
	}
	.custom-control-drawer{
		.ant-drawer-header{
			padding: 9px 0 9px 20px !important;
		}
		.ant-form-item{
			margin-bottom: 5px !important;
		}
		.ant-form-item-label{
			padding: 0;
		}
		.ant-drawer-body{
			padding: 20px;
		}
	} 
  .template-control-attr{
    width: 256px !important;
  }
	
</style>

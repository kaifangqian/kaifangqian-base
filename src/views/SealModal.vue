<template>
	<Modal v-model:visible="modalOpen" title="创建印章"  width="800px">
		<Form  :model="sealFrom" name="basic"
		  aut="off" ref="selaFromRef" >
			<div style="width: 442px;">
				<FormItem label="企业名称" name="entpName"
				    :rules="[{ required: true, message: '请输入企业名称' }]" >
				  <Input v-model:value="sealFrom.entpName"  size="middle" placeholder="请输入企业名称/生成用于签署的测试数字证书" class="input-width" />
				</FormItem>
			</div>
			<Row style="width: 100%;padding-top: 0px;">
			  <Col :span="12">
				  <RadioGroup v-model:value="sealFrom.createType" @change="createTypeChange">
				      <RadioButton :value="1">模板制作</RadioButton>
				      <RadioButton :value="2">印模生成</RadioButton>
				  </RadioGroup>
			  </Col>
			</Row>
			<div class="seal-make-layout" >
			  <div class="template-make" v-if="sealFrom.createType == 1">
			      <div class="temlate-item temlate-style">
			        <div class="title">
			          <span>选择印章样式</span>
			        </div>
			        <div class="seal-style">
			          <ul>
			            <li :class="sealFrom.sealStyle == 1?'active':''">
			              <div class="seal-img">
			                <img :src="templateGz" width="100%"/>
			              </div>
			              <div class="seal-name">公章</div>
			            </li>
			          </ul>
			        </div>
			      </div>
			      <div class="temlate-item">
			        <div class="title">
			          <span>印章文字设置</span>
			        </div>
			        <div class="seal-font-seting" style="padding-top:10px;">
			          <Form  :model="sealFrom" name="basic" autocomplete="off" layout="horizontal" labelAlign="left"
					  :labelCol=" { style: { width: '80px' } }">
			            <FormItem label="环绕文字" :rules="[{ required: true, message: '请输入环绕文字' }]">
			              <Input v-model:value="sealFrom.entpName"  class="input-width" />
			            </FormItem>
			            <FormItem label="横排文字" >
			              <Input v-model:value="sealFrom.middleText" class="input-width" />
			            </FormItem>
			          </Form>
			        </div>
			      </div>
			  </div>
			  <div class="seal-upload" v-if="sealFrom.createType == 2">
			  <Upload  v-if="fileList.length == 0"
			      v-model:file-list="fileList"
			      name="file"
			      :show-upload-list="false"
			      @change="handleChange">
			      <div class="local-upload pointer">
			        <div style="width:100%">点击上传</div>
			      </div>
				   <!-- :headers="uploadHeaders" -->
			    </Upload>
			   <div class="upload-img" v-else>
			     <img :src="uploadSeal" ref="sealRef" @load="imgeLoad"  style="height:100%;display: none;"/>
			   </div>
			   <div class="SealActions" v-if="fileList.length > 0">
				 <Row>
			      <Col :span="4" class="center">
			        <UndoOutlined @click="sealActions(-1)" class="pointer"/>
			      </Col>
			      <Col :span="4" class="center">
			        <RedoOutlined @click="sealActions(1)" class="pointer"/>
			      </Col>
			      <Col :span="4" class="center">
			        <MinusOutlined @click="sealActions(-2)" class="pointer"/>
			      </Col>
			      <Col :span="4" class="center">
			        <PlusOutlined @click="sealActions(2)" class="pointer"/>
			      </Col>
			      <Col :span="5">
			       <Upload 
			          v-model:file-list="fileList"
			          name="file"
			          :show-upload-list="false"
			          @change="handleChange2">
			            <Button type="link">重新上传</Button>
			        </Upload>
			      </Col>
			     </Row>
			    <Row>
			      <Col :span="8">
			       <h4 style="line-height: 32px;">旋转角度：</h4>
			      </Col>
			       <Col :span="16">
			        <Slider @afterChange="sealRotateChange" v-model:value="sealOptions.sealRotate" :min="-180" :max="180" />
			       </Col>
			     </Row>
			     <Row>
			       <Col :span="8">
			        <h4 style="line-height: 32px;">背景透明度：</h4>
			       </Col>
			        <Col :span="16">
			         <Slider @afterChange="buildCropperImage" v-model:value="sealOptions.sealBackground" :min="0" :max="100" />
			        </Col>
			      </Row>
			     <!-- <a-input-number v-model:value="sealOptions.sealRotate" :min="-360" :max="360" /> -->
			   </div>
			  </div> 
			  
			  <div class="seal-preview">
			    <div class="title">签章预览</div>
			    <div class="seal-preview-img">
			      <img :src="'data:image/png;base64,'+sealFrom.sealPreview" style="width:100%;height:100%" alt="暂未生成签章" v-if="sealFrom.sealPreview"/>
				  <span style="color: #aaa;" v-else>暂未生成签章</span>
			    </div>
			  </div>
			</div>
		</Form>
		<template #footer>
			<div style="text-align: center;">
				<Button key="back" @click="closeModal">取消</Button>
				<Button key="submit" type="primary"  @click="sealPreviewInfo" :loading="sealPreviewLoading">预览</Button>
				<Button key="submit" type="primary"  @click="handleOk" v-if="sealFrom.sealPreview">使用</Button>
			</div>
		</template>
			  
	</Modal>
</template>

<script>
	import { defineComponent,ref,unref,computed,reactive,onMounted,watch } from 'vue';
	import { Modal,Form,FormItem,Input,Row,Col,RadioGroup,RadioButton,Button,Upload,Slider,message} from 'ant-design-vue';
	
	import { UndoOutlined,RedoOutlined,MinusOutlined,PlusOutlined  } from '@ant-design/icons-vue';
	
	import {doGet,doPost} from "@/utils/request"
	import 'cropperjs/dist/cropper.css';
	import Cropper from 'cropperjs';
	import {templateGz} from "./data/image";
	
	function getBase64(file) {
	  return new Promise((resolve, reject) => {
	    const reader = new FileReader();
	    reader.readAsDataURL(file);
	    reader.onload = () => resolve(reader.result);
	    reader.onerror = error => reject(error);
	  });
	}
	
	export default defineComponent({
		name: 'SealModal',
		components:{
			Modal,Form,FormItem,Input,Row,Col,RadioGroup,RadioButton,Button,Upload,Slider,
			UndoOutlined,RedoOutlined,MinusOutlined,PlusOutlined,message
		},
		props:{
			modalVisible:{
				type:Boolean,
				default:false
			}
		},
		emits:["success"],
		setup(props,{emit}) {
			const modalOpen = ref(false);
			const sealPreviewLoading = ref(false);
			
			//监听父页面是否需要打开弹窗
			watch(() => props.modalVisible,
			(newValue,oldValue) => {
				modalOpen.value = true;
				console.log("有变化",newValue);
			})
			
			//印章表单属性
			const sealFrom = ref({
			  createType:1,
			  sealStyle:1,
			  entpName:"",
			  middleText:"测试印章",
			  sealPreview:false,
			});
			
			const selaFromRef = ref();
			const firstUpload = ref(true);
			const fileList = ref([]);
			const sealCropper = ref();
			const sealRef = ref(null);
			const uploadSeal = ref(null);
			const sealOptions = ref({
			  sealRotate:0,
			  sealBackground:50,
			  imageWidth:400,
			  imageHeight:400,
			})
			
			//印章制作完成确定按钮提交
			async function handleOk(){
				try{
					const values = await selaFromRef.value.validateFields();
					const data = {sealImage:sealFrom.value.sealPreview,entpName:sealFrom.value.entpName}
					emit('success',data);
					modalOpen.value  = false;
				}catch (errorInfo) {
				  console.log('Failed:', errorInfo);
				  message.warning("企业名称不能为空，请输入企业名称")
				}
			}
			
			//关闭弹窗
			function closeModal(){
				modalOpen.value  = false;
			}
			
			//印章生成方式切换
			function createTypeChange(value){
				sealFrom.value.sealPreview = false;
			}
			
			/**
			 * 签章预览
			 */
			async function sealPreviewInfo(){
				// sealFrom.value.sealPreview = true;
				sealPreviewLoading.value = true;
				if(sealFrom.value.createType == 1){
					try{
						const values = await selaFromRef.value.validateFields();
						const response = await doPost("/generate/seal",{middleText:sealFrom.value.middleText,topText:sealFrom.value.entpName});
						sealFrom.value.sealPreview = response.result.entSeal;
					}catch (errorInfo) {
					  console.log('Failed:', errorInfo);
					  message.warning("企业名称和横排文字不能为空")
					}
					
				}else{
					if(fileList.value.length == 0){
						message.warning("请先上传印模在进行预览");
					}else{
						await  buildCropperImage();
					}
				}
				sealPreviewLoading.value = false;
			}
			
			//首次上传印模图片
			function handleChange(info){
				getBase64(info.file.originFileObj).then(res=>{
					uploadSeal.value = res;
				})
			}
			
			//重新上传印模图片
			async function handleChange2 (info){
				firstUpload.value = false;
				await handleChange(info)
			}
			
			//印章图片旋转
			function sealActions(type){
			  switch(type){
			    case -1:
			      sealCropper.value.rotate(-90);
			      break;
			    case 1:
			      sealCropper.value.rotate(90);
			      break;
			    case -2:
			      sealCropper.value.zoom(-0.1);
			      break;
			    case 2:
			      sealCropper.value.zoom(0.1);
			      break;
			  }
			  setTimeout(function (){
			    buildCropperImage();
			  },100)
			}
			
			//修改印章的背景透明度
			function sealRotateChange(to){
			  sealCropper.value.rotateTo(to);
			  setTimeout(function (){
			    buildCropperImage();
			  },100)
			}
			
			//将上传的图片加载到 图片处理工具中
			function sealPreviewCropper(){
			  sealCropper.value = new Cropper(sealRef.value,{
			    viewMode: 1,
			    dragMode: 'move',
			    preview :".before",
			    initialAspectRatio: 1,
			    aspectRatio: 1,
			    background: true,
			    autoCrop:true,
			    autoCropArea: 0.7,
			    zoomOnWheel: true,
			    zoomOnTouch:true,
			    cropBoxResizable:false,
			    cropBoxMovable: false,
			    wheelZoomRatio:0.05,
			    cropend:sealCropend,
			  });
			}
			
			//图片处理完成后调用后端服务进行处理
			function sealCropend(end){
				buildCropperImage();
			}
			
			//印模图片加载完成事件
			function imgeLoad(img){
			  console.log("load",img);
			  if(!firstUpload.value){
			    sealCropper.value.destroy();
			  }
			  sealPreviewCropper();
			}
			
			//图片缩放、旋转、背景透明度修改后调用次方法重新生成
			async function buildCropperImage(){
			  if(!sealCropper.value){
			    return;
			  }else{
			  }
			  
			  // sealCropper.value.rotateTo(sealOptions.value.sealRotate);
			  const seal = sealCropper.value.getCroppedCanvas({
			    width: sealOptions.value.imageWidth,
			    height: sealOptions.value.imageHeight,
			    imageSmoothingQuality: 'high'
			  }).toDataURL('image/jpeg');
			  
			  const data =  {
			    "image": seal.split(",")[1],
			    "colorRange":sealOptions.value.sealBackground + 120,
			  };
			  //TODO  获取处理的印章
			  //const result = await templateGenerateSeal(data);
			  const response = await doPost("/clip/seal",data);
			  
			  sealFrom.value.sealPreview = response.result.entSeal;
			}
			
			return{
				handleOk,modalOpen,sealFrom,selaFromRef,createTypeChange,templateGz,sealPreviewInfo,fileList,imgeLoad,closeModal,
				handleChange,handleChange2,sealActions,sealRotateChange,sealRef,sealOptions,uploadSeal,buildCropperImage,sealPreviewLoading
				
			}
		}
	})
 
	
</script>

<style lang="less">
	.seal-make-layout{
	  display: flex;
	  .template-make{
	  }
	  .seal-preview{
	    padding-left: 40px;
	    .title{
	      font-size: 14px;
	      font-weight: 600;
	      height: 30px;
	    }
	  }
	   
	  .seal-upload{
	    padding:20px 0;
		width: 400px;
	  }
	  .seal-preview-img{
	    width:160px;
		height: 160px;
	    padding: 10px;
	    border: 1px solid #ededed;
	  }
	}
	
	.template-make{
	  width:400px;
	  border: 1px solid #ededed;
	  margin-top: 10px;
	  padding: 5px 20px;
	  .temlate-item .title {
	    font-weight: 600;
	  }
	  .seal-style{
	    //height:100px;
	    padding-top: 15px;
	    ul {
	      display:flex;
	      padding:0;
	      margin: 0;
	    }
	    ul li{
	      user-select: none;
	      cursor: pointer;
	      .seal-img{
	        padding:6px;
	        width: 100px;
	        height: 100px;
	        border: 1px solid #ededed;
	        border-radius: 4px;
	      }
	      .signature-style{
	        display: flex;
	        width:116px;
	        height: 116px;
	        padding: 8px 6px;
	        justify-items: center;
	        align-items: center;
	        border: 1px solid;
	        border-color: inherit;
	        border-radius: 4px;
	      }
	      .signature-style img{
	      	user-select: none;
	      }
	      .seal-name{
	        text-align: center;
	        line-height: 30px;
	        color:#9e9e9e;
	      }
	    }
	    ul li.active{
	      .seal-img{
	        border-color: #1890ff;
	      }
	      .seal-name{
	        color: #1890ff;
	      }
	    }
	    ul li:nth-child(n + 2){
	      margin-left: 10px;
	    }
	  }
	}
	.local-upload{
	  width:160px;
	  height:160px;
	  padding: 10px;
	  border: 1px dashed rgba(0,0,0,0.3);
	  display: flex;
	  align-items: center;
	  justify-items: center;
	  text-align: center;
	}
	.upload-img{
	  width: 300px;
	  height: 300px;
	}
	.SealActions{
	  width:300px;
	  padding: 8px 15px;
	  background-color: rgba(0,0,0,0.07);
	}
	.center{
	  text-align: center;
	  line-height: 32px;
	  
	}
	.pointer{
	  cursor: pointer;
	}
</style>

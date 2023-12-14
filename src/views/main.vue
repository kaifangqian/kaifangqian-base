<script>
	import { defineComponent,ref,unref,computed,reactive,onMounted,createVNode,h } from 'vue';
	import { Button,Input,Space,Divider,RadioGroup,RadioButton,Tooltip,Form,FormItem,message,Spin,Modal} from 'ant-design-vue';
	import { QuestionCircleOutlined,CheckCircleOutlined ,CloseCircleOutlined } from '@ant-design/icons-vue';
	import draggable from 'vuedraggable'
	
	import {getWindowHeight,base64ToBlob,saveBlob} from "@/utils"
	import {doPost} from "@/utils/request"
	
	// import LayoutHeader from "@/layouts/header/index.vue"
	import flexible from "@/utils/flexible.js"
	import {CanvasZoom,controlList,ControlType} from '@/components/control/data/ControlData'
	import {moveRange,currentPosition} from '@/components/control/data/ControlerMoveRange'
	import ControlItem from '@/components/control/ControlItem.vue';
	
	import SealModal from './SealModal.vue';
	import SignatureModal from './SignatureModal.vue';
	
	
	import image1 from "@/assets/images/doc_1.png"
	import image2 from "@/assets/images/doc_2.png"
	
	
	import "@/assets/style/main.less"
	
	export default defineComponent({
		name: 'mian',
		components:{
			Button,Input,Divider,RadioGroup,RadioButton,Space,Tooltip,Form,FormItem,Modal,
			QuestionCircleOutlined,draggable,ControlItem,SealModal,SignatureModal,Spin
		},
		setup() {
			//copy 控件防止修改了初始化控件的属性
			const thisControlList = ref(JSON.parse(JSON.stringify(controlList)));
			
			const keywordFromRef = ref();
			
			const spinning = ref(false);
			//签署相关的属性
			const signData = ref({
				signType:1,
				entKeyword:"",
				personalKeyword:"",
				entName:"",
				personalName:"",
				entSeal:"",
				personalSeal:"",
				entPositionList:[],
				personalPositionList:[]
				
			});
			//文档展示的数据  包含控件列表
			const documentPDF = ref({
				images:[
					{
						docPage:0,
						image:image1,
					},
					{
						docPage:1,
						image:image2,
					},
				],
				control:[]
			});
			//控件移出移入的配置
			const groupOut = ref({
				name: "itxst",
				put: false,
				pull:'clone', //允许拖出
			})
			const groupEnter = ref({
				name: "itxst",
				put: true, //允许拖入
				pull:false, //允许拖出
			})
			//深度克隆控件对象
			function clone(origin){
				const data = JSON.parse(JSON.stringify(origin))
				data.position.x=100;
				data.position.y=100;
				data.uid = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
				data.pageSize = documentPDF.value.images.length;
				return data;
			}
			//控件完成拖动触发事件
			function controlsDragOver(e){
				const moveTarget  = documentPDF.value.control[e.newIndex];
				if(e.pullMode){
					const opt = {
						x:e.originalEvent.offsetX - (moveTarget.size.width/2),
						y:e.originalEvent.offsetY - (moveTarget.size.height/2),
						pageSize:3,
						size:moveTarget.size,
						currentPage:0,
					};
					moveRange(opt);
					moveTarget.position.left = opt.x;
					moveTarget.position.top = opt.y
					moveTarget.position.page = opt.currentPage;
					moveTarget.controlClick = true;
				}
			}
			
			//制作印章弹窗的开关
			const sealModalShow = ref(false);
			
			//设置签名的弹窗开关
			const signatureModalShow = ref(false);
			
			//删除已拖动至文档中的控件
			function controlDelete(element){
				documentPDF.value.control = documentPDF.value.control.filter((item) => {
					return item.uid !== element.uid 
				})
			}
			/**
			 * 接受签章图片
			 */
			function sealModalSubmit(data){
				var temControlList = JSON.parse(JSON.stringify(thisControlList.value))
				temControlList[0].value = data.sealImage;
				temControlList[0].user.userName = data.entpName;
				signData.value.entName = data.entpName;
				signData.value.entSeal = data.sealImage;
				thisControlList.value = temControlList;
				//替换控件中已经使用的签章
				documentPDF.value.control.forEach(item=>{
					if(item.type == ControlType.Seal){
						item.value = data.sealImage;
					}
				})
			}
			/**
			 * 接收手写签名图片
			 */
			function signatureModalSubmit(data){
				var temControlList = JSON.parse(JSON.stringify(thisControlList.value))
				temControlList[1].value = data.image;
				temControlList[1].user.userName = data.userName;
				signData.value.personalName = data.userName;
				signData.value.personalSeal = data.image;
				thisControlList.value = temControlList;
				//替换控件中已经使用的手写签名
				documentPDF.value.control.forEach(item=>{
					if(item.type == ControlType.Signature){
						item.value = data.image;
					}
				})
			}
			
			/**
			 * 判断控件的class  是否可进行拖动
			 */
			function controlClass(element){
				if(element.type == ControlType.Seal && signData.value.signType == 1 && thisControlList.value[0].value){
					return "control-move";
				}else if(element.type == ControlType.Signature && signData.value.signType == 1 && thisControlList.value[1].value){
					return  "control-move";
				}else{
					return "control-disabled";
				}
				//return  "control-move";
			}
			
			/**
			 * 签署类型切换
			 */
			function signTypeChange(val){
				if(signData.value.signType == 2){
					documentPDF.value.control = [];
				}
			}
			/**
			 * 调用文件签署接口进行签署  并返回签署后的PDF文件
			 */
			async function signDocument(){
				// signSuccessTips();
				if(signData.value.signType == 1 && documentPDF.value.control.length == 0){
					message.warning("您尚未为电子印章/手写签名指定签署位置，请设置后再签署");
					return;
				}
				// console.log(signData.value.signType == 2 && (!signData.value.entKeyword  || !signData.value.personalKeyword));
				//判断如果是关键字签署类型  必须设置一个关键字
				if(signData.value.signType == 2 && !(signData.value.entKeyword  || signData.value.personalKeyword)){
					message.warning("您尚未设置关键字，请准确设置关键字后再签署");
					return;
				}
				//如果设置了电子印章关键字 必须设置印章才能提交
				if(signData.value.signType == 2 && signData.value.entKeyword && !signData.value.entSeal ){
					message.warning("尚未制作电子印章，请先制作电子印章后再签署");
					return;
				}
				//如果设置了手写前面关键字 必须设置签名才能提交
				if(signData.value.signType == 2 && signData.value.personalKeyword && !signData.value.personalSeal ){
					message.warning("尚未设置手写签名，请先设置手写签名后再签署");
					return;
				}
				spinning.value = true;
				signData.value.entPositionList = [];
				signData.value.personalPositionList = [];
				//将签名控件的位置转换为签署所需的坐标
				documentPDF.value.control.forEach(item=>{
					const temItem = {
						width: item.size.width,
						height:item.size.height,
						offsetX: item.position.left,
						offsetY: currentPosition(item.position.top,item.position.page),
						page: item.position.page + 1,
						pageHeight: CanvasZoom.height,
						pageWidth: CanvasZoom.width,
					};
					if(item.type == ControlType.Seal){
						signData.value.entPositionList.push(temItem)
					}else if(item.type == ControlType.Signature){
						signData.value.personalPositionList.push(temItem)
					}
				})
				//调用签署接口
				doPost("/sign",signData.value).then(res=>{
					console.log("sign res:",res);
					if(res.code == 200){
						saveBlob(base64ToBlob(res.result.signFile),new Date().getTime()+'.pdf');
						signSuccessTips();
					}else{
						signErrorTips(res.message);
					}
					spinning.value = false;
				});
			}
			/**
			 * 签署成功弹窗提示
			 */
			function signSuccessTips(){
				Modal.success({
					title: '文档签署成功，自动下载签署文件',
					width:'500px',
					icon: createVNode(CheckCircleOutlined),
					content: h('div',{}, [
						h('br'),
						h('div','1、可使用adobe软件打开文件，查看文件中的签章信息'),
						h('br'),
						h('div',{style: 'display:flex'},[
							h('div','2、可进入到“开放签”官网进行验签'),
							h('a',{href:'https://www.kaifangqian.com/#/verification',target:'_blank',style:'margin-left:10px'},'去验签'),
						]),
					]),
					okText:'确定'
				});
			}
			
			/**
			 * 签署失败弹窗提示
			 */
			function signErrorTips(msg){
				Modal.error({
					width:'500px',
					title: '文档签署失败',
					icon: createVNode(CloseCircleOutlined),
					content: createVNode('div',null, msg),
					okText:'确定'
				});
			}
			
			
			
			/**
			 * 
			 * 如果没有设置签名图片 打开相关的弹窗
			 * @param {Object} element 
			 */
			function openModal(element){
				if(element.type == ControlType.Seal && !element.value){
					//signData.value.entPositionList.push(temItem)
					sealModalShow.value = !sealModalShow.value;
				}else if(element.type == ControlType.Signature && !element.value){
					//signData.value.personalPositionList.push(temItem)
					signatureModalShow.value = !signatureModalShow.value;
				}
			}
			
			/**
			 * 重置页面
			 */
			function reloadPage(){
				window.location.reload();
			}
			
			/**
			 * 跳转 外部链接
			 * @param {Object} path 路径
			 */
			function jumpExternal(path){
				window.open(path, '_blank');
			}
			
			
			return{
				getWindowHeight,signData,documentPDF,CanvasZoom,thisControlList,clone,groupOut,groupEnter,controlsDragOver,
				controlDelete,sealModalShow,sealModalSubmit,signatureModalSubmit,signatureModalShow,signDocument,
				controlClass,openModal,ControlType,signTypeChange,keywordFromRef,reloadPage,jumpExternal,spinning
			}
		}
	})
</script>


<template>
	<Spin :spinning="spinning" tip="签署中" :delay="100" style="max-height: 100%;">  
		<div class="layout-main" :style="'height:'+getWindowHeight()+'px'">
			<div class="layout-header">
				<ul class="header-menu">
					<!-- logo -->
					<li class="header-logo">
						<img src="@/assets/images/logo-open-sign.png"/>
					</li>
					<!-- 操作按钮 -->
					<li class="header-btn">
						<Space :size="40">
							<Button size="large" class="custom-ant-button" @click="reloadPage">重置</Button>
							<Button type="primary" size="large" class="custom-ant-button" @click="sealModalShow = !sealModalShow">印章制作</Button>
							<Button type="primary" size="large" class="custom-ant-button" @click="signatureModalShow = !signatureModalShow">手写签名</Button>
							<Button type="primary" size="large" class="custom-ant-button" @click="signDocument">签署</Button>
						</Space>
					</li>
					<!-- 外部链接 -->
					<li class="header-external">
						<Space :size="40">
							<Button type="primary" size="large" class="custom-ant-button" @click="jumpExternal('https://www.kaifangqian.com/#/verification')" >文件验签</Button>
							<Button type="primary" size="large" class="custom-ant-button" @click="jumpExternal('https://www.kaifangqian.com')">了解“开放签”</Button>
						</Space>
					</li>
				</ul>
			</div>
			<div class="layout-body">
				<div class="sign-configure">
					<h3>签署配置</h3>
					<div class="sign-mode">
						<div class="configure-title">
							<span>签署方</span>
						</div>
						<RadioGroup v-model:value="signData.signType" @change="signTypeChange">
							<RadioButton :value="1">指定位置签署</RadioButton>
							<RadioButton :value="2">关键字签署</RadioButton>
						</RadioGroup>
						<div class="mode-location" v-if="signData.signType == 1">
							<span>签署方式选择“指定位置签署”时，可拖拽“电子印章”和“手写签名”至右侧的文档中，指定对应的签署位置；</span>
						</div>
						<div class="mode-keyword" v-if="signData.signType == 2">
							<ul>
								<Form  :model="signData" name="basic" aut="off" ref="keywordFromRef"  class="keyword-from">
									<li class="li-style">电子印章-关键字设置<Button type="link" @click="signData.entKeyword='电子印章盖章处'">默认关键字</Button></li>
									<li >
										<FormItem name="entKeyword"
											:rules="[{ required: false, message: '电子印章-关键字', trigger: 'change' }]" >
										  <Input v-model:value="signData.entKeyword" placeholder="请输入电子印章签署在文件中的关键字" class="input-width" />
										</FormItem>
									</li>
									<li class="li-style">手写签名-关键字设置<Button type="link" @click="signData.personalKeyword = '个人手写签名处'">默认关键字</Button></li>
									<li >
										<FormItem name="personalKeyword"
											:rules="[{ required: false, message: '电子印章-关键字',  trigger: 'change'}]" >
										  <Input v-model:value="signData.personalKeyword" placeholder="请输入手写前面签署在文件中的关键字" class="input-width" />
										</FormItem>
									</li>
								</Form>
							</ul>
						</div>
					</div>
					<div class="sign-signer">
						<div class="configure-title">
							<span>签署方</span>
						</div>
						<div class="signer-list">
							<ul>
								<draggable :list="thisControlList"  handle=".control-move" filter=".unmover"  item-key="name"
									 :force-fallback="true"  animation="300" @end="controlsDragOver"
									 :group="groupOut" :fallback-class="true" :fallback-on-body="true" :touch-start-threshold="50"
									 :fallback-tolerance="50" :clone="clone" :sort="false" drag-class="drag-class">
								 <template #item="{ element }">
									<div>
										<li class="unmover li-style" v-if="element.type== ControlType.Seal">电子印章-{{element.user.userName?element.user.userName:'未制作印章'}}</li>
										<li class="unmover li-style" v-if="element.type== ControlType.Signature">手写签名-{{element.user.userName?element.user.userName:'未手写签名'}}</li>
										<li  :class="['li-entp-seal',controlClass(element)]">
											<div class="entp-seal item" v-if="element.type== ControlType.Seal" @click="openModal(element)">
												<img :src="'data:image/png;base64,'+element.value" v-if="element.value"/>
												<span v-else>请先制作印章</span>
											</div>
											<div class="person-seal item" v-if="element.type== ControlType.Signature" @click="openModal(element)">
												<img :src="'data:image/png;base64,'+element.value" v-if="element.value"/>
												<span v-else>请先设置手写签名</span>
											</div>
										</li>
									</div>
								</template> 
								</draggable>
							</ul>
						</div>
					</div>
				</div>
				<div class="sign-content">
					<c-scrollbar>
					 <div class="document-content">
						<div class="document-list" :style="[
							'height: '+ (CanvasZoom.height * documentPDF.images.length + documentPDF.images.length * 16) +'px;'
						]" v-if="documentPDF && documentPDF.images">
							<template v-for="item in documentPDF.images">
								<div class="document-page group"  v-if="item"
									:style="'transform: translate(0,'+(item.docPage * CanvasZoom.height + (item.docPage+1) * 16)+'px);'">
									<img style="width: 100%;" :src="item.image"/>
								</div>
							</template>
							<draggable :list="documentPDF.control" ghost-class="ghost" draggable=".test"
								item-key="uid"
								:group="groupEnter" :force-fallback="true" chosen-class="chosenClass" animation="300"
								:fallback-class="true" :fallback-on-body="true" :touch-start-threshold="50"
								:fallback-tolerance="50" 
								style="width: 100%;height: 100%;position: relative;"> 
								<template #item="{ element }"  >
									<ControlItem :doc="documentPDF.control" :element="element" :isSign="false"
									 @controlDelete="controlDelete"/>
								</template>
							</draggable>
						</div>
					</div> 
					</c-scrollbar>
				</div>
				<div class="sign-description">
					<c-scrollbar>
						<div class="description-scrollbar">
							<h3>操作说明</h3>
							<ul>
								<li class="description-title">生成数字证书</li>
								<li class="description-text">该系统内置了用于演示的数字证书，关于数字证书的介绍，可参考《<a href="https://www.yuque.com/huxin-ch41t/kaifangqian/ni7eglg3by4wgm7v" target="_blank">数字证书</a>》</li>
								<Divider style="border-color: #bbb;margin: 10px 0;"/>
								<li class="description-title">制作电子印章/手写签名</li>
								<li class="description-text">点击【制作印章】按钮，按照提示信息生成电子印章</li>
								<li class="description-text">点击【手写签名】按钮，按照提示信息生成手写签名</li>
								<Divider style="border-color: #bbb;margin: 10px 0;"/>
								<li class="description-title">选择签署方式</li>
								<li class="description-text">本系统提供了两种签署方式：</li>
								<li class="description-text" style="padding-top: 0.1rem;line-height: 22px;">（1）指定位置签署：将电子印章/手写签名拖拽到文件页面的某一位置，签署时，系统会将电子印章/手写签名加盖到文件的对应位置；</li>
								<li class="description-text" style="padding-top: 0.1rem;line-height: 22px;">（2）关键字签署：输入电子印章/手写签名需要加盖到文件上的关键字，签署时，系统会在文档中查找对应关键字的位置，并加盖电子印章/手写签名；</li>
								<Divider style="border-color: #bbb;margin: 10px 0;"/>
								<li class="description-title">文件签署</li>
								<li class="description-text">点击【签署】按钮，进行文件签署</li>
								<Divider style="border-color: #bbb;margin: 10px 0;"/>
								<li class="description-title">签署文件验证</li>
								<li class="description-text">验证的两种方式：</li>
								<li class="description-text" style="padding-top: 0.1rem;line-height: 22px;">（1）下载自行验证：签署完成后，可下载签署后的文件，使用adobe等软件验证文件电子签章信息；</li>
								<li class="description-text" style="padding-top: 0.1rem;line-height: 22px;">（2）在“开放签”平台验证：签署完成后，可下载签署后的文件，点击导航栏的【文档验签】按钮，进入到“开放签”官网进行验签</li>
								
								<Divider style="border-color: #bbb;margin: 10px 0;"/>
								<li class="description-text font-red">注意</li>
								<li class="description-text font-red">该演示系统仅是为了帮助您更好的了解电子签章相关的操作，签署的文件不具备法律效力，同时对于该演示系统签署的任何文件所产生的法律问题，我方不承担任何后果！</li>
							</ul>
						</div>
					</c-scrollbar>
				</div>
			</div>
		</div>
	</Spin>
	<SealModal :modalVisible="sealModalShow" @success="sealModalSubmit"/>
	<SignatureModal :modalVisible="signatureModalShow" @success="signatureModalSubmit" />
</template>


<style>
	.keyword-from .ant-form-item{
		margin-bottom: 0 !important;
	}
</style>
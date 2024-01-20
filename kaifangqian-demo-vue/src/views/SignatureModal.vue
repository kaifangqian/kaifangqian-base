<template>
	<Modal v-model:visible="modalOpen" title="创建手写签名"  width="800px">
		<div style="width: 720px;padding-left: 40px;">
			<Form  :model="signatureForm" name="basic"
			  aut="off" ref="signatureFormRef" >
			<FormItem label="姓名" name="userName"
			    :rules="[{ required: true, message: '请输入姓名' }]" >
			  <Input v-model:value="signatureForm.userName"  size="middle" placeholder="请输入姓名/生成用于签署的测试数字证书" class="input-width" />
			</FormItem>
			<FormItem label="" >
				<div style="padding-bottom: 20px;">
					<div class="signature-box" :class="!startSignature?'signature-bg':''">
						<VueSignaturePad ref="signaturePad" :images="[{ src: 'A.png', x: 0, y: 0 }, { src: 'B.png', x: 0, y: 10 }, { src: 'C.png', x: 0, y: 20 }]" :options="options" />
					</div>
				</div>
			</FormItem>
			</Form>
		</div>
		<template #footer>
			<div style="text-align: center;">
				<Space :size="20">
					<Button type="link" @click="undoSignature">撤销</Button>
					<Button type="link" @click="clearSignature">重写</Button>
					<Button key="back" @click="modalOpen = false">取消</Button>
					<Button key="submit" type="primary"  @click="handleOk">使用</Button>
				</Space>
			</div>
		</template>
	</Modal>
</template>

<script>
	import { defineComponent,ref,unref,computed,reactive,onMounted,watch } from 'vue';
	import { Modal,Form,FormItem,Input,Row,Col,RadioGroup,RadioButton,Button,Upload,message,Space} from 'ant-design-vue';
	import { VueSignaturePad } from 'vue-signature-pad';
	import signatureBg from "@/assets/images/signature-bg.png";
	
	export default defineComponent({
		name: 'SignatureModal',
		components:{
			Space,
			Modal,Button,FormItem,Form,Input,VueSignaturePad
		},
		props:{
			modalVisible:{
				type:Boolean,
				default:false
			}
		},
		emits:["success"],
		setup(props,{emit}) {
			//手写前面modal的开关
			const modalOpen = ref(false);
			//监听main页面的modalVisible 是否需要打开弹窗
			watch(() => props.modalVisible,
			(newValue,oldValue) => {
				modalOpen.value = true;
				console.log();
			})
			
			//表单字段
			const signatureForm = ref({
				userName:""
			});
			const signatureFormRef = ref();
			//手写面板是否已经开始了  如果开始把前面面板的背景色去掉
			const startSignature = ref(false);
			
			const signaturePad = ref();
			
			//签名面板配置
			const options = ref({
			    penColor: "#000",
			    dotSize: (1 + 4) / 2,
			    minWidth: 1,
			    maxWidth: 4,
			    throttle: 16,
			    minDistance: 5,
			    backgroundColor: 'rgba(0,0,0,0)',
			    velocityFilterWeight: 0.5,
			    onBegin: () => {
					startSignature.value = true;
			    },
			    onEnd: () => {
			    }
			});
			
			/**
			 * 提交手写签名
			 */
			async function handleOk(){
				try{
					//验证姓名是否输入   便于签名证书的生成
					const values = await signatureFormRef.value.validateFields();
					//获取签名图片
					const { isEmpty, data } = signaturePad.value.saveSignature();
					if(isEmpty){
						message.warning("签名为空，请书写后再进行添加")
						return;
					}
					const resultImg = data.split(",");
					//将签名图片返给main页面
					const result = {image:resultImg[1],userName:signatureForm.value.userName}
					emit('success',result);
					modalOpen.value  = false;
					
					 
				}catch (errorInfo) {
				  console.log('Failed:', errorInfo);
				  message.warning("姓名不能为空，请输入姓名")
				}
				
			}
			
			/**
			 * 撤销
			 */
			function undoSignature(){
				signaturePad.value.undoSignature();
			}
			
			/**
			 * 重写
			 */
			function clearSignature(){
				signaturePad.value.clearSignature();
				startSignature.value = false;
			}
			
			return{
				handleOk,modalOpen,signatureForm,signatureFormRef,
				startSignature,signaturePad,options,undoSignature,clearSignature
			}
		}
	})
 
	
</script>

<style lang="less" scoped>
	
	.signature-box{
		width:100%;
		height: 360px;
		border:1px solid #dedede;
	}
	.signature-bg{
		background: url('@/assets/images/signature-bg.png') no-repeat;
		background-size: 70% 70%;
		background-position: 50% 50%;
	}
</style>
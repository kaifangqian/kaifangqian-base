<template>
	<Button @click="susssss">提交</Button>
</template>

<script lang="ts">
	import { defineComponent,ref,unref,computed,reactive,onMounted,createVNode,h } from 'vue';
	import { Button,Modal} from 'ant-design-vue';
	export default defineComponent({
		name: 'mian',
		components:{
			Modal,Button
		},
		setup() {
			function susssss(){
				Modal.confirm({
				        title: 'Info',
				        content: 'This is some information.',
				        onOk: () => {
				          // 手动关闭弹框
						  console.log(Modal.destroyAll());
				        },
				      });
			}
			return{
				susssss
			}
		}
	})
		
	
</script>

<style>
</style>
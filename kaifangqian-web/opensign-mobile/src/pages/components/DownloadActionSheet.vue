<!--
  @description 签署文档下载

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
	<!-- <van-action-sheet v-model="actionSheet.show" title="标题">
	  <p>内容</p>
	</van-action-sheet> -->
	<van-action-sheet v-model:show="actionSheet.show" title="下载文件">
		<div>
			<van-checkbox-group v-model="checked" shape="square">
			  <van-cell-group inset>
			    <div class="file-content">
					<div class="title">签约文件</div>
					<div class="list" v-for="(item, index) in actionSheet.fileList" >
						<div class="filename" @click="selectFile(item)">{{item.name}}</div>
						<div class="checkbox" style="width: 1rem;">
							<van-checkbox :name="item.id" />
						</div>
					</div>
				</div>
				<van-field label="邮箱" label-align="left" label-width="3em" v-model="email" name="emailValidator"
				 placeholder="填写，下载到邮箱，不填，下载到本地" :border="true" />
				<!-- <p class="email-tips">填写，则下载到邮箱，不填，则下载到本地</p> -->
				 <van-field label-width="3em"
				    v-model="message"
				    rows="2"
					maxlength="50"
				    label="留言"
				    type="textarea"
				    placeholder="请输入留言"
					show-word-limit
					border
				  />
				<van-cell >
					<div style="width: 100%;text-align: center;">
						<van-button type="primary" size="small" style="width: 120px;" @click="onSubmit">下载</van-button>
					</div>
				</van-cell>
			  </van-cell-group>
			  
			</van-checkbox-group>
			</div>
	</van-action-sheet>
</template>

<script lang="ts">
	import { ref, defineComponent,onBeforeUpdate } from "vue"
	import { Toast } from 'vant';
	import Api from '@/api/contract/index';
	import { handleRuDownload } from '@/utils/util';
	export default defineComponent({
	    name: "DownloadActionSheet",
	    props: {
	        actionSheet: {
	            type: Object,
	            default: {
					show:false,
					fileList:[],
					signRuId:"",
				}
	        },
	    },
	    setup(props) {
			const checked = ref([]);
			const checkboxRefs = ref([]);
			const email = ref("");
			const message = ref("");
			
			onBeforeUpdate(() => {
				checkboxRefs.value = [];
			});
			const toggle = (index) => {
				checkboxRefs.value[index].toggle();
			};
			function initData(){
				checked.value = [];
				if(props.actionSheet.fileList && props.actionSheet.fileList.length>0){
					props.actionSheet.fileList.forEach(item=>{
						checked.value.push(item.id);
					})
				}
			}
			
			const emailValidator = (val) =>{
				if(!val) return true;
				return /^([a-zA-Z0-9._-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(val)
			}
			async function onSubmit(){
				if(!checked.value || checked.value.length == 0){
					Toast({
						message:'请选择要下载的签约文件',
						position: 'bottom',
					});
					return;
				}
				if(!emailValidator(email.value)){
					Toast({
						message:'您输入的邮箱格式不正确',
						position: 'bottom',
					});
					return;
				}
				props.actionSheet.show = false;
				const loading = Toast.loading({
					duration:0,
					message: '下载中...',
					forbidClick: true,
				})
				if(email.value){
					const result = await Api.sendEmaildownlaod({
						signRuId:props.actionSheet.signRuId,
						sendEmail: 'email',
						email:email.value,
						remarks:message.value
					},buildParams(checked.value));
					if(result.success){
						setTimeout(function(){
							loading.clear();
							Toast.success('文档已发送至指定邮箱，请注意查收');
						},300)
					}
				}else{
					await handleRuDownload({
						signRuId:props.actionSheet.signRuId,
						ruDocIdList:checked.value
					});
					setTimeout(function(){
						loading.clear();
						Toast.success('下载成功');
					},500)
				}
			}
			function buildParams(arr:any){
				var params = "?";
				arr.forEach(item=>{
					params += "ruDocIdList="+item + "&";
				})
				return params;
			}
			function selectFile(file){
				var delFlag = false;
				checked.value.forEach((item,index,arr)=>{
					if(item == file.annexId){
						arr.splice(index,1);
						delFlag = true;
					}
				})
				if(!delFlag){
					checked.value.push(file.annexId);
				}
			}
			return {
				checked,toggle,checkboxRefs,email,initData,emailValidator,onSubmit,
				selectFile,message
			}
	    }
	})
</script>

<style scoped lang="less">
	.email-tips{
		padding: 0;
		margin: 0;
		text-align: center;
		color: rgba(0,0,0,0.3);
		font-size: 0.3rem;
		line-height: 1rem;
	}
	.file-content{
		padding:var(--van-cell-vertical-padding) var(--van-cell-horizontal-padding);
		.title{
			font-size: var(--van-cell-font-size);
			line-height: 1rem;
		}
		.list{
			line-height: 1rem;
			// padding-left: 1rem;
			padding-left:calc(3em + var(--van-field-label-margin-right));
			display: flex;
			position: relative;
			// border-bottom: 1px solid rgba(0,0,0,0.1);
			.filename{
				font-size: var(--van-cell-font-size);
				color: rgba(0,0,0,0.5);
				flex:1;
				overflow:hidden;/*内容超出后隐藏*/
				text-overflow:ellipsis;/*超出内容显示为省略号*/
				white-space:nowrap;/*文本不进行换行*/
			}
			.checkbox{
				display: flex;
				align-items: center;
				justify-content: center;
			}
			&::after{
				content: '';
				position: absolute;
				bottom: 0;
				left: 1rem;
				width: calc(100% - 3em - var(--van-field-label-margin-right));
				height: 1px;
				background-color: rgba(0,0,0,0.1);
			}
		}
	}
</style>
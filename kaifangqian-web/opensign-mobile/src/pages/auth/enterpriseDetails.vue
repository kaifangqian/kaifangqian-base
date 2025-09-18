<!--
  @description 企业详情

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
	<div class="auth-step form-auth-container" >
		 
	<div class="step-2">
		<div>
			  <van-cell-group inset>
			    <van-field
			      v-model="formData.name"
			      name="name"
			      label="组织机构名称"
				  readonly
			    />
			    <van-field
			      v-model="formData.organizationNo"
			      type="text"
			      name="idCard"
			      label="组织机构代码"
				  readonly
			    />
				<van-field
				  v-model="formData.corporation"
				  name="corporation"
				  label="法人姓名"
				  readonly
				/>
				<van-field
				  v-model="formData.corporationNo"
				  name="corporationNo"
				  label="法人证件号"
				  readonly
				/>
				<van-field
				  v-model="formData.phone"
				  type="text"
				  name="phone"
				  label="法人手机号"
				  readonly
				/>
			  </van-cell-group>
			  <div style="margin: 16px;">
			   <van-button type="primary" block @click="gohome">返回首页</van-button>
			  </div>
		</div>
	</div>
	</div>
</template>

<script lang="ts" setup>
	import { ref ,reactive,onMounted,onBeforeUnmount} from "vue";
	import { useUserStore } from '@/store/modules/user';
	
	const router = useRouter();
	
	const userStore = useUserStore();
	const tenantInfo = userStore.getTenantInfo
	
	 function gohome(){
		router.push("/home");
	 }
	 onMounted(()=>{
	 	formData.value.name = tenantInfo.name;
	 	formData.value.organizationNo = tenantInfo.organizationNo;
	 	formData.value.phone = tenantInfo.phone;
		formData.value.corporation = tenantInfo.corporation;
		formData.value.corporationNo = tenantInfo.corporationNo;
	 })
	 
		
	const formData = ref({
		name:"",
		idCardFace:"",
		idCardEmblem:"",
		organizationNo:"",
		corporation:"",
		corporationNo:"",
		phone:"",
		captcha:"",
		captchaKey:"",
		authType:1,
		realItem:1,
	});
 
	 
	
</script>

<style lang="less" scoped>
	
	.step-1{
	  width: 8rem;
	  margin: 0 auto;
	  .auth-tip-container{
	    display: flex;
	    flex-direction: column;
	  }
	  .tip-title{
	    font-weight: 600;
		text-align: left;
		align-items: left;
		padding: 0;
		margin: 0;
	  }
	  .tip-info{
	    background: #f4f9ff;
	    padding:10px 15px;
	    border-radius: 2px;
	  }
	  .protocol-info{
	    margin:15px 0;
	  }
	}
	.step-2{
		.van-cell-group--inset{
			margin: 0;
		}
	}
</style>
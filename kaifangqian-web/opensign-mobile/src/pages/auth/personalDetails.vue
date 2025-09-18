<!--
  @description 个人详情

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
	<div class="personal">
		  <van-cell-group inset>
		    <van-field
		      v-model="formData.name"
		      name="name"
		      label="姓名"
			  readonly
		      placeholder="请输入姓名"
		    />
		    <van-field
		      v-model="formData.organizationNo"
		      type="text"
			  readonly
		      name="idCard"
		      label="身份证号"
		    />
			<van-field
			  v-model="formData.phone"
			  type="text"
			  readonly
			  name="phone"
			  label="手机号"
			/>
		  </van-cell-group>
		  <div style="height: 20px;"></div>
		  <van-button type="primary" block @click="gohome">返回首页</van-button>
	</div>
</template>

<script lang="ts" setup>
	import { ref ,reactive,onMounted,onBeforeUnmount} from "vue";
	import { useUserStore } from '@/store/modules/user';
	const router = useRouter();
	const userStore = useUserStore();
	const tenantInfo = userStore.getTenantInfo
	const formData = ref({
		name:"",
		idCardFace:"",
		idCardEmblem:"",
		organizationNo:"",
		phone:"",
		captcha:"",
		captchaKey:""
	});
	
	onMounted(()=>{
		formData.value.name = tenantInfo.name;
		formData.value.organizationNo = tenantInfo.organizationNo;
		formData.value.phone = tenantInfo.phone;
	})
	function gohome(){
		router.push("/home");
	}
	
	
  
</script>

<style lang="less" scoped>
	.personal{
		.van-cell-group--inset{
			margin: 0;
		}
	}
</style>
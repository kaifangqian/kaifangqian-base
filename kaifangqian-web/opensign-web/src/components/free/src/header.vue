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
	<div class="free-header">
		<!-- <div class="header-1366"> -->
			<div class="left-content">
				<Button type="text" size="large" @click="preStep.callBack">
					<template #icon>
						   <Icon :icon="preStep.icon" style="font-size: 20px;"/>
					</template>
				</Button>
				<div class="header-name">
					{{title}}
				</div>
			</div>
			<div class="center-content">
				<slot></slot>
			</div>
			<div class="right-content">
				<template v-for="item in buttons">
					<Button :type="item.type" @click="item.callBack"
					style="margin-left:20px;" v-if="!item.disabled"
					>{{item.name}}</Button>
				</template>
			</div> 
		<!-- </div> -->
	</div>
</template>

<script lang="ts">
	
	import { defineComponent, } from 'vue'
	import { Button,PageHeader } from 'ant-design-vue'
	import { CloseOutlined } from '@ant-design/icons-vue';
	import {Icon} from "@/components/Icon";
	
	export default defineComponent({
		name: 'ContentHeader',
		components: {
			Button,
			PageHeader,
			CloseOutlined,
			Icon
		},
		props:{
			title: {
				type: String,
				required:true
			},
			preStep:{
				type: Object
			},
			buttons:{
				type: Object
			}
		},
		setup() {
			return {
				
			}
		}
	})
</script>

<style lang="scss" scoped>
	.free-header{
		height: 64px;
		border-bottom: 1px solid rgba(0, 0, 0, 0.08);
		background-color: #fff;
		position: absolute;
		display: flex;
		top:0;
		width: 100%;
		min-width: 1366px;
		z-index: 999;
		.center-content{
			font-size: 14px;
			flex: 1;
			text-align: center;
			min-width:800px;
		}
		.left-content{
			width: 283px;
			line-height: 64px;
			padding-left: 20px;
			box-sizing: border-box;
			display: flex;
			align-items: center;
			.header-name{
				font-size: 20px;
				font-weight: 600;
				position: relative;
				padding-left: 30px;
			}
			.header-name::after{
				content: "";
				position: absolute;
				width: 2px;
				height: 18px;
				background-color: #999;
				left: 10px;
				top:22px
			}
		}
		.right-content{
			width: 283px;
			text-align: right;
			line-height: 64px;
			padding-right: 20px;
		}
	}
	.header-1366 {
		display: flex;
		width:1366px;
		margin: 0 auto;
	}
</style>

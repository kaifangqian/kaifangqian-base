<script setup>
	import { Button,Space } from 'ant-design-vue';
	
</script>
<template>
	<ul class="header-menu">
		<!-- logo -->
		<li class="header-logo">
			<img src="@/assets/images/logo-open-sign.png"/>
		</li>
		<!-- 操作按钮 -->
		<li class="header-btn">
			<Space :size="40">
				<Button size="large" class="custom-ant-button">重置</Button>
				<Button type="primary" size="large" class="custom-ant-button">印章制作</Button>
				<Button type="primary" size="large" class="custom-ant-button">手写签名</Button>
				<Button type="primary" size="large" class="custom-ant-button">签署</Button>
			</Space>
		</li>
		<!-- 外部链接 -->
		<li class="header-external">
			<Space :size="40">
				<Button type="primary" size="large" class="custom-ant-button">文件验签</Button>
				<Button type="primary" size="large" class="custom-ant-button">了解“开放签”</Button>
			</Space>
		</li>
	</ul>
</template>


<style scoped lang="less">
	.header-menu{
		width: 100%;
		border-bottom: 1px solid #e3e3e3;
		padding: 0;
		margin: 0;
		display: flex;
		padding: 0.04rem 0;
	}
	.header-menu li{
		list-style: none;
		height: 0.7rem;
	}
	.header-menu .header-logo{
		width: 4rem;
		justify-content: center;
		align-items: center;
		display: flex;
		box-sizing: border-box;
		img{
			height: 0.62rem;
		}
	}
	.header-menu .header-btn{
		flex:1;
		min-width: 8rem;
		justify-content: center;
		align-items: center;
		display: flex;
		
	}
	.header-menu .header-external{
		width: 4rem;
		justify-content: center;
		align-items: center;
		display: flex;
	}
	.custom-ant-button{
		width: 1.40rem;
		font-size: 0.16rem !important;
		height: 0.4rem;
		padding: 0;
	}
</style>
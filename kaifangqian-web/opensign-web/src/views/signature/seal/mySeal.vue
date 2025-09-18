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
  <div class="signature-main">
  	<div class="user-seal-content">
  		<ul :class="['seal_list',sealList&&sealList.length?'':'no_seal_list']">
  			<li v-for="item in sealList" v-if="sealList&&sealList.length">
  				<div class="seal-item" :class="item.status == 2?'disable':''">
  					<div class="seal-img-box ">
  						<!-- <span class="seal-stop" v-if="item.status == 2">已停用</span> -->
  						<img class="seal-img" :src="item.signature"/>
              <div class="seal-mask" v-if="item.sealStatus != 4 || item.status == 2">
                <span class="seal-status-text" v-if="item.status == 1">{{ loadSealStatusText(item.sealStatus) }}</span>
                <span class="seal-status-text" v-if="item.status == 2">禁用</span>
              </div>
  					</div>
            <a-tooltip>
              <template #title> {{item.sealName}} </template>
                  <div class="seal-name">
                  {{item.sealName}}
                </div>
            </a-tooltip>
  				
  					<div class="seal-name">
  						<template v-if="item.isDefault == 1">
  							<Icon color="#00aa64" icon="ant-design:check-circle-outlined"/>
  							<span style="line-height: 20px;color: #999;">默认签章</span>
  						</template>
  						<!-- <Button v-else type="link" @click="setDefaultSeal(item)" :disabled="item.status == 2">设为默认印章</Button> -->
  					</div>
  				</div>
  			</li>
        <div v-else>
            <p class="no-data">
                <img src="~@/assets/images/no-data.png" alt="">
            </p>
          </div>
  		</ul>
  	</div>
  </div>
</template>

<script lang="ts">
  
  import {defineComponent,ref,onMounted} from 'vue';
  
  import {message } from 'ant-design-vue';
  
  import {Icon} from "/@/components/Icon";
  
  import {getMySeal} from "./api";
  import { getImgBase64} from '/@/api/sys/upload'; 
  
  
  export default defineComponent({
  	name: 'userSeal',
  	components: {
  		Icon,
  		message
  	},
  	setup() {
       const sealList:any = ref([]);
       
      function initData(){
      	getMySeal().then((result:any)=>{
      		
      		if(result && result.length>0){
      			// result.sort(function(a:any, b:any){
      			//     return a.isDefault - b.isDefault
      			// })
      			sealList.value = result;
      			loadSealBase64();
      		}else{
      			sealList.value = [];
      		}
      		// console.log("sealList：---",sealList.value,res)
      	})
      }
      function loadSealBase64(){
      	sealList.value.forEach((item:any)=>{
      		getImgBase64({imgId:item.annexId}).then((img:any)=>{
      			item.signature = img.image;
      		})
      	})
      }
      function loadSealStatusText(status){
        switch(status){
          case 1:
            return '制作中';
          case 2:
            return '失败';
          case 3:
            return '停用';
          case 5:
            return '收缴';
          case 6:
            return '销毁';
          default: 
            return ''
        }
      }
      onMounted(() => {
        initData();
      });
      return{
        sealList,
        loadSealStatusText
      }
    }
    
  })
</script>

<style lang="less" scoped>
  .signature-main{
    // width: 1286px;
    margin: 0 auto;
    height: calc(100vh - 160px);
    overflow: auto;
  }
	.user-seal-content{
		ul li{
			margin-right: 20px;
      // flex: 1 0 30%; 
      // margin-right: auto;
		}
    .seal_list{
      display: flex;
      flex-wrap:wrap;
      margin-top:25px;
      // justify-content: center;
    }
    .no_seal_list{
      justify-content:center;
    }
		.seal-item{
			.seal-img-box{
        width: 166px;
        height: 166px;
				padding: 20px;
				border: 1px dashed #ccc;
        margin:0 auto;
				position: relative;
        .seal-mask{
          position: absolute;
          left:0;
          top:0;
          right:0;
          bottom:0;
          background-color: rgba(187, 187, 187, 0.8);
          display: flex;
          align-items: center;
          justify-content: center;
          .seal-status-text{
            font-size: 16px;
          }
			  }
      }
			.seal-stop{
				position: absolute;
				top: 5px;
				right: 10px;
				color: #fff;
				padding: 0 5px;
				background-color: red;
			}
		/* 	.seal-img{
				width: 166px;
				width: 166px;
			} */
			.seal-name{
				line-height: 40px;
				text-align: center;
				font-size: 14px;
        width:200px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
			}
		}
		.disable{
			cursor: not-allowed;
		}
	}
</style>

<!--
  @description image

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
    <div :class="['control-'+element.controlType,'control-item','arow-content','this-click',
        element.controlClick?'click':'default',
        ]" :style="[
        'width:' + element.size.width + 'px',
        'height:' + element.size.height + 'px']"
      >
  
      <div :class="element.uploadImg?'preview':''"
      >
        <div v-if="!element.uploadImg">
            <span v-if="!element.uploadImg" style="visibility:hidden;position:absolute">{{loadImg(element)}}</span>
            <SvgIcon name="img" :size="30" style="margin:0 auto;"></SvgIcon>
            <!-- <p style="margin: 0;text-align: center;color:#979797">图片</p> -->
        </div>
        <div v-if="element.uploadImg" class="default-img">
            <van-image
                :src="element.uploadImg"
            />
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
    import {ref} from 'vue';
    import Api from '@/api/system';



    const props = defineProps({
      element:{
          type: Object,
        default:{}
      },
    });
    const emit = defineEmits(["change","blur"]);
    const inputRef = ref();
    const fileList = ref([]);

    async function loadImg(item:any){
      if(!item.value) return;
      if(item.controlType != 'image') return;
      let { result, code } = await Api.getImgBase64({imgId:item.value})
          if(code==200){
              item.uploadImg = result.image; 
      }
    }
  
    
   
    
    function inputBlur(){
      emit('blur')
    }
    
    function itemFocus(){
      
      if(inputRef.value)
      inputRef.value.click();
    }
    
    const uploadImg = ref();
   
    
    
    
    defineExpose({
      itemFocus
    })
  </script>
  
  <style lang="less">
    .control-image{
      display: flex;
      justify-content: center;
      align-items: center;
      .default-img{
        width: 100%;
        height:100%;
      }
      .van-image{
        width:100%;
        height:100%;
      }
      .preview{
        width: 100%;
        height: 100%;
        // display: flex;
        // align-items: center;
        &>span{
          width: 100%;
          height: 100%;
        };
        .ant-upload{
          width: 100%;
          height: 100%;
        }
        
        background-repeat: no-repeat;
        background-position: center;
        background-size: contain;
      }
      
    }
  </style>
  
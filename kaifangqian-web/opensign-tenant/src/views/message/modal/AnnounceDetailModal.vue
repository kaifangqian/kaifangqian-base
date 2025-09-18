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
  <div>
         <BasicModal v-bind="$attrs" @register="registerModal" :title="getTitle" @ok="handleSubmit" :destroyOnClose="true" :style="{'height':'800px'}">
            <Descriptions   size="middle"  :title="state.detailInfo.title"  class="detail-header"
            >
              <DescriptionsItem>
                  <div>
                      <div class="">
                        <span class="detail-item"><span class="item-title">发布人：</span>{{state.detailInfo.sender}}</span>
                        <span class="detail-item"><span class="item-title">发布时间： </span> {{state.detailInfo.createTime}}</span>
                      </div>
                  </div>
              </DescriptionsItem>
            </Descriptions>
            <span class="html-text-box" v-html="state.detailInfo.msgContent"></span>
            <div class="file-list">
              <p class="file-title">附件列表</p>
              <div v-for="(item,index) in fileList" :key="index">
                <span class="file-name">{{item.realName}}</span>
                <a href="javascript:;" @click="handleDownload(item)">
                <!-- <a :href="baseDownLoadUrl+'/file/downloadFileStream/'+ item.id" :download="item.realName">  -->
                  下载</a>
              </div>
            </div>
            <div class="detail-button">
              <template v-for="(item,index) in  state.detailInfo.buttons" :key="index">
                <a-button @click="handleButton(item)" :type="item.buttonStyle" >{{item.buttonName}}</a-button>
              </template>
            </div>
            <template #footer>
              <a-button @click="closeModal()">关闭</a-button>
            </template>
        </BasicModal>
  </div>
</template>
  <script lang="ts">
    import { defineComponent,ref,reactive, nextTick } from 'vue';
    import { BasicModal, useModalInner } from '/@/components/Modal';
    // import { downloadByData } from '/@/utils/file/download';
    import { Descriptions,DescriptionsItem } from 'ant-design-vue';
    import { announceDetail,getAnnounceFile } from '/@/api/announce';
    import { getFileStrem,getFileArrayBuffer } from '/@/api/sys/upload';
    import { useGo } from '/@/hooks/web/usePage';
    // import FileSaver from 'file-saver';
    import axios from 'axios';
    import { getToken } from '/@/utils/auth';

  
    export default defineComponent({
      name:'MessageDetailModal',
      components: { Descriptions,BasicModal,DescriptionsItem},
      setup() {
        const state = reactive({
          detailInfo:{
            title:'',
            sender:'',
            msgContent:'',
            createTime:''
          }
        });
        const baseDownLoadUrl  = ref(import.meta.env.VITE_PROXY_ADDRESS);
        const fileList  =  ref([]);
        const minHeight = ref('');
        const go = useGo();
        const [registerModal, { setModalProps,closeModal }] = useModalInner(async (data) => {
          console.log(data)
          setModalProps({ confirmLoading: false,width:1200 });
          let recordId = data.record.id;
          if(recordId){
            let result = await announceDetail({id:recordId});
            fileList.value = await getAnnounceFile({fatherId:recordId});
            if(result){
              state.detailInfo = {...result};
              nextTick(()=>{
                minHeight.value = '90%';
              })
            }
          }
            
        });
        const getTitle = ref('查看详情')
        const detailTitle = ref('测试')
        function handleSubmit(){
          closeModal();
  
        }
        function handleDownload (record) {
            const token:string = getToken() as string;
            console.log(token,'token字符串');
            const req = new XMLHttpRequest();
            req.open('GET', import.meta.env.VITE_PROXY_ADDRESS + '/file/downloadFileStream/'+ record.id, true);
            req.responseType = 'blob';
            req.setRequestHeader('X-Access-Token',token);
            req.onload = function() {
              const data = req.response;
              const blob = new Blob([data], { type: 'application/octet-stream' });
              console.log(blob,'文件对象')
              let dom = document.createElement('a')
              let url = window.URL.createObjectURL(blob)
              dom.href = url
              dom.download = decodeURI(record.realName)
              dom.style.display = 'none'
              document.body.appendChild(dom)
              dom.click()
              dom?.parentNode.removeChild(dom)
              window.URL.revokeObjectURL(url)
            };
            req.send();
        };
        // async function handleDownload(record){
        //   const token = getToken();
        //   let options = {
        //       method: 'get',
        //       // url:  import.meta.env.VITE_PROXY_ADDRESS + '/file/downloadFileStream/ '+ record.id,
        //       url:  import.meta.env.VITE_PROXY_ADDRESS + '/file/downloadFileByte/'+ record.id,
        //       responseType: 'blob',
        //       headers: { 'X-Access-Token': token },
        //   }
        //   axios(options).then(res=>{
        //     console.log(res,'00000');
        //      const blob = new Blob([res.data], { type: 'application/octet-stream' });
        //       console.log(blob,'文件对象')
        //       let dom = document.createElement('a')
        //       let url = window.URL.createObjectURL(blob)
        //       dom.href = url
        //       dom.download = decodeURI(record.realName)
        //       dom.style.display = 'none'
        //       document.body.appendChild(dom)
        //       dom.click()
        //       dom?.parentNode.removeChild(dom)
        //       window.URL.revokeObjectURL(url)

        //   })
        //   // let result = await getFileStrem({fileId:record.id});
        //   // if(result){
        //     //  const { data, headers } = res
        //       // const fileName = headers['content-disposition'].replace(/\w+;filename=(.*)/, '$1')
        //       // 此处当返回json文件时需要先对data进行JSON.stringify处理，其他类型文件不用做处理
        //       //const blob = new Blob([JSON.stringify(data)], ...)
        //       // const blob = new Blob([result], {type: headers['content-type']})


        //         // let str =  Buffer.from(result, 'binary');
        //         // let data = "";
        //         // let len = str.byteLength;
        //         // for (let i = 0; i < len; i++) {
        //         //   data += String.fromCharCode(str[i]);
        //         // }
        //         // console.log(data,'-----ddddddd--')
        //         // downloadByData(result,record.realName)
        //       // const blob = new Blob([result], { type: 'application/octet-stream' });

        //       // FileSaver.saveAs(blob, record.realName);

        //       // console.log(blob,'文件对象')
        //       // let dom = document.createElement('a')
        //       // let url = window.URL.createObjectURL(blob)
        //       // dom.href = url
        //       // dom.download = decodeURI(record.realName)
        //       // dom.style.display = 'none'
        //       // document.body.appendChild(dom)
        //       // dom.click()
        //       // dom?.parentNode.removeChild(dom)
        //       // window.URL.revokeObjectURL(url)

        //       // if (typeof window.navigator.msSaveBlob !== 'undefined') {
        //       //   window.navigator.msSaveBlob(new Blob([data]), fileName)
        //       // } else {
        //       //   let url = window.URL.createObjectURL(new Blob([data]))
        //       //   let link = document.createElement('a')
        //       //   link.style.display = 'none'
        //       //   link.href = url
        //       //   link.setAttribute('download', fileName)
        //       //   document.body.appendChild(link)
        //       //   link.click()
        //       //   document.body.removeChild(link) //下载完成移除元素
        //       //   window.URL.revokeObjectURL(url) //释放掉blob对象
        //       // }
        //   // } 
 
        //   // let url = import.meta.env.VITE_PROXY_ADDRESS + '/downloadFileStream/' + record.id;
        //   // downloadByUrl({url,target:'_self',fileName:record.realName});
        // }
        function handleButton(item){
          console.log(item)
          closeModal();
          go('/myannounce/'+ item.id);
      }
  
        return {
          closeModal,
          registerModal,
          getTitle,
          detailTitle,
          handleSubmit,
          state,
          handleButton,
          minHeight,
          fileList,
          handleDownload,
          baseDownLoadUrl
        };
      },
    });
</script>
<style lang="less" scoped>
  .detail-header{
    border-bottom: 1px solid #f0f0f0;
    .detail-item{
      margin-right: 15px;
    }
  }
  .detail-button{
    position: absolute;
    bottom: 5px;
    right: 14px;
    :deep(.ant-btn){
      margin:0 4px;
    }
  }
  .ant-modal{
    height:100%;
  }
  .file-list{
    .file-title{

    }
    .file-name{
      // color:@primary-color;
      margin-right:5px;
    }
  }
</style>
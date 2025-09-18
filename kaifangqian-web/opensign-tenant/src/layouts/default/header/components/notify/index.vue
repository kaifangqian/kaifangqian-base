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
    <div :class="prefixCls">
        <Badge :count="count"  show-zero :numberStyle="numberStyle" @click="handleNotice">
          <BellOutlined style="color:'#2b2b2b'"/>
        </Badge>
    </div>
    <NoticeDrawer @register="registerDrawer" @success="handleSuccess" @changeCount="changeCount" @route="handleMsgDetail"/>
    <MessageDetailModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
 
<script lang="ts">
  import type { VNode } from 'vue';
  import { computed, defineComponent, ref, h, onMounted } from 'vue';
  import { Popover, Tabs, Badge, Button } from 'ant-design-vue';
  import { BellOutlined } from '@ant-design/icons-vue';
  import { tabListData, ListItem } from './data';
  import { useDrawer } from '/@/components/Drawer';
  import NoticeList from './NoticeList.vue';
  import { useDesign } from '/@/hooks/web/useDesign';
  import { useMessage } from '/@/hooks/web/useMessage';
  import NoticeDrawer from './NoticeDrawer.vue';
  import { useUserStore } from '/@/store/modules/user';
  import { useGo } from '/@/hooks/web/usePage';
  import { getMyMessage } from '/@/api/message';
  import { useAppStore } from '/@/store/modules/app';
  import { useModal } from '/@/components/Modal';
  import { objectToQueryString } from '/@/utils'
  import MessageDetailModal from '/@/views/message/modal/MessageDetailModal.vue';
  import { listenerRouteChange } from '/@/logics/mitt/routeChange';
  import { REDIRECT_NAME } from '/@/router/constant'
  import {  getAppTokenByAuthToken } from '/@/api/sys/user';
  import { getToken } from '/@/utils/auth';

  export default defineComponent({
    components: { Popover, BellOutlined, Tabs, TabPane: Tabs.TabPane, Badge, NoticeList, NoticeDrawer,Button,MessageDetailModal },
    setup() {
      const { prefixCls } = useDesign('header-notify');
      const { createMessage,notification } = useMessage();
      const [registerModal, { openModal }] = useModal();
      const userStore = useUserStore();
      const go = useGo();
      const appStore = useAppStore();
      const storeUserInfo =  userStore.getUserInfo;
      const socketAppInfo = ref({
        appAddress:'',
        appCode:'',
        appId:'',
        departId:'',
        tenantId:'',
      })

      
      console.log(userStore.getToken,'app信息')

      const listData = ref(tabListData);
      const websocket = ref();
      const heartCheck = ref();
      const basiceCount = ref(0);
      const count = computed(() =>{
        return basiceCount.value
        // return appStore.getMsgCount;
      })

      listenerRouteChange((route) => {
        if (route.name === REDIRECT_NAME) return;
        console.log(route,'路由变哈了')
        initWebSocket()
      });


      // initWebSocket();
      // heartCheckFun();

      // const count = computed(() => {
      //   let count = 0;
      //   for (let i = 0; i < tabListData.length; i++) {
      //     count += tabListData[i].list.length;
      //   }
      //   return count;
      // });
      onMounted(()=>{
          fetch()
      })
      async function fetch(){
        let result = await getMyMessage({pageNo:1,pageSize:20,readFlag:0});
        if(result){
          basiceCount.value = result.total;
          appStore.setMsgCount( result.total);
        }
      }
      const [registerDrawer, { openDrawer }] = useDrawer();

      function changeCount(val){
        console.log('重新计数',val)
        basiceCount.value = val;
        appStore.setMsgCount(val);
        
      }

      function heartCheckFun(){
        var that = this;
        //心跳检测,每20s心跳一次
        heartCheck.value = {
          timeout: 2000,
          timeoutObj: null,
          serverTimeoutObj: null,
          reset: function(){
            clearTimeout(this.timeoutObj);
            //clearTimeout(this.serverTimeoutObj);
            return this;
          },
          start: function(){
            var self = this;
            this.timeoutObj = setTimeout(function(){
              //这里发送一个心跳，后端收到后，返回一个心跳消息，
              //onmessage拿到返回的心跳就说明连接正常
              that.websocketSend("HeartBeat");
              console.info("客户端发送心跳");
              self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
               that.websock.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
              }, self.timeout)
            }, this.timeout)
          }
        }
      }

      function initWebSocket() {
        const websocketInfo =  {
            url:location.origin
        };
        console.log(websocketInfo,'-----开始建立连接---')
        if(websocketInfo.url){
          // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
          var url = websocketInfo.url.replace("https://","wss://").replace("http://","ws://") +"/resrun-paas/websocket/"+storeUserInfo.id;
          // console.log(url);
          websocket.value = new WebSocket(url);
          websocket.value.onopen = websocketOnopen;
          websocket.value.onerror = websocketOnerror;
          websocket.value.onmessage = websocketOnmessage;
          websocket.value.onclose = websocketOnclose;

          console.log(websocket,'--websocket实例-')
        }
     
       
      }

      function websocketOnopen() {
        console.log("WebSocket连接成功");
      }
      function websocketOnerror() {
        console.log("WebSocket连接发生错误");
        //重新链接
        // initWebSocket()
      }
      function websocketOnmessage(e) {
        var data = eval("(" + e.data + ")"); //解析对象
        console.log('-------接收到socket消息-------',data)
        socketAppInfo.value = {
          ...data
        }
        console.log(socketAppInfo.value,'---应用信息--')
        let btns:VNode[] = [];
        data.buttons&&data.buttons.map(item=>{
          let button =  h(Button,{
              type:item.buttonStyle,
              size: 'small',
              style:'margin:0 3px',
              onClick: () => handleBtn(item)
            },
            item.buttonName)
          btns.push(button)
        })
        const key = `open${Date.now()}`;
        // const content =  h('span', {domProps:{ innerHTML: data?.mesContent }})
        const content =  h('span', null,convertHTMLToVNode( data?.mesContent) )
        notification.open({
          duration:5,
          message: data.mesTitle,
          description: () => {
            return  h('div', {
                      style:'cursor: pointer',
                      onClick: () => handleMsgDetail(data)
                    },[
                      content,
                    ])
            },
            btn: () =>
              // h(
              //   Button,
              //   {
              //     type: 'primary',
              //     size: 'small',
              //     onClick: () => notification.close(key),
              //   },
              // ),
              h('div',{
                  class:'notice-btn'
              },{default: () => btns }),
            key,
        }); 
        fetch();

      }
      function websocketOnclose(e) {
        console.log(e,'---------连接关闭-----')
        // initWebSocket()
      }

      function convertHTMLToVNode(htmlString) {
            // Regular expression to match style attributes
            const styleRegex = /style="([^"]*)"/g;

            // Find matches for style attributes
            const matches = htmlString.match(styleRegex);

            if (matches) {
              // Convert matches to style objects
              const styleObjects = matches.map(match => {
                const styleString = match.replace('style="', '').replace('"', '');
                const styles = styleString.split(';').reduce((acc, style) => {
                  const [property, value] = style.split(':').map(part => part.trim());
                  if (property && value) {
                    acc[property] = value;
                  }
                  return acc;
                }, {});
                return styles;
              });

              // Replace style attributes with Vue style objects
              styleObjects.forEach((styles, index) => {
                htmlString = htmlString.replace(matches[index], `v-bind:style="${JSON.stringify(styles)}"`);
              });
            }

            // Create a VNode from the HTML string
            return h('div', { innerHTML: htmlString });
          }
      async function handleBtn(item){
        console.log(item)
        let routerParams = JSON.parse(item.buttonPara);
        // go('/myannounce/'+ routerParams.infoId)
        if(!item.buttonRoute) return;
        if(item.buttonName == '详情' && item.buttonRoute !== '/myannounce/' ){
          openModal(true,{
            isUpdate:false,
            record:{
              id:routerParams.infoId
            }
          })
        }else{
          // let params = {
          //   tenantId:socketAppInfo.value.tenantId,
          //   departId:socketAppInfo.value.departId
          // }
          // let newToken = await getAppTokenByAuthToken(params);

          let token = getToken();
          let appAuthInfo = window.appInfo.sign_app_info;
          if(token){
            let appInfo = {
              token:token,
              appCode:socketAppInfo.value.appCode,
              appId:socketAppInfo.value.appId,
              departId:socketAppInfo.value.departId,
              redirect:socketAppInfo.value.appAddress + '/#' + item.buttonRoute
            }
            let paramsString = new URLSearchParams(appInfo as any).toString();
            let urlParams = objectToQueryString(routerParams);
            window.open(appAuthInfo.url + '/#/auth/login?directly=true&appId='+ socketAppInfo.value.appId + '&appCode='+ socketAppInfo.value.appCode + paramsString +'&' + urlParams)
            // window.open(socketAppInfo.value.appAddress + '/#' +  item.buttonRoute + '?'+ paramsString +'&' + urlParams,'_blank')
          }
        }
       
      }
      function handleMsgDetail(data){
        //  openModal(true,{
        //     isUpdate:false,
        //     record:{
        //       id:data.id
        //     }
        //   })
        // go('/message/my/'+ data.id) 
      }


      function onNoticeClick(record: ListItem) {
        createMessage.success('你点击了通知，ID=' + record.id);
        // 可以直接将其标记为已读（为标题添加删除线）,此处演示的代码会切换删除线状态
        record.titleDelete = !record.titleDelete;
      }

      async function handleNotice(){
        const appMessageInfo =  window?.appInfo.message_app_info;
        const token = getToken() as string;
        let appInfo = {
            token:token,
            appCode:appMessageInfo.appCode,
            appId:appMessageInfo.appId,
            departId:storeUserInfo.loginDepartId,
            redirect:  appMessageInfo.url
          }
          let paramsString = new URLSearchParams(appInfo).toString();
          window.open(appMessageInfo.url + '/#/message' + '?' + paramsString,'_blank')

        // openDrawer(true,{
        //   isUpdate:false,
        //   onChangeCount:changeCount
        // })
      }

      function handleSuccess(){

      }
      function handleMessageRoute(item){
        console.log('触发2');
        openModal(true,{
            isUpdate:false,
            record:{
              id:item.id
            }
        })
      }

      return {
        handleMsgDetail,
        registerDrawer,
        prefixCls,
        listData,
        count,
        onNoticeClick,
        handleSuccess,
        handleNotice,
        numberStyle: {},
        changeCount,
        registerModal,
        handleMessageRoute
      };
    },
  });
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-header-notify';

  .@{prefix-cls} {
    padding-top: 2px;

    &__overlay {
      max-width: 400px;
      min-width: 300px;
    }

    .ant-tabs-content {
      width: 350px;
    }

    .ant-badge {
      font-size: 22px;
      cursor: pointer;
      // color: #fff;

      .ant-badge-multiple-words {
        padding: 0 4px;
      }

      svg {
        width: 0.9em;
      }
    }
  }
</style>

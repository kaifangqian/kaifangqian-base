<template>
  <div class="unauthenticated-container">
    <img src="@/assets/images/warning-icon.png" alt="Warning Icon" class="warning-icon" />
    <p class="message">当前个人账号未认证</p>
    <a-button class="go-to-auth" type="primary" @click="toAuth">去认证</a-button>


  </div>
</template>

<script lang="ts"  setup>
  import { useRouter } from 'vue-router';
  import { defineComponent, h} from 'vue';
  import { handleAuth, handleUpdateAuth } from '/@/views/sys/user';
  import { useMessage } from '/@/hooks/web/useMessage';

  const { createMessage: msg, createConfirm } = useMessage();
  const props = defineProps({
    index:{
    	type: Number,
      default:0
    },
  });

  const router = useRouter();

  async function toAuth() {
    createConfirm({
      iconType: 'info',
      width: '600px',
      title: '开启安全可靠的电子签章',
      content: h('div', [
        // 第二部分：操作指引
        h('div', {
          style: {
            marginBottom: '16px'
          }
        }, [
          h('div', {
            style: {
              marginBottom: '8px',
              fontWeight: '500'
            }
          }, '请完成以下步骤：'),
          h('div', {
            style: {
              paddingLeft: '8px'
            }
          }, [
            h('div', '1. 点击【立即认证】'),
            h('div', '2. 在「云盾系统」完成个人实名认证')
          ])
        ]),
        
        // 第三部分：服务说明（优化样式）
        h('div', {
          style: {
            background: '#f0f8ff',
            border: '1px solid #d0e9ff',
            borderRadius: '4px',
            padding: '12px',
            fontSize: '13px',
            color: '#31708f',
            marginRight: '38px'
          }
        }, [
          h('div', {
            style: {
              display: 'flex',
              alignItems: 'flex-start',
              marginBottom: '6px'
            }
          }, [
            h('span', {
              innerHTML: '&#128274;', // 锁图标
              style: {
                marginRight: '8px',
                fontSize: '14px'
              }
            }),
            h('span', {
              style: {
                fontWeight: '600',
                fontSize: '14px'
              }
            }, '安全保障')
          ]),
          h('div', {
            style: {
              lineHeight: '1.6',
              paddingLeft: '24px'
            }
          }, '云盾系统为电子签章提供核心保障服务，包括实名认证、权威CA证书签发及签署意愿验证，确保签署过程安全合规。')
        ])
      ]),
      okText: '立即认证',
      async onOk() {
         handleAuth(2, `${window.location.origin}/#/user/centerInfo?index=${props.index}`);
      },
    });
   
  }
</script>

<style scoped>
.unauthenticated-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  margin-top: 160px;
}

.warning-icon {
  width: 60px;
  height: 60px;
  margin-bottom: 20px;
}

.message {
  font-size: 20px;
  color: #000;
  margin-bottom: 30px;
}

.go-to-auth { 
  /* margin-top: 20px; */
  width: 200px;
  height: 40px;
  background: linear-gradient(to right, #1890ff, #40a9ff);
  border-color: transparent;
  /* font-size: 16px; */
  text-align: center;
  justify-content: center;

}

</style>
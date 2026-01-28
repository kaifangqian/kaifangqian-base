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
  <div class="sign-control-container">
    <p class="sign-control-title">
      <span>签署控件</span>
      <a-tooltip>
        <template #title>{{ signControlTip }}</template>
        <Icon icon="ant-design:question-circle-outlined" style="margin-left: 10px" />
      </a-tooltip>
    </p>
    <div class="signer-control">
      <draggable
        :class="{ 'disabled-control': disabledSignControls }"
        :list="loadControlList()"
        ghost-class="ghost"
        handle=".control-move"
        filter=".forbid"
        item-key="icon"
        :force-fallback="true"
        chosen-class="chosenClass"
        drag-class="drag-control"
        animation="300"
        @end="controlsDragOver"
        @start="controlsDragStart"
        :group="group"
        :fallback-class="false"
        :fallback-on-body="true"
        :touch-start-threshold="50"
        :fallback-tolerance="50"
        :clone="clone"
        :sort="false"
        :draggable="disabledSignControls"
      >
        <template #item="{ element }">
          <div class="control-item">
            <template v-if="!(element.type == ControlType.ChopStamp && !element.chopStampUse)">
              <div
                :class="['control-li control-move']"
                :style="{ borderColor: '#127FD2', color: '#127FD2' }"
              >
                <div>
                  <span>
                    <SvgIcon :name="element.icon" :size="18"></SvgIcon>
                  </span>
                  <span class="name">{{ element.name }}</span>
                </div>
                <span class="control-count">{{ element.controlCount }}</span>
              </div>
            </template>
            <template v-else>
              <a-tooltip placement="bottom">
                <template #title>
                  <span>当前软件授权不支持骑缝章，如需使用，请联系系统管理员升级软件授权</span>
                </template>
                <div :class="['control-li control-disabled']">
                  <div>
                    <span>
                      <SvgIcon :name="element.icon" :size="18"></SvgIcon>
                    </span>
                    <span class="name">{{ element.name }}</span>
                  </div>
                  <span class="control-count">{{ element.controlCount }}</span>
                </div>
              </a-tooltip>
            </template>
            <a-popover title="签署控件位置">
              <template #content>
                <div v-for="(doc, docIndex) in documentList" :key="docIndex">
                  <div v-if="loadIsShowPopControl(element, doc)">
                    <p
                      class="pos-doc-header"
                      style="display: flex; align-items: center; justify-content: space-between;font-weight: 600;"
                    >
                      <span>{{ doc.documentName }}</span>
                      <span>{{ doc[element.controlType + 'docControlCount'] + ' 处' }}</span>
                    </p>
                    <ul>
                      <li
                        v-for="(el, elIndex) in [...doc[element.controlType + 'docPopControls']].sort((a, b) => a.position.page - b.position.page)"
                        :key="elIndex"
                      >
                        <span>第{{ el.position.page + 1 }}页</span>
                        <a-button type="link" @click="handleControlPos(doc, el)">定位</a-button>
                      </li>
                    </ul>
                  </div>
                </div>
              </template>
              <SvgIcon
                name="location"
                :size="24"
                class="control-pos"
                v-show="element.controlCount"
              ></SvgIcon>
            </a-popover>
          </div>
        </template>
      </draggable>
    </div>
    <div v-if="showSignControlTip" class="sign-control-tip-below">
      <div class="tip-content">
        <div class="tip-message">
          <Icon icon="ant-design:info-circle-outlined" class="tip-icon" />
          <span class="tip-text">{{ signControlTip }}</span>
        </div>
        <div class="tip-actions">
          <a-button type="primary" size="small" @click="handleAcknowledge" class="acknowledge-btn">
            已知晓，不再提示
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import { ref, defineComponent, unref, onMounted, watch } from 'vue';
  import draggable from 'vuedraggable';
  import { Icon, SvgIcon } from '/@/components/Icon';
  import { controlListSource, CanvasZoom, ControlType } from './data/ControlData';
  import { moveRange, findMinImageSize } from './data/ControlerMoveRange';
  import { cloneDeep } from 'lodash-es';
  import { useUserStore } from '/@/store/modules/user';
  import { getSignControlType } from '/@/api/contract';
  import { useRouter } from 'vue-router';
  import { getColor } from './data/ReceiveColor';
import { json } from 'stream/consumers';

  export default defineComponent({
    name: 'SignControls',
    props: {
      user: {
        type: Object,
      },
      documentList: {
        type: Object,
      },
      signerList: {
        type: Object,
      },
      nowDocument: {
        type: Object,
      },
      controlChangeFlag: {
        type: [String, Number],
      },
      defaultHasControl: {
        type: Boolean,
      },
      chopStampUseFlag: {
        type: Boolean,
      },
    },
    components: {
      Icon,
      SvgIcon,
      draggable,
    },
    setup(props, { emit }) {
      watch(
        () => props.nowDocument,
        (doc) => {
          if (doc) {
            // let signControls = doc.activeControl.filter(v=>v.controlType==='seal' || v.controlType==='chop-stamp' || v.controlType==='signature');
            if (props.defaultHasControl && props.controlChangeFlag == 'necessary_no_add') {
              disabledSignControls.value = true;
              signControlTip.value = '发起方已为您预设签署位置，不可添加，请直接完成签署';
            } else {
              disabledSignControls.value = false;
              signControlTip.value = '可拖拽签署控件到文档正文区域，自由放置到所需位置';
            }
          }
        },
        { deep: true },
      );

      // 添加控制提示显示的响应式变量
      const showSignControlTip = ref(true);

      showSignControlTip.value = (localStorage.getItem("showSignControlTip") == 'false') ? false : true;
      
  
      const signControlsList: any = ref([]);
      const signControlInfo = ref({
        type: '',
      });
      const disabledSignControls = ref(false);
      const signControlTip = ref('可拖拽签署控件到文档正文区域，自由放置到所需位置');
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const signRuId = route.query?.signRuId;

      const group = ref({
        name: 'itxst',
        put: false, //允许拖入
        pull: 'clone', //允许拖出
        /* pull:(e:any)=>{
					return false;
				} */
      });

      const userStore = useUserStore();
      const tenantInfo = userStore.getTenantInfo;

      // 添加处理"已知晓，不再提示"按钮点击的函数
      function handleAcknowledge() {
        showSignControlTip.value = false;
        localStorage.setItem("showSignControlTip", String(showSignControlTip.value));
      }
      function clone(origin: any) {
        console.log(origin, '源----');
        const data = JSON.parse(JSON.stringify(origin));
        delete data.popControls;
        //判断是否选中可参与方
        if (props.user) {
          data.user.index = props.user.index;
          data.user.signerName = props.user.signerName;
          data.user.signerId = props.user.signerId;
          data.user.signerType = props.user.signerType;
          data.user.tenantId = props.user.tenantId;
        }
        data.controlClick = true;
        // data.user.userName = signerIndex.value;
        data.uid = parseInt(
          new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
        ).toString(16);
        // if (data.type == 'sign-date') {
        //     data.format = 'YYYY-MM-DD';
        //   }
        console.log(origin, '源----',data);
        return data;
      }
      onMounted(() => {
        loadSignControlType();
      });
      async function loadSignControlType() {
        let result = await getSignControlType({ ruId: signRuId });
        if (result) {
          signControlInfo.value = result;
          emit('signControlType', result);
        }
      }
      //拖拽开始
      function controlsDragStart() {
        console.log(props.nowDocument, '-开始拖拽------------');
      }
      //拖拽结束
      function controlsDragOver(e: any, element?: any) {
        //这里目标 props.nowDocument?.activeControl 控件数组会自定push当前拖拽空间 无需手动添加再
        // 故可直接从 props.nowDocument?.activeControl[e.newIndex] 取当前 控件
        const moveTarget = props.nowDocument?.activeControl[e.newIndex];
        if (moveTarget) {
          const opt: any = {
            x: e.originalEvent.offsetX - moveTarget.size.width / 2,
            y: e.originalEvent.offsetY - moveTarget.size.height / 2,
            pageSize: props.nowDocument?.pageSize,
            size: moveTarget.size,
            currentPage: 0,
            allHeight: props.nowDocument.imageAllHeight,
            //所有页面最大的宽度
            maxWidth: props.nowDocument.maxWidth,
          };
          moveRange(opt, props.nowDocument.targets, moveTarget.isBatch);
          moveTarget.id =
            moveTarget.id ||
            parseInt(
              new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
            ).toString(16);

          if (moveTarget.controlType == 'chop-stamp') {
            const elementTarget = props.nowDocument.targets[opt.currentPage];
            const elementOffsetWidth = (props.nowDocument.maxWidth - elementTarget.width) / 2;
            opt.x = elementOffsetWidth + elementTarget.width - moveTarget.size.width;
            moveTarget.isBatch = true;
          } else {
            moveTarget.isBatch = moveTarget.isBatch || false;
          }
          moveTarget.id =
            moveTarget.id ||
            parseInt(
              new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
            ).toString(16);
          // 默认添加签署控件 propertyVoList
          if (moveTarget.controlType == 'chop-stamp') {
            moveTarget.isBatch = true;
            moveTarget.propertyVoList = [
              {
                controlId: moveTarget.id,
                id:
                  'relation_doc_id' +
                  parseInt(
                    new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
                  ).toString(16),
                propertyType: 'relation_doc',
                propertyValue: props.nowDocument.signRuDocId,
              },
              {
                controlId: moveTarget.id,
                id:
                  'page_config_id' +
                  parseInt(
                    new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
                  ).toString(16),
                propertyType: 'page_config',
                propertyValue: 'all',
              },
            ];
            let pages: number[] = [];
            let docKeys: string[] = [];
            props.nowDocument.images.map((v) => {
              pages.push(v.page);
            });
            docKeys = moveTarget.propertyVoList
              .filter((item) => item.propertyType === 'relation_doc')
              .map((item) => item.propertyValue);
            const minTarget = findMinImageSize(
              props.documentList,
              docKeys,
              Array.from(new Set(pages)),
            );
            moveTarget.minTarget = minTarget;
          } else {
            moveTarget.isBatch = moveTarget.isBatch || false;
            // 默认添加签署控件 propertyVoList
            moveTarget.propertyVoList = [
              {
                controlId: moveTarget.id,
                id:
                  'relation_doc_id' +
                  parseInt(
                    new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
                  ).toString(16),
                propertyType: 'relation_doc',
                propertyValue: props.nowDocument.signRuDocId,
              },
              {
                controlId: moveTarget.id,
                id:
                  'page_config_id' +
                  parseInt(
                    new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
                  ).toString(16),
                propertyType: 'page_config',
                propertyValue: 'custom',
              },
              {
                controlId: moveTarget.id,
                id:
                  'page_config_id' +
                  parseInt(
                    new Date().getMilliseconds() + '' + Math.ceil(Math.random() * 100000),
                  ).toString(16),
                propertyType: 'custom',
                propertyValue: opt.currentPage + 1 + '',
              },
            ];
          }
          moveTarget.position.left = opt.x;
          moveTarget.position.top = opt.y;
          moveTarget.position.page = opt.currentPage;
          moveTarget.controlClick = true;
          moveTarget.delete = true;
          moveTarget.signRuDocId = props.nowDocument.signRuDocId;
          moveTarget.controlType = moveTarget.type;
          moveTarget.originType = moveTarget.originType;
          moveTarget.colorIndex = 7;
          moveTarget.signature = '';
          // console.log(JSON.stringify(moveTarget), '控件位置-------')
          
          
          console.log(moveTarget, '控件位置');
          emit('dragOver', e, moveTarget);
          //controlMove.elementMove = moveTarget;
        }
      }
      //加载控件
      function loadControlList() {
        const newControlList = ref<any>([]);
        let types: string[] = [];
        if (signControlInfo.value?.type == '2') {
          types = ['seal', 'sign-date', 'chop-stamp'];
        } else {
          types = ['signature', 'sign-date'];
        }
        let docControls =
          props?.documentList && props.documentList.flatMap((item) => item.activeControl);
        controlListSource.map((item: any) => {
          // signerId :签署方id
          types.forEach((v) => {
            if (item.type == v) {
              if (v == ControlType.ChopStamp) {
                item.chopStampUse = props.chopStampUseFlag;
              }
              props?.documentList &&
                props.documentList.map((u) => {
                  let currentDocControls = u.activeControl;
                  let docMatchControls = currentDocControls.filter(
                    (m: any) => m.controlType == v && u.signRuDocId == m.signRuDocId,
                  );
                  u[v + 'docControlCount'] = docMatchControls.length;
                  u[v + 'docPopControls'] = docMatchControls;
                  // console.log( u,'匹配到的文档控件')
                  //控件类型
                });
              let matchControls = docControls.filter((m: any) => m.controlType == v);
              item.controlCount = matchControls.length;
              item.popControls = matchControls;
              item.controlType = v;
              // //控件类型
              item.originType = 3;
              newControlList.value.push(cloneDeep(item));
            }
          });
        });
        // console.log(newControlList.value,'newControlList.value----')
        return newControlList.value;
      }

      //判断是否显示控件文档
      function loadIsShowPopControl(el, doc) {
        let docHasControl = el.popControls.filter((v) => v.signRuDocId == doc.signRuDocId);
        return docHasControl.length ? true : false;
      }

      // 定位空间
      function handleControlPos(doc, control) {
        emit('scroll', doc, control);
      }
      return {
        signControlsList,
        group,
        clone,
        controlsDragOver,
        disabledSignControls,
        controlsDragStart,
        loadIsShowPopControl,
        handleControlPos,
        loadControlList,
        getColor,
        signControlTip,
        ControlType,
        showSignControlTip,
        handleAcknowledge,
      };
    },
  });
</script>

<style lang="less" scoped>
  .sign-control-container {
    border-top: 1px solid #e1e8ed;
    padding: 20px 0;
  }
  .disabled-control {
    .control-move {
      cursor: no-drop;
    }
  }
  .sign-control-title {
    font-size: 18px;
    font-weight: 550;
  }
  .control-move {
    -webkit-user-select: none; /* Safari 和 Chrome */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* Internet Explorer */
    user-select: none; /* 标准语法 */
    cursor: grab;
  }

  .sign-control-tip-below {
    background: linear-gradient(135deg, #ffffff, #f9f9f9);
    border: 1px solid #e1e8ed;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12), 0 2px 4px rgba(0, 0, 0, 0.08);
    padding: 16px 20px;
    margin-top: 20px;
    position: relative;
    overflow: hidden;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 4px;
      height: 100%;
      background: linear-gradient(to bottom, #1890ff, #40a9ff);
    }
    
    .tip-content {
      display: flex;
      flex-direction: column;
      gap: 12px;
      
      .tip-message {
        display: flex;
        align-items: flex-start;
        gap: 10px;
        
        .tip-icon {
          color: #1890ff;
          font-size: 18px;
          flex-shrink: 0;
          margin-top: 2px;
        }
        
        .tip-text {
          font-size: 14px;
          color: #333;
          line-height: 1.5;
          flex: 1;
        }
      }
      
      .tip-actions {
        display: flex;
        justify-content: center;
        margin-top: 8px;
        
        .acknowledge-btn {
          border-radius: 16px;
          padding: 0 20px;
          height: 32px;
          font-size: 13px;
          box-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
          transition: all 0.3s ease;
          
          &:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 8px rgba(24, 144, 255, 0.3);
          }
        }
      }
    }
  }

  /* 响应式处理 */
  @media (max-width: 768px) {
    .sign-control-tip-below {
      padding: 14px 16px;
      
      .tip-content {
        .tip-message {
          .tip-text {
            font-size: 13px;
          }
        }
        
        .tip-actions {
          .acknowledge-btn {
            width: 100%;
            max-width: 200px;
          }
        }
      }
    }
  }
</style>

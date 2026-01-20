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
  <div class="doc-control-container">
    <ul>
      <li v-for="(item, index) in signerList" :key="index" >
        <div v-if="item.signerType == 1 && item.senderList && item.senderList.length>0">
          <div class="signer-name">
            <span class="sender-line"></span>
            <span>发起方：{{ item.signerName }}</span>
          </div>
          <div
            v-for="(sendItem, sendIndex) in item.senderList"
            :key="sendIndex"
            class="signer-control-info"
          >
            <div class="signer-head">
              <a-badge status="default" />
              <span style="font-weight: 400;">{{
                sendItem.senderName +
                '  —  ' +
                '[' +
                (sendItem.senderSignType == 1 ? '自动盖章' : sendItem.senderUserName) +
                ']'
              }}</span>
            </div>
            <div class="signer-control">
              <draggable
                :list="
                  loadControlList(sendItem.senderType, {
                    id: sendItem.id,
                    signerType: 1,
                    signerId: sendItem.id,
                  }, {...sendItem,signerType:1,startName:item.signerName,nodeName:sendItem.senderName})
                "
                ghost-class="ghost"
                handle=".control-move"
                filter=".forbid"
                item-key="icon"
                :force-fallback="true"
                chosen-class="chosenClass"
                drag-class="drag-control"
                animation="300"
                @end="(e)=>controlsDragOver(e,{colorIndex:sendIndex})"
                @start="controlsDragStart"
                :group="group"
                :fallback-class="false"
                :fallback-on-body="true"
                :touch-start-threshold="50"
                :fallback-tolerance="50"
                :clone="clone"
                :sort="false"
              >
                <template #item="{ element }">
                  <div class="control-item">
                    <template v-if="!(element.type == ControlType.ChopStamp && !element.chopStampUse)">
                      <div
                        :class="['control-li control-move']"
                        :style="['borderColor:' + getColor(sendIndex, 'background'),
                          'color:' + getColor(sendIndex, 'background'),]"
                          >
                        <div>
                          <span>
                            <SvgIcon :name="element.icon" :size="18"
                              :color="getColor(sendIndex, 'background')"></SvgIcon>
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
                        <div
                          :class="['control-li control-disabled']">
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
                    
                    <a-popover title="文档控件">
                      <template #content>
                        <div v-for="(doc, docIndex) in documentList" :key="docIndex">
                          <div v-if="loadIsShowPopControl(element, doc)">
                            <p
                              class="pos-doc-header"
                              style="
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                              "
                            >
                              <span>{{ doc.documentName }}</span>
                              <span>{{
                                loadPopControlCount(
                                  doc[element.controlType + 'docPopControls'],
                                  element,
                                ) + '处'
                              }}</span>
                            </p>
                            <ul>
                              <li
                                v-for="(el, elIndex) in doc[element.controlType + 'docPopControls']"
                                :key="elIndex"
                              >
                                <div v-if="el.signerId == element.signerId">
                                  <span>第{{ el.position.page + 1 }}页</span>
                                  <a-button type="link" @click="handleControlPos(doc, el)"
                                    >定位</a-button
                                  >
                                </div>
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
          </div>
        </div>
        <div v-if="item.signerType == 2">
          <div class="signer-name">
            <span class="sender-line" style="background-color: #e48523"></span>
            <span>接收方{{ (hasSenderSigner ? (index == '0' ? index + 1 : index) : (index + 1))}}：{{ item.signerName }}</span>
          </div>
          <div class="signer-control">
            <draggable
              :list="loadControlList('sign', { ...item, signerId: item.id }, {...item,receiverOrder:index,signerType:2,receiveName:item.signerName})"
              ghost-class="ghost"
              handle=".control-move"
              filter=".forbid"
              item-key="icon"
              :force-fallback="true"
              chosen-class="chosenClass"
              animation="300"
              drag-class="drag-control"
              @end="(e)=>controlsDragOver(e,{colorIndex:hasSenderSigner?Number(senderLength + index) - 1:Number(senderLength + index)})"
              @start="controlsDragStart"
              :group="group"
              :fallback-class="false"
              :fallback-on-body="true"
              :touch-start-threshold="50"
              :fallback-tolerance="50"
              :clone="clone"
              :sort="false"
            >
              <template #item="{ element }">
                <div class="control-item">
                  <div
                    :class="['control-li control-move']"
                    :style="[
                      'borderColor:' + getColor(hasSenderSigner?Number(senderLength + index) - 1:Number(senderLength + index), 'background'),
                      'color:' + getColor(hasSenderSigner?Number(senderLength + index) - 1:Number(senderLength + index), 'background'),
                    ]"
                  >
                    <div>
                      <span>
                        <SvgIcon :name="element.icon" :size="18"></SvgIcon>
                      </span>
                      <span class="name">{{ element.name }}</span>
                    </div>
                    <span class="control-count">{{ element.controlCount }}</span>
                  </div>
                  <a-popover title="文档控件">
                    <template #content>
                      <div v-for="(doc, docIndex) in documentList" :key="docIndex">
                        <div v-if="loadIsShowPopControl(element, doc)">
                          <p
                            class="pos-doc-header"
                            style="
                              display: flex;
                              align-items: center;
                              justify-content: space-between;
                            "
                          >
                            <span>{{ doc.documentName }}</span>
                            <span>{{
                              loadPopControlCount(
                                doc[element.controlType + 'docPopControls'],
                                element,
                              ) + '处'
                            }}</span>
                          </p>
                          <ul>
                            <li
                              v-for="(el, elIndex) in doc[element.controlType + 'docPopControls']"
                              :key="elIndex"
                            >
                              <div v-if="el.signerId == element.signerId">
                                <span>第{{ el.position.page + 1 }}页</span>
                                <a-button type="link" @click="handleControlPos(doc, el)"
                                  >定位</a-button
                                >
                              </div>
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
        </div>
        <div v-if="item.signerType == 3">
          <div class="signer-name">
            <span class="sender-line" style="background-color: #48b931"></span>
            <span>接收方{{ (hasSenderSigner ? (index == '0' ? index + 1 : index) : (index + 1)) }}：{{ item.signerName }}</span>
          </div>
          <div
            v-for="(sendItem, sendIndex) in item.senderList"
            :key="sendIndex"
            class="signer-control-info"
          >
            <div class="signer-head">
              <a-badge status="default" />
              <span style="font-weight: 400;">{{
                (sendItem.senderType == 1 ? '经办人签字' : '组织签章') +
                '  —  ' +
                '[' +
                sendItem.senderName  +
                ']'
              }}</span>
            </div>
            <div class="signer-control">
              <draggable
                :list="
                  loadControlList(sendItem.senderType, {
                    id: sendItem.id,
                    signerType: 1,
                    signerId: sendItem.id,
                  }, {...sendItem,receiverOrder:index,receiveName:item.signerName,nodeName: sendItem.senderName,signerType:3})
                "
                ghost-class="ghost"
                handle=".control-move"
                filter=".forbid"
                item-key="icon"
                :force-fallback="true"
                chosen-class="chosenClass"
                drag-class="drag-control"
                animation="300"
                @end="(e)=>controlsDragOver(e,{colorIndex:sendItem.colorIndex})"
                @start="controlsDragStart"
                :group="group"
                :fallback-class="false"
                :fallback-on-body="true"
                :touch-start-threshold="50"
                :fallback-tolerance="50"
                :clone="clone"
                :sort="false"
              >
                <template #item="{ element }">
                  <div class="control-item">
                    <template v-if="!(element.type == ControlType.ChopStamp && !element.chopStampUse)">
                      <div :class="['control-li control-move']"
                        :style="['borderColor:' + getColor(sendItem.colorIndex, 'background'),
                          'color:' + getColor(sendItem.colorIndex, 'background')]">
                        <div>
                          <span>
                            <SvgIcon :name="element.icon" :size="18"
                              :color="getColor(sendItem.colorIndex, 'background')"></SvgIcon>
                          </span>
                          <span class="name">{{ element.name }}</span>
                        </div>
                        <span class="control-count">{{ element.controlCount }}</span>
                      </div>
                    </template>
                    <template v-else>
                      <div :class="['control-li control-disabled']">
                        <div>
                          <span>
                            <SvgIcon :name="element.icon" :size="18"></SvgIcon>
                          </span>
                          <span class="name">{{ element.name }}</span>
                        </div>
                        <span class="control-count">{{ element.controlCount }}</span>
                      </div>
                    </template>
                    
                    <a-popover title="文档控件">
                      <template #content>
                        <div v-for="(doc, docIndex) in documentList" :key="docIndex">
                          <div v-if="loadIsShowPopControl(element, doc)">
                            <p
                              class="pos-doc-header"
                              style="
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                              "
                            >
                              <span>{{ doc.documentName }}</span>
                              <span>{{
                                loadPopControlCount(
                                  doc[element.controlType + 'docPopControls'],
                                  element,
                                ) + '处'
                              }}</span>
                            </p>
                            <ul>
                              <li
                                v-for="(el, elIndex) in doc[element.controlType + 'docPopControls']"
                                :key="elIndex"
                              >
                                <div v-if="el.signerId == element.signerId">
                                  <span>第{{ el.position.page + 1 }}页</span>
                                  <a-button type="link" @click="handleControlPos(doc, el)"
                                    >定位</a-button
                                  >
                                </div>
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
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent, onMounted, watch } from 'vue';
import draggable from 'vuedraggable';
import { Icon, SvgIcon } from '/@/components/Icon';
import { controlListSource, CanvasZoom,ControlType } from './data/ControlData';
import { getColor } from './data/ReceiveColor';
import { moveRange, findMinImageSize } from './data/ControlerMoveRange';
import { cloneDeep } from 'lodash-es';
import { loadControlIcon } from '../typeToName';

export default defineComponent({
  name: 'Controllers',
  components: {
    draggable,
    Icon,
    SvgIcon,
  },
  props: {
    documentList: {
      type: Object,
    },
    signerList: {
      type: Object,
    },
    nowDocument: {
      type: Object,
    },
    user: {
      type: Object,
    },
    originType: {
      type: Number,
    },
    chopStampUseFlag:{
      type: Boolean
    }
  },
  setup(props, { emit }) {
    const newControlList = ref([]);

    const ReceiveColorItem: any = ref(null);
    const hasSenderSigner = ref(false);
    const senderLength = ref(0);

    const group = ref({
      name: 'itxst',
      put: false, //允许拖入
      pull: 'clone', //允许拖出
      /* pull:(e:any)=>{
					return false;
				} */
    });

    //设置默认控件
    const defaultControlList = loadControlList();
    const sealControlList = loadControlList('seal');

    onMounted(() => {
    });
    watch(
      () => props.documentList,
      (newValues) => {
      },
    );
    watch(
      () => props.signerList,
      (newValues) => {
        if (newValues.length) {
          const res = props.signerList.filter((item) => item.signerType == 1);
          if (res && res.length > 0) {
            senderLength.value = res[0].senderList.length;
          }
        }
      },
    );
    //加载对应参与人控件
    function loadControlList(type?: string | number, signer?: any, signerInfo?: any) {
      const newControlList = ref<any>([]);
      let types: string[] = [];
      if (type == 'seal' || type == '4') {
        types = ['seal', 'sign-date', 'chop-stamp'];
      } else {
        types = ['signature', 'sign-date'];
      }
      let controls =
        props?.documentList && props.documentList.flatMap((item) => item.activeControl);
      // console.log(controls,'控件-----亲属人')
      controlListSource.map((item: any) => {
        // signerId :签署方id
        types.forEach((v) => {
          if (item.type == v) {
            if(v == ControlType.ChopStamp){
              item.chopStampUse = props.chopStampUseFlag;
            }
            props?.documentList &&
              props.documentList.map((u) => {
                let currentDocControls = u.activeControl;
                let docMatchControls = currentDocControls.filter(
                  (m: any) => m.controlType == v && u.signRuDocId == m.signRuDocId,
                );
                // if(v=='signature'){
                //   console.log(currentDocControls,v,u,docMatchControls,'匹配类型文档空间--=======================' )
                // }
                u[v + 'docControlCount'] = docMatchControls.length;
                u[v + 'docPopControls'] = docMatchControls;
                // console.log( u,'匹配到的文档控件')
                //控件类型
              });
            let matchControls = controls.filter(
              (m: any) => m.controlType == v && m.signerId == signer?.signerId,
            );
            item.controlCount = matchControls.length;
            item.popControls = matchControls;
            item.controlType = v;
            item.originType = 2;
            if (signer) {
              item.signerId = signer.id;
              item.signerType = signer.signerType;
            }
            //计算控件颜色序号
            props.signerList&&props.signerList.map((v, index) => {
              if (v.signerType == 1) {
                hasSenderSigner.value = true;
                v.senderList.forEach((m, mIndex) => {
                  if (m.id == item.signerId) {
                    // item.colorIndex = m.senderOrder-1;
                    item.colorIndex = mIndex;
                  }
                });
              }
              if (v.signerType == 2) {
                const temTes = props.signerList && props.signerList.filter((item) => item.signerType == 1);
                if (temTes && temTes.length > 0) {
                  let senderLength = temTes[0].senderList.length;
                  if (v.id == item.signerId) {
                    item.colorIndex = senderLength + index - 1;
                  }
                }

                if(v.signerType == 3){
                      const temRes = props.signerList.value.filter(item=>item.signerType==1)
                      let  senderLength = 0;
                      if(temRes && temRes.length>0){
                          senderLength = temRes[0].senderList.length;
                      }
                      let  receivePersonalLength = props.signerList.value.filter(item=>item.signerType==2).length;
                      v.senderList.forEach((m,mIndex)=>{
                          m.colorIndex = senderLength + receivePersonalLength+ mIndex;
                          if(m.id == item.signerId){
                            item.colorIndex = m.colorIndex
                          }
                      })
                  }
              }
            });
            item.signerInfo = {
              ...signerInfo,
            }; 
            newControlList.value.push(cloneDeep(item));
          }
        });
      });
      return newControlList.value;
    }
    // 计算pop卡片当前签署人控件数量
    function loadPopControlCount(list, el) {
      if (list.length && el) {
        return list.filter((v) => v.signerId == el.signerId).length;
      } else {
        return 0;
      }
    }
    function clone(origin: any) {
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
      return data;
    }
    //拖拽开始
    function controlsDragStart() {
    }
    //拖拽结束
    function controlsDragOver(e: any,element?:any) {
      //这里目标 props.nowDocument?.activeControl 空间数组会自定push当前拖拽空间 无需手动添加再 故可直接从 props.nowDocument?.activeControl[e.newIndex] 取当前空间
      const moveTarget = props.nowDocument?.activeControl[e.newIndex];
      const container = document.getElementsByClassName('control-item-box')[0];
      if (moveTarget) {
        moveTarget.colorIndex = element.colorIndex;
        const opt: any = {
          x: e.originalEvent.offsetX - moveTarget.size.width / 2,
          y: e.originalEvent.offsetY - moveTarget.size.height / 2,
          pageSize: props.nowDocument?.pageSize,
          size: moveTarget.size,
          currentPage: 0,
          allHeight:props.nowDocument.imageAllHeight,
          //所有页面最大的宽度
          maxWidth: props.nowDocument.maxWidth,
        };
        moveRange(opt, props.nowDocument.targets, moveTarget.isBatch);
        if (moveTarget.controlType == 'chop-stamp') {
          const elementTarget = props.nowDocument.targets[opt.currentPage];
          const elementOffsetWidth = ( props.nowDocument.maxWidth - elementTarget.width) / 2;
         
          opt.x = (elementOffsetWidth + elementTarget.width) -  moveTarget.size.width;
          moveTarget.isBatch = true;
        } else {
          moveTarget.isBatch = moveTarget.isBatch || false;
        }
        moveTarget.position.left = opt.x;
        moveTarget.position.top = opt.y;
        moveTarget.position.page = opt.currentPage;
        moveTarget.controlClick = true;
        moveTarget.showPopover = false;
        moveTarget.delete = true;
        moveTarget.signRuDocId = props.nowDocument.signRuDocId;
        // moveTarget.signRuDocId = props.nowDocument.controlDocId;
        moveTarget.controlType = moveTarget.type;
        moveTarget.originType = moveTarget.originType || 3;
        moveTarget.id = moveTarget.id || parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
        // 默认添加签署控件 propertyVoList
        if (moveTarget.controlType == 'chop-stamp') {
            moveTarget.isBatch = true;
            moveTarget.propertyVoList = [
                {
                    controlId: moveTarget.id,
                    id:'relation_doc_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"relation_doc",
                    propertyValue:props.nowDocument.signRuDocId,
                },
                {
                    controlId: moveTarget.id,
                    id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"page_config",
                    propertyValue:'all',
                },
                
            ];
            let pages:number[] = []
            let docKeys:string[] = []
            props.nowDocument.images.map(v=>{
              pages.push(v.page)
            })
            docKeys =  moveTarget.propertyVoList.filter(item => item.propertyType === "relation_doc").map(item => item.propertyValue);
            const minTarget  = findMinImageSize(props.documentList, docKeys, Array.from(new Set(pages)))
            moveTarget.minTarget = minTarget;
        } else {
            moveTarget.isBatch = moveTarget.isBatch || false;
                // 默认添加签署控件 propertyVoList
            moveTarget.propertyVoList = [
                {
                    controlId: moveTarget.id,
                    id:'relation_doc_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"relation_doc",
                    propertyValue:props.nowDocument.signRuDocId,
                },
                {
                    controlId: moveTarget.id,
                    id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"page_config",
                    propertyValue:'custom',
                },
                {
                    controlId: moveTarget.id,
                    id:'page_config_id' +  parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16),
                    propertyType:"custom",
                    propertyValue:opt.currentPage+1+'',
                }
            ];
        }
       
        emit('dragOver', e, moveTarget);
        //controlMove.elementMove = moveTarget;
      }
    }
    // 定位空间
    function handleControlPos(doc, control) {
      emit('scroll', doc, control);
    }
    //判断是否显示控件文档
    function loadIsShowPopControl(el, doc) {
      let docHasControl = el.popControls.filter((v) => v.signRuDocId == doc.signRuDocId);
      return docHasControl.length ? true : false;
    }
    return {
      newControlList,
      controlsDragOver,
      controlsDragStart,
      clone,
      loadPopControlCount,
      group,
      loadControlIcon,
      loadControlList,
      defaultControlList,
      sealControlList,
      handleControlPos,
      loadIsShowPopControl,
      ReceiveColorItem,
      getColor,
      senderLength,
      hasSenderSigner,
      ControlType
    };
  },
});
</script>

<style lang="less" scoped>
.control-move {
  -webkit-user-select: none; /* Safari 和 Chrome */
  -moz-user-select: none; /* Firefox */
  -ms-user-select: none; /* Internet Explorer */
  user-select: none; /* 标准语法 */
  cursor: grab;
}
.signer-name {
  margin-top:10px;
  // border-top: 1px solid #e1e8ed;
  // padding: 40px 0;
}
</style>





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
        <p class="sign-control-title">签约控件</p>
        <div class="signer-control">
            <draggable :list="loadControlList()" ghost-class="ghost" handle=".control-move" filter=".forbid" item-key="icon"
                :force-fallback="true" chosen-class="chosenClass" drag-class="drag-control" animation="300"
                @end="controlsDragOver" @start="controlsDragStart" :group="group" :fallback-class="false"
                :fallback-on-body="true" :touch-start-threshold="50" :fallback-tolerance="50" :clone="clone" :sort="false">
                <template #item="{ element }">
                    <div class="control-item">
                        <div :class="['control-li control-move']" :style="[
                            'borderColor:' + getColor(7, 'background'),
                            'color:' + getColor(7, 'background'),
                        ]">
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
                                        <p class="pos-doc-header"
                                            style="display: flex;align-items: center;justify-content: space-between;">
                                            <span>{{ doc.documentName }}</span>
                                            <span>{{ doc[element.controlType + 'docControlCount'] + '处' }}</span>
                                        </p>
                                        <ul>
                                            <li v-for="(el, elIndex) in doc[element.controlType + 'docPopControls']"
                                                :key="elIndex">
                                                <span>第{{ el.position.page + 1 }}页</span>
                                                <a-button type="link" @click="handleControlPos(doc, el)">定位</a-button>
                                            </li>
                                        </ul>
                                    </div>
                                </div>

                            </template>
                            <SvgIcon name="location" :size="24" class="control-pos" v-show="element.controlCount"></SvgIcon>
                        </a-popover>
                    </div>

                </template>
            </draggable>
        </div>
    </div>
</template>

<script lang="ts">
import { ref, defineComponent, unref, onMounted } from "vue";
import draggable from 'vuedraggable';
import { Icon, SvgIcon } from "/@/components/Icon";
import { controlListSource } from './data/ControlData';
import { moveRange } from "./data/ControlerMoveRange"
import { cloneDeep } from 'lodash-es';
import { useUserStore } from '/@/store/modules/user';
import { getSignControlType } from '/@/api/contract';
import { useRouter } from 'vue-router';
import { getColor } from "./data/ReceiveColor"

export default defineComponent({
    name: "SignControls",
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
            type: Object
        },
    },
    components: {
        Icon, SvgIcon, draggable
    },
    setup(props, { emit }) {
        const signControlsList: any = ref([]);
        const signControlInfo = ref('');
        const router = useRouter();
        const { currentRoute } = router;
        const route = unref(currentRoute);
        const signRuId = route.query?.signRuId;


        const group = ref({
            name: "itxst",
            put: false, //允许拖入
            pull: 'clone', //允许拖出
            /* pull:(e:any)=>{
                return false;
            } */
        })

        const userStore = useUserStore();
        const tenantInfo = userStore.getTenantInfo;
        function clone(origin: any) {
            console.log(origin, '源----')
            const data = JSON.parse(JSON.stringify(origin))
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
            data.uid = parseInt(new Date().getMilliseconds() + "" + Math.ceil(Math.random() * 100000)).toString(16);
            return data
        }
        onMounted(() => {
            loadSignControlType()
        })
        async function loadSignControlType() {
            let result = await getSignControlType({ ruId: signRuId });
            if (result) {
                signControlInfo.value = result;
                emit('signControlType', result)
            }
        }
        //拖拽开始
        function controlsDragStart() {
            console.log(props.nowDocument, '-开始拖拽------------')
        }
        //拖拽结束
        function controlsDragOver(e: any) {
            //这里目标 props.nowDocument?.activeControl 空间数组会自定push当前拖拽空间 无需手动添加再 故可直接从 props.nowDocument?.activeControl[e.newIndex] 取当前空间
            const moveTarget = props.nowDocument?.activeControl[e.newIndex];
            console.log(moveTarget, '当前拖拽目标');
            if (moveTarget) {
                const opt: any = {
                    x: e.originalEvent.offsetX - (moveTarget.size.width / 2),
                    y: e.originalEvent.offsetY - (moveTarget.size.height / 2),
                    pageSize: props.nowDocument?.pageSize,
                    size: moveTarget.size,
                    currentPage: 0,
                };
                moveRange(opt);
                moveTarget.position.left = opt.x;
                moveTarget.position.top = opt.y;
                moveTarget.position.page = opt.currentPage;
                moveTarget.controlClick = true;
                moveTarget.delete = true;
                moveTarget.signRuDocId = props.nowDocument.signRuDocId;
                moveTarget.controlType = moveTarget.type;
                moveTarget.originType = moveTarget.originType;
                moveTarget.colorIndex = 7;
                console.log(moveTarget, '控件位置')
                emit("dragOver", e, moveTarget)
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
            let docControls = props?.documentList && props.documentList.flatMap(item => item.activeControl);
            controlListSource.map((item: any) => {
                // signerId :签署方id
                types.forEach(v => {
                    if (item.type == v) {
                        props?.documentList && props.documentList.map(u => {
                            let currentDocControls = u.activeControl;
                            let docMatchControls = currentDocControls.filter((m: any) => (m.controlType == v && u.signRuDocId == m.signRuDocId))
                            u[v + 'docControlCount'] = docMatchControls.length;
                            u[v + 'docPopControls'] = docMatchControls;
                            // console.log( u,'匹配到的文档控件')
                            //控件类型
                        })
                        let matchControls = docControls.filter((m: any) => (m.controlType == v))
                        item.controlCount = matchControls.length;
                        item.popControls = matchControls;
                        item.controlType = v;
                        // //控件类型
                        item.originType = 3;
                        newControlList.value.push(cloneDeep(item))
                    }
                })
            })
            console.log(newControlList.value, '当前文档控件-------------')
            return newControlList.value;
        }

        //判断是否显示控件文档
        function loadIsShowPopControl(el, doc) {
            let docHasControl = el.popControls.filter(v => v.signRuDocId == doc.signRuDocId)
            return docHasControl.length ? true : false
        }

        // 定位空间
        function handleControlPos(doc, control) {
            console.log(doc, control, '0000000------------111')
            emit('scroll', doc, control)
        }
        return {
            signControlsList,
            group,
            clone,
            controlsDragOver,
            controlsDragStart,
            loadIsShowPopControl,
            handleControlPos,
            loadControlList,
            getColor
        }
    }
})
</script>

<style lang="less" scoped>
.sign-control-container {
    border-top: 1px solid #f9f9f9;
    padding: 20px 0;
}

.sign-control-title {
    font-size: 18px;
    font-weight: 550;
}

.control-move {
    -webkit-user-select: none;
    /* Safari 和 Chrome */
    -moz-user-select: none;
    /* Firefox */
    -ms-user-select: none;
    /* Internet Explorer */
    user-select: none;
    /* 标准语法 */
    cursor: grab;
}</style>

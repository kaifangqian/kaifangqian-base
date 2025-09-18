<!--
  @description 文档列表

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
   
    <div class="doc-page">
        
        <div class="doc-list-search">
            <div class="flex-row doc-search-left">
                <!-- <van-popover v-model:show="showLoginOutPopover" :actions="actions" @select="onSelect"
                    placement="bottom-start">
                    <template #reference>
                        <div class="account-avatar">
                            <image src="" alt="公司logo" />
                        </div>
                    </template>
                </van-popover> -->
                <!-- {{ searchForm }} -->
                <SwitchIdentity>
                    <div @click="handleSearchMore">

                        <van-badge :content="searchFormLength" :show-zero="false">
                            <svg t="1706719411713" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                xmlns="http://www.w3.org/2000/svg" p-id="5958" width="18" height="18">
                                <path
                                    d="M978.94584 85.542289H40.488129C16.93709 84.006352-0.982179 64.039166 0.041779 40.488127 1.065737 18.473026 18.473027 1.065736 40.488129 0.041777h938.457711c23.551039-1.023958 43.518224 16.895311 45.054162 40.44635s-16.895311 43.518224-40.44635 45.054162h-4.607812zM40.488129 426.520377h85.500512c23.551039 1.023958 41.470308 21.503123 40.446349 45.054162-1.023958 22.015102-18.431248 39.422392-40.446349 40.44635H40.488129c-23.551039-1.023958-41.470308-21.503123-40.44635-45.054162 1.023958-21.503123 18.431248-38.910412 40.44635-40.44635z m0 511.979111h255.989556c23.551039 1.023958 41.470308 21.503123 40.446349 45.054162-1.023958 22.015102-18.431248 39.422392-40.446349 40.44635h-255.989556c-23.551039-1.023958-41.470308-21.503123-40.44635-45.054162 1.023958-21.503123 18.431248-38.910412 40.44635-40.44635z m498.155675-753.121272c203.255707-55.293744 413.167143 65.021347 467.948908 268.277054 37.374475 136.698423-4.607812 283.124449-108.539572 379.376522l108.027593 108.027592c18.431248 18.431248 18.431248 48.126036 0 66.557284-18.431248 18.431248-48.126036 18.431248-66.557285 0l-118.267174-118.267174c-181.240605 103.93176-412.655164 40.958329-516.074944-140.282277-16.895311-29.694788-29.694788-61.437493-38.398434-94.204156C213.025089 450.583395 334.364139 240.67196 538.643804 185.378216z m-188.920292 447.981722c43.006245 159.225504 206.327582 252.917681 365.553086 210.423415s252.917681-206.327582 210.423414-365.553086c-42.494266-158.713524-205.815603-252.917681-364.529127-210.423414-158.713524 41.470308-253.42966 204.279665-211.959352 362.99319 0 0.511979 0.511979 1.535937 0.511979 2.559895z"
                                    fill="#C0C4CC" p-id="5959"></path>
                            </svg>
                        </van-badge>
                    </div>
                </SwitchIdentity>
            </div>


        </div>
        <div class="doc-group">
            <van-tabs v-model:active="activeTabKey" @change="(e) => { loading = true; handleTabChange(e); }">
                <van-tab title="待我处理" name="1" :badge="myJobCount" :show-zero-badge="false"></van-tab>
                <van-tab title="全部文档" name="2"></van-tab>
                <van-tab title="已发送" name="3" v-if="appUserInfo && appUserInfo.loginTenantType == 1"></van-tab>
                <van-tab title="抄送给我" name="4"></van-tab>
                <van-tab title="已完成" name="5"></van-tab>
                <van-tab title="已失效" name="6"></van-tab>
            </van-tabs>
        </div>
        <div class="doc-list">
            <van-pull-refresh v-model="refreshLoading" @refresh="onRefresh">
                <van-list v-model:loading="loading" :finished="finished" v-if="docList && docList.length"
                    :finished-text="docList && docList.length ? '没有更多了' : '暂无数据'" @load="onLoad" :immediate-check="false">
                    <template v-for="item in docList">
                        <van-card desc="" title="" @click="handleRunStatusPage(item)">
                            <template #title>
                                <div class="doc-header flex-row">
                                    <span class="doc-title">{{ item.subject }}</span>
                                    <template v-if="item.status !== undefined">
                                        <van-tag
                                            plain
                                            type="primary"
                                            :color="getStatusColor(item.status)"
                                            :style="{ fontWeight: '400', fontSize: '12px'}"
                                        >
                                            {{ loadRuStatus(item.status) }}
                                        </van-tag>
                                    </template>
                                </div>
                            </template>
                            <template #desc>
                                <div class="doc-body">
                                    <ul>
                                        <li>
                                            <span class="doc-label">发送方：</span>
                                            <span class="doc-value">{{ item.fromTenantName }}</span>
                                        </li>
                                        <li>
                                            <span class="doc-label">发送时间：</span>
                                            <span class="doc-value">{{ item.createTime }}</span>
                                        </li>
                                        <li>
                                            <span class="doc-label">截止时间：</span>
                                            <span class="doc-value">{{ item.expireDate || '未设置' }}</span>
                                        </li>
                                    </ul>
                                </div>

                            </template>

                        </van-card>
                    </template>
                </van-list>
                <p v-else class="van-list__finished-text">暂无数据 </p>
            </van-pull-refresh>

        </div>
        <van-popup v-model:show="searchDrawer" position="right" :close-on-click-overlay="true"
            :safe-area-inset-bottom="true" :style="{ width: '80%', height: '100%' }" @close="handleConfrim">

            <template #default>

                <div class="search-header">

                    <div class="search-title">文件搜索</div>

                </div>

                <div class="search-body">
                    <ul>
                        <li class="search-item">
                            <p class="search-sub-title">签约主题</p>
                            <div class="search-value">
                                <van-field class="vant-input" v-model="searchForm.subject" label="" placeholder="请输入签约主题" />
                            </div>
                        </li>
                        <li class="search-item">
                            <p class="search-sub-title">签约状态</p>
                            <div class="sign-status-tag">

                                <van-tag plain type="primary" :color="statusActiveKey == 11 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(11)">已完成</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 5 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(5)">填写中</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 7 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(7)">签署中</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 9 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(9)">已失效</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 6 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(6)">已拒填</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 8 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(8)">已拒签</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 1 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(1)">草稿</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 4 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(4)">已删除</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 10 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(10)">已撤回</van-tag>

                                <!-- <van-tag plain type="primary" :color="statusActiveKey == 2 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(2)">发起审批中</van-tag>

                                <van-tag plain type="primary" :color="statusActiveKey == 3 ? '#288cf2' : ''"
                                    @click="handleChangeStatusKey(3)">发起审批不通过</van-tag> -->
                            </div>
                        </li>
                        <li class="search-item">
                            <p class="search-sub-title">文档编号</p>
                            <div>
                                <van-field class="vant-input" v-model="searchForm.code" label="" placeholder="请输入文档编号" />
                            </div>
                        </li>
                        <li class="search-item">
                            <p class="search-sub-title">创建时间</p>
                            <div>
                                <van-field class="vant-input" v-model="searchForm.createTime" @focus="handleCreateVisible"
                                    placeholder="请选择日期" />
                                <van-calendar v-model:show="createTimeVisible" type="range" @confirm="onConfirm"
                                    :min-date="minDate" />
                            </div>
                            <div class="sign-date-tag">
                                <van-tag plain type="primary" :color="createTimeActiveKey == 1 ? '#288cf2' : ''"
                                    @click="handleChangeCreateTimeKey(1)">近一周</van-tag>
                                <van-tag plain type="primary" :color="createTimeActiveKey == 2 ? '#288cf2' : ''"
                                    @click="handleChangeCreateTimeKey(2)">近一月</van-tag>
                                <van-tag plain type="primary" :color="createTimeActiveKey == 3 ? '#288cf2' : ''"
                                    @click="handleChangeCreateTimeKey(3)">近三个月</van-tag>
                                <van-tag plain type="primary" :color="createTimeActiveKey == 4 ? '#288cf2' : ''"
                                    @click="handleChangeCreateTimeKey(4)">近六个月</van-tag>
                                <van-tag plain type="primary" :color="createTimeActiveKey == 5 ? '#288cf2' : ''"
                                    @click="handleChangeCreateTimeKey(5)">近一年</van-tag>
                                <van-tag plain type="primary" :color="createTimeActiveKey == 6 ? '#288cf2' : ''"
                                    @click="handleChangeCreateTimeKey(6)">近三年</van-tag>
                            </div>
                        </li>
                        <li class="search-item">
                            <p class="search-sub-title">发起时间</p>
                            <div>
                                <van-field class="vant-input" v-model="searchForm.startTime" @focus="handleStartVisible"
                                    placeholder="请选择日期" />
                                <van-calendar v-model:show="startTimeVisible" type="range" :min-date="minDate"
                                    @confirm="onStartTimeConfirm" />
                            </div>
                            <div class="sign-date-tag">
                                <van-tag plain type="primary" :color="startTimeActiveKey == 1 ? '#288cf2' : ''"
                                    @click="handleChangeStartTimeKey(1)">近一周</van-tag>
                                <van-tag plain type="primary" :color="startTimeActiveKey == 2 ? '#288cf2' : ''"
                                    @click="handleChangeStartTimeKey(2)">近一月</van-tag>
                                <van-tag plain type="primary" :color="startTimeActiveKey == 3 ? '#288cf2' : ''"
                                    @click="handleChangeStartTimeKey(3)">近三个月</van-tag>
                                <van-tag plain type="primary" :color="startTimeActiveKey == 4 ? '#288cf2' : ''"
                                    @click="handleChangeStartTimeKey(4)">近六个月</van-tag>
                                <van-tag plain type="primary" :color="startTimeActiveKey == 5 ? '#288cf2' : ''"
                                    @click="handleChangeStartTimeKey(5)">近一年</van-tag>
                                <van-tag plain type="primary" :color="startTimeActiveKey == 6 ? '#288cf2' : ''"
                                    @click="handleChangeStartTimeKey(6)">近三年</van-tag>
                            </div>
                        </li>
                        <li class="search-item">
                            <p class="search-sub-title">完成时间</p>
                            <div>
                                <van-field class="vant-input" v-model="searchForm.finishTime" @focus="handleFinishVisible"
                                    placeholder="请选择日期" />
                                <van-calendar v-model:show="finishTimeVisible" type="range" :min-date="minDate"
                                    @confirm="onFinishTimeConfirm" />
                            </div>
                            <div class="sign-date-tag">
                                <van-tag plain type="primary" :color="finishTimeActiveKey == 1 ? '#288cf2' : ''"
                                    @click="handleChangeFinishTimeKey(1)">近一周</van-tag>
                                <van-tag plain type="primary" :color="finishTimeActiveKey == 2 ? '#288cf2' : ''"
                                    @click="handleChangeFinishTimeKey(2)">近一月</van-tag>
                                <van-tag plain type="primary" :color="finishTimeActiveKey == 3 ? '#288cf2' : ''"
                                    @click="handleChangeFinishTimeKey(3)">近三个月</van-tag>
                                <van-tag plain type="primary" :color="finishTimeActiveKey == 4 ? '#288cf2' : ''"
                                    @click="handleChangeFinishTimeKey(4)">近六个月</van-tag>
                                <van-tag plain type="primary" :color="finishTimeActiveKey == 5 ? '#288cf2' : ''"
                                    @click="handleChangeFinishTimeKey(5)">近一年</van-tag>
                                <van-tag plain type="primary" :color="finishTimeActiveKey == 6 ? '#288cf2' : ''"
                                    @click="handleChangeFinishTimeKey(6)">近三年</van-tag>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="search-footer">
                    <van-button type="default" @click="resetSearchForm">重置</van-button>
                    <van-button type="primary" @click="handleConfrim(true)">确认</van-button>
                </div>
            </template>
        </van-popup>
    </div>
</template>



<script  lang="ts">
import { ref, defineComponent, onMounted, onActivated, watch } from "vue"
import { Toast, Dialog, Notify } from 'vant';
import Api from '@/api/contract/index';
import { loadRuStatus } from './transform';
import { useRouter, useRoute } from 'vue-router';
import SwitchIdentity from '@/pages/components/SwitchIdentity.vue';
import { timeRanges } from '@/utils/util';
import session from "@/utils/cache/session";

interface Requset {
    [request: string]: () => void;
}
export default defineComponent({
    name: '',
    components: {
        Toast,
        Dialog,
        SwitchIdentity,
        'v-dialog': Dialog.Component,
    },
    setup() {

        const showPopover = ref(false);
        const topactions = [
        { text: '选项一' },
        { text: '选项二' },
        { text: '选项三' },
        ];

        const tenantId = ref(0)
        const statusActiveKey = ref(0)
        const createTimeActiveKey = ref(0)
        const startTimeActiveKey = ref(0)
        const finishTimeActiveKey = ref(0)
        const activeTabKey = ref('1')
        const searchDrawer = ref(false);
        const createTimeVisible = ref(false);
        const startTimeVisible = ref(false);
        const finishTimeVisible = ref(false);
        const showLoginOutPopover = ref(false);
        const loginTenantName = ref('');
        const tenantVisible = ref(false)
        const loading = ref(false)
        const myJobCount = ref(0);
        const searchFormLength = ref(0)
        const refreshLoading = ref(false)
        const finished = ref(false)
        const total = ref(0);
        const minDate = new Date(2000, 0, 1);
        const searchForm: any = ref({
            pageNo: 1,
            pageSize: 10
        });
        const actions = [
            { text: '退出登录' }
        ];
        const router = useRouter();
        const route = useRoute();
        const docList: any = ref([])
        const columns = ref([

        ])

        const appUserInfo = session.getItem('app_user_info');

        onActivated(() => {
            handleTabChange(activeTabKey.value)
        })
        onMounted(() => {
            init()
            console.log("onMounted 1111");
        })
        watch(
            () => searchForm.value,
            (values) => {
                searchFormLength.value = 0;
                Object.keys(values).forEach(key => {
                    if ((key == 'subject' || key == 'status' || key == 'code' || key == 'createTime' || key == 'startTime' || key == 'finishTime') && values[key]) {
                        searchFormLength.value += 1;
                    }
                })
            },
            { deep: true }
        )

        async function init() {
            // docList.value = [];
            const { result } = await Api.getListMy({ ...searchForm.value })
            if (result) {
                docList.value = result.records;
                myJobCount.value = result.total;
                if (!myJobCount.value) {
                    handleTabChange('2')
                }
            }
        }
        //下拉刷新
        const onRefresh = () => {
            setTimeout(() => {
                refreshLoading.value = false;
                searchForm.value.pageNo = 1;
                docList.value = [];
                finished.value = false;
                handleTabChange(activeTabKey.value, false)
            }, 500);
        };
        function onSelect() {
            Dialog.confirm({
                title: '退出登录',
                message: '是否确认退出系统',

            }).then(() => {
                // on confirm
            }).catch(() => {
                // on cancel
            });
        }

        function getStatusColor(status: number) {
            switch (status) {
                case 1:
                    return '#dcdee0'; // 草稿
                case 5:
                    return '#1989fa'; // 填写中
                case 7:
                    return '#1989fa'; // 签署中
                case 9:
                    return '#dcdee0'; // 已失效
                case 6:
                    return '#ed6a0c'; // 已拒填
                case 8:
                    return '#ed6a0c'; // 已拒签
                case 11:
                    return '#07c160'; // 已完成
                case 4:
                    return '#ee0a24'; // 已删除
                case 10:
                    return '#dcdee0'; // 已撤回
                default:
                    return '#1989fa'; // 默认颜色
            }
        }
        function handleChangeStatusKey(key: number) {
            if (key == statusActiveKey.value) {
                statusActiveKey.value = 0
                searchForm.value.status = ''
            } else {
                statusActiveKey.value = key
                searchForm.value.status = key
            }

        }
        function handleChangeCreateTimeKey(key: number) {

            if (key === createTimeActiveKey.value) {
                createTimeActiveKey.value = 0;
                searchForm.value.beginCreateTime = '';
                searchForm.value.endCreateTime = '';
                searchForm.value.createTime = '';

            } else {
                createTimeActiveKey.value = key
                if (key == 1) {
                    searchForm.value.beginCreateTime = timeRanges.oneWeekAgo + ' ' + '00:00:00';
                    searchForm.value.endCreateTime = timeRanges.currentDate + ' ' + '23:59:59';
                    searchForm.value.createTime = `${timeRanges.oneWeekAgo} ~ ${timeRanges.currentDate}`;
                }
                if (key == 2) {
                    searchForm.value.beginCreateTime = timeRanges.oneMonthAgo + ' ' + '00:00:00';
                    searchForm.value.endCreateTime = timeRanges.currentDate + ' ' + '23:59:59';
                    searchForm.value.createTime = `${timeRanges.oneMonthAgo} ~ ${timeRanges.currentDate}`;
                }
                if (key == 3) {
                    searchForm.value.beginCreateTime = timeRanges.threeMonthsAgo + ' ' + '00:00:00';
                    searchForm.value.endCreateTime = timeRanges.currentDate + ' ' + '23:59:59';
                    searchForm.value.createTime = `${timeRanges.threeMonthsAgo} ~ ${timeRanges.currentDate}`;
                }
                if (key == 4) {
                    searchForm.value.beginCreateTime = timeRanges.sixMonthsAgo + ' ' + '00:00:00';
                    searchForm.value.endCreateTime = timeRanges.currentDate + ' ' + '23:59:59';
                    searchForm.value.createTime = `${timeRanges.sixMonthsAgo} ~ ${timeRanges.currentDate}`;
                }
                if (key == 5) {
                    searchForm.value.beginCreateTime = timeRanges.oneYearAgo + ' ' + '00:00:00';
                    searchForm.value.endCreateTime = timeRanges.currentDate + ' ' + '23:59:59';
                    searchForm.value.createTime = `${timeRanges.oneYearAgo} ~ ${timeRanges.currentDate}`;
                }
                if (key == 6) {
                    searchForm.value.beginCreateTime = timeRanges.threeYearAgo + ' ' + '00:00:00';
                    searchForm.value.endCreateTime = timeRanges.currentDate + ' ' + '23:59:59';
                    searchForm.value.createTime = `${timeRanges.threeYearAgo} ~ ${timeRanges.currentDate}`;
                }
            }

        }
        function handleChangeStartTimeKey(key: number) {
            if (startTimeActiveKey.value == key) {
                startTimeActiveKey.value = 0;
                searchForm.value.beginStartTime = '';
                searchForm.value.endStartTime = '';
                searchForm.value.startTime = '';
                return
            }
            startTimeActiveKey.value = key
            if (key == 1) {
                searchForm.value.beginStartTime = timeRanges.oneWeekAgo + ' ' + '00:00:00';
                searchForm.value.endStartTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.startTime = `${timeRanges.oneWeekAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 2) {
                searchForm.value.beginStartTime = timeRanges.oneMonthAgo + ' ' + '00:00:00';
                searchForm.value.endStartTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.startTime = `${timeRanges.oneMonthAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 3) {
                searchForm.value.beginStartTime = timeRanges.threeMonthsAgo + ' ' + '00:00:00';
                searchForm.value.endStartTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.startTime = `${timeRanges.threeMonthsAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 4) {
                searchForm.value.beginStartTime = timeRanges.sixMonthsAgo + ' ' + '00:00:00';
                searchForm.value.endStartTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.startTime = `${timeRanges.sixMonthsAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 5) {
                searchForm.value.beginStartTime = timeRanges.oneYearAgo + ' ' + '00:00:00';
                searchForm.value.endStartTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.startTime = `${timeRanges.oneYearAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 6) {
                searchForm.value.beginStartTime = timeRanges.threeYearAgo + ' ' + '00:00:00';
                searchForm.value.endStartTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.startTime = `${timeRanges.threeYearAgo} ~ ${timeRanges.currentDate}`;
            }
        }
        function handleChangeFinishTimeKey(key: number) {
            if (finishTimeActiveKey.value == key) {
                finishTimeActiveKey.value = 0;
                searchForm.value.beginFinishTime = '';
                searchForm.value.endFinishTime = '';
                searchForm.value.finishTime = '';
                return
            }
            finishTimeActiveKey.value = key
            if (key == 1) {
                searchForm.value.beginFinishTime = timeRanges.oneWeekAgo + ' ' + '00:00:00';
                searchForm.value.endFinishTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.finishTime = `${timeRanges.oneWeekAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 2) {
                searchForm.value.beginFinishTime = timeRanges.oneMonthAgo + ' ' + '00:00:00';
                searchForm.value.endFinishTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.finishTime = `${timeRanges.oneMonthAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 3) {
                searchForm.value.beginFinishTime = timeRanges.threeMonthsAgo + ' ' + '00:00:00';
                searchForm.value.endFinishTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.finishTime = `${timeRanges.threeMonthsAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 4) {
                searchForm.value.beginFinishTime = timeRanges.sixMonthsAgo + ' ' + '00:00:00';
                searchForm.value.endFinishTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.finishTime = `${timeRanges.sixMonthsAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 5) {
                searchForm.value.beginFinishTime = timeRanges.oneYearAgo + ' ' + '00:00:00';
                searchForm.value.endFinishTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.finishTime = `${timeRanges.oneYearAgo} ~ ${timeRanges.currentDate}`;
            }
            if (key == 6) {
                searchForm.value.beginFinishTime = timeRanges.threeYearAgo + ' ' + '00:00:00';
                searchForm.value.endFinishTime = timeRanges.currentDate + ' ' + '23:59:59';
                searchForm.value.finishTime = `${timeRanges.threeYearAgo} ~ ${timeRanges.currentDate}`;
            }
        }
        function onConfirm(values: any) {
            const formatDate = (date: any) => `${date.getFullYear()}-${(date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1}-${date.getDate() < 10 ? '0' + date.getDate() : date.getDate()}`;
            const [start, end] = values;
            createTimeVisible.value = false;
            searchForm.value.createTime = `${formatDate(start)} ~ ${formatDate(end)}`;
            searchForm.value.beginCreateTime = `${formatDate(start) + ' ' + '00:00:00'}`;
            searchForm.value.endCreateTime = `${formatDate(end) + ' ' + '23:59:59'}`;
        }

        function onStartTimeConfirm(values: any) {
            const formatDate = (date: any) => `${date.getFullYear()}-${(date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1}-${date.getDate() < 10 ? '0' + date.getDate() : date.getDate()}`;
            const [start, end] = values;
            startTimeVisible.value = false;
            searchForm.value.startTime = `${formatDate(start)} ~ ${formatDate(end)}`;
            searchForm.value.beginStartTime = `${formatDate(start) + ' ' + '00:00:00'}`;
            searchForm.value.endStartTime = `${formatDate(end) + ' ' + '23:59:59'}`;
        }
        function onFinishTimeConfirm(values: any) {
            const formatDate = (date: any) => `${date.getFullYear()}-${(date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1}-${date.getDate() < 10 ? '0' + date.getDate() : date.getDate()}`;
            const [start, end] = values;
            finishTimeVisible.value = false;
            searchForm.value.finishTime = `${formatDate(start)} ~ ${formatDate(end)}`;
            searchForm.value.beginFinishTime = `${formatDate(start) + ' ' + '00:00:00'}`;
            searchForm.value.endFinishTime = `${formatDate(end) + ' ' + '23:59:59'}`;
        }
        function onCancel() {

        }
        function onChange() {

        }
        function resetSearchForm() {
            searchForm.value = {
                subject: '',
                code: '',
                createTime: '',
                startTime: '',
                finishTime: ''
            }
            statusActiveKey.value = 0;
            createTimeActiveKey.value = 0;
            startTimeActiveKey.value = 0;
            finishTimeActiveKey.value = 0;
        }

        function handleConfrim(val: undefined | boolean) {
            if (val) {
                searchDrawer.value = false;
                handleTabChange(activeTabKey.value)
            }
        }
        function handleSearchMore() {
            searchDrawer.value = true;
            createTimeVisible.value = false;
            startTimeVisible.value = false;
            finishTimeVisible.value = false;
        }
        function handleTenants() {
            tenantVisible.value = true;
        }
        function handleCreateVisible() {
            createTimeVisible.value = true;
        }
        function handleStartVisible() {
            startTimeVisible.value = true;
        }
        function handleFinishVisible() {
            finishTimeVisible.value = true;
        }
        function onLoad() {
            setTimeout(() => {
                searchForm.value.pageNo += 1;
                console.log('下拉触发次数')
                handleTabChange(activeTabKey.value, true)
                // 加载状态结束
                //这里设置为true不再次触发加载，请求完了再关闭loading 
                loading.value = true;
                // 数据全部加载完成
                if (docList.value.length >= total.value) {
                    finished.value = true;
                }
            }, 1000);
        }
        async function handleTabChange(val: string, isRrefresh: boolean = false) {
            activeTabKey.value = val;
            if (!isRrefresh || searchForm.value.pageNo == 1) {
                searchForm.value.pageNo = 1;
                docList.value = []
                finished.value = false;
            }
            let apiMethod = ''
            switch (val) {
                case '1':
                    apiMethod = 'getListMy';
                    break;
                case '2':
                    apiMethod = 'getListAll';
                    break;
                case '3':
                    apiMethod = 'getListSend';
                    break;
                case '4':
                    apiMethod = 'getListCopyMe';
                    break;
                case '5':
                    apiMethod = 'getListFinish';
                    break;
                case '6':
                    apiMethod = 'getListInvalid';
                    break;
                default:
                    apiMethod = 'getListMy'
            }
            loading.value = true;
            let { result } = await Api[apiMethod]({ ...searchForm.value, startTime: undefined, createTime: undefined, finishTime: undefined });
            if (result) {
                if (val == 1) {
                    myJobCount.value = result.total;
                }
                docList.value.push(...result.records);
                total.value = result.total;
                loading.value = false;

            }
        }

        async function handleRunStatusPage(row: any) {
            if(row.status === 1){
                Notify({ type: 'warning', message: '暂不支持在手机端编辑和发起', duration: 2000 });
                return;
            }
            const { result, code } = await Api.checkOperate({ signRuId: row.signRuId });
            if (code == 200) {
                if (row.status == 5) {
                    if (result.taskId) {
                        router.push({
                            path: '/write',
                            query: {
                                signRuId: row.signRuId,
                                taskId: result.taskId
                            }
                        });
                    } else {
                        router.push({
                            path: '/detail',
                            query: {
                                signRuId: row.signRuId
                            }
                        });
                    }

                } else if (row.status == 7) {
                    if (result.taskId) {
                        router.push({
                            path: '/signContract',
                            query: {
                                signRuId: row.signRuId,
                                taskId: result.taskId
                            }
                        });
                    } else {
                        router.push({
                            path: '/detail',
                            query: {
                                signRuId: row.signRuId
                            }
                        });
                    }

                } else if (row.status == 11) {
                    router.push({
                        path: '/detail',
                        query: {
                            signRuId: row.signRuId
                        }
                    });
                } else if (row.status == 1 || row.status == 6 || row.status == 8 || row.status == 9 || row.status == 10) {
                    router.push({
                        path: '/detail',
                        query: {
                            signRuId: row.signRuId
                        }
                    });
                }
            }
        }

        return {
            topactions,
            showPopover,
            handleRunStatusPage,
            columns,
            onConfirm,
            appUserInfo,
            onCancel,
            onChange,
            tenantId,
            activeTabKey,
            handleChangeStatusKey,
            docList,
            handleSearchMore,
            myJobCount,
            searchDrawer,
            minDate,
            searchForm,
            actions,
            showLoginOutPopover,
            onSelect,
            handleTenants,
            onRefresh,
            createTimeVisible,
            tenantVisible,
            loginTenantName,
            statusActiveKey,
            loadRuStatus,
            handleCreateVisible,
            loading,
            finished,
            onLoad,
            handleStartVisible,
            handleFinishVisible,
            startTimeVisible,
            finishTimeVisible,
            onStartTimeConfirm,
            onFinishTimeConfirm,
            handleTabChange,
            handleChangeCreateTimeKey,
            searchFormLength,
            handleChangeStartTimeKey,
            handleChangeFinishTimeKey,
            startTimeActiveKey,
            finishTimeActiveKey,
            createTimeActiveKey,
            resetSearchForm,
            refreshLoading,
            handleConfrim,
            getStatusColor
        }
    }

})
</script>

<style lang="less" scoped>
.doc-page {
    position: relative;

    .login-name {
        font-size: 32px;
        margin-left: 20px;

    }

}

:deep(.tenant-list-dialog .van-dialog__header) {
    padding: 16px 0;
    border-bottom: 2px solid #e4e4e4;
    text-align: left;
    margin-left: 20px;
}

.doc-list-search {
    position: fixed;
    top: calc(@header-height + 2px);
    height: 92px;
    background: #fff;
    z-index: 10;
    width: 100%;
    left: 0;
    // padding-right: 20px;
    box-shadow: 0 0 2px 2px rgba(64, 106, 103, 0.13);
}

.doc-group {
    // margin-bottom: 20px;
    position: fixed;
    top: calc(@header-height + @header-height);
    height: 92px;
    background: #fff;
    z-index: 5;
    width: 100%;
    left: 0;
    padding-right: 20px;
    box-shadow: 0 0px 2px 2px rgba(64, 106, 103, 0.13);
}

.doc-list-search {
    // display: flex;
    // justify-content: space-between;
    // align-items: center;

    svg,
    img {
        display: block;
    }

    :deep(.van-dropdown-menu__bar) {
        box-shadow: none;
    }

    .doc-search-left {
        padding: 0 20px;

        .account-avatar {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            background: #8ac4eb;
        }

        :deep(.van-dropdown-menu) {
            width: 14rem;
            height: 94px;
        }

        :deep(.van-dropdown-menu__item) {
            justify-content: left;
        }

        :deep(.van-dropdown-menu__bar) {
            height: 100%;
        }
    }

}

:deep(.van-tabs__line) {
    background: @blue;
}

:deep(.van-card__bottom) {
    border-top: 1px solid #e4e4e4;
}

.doc-list {
    margin-top: 140px;

    :deep(.van-card) {
        background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(345deg, #eff6ff -4%, #ffffff 100%);
        // background: #fff;
        // background: linear-gradient(0deg, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0)), linear-gradient(135deg, #eff6ff -4%, #ffffff 100%);
        border-radius: 8px;
        box-shadow: 0 2px 8px 0 rgba(64, 106, 103, 0.10), 0 1.5px 6px 0 rgba(40, 140, 242, 0.08);
        margin: 40px 0;
        padding: 0;
        border: 1px solid #e4e4e4;

        .doc-header {
            padding: 20px 30px;
            border-bottom: 1px solid #e4e4e4;
            color: #000;
            font-size: 28px;
            font-weight: 600;

            .doc-title {
                max-width: 520px;
                text-overflow: ellipsis;
                overflow: hidden;
                display: inline-block;
                white-space: nowrap;
            }
        }
    }

    .doc-body {
        padding: 40px 50px;

        li {
            margin: 20px 16px;
            display: flex;
        }

        .doc-label {
            width: 160px;
            display: block;
        }

        .doc-value {
            flex-basis: 400px;
            font-weight: 500;
            color: #000;

        }

    }

    .doc-footer {
        padding: 20px;
        width: 100%;
        text-align: right;
    }
}

.search-header {
    height: 92px;
    border-bottom: 1px solid #e4e4e4;
    padding: 20px;
    text-align: center;
    font-size: 28px;
}

.search-body {
    padding: 20px 30px;
    position: relative;
    height: calc(100% - 184px);
    overflow: auto;

    :deep(.van-field__body) {
        height: 100%;
    }

    :deep(.van-calendar__body) {

        .van-calendar__day--end,
        .van-calendar__day--start {
            background: @blue;
        }
    }

    :deep(.van-calendar__footer) {
        position: absolute;
        bottom: 0px;
        width: 100%;
        z-index: 100;

        .van-button {

            background: @blue;
            border-color: @blue;
            border-radius: 2px;
        }
    }

    .search-sub-title {
        font-size: 26px;
    }

    .search-item {
        .vant-input {
            border: 1px solid #e4e4e4;
            padding: 4px 30px;
            height: 64px;
            line-height: 64px;

        }
    }

    .sign-status-tag {
        display: flex;
        flex-wrap: wrap;
        align-items: center;

        :deep(.van-tag--primary.van-tag--plain) {
            color: #999;
            margin: 8px 20px;
            border-radius: 4px;
            padding: 8px 16px;
            min-width: 140px;
            text-align: center;
            display: inline-block;
        }

    }



    .sign-date-tag {

        display: flex;
        flex-wrap: wrap;
        align-items: center;
        margin-top: 20px;

        :deep(.van-tag--primary.van-tag--plain) {
            color: #999;
            margin: 8px 20px;
            border-radius: 2px;
            padding: 8px;
            width: 140px;
            text-align: center;
            display: inline-block;
        }
    }
}

.search-footer {
    position: absolute;
    bottom: 0;
    display: none;
    width: 100%;
    border-top: 1px solid #e4e4e4;
    display: flex;
    align-items: center;
    height: 92px;
    z-index: 4;

    :deep(.van-button) {
        width: 50%;
        border: none;
        border-radius: none;
        height: 100%;
        padding: 0;
        margin: 0;

        &:nth-child(2) {
            border-left: 1px solid #e4e4e4;
        }
    }
}

:deep(.van-list__finished-text) {
    margin-top: 480px;
}

:deep(.van-list__finished-text) {
    margin-top: 0;
}
</style>


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
  <div class="business-container">
    <!-- 检索条件区域 -->
    <div v-if="showSearchForm"  class="search-form-container">
      <div class="search-form">
        <a-form layout="inline" :model="searchForm" @submit.prevent="handleSearch">
          <a-form-item  label="业务线名称">
            <a-input 
              v-model:value="searchForm.name" 
              placeholder="请输入业务线名称" 
              allow-clear
              style="width: 320px"
              @pressEnter="handleSearch"
            />
          </a-form-item>
          <a-form-item label="业务线状态">
            <a-select 
              v-model:value="searchForm.status" 
              placeholder="请选择状态" 
              allow-clear
              style="width: 220px;font-size: 12px;"
              @change="handleSearch"
            >
              <a-select-option :value="1">启用</a-select-option>
              <a-select-option :value="2">停用</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" style="margin-left: 18px; width:160px " @click="handleSearch">查询</a-button>
            <a-button style="margin-left: 18px; width:160px" @click="handleReset">重置</a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
    <!-- 滚动容器 -->
    <Scrollbar width="100%" height="100%" :native="true" :noresize="true" :class="{ 'business-scrollbar': showSearchForm }">
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading-state">加载中...</div>

      <!-- 卡片列表 -->
      <div v-else-if="tableData.length > 0" class="card-list">
        <div v-for="(record, index) in tableData" :key="record.id || index" class="card-item">
          <div v-if="record">
            <!-- 状态标签 -->
            <div class="status-tag" :class="statusClass(record.status)">
              {{ statusText(record.status) }}
            </div>

            <!-- 卡片内容 -->
            <div class="card-content">
              <!-- 业务线名称（标题） -->
              <div class="card-title">{{ record?.name || '未命名业务线' }}</div>

              <!-- 管理员信息 -->
              <div class="card-field admin">
                <div class="admin-icon"></div>
                <span class="value">
                  业务管理员： {{ formatManagerList(record.managerList) }}
                </span>
              </div>

              <!-- 类型 -->

              <div class="card-field admin">
                <span class="value"> 业务类型：&nbsp;&nbsp;&nbsp; {{ record?.folderName }} </span>
              </div>

              <!-- 操作按钮 -->
              <div class="card-actions">
                <a-button
                  :disabled="record.status !== 1"
                  type="primary"
                  @click="startBusiness(record)"
                 
                >
                  发起签署
                </a-button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 空数据状态 -->
        <div v-else  class="empty-state">
          <a-empty :image="simpleImage" >
              <template #description>
                <span>
                  <p> 暂无可用于发起签署的授权业务线</p>
                  <p> 请联系管理员，为您授权业务线的发起权限</p>
                </span>
            </template>
          </a-empty>
      </div>
    </Scrollbar>

    <!-- 分页组件 -->
    <div class="pagination-wrapper" v-if="!loading && pagination.total > 9">
      <a-pagination
        v-model:current="pagination.current"
        v-model:pageSize="pagination.pageSize"
        :total="pagination.total"
        show-size-changer
        @showSizeChange="debouncedOnPageSizeChange"
        @change="debouncedOnPageChange"
      />
    </div>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, onMounted, computed  } from 'vue';
  import { useRouter } from 'vue-router';
  import { getBusinessLineList } from '/@/api/contract';
  import { Scrollbar } from '/@/components/Scrollbar';
  import { Empty, Form, Input, Select, Button } from 'ant-design-vue';
  const simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;

  const START_PATH = '/contract/start';
  const DEFAULT_PAGE_SIZE = 9;

  export default defineComponent({
    name: 'BusinessListView',
    components: {
      Scrollbar,
      [Form.name]: Form,
      [Form.Item.name]: Form.Item,
      [Input.name]: Input,
      [Select.name]: Select,
      [Select.Option.name]: Select.Option,
      [Button.name]: Button,
    },
    setup() {
      const router = useRouter();

      // 数据与分页
      const tableData = ref<any[]>([]);
      const loading = ref(true);
      const pagination = ref({
        current: 1,
        pageSize: DEFAULT_PAGE_SIZE,
        total: 0,
      });

      // 检索表单
      const searchForm = ref({
        name: '',
        status: undefined,
      });

      // 是否显示检索条件
      const showSearchForm = ref(false);
      // 是否已经进行过检索
      const hasSearched = ref(false);

      let lastValidPage = {
        current: 1,
        pageSize: DEFAULT_PAGE_SIZE,
      };

      // 计算是否应该显示搜索表单
      const shouldShowSearchForm = computed(() => {
        // 如果已经进行过检索，始终显示
        if (hasSearched.value) {
          return true;
        }
        // 初始化时，如果数据超过一页，显示搜索表单
        return pagination.value.total > pagination.value.pageSize;
      });

      // 检索处理
      const handleSearch = () => {
        hasSearched.value = true;
        pagination.value.current = 1;
        loadData();
      };

      // 重置检索条件
      const handleReset = () => {
        searchForm.value.name = '';
        searchForm.value.status = undefined;
        hasSearched.value = false;
        pagination.value.current = 1;
        loadData();
      };

      // 加载数据
      const loadData = async () => {
        loading.value = true;
        try {
          const params: any = {
            pageNo: pagination.value.current,
            pageSize: pagination.value.pageSize,
          };

          // 添加检索条件
          if (searchForm.value.name) {
            params.name = searchForm.value.name;
          }
          if (searchForm.value.status !== undefined && searchForm.value.status !== null) {
            params.status = searchForm.value.status;
          }

          const res = await getBusinessLineList(params);
          if (res && Array.isArray(res.records)) {
            tableData.value = res.records;
            pagination.value.total = res.total || 0;
            lastValidPage = { ...pagination.value };
            // 更新是否显示搜索表单
            showSearchForm.value = shouldShowSearchForm.value;
          } else {
            tableData.value = [];
            console.warn('接口返回格式异常');
            restoreLastValidPage();
          } 
        } catch (error) {
          console.error('加载失败:', error);
          tableData.value = [];
          restoreLastValidPage();
        } finally {
          loading.value = false;
        }
      };

      function restoreLastValidPage() {
        pagination.value.current = lastValidPage.current;
        pagination.value.pageSize = lastValidPage.pageSize;
      }

      // 页面挂载时加载数据
      onMounted(() => {
        loadData();
      });

      // 分页事件处理
      const onPageChange = (page: number) => {
        // console
        pagination.value.current = page;
        loadData();
      };

      const onPageSizeChange = (current: number, size: number) => {
        pagination.value.current = 1;
        pagination.value.pageSize = size;
        loadData();
      };

      // 防抖包装
      const debounce = (fn: Function, delay: number) => {
        let timer: any;
        return (...args: any[]) => {
          clearTimeout(timer);
          timer = setTimeout(() => fn(...args), delay);
        };
      };

      const debouncedOnPageChange = debounce(onPageChange, 300);
      const debouncedOnPageSizeChange = debounce(onPageSizeChange, 300);

      // 发起签署操作
      const startBusiness = (item: any) => {
        router.push({
          path: START_PATH,
          query: {
            __full__: '',
            signReId: item.id,
          },
        });
      };

      // 格式化管理员列表
      const formatManagerList = (managerList: string[] | undefined): string => {
        if (!managerList || !Array.isArray(managerList)) return '';
        return managerList.filter((m) => typeof m === 'string').join('、');
      };

      const statusText = (status: number): string => {
        return status === 1 ? '启用' : '停用';
      };

      const statusClass = (status: number): string => {
        return status === 1 ? 'active' : 'inactive';
      };

      return {
        tableData,
        pagination,
        loading,
        startBusiness,
        debouncedOnPageChange,
        debouncedOnPageSizeChange,
        formatManagerList,
        statusText,
        statusClass,
        searchForm,
        showSearchForm,
        handleSearch,
        handleReset,
      };
    },
  });
</script>

<style lang="less" scoped>

  .business-container {
    width: 100%;
    max-width: 1506px;
    margin: 0 auto;
    padding: 20px;

    .search-form-container {
      background: linear-gradient(135deg, #ffffff 0%, #e4eaf96e 100%);
      // border-radius: 12px;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
      padding: 20px;
      margin-bottom: 20px;
      border: 1px solid #e8e8e8;
      // max-width: 1000px; 
      margin-left: auto;
      margin-right: auto;
      width: 100%;

      .search-form {
        :deep(.ant-form-item) {
          // margin-bottom: 12px;
        }

        :deep(.ant-form-item-label) {
          label {
            font-weight: 500;
            color: #555;
          }
        }

        :deep(.ant-input) {
          // border-radius: 6px;
        }

        :deep(.ant-select) {
          // border-radius: 6px;
        }
      }
    }

    .card-list {
      display: flex;
      flex-wrap: wrap;
      gap: 24px;
      justify-content: center;
      // min-height: 300px;
      transition: opacity 0.3s ease-in-out;
    }

    .card-item {
      position: relative;
      background: linear-gradient(135deg, #ffffff 0%, #f8faff 100%);
      // border-radius: 12px;
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
      padding: 24px;
      width: calc(33.33% - 66px);
      box-sizing: border-box;
      transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
      border: 1px solid #e8e8e8;
      overflow: hidden;
      display: flex;
      flex-direction: column;

      .status-tag {
        position: absolute;
        top: 0;
        right: 0;
        font-size: 12px;
        font-weight: bold;
        padding: 4px 12px;
        // border-bottom-left-radius: 8px;
        color: white;
        width: 60px; /* 固定宽度 */
        text-align: center; /* 文字居中 */
        z-index: 2; /* 提高 z-index 确保覆盖 */

        &.active {
          // background: linear-gradient(to right, rgb(9, 130, 69), rgb(38, 233, 47));
          background:  rgb(9, 130, 69);
        }

        &.inactive {
          background: #ccc;
        }
      }

      .card-content {
        display: flex;
        flex-direction: column;
        gap: 12px;
        padding-top: 12px;
        position: relative;
        z-index: 1;
      }

      .card-title {
        font-size: 18px;
        font-weight: bold;
        color: #000;
      }

      .card-field {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: #555;

        .label {
          min-width: 70px;
          font-weight: bold;
          color: #888;
        }

        .value {
          flex: 1;
          color: #33333385;
          word-break: break-word;
        }

        .admin-icon {
          url: url('../../../../assets/images/logo-sign.png');
          // margin-right: 8px;
          font-size: 16px;
          display: inline-flex;
          align-items: center;
          justify-content: center;
        }
      }

      .card-field.admin {
        .icon {
          font-size: 18px; /* 调整图标大小 */
        }
      }

      .card-actions {
        margin-top: auto;
        display: flex;
        justify-content: center;
        align-items: center; /* 垂直居中 */
        // border-radius: 8px;
      }

      .ant-btn-primary {
        // border-radius: 8px;
        width: 60%;
        height: 40px;
        margin-top: 16px;
        
      }
    }

    // 空态和加载态
    .loading-state,
    .empty-state {
      padding: 160px 0;
      text-align: center;
      color: #555;
      font-size: 16px;
    }

    // 分页样式
    .pagination-wrapper {
      margin-top: 30px;
      display: flex;
      justify-content: center;
      // background-color: #f5faff;
      padding: 10px 0;
      // border-radius: 8px;
    }


  }

    :deep(.ant-pagination) {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
      color: rgba(0, 0, 0, 0.85);
      font-size: 14px;
      font-variant: tabular-nums;
      line-height: 1.5715;
      list-style: none;
      position: relative;
      border-top: 0px solid #eee;


    }

  // 响应式适配
  @media (max-width: 1200px) {
    .business-container .card-item {
      width: calc(50% - 12px);
    }
  }

  @media (max-width: 768px) {
    .business-container .card-item {
      width: 100%;
    }

    .card-field {
      flex-direction: column;
      align-items: flex-start;
      gap: 4px;
    }

    .card-field .label {
      min-width: 100%;
      font-weight: normal;
      color: #999;
    }

    .card-field.admin {
      .icon {
        margin-right: 4px; /* 调整间距 */
      }
    }
  }

  .business-scrollbar {
    background: linear-gradient(135deg, #ffffff 0%, #e4eaf96e 100%);
    // border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
    border: 1px solid #e8e8e8;
    padding: 20px;
    box-sizing: border-box;
  }

// 确保内部内容居中显示
// .business-scrollbar :deep(.scrollbar__wrap) {
//   display: flex;
//   justify-content: center;
// }
</style>

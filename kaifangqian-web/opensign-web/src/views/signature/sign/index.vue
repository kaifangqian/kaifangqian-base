<template>
  <div class="seal-main" v-if="tenantInfo.authStatus === 2">
    <div class="signature-container">
      <!-- 新增签名按钮 -->
      <!-- <div class="signature-card add-card" @click="handleAdd">
        <div class="card-content">
          <div class="add-icon">+</div>
          <div class="add-text">新增签名</div>
        </div>
      </div> -->
      
      <!-- 签名卡片列表 -->
      <div class="signature-cards">
        <div class="signature-card add-card" @click="handleAdd">
          <div class="card-content">
            <div class="add-icon">+</div>
            <div class="add-text">添加签名</div>
          </div>
        </div>
        <div 
          v-for="(record, index) in tableData" 
          :key="record.sealId || index" 
          class="signature-card"
        >
          <div class="card-header">
            <div class="preview-img">
              <img 
                :src="record.image" 
                alt="签名预览" 
                v-if="record.image"
              />
              <div v-else class="no-image-placeholder">暂无图片</div>
            </div>
          </div>
          <div class="card-body">
            <!-- <div class="card-title">{{ record.sealName || '未命名' }}</div> -->
            <div class="card-info">
              <div class="info-item">
                <span class="label">名称：</span>
                <span class="value">{{ record.sealName || '未命名' }}</span>
              </div>
              <div class="info-item">
                <span class="label">创建时间：</span>
                <span class="value">{{ record.createTime || '未知' }}</span>
              </div>
              <div class="card-actions"> 
                <a-button type="primary" danger  size="default" @click="signDeleteInfo(record.sealId)">删除</a-button>
              </div>
            </div>
          </div>
          <!-- <div class="card-footer">
            <a-button type="link" size="small" @click="signDeleteInfo(record.sealId)">删除</a-button>
            <a-button 
              type="link" 
              size="small" 
              v-if="record.isDefault !== 1"
              @click="setSignDefault(record.sealId)"
            >
              设为默认
            </a-button>
          </div> -->
        </div>
      </div>
    </div>
    
    <!-- 分页组件 -->
    <!-- <div class="pagination-container">
      <a-pagination
        v-model:current="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        show-size-changer
        show-jumper
        show-less-items
        @change="handlePageChange"
        @showSizeChange="handlePageSizeChange"
      />
    </div> -->
    
    <AddSignatureModal @register="registerModal" @success="handleSuccess"/>
  </div>
  <Unauthenticated :index="2" class="seal-main" v-if="tenantInfo.authStatus !== 2" />
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import { message } from 'ant-design-vue';
  import { useModal } from '/@/components/Modal';
  import { useRouter } from 'vue-router';
  import AddSignatureModal from "./modal/AddSignatureModal.vue";
  import { getImgBase64 } from '/@/api/sys/upload';
  import { signList, signDefault, signDelete } from "./api";
  import Unauthenticated from "/@/views/signature/authorisation/modal/Unauthenticated.vue";
  import { useUserStore } from '/@/store/modules/user';

  export default defineComponent({
    name: 'App',
    components: { 
      AddSignatureModal, Unauthenticated
    },
    setup() {
      const router = useRouter();
      const userStore = useUserStore();
      const tenantInfo = ref({ ...userStore.getTenantInfo });
      
      // 分页相关数据
      const currentPage = ref(1);
      const pageSize = ref(1000);
      const total = ref(0);
      const tableData = ref([]);
      
      // 获取签名列表
      async function fetchSignatureList() {
        try {
          console.log('正在获取签名列表，当前页:', currentPage.value, '每页:', pageSize.value);
          
          const params = {
            pageNo: currentPage.value,
            pageSize: pageSize.value
          };
          
          const res  = await signList(params);
          console.log('API返回结果:', res);
          
          if (res && Array.isArray(res.records)) {
            console.log('处理签名列表数据...');
            total.value = res.total;
            tableData.value = res.records;
            
            // 处理图片
            for (let i = 0; i < tableData.value.length; i++) {
              try {
                const imgResult = await getImgBase64({ imgId: tableData.value[i].annexId });
                tableData.value[i].image = imgResult.image;
              } catch (error) {
                console.error('获取图片失败:', error);
                tableData.value[i].image = null;
              }
            }
          }
        } catch (error) {
          console.error('获取签名列表失败:', error);
          message.error('获取签名列表失败');
        }
      }

      // 页面切换
      function handlePageChange(page: number) {
        currentPage.value = page;
        fetchSignatureList();
      }

      // 每页数量改变
      function handlePageSizeChange(current: number, size: number) {
        pageSize.value = size;
        currentPage.value = current;
        fetchSignatureList();
      }

      // 模态框管理
      const [registerModal, { openModal, closeModal }] = useModal();

      // 处理函数
      function handleAdd() {
        openModal(true, {
          isUpdate: false,
        });
      }

      function handleSuccess() {
        fetchSignatureList(); // 重新获取数据
      }

      async function setSignDefault(sealId) {
        const result = await signDefault({ sealId: sealId });
        if (result.code == 200) {
          fetchSignatureList(); // 重新获取数据
          message.success("设置默认签名成功！");
        }
      }

      async function signDeleteInfo(sealId) {
        const result = await signDelete({ sealId: sealId });
        if (result.code == 200) {
          fetchSignatureList(); // 重新获取数据
          message.success("删除成功！");
        }
      }

      // 组件挂载时获取数据
      onMounted(() => {
        fetchSignatureList();
      });

      return {
        currentPage,
        pageSize,
        total,
        tableData,
        handleAdd,
        registerModal,
        handleSuccess,
        signDeleteInfo,
        setSignDefault,
        tenantInfo,
        handlePageChange,
        handlePageSizeChange,
      };
    }
  });
</script>

<style lang="less" scoped>
  .seal-main {
    // width: 1080px;
    margin: 0 auto;
    height: calc(100vh - 160px);
    overflow: auto;
    padding: 20px;
  }

  .signature-container {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    margin-bottom: 20px;
  }

  .signature-cards {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    width: 100%;
  }

  .signature-card {
    // flex: 1;
    // width: calc(33.33% - 10px);
    width:280px;
    height: 220px;
    // min-width: 280px;
    // max-width: 320px;
    border: 1px solid #e5e5e5;
    border-radius: 8px;
    background-color: white;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    // cursor: pointer;
    flex-shrink: 0;
    position: relative;
    &:hover {
      // transform: translateY(-2px);
      border-color: #1890ff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
    
    &.add-card {
      display: flex;
      justify-content: center;
      align-items: center;
      height: 220px;
      width:280px;
      border: 1px solid #d9d9d9;
      color: #1890ff;
      font-size: 14px;
      cursor: pointer;
      flex-direction: row;
      background-image: linear-gradient(180deg, rgba(39, 110, 249, 0) 0%, rgba(39, 110, 249, 0.06) 100%);
      
      &:hover {
        // border-color: #1890ff;
        background-image: linear-gradient(180deg, rgba(39, 110, 249, 0) 0%, rgba(0, 85, 254, 0.131) 100%);
        background-color: #f5f5f5;
      }
    }

    .card-content {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: center;
      height: 100%;
      gap: 10px;
    }
    
    &.default-card {
      border-color: #1890ff;
      box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.1);
    }
  }

  .card-header {
    padding: 16px;
    text-align: center;
    background-color: #f8f9fa;
    border-bottom: 1px solid #e5e5e5;
    border-radius: 8px 8px 0 0;
  }

  .preview-img {
    width: 100%;
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    img {
      max-width: 100%;
      max-height: 100%;
      object-fit: contain;
    }
  }

  .no-image-placeholder {
    color: #999;
    font-size: 14px;
    text-align: center;
  }

  .card-body {
    padding: 16px;
  }

  .card-title {
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 8px;
    color: #333;
  }

  .card-info {
    margin-top: 8px;
    font-size: 12px;
    color: #666;
    position: relative;

  }
  .card-actions {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(255, 255, 255, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: 10;
  }

.card-actions a-button {
  background-color: transparent;
  border: none;
  color: #ff4d4f;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.signature-card:hover .card-actions {
  opacity: 1;
}

  .info-item {
    margin-bottom: 4px;
    display: flex;
    align-items: center;
  }

  .label {
    width: 60px;
    color: #999;
  }

  .value {
    flex: 1;
  }

  .card-footer {
    padding: 12px 16px;
    border-top: 1px solid #e5e5e5;
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }

  .add-icon {
    font-size: 24px;
    margin-bottom: 5px;
  }

  .add-text {
    font-size: 14px;
    color: #1890ff;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
    padding-right: 20px;
  }

  :deep(.ant-pagination) {
    position: sticky;
    width: 100%;
    bottom: -1px;
  }
</style>
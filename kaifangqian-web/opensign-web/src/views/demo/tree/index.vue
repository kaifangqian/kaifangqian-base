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
  <div title="Tree基础示例">
    <Row :gutter="[16, 16]">
      <Col :span="8">
        <BasicTree title="基础示例，默认展开第一层" :treeData="treeData" defaultExpandLevel="1">
          <template #title> 123123 </template>
        </BasicTree>
      </Col>
      <Col :span="8">
        <BasicTree
          title="可勾选，默认全部展开"
          :treeData="treeData"
          :checkable="true"
          defaultExpandAll
          @check="handleCheck"
        />
      </Col>
      <Col :span="8">
        <BasicTree
          title="指定默认展开/勾选示例"
          :treeData="treeData"
          :checkable="true"
          :expandedKeys="['0-0']"
          :checkedKeys="['0-0']"
        />
      </Col>
      <Col :span="8">
        <BasicTree
          title="懒加载异步树"
          ref="asyncTreeRef"
          :treeData="tree"
          :load-data="onLoadData"
        />
      </Col>
      <Col :span="8">
        <Card title="异步数据，默认展开">
          <template #extra>
            <a-button @click="loadTreeData" :loading="treeLoading">加载数据</a-button>
          </template>
          <Spin :spinning="treeLoading">
            <BasicTree ref="asyncExpandTreeRef" :treeData="tree2" />
          </Spin>
        </Card>
      </Col>
      <Col :span="8">
        <Card title="BasicTree内置加载">
          <template #extra>
            <a-button @click="loadTreeData2" :loading="treeLoading">请求数据</a-button>
          </template>
          <BasicTree ref="loadTreeRef" :treeData="tree2" :loading="treeLoading" />
        </Card>
      </Col>
    </Row>
  </div>
</template>
<script lang="ts">
  import { defineComponent, nextTick, ref, unref } from 'vue';
  import { BasicTree, TreeActionType, TreeItem } from '/@/components/Tree/index';
  import { treeData } from './data';
  import { Card, Row, Col, Spin } from 'ant-design-vue';
  import { cloneDeep } from 'lodash-es';

  export default defineComponent({
    components: { BasicTree, Card, Row, Col, Spin },
    setup() {
      const asyncTreeRef = ref<Nullable<TreeActionType>>(null);
      const asyncExpandTreeRef = ref<Nullable<TreeActionType>>(null);
      const loadTreeRef = ref<Nullable<TreeActionType>>(null);
      const tree2 = ref<TreeItem[]>([]);
      const treeLoading = ref(false);

      function handleCheck(checkedKeys, e) {
        console.log('onChecked', checkedKeys, e);
      }

      function loadTreeData() {
        treeLoading.value = true;
        // 以下是模拟异步获取数据
        setTimeout(() => {
          // 设置数据源
          tree2.value = cloneDeep(treeData);
          treeLoading.value = false;
          // 展开全部
          nextTick(() => {
            console.log(unref(asyncExpandTreeRef));
            unref(asyncExpandTreeRef)?.expandAll(true);
          });
        }, 2000);
      }
      function loadTreeData2() {
        treeLoading.value = true;
        // 以下是模拟异步获取数据
        setTimeout(() => {
          // 设置数据源
          tree2.value = cloneDeep(treeData);
          treeLoading.value = false;
        }, 2000);
      }

      const tree = ref([
        {
          title: 'parent ',
          key: '0-0',
          children:[]
        },
      ]);

      function onLoadData(treeNode) {
        return new Promise((resolve: (value?: unknown) => void) => {
          if (!treeNode.children) {
            resolve();
            return;
          }
          setTimeout(() => {
            const asyncTreeAction: TreeActionType | null = unref(asyncTreeRef);
            console.log(asyncTreeAction,'3333333333')
            if (asyncTreeAction) {
              const nodeChildren = [
                { title: `Child Node ${treeNode.eventKey}-0`, key: `${treeNode.eventKey}-0` },
                { title: `Child Node ${treeNode.eventKey}-1`, key: `${treeNode.eventKey}-1` },
              ];
              asyncTreeAction.updateNodeByKey(treeNode.eventKey, { children: nodeChildren });
              asyncTreeAction.setExpandedKeys([
                treeNode.eventKey,
                ...asyncTreeAction.getExpandedKeys(),
              ]);
            }

            resolve();
            return;
          }, 1000);
        });
      }
      return {
        treeData,
        handleCheck,
        tree,
        onLoadData,
        asyncTreeRef,
        asyncExpandTreeRef,
        loadTreeRef,
        tree2,
        loadTreeData,
        treeLoading,
        loadTreeData2,
      };
    },
  });
</script>

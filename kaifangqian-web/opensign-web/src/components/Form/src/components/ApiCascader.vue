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
  <span class="cascader-area" style="flex:1">
      <a-cascader 
      v-if="isLoadData"
      v-model:value="state"
      :options="options"
      :load-data="loadData"
      change-on-select
      :show-search="{ filter }"
      @change="handleChange"
      :displayRender="handleRenderDisplay"
    >
      <template #suffixIcon v-if="loading">
        <LoadingOutlined spin />
      </template>
      <template #notFoundContent v-if="loading">
        <span>
          <LoadingOutlined spin class="mr-1" />
          请等待数据加载完成
        </span>
      </template>
    </a-cascader>
    <a-cascader 
      v-if="!isLoadData"
      v-model:value="state"
      :options="options"
      change-on-select
      @change="handleChange"
      placeholder="请选择"
    >
      <template #suffixIcon v-if="loading">
        <LoadingOutlined spin />
      </template>
      <template #notFoundContent v-if="loading">
        <span>
          <LoadingOutlined spin class="mr-1" />
          请等待数据加载完成
        </span>
      </template>
    </a-cascader>
  </span>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, unref, watch, watchEffect } from 'vue';
  import { Cascader } from 'ant-design-vue';
  import { propTypes } from '/@/utils/propTypes';
  import { isFunction } from '/@/utils/is';
  import { treeToArray } from '/@/utils';
  import { get, omit } from 'lodash-es';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { eachTree } from '/@/utils/helper/treeHelper';

  import { getDictItemsByFixedId,getDictTreeItems } from '/@/api/dict';
  interface Option {
    value: string;
    label: string;
    loading?: boolean;
    isLeaf?: boolean;
    children?: Option[];
  }
  export default defineComponent({
    name: 'ApiCascader',
    components: {
      LoadingOutlined,
      [Cascader.name]: Cascader,
    },
    props: {
      value: {
        type: Array,
      },
      api: {
        type: Function as PropType<(arg?: Recordable) => Promise<Option[]>>,
        default: null,
      },
      isLoadData:propTypes.bool.def(true),
      numberToString: propTypes.bool,
      resultField: propTypes.string.def(''),
      labelField: propTypes.string.def('label'),
      valueField: propTypes.string.def('value'),
      childrenField: propTypes.string.def('children'),
      asyncFetchParamKey: propTypes.string.def('parentCode'),
      immediate: propTypes.bool.def(true),
      // init fetch params
      initFetchParams: {
        type: Object as PropType<Recordable>,
        default: () => ({}),
      },
      // 是否有下级，默认是
      isLeaf: {
        type: Function as PropType<(arg: Recordable) => boolean>,
        default: null,
      },
      displayRenderArray: {
        type: Array,
      },
    },
    emits: ['change', 'defaultChange'],
    setup(props, { emit }) {
      console.log(props,'组件props------------')
      const apiData = ref<any[]>([]);
      const options = ref<Option[]>([]);
      const loading = ref<boolean>(false);
      const emitData = ref<any[]>([]);
      const isFirstLoad = ref(true);
      const keys = ref(<string[]>[]);
      const childOptions = ref<Option[]>([]);
      const selectOptions =ref<Option[]>([]);
      const [state] = useRuleFormItem(props, 'value', 'change', emitData);
      const filter: ShowSearchType['filter'] = (inputValue, path) => {
          return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1);
        };
     

      watch(
        apiData,
        (data) => {
          const opts = generatorOptions(data);
          options.value = opts;
          childOptions.value = opts;
          if(props.value){
            
            selectOptions.value = [];
            keys.value = [];
            Object.values(props.value).map((item,index)=>{
              
              //最后一级不显示
              if(index <  Object.values(props.value).length){
                getLevelOptions(item)
                // getChildren(item)
              }
            })
            handleChange( keys.value,selectOptions.value)
          }
         

        },
        { deep: true },
      );

      async function getLevelOptions(key:string){
        keys.value.push(key);
        // console.log(options.value)
        // let matchTreeLeaf = treeToArray(options.value).filter(i=>i.itemValue==key);
        // console.log(matchTreeLeaf)
        // if(matchTreeLeaf.length){
        //   selectOptions.value.push(matchTreeLeaf[0]);
        //   loadData(matchTreeLeaf);
        // }
        // let nextResult = await getDictItemsByFixedId({parentId:key});
        // const {  childrenField } = props;
        console.log(childOptions.value,'当前节点',key)
        childOptions.value.map(item=>{
          if(item.itemValue == key){
          console.log(item,key,'----------------------------------')
            // Reflect.set(item, childrenField, generatorOptions(nextResult.records));
            selectOptions.value.push(item);
            // getChildren(key)
            // item.children = nextResult.records;
            loadData([item]);
          }
        })

        // console.log(options.value,'options数据')
        // eachTree(options.value,(item, _parent)=>{
        //   if(item.itemValue == key){
        //   console.log(item,key,'----------------------------------')
        //     // Reflect.set(item, childrenField, generatorOptions(nextResult.records));
        //     selectOptions.value.push(item);
        //     // item.children = nextResult.records;
        //     loadData([item]);
        //   }
        // })


        // console.log( options.value, '当前选项')
        
        // let parentItem = options.value.filter(item=>item.itemValue==key);
        // let parentItem = getChidlren(key,options.value);
        // console.log(parentItem)
        // const {  childrenField } = props;
       
        // if(nextResult){
        //   Reflect.set(parentItem, childrenField, generatorOptions(nextResult.records));
        // }
        // // handleChange(options.value,parentItem,)
        // // console.log(options.value,' 省市区信息')
        

        // // console.log( options.value)
        // // console.log(parentItem,'节点信息11111')
        // // if(parentItem.length){
        //   if(parentItem){
        //     loadData([parentItem])
        //   }
        // // }
      }
      async function getChildren(key){
        let nextResult = await getDictItemsByFixedId({parentId:key});
        if(nextResult){
          childOptions.value = nextResult.records;
          selectOptions.value.push
        }
      }

      function generatorOptions(options: any[]): Option[] {
        const { labelField, valueField, numberToString, childrenField, isLeaf } = props;
        return options.reduce((prev, next: Recordable) => {
          if (next) {
            const value = next[valueField];
            const item = {
              ...omit(next, [labelField, valueField]),
              label: next[labelField],
              value: numberToString ? `${value}` : value,
              isLeaf: isLeaf && typeof isLeaf === 'function' ? isLeaf(next) : false,
            };
            const children = Reflect.get(next, childrenField);
            if (children) {
              Reflect.set(item, childrenField, generatorOptions(children));
            }
            prev.push(item);
          }
          return prev;
        }, [] as Option[]);
      }

      async function initialFetch() {
        const api = props.api;
        if (!api || !isFunction(api)) return;
        apiData.value = [];
        loading.value = true;
        try {
          const res = await api(props.initFetchParams);
          if (Array.isArray(res)) {
            apiData.value = res;
            return;
          }
          if (props.resultField) {
            apiData.value = get(res, props.resultField) || [];
          }
        } catch (error) {
          console.warn(error);
        } finally {
          loading.value = false;
        }
      }

      async function loadData(selectedOptions: Option[]) {
        console.log(selectedOptions,'当前节点信息')
        const targetOption = selectedOptions[selectedOptions.length - 1];
        // targetOption.loading = true;

        const api = props.api;
        if (!api || !isFunction(api)) return;
        try {
          const res = await api({
            [props.asyncFetchParamKey]: Reflect.get(targetOption, 'value'),
          });
          if (Array.isArray(res)) {
            const children = generatorOptions(res);
            targetOption.children = children;
            return;
          }
          if (props.resultField) {
            const children = generatorOptions(get(res, props.resultField) || []);
            console.log(children,'接口查到的数据 ')
            childOptions.value = children;
            targetOption.children = children;
          }
        } catch (e) {
          console.error(e);
        } finally {
          // targetOption.loading = false;
        }
      }

      watchEffect(() => {
        props.immediate && initialFetch();
      });

      watch(
        () => props.initFetchParams,
        () => {
          !unref(isFirstLoad) && initialFetch();
        },
        { deep: true },
      );

      function handleChange(keys, args) {
        console.log(keys, args,'事件')
        emitData.value = keys;
        emit('defaultChange', keys, args);
      }

      function handleRenderDisplay({ labels, selectedOptions }) {
        if (unref(emitData).length === selectedOptions.length) {
          return labels.join(' / ');
        }
        if (props.displayRenderArray) {
          return props.displayRenderArray.join(' / ');
        }
        return '';
      }

      return {
        state,
        options,
        loading,
        filter,
        handleChange,
        loadData,
        handleRenderDisplay,
      };
    },
  });
</script>

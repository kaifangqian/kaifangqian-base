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
    <slot name="prefixDefaultItem"></slot>
    <a-form-item>
      <a-select v-model:value="value1" :options="options" @change="handleSelectChange(value1,0)" :allowClear="true" placeholder="请选择"></a-select>
    </a-form-item>
    <a-form-item v-if="selectCount>=2">
      <a-select v-model:value="value2" :options="options2"  @change="handleSelectChange(value2,1)" :allowClear="true" placeholder="请选择"></a-select>
    </a-form-item>
    <a-form-item v-if="selectCount==3">
      <a-select v-model:value="value3" :options="options3" @change="handleSelectChange(value3,2)" :allowClear="true"  placeholder="请选择"></a-select>
    </a-form-item>
    <slot name="suffixDefaultItem"></slot>
  </span>
</template>
<script lang="ts">
  import { defineComponent, PropType, ref, unref, watch, watchEffect, onMounted } from 'vue';
  import { Cascader } from 'ant-design-vue';
  import { propTypes } from '/@/utils/propTypes';
  import { isFunction } from '/@/utils/is';
  import { get, omit } from 'lodash-es';
  import { useRuleFormItem } from '/@/hooks/component/useFormItem';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { getDictItemsByFixedId } from '/@/api/dict';
  interface Option {
    value: string;
    label: string;
    loading?: boolean;
    isLeaf?: boolean;
    children?: Option[];
  }
  export default defineComponent({
    name: 'SelectCascader',
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
      selectCount:{
        type:Number,
        default:3
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
    emits: ['change', 'defaultChange', 'valueChange'],
    setup(props, { emit }) {
      console.log(props,'组件props------------')
      const apiData = ref<any[]>([]);
      const options = ref<Option[]>([]);
      const options1 = ref<Option[]>([]);
      const options2 = ref<Option[]>([]);
      const options3 = ref<Option[]>([]);
      const loading = ref<boolean>(false);
      const emitData = ref<any[]>([]);
      const isFirstLoad = ref(true);
      const keys = ref(<string[]>[]);
      const value1 = ref('');
      const value2 = ref('');
      const value3 = ref('');
      const childOptions = ref<Option[]>([]);
      const selectOptions =ref<Option[]>([]);
      const [state] = useRuleFormItem(props, 'value', 'change', emitData);
      const filter: ShowSearchType['filter'] = (inputValue, path) => {
          return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1);
        };
        onMounted(()=>{
          if(props.value){
            selectOptions.value = [];
            console.log(props.value,'默认值')
            Object.values(props.value).map((item:string,index:number)=>{
              if(index==0){
                value1.value = item
              }
              if(index==1){
                value2.value = item
              }
              if(index==2){
                value3.value = item
              }
              //最后一级不显示
              if(index+1 <  Object.values(props.value).length){
                getChildren(item,index)
              }
            })

          }

        })

      
     

      watch(
        apiData,
        (data) => {
          const opts = generatorOptions(data);
          options.value = opts;
          options1.value = opts;

          childOptions.value = opts;
        },
        { deep: true },
      );

     

      function handleSelectChange(key,index){
        if(index==0){
          options2.value = []
          value2.value = '';
          emit('valueChange',[unref(value1)])
         
        }
        if(index==1){
          options3.value =[]
          value3.value = '';
          emit('valueChange',[unref(value1),unref(value2)])
        }
        if(index==2){
          emit('valueChange',[unref(value1),unref(value2),unref(value3)])
        }
        getChildren(key,index);
      }

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
        // console.log(childOptions.value,'当前节点',key)
        childOptions.value.map(item=>{
          if(item.itemValue == key){
            // Reflect.set(item, childrenField, generatorOptions(nextResult.records));
            selectOptions.value.push(item);
            // getChildren(key)
            // item.children = nextResult.records;
            loadData([item]);
          }
        })
    
      }
      async function getChildren(key,index){
        let nextResult = await getDictItemsByFixedId({parentId:key,pageSize:10000});
        if(nextResult){
         if(index+1 == 1){
            options2.value = generatorOptions(nextResult.records)
         }
         if(index+1 == 2){
            options3.value = generatorOptions(nextResult.records)
         }
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
            // console.log(options,'---------新的options-----')
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
          const res = await api({...props.initFetchParams,pageSize:1000});
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
        const targetOption = selectedOptions[selectedOptions.length - 1];
        // targetOption.loading = true;

        const api = props.api;
        if (!api || !isFunction(api)) return;
        try {
          const res = await api({
            ...props.initFetchParams,
            [props.asyncFetchParamKey]: Reflect.get(targetOption, 'value'),
          });
          if (Array.isArray(res)) {
            const children = generatorOptions(res);
            targetOption.children = children;
            return;
          }
          if (props.resultField) {
            const children = generatorOptions(get(res, props.resultField) || []);
            targetOption.children = children;
            childOptions.value = children;

          }
          console.log(childOptions,'oooooooooo')
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
        options2,
        options3,
        loading,
        value1,
        value2,
        value3,
        filter,
        handleSelectChange,
        handleChange,
        loadData,
        handleRenderDisplay,
      };
    },
  });
</script>
<style lang="less" scoped>
   .cascader-area{
    display:flex;
    :deep(.ant-form-item){
      width:100%;
    }
  }
</style>

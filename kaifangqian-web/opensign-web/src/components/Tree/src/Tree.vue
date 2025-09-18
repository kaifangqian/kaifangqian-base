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

<script lang="tsx">
  import type { CSSProperties } from 'vue';
  import type { FieldNames, TreeState, TreeItem, KeyType, CheckKeys, TreeActionType } from './tree';

  import {
    defineComponent,
    reactive,
    computed,
    unref,
    ref,
    watchEffect,
    toRaw,
    watch,
    onMounted,
  } from 'vue';
  import TreeHeader from './TreeHeader.vue';
  import TreeResult from './TreeResult.vue';
  import { Tree, Spin, Empty } from 'ant-design-vue';
  import { TreeIcon } from './TreeIcon';
  import { ScrollContainer } from '/@/components/Container';
  import {BasicToolTip} from '/@/components/ToolTip'
  import { omit, get, difference, cloneDeep } from 'lodash-es';
  import { isArray, isBoolean, isEmpty, isFunction } from '/@/utils/is';
  import { extendSlots, getSlot } from '/@/utils/helper/tsxHelper';
  import { filter, treeToList, eachTree } from '/@/utils/helper/treeHelper';
  import { useTree } from './useTree';
  import { useContextMenu } from '/@/hooks/web/useContextMenu';
  import { CreateContextOptions } from '/@/components/ContextMenu';
  import { treeEmits, treeProps } from './tree';
  import { createBEM } from '/@/utils/bem';
  import { CarryOutOutlined, SmileTwoTone} from '@ant-design/icons-vue';
  import { SvgIcon } from '/@/components/Icon';

  export default defineComponent({
    name: 'BasicTree',
    inheritAttrs: false,
    props: treeProps,
    emits: treeEmits,
    components: {
      CarryOutOutlined,
      SmileTwoTone,
      SvgIcon 
    },
    setup(props, { attrs, slots, emit, expose }) {
      const [bem] = createBEM('tree');
      const state = reactive<TreeState>({
        checkStrictly: props.checkStrictly,
        expandedKeys: props.expandedKeys || [],
        selectedKeys: props.selectedKeys || [],
        checkedKeys: props.checkedKeys || [],
      });
      const searchState = reactive({
        startSearch: false,
        searchText: '',
        searchData: [] as TreeItem[],
      });

      const treeDataRef = ref<TreeItem[]>([]);

      // const searchTreeData = computed(()=>{
      //   const {searchApiData} = props;
      //   console.log(searchApiData,'--搜索结果-')
      //   return searchApiData;
      // })

      const panelShow = ref(true)

      const [createContextMenu] = useContextMenu();

      const getFieldNames = computed((): Required<FieldNames> => {
        const { fieldNames } = props;
        return {
          children: 'children',
          title: 'title',
          key: 'key',
          ...fieldNames,
        };
      });

      const getBindValues = computed(() => {
        let propsData = {
          blockNode: true,
          ...attrs,
          ...props,
          expandedKeys: state.expandedKeys,
          selectedKeys: state.selectedKeys,
          checkedKeys: state.checkedKeys,
          checkStrictly: state.checkStrictly,
          fieldNames: unref(getFieldNames),
          'onUpdate:expandedKeys': (v: KeyType[]) => {
            state.expandedKeys = v;
            emit('update:expandedKeys', v);
          },
          'onUpdate:selectedKeys': (v: KeyType[]) => {
            state.selectedKeys = v;
            emit('update:selectedKeys', v);
          },
          onCheck: (v: CheckKeys, e) => {
            let currentValue = toRaw(state.checkedKeys) as KeyType[];
            if (isArray(currentValue) && searchState.startSearch) {
              const { key } = unref(getFieldNames);
              currentValue = difference(currentValue, getChildrenKeys(e.node.$attrs.node[key]));
              if (e.checked) {
                currentValue.push(e.node.$attrs.node[key]);
              }
              state.checkedKeys = currentValue;
            } else {
              state.checkedKeys = v;
            }

            const rawVal = toRaw(state.checkedKeys);
            emit('update:value', rawVal);
            emit('check', rawVal, e);
          },
          onRightClick: handleRightClick,
          onSelect: (v: KeyType[],e) =>{
            emit('select', v, e);
          },
          onExpand: (v: KeyType[], e ) => {
            console.log(v, e,'节点展开收起');
            emit('expand', v, e);
          }
        };
        return omit(propsData, 'treeData', 'class');
      });

      const getTreeData = computed((): TreeItem[] =>
        searchState.startSearch ? searchState.searchData : unref(treeDataRef),
      );

      const getNotFound = computed((): boolean => {
        return !getTreeData.value || getTreeData.value.length === 0;
      });

      const {
        deleteNodeByKey,
        insertNodeByKey,
        insertNodesByKey,
        filterByLevel,
        updateNodeByKey,
        getAllKeys,
        getChildrenKeys,
        getEnabledKeys,
        getSelectedNode,
      } = useTree(treeDataRef, getFieldNames);

      function getIcon(params: Recordable, icon?: string) {
        if (!icon) {
          if (props.renderIcon && isFunction(props.renderIcon)) {
            return props.renderIcon(params);
          }
        }
        return icon;
      }

      async function handleRightClick({ event, node }: Recordable) {
        const { rightMenuList: menuList = [], beforeRightClick } = props;
        let contextMenuOptions: CreateContextOptions = { event, items: [] };

        if (beforeRightClick && isFunction(beforeRightClick)) {
          let result = await beforeRightClick(node, event);
          if (Array.isArray(result)) {
            contextMenuOptions.items = result;
          } else {
            Object.assign(contextMenuOptions, result);
          }
        } else {
          contextMenuOptions.items = menuList;
        }
        if (!contextMenuOptions.items?.length) return;
        createContextMenu(contextMenuOptions);
      }

      function setExpandedKeys(keys: KeyType[]) {
        state.expandedKeys = keys;
      }

      function getExpandedKeys() {
        return state.expandedKeys;
      }
      function setSelectedKeys(keys: KeyType[]) {
        state.selectedKeys = keys;
      }

      function getSelectedKeys() {
        return state.selectedKeys;
      }

      function setCheckedKeys(keys: CheckKeys) {
        state.checkedKeys = keys;
      }

      function getCheckedKeys() {
        return state.checkedKeys;
      }

      function checkAll(checkAll: boolean) {
        state.checkedKeys = checkAll ? getEnabledKeys() : ([] as KeyType[]);
      }

      function expandAll(expandAll: boolean) {
        state.expandedKeys = expandAll ? getAllKeys() : ([] as KeyType[]);
      }

      function refresh(){
        console.log('开始刷新了。。。。')
        emit('refresh')
      }
      function rowClick(row,type){
        emit('rowClick',row,type)
      }
      function loadSearchData(type:string,page:number){
        console.log(type,page,'翻页')
        emit('loadSearchData',type,page)
      }

      function onStrictlyChange(strictly: boolean) {
        state.checkStrictly = strictly;
      }

      watch(
        () => props.searchValue,
        (val) => {
          if (val !== searchState.searchText) {
            searchState.searchText = val;
          }
          if(!val){

          }
        },
        {
          immediate: true,
        },
      );

      watch(
        () => searchState.searchText,
        (val) => {
          if (val) {
           if(!val){
              searchState.startSearch = false;
           }
          }
        },
      );
      watch(
        () => props.treeData,
        (val) => {
          if (val) {
            handleSearch(searchState.searchText);
          }
        },
      );
      watch(
        () => props.searchApiData,
        (v) => {
          if(props.isRemoteSearch){
            searchState.searchData = v;
          }
        },
      );

      function handleSearch(searchValue: string) {
        if (searchValue !== searchState.searchText) searchState.searchText = searchValue;
          emit('update:searchValue', searchValue);
        if (!searchValue) {
          searchState.startSearch = false;
          return;
        }     

        const { filterFn, checkable, expandOnSearch, checkOnSearch, selectedOnSearch, isRemoteSearch} =  unref(props);
        searchState.startSearch = true;
        // 采用远程搜索
        if(isRemoteSearch){
          emit('searchChange', searchValue);
          return 
        }
        const { title: titleField, key: keyField } = unref(getFieldNames);
        const matchedKeys: string[] = [];
        searchState.searchData = filter(
          unref(treeDataRef),
          (node) => {
            const result = filterFn
              ? filterFn(searchValue, node, unref(getFieldNames))
              : node[titleField]?.includes(searchValue) ?? false;
            if (result) {
              matchedKeys.push(node[keyField]);
            }
            return result;
          },
          unref(getFieldNames),
        );

        if (expandOnSearch) {
          const expandKeys = treeToList(searchState.searchData).map((val) => {
            return val[keyField];
          });
          if (expandKeys && expandKeys.length) {
            setExpandedKeys(expandKeys);
          }
        }

        if (checkOnSearch && checkable && matchedKeys.length) {
          setCheckedKeys(matchedKeys);
        }

        if (selectedOnSearch && matchedKeys.length) {
          setSelectedKeys(matchedKeys);
        }
      }

      function handleClickNode(key: string, children: TreeItem[]) {
        if (!props.clickRowToExpand || !children || children.length === 0) return;
        if (!state.expandedKeys.includes(key)) {
          setExpandedKeys([...state.expandedKeys, key]);
        } else {
          const keys = [...state.expandedKeys];
          const index = keys.findIndex((item) => item === key);
          if (index !== -1) {
            keys.splice(index, 1);
          }
          setExpandedKeys(keys);
        }
      }
      function toggleTreePanel(){
        panelShow.value = !panelShow.value
        emit('changePanelShow', panelShow.value)
      }

      watchEffect(() => {
        treeDataRef.value = props.treeData as TreeItem[];
      });

      onMounted(() => {
        const level = parseInt(props.defaultExpandLevel);
        if (level > 0) {
          state.expandedKeys = filterByLevel(level);
        } else if (props.defaultExpandAll) {
          expandAll(true);
        }
      });

      watchEffect(() => {
        state.expandedKeys = props.expandedKeys;
      });

      watchEffect(() => {
        state.selectedKeys = props.selectedKeys;
      });

      watchEffect(() => {
        state.checkedKeys = props.checkedKeys;
      });

      watch(
        () => props.value,
        () => {
          state.checkedKeys = toRaw(props.value || []);
        },
        { immediate: true },
      );

      watch(
        () => state.checkedKeys,
        () => {
          const v = toRaw(state.checkedKeys);
          emit('update:value', v);
          emit('change', v);
        },
      );

      watchEffect(() => {
        state.checkStrictly = props.checkStrictly;
      });

      const instance: TreeActionType = {
        setExpandedKeys,
        getExpandedKeys,
        setSelectedKeys,
        getSelectedKeys,
        setCheckedKeys,
        getCheckedKeys,
        insertNodeByKey,
        insertNodesByKey,
        deleteNodeByKey,
        updateNodeByKey,
        getSelectedNode,
        checkAll,
        expandAll,
        refresh,
        filterByLevel: (level: number) => {
          state.expandedKeys = filterByLevel(level);
        },
        setSearchValue: (value: string) => {
          handleSearch(value);
        },
        getSearchValue: () => {
          return searchState.searchText;
        },
      };

      function renderAction(node: TreeItem) {
        const { actionList } = props;
        if (!actionList || actionList.length === 0) return;
        return actionList.map((item, index) => {
          let nodeShow = true;
          if (isFunction(item.show)) {
            nodeShow = item.show?.(node);
          } else if (isBoolean(item.show)) {
            nodeShow = item.show;
          }

          if (!nodeShow) return null;

          return (
            <span key={index} class={bem('action')}>
              {item.render(node)}
            </span>
          );
        });
      }
      async function handleNodeMenu(e,node){
        e.stopPropagation()
        const { beforeRightClick ,rightMenuList:menuList = []} = props;
        let contextMenuOptions:CreateContextOptions = { event:e, items: [] }
        if (beforeRightClick && isFunction(beforeRightClick)) {
          let result = await beforeRightClick(node, e);
          if (Array.isArray(result)) {
            contextMenuOptions.items = result;
          } else {
            Object.assign(contextMenuOptions, result);
          }
        } else {
          contextMenuOptions.items = menuList;
        }
        createContextMenu(contextMenuOptions);
      }

      const treeData = computed(() => {
        const data = cloneDeep(getTreeData.value);
        const { switchIcon } = props;
        // const data = getTreeData.value;
        eachTree(data, (item, _parent) => {
          const searchText = searchState.searchText;
          const { highlight } = unref(props);
          const {
            title: titleField,
            key: keyField,
            children: childrenField,
          } = unref(getFieldNames);

          const icon = getIcon(item, item.icon);
          const title = get(item, titleField);
          const expandedIcon = item.expandedIcon;
          // 文件分组 必须要求为group
          const isGroup =  item.type=== 'group'?true:false;

          const searchIdx = searchText ? title.indexOf(searchText) : -1;
          const isHighlight =
            searchState.startSearch && !isEmpty(searchText) && highlight && searchIdx !== -1;
          const highlightStyle = `color: ${isBoolean(highlight) ? '#f50' : highlight}`;

          const titleDom = isHighlight ? (
            <span class={unref(getBindValues)?.blockNode ? `${bem('content')}` : ''}>
              <span>{title.substr(0, searchIdx)}</span>
              <span style={highlightStyle}>{searchText}</span>
              <span>{title.substr(searchIdx + (searchText as string).length)}</span>
            </span>
          ) : (
            <BasicToolTip  content={title}  class="wid190"  refName={"tooltip"+title}>
               <span class="tree-title">{title}</span>
            </BasicToolTip>
          );
          item[titleField] = (
            <span
              class={`${bem('title')}`}
              onClick={handleClickNode.bind(null, item[keyField], item[childrenField])}
            >
              {slots?.title ? (
                getSlot(slots, 'title', item)
              ) : (
                <>
                  {icon && !isGroup && <TreeIcon icon={icon} color={item.color}/> }
                  {switchIcon && isGroup && <SvgIcon name={expandedIcon?'category-open':'category-close' }/> }
                  {titleDom}
                  {
                    <span class={bem('actions')} onClick={(e)=>handleNodeMenu(e,item)}>{renderAction(item)}</span>
                  }
                </>
              )}
            </span>
          );
          return item;
        });
        return data;
      });

      expose({instance});

      return () => {
        const { title, helpMessage, toolbar, search, checkable, toggleSwitch, checkStrictlySwitch } = props;
        const showTitle = title || toolbar || search || slots.headerTitle;
        // const showToggle = toggleSwitch;
        const scrollStyle: CSSProperties = { height: 'calc(100% - 38px)' };
        return (
          <div class={bem('panel')}>
           
            {true?
                <div class={[bem(), 'h-full', attrs.class,unref(panelShow)?'open':'closed']}>
                  {showTitle && (
                    <TreeHeader
                      checkable={checkable}
                      checkStrictlySwitch={checkStrictlySwitch}
                      checkAll={checkAll}
                      expandAll={expandAll}
                      refresh={refresh}
                      title={title}
                      search={search}
                      toolbar={toolbar}
                      helpMessage={helpMessage}
                      onStrictlyChange={onStrictlyChange}
                      onSearch={handleSearch}
                      searchText={searchState.searchText}
                    >
                      {extendSlots(slots)}
                    </TreeHeader>
                  )}
                  <div class={bem('tree-action')}>
                    {slots.action?.() }
                  </div>
                  <Spin spinning={unref(props.loading)} tip="加载中...">
                    <ScrollContainer style={scrollStyle} v-show={!unref(getNotFound)}>
                      {
                        searchState.startSearch?
                        <TreeResult searchApiData={props.searchApiData} loadData={loadSearchData} rowClick={rowClick}/>:
                        <Tree {...unref(getBindValues)} showIcon={false} treeData={treeData.value}></Tree>
                      }
                    </ScrollContainer>
                    <Empty
                      v-show={unref(getNotFound)}
                      image={Empty.PRESENTED_IMAGE_SIMPLE}
                      class="!mt-4"
                    />
                  </Spin>
                </div>
            :<></>}
          </div>
      );
      };
    },
  });
</script>

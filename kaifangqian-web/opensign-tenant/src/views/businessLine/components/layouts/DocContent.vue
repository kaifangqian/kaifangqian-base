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
    <div class="doc-layout-container">
        <slot></slot>
        <div class="doc-content" @scroll="handleScroll"
        :style="[
          'width:' + (containerWidth) + 'px',
        ]">
            <!-- <a-spin :spinning="spinning"> -->
            <!-- <Scrollbar width="100%" height="100%" ref="scrollbarRef"> -->
              <div  class="zoom-container" 
                :style="[
                  'width:' + nowDocument.maxWidth * (zoom / 100) + 'px',
                  'height: '+ (nowDocument.imageAllHeight) * (zoom / 100) +'px;',
                  'margin:0 auto',
                ]">
                <div class="document-list" :style="[
                   'height: '+ (nowDocument.imageAllHeight) +'px;',
                    'width:'+ nowDocument.maxWidth + 'px;',
                    'transform:scale('+ zoom / 100 +')',
                    // 'transform-origin: 50% 0%'
                    'transform-origin:' + ((zoom > 100)? '50% 0%': '0 0'),
                    // 'transform-origin: 0 0',
                    'transition: transform 0.5s ease'
                ]" v-if="nowDocument && nowDocument.images && nowDocument.imageLoading">
                    <template v-for="(item,index) in nowDocument.images" :key="index">
                        <div class="document-page group" v-if="item"
                            :style="[
                              'transform: translate('+ '-50%,'+(item.target.transformHeight + (item.page+1) * 16)+'px);',
                              'width:' + item.target.width+'px;',
                              'height' + item.target.height+'px;',

                            ]">
                            <img style="width: 100%;" v-lazy="baseUrl + '/file/downloadFileStream/' + item.annexId" />
                            <div class="img-range-line" v-if="item.showRange" :style="[
                                'height:'+ minTargetInfo.minHeight +'px',
                                'width:'+ minTargetInfo.minWidth +'px',
                                'position: absolute;',
                                'left: 0;',
                                'top: 0;',
                                'border: 1px dashed #00a0e8',
                                ]"></div>
                            <div class="img-range-line" v-if="item.showChopStampRange" :style="[
                                'height:'+ 1 +'px',
                                'width:'+ 100 +'%',
                                'position: absolute;',
                                'left: 0;',
                                'top:' + item.rangHeight + 'px',
                                'border-bottom: 1px dashed #00a0e8',
                                ]"></div>
                                <!-- 页面编号角标 -->
                            <div class="page-corner">
                            </div>
                            <div class="page-num">
                            {{ item.page + 1 }}
                            </div>
                        </div>
                    </template>
                    <draggable :list="nowDocument.activeControl" ghost-class="ghost" draggable=".test" item-key="unequalId"
                        :group="group" :force-fallback="true" drag-class="drag-control" chosen-class="chosenClass" animation="100"
                        :fallback-class="true" :fallback-on-body="true" :touch-start-threshold="50" :fallback-tolerance="50"
                        style="width: 100%;height: 100%;position: relative;" v-if="showControl">
                          <template #item="{ element }">
                              <control-item :doc="nowDocument" :element="element" :isSign="isSign"
                                  :isSubmitControl="isSubmitControl" @controlBatchUse="controlBatchUse"
                                  @controlFocus="controlFocus"
                                  :scroll="true"
                                  :zoom="zoom / 100"
                                  @controlDelete="controlDelete" @controlMousedown="controlMousedown" @clearSeal="clearSeal"
                                  @controlClick="controlClick" @refreshControlPosition="refreshControlPosition" @signDateFormat="signDateFormat">
                                  <a-popover title="批量应用" v-model:visible="element.showPopover" trigger="click"
                                      placement="rightTop" overlayClassName="control-pop" style="max-width: 500px;" :align="{
                                          offset: [160, 0]
                                      }">
                                      <template #content>
                                          <Alert message="应用后该批控件将加盖同一个签章" type="info"
                                              show-icon />
                                          <div class="pop-content">
                                              <p class="sub-title">{{ checkedKeys.length ? '应用文档' + '(' +
                                                  checkedKeys.length +
                                                  ')' : '应用文档' }}</p>
                                              <BasicTable @register="registerTable"
                                                  :rowSelection="{ type: 'checkbox', selectedRowKeys: checkedKeys, onChange: onSelectChange }">

                                              </BasicTable>
                                          </div>
                                          <div class="pop-content">
                                              <p class="sub-title">应用页面</p>
                                              <a-select ref="select" v-model:value="element.oddEventType" style="width: 100%"
                                                  placeholder="请选择应用的页面范围">
                                                  <a-select-option value="all">全部页面</a-select-option>
                                                  <a-select-option value="odd_number">奇数页</a-select-option>
                                                  <a-select-option value="even_number">偶数页</a-select-option>
                                                  <a-select-option value="custom">指定页面</a-select-option>
                                              </a-select>
                                              <div v-if="element.oddEventType == 'custom'">
                                                  <p style="margin-bottom: 0;font-size:14px;font-weight: 550;margin:5px 0;">
                                                      <span
                                                          style="font-size:12px;color:#999;margin-left:0px;">（请输入页码或页码范围，以逗号分隔，如1,2,7-10）</span>
                                                  </p>
                                                  <a-input v-model:value="element.custom" placeholder="请输入页码"
                                                      @blur="checkPage(element)"></a-input>
                                                  <p class="custom-value-warning" v-if="!customValueRight">
                                                      <Icon icon="ant-design:exclamation-circle-outlined" size="12"
                                                          color="#f00909" />页码格式不正确
                                                  </p>
                                              </div>

                                          </div>
                                          <div class="pop-action">
                                              <a-button type="default" @click="element.showPopover = false">取消</a-button>
                                              <a-button type="primary" @click="handleSetControl(element)"
                                                  style="margin-left:20px;">确定</a-button>
                                          </div>

                                      </template>

                                  </a-popover>
                              </control-item>
                          </template>
                    </draggable>
                </div>
              </div>
            <!-- </Scrollbar> -->
            <!-- </a-spin> -->

        </div>
        <div class="zoom-control">
          <div class="page-turn">
              <a-button @click="handlePage('pre')" :disabled="currentPage==1">上一页</a-button>
                <span>{{ currentPage  + ' / ' + (nowDocument.images&&nowDocument.images.length) }}</span>
              <a-button @click="handlePage('next')" :disabled="currentPage==(nowDocument.images&&nowDocument.images.length)">下一页</a-button>
          
          </div>
          <a-divider type="vertical" style='margin-right:20px;'/>
          <div class="page-zoom">
            <Icon icon="ant-design:line-outlined" size="20" color="#958787" @click="handleZoom(1)"/>
            <a-input v-model:value="zoom" style="width:60px" suffix="%"></a-input>
            <Icon icon="ant-design:plus-outlined" size="20" color="#958787" @click="handleZoom(2)"/>
          </div>
        
        </div>
    </div>
</template>

<script lang="ts">
import { ref, defineComponent, watch } from "vue";
import { Scrollbar } from '/@/components/Scrollbar';
import draggable from 'vuedraggable';
import { CanvasZoom, docColumns } from '../data/ControlData';
import { throttle } from '/@/utils';
import { getAppEnvConfig } from '/@/utils/env';
import ControlItem from '../items/ControlItem.vue';
import { Icon } from '/@/components/Icon';
import { Alert } from 'ant-design-vue';
import { BasicTable, useTable } from '/@/components/Table';
import { useMessage } from '/@/hooks/web/useMessage';

export default defineComponent({
    name: "DocContent",
    components: {
        Scrollbar,
        draggable,
        ControlItem,
        Icon,
        Alert,
        BasicTable
    },
    props: {
        nowDocument: {
            type: Object,
            default: () => {
              return {}
            }
        },
        docs: {
            type: Array,
            default: () => {
                return []
            }
        },
        // currentPage:{
        //   type: Number,
        //   default: 1
        // },
        showControl: {
            type: Boolean
        },
        isSign: {
            type: Boolean,
            default: false
        },
        showRange: {
            type: Boolean,
            default: false
        },
        hasWriteControl: {
            type: Boolean,
            default: true
        },
        minTargetInfo: {
            type: Object,
            default: () => {
              return {}
            }
        },
        spinning: {
            type: Boolean,
            default: false
        },
        isSubmitControl: {
            type: Boolean,
            default: false
        }
    },
    setup(props, { emit }) {
        const scrollbarRef = ref();
        const customValueRight = ref(true);
        const currentElement: any = ref();
        const group = ref({
            name: "itxst",
            put: true, //允许拖入
            pull: 'clone', //允许拖出

        })
        watch(
          ()=> props.nowDocument.maxWidth,
          (val)=>{
            if(val){
              zoom.value = parseInt( (window.innerWidth - (350+380+ 100)) / val  * 100)
            }
          },
          {deep:true}
        )
        watch(
          ()=> props.hasWriteControl,
          (val)=>{
            if(val){
              containerWidth.value = window.innerWidth -  (350+380 + 50)
            }else{
              containerWidth.value = window.innerWidth -  (350 + 50)
            }
          },
          {deep:true}
        )

        const zoom = ref(134);
        const containerWidth = ref(props.hasWriteControl? window.innerWidth -  (350+380):window.innerWidth -  (350))
        const currentPage = ref(1);
        const handleZoom = (type:number) => {
          if(type === 1){
            zoom.value = Number(zoom.value) - 10;
          }else{
            zoom.value = Number(zoom.value) + 10;
          }
          CanvasZoom.zoom = zoom.value;
          document.getElementsByClassName('doc-content')[0].scrollLeft = ((props.nowDocument.maxWidth * (zoom.value / 100)) - containerWidth.value) / 2;
          emit("handleZoom",zoom.value);
        }
        const checkedKeys = ref<Array<string | number>>([]);

        const { VITE_GLOB_API_URL } = getAppEnvConfig();
        const baseUrl = ref(VITE_GLOB_API_URL);
        const { createMessage: msg } = useMessage();

        const [registerTable, { setSelectedRowKeys, setTableData }] = useTable({
            title: '',
            titleHelpMessage: [],
            columns: docColumns,
            immediate: false,
            fetchSetting: {
                listField: 'records'
            },
            rowKey: 'id',
            useSearchForm: false,
            showIndexColumn: true,
            canResize: false,
            isTriggerSelect: false,
            striped: false,
            showTableSetting: false,
            tableSetting: { fullScreen: false, redo: true, setting: false, size: false },
            pagination: false
        });
        const isScriptScroll = ref(false);
        function handlePage(type){
          isScriptScroll.value = true
          if(type=='pre'){
            if(currentPage.value==1) return;
              currentPage.value--;
              let scrollDis = (props.nowDocument.targets[currentPage.value-1].transformHeight +  currentPage.value * 16) * zoom.value / 100;
              document.getElementsByClassName('doc-content')[0].scrollTo({top: scrollDis, behavior: 'auto'});
              return
          }
          if(type=='next'){
            if(currentPage.value == props.nowDocument.images.length) return;
              currentPage.value++;
              let scrollDis = (props.nowDocument.targets[currentPage.value-1].transformHeight +  currentPage.value * 16) * zoom.value / 100;
              document.getElementsByClassName('doc-content')[0].scrollTo({top: scrollDis, behavior: 'auto'});
              return
          }
        }

        function findPageByHeight(height, data) {
          for (let i = 0; i < data.length; i++) {
              const current = data[i];
              const next = data[i + 1];

              if (height >= (current.transformHeight - 200) && (!next || height < next.transformHeight)) {
                  return current.page + 1;
              }
          }

          // 如果没有找到匹配的页面，则返回一个默认值，例如 -1
          return 1;
      }

        
        function caculateScroll(){

          const images = document.querySelectorAll('.document-page');
          images.forEach((img,index) => {
            if(img.getBoundingClientRect().top > 0 && img.getBoundingClientRect().top < 500){
              currentPage.value = index + 1
            }
          });

            return 
            if(isScriptScroll.value){
              isScriptScroll.value = false;
              return
            }
            let transformHeight =  document.getElementsByClassName('doc-content')[0].scrollTop / (zoom.value / 100) - currentPage.value * 16;
            let page = findPageByHeight(transformHeight, props.nowDocument.targets)
            console.log(page,'page------')
            currentPage.value = page
            // console.log(currentPage.value,'当前页数')
            // console.log(transformHeight,'滚动距离'
              
            // console.log(props.nowDocument.targets,'目标12')
            //   props.nowDocument.targets.map((v,i)=>{
            //   console.log(transformHeight)
            //     if(transformHeight >= v.transformHeight){
            //       if( currentPage.value == props.nowDocument.images.length){
            //           return
            //       }
            //       currentPage.value ++
            //     }
            //     if(transformHeight <= v.transformHeight){
            //       if( currentPage.value == 1){
            //           return
            //       }
            //       currentPage.value --
            //     }
            //   })
        }
        let throttleInput = throttle((e)=>caculateScroll(e), 200)

        function handleScroll(){
          caculateScroll()
        //   throttle( function caculateScroll(){
        //     let scrollDis =  document.getElementsByClassName('doc-content')[0].scrollTop;
        //       console.log(scrollDis)
        // },100)()
          // throttleInput()
        }

        //签署人点击事件
        function controlDelete(item: any) {
            emit("controlDelete", item)
        }
        function controlMousedown(item: any) {
            emit("controlMousedown", item)
        }
        function clearSeal() {

        }
        function controlBatchUse(element) {
            element.showPopover = true;
            console.log(element,'点击的当前控件---')
            if(element.propertyVoList){
                checkedKeys.value = []
                element.propertyVoList.forEach(item=>{
                    if(item.propertyType=='relation_doc'){
                       checkedKeys.value.push(item.propertyValue)
                    }
                    if(item.propertyType=='page_config'){
                        element.oddEventType = item.propertyValue;
                    }
                    if(item.propertyType=='custom'){
                        element.custom = item.propertyValue;
                    }
                })
            }
            currentElement.value = element;
            setTimeout(() => {
                setTableData(props.docs)
                // setSelectedRowKeys([element.signReDocId])
            })
        }
        //批量设置控件日期格式
        function signDateFormat(element){
            emit("signDateFormat",element)
        }

        //控件点击
        function controlClick(e, element) {
            emit("controlClick", e, element)
        }
        //控件设置应用于整个文档
        function handleSetControl(element) {

            if (!checkedKeys.value.length) {
                msg.warning('请勾选应用文档')
                return
            }
            if (!currentElement.value.oddEventType) {
                msg.warning('请设置应用页面')
                return
            }
            checkPage(currentElement.value)
            if (!customValueRight.value && currentElement.value.oddEventType == 'custom') {
                return;
            }
            element.showPopover = false;
            emit('handleSetControl', checkedKeys.value, element)

        }
        //重新计算控件位置
        function refreshControlPosition(element, moveOpt) {
            emit('refreshControlPosition', element, moveOpt)
        }

        function onSelectChange(selectedRowKeys: (string | number)[]) {
            checkedKeys.value = selectedRowKeys;
        }
        //校验输入的页码 
        function checkPage(element) {
            let reg = /^(?:(\d+)|(\d+(?:,\d+)*)(?:(?:,|-)\d+(?:,\d+)*)*)$/;
            if (!reg.test(element.custom)) {
                customValueRight.value = false;
                // nextTick(() => {
                //     element.custom = '';
                // })
            } else {
                customValueRight.value = true
            }

        }
         //控件点击联动
        function controlFocus(element){
            emit("controlFocus",element)
        }
        return {
            controlFocus,
            CanvasZoom,
            baseUrl,
            controlDelete,
            controlMousedown,
            group, scrollbarRef,
            clearSeal,
            controlBatchUse,
            zoom,
            handlePage,
            controlClick,
            registerTable,
            checkedKeys,
            onSelectChange,
            handleZoom,
            handleSetControl,
            currentPage,
            containerWidth,
            isScriptScroll,
            refreshControlPosition,
            checkPage,
            customValueRight,
            signDateFormat,
            handleScroll

        }
    }
})
</script>

<style lang="less" scoped>
.doc-layout-container {
    flex: 1 1 0;
    padding: 10px 0 45px;
    background: #f9f9f9;
    position: relative;
    // overflow: auto;

    .doc-content {
        height: 100%;
        // border: 1px solid #eaeaea;
        position:relative;
        overflow-y:scroll;
        overflow-x:scroll;
        margin:0 auto;
        .scrollbar__view {
            height: 100%;
            overflow-y: scroll;
        }
    }
    // .zoom-container{
    //   display:flex;
    // }
    // .doc-content:hover{
    //   overflow-x:scroll;
    // }
    .zoom-control{
      position: absolute;
      bottom: 5px;
      right: 20px;
      margin-top: 10px;
      left: 50%;
      transform: translate(-170px, 0);
      display: flex;
      cursor: pointer;
      align-items: center;
    }
    .page-turn{
      color:#958787;
      :deep(.ant-btn){
        border: none;
        background: #f9f9f9;
        color:#958787;
      }

      button[ant-click-animating-without-extra-node]:after {
        border: 0 none;
        opacity: 0;
        animation:none 0 ease 0 1 normal;
        outline:none;
      }
    }
    .page-zoom{
      display: flex;
      cursor: pointer;
      align-items: center;
      
      :deep(.ant-input-affix-wrapper){
        margin:0 5px;
        border: none;
        color:#958787;
      }
    }

    .doc-content::-webkit-scrollbar-thumb {
        background-color: rgb(190, 188, 188);
        /* 设置滚动条滑块的颜色 */
        border-radius: 6px;
        /* 设置滚动条滑块的圆角 */
        cursor: pointer;
    }

    .doc-content::-webkit-scrollbar {
        width: 8px;
        /* 设置滚动条的宽度 */
    }

    .doc-content::-webkit-scrollbar-track {
        background-color: #f0f0f0;
        /* 滚动条轨道的背景色 */
    }

    :deep(.scrollbar__wrap, .scrollbar) {
        overflow: visible;
    }


    .document-list {
        position: relative;
        margin: 0 auto;
        // width: 800px;
        display:flex;
        flex-direction: column;
        align-items: center;
    }

    .document-page {
        width: 800px;
        position: absolute;
        top: 0;
        left: 50%;
        img{
          width:100%;
          height:100%;
          display:inline-block;
        }
    }

    .document-bottom {
        height: 40px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.08);
        border-top: 1px solid rgba(0, 0, 0, 0.08);
    }

    .document-body {
        height: calc(100% - 40px);
    }
    
}
</style>

<style lang="less">
.control-pop {
    .sub-title {
        margin-bottom: 0;
        font-weight: 550;
        margin-top: 20px;
    }



    .resrun-basic-table {

        // height: 200px;
        .ant-table-content {
            padding-bottom: 0;

        }
    }

    .pop-action {
        margin-top: 20px;
        text-align: right;
        width: 100%;

        :deep(.ant-btn) {
            margin: 0 10px;
        }
    }
}

.custom-value-warning {
    font-size: 12px;
    margin: 2px;
    color: #f00909;
}
.ghost{
  display:none;
}
// .chosenClass.control-item{
//       z-index:2022;
//       position:absolute;
//     }

.page-corner {
  position: absolute;
  right: 0px;
  bottom: 0px;
  width: 0;
  height: 0;
  border-color: #ccc transparent;
  border-width: 0 0 30px 30px;
  border-style: solid;
}

.page-num {
  position: absolute;
  bottom: 1px;
  right: 2px;
  font-size: 9px;
  color: #fff;
  // padding-right: 20px;
}

</style>

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
  import { defineComponent, CSSProperties, watch, nextTick } from 'vue';
  import { fileListProps } from './props';
  import { isFunction } from '/@/utils/is';
  import { useModalContext } from '/@/components/Modal/src/hooks/useModalContext';

  export default defineComponent({
    name: 'FileList',
    props: fileListProps,
    setup(props) {
      const modalFn = useModalContext();
      watch(
        () => props.dataSource,
        () => {
          nextTick(() => {
            modalFn?.redoModalHeight?.();
          });
        },
      );
      return () => {
        const { columns, actionColumn, dataSource } = props;
        const columnList = [...columns, actionColumn];
        return (
          <table class="file-table">
            <colgroup>
              {columnList.map((item) => {
                const { width = 0, dataIndex } = item;
                const style: CSSProperties = {
                  width: `${width}px`,
                  minWidth: `${width}px`,
                };
                return <col style={width ? style : {}} key={dataIndex} />;
              })}
            </colgroup>
            <thead>
              <tr class="file-table-tr">
                {columnList.map((item) => {
                  const { title = '', align = 'center', dataIndex } = item;
                  return (
                    <th class={['file-table-th', align]} key={dataIndex}>
                      {title}
                    </th>
                  );
                })}
              </tr>
            </thead>
            <tbody>
              {dataSource.map((record = {}, index) => {
                return (
                  <tr class="file-table-tr" key={`${index + record.name || ''}`}>
                    {columnList.map((item) => {
                      const { dataIndex = '', customRender, align = 'center' } = item;
                      const render = customRender && isFunction(customRender);
                      return (
                        <td class={['file-table-td', align]} key={dataIndex}>
                          {render
                            ? customRender?.({ text: record[dataIndex], record })
                            : record[dataIndex]}
                        </td>
                      );
                    })}
                  </tr>
                );
              })}
            </tbody>
          </table>
        );
      };
    },
  });
</script>
<style lang="less">
  .file-table {
    width: 100%;
    border-collapse: collapse;

    .center {
      text-align: center;
    }

    .left {
      text-align: left;
    }

    .right {
      text-align: right;
    }

    &-th,
    &-td {
      padding: 12px 8px;
    }

    thead {
      background-color: @background-color-light;
    }

    table,
    td,
    th {
      border: 1px solid @border-color-base;
    }
  }
</style>

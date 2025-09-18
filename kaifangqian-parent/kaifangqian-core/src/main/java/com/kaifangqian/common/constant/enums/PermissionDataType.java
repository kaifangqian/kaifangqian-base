/**
 * @description 数据权限数据类型
 * 当被请求的方法有注解PermissionData时,会在往当前request中写入数据权限信息
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.common.constant.enums;

import java.util.Arrays;
import java.util.List;

/**
 * 权限策略枚举
 *
 * @author zhh
 */
public enum PermissionDataType {
    // 个人
    SELF("self", "个人", "个人", 0),
    // 部门
    DEPART("depart", "部门", "部门", 1),
    // 部门及以下
    DEPARTALL("departall", "部门及以下", "部门及以下", 2),
    // 全部
    ALL("all", "全部", "全部", 3),
    //自定义
    CUSTOM("custom", "自定义", "自定义", 4);

    private String type;

    private String name;

    private String desc;

    private Integer orderNo;

    public String getType() {
        return type;
    }

    public PermissionDataType setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public PermissionDataType setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public PermissionDataType setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public PermissionDataType setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    PermissionDataType() {

    }

    PermissionDataType(String type, String name, String desc, Integer orderNo) {
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.orderNo = orderNo;
    }

    public static List<PermissionDataType> getAll() {
        return Arrays.asList(PermissionDataType.values());
    }

}

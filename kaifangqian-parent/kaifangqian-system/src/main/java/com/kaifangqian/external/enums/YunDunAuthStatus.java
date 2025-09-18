/**
 * @description 电子签名开通实名认证状态枚举
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
package com.kaifangqian.external.enums;

/**
 * @author : wwt
 * create at: 2025/6/6
 */
public enum YunDunAuthStatus {
    CANCELED(3, "已撤销"),
    SUCCESS(1, "已认证完成"),
    PROCESSING(2, "认证中（自主认证中、认证审核中（针对于人工审核方式）、审核未通过（针对于人工审核方式）、认证授权审批中、认证授权审批未通过）"),
    INVALID(4, "已失效"),
    FRISTVERIFY(1, "首次认证"),
    UPDATE(2, "更新认证");

    private Integer type;

    private String name;

    YunDunAuthStatus(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public YunDunAuthStatus setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public YunDunAuthStatus setName(String name) {
        this.name = name;
        return this;
    }
}
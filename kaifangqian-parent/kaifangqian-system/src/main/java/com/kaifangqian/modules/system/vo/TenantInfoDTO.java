/**
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
package com.kaifangqian.modules.system.vo;

import com.kaifangqian.modules.storage.dto.StorageDto;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author : zhenghuihan
 * create at:  2023/1/30  14:28
 * @description: 开通租户信息
 */
@Data
public class TenantInfoDTO {
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 租户类型:个人1 企业2 工作站3 版权局4 区县版权局5
     */
    // @ApiModelProperty(value = "租户类型")
    private Integer tenantType;
    /**
     * 姓名（个人、企业、工作站名称
     */
    // @ApiModelProperty(value = "姓名（个人、企业、工作站名称")
    private java.lang.String name;
    /**
     * 单位证件号码
     */
    // @ApiModelProperty(value = "单位证件号码")
    private java.lang.String organizationNo;
    /**
     * 法定代表人
     */
    // @ApiModelProperty(value = "法定代表人")
    private java.lang.String corporation;
    /**
     * 代表人证件号
     */
    // @ApiModelProperty(value = "代表人证件号")
    private java.lang.String corporationNo;
    /**
     * 手机号
     */
    // @ApiModelProperty(value = "手机号")
    private java.lang.String phone;

    /**
     * 认证状态 未认证0 审核中1 已认证2 未通过-1
     */
    // @ApiModelProperty(value = "认证状态 未认证0 审核中1 已认证2 未通过3")
    private java.lang.Integer authStatus;
    /**
     * 组织码
     */
    // @ApiModelProperty(value = "组织码")
    private java.lang.String orgCode;
    /**
     * 审核意见
     */
    private String checkMes;

    private Date createTime;

    // @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    // @ApiModelProperty(value = "申请用户")
    private String applyTenantUser;

    // @ApiModelProperty(value = "个人/法人证件类型")
    private Integer idCardType;

    private Integer authType;

    private String departId;

    private Integer realItem;
    /**
     * 身份证正面
     */
    private StorageDto idCardFace;
    /**
     * 身份证反面
     */
    private StorageDto idCardEmblem;
    /**
     * 企业证件
     */
    private StorageDto businessLicense;

    private StorageDto authorizeBook;
}
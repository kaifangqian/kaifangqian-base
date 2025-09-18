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
 * create at:  2022/6/28  17:58
 * @description:用户认证日志
 */
@Data
public class TenantAuthLogVO {

    private String id;
    // @ApiModelProperty(value = "认证类型：1=新建企业认证、2=重新认证、3=认证变更")
    private Integer authType;

    // @ApiModelProperty(value = "租户id，新建不需要此参数")
    private String tenantId;

    // @ApiModelProperty(value = "姓名或企业名称")
    private String name;

    /**
     * 身份证号码或单位证件号码
     */
    // @ApiModelProperty(value = "身份证号或者组织机构代码")
    private String organizationNo;

    /**
     * 法定代表人
     */
    // @ApiModelProperty(value = "法人姓名")
    private String corporation;

    /**
     * 代表人证件号
     */
    // @ApiModelProperty(value = "法人身份证号")
    private String corporationNo;

    /**
     * 手机号
     */
    // @ApiModelProperty(value = "手机号码")
    private String phone;


    /**
     * 认证状态 未认证0 审核中1 已认证2 未通过-1
     */
    // @ApiModelProperty(value = "认证状态 未认证0 审核中1 已认证2 未通过3")
    private java.lang.Integer authStatus;

    /**
     * 手机号
     */
    // @ApiModelProperty(value = "事项，创建不需要传")
    private Integer realItem;


    // @ApiModelProperty(value = "申请人")
    private String applyUser;

    // @ApiModelProperty(value = "审核人")
    private String checkUser;


    // @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private Integer tenantType;

    // @ApiModelProperty(value = "申请时间")
    private Date applyTime;

    // @ApiModelProperty(value = "审核时间")
    private Date checkTime;

    // @ApiModelProperty(value = "审核不通过原因")
    private Date checkMsg;

    /**
     * 身份证正面
     */
    // @ApiModelProperty(value = "身份证人像面")
    private StorageDto idCardFace;
    /**
     * 身份证反面
     */
    // @ApiModelProperty(value = "身份证国徽面")
    private StorageDto idCardEmblem;
    /**
     * 企业证件
     */
    // @ApiModelProperty(value = "企业营业执照")
    private StorageDto businessLicense;

    // @ApiModelProperty(value = "授权书")
    private StorageDto authorizeBook;



}

/**
 * @description 数字证书信息表
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
package com.kaifangqian.modules.opensign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: SignCertInfo
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignCertInfo
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_cert_info")
// @ApiModel("数字证书信息表")
public class SignCertInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4914230382513200997L;

    @TableId(type = IdType.ASSIGN_ID)
    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("印章所属系统租户id")
    private String sysTenantId ;

    // @ApiModelProperty("印章所属系统账号id")
    private String sysAccountId ;

    // @ApiModelProperty("印章所属系统租户下用户id")
    private String sysUserId ;

    // @ApiModelProperty("印章所属系统部门编码")
    private String sysOrgCode ;

    // @ApiModelProperty("印章所属系统部门id")
    private String sysDeptId ;

    // @ApiModelProperty("证书所属类型（1、ca根证书；2、系统防篡改根证书；3、ca个人证书；4、ca企业证书；5、系统防篡改个人证书；6、系统防篡改企业证书）")
    private Integer holderType ;

    // @ApiModelProperty("证书序列号")
    private String certSuqeNo;

    // @ApiModelProperty("'证书颁发时间'")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueTime ;

    // @ApiModelProperty("颁发机构")
    private String issueOrg ;

    // @ApiModelProperty("证书密码")
    private String certPassword ;

    // @ApiModelProperty("证书类型（1、云证书")
    private Integer certType ;

    // @ApiModelProperty("证书文件地址")
    private String pfxPath ;

    // @ApiModelProperty("加密算法（1、RSA；2、SM2）")
    private String algorithmType ;

    // @ApiModelProperty("证书有效状态（1、有效；2、无效")
    private Integer validateStatus ;

    // @ApiModelProperty("证书有效期起始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termOfValidityStartTime ;

    // @ApiModelProperty("证书有效期结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termOfValidityEndTime ;

    // @ApiModelProperty("存储介质")
    private String storageMedium ;

    // @ApiModelProperty("备注")
    private String notes;


}
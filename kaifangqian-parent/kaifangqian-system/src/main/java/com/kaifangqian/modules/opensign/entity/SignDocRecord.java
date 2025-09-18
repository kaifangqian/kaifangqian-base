/**
 * @description 文档签署记录
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
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: SignDocRecord
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignDocRecord
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_doc_record")
// @ApiModel("文档签署记录")
public class SignDocRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -5139997344229143016L;

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

    // @ApiModelProperty("文档id")
    private String docId ;

    // @ApiModelProperty("签章id")
    private String sealId ;

    // @ApiModelProperty("证书id")
    private String certId ;

    // @ApiModelProperty("操作时间")
    private Date operateTime ;

    // @ApiModelProperty("操作状态,0为草稿，1为填写，2为签署,3拒绝签署")
    private Integer operateStatus ;

    // @ApiModelProperty("操作备注")
    private String operateNotes ;

    // @ApiModelProperty("意愿校验类型（多选：0、无需校验；1、短信校验；2、扫脸校验")
    private Integer validateType ;

    // @ApiModelProperty("意愿校验状态，1通过，2未通过")
    private Integer validateStatus ;

    // @ApiModelProperty("是否为最新签署的，1为是，2为否")
    private Integer isCurrent ;

    // @ApiModelProperty("文件id")
    private String annexId ;

}
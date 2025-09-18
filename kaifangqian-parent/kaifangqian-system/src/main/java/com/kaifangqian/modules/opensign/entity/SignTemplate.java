/**
 * @description 模板表
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

/**
 * @Description: SignTemplate
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignTemplate
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_template")
// @ApiModel("模板表")
public class SignTemplate extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1032563518832945882L;

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

    // @ApiModelProperty("模板编号")
    private String templateCode ;

    // @ApiModelProperty("模板名称")
    private String templateName ;

    // @ApiModelProperty("业务类型字典id")
    private String businessType ;

    // @ApiModelProperty("模板类型（1、有参数模板；2、无参数模板；")
    private Integer templateType ;

    // @ApiModelProperty("签章id")
    private String sealId ;

    // @ApiModelProperty("备注")
    private String note ;

    // @ApiModelProperty("模板状态（1、制作中，2、制作失败，3、已停用，4、已启用）")
    private Integer templateStatus ;

//    // @ApiModelProperty("申请状态（待提交、待重新提交、待审批、审批未通过、审批通过、作废）")
//    private Integer applyStatus ;
}
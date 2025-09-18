/**
 * @description 文档控件表
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
 * @Description: SignDocControl
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignDocControl
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_doc_control")
// @ApiModel("文档控件表")
public class SignDocControl extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2391249595568705819L;

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

    // @ApiModelProperty("控件类型（控件类型，1为签署，2为填写）")
    private String type ;

    // @ApiModelProperty("控件名称")
    private String name ;

    // @ApiModelProperty("控件X坐标(左上角)")
    private String offsetX ;

    // @ApiModelProperty("控件Y坐标(左上角)")
    private String offsetY ;

    // @ApiModelProperty("控件宽度")
    private String width ;

    // @ApiModelProperty("控件高度")
    private String height ;

    // @ApiModelProperty("控件所属页码")
    private Integer page ;

    // @ApiModelProperty("是否已经填充，1为已填充，2为未填写")
    private Integer isFilled;

    // @ApiModelProperty("是否为必填项，1为必填项，2为非必填项")
    private Integer isRequired;

    // @ApiModelProperty("文字字体")
    private String fontFamily ;

    // @ApiModelProperty("对其方式")
    private String textAlign ;

    // @ApiModelProperty("文字大小")
    private Integer fontSize ;

    // @ApiModelProperty("提示值")
    private String placeholder ;

    // @ApiModelProperty("填写值")
    private String value ;

    // @ApiModelProperty("解析格式")
    private String format ;

    // @ApiModelProperty("当前文件页面宽度")
    private String pageWidth ;

    // @ApiModelProperty("当前文件页面高度")
    private String pageHeight ;






}
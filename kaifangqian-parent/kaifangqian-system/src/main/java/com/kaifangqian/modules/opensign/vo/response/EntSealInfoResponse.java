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
package com.kaifangqian.modules.opensign.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kaifangqian.modules.opensign.vo.base.BusinessAuthVo;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: EntSealInfoResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: EntSealInfoResponse
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("企业印章详情返回对象")
public class EntSealInfoResponse implements Serializable {


    private static final long serialVersionUID = -1440467589442961877L;

    // @ApiModelProperty("印章id")
    private String sealId ;

    // @ApiModelProperty("印章名称")
    private String sealName ;

    // @ApiModelProperty("印章类型（1、公章；2、财务专用章；3、合同专用章；4、人事专用章；5、其他）")
    private Integer sealType ;

    // @ApiModelProperty("印章样式（1、公章；2、圆形章；3、椭圆形；）")
    private Integer sealStyle ;

    // @ApiModelProperty("创建类型（1、模板创建；2、上传创建）")
    private Integer createType ;

    // @ApiModelProperty("横排文字")
    private String middleText ;

    // @ApiModelProperty("上排环绕文字")
    private String topText ;

    // @ApiModelProperty("下弦文")
    private String bottomText ;

    // @ApiModelProperty("生成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // @ApiModelProperty("文件id")
    private String annexId ;

    // @ApiModelProperty("用途说明")
    private String description;

    // @ApiModelProperty("原因")
    private String reason;


    // @ApiModelProperty("印章状态（制作中、制作失败、已停用、已启用、已收缴、已销毁）")
    private Integer sealStatus ;

    // @ApiModelProperty("申请操作类型,1、制作，2、编辑，3、章面变更，4、启用，5、停用，6、收缴，7销毁")
    private Integer operateType ;

    // @ApiModelProperty("申请状态（1、待提交，2、待重新提交，3、待审批，4、审批未通过，5、审批通过，6、作废）")
    private Integer applyStatus ;

    // @ApiModelProperty("申请原因")
    private String applyDescription ;

    // @ApiModelProperty("印章申请记录id")
    private String sealApplyId ;


    // @ApiModelProperty("印章管理员id，印章所属系统租户下用户id")
    private String adminId ;

    // @ApiModelProperty("印章管理员id，印章所属系统租户下用户名称")
    private String adminName ;


    // @ApiModelProperty("印章管理员列表")
    private List<BusinessAuthVo> managerList ;

    // @ApiModelProperty("印章使用者列表")
    private List<BusinessAuthVo> userList ;

    // @ApiModelProperty("印章审计者列表")
    private List<BusinessAuthVo> auditorList ;

    // @ApiModelProperty("1有效，2失效")
    private Integer status ;


}
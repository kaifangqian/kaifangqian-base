/**
 * @description 文档基础数据对象
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
package com.kaifangqian.modules.opensign.vo.base;

import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: DocVo
 * @Package: com.kaifangqian.modules.opensign.vo.base
 * @ClassName: DocVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("文档基础数据对象")
public class DocVo implements Serializable {

    private static final long serialVersionUID = -4829924328211608565L;

    // @ApiModelProperty("主键")
    private String id;

    // @ApiModelProperty("文档主题")
    private String docSubject ;

    // @ApiModelProperty("业务类型字典id")
    private String businessType ;

    // @ApiModelProperty("用印场景类型（1、加盖电子印章；2、加盖物理印章；")
    private Integer sceneType ;

    // @ApiModelProperty("用章类型")
    private Integer sealType ;

    // @ApiModelProperty("用印份数")
    private Integer useCount ;

    // @ApiModelProperty("签章id")
    private String sealId ;

    // @ApiModelProperty("用印事由")
    private String reason ;

    // @ApiModelProperty("发往单位")
    private String sendDept ;

    // @ApiModelProperty("签署截止时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime ;

    // @ApiModelProperty("备注")
    private String note ;

    // @ApiModelProperty("文档状态（待发起、待重新发起、待审批、审批未通过、待签章、签署失败、已完成、已过期、作废）")
    private Integer docStatus ;

    // @ApiModelProperty("发起时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime ;


    // @ApiModelProperty("主文件id")
    private String mainAnnexId ;

    // @ApiModelProperty("其他附件id列表")
    private List<String> otherAnnexList ;

}
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
package com.kaifangqian.modules.opensign.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: DocListRequest
 * @Package: com.kaifangqian.modules.opensign.vo.request
 * @ClassName: DocListRequest
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("文档列表请求对象")
public class DocListRequest implements Serializable {


    private static final long serialVersionUID = -7687103172768742584L;

    // @ApiModelProperty("文档主题")
    private String docSubject;
    // @ApiModelProperty("业务类型")
    private String businessType;
    // @ApiModelProperty("用印场景")
    private Integer sceneType ;
    // @ApiModelProperty("发起人")
    private String sender ;
    // @ApiModelProperty("发起部门")
    private String sendDept ;
    // @ApiModelProperty("申请状态")
    private Integer docStatus ;
    // @ApiModelProperty("申请日期-开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime ;
    // @ApiModelProperty("申请日期-结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime ;


    // @ApiModelProperty("页码")
    Integer pageNo = 1;

    // @ApiModelProperty("页码大小")
    Integer pageSize = 10 ;



}
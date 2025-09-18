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
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TemplateListResponse
 * @Package: com.kaifangqian.modules.opensign.vo.response
 * @ClassName: TemplateListResponse
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("模板列表返回对象")
public class TemplateListResponse implements Serializable {


    private static final long serialVersionUID = 4353135738243190817L;

    // @ApiModelProperty("主键")
    private String id ;

    // @ApiModelProperty("模板编号")
    private String templateCode ;

    // @ApiModelProperty("模板名称")
    private String templateName ;

    // @ApiModelProperty("业务类型字典id")
    private String businessType ;

    // @ApiModelProperty("模板类型（1、有参数模板；2、无参数模板；")
    private Integer templateType ;

    // @ApiModelProperty("模板状态（制作中、制作失败、已停用、已启用）")
    private Integer templateStatus ;

    // @ApiModelProperty("修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime ;

    // @ApiModelProperty("真实文件id")
    private String annexId ;

}
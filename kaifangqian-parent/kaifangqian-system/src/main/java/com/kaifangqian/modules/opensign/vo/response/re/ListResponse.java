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
package com.kaifangqian.modules.opensign.vo.response.re;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @Description: ListReponse
 * @Package: com.kaifangqian.modules.opensign.vo.response.re
 * @ClassName: ListReponse
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("业务线配置列表-返回对象")
public class ListResponse implements Serializable {

    private static final long serialVersionUID = 989711067561773123L;

    // @ApiModelProperty("业务线id")
    private String id ;

    // @ApiModelProperty("业务线名称")
    private String name ;

    // @ApiModelProperty("管理员列表")
    private List<String> managerList ;

    // @ApiModelProperty("'异常状态，1为启用，2为停用'")
    private Integer status ;

    // @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate ;

    // @ApiModelProperty("'异常状态，1为是，2为否'")
    private Integer errorStatus ;

    // @ApiModelProperty("所属文件夹名称（类型名称）")
    private String folderName ;

}
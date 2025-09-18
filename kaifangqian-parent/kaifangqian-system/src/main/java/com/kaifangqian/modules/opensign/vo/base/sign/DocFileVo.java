/**
 * @description 签约文件数据对象
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
package com.kaifangqian.modules.opensign.vo.base.sign;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: DocFileVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: DocFileVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("签约文件数据对象")
public class DocFileVo implements Serializable {

    private static final long serialVersionUID = -5799656589362270699L;

    // @ApiModelProperty("签约文件id")
    private String id ;

    // @ApiModelProperty("签约文件名称")
    private String name ;

    // @ApiModelProperty("签约文件类型，1、上传、2模板")
    private Integer docType ;

    // @ApiModelProperty("签约文件来源id")
    private String docOriginId ;

    // @ApiModelProperty("签约文件真实文件id")
    private String annexId ;

//    // @ApiModelProperty("图片列表")
//    private List<DocImageVo> imageList ;

    // @ApiModelProperty("来源类型，1、业务线，2、自行上传")
    private Integer originType ;

    // @ApiModelProperty("来源类型，1、有参数；2、无参数")
    private Integer paramType ;

    // @ApiModelProperty("文件页数")
    private Integer docPage ;

    // @ApiModelProperty("签约文件顺序")
    private Integer docOrder ;

}
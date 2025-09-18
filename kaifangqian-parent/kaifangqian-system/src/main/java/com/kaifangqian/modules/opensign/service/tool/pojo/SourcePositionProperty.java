/**
 * @description 文件签署属性类
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
package com.kaifangqian.modules.opensign.service.tool.pojo;

// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 原始文件签署位置属性
 * @Package: com.kaifangqian.modules.sign.service.tool.pojo
 * @ClassName: SourcePositionProperty
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourcePositionProperty implements Serializable {

    private static final long serialVersionUID = 725976764583634367L;

    // @ApiModelProperty("控件X坐标(左上角)")
    private Float offsetX ;
    // @ApiModelProperty("控件Y坐标(左上角)")
    private Float offsetY ;
    // @ApiModelProperty("控件宽度")
    private Float width ;
    // @ApiModelProperty("控件高度")
    private Float height ;
    // @ApiModelProperty("当前文件页面宽度")
    private Float pageWidth ;
    // @ApiModelProperty("当前文件页面高度")
    private Float pageHeight ;
    // @ApiModelProperty("控件所属页码")
    private Integer page ;

    // @ApiModelProperty("当前文件页面宽度")
    private Float realWidth ;
    // @ApiModelProperty("当前文件页面高度")
    private Float realHeight ;

    // @ApiModelProperty("填写值，填写专用")
    private String value ;
    // @ApiModelProperty("对齐方式")
    private String align;
    // @ApiModelProperty("字体")
    private String fontFamily ;
    // @ApiModelProperty("文字大小")
    private Integer fontSize ;

    // @ApiModelProperty("'控件类型'")
    private String controlType ;

    // @ApiModelProperty("填写控件内部参数")
    private List<ControlWidgetProperty> widgetPropertyList ;



}
/**
 * @description 业务线控件基础数据对象
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
 * @Description: DocControlVo
 * @Package: com.kaifangqian.modules.opensign.vo.base.sign
 * @ClassName: DocControlVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("业务线控件基础数据对象")
public class DocControlVo implements Serializable {

    private static final long serialVersionUID = 2231954896159849026L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("业务线主表id")
    private String signReId ;

    // @ApiModelProperty("'业务线实例主表id'")
    private String signRuId ;

    // @ApiModelProperty("'业务线配置-签约文件id'")
    private String signReDocId ;

    // @ApiModelProperty("'业务线实例-签约文件id'")
    private String signRuDocId ;

    // @ApiModelProperty("'人员类型，1发起方，2接收方'")
    private Integer signerType ;

    // @ApiModelProperty("'签署方id，signerId或者senderId'")
    private String signerId ;

    // @ApiModelProperty("'控件类型'")
    private String controlType ;

    // @ApiModelProperty("''异常状态，1为是，2为否''")
    private Integer errorStatus ;

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

    // @ApiModelProperty("接口参数名称")
    private String interfaceParamName ;

    // @ApiModelProperty("来源类型，1、业务线，2、发起时设置、3、操作时设置")
    private Integer originType ;

    // @ApiModelProperty("是否为当前签署人所有，1为是，2为否")
    private Integer isMineFlag;

    // @ApiModelProperty("控件关联属性列表")
    private List<ControlPropertyVo> propertyVoList;

//    // @ApiModelProperty("填写控件属性")
//    private List<ControlWidgetVo> widgetVoList ;


    // @ApiModelProperty("控件属性")
    private String properties;

    // @ApiModelProperty("顺序")
    private Integer controlOrder;
}
/**
 * @description 签署人控件配置表
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

import com.baomidou.mybatisplus.annotation.TableName;
import com.kaifangqian.common.base.entity.BaseEntity;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignRuDocControl
 * @Package: com.kaifangqian.modules.opensign.entity
 * @ClassName: SignRuDocControl
 * @author: FengLai_Gong
 */
@Data
@TableName("sign_re_doc_control")
// @ApiModel("签署人控件配置表")
public class SignReDocControl extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 7858378251818415222L;

    // @ApiModelProperty("'主键'")
    private String id ;

    // @ApiModelProperty("业务线主表id")
    private String signReId ;

    // @ApiModelProperty("业务线签约文件id")
    private String signReDocId ;

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

    // @ApiModelProperty("填写控件属性")
    private String properties ;

}
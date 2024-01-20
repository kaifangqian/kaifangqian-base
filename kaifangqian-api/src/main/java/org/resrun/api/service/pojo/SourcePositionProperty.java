package org.resrun.api.service.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 原始文件签署位置属性
 * @Package: org.resrun.modules.sign.service.tool.pojo
 * @ClassName: SourcePositionProperty
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourcePositionProperty implements Serializable {

    private static final long serialVersionUID = 725976764583634367L;

    @ApiModelProperty("控件X坐标(左上角)")
    private Float offsetX ;
    @ApiModelProperty("控件Y坐标(左上角)")
    private Float offsetY ;
    @ApiModelProperty("控件宽度")
    private Float width ;
    @ApiModelProperty("控件高度")
    private Float height ;
    @ApiModelProperty("当前文件页面宽度")
    private Float pageWidth ;
    @ApiModelProperty("当前文件页面高度")
    private Float pageHeight ;
    @ApiModelProperty("控件所属页码")
    private Integer page ;

    @ApiModelProperty("当前文件页面宽度")
    private Float realWidth ;
    @ApiModelProperty("当前文件页面高度")
    private Float realHeight ;

    @ApiModelProperty("填写值，填写专用")
    private String value ;
    @ApiModelProperty("对齐方式")
    private String align;
    @ApiModelProperty("字体")
    private String fontFamily ;
    @ApiModelProperty("文字大小")
    private Integer fontSize ;





}
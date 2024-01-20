package org.resrun.service.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 原始文件签署位置属性
 * @Package: org.resrun.service.pojo
 * @ClassName: SourcePositionProperty
 * @copyright 北京资源律动科技有限公司
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


}
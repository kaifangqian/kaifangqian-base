package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: PositionRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: PositionRequest
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("签署位置-请求对象")
public class PositionRequest implements Serializable {

    private static final long serialVersionUID = 1407897694551031954L;

    @ApiModelProperty("控件X坐标(左上角)")
    private String offsetX ;

    @ApiModelProperty("控件Y坐标(左上角)")
    private String offsetY ;

    @ApiModelProperty("控件宽度")
    private String width ;

    @ApiModelProperty("控件高度")
    private String height ;

    @ApiModelProperty("控件所属页码")
    private Integer page ;

    @ApiModelProperty("当前文件页面宽度")
    private String pageWidth ;

    @ApiModelProperty("当前文件页面高度")
    private String pageHeight ;

}
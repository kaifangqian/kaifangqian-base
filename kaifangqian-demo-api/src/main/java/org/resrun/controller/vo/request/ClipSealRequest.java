package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: ClipSealRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: ClipSealRequest
 * @copyright 北京资源律动科技有限公司
 */
@ApiModel("企业印章上传生成-请求对象")
public class ClipSealRequest implements Serializable {

    private static final long serialVersionUID = 7532064967450524188L;

    @ApiModelProperty("图片base64")
    private String image ;

    @ApiModelProperty("色差范围0~255")
    private Integer colorRange ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getColorRange() {
        return colorRange;
    }

    public void setColorRange(Integer colorRange) {
        this.colorRange = colorRange;
    }

    public ClipSealRequest(String image, Integer colorRange) {
        this.image = image;
        this.colorRange = colorRange;
    }

    public ClipSealRequest() {
    }
}
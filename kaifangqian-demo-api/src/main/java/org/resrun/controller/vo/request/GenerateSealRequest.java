package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: SealRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: SealRequest
 * @copyright 北京资源律动科技有限公司
 */
@ApiModel("企业印章参数生成-请求对象")
public class GenerateSealRequest implements Serializable {

    private static final long serialVersionUID = -7107056549410243340L;

    @ApiModelProperty("企业名称")
    private String topText;
    @ApiModelProperty("横排文字")
    private String middleText ;

    public GenerateSealRequest() {
    }

    public GenerateSealRequest(String topText, String middleText) {
        this.topText = topText;
        this.middleText = middleText;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public String getMiddleText() {
        return middleText;
    }

    public void setMiddleText(String middleText) {
        this.middleText = middleText;
    }
}
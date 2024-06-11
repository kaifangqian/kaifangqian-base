package org.resrun.controller.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: SealResponse
 * @Package: org.resrun.controller.vo.response
 * @ClassName: SealResponse
 * @copyright 北京资源律动科技有限公司
 */
@ApiModel("企业印章生成-返回对象")
public class SealResponse implements Serializable {

    private static final long serialVersionUID = 3376686508340918420L;

    @ApiModelProperty("企业签章base64")
    private String entSeal ;

    

    public String getEntSeal() {
        return entSeal;
    }

    public void setEntSeal(String entSeal) {
        this.entSeal = entSeal;
    }
}
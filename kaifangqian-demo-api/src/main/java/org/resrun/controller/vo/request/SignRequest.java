package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SignRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: SignRequest
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("签署-请求对象")
public class SignRequest implements Serializable {

    @ApiModelProperty("签署类型，1位置签署，2关键字签署")
    private Integer signType ;

    @ApiModelProperty("企业签章base64")
    private String entSeal ;

    @ApiModelProperty("企业名称")
    private String entName ;

    @ApiModelProperty("个人签章base64")
    private String personalSeal ;

    @ApiModelProperty("个人名称")
    private String personalName ;

    @ApiModelProperty("企业签署位置")
    private List<PositionRequest> entPositionList ;

    @ApiModelProperty("个人签章位置")
    private List<PositionRequest> personalPositionList ;

    @ApiModelProperty("企业签章关键字")
    private String entKeyword ;

    @ApiModelProperty("个人签章关键字")
    private String personalKeyword ;


}
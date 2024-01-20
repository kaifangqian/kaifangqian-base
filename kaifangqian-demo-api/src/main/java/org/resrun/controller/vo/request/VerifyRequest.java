package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: VerifyRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: VerifyRequest
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文件验签-请求对象")
public class VerifyRequest implements Serializable {

    private static final long serialVersionUID = -5412004997017887483L;

    @ApiModelProperty("验签文件base64")
    private String verifyFile ;

    @ApiModelProperty("验签文件名称")
    private String fileName ;

}
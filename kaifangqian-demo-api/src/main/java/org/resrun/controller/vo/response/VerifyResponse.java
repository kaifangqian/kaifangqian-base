package org.resrun.controller.vo.response;

import org.resrun.service.pojo.SignPdfInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: VerifyRequest
 * @Package: org.resrun.controller.vo.response
 * @ClassName: VerifyRequest
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文件验签-返回对象")
public class VerifyResponse implements Serializable {

    private static final long serialVersionUID = -1498109564641142303L;

    @ApiModelProperty("返回信息")
    private String msg ;

    @ApiModelProperty("详细数据")
    private SignPdfInfoVo vo ;

}
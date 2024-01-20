package org.resrun.controller.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: ClipSealRequest
 * @Package: org.resrun.controller.vo.request
 * @ClassName: ClipSealRequest
 * @copyright 北京资源律动科技有限公司
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("企业印章上传生成-请求对象")
public class ClipSealRequest implements Serializable {

    private static final long serialVersionUID = 7532064967450524188L;

    @ApiModelProperty("图片base64")
    private String image ;

    @ApiModelProperty("色差范围0~255")
    private Integer colorRange ;

}
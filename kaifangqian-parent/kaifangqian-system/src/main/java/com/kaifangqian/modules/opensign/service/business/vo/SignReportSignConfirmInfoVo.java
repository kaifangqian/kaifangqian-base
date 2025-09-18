package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignReportSignConfrimInfoVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignReportSignConfrimInfoVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("电子文件签署节点-签署意愿信息-对象")
public class SignReportSignConfirmInfoVo implements Serializable {

    private static final long serialVersionUID = 3235610722052958713L;

    // @ApiModelProperty("校验类型")
    private String confirmType ;

    // @ApiModelProperty("签署校验时间")
    private String confirmTime ;

    // @ApiModelProperty("签署校验信息")
    private String confirmInfo ;


}
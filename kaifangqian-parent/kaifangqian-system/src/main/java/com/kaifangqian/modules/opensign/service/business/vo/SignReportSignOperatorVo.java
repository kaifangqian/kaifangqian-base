package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignReportSignOperatorVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignReportSignOperatorVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("电子文件签署节点-操作人-对象")
public class SignReportSignOperatorVo extends SignReportSignCertInfoVo implements Serializable {

    private static final long serialVersionUID = 2738295175458838993L;

    // @ApiModelProperty("操作主体")
    private String operatorTenantName ;

    // @ApiModelProperty("操作主体ID")
    private String operatorTenantId ;

    // @ApiModelProperty("操作节点")
    private String operatorNode ;

    // @ApiModelProperty("签署类型")
    private String operatorSignType ;

    // @ApiModelProperty("操作人")
    private String operatorTenantUserName ;

    // @ApiModelProperty("操作人ID")
    private String operatorTenantUserId ;

    // @ApiModelProperty("操作时间")
    private String operatorTime ;

    // @ApiModelProperty("签章图片")
    private String operatorSignImage ;
}
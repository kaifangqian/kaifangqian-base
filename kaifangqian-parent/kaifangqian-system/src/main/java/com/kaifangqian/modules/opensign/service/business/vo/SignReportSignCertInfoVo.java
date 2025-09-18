package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.dto.ReportUserConfirmInfo;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignReportSignCertInfoVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignReportSignCertInfoVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("电子文件签署节点-证书信息-对象")
public class SignReportSignCertInfoVo extends ReportUserConfirmInfo implements Serializable {

    private static final long serialVersionUID = -5812415749492093699L;

    // @ApiModelProperty("证书类型")
    private String certType;

    // @ApiModelProperty("证书状态")
    private String certStatus;

    // @ApiModelProperty("颁发机构")
    private String certIssueOrg;

    // @ApiModelProperty("签名算法")
    private String certAlgorithm;

    // @ApiModelProperty("序列号")
    private String certSequenceNo;

    // @ApiModelProperty("主体信息")
    private String certSubject;

    // @ApiModelProperty("有效期")
    private String certExpireTime;

}
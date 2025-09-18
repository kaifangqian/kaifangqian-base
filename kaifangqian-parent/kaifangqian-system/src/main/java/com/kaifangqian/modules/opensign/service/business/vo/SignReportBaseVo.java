package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: ReportBaseVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: ReportBaseVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("电子文件基本信息")
public class SignReportBaseVo implements Serializable {

    private static final long serialVersionUID = -4453415691383298223L;

    // @ApiModelProperty("文件编号")
    private String code;

    // @ApiModelProperty("文件主题")
    private String subject;

    // @ApiModelProperty("发起时间")
    private String startTime;

    // @ApiModelProperty("签署截止时间")
    private String expireDate;

    // @ApiModelProperty("完成时间")
    private String finishTime;

    // @ApiModelProperty("发起方")
    private String sender ;

    // @ApiModelProperty("签署方")
    private String signer ;

    // @ApiModelProperty("电子文件清单")
    private List<SignReportDocFileVo> docFileVoList ;


}
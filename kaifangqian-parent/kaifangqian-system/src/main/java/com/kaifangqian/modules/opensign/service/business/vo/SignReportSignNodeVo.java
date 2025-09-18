package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SignReportSignNodeVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignReportSignNodeVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("电子文件签署节点对象")
public class SignReportSignNodeVo extends SignReportSignOperatorVo implements Serializable {

    private static final long serialVersionUID = -951383592781165887L;

    private Integer num;

    // @ApiModelProperty("电子文件清单")
    private List<SignReportDocFileVo> docFileVoList;



    // @ApiModelProperty("测试扩展")
    private List<SignReportSignSubVO> subReport1;

}
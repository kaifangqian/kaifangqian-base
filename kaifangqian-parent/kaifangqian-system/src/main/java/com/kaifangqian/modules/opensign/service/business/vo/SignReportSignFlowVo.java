package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SignReportSignFlowVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignReportSignFlowVo
 * @author: FengLai_Gong
 */
@Data
// @ApiModel("电子文件签署流程对象")
public class SignReportSignFlowVo extends SignReportSignOperatorVo implements Serializable {


    private static final long serialVersionUID = -6112659532205549153L;


    // @ApiModelProperty("签署节点列表")
    private List<SignReportSignNodeVo> nodeVoList ;




}
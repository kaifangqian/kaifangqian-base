package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.api.vo.base.ContractUser;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: SignCallbackVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignCallbackVo
 * @author: FengLai_Gong
 */
// @ApiModel("回调数据对象")
@Data
public class SignCallbackVo implements Serializable {


    private static final long serialVersionUID = -1432518813216743114L;

    // @ApiModelProperty("合同id")
    private String contractId;

    // @ApiModelProperty("合同状态，见contractStatus表")
    private String contractStatus;

    // @ApiModelProperty("通知的事件类型")
    private String callbackType;

    // @ApiModelProperty("填写、签署任务的id")
    private String taskId;

    // @ApiModelProperty("签署方类型,PERSONAL-个人,ENTERPRISE-企业")
    private String signerType;

    // @ApiModelProperty("签署方名称，签署方类型为企业时，返回企业名称")
    private String signerName;

    // @ApiModelProperty("签署节点类型：PERSONAL_SIGN-个人签字,AGENT_SIGN-经办人签字,LEGAL_PERSON_SIGN-法人签字,ENTERPRISE_SEAL-企业签章")
    private String nodeType;

    // @ApiModelProperty("办理人")
    private ContractUser signer;

    // @ApiModelProperty("原因")
    private String reason ;


}
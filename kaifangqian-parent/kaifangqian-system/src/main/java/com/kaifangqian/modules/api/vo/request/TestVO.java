package com.kaifangqian.modules.api.vo.request;

import com.kaifangqian.modules.api.base.ReqBaseVO;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : zhenghuihan
 * create at:  2024/3/29  14:10
 * @description:
 */
@Data
public class TestVO extends ReqBaseVO {
    private String name;


    // @ApiModelProperty("合同ID")
    private String signRuId;

    // @ApiModelProperty("账号")
    private String username;

    // @ApiModelProperty("身份信息")
    private String tenantName;

    // @ApiModelProperty("任务ID")
    private String taskId;
}
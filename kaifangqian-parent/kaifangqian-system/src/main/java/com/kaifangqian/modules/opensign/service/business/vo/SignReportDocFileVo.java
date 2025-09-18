package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: SignReportDocFileVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignReportDocFileVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// @ApiModel("电子文件清单对象")
public class SignReportDocFileVo implements Serializable {

    private static final long serialVersionUID = 2603882372359338534L;

    // @ApiModelProperty("第一个值")
    private String valueFirst;

    // @ApiModelProperty("第二个值")
    private String valueSecond;
}
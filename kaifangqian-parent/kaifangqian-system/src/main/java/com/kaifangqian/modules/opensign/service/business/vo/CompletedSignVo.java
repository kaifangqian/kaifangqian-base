package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.vo.base.sign.DocControlVo;
// import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: CompletedSignVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: CompletedSignVo
 * @author: FengLai_Gong
 */
@Data
public class CompletedSignVo implements Serializable {

    // @ApiModelProperty("业务线实例id")
    private String signRuId ;
    // @ApiModelProperty("签署意愿校验订单号")
    private String signConfirmOrderNo ;
    // @ApiModelProperty("任务id")
    private String taskId ;

    // @ApiModelProperty("签章文件id")
    private String sealAnnexId ;


    // @ApiModelProperty("证书持有者租户id")
    private String certTenantId ;
    // @ApiModelProperty("证书类型")
    private Integer certType ;
    // @ApiModelProperty("证书持有类型")
    private Integer certHolderType ;

    // @ApiModelProperty("当前操作人-账号id")
    private String userId ;
    // @ApiModelProperty("当前操作人-租户id")
    private String tenantId ;
    // @ApiModelProperty("当前操作人-租户下用户id")
    private String tenantUserId ;

    private Integer operateType ;


    // @ApiModelProperty("签署控件数据")
    private List<DocControlVo> controlList ;


}
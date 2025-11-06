package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignReSender;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 签署人内层数据
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: ReSaveDataSender
 * @author: FengLai_Gong
 */
@Data
public class ReSaveDataSender implements Serializable {

    private static final long serialVersionUID = 856850570898002451L;

    private Integer confirmType ;

    private Integer signerType ;

    private String verifyType;

    private Integer agreeSkipWillingness;

    //个人签署节点实名认证类型：value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;

    // @ApiModelProperty("不限制：NOLIMIT；个人签名方式：TEMPLATE：模板生成、HAND：手写签名")
    private String sealType ;

    private SignReSender sender ;
}
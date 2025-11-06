package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuSender;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: RuDataSender
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuDataSender
 * @author: FengLai_Gong
 */
@Data
public class RuDataSender implements Serializable {

    private static final long serialVersionUID = 2524425982890097318L;

    //人脸校验
    private Integer confirmType ;

    private SignRuSender ruSender ;

    private String reSenderId ;

    private String verifyType;

    //"是否为快速签署，1是，0否"
    private Integer agreeSkipWillingness;

    //value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;

    // 不限制：NOLIMIT；个人签名方式：TEMPLATE：模板生成、HAND：手写签名"
    private String sealType ;



}
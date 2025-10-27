package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuSender;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: RuSaveDataSender
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuSaveDataSender
 * @author: FengLai_Gong
 */
@Data
public class RuSaveDataSender implements Serializable {

    private static final long serialVersionUID = 8945808547954677730L;

    //人脸校验
    private Integer confirmType ;

    //验证类型
    private String verifyType;

    //签署方是否同意跳过
    private Integer agreeSkipWillingness;

    //签署方信息
    private SignRuSender sender ;

    //value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;
}
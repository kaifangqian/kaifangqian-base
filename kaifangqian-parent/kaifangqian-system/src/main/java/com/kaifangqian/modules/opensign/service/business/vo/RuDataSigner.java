package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuSigner;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: RuDataSinger
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuDataSinger
 * @author: FengLai_Gong
 */
@Data
public class RuDataSigner implements Serializable {

    private static final long serialVersionUID = 4022420355975768300L;

    private SignRuSigner ruSigner ;

    //人脸校验
    private Integer confirmType ;
    //签署方类型
    private Integer signerType ;

    private String reSignerId ;

    private String verifyType;

    private Integer agreeSkipWillingness;

    //value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;

    private List<RuDataSender> addSenderList = new ArrayList<>();

    private List<RuDataSender> updateSenderList = new ArrayList<>();

    private List<RuDataSender> deleteSenderList = new ArrayList<>();


}
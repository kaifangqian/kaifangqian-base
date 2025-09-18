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

    private List<RuDataSender> addSenderList = new ArrayList<>();

    private List<RuDataSender> updateSenderList = new ArrayList<>();

    private List<RuDataSender> deleteSenderList = new ArrayList<>();


}
package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuSigner;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: RuSaveDataSigner
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuSaveDataSigner
 * @author: FengLai_Gong
 */
@Data
public class RuSaveDataSigner implements Serializable {

    private static final long serialVersionUID = -3207654389580916284L;

    //人脸校验
    private Integer confirmType ;
    //签署方类型
    private Integer signerType ;

    private SignRuSigner ruSigner ;

    private String verifyType;

    private Integer agreeSkipWillingness;

    private List<RuSaveDataSender> addSenderList = new ArrayList<>();

    private List<RuSaveDataSender> updateSenderList = new ArrayList<>();

    private List<RuSaveDataSender> deleteSenderList = new ArrayList<>();

}
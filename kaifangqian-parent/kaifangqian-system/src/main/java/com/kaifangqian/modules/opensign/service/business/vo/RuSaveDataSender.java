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

    private String verifyType;

    private Integer agreeSkipWillingness;

    private SignRuSender sender ;
}
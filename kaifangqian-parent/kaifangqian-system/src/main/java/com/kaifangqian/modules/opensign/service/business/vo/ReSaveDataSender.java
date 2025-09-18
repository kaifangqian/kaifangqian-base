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

    private SignReSender sender ;
}
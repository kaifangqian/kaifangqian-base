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

    //签署方信息
    private SignRuSigner ruSigner ;

    //签署方校验
    private String verifyType;

    //签署方是否同意跳过
    private Integer agreeSkipWillingness;

    //value字段值为：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）
    private String personalSignAuth ;

    //签署方发送人列表
    private List<RuSaveDataSender> addSenderList = new ArrayList<>();

    //签署方修改人列表
    private List<RuSaveDataSender> updateSenderList = new ArrayList<>();

    //签署方删除人列表
    private List<RuSaveDataSender> deleteSenderList = new ArrayList<>();

}
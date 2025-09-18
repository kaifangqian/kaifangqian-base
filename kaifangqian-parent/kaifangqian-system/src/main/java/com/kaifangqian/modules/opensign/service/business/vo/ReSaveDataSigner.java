package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignReSender;
import com.kaifangqian.modules.opensign.entity.SignReSigner;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 签署人外层数据
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: ReSaveDataSigner
 * @author: FengLai_Gong
 */
@Data
public class ReSaveDataSigner implements Serializable {

    private static final long serialVersionUID = -3550712732980430493L;

    //人脸校验
    private Integer confirmType ;
    //签署方类型
    private Integer signerType ;

    //验证方式
    private String verifyType;

    //是否快速签署
    private Integer agreeSkipWillingness;

    //签署方数据
    private SignReSigner reSigner ;
    //内部节点数据
    private List<ReSaveDataSender> addSenderList = new ArrayList<>();

    private List<ReSaveDataSender> updateSenderList = new ArrayList<>();

    private List<SignReSender> deleteSenderList = new ArrayList<>();



}
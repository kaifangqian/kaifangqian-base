package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignReSender;
import com.kaifangqian.modules.opensign.entity.SignRuSender;
import com.kaifangqian.modules.opensign.vo.base.sign.DocSenderVo;
// import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SenderCalculateVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SenderCalculateVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @ApiModel("发起人内部设置-变更-计算对象")
public class SenderCalculateVo implements Serializable {

    private static final long serialVersionUID = -6782736141135775361L;

    //新增发起方
    private List<DocSenderVo> addSenderVoList ;
    //更新发起方
    private List<DocSenderVo> updateSenderVoList ;
    //删除-业务线实例-发起方
    private List<SignRuSender> deleteRuSenderList ;
    //删除-业务线配置-发起方
    private List<SignReSender> deleteReSenderList ;


}
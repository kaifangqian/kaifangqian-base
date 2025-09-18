package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignReSigner;
import com.kaifangqian.modules.opensign.entity.SignRuSigner;
import com.kaifangqian.modules.opensign.vo.base.sign.DocSignerVo;
// import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: SignerCalculateVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: SignerCalculateVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @ApiModel("签署人-变更-计算对象")
public class SignerCalculateVo implements Serializable {


    private static final long serialVersionUID = -6220875438180999591L;


    //新增签署人列表
    private List<DocSignerVo> addSignerList ;
    //更新签署人列表
    private List<DocSignerVo> updateSignerList ;
    //删除业务线实例-签署人列表
    private List<SignRuSigner> deleteRuSignerList ;
    //删除业务线配置-签署人列表
    private List<SignReSigner> deleteReSignerList ;
}
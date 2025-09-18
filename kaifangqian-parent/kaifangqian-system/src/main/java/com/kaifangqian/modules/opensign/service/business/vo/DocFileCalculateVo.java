package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignReDoc;
import com.kaifangqian.modules.opensign.entity.SignRuDoc;
import com.kaifangqian.modules.opensign.vo.base.sign.DocFileVo;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: DocFileCalculateVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: DocFileCalculateVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @ApiModel("签约文件-变更-计算对象")
public class DocFileCalculateVo implements Serializable {


    private static final long serialVersionUID = 6169504173659456994L;

    // @ApiModelProperty("新增签约文件列表")
    private List<DocFileVo> addFileVoList ;

    // @ApiModelProperty("删除-业务线实例-签约文件列表")
    private List<SignRuDoc> deleteRuDocList ;

    // @ApiModelProperty("删除-业务线配置-签约文件列表")
    private List<SignReDoc> deleteReDocList ;



}
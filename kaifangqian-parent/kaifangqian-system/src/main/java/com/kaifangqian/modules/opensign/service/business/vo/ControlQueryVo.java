package com.kaifangqian.modules.opensign.service.business.vo;

// import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: ControlQueryVo
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: ControlQueryVo
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControlQueryVo implements Serializable {

    private static final long serialVersionUID = 4846496787151112176L;

    // @ApiModelProperty("'业务线实例主表id'")
    private String signRuId ;

    // @ApiModelProperty("'业务线实例签约文件id'")
    private String signRuDocId ;

    // @ApiModelProperty("'业务线实例签约文件id列表'")
    private List<String> signRuDocIdList ;

    // @ApiModelProperty("'业务线配置主表id'")
    private String signReId;

    // @ApiModelProperty("'业务线配置-签约文件id'")
    private String signReDocId ;

    // @ApiModelProperty("'业务线配置签约文件id列表'")
    private List<String> signReDocIdList ;

    // @ApiModelProperty("'人员类型，1发起方，2接收方'")
    private Integer signerType ;

    // @ApiModelProperty("'签署方id，signerId或者senderId'")
    private String signerId ;

    // @ApiModelProperty("'签署方id列表，signerId或者senderId'")
    private List<String> signerIdList ;

    // @ApiModelProperty("'控件类型'")
    private String controlType ;

    // @ApiModelProperty("'控件类型列表")
    private List<String> controlTypeList;

    // @ApiModelProperty("''异常状态，1为是，2为否''")
    private Integer errorStatus ;


}
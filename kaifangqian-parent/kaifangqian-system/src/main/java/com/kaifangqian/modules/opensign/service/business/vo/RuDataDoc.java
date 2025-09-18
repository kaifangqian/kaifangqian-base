package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuDoc;
import com.kaifangqian.modules.opensign.entity.SignRuDocOperate;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: RuDataDoc
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuDataDoc
 * @author: FengLai_Gong
 */
@Data
public class RuDataDoc implements Serializable {

    private static final long serialVersionUID = 1285443698871863855L;

    private SignRuDoc ruDoc ;

    private SignRuDocOperate ruDocOperate ;

    private byte[] fileByte ;

    private List<RuDataControl> writeControlList = new ArrayList<>();
    //业务线源文档id
    private String reDocId ;
    //文档页码
    private Integer docPage ;
    //业务线源文档类型，模板，上传
    private Integer docType ;
    //文档来源类型，自行上传，业务线
    private Integer originType ;
    //文档来源id
    private String originId ;
}
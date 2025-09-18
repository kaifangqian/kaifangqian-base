package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuDocControl;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: RuDataControl
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuDataControl
 * @author: FengLai_Gong
 */
@Data
public class RuDataControl implements Serializable {

    private static final long serialVersionUID = 1151264382405310590L;

    private SignRuDocControl ruDocControl ;

    private String reSignerId ;

    private String reDocId ;

    private List<RuDataControlProperty> dataControlPropertyList = new ArrayList<>();



}
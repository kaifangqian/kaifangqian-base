package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuDocControlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: RuDataControlProperty
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuDataControlProperty
 * @author: FengLai_Gong
 */
@Data
public class RuDataControlProperty implements Serializable {

    private static final long serialVersionUID = 981458709692684199L;

    private SignRuDocControlProperty ruDocControlProperty ;

    private String reDocId ;




}
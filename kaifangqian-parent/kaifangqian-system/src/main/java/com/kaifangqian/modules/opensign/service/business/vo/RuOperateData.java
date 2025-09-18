package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRuDocOperate;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: RuOperateData
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuOperateData
 * @author: FengLai_Gong
 */
@Data
public class RuOperateData implements Serializable {


    private static final long serialVersionUID = -3986602128027964422L;

    //旧操作文件数据对象
    private SignRuDocOperate oldRuDocOperate ;

    //新操作文件数据对象
    private SignRuDocOperate newRuDocOperate ;

    //新操作文件二进制数据
    private byte[] newDocFileByte ;

    //新操作文件id
    private String newAnnexId ;


}
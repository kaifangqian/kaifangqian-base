package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuCcer;
import com.kaifangqian.modules.opensign.entity.SignRuDoc;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: RuSaveData
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuSaveData
 * @author: FengLai_Gong
 */
@Data
public class RuSaveData implements Serializable {

    private static final long serialVersionUID = 1646294838045427850L;

    private String ruId ;

    private SignRu ru ;

    private List<RuSaveDataSigner> addSignerList = new ArrayList<>();

    private List<RuSaveDataSigner> updateSignerList = new ArrayList<>();

    private List<RuSaveDataSigner> deleteSignerList = new ArrayList<>();

    //添加文档相关数据
    private List<RuDataDoc> addDocList = new ArrayList<>();

    private List<SignRuDoc> updateDocOrderList = new ArrayList<>();

    //删除文档相关数据
    private List<String> deleteDocIdList = new ArrayList<>();
    private List<String> deleteControlIdList = new ArrayList<>();
    private List<String> deleteControlPropertyIdList = new ArrayList<>();
    private List<String> deleteControlPropertyByControlIdList = new ArrayList<>();


    //抄送人
    private List<SignRuCcer> ccerList = new ArrayList<>();
    //附件列表
    private List<String> otherFileList = new ArrayList<>();


    private String reId ;

}
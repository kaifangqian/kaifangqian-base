package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.vo.GlobalIdConfigModule;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ReDataService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: ReDataService
 * @author: FengLai_Gong
 */
@Data
public class ReSaveData implements Serializable {

    private static final long serialVersionUID = 3224694325951336555L;

    //业务线主数据
    private SignRe re ;
    //业务线主数据id
    private String reId ;

    //签署人类型，1自定义、2预设流程
    private Integer signerType ;

    //新增签署人
    private List<ReSaveDataSigner> addSignerList = new ArrayList<>();
    //更新签署人
    private List<ReSaveDataSigner> updateSignerList = new ArrayList<>();
    //删除签署人
    private List<ReSaveDataSigner> deleteSignerList = new ArrayList<>();


    private List<SignReCcer> ccerList = new ArrayList<>();

    //新增文档
    private List<SignReDoc> addReDocList = new ArrayList<>();
    //删除文档
    private List<String> deleteDocIdList = new ArrayList<>();

    //更新文档顺序列表
    private List<SignReDoc> updateDocOrderList = new ArrayList<>();

    //删除控件
    private List<String> deleteControlIdList = new ArrayList<>();
    //删除控件属性
    private List<String> deleteControlPropertyIdList = new ArrayList<>();
    //根据控件删除控件属性列表
    private List<String> deletePropertyByControlIdList = new ArrayList<>();
    //删除填写控件关联关系列表
    private List<String> deleteReDocParamIdList = new ArrayList<>();



    //模板数据，用于更新模板页码，适用于老数据
    private List<SignTemplateRecord> updateTemplateRecordList = new ArrayList<>();
    //新增权限
    private List<SignReAuth> addAuthList = new ArrayList<>();

    //对应分组
    private SignReFolder reFolder ;
    //对应分组关系
    private SignReFolderRelation reFolderRelation ;


    private SignReRule codeReRule ;

    private List<SignReRuleDetail> codeDetails;

    private GlobalIdConfigModule codeModule ;



    private SignReRule subjectReRule ;

    private List<SignReRuleDetail> subjectDetails;

    private GlobalIdConfigModule subjectModule ;



}
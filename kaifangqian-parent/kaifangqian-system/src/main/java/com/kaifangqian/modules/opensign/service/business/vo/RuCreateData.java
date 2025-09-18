package com.kaifangqian.modules.opensign.service.business.vo;

import com.kaifangqian.modules.api.vo.base.ContractSigner;
import com.kaifangqian.modules.opensign.entity.*;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.SysTenantUser;
import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.modules.opensign.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @Description: RuCreateData
 * @Package: com.kaifangqian.modules.opensign.service.business.vo
 * @ClassName: RuCreateData
 * @author: FengLai_Gong
 */
@Data
public class RuCreateData implements Serializable {

    private static final long serialVersionUID = 4060087514043132593L;

    //主数据id
    private String ruId ;

    private SignRu signRu ;
    //签署人列表
    private List<RuDataSigner> signerList = new ArrayList<>();
    //文档列表
    private List<RuDataDoc> docList = new ArrayList<>();

    private List<String> reDocIdList = new ArrayList<>();

    //填写控件列表
    private List<RuDataControl> writeControlList = new ArrayList<>();
    //签署控件列表
    private List<RuDataControl> signControlList = new ArrayList<>();
//    //控件属性列表
//    private List<RuDataControlProperty> signControlPropertyList = new ArrayList<>();

    //抄送人
    private List<SignRuCcer> ccerList = new ArrayList<>();
    //附件列表
    private List<String> otherFileList = new ArrayList<>();
    //业务线设置签署人类型
    private Integer reSignerType ;


    //业务线配置签约文件和业务线实例签约文件关联关系，key是reDocId，value是ruDocId
    Map<String,String> reDoc2RuDocMap = new HashMap<>();
    //业务线配置签署人和业务线实例签署人关联关系，key是reSignerId，value是ruSignerId
    Map<String,String> reSigner2RuSingerMap = new HashMap<>();


    //账户数据
    private SysUser sysUser ;
    //租户数据
    private SysTenantInfo tenantInfo ;
    //租户下用户数据
    private SysTenantUser tenantUser ;

    //到期时间
    private Date expireDate ;

    private List<String> templateIdList = new ArrayList<>();

    private Map<String,String> templateId2RuDocMap = new HashMap<>();


    private List<String> addDocAnnexId = new ArrayList<>();

    private Map<String,String> addDocAnnex2RuDocMap = new HashMap<>();

    //请求参数中合同签署人map，key为签署人顺序，value为签署人对象数据
    private Map<String, ContractSigner> contractOrderMap = new HashMap<>();

    //主数据id
    private String reId ;

    //签署控件、签署控件属性、关键字、关键字属性
    private List<SignRuDocControl> ruSignControlList = new ArrayList<>();
    private List<SignRuDocControlProperty> ruSignControlPropertyList = new ArrayList<>();
    private List<SignRuKeyword> ruKeywordList = new ArrayList<>();
    private List<SignRuKeywordProperty> ruKeywordPropertyList = new ArrayList<>();



}
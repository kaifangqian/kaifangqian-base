package com.kaifangqian.modules.cert.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.modules.cert.enums.*;
import com.kaifangqian.modules.cert.pojo.*;
import com.kaifangqian.modules.system.entity.CertificateInfo;
import com.kaifangqian.modules.system.entity.TenantCertificate;
import com.kaifangqian.modules.system.vo.TenantInfoDTO;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.entity.SysConfig;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.cert.enums.*;
import com.kaifangqian.modules.cert.pojo.*;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.opensign.service.tool.CertificateUtils;
import com.kaifangqian.modules.opensign.service.tool.pojo.BaseCertificateInfo;
import com.kaifangqian.modules.cert.tool.GenerateRootCertificate;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.service.tool.pojo.CertificateProperty;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.modules.system.service.CertificateInfoDao;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ITenantCertificateService;
import com.kaifangqian.modules.system.service.ITenantInfoExtendService;
import com.kaifangqian.service.ISysConfigService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description: CertBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: CertBusinessService
 * @author: FengLai_Gong
 */
@Service
public class CertBusinessService {


    public static final String PASSWORD = "123456";

    public static final String REAL_CA_ISSUE_ORG = "中认环宇";

    public static final String TEST_CA_ISSUE_ORG = "开放签测试证书";

    public static final String SYSTEM_ISSUE_ORG = "开放签防篡改证书";


    //    @Autowired
//    private SignCertInfoService certInfoService ;
    @Autowired
    private SignFileService fileService;


    @Autowired
    private CertificateInfoDao certificateInfoDao;
    @Autowired
    private ITenantCertificateService tenantCertificateService;
    @Autowired
    private ITenantInfoExtendService tenantInfoExtendService;
    @Autowired
    private ISysTenantInfoService tenantInfoService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private CertHttpService certHttpService;

    //    @Value("${rootCertPath}")
//    private String rootCertPath;
    @Autowired
    private FileProperties fileProperties;


    /**
     * @return void
     * @Description #使租户下所有证书失效
     * @Param [tenantId]
     **/
    public void unable(String tenantId, List<Integer> certTypeList) {


        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId, tenantId);
        wrapper.lambda().eq(TenantCertificate::getCertStatus, TenantCertStatusEnum.ENABLE.getCode());
        wrapper.lambda().in(TenantCertificate::getCertType, certTypeList);
        TenantCertificate certificate = new TenantCertificate();
        certificate.setCertStatus(TenantCertStatusEnum.UN_ENABLE.getCode());
        tenantCertificateService.update(certificate, wrapper);


    }

    /**
     * @return void
     * @Description #使租户下该证书失效
     * @Param [tenantId]
     **/
    public void unable(String tenantCertificateId) {

        TenantCertificate certificate = new TenantCertificate();
        certificate.setId(tenantCertificateId);
        certificate.setCertStatus(TenantCertStatusEnum.UN_ENABLE.getCode());
        tenantCertificateService.updateById(certificate);

    }


    /**
     * @Description #事件证书使用模式
     * @Param []
     * @return java.lang.Integer
     **/
    public Integer getEventCaUseModel(){
        SysConfig eventCaUseModel = sysConfigService.getByType("event_ca_use_model");
        if (eventCaUseModel == null || eventCaUseModel.getValue() == null) {
            return EventCaUseModelEnum.ONCE_USE.getCode();
        }
        Integer value = Integer.valueOf(eventCaUseModel.getValue());
        return value;
    }

    /**
     * @return java.lang.Boolean
     * @Description #获取系统证书配置
     * @Param []
     **/
    public Boolean getCertSysConfig() {
        SysConfig ca_type = sysConfigService.getByType("ca_type");
        if (ca_type == null || ca_type.getValue() == null) {
            return false;
        }
        Boolean value = Boolean.valueOf(ca_type.getValue());
        return value;
    }

    /**
     * @return java.lang.Boolean
     * @Description #获取系统使用证书配置
     * @Param []
     **/
    public Integer getCertUseConfig() {
        SysConfig ca_type = sysConfigService.getByType("ca_use_type");
        if (ca_type == null || ca_type.getValue() == null) {
            return 0;
        }
        Integer value = Integer.valueOf(ca_type.getValue());
        return value;
    }


    /**
     * @return java.lang.Integer
     * @Description #根据系统配置，计算最终需要的证书类型
     * @Param [certType]
     **/
    public CertTypeEnum calculateCertType(Integer certType, Integer holderType) {
        if (certType.equals(CertTypeEnum.SYSTEM.getCode())) {
            //平台防篡改证书
            return CertTypeEnum.SYSTEM;
        } else {
            Boolean certSysConfig = getCertSysConfig();
            //获取系统证书配置
            if (certSysConfig != null && certSysConfig) {
                Integer certUseConfig = getCertUseConfig();
                if (certUseConfig == null || certUseConfig == 0) {
                    //生成CA测试证书
                    return CertTypeEnum.CA_TEST;
                } else if (CertUseTypeEnum.ALL_SHORT_TERM.getCode().equals(certUseConfig)) {
                    //生成真实事件证书
                    return CertTypeEnum.CA_SHORT_TERM;
                } else if (CertUseTypeEnum.ALL_LONG_TERM.getCode().equals(certUseConfig)) {
                    //生成真实长效证书
                    return CertTypeEnum.CA_LONG_TERM;
                } else if (CertUseTypeEnum.PERSON_SHORT_ENT_LONG.getCode().equals(certUseConfig)) {
                    if (CertHolderTypeEnum.PERSONAL.getCode().equals(holderType)) {
                        //生成真实事件证书
                        return CertTypeEnum.CA_SHORT_TERM;
                    } else if (CertHolderTypeEnum.ENTERPRISE.getCode().equals(holderType)) {
                        //生成真实长效证书
                        return CertTypeEnum.CA_LONG_TERM;
                    }
                }
            } else {
                //生成CA测试证书
                return CertTypeEnum.CA_TEST;
            }
        }
        //平台防篡改证书
        return CertTypeEnum.SYSTEM;
    }

    /**
     * @return com.kaifangqian.modules.opensign.service.tool.pojo.CertificateProperty
     * @Description #根据参数获取相应证书, 如果证书没有或者过期了，重新发放证书
     * @Param [tenantId, certTypeEnum, holderType]
     **/
    public CertificateProperty findAndGenerateCertProperty(String tenantId, CertTypeEnum certTypeEnum, Integer holderType) {
        if (tenantId == null || certTypeEnum == null || holderType == null) {
            throw new PaasException("查找证书-租户参数缺失");
        }
        TenantCertificate tenantCertificate = null;
        CertificateProperty certificateProperty = null;
        CertificateInfo certificateInfo = null;
        Boolean generateRealCACertFlag = false;

        Integer eventCaUseModel = getEventCaUseModel();
        if(eventCaUseModel == EventCaUseModelEnum.EXPIRE_USE.getCode()){
            List<TenantCertificate> enabledCertList = tenantCertificateService.getEnabledCertList(tenantId, certTypeEnum);
            if(enabledCertList != null && enabledCertList.size() > 0){
                for(TenantCertificate enableTenantCertificate : enabledCertList){
                    if (enableTenantCertificate != null && enableTenantCertificate.getCertId() != null && enableTenantCertificate.getCertId().length() > 0) {
                        CertificateInfo enableCertificateInfo = certificateInfoDao.getById(enableTenantCertificate.getCertId());
                        if(enableCertificateInfo != null){
                            //如果证书过期，重新颁发证书
                            Date termOfValidityEndTime = enableCertificateInfo.getTermOfValidityEndTime();
                            if (termOfValidityEndTime != null && termOfValidityEndTime.after(new Date())) {
                                tenantCertificate = enableTenantCertificate ;
                                certificateInfo = enableCertificateInfo ;
                            }else {
                                enableTenantCertificate.setCertStatus(TenantCertStatusEnum.UN_ENABLE.getCode());
                                tenantCertificateService.updateById(enableTenantCertificate);
                            }
                        }
                    }
                }
                if(tenantCertificate == null || certificateInfo == null){
                    generateRealCACertFlag = true;
                }
            }else {
                generateRealCACertFlag = true;
            }

        }else if(eventCaUseModel == EventCaUseModelEnum.ONCE_USE.getCode()) {
            if (certTypeEnum.getCode() == CertTypeEnum.CA_TEST.getCode() || certTypeEnum.getCode() == CertTypeEnum.CA_SHORT_TERM.getCode()) {
                //如果是测试证书或者是短期事件证书
                generateRealCACertFlag = true;
            } else {
                tenantCertificate = tenantCertificateService.getEnabledCert(tenantId, certTypeEnum);
                if (tenantCertificate != null && tenantCertificate.getCertId() != null && tenantCertificate.getCertId().length() > 0) {
                    certificateInfo = certificateInfoDao.getById(tenantCertificate.getCertId());
                }
                if (certificateInfo == null) {
                    generateRealCACertFlag = true;
                } else {
                    //如果证书过期，重新颁发证书
                    Date termOfValidityEndTime = certificateInfo.getTermOfValidityEndTime();
                    if (termOfValidityEndTime != null && termOfValidityEndTime.before(new Date())) {
                        //如果是中认环宇颁发的证书，但是时间上失效了，也需要重新颁发
                        generateRealCACertFlag = true;
                    }
                }
            }
        }
        if (generateRealCACertFlag) {
            //重新颁发证书
            TenantCertInfo tenantCertInfo = new TenantCertInfo();
            tenantCertInfo.setTenantId(tenantId);
            tenantCertInfo.setHolderType(holderType);
            tenantCertInfo.setCertType(certTypeEnum.getCode());
            TenantInfoDTO tenantInfoExt = tenantInfoExtendService.getTenantInfoExt(tenantId);
            if (tenantInfoExt != null) {
                if (holderType.equals(CertHolderTypeEnum.PERSONAL.getCode())) {
                    tenantCertInfo.setName(tenantInfoExt.getName());
                    tenantCertInfo.setPhone(tenantInfoExt.getPhone());
                    tenantCertInfo.setIdCardNumber(tenantInfoExt.getOrganizationNo());
                } else if (holderType.equals(CertHolderTypeEnum.ENTERPRISE.getCode())) {
                    tenantCertInfo.setEntName(tenantInfoExt.getName());
                    tenantCertInfo.setUscc(tenantInfoExt.getOrganizationNo());
                }
            }
            //生成证书或者是申请第三方真实证书
            String tenantCertificateId = generateTenantCert(tenantCertInfo);
            if (tenantCertificateId == null) {
                throw new PaasException("证书过期，重新颁发证书失败");
            }
            tenantCertificate = tenantCertificateService.getById(tenantCertificateId);
            if (tenantCertificate == null) {
                throw new PaasException("证书过期，重新颁发证书失败");
            }
            certificateInfo = certificateInfoDao.getById(tenantCertificate.getCertId());
            if (certificateInfo == null) {
                throw new PaasException("证书过期，重新颁发证书失败");
            }
        }


        byte[] certFile = fileService.getByteByFatherIdAndDataCategory(SignFileEnum.PFX, certificateInfo.getId());
        if (certFile == null) {
            return certificateProperty;
        }
        certificateProperty = new CertificateProperty();
        String password = certificateInfo.getCertPassword();
        try {
            password = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, password);
        } catch (Exception e) {
            throw new PaasException("证书密码解密失败");
        }
        certificateProperty.setPassword(password);
        certificateProperty.setCertType("PKCS12");
        certificateProperty.setCertFile(certFile);
        //证书数据id
        certificateProperty.setCertificateInfoId(certificateInfo.getId());
        //证书关联租户数据
        certificateProperty.setTenantCertificateId(tenantCertificate.getId());
        certificateProperty.setTenantCertificateType(tenantCertificate.getCertType());
        certificateProperty.setTenantId(tenantCertificate.getTenantId());

        //校验证书开始有效时间
        if(certificateInfo.getTermOfValidityStartTime() == null){
            throw new PaasException("证书开始有效时间异常");
        }
        //获取证书开始有效时间
        Date termOfValidityStartTime = certificateInfo.getTermOfValidityStartTime();
        //获取当前时间5秒之后的时间
        long certValidityStartTime = termOfValidityStartTime.getTime();

        long currentTimeMillis = System.currentTimeMillis();

        long sub = certValidityStartTime - currentTimeMillis ;


        if(sub > 0l){
            if(sub > 5000l){
                //证书开始有效时间如果大于当前时间5秒钟
                throw new PaasException("证书开始有效时间大于当前时间5秒钟");
            }else {
                //证书开始有效时间如果大于当前时间5秒钟
                try {
                    Thread.sleep(sub);
                } catch (Exception e) {
                    throw new PaasException("等待证书生效异常");
                }
            }
        }

        return certificateProperty;
    }

    /**
     * @return java.lang.String
     * @Description #申请租户下证书
     * @Param [certTypeEnum 证书类型 , tenantCertInfo 证书所需信息]
     **/
    @Transactional(rollbackFor = Exception.class)
    public String generateTenantCert(TenantCertInfo tenantCertInfo) {

        if (tenantCertInfo == null || tenantCertInfo.getCertType() == null || tenantCertInfo.getHolderType() == null || tenantCertInfo.getTenantId() == null) {
            throw new PaasException("证书参数缺失");
        }
        CertificateInfo certificateInfo = generateCert(tenantCertInfo);
        if (certificateInfo == null || certificateInfo.getId() == null) {
            throw new PaasException("证书生成失败");
        }

        //将之前该类型的证书设置为失效
        tenantCertificateService.unable(tenantCertInfo.getTenantId(), certificateInfo.getCertType());
        //新增有效的证书
        TenantCertificate tenantCertificate = new TenantCertificate();
        tenantCertificate.setCertId(certificateInfo.getId());
        tenantCertificate.setTenantId(tenantCertInfo.getTenantId());
        tenantCertificate.setCertType(certificateInfo.getCertType());
        tenantCertificate.setHolderType(certificateInfo.getHolderType());
        tenantCertificate.setCertStatus(TenantCertStatusEnum.ENABLE.getCode());

        boolean save = tenantCertificateService.save(tenantCertificate);
        if (!save) {
            throw new PaasException("证书申请失败");
        }

        return tenantCertificate.getId();
    }

    /**
     * @return com.kaifangqian.modules.system.entity.CertificateInfo
     * @Description #生成证书数据
     * @Param [tenantCertInfo]
     **/
    public CertificateInfo generateCert(TenantCertInfo tenantCertInfo) {
        CertificateInfo certificateInfo = new CertificateInfo();
        //生成证书主体主题信息
        certificateInfo.setHolderType(tenantCertInfo.getHolderType());
        String identifySubject = "";
        if (certificateInfo.getHolderType().equals(CertHolderTypeEnum.PERSONAL.getCode())) {
            //个人主体
//            identifySubject = tenantCertInfo.getName() + "@" + tenantCertInfo.getIdCardNumber() + "@" + tenantCertInfo.getPhone();
            identifySubject = tenantCertInfo.getName() + "@" + tenantCertInfo.getIdCardNumber();
        } else if (certificateInfo.getHolderType().equals(CertHolderTypeEnum.ENTERPRISE.getCode())) {
            //企业主体
            identifySubject = tenantCertInfo.getEntName() + "@" + tenantCertInfo.getUscc();
        }
        //生成证书主题信息
        Integer certType = tenantCertInfo.getCertType();
        GenerateCertificateInfo generateCertificateInfo = null;
        if (certType.equals(CertTypeEnum.SYSTEM.getCode())) {
            //平台防篡改证书
            //颁发机构
            certificateInfo.setIssueOrg(SYSTEM_ISSUE_ORG);
            certificateInfo.setCertSubject("C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" + "@" + identifySubject + "@" + certificateInfo.getIssueOrg());
            certificateInfo.setCertType(CertTypeEnum.SYSTEM.getCode());
            if (certificateInfo.getCertSubject() != null && certificateInfo.getCertSubject().length() > 0) {
                String certSubject = certificateInfo.getCertSubject();
                certSubject = certSubject.replace("@null", "");
                certificateInfo.setCertSubject(certSubject);
            }
            generateCertificateInfo = generateSystem(certificateInfo.getCertSubject());
        } else if (certType.equals(CertTypeEnum.CA_TEST.getCode())) {
            //生成CA测试证书
            certificateInfo.setIssueOrg(TEST_CA_ISSUE_ORG);
            certificateInfo.setCertSubject("C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" + "@" + identifySubject + "@" + certificateInfo.getIssueOrg());
            certificateInfo.setCertType(CertTypeEnum.CA_TEST.getCode());
            if (certificateInfo.getCertSubject() != null && certificateInfo.getCertSubject().length() > 0) {
                String certSubject = certificateInfo.getCertSubject();
                certSubject = certSubject.replace("@null", "");
                certificateInfo.setCertSubject(certSubject);
            }
            generateCertificateInfo = generateCATest(certificateInfo.getCertSubject());
        } else if (certType.equals(CertTypeEnum.CA_SHORT_TERM.getCode())) {
            //生成真实CA证书
            certificateInfo.setIssueOrg(REAL_CA_ISSUE_ORG);
//            certificateInfo.setCertSubject("C=CN,CN=" + "@" + identifySubject + "@" + certificateInfo.getIssueOrg());
            certificateInfo.setCertSubject("C=CN,CN=" + "@" + identifySubject );
            //改为短期事件证书类型
            certificateInfo.setCertType(CertTypeEnum.CA_SHORT_TERM.getCode());
            if (certificateInfo.getCertSubject() != null && certificateInfo.getCertSubject().length() > 0) {
                String certSubject = certificateInfo.getCertSubject();
                certSubject = certSubject.replace("@null", "");
                certificateInfo.setCertSubject(certSubject);
            }
            generateCertificateInfo = generateCAShortTerm(certificateInfo.getCertSubject());
        } else if (certType.equals(CertTypeEnum.CA_LONG_TERM.getCode())) {
            //长效证书
            certificateInfo.setIssueOrg(REAL_CA_ISSUE_ORG);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = simpleDateFormat.format(new Date());
//            certificateInfo.setCertSubject("C=CN,CN=" + "@" + identifySubject + "@" + certificateInfo.getIssueOrg() + "@" + date);
            certificateInfo.setCertSubject("C=CN,CN=" + "@" + identifySubject);
            //改为长效证书类型
            certificateInfo.setCertType(CertTypeEnum.CA_LONG_TERM.getCode());

            generateCertificateInfo = generateCALongTerm(certificateInfo.getCertSubject(), certificateInfo.getHolderType());
        }
        if (generateCertificateInfo == null || generateCertificateInfo.getPfx() == null) {
            return null;
        }
        certificateInfo.setIssueTime(new Date());
        certificateInfo.setUniqueCode(generateCertificateInfo.getUniqueCode());
        certificateInfo.setTermOfValidityStartTime(generateCertificateInfo.getTermOfValidityStartTime());
        certificateInfo.setTermOfValidityEndTime(generateCertificateInfo.getTermOfValidityEndTime());
        certificateInfo.setCertSuqeNo(generateCertificateInfo.getSerial());
        certificateInfo.setAlgorithmType(generateCertificateInfo.getAlgorithm());
        certificateInfo.setDeleteFlag(false);
        //密码加密
        String password = PASSWORD;
        try {
            password = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        certificateInfo.setCertPassword(password);

        boolean save = certificateInfoDao.save(certificateInfo);
        if (!save) {
            throw new PaasException("证书颁发失败");
        }
        //存储pfx文件
        fileService.saveAnnexStorage(generateCertificateInfo.getPfx(), SignFileEnum.PFX, certificateInfo.getId());


        return certificateInfo;
    }


    /**
     * @return java.lang.String
     * @Description #平台防篡改证书
     * @Param [tenantCertInfo]
     **/
    public GenerateCertificateInfo generateSystem(String subject) {
        byte[] rootCert = getRootByte(fileProperties.getPath().getPath() + "system_root.jks");
        if (rootCert == null) {
            throw new PaasException("没有根证书");
        }
        BaseCertificateInfo baseCertificateInfo = new BaseCertificateInfo();
        baseCertificateInfo.setAlias("root");
        baseCertificateInfo.setCert(loadJks(rootCert, PASSWORD));
        baseCertificateInfo.setPassword(PASSWORD);
        GenerateCertificateInfo generateCertificateInfo = null;
        try {
            generateCertificateInfo = GenerateRootCertificate.instance(subject, CertificateType.RSA).generateCertificate(baseCertificateInfo, PASSWORD, 100, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateCertificateInfo;
    }

    /**
     * @return java.lang.String
     * @Description #CA测试证书
     * @Param [name, idCardNumber, phone]
     **/
    public GenerateCertificateInfo generateCATest(String subject) {
        byte[] rootCert = getRootByte(fileProperties.getPath().getPath() + "ca_root.jks");
        if (rootCert == null) {
            throw new PaasException("没有根证书");
        }
        BaseCertificateInfo baseCertificateInfo = new BaseCertificateInfo();
        baseCertificateInfo.setAlias("root");
        baseCertificateInfo.setCert(loadJks(rootCert, PASSWORD));
        baseCertificateInfo.setPassword(PASSWORD);
        GenerateCertificateInfo generateCertificateInfo = null;
        try {
            generateCertificateInfo = GenerateRootCertificate.instance(subject, CertificateType.RSA).generateCertificate(baseCertificateInfo, PASSWORD, 1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateCertificateInfo;
    }

    /**
     * @return java.lang.String
     * @Description #CA短期事件证书
     * @Param [name, idCardNumber, phone]
     **/
    public GenerateCertificateInfo generateCAShortTerm(String subject) {
        CertificationInfo info = new CertificationInfo();
        info.setCertDn(subject);
        info.setKeyName(CertHttpService.keyName);
        info.setPassword(PASSWORD);
        certHttpService.generateP10(info);

        ApplyCert cert = certHttpService.certEvent(info);
        if (cert == null || cert.getCode() != 10000 || cert.getData() == null || cert.getData().getP7b() == null) {
            throw new PaasException("申请事件证书失败");
        }
        ApplyCertData data = cert.getData();
        byte[] p7bByte = Base64.decode(data.getP7b());
        if (p7bByte == null) {
            throw new PaasException("申请事件证书失败");
        }
        info.setP7bFile(p7bByte);
        certHttpService.p7bToJks(info);
        byte[] pfx = CertificateUtils.coverToPfx(info.getCertFile(), info.getPassword());
        info.setPfxFile(pfx);

        if (info.getPfxFile() == null) {
            throw new PaasException("申请事件证书失败");
        }

        GenerateCertificateInfo generateCertificateInfo = new GenerateCertificateInfo();
        generateCertificateInfo.setPfx(pfx);
        generateCertificateInfo.setCertSubject(data.getCertSubject());
        generateCertificateInfo.setSerial(data.getCertSN());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date termOfValidityStartTime = null;
        Date termOfValidityEndTime = null;
        try {
            termOfValidityStartTime = simpleDateFormat.parse(data.getCertValidityNotBefore());
            termOfValidityEndTime = simpleDateFormat.parse(data.getCertValidityNotAfter());
        } catch (Exception e) {

        }
        generateCertificateInfo.setTermOfValidityStartTime(termOfValidityStartTime);
        generateCertificateInfo.setTermOfValidityEndTime(termOfValidityEndTime);
        generateCertificateInfo.setPassword(PASSWORD);
        return generateCertificateInfo;
    }

    /**
     * @return java.lang.String
     * @Description #CA长期事件证书
     * @Param [name, idCardNumber, phone]
     **/
    public GenerateCertificateInfo generateCALongTerm(String subject, Integer holderType) {
        CertificationInfo info = new CertificationInfo();
        info.setCertDn(subject);
        info.setKeyName(CertHttpService.keyName);
        info.setPassword(PASSWORD);
        certHttpService.generateP10(info);
        ApplyCert cert = null;
        //长期证书
        if (holderType.equals(CertHolderTypeEnum.PERSONAL.getCode())) {
            //个人
            cert = certHttpService.certIndividual(info);
        } else if (holderType.equals(CertHolderTypeEnum.ENTERPRISE.getCode())) {
            //企业
            cert = certHttpService.certEnterprise(info);
        }
        if (cert == null || cert.getCode() != 10000 || cert.getData() == null || cert.getData().getP7b() == null) {
            throw new PaasException("申请事件证书失败");
        }

        ApplyCertData data = cert.getData();
        byte[] p7bByte = Base64.decode(data.getP7b());
        if (p7bByte == null) {
            throw new PaasException("申请事件证书失败");
        }
        info.setP7bFile(p7bByte);
        certHttpService.p7bToJks(info);
        byte[] pfx = CertificateUtils.coverToPfx(info.getCertFile(), info.getPassword());
        info.setPfxFile(pfx);

        if (info.getPfxFile() == null) {
            throw new PaasException("申请事件证书失败");
        }

        GenerateCertificateInfo generateCertificateInfo = new GenerateCertificateInfo();
        generateCertificateInfo.setPfx(pfx);
        generateCertificateInfo.setCertSubject(data.getCertSubject());
        generateCertificateInfo.setSerial(data.getCertSN());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date termOfValidityStartTime = null;
        Date termOfValidityEndTime = null;
        try {
            termOfValidityStartTime = simpleDateFormat.parse(data.getCertValidityNotBefore());
            termOfValidityEndTime = simpleDateFormat.parse(data.getCertValidityNotAfter());
        } catch (Exception e) {

        }
        generateCertificateInfo.setTermOfValidityStartTime(termOfValidityStartTime);
        generateCertificateInfo.setTermOfValidityEndTime(termOfValidityEndTime);
        generateCertificateInfo.setPassword(PASSWORD);
        return generateCertificateInfo;
    }

//    /**
//     * @Description #个人CA证书
//     * @Param [name, idCardNumber, phone]
//     * @return java.lang.String
//     **/
//    public String generateCATest(TenantCertInfo tenantCertInfo){
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" +
//                tenantCertInfo.getName() + "@" + tenantCertInfo.getIdCardNumber() + "@" + tenantCertInfo.getPhone() ;
//        Boolean certSysConfig = getCertSysConfig();
//        if(certSysConfig){
//            x500Name = "C=CN,CN=" + tenantCertInfo.getName() + "@" + tenantCertInfo.getIdCardNumber() + "@" + tenantCertInfo.getPhone() ;
//            return  generateShortTermEventCert(x500Name, CertTypeEnum.CA_PERSONAL,tenantCertInfo.getTenantId());
//        }else {
//            x500Name = x500Name + "@" + TEST_CA_ISSUE_ORG ;
//            return  generateCert(x500Name, CertTypeEnum.CA_PERSONAL,tenantCertInfo.getTenantId());
//        }
//
//
//    }
//


//    /**
//     * @Description #个人CA证书
//     * @Param [name, idCardNumber, phone]
//     * @return java.lang.String
//     **/
//    public String generateCAPrivate(TenantCertInfo tenantCertInfo){
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" +
//                tenantCertInfo.getName() + "@" + tenantCertInfo.getIdCardNumber() + "@" + tenantCertInfo.getPhone() ;
//        Boolean certSysConfig = getCertSysConfig();
//        if(certSysConfig){
//            x500Name = "C=CN,CN=" + tenantCertInfo.getName() + "@" + tenantCertInfo.getIdCardNumber() + "@" + tenantCertInfo.getPhone() ;
//            return  generateShortTermEventCert(x500Name, CertTypeEnum.CA_PERSONAL,tenantCertInfo.getTenantId());
//        }else {
//            x500Name = x500Name + "@" + TEST_CA_ISSUE_ORG ;
//            return  generateCert(x500Name, CertTypeEnum.CA_PERSONAL,tenantCertInfo.getTenantId());
//        }
//
//
//    }


//    /**
//     * @Description #企业CA证书
//     * @Param [entName, uscc]
//     * @return java.lang.String
//     **/
//    public String generateCAEnt(TenantCertInfo tenantCertInfo){
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" + tenantCertInfo.getEntName() + "@" + tenantCertInfo.getUscc() ;
//        Boolean certSysConfig = getCertSysConfig();
//        if(certSysConfig){
//            x500Name = "C=CN,CN=" + tenantCertInfo.getEntName() + "@" + tenantCertInfo.getUscc() ;
//            return  generateShortTermEventCert(x500Name, CertTypeEnum.CA_ENTERPRISE,tenantCertInfo.getTenantId());
//        }else {
//            x500Name = x500Name + "@" + TEST_CA_ISSUE_ORG ;
//            return  generateCert(x500Name, CertTypeEnum.CA_ENTERPRISE,tenantCertInfo.getTenantId());
//        }
//
//    }


//    /**
//     * @Description #系统防篡改个人证书
//     * @Param [name, phone]
//     * @return java.lang.String
//     **/
//    public String generateSystemPrivate(TenantCertInfo tenantCertInfo){
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" + tenantCertInfo.getName() + "@" + tenantCertInfo.getPhone() + "@" + TEST_CA_ISSUE_ORG ; ;
//        return  generateCert(x500Name, CertTypeEnum.SYSTEM_PERSONAL,tenantCertInfo.getTenantId());
//    }


//    /**
//     * @Description #系统防篡改企业证书
//     * @Param [entName, uscc]
//     * @return java.lang.String
//     **/
//    public String generateSystemEnt(TenantCertInfo tenantCertInfo ){
//
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=" + tenantCertInfo.getEntName() + "@" + tenantCertInfo.getUscc() + "@" + TEST_CA_ISSUE_ORG ;
//        return  generateCert(x500Name, CertTypeEnum.SYSTEM_ENTERPRISE,tenantCertInfo.getTenantId());
//    }


//    /**
//     * @Description #CA根证书
//     * @Param []
//     * @return java.lang.String
//     **/
//    public String generateCARoot(TenantCertInfo tenantCertInfo){
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=开放签_Test@" + TEST_CA_ISSUE_ORG;
//        return  generateCert(x500Name, CertTypeEnum.CA_ROOT,tenantCertInfo.getTenantId());
//    }
//
//    /**
//     * @Description #系统防篡改根证书
//     * @Param []
//     * @return java.lang.String
//     **/
//    public String generateSystemRoot(TenantCertInfo tenantCertInfo){
//        String x500Name = "C=CN,ST=北京,L=北京,O=开放签 CA,OU=产品部,CN=开放签@" + TEST_CA_ISSUE_ORG;
//        return  generateCert(x500Name, CertTypeEnum.SYSTEM_ROOT,tenantCertInfo.getTenantId());
//    }
//


//    /**
//     * @Description #生成证书通用方法
//     * @Param [x500Name, holderType]
//     * @return java.lang.String
//     **/
//    public String generateCert(String x500Name ,CertTypeEnum holderType ,String tenantId){
//        String certId = null ;
//        GenerateCertificateInfo certificateInfo = null;
//        BaseCertificateInfo baseCertificateInfo = null ;
//        byte[] rootCert = null;
//        //如果是生成根证书
//        if(holderType.getCode() == CertTypeEnum.CA_ROOT.getCode() || holderType.getCode() == CertTypeEnum.SYSTEM_ROOT.getCode()){
//            baseCertificateInfo = null ;
//        }
//        //如果是生成二级证书
//        else {
//            //获取根证书
//            if(holderType.getCode() == CertTypeEnum.SYSTEM_PERSONAL.getCode() || holderType.getCode() == CertTypeEnum.SYSTEM_ENTERPRISE.getCode()){
//                rootCert = getRootByte(fileProperties.getPath().getPath() + "system_root.jks");
//            }else if(holderType.getCode() == CertTypeEnum.CA_ENTERPRISE.getCode() || holderType.getCode() == CertTypeEnum.CA_PERSONAL.getCode()){
//                rootCert = getRootByte(fileProperties.getPath().getPath() + "ca_root.jks");
//            }
//            if(rootCert == null){
//                throw new PaasException("没有根证书");
//            }
//            baseCertificateInfo = new BaseCertificateInfo();
//            baseCertificateInfo.setAlias("root");
//            baseCertificateInfo.setCert(loadJks(rootCert,PASSWORD));
//            baseCertificateInfo.setPassword(PASSWORD);
//        }
//
//        try {
//            certificateInfo = GenerateRootCertificate.instance(x500Name, CertificateType.RSA).generateCertificate(baseCertificateInfo, PASSWORD, 100);
//            if(certificateInfo == null || certificateInfo.getJks() == null || certificateInfo.getPfx() == null){
//                return certId ;
//            }
//            CertificateInfo certInfo = new CertificateInfo();
//            if(holderType.getCode() == CertTypeEnum.SYSTEM_PERSONAL.getCode() || holderType.getCode() == CertTypeEnum.CA_PERSONAL.getCode()){
//                certInfo.setHolderType(CertHolderTypeEnum.PERSONAL.getCode());
//            }else {
//                certInfo.setHolderType(CertHolderTypeEnum.ENTERPRISE.getCode());
//            }
//            certInfo.setCertSubject(x500Name);
//            certInfo.setCertSuqeNo(certificateInfo.getSerial());
//            certInfo.setIssueTime(new Date());
//
//            certInfo.setCertPassword(PASSWORD);
//            certInfo.setIssueOrg(TEST_CA_ISSUE_ORG);
////            certInfo.setStorageMedium("云文件服务器");
//            certInfo.setAlgorithmType(certificateInfo.getAlgorithm());
//            certInfo.setTermOfValidityStartTime(certificateInfo.getTermOfValidityStartTime());
//            certInfo.setTermOfValidityEndTime(certificateInfo.getTermOfValidityEndTime());
//            certInfo.setCertType(1);
//            certInfo.setDeleteFlag(false);
//
//            boolean save = certificateInfoDao.save(certInfo);
//            if(!save){
//                throw new PaasException("证书颁发失败");
//            }
//            if(holderType.getCode() == CertTypeEnum.CA_ROOT.getCode() || holderType.getCode() == CertTypeEnum.SYSTEM_ROOT.getCode()){
//                //如果是生成根证书
//                //存储jks文件
//                fileService.saveAnnexStorage(certificateInfo.getJks(),SignFileEnum.JKS,certInfo.getId());
//            }
//            //存储pfx文件
//            fileService.saveAnnexStorage(certificateInfo.getPfx(),SignFileEnum.PFX,certInfo.getId());
//            return certInfo.getId();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    /**
//     * @Description #事件证书生成方法
//     * @Param [x500Name, holderType, tenantId]
//     * @return java.lang.String
//     **/
//    public String generateShortTermEventCert(String x500Name ,CertTypeEnum holderType ,String tenantId){
//
//        CertificationInfo info = new CertificationInfo();
//        info.setCertDn(x500Name);
//        info.setKeyName(CertHttpService.keyName);
//        info.setPassword(PASSWORD);
//        certHttpService.generateP10(info);
//
//        ShortTermVo shortTermVo = certHttpService.applyCert(info);
//        if(shortTermVo == null || shortTermVo.getP7b() == null){
//            throw new PaasException("申请事件证书失败");
//        }
//        byte[] p7bByte = Base64.decode(shortTermVo.getP7b());
//        if(p7bByte == null){
//            throw new PaasException("申请事件证书失败");
//        }
//        info.setP7bFile(p7bByte);
//        certHttpService.p7bToJks(info);
//        byte[] pfx = CertificateUtils.coverToPfx(info.getCertFile(), info.getPassword());
//        info.setPfxFile(pfx);
//
//        if(info.getPfxFile() == null){
//            throw new PaasException("申请事件证书失败");
//        }
//        CertificateInfo certInfo = new CertificateInfo();
//        if(holderType.getCode() == CertTypeEnum.SYSTEM_PERSONAL.getCode() || holderType.getCode() == CertTypeEnum.CA_PERSONAL.getCode()){
//            certInfo.setHolderType(CertHolderTypeEnum.PERSONAL.getCode());
//        }else {
//            certInfo.setHolderType(CertHolderTypeEnum.ENTERPRISE.getCode());
//        }
//        certInfo.setCertSubject(shortTermVo.getCertSubject());
//        certInfo.setCertSuqeNo(shortTermVo.getCertSN());
//        certInfo.setIssueTime(new Date());
//
//        certInfo.setCertPassword(PASSWORD);
//        certInfo.setIssueOrg(REAL_CA_ISSUE_ORG);
//        certInfo.setAlgorithmType("RSA");
//        certInfo.setUniqueCode(shortTermVo.getUniqueCode());
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date termOfValidityStartTime = null;
//        Date termOfValidityEndTime = null;
//        try {
//            termOfValidityStartTime = simpleDateFormat.parse(shortTermVo.getCertValidityNotBefore());
//            termOfValidityEndTime = simpleDateFormat.parse(shortTermVo.getCertValidityNotAfter());
//        }catch (Exception e){
//
//        }
//        certInfo.setTermOfValidityStartTime(termOfValidityStartTime);
//        certInfo.setTermOfValidityEndTime(termOfValidityEndTime);
//        certInfo.setCertType(1);
//        certInfo.setDeleteFlag(false);
//
//        boolean save = certificateInfoDao.save(certInfo);
//        if(!save){
//            throw new PaasException("证书颁发失败");
//        }
//        //存储pfx文件
//        fileService.saveAnnexStorage(info.getPfxFile(),SignFileEnum.PFX,certInfo.getId());
//        return certInfo.getId();
//    }

//    /**
//     * @Description #获取根证书数据
//     * @Param [certTypeEnum]
//     * @return byte[]
//     **/
//    public byte[] getRootCert(CertTypeEnum certTypeEnum,String tenantId){
//
//        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(TenantCertificate::getCertStatus,TenantCertStatusEnum.ENABLE.getCode());
//        wrapper.lambda().eq(TenantCertificate::getTenantId,tenantId);
//        if(certTypeEnum.getCode().equals(CertTypeEnum.CA_ENTERPRISE.getCode()) || certTypeEnum.getCode().equals(CertTypeEnum.CA_PERSONAL.getCode())){
//            wrapper.lambda().eq(TenantCertificate::getCertType,CertTypeEnum.CA_ROOT.getCode());
//        }
//        if(certTypeEnum.getCode().equals(CertTypeEnum.SYSTEM_ENTERPRISE.getCode()) || certTypeEnum.getCode().equals(CertTypeEnum.SYSTEM_PERSONAL.getCode())){
//            wrapper.lambda().eq(TenantCertificate::getCertType,CertTypeEnum.SYSTEM_ROOT.getCode());
//        }
//
//        List<TenantCertificate> list = tenantCertificateService.list(wrapper);
//        if(list == null || list.size() == 0){
//            throw new PaasException("没有根证书");
//        }
//
//        TenantCertificate tenantCertificate = list.get(0);
//        if(tenantCertificate.getCertId() == null || tenantCertificate.getCertId().length() == 0){
//            throw new PaasException("没有根证书");
//        }
//        CertificateInfo certificateInfo = certificateInfoDao.getById(tenantCertificate.getCertId());
//        if(certificateInfo == null){
//            throw new PaasException("没有根证书");
//        }
//
//        byte[] jks = fileService.getByteByFatherIdAndDataCategory(SignFileEnum.JKS, certificateInfo.getId());
//        return jks ;
//    }

    public byte[] getRootByte(String name) {
        try {
//            String path = this.getClass().getResource("/").getPath() + name;

            byte[] rootByte = IOUtils.toByteArray(new FileInputStream(new File(name)));
            return rootByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
//
//        try {
//            InputStream inputStream = ResourceUtils.class.getClassLoader()
//                    .getResourceAsStream(name);
//            return read(inputStream);
//        }catch (Exception e){
//            System.err.println(name);
//            e.printStackTrace();
//        }
//        return null;

    }

    public static byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    public KeyStore loadJks(byte[] certByte, String password) {
        KeyStore store = null;
        try {
            store = KeyStore.getInstance("JKS");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(certByte);
            store.load(inputStream, password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return store;
    }


//    public static void main(String[] args) {
//
////        String x500Name = "C==CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=开放签_Test";
////        String x500Name = "C==CN,ST=北京,L=北京,O=开放签 CA,OU=产品部,CN=开放签";
//        String x500Name = "C==CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=cccc@aaaaaaa";
//
//        GenerateCertificateInfo certificateInfo = null;
//        try {
//            byte[] rootByte = IOUtils.toByteArray(new FileInputStream(new File("E:\\project\\resrun\\resrun-paas-parent\\resrun-paas-system\\src\\main\\resources\\cert\\system_root.jks")));
//            byte rootByte1 [] = getRootByte("cert/system_root.jks");
//            KeyStore store = KeyStore.getInstance("JKS");
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(rootByte);
//            store.load(inputStream, "123456".toCharArray());
//
//            BaseCertificateInfo baseCertificateInfo = new BaseCertificateInfo();
//            baseCertificateInfo.setAlias("root");
//            baseCertificateInfo.setCert(store);
//            baseCertificateInfo.setPassword("123456");
//            certificateInfo = GenerateRootCertificate.instance(x500Name, CertificateType.RSA).generateCertificate(baseCertificateInfo, "123456", 100);
////            certificateInfo = GenerateRootCertificate.instance(x500Name, CertificateType.RSA).generateCertificate(null,"123456",100);
////            FileUtils.writeByteArrayToFile(new File("/Users/gongfenglai/Desktop/test/cert/private.pfx"), certificateInfo.getJks());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
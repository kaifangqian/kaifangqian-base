package com.kaifangqian.modules.cert.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.CertificateInfo;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.entity.TenantCertificate;
import com.kaifangqian.modules.system.enums.TenantStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.cert.enums.CertHolderTypeEnum;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.cert.vo.CertPageRequest;
import com.kaifangqian.modules.cert.vo.CertVo;
import com.kaifangqian.modules.system.service.CertificateInfoDao;
import com.kaifangqian.modules.system.service.ISysTenantInfoService;
import com.kaifangqian.modules.system.service.ISysTenantUserService;
import com.kaifangqian.modules.system.service.ITenantCertificateService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: CertController
 * @Package: com.kaifangqian.modules.cert.controller
 * @ClassName: CertController
 * @author: FengLai_Gong
 */
@Slf4j
@RestController
@RequestMapping("/cert")
@ResrunLogModule(name = "开放签-证书管理")
// @Api(tags = "开放签-证书管理")
public class CertController {


    @Autowired
    private ISysTenantUserService tenantUserService ;
    @Autowired
    private ISysTenantInfoService tenantInfoService ;
    @Autowired
    private CertificateInfoDao certificateInfoDao ;
    @Autowired
    private ITenantCertificateService tenantCertificateService ;



    // @ApiOperation("开放签-证书管理-企业管理后台-证书列表")
    @RequestMapping(value = "/enterprise/manage/list",method = RequestMethod.GET)
    public Result<IPage<CertVo>> enterpriseMangeList(CertPageRequest request){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();

        IPage<CertVo> returnPage = new Page<>();

        QueryWrapper<TenantCertificate> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TenantCertificate::getTenantId,currentUser.getTenantId());

        List<TenantCertificate> list = tenantCertificateService.list(wrapper);
        if(list == null || list.size() == 0){
            return Result.OK(returnPage) ;
        }
        List<String> certIdList = list.stream().map(TenantCertificate::getCertId).collect(Collectors.toList());
        Map<String, TenantCertificate> tenantCertificateMap = list.stream().collect(Collectors.toMap(TenantCertificate::getCertId, t -> t, (k1, k2) -> k1));
        if(certIdList == null || certIdList.size() == 0){
            return Result.OK(returnPage) ;
        }
        QueryWrapper<CertificateInfo> certificateInfoQueryWrapper = new QueryWrapper<>();
        certificateInfoQueryWrapper.lambda().in(CertificateInfo::getId,certIdList);
        certificateInfoQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        certificateInfoQueryWrapper.lambda().orderByDesc(CertificateInfo::getIssueTime);
        Page<CertificateInfo> page = certificateInfoDao.page(new Page<>(request.getPageNo(), request.getPageSize()), certificateInfoQueryWrapper);

        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
            List<CertificateInfo> certificateInfoList = page.getRecords();

            SysTenantInfo tenantInfo = tenantInfoService.getById(currentUser.getTenantId());

            List<CertVo> certVoList = new ArrayList<>();
            for(CertificateInfo certificateInfo : certificateInfoList){
                CertVo vo = new CertVo();
                if(tenantCertificateMap != null && tenantCertificateMap.containsKey(certificateInfo.getId())){
                    TenantCertificate tenantCertificate = tenantCertificateMap.get(certificateInfo.getId());
                    if(tenantCertificate != null){
                        //证书类型
                        vo.setCertType(tenantCertificate.getCertType());
                        //状态
                        vo.setCertStatus(tenantCertificate.getCertStatus());
                    }
                }
                //颁发时间
                vo.setIssueTime(certificateInfo.getIssueTime());
                //有效期
                vo.setTermOfValidityStartTime(certificateInfo.getTermOfValidityStartTime());
                vo.setTermOfValidityEndTime(certificateInfo.getTermOfValidityEndTime());
                //证书序列号
                vo.setCertSuqeNo(certificateInfo.getCertSuqeNo());
                //颁发机构
                vo.setIssueOrg(certificateInfo.getIssueOrg());
                //加密算法
                vo.setAlgorithmType(certificateInfo.getAlgorithmType());
                //存储介质
                vo.setStorageMedium(certificateInfo.getStorageMedium());

                //使用者
                if(tenantInfo != null){
                    vo.setUserName(tenantInfo.getTenantName());
                }
                certVoList.add(vo);
            }
            returnPage.setRecords(certVoList);
            returnPage.setCurrent(page.getCurrent());
            returnPage.setPages(page.getPages());
            returnPage.setTotal(page.getTotal());
            returnPage.setSize(page.getSize());
        }

        return Result.OK(returnPage) ;
    }


    // @ApiOperation("开放签-证书管理-运营管理后台-个人证书列表")
    @RequestMapping(value = "/operation/manage/personal/list",method = RequestMethod.GET)
    public Result<IPage<CertVo>> operationManagePersonalList(CertPageRequest request){

        IPage<CertVo> returnPage = new Page<>();
        //查询所有个人租户
        QueryWrapper<SysTenantInfo> tenantInfoQueryWrapper = new QueryWrapper<>();
        tenantInfoQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        tenantInfoQueryWrapper.lambda().eq(SysTenantInfo::getTenantType, TenantType.PERSONAL.getType());
        tenantInfoQueryWrapper.lambda().eq(SysTenantInfo::getTenantStatus, TenantStatus.ENABLE.getType());
        if(request.getUserName() != null && request.getUserName().length() > 0){
            tenantInfoQueryWrapper.lambda().like(SysTenantInfo::getTenantName,"%" + request.getUserName() + "%");
        }
        List<SysTenantInfo> tenantInfoList = tenantInfoService.list(tenantInfoQueryWrapper);
        if(tenantInfoList == null || tenantInfoList.size() == 0){
            return Result.OK(returnPage) ;
        }
        List<String> tenantIdList = tenantInfoList.stream().map(SysTenantInfo::getId).collect(Collectors.toList());
        Map<String, SysTenantInfo> tenantInfoMap = tenantInfoList.stream().collect(Collectors.toMap(SysTenantInfo::getId, t -> t, (k1, k2) -> k1));
        if(tenantIdList == null || tenantIdList.size() == 0){
            return Result.OK(returnPage) ;
        }

        //查询证书
        QueryWrapper<TenantCertificate> tenantCertificateQueryWrapper = new QueryWrapper<>();
        tenantCertificateQueryWrapper.lambda().in(TenantCertificate::getTenantId,tenantIdList);
        //证书状态条件参数
        if(request.getCertStatus() != null){
            tenantCertificateQueryWrapper.lambda().eq(TenantCertificate::getCertStatus,request.getCertStatus());
        }
        //证书类型条件参数
        if(request.getCertType() != null){
            tenantCertificateQueryWrapper.lambda().eq(TenantCertificate::getCertType,request.getCertType());
        }else {
            List<Integer> certTypeList = new ArrayList<>();
            certTypeList.add(CertTypeEnum.SYSTEM.getCode());
            certTypeList.add(CertTypeEnum.CA_TEST.getCode());
            certTypeList.add(CertTypeEnum.CA_SHORT_TERM.getCode());
            certTypeList.add(CertTypeEnum.CA_LONG_TERM.getCode());
            tenantCertificateQueryWrapper.lambda().in(TenantCertificate::getCertType, certTypeList);
        }
        tenantCertificateQueryWrapper.lambda().eq(TenantCertificate::getHolderType, CertHolderTypeEnum.PERSONAL.getCode());
        //查询租户证书关联数据
        List<TenantCertificate> tenantCertificateList = tenantCertificateService.list(tenantCertificateQueryWrapper);
        if(tenantCertificateList == null || tenantCertificateList.size() == 0){
            return Result.OK(returnPage) ;
        }
        Map<String, TenantCertificate> tenantCertificateMap = tenantCertificateList.stream().collect(Collectors.toMap(TenantCertificate::getCertId, t -> t, (k1, k2) -> k1));
        List<String> certIdList = tenantCertificateList.stream().map(TenantCertificate::getCertId).collect(Collectors.toList());
        if(certIdList == null || certIdList.size() == 0){
            return Result.OK(returnPage) ;
        }
        //查询证书数据
        QueryWrapper<CertificateInfo> certificateInfoQueryWrapper = new QueryWrapper<>();
        certificateInfoQueryWrapper.lambda().in(CertificateInfo::getId,certIdList);
        if(request.getIssueTimeEndTime() != null){
            certificateInfoQueryWrapper.lambda().le(CertificateInfo::getIssueTime,request.getIssueTimeEndTime());
        }
        if(request.getIssueTimeStartTime() != null){
            certificateInfoQueryWrapper.lambda().ge(CertificateInfo::getIssueTime,request.getIssueTimeStartTime());
        }
        if(request.getTermStartTime() != null){
            certificateInfoQueryWrapper.lambda().ge(CertificateInfo::getTermOfValidityEndTime,request.getTermStartTime());
        }
        if(request.getTermEndTime() != null){
            certificateInfoQueryWrapper.lambda().le(CertificateInfo::getTermOfValidityEndTime,request.getTermEndTime());
        }
        certificateInfoQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        certificateInfoQueryWrapper.lambda().orderByDesc(CertificateInfo::getIssueTime);
        Page<CertificateInfo> page = certificateInfoDao.page(new Page<>(request.getPageNo(), request.getPageSize()), certificateInfoQueryWrapper);
        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
            List<CertVo> certVoList = new ArrayList<>();
            for(CertificateInfo certificateInfo : page.getRecords()){
                CertVo vo = new CertVo();
                //颁发时间
                vo.setIssueTime(certificateInfo.getIssueTime());
                //有效期
                vo.setTermOfValidityStartTime(certificateInfo.getTermOfValidityStartTime());
                vo.setTermOfValidityEndTime(certificateInfo.getTermOfValidityEndTime());
                //证书序列号
                vo.setCertSuqeNo(certificateInfo.getCertSuqeNo());
                //颁发机构
                vo.setIssueOrg(certificateInfo.getIssueOrg());
                //加密算法
                vo.setAlgorithmType(certificateInfo.getAlgorithmType());
                //存储介质
                vo.setStorageMedium(certificateInfo.getStorageMedium());
                if(tenantCertificateMap != null && tenantCertificateMap.containsKey(certificateInfo.getId())){
                    TenantCertificate tenantCertificate = tenantCertificateMap.get(certificateInfo.getId());
                    if(tenantCertificate != null){
                        //证书类型
                        vo.setCertType(tenantCertificate.getCertType());
                        //状态
                        vo.setCertStatus(tenantCertificate.getCertStatus());
                        //使用者
                        if(tenantInfoMap != null && tenantInfoMap.containsKey(tenantCertificate.getTenantId())){
                            SysTenantInfo tenantInfo = tenantInfoMap.get(tenantCertificate.getTenantId());
                            if(tenantInfo != null){
                                vo.setUserName(tenantInfo.getTenantName());
                            }
                        }
                    }
                }
                certVoList.add(vo);
            }
            returnPage.setRecords(certVoList);
            returnPage.setCurrent(page.getCurrent());
            returnPage.setPages(page.getPages());
            returnPage.setTotal(page.getTotal());
            returnPage.setSize(page.getSize());
        }

        return Result.OK(returnPage) ;
    }

    // @ApiOperation("开放签-证书管理-运营管理后台-企业证书列表")
    @RequestMapping(value = "/operation/manage/enterprise/list",method = RequestMethod.GET)
    public Result<IPage<CertVo>> operationManageEnterpriseList(CertPageRequest request){

        IPage<CertVo> returnPage = new Page<>();
        //查询所有个人租户
        QueryWrapper<SysTenantInfo> tenantInfoQueryWrapper = new QueryWrapper<>();
        tenantInfoQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        tenantInfoQueryWrapper.lambda().eq(SysTenantInfo::getTenantType, TenantType.GROUP.getType());
        tenantInfoQueryWrapper.lambda().eq(SysTenantInfo::getTenantStatus, TenantStatus.ENABLE.getType());
        if(request.getUserName() != null && request.getUserName().length() > 0){
            tenantInfoQueryWrapper.lambda().like(SysTenantInfo::getTenantName,"%" + request.getUserName() + "%");
        }
        List<SysTenantInfo> tenantInfoList = tenantInfoService.list(tenantInfoQueryWrapper);
        if(tenantInfoList == null || tenantInfoList.size() == 0){
            return Result.OK(returnPage) ;
        }
        List<String> tenantIdList = tenantInfoList.stream().map(SysTenantInfo::getId).collect(Collectors.toList());
        Map<String, SysTenantInfo> tenantInfoMap = tenantInfoList.stream().collect(Collectors.toMap(SysTenantInfo::getId, t -> t, (k1, k2) -> k1));
        if(tenantIdList == null || tenantIdList.size() == 0){
            return Result.OK(returnPage) ;
        }

        //查询证书
        QueryWrapper<TenantCertificate> tenantCertificateQueryWrapper = new QueryWrapper<>();
        tenantCertificateQueryWrapper.lambda().in(TenantCertificate::getTenantId,tenantIdList);
        //证书状态条件参数
        if(request.getCertStatus() != null){
            tenantCertificateQueryWrapper.lambda().eq(TenantCertificate::getCertStatus,request.getCertStatus());
        }
        //证书类型条件参数
        if(request.getCertType() != null){
            tenantCertificateQueryWrapper.lambda().eq(TenantCertificate::getCertType,request.getCertType());
        }else {
            List<Integer> certTypeList = new ArrayList<>();
            certTypeList.add(CertTypeEnum.SYSTEM.getCode());
            certTypeList.add(CertTypeEnum.CA_TEST.getCode());
            certTypeList.add(CertTypeEnum.CA_SHORT_TERM.getCode());
            certTypeList.add(CertTypeEnum.CA_LONG_TERM.getCode());
            tenantCertificateQueryWrapper.lambda().in(TenantCertificate::getCertType, certTypeList);
        }
        tenantCertificateQueryWrapper.lambda().eq(TenantCertificate::getHolderType,CertHolderTypeEnum.ENTERPRISE.getCode());
        //查询租户证书关联数据
        List<TenantCertificate> tenantCertificateList = tenantCertificateService.list(tenantCertificateQueryWrapper);
        if(tenantCertificateList == null || tenantCertificateList.size() == 0){
            return Result.OK(returnPage) ;
        }
        Map<String, TenantCertificate> tenantCertificateMap = tenantCertificateList.stream().collect(Collectors.toMap(TenantCertificate::getCertId, t -> t, (k1, k2) -> k1));
        List<String> certIdList = tenantCertificateList.stream().map(TenantCertificate::getCertId).collect(Collectors.toList());
        if(certIdList == null || certIdList.size() == 0){
            return Result.OK(returnPage) ;
        }
        //查询证书数据
        QueryWrapper<CertificateInfo> certificateInfoQueryWrapper = new QueryWrapper<>();
        certificateInfoQueryWrapper.lambda().in(CertificateInfo::getId,certIdList);
        if(request.getIssueTimeEndTime() != null){
            certificateInfoQueryWrapper.lambda().le(CertificateInfo::getIssueTime,request.getIssueTimeEndTime());
        }
        if(request.getIssueTimeStartTime() != null){
            certificateInfoQueryWrapper.lambda().ge(CertificateInfo::getIssueTime,request.getIssueTimeStartTime());
        }
        if(request.getTermStartTime() != null){
            certificateInfoQueryWrapper.lambda().ge(CertificateInfo::getTermOfValidityEndTime,request.getTermStartTime());
        }
        if(request.getTermEndTime() != null){
            certificateInfoQueryWrapper.lambda().le(CertificateInfo::getTermOfValidityEndTime,request.getTermEndTime());
        }
        certificateInfoQueryWrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        certificateInfoQueryWrapper.lambda().orderByDesc(CertificateInfo::getIssueTime);
        Page<CertificateInfo> page = certificateInfoDao.page(new Page<>(request.getPageNo(), request.getPageSize()), certificateInfoQueryWrapper);
        if(page != null && page.getRecords() != null && page.getRecords().size() > 0){
            List<CertVo> certVoList = new ArrayList<>();
            for(CertificateInfo certificateInfo : page.getRecords()){
                CertVo vo = new CertVo();
                //颁发时间
                vo.setIssueTime(certificateInfo.getIssueTime());
                //有效期
                vo.setTermOfValidityStartTime(certificateInfo.getTermOfValidityStartTime());
                vo.setTermOfValidityEndTime(certificateInfo.getTermOfValidityEndTime());
                //证书序列号
                vo.setCertSuqeNo(certificateInfo.getCertSuqeNo());
                //颁发机构
                vo.setIssueOrg(certificateInfo.getIssueOrg());
                //加密算法
                vo.setAlgorithmType(certificateInfo.getAlgorithmType());
                //存储介质
                vo.setStorageMedium(certificateInfo.getStorageMedium());
                if(tenantCertificateMap != null && tenantCertificateMap.containsKey(certificateInfo.getId())){
                    TenantCertificate tenantCertificate = tenantCertificateMap.get(certificateInfo.getId());
                    if(tenantCertificate != null){
                        //证书类型
                        vo.setCertType(tenantCertificate.getCertType());
                        //状态
                        vo.setCertStatus(tenantCertificate.getCertStatus());
                        //使用者
                        if(tenantInfoMap != null && tenantInfoMap.containsKey(tenantCertificate.getTenantId())){
                            SysTenantInfo tenantInfo = tenantInfoMap.get(tenantCertificate.getTenantId());
                            if(tenantInfo != null){
                                vo.setUserName(tenantInfo.getTenantName());
                            }
                        }
                    }
                }
                certVoList.add(vo);
            }
            returnPage.setRecords(certVoList);
            returnPage.setCurrent(page.getCurrent());
            returnPage.setPages(page.getPages());
            returnPage.setTotal(page.getTotal());
            returnPage.setSize(page.getSize());
        }

        return Result.OK(returnPage) ;
    }





}
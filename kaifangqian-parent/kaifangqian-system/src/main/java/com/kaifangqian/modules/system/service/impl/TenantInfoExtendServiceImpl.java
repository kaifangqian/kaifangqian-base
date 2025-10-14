/**
 *
 * Copyright (C) [2025] [版权所有者（北京资源律动科技有限公司）]. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * 注意：本代码基于 AGPLv3 协议发布。若通过网络提供服务（如 Web 应用），
 * 必须公开修改后的完整源代码（包括衍生作品），详见协议全文。
 */
package com.kaifangqian.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.config.AnnexType;
import com.kaifangqian.modules.api.entity.ApiRelationLink;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.api.service.IApiRelationLinkService;
import com.kaifangqian.modules.api.vo.request.*;
import com.kaifangqian.modules.api.vo.response.EmployeeRes;
import com.kaifangqian.modules.opensign.enums.SealColorEnum;
import com.kaifangqian.modules.opensign.enums.SignFileEnum;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.TenantAuthStatus;
import com.kaifangqian.modules.system.enums.TenantAuthType;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.mapper.TenantInfoExtendMapper;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.common.aspect.LicenseBean;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.DesensitizationUtils;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.api.vo.request.*;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.entity.SignPersonSeal;
import com.kaifangqian.modules.opensign.enums.*;
import com.kaifangqian.modules.opensign.service.business.EntSealBusinessService;
import com.kaifangqian.modules.opensign.service.flow.IFlowService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.opensign.service.seal.SignPersonSealService;
import com.kaifangqian.modules.opensign.service.tool.PsersonSealGenerateService;
import com.kaifangqian.modules.opensign.service.tool.SignFileService;
import com.kaifangqian.modules.opensign.vo.base.ConfirmPara;
import com.kaifangqian.modules.storage.dto.StorageDto;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TenantInfoExtendServiceImpl extends ServiceImpl<TenantInfoExtendMapper, TenantInfoExtend> implements ITenantInfoExtendService {

    @Autowired
    private ISysTenantInfoService sysTenantInfoService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private ISysTenantInfoService iSysTenantInfoService;

    @Autowired
    private ISysUserDepartService iSysUserDepartService;
    @Resource
    private TenantInfoExtendMapper mapper;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private SignRuTaskService signRuTaskService;

    @Autowired
    private IApiRelationLinkService apiRelationLinkService;

    @Lazy
    @Resource
    private LicenseBean licenseBean;
    @Autowired
    private PsersonSealGenerateService sealGenerateService;
    @Autowired
    private SignFileService signFileService;
    @Autowired
    private SignPersonSealService personSealService;

    @Autowired
    private EntSealBusinessService entSealBusinessService ;

    @Autowired
    private ISysDepartService iSysDepartService ;

    @Override
    public IPage<TenantInfoDTO> pageExt(Page<TenantInfoDTO> page, TenantQueryDTO tenantQueryDTO) {
        return mapper.pageExt(page, tenantQueryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExt(TenantInfoDTO tenantInfoDTO) {
//        valid(tenantInfoDTO);
//        checkRepeat(tenantInfoDTO);
//        if (tenantInfoDTO.getTenantType() == null) {
//            throw new PaasException("类型不能为空");
//        }
//        //开通租户
//        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
//        if (tenantInfoDTO.getTenantType().equals(TenantExtendType.TYPE1.getType())) {
//            sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());
//        } else {
//            sysTenantInfoVO.setTenantType(TenantType.GROUP.getType());
//        }
//        sysTenantInfoVO.setTenantName(tenantInfoDTO.getTenantName());
//        sysTenantInfoVO.setRealName(tenantInfoDTO.getTenantName());
//        sysTenantInfoVO.setUsername(tenantInfoDTO.getUsername());
//        if (tenantInfoDTO.getTenantType().equals(TenantExtendType.TYPE1.getType()) || tenantInfoDTO.getTenantType().equals(TenantExtendType.TYPE2.getType())) {
//            sysTenantInfoVO.setPhone(tenantInfoDTO.getPhone());
//        } else {
//            sysTenantInfoVO.setPhone(tenantInfoDTO.getContactsPhone());
//        }
//        sysTenantInfoVO.setPassword(tenantInfoDTO.getPassword());
//
//        String tenantId = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);
//
//        //维护租户扩展信息
//        TenantInfoExtend tenantInfoExtend = new TenantInfoExtend();
//        BeanUtils.copyProperties(tenantInfoDTO, tenantInfoExtend);
//        //工作站生成组织码
//        if (tenantInfoDTO.getTenantType().equals(TenantExtendType.TYPE3.getType())) {
//            String orgCode = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8).toUpperCase();
//            while (getByOrgCode(orgCode) != null) {
//                orgCode = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8).toUpperCase();
//            }
//            tenantInfoExtend.setOrgCode(orgCode);
//        }
//
//        tenantInfoExtend.setTenantId(tenantId);
//        //已认证
//        tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS2.getStatus());
//        this.save(tenantInfoExtend);
//
//        //关联附件
//        List<StorageDto> files = new ArrayList<>();
//        files.add(tenantInfoDTO.getIdPic1());
//        files.add(tenantInfoDTO.getIdPic2());
//        files.add(tenantInfoDTO.getOrganizationPic());
//        iAnnexStorageService.updateMainDataFiles(tenantInfoExtend.getId(), files);
    }

    private void valid(TenantInfoDTO m) {
//        CommonUtils.requireNonNull(m.getTenantProvince(), "所属省不能为空");
//        CommonUtils.requireNonNull(m.getTenantCity(), "所属市不能为空");
//        CommonUtils.requireNonNull(m.getTenantDistrict(), "所属区不能为空");
    }

    @Override
    public TenantInfoDTO getByIdExt(String id) {
        TenantInfoExtend tenantInfoExtend = this.getById(id);
        if (tenantInfoExtend != null) {


            TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
            BeanUtils.copyProperties(tenantInfoExtend, tenantInfoDTO);
            String organizationNo = tenantInfoExtend.getOrganizationNo();
            try {
                organizationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, organizationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tenantInfoDTO.setOrganizationNo(organizationNo);

            String corporationNo = tenantInfoExtend.getCorporationNo();
            try {
                corporationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, corporationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tenantInfoDTO.setCorporationNo(corporationNo);

            String phone = tenantInfoExtend.getPhone();
            try {
                phone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, phone);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tenantInfoDTO.setPhone(phone);


            AnnexStorage query = new AnnexStorage();
            if (tenantInfoExtend.getAuthId() != null) {
                TenantAuthLog authLog = tenantAuthLogService.getById(tenantInfoExtend.getAuthId());
                tenantInfoDTO.setCheckMes(authLog.getCheckMsg());
                tenantInfoDTO.setRealItem(authLog.getRealItem());
            }
            query.setFatherId(tenantInfoExtend.getAuthId());
            List<AnnexStorage> files = iAnnexStorageService.getByEntity(query);
            if (CollUtil.isNotEmpty(files)) {
                files.forEach(f -> {
                    StorageDto storageDto = new StorageDto();
                    storageDto.setId(f.getId());
                    storageDto.setRealName(f.getRealName());
                    if (f.getDataCategory().equals(AnnexType.IDPIC1)) {
                        tenantInfoDTO.setIdCardFace(storageDto);
                    } else if (f.getDataCategory().equals(AnnexType.IDPIC2)) {
                        tenantInfoDTO.setIdCardEmblem(storageDto);
                    } else if (f.getDataCategory().equals(AnnexType.ORGANIZATIONPIC)) {
                        tenantInfoDTO.setBusinessLicense(storageDto);
                    } else if (f.getDataCategory().equals(AnnexType.AUTHORIZEBOOK)) {
                        tenantInfoDTO.setAuthorizeBook(storageDto);
                    }
                });
            }

            return tenantInfoDTO;
        }
        throw new PaasException("对象不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editExt(TenantInfoDTO tenantInfoDTO) {
//        checkRepeat(tenantInfoDTO);
//        if (MyStringUtils.isBlank(tenantInfoDTO.getId())) {
//            throw new PaasException("传值错误");
//        }
//        TenantInfoExtend tenantInfoExtend = new TenantInfoExtend();
//        BeanUtils.copyProperties(tenantInfoDTO, tenantInfoExtend);
//        tenantInfoExtend.setTenantId(null);
//        tenantInfoExtend.setTenantType(null);
//        tenantInfoExtend.setOrgCode(null);
//        tenantInfoExtend.setAuthStatus(null);
//
//        this.updateById(tenantInfoExtend);
//
//        //处理附件
//        //删除历史附件、关联新附件
//        List<StorageDto> files = new ArrayList<>();
//        files.add(tenantInfoDTO.getIdPic1());
//        files.add(tenantInfoDTO.getIdPic2());
//        files.add(tenantInfoDTO.getOrganizationPic());
//        iAnnexStorageService.updateMainDataFiles(tenantInfoExtend.getId(), files);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tenantRegister(UserJionSystemVO vo) {
        vo.setTenantType(TenantType.PERSONAL.getType());
        if (vo.getTenantType() == null) {
            throw new PaasException("租户类型不能为空");
        }
        String realKey = MD5Util.MD5Encode(vo.getPhone() + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            throw new PaasException("验证码错误");
        }
        JoinTenant(vo);
    }

    private SysTenantUser JoinTenant(UserJionSystemVO vo) {
        //开通租户
        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
        sysTenantInfoVO.setRealName(vo.getPhone());
        sysTenantInfoVO.setPhone(vo.getPhone());
        sysTenantInfoVO.setEmail(vo.getEmail());
        sysTenantInfoVO.setPassword(vo.getPassword());
        sysTenantInfoVO.setTenantName(vo.getRealName());
        sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());

        SysAddTenantInfoVO sysAddTenantInfoVO = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);
        String tenantId = sysAddTenantInfoVO.getTenantId();

        //维护租户扩展信息
        TenantInfoExtend tenantInfoExtend = new TenantInfoExtend();
        tenantInfoExtend.setTenantId(tenantId);
        tenantInfoExtend.setTenantType(vo.getTenantType());
        tenantInfoExtend.setPhone(vo.getPhone());
        //未认证
        tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
        this.save(tenantInfoExtend);

        //解析数据库已有数据做绑定表：sign_ru_relation sign_ru_task
        SysUser sysUser = null;
        if (MyStringUtils.isNotBlank(vo.getPhone())) {
            sysUser = sysUserService.getUserByPhone(vo.getPhone());
        }
        if (MyStringUtils.isNotBlank(vo.getEmail())) {
            sysUser = sysUserService.getUserByEmail(vo.getEmail());
        }
        sysUserService.getUserByPhone(vo.getPhone());
        if (sysUser != null) {
            SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(sysUser.getId());
            if (tenantUser != null) {
                //解析数据库已有数据做绑定表：sign_ru_relation
                flowService.bindOutUserRelation(vo.getPhone(), vo.getEmail(), tenantUser.getId(), "register");
                //解析数据库已有数据做绑定表：sign_ru_task
                //绑定手机号：账号ID
                flowService.bindOutUserTask(null, vo.getPhone(), vo.getEmail(), null, null, sysUser.getId(), null, null, "register");
                //绑定手机号+租户名（个人租户）：租户ID，租户账号ID
                flowService.bindOutUserTask("个人租户", vo.getPhone(), vo.getEmail(), null, null, tenantUser.getUserId(), tenantUser.getTenantId(), tenantUser.getId(), "register");
            }
            return tenantUser;
        }
        return null;
    }

    @Override
    public String personalTenantRegisterForApi(PersonalRegister register) {
        String tenantId = null;
        //校验是否已经绑定
        String tenantUserId = apiRelationLinkService.getSystemIdByExAccount(register.getAppAuthToken(), ApiConstants.EXTEND_TYPE_USER, register.getAccount());
        if (MyStringUtils.isNotBlank(tenantUserId)) {
            throw new PaasException(ApiCode.EXTERNAL_ACCOUNT_REPEAT.getCode(), ApiCode.EXTERNAL_ACCOUNT_REPEAT.getTemplate());
        }
        //校验是否已经开通个人租户
        SysUser sysUser = null;
        if(register.getContactType().equals("MOBILE")){
            sysUser = sysUserService.getUserByPhone(register.getAccount());
        }else if(register.getContactType().equals("EMAIL")){
            sysUser = sysUserService.getUserByEmail(register.getAccount());
        }

        if (sysUser != null) {
            SysTenantUser tenantUser = iSysTenantUserService.getPersonalTenantUser(sysUser.getId());
            if (tenantUser != null) {
                //修改用户名
                tenantUser.setNickName(register.getName());
                iSysTenantUserService.updateById(tenantUser);
                //绑定关系
                bindExtractedRelation(register.getAppAuthToken(), register.getAccount(), tenantUser.getId(), ApiConstants.EXTEND_TYPE_USER);
                return tenantUser.getTenantId();
            }
        }
        //开通个人租户
        UserJionSystemVO vo = new UserJionSystemVO();
        if(MyStringUtils.isNotBlank(register.getMobile())){
            vo.setPhone(register.getMobile());
        }else if(MyStringUtils.isNotBlank(register.getEmail())){
            vo.setEmail(register.getEmail());
        }
        vo.setRealName(register.getName());
        vo.setAccount(register.getAccount());
        vo.setTenantType(TenantType.PERSONAL.getType());
        //开通租户
        SysTenantUser tenantUser = JoinTenant(vo);
        if (tenantUser != null) {
            //修改用户名
            tenantUser.setNickName(register.getName());
            iSysTenantUserService.updateById(tenantUser);
            //绑定关系
            bindExtractedRelation(register.getAppAuthToken(), register.getAccount(), tenantUser.getId(), ApiConstants.EXTEND_TYPE_USER);
            tenantId = tenantUser.getTenantId();
        }
        return tenantId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void enterpriseTenantRegisterForApi(EnterpriseRegister register) {
//        //校验是否已经绑定
//        String tenantUserId = apiRelationLinkService.getSystemIdByExAccount(register.getAppAuthToken(), ApiConstants.EXTEND_TYPE_TENANT, register.getAccount());
//        if (MyStringUtils.isNotBlank(tenantUserId)) {
//            throw new PaasException(ApiCode.EXTERNAL_ACCOUNT_REPEAT.getCode(), ApiCode.EXTERNAL_ACCOUNT_REPEAT.getTemplate());
//        }
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        //校验是否已经开通企业租户
        SysTenantInfo tenantInfo = sysTenantInfoService.getByName(register.getEnterpriseName());
        String tenantId = null;
        if (tenantInfo == null) {
            //否-创建租户
            tenantId = addTenantInfo(register);

            SysUser sysUser = null;
            if(register.getContactType().equals("MOBILE")){
                sysUser = sysUserService.getUserByPhone(register.getAccount());
            }else if(register.getContactType().equals("EMAIL")){
                sysUser = sysUserService.getUserByEmail(register.getAccount());
            }
            if (sysUser != null) {
                SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(tenantId, sysUser.getId());
                if (tenantUser != null) {
                    //修改用户名
                    tenantUser.setNickName(register.getName());
                    tenantUser.setAddType("system");
                    iSysTenantUserService.updateById(tenantUser);
//                    //绑定租户-用户关系
//                    bindExtractedRelation(register.getAppAuthToken(), register.getAccount(), tenantUser.getId(), ApiConstants.EXTEND_TYPE_USER);
                }

                //创建个人租户空间
                if (!iSysTenantUserService.checkPersonalTenant(sysUser.getId())) {
                    SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
                    if(register.getContactType().equals("MOBILE")){
                        sysTenantInfoVO.setPhone(register.getMobile());
                    }else if(register.getContactType().equals("EMAIL")){
                        sysTenantInfoVO.setEmail(register.getEmail());
                    }
                    sysTenantInfoVO.setTenantName(register.getName());
                    sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());
                    sysTenantInfoVO.setRealName(register.getName());

                    String pTenantId = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO).getTenantId();

                    SysTenantUser personalTenantUser = iSysTenantUserService.getTenantUser(pTenantId, sysUser.getId());

                    TenantInfoExtend infoExtend = new TenantInfoExtend();
                    infoExtend.setName(register.getName());
                    infoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
                    if(register.getContactType().equals("MOBILE")){
                        try {
                            String phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, register.getMobile());
                            infoExtend.setPhone(phone);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else if(register.getContactType().equals("EMAIL")){
                        infoExtend.setEmail(register.getEmail());
                    }
                    infoExtend.setAuthType(TenantAuthType.CREATED.getType());
                    infoExtend.setTenantId(pTenantId);
                    infoExtend.setTenantType(TenantType.PERSONAL.getType());
                    save(infoExtend);

                    if (tenantUser != null) {
                        //解析数据库已有数据做绑定表：sign_ru_relation
                        flowService.bindOutUserRelation(register.getMobile(), register.getEmail(), tenantUser.getId(), "register");
                        //解析数据库已有数据做绑定表：sign_ru_task
                        //绑定手机号：账号ID
                        flowService.bindOutUserTask(null, register.getMobile(), register.getEmail(), null, null, sysUser.getId(), null, null, "register");
                        //绑定手机号+租户名（个人租户）：租户ID，租户账号ID
                        flowService.bindOutUserTask("个人租户", register.getMobile(), register.getEmail(), null, null, sysUser.getId(), pTenantId, personalTenantUser.getId(), "register");
                    }
                }
            }
        } else {
            tenantId = tenantInfo.getId();

            SysUser sysUser = null;
            if(register.getContactType().equals("MOBILE")){
                sysUser = sysUserService.getUserByPhone(register.getAccount());
            }else if(register.getContactType().equals("EMAIL")){
                sysUser = sysUserService.getUserByEmail(register.getAccount());
            }
            String accountId = null;
            if (sysUser == null) {
                //校验用户是否已经注册：否：注册账号
                accountId = addUser(register.getName(), register.getContactType(),register.getAccount());
            } else {
                accountId = sysUser.getId();
            }
            currentUser.setId(accountId);
            currentUser.setTenantId(tenantId);
            SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(tenantId, accountId);
            if (tenantUser == null) {
                //账号是否已经加入租户下：否：加入租户
                //关联租户-用户
                tenantUser = new SysTenantUser();
                tenantUser.setTenantId(tenantId);
                tenantUser.setUserId(accountId);
                tenantUser.setNickName(register.getName());
                tenantUser.setStatus(1);
                iSysTenantUserService.save(tenantUser);
                currentUser.setTenantUserId(tenantUser.getId());
            }
            //添加用户权限
            sysTenantInfoService.addEnterpriseTenantAppVersion(tenantId,accountId,null);

        }

//        //绑定租户关系
//        bindExtractedRelation(register.getAppAuthToken(), register.getAccount(), tenantId, ApiConstants.EXTEND_TYPE_TENANT);
        //将经办人设置为租户管理员

        SysUser sysUser = null;
        if(register.getContactType().equals("MOBILE")){
            sysUser = sysUserService.getUserByPhone(register.getAccount());
        }else if(register.getContactType().equals("EMAIL")){
            sysUser = sysUserService.getUserByEmail(register.getAccount());
        }

        if(sysUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败，用户不存在");
        }
        currentUser.setId(sysUser.getId());
        SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(tenantId, sysUser.getId());
        if(tenantUser == null){
            throw new RequestParamsException(ApiCode.BUSINESS_HANDLE_ERROR,"业务处理失败，用户不存在");
        }
        tenantUser.setAddType("system");
        iSysTenantUserService.updateById(tenantUser);
        currentUser.setTenantId(tenantUser.getTenantId());
        currentUser.setTenantUserId(tenantUser.getId());

        String departId = null ;
        SysUserDepart sysUserDepart = new SysUserDepart();
        sysUserDepart.setTenantId(tenantId);
        sysUserDepart.setUserId(sysUser.getId());
        List<SysUserDepart> userDepartList = iSysUserDepartService.getByEntity(sysUserDepart);
        if(userDepartList == null || userDepartList.size() == 0){
            //租户下根部门
            SysDepart query = new SysDepart();
            query.setTenantId(tenantUser.getTenantId());
            List<SysDepart> departs = iSysDepartService.getByEntity(query);
            if (CollUtil.isNotEmpty(departs)) {
                SysUserDepart userDepart = new SysUserDepart(tenantUser.getTenantId(), tenantUser.getUserId(), departs.get(0).getId(), true);
                iSysUserDepartService.addExt(userDepart);
            }
        }else {
            SysUserDepart userDepart = userDepartList.get(0);
            departId = userDepart.getDepartId();
            userDepart.setManageFlag(true);
            iSysUserDepartService.updateById(userDepart);
        }

        currentUser.setDepartId(departId);
        //更新实名认证数据
        String authLogId = UUIDGenerator.generate();
        TenantInfoExtend tenantInfoExtend = getTenantInfoByTenantId(tenantId);
        //如果不存在则创建
        if(tenantInfoExtend == null){
            tenantInfoExtend = new TenantInfoExtend();
            //填充数据或者更新数据
            tenantInfoExtend.setName(register.getEnterpriseName());
            tenantInfoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
            tenantInfoExtend.setAuthType(TenantAuthType.CREATED.getType());
            tenantInfoExtend.setTenantId(tenantId);
            tenantInfoExtend.setTenantType(TenantType.GROUP.getType());
            tenantInfoExtend.setOrganizationNo(register.getCreditCode());
            tenantInfoExtend.setCorporation(register.getLegalPerson());
            String corporationNo = register.getIdentity();
            String phone = register.getMobile();
            String organizationNo = register.getCreditCode();
            try {
                corporationNo = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, corporationNo);
                phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, phone);
                organizationNo = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, organizationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tenantInfoExtend.setCorporationNo(corporationNo);
            tenantInfoExtend.setPhone(phone);
            tenantInfoExtend.setOrganizationNo(organizationNo);
            tenantInfoExtend.setAuthId(authLogId);
            tenantInfoExtend.setApplyTenantUser(tenantUser.getId());
            //如果不存在则插入
            tenantInfoExtend.setId(UUIDGenerator.generate());
            save(tenantInfoExtend);
        }else if(tenantInfoExtend != null && tenantInfoExtend.getAuthStatus() != null && tenantInfoExtend.getAuthStatus() == TenantAuthStatus.STATUS0.getStatus()){
            tenantInfoExtend.setName(register.getEnterpriseName());
            tenantInfoExtend.setAuthType(TenantAuthType.CREATED.getType());
            tenantInfoExtend.setTenantId(tenantId);
            tenantInfoExtend.setTenantType(TenantType.GROUP.getType());
            tenantInfoExtend.setOrganizationNo(register.getCreditCode());
            tenantInfoExtend.setCorporation(register.getLegalPerson());
            String corporationNo = register.getIdentity();
            String phone = register.getMobile();
            String organizationNo = register.getCreditCode();
            try {
                corporationNo = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, corporationNo);
                phone = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, phone);
                organizationNo = RsaUtils.encryptByPublicKey(SignCommonConstant.PUBLICKEYSTRING, organizationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            tenantInfoExtend.setCorporationNo(corporationNo);
            tenantInfoExtend.setPhone(phone);
            tenantInfoExtend.setOrganizationNo(organizationNo);
            tenantInfoExtend.setAuthId(authLogId);
            tenantInfoExtend.setApplyTenantUser(tenantUser.getId());
            updateById(tenantInfoExtend);
        }
        //实名认证记录数据
//        TenantAuthLog tenantAuthLog = new TenantAuthLog();
//        tenantAuthLog.setId(authLogId);
//        tenantAuthLog.setName(tenantInfoExtend.getName());
//        tenantAuthLog.setOrganizationNo(organizationNo);
//        tenantAuthLog.setCorporation(tenantInfoExtend.getCorporation());
//        tenantAuthLog.setCorporationNo(corporationNo);
//        tenantAuthLog.setPhone(phone);
//        tenantAuthLog.setAuthStatus(TenantAuthStatus.STATUS2.getStatus());
//        tenantAuthLog.setExtendId(tenantInfoExtend.getId());
//        tenantAuthLog.setAuthType(TenantAuthType.CREATED.getType());
//        tenantAuthLog.setRealItem(TenantAuthRealItemType.Item1.getType());
//        tenantAuthLog.setApplyTime(new Date());
//        tenantAuthLog.setTenantId(tenantId);
//        tenantAuthLog.setTenantType(TenantType.GROUP.getType());
//        tenantAuthLog.setApplyTenantUser(tenantUser.getId());
//        tenantAuthLogService.save(tenantAuthLog);

        //生成企业签章
//        if(register.getIsMakeSeal() != null && register.getIsMakeSeal() == 1){
//            entSealBusinessService.generateEntDefaultSeal(register.getEnterpriseName(),tenantUser,"");
//        }

    }

    @Override
    public void employeeCreate(EmployeeRegister register) {
        //校验租户是否存在
        String tenantId = apiRelationLinkService.getSystemIdByExAccount(register.getAppAuthToken(), ApiConstants.EXTEND_TYPE_TENANT, register.getEmployeeAccount());
        if (MyStringUtils.isBlank(tenantId)) {
            throw new PaasException(ApiCode.TARGET_COMPANY_UNREGISTERED.getCode(), ApiCode.TARGET_COMPANY_UNREGISTERED.getTemplate());
        }
        //租户加入用户
        EnterpriseRegister newRegister = new EnterpriseRegister();
        BeanUtils.copyProperties(register, newRegister);
        extendJoinTenant(tenantId, newRegister);
    }

    @Override
    public void employeeModify(EmployeeModify modify) {
        //校验租户-用户是否存在
        String tenantUserId = apiRelationLinkService.getSystemIdByExAccount(modify.getAppAuthToken(), ApiConstants.EXTEND_TYPE_USER, modify.getAccount());
        if (MyStringUtils.isBlank(tenantUserId)) {
            throw new PaasException(ApiCode.TARGET_USER_UNREGISTERED.getCode(), ApiCode.TARGET_USER_UNREGISTERED.getTemplate());
        }
        SysTenantUser tenantUser = iSysTenantUserService.getById(tenantUserId);
        if (tenantUser == null) {
            throw new PaasException(ApiCode.TARGET_USER_LOG_OUT.getCode(), ApiCode.TARGET_USER_LOG_OUT.getTemplate());
        }
        tenantUser.setNickName(modify.getName());
        iSysTenantUserService.updateById(tenantUser);
    }

    @Override
    public void employeeRemove(EmployeeRemove remove) {
        //移除租户下的用户
        String tenantUserId = apiRelationLinkService.getSystemIdByExAccount(remove.getAppAuthToken(), ApiConstants.EXTEND_TYPE_USER, remove.getAccount());
        if (MyStringUtils.isNotBlank(tenantUserId)) {
            List<String> tenantUserIds = new ArrayList<>();
            tenantUserIds.add(tenantUserId);
            sysUserService.deleteBatchUsers(tenantUserIds);
        }
        //移除关联关系
        apiRelationLinkService.removeByExAccount(remove.getAppAuthToken(), ApiConstants.EXTEND_TYPE_USER, remove.getAccount());
    }

    @Override
    public EmployeeRes employee(EmployeeQuery info) {
        //校验租户-用户是否存在
        String tenantUserId = apiRelationLinkService.getSystemIdByExAccount(info.getAppAuthToken(), ApiConstants.EXTEND_TYPE_USER, info.getAccount());
        if (MyStringUtils.isBlank(tenantUserId)) {
            throw new PaasException(ApiCode.TARGET_USER_UNREGISTERED.getCode(), ApiCode.TARGET_USER_UNREGISTERED.getTemplate());
        }
        SysTenantUser tenantUser = iSysTenantUserService.getById(tenantUserId);
        if (tenantUser == null) {
            throw new PaasException(ApiCode.TARGET_USER_LOG_OUT.getCode(), ApiCode.TARGET_USER_LOG_OUT.getTemplate());
        }
        EmployeeRes res = new EmployeeRes();
        res.setAccount(info.getAccount());
        res.setName(tenantUser.getNickName());
        SysUser sysUser = sysUserService.getById(tenantUser.getUserId());
        if (sysUser == null) {
            throw new PaasException(ApiCode.TARGET_ACCOUNT_LOG_OUT.getCode(), ApiCode.TARGET_ACCOUNT_LOG_OUT.getTemplate());
        }
        res.setMobile(sysUser.getPhone());

        return res;
    }

    void extendJoinTenant(String tenantId, EnterpriseRegister register) {
        SysUser sysUser = sysUserService.getUserByPhone(register.getMobile());
        String accountId = null;
        if (sysUser == null) {
            //校验用户是否已经注册：否：注册账号
            accountId = addUser(register.getName(), "MOBILE",register.getMobile());
        } else {
            accountId = sysUser.getId();
        }
        SysTenantUser tenantUser = iSysTenantUserService.getTenantUser(tenantId, accountId);
        if (tenantUser == null) {
            //账号是否已经加入租户下：否：加入租户
            //关联租户-用户
            tenantUser = new SysTenantUser();
            tenantUser.setTenantId(tenantId);
            tenantUser.setUserId(accountId);
            tenantUser.setNickName(register.getName());
            tenantUser.setStatus(1);
            tenantUser.setAddType("join");

            iSysTenantUserService.save(tenantUser);
        }

//        //绑定租户-用户关系
//        bindExtractedRelation(register.getAppAuthToken(), register.getEmployeeAccount(), tenantUser.getId(), ApiConstants.EXTEND_TYPE_USER);
    }

    String addUser(String name, String contectType,String account) {
        SysUser user = new SysUser();
        user.setRealname(name);
        user.setUsername(account);

        String password = MD5Util.MD5Encode(RandomUtil.randomString(16), "UTF-8");
        user.setPassword(password);
        if(contectType.equals("MOBILE")){
            user.setPhone(account);
        }else if(contectType.equals("EMAIL")){
            user.setEmail(account);
        }

        user.setInitUserInfo(false);
        String salt = oConvertUtils.randomGen(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
        user.setPassword(passwordEncode);
        user.setStatus(1);
        user.setUpdatePasswordTime(new Date());
        sysUserService.save(user);
        return user.getId();
    }

    private void bindExtractedRelation(String appAuthToken, String account, String systemId, String type) {
        ApiRelationLink relationLink = new ApiRelationLink();
        relationLink.setToken(appAuthToken);
        relationLink.setExternalAccount(account);
        relationLink.setType(type);
        relationLink.setSystemId(systemId);
        apiRelationLinkService.save(relationLink);
    }

    @Override
    public TenantInfoDTO getTenantInfoExt(String tenantId) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getDeleteFlag, 0).eq(TenantInfoExtend::getTenantId, tenantId);
//                .eq(StringUtils.isNotEmpty(userId),TenantInfoExtend::getSysUserId,userId);
        TenantInfoExtend temp = this.getOne(queryWrapper);
        if (temp == null) {
            return new TenantInfoDTO();
        }


        TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
        tenantInfoDTO.setId(temp.getId());
        tenantInfoDTO.setAuthStatus(temp.getAuthStatus());
        tenantInfoDTO.setName(temp.getName());
        if (temp.getAuthId() != null) {
            TenantAuthLog authLog = tenantAuthLogService.getById(temp.getAuthId());
            if(authLog != null){
                tenantInfoDTO.setCheckMes(authLog.getCheckMsg());
                tenantInfoDTO.setRealItem(authLog.getRealItem());
            }
        }

//        if (MyStringUtils.isNotBlank(temp.getOrganizationNo())) {
//            tenantInfoDTO.setOrganizationNo(DesensitizationUtils.around(temp.getOrganizationNo(), 6, 4));
//        }
        String organizationNo = temp.getOrganizationNo();
        try {
            organizationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, organizationNo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(temp.getTenantType() == TenantType.PERSONAL.getType()){
            tenantInfoDTO.setOrganizationNo(DesensitizationUtils.getIdNumber(organizationNo));
        }else if(temp.getTenantType() == TenantType.GROUP.getType()){
            tenantInfoDTO.setOrganizationNo(organizationNo);
        }

        tenantInfoDTO.setCreateTime(temp.getCreateTime());
        tenantInfoDTO.setCorporation(temp.getCorporation());
        tenantInfoDTO.setAuthType(temp.getAuthType());
        tenantInfoDTO.setTenantId(temp.getTenantId());
        tenantInfoDTO.setApplyTenantUser(temp.getApplyTenantUser());
        tenantInfoDTO.setIdCardType(temp.getIdCardType());
        String corporationNo = temp.getCorporationNo();
        try {
            corporationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, corporationNo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoDTO.setCorporationNo(DesensitizationUtils.getIdNumber(corporationNo));
        String phone = temp.getPhone();
        try {
            phone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tenantInfoDTO.setPhone(DesensitizationUtils.getPhone(phone));
        tenantInfoDTO.setTenantType(temp.getTenantType());
//        AnnexStorage query = new AnnexStorage();
//        query.setFatherId(temp.getAuthId());
//        List<AnnexStorage> files = iAnnexStorageService.getByEntity(query);
//        if (CollUtil.isNotEmpty(files)) {
//            files.forEach(f -> {
//                StorageDto storageDto = new StorageDto();
//                storageDto.setId(f.getId());
//                storageDto.setRealName(f.getRealName());
//                if (f.getDataCategory().equals(AnnexType.IDPIC1)) {
//                    tenantInfoDTO.setIdCardFace(storageDto);
//                } else if (f.getDataCategory().equals(AnnexType.IDPIC2)) {
//                    tenantInfoDTO.setIdCardEmblem(storageDto);
//                } else if (f.getDataCategory().equals(AnnexType.ORGANIZATIONPIC)) {
//                    tenantInfoDTO.setBusinessLicense(storageDto);
//                }else if(f.getDataCategory().equals(AnnexType.AUTHORIZEBOOK)){
//                    tenantInfoDTO.setAuthorizeBook(storageDto);
//                }
//            });
//        }

        return tenantInfoDTO;
    }

    @Override
    public List<TenantInfoDTO> getJoinedEnterprise(String tenantUserId) {
        List<TenantInfoDTO> result = new ArrayList<>();
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getDeleteFlag, 0)
//                .eq(TenantInfoExtend::getTenantId, tenantId);
                .eq(StringUtils.isNotEmpty(tenantUserId), TenantInfoExtend::getSysUserId, tenantUserId);
        List<TenantInfoExtend> extendList = this.list(queryWrapper);
//        result.add()
        extendList.forEach(temp -> {
            TenantInfoDTO tenantInfoDTO = new TenantInfoDTO();
            tenantInfoDTO.setId(temp.getId());
            tenantInfoDTO.setAuthStatus(temp.getAuthStatus());
            tenantInfoDTO.setName(temp.getName());
            SysUserDepart sysUserDepart = iSysUserDepartService.getOne(Wrappers.lambdaQuery(SysUserDepart.class).eq(SysUserDepart::getTenantId, temp.getTenantId()).select(SysUserDepart::getId).last("limit 0,1"));
            tenantInfoDTO.setDepartId(sysUserDepart.getDepartId());
            tenantInfoDTO.setOrganizationNo(temp.getOrganizationNo());
            tenantInfoDTO.setCreateTime(temp.getCreateTime());
            tenantInfoDTO.setCorporation(temp.getCorporation());
            tenantInfoDTO.setAuthType(temp.getAuthType());
            tenantInfoDTO.setTenantId(temp.getTenantId());
            tenantInfoDTO.setCorporationNo(temp.getCorporationNo());
            tenantInfoDTO.setPhone(temp.getPhone());
            tenantInfoDTO.setTenantType(temp.getTenantType());
            result.add(tenantInfoDTO);
        });
        return result;
    }


    @Override
    public List<TenantInfoExtend> getByEntity(TenantInfoExtend query) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getDeleteFlag, 0).eq(MyStringUtils.isNotBlank(query.getOrganizationNo()), TenantInfoExtend::getOrganizationNo, query.getOrganizationNo()).eq(query.getTenantType() != null, TenantInfoExtend::getTenantType, query.getTenantType()).eq(query.getAuthStatus() != null, TenantInfoExtend::getAuthStatus, query.getAuthStatus()).like(MyStringUtils.isNotBlank(query.getName()), TenantInfoExtend::getName, query.getName());

        return this.list(queryWrapper);
    }

    @Autowired
    private ITenantAuthLogService tenantAuthLogService;

    private void generatePersonDefaultSeal(String name) {
        BufferedImage bufferedImage = null;
        byte[] sealByte = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            bufferedImage = sealGenerateService.drawRectangleNoFrame(name, SealColorEnum.RED.getCode());
            ImageIO.write(bufferedImage, "png", output);
            sealByte = output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }


        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignPersonSeal personSeal = new SignPersonSeal();
        personSeal.setSealName("默认签名");
        personSeal.setSysDeptId(currentUser.getDepartId());
        personSeal.setDeleteFlag(false);

        boolean save = personSealService.save(personSeal);
        String annexId = signFileService.saveAnnexStorage(sealByte, SignFileEnum.SEAL_FILE_PERSON, personSeal.getId());
//        signFileService.updateAnnexStorage(SignFileEnum.SEAL_FILE_PERSON, personSeal.getId(), annexId);


    }

    @Override
    public List<SysTenantInfoExtendVO> listMyTenant() {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        List<SysTenantInfoExtendVO> result = mapper.listMyTenant(loginUser.getId());
        if (CollUtil.isNotEmpty(result)) {
            List<SysTenantInfoExtendVO> jobs = signRuTaskService.getMyTenantJobs();
            if (CollUtil.isNotEmpty(jobs)) {
                Map<String, Integer> myJobMap = jobs.stream().collect(Collectors.toMap(SysTenantInfoExtendVO::getTenantId, SysTenantInfoExtendVO::getMyJobCount));
                result.forEach(r -> {
                    r.setMyJobCount(myJobMap.get(r.getTenantId()));
                });
            }
        }

        return result;
    }

    @Override
    public ConfirmPara getNameAndNo(String tenantId) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getTenantId, tenantId).eq(TenantInfoExtend::getDeleteFlag, 0);
        List<TenantInfoExtend> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            TenantInfoExtend tem = list.get(0);
            ConfirmPara para = new ConfirmPara();
            para.setName(tem.getName());
            String organizationNo = tem.getOrganizationNo();
            try {
                organizationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, organizationNo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            para.setIdCard(organizationNo);

            return para;
        }

        return null;
    }

    @Override
    public TenantInfoExtend getTenantInfoByTenantId(String tenantId) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getTenantId, tenantId).eq(TenantInfoExtend::getDeleteFlag, 0);
        List<TenantInfoExtend> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TenantInfoExtend getTenantInfoByTenantIdAndOrderNo(String tenantId, String orderNo) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getTenantId, tenantId).eq(TenantInfoExtend::getOrderNo, orderNo).eq(TenantInfoExtend::getDeleteFlag, 0);
        List<TenantInfoExtend> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TenantInfoExtend getTenantInfoByOrderNo(String orderNo) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getOrderNo, orderNo).eq(TenantInfoExtend::getDeleteFlag, 0);
        List<TenantInfoExtend> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateSysBType(TenantSysBTypeVO vo) {
        if (MyStringUtils.isBlank(vo.getTenantId()) || MyStringUtils.isBlank(vo.getSysType())) {
            throw new PaasException("参数不能为空");
        }
        if ("core".equals(vo.getSysType())) {
            LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TenantInfoExtend::getDeleteFlag, 0).eq(TenantInfoExtend::getSysType, "core");
            int count = (int)count(queryWrapper);
            if (count >= licenseBean.getGrant().getCoreFirmCeiling()) {
                throw new PaasException("只能创建" + licenseBean.getGrant().getCoreFirmCeiling() + "个核心企业");
            }
        }
        LambdaUpdateWrapper<TenantInfoExtend> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TenantInfoExtend::getTenantId, vo.getTenantId()).eq(TenantInfoExtend::getDeleteFlag, 0).set(TenantInfoExtend::getSysType, vo.getSysType());
        update(updateWrapper);
    }

    @Override
    public TenantCompanyInfoDTO getTenantInfoCompanyInfo(String tenantName) throws Exception {

        TenantCompanyInfoDTO tenantCompanyInfoDTO = new TenantCompanyInfoDTO();

        TenantInfoExtend tenantInfoExtend = this.getOne(new LambdaQueryWrapper<TenantInfoExtend>()
                .eq(TenantInfoExtend::getName, tenantName)
                .eq(TenantInfoExtend::getAuthStatus, TenantAuthStatus.STATUS2.getStatus())
                .eq(TenantInfoExtend::getTenantType, TenantType.GROUP.getType())
                .eq(TenantInfoExtend::getDeleteFlag, 0));
        if (tenantInfoExtend != null) {
            tenantCompanyInfoDTO.setId(tenantInfoExtend.getId());
            tenantCompanyInfoDTO.setTenantId(tenantInfoExtend.getTenantId());
            tenantCompanyInfoDTO.setOrganizationNo(RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, tenantInfoExtend.getOrganizationNo()));
            tenantCompanyInfoDTO.setName(tenantInfoExtend.getName());
            tenantCompanyInfoDTO.setCreateTime(DateUtil.date2Str(tenantInfoExtend.getCreateTime(), DateUtil.datetimeFormat.get()));
            tenantCompanyInfoDTO.setVerifyTime(DateUtil.date2Str(tenantInfoExtend.getVerifyTime(), DateUtil.datetimeFormat.get()));

            List<SysTenantUser> sysTenantUsers = iSysTenantUserService.getTenantSystemUsersByTenantId(tenantInfoExtend.getTenantId());
            List<SysUserForAddCompanyVO> sysUsers = new ArrayList<>();
            for(SysTenantUser sysTenantUser : sysTenantUsers){
                SysUser sysUser = sysUserService.getById(sysTenantUser.getUserId());
                SysUserForAddCompanyVO sysUserVO = new SysUserForAddCompanyVO();
                sysUserVO.setRealname(sysUser.getRealname());
                sysUserVO.setUsername(sysUser.getUsername());
                sysUsers.add(sysUserVO);
            }
            tenantCompanyInfoDTO.setSysTenantUsers(sysTenantUsers);
            tenantCompanyInfoDTO.setSysUserVO(sysUsers);
            tenantCompanyInfoDTO.setIsExist(1);
        }else{
            tenantCompanyInfoDTO.setIsExist(0);
        }

        return tenantCompanyInfoDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> createCompanyTenant(TenantCompanyInfoAddVO tenantCompany) {
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        TenantInfoExtend infoExtend = this.getOne(new LambdaQueryWrapper<TenantInfoExtend>()
                .eq(TenantInfoExtend::getName, tenantCompany.getName())
                .eq(TenantInfoExtend::getAuthStatus, TenantAuthStatus.STATUS2.getStatus())
                .eq(TenantInfoExtend::getTenantType, TenantType.GROUP.getType())
                .eq(TenantInfoExtend::getDeleteFlag, 0));

        if(infoExtend != null){
            return Result.error("企业已经存在，不能重复创建");
        }else if(infoExtend == null){
            //创建租户
            SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
            sysTenantInfoVO.setPhone(loginUser.getPhone());
            sysTenantInfoVO.setTenantName(tenantCompany.getName());
            sysTenantInfoVO.setEmail(loginUser.getEmail());
//            sysTenantInfoVO.setUsername(loginUser.getUsername());
            sysTenantInfoVO.setTenantType(TenantType.GROUP.getType());
            sysTenantInfoVO.setRealName(loginUser.getRealname());
            SysAddTenantInfoVO sysAddTenantInfoVO = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO);
            String tenantId = sysAddTenantInfoVO.getTenantId();
            SysTenantUser sysTenantUser = iSysTenantUserService.getTenantUser(tenantId, loginUser.getId());
            SysDepart sysDepart = iSysDepartService.getById(sysAddTenantInfoVO.getDepartId());

            infoExtend = new TenantInfoExtend();
            infoExtend.setName(tenantCompany.getName());
            infoExtend.setTenantId(tenantId);
            infoExtend.setTenantType(TenantType.GROUP.getType());
            infoExtend.setApplyTenantUser(sysTenantUser.getId());
            infoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
            save(infoExtend);

            //绑定数据关系
            bindTaskData(tenantCompany.getName(), tenantId);
            return Result.OK(infoExtend.getTenantId());
        }
        return Result.OK(infoExtend.getTenantId());
    }

    private String addTenantInfo(EnterpriseRegister register) {
        //创建租户
        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
        if(register.getContactType().equals("MOBILE")){
            sysTenantInfoVO.setPhone(register.getMobile());
        }else if (register.getContactType().equals("EMAIL")){
            sysTenantInfoVO.setEmail(register.getEmail());
        }

        sysTenantInfoVO.setTenantName(register.getEnterpriseName());
        sysTenantInfoVO.setTenantType(TenantType.GROUP.getType());
        sysTenantInfoVO.setRealName(register.getName());

        String tenantId = sysTenantInfoService.addTenantAndAddApp(sysTenantInfoVO).getTenantId();

        TenantInfoExtend infoExtend = new TenantInfoExtend();
        infoExtend.setName(register.getEnterpriseName());
        infoExtend.setAuthStatus(TenantAuthStatus.STATUS0.getStatus());
        infoExtend.setAuthType(TenantAuthType.CREATED.getType());
        infoExtend.setTenantId(tenantId);
        infoExtend.setTenantType(TenantType.GROUP.getType());
        save(infoExtend);

        //绑定数据关系
        bindTaskData(register.getEnterpriseName(), tenantId);

        return tenantId;
    }

    private void bindTaskData(String tenantName, String tenantId) {
        //解析数据库已有数据做绑定表:sign_ru_task
        //绑定企业名：租户ID
        flowService.bindOutUserTask(tenantName, null, null, null, null, null, tenantId, null, "tenantCheck");
        //绑定租户ID：(查询用户ID)租户账号ID
        flowService.bindTenantUserTaskAll(tenantId, "tenantCheck");
    }

    private void saveTenantAnnex(TenantAuthVO vo, String fatherId) {
        List<StorageDto> files = new ArrayList<>();
        if (StringUtils.isNotEmpty(vo.getIdCardEmblem())) {
            files.add(new StorageDto(vo.getIdCardEmblem()));
        }
        if (StringUtils.isNotEmpty(vo.getIdCardFace())) {
            files.add(new StorageDto(vo.getIdCardFace()));
        }
        if (StringUtils.isNotEmpty(vo.getBusinessLicense())) {
            files.add(new StorageDto(vo.getBusinessLicense()));
        }
        if (StringUtils.isNotEmpty(vo.getAuthorizeBook())) {
            files.add(new StorageDto(vo.getAuthorizeBook()));
        }
        if (files.size() > 0) iAnnexStorageService.updateMainDataFiles(fatherId, files);
    }

    TenantInfoExtend getByOrgCode(String orgCode) {
        LambdaQueryWrapper<TenantInfoExtend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantInfoExtend::getDeleteFlag, 0).eq(TenantInfoExtend::getOrgCode, orgCode);

        return this.getOne(queryWrapper);
    }

    void checkRepeat(TenantInfoDTO tenantInfoDTO) {
        String organizationNo = tenantInfoDTO.getOrganizationNo();
        if (MyStringUtils.isBlank(organizationNo)) {
            if (tenantInfoDTO.getTenantType().equals(4) || tenantInfoDTO.getTenantType().equals(5)) {
                return;
            }
            throw new PaasException("组织号码不能为空");
        }
        TenantInfoExtend query = new TenantInfoExtend();
        query.setOrganizationNo(organizationNo);
        List<TenantInfoExtend> list = getByEntity(query);
        if (CollUtil.isNotEmpty(list)) {
            if (MyStringUtils.isBlank(tenantInfoDTO.getId())) {
                throw new PaasException("数据重复");
            } else if (!tenantInfoDTO.getId().equals(list.get(0).getId())) {
                throw new PaasException("数据重复");
            }
        }
    }
}
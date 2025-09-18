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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.api.exception.RequestParamsException;
import com.kaifangqian.modules.cert.enums.CertHolderTypeEnum;
import com.kaifangqian.modules.cert.enums.CertTypeEnum;
import com.kaifangqian.modules.cert.pojo.TenantCertInfo;
import com.kaifangqian.modules.cert.service.CertBusinessService;
import com.kaifangqian.modules.opensign.constant.SignCommonConstant;
import com.kaifangqian.modules.opensign.service.business.ReBusinessService;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportDocFileVo;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.*;
import com.kaifangqian.modules.system.mapper.SysTenantAppVersionMapper;
import com.kaifangqian.modules.system.mapper.SysTenantInfoMapper;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.common.constant.ApiCode;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.common.constant.enums.AuthType;
import com.kaifangqian.common.redis.util.RedisUtil;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.DesensitizationUtils;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.entity.*;
import com.kaifangqian.modules.system.enums.*;
import com.kaifangqian.modules.system.service.*;
import com.kaifangqian.modules.system.vo.*;
import com.kaifangqian.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 租户管理服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantInfoServiceImpl extends ServiceImpl<SysTenantInfoMapper, SysTenantInfo> implements ISysTenantInfoService {

    @Resource
    private SysTenantInfoMapper mapper;
    @Resource
    private SysTenantAppVersionMapper sysTenantAppVersionMapper;
    @Autowired
    private ISysDepartService iSysDepartService;
    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private ISysUserDepartService iSysUserDepartService;
    @Autowired
    private ISysAppInfoService sysAppInfoService;
    @Autowired
    private ISysAppVersionService iSysAppVersionService;
    @Autowired
    private ISysTenantAppVersionService iSysTenantAppVersionService;
    @Autowired
    private ISysTenantUserAppService iSysTenantUserAppService;
    @Autowired
    private ISysAuthGroupService iSysAuthGroupService;
    @Autowired
    private ISysAuthGroupMemberService iSysAuthGroupMemberService;
    @Autowired
    private ISysAuthGroupPermissionService iSysAuthGroupPermissionService;
    @Autowired
    private ISysAppVersionPermissionService iSysAppVersionPermissionService;
    @Autowired
    private ISysTenantUserService iSysTenantUserService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysAppVersionGroupService iSysAppVersionGroupService;
    @Autowired
    private ISysAppVersionGroupPermissionService iSysAppVersionGroupPermissionService;
    @Autowired
    private ISysTenantConfigService sysTenantConfigService;
    @Autowired
    private CertBusinessService certBusinessService;
    @Autowired
    private ReBusinessService reBusinessService;

    @Override
    public IPage<SysTenantInfoRes> pageExt(Page<SysTenantInfoRes> page, SysTenantInfoQuery query) {
        IPage<SysTenantInfoRes> result = mapper.pageExt(page, query);
        List<SysTenantInfoRes> list = result.getRecords();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(l -> {
                String phone = l.getPhone();
                try {
                    phone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, phone);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                l.setPhone(phone);
            });
            List<String> tenantIds = list.stream().map(SysTenantInfoRes::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(tenantIds)) {
                List<SysTenantAppVersionVO> sysTenantAppVersionVOS = sysTenantAppVersionMapper.getTenantsApps(tenantIds);
                if (CollUtil.isNotEmpty(sysTenantAppVersionVOS)) {
                    Map<String, List<SysTenantAppVersionVO>> tenantAppMap = sysTenantAppVersionVOS.stream().collect(Collectors.groupingBy(SysTenantAppVersionVO::getTenantId));
                    list.forEach(a -> {
                        a.setApps(tenantAppMap.get(a.getId()));
                    });
                }
            }
        }
        return result;
    }

    @Override
    public SysTenantInfo getByInvitationCode(String code) {
        LambdaQueryWrapper<SysTenantInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantInfo::getDeleteFlag, 0).eq(SysTenantInfo::getInvitationCode, code);

        return this.getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void personalTenantRegister(UserJionSystemVO vo) {
        String realKey = MD5Util.MD5Encode(vo.getPhone() + vo.getCaptchaKey(), "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(vo.getCaptcha())) {
            throw new PaasException("验证码错误");
        }
        //开通个人租户
        SysTenantInfoVO sysTenantInfoVO = new SysTenantInfoVO();
        sysTenantInfoVO.setTenantType(TenantType.PERSONAL.getType());
        sysTenantInfoVO.setRealName(vo.getRealName());
        sysTenantInfoVO.setPhone(vo.getPhone());

        addTenantAndAddApp(sysTenantInfoVO);
    }

    /**
     * @create by zhenghuihan
     * @createTime 2023/1/30 15:44
     * @description 开通租户时顺便处理账号，手机号与用户名至少一个非空。
     * 当username不为空时，根据username检索用户，当存在时，重置状态。当不存在时，新建账号，手机号不处理，设置为空。
     * 当phone不为空，根据手机号检索用户：当存在时，重置状态。当不存在时，把手机号当做用户名根据用户名检索用户：当存在时，返回错误。当不存在时，新建账号，用户名和账号同时设置为手机号。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAddTenantInfoVO addTenantAndAddApp(SysTenantInfoVO sysTenantInfoVO) {
        checkDate(sysTenantInfoVO);

        SysAddTenantInfoVO sysAddTenantInfoVO = new SysAddTenantInfoVO();
        SysTenantInfo sysTenantInfo = new SysTenantInfo();
        if (sysTenantInfoVO.getTenantStatus() != null) {
            sysTenantInfo.setTenantStatus(sysTenantInfoVO.getTenantStatus());
        } else {
            sysTenantInfo.setTenantStatus(TenantStatus.ENABLE.getType());
        }
        sysTenantInfo.setTenantType(sysTenantInfoVO.getTenantType());
        if (TenantType.GROUP.getType().equals(sysTenantInfoVO.getTenantType())) {
            sysTenantInfo.setTenantName(sysTenantInfoVO.getTenantName());
            sysTenantInfo.setTenantDesc(sysTenantInfoVO.getTenantDesc());
            //设置租户邀请码
            String invitationCode = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8).toUpperCase();
            while (getByInvitationCode(invitationCode) != null) {
                invitationCode = RandomUtil.randomString(CommonConstant.BASE_CHECK_CODES, 8).toUpperCase();
            }
            sysTenantInfo.setInvitationCode(invitationCode);
        } else if (TenantType.PERSONAL.getType().equals(sysTenantInfoVO.getTenantType())) {
            if(StringUtils.isNotEmpty(sysTenantInfoVO.getPhone())){
                sysTenantInfo.setTenantName(sysTenantInfoVO.getPhone());
                sysTenantInfo.setTenantDesc(sysTenantInfoVO.getPhone());
            }else{
                sysTenantInfo.setTenantName(sysTenantInfoVO.getEmail());
                sysTenantInfo.setTenantDesc(sysTenantInfoVO.getEmail());
            }

            sysTenantInfoVO.setTenantName(MyStringUtils.isNotBlank(sysTenantInfoVO.getUsername()) ? sysTenantInfoVO.getUsername() : sysTenantInfoVO.getPhone() + "的个人空间");

        } else {
            throw new PaasException("信息错误");
        }

        //开通租户
        this.save(sysTenantInfo);
        sysAddTenantInfoVO.setTenantId(sysTenantInfo.getId());

        String tenantId = sysTenantInfo.getId();

        //新增租户配置
        SysTenantConfig config = new SysTenantConfig();
        config.setTenantId(tenantId);
        config.setType("tenant_user_check");
        config.setName("用户加入租户是否需要审核");
        config.setValue("false");
        sysTenantConfigService.save(config);

        sysTenantInfoVO.setId(tenantId);

        //创建根组织
        SysDepartVO sysDepartVO = new SysDepartVO();
        sysDepartVO.setDepartName(sysTenantInfoVO.getTenantName());
        String departId = iSysDepartService.saveOrganization(sysDepartVO, tenantId, null);
        sysAddTenantInfoVO.setDepartId(departId);

        //处理账号
        String userId = null;
        SysUser sysUser = null;
        if (MyStringUtils.isNotBlank(sysTenantInfoVO.getUsername())) {
            sysUser = iSysUserService.getUserByName(sysTenantInfoVO.getUsername());
        } else {
            if (MyStringUtils.isNotBlank(sysTenantInfoVO.getPhone())) {
                sysUser = iSysUserService.getUserByPhone(sysTenantInfoVO.getPhone());
            } else if (MyStringUtils.isNotBlank(sysTenantInfoVO.getEmail())) {
                sysUser = iSysUserService.getUserByEmail(sysTenantInfoVO.getEmail());
            }
        }
        if (sysUser != null) {
            if (0 == sysUser.getStatus()) {
                sysUser.setRealname(sysTenantInfoVO.getRealName());
                sysUser.setStatus(1);

                iSysUserService.updateById(sysUser);
            }
            userId = sysUser.getId();
        } else {
            if (MyStringUtils.isNotBlank(sysTenantInfoVO.getPhone())) {
                sysUser = iSysUserService.getUserByName(sysTenantInfoVO.getPhone());
            } else if (MyStringUtils.isNotBlank(sysTenantInfoVO.getEmail())) {
                sysUser = iSysUserService.getUserByName(sysTenantInfoVO.getEmail());
            }
            String account = "";
            if (MyStringUtils.isNotBlank(sysTenantInfoVO.getPhone())) {
                account = sysTenantInfoVO.getPhone();
            } else if (MyStringUtils.isNotBlank(sysTenantInfoVO.getEmail())) {
                account = sysTenantInfoVO.getEmail();
            }
            if (sysUser != null) {
                throw new PaasException("账号：" + account + "对应的用户已经存在");
            }
            SysUser user = new SysUser();
            user.setRealname(sysTenantInfoVO.getRealName());
            user.setUsername(MyStringUtils.isNotBlank(sysTenantInfoVO.getUsername()) ? sysTenantInfoVO.getUsername() : account);
            String password = MD5Util.MD5Encode(RandomUtil.randomString(16), "UTF-8");
            if (MyStringUtils.isBlank(sysTenantInfoVO.getPassword())) {
                //123456
                user.setPassword(password);
            } else {
                user.setPassword(sysTenantInfoVO.getPassword());
            }
            user.setPhone(sysTenantInfoVO.getPhone());
            user.setEmail(sysTenantInfoVO.getEmail());
            user.setInitUserInfo(false);
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setUpdatePasswordTime(new Date());
            iSysUserService.save(user);
            userId = user.getId();
        }
        if (TenantType.PERSONAL.getType().equals(sysTenantInfoVO.getTenantType())) {
            if (iSysTenantUserService.checkPersonalTenant(userId)) {
                throw new PaasException("个人租户已经开通，不可以重复开通");
            }
        }
        //关联部门-账号
        List<String> userList = new ArrayList<>();
        userList.add(userId);

        iSysUserDepartService.addUserByDepartId(tenantId, departId, userList);

        //关联租户-用户
        SysTenantUser tenantUser = new SysTenantUser();
        tenantUser.setTenantId(tenantId);
        tenantUser.setUserId(userId);
        tenantUser.setNickName(sysTenantInfoVO.getRealName());
        tenantUser.setStatus(1);
        tenantUser.setAddType("system");

        iSysTenantUserService.save(tenantUser);

        List<SysAppVersion> appVersions = checkAndGetAppVersions(sysTenantInfoVO.getInVersionIds(), sysTenantInfoVO.getTenantType());
        //新增租户应用-权限组  租户唯一
        String pId = addGroup(tenantId, null, "默认权限组分类", "默认权限组分类", true, AuthGroupType.GROUP.getType());
        String allGroupId = addGroup(tenantId, pId, "全部权限", "全部权限", true, AuthGroupType.ALL.getType());
        String baseGroupId = addGroup(tenantId, pId, "基础权限", "基础权限", true, AuthGroupType.BASE.getType());
        //处理租户内置应用
        if (CollUtil.isNotEmpty(appVersions)) {
            for (SysAppVersion v : appVersions) {
                //新增租户-应用
                SysAppInfo appInfo = sysAppInfoService.getById(v.getAppInfoId());
                addTenantApp(tenantId, v.getAppInfoId(), v.getId(), appInfo.getUseful());

                //新增租户-用户-应用
                if (!appInfo.getUseful()) {
                    //新增租户-用户-应用
                    SysTenantUserApp sysTenantUserApp = new SysTenantUserApp();
                    sysTenantUserApp.setTenantId(tenantId);
                    sysTenantUserApp.setUserId(userId);
                    sysTenantUserApp.setAppId(v.getAppInfoId());
                    sysTenantUserApp.setAppVersionId(v.getId());
                    iSysTenantUserAppService.saveExt(sysTenantUserApp);
                }

                //维护权限组-用户
                SysAuthGroupMemberList2 memberList = new SysAuthGroupMemberList2();
                memberList.setAuthId(userId);
                memberList.setAuthType(AuthType.USER.getType());
                memberList.setTenantId(tenantId);
                List<String> groups = new ArrayList<>();
                groups.add(allGroupId);
                memberList.setGroupIds(groups);
                iSysAuthGroupMemberService.saveExt2(memberList);

                //维护初始化的最大权限组-权限关系
                addGroupPermisson(allGroupId, v.getId());
                //维护初始化的基础权限组-权限关系
                addBaseGroupPermisson(baseGroupId, v.getId());
                //应用版本对应的权限组-权限关系
                if (v.isDefaultFlag()) {
                    addAppGroupPermission(tenantId, pId, v.getId());
                }
            }
        }
        //租户证书申请对象
        TenantCertInfo tenantCertInfo = new TenantCertInfo();
        tenantCertInfo.setTenantId(tenantId);

        if (TenantType.GROUP.getType().equals(sysTenantInfoVO.getTenantType())) {
            //新增团体租户应用
            addGroupTenantAppVersion(pId, allGroupId, baseGroupId, sysTenantInfoVO);
            //开通团体租户防篡改证书
            tenantCertInfo.setEntName(sysTenantInfo.getTenantName());
            tenantCertInfo.setUscc("");
            tenantCertInfo.setCertType(CertTypeEnum.SYSTEM.getCode());
            tenantCertInfo.setHolderType(CertHolderTypeEnum.ENTERPRISE.getCode());
            //certBusinessService.generateTenantCert(tenantCertInfo);
            //创建租户默认业务线
            reBusinessService.init(tenantUser);
        } else if (TenantType.PERSONAL.getType().equals(sysTenantInfoVO.getTenantType())) {
            //新增个人租户应用
            addPersonalTenantAppVersion(sysTenantInfoVO);
            //开通个人租户防篡改证书
            tenantCertInfo.setName(sysTenantInfo.getTenantName());
            tenantCertInfo.setPhone(sysTenantInfoVO.getPhone());
            tenantCertInfo.setCertType(CertTypeEnum.SYSTEM.getCode());
            tenantCertInfo.setHolderType(CertHolderTypeEnum.PERSONAL.getCode());
            //certBusinessService.generateTenantCert(tenantCertInfo);
        }

        return sysAddTenantInfoVO;
    }


    void addBaseGroupPermisson(String baseGroupId, String versionId) {
        SysAppVersionGroup query1 = new SysAppVersionGroup();
        query1.setAppVersionId(versionId);
        query1.setUseFlag(true);
        query1.setInitFlag(true);
        List<SysAppVersionGroup> appVersionGroups = iSysAppVersionGroupService.listByEntity(query1);
        if (CollUtil.isNotEmpty(appVersionGroups)) {
            appVersionGroups.forEach(g -> {
                SysAppVersionGroupPermission query2 = new SysAppVersionGroupPermission();
                query2.setGroupId(g.getId());
                List<SysAppVersionGroupPermission> groupPermissions = iSysAppVersionGroupPermissionService.listByEntity(query2);
                if (CollUtil.isNotEmpty(groupPermissions)) {
                    addGroupPermissonRule(baseGroupId, versionId, groupPermissions);
                }
            });
        }
    }

    void addAppGroupPermission(String tenantId, String groupPId, String versionId) {
        SysAppVersionGroup query1 = new SysAppVersionGroup();
        query1.setAppVersionId(versionId);
        query1.setUseFlag(true);
        List<SysAppVersionGroup> appVersionGroups = iSysAppVersionGroupService.listByEntity(query1);
        if (CollUtil.isNotEmpty(appVersionGroups)) {
            appVersionGroups.forEach(g -> {
                //新建权限组
                String groupId = addGroup(tenantId, groupPId, g.getGroupName(), g.getGroupDesc(), true, AuthGroupType.CUSTOMIZE.getType());
                //绑定权限
                SysAppVersionGroupPermission query2 = new SysAppVersionGroupPermission();
                query2.setGroupId(g.getId());
                List<SysAppVersionGroupPermission> groupPermissions = iSysAppVersionGroupPermissionService.listByEntity(query2);
                if (CollUtil.isNotEmpty(groupPermissions)) {
                    addGroupPermissonRule(groupId, g.getAppVersionId(), groupPermissions);
                }
            });
        }
    }

    //新增租户-应用
    void addTenantApp(String tenantId, String appId, String versionId, Boolean usefule) {
        SysTenantAppVersion sysTenantAppVersion = new SysTenantAppVersion();
        sysTenantAppVersion.setAppId(appId);
        sysTenantAppVersion.setAppVersionId(versionId);
        sysTenantAppVersion.setTenantId(tenantId);
        sysTenantAppVersion.setUseful(usefule);

        iSysTenantAppVersionService.saveExt(sysTenantAppVersion);
    }

    //新增权限组
    String addGroup(String tenantId, String pId, String groupName, String groupDesc, Boolean systemFlag, Integer groupType) {
        //新增权限组
        SysAuthGroup group = new SysAuthGroup();
        group.setTenantId(tenantId);
        group.setSystemFlag(systemFlag);
        group.setParentId(pId);
        group.setGroupName(groupName);
        group.setGroupDesc(groupDesc);
        group.setGroupType(groupType);
        return iSysAuthGroupService.saveExt(group);
    }

    //新增权限组-权限
    void addGroupPermisson(String groupId, String versionId) {
        List<String> permissions = iSysAppVersionPermissionService.listVersionPermissions(versionId);
        //维护权限组-权限
        if (CollUtil.isNotEmpty(permissions)) {
            SysAppVersion sysAppVersion = iSysAppVersionService.getById(versionId);
            SysAuthGroupPermissionReq req = new SysAuthGroupPermissionReq();
            req.setGroupId(groupId);
            List<AppPermissionVO> appPermissionVOS = new ArrayList<>();
            AppPermissionVO appPermissionVO = new AppPermissionVO();
            appPermissionVO.setAppId(sysAppVersion.getAppInfoId());
            List<PermissionRuleVO> ruleVOS = new ArrayList<>();
            permissions.forEach(p -> {
                PermissionRuleVO vo = new PermissionRuleVO();
                vo.setPermissionId(p);

                ruleVOS.add(vo);
            });
            appPermissionVO.setRuleVOS(ruleVOS);

            appPermissionVOS.add(appPermissionVO);

            req.setAppPermissionVOS(appPermissionVOS);
            iSysAuthGroupPermissionService.addExt(req);
        }
    }

    //新增权限组-权限带数据权限
    void addGroupPermissonRule(String groupId, String versionId, List<SysAppVersionGroupPermission> groupPermissions) {
        //维护权限组-权限
        if (CollUtil.isNotEmpty(groupPermissions)) {
            SysAppVersion sysAppVersion = iSysAppVersionService.getById(versionId);
            Map<String, List<SysAppVersionGroupPermission>> groupPermissionMap = groupPermissions.stream().collect(Collectors.groupingBy(SysAppVersionGroupPermission::getPermissionId));
            SysAuthGroupPermissionReq req = new SysAuthGroupPermissionReq();
            req.setGroupId(groupId);
            List<AppPermissionVO> appPermissionVOS = new ArrayList<>();
            AppPermissionVO appPermissionVO = new AppPermissionVO();
            appPermissionVO.setAppId(sysAppVersion.getAppInfoId());
            List<PermissionRuleVO> ruleVOS = new ArrayList<>();
            for (Map.Entry<String, List<SysAppVersionGroupPermission>> entry : groupPermissionMap.entrySet()) {
                PermissionRuleVO vo = new PermissionRuleVO();
                vo.setPermissionId(entry.getKey());
                if (CollUtil.isNotEmpty(entry.getValue())) {
                    vo.setRuleIds(entry.getValue().stream().map(SysAppVersionGroupPermission::getPermissionDataId).collect(Collectors.toList()));
                }

                ruleVOS.add(vo);
            }
            appPermissionVO.setRuleVOS(ruleVOS);
            appPermissionVOS.add(appPermissionVO);
            req.setAppPermissionVOS(appPermissionVOS);

            iSysAuthGroupPermissionService.addExt(req);
        }
    }

    List<SysAppVersion> checkAndGetAppVersions(List<String> versionIds, Integer tenantType) {
        List<SysAppVersion> result = new ArrayList<>();
        List<Integer> types = new ArrayList<>();
        if (TenantType.GROUP.getType().equals(tenantType)) {
            types.add(AppAndVersionType.GROUP.getType());
        } else if (TenantType.PERSONAL.getType().equals(tenantType)) {
            types.add(AppAndVersionType.PERSONAL.getType());
        }
        types.add(AppAndVersionType.ALL.getType());
        List<SysAppInfoVO> list = sysAppInfoService.listAllAppsByTypes(types, true);
        if (CollUtil.isEmpty(versionIds)) {
            //默认内置应用
            for (SysAppInfoVO appInfoVO : list) {
                List<SysAppVersionVO> versionVOS = appInfoVO.getAppVersionVOS();
                if (CollUtil.isNotEmpty(versionVOS)) {
                    SysAppVersion sysAppVersion = new SysAppVersion();
                    sysAppVersion.setId(versionVOS.get(0).getId());
                    sysAppVersion.setAppInfoId(appInfoVO.getId());

                    result.add(sysAppVersion);
                }
            }
        } else {
            if (CollUtil.isNotEmpty(list)) {
                //处理list
                Iterator<SysAppInfoVO> iterator1 = list.iterator();
                while (iterator1.hasNext()) {
                    SysAppInfoVO appInfoVO = iterator1.next();
                    if (CollUtil.isEmpty(appInfoVO.getAppVersionVOS())) {
                        iterator1.remove();
                    }
                }

                if (CollUtil.isEmpty(versionIds)) {
                    throw new PaasException("未包含全部内置应用");
                }
                if (list.size() != versionIds.size()) {
                    throw new PaasException("未包含全部内置应用");
                }
                result = iSysAppVersionService.getByIds(versionIds);
                if (list.size() != result.size()) {
                    throw new PaasException("未包含全部内置应用");
                }
                Iterator<SysAppInfoVO> iterator = list.iterator();
                while (iterator.hasNext()) {
                    SysAppInfoVO appInfoVO = iterator.next();
                    result.forEach(v -> {
                        if (appInfoVO.getId().equals(v.getAppInfoId())) {
                            iterator.remove();
                        }
                    });
                }

                if (list.size() > 0) {
                    throw new PaasException("未包含全部内置应用");
                }
            }
        }
        return result;
    }

    void checkDate(SysTenantInfoVO sysTenantInfoVO) {
        if (sysTenantInfoVO.getTenantType() == null) {
            throw new PaasException("信息不全");
        }
        if (TenantType.GROUP.getType().equals(sysTenantInfoVO.getTenantType()) && MyStringUtils.isBlank(sysTenantInfoVO.getTenantName())) {
            throw new PaasException("信息不全");
        }
        if (MyStringUtils.isBlank(sysTenantInfoVO.getPhone()) && MyStringUtils.isBlank(sysTenantInfoVO.getEmail()) && MyStringUtils.isBlank(sysTenantInfoVO.getUsername())) {
            throw new PaasException("信息不全");
        }
    }

    @Override
    public void updateTenantStatus(String id) {
        if (MyStringUtils.isBlank(id)) {
            throw new PaasException("唯一标识不能为空");
        }
        SysTenantInfo sysTenantInfo = this.getById(id);
        if (sysTenantInfo == null) {
            throw new PaasException("传值有误");
        }
        if (TenantStatus.DISABLE.getType().equals(sysTenantInfo.getTenantStatus())) {
            sysTenantInfo.setTenantStatus(TenantStatus.ENABLE.getType());
        } else if (TenantStatus.ENABLE.getType().equals(sysTenantInfo.getTenantStatus())) {
            sysTenantInfo.setTenantStatus(TenantStatus.DISABLE.getType());
        }
        this.updateById(sysTenantInfo);
    }

    @Override
    public void updateExt(SysTenantInfo sysTenantInfo) {
        SysTenantInfo tem = new SysTenantInfo();
        tem.setId(sysTenantInfo.getId());
        tem.setTenantName(sysTenantInfo.getTenantName());

        this.updateById(sysTenantInfo);
    }

    @Override
    public List<SysTenantAppVersionVO> getTenantApps(String id) {
        return mapper.getTenantApps(id);
    }

    @Override
    public void updateTenantAppStatus(String id) {
        if (MyStringUtils.isBlank(id)) {
            throw new PaasException("唯一标识不能为空");
        }
        SysTenantAppVersion tenantAppVersion = iSysTenantAppVersionService.getById(id);
        if (tenantAppVersion == null) {
            throw new PaasException("传值有误");
        }
        if (TenantAppStatus.DISABLE.getType().equals(tenantAppVersion.getStatus())) {
            tenantAppVersion.setStatus(TenantAppStatus.ENABLE.getType());
        } else if (TenantAppStatus.ENABLE.getType().equals(tenantAppVersion.getStatus())) {
            tenantAppVersion.setStatus(TenantAppStatus.DISABLE.getType());
        }
        iSysTenantAppVersionService.updateById(tenantAppVersion);
    }

    @Override
    public void updateTenantAppStatus(String tenantId, String appId, TenantAppStatus appStatus) {
        SysTenantAppVersion tenantAppVersion = iSysTenantAppVersionService.getByTenantAndApp(tenantId, appId);
        if (tenantAppVersion != null) {
            tenantAppVersion.setStatus(appStatus.getType());

            iSysTenantAppVersionService.updateById(tenantAppVersion);
        }
    }

    @Override
    public SysTenantInfo getByName(String tenantName) {
        LambdaQueryWrapper<SysTenantInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantInfo::getTenantName, tenantName).eq(SysTenantInfo::getDeleteFlag, 0);

        List<SysTenantInfo> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Autowired
    private ITenantAuthLogService tenantAuthLogService;

    @Autowired
    private ITenantInfoExtendService iTenantInfoExtendService;

    @Override
    public List<SignReportSignSubVO> queryTenantAuthLog(Set<String> tenantIds) {
        List<SignReportSignSubVO> auths = new ArrayList<>();
        if (tenantIds == null || tenantIds.isEmpty()) {
            return auths;
        }
        //查询租户扩展信息
        List<TenantInfoExtend> tenantExtends = iTenantInfoExtendService.list(Wrappers.lambdaQuery(TenantInfoExtend.class).in(TenantInfoExtend::getTenantId, tenantIds).select(TenantInfoExtend::getAuthId));
        if (tenantExtends == null || tenantExtends.size() == 0) {
            return auths;
        }
        //查询租户认证记录
        Set<String> extendIds = tenantExtends.stream().filter(tenantInfoExtend -> tenantInfoExtend.getAuthId() != null).map(TenantInfoExtend::getAuthId).collect(Collectors.toSet());
        if (extendIds == null || extendIds.size() == 0) {
            return auths;
        }
        List<TenantAuthLog> tenantAuthLogs = tenantAuthLogService.list(Wrappers.lambdaQuery(TenantAuthLog.class).in(TenantAuthLog::getId, extendIds));
        List<String> applyUserIds = tenantAuthLogs.stream().filter(t -> t.getTenantType().equals(TenantType.GROUP.getType())).map(TenantAuthLog::getApplyTenantUser).collect(Collectors.toList());
        Map<String, SysTenantUser> tenantUserMap = null;
        Map<String, SysUser> userMap = null;
        if (applyUserIds != null && applyUserIds.size() > 0) {
            //查询申请人租户信息
            List<SysTenantUser> tenantUsers = iSysTenantUserService.getByIds(applyUserIds);
            tenantUserMap = tenantUsers.stream().collect(Collectors.toMap(SysTenantUser::getId, t -> t));

            //查询租户的实际user信息
            List<String> userIds = tenantUsers.stream().map(SysTenantUser::getUserId).collect(Collectors.toList());
            List<SysUser> users = iSysUserService.list(Wrappers.lambdaQuery(SysUser.class).in(SysUser::getId, userIds));
            userMap = users.stream().collect(Collectors.toMap(SysUser::getId, t -> t));
        }

        for (TenantAuthLog authLog : tenantAuthLogs) {
            SignReportSignSubVO signSubVO = new SignReportSignSubVO();
            signSubVO.setTitle(authLog.getName());
            List<SignReportDocFileVo> subReport = new ArrayList<>();

            if (authLog.getTenantType().equals(TenantType.GROUP.getType())) {
                SignReportDocFileVo vo1 = new SignReportDocFileVo();
                vo1.setValueFirst("单位名称：");
                vo1.setValueSecond(authLog.getName());

                SignReportDocFileVo vo2 = new SignReportDocFileVo();
                vo2.setValueFirst("社会信息用代码：");
                String organizationNo = authLog.getOrganizationNo();
                try {
                    organizationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, organizationNo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                vo2.setValueSecond(DesensitizationUtils.getIdNumber(organizationNo));


                SignReportDocFileVo vo3 = new SignReportDocFileVo();
                vo3.setValueFirst("法人姓名：");
                vo3.setValueSecond(authLog.getCorporation());

                SignReportDocFileVo vo4 = new SignReportDocFileVo();
                vo4.setValueFirst("法人身份证号：");
                String corporationNo = authLog.getCorporationNo();
                try {
                    corporationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, corporationNo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                vo4.setValueSecond(DesensitizationUtils.getIdNumber(corporationNo));

                SignReportDocFileVo vo5 = new SignReportDocFileVo();
                vo5.setValueFirst("法人手机号:");
                String phone = authLog.getPhone();
                try {
                    phone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, phone);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                vo5.setValueSecond(DesensitizationUtils.getPhone(phone));

                SignReportDocFileVo vo6 = new SignReportDocFileVo();
                vo6.setValueFirst("手机验证码：");
                vo6.setValueSecond(authLog.getVerifyInfo());

                subReport.add(vo1);
                subReport.add(vo2);
                subReport.add(vo3);
                subReport.add(vo4);
                subReport.add(vo5);
                subReport.add(vo6);

                if (tenantUserMap != null && tenantUserMap.get(authLog.getApplyTenantUser()) != null) {
                    SysTenantUser tenantUser = tenantUserMap.get(authLog.getApplyTenantUser());
                    if (userMap != null && userMap.get(tenantUser.getUserId()) != null) {
                        SysUser sysUser = userMap.get(tenantUser.getUserId());
                        SignReportDocFileVo vo7 = new SignReportDocFileVo();
                        vo7.setValueFirst("认证提交人：");
                        vo7.setValueSecond(sysUser.getRealname());
                        subReport.add(vo7);

                        SignReportDocFileVo vo8 = new SignReportDocFileVo();
                        vo8.setValueFirst("提交人手机号：");
                        vo8.setValueSecond(DesensitizationUtils.getPhone(sysUser.getPhone()));
                        subReport.add(vo8);
                    }

                }
            } else if (authLog.getTenantType().equals(TenantType.PERSONAL.getType())) {
                SignReportDocFileVo vo1 = new SignReportDocFileVo();
                vo1.setValueFirst("姓名：");
                vo1.setValueSecond(authLog.getName());

                SignReportDocFileVo vo2 = new SignReportDocFileVo();
                vo2.setValueFirst("身份证号：");
                String organizationNo = authLog.getOrganizationNo();
                try {
                    organizationNo = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, organizationNo);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                vo2.setValueSecond(DesensitizationUtils.getIdNumber(organizationNo));

                SignReportDocFileVo vo3 = new SignReportDocFileVo();
                vo3.setValueFirst("手机号：");
                String phone = authLog.getPhone();
                try {
                    phone = RsaUtils.decryptByPrivateKey(SignCommonConstant.PRIVATEKEYSTRING, phone);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                vo3.setValueSecond(DesensitizationUtils.getPhone(phone));

                SignReportDocFileVo vo4 = new SignReportDocFileVo();
                vo4.setValueFirst("认证方式：");

                if(authLog.getAuthMethod() == null || authLog.getAuthMethod() == 0){
                    vo4.setValueSecond("手机号三要素");
                }else if(authLog.getAuthMethod() == 2) {
                    vo4.setValueSecond("人脸识别");
                }
                SignReportDocFileVo vo5 = new SignReportDocFileVo();
                vo5.setValueFirst("手机验证码：");
                vo5.setValueSecond(authLog.getVerifyInfo());

                subReport.add(vo1);
                subReport.add(vo2);
                subReport.add(vo3);
                subReport.add(vo4);
                subReport.add(vo5);
            }


            SignReportDocFileVo vo11 = new SignReportDocFileVo();
            vo11.setValueFirst("认证提交时间：");
            vo11.setValueSecond(DateUtil.formatDate(authLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));

            SignReportDocFileVo vo12 = new SignReportDocFileVo();
            vo12.setValueFirst("认证IP地址：");
            vo12.setValueSecond(authLog.getSubmitIp());

            subReport.add(vo11);
            subReport.add(vo12);

            if (authLog.getCheckTime() != null) {
                SignReportDocFileVo vo13 = new SignReportDocFileVo();
                vo13.setValueFirst("认证通过时间：");
                vo13.setValueSecond(DateUtil.formatDate(authLog.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
                subReport.add(vo13);
            }

            signSubVO.setSubReport(subReport);

            auths.add(signSubVO);
        }
        return auths;
    }

    @Override
    public List<TenantUser3rd> getByUsernameAndTenantName(String username, String tenantName) {
        return mapper.getByUsernameAndTenantName(username, tenantName);
    }

    @Override
    public List<SysAppVersion> getTenantAppVersions(SysTenantAppVersion sysTenantAppVersion) {
        return null;
    }

    @Override
    public void updateTenantAppVersion(SysTenantAppVersion sysTenantAppVersion) {

    }

    @Override
    public List<SysAppInfoVO> getAllApps(String tenantId) {
        SysTenantInfo sysTenantInfo = this.getById(tenantId);
        List<Integer> types = new ArrayList<>();
        types.add(AppAndVersionType.ALL.getType());
        types.add(sysTenantInfo.getTenantType());
        List<SysAppInfoVO> sysAppInfoVOS = sysAppInfoService.listAllAppsByTypes(types, null);
        if (CollUtil.isNotEmpty(sysAppInfoVOS)) {
            List<SysTenantAppVersion> tenantAppVersions = iSysTenantAppVersionService.getByTenantId(tenantId);
            if (CollUtil.isNotEmpty(tenantAppVersions)) {
                List<String> appIds = tenantAppVersions.stream().map(SysTenantAppVersion::getAppId).collect(Collectors.toList());
                sysAppInfoVOS.forEach(a -> {
                    if (appIds.contains(a.getId())) {
                        a.setContainsFlag(true);
                    }
                });
            }
        }

        return sysAppInfoVOS;
    }

    //团体租户新增应用
    @Override
    public void addGroupTenantAppVersion(String pId, String allGroupId, String baseGroupId, SysTenantInfoVO sysTenantInfoVO) {
        String tenantId = sysTenantInfoVO.getId();
        List versionIds = sysTenantInfoVO.getOutVersionIds();
        if (MyStringUtils.isNotBlank(tenantId) && CollUtil.isNotEmpty(versionIds)) {
            SysTenantInfo sysTenantInfo = this.getById(tenantId);
            if (!AppAndVersionType.GROUP.getType().equals(sysTenantInfo.getTenantType())) {
                throw new PaasException("团体租户才可以添加应用");
            }
            //用户已有应用、判重
            List<SysTenantAppVersion> tenantAppVersions = iSysTenantAppVersionService.getByTenantId(tenantId);
            List<String> appIds = new ArrayList<>();
            if (CollUtil.isNotEmpty(tenantAppVersions)) {
                appIds = tenantAppVersions.stream().map(SysTenantAppVersion::getAppId).collect(Collectors.toList());
            }
            List<SysAppVersion> appVersions = iSysAppVersionService.getByIds(versionIds);
            if (CollUtil.isNotEmpty(appVersions)) {
                for (SysAppVersion v : appVersions) {
                    if (!appIds.contains(v.getAppInfoId())) {
                        if (AppAndVersionType.GROUP.getType().equals(v.getVersionType()) || AppAndVersionType.ALL.getType().equals(v.getVersionType())) {
                            //新增租户-应用
                            SysAppInfo appInfo = sysAppInfoService.getById(v.getAppInfoId());
                            addTenantApp(tenantId, v.getAppInfoId(), v.getId(), appInfo.getUseful());
                            //所有权限的权限组初始化
                            addGroupPermisson(allGroupId, v.getId());
                            //基础权限的权限组初始化
                            addBaseGroupPermisson(baseGroupId, v.getId());
                            //初始化版本对应的权限组
                            if (v.isDefaultFlag()) {
                                addAppGroupPermission(tenantId, pId, v.getId());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public SysTenantInfo getPersonalByTenantUserId(String tenantUserId) {
        return mapper.getPersonalByTenantUserId(tenantUserId);
    }

    @Override
    public void addPersonalTenantAppVersion(SysTenantInfoVO sysTenantInfoVO) {
        String tenantId = sysTenantInfoVO.getId();
        if (MyStringUtils.isBlank(tenantId)) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            tenantId = loginUser.getTenantId();
        }
        List versionIds = sysTenantInfoVO.getOutVersionIds();
        if (MyStringUtils.isNotBlank(tenantId) && CollUtil.isNotEmpty(versionIds)) {
            SysTenantInfo sysTenantInfo = this.getById(tenantId);
            if (!AppAndVersionType.PERSONAL.getType().equals(sysTenantInfo.getTenantType())) {
                throw new PaasException("个人租户才可以添加应用");
            }
            //用户已有应用、判重
            List<SysTenantAppVersion> tenantAppVersions = iSysTenantAppVersionService.getByTenantId(tenantId);
            List<String> appIds = new ArrayList<>();
            if (CollUtil.isNotEmpty(tenantAppVersions)) {
                appIds = tenantAppVersions.stream().map(SysTenantAppVersion::getAppId).collect(Collectors.toList());
            }
            List<SysAppVersion> appVersions = iSysAppVersionService.getByIds(versionIds);
            if (CollUtil.isNotEmpty(appVersions)) {
                for (SysAppVersion v : appVersions) {
                    if (!appIds.contains(v.getAppInfoId())) {
                        if (AppAndVersionType.PERSONAL.getType().equals(v.getVersionType()) || AppAndVersionType.ALL.getType().equals(v.getVersionType())) {
                            //新增租户-应用
                            addTenantApp(tenantId, v.getAppInfoId(), v.getId(), true);

                            //初始化权限--暂时个人租户应用全部分配权限
                            if (v.isDefaultFlag() || true) {
                                //新增权限组
                                String pId = addGroup(tenantId, null, "默认权限组", "默认权限组", true, null);
                                String groupId = addGroup(tenantId, pId, "默认权限", "默认权限", true, 1);
                                //查找个人租户下的唯一用户
                                SysTenantUser sysTenantUser = iSysTenantUserService.getTenantUser(tenantId, null);
                                if (sysTenantUser != null) {
                                    //维护权限组-用户
                                    SysAuthGroupMemberList2 memberList = new SysAuthGroupMemberList2();
                                    memberList.setAuthId(sysTenantUser.getUserId());
                                    memberList.setAuthType(AuthType.USER.getType());
                                    memberList.setTenantId(tenantId);
                                    List<String> groups = new ArrayList<>();
                                    groups.add(groupId);
                                    memberList.setGroupIds(groups);
                                    iSysAuthGroupMemberService.saveExt2(memberList);
                                } else {
                                    throw new PaasException("失败");
                                }

                                //新增权限组-应用
                                addGroupPermisson(groupId, v.getId());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void addEnterpriseTenantAppVersion(String tenantId,String userId,List<String> versionIdList) {
        //新增租户应用-权限组  租户唯一
        String pId = null ;
        List<SysAuthGroup> pIdSysAuthGroups = iSysAuthGroupService.listByEntity(new SysAuthGroup().setTenantId(tenantId).setGroupType(AuthGroupType.GROUP.getType()).setSystemFlag(true));
        if(pIdSysAuthGroups != null && pIdSysAuthGroups.size() > 0){
            pId = pIdSysAuthGroups.get(0).getId();
        }
        if(pId == null){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"未初始化租户权限组");
        }
        List<String> allGroupIds = null ;
        List<SysAuthGroup> allGroupIdSysAuthGroups = iSysAuthGroupService.listByEntity(new SysAuthGroup().setTenantId(tenantId).setGroupType(AuthGroupType.ALL.getType()).setSystemFlag(true).setParentId(pId));
        if(allGroupIdSysAuthGroups != null && allGroupIdSysAuthGroups.size() > 0){
            allGroupIds = allGroupIdSysAuthGroups.stream().map(SysAuthGroup::getId).collect(Collectors.toList());
        }
        if(allGroupIds == null || allGroupIds.size() == 0){
            throw new RequestParamsException(ApiCode.PARAM_MISSING,"未初始化租户权限组");
        }
        //维护权限组-用户
        SysAuthGroupMemberList2 memberList = new SysAuthGroupMemberList2();
        memberList.setAuthId(userId);
        memberList.setAuthType(AuthType.USER.getType());
        memberList.setTenantId(tenantId);
        List<String> groups = new ArrayList<>();
        groups.addAll(allGroupIds);
        memberList.setGroupIds(groups);
        iSysAuthGroupMemberService.saveExt2(memberList);
        List<SysAppVersion> appVersions = checkAndGetAppVersions(null,AppAndVersionType.GROUP.getType() );
        //处理租户内置应用
        if (CollUtil.isNotEmpty(appVersions)) {
            for (SysAppVersion v : appVersions) {
                //新增租户-应用
                QueryWrapper<SysTenantUserApp> wrapper = new QueryWrapper<>();
                wrapper.lambda().eq(SysTenantUserApp::getAppId,v.getAppInfoId());
                wrapper.lambda().eq(SysTenantUserApp::getAppVersionId,v.getId());
                wrapper.lambda().eq(SysTenantUserApp::getTenantId,tenantId);
                wrapper.lambda().eq(SysTenantUserApp::getUserId,userId);
                List<SysTenantUserApp> list = iSysTenantUserAppService.list(wrapper);
                if(list == null || list.size() == 0){
                    //新增租户-用户-应用
                    SysTenantUserApp sysTenantUserApp = new SysTenantUserApp();
                    sysTenantUserApp.setTenantId(tenantId);
                    sysTenantUserApp.setUserId(userId);
                    sysTenantUserApp.setAppId(v.getAppInfoId());
                    sysTenantUserApp.setAppVersionId(v.getId());
                    iSysTenantUserAppService.saveExt(sysTenantUserApp);
                }
            }
        }
    }

    @Override
    public List<SysTenantInfo> getByIds(List<String> ids) {
        LambdaQueryWrapper<SysTenantInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysTenantInfo::getId, ids).eq(SysTenantInfo::getDeleteFlag, 0);

        return this.list(queryWrapper);
    }

    @Override
    public TenantType getByTenantId(String tenantId) {
        SysTenantInfo sysTenantInfo = this.getById(tenantId);
        if (sysTenantInfo != null) {
            return TenantType.getByType(sysTenantInfo.getTenantType());
        }
        return TenantType.UNKNOWN;
    }
}

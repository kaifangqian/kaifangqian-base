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
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.storage.StorageService;
import com.kaifangqian.modules.storage.entity.AnnexStorage;
import com.kaifangqian.modules.storage.service.IAnnexStorageService;
import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.modules.system.entity.SysAppVersion;
import com.kaifangqian.modules.system.enums.AppAndVersionStatus;
import com.kaifangqian.modules.system.enums.AppAndVersionType;
import com.kaifangqian.modules.system.mapper.SysAppInfoMapper;
import com.kaifangqian.modules.system.util.ImageUtils;
import com.kaifangqian.modules.system.vo.AppInfoVO;
import com.kaifangqian.modules.system.vo.SysAppInfoVO;
import com.kaifangqian.modules.system.vo.SysAppVersionVO;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.Base64Utils;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import com.kaifangqian.modules.system.service.ISysAppVersionService;
import com.kaifangqian.utils.IOUtils;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author zhenghuihan
 * @description 系统应用服务
 * @createTime 2022/9/2 17:40
 */
@Service
public class SysAppInfoServiceImpl extends ServiceImpl<SysAppInfoMapper, SysAppInfo> implements ISysAppInfoService {

    @Resource
    private SysAppInfoMapper mapper;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IAnnexStorageService iAnnexStorageService;
    @Autowired
    private ISysAppVersionService iSysAppVersionService;


    @Override
    public IPage<SysAppInfo> pageExt(Page<SysAppInfo> page, QueryWrapper<SysAppInfo> queryWrapper) {
        IPage<SysAppInfo> result = page(page, queryWrapper);
        List<SysAppInfo> list = result.getRecords();
        if (CollUtil.isNotEmpty(list)) {
            List<String> appIds = list.stream().map(SysAppInfo::getId).collect(Collectors.toList());
            List<SysAppVersion> appVersions = iSysAppVersionService.getByAppIds(appIds, null, null);
            if (CollUtil.isNotEmpty(appVersions)) {
                Map<String, List<SysAppVersion>> appVersionMap = appVersions.stream().collect(Collectors.groupingBy(SysAppVersion::getAppInfoId));
                list.forEach(a -> {
                    a.setVersionList(appVersionMap.get(a.getId()));
                });
            }
        }
        return result;
    }

    @Override
    public SysAppInfo getByAppCode(String appCode) {
        LambdaQueryWrapper<SysAppInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAppInfo::getAppCode, appCode);
        queryWrapper.eq(SysAppInfo::getDeleteFlag, 0);

        return mapper.selectOne(queryWrapper);
    }

    @Override
    public void saveExt(SysAppInfo sysAppInfo) {
        checkDate(sysAppInfo);
        sysAppInfo.setAppStatus(AppAndVersionStatus.DRAFT.getType());

        if (MyStringUtils.isNotBlank(sysAppInfo.getAppIconAddress())) {
            String base64 = getBase64ByFileId(sysAppInfo.getAppIconAddress());

            sysAppInfo.setAppIcon(base64);
        }

        this.save(sysAppInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(SysAppInfo sysAppInfo) {
        if (MyStringUtils.isNotBlank(sysAppInfo.getId())) {
            SysAppInfo appInfo = this.getById(sysAppInfo.getId());
            if (appInfo != null) {
                if (AppAndVersionStatus.DRAFT.getType().equals(appInfo.getAppStatus())) {
                    appInfo.setAppStatus(AppAndVersionStatus.PUBLISHED.getType());
                    if (CollUtil.isNotEmpty(sysAppInfo.getVersionList())) {
                        sysAppInfo.getVersionList().forEach(v -> {
                            if (MyStringUtils.isNotBlank(v.getId())) {
                                SysAppVersion appVersion = new SysAppVersion();
                                appVersion.setId(v.getId());
                                appVersion.setVersionStatus(AppAndVersionStatus.PUBLISHED.getType());

                                iSysAppVersionService.updateById(appVersion);
                            } else {
                                throw new PaasException("版本ID不能为空");
                            }
                        });
                    } else {
                        throw new PaasException("版本不能为空");
                    }
                } else if (AppAndVersionStatus.PUBLISHED.getType().equals(appInfo.getAppStatus())) {
                    appInfo.setAppStatus(AppAndVersionStatus.UNPUBLISHED.getType());
                } else if (AppAndVersionStatus.UNPUBLISHED.getType().equals(appInfo.getAppStatus())) {
                    appInfo.setAppStatus(AppAndVersionStatus.PUBLISHED.getType());
                }

                this.updateById(appInfo);
            } else {
                throw new PaasException("传值错误");
            }
        } else {
            throw new PaasException("应用ID不能为空");
        }
    }

    @Override
    public void updateExt(SysAppInfo sysAppInfo) {
        checkDate(sysAppInfo);
        sysAppInfo.setAppStatus(null);
        SysAppInfo appInfo = getById(sysAppInfo.getId());
        if (MyStringUtils.isNotBlank(sysAppInfo.getAppIconAddress())) {
            if (!sysAppInfo.getAppIconAddress().equals(appInfo.getAppIconAddress())) {
                String base64 = getBase64ByFileId(sysAppInfo.getAppIconAddress());

                sysAppInfo.setAppIcon(base64);
            }
        } else {
            sysAppInfo.setAppIcon("");
        }
        if (!AppAndVersionStatus.DRAFT.getType().equals(appInfo.getAppStatus())) {
            sysAppInfo.setDefaultFlag(null);
            if (sysAppInfo.getAppType() != null) {
                if (AppAndVersionType.ALL.getType().equals(appInfo.getAppType())) {
                    sysAppInfo.setAppType(null);
                } else if (!AppAndVersionType.ALL.getType().equals(sysAppInfo.getAppType())) {
                    sysAppInfo.setAppType(null);
                }
            }
        }

        this.updateById(sysAppInfo);
    }

    void checkDate(SysAppInfo sysAppInfo) {
        if (MyStringUtils.isBlank(sysAppInfo.getAppCode())) {
            throw new PaasException("appCode不能为空");
        }
        SysAppInfo appInfo = getByAppCode(sysAppInfo.getAppCode());
        if (appInfo != null) {
            if (MyStringUtils.isBlank(sysAppInfo.getId())) {
                throw new PaasException("数据不满足唯一性要求");
            } else {
                if (!appInfo.getId().equals(sysAppInfo.getId())) {
                    throw new PaasException("数据不满足唯一性要求");
                }
            }
        }
    }

    @Override
    public void deleteExt(String id) {
        SysAppInfo sysAppInfo = this.getById(id);
        if (sysAppInfo != null) {
            if (!AppAndVersionStatus.DRAFT.getType().equals(sysAppInfo.getAppStatus())) {
                throw new PaasException("非草稿状态的不可以删除");
            }
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            sysAppInfo.setDeleteFlag(true);
            sysAppInfo.setDeleteTime(new Date());
            sysAppInfo.setDeleteBy(loginUser.getId());

            this.updateById(sysAppInfo);
        }
    }

    @Override
    public void deleteBatchExt(List<String> ids) {
        List<SysAppInfo> appInfos = new ArrayList<>();
        LoginUser loginUser = MySecurityUtils.getCurrentUser();
        ids.forEach(i -> {
            SysAppInfo sysAppInfo = new SysAppInfo();
            sysAppInfo.setId(i);
            sysAppInfo.setDeleteFlag(true);
            sysAppInfo.setDeleteTime(new Date());
            sysAppInfo.setDeleteBy(loginUser.getId());

            appInfos.add(sysAppInfo);
        });
        //1.删除APP
        if (CollUtil.isNotEmpty(appInfos)) {
            this.updateBatchById(appInfos);
        }

        //todo 是否全部清除？？
        //2.删除APP-版本

        //3.删除版本-权限

        //4.删除租户-app

        //5.删除用户-app

        //6.角色

        //7.权限组-权限
    }

    @Override
    public List<SysAppInfoVO> listAllAppsByTypes(List<Integer> types, Boolean defaultFlag) {
        List<SysAppInfoVO> result = new ArrayList<>();
        LambdaQueryWrapper<SysAppInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysAppInfo::getAppType, types).eq(SysAppInfo::getDeleteFlag, 0).ne(SysAppInfo::getAppStatus, AppAndVersionStatus.DRAFT.getType()).eq(defaultFlag != null, SysAppInfo::getDefaultFlag, defaultFlag);
        List<SysAppInfo> list = this.list(queryWrapper);

        if (CollUtil.isNotEmpty(list)) {
            List<String> appIds = list.stream().map(SysAppInfo::getId).collect(Collectors.toList());
            List<SysAppVersion> appVersions = iSysAppVersionService.getByAppIds(appIds, types, AppAndVersionStatus.PUBLISHED.getType());
            if (CollUtil.isNotEmpty(appVersions)) {
                Map<String, List<SysAppVersion>> appVersionMap = appVersions.stream().collect(Collectors.groupingBy(SysAppVersion::getAppInfoId));
                list.forEach(a -> {
                    SysAppInfoVO vo = new SysAppInfoVO();
                    BeanUtils.copyProperties(a, vo);

                    List<SysAppVersion> child = appVersionMap.get(a.getId());
                    if (CollUtil.isNotEmpty(child)) {
                        List<SysAppVersionVO> vos = new ArrayList<>();
                        child.forEach(c -> {
                            SysAppVersionVO versionVO = new SysAppVersionVO();
                            BeanUtils.copyProperties(c, versionVO);

                            vos.add(versionVO);
                        });
                        vo.setAppVersionVOS(vos);
                    }
                    result.add(vo);
                });
            }
        }
        return result;
    }

    @Override
    public String getBase64ByFileId(String fileId) {
        AnnexStorage annexStorage = iAnnexStorageService.getById(fileId);
        if (annexStorage == null) {
            return null;
        }
        String filePath = annexStorage.getPath();
        //创建临时文件
        String base64 = null;
        String base64Pre = null;
        File fileTem = null;
        InputStream inputStream = null;
        OSS ossClient = null;
        try {
            String suffix = annexStorage.getSuffix();
            base64Pre = Base64Utils.getBase64Pre(suffix);
            fileTem = File.createTempFile(UUID.randomUUID().toString(), "." + suffix);
            ossClient = storageService.getOSSClient();
            inputStream = storageService.loadAsInputStream(filePath, ossClient);
            ImageUtils.compressImg(inputStream, fileTem, 80, 80);
            byte[] bytes = IOUtils.toByteArray(fileTem);
            base64 = Base64.getEncoder().encodeToString(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileTem.exists()) {
                fileTem.delete();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return base64 == null ? null : base64Pre + base64.replaceAll("\n", "").replaceAll("\r", "");
    }

    @Override
    public List<AppInfoVO> getAll() {
        List<AppInfoVO> result = new ArrayList<>();
        List<SysAppInfo> list = this.list();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(a -> {
                AppInfoVO vo = new AppInfoVO();
                vo.setAppId(a.getId());
                vo.setAppCode(a.getAppCode());
                vo.setUrl(a.getAppAddress());
                result.add(vo);
            });
        }
        return result;
    }
}

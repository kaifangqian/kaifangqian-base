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
package com.kaifangqian.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
import com.kaifangqian.modules.system.entity.SysAppVersion;
import com.kaifangqian.modules.system.entity.SysTenantAppVersion;
import com.kaifangqian.modules.system.entity.SysTenantInfo;
import com.kaifangqian.modules.system.enums.TenantAppStatus;
import com.kaifangqian.modules.system.enums.TenantType;
import com.kaifangqian.modules.system.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.system.vo.*;

import java.util.List;
import java.util.Set;
/**
 * @author zhenghuihan
 * @description 租户管理服务类
 * @createTime 2022/9/2 18:13
 */
public interface ISysTenantInfoService extends IService<SysTenantInfo> {

    IPage<SysTenantInfoRes> pageExt(Page<SysTenantInfoRes> page, SysTenantInfoQuery query);

    SysAddTenantInfoVO addTenantAndAddApp(SysTenantInfoVO sysTenantInfoVO);

    void updateTenantStatus(String id);

    List<SysTenantAppVersionVO> getTenantApps(String id);

    void updateTenantAppStatus(String id);

    List<SysAppVersion> getTenantAppVersions(SysTenantAppVersion sysTenantAppVersion);

    void updateTenantAppVersion(SysTenantAppVersion sysTenantAppVersion);

    List<SysAppInfoVO> getAllApps(String tenantId);

    void addGroupTenantAppVersion(String pId, String allGroupId, String baseGroupId, SysTenantInfoVO sysTenantInfoVO);

    void addPersonalTenantAppVersion(SysTenantInfoVO sysTenantInfoVO);

    void addEnterpriseTenantAppVersion(String tenantId,String userId,List<String> versionIdList);

    List<SysTenantInfo> getByIds(List<String> ids);

    SysTenantInfo getByInvitationCode(String code);

    void personalTenantRegister(UserJionSystemVO userJionSystemVO);

    //签章系统使用
    void updateExt(SysTenantInfo sysTenantInfo);

    TenantType getByTenantId(String tenantId);

    void updateTenantAppStatus(String tenantId, String appId, TenantAppStatus appStatus);

    SysTenantInfo getByName(String tenantName);


    /**
     * 根据租户ID查询认证记录
     *
     * @param tenantIds
     * @return
     */
    List<SignReportSignSubVO> queryTenantAuthLog(Set<String> tenantIds);

    List<TenantUser3rd> getByUsernameAndTenantName(String username, String tenantName);

    SysTenantInfo getPersonalByTenantUserId(String tenantUserId);
}

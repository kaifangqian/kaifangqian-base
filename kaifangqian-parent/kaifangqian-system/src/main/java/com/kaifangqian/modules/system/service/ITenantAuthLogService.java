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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.TenantAuthLog;
import com.kaifangqian.modules.system.vo.TenantAuthLogVO;
import com.kaifangqian.modules.system.vo.request.TenantAuthAuditRequest;
import com.kaifangqian.modules.system.vo.request.TenantAuthRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 租户扩展信息历史记录表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2023-10-10
 */
public interface ITenantAuthLogService extends IService<TenantAuthLog> {


    Page<TenantAuthLogVO> getTenantAuthLog(Page<TenantAuthLog> page, TenantAuthRequest authRequest);

    boolean authAudit(TenantAuthAuditRequest authRequest, String checkUser) throws Exception;


    TenantAuthLogVO getAuthLogById(String logId);

    TenantAuthLog getAuthLogByOrderNo(String tenantId,String orderNo);

    TenantAuthLog getAuthLogByNoAuth(String tenantId);

    TenantAuthLog getAuthLogByNoAuthUpdate(String tenantId);
}

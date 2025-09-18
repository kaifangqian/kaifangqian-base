/**
 * Discription:租户信息扩展Mapper
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
package com.kaifangqian.modules.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kaifangqian.modules.system.entity.TenantInfoExtend;
import com.kaifangqian.modules.system.vo.SysTenantInfoExtendVO;
import com.kaifangqian.modules.system.vo.TenantInfoDTO;
import com.kaifangqian.modules.system.vo.TenantQueryDTO;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface TenantInfoExtendMapper extends BaseMapper<TenantInfoExtend> {
    IPage<TenantInfoDTO> pageExt(Page<TenantInfoDTO> page, @Param("req") TenantQueryDTO req);

    List<SysTenantInfoExtendVO> listMyTenant(@Param("userId") String userId);
}

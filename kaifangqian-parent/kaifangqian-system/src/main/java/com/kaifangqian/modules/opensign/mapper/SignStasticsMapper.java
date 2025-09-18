/**
 * @description 印章信息表Mapper
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
package com.kaifangqian.modules.opensign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.dto.SealAuditDto;
import com.kaifangqian.modules.opensign.dto.UseSealDetailDto;
import com.kaifangqian.modules.opensign.entity.SignEntSeal;
import com.kaifangqian.modules.opensign.vo.base.SealAuditVo;
import com.kaifangqian.modules.opensign.vo.base.UseSealDetailVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description: SignStasticsMapper
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignStasticsMapper
 * @author: Fusion
 * CreateTime:  2023/8/22  9:53
 * @copyright 北京资源律动科技有限公司
 */

public interface SignStasticsMapper extends BaseMapper<SignEntSeal> {

    //印章统计相关统计
    Integer getUseCount(@Param("tenantId") String tenantId);

    Integer getStopCount(@Param("tenantId") String tenantId);

    Integer getCollectCount(@Param("tenantId") String tenantId);

    Integer getDestructionCount(@Param("tenantId") String tenantId);

    //用印统计相关统计
    Integer getElectronicUseSealCount(@Param("tenantId") String tenantId);

    Integer getPhysicsUseSealCount(@Param("tenantId") String tenantId);

    Integer getInterfaceUseSealCount(@Param("tenantId") String tenantId);


    IPage<UseSealDetailVo> getUseSealDetailList(Page page, @Param("req") UseSealDetailDto useSealDetailDto);

    List<UseSealDetailVo> getUseSealDetailList(@Param("req") UseSealDetailDto useSealDetailDto);

    IPage<SealAuditVo> getSealAuditList(Page page, @Param("req") SealAuditDto sealAuditDto);

    List<SealAuditVo> getSealAuditList(@Param("req") SealAuditDto sealAuditDto);

}

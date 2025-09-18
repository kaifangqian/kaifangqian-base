/**
 * @description 统计分析服务接口
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
package com.kaifangqian.modules.opensign.service.statistics;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.dto.SealAuditDto;
import com.kaifangqian.modules.opensign.dto.UseSealDetailDto;
import com.kaifangqian.modules.opensign.vo.base.SealAuditVo;
import com.kaifangqian.modules.opensign.vo.base.SealStatisticsVo;
import com.kaifangqian.modules.opensign.vo.base.UseSealDetailVo;
import com.kaifangqian.modules.opensign.vo.base.UseSealStatisticsVo;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 统计分析服务接口
 * @Package: com.kaifangqian.modules.opensign.service.cert
 * @ClassName: SignStatisticsServiceImpl
 * @author: Fusion
 * CreateTime:  2023/8/18  10:53
 * @copyright 北京资源律动科技有限公司
 */
public interface SignStatisticsService{
    /**
     * 印章统计
     */
    SealStatisticsVo sealStatistics();

    /**
     * 用印统计
     */
    UseSealStatisticsVo useSealStatistics();

    /**
     * 用印明细-分页
     */
    IPage<UseSealDetailVo> useSealDetailList(Page<UseSealDetailVo> page, UseSealDetailDto useSealDetailDto);

    /**
     * 印章审计
     */
    IPage<SealAuditVo> sealAuditList(Page<SealAuditVo> page, SealAuditDto sealAuditDto);

    /**
     * 导出用印统计报表
     */
    void exportUseSealStatistic(HttpServletResponse response,UseSealDetailDto useSealDetailDto);

    /**
     * 导出用印审计报表
     */
    void exportSealAuditStatistic(HttpServletResponse response,SealAuditDto sealAuditDto);

    /**
     * 用印明细-列表
     */
    List<UseSealDetailVo> useSealDetailList(UseSealDetailDto useSealDetailDto);
}

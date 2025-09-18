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
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysTenantConfig;
import com.kaifangqian.modules.system.mapper.SysTenantConfigMapper;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.system.service.ISysTenantConfigService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author zhenghuihan
 * @description 租户系统配置服务类
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTenantConfigServiceImpl extends ServiceImpl<SysTenantConfigMapper, SysTenantConfig> implements ISysTenantConfigService {

    @Override
    public SysTenantConfig getConfigByType(String tenantId, String type) {
        if (MyStringUtils.isBlank(tenantId)) {
            tenantId = MySecurityUtils.getCurrentUser().getTenantId();
        }
        LambdaQueryWrapper<SysTenantConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenantConfig::getTenantId, tenantId).eq(SysTenantConfig::getType, type);

        List<SysTenantConfig> list = list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            SysTenantConfig tem = new SysTenantConfig();
            tem.setTenantId(tenantId);
            tem.setName(type);
            tem.setType(type);
            tem.setValue("false");

            save(tem);
            return tem;
        }
        return list.get(0);
    }
}

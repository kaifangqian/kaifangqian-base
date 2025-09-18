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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysTextConfig;
import com.kaifangqian.modules.system.mapper.SysTextConfigMapper;
import com.kaifangqian.modules.system.service.ISysTextConfigService;
import org.springframework.stereotype.Service;
/**
 * @author zhenghuihan
 * @description 系统服务协议配置服务
 * @createTime 2022/9/2 18:13
 */
@Service
public class SysTextConfigServiceImpl extends ServiceImpl<SysTextConfigMapper, SysTextConfig> implements ISysTextConfigService {


    @Override
    public SysTextConfig getByType(String type) {
        LambdaQueryWrapper<SysTextConfig> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysTextConfig::getType, type);

        return this.getOne(queryWrapper);
    }
}

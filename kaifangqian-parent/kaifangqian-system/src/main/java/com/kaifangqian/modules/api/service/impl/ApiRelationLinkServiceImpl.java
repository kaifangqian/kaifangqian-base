/**
 * @description API关系链服务类类
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
package com.kaifangqian.modules.api.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.api.entity.ApiRelationLink;
import com.kaifangqian.modules.api.mapper.ApiRelationLinkMapper;
import com.kaifangqian.modules.api.service.IApiRelationLinkService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:05
 */
@Service
public class ApiRelationLinkServiceImpl extends ServiceImpl<ApiRelationLinkMapper, ApiRelationLink> implements IApiRelationLinkService {

    @Override
    public String getSystemIdByExAccount(String token, String type, String exAccount) {
        LambdaQueryWrapper<ApiRelationLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiRelationLink::getToken, token).eq(ApiRelationLink::getType, type).eq(ApiRelationLink::getExternalAccount, exAccount);

        List<ApiRelationLink> list = super.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0).getSystemId();
        }
        return null;
    }

    @Override
    public void removeByExAccount(String token, String type, String exAccount) {
        LambdaQueryWrapper<ApiRelationLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiRelationLink::getToken, token).eq(ApiRelationLink::getType, type).eq(ApiRelationLink::getExternalAccount, exAccount);

        this.remove(queryWrapper);
    }

    @Override
    public void removeBySystemIds(String type, List<String> systemIds) {
        LambdaQueryWrapper<ApiRelationLink> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiRelationLink::getType, type)
                .in(ApiRelationLink::getExternalAccount, systemIds);

        this.remove(queryWrapper);
    }
}

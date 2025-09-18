/**
 * @description 获取签署文档操作关联人接口实现类
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
package com.kaifangqian.modules.opensign.service.ru.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignRuRelation;
import com.kaifangqian.modules.opensign.mapper.SignRuRelationMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuRelationService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuRelationServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuRelationServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuRelationServiceImpl extends ServiceImpl<SignRuRelationMapper, SignRuRelation> implements SignRuRelationService {

    @Override
    public List<SignRuRelation> getNoUserList(SignRuRelation query) {
        LambdaQueryWrapper<SignRuRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignRuRelation::getExternalCcedType, query.getExternalCcedType())
                .eq(SignRuRelation::getExternalCcedValue, query.getExternalCcedValue())
                .eq(SignRuRelation::getDeleteFlag, false)
                .isNull(SignRuRelation::getTenantUserId);

        return list(queryWrapper);
    }

    @Override
    public List<SignRuRelation> getByEntity(SignRuRelation query) {
        LambdaQueryWrapper<SignRuRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getSignRuId()), SignRuRelation::getSignRuId, query.getSignRuId())
                .eq(MyStringUtils.isNotBlank(query.getTenantUserId()), SignRuRelation::getTenantUserId, query.getTenantUserId())
                .eq(query.getRelationType() != null, SignRuRelation::getRelationType, query.getRelationType())
                .eq(MyStringUtils.isNotBlank(query.getExternalCcedValue()), SignRuRelation::getExternalCcedValue, query.getExternalCcedValue())
                .eq(SignRuRelation::getDeleteFlag, false);

        return list(queryWrapper);
    }
}
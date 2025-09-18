/**
 * @description 获取签署文档操作人接口实现类
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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignRuOperator;
import com.kaifangqian.modules.opensign.mapper.SignRuOperatorMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuOperatorService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuOperatorServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuOperatorServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuOperatorServiceImpl extends ServiceImpl<SignRuOperatorMapper, SignRuOperator> implements SignRuOperatorService {
    @Override
    public List<SignRuOperator> listByRuId(String ruId) {
        QueryWrapper<SignRuOperator> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuOperator::getSignRuId, ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);

        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuOperator> getByEntity(SignRuOperator query) {
        LambdaQueryWrapper<SignRuOperator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getSignRuId()), SignRuOperator::getSignRuId, query.getSignRuId())
                .eq(query.getOperateType() != null, SignRuOperator::getOperateType, query.getOperateType())
                .eq(query.getSignerType() != null, SignRuOperator::getSignerType, query.getSignerType())
                .eq(query.getOperateStatus() != null, SignRuOperator::getOperateStatus, query.getOperateStatus())
                .eq(MyStringUtils.isNotBlank(query.getSignerId()), SignRuOperator::getSignerId, query.getSignerId())
                .in(CollUtil.isNotEmpty(query.getSignerTypes()), SignRuOperator::getSignerType, query.getSignerTypes())
                .eq(SignRuOperator::getDeleteFlag, false);

        queryWrapper.orderByAsc(SignRuOperator::getOperateOrder);

        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByRuId(String ruId) {
        QueryWrapper<SignRuOperator> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuOperator::getSignRuId, ruId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);

        this.baseMapper.delete(wrapper);
    }
}
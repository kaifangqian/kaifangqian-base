/**
 * @description 获取签署文档操作发送人接口实现类
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignRuSender;
import com.kaifangqian.modules.opensign.mapper.SignRuSenderMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuSenderService;
import com.kaifangqian.utils.MyStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuSenderServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuSenderServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuSenderServiceImpl extends ServiceImpl<SignRuSenderMapper, SignRuSender> implements SignRuSenderService {
    @Override
    public List<SignRuSender> listBySignerId(String signerId) {
        QueryWrapper<SignRuSender> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuSender::getSignerId, signerId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag, false);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public List<SignRuSender> getByEntity(SignRuSender query) {
        LambdaQueryWrapper<SignRuSender> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MyStringUtils.isNotBlank(query.getSignerId()), SignRuSender::getSignerId, query.getSignerId())
                .eq(SignRuSender::getDeleteFlag, false);

        queryWrapper.orderByAsc(SignRuSender::getSenderOrder);

        return list(queryWrapper);
    }
}
/**
 * @description 获取签署文档清单接口实现类
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignRuOperateDocRecord;
import com.kaifangqian.modules.opensign.mapper.SignRuOperateDocRecordMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuOperateDocRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuOperateDocRecordServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuOperateDocRecordServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuOperateDocRecordServiceImpl extends ServiceImpl<SignRuOperateDocRecordMapper,SignRuOperateDocRecord> implements SignRuOperateDocRecordService {

    @Override
    public List<SignRuOperateDocRecord> listByOperateRecordId(String operateRecordId) {

        QueryWrapper<SignRuOperateDocRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuOperateDocRecord::getOperateRecordId,operateRecordId);
        return this.baseMapper.selectList(wrapper);
    }


}
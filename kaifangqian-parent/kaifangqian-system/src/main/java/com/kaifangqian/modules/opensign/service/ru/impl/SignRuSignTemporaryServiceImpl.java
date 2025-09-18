/**
 * @description 签署业务签署临时数据管理接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignRuSignTemporary;
import com.kaifangqian.modules.opensign.mapper.SignRuSignTemporaryMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuSignTemporaryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuSignTemporaryServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuSignTemporaryServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuSignTemporaryServiceImpl extends ServiceImpl<SignRuSignTemporaryMapper, SignRuSignTemporary> implements SignRuSignTemporaryService {

    @Override
    public SignRuSignTemporary getByOrderNo(String orderNo) {
        QueryWrapper<SignRuSignTemporary> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuSignTemporary::getSignConfirmOrderNo,orderNo);

        List<SignRuSignTemporary> temporaryList = this.baseMapper.selectList(wrapper);
        if(temporaryList != null && temporaryList.size() > 0){
            return temporaryList.get(0);
        }
        return null;
    }

    @Override
    public SignRuSignTemporary getByParam(String orderNo, String ruId, String taskId) {
        QueryWrapper<SignRuSignTemporary> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuSignTemporary::getSignConfirmOrderNo,orderNo);
        wrapper.lambda().eq(SignRuSignTemporary::getSignRuId,ruId);
        wrapper.lambda().eq(SignRuSignTemporary::getTaskId,taskId);
        List<SignRuSignTemporary> temporaryList = this.baseMapper.selectList(wrapper);
        if(temporaryList != null && temporaryList.size() > 0){
            return temporaryList.get(0);
        }
        return null;
    }


}
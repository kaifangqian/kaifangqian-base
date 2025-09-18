/**
 * @description 业务线通知接口实现类
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
package com.kaifangqian.modules.opensign.service.re.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignReNotice;
import com.kaifangqian.modules.opensign.mapper.SignReNoticeMapper;
import com.kaifangqian.modules.opensign.service.re.SignReNoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignReNoticeServiceImpl extends ServiceImpl<SignReNoticeMapper, SignReNotice> implements SignReNoticeService {

    @Override
    public Boolean getByReIdAndType(String reId, String type) {
        LambdaQueryWrapper<SignReNotice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignReNotice::getSignReId, reId).eq(SignReNotice::getNoticeType, type);

        List<SignReNotice> list = list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0).getOpenFlag();
        } else {
            SignReNotice signReNotice = new SignReNotice();
            signReNotice.setSignReId(reId);
            signReNotice.setNoticeType(type);
            signReNotice.setOpenFlag(true);

            save(signReNotice);

            return signReNotice.getOpenFlag();
        }
    }

    @Override
    public void updateByReIdAndType(String reId, String type, Boolean flag) {
        LambdaUpdateWrapper<SignReNotice> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SignReNotice::getSignReId, reId).eq(SignReNotice::getNoticeType, type).set(SignReNotice::getOpenFlag, flag);

        update(updateWrapper);
    }
}
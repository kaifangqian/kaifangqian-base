/**
 * @description 签署业务校验服务接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignRuSignConfirm;
import com.kaifangqian.modules.opensign.mapper.SignRuSignConfirmMapper;
import com.kaifangqian.modules.opensign.service.ru.SignRuSignConfirmService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignRuSignConfirmServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.ru.impl
 * @ClassName: SignRuSignConfirmServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignRuSignConfirmServiceImpl extends ServiceImpl<SignRuSignConfirmMapper, SignRuSignConfirm> implements SignRuSignConfirmService {


    @Override
    public void save(String signerId, String ruId, Integer signerType,Integer isFastSign, String verifyType, String personalSignAuth) {
        SignRuSignConfirm signRuSignConfirm = new SignRuSignConfirm();
        signRuSignConfirm.setSignRuId(ruId);
        signRuSignConfirm.setSignerId(signerId);
        signRuSignConfirm.setSignerType(signerType);
        signRuSignConfirm.setConfirmType(verifyType);
        signRuSignConfirm.setAgreeSkipWillingness(isFastSign);
        signRuSignConfirm.setPersonalSignAuth(personalSignAuth);
        save(signRuSignConfirm);
    }

    @Override
    public SignRuSignConfirm getByParam(String signerId, String ruId) {
        QueryWrapper<SignRuSignConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuSignConfirm::getSignRuId,ruId);
        wrapper.lambda().eq(SignRuSignConfirm::getSignerId,signerId);
        List<SignRuSignConfirm> signConfirmList = this.baseMapper.selectList(wrapper);
        if(signConfirmList != null && signConfirmList.size() > 0){
            return signConfirmList.get(0);
        }
        return null;
    }

    @Override
    public List<SignRuSignConfirm> listByParam(String ruId) {
        QueryWrapper<SignRuSignConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuSignConfirm::getSignRuId,ruId);
        List<SignRuSignConfirm> signConfirmList = this.baseMapper.selectList(wrapper);
        return signConfirmList;
    }

    @Override
    public void delete(String signerId, String ruId) {
        QueryWrapper<SignRuSignConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignRuSignConfirm::getSignRuId,ruId);
        wrapper.lambda().eq(SignRuSignConfirm::getSignerId,signerId);
        this.baseMapper.delete(wrapper);
    }
}
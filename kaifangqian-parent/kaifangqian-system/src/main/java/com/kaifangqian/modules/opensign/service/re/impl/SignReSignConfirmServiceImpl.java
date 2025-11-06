/**
 * @description 业务线管理接口实现
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.entity.SignReSignConfirm;
import com.kaifangqian.modules.opensign.mapper.SignReSignConfirmMapper;
import com.kaifangqian.modules.opensign.service.re.SignReSignConfirmService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignReSignConfirmServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.re.impl
 * @ClassName: SignReSignConfirmServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignReSignConfirmServiceImpl extends ServiceImpl<SignReSignConfirmMapper, SignReSignConfirm> implements SignReSignConfirmService {

    @Override
    public void save(String signerId, String reId, Integer signerType,Integer isFastSign, String verifyType, String personalSignAuth, String sealType) {
        SignReSignConfirm reSignConfirm = new SignReSignConfirm();
        reSignConfirm.setSignReId(reId);
        reSignConfirm.setSignerId(signerId);
        reSignConfirm.setSignerType(signerType);
        reSignConfirm.setAgreeSkipWillingness(isFastSign);
        reSignConfirm.setConfirmType(verifyType);
        reSignConfirm.setPersonalSignAuth(personalSignAuth);
        reSignConfirm.setSealType(sealType);
        save(reSignConfirm);
    }

    @Override
    public SignReSignConfirm getByParam(String signerId, String reId) {
        QueryWrapper<SignReSignConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReSignConfirm::getSignReId,reId);
        wrapper.lambda().eq(SignReSignConfirm::getSignerId,signerId);

        List<SignReSignConfirm> signConfirmList = this.baseMapper.selectList(wrapper);
        if(signConfirmList != null && signConfirmList.size() > 0){
            return signConfirmList.get(0);
        }
        return null;
    }

    @Override
    public List<SignReSignConfirm> listByParam(String reId) {
        QueryWrapper<SignReSignConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReSignConfirm::getSignReId,reId);

        List<SignReSignConfirm> signConfirmList = this.baseMapper.selectList(wrapper);
        return signConfirmList;
    }

    @Override
    public void delete(String signerId, String reId) {
        QueryWrapper<SignReSignConfirm> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignReSignConfirm::getSignReId,reId);
        wrapper.lambda().eq(SignReSignConfirm::getSignerId,signerId);

        this.baseMapper.delete(wrapper);
    }

}
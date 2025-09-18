/**
 * @description 电子印章-企业印章管理接口实现类
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
package com.kaifangqian.modules.opensign.service.seal.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignEntSeal;
import com.kaifangqian.modules.opensign.enums.EntSealApplyStatusEnum;
import com.kaifangqian.modules.opensign.enums.EntSealStatusEnum;
import com.kaifangqian.modules.opensign.mapper.SignEntSealMapper;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignEntSealServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignEntSealServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignEntSealServiceImpl extends ServiceImpl<SignEntSealMapper, SignEntSeal> implements SignEntSealService {



    @Override
    public List<SignEntSeal> getList(String tenantId) {

        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignEntSeal::getSysTenantId,tenantId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().orderByDesc(BaseEntity::getCreateTime);
        List<SignEntSeal> signEntSeals = this.baseMapper.selectList(wrapper);

        return signEntSeals;
    }

    @Override
    public Boolean updateStatus(EntSealStatusEnum entSealStatus, EntSealApplyStatusEnum entSealApplyStatus, String sealId) {

        SignEntSeal entSeal = new SignEntSeal();
        entSeal.setId(sealId);
//        if(entSealApplyStatus != null){
//            entSeal.setApplyStatus(entSealApplyStatus.getCode());
//        }
        if(entSealStatus != null){
            entSeal.setSealStatus(entSealStatus.getCode());
        }

        int i = this.baseMapper.updateById(entSeal);
        if(i > 0){
            return true;
        }

        return false;
    }
}
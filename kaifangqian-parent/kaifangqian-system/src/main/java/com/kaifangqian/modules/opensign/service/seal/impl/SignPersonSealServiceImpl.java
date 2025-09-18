/**
 * @description 电子印章-个人印章管理接口实现类
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
import com.kaifangqian.modules.opensign.entity.SignPersonSeal;
import com.kaifangqian.modules.opensign.enums.SealDefaultEnum;
import com.kaifangqian.modules.opensign.mapper.SignPersonSealMapper;
import com.kaifangqian.modules.opensign.service.seal.SignPersonSealService;

import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: SignPersonSealServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.seal.impl
 * @ClassName: SignPersonSealServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignPersonSealServiceImpl extends ServiceImpl<SignPersonSealMapper, SignPersonSeal> implements SignPersonSealService {


    @Override
    public Boolean cancelDefault() {
        QueryWrapper<SignPersonSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);
        wrapper.lambda().eq(SignPersonSeal::getIsDefault, SealDefaultEnum.IS.getCode());

        SignPersonSeal seal = new SignPersonSeal();
        seal.setIsDefault(SealDefaultEnum.NOT.getCode());
        seal.setUpdateTime(new Date());
        this.baseMapper.update(seal,wrapper);
        return true;
    }

    @Override
    public Boolean setDefault(String sealId) {
        SignPersonSeal seal = new SignPersonSeal();
        seal.setId(sealId);
        seal.setIsDefault(SealDefaultEnum.IS.getCode());
        seal.setUpdateTime(new Date());
        this.baseMapper.updateById(seal);
        return true;
    }

    @Override
    public Boolean delete(String sealId) {
        SignPersonSeal seal = new SignPersonSeal();
        seal.setId(sealId);
        seal.setDeleteFlag(true);
        seal.setDeleteTime(new Date());
        this.baseMapper.updateById(seal);
        return true;
    }
}
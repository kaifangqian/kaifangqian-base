/**
 * @description 印章状态管理
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
package com.kaifangqian.modules.opensign.service.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.common.base.entity.BaseEntity;
import com.kaifangqian.modules.opensign.entity.SignEntSeal;
import com.kaifangqian.modules.opensign.entity.SignPersonSeal;
import com.kaifangqian.modules.opensign.enums.SealStatusEnum;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealService;
import com.kaifangqian.modules.opensign.service.seal.SignPersonSealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SealStatusBusinessService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: SealStatusBusinessService
 * @author: FengLai_Gong
 */
@Service
public class SealStatusBusinessService {


    @Autowired
    private SignEntSealService entSealService ;
    @Autowired
    private SignPersonSealService personSealService ;


    /**
     * @Description #更改所有企业签章状态
     * @Param [tenantId 租户id, sealStatusEnum 签章状态]
     * @return void
     **/
    public void disableEntSeal(String tenantId){

        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignEntSeal::getSysTenantId,tenantId);
        wrapper.lambda().eq(SignEntSeal::getCreateType,1);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignEntSeal entSeal = new SignEntSeal();
        entSeal.setStatus(SealStatusEnum.UN_ENABLE.getCode());

        entSealService.update(entSeal, wrapper);


    }

    public void enableEntSeal(String tenantId , List<Integer> typeList){

        QueryWrapper<SignEntSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignEntSeal::getSysTenantId,tenantId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        wrapper.lambda().in(SignEntSeal::getSealType,typeList);

        SignEntSeal entSeal = new SignEntSeal();
        entSeal.setStatus(SealStatusEnum.ENABLE.getCode());

        entSealService.update(entSeal, wrapper);

    }


    /**
     * @Description #更改所有个人签章状态
     * @Param [tenantId 租户id, sealStatusEnum 签章状态]
     * @return void
     **/
    public void changePersonSealStatus(String tenantId ,SealStatusEnum sealStatusEnum){

        QueryWrapper<SignPersonSeal> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SignPersonSeal::getSysTenantId,tenantId);
        wrapper.lambda().eq(BaseEntity::getDeleteFlag,false);

        SignPersonSeal personSeal = new SignPersonSeal();
        personSeal.setStatus(sealStatusEnum.getCode());

        personSealService.update(personSeal,wrapper);


    }


}
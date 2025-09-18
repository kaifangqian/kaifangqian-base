/**
 * @description 电子印章-企业印章管理操作记录接口实现类
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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.modules.opensign.entity.SignEntSealLogOperate;
import com.kaifangqian.modules.opensign.enums.EntSealOperateTypeEnum;
import com.kaifangqian.modules.opensign.mapper.SignEntSealLogOperateMapper;
import com.kaifangqian.modules.opensign.service.seal.SignEntSealLogOperateService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: SignEntSealLogOperateServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.seal.impl
 * @ClassName: SignEntSealLogOperateServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignEntSealLogOperateServiceImpl extends ServiceImpl<SignEntSealLogOperateMapper, SignEntSealLogOperate> implements SignEntSealLogOperateService {


    @Override
    public void insert(String sealId ,EntSealOperateTypeEnum entSealOperateTypeEnum){
        LoginUser currentUser = MySecurityUtils.getCurrentUser();
        SignEntSealLogOperate signEntSealLogOperate = new SignEntSealLogOperate();
        signEntSealLogOperate.setSealId(sealId);
        signEntSealLogOperate.setOperateTime(new Date());
        signEntSealLogOperate.setOperateType(entSealOperateTypeEnum.getCode());
        signEntSealLogOperate.setSysDeptId(currentUser.getDepartId());
        this.baseMapper.insert(signEntSealLogOperate);



    }



}
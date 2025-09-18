/**
 * @description 签署业务服务接口实现类，获取签署权限
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
package com.kaifangqian.modules.opensign.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.opensign.service.auth.vo.BusinessAuthQueryVo;
import com.kaifangqian.modules.opensign.entity.SignBusinessAuth;
import com.kaifangqian.modules.opensign.mapper.SignBusinessAuthMapper;
import com.kaifangqian.modules.opensign.service.auth.SignBusinessAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: SignBusinessAuthServiceImpl
 * @Package: com.kaifangqian.modules.opensign.service.auth.impl
 * @ClassName: SignBusinessAuthServiceImpl
 * @author: FengLai_Gong
 */
@Service
public class SignBusinessAuthServiceImpl extends ServiceImpl<SignBusinessAuthMapper, SignBusinessAuth> implements SignBusinessAuthService {

    @Override
    public List<SignBusinessAuth> queryAuthList(BusinessAuthQueryVo vo) {

        return this.baseMapper.getAuthList(vo);
    }

    @Override
    public Integer getAuthIdentify(BusinessAuthQueryVo vo) {
        return this.baseMapper.getAuthIdentify(vo);
    }

}
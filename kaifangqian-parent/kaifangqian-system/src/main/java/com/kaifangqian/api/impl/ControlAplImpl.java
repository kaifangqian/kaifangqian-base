package com.kaifangqian.api.impl;

import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.inteface.ControlAPI;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
/**
 * @description 控制实现类
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

/**
 * @author : zhh
 * create at: 2022/7/13
 */
@Slf4j
@Component
@Primary
public class ControlAplImpl implements ControlAPI {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public boolean checkPassword(String password) {
        LoginUser user = MySecurityUtils.getCurrentUser();
        SysUser sysUser = sysUserService.getById(user.getId());
        String userpassword = PasswordUtil.encrypt(sysUser.getUsername(), password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (syspassword.equals(userpassword)) {
            return true;
        }
        return false;
    }
}
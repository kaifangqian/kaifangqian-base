/**
 * @description 签署业务签章权限管理接口类
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
package com.kaifangqian.modules.opensign.service.ru;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.opensign.dto.UserSealAuthListVO;
import com.kaifangqian.modules.opensign.entity.UserSealAuth;

public interface UserSealAuthService extends IService<UserSealAuth> {

    IPage<UserSealAuthListVO> pageExt(Page<UserSealAuthListVO> page, UserSealAuth userSealAuth);

    void saveExt(UserSealAuth userSealAuth);

    void cancle(String id);

    void deleteExt(String id);

    //tenantUserId 租户下用户ID，signReId业务线ID
    UserSealAuth getSealId(String tenantUserId, String signReId);

    void refreshAuth();

}
/**
 * @description 签署命令执行
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
package com.kaifangqian.modules.opensign.interceptor;

import com.kaifangqian.modules.opensign.dto.SignTaskInfo;
import com.kaifangqian.modules.opensign.dto.SignTaskThreadlocalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : zhenghuihan
 * create at:  2022/2/23  3:43 PM
 * @description: 命令执行
 */
@Component
public class SignCommandInvoker extends SignAbstractCommandInterceptor {

    @Autowired
    private SignCommandContext signCommandContext;

    @Override
    public <T> T execute(final SignCommandConfig config, final SignCommand<T> command) {
        SignTaskThreadlocalVO threadlocalVO = SignTaskInfo.THREAD_LOCAL.get();
        signCommandContext.setSignRuId(threadlocalVO.getSignRuId());
        signCommandContext.setTaskId(threadlocalVO.getTaskId());
        signCommandContext.setTaskType(threadlocalVO.getTaskType());
        signCommandContext.setUserType(threadlocalVO.getUserType());
        signCommandContext.setUserTaskId(threadlocalVO.getUserTaskId());
        return command.execute(signCommandContext);
    }

    @Override
    public SignCommandInterceptor getNext() {
        return null;
    }

    @Override
    public void setNext(SignCommandInterceptor next) {
        throw new UnsupportedOperationException("CommandInvoker must be the last interceptor in the chain");
    }

}
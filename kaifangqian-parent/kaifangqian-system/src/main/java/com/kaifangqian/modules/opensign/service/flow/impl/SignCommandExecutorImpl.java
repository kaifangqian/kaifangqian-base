/**
 * @description 命令执行器
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
package com.kaifangqian.modules.opensign.service.flow.impl;

import com.kaifangqian.modules.opensign.interceptor.SignCommand;
import com.kaifangqian.modules.opensign.interceptor.SignCommandConfig;
import com.kaifangqian.modules.opensign.interceptor.SignCommandInterceptor;
import com.kaifangqian.modules.opensign.service.flow.SignCommandExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : zhenghuihan
 * create at:  2022/2/21  3:29 PM
 * @description:
 */
@Component
public class SignCommandExecutorImpl implements SignCommandExecutor {

    @Autowired
    protected SignCommandConfig defaultConfig;

    protected SignCommandInterceptor first;

    public SignCommandInterceptor getFirst() {
        return first;
    }

    public void setFirst(SignCommandInterceptor commandInterceptor) {
        this.first = commandInterceptor;
    }

    @Override
    public SignCommandConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public <T> T execute(SignCommand<T> command) {
        return execute(defaultConfig, command);
    }

    @Override
    public <T> T execute(SignCommandConfig config, SignCommand<T> command) {
        return first.execute(config, command);
    }
}
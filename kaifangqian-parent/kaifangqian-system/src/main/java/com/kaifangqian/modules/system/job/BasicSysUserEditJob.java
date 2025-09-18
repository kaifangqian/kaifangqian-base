/**
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
package com.kaifangqian.modules.system.job;

import com.kaifangqian.modules.system.entity.SysUser;
import com.kaifangqian.common.constant.CommonConstant;
import com.kaifangqian.modules.system.service.ISysUserService;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

/**
 * @author : zhenghuihan
 * create at:  2022/9/5  16:25
 * @description: 修改用户信息job
 */
@Component
@Slf4j
public class BasicSysUserEditJob implements BasicProcessor {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        OmsLogger omsLogger = taskContext.getOmsLogger();
        omsLogger.info("BasicSysUserEditJob开始处理, 参数为 {}.", taskContext.getJobParams());

        if (MyStringUtils.isNotBlank(taskContext.getInstanceParams())) {
            SysUser sysUser = sysUserService.getUserByName(taskContext.getInstanceParams());
            if (sysUser != null && CommonConstant.USER_FREEZE_AUTO.equals(sysUser.getStatus())) {
                sysUser.setStatus(CommonConstant.USER_UNFREEZE);

                sysUserService.updateById(sysUser);
            }
        }

        return new ProcessResult(true, "任务处理成功");
    }

}
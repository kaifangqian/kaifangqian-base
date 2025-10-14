/**
 * @description 签署业务签署任务接口类，获取各类签署任务
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

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.system.vo.SysTenantInfoExtendVO;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.vo.base.sign.TaskInfoFor3rdVO;
import com.kaifangqian.modules.opensign.vo.base.sign.TaskSearchFor3rdVO;

import java.util.List;

public interface SignRuTaskService extends IService<SignRuTask> {

    List<SignRuTask> getByEntity(SignRuTask query);

    List<SignRuTask> getNoUserList(SignRuTask query);

    List<SignRuTask> getNoTenantUserList(SignRuTask query);

    List<SignRuTask> getTenantNoBindList(SignRuTask query);

    List<SignRuTask> getTenantNoBindListForApi(SignRuTask query);

    List<SignRuTask> getTenantNoBindListForLoading(SignRuTask query);

    List<SignRuTask> getPersonalTenanList(SignRuTask query);

    List<SysTenantInfoExtendVO> getMyTenantJobs();

    TaskInfoFor3rdVO getTaskInfoFor3rd(TaskSearchFor3rdVO searchFor3rdVO);

    String getCodeFor3rd(TaskSearchFor3rdVO searchFor3rdVO);
}
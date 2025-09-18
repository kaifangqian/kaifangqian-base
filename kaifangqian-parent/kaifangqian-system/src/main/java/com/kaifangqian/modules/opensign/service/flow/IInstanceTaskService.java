/**
 * @description 流程任务查询
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
package com.kaifangqian.modules.opensign.service.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.vo.request.ru.CompanyStasticsVO;
import com.kaifangqian.modules.opensign.vo.request.ru.MenuStasticsVO;
import com.kaifangqian.modules.opensign.vo.request.ru.TaskListInfoReq;
import com.kaifangqian.modules.opensign.vo.request.ru.TaskListInfoRes;

/**
 * @author : zhenghuihan
 * create at:  2023/11/8  15:11
 * @description: 流程任务查询
 */
public interface IInstanceTaskService {

    IPage<TaskListInfoRes> listInbox(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listSend(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listCopyMe(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listDraft(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listRecycle(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listCompanyAll(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listPersonalAll(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listMyJob(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listCompanyOtherJob(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listPersonalOtherJob(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listRunning(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listFinish(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listInvalid(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listMySignJob(Page<TaskListInfoRes> page, TaskListInfoReq req);

    IPage<TaskListInfoRes> listMyFillInJob(Page<TaskListInfoRes> page, TaskListInfoReq req);

    CompanyStasticsVO myStastics();

    MenuStasticsVO companyMenuStastics();

    MenuStasticsVO personalMenuStastics();

    /**
     * @param
     */
    Integer checkAuth(String taskId);

    void setParticipateNames(IPage<TaskListInfoRes> result);
}
/**
 * @description 业务线实例-任务表Mapper
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
package com.kaifangqian.modules.opensign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.vo.SysTenantInfoExtendVO;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.vo.request.ru.TaskListInfoReq;
import com.kaifangqian.modules.opensign.vo.request.ru.TaskListInfoRes;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SignRuTaskMapper extends BaseMapper<SignRuTask> {

    IPage<TaskListInfoRes> listInbox(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listSend(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listCopyMe(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listDraft(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listRecycle(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listCompanyAll(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listPersonalAll(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listMyJob(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listMyJobSignUsers(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listCompanyOtherJob(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listPersonalOtherJob(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listRunning(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listFinish(Page page, @Param("req") TaskListInfoReq req);

    IPage<TaskListInfoRes> listInvalid(Page page, @Param("req") TaskListInfoReq req);

    List<SysTenantInfoExtendVO> getMyTenantJobs(@Param("userId") String userId);
}
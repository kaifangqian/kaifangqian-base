/**
 * @description 企业签署文档列表，状态为：填写中、签署中、已撤回时调用
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
package com.kaifangqian.modules.opensign.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.opensign.vo.request.ru.*;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.system.vo.LoginUser;
import com.kaifangqian.common.util.MySecurityUtils;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuTask;
import com.kaifangqian.modules.opensign.enums.SignRuStatusEnum;
import com.kaifangqian.modules.opensign.service.flow.IInstanceTaskService;
import com.kaifangqian.modules.opensign.service.ru.SignRuService;
import com.kaifangqian.modules.opensign.service.ru.SignRuTaskService;
import com.kaifangqian.modules.opensign.vo.request.ru.*;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @create by zhenghuihan
 * @createTime 2023/11/23 16:54
 * @description 状态为：填写中、签署中、已撤回时调用
 */
@Slf4j
@RestController
@RequestMapping("/company/task")
@ResrunLogModule(name = "企业文档列表")
// @Api(tags = "企业文档列表")
public class CompanyTaskController {

    @Autowired
    private IInstanceTaskService instanceTaskService;
    @Autowired
    private SignRuService signRuService;
    @Autowired
    private SignRuTaskService signRuTaskService;

    // @ApiOperation(value = "校验操作", notes = "校验操作")
    @GetMapping(value = "/checkOperate")
    @ResrunLogMethod(name = "校验操作", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<CheckOprateRes> checkOperate(@RequestParam(name = "signRuId") String signRuId) {
        CheckOprateRes result = new CheckOprateRes();
        result.setSignRuId(signRuId);
        SignRu signRu = signRuService.getById(signRuId);
        if (signRu != null) {
            // if (signRu.getStatus() == SignRuStatusEnum.SIGNING.getCode() || signRu.getStatus() == SignRuStatusEnum.WRITING.getCode() || signRu.getStatus() == SignRuStatusEnum.REVOKE.getCode()) {
            LoginUser loginUser = MySecurityUtils.getCurrentUser();
            if (loginUser.getTenantUserId().equals(signRu.getSysUserId())) {
                result.setStartFlag(true);
            }
            SignRuTask query = new SignRuTask();
            query.setSignRuId(signRuId);
            query.setTaskStatus(1);
            List<SignRuTask> tasks = signRuTaskService.getByEntity(query);
            if (CollUtil.isNotEmpty(tasks)) {
                result.setConfirmTaskId(tasks.get(0).getId());
            }
            if (signRu.getStatus() == SignRuStatusEnum.SIGNING.getCode() || signRu.getStatus() == SignRuStatusEnum.WRITING.getCode()) {
                query.setSignRuId(signRuId);
                query.setTenantUserId(loginUser.getTenantUserId());
                query.setTaskStatus(1);
                tasks = signRuTaskService.getByEntity(query);
                if (CollUtil.isNotEmpty(tasks)) {
                    result.setTaskId(tasks.get(0).getId());
                    result.setTaskType(tasks.get(0).getTaskType());
                }
            }
            Boolean downloadFlag = signRuService.checkDownloadAuth(signRu.getId());
            result.setDownloadFlag(downloadFlag);
            //}
        }

        return Result.OK(result);
    }

    // @ApiOperation(value = "收件箱", notes = "收件箱")
    @GetMapping(value = "/listInbox")
    @ResrunLogMethod(name = "收件箱", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listInbox(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listInbox(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "已发送", notes = "已发送")
    @GetMapping(value = "/listSend")
    @ResrunLogMethod(name = "已发送", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listSend(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listSend(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "抄送我的", notes = "抄送我的")
    @GetMapping(value = "/listCopyMe")
    @ResrunLogMethod(name = "抄送我的", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listCopyMe(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listCopyMe(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "草稿", notes = "草稿")
    @GetMapping(value = "/listDraft")
    @ResrunLogMethod(name = "草稿", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listDraft(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listDraft(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "回收站", notes = "回收站")
    @GetMapping(value = "/listRecycle")
    @ResrunLogMethod(name = "回收站", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listRecycle(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listRecycle(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "全部文档", notes = "全部文档")
    @GetMapping(value = "/listAll")
    @ResrunLogMethod(name = "全部文档", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listAll(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listCompanyAll(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "待我处理", notes = "待我处理")
    @GetMapping(value = "/listMyJob")
    @ResrunLogMethod(name = "待我处理", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listMyJob(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listMyJob(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "待他人处理", notes = "待他人处理")
    @GetMapping(value = "/listOtherJob")
    @ResrunLogMethod(name = "待他人处理", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listCompanyOtherJob(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listCompanyOtherJob(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "未完成", notes = "未完成")
    @GetMapping(value = "/listRunning")
    @ResrunLogMethod(name = "未完成", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listRunning(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        req.setSignReFlag(true);
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listRunning(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "已完成", notes = "已完成")
    @GetMapping(value = "/listFinish")
    @ResrunLogMethod(name = "已完成", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listFinish(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        req.setSignReFlag(true);
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listFinish(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "已失效", notes = "已失效")
    @GetMapping(value = "/listInvalid")
    @ResrunLogMethod(name = "已失效", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listInvalid(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        req.setSignReFlag(true);
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listInvalid(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    //首页相关
    // @ApiOperation(value = "待我签署", notes = "待我签署")
    @GetMapping(value = "/listMySignJob")
    @ResrunLogMethod(name = "待我签署", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listMySignJob(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listMySignJob(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "待我签署", notes = "待我签署")
    @GetMapping(value = "/listMyApproveJob")
    @ResrunLogMethod(name = "待我审批", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listMyApproveJob(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listMyApproveJob(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "待我填写", notes = "待我填写")
    @GetMapping(value = "/listMyFillInJob")
    @ResrunLogMethod(name = "待我填写", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<IPage<TaskListInfoRes>> listMyFillInJob(TaskListInfoReq req, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<TaskListInfoRes> page = new Page<TaskListInfoRes>(pageNo, pageSize);
        IPage<TaskListInfoRes> result = instanceTaskService.listMyFillInJob(page, req);
        instanceTaskService.setParticipateNames(result);
        return Result.OK(result);
    }

    // @ApiOperation(value = "首页统计", notes = "首页统计")
    @GetMapping(value = "/myStastics")
    @ResrunLogMethod(name = "首页统计", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<CompanyStasticsVO> myStastics() {
        CompanyStasticsVO result = instanceTaskService.myStastics();

        return Result.OK(result);
    }

    // @ApiOperation(value = "列表统计", notes = "列表统计")
    @GetMapping(value = "/menuStastics")
    @ResrunLogMethod(name = "列表统计", operateType = OperateLogType.OPERATE_TYPE_1)
    public Result<MenuStasticsVO> menuStastics() {
        MenuStasticsVO result = instanceTaskService.companyMenuStastics();

        return Result.OK(result);
    }
}
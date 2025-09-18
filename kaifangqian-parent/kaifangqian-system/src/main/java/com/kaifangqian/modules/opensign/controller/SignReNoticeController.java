/**
 * @description 业务线配置管理，业务线通知配置
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

import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.dto.SignReNoticeVO;
import com.kaifangqian.modules.opensign.enums.ReNoticeTypeEnum;
import com.kaifangqian.modules.opensign.service.re.SignReNoticeService;
import com.kaifangqian.utils.MyStringUtils;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: SignReController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignReController
 * @author: FengLai_Gong
 * @Date: 2023/12/12
 */
@Slf4j
@RestController
@RequestMapping("/sign/re/notice")
@ResrunLogModule(name = "业务线通知配置")
// @Api(tags = "业务线通知配置")
public class SignReNoticeController {

    @Autowired
    private SignReNoticeService signReNoticeService;


    // @ApiOperation("获取通知配置")
    @GetMapping
    public Result<SignReNoticeVO> get(@RequestParam String singReId) {
        SignReNoticeVO result = new SignReNoticeVO();
        result.setSignReId(singReId);
        result.setWriteTaskFlagPhone(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.WRITE_TASK_PHONE.getType()));
        result.setSignTaskInFlagPhone(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.SIGN_TASK_IN_PHONE.getType()));
        result.setSignTaskOutFlagPhone(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.SIGN_TASK_OUT_PHONE.getType()));
        result.setCopyBeginFlagPhone(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.COPY_BEGIN_PHONE.getType()));
        result.setCopySignFlagPhone(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.COPY_SIGN_PHONE.getType()));
        result.setWriteTaskFlagEmail(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.WRITE_TASK_EMAIL.getType()));
        result.setSignTaskInFlagEmail(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.SIGN_TASK_IN_EMAIL.getType()));
        result.setSignTaskOutFlagEmail(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.SIGN_TASK_OUT_EMAIL.getType()));
        result.setCopyBeginFlagEmail(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.COPY_BEGIN_EMAIL.getType()));
        result.setCopySignFlagEmail(signReNoticeService.getByReIdAndType(singReId, ReNoticeTypeEnum.COPY_SIGN_EMAIL.getType()));

        return Result.OK("ok", result);
    }

    @PostMapping
    @ResrunLogMethod(name = "更新通知配置", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> update(@RequestBody SignReNoticeVO vo) {
        if (MyStringUtils.isBlank(vo.getSignReId())) {
            throw new PaasException("业务线唯一标识不能为空");
        }
        if (vo.getWriteTaskFlagPhone() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.WRITE_TASK_PHONE.getType(), vo.getWriteTaskFlagPhone());
        }
        if (vo.getSignTaskInFlagPhone() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_PHONE.getType(), vo.getSignTaskInFlagPhone());
        }
        if (vo.getSignTaskOutFlagPhone() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_PHONE.getType(), vo.getSignTaskOutFlagPhone());
        }
        if (vo.getCopyBeginFlagPhone() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.COPY_BEGIN_PHONE.getType(), vo.getCopyBeginFlagPhone());
        }
        if (vo.getCopySignFlagPhone() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.COPY_SIGN_PHONE.getType(), vo.getCopySignFlagPhone());
        }

        if (vo.getWriteTaskFlagEmail() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.WRITE_TASK_EMAIL.getType(), vo.getWriteTaskFlagEmail());
        }
        if (vo.getSignTaskInFlagEmail() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_IN_EMAIL.getType(), vo.getSignTaskInFlagEmail());
        }
        if (vo.getSignTaskOutFlagEmail() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.SIGN_TASK_OUT_EMAIL.getType(), vo.getSignTaskOutFlagEmail());
        }
        if (vo.getCopyBeginFlagEmail() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.COPY_BEGIN_EMAIL.getType(), vo.getCopyBeginFlagEmail());
        }
        if (vo.getCopySignFlagEmail() != null) {
            signReNoticeService.updateByReIdAndType(vo.getSignReId(), ReNoticeTypeEnum.COPY_SIGN_EMAIL.getType(), vo.getCopySignFlagEmail());
        }

        return Result.OK();
    }
}
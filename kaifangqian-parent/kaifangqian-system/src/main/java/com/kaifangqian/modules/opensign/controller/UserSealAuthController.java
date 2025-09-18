/**
 * @description 用户签名授权
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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaifangqian.modules.system.entity.SysDict;
import com.kaifangqian.annotation.ResrunLogMethod;
import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.enums.OperateLogType;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.dto.UserSealAuthListVO;
import com.kaifangqian.modules.opensign.entity.SignRe;
import com.kaifangqian.modules.opensign.entity.UserSealAuth;
import com.kaifangqian.modules.opensign.service.confirm.IUserConfirmService;
import com.kaifangqian.modules.opensign.service.re.SignReService;
import com.kaifangqian.modules.opensign.service.ru.UserSealAuthService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: SignVerifyController
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignVerifyController
 * @author: Fusion
 * CreateTime:  2023/8/20  9:53
 * @copyright 北京资源律动科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/user/seal/auth")
@ResrunLogModule(name = "用户签名授权")
// @Api(tags = "用户签名授权")
public class UserSealAuthController {

    @Autowired
    private UserSealAuthService userSealAuthService;
    @Autowired
    private SignReService signReService;
    @Autowired
    private IUserConfirmService userConfirmService;


    @ResrunLogMethod(name = "分页查询用户签名授权列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<UserSealAuthListVO>> queryPageList(UserSealAuth userSealAuth, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<UserSealAuthListVO> page = new Page<UserSealAuthListVO>(pageNo, pageSize);
        IPage<UserSealAuthListVO> pageList = userSealAuthService.pageExt(page, userSealAuth);
        return Result.OK(pageList);
    }

    @ResrunLogMethod(name = "分页查询租户下业务线列表", operateType = OperateLogType.OPERATE_TYPE_1)
    @RequestMapping(value = "/listSignRe", method = RequestMethod.GET)
    public Result<IPage<SignRe>> listSignRe(@RequestParam(name = "tenantId") String tenantId, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<SignRe> page = new Page<SignRe>(pageNo, pageSize);
        LambdaQueryWrapper<SignRe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SignRe::getSysTenantId, tenantId).eq(SignRe::getDeleteFlag, 0);
        IPage<SignRe> pageList = signReService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    // @ApiOperation("获取校验方式")
    @RequestMapping(value = "/getMyConfirmType", method = RequestMethod.GET)
    public Result<?> getMyConfirmType() {
        return Result.OK("ok", userConfirmService.getMyConfirmType());
    }


    @PostMapping("/add")
    @ResrunLogMethod(name = "新增", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> add(@RequestBody UserSealAuth userSealAuth) {
        userSealAuthService.saveExt(userSealAuth);
        return Result.OK();
    }

    @PostMapping("/cancle")
    @ResrunLogMethod(name = "取消授权", operateType = OperateLogType.OPERATE_TYPE_3)
    public Result<?> cancle(@RequestBody UserSealAuth userSealAuth) {
        userSealAuthService.cancle(userSealAuth.getId());
        return Result.OK();
    }

    @ResrunLogMethod(name = "删除授权", operateType = OperateLogType.OPERATE_TYPE_4)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysDict> delete(@RequestParam(name = "id", required = true) String id) {
        userSealAuthService.deleteExt(id);
        return Result.OK();
    }
}
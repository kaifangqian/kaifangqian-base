/**
 * @description 个人签署节点实名认证类型配置
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

import com.kaifangqian.annotation.ResrunLogModule;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.modules.opensign.service.business.RuBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 个人签署节点实名认证类型配置
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: PersonalSignAuthController
 * @author: resrun-Y
 */
@Slf4j
@RestController
@RequestMapping("/sign/personal/auth")
@ResrunLogModule(name = "业务线-个人签署节点实名认证类型配置")
public class PersonalSignAuthController {

    @Autowired
    private RuBusinessService ruBusinessService ;

    @RequestMapping(value = "/sys/type", method = RequestMethod.GET)
    public Result<String> getSysPersonalSignAuthType(){
        return Result.OK(ruBusinessService.getSystemPersonalSignAuthType()) ;
    }



}
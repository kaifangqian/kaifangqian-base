/**
 * @description 业务线运行实例-签署报告
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
import com.kaifangqian.modules.opensign.service.business.RuSignReportService;
import com.kaifangqian.modules.opensign.service.business.vo.SignReportSignSubVO;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Description: SignRuReportControll
 * @Package: com.kaifangqian.modules.opensign.controller
 * @ClassName: SignRuReportControll
 * @author: FengLai_Gong
 * @Date: 2023/11/17 10:09
 */
@Slf4j
@RestController
@RequestMapping("/sign/ru")
@ResrunLogModule(name = "业务线运行实例-签署报告")
// @Api(tags = "业务线运行实例-签署报告")
public class SignRuReportController {


    @Autowired
    private RuSignReportService reportService ;


    // @ApiOperation("业务线实例-操作记录测试接口")
    @RequestMapping(value = "/test/report/data", method = RequestMethod.GET)
    public Result<?> listOperateRecord(@RequestParam("signRuId") String signRuId) {

        Map<String,Object> map = new HashMap<>();

        List<SignReportSignSubVO> base = reportService.baseInfo(signRuId);
        if(base != null && base.size() > 0){
            map.put("base",base);
        }else {
            map.put("base","");
        }

        List<SignReportSignSubVO> flow = reportService.flowInfo(signRuId);
        if(flow != null && flow.size() > 0){
            map.put("flow",flow);
        }else {
            map.put("flow","");
        }

        Set<String> allTenantIds = reportService.getAllTenantIds(signRuId);
        if(allTenantIds != null && allTenantIds.size() > 0){
            map.put("allTenantIds",allTenantIds);
        }else {
            map.put("allTenantIds","");
        }

        return Result.OK(map);
    }
}
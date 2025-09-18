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
package com.kaifangqian.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.system.entity.SysCheckRule;

/**
 * @author zhenghuihan
 * @description 编码校验规则
 * @createTime 2022/9/2 18:12
 */
public interface ISysCheckRuleService extends IService<SysCheckRule> {

    /**
     * 通过 code 获取规则
     *
     * @param ruleCode
     * @return
     */
    SysCheckRule getByCode(String ruleCode);


    /**
     * 通过用户设定的自定义校验规则校验传入的值
     *
     * @param checkRule
     * @param value
     * @return 返回 null代表通过校验，否则就是返回的错误提示文本
     */
    JSONObject checkValue(SysCheckRule checkRule, String value);

}

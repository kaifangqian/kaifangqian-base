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
package com.kaifangqian.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.modules.system.entity.SysCheckRule;
import com.kaifangqian.modules.system.mapper.SysCheckRuleMapper;
import org.apache.commons.lang3.StringUtils;
import com.kaifangqian.modules.system.service.ISysCheckRuleService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author zhenghuihan
 * @description 编码校验规则
 * @createTime 2022/9/2 18:16
 */
@Service
public class SysCheckRuleServiceImpl extends ServiceImpl<SysCheckRuleMapper, SysCheckRule> implements ISysCheckRuleService {

    /**
     * 位数特殊符号，用于检查整个值，而不是裁剪某一段
     */
    private final String CHECK_ALL_SYMBOL = "*";

    @Override
    public SysCheckRule getByCode(String ruleCode) {
        LambdaQueryWrapper<SysCheckRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysCheckRule::getRuleCode, ruleCode);
        return super.getOne(queryWrapper);
    }

    /**
     * 通过用户设定的自定义校验规则校验传入的值
     *
     * @param checkRule
     * @param value
     * @return 返回 null代表通过校验，否则就是返回的错误提示文本
     */
    @Override
    public JSONObject checkValue(SysCheckRule checkRule, String value) {
        if (checkRule != null && StringUtils.isNotBlank(value)) {
            String ruleJson = checkRule.getRuleJson();
            if (StringUtils.isNotBlank(ruleJson)) {
                // 开始截取的下标，根据规则的顺序递增，但是 * 号不计入递增范围
                int beginIndex = 0;
                JSONArray rules = JSON.parseArray(ruleJson);
                for (int i = 0; i < rules.size(); i++) {
                    JSONObject result = new JSONObject();
                    JSONObject rule = rules.getJSONObject(i);
                    // 位数
                    String digits = rule.getString("digits");
                    result.put("digits", digits);
                    // 验证规则
                    String pattern = rule.getString("pattern");
                    result.put("pattern", pattern);
                    // 未通过时的提示文本
                    String message = rule.getString("message");
                    result.put("message", message);

                    // 根据用户设定的区间，截取字符串进行验证
                    String checkValue;
                    // 是否检查整个值而不截取
                    if (CHECK_ALL_SYMBOL.equals(digits)) {
                        checkValue = value;
                    } else {
                        int num = Integer.parseInt(digits);
                        int endIndex = beginIndex + num;
                        // 如果结束下标大于给定的值的长度，则取到最后一位
                        endIndex = endIndex > value.length() ? value.length() : endIndex;
                        // 如果开始下标大于结束下标，则说明用户还尚未输入到该位置，直接赋空值
                        if (beginIndex > endIndex) {
                            checkValue = "";
                        } else {
                            checkValue = value.substring(beginIndex, endIndex);
                        }
                        result.put("beginIndex", beginIndex);
                        result.put("endIndex", endIndex);
                        beginIndex += num;
                    }
                    result.put("checkValue", checkValue);
                    boolean passed = Pattern.matches(pattern, checkValue);
                    result.put("passed", passed);
                    // 如果没有通过校验就返回错误信息
                    if (!passed) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

}

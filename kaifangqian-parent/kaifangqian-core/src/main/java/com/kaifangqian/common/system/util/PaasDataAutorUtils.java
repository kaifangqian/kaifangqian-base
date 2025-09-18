/**
 * @description 数据权限查询规则容器工具类
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
package com.kaifangqian.common.system.util;

import com.kaifangqian.common.system.vo.SysPermissionDataRuleModel;
import com.kaifangqian.common.util.SpringContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author : zhenghuihan
 * create at: 2023/12/18
 */
public class PaasDataAutorUtils {

    public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";

    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param request
     * @param dataRules
     */
    public static synchronized void installDataSearchConditon(HttpServletRequest request, List<List<List<List<SysPermissionDataRuleModel>>>> dataRules) {
        request.setAttribute(MENU_DATA_AUTHOR_RULES, dataRules);
    }

    /**
     * 获取请求对应的数据权限规则
     *
     * @returnMENU_DATA_AUTHOR_RULES
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<List<List<List<SysPermissionDataRuleModel>>>> loadDataSearchConditon() {
        return (List<List<List<List<SysPermissionDataRuleModel>>>>) SpringContextUtils.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULES);
    }
}

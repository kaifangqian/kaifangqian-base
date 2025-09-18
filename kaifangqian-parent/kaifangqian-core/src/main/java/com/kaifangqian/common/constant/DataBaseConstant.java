/**
 * @description 数据库上下文常量
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
package com.kaifangqian.common.constant;

/**
 * @author: zhh
 * @date: 2022-05-31
 */
public interface DataBaseConstant {
    /**
     * 数据-所属机构编码
     */
    public static final String SYS_ORG_CODE = "sysOrgCode";
    /**
     * 数据-所属机构编码
     */
    public static final String SYS_ORG_CODE_TABLE = "sys_org_code";

    /**
     * 数据-系统账号ID
     */
    public static final String SYS_USER_ID = "sysUserId";

    /**
     * 数据-系统账号ID
     */
    public static final String SYS_USER_ID_TABLE = "sys_user_id";

    /**
     * 数据-系统租户ID
     */
    public static final String TENANT_ID = "tenantId";

    /**
     * 数据-系统租户ID
     */
    public static final String TENANT_ID_TABLE = "tenant_id";

    /**
     * 数据-系统租户-用户ID
     */
    public static final String TENANT_USER_ID = "tenantUserId";

    /**
     * 数据-系统租户-用户ID
     */
    public static final String TENANT_USER_ID_TABLE = "tenant_user_id";

    /**
     * 数据-系统用户编码（对应登录用户账号）
     */
    public static final String SYS_USER_CODE = "sysUserCode";
    /**
     * 数据-系统用户编码（对应登录用户账号）
     */
    public static final String SYS_USER_CODE_TABLE = "sys_user_code";

    /**
     * 登录用户真实姓名
     */
    public static final String SYS_USER_REAL_NAME = "sysUserRealName";
    /**
     * 登录用户真实姓名
     */
    public static final String SYS_USER_REAL_NAME_TABLE = "sys_user_real_name";
    /**
     * 系统日期"yyyy-MM-dd"
     */
    public static final String SYS_DATE = "sysDate";
    /**
     * 系统日期"yyyy-MM-dd"
     */
    public static final String SYS_DATE_TABLE = "sys_date";
    /**
     * 系统时间"yyyy-MM-dd HH:mm"
     */
    public static final String SYS_TIME = "sysTime";
    /**
     * 系统时间"yyyy-MM-dd HH:mm"
     */
    public static final String SYS_TIME_TABLE = "sys_time";
}

/**
 * @description: 多数据源事务问题
 * 实现思路：转换为单数据源事务，最终类似在方法上加两个事务注解，最终还是用spring事务。
 * 因为spring不支持加两个事务注解，所以通过该方法，将多数据源事务转换成单数据源事务问题。
 * 存在问题：因为不是分布式事务的解决方案，所以当数据库宕机或其他不可控因素发生时，也会存在一些问题。
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
package com.kaifangqian.common.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author : zhh
 * create at:  2021/2/3  5:26 PM
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiTransactional {

    /**
     * 事务管理器数组
     */
    String[] value() default {};
}

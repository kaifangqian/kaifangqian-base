/**
 * @description 任务属性
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
package com.kaifangqian.modules.opensign.vo.request.ru;

import lombok.Data;

/**
 * @author : zhenghuihan
 * create at:  2023/11/20  14:32
 * @description:
 */
@Data
public class CompanyStasticsVO {
    //进行中 填写中、签署中
    private Integer statusCount;
    //已拒填
    private Integer status6Count;
    //已拒签
    private Integer status8Count;
    //已撤回（撤销）
    private Integer status10Count;
    //已失效（预期）
    private Integer status9Count;
    //已完成
    private Integer status11Count;
}
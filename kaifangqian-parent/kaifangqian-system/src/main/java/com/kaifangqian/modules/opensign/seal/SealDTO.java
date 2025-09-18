/**
 * @description 印章数据类
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
package com.kaifangqian.modules.opensign.seal;

import lombok.Data;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Data
public class SealDTO {
    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 颜色
     */
    private Integer color = 0;

    /**
     * 下弦文
     */
    private String serNo;
    /**
     * 五角星
     */
    private boolean starFlag = true;

    /**
     * 中间文字
     */
    private String center;

    /**
     * 副标题
     */
    private String title;

    /**
     * 颜色的集合
     */
    private static List<Color> colorList = Arrays.asList(Color.RED, Color.BLUE, Color.BLACK);


    public Color getColor() {
        return color > colorList.size() ? colorList.get(0) : colorList.get(color);
    }
}

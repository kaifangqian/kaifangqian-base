/**
 * @description 文件签署属性类
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
package com.kaifangqian.modules.opensign.service.tool.pojo;

import java.io.Serializable;

/**
 * @Description: PositionSignPage
 * @Package: com.kaifangqian.modules.sign.pojo
 * @ClassName: PositionSignPage
 * @author: FengLai_Gong
 */
public class PositionSignPage implements Serializable {

    //签署点坐在页码
    private int pageNum;
    //签署点相对于该页的x坐标
    private String xScale;
    //签署点相对于该页的y坐标
    private String yScale;

    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public String getxScale() {
        return xScale;
    }
    public void setxScale(String xScale) {
        this.xScale = xScale;
    }
    public String getyScale() {
        return yScale;
    }
    public void setyScale(String yScale) {
        this.yScale = yScale;
    }
}
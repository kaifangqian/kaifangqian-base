/**
 * @description pdf文件操作属性对象
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: pdf文件操作属性对象
 * @Package: com.kaifangqian.modules.sign.service.tool.pojo
 * @ClassName: PdfOperateProperty
 * @author: FengLai_Gong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfOperateProperty implements Serializable {

    private static final long serialVersionUID = 136217989504135671L;

    //pdf文件数据
    private byte[] pdfFile ;
    //图片数据
    private byte[] imageFile ;
    //文本数据
    private String value ;
    //横坐标偏移量
    private Float offsetX ;
    //纵坐标偏移量
    private Float offsetY ;
    //页码
    private Integer pageNum ;




}
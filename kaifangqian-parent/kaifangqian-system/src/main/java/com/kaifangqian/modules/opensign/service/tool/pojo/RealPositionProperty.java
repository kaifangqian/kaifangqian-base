/**
 * @description 经过计算后的文件签署位置属性类
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
import java.util.List;

/**
 * @Description: 经过计算后的文件签署位置属性类
 * @Package: com.kaifangqian.modules.sign.pojo
 * @ClassName: PositionProperty
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RealPositionProperty implements Serializable {


    private static final long serialVersionUID = 8586984409612483553L;

    /** 签章左下角x坐标 */
    private float startx;

    /** 签章左下角y坐标*/
    private float starty;

    /** 签章右上角x坐标*/
    private float endx;

    /** 签章右上角x坐标*/
    private float endy;

    private int pageNum;

    //真实pdf文件宽
    private float realPdfWidth;
    //真实pdf文件高
    private float realPdfHeight;

    // 填写值，填写专用
    private String value;
    //对齐方式
    private String align;
    //字体
    private String fontFamily;
    //文字大小
    private String fontSize;

    private String controlType;

    private String filedName;

    private List<ControlWidgetProperty> widgetPropertyList;
}
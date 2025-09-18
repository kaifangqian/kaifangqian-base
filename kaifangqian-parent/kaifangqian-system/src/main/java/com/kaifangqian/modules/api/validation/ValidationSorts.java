/**
 * @description 参数验签顺序控制类
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
package com.kaifangqian.modules.api.validation;

import javax.validation.GroupSequence;

/**
 * @Description: 参数验签顺序控制类
 * @Package: com.cert.validation
 * @ClassName: ValidationGroups
 * @author: WenTing_Wang
 * @createDate: 2022/7/26 13:50
 * <p>
 * ---------------------------------------------------------
 * Version    Author       Status    Date
 * V1.0       WenTing_Wang    C      2022/7/26
 */
public class ValidationSorts {

    /**
     * @Description 按照Sort A1、A2、A3、A4  B1、B2、B3、B4.....的顺序对参数进行校验
     *
     * @return
     **/
    @GroupSequence({
            SortA1.class,SortA2.class,SortA3.class,SortA4.class,
            SortB1.class,SortB2.class,SortB3.class,SortB4.class,
            SortC1.class,SortC2.class,SortC3.class,SortC4.class,
            SortD1.class,SortD2.class,SortD3.class,SortD4.class,
            SortE1.class,SortE2.class,SortE3.class,SortE4.class,
            SortF1.class,SortF2.class,SortF3.class,SortF4.class,
            SortG1.class,SortG2.class,SortG3.class,SortG4.class,
            SortH1.class,SortH2.class,SortH3.class,SortH4.class,
            SortI1.class,SortI2.class,SortI3.class,SortI4.class,
            SortJ1.class,SortJ2.class,SortJ3.class,SortJ4.class,
            SortK1.class,SortK2.class,SortK3.class,SortK4.class,
            SortL1.class,SortL2.class,SortL3.class,SortL4.class,
            SortM1.class,SortM2.class,SortM3.class,SortM4.class,
            SortN1.class,SortN2.class,SortN3.class,SortN4.class,
            SortO1.class,SortO2.class,SortO3.class,SortO4.class,
            SortP1.class,SortP2.class,SortP3.class,SortP4.class,
            SortT1.class,SortT2.class,SortT3.class,SortT4.class,
    })
    public interface Sort {}

    public interface SortA1 {}
    public interface SortA2 {}
    public interface SortA3 {}
    public interface SortA4 {}


    public interface SortB1 {}
    public interface SortB2 {}
    public interface SortB3 {}
    public interface SortB4 {}


    public interface SortC1 {}
    public interface SortC2 {}
    public interface SortC3 {}
    public interface SortC4 {}

    public interface SortD1 {}
    public interface SortD2 {}
    public interface SortD3 {}
    public interface SortD4 {}

    public interface SortE1 {}
    public interface SortE2 {}
    public interface SortE3 {}
    public interface SortE4 {}

    public interface SortF1 {}
    public interface SortF2 {}
    public interface SortF3 {}
    public interface SortF4{}

    public interface SortG1 {}
    public interface SortG2 {}
    public interface SortG3 {}
    public interface SortG4 {}

    public interface SortH1 {}
    public interface SortH2 {}
    public interface SortH3 {}
    public interface SortH4 {}

    public interface SortI1 {}
    public interface SortI2 {}
    public interface SortI3 {}
    public interface SortI4 {}

    public interface SortJ1 {}
    public interface SortJ2 {}
    public interface SortJ3 {}
    public interface SortJ4 {}

    public interface SortK1 {}
    public interface SortK2 {}
    public interface SortK3 {}
    public interface SortK4 {}

    public interface SortL1 {}
    public interface SortL2 {}
    public interface SortL3 {}
    public interface SortL4 {}

    public interface SortM1 {}
    public interface SortM2 {}
    public interface SortM3 {}
    public interface SortM4 {}

    public interface SortN1 {}
    public interface SortN2 {}
    public interface SortN3 {}
    public interface SortN4 {}

    public interface SortO1 {}
    public interface SortO2 {}
    public interface SortO3 {}
    public interface SortO4 {}

    public interface SortP1 {}
    public interface SortP2 {}
    public interface SortP3 {}
    public interface SortP4 {}

    public interface SortT1 {}
    public interface SortT2 {}
    public interface SortT3 {}
    public interface SortT4 {}

}

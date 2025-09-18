/**
 * @description DateConverter
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
package com.kaifangqian.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author : zhenghuihan
 * create at:  2021/4/8  7:26 PM
 */

@Component
public class DateConverter implements Converter<String, Date> {

    static List<SimpleDateFormat> formatter = new ArrayList();
    static{
        formatter.add(new SimpleDateFormat("yyyy-MM-dd"));
        formatter.add(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        formatter.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formatter.add(new SimpleDateFormat("yyyy/MM/dd"));

    }

    @Override
    public Date convert(String source) {
        try {
            if(Pattern.matches("\\d{4}-\\d{2}-\\d{2}",source)){
                return formatter.get(0).parse(source);
            }
            if(Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}",source)){
                return formatter.get(1).parse(source);
            }
            if(Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}",source)){
                return formatter.get(2).parse(source);
            }
            if(Pattern.matches("\\d{4}/\\d{2}/\\d{2}",source)){
                return formatter.get(2).parse(source);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/**
 * @description 时间操作工具类
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
package com.kaifangqian.modules.opensign.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description: 时间操作工具类
 * @Package: com.kaifangqian.modules.opensign.util
 * @ClassName: TimeUtils
 * @author: Fusion
 * CreateTime:  2023/8/22  16:53
 * @copyright 北京资源律动科技有限公司
 */
public class TimeUtils {

    /**
     * 获取当前时间的年
     * eg:2020
     * @return
     */
    public static String getYear() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        return now.format(dateTimeFormatter);
    }

    /**
     * 获取当前年和月
     * eg:20200317
     * @return
     */
    public static String getYearAndMonth() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        return now.format(dateTimeFormatter);
    }

    /**
     * 获取当前年和月ri
     * eg:20200317
     * @return
     */
    public static String getYearToDay() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(dateTimeFormatter);
    }

    /**
     * 获取UTC时间 格式为 yyyyMMddHH  2019112909
     * @return
     */
    public static String getUTC() {
        return OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
    }


    public static String getYearToMinute() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return now.format(dateTimeFormatter);
    }

    public static String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        System.out.println(LocalDateTime.now().format(formatter));
        return LocalDateTime.now().format(formatter);
    }

        public static String getYearToHou() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        return now.format(dateTimeFormatter);
    }

    /**
     * 获取下一年开始的时间
     * @return
     */
    public static Date getNextYearBeginDate() {
        LocalDateTime nextYearDate = LocalDateTime.now().plusYears(1);
        int year = nextYearDate.getYear();
        LocalDateTime newYearDateTime = LocalDateTime.parse(year+"-01-01T00:00:00");
        return Date.from(newYearDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取下月开始的时间
     * @return
     */
    public static Date getNextMonthBeginDate() {
        LocalDateTime nextMonthDate = LocalDateTime.now().plusMonths(1);
        int year = nextMonthDate.getYear();
        String month = String.valueOf(nextMonthDate.getMonthValue());
        if (month.length() < 2) {
            month = "0"+month;
        }
        LocalDateTime newYearDateTime = LocalDateTime.parse(year+"-"+month+"-01T00:00:00");
        return Date.from(newYearDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间的前几天
     * @param day
     * @return
     */
    public static Date getToDayBefore(int day) {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(day);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取明天的当前时间
     * @return
     */
    public static Date getOneDayAfter() {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取三分钟以后的时间
     * @return
     */
    public static Date getThreeMinuteAfter() {
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(3);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}

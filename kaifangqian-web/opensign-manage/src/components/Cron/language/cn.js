/*
 * @description 开放签
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
 * 必须公开修改后的完整源码（包括衍生作品），详见协议全文。
 */

export default {
    Seconds:{
        name:'秒',
        every:'每一秒钟',
        interval:['每隔','秒执行 从','秒开始'],
        specific:'具体秒数',
        cycle:['周期从','到','秒']
    },
    Minutes:{
        name:'分',
        every:'每一分钟',
        interval:['每隔','分执行 从','分开始'],
        specific:'具体分钟数',
        cycle:['周期从','到','分']
    },
    Hours:{
        name:'时',
        every:'每一小时',
        interval:['每隔','小时执行 从','小时开始'],
        specific:'具体小时数',
        cycle:['周期从','到','小时']
    },
    Day:{
        name:'天',
        every:'每一天',
        intervalWeek:['每隔','周执行 从','开始'],
        intervalDay:['每隔','天执行 从','天开始'],
        specificWeek:'具体星期几',
        specificDay:'具体天数',
        lastDay:'在这个月的最后一天',
        lastWeekday:'在这个月的最后一个工作日',
        lastWeek:['在这个月的最后一个'],
        beforeEndMonth:['在本月底前','天'],
        nearestWeekday:['最近的工作日（周一至周五）至本月','日'],
        someWeekday:['在这个月的第','个'],
    },
    Week:['天','一','二','三','四','五','六'].map(val=>'星期'+val),
    Month:{
        name:'月',
        every:'每一月',
        interval:['每隔','月执行 从','月开始'],
        specific:'具体月数',
        cycle:['从','到','月之间的每个月']
    },
    Year:{
        name:'年',
        every:'每一年',
        interval:['每隔','年执行 从','年开始'],
        specific:'具体年份',
        cycle:['从','到','年之间的每一年']
    },
    Save:'生成'
}
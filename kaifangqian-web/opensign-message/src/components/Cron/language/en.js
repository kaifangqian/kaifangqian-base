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
        name:'Seconds',
        every:'Every second',
        interval:['Every','second(s) starting at second'],
        specific:'Specific second (choose one or many)',
        cycle:['Every second between second','and second']
    },
    Minutes:{
        name:'Minutes',
        every:'Every minute',
        interval:['Every','minute(s) starting at minute'],
        specific:'Specific minute (choose one or many)',
        cycle:['Every minute between minute','and minute']
    },
    Hours:{
        name:'Hours',
        every:'Every hour',
        interval:['Every','hour(s) starting at hour'],
        specific:'Specific hour (choose one or many)',
        cycle:['Every hour between hour','and hour']
    },
    Day:{
        name:'Day',
        every:'Every day',
        intervalWeek:['Every','day(s) starting on'],
        intervalDay:['Every','day(s) starting at the','of the month'],
        specificWeek:'Specific day of week (choose one or many)',
        specificDay:'Specific day of month (choose one or many)',
        lastDay:'On the last day of the month',
        lastWeekday:'On the last weekday of the month',
        lastWeek:['On the last',' of the month'],
        beforeEndMonth:['day(s) before the end of the month'],
        nearestWeekday:['Nearest weekday (Monday to Friday) to the','of the month'],
        someWeekday:['On the','of the month'],
    },
    Week:['Sunday','Monday','Tuesday','Wednesday','Thursday','Friday','Saturday'],
    Month:{
        name:'Month',
        every:'Every month',
        interval:['Every','month(s) starting in'],
        specific:'Specific month (choose one or many)',
        cycle:['Every month between','and']
    },
    Year:{
        name:'Year',
        every:'Any year',
        interval:['Every','year(s) starting in'],
        specific:'Specific year (choose one or many)',
        cycle:['Every year between','and']
    },
    Save:'Generate',
}
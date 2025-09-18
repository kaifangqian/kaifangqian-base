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
        name:'Segundos',
        every:'A cada segundo',
        interval:['A cada','segundo(s) começando no segundo'],
        specific:'Segundo específico (escolha um ou muitos)',
        cycle:['A Cada segundo entre segundos','e segundo']
    },
    Minutes:{
        name:'Minutos',
        every:'A cada minuto',
        interval:['A cada','minuto(s) começando no minuto'],
        specific:'Minuto específico (escolha um ou muitos)',
        cycle:['A cada minuto entre minutos','e minutos']
    },
    Hours:{
        name:'Horas',
        every:'A cada hora',
        interval:['A cada','hora(s) começando na hora'],
        specific:'Hora específica (escolha uma ou muitas)',
        cycle:['A cada hora entre horas','e horas']
    },
    Day:{
        name:'Dia',
        every:'A cada dia',
        intervalWeek:['A cada','dia(s) começando em'],
        intervalDay:['A cada','dia(s) começando no','do mês'],
        specificWeek:'Dia específico da semana (escolha um ou vários)',
        specificDay:'Dia específico do mês (escolha um ou vários)',
        lastDay:'No último dia do mês',
        lastWeekday:'No último dia da semana do mês',
        lastWeek:['No último',' do mês'],
        beforeEndMonth:['dia(s) antes do final do mês'],
        nearestWeekday:['Dia da semana mais próximo (segunda a sexta) ao ','do mês'],
        someWeekday:['No','do mês'],
    },
    Week:['Domingo','Segunda-feira','Terça-feira','Quarta-feira','Quinta-feira','Sexta-feira','Sábado'],
    Month:{
        name:'Mês',
        every:'A cada mês',
        interval:['A cada','mês(es) começando em'],
        specific:'Mês específico (escolha um ou muitos)',
        cycle:['Todo mês entre','e']
    },
    Year:{
        name:'Ano',
        every:'Qualquer ano',
        interval:['A cada','ano(s) começando em'],
        specific:'Ano específico (escolha um ou muitos)',
        cycle:['Todo ano entre','e']
    },
    Save:'Salvar',
    Close:'Fechar'
}
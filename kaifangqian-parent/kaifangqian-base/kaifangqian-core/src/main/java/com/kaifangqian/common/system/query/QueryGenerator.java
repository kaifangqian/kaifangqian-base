/**
 * @description 查询构建器
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
package com.kaifangqian.common.system.query;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaifangqian.common.system.util.PaasDataAutorUtils;
import com.kaifangqian.common.system.util.JwtUtil;
import com.kaifangqian.common.system.vo.SysPermissionDataRuleModel;
import com.kaifangqian.common.util.SqlInjectionUtil;
import com.kaifangqian.common.util.oConvertUtils;
import com.kaifangqian.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import org.springframework.util.NumberUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * @author : zhenghuihan
 * create at: 2023/12/18
 */
@Slf4j
public class QueryGenerator {

    //实体类字段查询
    /**
     * 模糊查询，左模糊：*key 右模糊：key* 全模糊：*key*
     */
    private static final String STAR = "*";

    /**
     * in查询 逗号分隔
     */
    private static final String COMMA = ",";

    /**
     * 不等于：!= key
     */
    private static final String NOT_EQUAL = "!=";


    //实体类字段结束


    //扩展字段查询开始
    /**
     * 区间查询，开始、结束标识
     */
    private static final String BEGIN = "_begin";
    private static final String END = "_end";

    /**
     * in查询：数字类型字段，拼接此后缀，接受多值参数，用英文逗号","分隔
     */
    private static final String MULTI = "_MultiString";

    /**
     * 排序列
     */
    private static final String ORDER_COLUMN = "orderColumn";
    /**
     * 排序方式
     */
    private static final String ORDER_TYPE = "orderType";

    /**
     * 排序方式-正序
     */
    private static final String ORDER_TYPE_ASC = "ASC";

    /**
     * 排序方式-倒序
     */
    private static final String ORDER_TYPE_DESC = "DESC";

    //扩展字段查询结束

    /**
     * 页面带有规则值查询，空格作为分隔符
     */
    private static final String QUERY_SEPARATE_KEYWORD = " ";

    /**
     * 数据权限-自定义sql
     */
    public static final String SQL_RULES_COLUMN = "SQL_RULES_COLUMN";


    /**
     * 时间格式化
     */
    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<SimpleDateFormat>();

    private static SimpleDateFormat getTime() {
        SimpleDateFormat time = local.get();
        if (time == null) {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            local.set(time);
        }
        return time;
    }

    /**
     * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
     *
     * @param searchObj    查询实体
     * @param parameterMap request.getParameterMap()
     * @return QueryWrapper实例
     */
    public static <T> QueryWrapper<T> initQueryWrapper(T searchObj, Map<String, String[]> parameterMap) {
        long start = System.currentTimeMillis();
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        installMplus(queryWrapper, searchObj, parameterMap);
        log.debug("---查询条件构造器初始化完成,耗时:" + (System.currentTimeMillis() - start) + "毫秒----");
        return queryWrapper;
    }

    /**
     * 组装Mybatis Plus 查询条件
     * <p>使用此方法 需要有如下几点注意:
     * <br>1.使用QueryWrapper 而非LambdaQueryWrapper;
     * <br>2.实例化QueryWrapper时不可将实体传入参数
     * <br>3.也可以不使用这个方法直接调用 {@link #initQueryWrapper}直接获取实例
     */
    private static void installMplus(QueryWrapper<?> queryWrapper, Object searchObj, Map<String, String[]> parameterMap) {

        //数据权限组装  区间条件组装 模糊查询 高级查询组装 简单排序
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);

        //数据权限组装
        //1、权限组之间or
        //2、权限组中的权限策略之间and
        //3、权限策略中的权限分组之间or
        //4、权限策略中的权限分组内and
        List<List<List<Map<String, List<SysPermissionDataRuleModel>>>>> ruleMapNew = getRuleMapNew();

        //权限组循环
        if (CollUtil.isNotEmpty(ruleMapNew)) {
            queryWrapper.nested(query -> {
                for (int i = 0; i < ruleMapNew.size(); i++) {
                    List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList = ruleMapNew.get(i);
                    if (CollUtil.isNotEmpty(groupRuleMapList)) {
                        if (i == 0) {
                            //第一个权限组
                            query.nested(wrapper -> {
                                groupComp(wrapper, searchObj, origDescriptors, groupRuleMapList);
                            });
                        } else {
                            //第一个之后的权限组
                            query.or(wrapper -> {
                                groupComp(wrapper, searchObj, origDescriptors, groupRuleMapList);
                            });
                        }
                    }
                }
            });
        }

        //区间查询 模糊查询 高级查询组装 简单排序
        String name, type, column;
        for (int i = 0; i < origDescriptors.length; i++) {
            name = origDescriptors[i].getName();
            type = origDescriptors[i].getPropertyType().toString();
            try {
                //过滤特殊字段、检验可读性，判断是否继续处理
                if (judgedIsUselessField(name) || !PropertyUtils.isReadable(searchObj, name)) {
                    continue;
                }

                Object value = PropertyUtils.getSimpleProperty(searchObj, name);
                column = getTableFieldName(searchObj.getClass(), name);

                //如果字段加注解了@TableField(exist = false),不走DB查询
                if (column == null) {
                    continue;
                }

                //扩展字段查询：区间查询+多值查询
                doIntervalQuery(queryWrapper, parameterMap, type, name, column);

                //实体字段查询
                if (null != value) {
                    //根据参数值带什么关键字符串判断走什么类型的查询
                    QueryRuleEnum rule = convert2Rule(value);
                    if (rule != null) {
                        value = replaceValue(rule, value);
                        if (value != null) {
                            addEasyQuery(queryWrapper, column, rule, value);
                        }
                    }
                }

                //过滤delete_flag = 1（已删除）
                if ("deleteFlag".equals(name)) {
                    addEasyQuery(queryWrapper, column, QueryRuleEnum.EQ, 0);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        //简单排序处理-单字段排序
        doMultiFieldsOrder(queryWrapper, parameterMap);
    }

    private static void groupComp(QueryWrapper<?> queryWrapper, Object searchObj, PropertyDescriptor[] origDescriptors, List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList) {
        for (int j = 0; j < groupRuleMapList.size(); j++) {
            List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList = groupRuleMapList.get(j);
            //第一个权限组的第一个权限
            if (CollUtil.isNotEmpty(dataRuleMapList)) {
                if (j == 0) {
                    queryWrapper.nested(wrapper -> {
                        groupDataComp(wrapper, searchObj, origDescriptors, dataRuleMapList);
                    });
                } else {
                    queryWrapper.and(wrapper -> {
                        //第一个权限组的第一个之后权限
                        groupDataComp(wrapper, searchObj, origDescriptors, dataRuleMapList);
                    });
                }
            }
        }
    }

    private static void groupDataComp(QueryWrapper<?> queryWrapper, Object searchObj, PropertyDescriptor[] origDescriptors, List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList) {
        for (int k = 0; k < dataRuleMapList.size(); k++) {
            Map<String, List<SysPermissionDataRuleModel>> ruleCollumMap = dataRuleMapList.get(k);
            //第一个权限的第一组权限
            if (k == 0) {
                queryWrapper.nested(wrapper -> {
                    dataQuery(searchObj, origDescriptors, ruleCollumMap, wrapper);
                });
            } else {
                queryWrapper.or(wrapper -> {
                    dataQuery(searchObj, origDescriptors, ruleCollumMap, wrapper);
                });
            }
        }
    }

    private static void dataQuery(Object searchObj, PropertyDescriptor[] origDescriptors, Map<String, List<SysPermissionDataRuleModel>> ruleNewMap, QueryWrapper<?> wrapper) {
        for (String c : ruleNewMap.keySet()) {
            //权限规则自定义SQL表达式
            if (oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                ruleNewMap.get(c).forEach(rule -> {
                    wrapper.and(t -> t.apply(getSqlRuleValue(rule.getRuleValue())));
                });
            }
        }

        for (int j = 0; j < origDescriptors.length; j++) {
            String name = origDescriptors[j].getName();
            if (judgedIsUselessField(name) || !PropertyUtils.isReadable(searchObj, name)) {
                continue;
            }
            String column = getTableFieldName(searchObj.getClass(), name);
            if (column == null) {
                //添加了注解@TableField(exist = false) 后续都不用处理了
                continue;
            }
            if (ruleNewMap.containsKey(name)) {
                for (SysPermissionDataRuleModel rule : ruleNewMap.get(name)) {
                    addRuleToQueryWrapper(rule, column, origDescriptors[j].getPropertyType(), wrapper);
                }
            }
        }
    }


    /**
     * 区间查询+多值查询（in）
     *
     * @param queryWrapper query对象
     * @param parameterMap 参数map
     * @param type         字段类型
     * @param filedName    字段名称
     * @param columnName   列名称
     */
    private static void doIntervalQuery(QueryWrapper<?> queryWrapper, Map<String, String[]> parameterMap, String type, String filedName, String columnName) throws ParseException {
        // 添加 判断是否有区间值
        String endValue = null, beginValue = null;
        if (parameterMap != null && parameterMap.containsKey(filedName + BEGIN)) {
            beginValue = parameterMap.get(filedName + BEGIN)[0].trim();
            addQueryByRule(queryWrapper, columnName, type, beginValue, QueryRuleEnum.GE);

        }
        if (parameterMap != null && parameterMap.containsKey(filedName + END)) {
            endValue = parameterMap.get(filedName + END)[0].trim();
            addQueryByRule(queryWrapper, columnName, type, endValue, QueryRuleEnum.LE);
        }
        //多值查询
        if (parameterMap != null && parameterMap.containsKey(filedName + MULTI)) {
            endValue = parameterMap.get(filedName + MULTI)[0].trim();
            addQueryByRule(queryWrapper, columnName.replace(MULTI, ""), type, endValue, QueryRuleEnum.IN);
        }
    }

    //单字段排序
    private static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper, Map<String, String[]> parameterMap) {
        String column = null, order = null;
        if (parameterMap != null && parameterMap.containsKey(ORDER_COLUMN)) {
            column = parameterMap.get(ORDER_COLUMN)[0];
        }
        if (parameterMap != null && parameterMap.containsKey(ORDER_TYPE)) {
            order = parameterMap.get(ORDER_TYPE)[0];
        }
        log.info("排序规则>>列:" + column + ",排序方式:" + order);
        if (oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
            //SQL注入check
            SqlInjectionUtil.filterContent(column);
            String columnStr = oConvertUtils.camelToUnderline(column);
            if (order.toUpperCase().equals(ORDER_TYPE_ASC)) {
                queryWrapper.orderByAsc(columnStr);
            } else if (order.toUpperCase().equals(ORDER_TYPE_DESC)) {
                queryWrapper.orderByDesc(columnStr);
            }
        }
    }

    /**
     * 根据所传的值 转化成对应的比较方式
     * 支持><= like in !
     *
     * @param value
     * @return
     */
    public static QueryRuleEnum convert2Rule(Object value) {
        if (!(value instanceof String)) {
            return null;
        }

        String val = value.toString().trim();
        if (val.length() == 0) {
            return null;
        }

        // step 1 .>= =< !=
        if (val.length() >= 3) {
            if (QUERY_SEPARATE_KEYWORD.equals(val.substring(2, 3))) {
                return QueryRuleEnum.getByValue(val.substring(0, 2));
            }
        }
        // step 2 .> <
        if (val.length() >= 2) {
            if (QUERY_SEPARATE_KEYWORD.equals(val.substring(1, 2))) {
                return QueryRuleEnum.getByValue(val.substring(0, 1));
            }
        }
        // step 3 like
        if (val.contains(STAR)) {
            if (val.startsWith(STAR) && val.endsWith(STAR)) {
                return QueryRuleEnum.LIKE;
            } else if (val.startsWith(STAR)) {
                return QueryRuleEnum.LEFT_LIKE;
            } else if (val.endsWith(STAR)) {
                return QueryRuleEnum.RIGHT_LIKE;
            }
        }
        // step 4 in
        if (val.contains(COMMA)) {
            return QueryRuleEnum.IN;
        }

        return QueryRuleEnum.EQ;
    }

    /**
     * 替换掉关键字字符
     *
     * @param rule
     * @param value
     * @return
     */
    private static Object replaceValue(QueryRuleEnum rule, Object value) {
        if (!(value instanceof String)) {
            return null;
        }
        String val = value.toString().trim();
        if (rule == QueryRuleEnum.LIKE) {
            value = val.substring(1, val.length() - 1);
        } else if (rule == QueryRuleEnum.LEFT_LIKE || rule == QueryRuleEnum.NE) {
            value = val.substring(1);
        } else if (rule == QueryRuleEnum.RIGHT_LIKE) {
            value = val.substring(0, val.length() - 1);
        } else if (rule == QueryRuleEnum.IN) {
            value = val.split(",");
        } else if (rule == QueryRuleEnum.EQ) {
            value = val;
        } else {
            if (val.startsWith(rule.getValue() + QUERY_SEPARATE_KEYWORD)) {
                value = val.replaceFirst(rule.getValue() + QUERY_SEPARATE_KEYWORD, "").trim();
            }
        }
        return value;
    }

    private static void addQueryByRule(QueryWrapper<?> queryWrapper, String name, String type, String value, QueryRuleEnum rule) throws ParseException {
        if (oConvertUtils.isNotEmpty(value)) {
            Object temp;
            // 针对数字类型字段，多值查询
            if (value.indexOf(COMMA) != -1) {
                temp = value;
                addEasyQuery(queryWrapper, name, rule, temp);
                return;
            }

            switch (type) {
                case "class java.lang.Integer":
                    temp = Integer.parseInt(value);
                    break;
                case "class java.math.BigDecimal":
                    temp = new BigDecimal(value);
                    break;
                case "class java.lang.Short":
                    temp = Short.parseShort(value);
                    break;
                case "class java.lang.Long":
                    temp = Long.parseLong(value);
                    break;
                case "class java.lang.Float":
                    temp = Float.parseFloat(value);
                    break;
                case "class java.lang.Double":
                    temp = Double.parseDouble(value);
                    break;
                case "class java.util.Date":
                    temp = getDateQueryByRule(value, rule);
                    break;
                default:
                    temp = value;
                    break;
            }
            addEasyQuery(queryWrapper, name, rule, temp);
        }
    }

    /**
     * 获取日期类型的值
     *
     * @param value
     * @param rule
     * @return
     * @throws ParseException
     */
    private static Date getDateQueryByRule(String value, QueryRuleEnum rule) throws ParseException {
        Date date = null;
        if (value.length() == 10) {
            if (rule == QueryRuleEnum.GE) {
                //比较大于
                date = getTime().parse(value + " 00:00:00");
            } else if (rule == QueryRuleEnum.LE) {
                //比较小于
                date = getTime().parse(value + " 23:59:59");
            }
        }
        if (date == null) {
            date = getTime().parse(value);
        }
        return date;
    }

    /**
     * 根据规则走不同的查询
     *
     * @param queryWrapper QueryWrapper
     * @param name         字段名字
     * @param rule         查询规则
     * @param value        查询条件值
     */
    private static void addEasyQuery(QueryWrapper<?> queryWrapper, String name, QueryRuleEnum rule, Object value) {
        if (value == null || rule == null || oConvertUtils.isEmpty(value)) {
            return;
        }
        name = oConvertUtils.camelToUnderline(name);
        log.info("--查询规则-->" + name + " " + rule.getValue() + " " + value);
        switch (rule) {
            case GT:
                queryWrapper.gt(name, value);
                break;
            case GE:
                queryWrapper.ge(name, value);
                break;
            case LT:
                queryWrapper.lt(name, value);
                break;
            case LE:
                queryWrapper.le(name, value);
                break;
            case EQ:
                queryWrapper.eq(name, value);
                break;
            case NE:
                queryWrapper.ne(name, value);
                break;
            case IN:
                if (value instanceof String) {
                    queryWrapper.in(name, (Object[]) value.toString().split(","));
                } else if (value instanceof String[]) {
                    queryWrapper.in(name, (Object[]) value);
                } else if (value.getClass().isArray()) {
                    queryWrapper.in(name, (Object[]) value);
                } else {
                    queryWrapper.in(name, value);
                }
                break;
            case LIKE:
                queryWrapper.like(name, value);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(name, value);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(name, value);
                break;
            default:
                log.info("--查询规则未匹配到---");
                break;
        }
    }

    /**
     * @param name
     * @return
     */
    private static boolean judgedIsUselessField(String name) {
        return "class".equals(name) || "Ids".equals(name)
                || "page".equals(name) || "rows".equals(name)
                || "sort".equals(name) || "order".equals(name);
    }

    /**
     * 获取请求对应的数据权限规则--修复相同列权限多个存在的问题
     *
     * @return
     */
    public static List<List<List<Map<String, List<SysPermissionDataRuleModel>>>>> getRuleMapNew() {
        List<List<List<Map<String, List<SysPermissionDataRuleModel>>>>> ruleMapList = new ArrayList<>();
        List<List<List<List<SysPermissionDataRuleModel>>>> list = PaasDataAutorUtils.loadDataSearchConditon();
        if (list != null && list.size() > 0) {
            if (list.get(0) == null) {
                return ruleMapList;
            }
            //权限组
            for (List<List<List<SysPermissionDataRuleModel>>> groupRuleList : list) {
                //权限策略
                if (CollUtil.isNotEmpty(groupRuleList)) {
                    List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList = new ArrayList();
                    for (List<List<SysPermissionDataRuleModel>> dataRuleList : groupRuleList) {
                        //策略分组
                        if (CollUtil.isNotEmpty(dataRuleList)) {
                            List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList = new ArrayList();
                            for (List<SysPermissionDataRuleModel> ruleList : dataRuleList) {
                                if (CollUtil.isNotEmpty(ruleList)) {
                                    Map<String, List<SysPermissionDataRuleModel>> map = new HashMap<>();
                                    for (SysPermissionDataRuleModel rule : ruleList) {
                                        String column = rule.getRuleColumn();
                                        if (QueryRuleEnum.SQL_RULES.getValue().equals(rule.getRuleConditions())) {
                                            column = SQL_RULES_COLUMN + rule.getId();
                                        }
                                        if (map.get(column) != null) {
                                            map.get(column).add(rule);
                                        } else {
                                            List rules = new ArrayList();
                                            rules.add(rule);
                                            map.put(column, rules);
                                        }
                                    }
                                    dataRuleMapList.add(map);
                                }
                            }
                            groupRuleMapList.add(dataRuleMapList);
                        }
                    }
                    ruleMapList.add(groupRuleMapList);
                }
            }
        }
        return ruleMapList;
    }

    private static void addRuleToQueryWrapper(SysPermissionDataRuleModel dataRule, String name, Class propertyType, QueryWrapper<?> queryWrapper) {
        QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
        if (rule.equals(QueryRuleEnum.IN) && !propertyType.equals(String.class)) {
            String[] values = dataRule.getRuleValue().split(",");
            Object[] objs = new Object[values.length];
            for (int i = 0; i < values.length; i++) {
                objs[i] = NumberUtils.parseNumber(values[i], propertyType);
            }
            addEasyQuery(queryWrapper, name, rule, objs);
        } else {
            if (propertyType.equals(String.class)) {
                addEasyQuery(queryWrapper, name, rule, converRuleValue(dataRule.getRuleValue()));
            } else if (propertyType.equals(Date.class)) {
                String dateStr = converRuleValue(dataRule.getRuleValue());
                if (dateStr.length() == 10) {
                    addEasyQuery(queryWrapper, name, rule, DateUtil.str2Date(dateStr, DateUtil.date_sdf.get()));
                } else {
                    addEasyQuery(queryWrapper, name, rule, DateUtil.str2Date(dateStr, DateUtil.datetimeFormat.get()));
                }
            } else {
                addEasyQuery(queryWrapper, name, rule, NumberUtils.parseNumber(dataRule.getRuleValue(), propertyType));
            }
        }
    }

    public static String converRuleValue(String ruleValue) {
        String value = JwtUtil.getUserSystemData(ruleValue);
        return value != null ? value : ruleValue;
    }


    public static String getSqlRuleValue(String sqlRule) {
        try {
            Set<String> varParams = getSqlRuleParams(sqlRule);
            for (String var : varParams) {
                String tempValue = converRuleValue(var);
                sqlRule = sqlRule.replace("#{" + var + "}", tempValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sqlRule;
    }

    /**
     * 获取sql中的#{key} 这个key组成的set
     */
    public static Set<String> getSqlRuleParams(String sql) {
        if (oConvertUtils.isEmpty(sql)) {
            return null;
        }
        Set<String> varParams = new HashSet<String>();
        String regex = "\\#\\{\\w+\\}";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sql);
        while (m.find()) {
            String var = m.group();
            varParams.add(var.substring(var.indexOf("{") + 1, var.indexOf("}")));
        }
        return varParams;
    }

    /**
     * 获取单个查询条件的值
     *
     * @param rule
     * @param field
     * @param value
     * @param isString
     * @return
     */
    private static String getSingleSqlByRule(QueryRuleEnum rule, String field, Object value, boolean isString) {
        String res = "";
        switch (rule) {
            case GT:
                res = field + rule.getValue() + getFieldConditionValue(value, isString);
                break;
            case GE:
                res = field + rule.getValue() + getFieldConditionValue(value, isString);
                break;
            case LT:
                res = field + rule.getValue() + getFieldConditionValue(value, isString);
                break;
            case LE:
                res = field + rule.getValue() + getFieldConditionValue(value, isString);
                break;
            case EQ:
                res = field + rule.getValue() + getFieldConditionValue(value, isString);
                break;
            case NE:
                res = field + " <> " + getFieldConditionValue(value, isString);
                break;
            case IN:
                res = field + " in " + getInConditionValue(value, isString);
                break;
            case LIKE:
                res = field + " like " + getLikeConditionValue(value);
                break;
            case LEFT_LIKE:
                res = field + " like " + getLikeConditionValue(value);
                break;
            case RIGHT_LIKE:
                res = field + " like " + getLikeConditionValue(value);
                break;
            default:
                res = field + " = " + getFieldConditionValue(value, isString);
                break;
        }
        return res;
    }

    /**
     * 获取查询条件的值
     *
     * @param value
     * @param isString
     * @return
     */
    private static String getFieldConditionValue(Object value, boolean isString) {
        String str = value.toString().trim();
        if (isString) {
            return " '" + str + "' ";
        } else {
            return value.toString();
        }
    }

    private static String getInConditionValue(Object value, boolean isString) {
        String[] temp = value.toString().split(",");
        if (temp.length == 0) {
            return "('')";
        }
        if (isString) {
            List<String> res = new ArrayList<>();
            for (String string : temp) {
                res.add("'" + string + "'");
            }
            return "(" + String.join(",", res) + ")";
        } else {
            return "(" + value.toString() + ")";
        }
    }

    private static String getLikeConditionValue(Object value) {
        String str = value.toString().trim();
        if (str.startsWith("*") && str.endsWith("*")) {
            return "'%" + str.substring(1, str.length() - 1) + "%'";
        } else if (str.startsWith("*")) {
            return "'%" + str.substring(1) + "'";
        } else if (str.endsWith("*")) {
            return "'" + str.substring(0, str.length() - 1) + "%'";
        } else {
            if (str.indexOf("%") >= 0) {
                if (str.startsWith("'") && str.endsWith("'")) {
                    return str;
                } else {
                    return "'" + str + "'";
                }
            } else {
                return "'%" + str + "%'";
            }
        }
    }

    private static void dataQueryAuthJdbc(StringBuffer sb, PropertyDescriptor[] origDescriptors, Map<String, List<SysPermissionDataRuleModel>> ruleMap, Class<?> clazz) {

        Boolean andFlag = false;
        String sql_and = " and ";
        for (String c : ruleMap.keySet()) {
            //权限查询
            if (oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                for (SysPermissionDataRuleModel rule : ruleMap.get(c)) {
                    if (andFlag) {
                        sb.append(sql_and);
                    }
                    sb.append(getSqlRuleValue(rule.getRuleValue()));
                    andFlag = true;
                }
            }
        }

        for (int j = 0; j < origDescriptors.length; j++) {
            String name = origDescriptors[j].getName();
            if (judgedIsUselessField(name)) {
                continue;
            }
            if (ruleMap.containsKey(name)) {
                String column = getTableFieldName(clazz, name);
                if (column == null) {
                    //添加了注解@TableField(exist = false) 后续都不用处理了
                    continue;
                }
                for (SysPermissionDataRuleModel dataRule : ruleMap.get(name)) {
                    QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
                    Class propType = origDescriptors[j].getPropertyType();
                    boolean isString = propType.equals(String.class);
                    Object value;
                    if (isString) {
                        value = converRuleValue(dataRule.getRuleValue());
                    } else {
                        value = NumberUtils.parseNumber(dataRule.getRuleValue(), propType);
                    }
                    String filedSql = getSingleSqlByRule(rule, oConvertUtils.camelToUnderline(column), value, isString);
                    if (andFlag) {
                        sb.append(sql_and);
                    }
                    sb.append(filedSql);
                    andFlag = true;
                }
            }
        }

        sb.append(" )");
    }

    /**
     * 根据权限相关配置生成相关的SQL 语句
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String installAuthJdbc(Class<?> clazz) {
        StringBuffer sb = new StringBuffer();
        List<List<List<Map<String, List<SysPermissionDataRuleModel>>>>> ruleMapNew = getRuleMapNew();
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(clazz);

        //数据权限修改优化
        if (CollUtil.isNotEmpty(ruleMapNew)) {
            sb.append(" ( ");
            for (int i = 0; i < ruleMapNew.size(); i++) {
                List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList = ruleMapNew.get(i);
                if (CollUtil.isNotEmpty(groupRuleMapList)) {
                    if (i == 0) {
                        //第一个权限组
                        sb.append(" ( ");
                        groupCompAuthJdbc(sb, origDescriptors, groupRuleMapList, clazz);
                        sb.append(" ) ");
                    } else {
                        //第一个之后的权限组
                        sb.append(" or ( ");
                        groupCompAuthJdbc(sb, origDescriptors, groupRuleMapList, clazz);
                        sb.append(" ) ");
                    }
                }
            }
            sb.append(" ) ");
        }
        log.info("query auth sql is:" + sb.toString());
        return sb.toString();
    }

    private static void groupCompAuthJdbc(StringBuffer sb, PropertyDescriptor[] origDescriptors, List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList, Class<?> clazz) {
        for (int j = 0; j < groupRuleMapList.size(); j++) {
            List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList = groupRuleMapList.get(j);
            //第一个权限组的第一个权限
            if (CollUtil.isNotEmpty(dataRuleMapList)) {
                if (j == 0) {
                    sb.append(" ( ");
                    groupDataCompAuthJdbc(sb, origDescriptors, dataRuleMapList, clazz);
                    sb.append(" ) ");
                } else {
                    //第一个权限组的第一个之后权限
                    sb.append(" and ( ");
                    groupDataCompAuthJdbc(sb, origDescriptors, dataRuleMapList, clazz);
                    sb.append(" ) ");
                }
            }
        }
    }


    private static void groupDataCompAuthJdbc(StringBuffer sb, PropertyDescriptor[] origDescriptors, List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList, Class<?> clazz) {
        for (int k = 0; k < dataRuleMapList.size(); k++) {
            Map<String, List<SysPermissionDataRuleModel>> ruleCollumMap = dataRuleMapList.get(k);
            //第一个权限的第一组权限
            if (k == 0) {
                sb.append(" ( ");
                dataQueryAuthJdbc(sb, origDescriptors, ruleCollumMap, clazz);
            } else {
                sb.append(" or ( ");
                dataQueryAuthJdbc(sb, origDescriptors, ruleCollumMap, clazz);
            }
        }
    }

    private static void dataQueryAuthMplus(Class<?> clazz, PropertyDescriptor[] origDescriptors, Map<String, List<SysPermissionDataRuleModel>> ruleNewMap, QueryWrapper<?> wrapper) {
        for (String c : ruleNewMap.keySet()) {
            //权限规则自定义SQL表达式
            if (oConvertUtils.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                ruleNewMap.get(c).forEach(rule -> {
                    wrapper.and(t -> t.apply(getSqlRuleValue(rule.getRuleValue())));
                });
            }
        }

        for (int j = 0; j < origDescriptors.length; j++) {
            String name = origDescriptors[j].getName();
            if (judgedIsUselessField(name)) {
                continue;
            }
            String column = getTableFieldName(clazz, name);
            if (column == null) {
                //添加了注解@TableField(exist = false) 后续都不用处理了
                continue;
            }
            if (ruleNewMap.containsKey(name)) {
                for (SysPermissionDataRuleModel rule : ruleNewMap.get(name)) {
                    addRuleToQueryWrapper(rule, column, origDescriptors[j].getPropertyType(), wrapper);
                }
            }
        }
    }

    /**
     * 根据权限相关配置 组装mp需要的权限
     *
     * @param queryWrapper
     * @param clazz
     * @return
     */
    public static void installAuthMplus(QueryWrapper<?> queryWrapper, Class<?> clazz) {
        //权限查询
        List<List<List<Map<String, List<SysPermissionDataRuleModel>>>>> ruleMapNew = getRuleMapNew();
        PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(clazz);

        if (CollUtil.isNotEmpty(ruleMapNew)) {
            queryWrapper.nested(query -> {
                for (int i = 0; i < ruleMapNew.size(); i++) {
                    List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList = ruleMapNew.get(i);
                    if (CollUtil.isNotEmpty(groupRuleMapList)) {
                        if (i == 0) {
                            //第一个权限组
                            query.nested(wrapper -> {
                                groupCompAuthMplus(wrapper, clazz, origDescriptors, groupRuleMapList);
                            });
                        } else {
                            //第一个之后的权限组
                            query.or(wrapper -> {
                                groupCompAuthMplus(wrapper, clazz, origDescriptors, groupRuleMapList);
                            });
                        }
                    }
                }
            });
        }
    }

    private static void groupCompAuthMplus(QueryWrapper<?> queryWrapper, Class<?> clazz, PropertyDescriptor[] origDescriptors, List<List<Map<String, List<SysPermissionDataRuleModel>>>> groupRuleMapList) {
        for (int j = 0; j < groupRuleMapList.size(); j++) {
            List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList = groupRuleMapList.get(j);
            //第一个权限组的第一个权限
            if (CollUtil.isNotEmpty(dataRuleMapList)) {
                if (j == 0) {
                    queryWrapper.nested(wrapper -> {
                        groupDataCompAuthMplus(wrapper, clazz, origDescriptors, dataRuleMapList);
                    });
                } else {
                    queryWrapper.and(wrapper -> {
                        //第一个权限组的第一个之后权限
                        groupDataCompAuthMplus(wrapper, clazz, origDescriptors, dataRuleMapList);
                    });
                }
            }
        }
    }

    private static void groupDataCompAuthMplus(QueryWrapper<?> queryWrapper, Class<?> clazz, PropertyDescriptor[] origDescriptors, List<Map<String, List<SysPermissionDataRuleModel>>> dataRuleMapList) {
        for (int k = 0; k < dataRuleMapList.size(); k++) {
            Map<String, List<SysPermissionDataRuleModel>> ruleCollumMap = dataRuleMapList.get(k);
            //第一个权限的第一组权限
            if (k == 0) {
                queryWrapper.nested(wrapper -> {
                    dataQueryAuthMplus(clazz, origDescriptors, ruleCollumMap, wrapper);
                });
            } else {
                queryWrapper.or(wrapper -> {
                    dataQueryAuthMplus(clazz, origDescriptors, ruleCollumMap, wrapper);
                });
            }
        }
    }

    /**
     * 获取class的 包括父类的
     *
     * @param clazz
     * @return
     */
    private static List<Field> getClassFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        Field[] fields;
        do {
            fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                list.add(fields[i]);
            }
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return list;
    }

    /**
     * 获取表字段名，@TableField(exist = false) 返回 null
     *
     * @param clazz
     * @param name
     * @return
     */
    private static String getTableFieldName(Class<?> clazz, String name) {
        try {
            //如果字段加注解了@TableField(exist = false),不走DB查询
            Field field = null;
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                //e.printStackTrace();
            }

            //如果为空，则去父类查找字段
            if (field == null) {
                List<Field> allFields = getClassFields(clazz);
                List<Field> searchFields = allFields.stream().filter(a -> a.getName().equals(name)).collect(Collectors.toList());
                if (searchFields != null && searchFields.size() > 0) {
                    field = searchFields.get(0);
                }
            }

            if (field != null) {
                TableField tableField = field.getAnnotation(TableField.class);
                if (tableField != null) {
                    if (tableField.exist() == false) {
                        //如果设置了TableField false 这个字段不需要处理
                        return null;
                    } else {
                        String column = tableField.value();
                        //如果设置了TableField value 这个字段是实体字段
                        if (!"".equals(column)) {
                            return column;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }
}

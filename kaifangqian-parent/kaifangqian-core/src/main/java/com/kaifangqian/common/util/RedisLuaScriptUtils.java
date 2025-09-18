/**
 * @description redis工具类
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
package com.kaifangqian.common.util;

import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhh
 * create at:  2022/9/5
 */

@Slf4j
@Component
public class RedisLuaScriptUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param key:redis的key值
     * @param limitCount:限制次数
     * @param limitPeriod:限制时间（秒）
     * @return true：可以继续操作 false：超限，不可以继续操作
     * @create by zhenghuihan
     * @createTime 2022/9/5 15:09
     * @description
     */
    public boolean checkRepeatLimit(String key, int limitCount, int limitPeriod) {
        if (checkRepeatVolid(key, limitCount, limitPeriod)) {
            String luaScript = buildLuaScript();
            RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
            List<String> limitKeys = new ArrayList<>();
            limitKeys.add(key);

            Long count = redisTemplate.execute(redisScript, limitKeys, limitCount, limitPeriod);
            if (null != count && count.intValue() <= limitCount) {
                return true;
            } else {
                //直接删除key值，通过数据库锁定
                redisTemplate.delete(key);
                return false;
            }
        }
        return false;
    }

    boolean checkRepeatVolid(String key, int limitCount, int limitPeriod) {
        if (MyStringUtils.isBlank(key) || limitCount == 0 || limitPeriod == 0) {
            return false;
        }
        return true;
    }

    /**
     * 脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
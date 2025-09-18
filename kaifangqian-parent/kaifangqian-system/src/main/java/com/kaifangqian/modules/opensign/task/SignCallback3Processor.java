/**
 * @description API接口签署回调服务
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
package com.kaifangqian.modules.opensign.task;

import cn.hutool.core.collection.CollUtil;
import com.kaifangqian.modules.api.entity.ApiCallback;
import com.kaifangqian.modules.api.service.IApiCallbackService;
import com.kaifangqian.modules.api.vo.request.ApiCallbackVO;
import com.kaifangqian.common.constant.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.log.OmsLogger;

import java.util.List;

/**
 * @author : zhenghuihan
 * create at:  2022/8/24  17:02
 * @description: 回调1任务
 */
@Component
@Slf4j
public class SignCallback3Processor implements tech.powerjob.worker.core.processor.sdk.BasicProcessor {

    @Autowired
    private IApiCallbackService apiCallbackService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger omsLogger = context.getOmsLogger();
        omsLogger.info("回调定时任务开始");
        List<ApiCallback> list = apiCallbackService.getByStatus(3);
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(l -> {
                ApiCallbackVO callbackVO = new ApiCallbackVO();
                BeanUtils.copyProperties(l, callbackVO);
                redisTemplate.opsForList().leftPush(ApiConstants.API_QUEUE_KEY, callbackVO);
            });
        }
        return new ProcessResult(true, "回调定时任务开始");
    }
}
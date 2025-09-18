/**
 * @description API回调服务类
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
package com.kaifangqian.modules.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kaifangqian.common.constant.ApiConstants;
import com.kaifangqian.modules.api.entity.ApiCallback;
import com.kaifangqian.modules.api.mapper.ApiCallbackMapper;
import com.kaifangqian.modules.api.service.IApiCallbackService;
import com.kaifangqian.modules.api.vo.request.ApiCallbackVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhenghuihan
 * @description 表
 * @createTime 2022/9/2 18:05
 */
@Service
public class ApiCallbackServiceImpl extends ServiceImpl<ApiCallbackMapper, ApiCallback> implements IApiCallbackService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @create by zhenghuihan
     * @createTime 2024/3/26 11:22
     * @description 新增消息
     */
    @Override
    public boolean addCallback(String url, String data) {
        //入库
        ApiCallback callback = new ApiCallback();
        callback.setCallbackUrl(url);
        callback.setReqPara(data);
        callback.setStatus(0);

        this.save(callback);
        ApiCallbackVO callbackVO = new ApiCallbackVO();
        BeanUtils.copyProperties(callback, callbackVO);
        //入队列
        redisTemplate.opsForList().leftPush(ApiConstants.API_QUEUE_KEY, callbackVO);
        return true;
    }

    @Override
    public List<ApiCallback> getByStatus(Integer status) {
        LambdaQueryWrapper<ApiCallback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCallback::getStatus, status);

        return list(queryWrapper);
    }
}

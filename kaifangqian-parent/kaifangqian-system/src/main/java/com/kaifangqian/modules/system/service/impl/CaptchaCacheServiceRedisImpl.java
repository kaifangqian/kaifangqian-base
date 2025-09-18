package com.kaifangqian.modules.system.service.impl;

import com.anji.captcha.service.CaptchaCacheService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

public class CaptchaCacheServiceRedisImpl implements CaptchaCacheService {
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String type() {
        return "redis";
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String key, String value, long expiresInSeconds) {
        redisTemplate.opsForValue().set(key, value, expiresInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Long increment(String key, long val) {
        return redisTemplate.opsForValue().increment(key, val);
    }

    public void setExpire(String key, long l) {
        redisTemplate.expire(key, l, TimeUnit.SECONDS);
    }

}

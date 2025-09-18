package com.kaifangqian.config.limit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author WWT
 * @discription 频繁操作配置类
 */
@Component
@ConfigurationProperties( prefix = "paas.limit")
public class LimitConfig {

    private Boolean enable;
    private String email;

    //触发次数
    private int triggerCount = 3;

    //5分钟
    private int timeScoped = 60 * 5 ;


    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
    }

    public int getTimeScoped() {
        return timeScoped;
    }

    public void setTimeScoped(int timeScoped) {
        this.timeScoped = timeScoped;
    }
}

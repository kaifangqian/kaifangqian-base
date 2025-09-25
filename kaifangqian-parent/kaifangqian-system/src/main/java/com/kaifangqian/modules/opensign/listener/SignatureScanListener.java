package com.kaifangqian.modules.opensign.listener;

import com.kaifangqian.common.redis.base.BaseMap;
import com.kaifangqian.common.redis.listener.RedisListener;
import com.kaifangqian.modules.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : zhenghuihan
 * create at:  2022/9/16  11:01
 * @description: 二维码手写签名扫描二维码后监听器
 */
@Component
public class SignatureScanListener implements RedisListener {
    @Autowired
    private WebSocket webSocket;

    @Override
    public void onMessage(BaseMap message) {
        String userId = message.get("userId");

        webSocket.pushScanQR(userId);
    }
}
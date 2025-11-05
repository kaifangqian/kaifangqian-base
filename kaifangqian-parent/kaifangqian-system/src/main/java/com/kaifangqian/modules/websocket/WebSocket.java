/**
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
package com.kaifangqian.modules.websocket;

import com.kaifangqian.modules.system.entity.SysAppInfo;
import com.kaifangqian.dto.SendMailInfoDto;
import com.kaifangqian.modules.system.service.ISysAppInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhh
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{userId}", decoders = {ServerDecoder.class}, encoders = {ServerEncoder.class})
public class WebSocket {

    private Session session;

    private String userId;

    @Autowired
    private ISysAppInfoService sysAppInfoService;

    private static Map<String, Session> sessionPool = new HashMap<String, Session>();


    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            sessionPool.put(userId, session);
            log.info("【websocket消息】有新的连接，总数为:" + sessionPool.size());
        } catch (Exception e) {
        }
    }

    @OnClose
    public void onClose() {
        try {
            sessionPool.remove(this.userId);
            log.info("【websocket消息】连接断开，总数为:" + sessionPool.size());
        } catch (Exception e) {
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
//        System.err.println("WebSocket error occurred: " + throwable.getMessage());
//        throwable.printStackTrace();
        // 在这里可以添加更多的错误处理逻辑，如记录日志、发送错误通知等
    }


    /**
     * 服务端推送消息
     *
     * @param userId
     * @param sendMailInfoDto
     */
    public boolean pushMessage(String userId, SendMailInfoDto sendMailInfoDto) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    SysAppInfo sysAppInfo = sysAppInfoService.getById(sendMailInfoDto.getAppId());
                    if (sysAppInfo != null) {
                        sendMailInfoDto.setAppCode(sysAppInfo.getAppCode());
                        sendMailInfoDto.setAppAddress(sysAppInfo.getAppAddress());
                    }
                    log.info("【websocket消息】 单点消息:" + sendMailInfoDto);
                    session.getBasicRemote().sendObject(sendMailInfoDto);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 服务端推送消息：扫码成功
     *
     * @param userId
     */
    public boolean pushScanQR(String userId) {
        Session session = sessionPool.get(userId);
        SendMailInfoDto sendMailInfoDto = new SendMailInfoDto();
        sendMailInfoDto.setType("check");
        sendMailInfoDto.setValue("success");
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【websocket消息】 扫码成功");
                    session.getBasicRemote().sendObject(sendMailInfoDto);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 服务端推送消息：扫脸结果推送
     *
     * @param userId
     */
    public boolean pushFaceResult(String userId, String code) {
        Session session = sessionPool.get(userId);
        SendMailInfoDto sendMailInfoDto = new SendMailInfoDto();
        sendMailInfoDto.setType("faceResult");
        sendMailInfoDto.setValue(code);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【websocket消息】 扫脸完成");
                    session.getBasicRemote().sendObject(sendMailInfoDto);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 服务端推送消息：签名图片
     *
     * @param userId
     * @param signature
     */
    public void pushSignature(String userId, String signature) {
        Session session = sessionPool.get(userId);
        SendMailInfoDto sendMailInfoDto = new SendMailInfoDto();
        sendMailInfoDto.setType("signature");
        sendMailInfoDto.setValue(signature);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【websocket消息】 签名图片");
                    session.getBasicRemote().sendObject(sendMailInfoDto);
                    session.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkExit(String userId) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            return true;
        }
        return false;
    }
}
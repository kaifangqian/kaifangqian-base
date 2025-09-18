/**
 * [类功能描述：授权服务]
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
package com.kaifangqian.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.common.system.vo.Authorization;
import com.kaifangqian.common.util.RsaUtils;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: zhh
 */
@Component
@Slf4j
public class LicenseBean implements ApplicationRunner {
    public static Authorization grant;
    @Value("${service.token}")
    private String license;
    String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALYW9GOLSx0FXuC5WtR+U9X/N9O/yhqtwoCJM0DOeRK5Tgl+pSAt3mZ7EAi342gXl2dOdlXwXW1ZfL3Zd2al84ACAfGbLIYUzxACn7Y7wv+8DvAnJTBeRfLrZ16aT8O4H7DKWwqbBA04BK1HRLCyxs2F7k/K7vrGTU3yTz9omdkVAgMBAAECgYEAr9uPf6wBiS+sqCCbZmnzEdQA2NJ7oDR8iqL3Cvnd2IV6ppTXaTKjfhoQLDtctyVBphYTF4Ci2n74iGpEdLCFb1FFe1/IorEcLSJ5ahiQidr0Ufc6XzF76yVyIkU5rjgIY8LMg0UUbdTssq0i56mXgUMzJp7pBwrihkc8aG8igcECQQD0VfoPKRyjfyjKx8ldzBCF2woX4dK1brSkIz5LWk6PjHc5JsDy7KFkn4riVPbtwrymQ1r86ZafdqJgZBMoUbS5AkEAvshEwvrJbjXe/R8Zogl3FJ+kv7yMiYftABVxwvNXLEDUKfGuQYeAfxIbqipxMvXEzsq/aFEIqa8FKMOPq22RPQJAJJ9KZsFTwJHLrHE7lmqCw31sSt4XNgiM3NlHegXkJpH4QMG1Q/QB0NI0/+2aQVLh8c3Aso3UfLxMZEQ7ttxgSQJBAIPMYMx+aoerybf+Mzwg49Yoj60x+bjNYWpsZiHy8CcPRkMPxn1YuemPPfNpzLgS13qw0FilmqF22s6Vg3w/flUCQQCTt1zHJDj0WbEDtaVxv0XwlYjXo0JGPRtSq6RUAsznWpHacmNEO1+pI7BBMJv+fX6SaGJVEQ3Q6qrm4jaHxK+M";

    public Authorization getGrant() {
        Authorization grant = null;
        if (MyStringUtils.isBlank(license)) {
            log.info("未配置软件使用授权码，无法启动服务");
            System.exit(0);
        }
        try {
            String rs = RsaUtils.decrypt(license, privateKey);
            grant = JSONObject.parseObject(rs, Authorization.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("软件授权码错误，无法启动服务");
            System.exit(0);
        }

        return grant;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.grant = getGrant();
    }
}

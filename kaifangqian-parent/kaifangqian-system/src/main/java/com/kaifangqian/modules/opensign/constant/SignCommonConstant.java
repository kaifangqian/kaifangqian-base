/**
 * @description 签署业务常量
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
package com.kaifangqian.modules.opensign.constant;

/**
 * @author : zhenghuihan
 * create at:  2024/5/9  14:31
 * @description:
 */
public class SignCommonConstant {
    public static String USERSIGNPASSWORDKEY = "user_sign_password";


    //0,1:签约密码 2：双重认证
    public static String USERSIGNCONFIRMTYPEKEY = "user_sign_confirm_type";

    public static String PERSONALAUTHREGISTER = "personal_auth_register";

    public static String COMPANYAUTHREGISTER = "company_auth_register";

    public static String PERSONALAUTHUPDATE = "personal_auth_update";

    public static String COMPANYAUTHUPDATE = "company_auth_update";

    public static String VERIFY_SIGN_DOCUMENT = "verify_sign_document";

    public static String OPEN_FAST_SIGN = "open_fast_sign";

    public static String OPEN_AUTO_SIGN = "open_auto_sign";

    public static final long TWO_DAY = 2 * 24 * 60 * 60;


    public static  String PUBLICKEYSTRING = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsRlNp8et+XXIA/RnSN+yQTmcc0nhTb91UDVmD5Z1M2rf9VitZV5woJ9Q10Qs5+hmbZeMPzu0jHt44HVfjEs2I9ptpY063njjVzhEvy+8LxSILa1t+90bQeLgA0JC/W8O55yhSIjYNhLplJDCzw1hd13mk/hCEqr6HJAroJ6vlJbLzROmu6tL0bEqGNOf6RyAFskMO5ge+FH3mqhfKlgUjgD8Z35rQK83RHPq43vS3KP7KZRww8jVQ0i+gzvqo0ocd8z70C/wJNkRKj9cRYVUn7K+YQOnmdk4DYiSwfyBkl+ys4FozF/2t5QbiI++bHY8sEBuIsZTM1AbMdb0lD/u9wIDAQAB";
    public static  String PRIVATEKEYSTRING = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCxGU2nx635dcgD9GdI37JBOZxzSeFNv3VQNWYPlnUzat/1WK1lXnCgn1DXRCzn6GZtl4w/O7SMe3jgdV+MSzYj2m2ljTreeONXOES/L7wvFIgtrW373RtB4uADQkL9bw7nnKFIiNg2EumUkMLPDWF3XeaT+EISqvockCugnq+UlsvNE6a7q0vRsSoY05/pHIAWyQw7mB74UfeaqF8qWBSOAPxnfmtArzdEc+rje9Lco/splHDDyNVDSL6DO+qjShx3zPvQL/Ak2REqP1xFhVSfsr5hA6eZ2TgNiJLB/IGSX7KzgWjMX/a3lBuIj75sdjywQG4ixlMzUBsx1vSUP+73AgMBAAECggEADsdayJ6MMR4YE+RoXhd0y6agHKrUp2VLx+a83p4ip6jKibHdvnDUG0NQPraMvMmTCqAz7u7vkS2Yl7qTv6RjHzjSH5eaiwgxiIzEh+PW42LmPlGTtQnthWEGap8z8ew7pS5X8zKgQFrxrRkreJXYRVrLrBtygBZOiuWpDl1Pr6mGNg3oTp3VonF3Kx04J/pDK7wqtq1haF3eEFlQGArrtsTJmIIYYV4HHYxztXTr6+t2IvXr/1MoqKQ8FszREsLUf1leU6Jue0R6dVCLroqRRbatsXomHgHZ/YUc/4qr5zYr0yHgTqYDtD22B9SXBoHK5UDVIfSXDn14fsHboUyMaQKBgQDkgkaxVTp124GEmcylpTMr0dnl4MmKRvb8Wdv2aQdoFr/vmcttyHXpJE4ho04o83DAcPvHUXQWj85Lo4Lo8XpuPDeUyrI013DwOO11jxJBaSwvRecjfkK/qwW8nUKQlheRg9idJm0XWDKwpvcBizjPIkH7TNUihcZR0nbWbwoU1QKBgQDGZ60ofcq+ozxBn/Z88pWImih4XzPfRZZoutwF1JmRMMhbzcKtK4f4ECi5GxNuzFkAeHcCC4fQi3YbqqU2U/aRJ+IXWhY7Gxaxf9qhnjzI9dZliC82QbEm010Jgmg9Al0NOzA9sqpxK+ruM+e2OneL1o+iB4ayzc/cXdBxIYEKmwKBgDZUezZiiznGbO74Gl4v/xCmHma3oXONF+A8BT57DlXDQvNW37VEejRqe2EoEMdPyjkXI+XqCe3rVyNIxzFXaMKY8qYf42sI5kNXZa4TzEVdGjJLLn+T347uvLhxs4fpB8WDUZdbsYhhYfv6VrQ5Q9KL1KBJTkuc/uApaDPnxr/ZAoGBAJqW/ZynJuoTVkfr06rXeRvOwenIhCpbli5UKkuAs4clc3fUgvNbD76BGLniFuol3tM3CcNWKKSdhZFZDRGfqma/pLJ9RwUWvj/fVwLGwKX0pvDFg8PKu7V8yuk16vJefRJqI9Ru+c7kYVDosIqUU8FplNCy/IklQ+h4UIlVia4xAoGAVCEpfH0qktM38pv0FVdtyhJ4371MV18PpV9U51S5V2DV33FG+DuKZw/utIhrFYkcNLno3kynH7H/oLbZ7hTcjFVdHqDuCpgdwphOAIYNhRlwa/fA8aMI4a+VNO9avC1499mJAbGD3PZJ553uE1h6u9m2uk5x176LxKO6VqbHhxU=";
}
/**
 * @description 在线签名验签接口
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
package com.kaifangqian.modules.opensign.service.verify;

import com.kaifangqian.common.vo.Result;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * @Description: 在线签名验签接口
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignVerifyService
 * @author: Fusion
 * CreateTime:  2023/8/120  10:53
 * @copyright 北京资源律动科技有限公司
 */

public interface SignVerifyService {

    Result getImageFromPdf(MultipartFile file) throws IOException;
}

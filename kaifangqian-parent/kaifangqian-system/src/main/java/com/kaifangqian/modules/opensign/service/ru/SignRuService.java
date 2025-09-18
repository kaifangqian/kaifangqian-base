/**
 * @description 签署文档主业务接口类
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
package com.kaifangqian.modules.opensign.service.ru;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuTask;

import java.util.Date;
import java.util.List;

/**
 * @Description: SignRuServoice
 * @Package: com.kaifangqian.modules.opensign.service.ru
 * @ClassName: SignRuServoice
 * @author: FengLai_Gong
 */
public interface SignRuService extends IService<SignRu> {

    Integer countMyByStatus(Integer status, List<Integer> statusList);

    void updateStatus();


    Boolean updateExpireDate(Date expireDate, String id);

    Boolean checkDownloadAuth(String signRuId);

    Boolean checkViewAuth(String signRuId);

    /**
     * @create by zhenghuihan
     * @createTime 2024/2/29 14:18
     * @description 获取当前用户在 指定实例下是否有代办任务：有 返回taskId 没有:返回null
     */
    SignRuTask getMyTask(String signRuId);


    Boolean allTaskComplete(String signRuId);


}
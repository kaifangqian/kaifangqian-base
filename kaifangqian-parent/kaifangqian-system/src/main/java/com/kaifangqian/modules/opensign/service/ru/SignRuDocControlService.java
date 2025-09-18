/**
 * @description 签署文档控件管理接口类
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
import com.kaifangqian.modules.opensign.service.business.vo.ControlQueryVo;
import com.kaifangqian.modules.opensign.entity.SignRuDocControl;

import java.util.List;

/**
 * @Description: SignRuDocControlService
 * @Package: com.kaifangqian.modules.opensign.service.ru
 * @ClassName: SignRuDocControlService
 * @author: FengLai_Gong
 */
public interface SignRuDocControlService extends IService<SignRuDocControl> {


    List<SignRuDocControl> listByParam(ControlQueryVo vo);

    List<SignRuDocControl> listSignControlList(String ruId);

    List<SignRuDocControl> listSignControlBySignerId(String signerId);

    List<SignRuDocControl> listWriteByDocList(List<String> docList);

    Integer countByParam(ControlQueryVo vo);


    void deleteById(String controlId);

    void deleteByParam(List<String> docIdList ,String ruId);

    void deleteSignControlList(String ruId);

    void deleteSignControlList(String signerId,String ruId);

    void resetWriteControlList(String ruId);

    void resetWriteControlList(String signerId,Integer signerType ,String ruId);

}
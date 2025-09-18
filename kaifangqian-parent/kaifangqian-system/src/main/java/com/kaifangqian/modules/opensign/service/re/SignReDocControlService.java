/**
 * @description 业务线签署控件接口服务类
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
package com.kaifangqian.modules.opensign.service.re;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kaifangqian.modules.opensign.service.business.vo.ControlQueryVo;
import com.kaifangqian.modules.opensign.entity.SignReDocControl;

import java.util.List;

/**
 * @Description: SignReDocControlService
 * @Package: com.kaifangqian.modules.opensign.service.re
 * @ClassName: SignReDocControlService
 * @author: FengLai_Gong
 */
public interface SignReDocControlService extends IService<SignReDocControl> {


    List<SignReDocControl> listByParam(ControlQueryVo queryVo);

    List<SignReDocControl> listByParam(String reId);

    List<SignReDocControl> listByParam(String reId,String reDocId);

    List<SignReDocControl> listByReIdAndSignerId(String reId,String signerId);

    List<SignReDocControl> listBySignerId(String signerId);

    void deleteByParam(List<String> docIdList ,String reId);

    void deleteSignControlList(String signerId,String reId);

    void deleteById(String controlId);

    void deleteByReId(String reId);

    void resetWriteControlList(String signerId,Integer signerType ,String reId);


}
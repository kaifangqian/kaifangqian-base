/**
 * @description 企业印章工具类
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
package com.kaifangqian.modules.opensign.service.tool;

import com.kaifangqian.exception.PaasException;
import com.kaifangqian.modules.opensign.seal.CircleSeal;
import com.kaifangqian.modules.opensign.seal.EllipseSeal;
import com.kaifangqian.modules.opensign.seal.SealDTO;
import com.kaifangqian.modules.opensign.seal.StrategySeal;
import org.springframework.stereotype.Service;

/**
 * @Description: SealGenerateService
 * @Package: com.kaifangqian.modules.opensign.service.tool
 * @ClassName: SealGenerateService
 * @author: FengLai_Gong
 */
@Service
public class EntSealGenerateService {



    /**
     * @Description #生成企业签章
     * @Param [topText, middleText]
     * @return byte[]
     **/
    public byte[] generateEntSeal(String topText,String middleText){

        EntSealGenerator seal = new EntSealGenerator();
        seal.setFirm(topText);
        byte[] bytes = seal.export2pic(null, middleText);
        return bytes;
    }

    /**
     * @Description #生成企业圆形签章
     * @Param [topText 上弦文, middleText 横排文, bottomText 下弦文]
     * @return byte[]
     **/
    public String generateEntCircleSeal(String topText,String middleText,String bottomText,Boolean starFlag){
        SealDTO sealDTO = new SealDTO();
        sealDTO.setCompanyName(topText);
        sealDTO.setStarFlag(starFlag);
        sealDTO.setSerNo(bottomText);
        sealDTO.setTitle(middleText);
        sealDTO.setColor(0);

        StrategySeal seal = new StrategySeal(new CircleSeal());
        try {
            String sealBase64 = seal.run(sealDTO);
            return sealBase64;
        } catch (Exception e) {
            throw new PaasException("企业签章生成失败," + e.getMessage());
        }

    }

    /**
     * @Description #生成企业椭圆形签章
     * @Param [topText 上弦文, middleText 横排文, bottomText 下弦文, center 中心文字]
     * @return byte[]
     **/
    public String generateEntEllipseSeal(String topText,String middleText,String bottomText,String center){
        SealDTO sealDTO = new SealDTO();
        sealDTO.setCompanyName(topText);
        sealDTO.setSerNo(bottomText);
        sealDTO.setCenter(center);
        sealDTO.setTitle(middleText);
        sealDTO.setColor(0);
        StrategySeal seal = new StrategySeal(new EllipseSeal());
        try {
            String sealBase64 = seal.run(sealDTO);
            return sealBase64;
        } catch (Exception e) {
            throw new PaasException("企业签章生成失败," + e.getMessage());
        }

    }


    public byte[] clipEntSeal(byte[] originByte ,Integer colorRange){
        byte[] clip = EntSealClipper.clip(originByte,colorRange);
        return clip;
    }


}
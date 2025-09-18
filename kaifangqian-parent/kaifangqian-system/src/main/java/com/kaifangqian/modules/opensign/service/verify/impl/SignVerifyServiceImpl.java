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
package com.kaifangqian.modules.opensign.service.verify.impl;

import com.alibaba.fastjson.JSONObject;
import com.kaifangqian.common.vo.Result;
import com.kaifangqian.config.FileProperties;
import com.kaifangqian.modules.opensign.enums.SignStatus;
import com.kaifangqian.modules.opensign.service.verify.SignVerifyService;
import com.kaifangqian.modules.opensign.util.VerifySign;
import com.kaifangqian.modules.opensign.vo.base.SignPdfInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @Description: 在线签名验签服务实现类
 * @Package: com.kaifangqian.modules.opensign.service.doc.impl
 * @ClassName: SignVerifyServiceImpl
 * @author: Fusion
 * CreateTime:  2023/8/120  10:53
 * @copyright 北京资源律动科技有限公司
 */


@Slf4j
@Service
public class SignVerifyServiceImpl implements SignVerifyService {

    @Autowired
    private FileProperties fileProperties;
    /**
     * 获取pdf签名图片信息
     * @param  file
     * @return 提取结果
     */
    public Result<?> getImageFromPdf(MultipartFile file) throws IOException {
        Result<JSONObject> result = new Result<>();
//        String signFileUrl = fileProperties.getPath().getPath();

        //String signFileUrl = System.getProperty("user.dir") +"/resrun-paas-system/src/main/resources/upload/";    //本地测试路径
//        String signFilePath = signFileUrl + file.getOriginalFilename();
//        File oldFile = new File(signFilePath);
        byte[] bytes = file.getBytes();
        SignPdfInfoVo signPdfInfo = new SignPdfInfoVo();
        try {
//            Path path = Paths.get(signFileUrl + file.getOriginalFilename());
//            Files.write(path, bytes);

            signPdfInfo = VerifySign.getSignFromPdf(bytes);
            signPdfInfo.setPdfName(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/") + 1));
            signPdfInfo.setPdfSize(String.format("%.2f",bytes.length/1024.0));
            switch (signPdfInfo.getPdfSingResult()) {
                case 1:
                    return Result.OK(SignStatus.SIGN_STATUS_NOSIGNATURE.getMsg(), signPdfInfo);
                case 2:
                    return Result.OK(SignStatus.SIGN_STATUS_ERROR.getMsg(), signPdfInfo);
                case 3:
                    return Result.OK(SignStatus.SIGN_STATUS_FIDDLE.getMsg(), signPdfInfo);
                default:
                    return Result.OK(SignStatus.SIGN_STATUS_RIGHT.getMsg(), signPdfInfo);
            }


        }catch (Exception e){
//            signPdfInfo.setPdfName(signFilePath.substring(signFilePath.lastIndexOf("/") + 1));
//            signPdfInfo.setPdfSize(String.valueOf(oldFile.length()));
            signPdfInfo.setPdfName(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("/") + 1));
            signPdfInfo.setPdfSize(String.valueOf(bytes.length/1024));

            signPdfInfo.setPdfSingResult(SignStatus.SIGN_STATUS_NOSIGNATURE.getCode());
//            deleteDirectory(oldFile);
            return Result.OK(SignStatus.SIGN_STATUS_NOSIGNATURE.getMsg(),signPdfInfo);
        }
    }

}

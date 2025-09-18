/**
 * @description PDF文件加密服务
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
package com.kaifangqian.modules.opensign.service.business;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Description: PdfEncryptionService
 * @Package: com.kaifangqian.modules.opensign.service.business
 * @ClassName: PdfEncryptionService
 * @author: FengLai_Gong
 */
@Service
public class PdfEncryptionService {

    @Value("${paas.pdf-encryption-enable}")
    private Boolean pdfEncryptionFlag;
    @Value("${paas.pdf-encryption-password}")
    private String pdfEncryptionPassword;


    /**
     * 文件加密
     * @param pdfFileBytes
     * @throws Exception
     */
    public byte[] pdfToEncrypted(byte[] pdfFileBytes){
        byte[] pdfByte = null ;
        if(pdfEncryptionFlag){
            try{
                //读取PDF文件
                PDDocument doc = Loader.loadPDF(pdfFileBytes) ;
                //判断是否加密过
                if (doc.isEncrypted()) {
                    pdfByte = pdfFileBytes ;
                }else {
                    // 创建访问权限对象
                    AccessPermission ap = new AccessPermission();
                    // 设置权限
                    ap.setCanModify(false); // 不允许修改
                    ap.setCanPrint(true); // 允许打印
                    ap.setCanExtractContent(false);//
                    ap.setReadOnly();// 只读
                    ap.setCanFillInForm(false); // 不允许填写表单
                    // 创建保护策略，ownerPassword设置后，用户另存为其它文件时，需要输入ownerPassword才能执行
                    StandardProtectionPolicy spp = new StandardProtectionPolicy(pdfEncryptionPassword, null, ap);
                    spp.setEncryptionKeyLength(128); // 设置加密密钥长度
                    // 应用保护策略
                    doc.protect(spp);

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    doc.save(out);
                    doc.close();
                    //输出文件字节数组
                    pdfByte = out.toByteArray();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            pdfByte = pdfFileBytes ;
        }

        return pdfByte ;
    }

    /**
     * @Description # 文件解密
     * @Param [pdfFileBytes]
     * @return byte[]
     **/
    public byte[] pdfToDecrypted(byte[] pdfFileBytes) {
        byte[] pdfByte = null ;
        if(pdfEncryptionFlag){
            try{
                //读取PDF文件，并且指定解密密码
                PDDocument doc = Loader.loadPDF(pdfFileBytes,pdfEncryptionPassword) ;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                doc.save(out);
                doc.close();
                //输出文件字节数组
                pdfByte = out.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            pdfByte = pdfFileBytes ;
        }

        return pdfByte ;

    }

    public Boolean isEncryption() {
        return pdfEncryptionFlag;
    }

    public String getPdfEncryptionPassword() {
        return pdfEncryptionPassword;
    }




}
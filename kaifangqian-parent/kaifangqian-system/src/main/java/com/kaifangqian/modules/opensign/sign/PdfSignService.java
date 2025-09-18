/**
 * @description 文件签署服务
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
package com.kaifangqian.modules.opensign.sign;

import com.kaifangqian.modules.opensign.enums.SignTypeEnum;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionArrayData;
import com.kaifangqian.exception.PaasException;
import com.kaifangqian.external.sign.request.AutoSignDocumentRequest;
import com.kaifangqian.external.sign.request.DocumentInfo;
import com.kaifangqian.external.sign.request.VerifySignDocumentRequest;
import com.kaifangqian.external.sign.response.AuthSignDocumentResponse;
import com.kaifangqian.external.sign.response.AutoSignDocumentResponse;
import com.kaifangqian.external.sign.service.SignServiceExternal;
import com.kaifangqian.modules.opensign.entity.SignRu;
import com.kaifangqian.modules.opensign.entity.SignRuDoc;
import com.kaifangqian.modules.opensign.service.business.PdfEncryptionService;
import com.kaifangqian.modules.opensign.service.business.vo.PdfboxSignData;
import com.kaifangqian.modules.opensign.service.business.vo.YundunSignPositionData;
import com.kaifangqian.modules.opensign.service.ru.SignRuDocService;
import com.kaifangqian.modules.opensign.service.tool.pojo.RealPositionProperty;
import com.kaifangqian.modules.opensign.utils.Base64;
import com.kaifangqian.pdfbox.AddExternalSignature;
import com.kaifangqian.pdfbox.AssinaturaPDF2;
import com.kaifangqian.pdfbox.vo.*;
import com.kaifangqian.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: PdfSignService
 * @Package: com.kaifangqian.modules.opensign.pdfbox
 * @ClassName: PdfSignService
 * @author: yxb
 * @Date 2025/5/31
 */
@Slf4j
@Service
public class PdfSignService {

    @Autowired
    private SignServiceExternal signServiceExternal ;

    @Autowired
    private SignRuDocService signRuDocService ;

    @Autowired
    private PdfEncryptionService pdfEncryptionService ;

    public Integer getPdfPage(byte[] pdfByte){
        Integer page = 0 ;

        try {
            PDDocument document = Loader.loadPDF(pdfByte);
            if (document == null) {
                throw new PaasException("pdf文件解析失败");
            }
            page = document.getNumberOfPages();
            document.close();
        } catch (Exception e) {
            throw new PaasException("pdf文件异常");
        }

        return page ;
    }

    /**
     * @Description #签署
     * @Param [pdfFile 文档, signByte 签章, certInfo 证书, positions 位置]
     * @return byte[]
     **/
    public Map<String,byte[]> signWithYundunHash(String contractId, String appName, String appId, Map<String,byte[]> newDocFileByteMap, byte[] entSealByte, String orderNo, String certHolderTenantId, SignRu signRu, List<YundunSignPositionArrayData> yundunSignPositionArrayDatas, String signType){
//        log.info("开始签署了");
        //开始签署
        List<DocumentInfo> documentList = new ArrayList<>();
        VerifySignDocumentRequest verifySignDocumentRequest = null;
        AutoSignDocumentRequest autoSignDocumentRequest = null;

        if(signType.equals(SignTypeEnum.AUTH_SIGN.getCode())){
            verifySignDocumentRequest = new VerifySignDocumentRequest();
            verifySignDocumentRequest.setSeal(Base64.encode(entSealByte));
            verifySignDocumentRequest.setOrderNo(orderNo);
        }else if(signType.equals(SignTypeEnum.AUTO_SIGN.getCode())){
            autoSignDocumentRequest = new AutoSignDocumentRequest();
            autoSignDocumentRequest.setContractNo(signRu.getId());
            autoSignDocumentRequest.setContractName(signRu.getSubject());
            autoSignDocumentRequest.setUnionId(certHolderTenantId);
            autoSignDocumentRequest.setSeal(Base64.encode(entSealByte));
        }

        Map<String,PdfboxSignData> asssinaturePdfMap = new HashMap<String,PdfboxSignData>();
        //List<AssinaturaPDF2> assinaturas = new ArrayList<>();
        AssinaturaModel assinatura = null;

        //遍历每个文件，设置签署位置，执行签署
        for (Map.Entry<String, byte[]> entry : newDocFileByteMap.entrySet()) {
            byte[] newDocFileByte = null;
            String docId = entry.getKey();
            SignRuDoc signRuDoc = signRuDocService.getById(docId);
            String docName = "";
            if(signRuDoc != null && MyStringUtils.isNotBlank(signRuDoc.getDocName())){
                docName = signRuDoc.getDocName();
            }

            byte[] docBytes = entry.getValue();

            //文件加密
            newDocFileByte = pdfEncryptionService.pdfToEncrypted(docBytes);

            //签署所需基础数据
            assinatura = new AssinaturaModel();
            assinatura.setLocation(appName+"："+appId);
            assinatura.setReason("ID:"+contractId+"，依据电子签名法此电子签名与本人的签名/签章具有同等法律效力。");
            //文件
            assinatura.setPdf(newDocFileByte);
            //签章
            assinatura.setSignatureImage(entSealByte);

            List<AssinaturaPosition> realPositions = new ArrayList<>();

            for(int i = 0 ; i < yundunSignPositionArrayDatas.size() ; i++){

                YundunSignPositionArrayData yundunSignPositionArrayData = yundunSignPositionArrayDatas.get(i);

                if(docId.equals(yundunSignPositionArrayData.getDocId())){
                    List<YundunSignPositionData> yundunSignPositionDataList = yundunSignPositionArrayData.getYundunSignPositionDataList();

                    for (YundunSignPositionData yundunSignPositionData : yundunSignPositionDataList){
                        RealPositionProperty realPositionProperty = yundunSignPositionData.getSealPosition();
                        byte[] sealImgByte = yundunSignPositionData.getSealImgByte();

                        AssinaturaPosition position = new AssinaturaPosition();
                        position.setPage(realPositionProperty.getPageNum());
                        position.setOffsetX(realPositionProperty.getStartx() + "");
                        position.setSignWidth((realPositionProperty.getEndx() - realPositionProperty.getStartx()) + "");
                        //纵坐标，pdfbox是从下向上计算的
                        float signHeight = realPositionProperty.getStarty() - realPositionProperty.getEndy();
                        if(signHeight < 0){
                            signHeight = realPositionProperty.getEndy() - realPositionProperty.getStarty() ;
                        }
                        position.setSignHeight(signHeight + "");
                        position.setOffsetY((realPositionProperty.getRealPdfHeight() - realPositionProperty.getStarty() - signHeight) + "");
                        position.setSeal(sealImgByte);
                        position.setFieldName(UUID.randomUUID().toString().replace("-", ""));

                        realPositions.add(position);
                    }
                    assinatura.setPositions(realPositions);
                }
            }
            try {
                AssinaturaPDF2 assinaturaPDF = new AssinaturaPDF2(assinatura, true);

                byte[] signedFile = assinaturaPDF.assina();
                PdfboxSignData pdfboxSignData = new PdfboxSignData();
                pdfboxSignData.setSignedFile(signedFile);
                if (assinaturaPDF.getLateExternalSigning()) {
                    LateExternalSignData signData = assinaturaPDF.getLateExternalSignData();
                    pdfboxSignData.setOffset(signData.getOffset());
                    asssinaturePdfMap.put(docId,pdfboxSignData);

                    // 构建云盾签署请求
                    DocumentInfo documentInfo = new DocumentInfo();
                    documentInfo.setDocumentId(docId);
                    documentInfo.setDocumentName(docName);
                    documentInfo.setDocumentHash(org.apache.pdfbox.util.Hex.getString((signData.getFileHash())));
                    documentList.add(documentInfo);

                }
            }catch (Exception e){
                e.printStackTrace();
                throw new PaasException("签署失败",e);
            }
            if(signType.equals(SignTypeEnum.AUTH_SIGN.getCode())){
                verifySignDocumentRequest.setDocuments(documentList);
            }else if (signType.equals(SignTypeEnum.AUTO_SIGN.getCode())){
                autoSignDocumentRequest.setDocuments(documentList);
            }
        }
        try {
            List<DocumentInfo> yundunDocumentList = null;
            if(signType.equals(SignTypeEnum.AUTH_SIGN.getCode())){
                // 构建云盾意愿校验签署返回数据
                AuthSignDocumentResponse authSignDocumentResponse = null;
                // 返回云盾签署数据
                authSignDocumentResponse = signServiceExternal.submitAuthHashSign(verifySignDocumentRequest);
                yundunDocumentList = authSignDocumentResponse.getDocuments();

            }else if (signType.equals(SignTypeEnum.AUTO_SIGN.getCode())){
                AutoSignDocumentResponse autoSignDocumentResponse = null;
                autoSignDocumentResponse = signServiceExternal.submitAutoHashSign(autoSignDocumentRequest);
                yundunDocumentList = autoSignDocumentResponse.getDocuments();
            }
            if(yundunDocumentList !=null && yundunDocumentList.size() > 0){
                for(DocumentInfo documentInfoTemp : yundunDocumentList){
                    PdfboxSignData pdfboxSignData = asssinaturePdfMap.get(documentInfoTemp.getDocumentId());
                    byte[] newPDF = AddExternalSignature.addSignature(pdfboxSignData.getSignedFile(), pdfboxSignData.getOffset(), Base64.decode(documentInfoTemp.getSignature()));
                    newDocFileByteMap.put(documentInfoTemp.getDocumentId(),newPDF);
                }
            }else{
                throw new PaasException("文件签署失败，未检测到签署文件");
            }
        } catch (Exception e) {
            log.error("签署失败",e);
            throw new PaasException("签署失败",e);
        }
//        log.info("签署完成了");
        return newDocFileByteMap ;
    }
}
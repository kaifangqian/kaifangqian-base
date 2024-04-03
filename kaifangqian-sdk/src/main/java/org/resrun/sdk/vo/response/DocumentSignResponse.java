package org.resrun.sdk.vo.response;

import java.io.Serializable;

/**
 * @Description: DocumentSignResponse
 * @Package: org.resrun.vo.response
 * @ClassName: DocumentSignResponse
 * @author: FengLai_Gong
 */
public class DocumentSignResponse implements Serializable {

    private static final long serialVersionUID = 620669287901843970L;

    //签署后的文件
    private byte [] documentFile ;

    public byte [] getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(byte [] documentFile) {
        this.documentFile = documentFile;
    }
}
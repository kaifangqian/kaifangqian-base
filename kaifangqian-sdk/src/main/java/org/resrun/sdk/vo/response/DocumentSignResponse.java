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
    private String documentFile ;

    public String getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(String documentFile) {
        this.documentFile = documentFile;
    }
}
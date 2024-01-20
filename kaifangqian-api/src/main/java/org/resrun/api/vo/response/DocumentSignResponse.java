package org.resrun.api.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: DocumentSignResponse
 * @Package: org.resrun.vo.response
 * @ClassName: DocumentSignResponse
 * @author: FengLai_Gong
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentSignResponse implements Serializable {

    private static final long serialVersionUID = 620669287901843970L;

    //签署后的文件
    private String documentFile ;

}
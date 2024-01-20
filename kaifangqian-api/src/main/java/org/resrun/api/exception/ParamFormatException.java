package org.resrun.api.exception;

import org.resrun.api.enums.APIResultEnum;
import org.resrun.api.enums.ParamFormatErrorEnum;

/**
 * @Description: PaasException
 * @Package: org.resrun.exception
 * @ClassName: PaasException
 * @author: FengLai_Gong
 */
public class ParamFormatException extends RuntimeException{

//    private static final long serialVersionUID = 1L;

    protected final transient APIResultEnum resultEnum ;

    protected final transient ParamFormatErrorEnum paramFormatErrorEnum ;


    public ParamFormatException(APIResultEnum resultEnum, ParamFormatErrorEnum paramFormatErrorEnum){
        this.resultEnum = resultEnum ;
        this.paramFormatErrorEnum = paramFormatErrorEnum;
    }





}
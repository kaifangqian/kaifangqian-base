package org.resrun.sdk.exception;

import org.resrun.sdk.enums.APIResultEnum;
import org.resrun.sdk.enums.ParamFormatErrorEnum;

/**
 * @Description: PaasException
 * @Package: org.resrun.exception
 * @ClassName: PaasException
 * @author: FengLai_Gong
 */
public class ParamFormatException extends RuntimeException{

    protected final transient APIResultEnum resultEnum ;

    protected final transient ParamFormatErrorEnum paramFormatErrorEnum ;


    public ParamFormatException(APIResultEnum resultEnum, ParamFormatErrorEnum paramFormatErrorEnum){
        this.resultEnum = resultEnum ;
        this.paramFormatErrorEnum = paramFormatErrorEnum;
    }





}
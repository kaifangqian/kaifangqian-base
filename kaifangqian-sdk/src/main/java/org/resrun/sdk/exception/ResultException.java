package org.resrun.sdk.exception;

/**
 * @Description: PaasException
 * @Package: org.resrun.exception
 * @ClassName: PaasException
 * @author: FengLai_Gong
 */
public class ResultException extends RuntimeException{


    protected final transient Integer code ;

    protected final transient String message ;

    protected final transient String uniqueCode ;




    public ResultException(Integer code,String message,String uniqueCode){
        this.code = code ;
        this.message = message ;
        this.uniqueCode = uniqueCode ;
    }





}
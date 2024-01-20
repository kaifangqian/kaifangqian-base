package org.resrun.api.enums;

/**
 * @Description: 业务异常枚举
 * @Package: org.resrun.enums
 * @ClassName: BusinessErrorEnum
 * @author: FengLai_Gong
 */
public enum BusinessErrorEnum {


    CERT_INVALID_ERROR(1,"证书已失效，无法签署"),

    SIGN_FAILED_ERROR(2,"文件签署失败"),
    KEYWORDS_NO_EXISTS_ERROR(3,"签署关键字在文件中不存在，无法签署"),
    DOWNLOAD_FILE_ERROR(4,"文件网络地址下载失败"),
    FILE_PARSE_BASE64_ERROR(5,"文件格式不正确或文件解析失败"),
    FILE_READ_ERROR(5,"文件读取失败"),
    PAGE_NUMBER_ERROR(6,"page页码不合规，规范：0<page<=当前pdf文件的页数"),
    SIGN_WIDTH_GT_PAGE_WIDTH_ERROR(7,"signWidth参数超出范围，该值需要小于pageWidth"),
    SIGN_HEIGHT_GT_PAGE_HEIGHT_ERROR(8,"signHeight参数超出范围，该值需要小于pageHeight"),
    OFFSET_X_GT_PAGE_WIDTH_ERROR(9,"offsetX参数超出范围，该值需要小于pageWidth"),
    OFFSET_Y_GT_PAGE_HEIGHT_ERROR(10,"offsetY参数超出范围，该值需要小于pageHeight"),


    FILE_BASE64_URL_BLANK(11,"签署文件documentFile和documentNetworkURL不能同时为空"),
    CERT_BASE64_URL_BLANK(12,"签名证书pfx和pfxNetworkURL不能同时为空"),
    SIGNATURE_BASE64_URL_BLANK(13,"签章图片signatureFile和signatureNetworkURL不能同时为空"),

    SIGN_POSITION_CALCULATE_ERROR(14,"签署位置计算失败"),

    P10_GENERATE_ERROR(15,"p10文件生成失败"),
    P7B_GENERATE_ERROR(16,"p7b文件生成失败"),
    PFX_GENERATE_ERROR(17,"pfx文件生成失败"),

    CERT_SUBJECT_ERROR(18,"证书主体格式异常"),

    CERT_PARSE_ERROR(19,"证书解析失败，请检查证书和密码"),

    ;

    private Integer code  ;

    private String name ;

    BusinessErrorEnum(Integer code, String name){
        this.code = code ;
        this.name = name;
    }


    public Integer getCode(){
        return this.code;
    }

    public String getName(){
        return this.name ;
    }




}
package com.kaifangqian.common.util;

/** 
 * 文件类型枚取 
 */  
public enum FileType {
    /**
     * jpeg 、 jpg
     */
    JPG("FFD8FF"),  
      
    /** 
     * PNG. 
     */  
    PNG("89504E47"),  
      
    /** 
     * GIF. 
     */  
    GIF("47494638"),  
      

      

    /** 
     * Adobe Acrobat. 
     */  
    PDF("255044462D312E"),  

      
    /** 
     * RAR Archive. 
     */  
    RAR("52617221"),


    /**
     * office类型，包括doc、xls、ppt、vsd
     */
    OFFICE_OLD("D0CF11E0"),

    /**
     * zip docx xlsx pptx vsdx
     */
    OFFICE_NEW("504B0304"),

    ;
      
    private String value ;  
      

    private FileType(String value) {  
        this.value = value;  
    }  
  
    public String getValue() {  
        return value;  
    }  
  
    public void setValue(String value) {  
        this.value = value;  
    }  
} 

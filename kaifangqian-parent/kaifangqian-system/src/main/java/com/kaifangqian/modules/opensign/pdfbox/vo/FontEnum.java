package com.kaifangqian.modules.opensign.pdfbox.vo;

/**
 * @Description: FontEnum
 * @Package: com.kaifangqian.modules.opensign.pdfbox.vo
 * @ClassName: FontEnum
 * @author: FengLai_Gong
 */
public enum FontEnum {
    /**
     * 中易宋体
     */
    SimSun("SimSun","simsun.ttc","宋体","ttc"),
    /**
     * 中易黑体
     */
    SimHei("SimHei","simhei.ttf","黑体","ttf"),
    /**
     * 中易楷体
     */
    SimKai("SimKai","simkai.ttf","楷体","ttf"),
    /**
     * 中易仿宋
     */
    SimFang("SimFang","simfang.ttf","仿宋","ttf"),
    /**
     * Arial
     */
//    Arial("Arial","arial.ttf","Arial","ttf"),

    /**
     * 宋体扩展字体
     */
//    SimSunExt("SimSunExt","simsunb.ttf","宋体扩展字体","ttf"),
    ;

    private String fontKey;
    private String fontName;
    private String name ;

    private String type;
    FontEnum (String fontKey,String fontName){
        this.fontKey = fontKey;
        this.fontName = fontName;
    }

    FontEnum (String fontKey,String fontName,String name,String type){
        this.fontKey = fontKey;
        this.fontName = fontName;
        this.name = name;
        this.type = type;
    }

    public String getFontKey() {
        return fontKey;
    }
    public void setFontKey(String fontKey) {
        this.fontKey = fontKey;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getName(){
        return name ;
    }

    public void  setName(String name){
        this.name = name ;
    }

    public static FontEnum findFont(String fontName){
        for (FontEnum font:FontEnum.values()){
            if(font.getFontKey().equals(fontName)){
                return font;

            }
        }
        return null;
    }

    public String getType() {
        return type;
    }
}
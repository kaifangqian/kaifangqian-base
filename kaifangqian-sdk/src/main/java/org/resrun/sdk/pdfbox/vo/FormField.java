package org.resrun.sdk.pdfbox.vo;

import java.io.Serializable;

/**
 * @Description: SignDocControl
 * @Package: org.resrun.modules.opensign.entity
 * @ClassName: SignDocControl
 * @author: FengLai_Gong
 */
public class FormField implements Serializable {

    private static final long serialVersionUID = 2391149595568705819L;

    private String name ;

    private String offsetX ;

    private String offsetY ;

    private String width ;

    private String height ;

    private Integer page ;

    private Integer isRequired;

    private String fontFamily ;

    private String controlType;

    private String textAlign ;

    private Integer fontSize ;


    private String  interfaceParamName;


    private String value ;


    private String pageWidth ;

    private String pageHeight ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(String offsetX) {
        this.offsetX = offsetX;
    }

    public String getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(String offsetY) {
        this.offsetY = offsetY;
    }

    public String getInterfaceParamName() {
        return interfaceParamName;
    }

    public void setInterfaceParamName(String interfaceParamName) {
        this.interfaceParamName = interfaceParamName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(String pageWidth) {
        this.pageWidth = pageWidth;
    }

    public String getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(String pageHeight) {
        this.pageHeight = pageHeight;
    }
}
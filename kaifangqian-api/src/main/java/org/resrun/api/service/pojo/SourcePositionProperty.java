package org.resrun.api.service.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: 原始文件签署位置属性
 * @Package: org.resrun.modules.sign.service.tool.pojo
 * @ClassName: SourcePositionProperty
 * @author: FengLai_Gong
 */
public class SourcePositionProperty implements Serializable {

    private static final long serialVersionUID = 725976764583634367L;

    @ApiModelProperty("控件X坐标(左上角)")
    private Float offsetX ;
    @ApiModelProperty("控件Y坐标(左上角)")
    private Float offsetY ;
    @ApiModelProperty("控件宽度")
    private Float width ;
    @ApiModelProperty("控件高度")
    private Float height ;
    @ApiModelProperty("当前文件页面宽度")
    private Float pageWidth ;
    @ApiModelProperty("当前文件页面高度")
    private Float pageHeight ;
    @ApiModelProperty("控件所属页码")
    private Integer page ;

    @ApiModelProperty("当前文件页面宽度")
    private Float realWidth ;
    @ApiModelProperty("当前文件页面高度")
    private Float realHeight ;

    @ApiModelProperty("填写值，填写专用")
    private String value ;
    @ApiModelProperty("对齐方式")
    private String align;
    @ApiModelProperty("字体")
    private String fontFamily ;
    @ApiModelProperty("文字大小")
    private Integer fontSize ;


    public Float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Float offsetX) {
        this.offsetX = offsetX;
    }

    public Float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Float offsetY) {
        this.offsetY = offsetY;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Float getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Float pageHeight) {
        this.pageHeight = pageHeight;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Float getRealWidth() {
        return realWidth;
    }

    public void setRealWidth(Float realWidth) {
        this.realWidth = realWidth;
    }

    public Float getRealHeight() {
        return realHeight;
    }

    public void setRealHeight(Float realHeight) {
        this.realHeight = realHeight;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }
}
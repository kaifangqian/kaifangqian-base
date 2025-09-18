package com.kaifangqian.modules.opensign.pdfbox.vo;

/**
 * @Description: Widget
 * @Package: com.kaifangqian.modules.opensign.pdfbox.vo
 * @ClassName: Widget
 * @author: FengLai_Gong
 */
public class Widget {

    private String offsetX ;

    private String offsetY ;

    private Integer width = 32;

    private Integer height = 32;

    private String widgetName;

    private Boolean widgetValue;


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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public Boolean getWidgetValue() {
        return widgetValue;
    }

    public void setWidgetValue(Boolean widgetValue) {
        this.widgetValue = widgetValue;
    }


    public Widget() {
    }

    public Widget(String offsetX, String offsetY, String widgetName, Boolean widgetValue) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.widgetName = widgetName;
        this.widgetValue = widgetValue;
    }

    public Widget( String widgetName, Boolean widgetValue) {
        this.widgetName = widgetName;
        this.widgetValue = widgetValue;
    }


}